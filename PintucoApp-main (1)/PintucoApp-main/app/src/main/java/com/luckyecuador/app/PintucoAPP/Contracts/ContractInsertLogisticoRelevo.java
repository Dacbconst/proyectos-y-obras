package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractInsertLogisticoRelevo {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_LOGISTICO_RELEVO = "insert_logistico_relevo";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_LOGISTICO_RELEVO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_LOGISTICO_RELEVO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_LOGISTICO_RELEVO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_LOGISTICO_RELEVO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_LOGISTICO_RELEVO + "/#", SINGLE_ROW);
    }

    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }
        public final static String PHARMA_ID = "id";
        public final static String CODIGO = "codigo";
        public final static String USUARIO = "usuario";

        public final static String SUPERVISOR = "supervisor";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";

        public final static String CATEGORIA = "categoria";
        public final static String BRAND = "brand";
        public final static String SKU_CODE = "sku_code";

        public final static String PREGULAR = "regular_price";
        public final static String CAUSAL = "causal";
        public final static String TIPO_LOGISTICO = "tipo_logistico";

        public final static String FOTO = "foto";
        public final static String COMENTARIO = "comentario";
        public final static String FECHA_PROD_CADUCADO = "fecha_prod_caducado";

        public final static String FECHA_PROD_PROPENSO = "fecha_prod_propenso";
    }

}
