package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractMetas {
    /**
     * Representación de la tabla a consultar
     */
    public static final String METAS = "repo_metas";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */

    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + METAS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + METAS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + METAS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, METAS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, METAS + "/#", SINGLE_ROW);
    }

    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String MODULO = "modulo";
        public final static String USUARIO = "usuario";
        public final static String CODIGO = "codigo_pdv";
        public final static String CANAL = "canal";
        public final static String METAF = "meta_f";
        public final static String METAHC = "meta_hc";
        public final static String FECHAINICIO = "fecha_inicio";
        public final static String FECHAFIN = "fecha_fin";

     //   public final static String MARCA_FULL = "marca_full";// CONCATENADO MARCA+SUBCATEGORIA
    }
}
