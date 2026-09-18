package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesGps {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_IDPDV = 2;
    public static final int COLUMNA_USUARIO= 3;
    public static final int COLUMNA_TIPO = 4;
    public static final int COLUMNA_VERSION = 5;
    public static final int COLUMNA_LATITUDE = 6;
    public static final int COLUMNA_LONGITUDE = 7;
    public static final int COLUMNA_FECHA = 8;
    public static final int COLUMNA_HORA = 9;
    public static final int COLUMNA_CAUSAL = 10;
    public static final int COLUMNA_FOTO = 11;
    public static final int COLUMNA_DISTANCIA = 12;
    public static final int COLUMNA_TIPO_RELEVO = 13;
    public static final int COLUMNA_POS_NAME = 14;
    public static final int COLUMNA_ID_RUTA = 15;

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
        String id_pdv;
        String usuario;
        String tipo;
        String version;
        String latitude;
        String longitude;
        String fecha;
        String hora;
        String causal;
        String foto;
        String distancia;
        String tipo_relevo;
        String pos_name;
        String id_ruta;

        id_pdv=c.getString(COLUMNA_IDPDV);
        usuario=c.getString(COLUMNA_USUARIO);
        tipo = c.getString(COLUMNA_TIPO);
        version = c.getString(COLUMNA_VERSION);
        latitude = c.getString(COLUMNA_LATITUDE);
        longitude = c.getString(COLUMNA_LONGITUDE);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        causal = c.getString(COLUMNA_CAUSAL);
        foto = c.getString(COLUMNA_FOTO);
        distancia = c.getString(COLUMNA_DISTANCIA);
        tipo_relevo = c.getString(COLUMNA_TIPO_RELEVO);
        pos_name = c.getString(COLUMNA_POS_NAME);
        id_ruta = c.getString(COLUMNA_ID_RUTA);

        try {
            jObject.put(ContractInsertGps.Columnas.IDPDV,id_pdv);
            jObject.put(ContractInsertGps.Columnas.USUARIO,usuario);
            jObject.put(ContractInsertGps.Columnas.TIPO, tipo);
            jObject.put(ContractInsertGps.Columnas.VERSION, version);
            jObject.put(ContractInsertGps.Columnas.LATITUDE, latitude);
            jObject.put(ContractInsertGps.Columnas.LONGITUDE, longitude);
            jObject.put(ContractInsertGps.Columnas.FECHA, fecha);
            jObject.put(ContractInsertGps.Columnas.HORA, hora);
            jObject.put(ContractInsertGps.Columnas.CAUSAL, causal);
            jObject.put(ContractInsertGps.Columnas.FOTO, foto);
            jObject.put(ContractInsertGps.Columnas.DISTANCIA, distancia);
            jObject.put(ContractInsertGps.Columnas.TIPO_RELEVO, tipo_relevo);
            jObject.put(ContractInsertGps.Columnas.POS_NAME, pos_name);
            jObject.put(Constantes.ID_REMOTA_RUTA, id_ruta);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
