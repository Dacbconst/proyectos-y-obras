package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractInsertAlmuerzo {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_ALMUERZO = "insert_almuerzo";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_ALMUERZO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_ALMUERZO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI = Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_ALMUERZO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_ALMUERZO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_ALMUERZO + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }


        public final static String USUARIO = "usuario";
        public final static String JORNADA_LABORAL = "jornada_laboral";
        public final static String TIEMPO_ALMUERZO = "tiempo_almuerzo";
        public final static String TIEMPO_FUERA = "tiempo_fuera";
        public final static String FOTO = "foto";
        public final static String LATITUD = "latitud";
        public final static String LONGITUD = "longitud";
        public final static String FECHA = "fecha";
        public final static String HORA_INI_ALMUERZO = "hora_ini_almuerzo";
        public final static String HORA_FIN_ALMUERZO = "hora_fin_almuerzo";



    }
}
