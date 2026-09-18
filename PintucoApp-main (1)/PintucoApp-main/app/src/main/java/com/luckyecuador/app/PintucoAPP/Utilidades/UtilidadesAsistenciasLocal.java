package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesAsistenciasLocal {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_HORA = 2;
    public static final int COLUMNA_REGISTRADO= 3;
    public static final int COLUMNA_ESTADO_VALIDACION= 4;


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
        String hora;
        String registrado;
        String estado_validacion;


        hora=c.getString(COLUMNA_HORA);
        registrado=c.getString(COLUMNA_REGISTRADO);
        estado_validacion=c.getString(COLUMNA_ESTADO_VALIDACION);

        try {
            jObject.put(ContractAsistenciasLocal.Columnas.HORA,hora);
            jObject.put(ContractAsistenciasLocal.Columnas.REGISTRADO,registrado);
            jObject.put(ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION,estado_validacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
