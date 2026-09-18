package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 23/03/2018.
 */

public class UtilidadesVenta {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_TIPO_FACTURA = 6;
    public static final int COLUMNA_NUM_FACTURA = 7;
    public static final int COLUMNA_MONTO_FACTURA = 8;
    public static final int COLUMNA_FECHA_VENTA = 9;
    public static final int COLUMNA_IMAGE = 10;
    public static final int COLUMNA_FECHA = 11;
    public static final int COLUMNA_HORA = 12;

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
         String tipo_factura;
         String num_factura;
         String monto_factura;
         String fecha_venta;
         String image;
         String fecha;
         String hora;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        tipo_factura = c.getString(COLUMNA_TIPO_FACTURA);
        num_factura = c.getString(COLUMNA_NUM_FACTURA);
        monto_factura = c.getString(COLUMNA_MONTO_FACTURA);
        fecha_venta = c.getString(COLUMNA_FECHA_VENTA);
        image = c.getString(COLUMNA_IMAGE);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractInsertVenta.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertVenta.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertVenta.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertVenta.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertVenta.Columnas.TIPO_FACTURA, tipo_factura);
            jObject.put(ContractInsertVenta.Columnas.NUM_FACTURA, num_factura);
            jObject.put(ContractInsertVenta.Columnas.MONTO_FACTURA, monto_factura);
            jObject.put(ContractInsertVenta.Columnas.FECHA_VENTA, fecha_venta);
            jObject.put(ContractInsertVenta.Columnas.KEY_IMAGE, image);
            jObject.put(ContractInsertVenta.Columnas.FECHA, fecha);
            jObject.put(ContractInsertVenta.Columnas.HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
