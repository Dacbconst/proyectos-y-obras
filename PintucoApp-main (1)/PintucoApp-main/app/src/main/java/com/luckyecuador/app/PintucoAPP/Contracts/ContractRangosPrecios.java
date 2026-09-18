package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractRangosPrecios {
    public static final String RANGOS_PRECIOS = "repo_rangos_precios";

    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + RANGOS_PRECIOS);

    public static final UriMatcher uriMatcher;
    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;
    public static final int BY_PRESENTACION = 3;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS + "/#", SINGLE_ROW);
        uriMatcher.addURI(Constantes.AUTHORITY, RANGOS_PRECIOS + "/*", BY_PRESENTACION);
    }

    public static class Columnas implements BaseColumns {
        private Columnas() {}

        public final static String PRESENTACION = "presentacion";
        public final static String MINIMO = "minimo";
        public final static String MAXIMO = "maximo";
    }
}
