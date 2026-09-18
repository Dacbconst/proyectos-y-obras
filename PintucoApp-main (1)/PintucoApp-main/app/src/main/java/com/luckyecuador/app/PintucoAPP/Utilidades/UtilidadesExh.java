package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesExh {


    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_SECTOR = 8;
    public static final int COLUMNA_CATEGORIA = 9;
    public static final int COLUMNA_SUBCATEGORIA = 10;
    public static final int COLUMNA_SEGMENTO = 11;
    public static final int COLUMNA_BRAND = 12;
    public static final int COLUMNA_TIPO_EXH = 13;
    public static final int COLUMNA_ZONA_EX = 14;

    public static final int COLUMNA_NIVEL = 15;
    public static final int COLUMNA_TIPO = 16;
    public static final int COLUMNA_CONTRATADA = 17;
    public static final int COLUMNA_CONDICION = 18;
    public static final int COLUMNA_FOTO = 19;
    public static final int COLUMNA_POS_NAME = 20;
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
        String subcategoria;
        String segmento;
        String brand;
        String tipo_exh;
        String zona_exh;
        String nivel;
        String tipo;
        String contratada;
        String condicion;
        String foto;
        String pos_name;
        String plataforma;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        sector = c.getString(COLUMNA_SECTOR);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        segmento = c.getString(COLUMNA_SEGMENTO);
        brand = c.getString(COLUMNA_BRAND);
        tipo_exh = c.getString(COLUMNA_TIPO_EXH);
        zona_exh = c.getString(COLUMNA_ZONA_EX);
        nivel = c.getString(COLUMNA_NIVEL);
        tipo = c.getString(COLUMNA_TIPO);
        contratada = c.getString(COLUMNA_CONTRATADA);
        condicion = c.getString(COLUMNA_CONDICION);
        foto = c.getString(COLUMNA_FOTO);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertExh.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertExh.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertExh.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertExh.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertExh.Columnas.FECHA, fecha);
            jObject.put(ContractInsertExh.Columnas.HORA, hora);
            jObject.put(ContractInsertExh.Columnas.SECTOR, sector);
            jObject.put(ContractInsertExh.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertExh.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertExh.Columnas.SEGMENTO, segmento);
            jObject.put(ContractInsertExh.Columnas.BRAND, brand);
            jObject.put(ContractInsertExh.Columnas.TIPO_EXH, tipo_exh);
            jObject.put(ContractInsertExh.Columnas.ZONA_EX, zona_exh);
            jObject.put(ContractInsertExh.Columnas.NIVEL, nivel);
            jObject.put(ContractInsertExh.Columnas.TIPO, tipo);
            jObject.put(ContractInsertExh.Columnas.CONTRATADA, contratada);
            jObject.put(ContractInsertExh.Columnas.CONDICION, condicion);
            jObject.put(ContractInsertExh.Columnas.FOTO, foto);
            jObject.put(ContractInsertExh.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertExh.Columnas.PLATAFORMA, plataforma);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
