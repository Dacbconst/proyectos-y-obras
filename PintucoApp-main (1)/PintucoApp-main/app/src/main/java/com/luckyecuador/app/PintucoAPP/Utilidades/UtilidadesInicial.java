package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertInicial;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 16/04/2018.
 */

public class UtilidadesInicial {

    public static final int COLUMNA_IDPDV = 2;
    public static final int COLUMNA_CODIGO=3;
    public static final int COLUMNA_TIPO=4;
    public static final int COLUMNA_DEALER=5;
    public static final int COLUMNA_UBICACION=6;
    public static final int COLUMNA_CORREO= 7;
    public static final int COLUMNA_LATITUD = 8;
    public static final int COLUMNA_LONGITUD = 9;
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

    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id_pdv;
        String codigo;
        String tipo;
        String establecimiento;
        String telefono;
        String direccion;
        String latitude;
        String longitude;
        String fecha;
        String hora;


        id_pdv = c.getString(COLUMNA_IDPDV);
        codigo= c.getString(COLUMNA_CODIGO);
        tipo=c.getString(COLUMNA_TIPO);
        establecimiento=c.getString(COLUMNA_DEALER);
        telefono=c.getString(COLUMNA_UBICACION);
        direccion = c.getString(COLUMNA_CORREO);
        latitude = c.getString(COLUMNA_LATITUD);
        longitude = c.getString(COLUMNA_LONGITUD);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractInsertInicial.Columnas.IDPDV, id_pdv);
            jObject.put(ContractInsertInicial.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertInicial.Columnas.TIPO, tipo);
            jObject.put(ContractInsertInicial.Columnas.DEALER,establecimiento);
            jObject.put(ContractInsertInicial.Columnas.UBICACION, telefono);
            jObject.put(ContractInsertInicial.Columnas.CORREO, direccion);
            jObject.put(ContractInsertInicial.Columnas.LATITUD, latitude);
            jObject.put(ContractInsertInicial.Columnas.LONGITUD, longitude);
            jObject.put(ContractInsertInicial.Columnas.FECHA, fecha);
            jObject.put(ContractInsertInicial.Columnas.HORA, hora);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
