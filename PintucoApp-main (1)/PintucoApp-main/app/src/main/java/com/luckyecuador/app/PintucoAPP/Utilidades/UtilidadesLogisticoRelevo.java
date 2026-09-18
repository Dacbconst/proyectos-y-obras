package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesLogisticoRelevo {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;

    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;

    public static final int COLUMNA_CATEGORIA = 8;
    public static final int COLUMNA_BRAND = 9;
    public static final int COLUMNA_SKU_CODE = 10;

    public static final int COLUMNA_PREGULAR = 11;
    public static final int COLUMNA_CAUSAL = 12;
    public static final int COLUMNA_TIPO_LOGISTICO = 13;

    public static final int COLUMNA_FOTO = 14;
    public static final int COLUMNA_COMENTARIO = 15;
    public static final int COLUMNA_FECHA_PROD_CADUCADO = 16;

    public static final int COLUMNA_FECHA_PROD_PROPENSO = 17;

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
        String brand;
        String sku_code;

        String regular_price;
        String causal;
        String tipo_logistico;

        String foto;
        String comentario;
        String fecha_prod_caducado;

        String fecha_prod_propenso;


        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);

        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        categoria = c.getString(COLUMNA_CATEGORIA);
        brand = c.getString(COLUMNA_BRAND);
        sku_code = c.getString(COLUMNA_SKU_CODE);

        regular_price = c.getString(COLUMNA_PREGULAR);
        causal = c.getString(COLUMNA_CAUSAL);
        tipo_logistico = c.getString(COLUMNA_TIPO_LOGISTICO);

        foto = c.getString(COLUMNA_FOTO);
        comentario = c.getString(COLUMNA_COMENTARIO);
        fecha_prod_caducado = c.getString(COLUMNA_FECHA_PROD_CADUCADO);

        fecha_prod_propenso = c.getString(COLUMNA_FECHA_PROD_PROPENSO);


        try {

            jObject.put(ContractInsertLogisticoRelevo.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.USUARIO, usuario);

            jObject.put(ContractInsertLogisticoRelevo.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.FECHA, fecha);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.HORA, hora);

            jObject.put(ContractInsertLogisticoRelevo.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.BRAND, brand);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.SKU_CODE, sku_code);

            jObject.put(ContractInsertLogisticoRelevo.Columnas.PREGULAR, regular_price);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.CAUSAL, causal);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO, tipo_logistico);

            jObject.put(ContractInsertLogisticoRelevo.Columnas.FOTO, foto);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.COMENTARIO, comentario);
            jObject.put(ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_CADUCADO, fecha_prod_caducado);

            jObject.put(ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_PROPENSO, fecha_prod_propenso);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
