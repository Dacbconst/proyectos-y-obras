package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class UtilidadesImpulso {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_BRAND = 9;
    public static final int COLUMNA_SKU_CODE = 10;
    public static final int COLUMNA_ASIGNADA = 11;
    public static final int COLUMNA_VENCIDA = 12;
    public static final int COLUMNA_ADICIONAL = 13;
    public static final int COLUMNA_CUMPLIMIENTO = 14;
    public static final int COLUMNA_IMPULSADORA = 15;
    public static final int COLUMNA_OBSERVACION = 16;
    public static final int COLUMNA_FOTO = 17;
    public static final int COLUMNA_POS_NAME = 18;
    public static final int COLUMNA_PRECIO_VENTA = 19;
    public static final int COLUMNA_ALERTA_STOCK = 20;
    public static final int COLUMNA_PLATAFORMA = 21;

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
        String segment1;
        String segment2;
        String brand;
        String contenido;
        String sku_code;
        String inventarios;
        String souvenirs;
        String foto;
        String pos_name;
        String precio_venta;
        String alerta_stock;
        String plataforma;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        sector = c.getString(COLUMNA_CATEGORIA);
        categoria = c.getString(COLUMNA_BRAND);
        segment1 = c.getString(COLUMNA_SKU_CODE);
        segment2 = c.getString(COLUMNA_ASIGNADA);
        brand = c.getString(COLUMNA_VENCIDA);
        contenido = c.getString(COLUMNA_ADICIONAL);
        sku_code = c.getString(COLUMNA_CUMPLIMIENTO);
        inventarios = c.getString(COLUMNA_IMPULSADORA);
        souvenirs = c.getString(COLUMNA_OBSERVACION);
        foto = c.getString(COLUMNA_FOTO);
        pos_name = c.getString(COLUMNA_POS_NAME);
        precio_venta = c.getString(COLUMNA_PRECIO_VENTA);
        alerta_stock = c.getString(COLUMNA_ALERTA_STOCK);
        plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertImpulso.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertImpulso.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertImpulso.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertImpulso.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertImpulso.Columnas.FECHA, fecha);
            jObject.put(ContractInsertImpulso.Columnas.HORA, hora);
            jObject.put(ContractInsertImpulso.Columnas.CATEGORIA, sector);
            jObject.put(ContractInsertImpulso.Columnas.BRAND, categoria);
            jObject.put(ContractInsertImpulso.Columnas.SKU_CODE, segment1);
            jObject.put(ContractInsertImpulso.Columnas.CANTIDAD_ASIGNADA, segment2);
            jObject.put(ContractInsertImpulso.Columnas.CANTIDAD_VENDIDA, brand);
            jObject.put(ContractInsertImpulso.Columnas.CANTIDAD_ADICIONAL, contenido);
            jObject.put(ContractInsertImpulso.Columnas.CUMPLIMIENTO, sku_code);
            jObject.put(ContractInsertImpulso.Columnas.IMPULSADORA, inventarios);
            jObject.put(ContractInsertImpulso.Columnas.OBSERVACION, souvenirs);
            jObject.put(ContractInsertImpulso.Columnas.FOTO, foto);
            jObject.put(ContractInsertImpulso.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertImpulso.Columnas.PRECIO_VENTA, precio_venta);
            jObject.put(ContractInsertImpulso.Columnas.ALERTA_STOCK, alerta_stock);
            jObject.put(ContractInsertImpulso.Columnas.PLATAFORMA, plataforma);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
