package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractRangosPreciosSku {
    public static final String RANGOS_PRECIOS_SKU = "repo_rangos_precios_sku";

    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + RANGOS_PRECIOS_SKU);

    public static final UriMatcher uriMatcher;
    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;
    public static final int BY_SKU = 3;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS_SKU, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS_SKU + "/#", SINGLE_ROW);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS_SKU + "/*", BY_SKU);
    }

    public static class Columnas implements BaseColumns {
        private Columnas() {}

        public final static String SKU = "sku";
        public final static String MINIMO = "minimo";
        public final static String MAXIMO = "maximo";
    }
}
