package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractInsertValores {

    public static final String INSERT_VALORES = "insert_valores";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_VALORES;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_VALORES;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_VALORES);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_VALORES, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_VALORES + "/#", SINGLE_ROW);
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
        public final static String SEGMENTO1 = "segment1";
        //public final static String POFERTA = "segment2";
        public final static String BRAND = "brand";
        /*public final static String TAMANO = "tamano";
        public final static String CANTIDAD = "cantidad";*/
        public final static String SKU_CODE = "sku_code";
        public final static String CODIFICA = "codifica";
        public final static String AUSENCIA = "ausencia";
        public final static String DISPONIBLE = "disponible";
        public final static String RESPONSABLE = "tipo";
        public final static String RAZONES = "descripcion";
        public final static String SUGERIDO = "sugerido";
        public final static String TIPO_SUGERIDO = "tipo_sugerido";
        public final static String PVP = "pvp";
        public final static String PVC = "pvc";
        public final static String POFERTA = "precio_oferta";
        public final static String MANUFACTURER = "manufacturer";
        public final static String QUIEBRE_PERCHA = "quiebre_percha";
        public final static String QUIEBRE_BODEGA = "quiebre_bodega";
        public final static String POS_NAME = "pos_name";
        public final static String PLATAFORMA = "plataforma";
    }
}
