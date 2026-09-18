package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesPagoFactura {

    // Indices para las columnas indicadas en PROJECTION_INSERT_PAGO_FACTURA
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_ID_PROFORMA = 2;
    public static final int COLUMNA_ID_AGENDAMIENTO = 3;
    public static final int COLUMNA_CODIGO_PDV = 4;
    public static final int COLUMNA_USUARIO = 5;
    public static final int COLUMNA_NUMERO_CUOTA = 6;
    public static final int COLUMNA_MONTO_PAGO = 7;
    public static final int COLUMNA_FOTO_PAGO = 8;
    public static final int COLUMNA_FECHA_PAGO = 9;
    public static final int COLUMNA_OBSERVACION = 10;

    /**
     * Copia los datos de un registro de Pago de Factura almacenados en un
     * cursor hacia un JSONObject, para la subida al servidor.
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();

        String idRemota = c.getString(COLUMNA_ID_REMOTA);
        String idProforma = c.getString(COLUMNA_ID_PROFORMA);
        String idAgendamiento = c.getString(COLUMNA_ID_AGENDAMIENTO);
        String codigoPdv = c.getString(COLUMNA_CODIGO_PDV);
        String usuario = c.getString(COLUMNA_USUARIO);
        String numeroCuota = c.getString(COLUMNA_NUMERO_CUOTA);
        String montoPago = c.getString(COLUMNA_MONTO_PAGO);
        String fotoPago = c.getString(COLUMNA_FOTO_PAGO);
        String fechaPago = c.getString(COLUMNA_FECHA_PAGO);
        String observacion = c.getString(COLUMNA_OBSERVACION);

        try {
            // Si ya tiene id_remota, el PHP debe hacer UPDATE en vez de INSERT.
            jObject.put(Constantes.ID_REMOTA, idRemota);
            jObject.put(ContractInsertPagoFactura.Columnas.ID_PROFORMA, idProforma);
            jObject.put(ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO, idAgendamiento);
            jObject.put(ContractInsertPagoFactura.Columnas.CODIGO_PDV, codigoPdv);
            jObject.put(ContractInsertPagoFactura.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPagoFactura.Columnas.NUMERO_CUOTA, numeroCuota);
            jObject.put(ContractInsertPagoFactura.Columnas.MONTO_PAGO, montoPago);
            jObject.put(ContractInsertPagoFactura.Columnas.FOTO_PAGO, fotoPago);
            jObject.put(ContractInsertPagoFactura.Columnas.FECHA_PAGO, fechaPago);
            if (observacion != null && !observacion.isEmpty()) {
                jObject.put(ContractInsertPagoFactura.Columnas.OBSERVACION, observacion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-PagoFactura", String.valueOf(jObject));

        return jObject;
    }

    public static String optStringSeguro(JSONObject json, String key) {
        return optStringSeguro(json, key, "");
    }

    public static String optStringSeguro(JSONObject json, String key, String valorPorDefecto) {
        if (json == null || !json.has(key) || json.isNull(key)) {
            return valorPorDefecto;
        }
        return json.optString(key, valorPorDefecto);
    }

}
