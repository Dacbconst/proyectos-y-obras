package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesFotografico {

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_CATEGORIA = 5;
    public static final int COLUMNA_SUBCATEGORIA = 6;
    public static final int COLUMNA_MARCA = 7;
    public static final int COLUMNA_LOGRO = 8;
    public static final int COLUMNA_KEY_IMAGE = 9;
    public static final int COLUMNA_FECHA = 10;
    public static final int COLUMNA_HORA = 11;

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
        String categoria;
        String subcategoria;
        String marca;
        String logro;
        String key_image;
        String fecha;
        String hora;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        categoria = c.getString(COLUMNA_CATEGORIA);
        subcategoria = c.getString(COLUMNA_SUBCATEGORIA);
        marca = c.getString(COLUMNA_MARCA);
        logro = c.getString(COLUMNA_LOGRO);
        key_image = c.getString(COLUMNA_KEY_IMAGE);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractInsertFotografico.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertFotografico.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertFotografico.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertFotografico.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertFotografico.Columnas.SUBCATEGORIA, subcategoria);
            jObject.put(ContractInsertFotografico.Columnas.MARCA, marca);
            jObject.put(ContractInsertFotografico.Columnas.LOGRO, logro);
            jObject.put(ContractInsertFotografico.Columnas.KEY_IMAGE, key_image);
            jObject.put(ContractInsertFotografico.Columnas.FECHA, fecha);
            jObject.put(ContractInsertFotografico.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
