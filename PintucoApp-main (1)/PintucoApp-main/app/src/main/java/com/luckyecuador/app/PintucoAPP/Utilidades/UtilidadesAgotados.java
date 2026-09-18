package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 23/03/2018.
 */

public class UtilidadesAgotados {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_TIEMPO_INICIO = 6;
    public static final int COLUMNA_TIEMPO_FIN = 7;
    public static final int COLUMNA_FECHA = 8;
    public static final int COLUMNA_HORA = 9;

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

         String pharma_id;
         String codigo;
         String usuario;
         String supervisor;
         String tiempo_inicio;
         String tiempo_fin;
         String fecha;
         String hora;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        tiempo_inicio = c.getString(COLUMNA_TIEMPO_INICIO);
        tiempo_fin = c.getString(COLUMNA_TIEMPO_FIN);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {

            jObject.put(ContractInsertAgotados.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertAgotados.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertAgotados.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertAgotados.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertAgotados.Columnas.TIEMPO_INICIO, tiempo_inicio);
            jObject.put(ContractInsertAgotados.Columnas.TIEMPO_FIN, tiempo_fin);
            jObject.put(ContractInsertAgotados.Columnas.FECHA, fecha);
            jObject.put(ContractInsertAgotados.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
