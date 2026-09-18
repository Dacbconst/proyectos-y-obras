package com.luckyecuador.app.PintucoAPP.Utils;

import android.content.ContentValues;
import android.content.Context;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GuardarLog {

    Context context;

    public GuardarLog(Context context) {
        this.context = context;
    }

    public void saveLog(String usuario, String codigo, String accion) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        if (!codigo.trim().isEmpty()) {
            accion = accion + " - " + codigo;
        }
        ContentValues values = new ContentValues();

        values.put(ContractLog.Columnas.USUARIO, usuario);
        values.put(ContractLog.Columnas.ACCION, accion);
        values.put(ContractLog.Columnas.FECHA, fechaser);
        values.put(ContractLog.Columnas.HORA, horaser);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        context.getContentResolver().insert(ContractLog.CONTENT_URI, values);
    }

}
