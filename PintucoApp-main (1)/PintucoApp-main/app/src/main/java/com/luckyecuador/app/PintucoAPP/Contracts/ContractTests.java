package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;


/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractTests {
    /**
     * Representación de la tabla a consultar
     */                       /*TABLE QUEST = "quest"*/
    public static final String TABLE_TEST = "repositorio_test";

    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + TABLE_TEST;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + TABLE_TEST;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + TABLE_TEST);
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
        uriMatcher.addURI(Constantes.AUTHORITY, TABLE_TEST, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, TABLE_TEST + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public  static final String KEY_TEST_ID = "test_id";
        public  static final String KEY_TEST = "test";
        public static final String KEY_DESCRIPTION = "descripcion";
        public static final String KEY_DATE_START = "f_inicio";
        public static final String KEY_HOUR_START = "h_inicio";
        public static final String KEY_DATE_LIMIT = "f_limite";
        public static final String KEY_HOUR_LIMIT = "h_limite";
        public static final String KEY_ACTIVE = "active";

    }

}
