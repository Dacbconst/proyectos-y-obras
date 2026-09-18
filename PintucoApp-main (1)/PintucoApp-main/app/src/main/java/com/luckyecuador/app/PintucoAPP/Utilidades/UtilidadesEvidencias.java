package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesEvidencias {

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_CATEGORIA = 5;
    public static final int COLUMNA_COMENTARIO = 6;
    public static final int COLUMNA_FOTO_ANTES = 7;
    public static final int COLUMNA_FOTO_DESPUES = 8;
    public static final int COLUMNA_FECHA = 9;
    public static final int COLUMNA_HORA = 10;

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
        String comentario;
        String foto_antes;
        String foto_despues;
        String fecha;
        String hora;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        categoria = c.getString(COLUMNA_CATEGORIA);
        comentario = c.getString(COLUMNA_COMENTARIO);
        foto_antes = c.getString(COLUMNA_FOTO_ANTES);
        foto_despues = c.getString(COLUMNA_FOTO_DESPUES);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractInsertEvidencias.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertEvidencias.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertEvidencias.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertEvidencias.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertEvidencias.Columnas.COMENTARIO, comentario);
            jObject.put(ContractInsertEvidencias.Columnas.FOTO_ANTES, foto_antes);
            jObject.put(ContractInsertEvidencias.Columnas.FOTO_DESPUES, foto_despues);
            jObject.put(ContractInsertEvidencias.Columnas.FECHA, fecha);
            jObject.put(ContractInsertEvidencias.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
