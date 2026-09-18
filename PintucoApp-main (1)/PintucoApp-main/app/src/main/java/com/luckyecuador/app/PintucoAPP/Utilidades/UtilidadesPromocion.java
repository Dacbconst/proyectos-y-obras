package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 17/04/2018.
 */

public class UtilidadesPromocion {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_SUBCATEGORIA = 9;
    public static final int COLUMNA_BRAND = 10;
    public static final int COLUMNA_CANAL = 11;
    public static final int COLUMNA_TIPO_PROMOCION = 12;
    public static final int COLUMNA_DESCRIPCION_PROMOCION = 13;
    public static final int COLUMNA_PRODUCTO = 14;
    public static final int COLUMNA_MECANICA = 15;
    public static final int COLUMNA_INI_PROMO = 16;
    public static final int COLUMNA_FIN_PROMO = 17;
    public static final int COLUMNA_AGOTAR_STOCK = 18;
    public static final int COLUMNA_PVC_ANTERIOR = 19;
    public static final int COLUMNA_PVC_ACTUAL = 20;

    public static final int COLUMNA_MARGEN_DSCTO = 21;

    public static final int COLUMNA_FOTO = 22;
    public static final int COLUMNA_MANUFACTURER = 23;
    public static final int COLUMNA_SKU = 24;
    public static final int COLUMNA_POS_NAME = 25;
    public static final int COLUMNA_PLATAFORMA = 26;

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

        String pharma_id  = c.getString(COLUMNA_PHARMA_ID);
        String codigo = c.getString(COLUMNA_CODIGO);
        String usuario  = c.getString(COLUMNA_USUARIO);
        String supervisor = c.getString(COLUMNA_SUPERVISOR);
        String fecha = c.getString(COLUMNA_FECHA);
        String hora = c.getString(COLUMNA_HORA);
        String categoria = c.getString(COLUMNA_CATEGORIA);
        String subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        String brand = c.getString(COLUMNA_BRAND);
        String canal = c.getString(COLUMNA_CANAL);
        String tipo_promocion = c.getString(COLUMNA_TIPO_PROMOCION);
        String desc_promocion = c.getString(COLUMNA_DESCRIPCION_PROMOCION);
        String producto = c.getString(COLUMNA_PRODUCTO);
        String mecanica = c.getString(COLUMNA_MECANICA);
        String ini_promo = c.getString(COLUMNA_INI_PROMO);
        String fin_promo = c.getString(COLUMNA_FIN_PROMO);
        String agotar_stock = c.getString(COLUMNA_AGOTAR_STOCK);
        String pvc_anterior = c.getString(COLUMNA_PVC_ANTERIOR);
        String pvc_actual = c.getString(COLUMNA_PVC_ACTUAL);
        String margen_dscto = c.getString(COLUMNA_MARGEN_DSCTO);
        String foto = c.getString(COLUMNA_FOTO);
        String manufacturer = c.getString(COLUMNA_MANUFACTURER);
        String sku = c.getString(COLUMNA_SKU);
        String pos_name = c.getString(COLUMNA_POS_NAME);
        String plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertPromocion.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertPromocion.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertPromocion.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPromocion.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertPromocion.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPromocion.Columnas.HORA, hora);
            jObject.put(ContractInsertPromocion.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertPromocion.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertPromocion.Columnas.MARCA, brand);
            jObject.put(ContractInsertPromocion.Columnas.CANAL, canal);
            jObject.put(ContractInsertPromocion.Columnas.TIPO_PROMOCION, tipo_promocion);
            jObject.put(ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION, desc_promocion);
            jObject.put(ContractInsertPromocion.Columnas.PRODUCTO, producto);
            jObject.put(ContractInsertPromocion.Columnas.MECANICA, mecanica);
            jObject.put(ContractInsertPromocion.Columnas.INI_PROMO, ini_promo);
            jObject.put(ContractInsertPromocion.Columnas.FIN_PROMO, fin_promo);
            jObject.put(ContractInsertPromocion.Columnas.AGOTAR_STOCK, agotar_stock);
            jObject.put(ContractInsertPromocion.Columnas.PVC_ANTERIOR, pvc_anterior);
            jObject.put(ContractInsertPromocion.Columnas.PVC_ACTUAL, pvc_actual);
            jObject.put(ContractInsertPromocion.Columnas.MARGEN_DSCTO, margen_dscto);
            jObject.put(ContractInsertPromocion.Columnas.FOTO, foto);
            jObject.put(ContractInsertPromocion.Columnas.MANUFACTURER, manufacturer);
            jObject.put(ContractInsertPromocion.Columnas.SKU, sku);
            jObject.put(ContractInsertPromocion.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertPromocion.Columnas.PLATAFORMA, plataforma);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
