package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdv;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucky Ecuador on 23/03/2018.
 */

public class UtilidadesPdv {
    public static final int COLUMNA_IDPDV = 2;
    public static final int COLUMNA_ESTADOVISITA=3;
    public static final int COLUMNA_NOVEDADES=4;
    public static final int COLUMNA_FOTO=5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;

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
        String estado_visita;
        String novedades;
        String foto;
        String fecha;
        String hora;


        id_pdv = c.getString(COLUMNA_IDPDV);
        estado_visita= c.getString(COLUMNA_ESTADOVISITA);
        novedades=c.getString(COLUMNA_NOVEDADES);
        foto=c.getString(COLUMNA_FOTO);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);

        try {
            jObject.put(ContractInsertPdv.Columnas.IDPDV, id_pdv);
            jObject.put(ContractInsertPdv.Columnas.ESTADOVISITA, estado_visita);
            jObject.put(ContractInsertPdv.Columnas.NOVEDADES, novedades);
            jObject.put(ContractInsertPdv.Columnas.FOTO, foto);
            jObject.put(ContractInsertPdv.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPdv.Columnas.HORA, hora);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }
}
