package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesSugeridos {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_LOCAL = 8;
    public static final int COLUMNA_CODIGO_FABRIL = 9;
    public static final int COLUMNA_VENDEDOR_FABRIL = 10;
    public static final int COLUMNA_CATEGORIA = 11;
    public static final int COLUMNA_SUBCATEGORIA = 12;
    public static final int COLUMNA_BRAND = 13;
    public static final int COLUMNA_SKU_CODE = 14;
    public static final int COLUMNA_QUIEBRE = 15;
    public static final int COLUMNA_UNIDAD_DISPONIBLE = 16;
    public static final int COLUMNA_SUGERIDO = 17;
    public static final int COLUMNA_CANTIDAD = 18;
    public static final int COLUMNA_OBSERVACIONES = 19;
    public static final int COLUMNA_ENTREGA = 20;

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
        String local;
        String codigo_fabril;
        String vendedor_fabril;
        String categoria;
        String subcategoria;
        String brand;
        String sku_code;
        String quiebre;
        String unidad_disponible;
        String sugerido;
        String cantidad;
        String observaciones;
        String entrega;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        local = c.getString(COLUMNA_LOCAL);
        codigo_fabril = c.getString(COLUMNA_CODIGO_FABRIL);
        vendedor_fabril = c.getString(COLUMNA_VENDEDOR_FABRIL);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        brand = c.getString(COLUMNA_BRAND);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        quiebre = c.getString(COLUMNA_QUIEBRE);
        unidad_disponible = c.getString(COLUMNA_UNIDAD_DISPONIBLE);
        sugerido = c.getString(COLUMNA_SUGERIDO);
        cantidad = c.getString(COLUMNA_CANTIDAD);
        observaciones = c.getString(COLUMNA_OBSERVACIONES);
        entrega = c.getString(COLUMNA_ENTREGA);

        try {
            jObject.put(ContractInsertSugeridos.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertSugeridos.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertSugeridos.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertSugeridos.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertSugeridos.Columnas.FECHA, fecha);
            jObject.put(ContractInsertSugeridos.Columnas.HORA, hora);
            jObject.put(ContractInsertSugeridos.Columnas.LOCAL, local);
            jObject.put(ContractInsertSugeridos.Columnas.CODIGO_FABRIL, codigo_fabril);
            jObject.put(ContractInsertSugeridos.Columnas.VENDEDOR_FABRIL, vendedor_fabril);
            jObject.put(ContractInsertSugeridos.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertSugeridos.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertSugeridos.Columnas.BRAND, brand);
            jObject.put(ContractInsertSugeridos.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertSugeridos.Columnas.QUIEBRE, quiebre);
            jObject.put(ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE, unidad_disponible);
            jObject.put(ContractInsertSugeridos.Columnas.SUGERIDO, sugerido);
            jObject.put(ContractInsertSugeridos.Columnas.CANTIDAD, cantidad);
            jObject.put(ContractInsertSugeridos.Columnas.OBSERVACIONES, observaciones);
            jObject.put(ContractInsertSugeridos.Columnas.ENTREGA, entrega);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}

