package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractInsertPreguntas {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_PREGUNTAS = "insert_preguntas";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PREGUNTAS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PREGUNTAS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PREGUNTAS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PREGUNTAS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PREGUNTAS + "/#", SINGLE_ROW);
    }

    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String USUARIO = "user";
        public final static String TEST_ID = "test_id";
        public final static String TEST = "test";
        public final static String P1 = "p1";
        public final static String P2 = "p2";
        public final static String P3 = "p3";
        public final static String P4 = "p4";
        public final static String P5 = "p5";
        public final static String P6 = "p6";
        public final static String P7 = "p7";
        public final static String P8 = "p8";
        public final static String P9 = "p9";
        public final static String P10 = "p10";
        public final static String P11 = "p11";
        public final static String P12 = "p12";
        public final static String P13 = "p13";
        public final static String P14 = "p14";
        public final static String P15 = "p15";
        public final static String CORRECTAS = "correctas";
        public final static String INCORRECTAS = "incorrectas";
        public final static String CALIFICACION = "calificacion";
        public final static String OBSERVACION = "observacion";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";
        public final static String CRONOMETO = "cronometro";
        public final static String ESTADO_TEST = "estado_test";

    }

}
