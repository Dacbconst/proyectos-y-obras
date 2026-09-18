package com.luckyecuador.app.PintucoAPP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioPrioritario;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ListViewAdapterExh dataAdapter;
    ListViewAdapterPop dataAdapterpop;
    ArrayList<BasePortafolioPrioritario> selectedItems = new ArrayList<BasePortafolioPrioritario>();
    Chronometer crono;

    ProgressDialog progressDialog;
    AlertDialog alert_marcacion;

    DatabaseHelper handler;
    private boolean interno;

    float radius = 25f;
    String ACTION_FILTER = "versiones.luckyec.com.cronometro";

    private ImageButton btnCodificadoOSA;
    private ImageButton btnInventarios;
    private ImageButton btnPrecios;
    private ImageButton btnShareofShare;
    private ImageButton btnArriendos;
    private ImageButton btnExhibiciones;
    private ImageButton btnCompetencia;
    private ImageButton btnImpulso;
    private ImageButton btnCaducados;
    private ImageButton btnPacks;
    private ImageButton btnLogros;
    private ImageButton btnTareas;
    private ImageButton btnRotacion;
    private ImageButton btnSugeridos;
    private ImageButton btnMCI;
    private ImageButton btnEjecucionMateriales;
    /*private ImageButton btnVenta;
    private ImageButton btnFotografico;*/

    private Button btnEntradaJornada;
    private Button btnSalidaJornada;

    List<String> listSku;

    ListView listViewPrioritario;
    ListView listViewPop;


    private LinearLayout layout1;
    private LinearLayout layout2;

    final String[] lblLatitud = {"Latitud"};
    final String[] lblLongitud = {"Longitud"};


    String latitude, longitude;

    private TextView lDistancia;
    private EditText lPDV;
    private EditText lPDVNombre;

    private TextView txtPrecio, txtExhi, txtGps, txtFloor, txtPromo, txtPrecios, txtImple, txtNotf, txtShare, txtAgo;

    private String tipo;
    private String tiempo;

    private String hours;

    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, canal, device_id, device_name, perimetro_pdv;

    private TextView tvPrioritario;
    private TextView tvPop;

    BasePharmaValue pdv = new BasePharmaValue();

    private double latitud_pdv, longitud_pdv, distance_pdv;
    private double latitud = 0, longitud = 0, distance;
    private int contador = 0;
    private String causal_fuera_pdv = "";
    private boolean salida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setToolbar();
        LoadData();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            interno = extras.getBoolean(Constantes.INTERNO, false);
        }
        Log.i("Preferencias", "onCreate: "+user+"-"+tipo);

        //startService(new Intent(getApplicationContext(), MyService.class));

        //crono = (Chronometer) findViewById(R.id.chronometer);
        /*Button btn_fin = (Button) findViewById(R.id.btn_fin);
        btn_fin.setEnabled(false);*/

        lPDV = (EditText) findViewById(R.id.lPDV);
        lPDVNombre = (EditText) findViewById(R.id.lPDVNombre);
     //   lDistancia = (TextView) findViewById(R.id.lDistanciaPDV);

        btnCodificadoOSA = (ImageButton) findViewById(R.id.ibCodificadosOSA);
        btnInventarios = (ImageButton) findViewById(R.id.ibInventarios);
        btnPrecios = (ImageButton) findViewById(R.id.ibPrecios);
        btnShareofShare = (ImageButton) findViewById(R.id.ibShareOfShelf);
       btnExhibiciones = (ImageButton) findViewById(R.id.ibExhibicionesAdicionales);
        btnCompetencia = (ImageButton) findViewById(R.id.ibCompetencia);
        btnImpulso = (ImageButton) findViewById(R.id.ibImpulso);
        btnCaducados = (ImageButton) findViewById(R.id.ibCaducados);
        btnPacks = (ImageButton) findViewById(R.id.ibOnPacks);
        btnLogros = (ImageButton) findViewById(R.id.ibLogros);
        btnTareas = (ImageButton) findViewById(R.id.ibTareas);
        btnRotacion = (ImageButton) findViewById(R.id.ibRotacion);
        btnSugeridos = (ImageButton) findViewById(R.id.ibSugeridos);
        btnMCI = (ImageButton) findViewById(R.id.ibMCI);
        btnEjecucionMateriales = (ImageButton) findViewById(R.id.ibEjecucionMateriales);

        //btnVenta = (ImageButton) findViewById(R.id.ibVenta);
        //btnFotografico = (ImageButton) findViewById(R.id.ibFotografico);


        btnEntradaJornada = (Button) findViewById(R.id.btnEntradaJornada);
        btnSalidaJornada = (Button) findViewById(R.id.btnSalidaJornada);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);

        lPDV.setText(codigo_pdv.toUpperCase());
        lPDVNombre.setText(punto_venta.toUpperCase());
        /*
        txtPrecio=(TextView) findViewById(R.id.textView8);
        txtExhi=(TextView) findViewById(R.id.textView9);
        txtGps=(TextView) findViewById(R.id.textView10);
        txtFloor=(TextView) findViewById(R.id.textView11);
        txtPromo=(TextView) findViewById(R.id.textViewnew);
        txtPrecios=(TextView) findViewById(R.id.textViewnew2);
        txtImple=(TextView) findViewById(R.id.textViewnew3);
        txtNotf=(TextView) findViewById(R.id.textViewnew4);
        txtShare=(TextView) findViewById(R.id.textViewnew5);
        txtAgo=(TextView) findViewById(R.id.textViewnew6);
        */

        btnCodificadoOSA.setOnClickListener(this);
        btnInventarios.setOnClickListener(this);
        btnShareofShare.setOnClickListener(this);
        //btnArriendos.setOnClickListener(this);
        btnExhibiciones.setOnClickListener(this);
        btnPrecios.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);
        btnImpulso.setOnClickListener(this);
        btnCaducados.setOnClickListener(this);
        btnPacks.setOnClickListener(this);
        btnLogros.setOnClickListener(this);
        btnTareas.setOnClickListener(this);
        btnRotacion.setOnClickListener(this);
        btnSugeridos.setOnClickListener(this);
        btnMCI.setOnClickListener(this);
        btnEjecucionMateriales.setOnClickListener(this);
        /*btnVenta.setOnClickListener(this);
        btnFotografico.setOnClickListener(this);*/

        btnEntradaJornada.setOnClickListener(this);
        btnSalidaJornada.setOnClickListener(this);

        btnCodificadoOSA.setEnabled(true);
        btnInventarios.setEnabled(true);
        btnShareofShare.setEnabled(true);
        btnExhibiciones.setEnabled(true);
        btnPrecios.setEnabled(true);
        btnCompetencia.setEnabled(true);
        btnImpulso.setEnabled(true);
        btnLogros.setEnabled(true);
        btnTareas.setEnabled(true);
        btnRotacion.setEnabled(true);
        btnSugeridos.setEnabled(true);
        btnMCI.setEnabled(true);
        btnEjecucionMateriales.setEnabled(true);
        //btnCaducados.setEnabled(true);
        //btnPacks.setEnabled(true);
        /*btnVenta.setEnabled(true);
        btnFotografico.setEnabled(true);*/

        if (tipo.equalsIgnoreCase("MAYORISTA")) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            btnCodificadoOSA.setVisibility(View.VISIBLE);
            btnPrecios.setVisibility(View.VISIBLE);
            btnShareofShare.setVisibility(View.VISIBLE);
            btnCompetencia.setVisibility(View.VISIBLE);
            btnExhibiciones.setVisibility(View.VISIBLE);
            btnLogros.setVisibility(View.VISIBLE);
            btnTareas.setVisibility(View.VISIBLE);
            btnRotacion.setVisibility(View.VISIBLE);
            btnMCI.setVisibility(View.VISIBLE);
            btnEjecucionMateriales.setVisibility(View.VISIBLE);

            btnInventarios.setVisibility(View.VISIBLE);
            btnImpulso.setVisibility(View.INVISIBLE);

            //btnCaducados.setVisibility(View.VISIBLE);
            //btnPacks.setVisibility(View.VISIBLE);
        }

        if (tipo.equalsIgnoreCase("AUTOSERVICIO")) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            btnCodificadoOSA.setVisibility(View.VISIBLE);
            btnPrecios.setVisibility(View.VISIBLE);
            btnShareofShare.setVisibility(View.VISIBLE);
            btnCompetencia.setVisibility(View.VISIBLE);
            btnExhibiciones.setVisibility(View.VISIBLE);
            btnLogros.setVisibility(View.VISIBLE);
            btnTareas.setVisibility(View.VISIBLE);
            btnRotacion.setVisibility(View.VISIBLE);
            btnMCI.setVisibility(View.VISIBLE);
            btnEjecucionMateriales.setVisibility(View.VISIBLE);

            btnInventarios.setVisibility(View.INVISIBLE);
            btnImpulso.setVisibility(View.INVISIBLE);

            //btnCaducados.setVisibility(View.VISIBLE);
            //btnPacks.setVisibility(View.VISIBLE);
        }

        if (user.toUpperCase().contains("IMPULSO")) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            btnShareofShare.setVisibility(View.GONE);
            btnExhibiciones.setVisibility(View.GONE);
            btnInventarios.setVisibility(View.GONE);
            btnPrecios.setVisibility(View.GONE);
            btnCodificadoOSA.setVisibility(View.GONE);
            btnCompetencia.setVisibility(View.GONE);
            btnImpulso.setVisibility(View.VISIBLE);
            btnCaducados.setVisibility(View.GONE);
            btnPacks.setVisibility(View.GONE);
            btnLogros.setVisibility(View.GONE);
            btnTareas.setVisibility(View.VISIBLE);
            btnRotacion.setVisibility(View.GONE);
            btnMCI.setVisibility(View.VISIBLE);
            btnEjecucionMateriales.setVisibility(View.VISIBLE);
        }
        /*if ( user.equalsIgnoreCase("IMPULSO GYE") ||
            user.equalsIgnoreCase("IMPULSO CARAVANA") ||
            user.equalsIgnoreCase("IMPULSO GUAYAS") ||
            user.equalsIgnoreCase("IMPULSO MANABI") ||
            user.equalsIgnoreCase("IMPULSO QUITO")) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            btnShareofShare.setVisibility(View.GONE);
            btnExhibiciones.setVisibility(View.GONE);
            btnInventarios.setVisibility(View.GONE);
            btnPrecios.setVisibility(View.GONE);
            btnCodificadoOSA.setVisibility(View.GONE);
            btnCompetencia.setVisibility(View.GONE);
            btnImpulso.setVisibility(View.VISIBLE);
            btnCaducados.setVisibility(View.GONE);
            btnPacks.setVisibility(View.GONE);
            btnLogros.setVisibility(View.GONE);
        }*/
        /*btnVenta.setVisibility(View.VISIBLE);
        btnFotografico.setVisibility(View.VISIBLE);*/

