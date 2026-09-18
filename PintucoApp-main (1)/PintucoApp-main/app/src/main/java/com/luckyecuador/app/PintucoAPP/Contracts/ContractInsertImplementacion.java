package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractInsertImplementacion {

    public static final String INSERT_IMPLEM = "insert_implementacion";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_IMPLEM;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_IMPLEM;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_IMPLEM);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_IMPLEM, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_IMPLEM + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String USUARIO = "user";
        public final static String FECHA="fecha";
        public final static String HORA="hora";
        public final static String CIUDAD = "city";
        public final static String CANAL = "canal";
        public final static String CLIENTE = "cliente";
        public final static String FORMATO = "subchannel";
        public final static String ZONA = "zone";
        public final static String PDV = "pdv";
        public final static String DIRECCION = "address";
        public final static String LOCAL = "local";
        public final static String LATITUD = "latitud";
        public final static String LONGITUD = "longitud";
        public final static String FOTO = "foto";

    }


}
