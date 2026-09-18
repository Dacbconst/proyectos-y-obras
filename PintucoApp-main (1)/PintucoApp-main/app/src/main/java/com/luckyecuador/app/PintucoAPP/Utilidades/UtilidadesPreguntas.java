package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesPreguntas {

    public static final int COLUMNA_USUARIO = 2;

    public static final int COLUMNA_TEST_ID = 3;

    public static final int COLUMNA_TEST = 4;
    public static final int COLUMNA_P1 = 5;
    public static final int COLUMNA_P2 = 6;
    public static final int COLUMNA_P3 = 7;
    public static final int COLUMNA_P4 = 8;
    public static final int COLUMNA_P5 = 9;
    public static final int COLUMNA_P6 = 10;
    public static final int COLUMNA_P7 = 11;
    public static final int COLUMNA_P8 = 12;
    public static final int COLUMNA_P9 = 13;
    public static final int COLUMNA_P10 = 14;
    public static final int COLUMNA_P11 = 15;
    public static final int COLUMNA_P12 = 16;
    public static final int COLUMNA_P13 = 17;
    public static final int COLUMNA_P14 = 18;
    public static final int COLUMNA_P15 = 19;
    public static final int COLUMNA_CORRECTAS = 20;
    public static final int COLUMNA_INCORRECTAS = 21;
    public static final int COLUMNA_CALIFICACION = 22;
    public static final int COLUMNA_OBSERVACION = 23;
    public static final int COLUMNA_FECHA = 24;
    public static final int COLUMNA_HORA = 25;
    public static final int COLUMNA_CRONOMETRO = 26;
    public static final int COLUMNA_ESTADO_TEST = 27;

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

        String usuario;
        String test_id;
        String test;
        String p1;
        String p2;
        String p3;
        String p4;
        String p5;
        String p6;
        String p7;
        String p8;
        String p9;
        String p10;
        String p11;
        String p12;
        String p13;
        String p14;
        String p15;
        String correctas;
        String incorrectas;
        String calificacion;
        String observacion;
        String fecha;
        String hora;
        String cronometro;
        String estado_test;

        usuario = c.getString(COLUMNA_USUARIO);
        test_id = c.getString(COLUMNA_TEST_ID);
        test = c.getString(COLUMNA_TEST);
        p1 = c.getString(COLUMNA_P1);
        p2 = c.getString(COLUMNA_P2);
        p3 = c.getString(COLUMNA_P3);
        p4 = c.getString(COLUMNA_P4);
        p5 = c.getString(COLUMNA_P5);
        p6 = c.getString(COLUMNA_P6);
        p7 = c.getString(COLUMNA_P7);
        p8 = c.getString(COLUMNA_P8);
        p9 = c.getString(COLUMNA_P9);
        p10 = c.getString(COLUMNA_P10);
        p11 = c.getString(COLUMNA_P11);
        p12 = c.getString(COLUMNA_P12);
        p13 = c.getString(COLUMNA_P13);
        p14 = c.getString(COLUMNA_P14);
        p15 = c.getString(COLUMNA_P15);
        correctas = c.getString(COLUMNA_CORRECTAS);
        incorrectas = c.getString(COLUMNA_INCORRECTAS);
        calificacion = c.getString(COLUMNA_CALIFICACION);
        observacion = c.getString(COLUMNA_OBSERVACION);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        cronometro = c.getString(COLUMNA_CRONOMETRO);
        estado_test = c.getString(COLUMNA_ESTADO_TEST);
        
        try {
            jObject.put(ContractInsertPreguntas.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertPreguntas.Columnas.TEST_ID,test_id);
            jObject.put(ContractInsertPreguntas.Columnas.TEST,test);
            jObject.put(ContractInsertPreguntas.Columnas.P1, p1);
            jObject.put(ContractInsertPreguntas.Columnas.P2, p2);
            jObject.put(ContractInsertPreguntas.Columnas.P3, p3);
            jObject.put(ContractInsertPreguntas.Columnas.P4, p4);
            jObject.put(ContractInsertPreguntas.Columnas.P5, p5);
            jObject.put(ContractInsertPreguntas.Columnas.P6, p6);
            jObject.put(ContractInsertPreguntas.Columnas.P7, p7);
            jObject.put(ContractInsertPreguntas.Columnas.P8, p8);
            jObject.put(ContractInsertPreguntas.Columnas.P9, p9);
            jObject.put(ContractInsertPreguntas.Columnas.P10, p10);
            jObject.put(ContractInsertPreguntas.Columnas.P11, p11);
            jObject.put(ContractInsertPreguntas.Columnas.P12, p12);
            jObject.put(ContractInsertPreguntas.Columnas.P13, p13);
            jObject.put(ContractInsertPreguntas.Columnas.P14, p14);
            jObject.put(ContractInsertPreguntas.Columnas.P15, p15);
            jObject.put(ContractInsertPreguntas.Columnas.CORRECTAS, correctas);
            jObject.put(ContractInsertPreguntas.Columnas.INCORRECTAS, incorrectas);
            jObject.put(ContractInsertPreguntas.Columnas.CALIFICACION, calificacion);
            jObject.put(ContractInsertPreguntas.Columnas.OBSERVACION, observacion);
            jObject.put(ContractInsertPreguntas.Columnas.FECHA, fecha);
            jObject.put(ContractInsertPreguntas.Columnas.HORA, hora);
            jObject.put(ContractInsertPreguntas.Columnas.CRONOMETO,cronometro);
            jObject.put(ContractInsertPreguntas.Columnas.ESTADO_TEST,estado_test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
