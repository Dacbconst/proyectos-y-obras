package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesAlmuerzo {

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_USUARIO = 2;
    public static final int COLUMNA_JORNADA_LABORAL = 3;
    public static final int COLUMNA_TIEMPO_ALMUERZO = 4;
    public static final int COLUMNA_TIEMPO_FUERA = 5;
    public static final int COLUMNA_FOTO = 6;
    public static final int COLUMNA_LATITUD = 7;
    public static final int COLUMNA_LONGITUD = 8;
    public static final int COLUMNA_FECHA = 9;
    public static final int COLUMNA_HORA_INI_ALMUERZO = 10;
    public static final int COLUMNA_HORA_FIN_ALMUERZO = 11;

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


        String usuario;
        String jornada_laboral;
        String tiempo_almuerzo;
        String tiempo_fuera;
        String foto;
        String latitud;
        String longitud;
        String fecha;
        String hora_ini_almuerzo;
        String hora_fin_almuerzo;


        usuario = c.getString(COLUMNA_USUARIO);
        jornada_laboral = c.getString(COLUMNA_JORNADA_LABORAL);
        tiempo_almuerzo = c.getString(COLUMNA_TIEMPO_ALMUERZO);
        tiempo_fuera = c.getString(COLUMNA_TIEMPO_FUERA);
        foto = c.getString(COLUMNA_FOTO);
        latitud = c.getString(COLUMNA_LATITUD);
        longitud = c.getString(COLUMNA_LONGITUD);
        fecha = c.getString(COLUMNA_FECHA);
        hora_ini_almuerzo = c.getString(COLUMNA_HORA_INI_ALMUERZO);
        hora_fin_almuerzo = c.getString(COLUMNA_HORA_FIN_ALMUERZO);

        try {


            jObject.put(ContractInsertAlmuerzo.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertAlmuerzo.Columnas.JORNADA_LABORAL, jornada_laboral);
            jObject.put(ContractInsertAlmuerzo.Columnas.TIEMPO_ALMUERZO, tiempo_almuerzo);
            jObject.put(ContractInsertAlmuerzo.Columnas.TIEMPO_FUERA, tiempo_fuera);
            jObject.put(ContractInsertAlmuerzo.Columnas.FOTO, foto);
            jObject.put(ContractInsertAlmuerzo.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertAlmuerzo.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertAlmuerzo.Columnas.FECHA, fecha);
            jObject.put(ContractInsertAlmuerzo.Columnas.HORA_INI_ALMUERZO, hora_ini_almuerzo);
            jObject.put(ContractInsertAlmuerzo.Columnas.HORA_FIN_ALMUERZO, hora_fin_almuerzo);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
