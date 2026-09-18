package com.luckyecuador.app.PintucoAPP;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Utils.AlmuerzoNotificacion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CronometroServicePintuco extends Service {
    private Handler handler = new Handler();
    private DatabaseHelper databaseHelper;
    private long cronoAlmuerzo, cronoTiempoFuera;
    private boolean almuerzoEnCurso = false;
    private boolean cronoTiempoFueraIniciado = false;
    static String user,supervisor;
    private boolean seEnvioNotifFuera = false;
    private long tiempo_almuerzo = 0;
    private long v1,v2;

    private String tiempo_almuerzo_rest,tiempo_fuera_rest,hora_inicio_almuerzo;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();
        //Obtenemos el tiempo de almuerzo
        tiempo_almuerzo = databaseHelper.getTiempoAlmuerzoEnMs();
      //  tiempo_almuerzo = 60000;

        //Obtenemos el supervisor
        supervisor = databaseHelper.getSurpervisor();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Si no ha comenzado el almuerzo
        if (!almuerzoEnCurso) {

            // Iniciamos el cronometro
            cronoAlmuerzo = SystemClock.elapsedRealtime();
            // Se ira actualizando cada sge
            handler.post(actualizarCronometro);
            almuerzoEnCurso = true;

        // Si ha forzado sesión, y el almuerzo esta en curso
        } else {

            // Obtenemos la hora actual
            String horaser = obtenerHora();

            //obtenemos la diferencia en milisegundos entre la hora que inicio el almuerzo y la hora actual
            v1 = restarHoras(hora_inicio_almuerzo,horaser);
            Log.i("almuerzo_Transcurrido en ms",""+v1);
            //Obtenemos el tiempo fuera
            v2 = v1 - tiempo_almuerzo;

            //Obtenemos el tiempo de almuerzo guardado
            cronoAlmuerzo = SystemClock.elapsedRealtime() - v1;
            cronoTiempoFuera = SystemClock.elapsedRealtime() - v2;
        //    if (v1 > 0){
            //    cronoTiempoFuera = v2;
        //    }

        //    Toast.makeText(getApplicationContext(),"inicio",Toast.LENGTH_SHORT).show();

           // cronoTiempoFuera = v2;

            Log.i("crono2",""+v2);

            handler.post(actualizarCronometro);

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(actualizarCronometro);
        almuerzoEnCurso = false;
    //    cronoTiempoFueraIniciado = false;

    }

    public long convertirTiempoAEnMs(String tiempo){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT 5"));
        long milisegundos = 0;

        try {
            // Parsea la hora en string a un objeto Date
            Date hora = sdf.parse(tiempo);

            // Obtiene la representación de la hora en milisegundos
            milisegundos = hora.getTime();

            // Muestra el resultado
            System.out.println("Hora en milisegundos: " + milisegundos);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("error: " + e.getMessage());
        }

        return milisegundos;
    }


    private Runnable actualizarCronometro = new Runnable() {
        public void run() {

         //   Toast.makeText(getApplicationContext(),"activo",Toast.LENGTH_SHORT).show();

            long crono1 = SystemClock.elapsedRealtime() - cronoAlmuerzo;
            long crono2 = SystemClock.elapsedRealtime() - cronoTiempoFuera;

            // Si ya ha pasado el tiempo de almuerzo , se pintara de rojo
            //Y se habilita el segundo cronometro de tiempo fuera

            if (crono1 >= tiempo_almuerzo && !cronoTiempoFueraIniciado) {
              //  cronoTiempoFuera = SystemClock.elapsedRealtime();
              //  cronoTiempoFueraIniciado = true;
              //  guardarSharePref(cronoTiempoFueraIniciado);


                if (!seEnvioNotifFuera){
                    AlmuerzoNotificacion almuerzoNotificacion = new AlmuerzoNotificacion(getApplicationContext());
                    almuerzoNotificacion.enviarNotificacion(user, "TIEMPO_FUERA_ALMUERZO", supervisor);
                    guardarSharePref(Constantes.NOTIF_ALMUERZO_FUERA,true);
                    seEnvioNotifFuera = true;
                }


            }



            //Cuando se haya pasado el tiempo de almuerzo

            /*
            if (cronoTiempoFueraIniciado) {
                crono2 =  cronoTiempoFuera;
            }*/


            Intent intent = new Intent("actualizar_cronometro");
            intent.putExtra("crono1", crono1);
            intent.putExtra("crono2", crono2);
            intent.putExtra("almuerzoEnCurso", almuerzoEnCurso);
            sendBroadcast(intent);


            sendBroadcast(intent);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        hora_inicio_almuerzo = sharedPreferences.getString(Constantes.HORA_INICIO_ALMUERZO,Constantes.NODATA);
      //  tiempo_almuerzo_rest = sharedPreferences.getString(Constantes.TIEMPO_ALMUERZO, Constantes.NODATA);
      //  tiempo_fuera_rest = sharedPreferences.getString(Constantes.TIEMPO_FUERA, Constantes.NODATA);
        almuerzoEnCurso = sharedPreferences.getBoolean(Constantes.ALMUERZO_EN_CURSO, false);
        seEnvioNotifFuera = sharedPreferences.getBoolean(Constantes.NOTIF_ALMUERZO_FUERA,false);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
       // cronoTiempoFueraIniciado = sharedPreferences.getBoolean(Constantes.FUERO_TIEMPO_ACTIVO,false);


    }

    public long restarHoras(String hora_inicio_almuerzo,String hora_actual){

        // Formato para parsear las horas
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long diferenciaEnMilisegundos = 0;

                try {
                    // Parsear los strings a objetos Date
                    Date date1 = sdf.parse(hora_inicio_almuerzo);
                    Date date2 = sdf.parse(hora_actual);

                    // Restar las fechas
                    diferenciaEnMilisegundos = date2.getTime() - date1.getTime();

                    Log.i("hora_inicio_almuerzo: ",hora_inicio_almuerzo);
                    Log.i("hora_actual: ",hora_actual);

                    // Imprimir el resultado
                    Log.d("Resultado", "La diferencia en horas en milisegundos es: " + diferenciaEnMilisegundos);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            return diferenciaEnMilisegundos;
        }

        public String obtenerHora(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            return hour.format(currentLocalTime);
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




}