package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;



/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractInsertResultadoPreguntas {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_RESULTADO_PREGUNTAS = "insert_resultado_preguntas";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_RESULTADO_PREGUNTAS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_RESULTADO_PREGUNTAS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_RESULTADO_PREGUNTAS);
    /**
     * Comparador de URIs de contenido
     */
    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ALLROWS = 1;
    /**
     * Código para URIS de un solo registro
     */
    public static final int SINGLE_ROW = 2;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_RESULTADO_PREGUNTAS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_RESULTADO_PREGUNTAS + "/#", SINGLE_ROW);
    }

    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public static final String KEY_USUARIO= "user"; //Usuario
        public static final String KEY_TEST_ID= "test_id"; //Test_id
        public static final String KEY_TEST= "test"; //Test
        public static final String KEY_QUES = "question";
        public static final String KEY_OPTA= "opta"; //option a
        public static final String KEY_OPTB= "optb"; //option b
        public static final String KEY_OPTC= "optc"; //option c
        public static final String KEY_ANSWER = "answer"; //correct option
        public static final String KEY_ANSWER_USER = "answer_user";
        public static final String KEY_RESULT = "result";
        public static final String KEY_FECHA = "fecha";
        public static final String KEY_HORA = "hora";



    }

}

