package com.luckyecuador.app.PintucoAPP;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lucky Ecuador on 04/08/2017.
 */

public class TimeChangeBroadcastReceiver extends BroadcastReceiver {

    private String fecha, hora;

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        Log.i("ACTION E. ",action);

        if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                action.equals(Intent.ACTION_TIMEZONE_CHANGED) ||
                action.equals(Intent.ACTION_DATE_CHANGED)) {
            obtenerFecha();

            SharedPreferences sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

            boolean var = isTimeAutomatic(context);
            String autotime = String.valueOf(var);
            Log.i("AUTOTIME", autotime);
            String descripcion = "El gestor cambió la obtención de fecha/hora a AUTOTIME";
            if (!var) {
                descripcion = descripcion.replace("AUTOTIME", "manual");
            } else {
                descripcion = descripcion.replace("AUTOTIME", "automático");
            }

            Toast.makeText(context, "Prueba1234", Toast.LENGTH_SHORT).show();

            ContentValues values_audit = new ContentValues();
            values_audit.put(ContractNotificacion.Columnas.USER, user);
            values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
            values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
            values_audit.put(ContractNotificacion.Columnas.HORA, hora);
            values_audit.put(Constantes.PENDIENTE_INSERCION, 1);

            context.getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
            SyncAdapter.sincronizarAhora(context, true, Constantes.insertNot, null);
        } else if(action.equals(Intent.ACTION_BATTERY_LOW)){
            Log.i("BATTERY","NIVEL DE BATERIA BAJO");
            SharedPreferences sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        }
    }


    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public void obtenerFecha(){
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
