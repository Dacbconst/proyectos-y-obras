package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;


/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class ContractInsertPDI {


    public static final String INSERT_PDI = "insert_pdi";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PDI;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PDI;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PDI);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PDI, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PDI + "/#", SINGLE_ROW);
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
        public final static String CANAL = "canal";
        public final static String USUARIO = "user";
        public final static String SUPERVISOR = "supervisor";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";
        public final static String CATEGORIA = "categoria";
        public final static String MARCA_SELECCIONADA = "marca_seleccionada";
        public final static String CUMPLIMIENTO = "cumplimiento";
        public final static String UNIVERSO = "universo";
        public final static String CARAS = "caras";
        public final static String OTROS = "otros";
        public final static String FOTO = "image";
        public final static String OBJ_CATEGORIA = "obj_categoria";
        public final static String PART_CATEGORIA = "part_categoria";
        public final static String POS_NAME = "pos_name";
        public final static String PLATAFORMA = "plataforma";
    }
}
