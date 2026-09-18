package com.luckyecuador.app.PintucoAPP;

import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_DISTANCE;
import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_INTERVAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.Clase.Asistencia;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.Base_pharma_value;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.BatteryLevel;
import com.luckyecuador.app.PintucoAPP.Utils.EvidenciaMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AsistenciaActivity extends AppCompatActivity {


    private FusedLocationProviderClient fusedLocationClient;

    public String codigo,usuario, supervisor,punto_venta,dir, hora_marcacion, tiempo_espera, tiempo_notificacion;
    public double distance,distance_pdv,latitud_pdv,longitud_pdv;
    public Double latitud = 0.0, longitud = 0.0;

    public static AlertDialog ad;

    public static boolean valor = false;

    CountDownTimer countDownTimer;
    ArrayList<Asistencia> asistenciaList;
    private static boolean isActivityRunning = false;
    DatabaseHelper handler;

    public static Geocoder geocoder;

    public static List<Address> addresses;
    BasePharmaValue pdv = new BasePharmaValue();
    private boolean activityVisible;
    static String TAG = "AsistenciaActivity";

    public static ImageView img = null;
    public static ImageView img2 = null;

    public int c = 0;
    private long t_espera = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static final String ACTION_NOTIFICATION_CLICKED = "com.ejemplo.app.NOTIFICATION_CLICKED";
    private Boolean tuvoMarcacion = false;

    String hora;

    public static boolean isActivityRunning() {
        Log.i("activitruni","esta corriendo "+isActivityRunning);
        //Log.i("activitruni","esta corriendo "+ activityVisible);
        return isActivityRunning;
    }

    @Override
    protected void onDestroy() {
        Log.i("on destoyr aa", "true");
        super.onDestroy();
        isActivityRunning = false;
        SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.ACTIVIDAD, "cerrado");
        editor.commit();
        countDownTimer.cancel();
        //unregisterReceiver(recibirHoraMarcacion);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("on create asiteacia","ok");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

        //IntentFilter filter = new IntentFilter("enviar_hora_marcacion");
        //registerReceiver(recibirHoraMarcacion, filter);
        handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        //el unico metodo para obtener la hora de marcacion es la tabla local de unico registro
        hora_marcacion = handler.getHoraMarcacion();

        Log.i(TAG, "variables antes de intent { t.e= "+ getIntent().getStringExtra("tiempo_espera")+
                " t.n="+ getIntent().getStringExtra("tiempo_notificacion")+
                " h.m="+ getIntent().getStringExtra("hora")+" }");

        if (getIntent() != null && getIntent().hasExtra("hora")
                && getIntent() != null && getIntent().hasExtra("tiempo_espera")
                && getIntent() != null && getIntent().hasExtra("tiempo_notificacion")) {

            tiempo_espera = getIntent().getStringExtra("tiempo_espera");
            t_espera = Long.parseLong(tiempo_espera) * 1000; //convertir a milisegundo
            tiempo_notificacion = getIntent().getStringExtra("tiempo_notificacion");
            Log.i(TAG, "variables intent { t.e= "+ t_espera+" t.n="+ tiempo_notificacion+" h.m="+ hora_marcacion+" }");

            //en caso de forzar salida
            SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putString(Constantes.HORA_MARCACION, hora_marcacion);
            editor.putString(Constantes.TIEMPO_ESPERA, tiempo_espera);
            editor.putString(Constantes.TIEMPO_NOTIFICACION, tiempo_notificacion);
            editor.commit();
        } else {

            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            //hora_marcacion = sharedPreferences.getString(Constantes.HORA_MARCACION,Constantes.NODATA);

            tiempo_espera = sharedPreferences.getString(Constantes.TIEMPO_ESPERA,Constantes.NODATA);
            t_espera = Long.parseLong(tiempo_espera) * 1000; //convertir a milisegundo
            tiempo_notificacion = sharedPreferences.getString(Constantes.TIEMPO_NOTIFICACION,Constantes.NODATA);
            Log.i(TAG, "variables share { t.e= "+ t_espera+" t.n="+ tiempo_notificacion+" h.m="+ hora_marcacion+" }");
        }

        isActivityRunning = true;
        activityVisible = true;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {


                    if (latitud != null && longitud != null) {

                        //Setear los EditText
                        if (!latitud.equals(location.getLatitude()) && !longitud.equals(location.getLongitude())) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();

                           try {
                               if (VerificarNet.hayConexion(getApplicationContext())) {
                                   addresses = geocoder.getFromLocation(latitud, longitud, 1);
                                   if(latitud != 0 && longitud != 0){
                                       if(addresses.size()>0){
                                           dir = ""+ addresses.get(0).getAddressLine(0);
                                           //direccion.setText(dir);
                                       }
                                   }
                               } else {
                                   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                               }
                           }catch (Exception ex){
                               Log.i("ERROR",""+ex.getMessage());
                           }
                        }

                    }

                    locationManager.removeUpdates(locationListener);
//                locationManager = null;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Si el GPS esta deshabilitado, abrir la ventana de activacion del GPS en el dispositivo.
                Intent newIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(newIntent);
            }
        };

        LoadData();
        esHoraValida();
        obtenerCoordenadas();
        mostarDialog();
        iniciarCountDownTimer();

    }


   /* private BroadcastReceiver recibirHoraMarcacion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("enviar_hora_marcacion".equals(intent.getAction())) {
                // Llama al método showNotification() para mostrar la notificación nuevamente
                Log.i(TAG, "Broadcast recibió hora marcacion: " + hora_marcacion);

            }
        }
    };*/
    public void consultarHoraOld(){

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String cs = sharedPreferences.getString(Constantes.NUEVO,Constantes.NODATA);

        asistenciaList = handler.getHoraInsert();
        Log.i("lsota" , ""+(cs));
        if (asistenciaList.size() == 1){//no hay registro
            //showToast();
            if (!cs.equals("cancelar") && !cs.equals("aceptar")){
                Toast.makeText(this, "no se requiere registro", Toast.LENGTH_SHORT).show();
            }else{
                SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constantes.NUEVO, "abierto");
                editor.commit();
            }
            if(countDownTimer != null){
                countDownTimer.cancel();
            }
            finish();
        }

        /*LocalTime horaActual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            horaActual = LocalTime.now();
        }


        LocalTime horaProporcionada = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            horaProporcionada = LocalTime.parse(hora);
        }

        long diferenciaSegundos = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            diferenciaSegundos = ChronoUnit.SECONDS.between(horaActual, horaProporcionada);
        }

        if (Math.abs(diferenciaSegundos) <= 29) {
            Log.i("cdsaz","no sobrepasa " + hora);
        } else {
            Log.i("cdsaz","sobrepasa " + hora);
            finish();
        }*/
    }

    public void esHoraValida(){

        //variables que intervienen
       //hora en que manda la noticifacion
        Log.i(TAG, "esHoraValida, hora marcacion:"+ hora_marcacion);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String horaFormateada = dateFormat.format(calendar.getTime()); //la hora actual

        //tiempo de espera traido de la base
        int segundosEspera = Integer.parseInt(tiempo_notificacion);

        //Dar formato de hora
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        LocalTime h_marcacion = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            h_marcacion = LocalTime.parse(hora_marcacion, formatter);
        }

        LocalTime h_actual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            h_actual = LocalTime.parse(horaFormateada, formatter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime t_fin = h_marcacion.plusSeconds(segundosEspera);

            Log.i("hora actual", h_actual +"");
            Log.i("hora marcacion", h_marcacion +"");
            Log.i("tiempo fin", t_fin +"");

            if(h_actual.isBefore(t_fin) && (h_actual.isAfter(h_marcacion) || h_actual.equals(h_marcacion))){
                Log.i(TAG, "hora dentro del rango");

            } else {

                //SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                //String marcacion_anterior = sharedPreferences.getString(Constantes.MARCACION_ANTERIOR,Constantes.NODATA);

                /*if(!handler.marcacionEnviada(hora_marcacion)){
                    Log.i(TAG, "Database: no tiene marcacion enviada");
                    //insertDataRegistro(latitud,longitud,"NO VALIDADO");
                    Toast.makeText(this, "Tiempo límite alcanzado", Toast.LENGTH_LONG).show();
                }*/
                Log.i(TAG, "hora fuera del rango");

                ad.dismiss();
                finish();
            }
        }


    }

    private void iniciarCountDownTimer() {
        Log.i(TAG, "iniciarCountDownTimer: inicia tiempo espera para cerrar la alerta " + t_espera);

        countDownTimer = new CountDownTimer(t_espera, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                Log.d(TAG, "CountDownTimer, Segundos restantes: " + segundosRestantes + " actv: "+ activityVisible);
                /*Log.d("CountDownTimer", "Segundos restantes 2: " + asistenciaList.size());
                if (!activityVisible || asistenciaList.size() == 1) {
                    finish();
                    countDownTimer.cancel();
                }*/
            }

            @Override
            public void onFinish() {
                //insertDataRegistro(latitud,longitud,"NO VALIDADO");
                if (activityVisible) {
                    Log.i(TAG,"CountDownTimer, activityVisible al finalizar: true");
                    // Si el Activity aún está visible, mostrar el Toast
                    //showToast();
                    Toast.makeText(getApplicationContext(), "Tiempo limite alcanzado.", Toast.LENGTH_SHORT).show();
                    insertDataRegistro(latitud,longitud,"NO VALIDADO");
                    //countDownTimer.cancel();
                    Log.i(TAG,"se mostro el toast tiempo limite y se inserto el registro NO VALIDADO");

                }
                SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constantes.ACTIVIDAD, "cerrado");
                editor.commit();
                finish();
                Log.i(TAG,"se cerró la alerta");
                activityVisible = false;
                ad.dismiss();

            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityVisible = true;
        Log.i(TAG,"activityVisible en onResume: "+ activityVisible);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!valor){
            activityVisible = false;
            Log.i(TAG,"activityVisible en onPause y !valor: "+ activityVisible);
        }
        isActivityRunning = false;
        Log.i(TAG,"isActivityRunning en onPause: "+ isActivityRunning);
    }




    private void obtenerCoordenadas() {


//            txtlatitud.setHint("Buscando..");
//            txtlongitud.setHint("Buscando..");

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



    public boolean insertDataRegistro(double latitud, double longitud, String estado) {
        try {

            Log.i(TAG, "insertDataRegistro, estado: " + estado);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            DateFormat hour_2 = new SimpleDateFormat("HH:mm");
            hour_2.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser_ac = hour_2.format(currentLocalTime);


            if (latitud != 0.0 && longitud != 0.0){

                pdv = handler.getPdv(codigo);
                distance_pdv = Double.parseDouble(pdv.getDistancia());
                latitud_pdv = Double.parseDouble(pdv.getLatitud());
                longitud_pdv = Double.parseDouble(pdv.getLongitud());

                MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
                MarkerOptions place2 = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Posicion PDV");

                distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
                distance = Double.valueOf(Math.round(distance));

            }else{
                distance = 0.0;
            }

            String image = "";
            if (!estado.equals("NO VALIDADO")){

                /*int n = Resources.getSystem().getDisplayMetrics().widthPixels;
                int size = 45;
                if(n < 1080){
                    size = 35;
                }*/

                String ciudad = "Ciudad: " + handler.getCityPdv(codigo);;
                String local = "Local: " + punto_venta;
                String usuarios = "Usuario: " + usuario;
                String direccion = "" + dir;
                String coordenadas = "coordenadas: " + latitud + ", " + longitud;
                String fechaHora = "Fecha y hora: " + fechaser + " " + horaser;

                Bitmap temporal = ((BitmapDrawable) img.getDrawable()).getBitmap();
                EvidenciaMark im = new EvidenciaMark();
                Bitmap watermark = im.markSinMapa(temporal, ciudad, local, usuarios, coordenadas, direccion, fechaHora,Color.YELLOW, 100, 85,false);
                int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
                image = getStringImage(scaled);
                // Única foto que se guarda en galería: la foto cruda ahora vive en un
                // archivo privado de caché (ver CameraActivity), nunca llega a MediaStore.
                guardarFotoEnGaleria(scaled, fechaser, horaser);
            }else{

                image = "";
            }

            /*Bitmap temporal = ((BitmapDrawable) img.getDrawable()).getBitmap();

            EvidenciaMark em = new EvidenciaMark();
            Bitmap watermark = em.mark2(temporal, "supervisora", "ciudad: GUAYAQUIL", "local:SUPERMAXI", "usuario: PRUEBA LUCKY", "fechaHora: 2021/01/01 10:25", "coordenadas: -134195, 45235265", "dirFinal: CAMILO DESTRUJE ENTRE LA 35 Y 36", Color.YELLOW, 100, 80, false,img2);
            int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

            img.setImageBitmap(scaled);*/

            //handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);


            Base_pharma_value bpv = handler.getInfoPDV(codigo);

            ContentValues values = new ContentValues();

            values.put(ContractInsertAsistencia.Columnas.IDPDV, codigo);
            values.put(ContractInsertAsistencia.Columnas.USUARIO, usuario);
            values.put(ContractInsertAsistencia.Columnas.FOTO, image);
            values.put(ContractInsertAsistencia.Columnas.VERSION,getResources().getString(R.string.version));
            values.put(ContractInsertAsistencia.Columnas.LATITUDE, latitud + "");
            values.put(ContractInsertAsistencia.Columnas.LONGITUDE, longitud + "");
            values.put(ContractInsertAsistencia.Columnas.FECHA, fechaser);
            values.put(ContractInsertAsistencia.Columnas.HORA, horaser);
            values.put(ContractInsertAsistencia.Columnas.DISTANCIA, distance);
            values.put(ContractInsertAsistencia.Columnas.POS_NAME, bpv.getPos_name());
            values.put(ContractInsertAsistencia.Columnas.BATERIA, new BatteryLevel().nivelBateria(getApplicationContext()));
            values.put(ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA, estado);
            values.put(ContractInsertAsistencia.Columnas.SUPERVISOR, supervisor);
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            getContentResolver().insert(ContractInsertAsistencia.CONTENT_URI, values);

            ContentValues values2 = new ContentValues();
            values2.put(ContractAsistenciasLocal.Columnas.REGISTRADO, "enviado");
            values2.put(ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION, estado);

            new TareaEditarAsistenciaLocal(getApplicationContext().getContentResolver(), values2, hora_marcacion).execute(ContractAsistenciasLocal.CONTENT_URI);

            Toast.makeText(this, "Registrada su Asistencia ", Toast.LENGTH_SHORT).show();

            if (VerificarNet.hayConexion(getApplicationContext())) {
                Log.i(TAG, "si hay net");
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertAsistencia, null);
                Toast.makeText(this, Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    public String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public boolean guardarFotoEnGaleria(Bitmap scaled, String fechaser, String horaser){
        String usuario2 = usuario.replace(" ","_");
        String fechaS = fechaser.replace("/","");
        String horaS = horaser.replace(":","");

        String fileName = "ASISTENCIA_"+ usuario2 + "_" + fechaS + "_" + horaS + ".jpeg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Asistencia/");
        } else {
            File directory = Environment.getExternalStoragePublicDirectory("Asistencia");
            if (!directory.exists()){directory.mkdir();}
            File file = new File(directory, fileName);
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            return false;
        }

        try(OutputStream output = getContentResolver().openOutputStream(uri)) {
            scaled.compress(Bitmap.CompressFormat.JPEG, 100, output);
            Toast.makeText(getApplicationContext(),"Foto de asistencia guardada en galería: Asistencia",Toast.LENGTH_LONG).show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class TareaEditarAsistenciaLocal extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;
        private final String hora;

        public TareaEditarAsistenciaLocal(ContentResolver resolver, ContentValues valores, String hora) {
            this.resolver = resolver;
            this.valores = valores;
            this.hora = hora;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Log.i(TAG, "Actualizando tabla local");
            Uri uri = args[0];
            if (null != uri) {
                String selectQuery = ContractAsistenciasLocal.Columnas.HORA + "=?"; ///VER COMO ACTUALIZA
                String[] val = new String[]{hora};
                resolver.update(uri, valores, selectQuery, val);
            }
            return null;
        }

    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        codigo = sharedPreferences.getString(Constantes.CODIGO,Constantes.NODATA);
        usuario = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        //modulo_actual = sharedPreferences.getString(Constantes.MODULO_ACTUAL, Constantes.NODATA).toUpperCase();

        //idRuta =  sharedPreferences.getString(Constantes.ID_RUTA,Constantes.NODATA);
        //bateria = sharedPreferences.getString(Constantes.BATERIA, Constantes.NO_DATA);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    public void mostarDialog(){
        Log.i(TAG, "mostrarDialog");
        LayoutInflater myLayout = LayoutInflater.from(getApplicationContext());
        View dialogView = myLayout.inflate(R.layout.alertdialog_asistencia, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        ImageButton btn = dialogView.findViewById(R.id.btnCamera);
        img = dialogView.findViewById(R.id.ivFotoRegistro);

        img2 = dialogView.findViewById(R.id.ivFotoMapa);

        /*String imageUrl2 = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&markers=40.714728,-73.998672&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";
        String imageUrl = "https://www.prolipa.com.ec/actividades/mapapolitico/imagenes/mapa.png";

        //Picasso.get().load(imageUrl).into(img2);

        Picasso.get()
                .load(imageUrl)
                .error(R.mipmap.ic_launcher) // Si deseas mostrar una imagen de placeholder en caso de error
                .into(img2, new Callback() {
                    @Override
                    public void onSuccess() {
                        // La imagen se cargó con éxito
                    }

                    @Override
                    public void onError(Exception e) {
                        // Se produjo un error al cargar la imagen
                        e.printStackTrace();
                    }
                });*/

        //boton foto
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerCoordenadas();
                Intent n = new Intent(dialogView.getContext(), CameraActivity.class);
                n.putExtra("activity", "asistencia");
                startActivity(n);
                valor = true;
                activityVisible = true;
            }
        });

        ad = builder.create();
        ad.setCancelable(false);
        Log.i(TAG, "mostrando dialogo de asistencia, mostrando ad: "+ad);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(AsistenciaActivity.this, "aceptar", Toast.LENGTH_SHORT).show();

                if (esFormularioValido()){
                    insertDataRegistro(latitud,longitud,"VALIDADO");

                    SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constantes.ACTIVIDAD, "cerrado");
                    editor.putString(Constantes.NUEVO, "aceptar");
                    editor.commit();
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                        Log.i(TAG, "cerrando dialogo de asistencia, cerrando ad: "+ad);
                    }
                    ad.dismiss();
                /*Intent intent = new Intent(getApplicationContext(), MenuNavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                    finish();
                }

            }

        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if(esFormularioValido()){
                    insertDataRegistro(latitud,longitud,"NO VALIDADO");

                    SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constantes.ACTIVIDAD, "cerrado");
                    editor.putString(Constantes.NUEVO, "cancelar");
                    editor.commit();

                    if(countDownTimer != null){
                        countDownTimer.cancel();
                        Log.i(TAG, "cerrando dialogo de asistencia, cerrando ad: "+ad);
                    }

                    ad.dismiss();
                /*Intent intent = new Intent(getApplicationContext(), MenuNavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                    finish();
                //}
            }
        });

        Log.i(TAG, "activityVisible "+activityVisible);
        if(activityVisible) {
            Log.i(TAG, "mostrando ad? "+ ad.isShowing()); //false
            if(!ad.isShowing()){
                ad.show();
            }
        }

        /*String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&markers=40.714728,-73.998672&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";

        Picasso.get().load(imageUrl).into(img);*/

    }

    public boolean esFormularioValido(){
        if (img.getDrawable() == null){
            Toast.makeText(this, "La foto es obligatoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}