/*
        txtGps.setVisibility(View.VISIBLE);
        txtNotf.setVisibility(View.VISIBLE);
        txtPromo.setVisibility(View.VISIBLE);
        txtExhi.setVisibility(View.VISIBLE);
        txtPrecios.setVisibility(View.VISIBLE);
        txtFloor.setVisibility(View.VISIBLE);
        txtPrecio.setVisibility(View.VISIBLE);
        txtShare.setVisibility(View.VISIBLE);
        txtAgo.setVisibility(View.VISIBLE);
*/
        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        } else {
//            locationStart();
//            startService(new Intent(getApplicationContext(), MyService.class));
            //Si los permisos estan otorgados, llamar al evento onClick del boton
            //clickButton();
        }

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!isMyServiceRunning(LocationService.class)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(getApplicationContext(), LocationService.class));
            } else {
                startForegroundService(new Intent(getApplicationContext(), LocationService.class));
            }
        }

        mostrarPopup("Información");
    }


    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(MenuActivity.this);
//        new UniqueDevice().modalUniqueDevice(MenuActivity.this, user);
        registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_GPS_UPDATE));
//        if (interno) {
//            validadorPDV();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
//            Log.i("ENTRA", "BROADCAST RECEIVER");
            if(intent.hasExtra(Constantes.LATITUD)){
//                Log.i("ENTRA BROADCAST RECEIVER", "SI HAY DATA");
                latitud = intent.getDoubleExtra(Constantes.LATITUD, 0);
                longitud = intent.getDoubleExtra(Constantes.LONGITUD, 0);
//                Log.i("LATITUDE MENU", latitud + "");
//                Log.i("LONGITUDE MENU", longitud + "");

                MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
                MarkerOptions place2 = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Posicion PDV");

                distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
                distance = (int)Math.round(distance);

                Log.i("MENU DISTANCE", distance + "");

                if (distance >= distance_pdv) {
                    contador = 0;
                    causal_fuera_pdv = " - SALIDA FUERA DEL PERÍMETRO A " + distance + " MTS. DEL PDV";
                    lDistancia.setText("Usted se encuentra fuera del rango del PDV, a una distancia de: " + distance + " mts.");
                    lDistancia.setVisibility(View.VISIBLE);
                    if (alert_marcacion!=null && alert_marcacion.isShowing() && !salida) {
                        alert_marcacion.dismiss();
                    }
                } else {
                    causal_fuera_pdv = "";
                    lDistancia.setVisibility(View.GONE);
                    if (contador == 0) {
                        entrada();
                    }
                    contador++;
                }
                try {
                    Thread.sleep(2000);
                    if (progressDialog!=null && progressDialog.isShowing()) {
                        Log.i("PROGRESS_DIALOG", "CIERRE");
                        progressDialog.dismiss();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("MENU BROADCASTRECEIVER", "NO HAY DATA");
            }
        }
    };
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void entrada() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(currentLocalTime);

        boolean tiene_entrada = handler.tieneEntrada(codigo_pdv, user, fecha);
        if (!tiene_entrada && interno) {
            alertDialog("Entrada");
        }
    }

    public void validadorPDV() {
        progressDialog = new ProgressDialog(MenuActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setTitle("Distancia PDV");
        progressDialog.setMessage("Validando distancia al PDV");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public void alertDialog(final String tipo_registro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_causales, null);
        //Title
        //builder.setIcon(android.R.drawable.ic_menu_set_as);
        //builder.setTitle(tipo_registro.toUpperCase());
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_causales,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);
        listViewPrioritario = (ListView) dialogView.findViewById(R.id.listView);



        tittle.setText(tipo_registro.toUpperCase());

        builder.setPositiveButton(R.string.save,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getApplicationContext(),"No selecccionó una causal",Toast.LENGTH_LONG).show();
                        }else{
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                            String causal = radioButton.getText().toString();
                            Log.i("RB",radioButton.getText().toString());
                            insertDataRegistro(tipo_registro, lblLatitud, lblLongitud, causal);
                        }
                    }
                }
        );

        builder.setNeutralButton(R.string.cancel,null);

        //builder.setNegativeButton(R.string.cancel,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        ////pButton.setTextColor(Color.rgb(79,195,247));
        //pButton.setBackgroundColor(Color.rgb(79,195,247));
        pButton.setPadding(4,2,4,2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        ////cButton.setTextColor(Color.rgb(79,195,247));
        //cButton.setBackgroundColor(Color.rgb(79,195,247));
        cButton.setPadding(4,2,4,2);
    }

