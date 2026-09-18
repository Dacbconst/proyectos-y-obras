package com.luckyecuador.app.PintucoAPP;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.AlmuerzoNotificacion;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMarkAlmuerzo;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AlmuerzoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int LOCATION_INTERVAL = 0; //3Minutos
    private static final float LOCATION_DISTANCE = 0;
    private BroadcastReceiver cronometroReceiver;
    private Button btn_ini_almuerzo;
    private Button btn_fin_almuerzo;

    private ImageButton btn_camara;

    public static ImageView iv_foto;
    private TextView txt_jornada;
    private TextView txt_cronometro;
    private TextView txt_tiempo_fuera;

    private DatabaseHelper handler;
    private long tiempo_almuerzo;

    static String image;
    private static Bitmap scaled;  //se define aqui para usarla en varias funciones

    private LinearLayout linearLayoutTiempoFuera;

    private String foto_uri = "";

    private boolean almuerzoEnCurso = false;
    private boolean seEnvioNotifFuera = false;

    public static Double latitud = 0.0;
    public static Double longitud = 0.0;
    static String user,supervisor,hora_ini_almuerzo;

    private static LocationManager locationManager;
    private static LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almuerzo);

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        //Obtenemos el tiempo de almuerzo en miliségundos
        tiempo_almuerzo = handler.getTiempoAlmuerzoEnMs();
      //  tiempo_almuerzo = 60000;

        //Obtenemos valores del share pref
        LoadData();

        //Obtenemos el supervisor
        supervisor = handler.getSurpervisor();

        //Registramos las referencias
        asignarReferencias();

        //Permite muestrar la jornada laboral del mercaderista
        mostrarJornadaLaboral(user,txt_jornada);

