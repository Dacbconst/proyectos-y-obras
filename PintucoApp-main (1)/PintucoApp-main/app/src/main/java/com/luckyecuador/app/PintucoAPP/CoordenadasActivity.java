package com.luckyecuador.app.PintucoAPP;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CoordenadasActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //BASE SQLITE
    DatabaseHelper handler;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private Spinner spRegistro;

    private Button btnLocalizar;
    private Button btnSaveGPS;
    RelativeLayout reg;
    LinearLayout coor;

    //private TextView lblCoordenadas;

    private LocationManager locationManager;
    private LocationListener locationListener;
    SharedPreferences sharedPref;
    private static final String TAG = "LOG:";
    private static final int LOCATION_INTERVAL = 0; //3Minutos
    private static final float LOCATION_DISTANCE = 0;

    String latitude, longitude;
    private String id_pdv, user, fecha, hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);

//        operador = getIntent().getStringExtra(Constantes.PROMORESULT);

        setToolbar();
        Context context= this.getApplicationContext();
        sharedPref = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        LoadData();

        //startService(new Intent(getApplicationContext(), MyService.class));

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView)findViewById(R.id.txtLongitude);
        spRegistro = (Spinner) findViewById(R.id.spRegistro);
        spRegistro.setOnItemSelectedListener(this);
        reg=(RelativeLayout) findViewById(R.id.registrolayout);
        coor=(LinearLayout) findViewById(R.id.layout_photo);

        btnLocalizar = (Button)findViewById(R.id.btnLocalizar);
      //  btnSaveGPS = (Button)findViewById(R.id.btnSaveGps);

        llenarRegistro();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    //Setear los EditText
                    if (!txtLatitude.getText().equals(location.getLatitude())&&!txtLongitude.getText().equals(location.getLongitude()))
                    {
                        txtLatitude.setText(""+location.getLatitude());
                        txtLongitude.setText(""+location.getLongitude());
                        reg.setVisibility(View.VISIBLE);
                        coor.setEnabled(false);
                        coor.setBackgroundColor(777777);
                    }


                    locationManager.removeUpdates(locationListener);
//                locationManager = null;
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) {
                //Si el GPS esta deshabilitado, abrir la ventana de activacion del GPS en el dispositivo.
                Intent newIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(newIntent);
            }
        };


        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
            }return;
        }else {
            //Si los permisos estan otorgados, llamar al evento onClick del boton
            clickButton();
        }

//        btnSaveGPS.setOnClickListener(this);

    }//FIN ONCREATE


    /*
     *   TOOLBAR
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
    }

    public void llenarRegistro() {
        //Llenar Spinner Observacion
        ArrayAdapter adaptadorKeyword1 = ArrayAdapter.createFromResource(this,R.array.registro,android.R.layout.simple_spinner_item);
        adaptadorKeyword1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegistro.setAdapter(adaptadorKeyword1);
    }


    @Override
    public void onClick(View v) {

    }


    /*
     *  COORDENADAS CONFIGURACION
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int [] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    clickButton();
                return;
            //Si cumple este if, significa que el permiso fue aceptado (granted)
            //Si la solicitud de permiso es cancelada, entonces el array resultado esta vacio
        }
    }

    private void clickButton() {
        btnLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLatitude.setHint("Buscando..");
                txtLongitude.setHint("Buscando..");
                //Los permisos ya estan concedidos cuando se llama a esta funcion (clickButton()).
//                locationManager.requestLocationUpdates("gps",0,0,locationListener);
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            locationListener);
                } catch (SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                }
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            locationListener);
                } catch (SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView==spRegistro)
        {
            String registro=spRegistro.getSelectedItem().toString();
            try {
                if (registro != null || !registro.equals("") || !registro.isEmpty()) {
                    insertData();
                    //finish();
                }
            }catch (Exception a)
            {
                a.printStackTrace();
            }
        }
    }


    public void insertData()
        {

        try{
            if (!txtLatitude.getText().toString().equals("") && txtLatitude.getText().toString()!=null &&
                    !txtLongitude.getText().toString().equals("") && txtLongitude.getText().toString()!=null) {
                latitude = txtLatitude.getText().toString();
                longitude = txtLongitude.getText().toString();
                String registro = spRegistro.getSelectedItem().toString().trim();

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                ContentValues values = new ContentValues();

                values.put(ContractInsertGps.Columnas.IDPDV,id_pdv);
                values.put(ContractInsertGps.Columnas.USUARIO,user);
                values.put(ContractInsertGps.Columnas.TIPO,registro);
                values.put(ContractInsertGps.Columnas.LATITUDE, latitude);
                values.put(ContractInsertGps.Columnas.LONGITUDE, longitude);
                values.put(ContractInsertGps.Columnas.FECHA, fechaser);
                values.put(ContractInsertGps.Columnas.HORA, horaser);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertGps.CONTENT_URI, values);
                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true,Constantes.insertGps, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE,Toast.LENGTH_SHORT).show();
                }

                //VaciarViews
                txtLatitude.setText("");
                txtLongitude.setText("");

                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Genere las Coordenadas",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
