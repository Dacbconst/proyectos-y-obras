package com.luckyecuador.app.PintucoAPP.Contracts;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 27/02/2018.
 */

public class ContractRotacion {
    public static final String ROTACION = "rotacion";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + ROTACION;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + ROTACION;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + ROTACION);
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
        uriMatcher.addURI(Constantes.AUTHORITY, ROTACION, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, ROTACION + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        //public final static String CODIGO_PRODUCTO = "codigo_producto";
        public final static String CATEGORIA = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String MARCA = "marca";
        public final static String PRODUCTO = "producto";
        public final static String PROMOCIONAL = "promocional";
        public final static String MECANICA = "mecanica";
        public final static String PESO = "peso";
        public final static String TIPO = "tipo";
        public final static String PLATAFORMA = "plataforma";
        /*public final static String MANUFACTURER = "manufacturer";
        public final static String FORMAT = "format";*/


    }
}
