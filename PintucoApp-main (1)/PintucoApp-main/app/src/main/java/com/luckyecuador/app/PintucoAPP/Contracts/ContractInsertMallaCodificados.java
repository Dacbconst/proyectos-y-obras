package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractInsertMallaCodificados {

    public static final String INSERT_MALLA_CODIFICADOS = "insert_malla_codificados";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_MALLA_CODIFICADOS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_MALLA_CODIFICADOS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_MALLA_CODIFICADOS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_MALLA_CODIFICADOS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_MALLA_CODIFICADOS + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String PHARMA_ID = "id";
        public final static String CODIGO = "pos_id";
        public final static String USUARIO = "user";
        public final static String SUPERVISOR = "supervisor";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";
        public final static String SECTOR = "sector";
        public final static String CATEGORIA = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String SEGMENTO1 = "segment1";
        //public final static String POFERTA = "segment2";
        public final static String BRAND = "brand";
        /*public final static String TAMANO = "tamano";
        public final static String CANTIDAD = "cantidad";*/
        public final static String SKU_CODE = "sku_code";
        public final static String CODIFICA = "codifica";
        public final static String OBSERVACION = "observacion";
        public final static String MANUFACTURER = "manufacturer";
        public final static String POS_NAME = "pos_name";

    }
}
