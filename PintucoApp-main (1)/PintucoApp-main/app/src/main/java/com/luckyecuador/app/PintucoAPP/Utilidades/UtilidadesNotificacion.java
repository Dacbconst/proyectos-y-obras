package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesNotificacion {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_USER = 2;
    public static final int COLUMNA_SUPERVISOR = 3;
    public static final int COLUMNA_DESCRIPCION = 4;
    public static final int COLUMNA_FECHA = 5;
    public static final int COLUMNA_HORA = 6;

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String user;
        String supervisor;
        String descripcion;
        String fecha;
        String hora;

        user = c.getString(COLUMNA_USER);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        descripcion = c.getString(COLUMNA_DESCRIPCION);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractNotificacion.Columnas.USER, user);
            jObject.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
            jObject.put(ContractNotificacion.Columnas.FECHA, fecha);
            jObject.put(ContractNotificacion.Columnas.HORA, hora);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
