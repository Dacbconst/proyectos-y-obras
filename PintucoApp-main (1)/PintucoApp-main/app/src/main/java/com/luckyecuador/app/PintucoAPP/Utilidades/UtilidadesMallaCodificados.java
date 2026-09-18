package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesMallaCodificados {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_SECTOR = 8;
    public static final int COLUMNA_CATEGORIA = 9;
    public static final int COLUMNA_SUBCATEGORIA = 10;
    public static final int COLUMNA_SEGMENTO1 = 11;
    public static final int COLUMNA_BRAND = 12;
    public static final int COLUMNA_SKU_CODE = 13;
    public static final int COLUMNA_CODIFICA = 14;
    public static final int COLUMNA_OBSERVACION = 15;
    public static final int COLUMNA_MANUFACTURER = 16;
    public static final int COLUMNA_POS_NAME = 17;

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
        String sector;
        String categoria;
        String subcategoria;
        String segment1;
        String brand;
        String sku_code;
        String codifica;
        String observacion;
        String manufacturer;
        String pos_name;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        sector = c.getString(COLUMNA_SECTOR);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        segment1 = c.getString(COLUMNA_SEGMENTO1);
        brand = c.getString(COLUMNA_BRAND);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        codifica = c.getString(COLUMNA_CODIFICA);
        observacion = c.getString(COLUMNA_OBSERVACION);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);
        pos_name = c.getString(COLUMNA_POS_NAME);

        try {
            jObject.put(ContractInsertMallaCodificados.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertMallaCodificados.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertMallaCodificados.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertMallaCodificados.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertMallaCodificados.Columnas.FECHA, fecha);
            jObject.put(ContractInsertMallaCodificados.Columnas.HORA, hora);
            jObject.put(ContractInsertMallaCodificados.Columnas.SECTOR, sector);
            jObject.put(ContractInsertMallaCodificados.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertMallaCodificados.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertMallaCodificados.Columnas.SEGMENTO1, segment1);
            jObject.put(ContractInsertMallaCodificados.Columnas.BRAND, brand);
            jObject.put(ContractInsertMallaCodificados.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertMallaCodificados.Columnas.CODIFICA, codifica);
            jObject.put(ContractInsertMallaCodificados.Columnas.OBSERVACION, observacion);
            jObject.put(ContractInsertMallaCodificados.Columnas.MANUFACTURER, manufacturer);
            jObject.put(ContractInsertMallaCodificados.Columnas.POS_NAME, pos_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
