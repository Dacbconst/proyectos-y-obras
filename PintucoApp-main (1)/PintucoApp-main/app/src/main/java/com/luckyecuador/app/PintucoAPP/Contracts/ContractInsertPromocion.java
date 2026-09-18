package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 17/04/2018.
 */

public class ContractInsertPromocion {

    public static final String INSERT_PROMO = "insert_promocion_tb";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PROMO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PROMO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PROMO);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROMO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROMO + "/#", SINGLE_ROW);
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

        public final static String CATEGORIA = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String MARCA = "brand";
        public final static String CANAL = "canal";
        public final static String TIPO_PROMOCION = "tipo_promocion";
        public final static String DESCRIPCION_PROMOCION = "descripcion_promocion";
        public final static String PRODUCTO = "producto";
        public final static String MECANICA = "mecanica";
        public final static String INI_PROMO = "inicio_promocion";
        public final static String FIN_PROMO = "fin_promocion";
        public final static String AGOTAR_STOCK = "agotar_stock";
        public final static String PVC_ANTERIOR = "pvc_anterior";
        public final static String PVC_ACTUAL = "pvc_actual";
        public final static String MARGEN_DSCTO = "margen_dscto";
        public final static String FOTO = "foto";
        public final static String MANUFACTURER = "manufacturer";
        public final static String SKU = "sku";
        public final static String POS_NAME = "pos_name";
        public final static String PLATAFORMA = "plataforma";
    }
}
