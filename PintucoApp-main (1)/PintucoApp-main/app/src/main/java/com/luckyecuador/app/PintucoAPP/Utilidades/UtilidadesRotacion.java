package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesRotacion {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_PRODUCTO= 9;
    public static final int COLUMNA_PROMOCIONAL = 10;
    public static final int COLUMNA_MECANICA = 11;
    public static final int COLUMNA_PESO = 12;
    public static final int COLUMNA_CANTIDAD = 13;
    public static final int COLUMNA_FECHA_ROT = 14;
    public static final int COLUMNA_FOTO_GUIA = 15;
    public static final int COLUMNA_OBSERVACIONES = 16;

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
        String categoria;
        String producto;
        String promocional;
        String mecanica;
        String peso;
        String cantidad;
        String fecha_rot;
        String foto_guia;
        String observaciones;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        producto = c.getString(COLUMNA_PRODUCTO);
        promocional = c.getString(COLUMNA_PROMOCIONAL);
        mecanica = c.getString(COLUMNA_MECANICA);
        peso = c.getString(COLUMNA_PESO);
        cantidad = c.getString(COLUMNA_CANTIDAD);
        fecha_rot = c.getString(COLUMNA_FECHA_ROT);
        foto_guia = c.getString(COLUMNA_FOTO_GUIA);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);

        try {
            jObject.put(ContractInsertRotacion.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertRotacion.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertRotacion.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertRotacion.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertRotacion.Columnas.FECHA, fecha);
            jObject.put(ContractInsertRotacion.Columnas.HORA, hora);
            jObject.put(ContractInsertRotacion.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertRotacion.Columnas.PRODUCTO, producto);
            jObject.put(ContractInsertRotacion.Columnas.PROMOCIONAL, promocional);
            jObject.put(ContractInsertRotacion.Columnas.MECANICA, mecanica);
            jObject.put(ContractInsertRotacion.Columnas.PESO, peso);
            jObject.put(ContractInsertRotacion.Columnas.CANTIDAD, cantidad);
            jObject.put(ContractInsertRotacion.Columnas.FECHA_ROT, fecha_rot);
            jObject.put(ContractInsertRotacion.Columnas.FOTO_GUIA, foto_guia);
            jObject.put(ContractInsertRotacion.Columnas.OBSERVACIONES, observaciones);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
