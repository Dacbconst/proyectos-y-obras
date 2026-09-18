package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 27/02/2018.
 */

public class ContractPortafolioProductosMAYO {
    public static final String PORTAFOLIOPRODUCTOS_MAYO = "portafolioproductos_mayo";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + PORTAFOLIOPRODUCTOS_MAYO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + PORTAFOLIOPRODUCTOS_MAYO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + PORTAFOLIOPRODUCTOS_MAYO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, PORTAFOLIOPRODUCTOS_MAYO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, PORTAFOLIOPRODUCTOS_MAYO + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String CODIGO = "codigo";
        public final static String USUARIO = "usuario";
        public final static String CATEGORIA = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String MARCA = "marca";
        public final static String FABRICANTE = "fabricante";
        public final static String SKU = "sku";
        public final static String STATUS = "status";

    }
}
