package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractInsertMaterialesRecibidos {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_MATERIALES_RECIBIDOS = "insert_materiales_recibidos";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_MATERIALES_RECIBIDOS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_MATERIALES_RECIBIDOS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_MATERIALES_RECIBIDOS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_MATERIALES_RECIBIDOS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_MATERIALES_RECIBIDOS + "/#", SINGLE_ROW);
    }

    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String PHARMA_ID = "pharma_id";
        public final static String CODIGO = "codigo";
        public final static String CANAL = "canal";
        public final static String NOMBRE_COMERCIAL = "nombre_comercial";
        public final static String LOCAL = "local";
        public final static String REGION = "region";
        public final static String PROVINCIA = "provincia";
        public final static String CIUDAD = "ciudad";
        public final static String ZONA = "zona";
        public final static String DIRECCION = "direccion";
        public final static String SUPERVISOR = "supervisor";
        public final static String MERCADERISTA = "mercaderista";
        public final static String USUARIO = "usuario";
        public final static String LATITUD = "latitud";
        public final static String LONGITUD = "longitud";
        public final static String TERRITORIO = "territorio";
        public final static String ZONA_TERRITORIO = "zona_territorio";
        public final static String ALERTA = "alerta";
        public final static String TIPO = "tipo";
        public final static String MATERIAL = "material";
        public final static String CANTIDAD = "cantidad";
        public final static String ESTADO_MATERIAL = "estado_material";
        public final static String PRIORIDAD = "prioridad";
        public final static String OBSERVACIONES = "observaciones";
        public final static String FOTO = "foto";
        public final static String FECHA = "fecha";
        public final static String HORA = "hora";
    }
}
