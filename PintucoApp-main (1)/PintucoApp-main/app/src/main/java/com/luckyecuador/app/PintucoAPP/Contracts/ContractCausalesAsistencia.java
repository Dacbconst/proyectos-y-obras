package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractCausalesAsistencia {
    /**
     * Representación de la tabla en SQLite
     */
    public static final String CAUSALES_ASISTENCIA = "repo_causales_asistencia";

    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + CAUSALES_ASISTENCIA;

    /**
     * Tipo MIME que retorna la consulta de {@link #CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + CAUSALES_ASISTENCIA;

    /**
     * URI de contenido para el Provider
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + CAUSALES_ASISTENCIA);

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
        uriMatcher.addURI(Constantes.AUTHORITY, CAUSALES_ASISTENCIA, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, CAUSALES_ASISTENCIA + "/#", SINGLE_ROW);
    }

    /**
     * Columnas de la tabla
     */
    public static class Columnas implements BaseColumns {
        private Columnas() {
            // Sin instancias
        }

        public static final String ID = Constantes.ID_REMOTA;
        public static final String DESCRIPCION = "descripcion";
    }

    /**
     * Proyección por defecto (para SELECT *)
     */
    public static final String[] PROJECTION = new String[]{
            Columnas.ID,
            Columnas.DESCRIPCION
    };
}
