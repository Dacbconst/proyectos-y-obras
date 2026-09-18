package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesPrecios {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_SUBCATEGORIA = 9;
    public static final int COLUMNA_PRESENTACION = 10;
    public static final int COLUMNA_POFERTA = 11;
    public static final int COLUMNA_BRAND = 12;
    /*public static final int COLUMNA_TAMANO = 13;
    public static final int COLUMNA_CANTIDAD = 14;*/
    public static final int COLUMNA_SKU_CODE = 13;
    public static final int COLUMNA_PREGULAR = 14;
    public static final int COLUMNA_PPROMOCION = 15;
    public static final int COLUMNA_MANUFACTURER = 16;
    public static final int COLUMNA_POS_NAME = 17;
    public static final int COLUMNA_PLATAFORMA = 18;
    public static final int COLUMNA_TIPO = 19;
    public static final int COLUMNA_PVM = 20;

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
        String poferta;
        String brand;
        //String tamano;
        //String cantidad;
        String sku_code;
        String regular_price;
        String promotional_price;
        String manufacturer;
        String pos_name;
        String plataforma;
        String tipo;
        String pvm;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        presentacion = c.getString(COLUMNA_PRESENTACION);
        poferta = c.getString(COLUMNA_POFERTA);
        brand = c.getString(COLUMNA_BRAND);
        //tamano = c.getString(COLUMNA_TAMANO);
        //cantidad = c.getString(COLUMNA_CANTIDAD);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        regular_price = c.getString(COLUMNA_PREGULAR);
        promotional_price = c.getString(COLUMNA_PPROMOCION);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);
        tipo = c.getString(COLUMNA_TIPO);
        pvm = c.getString(COLUMNA_PVM);

        try {

            jObject.put(ContractInsertPrecios.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertPrecios.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertPrecios.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPrecios.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertPrecios.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPrecios.Columnas.HORA, hora);
            jObject.put(ContractInsertPrecios.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertPrecios.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertPrecios.Columnas.PRESENTACION, presentacion);
            jObject.put(ContractInsertPrecios.Columnas.POFERTA, poferta);
            jObject.put(ContractInsertPrecios.Columnas.BRAND, brand);
            /*jObject.put(ContractInsertPrecios.Columnas.TAMANO, tamano);
            jObject.put(ContractInsertPrecios.Columnas.CANTIDAD, cantidad);*/
            jObject.put(ContractInsertPrecios.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertPrecios.Columnas.PREGULAR, regular_price);
            jObject.put(ContractInsertPrecios.Columnas.PPROMOCION, promotional_price);
            jObject.put(ContractInsertPrecios.Columnas.MANUFACTURER, manufacturer);
            jObject.put(ContractInsertPrecios.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertPrecios.Columnas.PLATAFORMA, plataforma);
            jObject.put(ContractInsertPrecios.Columnas.TIPO, tipo);
            jObject.put(ContractInsertPrecios.Columnas.PVM, pvm);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
