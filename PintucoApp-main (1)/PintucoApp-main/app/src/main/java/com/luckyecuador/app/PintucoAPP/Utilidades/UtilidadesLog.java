package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractLog;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesLog {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_USER = 2;
    public static final int COLUMNA_FECHA = 3;
    public static final int COLUMNA_HORA = 4;
    public static final int COLUMNA_ACCION = 5;

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
        String fecha;
        String hora;
        String accion;

        user = c.getString(COLUMNA_USER);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        accion = c.getString(COLUMNA_ACCION);

        try {
            jObject.put(ContractLog.Columnas.USUARIO, user);
            jObject.put(ContractLog.Columnas.FECHA, fecha);
            jObject.put(ContractLog.Columnas.HORA, hora);
            jObject.put(ContractLog.Columnas.ACCION, accion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}