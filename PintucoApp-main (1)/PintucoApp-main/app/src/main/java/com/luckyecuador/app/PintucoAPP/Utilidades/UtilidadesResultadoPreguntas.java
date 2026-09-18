package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertResultadoPreguntas;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesResultadoPreguntas {

    public static final int COLUMNA_USUARIO = 2;
    public static final int COLUMNA_TEST_ID = 3;
    public static final int COLUMNA_TEST = 4;
    public static final int COLUMNA_QUESTION = 5;
    public static final int COLUMNA_OPTA = 6;
    public static final int COLUMNA_OPTB = 7;
    public static final int COLUMNA_OPTC = 8;
    public static final int COLUMNA_ANSWER = 9;
    public static final int COLUMNA_ANSWER_USER = 10;
    public static final int COLUMNA_RESULT = 11;
    public static final int COLUMNA_FECHA = 12;
    public static final int COLUMNA_HORA = 13;



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

        String usuario_id;
        String usuario;
        String test_id;
        String test;
        String question;
        String opta;
        String optb;
        String optc;
        String answer;
        String answer_user;
        String result;
        String fecha;
        String hora;



        usuario = c.getString(COLUMNA_USUARIO);
        test_id = c.getString(COLUMNA_TEST_ID);
        test = c.getString(COLUMNA_TEST);
        question = c.getString(COLUMNA_QUESTION);
        opta = c.getString(COLUMNA_OPTA);
        optb = c.getString(COLUMNA_OPTB);
        optc = c.getString(COLUMNA_OPTC);
        answer = c.getString(COLUMNA_ANSWER);
        answer_user = c.getString(COLUMNA_ANSWER_USER);
        result = c.getString(COLUMNA_RESULT);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);


        try {
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO, usuario);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID, test_id);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST, test);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_QUES, question);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTA, opta);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTB, optb);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTC, optc);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER, answer);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER, answer_user);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_RESULT, result);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_FECHA, fecha);
            jObject.put(ContractInsertResultadoPreguntas.Columnas.KEY_HORA, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
