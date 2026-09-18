package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class InsertFlooring {

    public static final String INSERT_FLOORING = "insert_flooring";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_FLOORING;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_FLOORING;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_FLOORING);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_FLOORING, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_FLOORING + "/#", SINGLE_ROW);
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
        public final static String PRESENTACION = "presentacion";
        public final static String BRAND = "brand";
        public final static String CONTENIDO = "contenido";
        public final static String SKU_CODE = "sku_code";
        public final static String INVENTARIOS = "inventarios";
        public final static String SEMANA = "semana";
        public final static String SUGERIDOS = "sugeridos";
        public final static String TIPO = "tipo_unidades";
        public final static String ENTREGA = "entrega";
        public final static String CAUSAL = "causal";
        public final static String OTROS = "otros";
        public final static String FECHA_CADUCIDAD = "caducidad";
        public final static String POS_NAME = "pos_name";
        public final static String PLATAFORMA = "plataforma";
        public final static String FECHA_INVENTARIO = "fecha_inventario";

    }
}
