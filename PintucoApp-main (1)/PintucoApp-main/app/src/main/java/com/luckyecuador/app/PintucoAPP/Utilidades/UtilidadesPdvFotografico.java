package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesPdvFotografico {

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_CANAL = 8;
    public static final int COLUMNA_CODIGO_PDV = 9;
    public static final int COLUMNA_MERCADERISTA = 10;
    public static final int COLUMNA_FOTO = 11;

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
        String canal;
        String codigo_pdv;
        String mercaderista;
        String foto;


        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        canal = c.getString(COLUMNA_CANAL);
        codigo_pdv = c.getString(COLUMNA_CODIGO_PDV);
        mercaderista = c.getString(COLUMNA_MERCADERISTA);
        foto = c.getString(COLUMNA_FOTO);

        try {
            jObject.put(ContractInsertPdvFotografico.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertPdvFotografico.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertPdvFotografico.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPdvFotografico.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertPdvFotografico.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPdvFotografico.Columnas.HORA, hora);
            jObject.put(ContractInsertPdvFotografico.Columnas.CANAL, canal);
            jObject.put(ContractInsertPdvFotografico.Columnas.CODIGO_PDV, codigo_pdv);
            jObject.put(ContractInsertPdvFotografico.Columnas.MERCADERISTA, mercaderista);
            jObject.put(ContractInsertPdvFotografico.Columnas.FOTO, foto);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
