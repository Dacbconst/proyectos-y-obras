package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 27/02/2018.
 */

public class ContractPortafolioProductos {
    public static final String PORTAFOLIOPRODUCTOS = "portafolioproductos";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + PORTAFOLIOPRODUCTOS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + PORTAFOLIOPRODUCTOS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + PORTAFOLIOPRODUCTOS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, PORTAFOLIOPRODUCTOS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, PORTAFOLIOPRODUCTOS + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        //public final static String CODIGO_PRODUCTO = "codigo_producto";
        public final static String SECTOR = "sector";
        public final static String CATEGORY = "categoria";
        public final static String SUBCATEGORIA = "subcategoria";
        public final static String SEGMENTO = "segmento";
        public final static String PRESENTACION = "presentacion";
        public final static String VARIANTE1 = "variante1";
        public final static String VARIANTE2 = "variante2";
        public final static String SKU = "sku";
        public final static String CONTENIDO = "contenido";
        public final static String MARCA = "marca";
        public final static String FABRICANTE = "fabricante";
        public final static String PVP = "pvp";
        public final static String CADENAS = "cadenas";
        public final static String FOTO = "foto";
        public final static String PLATAFORMA = "plataforma";
        public final static String LOCALES = "locales";
        public final static String DOLAR = "dolar";
        /*public final static String MANUFACTURER = "manufacturer";
        public final static String FORMAT = "format";*/


    }
}
