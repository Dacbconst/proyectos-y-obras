package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMaterialesRecibidos;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesMaterialesRecibidos {

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
    public static final int COLUMNA_ALERTA = 19;
    public static final int COLUMNA_TIPO = 20;
    public static final int COLUMNA_MATERIAL = 21;
    public static final int COLUMNA_CANTIDAD = 22;
    public static final int COLUMNA_ESTADO_MATERIAL = 23;
    public static final int COLUMNA_PRIORIDAD = 24;
    public static final int COLUMNA_OBSERVACIONES = 25;
    public static final int COLUMNA_FOTO = 26;
    public static final int COLUMNA_FECHA = 27;
    public static final int COLUMNA_HORA = 28;

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
        String alerta;
        String tipo;
        String material;
        String cantidad;
        String estado_material;
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
        alerta = c.getString(COLUMNA_ALERTA);
        tipo = c.getString(COLUMNA_TIPO);
        material = c.getString(COLUMNA_MATERIAL);
        cantidad = c.getString(COLUMNA_CANTIDAD);
        estado_material = c.getString(COLUMNA_ESTADO_MATERIAL);
        prioridad = c.getString(COLUMNA_PRIORIDAD);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);
        foto = c.getString(COLUMNA_FOTO);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);


        try {
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.CANAL, canal);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.NOMBRE_COMERCIAL, nombre_comercial);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.LOCAL, local);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.REGION, region);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.PROVINCIA, provincia);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.CIUDAD, ciudad);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.ZONA, zona);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.TERRITORIO, territorio);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.ZONA_TERRITORIO, zona_territorio);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.ALERTA, alerta);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.TIPO, tipo);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.MATERIAL, material);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.CANTIDAD, cantidad);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.ESTADO_MATERIAL, estado_material);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.PRIORIDAD, prioridad);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.OBSERVACIONES, observaciones);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.FOTO, foto);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.FECHA, fecha);
            jObject.put(ContractInsertMaterialesRecibidos.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
