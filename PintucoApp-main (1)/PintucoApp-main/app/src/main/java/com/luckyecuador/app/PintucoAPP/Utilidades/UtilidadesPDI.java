package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPDI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class UtilidadesPDI {


    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_MARCA_SELECCIONADA = 9;
    public static final int COLUMNA_CUMPLIMIENTO = 10;
    public static final int COLUMNA_UNIVERSO = 11;
    public static final int COLUMNA_CARAS = 12;
    public static final int COLUMNA_OTROS = 13;
    public static final int COLUMNA_OBJ_CATEGORIA = 14;
    public static final int COLUMNA_PART_CATEGORIA = 15;
    public static final int COLUMNA_IMAGE = 16;
    public static final int COLUMNA_CANAL = 17;
    public static final int COLUMNA_POS_NAME = 18;
    public static final int COLUMNA_PLATAFORMA = 19;

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
        String marca_seleccionada;
        String cumplimiento;
        String universo;
        String caras;
        String otros;
        String obj_categoria;
        String part_categoria;
        String image;
        String canal;
        String pos_name;
        String plataforma;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        categoria = c.getString(COLUMNA_CATEGORIA);
        marca_seleccionada = c.getString(COLUMNA_MARCA_SELECCIONADA);
        cumplimiento = c.getString(COLUMNA_CUMPLIMIENTO);
        universo = c.getString(COLUMNA_UNIVERSO);
        caras = c.getString(COLUMNA_CARAS);
        otros = c.getString(COLUMNA_OTROS);
        obj_categoria = c.getString(COLUMNA_OBJ_CATEGORIA);
        part_categoria = c.getString(COLUMNA_PART_CATEGORIA);
        image = c.getString(COLUMNA_IMAGE);
        canal = c.getString(COLUMNA_CANAL);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertPDI.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertPDI.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertPDI.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPDI.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertPDI.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPDI.Columnas.HORA, hora);
            jObject.put(ContractInsertPDI.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertPDI.Columnas.MARCA_SELECCIONADA, marca_seleccionada);
            jObject.put(ContractInsertPDI.Columnas.CUMPLIMIENTO, cumplimiento);
            jObject.put(ContractInsertPDI.Columnas.UNIVERSO, universo);
            jObject.put(ContractInsertPDI.Columnas.CARAS, caras);
            jObject.put(ContractInsertPDI.Columnas.OTROS, otros);
            jObject.put(ContractInsertPDI.Columnas.OBJ_CATEGORIA, obj_categoria);
            jObject.put(ContractInsertPDI.Columnas.PART_CATEGORIA, part_categoria);
            jObject.put(ContractInsertPDI.Columnas.FOTO, image);
            jObject.put(ContractInsertPDI.Columnas.CANAL, canal);
            jObject.put(ContractInsertPDI.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertPDI.Columnas.PLATAFORMA, plataforma);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
