package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class ContractInsertImpulso {

    public static final String INSERT_IMPUSLO = "insert_impulso";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_IMPUSLO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_IMPUSLO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_IMPUSLO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_IMPUSLO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_IMPUSLO + "/#", SINGLE_ROW);
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

        public final static String CATEGORIA = "categoria";//SI
        public final static String BRAND = "brand";//SI
        public final static String SKU_CODE = "sku_code";//SI

        public final static String CANTIDAD_ASIGNADA = "asignada";
        public final static String CANTIDAD_VENDIDA = "vendida";
        public final static String CANTIDAD_ADICIONAL = "adicional";
        public final static String CUMPLIMIENTO = "cumplimiento";

        public final static String IMPULSADORA = "impulsadora";
        public final static String OBSERVACION = "observacion";
        public final static String FOTO = "foto";
        public final static String POS_NAME = "pos_name";
        public final static String PRECIO_VENTA = "precio_venta";
        public final static String ALERTA_STOCK = "alerta_stock";
        public final static String PLATAFORMA = "plataforma";

    }
}
