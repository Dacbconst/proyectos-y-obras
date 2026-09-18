package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesEjecucionMateriales {

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
    public static final int COLUMNA_TIPO = 19;
    public static final int COLUMNA_MATERIAL = 20;
    public static final int COLUMNA_ESTADO = 21;
    public static final int COLUMNA_PRIORIDAD = 22;
    public static final int COLUMNA_OBSERVACIONES = 23;
    public static final int COLUMNA_FOTO = 24;
    public static final int COLUMNA_FECHA = 25;
    public static final int COLUMNA_HORA = 26;

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
        String tipo;
        String material;
        String estado;
        String prioridad;
        String observaciones;
        String foto;
        String fecha;
        String hora;

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
        tipo = c.getString(COLUMNA_TIPO);
        material = c.getString(COLUMNA_MATERIAL);
        estado = c.getString(COLUMNA_ESTADO);
        prioridad = c.getString(COLUMNA_PRIORIDAD);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);
        foto = c.getString(COLUMNA_FOTO);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);


        try {
            jObject.put(ContractInsertEjecucionMateriales.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.CANAL, canal);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.NOMBRE_COMERCIAL, nombre_comercial);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.LOCAL, local);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.REGION, region);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.PROVINCIA, provincia);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.CIUDAD, ciudad);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.ZONA, zona);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.TERRITORIO, territorio);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.ZONA_TERRITORIO, zona_territorio);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.TIPO, tipo);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.MATERIAL, material);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.ESTADO_MATERIAL, estado);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.PRIORIDAD, prioridad);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.OBSERVACIONES, observaciones);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.FOTO, foto);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.FECHA, fecha);
            jObject.put(ContractInsertEjecucionMateriales.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
