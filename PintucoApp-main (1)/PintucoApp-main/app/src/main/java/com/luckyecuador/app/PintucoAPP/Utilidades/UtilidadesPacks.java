package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPacks;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesPacks {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_SUBCATEGORIA = 9;
    public static final int COLUMNA_PRESENTACION = 10;
    public static final int COLUMNA_BRAND = 11;
    public static final int COLUMNA_SKU_CODE = 12;
    public static final int COLUMNA_OBSERVACION = 13;
    public static final int COLUMNA_FOTO = 14;
    public static final int COLUMNA_MANUFACTURER = 15;

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
        String presentacion;
        String brand;
        String sku_code;
        String observacion;
        String foto;
        String manufacturer;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        presentacion = c.getString(COLUMNA_PRESENTACION);
        brand = c.getString(COLUMNA_BRAND);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        observacion = c.getString(COLUMNA_OBSERVACION);
        foto = c.getString(COLUMNA_FOTO);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);

        try {

            jObject.put(ContractInsertPacks.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertPacks.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertPacks.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPacks.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertPacks.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPacks.Columnas.HORA, hora);
            jObject.put(ContractInsertPacks.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertPacks.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertPacks.Columnas.PRESENTACION, presentacion);
            jObject.put(ContractInsertPacks.Columnas.BRAND, brand);
            jObject.put(ContractInsertPacks.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertPacks.Columnas.OBSERVACION, observacion);
            jObject.put(ContractInsertPacks.Columnas.FOTO, foto);
            jObject.put(ContractInsertPacks.Columnas.MANUFACTURER, manufacturer);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
