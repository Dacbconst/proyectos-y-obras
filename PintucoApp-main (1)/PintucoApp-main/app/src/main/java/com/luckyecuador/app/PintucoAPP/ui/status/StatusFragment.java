package com.luckyecuador.app.PintucoAPP.ui.status;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.AlertInformationRetention;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StatusFragment extends Fragment implements View.OnClickListener {

    AlertDialog alert_informativo;
    private String status_url = "";
    private WebView wvMedidor;
    private SwipeRefreshLayout refreshLayout;

    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, canal, pref_pdv;

    final String[] lblLatitud = {"Latitud"};
    final String[] lblLongitud = {"Longitud"};

    String latitude, longitude;

    private Button btnEntradaJornada;
    private Button btnSalidaJornada;



    String ACTION_FILTER = "versiones.luckyec.com.cronometro";
    String GET_INFORMATIVO_PDV = "https://webecuador.azurewebsites.net/App/XploraEcuador/informativo_pdv_alicorp/vista_por_pdv.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status, container, false);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        pref_pdv = sharedPreferences.getString(Constantes.PDV,Constantes.NODATA);
        Log.i("ENTRO AL STATUS", "SIII");
        LoadData();
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        wvMedidor = (WebView) rootView.findViewById(R.id.wvStatus);
        btnEntradaJornada = (Button) rootView.findViewById(R.id.btnEntradaJornada);
        btnSalidaJornada = (Button) rootView.findViewById(R.id.btnSalidaJornada);

        wvMedidor.getSettings().setJavaScriptEnabled(true);
        wvMedidor.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        Log.i("INFO", codigo_pdv + " - " + punto_venta + " - " + user);

        status_url = "https://webecuador.azurewebsites.net/App/CtaPintuco/MedidorGestion/index.php" +
                "?codigo_pdv=" + codigo_pdv +
                "&nombre_pdv=" + punto_venta +
                "&plataforma=FOODS" +
                "&usuario=" + user;
        Log.i("URL STATUS", status_url);
        wvMedidor.loadUrl(status_url);

        btnEntradaJornada.setOnClickListener(this);
        btnSalidaJornada.setOnClickListener(this);
/*
        if (canal.equalsIgnoreCase("AUTOSERVICIO")){
            mostrar_alert(pref_pdv);
        }
*/



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        } else {
            locationStart();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false); wvMedidor.reload();
                    }
                },2000);
            }
        });

        refreshLayout.setColorSchemeColors(
            getResources().getColor(android.R.color.holo_blue_dark),
            getResources().getColor(android.R.color.holo_orange_dark),
            getResources().getColor(android.R.color.holo_green_dark),
            getResources().getColor(android.R.color.holo_red_dark)
        );

        // inst para info retenida
        new AlertInformationRetention().checkStorage(requireContext());

        return rootView;
    }

    public void mostrar_alert( String pref_pdv){

//        int orientation =  getActivity().getResources().getConfiguration().orientation;
        Log.i("ENTRO MOSTRAR ALERT","HOLAAA");
//        if(orientation == 1){
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_informativo, null);
        builder.setIcon(android.R.drawable.ic_menu_set_as);

        WebView wv = (WebView) dialogView.findViewById(R.id.wvInformativo);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());

        if (VerificarNet.hayConexion(getContext())) {
            wv.loadUrl(GET_INFORMATIVO_PDV +"?pdv=" + pref_pdv);
        } else {
            wv.loadUrl("file:///android_asset/html/pagina-error.html");
        }

        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
             getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        alert_informativo = builder.create();
        alert_informativo.show();

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if(dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
        }
    }

    public Dialog getDialog(){
        Dialog alert = alert_informativo;
        return alert;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
    }

    public void alertDialog(final String tipo_registro) {
        Log.i("ENTRO AL ALERTDIALOG", "HOLAAA2");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_causales, null);
        //Title
        //builder.setIcon(android.R.drawable.ic_menu_set_as);
        //builder.setTitle(tipo_registro.toUpperCase());
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_causales,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);
        
        tittle.setText(tipo_registro.toUpperCase());

        builder.setPositiveButton(R.string.save,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getContext(),"No selecccionó una causal",Toast.LENGTH_LONG).show();
                        }else{
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                            String causal = radioButton.getText().toString();
                            Log.i("RB",radioButton.getText().toString());
                            if (insertDataRegistro(tipo_registro, lblLatitud, lblLongitud, causal)){
//                                mostrar_alert(pref_pdv);
                            }
                        }
                    }
                }
        );

        builder.setNeutralButton(R.string.cancel,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setPadding(4,2,4,2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        cButton.setPadding(4,2,4,2);
    }
    
    public boolean insertDataRegistro(String TipoRegistro, String[] lblLatitud, String[] lblLongitud, String causal) {
        try {
            Toast.makeText(getContext(), "Registrando "+ TipoRegistro + " " + lblLatitud[0].toString() + " " + lblLongitud[0].toString(), Toast.LENGTH_LONG).show();
            if (!lblLatitud[0].toString().equals("") && !lblLongitud[0].toString().equals("") &&
                !lblLatitud[0].toString().equals("") && !lblLongitud[0].toString().equals("") &&
                !lblLatitud[0].toString().equals("Latitud") && !lblLongitud[0].toString().equals("Longitud")) {
                latitude = lblLatitud[0].toString();
                longitude = lblLongitud[0].toString();

                Log.i("Latitud", latitude);
                Log.i("Longitud", longitude);

                String registro = TipoRegistro;

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                //String causalFinal = causal.substring(3);

                new GuardarLog(getContext()).saveLog(user, codigo_pdv, registro.toUpperCase() + " PDV");

                ContentValues values = new ContentValues();

                values.put(ContractInsertGps.Columnas.IDPDV, codigo_pdv);
                values.put(ContractInsertGps.Columnas.USUARIO, user);
                values.put(ContractInsertGps.Columnas.TIPO, registro.toUpperCase());
                values.put(ContractInsertGps.Columnas.VERSION, getString(R.string.version));
                values.put(ContractInsertGps.Columnas.LATITUDE, latitude);
                values.put(ContractInsertGps.Columnas.LONGITUDE, longitude);
                values.put(ContractInsertGps.Columnas.FECHA, fechaser);
                values.put(ContractInsertGps.Columnas.HORA, horaser);
                values.put(ContractInsertGps.Columnas.CAUSAL, causal);
                values.put(Constantes.PENDIENTE_INSERCION, 1);
                getContext().getContentResolver().insert(ContractInsertGps.CONTENT_URI, values);

                Toast.makeText(getContext(), "Registrada su "+registro, Toast.LENGTH_SHORT).show();

                if (VerificarNet.hayConexion(getContext())) {
                    SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertGps, null);
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                return true;

                //finish();
            } else {
                Toast.makeText(getContext(), "Genere las Coordenadas", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == btnEntradaJornada) {
            alertDialog("Entrada");
        }
        if (view == btnSalidaJornada) {
            alertDialog("Salida");
        }
    }

    private void locationStart() {
        Log.i("ENTRA", "locationStart");
        double lat = -2.172652;
        double lng = -79.910018;
        float rad = 1000;
        getContext().registerReceiver(new ProximityReciever(), new IntentFilter(ACTION_FILTER), Context.RECEIVER_NOT_EXPORTED);
        LocationManager mlocManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        if (networkEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        }
        if (gpsEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        }
        //Setting up My Broadcast Intent
        Intent i = new Intent(ACTION_FILTER);
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), -1, i, PendingIntent.FLAG_IMMUTABLE);
        //setting up proximituMethod
//        mlocManager.addProximityAlert(lat, lng, rad, -1, pi);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    /**
     * Aqui empieza la Clase Localizacion
     */
    public class Localizacion implements LocationListener {

        private String fecha,hora,name;

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            if (!lblLatitud[0].equals(loc.getLatitude())&&!lblLongitud[0].equals(loc.getLongitude())) {
                lblLatitud[0] = ""+loc.getLatitude();
                lblLongitud[0] = ""+loc.getLongitude();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
            LoadName();
            obtenerFecha();
//            String name = "Ana Pinzon";
            ContentValues values = new ContentValues();
            values.put(ContractInsertRastreo.Columnas.USUARIO, name);
            values.put(ContractInsertRastreo.Columnas.LATITUD, "GPS DESACTIVADO");
            values.put(ContractInsertRastreo.Columnas.LONGITUD, "GPS DESACTIVADO");
            values.put(ContractInsertRastreo.Columnas.FECHA, fecha);
            values.put(ContractInsertRastreo.Columnas.HORA, hora);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContext().getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        public void LoadName() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            name = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        }

        public void obtenerFecha() {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            fecha = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            hora = hour.format(currentLocalTime);
        }
    }

    public class ProximityReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = LocationManager.KEY_PROXIMITY_ENTERING;

            Boolean entering = intent.getBooleanExtra(key, false);

//            if (entering) {
//                Toast.makeText(context, "LocationReminderReceiver entering", Toast.LENGTH_SHORT).show();
//                Log.i("ReceptorProximidad", "entering");
//                btnEntradaJornada.setEnabled(true);
//                btnSalidaJornada.setEnabled(true);
//            } else {
//                Toast.makeText(context, "LocationReminderReceiver exiting", Toast.LENGTH_SHORT).show();
//                Log.i("ReceptorProximidad", "exiting");
//                btnEntradaJornada.setEnabled(false);
//                btnSalidaJornada.setEnabled(false);
//            }
        }
    }
}