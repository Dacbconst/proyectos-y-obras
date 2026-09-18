package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractCausalesAsistenciaAtraso {

    public static final String CAUSALES_ASISTENCIA_ATRASO = "repo_causales_asistencia_atraso";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constantes.AUTHORITY + "/" + CAUSALES_ASISTENCIA_ATRASO);

    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;

    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, CAUSALES_ASISTENCIA_ATRASO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, CAUSALES_ASISTENCIA_ATRASO + "/#", SINGLE_ROW);
    }

    public static final String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + "." + CAUSALES_ASISTENCIA_ATRASO;
    public static final String SINGLE_MIME = "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + "." + CAUSALES_ASISTENCIA_ATRASO;

    public static class Columnas {
        public static final String _ID = "_id";
        public static final String ID = Constantes.ID_REMOTA;
        public static final String DESCRIPCION = "descripcion";
    }
}