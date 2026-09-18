package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class
UtilidadesFlooring {

    public static final int COLUMNA_PHARMA_ID=2;
    public static final int COLUMNA_CODIGO=3;
    public static final int COLUMNA_USUARIO=4;
    public static final int COLUMNA_SUPERVISOR=5;
    public static final int COLUMNA_FECHA=6;
    public static final int COLUMNA_HORA=7;
    public static final int COLUMNA_SECTOR=8;
    public static final int COLUMNA_CATEGORIA=9;
    public static final int COLUMNA_SEGMENTO1=10;
    public static final int COLUMNA_SEGMENTO2=11;
    public static final int COLUMNA_BRAND=12;
    public static final int COLUMNA_CONTENIDO=13;
    public static final int COLUMNA_SKU_CODE=14;
    public static final int COLUMNA_INVENTARIOS=15;
    public static final int COLUMNA_SEMANA =16;
    public static final int COLUMNA_SUGERIDOS =17;
    public static final int COLUMNA_TIPO =18;
    public static final int COLUMNA_ENTREGA =19;
    public static final int COLUMNA_CAUSAL=20;
    public static final int COLUMNA_OTROS=21;
    public static final int COLUMNA_CADUCIDAD=22;
    public static final int COLUMNA_POS_NAME=23;
    public static final int COLUMNA_PLATAFORMA=24;
    public static final int COLUMNA_FECHA_INVENTARIO =25;

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
        String semana;
        String sugeridos;
        String tipo;
        String entrega;
        String causal;
        String otros;
        String caducidad;
        String pos_name;
        String plataforma;
        String fecha_inventario;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        sector = c.getString(COLUMNA_SECTOR);
        categoria = c.getString(COLUMNA_CATEGORIA);
        segment1 = c.getString(COLUMNA_SEGMENTO1);
        segment2 = c.getString(COLUMNA_SEGMENTO2);
        brand = c.getString(COLUMNA_BRAND);
        contenido = c.getString(COLUMNA_CONTENIDO);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        inventarios = c.getString(COLUMNA_INVENTARIOS);
        semana = c.getString(COLUMNA_SEMANA);
        sugeridos = c.getString(COLUMNA_SUGERIDOS);
        tipo = c.getString(COLUMNA_TIPO);
        entrega = c.getString(COLUMNA_ENTREGA);
        causal = c.getString(COLUMNA_CAUSAL);
        otros = c.getString(COLUMNA_OTROS);
        caducidad = c.getString(COLUMNA_CADUCIDAD);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);
        fecha_inventario = c.getString(COLUMNA_FECHA_INVENTARIO);

        try {

            jObject.put(InsertFlooring.Columnas.PHARMA_ID,pharma_id);
            jObject.put(InsertFlooring.Columnas.CODIGO,codigo);
            jObject.put(InsertFlooring.Columnas.USUARIO,usuario);
            jObject.put(InsertFlooring.Columnas.SUPERVISOR,supervisor);
            jObject.put(InsertFlooring.Columnas.FECHA,fecha);
            jObject.put(InsertFlooring.Columnas.HORA,hora);
            jObject.put(InsertFlooring.Columnas.SECTOR,sector);
            jObject.put(InsertFlooring.Columnas.CATEGORIA,categoria);
            jObject.put(InsertFlooring.Columnas.SUBCATEGORIA,segment1);
            jObject.put(InsertFlooring.Columnas.PRESENTACION,segment2);
            jObject.put(InsertFlooring.Columnas.BRAND,brand);
            jObject.put(InsertFlooring.Columnas.CONTENIDO,contenido);
            jObject.put(InsertFlooring.Columnas.SKU_CODE,sku_code);
            jObject.put(InsertFlooring.Columnas.INVENTARIOS,inventarios);
            jObject.put(InsertFlooring.Columnas.SEMANA,semana);
            jObject.put(InsertFlooring.Columnas.SUGERIDOS,sugeridos);
            jObject.put(InsertFlooring.Columnas.TIPO,tipo);
            jObject.put(InsertFlooring.Columnas.ENTREGA,entrega);
            jObject.put(InsertFlooring.Columnas.CAUSAL,causal);
            jObject.put(InsertFlooring.Columnas.OTROS,otros);
            jObject.put(InsertFlooring.Columnas.FECHA_CADUCIDAD,caducidad);
            jObject.put(InsertFlooring.Columnas.POS_NAME,pos_name);
            jObject.put(InsertFlooring.Columnas.PLATAFORMA,plataforma);
            jObject.put(InsertFlooring.Columnas.FECHA_INVENTARIO,fecha_inventario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
