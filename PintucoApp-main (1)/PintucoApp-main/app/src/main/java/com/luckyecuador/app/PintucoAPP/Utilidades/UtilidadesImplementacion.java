package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesImplementacion {

    public static final int COLUMNA_USUARIO = 2;
    public static final int COLUMNA_FECHA=3;
    public static final int COLUMNA_HORA=4;
    public static final int COLUMNA_CIUDAD = 5;
    public static final int COLUMNA_CANAL = 6;
    public static final int COLUMNA_CLIENTE = 7;
    public static final int COLUMNA_FORMATO = 8;
    public static final int COLUMNA_ZONA = 9;
    public static final int COLUMNA_PDV = 10;
    public static final int COLUMNA_DIRECCION = 11;
    public static final int COLUMNA_LOCAL = 12;
    public static final int COLUMNA_LATITUD = 13;
    public static final int COLUMNA_LONGITUD = 14;
    public static final int COLUMNA_FOTO = 15;

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
        String fecha;
        String hora;
        String ciudad;
        String canal;
        String cliente;
        String formato;
        String zona;
        String pdv;
        String direccion;
        String local;
        String latitud;
        String longitud;
        String foto;

        usuario = c.getString(COLUMNA_USUARIO);
       fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        ciudad = c.getString(COLUMNA_CIUDAD);
        canal = c.getString(COLUMNA_CANAL);
        cliente = c.getString(COLUMNA_CLIENTE);
        formato = c.getString(COLUMNA_FORMATO);
        zona = c.getString(COLUMNA_ZONA);
        pdv = c.getString(COLUMNA_PDV);
        direccion = c.getString(COLUMNA_DIRECCION);
        local = c.getString(COLUMNA_LOCAL);
        latitud = c.getString(COLUMNA_LATITUD);
        longitud = c.getString(COLUMNA_LONGITUD);
        foto = c.getString(COLUMNA_FOTO);

        try {
            jObject.put(ContractInsertImplementacion.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertImplementacion.Columnas.FECHA, fecha);
            jObject.put(ContractInsertImplementacion.Columnas.HORA, hora);
            jObject.put(ContractInsertImplementacion.Columnas.CIUDAD, ciudad);
            jObject.put(ContractInsertImplementacion.Columnas.CANAL, canal);
            jObject.put(ContractInsertImplementacion.Columnas.CLIENTE, cliente);
            jObject.put(ContractInsertImplementacion.Columnas.FORMATO, formato);
            jObject.put(ContractInsertImplementacion.Columnas.ZONA, zona);
            jObject.put(ContractInsertImplementacion.Columnas.PDV, pdv);
            jObject.put(ContractInsertImplementacion.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertImplementacion.Columnas.LOCAL, local);
            jObject.put(ContractInsertImplementacion.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertImplementacion.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertImplementacion.Columnas.FOTO, foto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
