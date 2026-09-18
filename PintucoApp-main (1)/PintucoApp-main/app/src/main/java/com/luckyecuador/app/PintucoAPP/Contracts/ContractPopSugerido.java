package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 27/07/2021.
 */


public class ContractPopSugerido {


    public static final String POPSUGERIDO = "pop_sugerido";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + POPSUGERIDO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + POPSUGERIDO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + POPSUGERIDO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, POPSUGERIDO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, POPSUGERIDO + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        //public final static String CODIGO_PRODUCTO = "codigo_producto";
     //   public final static String ID = "id";
        public final static String CANAL = "canal";
        public final static String CODIGO_PDV = "codigo_pdv";
        public final static String POP_SUGERIDO = "pop_sugerido";
        /*public final static String MANUFACTURER = "manufacturer";
        public final static String FORMAT = "format";*/


    }
}

