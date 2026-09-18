package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 23/03/2018.
 */

public class ContractInsertVentas {


    public static final String INSERT_VENTAS = "insert_ventas";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_VENTAS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_VENTAS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_VENTAS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_VENTAS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_VENTAS + "/#", SINGLE_ROW);
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

        public final static String PHARMA_ID = "id";
        public final static String CODIGO = "pos_id";
        public final static String USUARIO = "usuario";
        public final static String SUPERVISOR = "supervisor";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";
        public final static String FECHA_VENTA = "fecha_venta";
        public final static String CATEGORIA = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String PRESENTACION = "presentacion";
        public final static String MARCA = "marca";
        public final static String SKU_CODE = "sku_code";
        public final static String TIPO_VENTA = "tipo_venta";
        public final static String STOCK_INICIAL = "stock_inicial";
        public final static String CANTIDAD = "cantidad";
        public final static String PREGULAR = "regular_price";
        public final static String PPROMOCION = "promotional_price";
        public final static String POFERTA = "ofert_price";
        public final static String STOCK_FINAL = "stock_final";
        public final static String MANUFACTURER = "manufacturer";
        public final static String POS_NAME = "pos_name";
        public final static String FOTO = "foto";


    }
}
