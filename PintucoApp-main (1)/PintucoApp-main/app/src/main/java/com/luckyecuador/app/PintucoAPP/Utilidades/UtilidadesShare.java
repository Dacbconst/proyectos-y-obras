package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class UtilidadesShare {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_SUBCATEGORIA = 9;
    public static final int COLUMNA_PRESENTACION = 10;
    public static final int COLUMNA_MARCA_SELECCIONADA = 11;
    public static final int COLUMNA_BRAND = 12;
    public static final int COLUMNA_CTMS_PERCHA = 13;
    public static final int COLUMNA_CTMS_MARCA = 14;
    public static final int COLUMNA_OTROS = 15;
    public static final int COLUMNA_MANUFACTURER = 16;
    public static final int COLUMNA_RAZONES = 17;
    public static final int COLUMNA_IMAGE = 18;
    public static final int COLUMNA_POS_NAME = 19;
    public static final int COLUMNA_PLATAFORMA = 20;
 //   public static final int COLUMNA_COMENTARIO = 21;


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
        String marca_seleccionada;
        String brand;
        String ctms_percha;
        String ctms_marca;
        String otros;
        String manufacurer;
        String razones;
        String image;
        String pos_name;
        String plataforma;
      //  String comentario;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        presentacion = c.getString(COLUMNA_PRESENTACION);
        marca_seleccionada = c.getString(COLUMNA_MARCA_SELECCIONADA);
        brand = c.getString(COLUMNA_BRAND);
        ctms_percha = c.getString(COLUMNA_CTMS_PERCHA);
        ctms_marca = c.getString(COLUMNA_CTMS_MARCA);
        otros = c.getString(COLUMNA_OTROS);
        manufacurer = c.getString(COLUMNA_MANUFACTURER);
        razones = c.getString(COLUMNA_RAZONES);
        image = c.getString(COLUMNA_IMAGE);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);
     //   comentario = c.getString(COLUMNA_COMENTARIO);

        try {
            jObject.put(ContractInsertShare.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertShare.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertShare.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertShare.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertShare.Columnas.FECHA, fecha);
            jObject.put(ContractInsertShare.Columnas.HORA, hora);
            jObject.put(ContractInsertShare.Columnas.SECTOR, categoria);
            jObject.put(ContractInsertShare.Columnas.CATEGORIA, subcategoria);
            jObject.put(ContractInsertShare.Columnas.SEGMENTO, presentacion);
            jObject.put(ContractInsertShare.Columnas.MARCA_SELECCIONADA, marca_seleccionada);
            jObject.put(ContractInsertShare.Columnas.BRAND, brand);
            jObject.put(ContractInsertShare.Columnas.CTMS_PERCHA, ctms_percha);
            jObject.put(ContractInsertShare.Columnas.CTMS_MARCA, ctms_marca);
            jObject.put(ContractInsertShare.Columnas.OTROS, otros);
            jObject.put(ContractInsertShare.Columnas.MANUFACTURER, manufacurer);
            jObject.put(ContractInsertShare.Columnas.RAZONES, razones);
            jObject.put(ContractInsertShare.Columnas.FOTO, image);
            jObject.put(ContractInsertShare.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertShare.Columnas.PLATAFORMA, plataforma);
           // jObject.put(ContractInsertShare.Columnas.COMENTARIO, comentario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
