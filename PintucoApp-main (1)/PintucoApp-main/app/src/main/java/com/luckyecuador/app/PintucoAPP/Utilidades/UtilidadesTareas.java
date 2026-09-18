package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertTareas;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesTareas {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CHANNEL = 8;
    public static final int COLUMNA_CODIGOPDV = 9;
    public static final int COLUMNA_MERCADERISTA = 10;
    public static final int COLUMNA_TAREAS = 11;
    public static final int COLUMNA_REALIZADO = 12;
    public static final int COLUMNA_FOTO = 13;

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
        String fecha;
        String hora;
        String canal;
        String codigopdv;
        String mercaderista;
        String tareas;
        String realizado;
        String foto;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        canal = c.getString(COLUMNA_CHANNEL);
        codigopdv = c.getString(COLUMNA_CODIGOPDV);
        mercaderista = c.getString(COLUMNA_MERCADERISTA);
        tareas = c.getString(COLUMNA_TAREAS);
        realizado = c.getString(COLUMNA_REALIZADO);
        foto = c.getString(COLUMNA_FOTO);

        try {
            jObject.put(ContractInsertTareas.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertTareas.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertTareas.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertTareas.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertTareas.Columnas.FECHA, fecha);
            jObject.put(ContractInsertTareas.Columnas.HORA, hora);
            jObject.put(ContractInsertTareas.Columnas.CHANNEL, canal);
            jObject.put(ContractInsertTareas.Columnas.CODIGOPDV, codigopdv);
            jObject.put(ContractInsertTareas.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertTareas.Columnas.TAREAS, tareas);
            jObject.put(ContractInsertTareas.Columnas.REALIZADO, realizado);
            jObject.put(ContractInsertTareas.Columnas.FOTO, foto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
