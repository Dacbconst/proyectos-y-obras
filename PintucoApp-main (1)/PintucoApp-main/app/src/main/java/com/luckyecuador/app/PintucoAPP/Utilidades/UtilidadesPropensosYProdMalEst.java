package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class UtilidadesPropensosYProdMalEst {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_SECTOR = 8;
    public static final int COLUMNA_CATEGORIA = 9;
    public static final int COLUMNA_SEGMENTO1 = 10;
    public static final int COLUMNA_SEGMENTO2 = 11;
    public static final int COLUMNA_BRAND = 12;
    public static final int COLUMNA_CONTENIDO = 13;
    public static final int COLUMNA_SKU_CODE = 14;
    public static final int COLUMNA_INVENTARIOS = 15;
    public static final int COLUMNA_SOUVENIRS = 16;
    public static final int COLUMNA_TIPO_CONTEO_TOTAL = 17;
    public static final int COLUMNA_TOTAL = 18;
    public static final int COLUMNA_TIPO_CONTEO_DEFECTUOSAS = 19;
    public static final int COLUMNA_CANT_DEFECTUOSAS = 20;
    public static final int COLUMNA_FECHA_CADUCIDAD_TOTAL = 21;
    public static final int COLUMNA_FECHA_CADUCIDAD = 22;
    public static final int COLUMNA_CAUSAL = 23;
    public static final int COLUMNA_FOTO = 24;
    public static final int COLUMNA_PLATAFORMA = 25;
    public static final int COLUMNA_MODULO = 26;
    public static final int COLUMNA_LITROS = 27;
    public static final int COLUMNA_DIAS_RESTANTES = 28;
   public static final int COLUMNA_VALORIZADO = 29;
   public static final int COLUMNA_SKU_CLIENTE = 30;


    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject  =  new JSONObject();
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
        String tipo_conteo_total;
        String total;
        String tipo_conteo_defectuosas;
        String cantidad_defectuosas;
        String fecha_caducidad_total;
        String fecha_caducidad;
        String causal;
        String foto;
        String plataforma;
        String modulo;
       String litros;
        String dias_restantes;
        String valorizado;
        String sku_cliente;

        pharma_id  =  c.getString(COLUMNA_PHARMA_ID);
        codigo  =  c.getString(COLUMNA_CODIGO);
        usuario  =  c.getString(COLUMNA_USUARIO);
        supervisor  =  c.getString(COLUMNA_SUPERVISOR);
        fecha  =  c.getString(COLUMNA_FECHA);
        hora  =  c.getString(COLUMNA_HORA);
        sector  =  c.getString(COLUMNA_SECTOR);
        categoria  =  c.getString(COLUMNA_CATEGORIA);
        segment1  =  c.getString(COLUMNA_SEGMENTO1);
        segment2  =  c.getString(COLUMNA_SEGMENTO2);
        brand  =  c.getString(COLUMNA_BRAND);
        contenido  =  c.getString(COLUMNA_CONTENIDO);
        sku_code  =  c.getString(COLUMNA_SKU_CODE);
        inventarios  =  c.getString(COLUMNA_INVENTARIOS);
        souvenirs  =  c.getString(COLUMNA_SOUVENIRS);
        tipo_conteo_total  =  c.getString(COLUMNA_TIPO_CONTEO_TOTAL);
        total  =  c.getString(COLUMNA_TOTAL);
        tipo_conteo_defectuosas  =  c.getString(COLUMNA_TIPO_CONTEO_DEFECTUOSAS);
        cantidad_defectuosas  =  c.getString(COLUMNA_CANT_DEFECTUOSAS);
        fecha_caducidad_total  =  c.getString(COLUMNA_FECHA_CADUCIDAD_TOTAL);
        fecha_caducidad  =  c.getString(COLUMNA_FECHA_CADUCIDAD);
        causal  =  c.getString(COLUMNA_CAUSAL);
        foto  =  c.getString(COLUMNA_FOTO);
        plataforma  =  c.getString(COLUMNA_PLATAFORMA);
        modulo  =  c.getString(COLUMNA_MODULO);
        litros  =  c.getString(COLUMNA_LITROS);
        dias_restantes  =  c.getString(COLUMNA_DIAS_RESTANTES);
      valorizado  =  c.getString(COLUMNA_VALORIZADO);
      sku_cliente  =  c.getString(COLUMNA_SKU_CLIENTE);

        try {

            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.PHARMA_ID,pharma_id);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.CODIGO,codigo);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.USUARIO,usuario);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR,supervisor);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA,fecha);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.HORA,hora);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SECTOR,sector);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA,categoria);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA,segment1);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION,segment2);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.BRAND,brand);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO,contenido);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE,sku_code);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.INVENTARIOS,inventarios);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SOUVENIRS,souvenirs);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL,tipo_conteo_total);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.TOTAL,total);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS,tipo_conteo_defectuosas);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS,cantidad_defectuosas);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL,fecha_caducidad_total);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD,fecha_caducidad);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.CAUSAL,causal);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.FOTO,foto);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.PLATAFORMA,plataforma);
            jObject.put(ContractInsertPropensosYProdMalEst.Columnas.MODULO,modulo);
           jObject.put(ContractInsertPropensosYProdMalEst.Columnas.LITROS,litros);
           jObject.put(ContractInsertPropensosYProdMalEst.Columnas.DIAS_RESTANTES,dias_restantes);
           jObject.put(ContractInsertPropensosYProdMalEst.Columnas.VALORIZADO,valorizado);
           jObject.put(ContractInsertPropensosYProdMalEst.Columnas.SKU_CLIENTE,sku_cliente);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
