package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProdCaducar;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesProdCad {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_SUBCATEGORIA = 9;
    public static final int COLUMNA_BRAND = 10;
    public static final int COLUMNA_SKU_CODE = 11;
    public static final int COLUMNA_FECHA_PROD = 12;
    public static final int COLUMNA_CANT_PROD = 13;
    public static final int COLUMNA_MANUFACTURER = 14;

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
        String subcategoria;
        String brand;
        String sku_code;
        String fecha_prod;
        String cant_prod;
        String manufacturer;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        brand = c.getString(COLUMNA_BRAND);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        fecha_prod = c.getString(COLUMNA_FECHA_PROD);
        cant_prod = c.getString(COLUMNA_CANT_PROD);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);

        try {
            jObject.put(ContractInsertProdCaducar.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertProdCaducar.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertProdCaducar.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertProdCaducar.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertProdCaducar.Columnas.FECHA, fecha);
            jObject.put(ContractInsertProdCaducar.Columnas.HORA, hora);
            jObject.put(ContractInsertProdCaducar.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertProdCaducar.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertProdCaducar.Columnas.BRAND, brand);
            jObject.put(ContractInsertProdCaducar.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertProdCaducar.Columnas.FECHA_PROD, fecha_prod);
            jObject.put(ContractInsertProdCaducar.Columnas.CANTIDAD_PROD, cant_prod);
            jObject.put(ContractInsertProdCaducar.Columnas.MANUFACTURER, manufacturer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
