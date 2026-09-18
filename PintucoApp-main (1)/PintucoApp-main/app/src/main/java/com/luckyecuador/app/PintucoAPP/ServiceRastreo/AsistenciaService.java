package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.AsistenciaActivity;
import com.luckyecuador.app.PintucoAPP.Clase.Asistencia;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractHoraMarcacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.BatteryLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AsistenciaService extends Service {

    private static final String CHANNEL_ID = "NotificationChannel";
    private static final String CHANNEL_ID_2 = "3";
    private static final int NOTIFICATION_ID = 123;
    public Double latitud = 0.0, longitud = 0.0;
    public static List<Address> addresses;
    public static Geocoder geocoder;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private static String TAG = "AsistenciaServices";
    private boolean clickableNotification = true;

    List<Asistencia> asistencias = new ArrayList<>();

    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;
    public String tiempo_base = "", tiempo_espera = "", tiempo_notificacion = "";

    private boolean isTimerRunning = false;

    private String hora_entrada;
    private String nueva_hora_entrada;

    private String pos_id,usuario, supervisor, distance, pos_name;
    private boolean falta_salida;

    public String hora_final;

    DatabaseHelper handler;
    LocalTime siguienteMarcacion;
    String sgtMarcacion;

    public static final String ACTION_NOTIFICATION_CLICKED = "com.ejemplo.app.NOTIFICATION_CLICKED";

    public AsistenciaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("Mostrar_Notificacion_Accion");
        registerReceiver(mostrarNotificacionReceiver, filter);
        handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);
        geocoder = new Geocoder(this, Locale.getDefault());

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
                                            //dir = ""+ addresses.get(0).getAddressLine(0);
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

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Crear y mostrar la notificación
        LoadData();
        Log.i("vfcds","entro 1");
        if (intent != null && intent.hasExtra("tiempo")) { //caso de entrada normal
            tiempo_base = intent.getStringExtra("tiempo");
            tiempo_espera = intent.getStringExtra("tiempo_espera");
            tiempo_notificacion = intent.getStringExtra("tiempo_notificacion");
            nueva_hora_entrada = intent.getStringExtra("hora_entrada");

            Log.i("tiempo base As",tiempo_base);
            Log.i("tiempo espera As",tiempo_espera);
        }else{
            if (falta_salida) { //caso de forzar cierre
                Log.i("vfcds","entro en falta salida");
                SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE); //retomando los valores
                tiempo_base = sharedPreferences.getString(Constantes.TIEMPO_BASE, Constantes.NODATA);
                tiempo_espera = sharedPreferences.getString(Constantes.TIEMPO_ESPERA, Constantes.NODATA);
                tiempo_notificacion = sharedPreferences.getString(Constantes.TIEMPO_NOTIFICACION, Constantes.NODATA);
                nueva_hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA, Constantes.NODATA);
            }
        }

        //hora_final = handler.getHoraFin(pos_id); //primera hora fin

        //getTiempo(,usuario,pos_id);
        createNotificationChannel();
        showNotification(); //notificacion inicial
        return START_STICKY;
    }

    private void stopForegroundNotification() {
        stopForeground(true);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_2,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.enableLights(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.canBubble();
            }
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


    //calcula hora del siguiente insert
    public void calcularTiempoInserts(){ //calcula la siguiente hora de insert cada vez que aparece la notificacion

        /*SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA,Constantes.NODATA);*/

        //Log.i("horario entrada" , ""+hora_entrada);

        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Date cTime = Calendar.getInstance().getTime();
        String time_now = sdf.format(cTime);

        try {
            cal2.setTime(sdf.parse(time_now)); //antes estaba la hora de entrada
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int segundos = cal2.get(Calendar.SECOND);

        if (segundos > 30) {
            cal2.add(Calendar.MINUTE, 1);
        }

        cal2.set(Calendar.SECOND, 0);

        SimpleDateFormat hour2 = new SimpleDateFormat("HH:mm:ss");
        String horaAjustada = hour2.format(cal2.getTime());

        //String horaInicio = "15:01:00";
        String horaInicio = horaAjustada; //hora actual
        //String horaFin = hora_final; //fin visita pdv d
        String minuto = tiempo_base;

        Log.i(TAG,"CalcularTiempoInserts, entrada ajustada: "+horaInicio);

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        LocalTime tiempoInicio = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            tiempoInicio = LocalTime.parse(horaInicio, formatter);
        }
        /*LocalTime tiempoFin = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            tiempoFin = LocalTime.parse(horaFin, formatter);
        }*/


        int minutoIntervalo = Integer.parseInt(minuto);

        //List<String> tiempos = new ArrayList<>();

        LocalTime tiempoActual = tiempoInicio; //hora actual ajustada
        //siguienteMarcacion = tiempoInicio; //hora siguiente marcacion

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.i(TAG, "CalcularTiempoInserts, tiempo actual "+tiempoActual);

            siguienteMarcacion = tiempoInicio.plusMinutes(minutoIntervalo);
            sgtMarcacion = siguienteMarcacion.toString() + ":00";

            ContentValues values = new ContentValues();
            values.put(ContractAsistenciasLocal.Columnas.HORA, sgtMarcacion);
            values.put(ContractAsistenciasLocal.Columnas.REGISTRADO, "no enviado");
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            //creando la tabla interna con los tiempos
            getContentResolver().insert(ContractAsistenciasLocal.CONTENT_URI, values);

            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constantes.HORA_MARCACION, sgtMarcacion);
            editor.putString("sh_prueba", "4");
            editor.commit();

            Log.i(TAG, "Siguiente marcacion definida en: "+sgtMarcacion+ " - "+sharedPreferences.getString(Constantes.HORA_MARCACION, Constantes.NODATA));

            /*while (tiempoActual.isBefore(tiempoFin) || tiempoActual.equals(tiempoFin)) { //mientras este dentro del rango de hora del rutero
                Log.i("tiempominutos 1",""+tiempoActual);
                Log.i("tiempominutos 2",""+tiempoInicio);
                int c = 0;
                if (!tiempoActual.format(formatter).equalsIgnoreCase(tiempoInicio.format(formatter))){
                    tiempos.add(tiempoActual.format(formatter));
                }
                tiempoActual = tiempoActual.plusMinutes(minutoIntervalo); //calcula todas las horas futuras donde se debe presentar la notificacion
            }*/
        }

        // Imprimir los tiempos
        //System.out.println("Tiempos generados:");
        /*for (String tiempo : tiempos) {
            ContentValues values = new ContentValues();
            values.put(ContractAsistenciasLocal.Columnas.HORA, tiempo);
            values.put(ContractAsistenciasLocal.Columnas.REGISTRADO, "no enviado");
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            //creando la tabla interna con los tiempos
            getContentResolver().insert(ContractAsistenciasLocal.CONTENT_URI, values);
        }*/

    }

    /*public static String enviarHoraMarcacion(){
        String hora_marcacion = "0";

        hora_marcacion = sgtMarcacion;

        return hora_marcacion;
    }*/

    //espera a que la hora sea con 0 seg para iniciar el contador de 8 horas
    public void calcularTiempo(){ //esperar a que sean 0 seg

        //long timeElapsed = SystemClock.elapsedRealtime() - tiempoEnMilisegundos;

        /*Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String horaConSegundos = dateFormat.format(currentTime);*/

        /*Calendar cal = Calendar.getInstance();
        int segundoActual = cal.get(Calendar.SECOND);
        Log.i("csvf 1",""+cal.getTime());

        if (segundoActual >= 30) {
            while (segundoActual != 0) {
                cal = Calendar.getInstance();
                segundoActual = cal.get(Calendar.SECOND);
            }
        }*/

        // Obtener la hora actual en miliseg
        Calendar cal = Calendar.getInstance();
        int segundoActual = cal.get(Calendar.SECOND);

        long tiempoRestanteMillis = (60 - segundoActual) * 1000;

        //esperar que sean 0seg
        new CountDownTimer(tiempoRestanteMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "Preparando para iniciar contador de 8 horas: " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "CountDownTimer finalizado");
                iniciarTiempo(); //empieza a verificar si es la hora
            }
        }.start();

        isTimerRunning = true;
    }

    //contador que verifica el tiempo y envia la notificaicon si ya es el momento (dura 8 horas)
    public void iniciarTiempo(){

        //int tiempo = Integer.parseInt(tiempo_base);

        //String hora_final = handler.getHoraFin(pos_id);

        int tiempo = 1;

        long milisegundos = tiempo * 60 * 1000;
        long milisegundos2 = tiempo * 60 * 1000;

        Calendar cal3 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        try {
            cal3.setTime(sdf2.parse(nueva_hora_entrada));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int segundos_ = cal3.get(Calendar.SECOND);


        if (segundos_ > 30) {
            cal3.add(Calendar.MINUTE, 1);
        }
        cal3.set(Calendar.SECOND, 0);

        SimpleDateFormat hour3 = new SimpleDateFormat("HH:mm:ss");
        String horaAjustada = hour3.format(cal3.getTime());

        //String horaInicio = "15:01:00";

        String horaInicio = horaAjustada;
        //String horaFin = hora_final; //fin de la visita al pdv

        //Log.i("hora fin cont",""+horaFin);

        // Formato de la hora
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long tiempoEnMilisegundos = 28800000; //8 horas
        try {
            // Convertir las horas de inicio y fin a objetos Date
            Date dateInicio = format.parse(horaInicio);
            //Date dateFin = format.parse(horaFin);


            // Calcular la diferencia de tiempo en milisegundos
            /*long diferenciaMillis = dateFin.getTime() - dateInicio.getTime();

            // Convertir la diferencia de tiempo a horas, minutos y segundos
            long horas = diferenciaMillis / (60 * 60 * 1000);
            long minutos = (diferenciaMillis / (60 * 1000)) % 60;
            long segundos = (diferenciaMillis / 1000) % 60;*/

            // Imprimir la diferencia de tiempo
            //System.out.println("Diferencia de tiempo: " + horas + " horas, " + minutos + " minutos, " + segundos + " segundos");
            //String nuevoTiempo = String.format("%02d:%02d:%02d", "8", "00", "00");
            ///
            /*String[] partes = nuevoTiempo.split(":");
            int horas2 = Integer.parseInt(partes[0]);
            int minutos2 = Integer.parseInt(partes[1]);
            int segundos2 = Integer.parseInt(partes[2]);

            // Convertir cada unidad a milisegundos y sumarlos
            tiempoEnMilisegundos = horas2 * 3600000L + minutos2 * 60000L + segundos2 * 1000L;*/

            Log.i(TAG,"8 horas en milisegundos: "+tiempoEnMilisegundos);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //actualiza el tiempo restante y verifica si es la hora de mostrar la alerta

        countDownTimer = new  CountDownTimer(tiempoEnMilisegundos, milisegundos) {
            int c = 0;
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "contador de 8 horas activo");
                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:00");
                String horaConSegundosEnCero = dateFormat.format(currentTime);
                //asistencias = handler.getHorasInsert();  //reemplazar
                //recorre los registros de la tabla interna

                //alerta

                /*SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

                String activity = sharedPreferences.getString(Constantes.ACTIVIDAD,Constantes.NODATA);*/

                //for(int i = 0 ; i < asistencias.size() ; i++){
                    //aqui deberia actualizar la siguiente hora
                Log.i("Comparacion ", "hora actual: "+ horaConSegundosEnCero + " siguiente marc: "+ sgtMarcacion);
                    if(horaConSegundosEnCero.equals(sgtMarcacion)){

                        /*Intent intentHm = new Intent("enviar_hora_marcacion");
                        intentHm.putExtra("hora_marcacion", sgtMarcacion);
                        Log.i(TAG, "enviand brodcast hm");
                        sendBroadcast(intentHm);*/

                        iniciarTiempoDeEspera(sgtMarcacion, tiempo_espera, tiempo_notificacion);

                        Intent intent = new Intent("Mostrar_Notificacion_Accion");
                        sendBroadcast(intent);
                        //Log.i("metodo abierto " , "" + AsistenciaActivity.isActivityRunning());

                        /*SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                        String activity = sharedPreferences.getString(Constantes.ACTIVIDAD,Constantes.NODATA);

                        Log.i("mjfnv" , "c " + activity);*/
                        //if (activity.equalsIgnoreCase("abierto")){
                            //if(!AsistenciaActivity.isActivityRunning()){

                        //
                        //if(!activity.equals("abierto")){
                        Intent n = new Intent(getApplicationContext(), AsistenciaActivity.class);
                        n.putExtra("hora",sgtMarcacion);
                        n.putExtra("tiempo_espera",tiempo_espera);
                        n.putExtra("tiempo_notificacion",tiempo_notificacion);

                        /*SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(Constantes.TIEMPO_NOTIFICACION, tiempo_notificacion);
                        editor.putString(Constantes.HORA_MARCACION, sgtMarcacion);
                        editor.commit();*/



                        //update hora marcacion
                        ContentValues values2 = new ContentValues();
                        values2.put(ContractHoraMarcacion.Columnas.HORA, sgtMarcacion);
                        Log.i(TAG, "Actualizando hora marcacion antes de enviar: " + sgtMarcacion);
                     //   new AsistenciaService.ActualizarHoraMArcacion(getApplicationContext().getContentResolver(), values2, sgtMarcacion).execute(ContractHoraMarcacion.CONTENT_URI);

                        Log.i(TAG, "variables enviadas intent: "+sgtMarcacion+ " t.e= "+ tiempo_espera+" "+ "t.n= "+ tiempo_notificacion+" ");

                        n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(n);


                            /*SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constantes.ACTIVIDAD, "abierto");
                            editor.commit();
                        }*//*else{
                            SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constantes.ACTIVIDAD, "cerrado");
                            editor.commit();
                        }*/


                            //}
                        //}

                        //asistencias.remove(i);

                        //abri dialogo de foto
                        /*if (c>0){
                            Intent n = new Intent(getApplicationContext(), AsistenciaActivity.class);
                            n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(n);
                        }*/
                        //c++;
                    }

                //}
            }

            public void onFinish() {
                Log.i(TAG,"finish contador de 8 horas" + c);
                isTimerRunning = false;
            }
        }.start();

    }

    public static class ActualizarHoraMArcacion extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;
        private final String hora;

        public ActualizarHoraMArcacion(ContentResolver resolver, ContentValues valores, String hora) {
            this.resolver = resolver;
            this.valores = valores;
            this.hora = hora;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Log.i(TAG, "Actualizando tabla local hora marcacion");
            Uri uri = args[0];
            if (null != uri) {
                String selectQuery = ContractHoraMarcacion.Columnas._ID + "!=?"; //condicion innecesaria
                String[] val = new String[]{""};
                resolver.update(uri, valores, selectQuery, val);
            }
            return null;
        }

    }


    private void iniciarTiempoDeEspera(String sgtMarc, String tiempo_espera, String tiempo_notificacion){
        Log.i(TAG, "iniciarTiempoDeEspera, sgt marcacion: " + sgtMarc);

        int tiempo = 1;
        long milisegundos = tiempo * 60 * 1000;

        /*DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        LocalTime t_1 = null;
        LocalTime t_2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            t_1 = LocalTime.parse(tiempo_espera, formatter);
            t_2 = LocalTime.parse(tiempo_notificacion, formatter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime t_fin = h_marcacion.plusSeconds(segundosEspera);
        }*/

        long tiempoEnMilisegundos = Long.parseLong(tiempo_espera) +Long.parseLong(tiempo_notificacion);
        tiempoEnMilisegundos = tiempoEnMilisegundos * 1000;

        countDownTimer2 = new  CountDownTimer(tiempoEnMilisegundos, milisegundos) {

            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "transcurre tiempo de espera total");
            }

            public void onFinish() {
                Log.i(TAG, "finalizo tiempo de espera total " + sgtMarc);
                if(!handler.marcacionEnviada(sgtMarc)){ //si no contesto la marcacion
                    Log.i(TAG, "iniciarTiempoDeEspera, no tiene marcacion: ");
                    insertDataRegistro(latitud,longitud, "NO VALIDADO", sgtMarc);
                    ContentValues values3 = new ContentValues();
                    values3.put(ContractHoraMarcacion.Columnas.HORA, sgtMarcacion);
                //    new AsistenciaService.ActualizarHoraMArcacion(getApplicationContext().getContentResolver(), values3, sgtMarcacion).execute(ContractHoraMarcacion.CONTENT_URI);

                    SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constantes.MARCACION_ANTERIOR, "enviada");
                    editor.commit();
                }
            }

        }.start();
    }

    public boolean insertDataRegistro(double latitud, double longitud, String estado, String hora_marc) {
        try {
            Log.i(TAG, "InsertData registro");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);


            String image = "";


            ContentValues values = new ContentValues();

            values.put(ContractInsertAsistencia.Columnas.IDPDV, pos_id);
            values.put(ContractInsertAsistencia.Columnas.USUARIO, usuario);
            values.put(ContractInsertAsistencia.Columnas.FOTO, image);
            values.put(ContractInsertAsistencia.Columnas.VERSION,getResources().getString(R.string.version));
            values.put(ContractInsertAsistencia.Columnas.LATITUDE, latitud + "");
            values.put(ContractInsertAsistencia.Columnas.LONGITUDE, longitud + "");
            values.put(ContractInsertAsistencia.Columnas.FECHA, fechaser);
            values.put(ContractInsertAsistencia.Columnas.HORA, hora_marc);
            values.put(ContractInsertAsistencia.Columnas.DISTANCIA, distance);
            values.put(ContractInsertAsistencia.Columnas.POS_NAME, pos_name);
            values.put(ContractInsertAsistencia.Columnas.BATERIA, new BatteryLevel().nivelBateria(getApplicationContext()));
            values.put(ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA, estado);
            values.put(ContractInsertAsistencia.Columnas.SUPERVISOR, supervisor);
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            getContentResolver().insert(ContractInsertAsistencia.CONTENT_URI, values);

            ContentValues values2 = new ContentValues();
            values2.put(ContractAsistenciasLocal.Columnas.REGISTRADO, "enviado");
            values2.put(ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION, estado);
            Log.i("AsActivity", "hora marcacion d " + hora_marc);
            new AsistenciaActivity.TareaEditarAsistenciaLocal(getApplicationContext().getContentResolver(), values2, hora_marc).execute(ContractAsistenciasLocal.CONTENT_URI);

            Toast.makeText(this, "Registrada su Asistencia ", Toast.LENGTH_SHORT).show();

            if (VerificarNet.hayConexion(getApplicationContext())) {
                Log.i("Asistencia Actv", "si hay net");
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


    private void showNotification(){

        Intent notificationIntent = new Intent(this, AsistenciaActivity.class);

        notificationIntent.setAction(ACTION_NOTIFICATION_CLICKED);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );


        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Si la versión del SDK es Oreo o superior, utilizar NotificationCompat.Builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                    .setContentTitle("Control de Asistencia")
                    .setContentText("se solicitará validación cada "+ tiempo_base +" minutos.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).setOngoing(true).setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE) // Establecer el sonido
                    //.addAction(0, "validar", pendingIntent)
                    .setVibrate(new long[]{0, 1000, 1000, 1000});
                    //.addAction(R.mipmap.ic_launcher, "Cerrar", dismissPendingIntent); // Agregar botón de acción "Cerrar"


            notification = builder.build();
        } else {
            // Si la versión del SDK es anterior a Oreo, utilizar Notification.Builder directamente
            notification = new Notification.Builder(this)
                    .setContentTitle("Control de Asistencia")
                    .setContentText("se solicitará validación")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    //.addAction(R.mipmap.ic_launcher, "Cerrar", dismissPendingIntent) // Agregar botón de acción "Cerrar"
                    .build();
        }

        startForeground(3, notification);
        Log.i(TAG,"showNotification, timmer is running? " + isTimerRunning);
        if(!isTimerRunning){
            Log.i(TAG, "contador de 8 horas inactivo");

            //if(!handler.getTablaAsistencia()){
            if(siguienteMarcacion == null){

                //SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                //String nueva_hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA,Constantes.NODATA);

                //Log.i("bfbfb" , ""+nueva_hora_entrada);

                //asistencias = handler.getHorasInsert();
            }
            calcularTiempo();
        }

        calcularTiempoInserts(); //calcula la siguiente hora de marcacion

    }

    public void getTiempo(Activity activity, String user, String id_pdv){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", user);
            map.put("id_pdv", id_pdv);
            Log.i("RESPONSE 3 ", "entro 3");
            // Crear nuevo objeto Json basado en el mapa
            JSONObject jobject = new JSONObject(map);

            //GET METHOD
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.GET_EVIDENCIAS, jobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    procesarRespuestaTiempo(response, activity);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.TIME_OUT);
                    } else if (error instanceof NoConnectionError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.NO_RED);
                    }else if (error instanceof AuthFailureError) {
                        //TODO
                    } else if (error instanceof ServerError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.SEVER_ERROR);
                    } else if (error instanceof NetworkError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.RED_ERROR);
                    } else if (error instanceof ParseError) {
                        //TODO
                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }catch (Exception e){
            Log.i("ERROR REQUEST USER DEVICE", e.getMessage());
            e.printStackTrace();
        }
    }

    public void procesarRespuestaTiempo(JSONObject response, Activity activity){
        Log.i("RESPONSE 4 ", "entro 4");
        if (response!=null){
//            List<String> mensajeList = new ArrayList<>();
            Log.i("RESPONSE 5 ", "entro 5");
            try{
                String estado = response.getString("estado");
                Log.i("RESPONSE 5 ", "entro estado"+estado);
                switch (estado){
                    case "1": //EXITO
                        //Toast.makeText(activity, "Se ha enlazado su usuario con este dispositivo", Toast.LENGTH_SHORT).show();
                        JSONArray mensaje = response.getJSONArray("evidencias");
                        Log.i("resultadode",""+mensaje);
                        JSONObject jb1 = mensaje.getJSONObject(0);
                        //String id = jb1.getString("device_id");
                        tiempo_base = jb1.getString("tiempo");
                        Log.i("ttfr",""+tiempo_base);
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON", mensaje2);
                        /*ad.show();
                        valor = false;*/
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA,false);
        hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA,Constantes.NODATA);
        usuario = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        pos_id = sharedPreferences.getString(Constantes.CODIGO,Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);
        distance = sharedPreferences.getString(Constantes.DISTANCIA,Constantes.NODATA);
        pos_name = sharedPreferences.getString(Constantes.PDV,Constantes.NODATA);

    }

    public void vaciarTabla(){
        String selection =  Constantes.PENDIENTE_INSERCION + "=?";
        String[] selectionArgs = new String[]{"1"};
        getContentResolver().delete(ContractAsistenciasLocal.CONTENT_URI,selection,selectionArgs);
    }

    public void cancelarCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private BroadcastReceiver mostrarNotificacionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("Mostrar_Notificacion_Accion".equals(intent.getAction())) {
                // Llama al método showNotification() para mostrar la notificación nuevamente
                Log.i(TAG, "BroadcastReceiver2");
                showNotification();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Detener el servicio correctamente
        Log.i("dccccc","destruyoas");
        stopForegroundNotification();
        // Otro código de limpieza, si es necesario
        cancelarCountDownTimer();
        Log.i("dccccc","destruyo");
        unregisterReceiver(mostrarNotificacionReceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("dccccc","termino tarea servicio");
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean _falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA,false);
        Log.i("astasdek","" + _falta_salida);
        if (!_falta_salida){
            cancelarCountDownTimer();
        }
        //cancelarCountDownTimer();
    }
}