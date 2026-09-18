package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesVentas {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_FECHA_VENTA = 8;
    public static final int COLUMNA_CATEGORIA = 9;
    public static final int COLUMNA_SUBCATEGORIA = 10;
    public static final int COLUMNA_PRESENTACION = 11;
    public static final int COLUMNA_MARCA = 12;
    public static final int COLUMNA_SKU_CODE = 13;
    public static final int COLUMNA_TIPO_VENTA = 14;
    public static final int COLUMNA_STOCK_INICAL = 15;
    public static final int COLUMNA_CANTIDAD = 16;
    public static final int COLUMNA_PREGULAR = 17;
    public static final int COLUMNA_PPROMOCION = 18;
    public static final int COLUMNA_POFERTA = 19;
    public static final int COLUMNA_STOCK_FINAL = 20;
    public static final int COLUMNA_MANUFACTURER = 21;
    public static final int COLUMNA_POS_NAME = 22;
    public static final int COLUMNA_FOTO = 23;


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
        String fecha_venta;
        String categoria;
        String subcategoria;
        String presentacion;
        String marca;
        String sku_code;
        String tipo_venta;
        String stock_incial;
        String cantidad;
        String regular_price;
        String promotional_price;
        String poferta;
        String stock_final;
        String manufacturer;
        String pos_name;
        String foto;




        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        fecha_venta = c.getString(COLUMNA_FECHA_VENTA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        presentacion = c.getString(COLUMNA_PRESENTACION);
        marca = c.getString(COLUMNA_MARCA);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        tipo_venta = c.getString(COLUMNA_TIPO_VENTA);
        stock_incial = c.getString(COLUMNA_STOCK_INICAL);
        cantidad = c.getString(COLUMNA_CANTIDAD);
        regular_price = c.getString(COLUMNA_PREGULAR);
        promotional_price = c.getString(COLUMNA_PPROMOCION);
        poferta = c.getString(COLUMNA_POFERTA);
        stock_final = c.getString(COLUMNA_STOCK_FINAL);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);
        pos_name = c.getString(COLUMNA_POS_NAME);
        foto = c.getString(COLUMNA_FOTO);


        try {

            jObject.put(ContractInsertVentas.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertVentas.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertVentas.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertVentas.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertVentas.Columnas.FECHA, fecha);
            jObject.put(ContractInsertVentas.Columnas.HORA, hora);
            jObject.put(ContractInsertVentas.Columnas.FECHA_VENTA, fecha_venta);
            jObject.put(ContractInsertVentas.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertVentas.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertVentas.Columnas.PRESENTACION, presentacion);
            jObject.put(ContractInsertVentas.Columnas.MARCA, marca);
            jObject.put(ContractInsertVentas.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertVentas.Columnas.TIPO_VENTA, tipo_venta);
            jObject.put(ContractInsertVentas.Columnas.STOCK_INICIAL, stock_incial);
            jObject.put(ContractInsertVentas.Columnas.CANTIDAD, cantidad);
            jObject.put(ContractInsertVentas.Columnas.PREGULAR, regular_price);
            jObject.put(ContractInsertVentas.Columnas.PPROMOCION, promotional_price);
            jObject.put(ContractInsertVentas.Columnas.POFERTA, poferta);
            jObject.put(ContractInsertVentas.Columnas.STOCK_FINAL, stock_final);
            jObject.put(ContractInsertVentas.Columnas.MANUFACTURER, manufacturer);
            jObject.put(ContractInsertVentas.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertVentas.Columnas.FOTO, foto);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
