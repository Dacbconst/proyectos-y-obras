package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesCanjes {

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
    public static final int COLUMNA_CATEGORIA = 19;
    public static final int COLUMNA_SUBCATEGORIA = 20;
    public static final int COLUMNA_MARCA = 21;
    public static final int COLUMNA_PRODUCTO = 22;
    public static final int COLUMNA_TIPO_COMBO = 23;
    public static final int COLUMNA_MECANICA = 24;
    public static final int COLUMNA_COMBOS_ARMADOS = 25;
    public static final int COLUMNA_STOCK = 26;
    public static final int COLUMNA_PVC_COMBO = 27;
    public static final int COLUMNA_PVC_UNITARIO = 28;
    public static final int COLUMNA_VISITA = 29;
    public static final int COLUMNA_MES = 30;
    public static final int COLUMNA_OBSERVACIONES = 31;
    public static final int COLUMNA_FOTO = 32;
    public static final int COLUMNA_FOTO_GUIA = 33;
    public static final int COLUMNA_FECHA = 34;
    public static final int COLUMNA_HORA = 35;
    public static final int COLUMNA_POS_NAME = 36;
    public static final int COLUMNA_PLATAFORMA = 37;

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
        String categoria;
        String subcategoria;
        String marca;
        String producto;
        String tipo_combo;
        String mecanica;
        String combos_armados;
        String stock;
        String pvc_combo;
        String pvc_unitario;
        String visita;
        String mes;
        String observaciones;
        String foto;
        String foto_guia;
        String fecha;
        String hora;
        String pos_name;
        String plataforma;

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
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        marca = c.getString(COLUMNA_MARCA);
        producto = c.getString(COLUMNA_PRODUCTO);
        tipo_combo = c.getString(COLUMNA_TIPO_COMBO);
        mecanica = c.getString(COLUMNA_MECANICA);
        combos_armados = c.getString(COLUMNA_COMBOS_ARMADOS);
        stock = c.getString(COLUMNA_STOCK);
        pvc_combo = c.getString(COLUMNA_PVC_COMBO);
        pvc_unitario = c.getString(COLUMNA_PVC_UNITARIO);
        visita = c.getString(COLUMNA_VISITA);
        mes = c.getString(COLUMNA_MES);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);
        foto = c.getString(COLUMNA_FOTO);
        foto_guia = c.getString(COLUMNA_FOTO_GUIA);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertCanjes.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertCanjes.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertCanjes.Columnas.CANAL, canal);
            jObject.put(ContractInsertCanjes.Columnas.NOMBRE_COMERCIAL, nombre_comercial);
            jObject.put(ContractInsertCanjes.Columnas.LOCAL, local);
            jObject.put(ContractInsertCanjes.Columnas.REGION, region);
            jObject.put(ContractInsertCanjes.Columnas.PROVINCIA, provincia);
            jObject.put(ContractInsertCanjes.Columnas.CIUDAD, ciudad);
            jObject.put(ContractInsertCanjes.Columnas.ZONA, zona);
            jObject.put(ContractInsertCanjes.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertCanjes.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertCanjes.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertCanjes.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertCanjes.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertCanjes.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertCanjes.Columnas.TERRITORIO, territorio);
            jObject.put(ContractInsertCanjes.Columnas.ZONA_TERRITORIO, zona_territorio);
            jObject.put(ContractInsertCanjes.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertCanjes.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertCanjes.Columnas.MARCA, marca);
            jObject.put(ContractInsertCanjes.Columnas.PRODUCTO, producto);
            jObject.put(ContractInsertCanjes.Columnas.TIPO_COMBO, tipo_combo);
            jObject.put(ContractInsertCanjes.Columnas.MECANICA, mecanica);
            jObject.put(ContractInsertCanjes.Columnas.COMBOS_ARMADOS, combos_armados);
            jObject.put(ContractInsertCanjes.Columnas.STOCK, stock);
            jObject.put(ContractInsertCanjes.Columnas.PVC_COMBO, pvc_combo);
            jObject.put(ContractInsertCanjes.Columnas.PVC_UNITARIO, pvc_unitario);
            jObject.put(ContractInsertCanjes.Columnas.VISITA, visita);
            jObject.put(ContractInsertCanjes.Columnas.MES, mes);
            jObject.put(ContractInsertCanjes.Columnas.OBSERVACIONES, observaciones);
            jObject.put(ContractInsertCanjes.Columnas.FOTO, foto);
            jObject.put(ContractInsertCanjes.Columnas.FOTO_GUIA, foto_guia);
            jObject.put(ContractInsertCanjes.Columnas.FECHA, fecha);
            jObject.put(ContractInsertCanjes.Columnas.HORA, hora);
            jObject.put(ContractInsertCanjes.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertCanjes.Columnas.PLATAFORMA, plataforma);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
