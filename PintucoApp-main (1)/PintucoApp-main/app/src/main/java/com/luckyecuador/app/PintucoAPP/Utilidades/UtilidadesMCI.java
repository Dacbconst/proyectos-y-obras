package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesMCI {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_CANAL = 4;
    public static final int COLUMNA_NOMBRE_COMERCIAL = 5;
    public static final int COLUMNA_LOCAL = 6;
    public static final int COLUMNA_REGION = 7;
    public static final int COLUMNA_PROVINCIA = 8;
    public static final int COLUMNA_CIUDAD = 9;
    public static final int COLUMNA_ZONA = 10;
    public static final int COLUMNA_DIRECCION = 11;
    public static final int COLUMNA_SUPERVISOR = 12;
    public static final int COLUMNA_MERCADERISTA = 13;
    public static final int COLUMNA_USUARIO = 14;
    public static final int COLUMNA_LATITUD = 15;
    public static final int COLUMNA_LONGITUD = 16;
    public static final int COLUMNA_TERRITORIO = 17;
    public static final int COLUMNA_ZONA_TERRITORIO = 18;
    public static final int COLUMNA_CAUSAL = 19;
    public static final int COLUMNA_OBSERVACIONES = 20;
    public static final int COLUMNA_FOTO = 21;
    public static final int COLUMNA_FECHA = 22;
    public static final int COLUMNA_HORA = 23;
    public static final int COLUMNA_POS_NAME = 24;

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
        String canal;
        String nombre_comercial;
        String local;
        String region;
        String provincia;
        String ciudad;
        String zona;
        String direccion;
        String supervisor;
        String mercaderista;
        String usuario;
        String latitud;
        String longitud;
        String territorio;
        String zona_territorio;
        String causal;
        String observaciones;
        String foto;
        String fecha;
        String hora;
        String pos_name;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        canal = c.getString(COLUMNA_CANAL);
        nombre_comercial = c.getString(COLUMNA_NOMBRE_COMERCIAL);
        local = c.getString(COLUMNA_LOCAL);
        region = c.getString(COLUMNA_REGION);
        provincia = c.getString(COLUMNA_PROVINCIA);
        ciudad = c.getString(COLUMNA_CIUDAD);
        zona = c.getString(COLUMNA_ZONA);
        direccion = c.getString(COLUMNA_DIRECCION);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        mercaderista = c.getString(COLUMNA_MERCADERISTA);
        usuario = c.getString(COLUMNA_USUARIO);
        latitud = c.getString(COLUMNA_LATITUD);
        longitud = c.getString(COLUMNA_LONGITUD);
        territorio = c.getString(COLUMNA_TERRITORIO);
        zona_territorio = c.getString(COLUMNA_ZONA_TERRITORIO);
        causal = c.getString(COLUMNA_CAUSAL);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);
        foto = c.getString(COLUMNA_FOTO);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        pos_name = c.getString(COLUMNA_POS_NAME);


        try {
            jObject.put(ContractInsertMCIPdv.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertMCIPdv.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertMCIPdv.Columnas.CANAL, canal);
            jObject.put(ContractInsertMCIPdv.Columnas.NOMBRE_COMERCIAL, nombre_comercial);
            jObject.put(ContractInsertMCIPdv.Columnas.LOCAL, local);
            jObject.put(ContractInsertMCIPdv.Columnas.REGION, region);
            jObject.put(ContractInsertMCIPdv.Columnas.PROVINCIA, provincia);
            jObject.put(ContractInsertMCIPdv.Columnas.CIUDAD, ciudad);
            jObject.put(ContractInsertMCIPdv.Columnas.ZONA, zona);
            jObject.put(ContractInsertMCIPdv.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertMCIPdv.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertMCIPdv.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertMCIPdv.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertMCIPdv.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertMCIPdv.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertMCIPdv.Columnas.TERRITORIO, territorio);
            jObject.put(ContractInsertMCIPdv.Columnas.ZONA_TERRITORIO, zona_territorio);
            jObject.put(ContractInsertMCIPdv.Columnas.CAUSAL, causal);
            jObject.put(ContractInsertMCIPdv.Columnas.OBSERVACIONES, observaciones);
            jObject.put(ContractInsertMCIPdv.Columnas.FOTO, foto);
            jObject.put(ContractInsertMCIPdv.Columnas.FECHA, fecha);
            jObject.put(ContractInsertMCIPdv.Columnas.HORA, hora);
            jObject.put(ContractInsertMCIPdv.Columnas.POS_NAME, pos_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