/*    public void mostrarPopup() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.action_popup))
                .setMessage(getString(R.string.user) + ": " + user + "\n")
                .setPositiveButton("ACEPTAR", null)

                .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_popup, null);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_causales,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);

        tittle.setText(R.string.action_popup);

        }*/

    public void mostrarPopup(final String informacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_popup, null);
        //Title
        //builder.setIcon(android.R.drawable.ic_menu_set_as);
        //builder.setTitle(tipo_registro.toUpperCase());
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_popup,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);

        tittle.setText(informacion.toUpperCase());

        TextView lblSegmentacion=(TextView)dialogView.findViewById(R.id.lblSegmentacion);
        TextView lblCompras=(TextView)dialogView.findViewById(R.id.lblCompras);
        listViewPrioritario = (ListView) dialogView.findViewById(R.id.listView);
        listViewPop = (ListView) dialogView.findViewById(R.id.listViewPop);
        tvPrioritario = (TextView) dialogView.findViewById(R.id.data_empty);
        tvPop = (TextView) dialogView.findViewById(R.id.data_empty2);

        showListView();
        showListViewPop();

        String Segmentacion = handler.getSegmentacionMenu();
        String Compras = handler.getComprasMenu();

        lblSegmentacion.setText(Segmentacion);
        lblCompras.setText(Compras);

        listViewPrioritario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ENTRA", "ENTRA");
                String selectedItemSku = ((TextView)view.findViewById(R.id.lblSku)).getText().toString();
               // String selectedItemNumExh = ((EditText)view.findViewById(R.id.lblNumExh)).getText().toString();

                BasePortafolioPrioritario listPort = new BasePortafolioPrioritario();
                listPort.setSku(selectedItemSku);
               // listSku.setNumExh(selectedItemNumExh);

                for (int j = 0; j < selectedItems.size(); j++) {
                    if (selectedItems.get(j).getSku().contains(selectedItemSku)) {
                        selectedItems.remove(j);
                      //  ((CheckBox) view.findViewById(R.id.respuesta)).setChecked(false);
                    }else{
                       // selectedItems.add(listSku);
                      //  ((CheckBox) view.findViewById(R.id.respuesta)).setChecked(true);
                    }
                }
            }
        });



     /*   builder.setPositiveButton(R.string.save,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getApplicationContext(),"No selecccionó una causal",Toast.LENGTH_LONG).show();
                        }else{
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                            String causal = radioButton.getText().toString();
                            Log.i("RB",radioButton.getText().toString());
                            insertDataRegistro(informacion, lblLatitud, lblLongitud, causal);
                        }
                    }
                }
        );*/

        builder.setPositiveButton(R.string.next,null);
        builder.setNeutralButton(R.string.cancel,null);

        //builder.setNegativeButton(R.string.cancel,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        ////pButton.setTextColor(Color.rgb(79,195,247));
        //pButton.setBackgroundColor(Color.rgb(79,195,247));
        pButton.setPadding(4,2,4,2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        ////cButton.setTextColor(Color.rgb(79,195,247));
        //cButton.setBackgroundColor(Color.rgb(79,195,247));
        cButton.setPadding(4,2,4,2);
    }

    public void showListView() {
        List<String> lista_prioritario = handler.filtrarListPortafolioPrioritario(canal, codigo_pdv);
        if (lista_prioritario.size()>0) {
            dataAdapter = new ListViewAdapterExh(this, lista_prioritario);
            listViewPrioritario.setAdapter(dataAdapter);
        } else {
            tvPrioritario.setVisibility(View.VISIBLE);
            listViewPrioritario.setVisibility(View.GONE);
        }
    }

    public void showListViewPop() {
        List<String> listPop = handler.filtrarListPop(canal, codigo_pdv);
        if (listPop.size()>0) {
            dataAdapterpop = new ListViewAdapterPop(this,listPop);
            listViewPop.setAdapter(dataAdapterpop);
        } else {
            tvPop.setVisibility(View.VISIBLE);
            listViewPop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Salir PDV");
        builder.setMessage("¿Desea salir PDV?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MenuActivity.this,PuntosListActivity.class);
                startActivity(intent);
                //Stop the activity
                finish();
            }
        });

        builder.setNeutralButton("NO",null);

        AlertDialog ad = builder.create();
        ad.show();
        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        //pButton.setTextColor(Color.rgb(79, 195, 247));
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        //cButton.setTextColor(Color.rgb(79, 195, 247));
    }

    /**
     * ESTE METODO INICIALIZA EL CRONOMETRO
     * @param view
     */
    /*public void iniciar_cronometro(View view) {
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        Button btn_inicio = (Button) findViewById(R.id.btn_inicio);
        btn_inicio.setEnabled(false);
        Button btn_fin = (Button) findViewById(R.id.btn_fin);
        btn_fin.setEnabled(true);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat hour = new SimpleDateFormat("HH:mm:ss.SSS");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        this.hours = hour.format(currentLocalTime);
    }*/

    /**
     * ESTE METODO DETIENE EL CRONOMETRO
     * @param view
     */
    /*public void detener_cronometro(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Detener Cronometro");
        builder.setMessage("¿Desea detener el cronometro?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Detener cronometro
                crono.stop();
                Button btn_inicio = (Button) findViewById(R.id.btn_inicio);
                btn_inicio.setEnabled(true);
                Button btn_fin = (Button) findViewById(R.id.btn_fin);
                btn_fin.setEnabled(false);
                showElapsedTime();
                insertDataTiempoGestion(MenuActivity.this.hours+"",MenuActivity.this.tiempo);
            }
        });

        builder.setNeutralButton("NO",null);

        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        //pButton.setTextColor(Color.rgb(79, 195, 247));
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        //cButton.setTextColor(Color.rgb(79, 195, 247));
    }*/

    /**
     * METODO PARA INDICAR EL TIEMPO RECORRIDO
     */
    public void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase();
        this.tiempo = String.format("%02d min, %02d sec, %02d mil",
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis),
                TimeUnit.MILLISECONDS.toSeconds(elapsedMillis),
                TimeUnit.MILLISECONDS.toMillis(elapsedMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis))
        );
        Toast.makeText(this, "Su tiempo fue: " + tiempo, Toast.LENGTH_LONG).show();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        tipo=sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
    }

    @Override
    public void onClick(View view) {
        if (view== btnCodificadoOSA) {
            Intent intent=new Intent(MenuActivity.this, ValoresActivity.class);
            startActivity(intent);
        }
        if (view== btnInventarios) {
            Intent intent= new Intent(MenuActivity.this, FlooringActivity.class);
            startActivity(intent);
        }
        if (view== btnShareofShare) {
            Intent intent=new Intent(MenuActivity.this, ShareActivity.class);
            startActivity(intent);
        }
        if (view== btnArriendos) {
            Intent intent = new Intent(MenuActivity.this, CoordenadasActivity.class);
            startActivity(intent);
        }
        if (view== btnExhibiciones) {
            Intent intent = new Intent(MenuActivity.this, ExhibicionActivity.class);
            startActivity(intent);
        }
        if (view==btnPrecios) {
            Intent intent = new Intent(MenuActivity.this, PreciosActivity.class);
            startActivity(intent);
        }
        if (view==btnImpulso) {
            Intent intent = new Intent(MenuActivity.this, ImpulsoActivity.class);
            startActivity(intent);
        }
        if (view==btnCaducados) {
            Intent intent = new Intent(MenuActivity.this, ProductosCaducarActivity.class);
            startActivity(intent);
        }
        if (view==btnPacks) {
            Intent intent = new Intent(MenuActivity.this, PacksActivity.class);
            startActivity(intent);
        }
        if (view==btnMCI) {
            Intent intent = new Intent(MenuActivity.this, MciPdvActivity.class);
            startActivity(intent);
        }
        if (view==btnEjecucionMateriales) {
            Intent intent = new Intent(MenuActivity.this, EjecucionMaterialesActivity.class);
            startActivity(intent);
        }
        if (view== btnCompetencia) {
            Intent intent=new Intent(MenuActivity.this, PromoActivity.class);
            startActivity(intent);
        }
        if (view== btnLogros) {
            Intent intent=new Intent(MenuActivity.this, FotograficoActivity.class);
            startActivity(intent);
        }
        if (view== btnTareas) {
            Intent intent=new Intent(MenuActivity.this, TareasActivity.class);
            startActivity(intent);
        }
        if (view== btnRotacion) {
            Intent intent=new Intent(MenuActivity.this, CanjesActivity.class);
            startActivity(intent);
        }
        if (view== btnSugeridos) {
            Intent intent=new Intent(MenuActivity.this, Sugeridos2Activity.class);
            startActivity(intent);
        }
        /*if (view==btnVenta) {
            Intent intent = new Intent(MenuActivity.this,VentaActivity.class);
            startActivity(intent);
        }
        if (view==btnFotografico) {
            Intent intent = new Intent(MenuActivity.this,FotograficoActivity.class);
            startActivity(intent);
        }*/
        if (view == btnEntradaJornada) {
            alertDialog("Entrada");
            //insertDataRegistro("Entrada", lblLatitud, lblLongitud);
        }
        if (view == btnSalidaJornada) {
            alertDialog("Salida");
            //insertDataRegistro("Salida", lblLatitud, lblLongitud);
        }
    }

    public void sendBroadcastMessageDataLoaded(String tipo_registro) {
        double latitud_pdv = this.latitud_pdv;
        double longitud_pdv = this.longitud_pdv;
        String codigo_pdv = this.codigo_pdv;
        String nombre_pdv = this.punto_venta;
        String perimetro_pdv = this.perimetro_pdv;
        double distance_pdv = this.distance_pdv;
        if (tipo_registro.equalsIgnoreCase("SALIDA")) {
            latitud_pdv = 0;
            longitud_pdv = 0;
            codigo_pdv = "";
            nombre_pdv = "";
            perimetro_pdv = "";
            distance_pdv = 0;
        }
        Intent intent = new Intent(Constantes.ACTION_PDV_LOCATION_UPDATE);
        intent.putExtra(Constantes.LATITUD_PDV_ACTUAL, latitud_pdv);
        intent.putExtra(Constantes.LONGITUDE_PDV_ACTUAL, longitud_pdv);
        intent.putExtra(Constantes.CODIGO_PDV_ACTUAL, codigo_pdv);
        intent.putExtra(Constantes.NOMBRE_PDV_ACTUAL, nombre_pdv);
        intent.putExtra(Constantes.PERIMETRO_PDV_ACTUAL, perimetro_pdv);
        intent.putExtra(Constantes.DISTANCE_PDV_ACTUAL, distance_pdv);
        sendBroadcast(intent);
    }

    public void insertDataRegistro(String TipoRegistro, String[] lblLatitud, String[] lblLongitud, String causal) {
        try {
            Toast.makeText(getApplicationContext(), "Registrando "+ TipoRegistro + " " + lblLatitud[0].toString() + " " + lblLongitud[0].toString(), Toast.LENGTH_LONG).show();
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

                new GuardarLog(MenuActivity.this).saveLog(user, codigo_pdv, registro.toUpperCase() + " PDV");

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
                getContentResolver().insert(ContractInsertGps.CONTENT_URI, values);

                Toast.makeText(getApplicationContext(), "Registrada su "+registro, Toast.LENGTH_SHORT).show();

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertGps, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                //finish();
            } else {
                Toast.makeText(getApplicationContext(), "Genere las Coordenadas", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void insertDataTiempoGestion(String tiempo_inicio, String tiempo_fin) {
        //Almacenar Datos
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss.SSS");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        values.put(ContractInsertAgotados.Columnas.PHARMA_ID, id_pdv);
        values.put(ContractInsertAgotados.Columnas.CODIGO, codigo_pdv);
        values.put(ContractInsertAgotados.Columnas.USUARIO, user);
        values.put(ContractInsertAgotados.Columnas.SUPERVISOR, punto_venta);
        values.put(ContractInsertAgotados.Columnas.TIEMPO_INICIO, tiempo_inicio);
        values.put(ContractInsertAgotados.Columnas.TIEMPO_FIN, tiempo_fin);
        values.put(ContractInsertAgotados.Columnas.FECHA, fechaser);
        values.put(ContractInsertAgotados.Columnas.HORA, horaser);

        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(ContractInsertAgotados.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true, Constantes.insertAgotados, null);
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync_subida1) {
            if (VerificarNet.hayConexion(this)) {
                try {
                    new GuardarLog(MenuActivity.this).saveLog(user, "", "Sincronización de información");
                    SyncAdapter.sincronizarAhora(this, true, Constantes.SUBIR_TODO, null);
                    Snackbar.make(findViewById(R.id.coordinatorPos), Mensajes.ON_SYNC_UP, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.coordinatorPos),
                        Mensajes.ERROR_RED, Snackbar.LENGTH_LONG).show();
            }
            return true;
        }

        if (id == R.id.action_log) {
            Intent intent = new Intent(MenuActivity.this, HistorialActivity.class);
            startActivity(intent);
            return true;
        }
/*
        if (id == R.id.action_cluf) {
            Intent intent = new Intent(MenuActivity.this, ClufActivity.class);
            intent.putExtra("menu", "1");
            startActivity(intent);
            return true;
        }
*/
        if (id == R.id.action_version) {
            mostrarVersion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void mostrarVersion() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.action_version))
                .setMessage(getString(R.string.user) + ": " + user + "\n" +
                        getString(R.string.version))
                .setPositiveButton("ACEPTAR", null)
                .show();
    }

    private void locationStart() {
        Log.i("ENTRA", "locationStart");
        double lat = -2.172652;
        double lng = -79.910018;
        float rad = 1000;
        registerReceiver(new ProximityReciever(), new IntentFilter(ACTION_FILTER));
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMenuActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
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
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, i, PendingIntent.FLAG_MUTABLE);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Aqui empieza la Clase Localizacion
     */
    public class Localizacion implements LocationListener {

        MenuActivity MenuActivity;
        private String fecha,hora,name;

        public MenuActivity getMenuActivity() {
            return MenuActivity;
        }

        public void setMenuActivity(MenuActivity MenuActivity) {
            this.MenuActivity = MenuActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            if (!lblLatitud[0].equals(loc.getLatitude())&&!lblLongitud[0].equals(loc.getLongitude())) {
                lblLatitud[0] = ""+loc.getLatitude();
                lblLongitud[0] = ""+loc.getLongitude();
            }

            //locationManager.removeUpdates(this);
            //Toast.makeText(getApplicationContext(), "Latitud: " + lblLatitud[0].toString() + " y Longitud: " + lblLongitud[0].toString(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
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

//            Toast.makeText(getApplicationContext(),"Saving Location..",Toast.LENGTH_SHORT).show();
            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
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
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
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
//            return localTime;
        }
    }

    public class ListViewAdapterExh extends ArrayAdapter<String> {

        private List<String> sku;
       // private String num_exhibicion;
        public Context context;

        public ListViewAdapterExh(Context context, List<String> sku) {
            super(context, 0, sku);
            this.sku = sku;
         //   this.num_exhibicion = num_exhibicion;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = convertView;

            if (null == convertView) {
                v = inflater.inflate(R.layout.list_row_prioritario, parent, false);
            }

            final TextView lblSku = (TextView) v.findViewById(R.id.lblSku);
//            final EditText lblNumExh = (EditText) v.findViewById(R.id.lblNumExh);2
//            final CheckBox btnSave = (CheckBox) v.findViewById(R.id.respuesta);

            if (sku.size() > 0) {
                lblSku.setText(sku.get(position));

            }
            return v;
        }
    }

    public class ListViewAdapterPop extends ArrayAdapter<String> {

        private List<String> popsugerido;
        // private String num_exhibicion;
        public Context context;

        public ListViewAdapterPop(Context context, List<String> popsugerido) {
            super(context, 0, popsugerido);
            this.popsugerido = popsugerido;
            //   this.num_exhibicion = num_exhibicion;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = convertView;

            if (null == convertView) {
                v = inflater.inflate(R.layout.list_row_popsugerido, parent, false);
            }

            final TextView lblPOPSugerido = (TextView) v.findViewById(R.id.lblPOPSugerido);
//            final EditText lblNumExh = (EditText) v.findViewById(R.id.lblNumExh);
//            final CheckBox btnSave = (CheckBox) v.findViewById(R.id.respuesta);

            if (popsugerido.size() > 0) {
                lblPOPSugerido.setText(popsugerido.get(position));

            }
            return v;
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