package com.luckyecuador.app.PintucoAPP;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class BatteryLevelReceiver extends BroadcastReceiver {

    private String fecha, hora, descripcion;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(Intent.ACTION_BATTERY_LOW)) {
            obtenerFecha();
            SharedPreferences sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
            String supervisor = sharedPreferences.getString(Constantes.SUPERVISOR, Constantes.NODATA);

            descripcion = "Gestor con nivel de bateria bajo";
            Log.i("BATTERY LEVEL ", descripcion);
            Toast.makeText(context, "Nivel de bateria bajo, conecte su cargador", Toast.LENGTH_SHORT).show();

            ContentValues values_audit = new ContentValues();
            values_audit.put(ContractNotificacion.Columnas.USER, user);
            values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
            values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
            values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
            values_audit.put(ContractNotificacion.Columnas.HORA, hora);
            values_audit.put(Constantes.PENDIENTE_INSERCION, 1);

            context.getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
            SyncAdapter.sincronizarAhora(context, true, Constantes.insertNot, null);
        }
    }

    private void obtenerFecha() {
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