//        //En caso, que se halla detenido el servicio por cierre forzado
        if (!isServiceRunning(CronometroServicePintuco.class) && almuerzoEnCurso){
        //    btn_ini_almuerzo.isClickable();

            // Verificar que estamos en el contexto correcto
            if (getApplicationContext().getPackageName().equals("com.luckyecuador.app.PintucoAPP")) {
                // Iniciamos el cronometro y por lo tanto el servicio
                Intent intent = new Intent(AlmuerzoActivity.this, CronometroServicePintuco.class);
                startService(intent);
            } else {
                Log.e("Security", "Intento de inicio de servicio desde app no autorizada");
            }



        }




        //Se va actualizando cada 1000 miliségundos (1 seg)
        cronometroReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long cronoAlmuerzo = intent.getLongExtra("crono1", 0);
                long cronoTiempoFuera = intent.getLongExtra("crono2", 0);
                almuerzoEnCurso = intent.getBooleanExtra("almuerzoEnCurso",false);

                //
                btn_camara.setImageResource(R.drawable.icons8_camara_50);
                // Se agrega el tiempo transcurrido del almuerzo
                txt_cronometro.setText(convertirMsAHoras(cronoAlmuerzo));
                txt_cronometro.setTextColor(getResources().getColor(R.color.colorBlack));

                // Cambia el color cuando se alcanza una hora por defecto
                if (cronoAlmuerzo >= tiempo_almuerzo) {
                    txt_cronometro.setTextColor(getResources().getColor(R.color.rojo_pintuco)); // Cambia a rojo
                    linearLayoutTiempoFuera.setVisibility(View.VISIBLE);
                    txt_tiempo_fuera.setText(convertirMsAHoras(cronoTiempoFuera));


                    /*
                    if (!seEnvioNotifFuera){
                        AlmuerzoNotificacion almuerzoNotificacion = new AlmuerzoNotificacion(getApplicationContext());
                        almuerzoNotificacion.enviarNotificacion(user, "TIEMPO_FUERA_ALMUERZO", supervisor);
                        guardarSharePref(Constantes.NOTIF_ALMUERZO_FUERA,true);
                        seEnvioNotifFuera = true;
                    } */

                } else {
                    txt_cronometro.setTextColor(getResources().getColor(R.color.colorBlack)); // Mantiene negro por defecto
                }


                // Esto es en caso, que se cierre y vuelva a la app
                if (almuerzoEnCurso){
                    btn_ini_almuerzo.setVisibility(View.GONE);
                    btn_fin_almuerzo.setVisibility(View.VISIBLE);
                } else {
                    btn_ini_almuerzo.setVisibility(View.VISIBLE);
                    btn_fin_almuerzo.setVisibility(View.GONE);
                }


                //Foto
                if (!foto_uri.equalsIgnoreCase(Constantes.NODATA) && iv_foto.getDrawable() == null){
                    iv_foto.setImageURI(Uri.parse(foto_uri));
                }


            }
        };


        //COORDENADAS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    //Setear los EditText
                    if(!latitud.equals(location.getLatitude())&&!longitud.equals(location.getLongitude()))
                    {
                        latitud =  location.getLatitude();
                        longitud =  location.getLongitude();
                    }

                    locationManager.removeUpdates(locationListener);

                }catch (Exception e){
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

        // Obtenemos las coordenadas
        getCoordenadas();

    }

    @Override
    public void onClick(View view) {


        // Verificar si ya uso su tiempo de almuerzo
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Boolean tiene_almuerzo = handler.tieneAlmuerzo(user,fechaActual);

        if (!tiene_almuerzo){
            if (view == btn_ini_almuerzo) {

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                hora_ini_almuerzo = hour.format(currentLocalTime);

                //Permite que se muestre "AlmuerzoActivity" al abrir la app
                //Si esta en su hora de almuerzo
                guardarSharePref(Constantes.ALMUERZO_EN_CURSO, true);
                guardarSharePref(Constantes.HORA_INICIO_ALMUERZO, hora_ini_almuerzo);

                btn_ini_almuerzo.setVisibility(View.GONE);
                btn_fin_almuerzo.setVisibility(View.VISIBLE);
                txt_cronometro.setTextColor(getResources().getColor(R.color.colorBlack));
                linearLayoutTiempoFuera.setVisibility(View.GONE);

                //Activamos la camara
                btn_camara.setActivated(true);
                btn_camara.setImageResource(R.drawable.icons8_camara_50);

                new GuardarLog(AlmuerzoActivity.this).saveLog(user, "", "Inicio su almuerzo");

                // Iniciamos el cronometro y por lo tanto el servicio
                Intent intent = new Intent(AlmuerzoActivity.this, CronometroServicePintuco.class);
                startService(intent);

                AlmuerzoNotificacion almuerzoNotificacion = new AlmuerzoNotificacion(getApplicationContext());
                almuerzoNotificacion.enviarNotificacion(user,"INICIO_ALMUERZO",supervisor);

            }

        } else {
                Toast.makeText(getApplicationContext(),"Ya ha utilizado su tiempo de almuerzo hoy",Toast.LENGTH_LONG).show();
        }

        if (view == btn_fin_almuerzo) {

            //Registramos la información
            insertData();

            //Evita que se les muestre el "AlmuerzoActivity"
            //una vez finalizado el almuerzo
            guardarSharePref(Constantes.ALMUERZO_EN_CURSO,false);

            btn_ini_almuerzo.setVisibility(View.VISIBLE);
            btn_fin_almuerzo.setVisibility(View.GONE);

            // Detenemos el cronometro y por lo tanto el servicio
            Intent intent = new Intent(AlmuerzoActivity.this, CronometroServicePintuco.class);
            stopService(intent);

            AlmuerzoNotificacion almuerzoNotificacion = new AlmuerzoNotificacion(getApplicationContext());
            almuerzoNotificacion.enviarNotificacion(user,"FIN_ALMUERZO",supervisor);

            new GuardarLog(AlmuerzoActivity.this).saveLog(user, "", "Finalizo su almuerzo");

        }

        if (view == btn_camara) {

            if (almuerzoEnCurso){
            //    btn_camara.setActivated(true);
            //    btn_camara.setImageResource(R.drawable.icons8_camara_50);
                Intent n = new Intent(AlmuerzoActivity.this, CameraActivity.class);
                n.putExtra("activity", "almuerzo");
                startActivity(n);
            } else {
                Toast.makeText(getApplicationContext(),"Debe iniciar su almuerzo",Toast.LENGTH_LONG).show();
            }


        }



    }



    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(cronometroReceiver, new IntentFilter("actualizar_cronometro"),RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cronometroReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    String tiempo_almuerzo = txt_cronometro.getText().toString();
    //    guardarSharePref(Constantes.TIEMPO_ALMUERZO,tiempo_almuerzo);

    //    String tiempo_fuera = txt_tiempo_fuera.getText().toString();
    //    guardarSharePref(Constantes.TIEMPO_FUERA,tiempo_fuera);
    }



    private void asignarReferencias(){
        //Referencias

        //layouts
        linearLayoutTiempoFuera = findViewById(R.id.linearLayoutTiempoFuera);

        // textviews
        txt_jornada = findViewById(R.id.tv_jornada);
        txt_cronometro = findViewById(R.id.tv_cronometro);
        txt_tiempo_fuera = findViewById(R.id.tv_tiempo_fuera);

        // ImageButton
        btn_camara = findViewById(R.id.btnCameraAlmuerzo);

        // ImageView
        iv_foto = findViewById(R.id.imageViewAlmuerzo);

        // botones
        btn_ini_almuerzo = findViewById(R.id.btnIniciar);
        btn_fin_almuerzo = findViewById(R.id.btnFinalizar);

        // Eventos
        btn_ini_almuerzo.setOnClickListener(this);
        btn_fin_almuerzo.setOnClickListener(this);
        btn_camara.setOnClickListener(this);
        btn_camara.setActivated(false);
    }

    private void mostrarJornadaLaboral(String usuario, TextView txt_jornada){

        DateFormat hour = new SimpleDateFormat("HH:mm");

        String jornada_laboral = "";
        // Obtenemos a que hora tiene su primera marcación de entrada
        String hora_ini_jornada = handler.getHoraInicioJornada(usuario);
        // Obtenemos a que hora tiene su ultima marcación de salida
        String hora_fin_jornada = handler.getHoraFinJornada(usuario);

        // Formato para parsear las horas
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long diferenciaEnMilisegundos = 0;


        if (!hora_ini_jornada.isEmpty() && !hora_fin_jornada.isEmpty()){

            try {
                // Parsear los strings a objetos Date
                Date hora1 = sdf.parse(hora_ini_jornada);
                Date hora2 = sdf.parse(hora_fin_jornada);

                jornada_laboral = sdf.format(hora1) + " a " + sdf.format(hora2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            jornada_laboral = "No hay información";
        }

        txt_jornada.setText(jornada_laboral);
    }

    private String convertirMsAHoras(long milisegundos) {
        int segundos = (int) (milisegundos / 1000) % 60;
        int minutos = (int) (milisegundos / 60000) % 60;
        int horas = (int) (milisegundos / 3600000);
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    private void getCoordenadas(){
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);
        } catch (SecurityException ex) {
            Log.i("TAG", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("TAG", "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);
        } catch (SecurityException ex) {
            Log.i("TAG", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("TAG", "gps provider does not exist " + ex.getMessage());
        }
    }



    /*
    private void guardarSharePref(boolean value){
        SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constantes.FALTA_ALMUERZO,value);
        editor.commit();
    }*/


    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        almuerzoEnCurso = sharedPreferences.getBoolean(Constantes.ALMUERZO_EN_CURSO,false);
        seEnvioNotifFuera = sharedPreferences.getBoolean(Constantes.NOTIF_ALMUERZO_FUERA,false);
        foto_uri = sharedPreferences.getString(Constantes.FOTO_ALMUERZO, Constantes.NODATA);
    }

    public void insertData(){

        int n = Resources.getSystem().getDisplayMetrics().widthPixels;
        int size = 90;
        if(n < 1080){
            size = 35;
        }

        // Obtenemos la fecha y la hora actual
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);
        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String hora_fin_almuerzo = hour.format(currentLocalTime);




        // En caso, que se haya tomado la foto
        if(iv_foto.getDrawable() != null) {

            String fecha = "Fecha : " + fechaser;
            String hora = "Hora : " + hora_fin_almuerzo;
            String usuario = "Usuario: " + user;
            String coordenadas = "Coordenadas: " + latitud + ", " + longitud;

            Bitmap temporal = ((BitmapDrawable) iv_foto.getDrawable()).getBitmap();
            ImageMarkAlmuerzo im = new ImageMarkAlmuerzo();
            Bitmap watermark = im.markSinMapa(temporal, fecha, hora, usuario, coordenadas, Color.YELLOW, 100, size, false);
            int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
            scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

            iv_foto.setImageBitmap(scaled);
            image = getStringImage(scaled);
        }


        if (image == null){
            image = "NO_FOTO";
        }


        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        hora_ini_almuerzo = sharedPreferences.getString(Constantes.HORA_INICIO_ALMUERZO,Constantes.NODATA);



        String jornada_laboral = txt_jornada.getText().toString();
        String tiempo_almuerzo = txt_cronometro.getText().toString();
        String tiempo_fuera = txt_tiempo_fuera.getText().toString();


        ContentValues values = new ContentValues();
        values.put(ContractInsertAlmuerzo.Columnas.USUARIO, user);
        values.put(ContractInsertAlmuerzo.Columnas.JORNADA_LABORAL,jornada_laboral);
        values.put(ContractInsertAlmuerzo.Columnas.TIEMPO_ALMUERZO, tiempo_almuerzo);
        values.put(ContractInsertAlmuerzo.Columnas.TIEMPO_FUERA, tiempo_fuera);
        values.put(ContractInsertAlmuerzo.Columnas.FOTO,image );
        values.put(ContractInsertAlmuerzo.Columnas.LATITUD, latitud);
        values.put(ContractInsertAlmuerzo.Columnas.LONGITUD,longitud );
        values.put(ContractInsertAlmuerzo.Columnas.FECHA, fechaser);
        values.put(ContractInsertAlmuerzo.Columnas.HORA_INI_ALMUERZO,hora_ini_almuerzo);
        values.put(ContractInsertAlmuerzo.Columnas.HORA_FIN_ALMUERZO,hora_fin_almuerzo);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(ContractInsertAlmuerzo.CONTENT_URI, values);
        vaciarCampos();
        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true,Constantes.insertAlmuerzo, null);
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Lo redirecciona a los pdv's
           Intent i = new Intent(AlmuerzoActivity.this, PuntosListActivity.class);
           startActivity(i);

    }

    public void vaciarCampos(){

        linearLayoutTiempoFuera.setVisibility(View.GONE);
        txt_cronometro.setText("00:00:00");
        txt_cronometro.setTextColor(getResources().getColor(R.color.gris));
        txt_tiempo_fuera.setText("00:00:00");
        iv_foto.setImageResource(0);
        btn_camara.setImageResource(R.drawable.icons8_camara_50_disabled);
        almuerzoEnCurso = false;
        image = null;

        guardarSharePref(Constantes.FOTO_ALMUERZO,Constantes.NODATA);
        guardarSharePref(Constantes.ALMUERZO_EN_CURSO,almuerzoEnCurso);
       // guardarSharePref(Constantes.FUERO_TIEMPO_ACTIVO,false);
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.i("service name",""+serviceClass.getName());
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void guardarSharePref(String nom_pref, Object value) {

        SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (value instanceof Boolean) {
            editor.putBoolean(nom_pref, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(nom_pref, (String) value);
        } else {
            throw new IllegalArgumentException("Tipo de dato no soportado");
        }

        editor.commit();
    }

    public static String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

