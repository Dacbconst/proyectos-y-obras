package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;


/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class ContractPharmaValue {
    /**
     * Representación de la tabla a consultar
     */
    public static final String POS = "pharmavalue";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + POS;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + POS;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + Constantes.AUTHORITY + "/" + POS);
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
        uriMatcher.addURI(Constantes.AUTHORITY, POS, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, POS + "/#", SINGLE_ROW);
    }


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String CHANNEL = "channel";
        public final static String SUBCHANNEL = "subchannel";
        public final static String CHANNEL_SEGMENT = "channel_segment";
        public final static String FORMAT = "format";
        public final static String CUSTOMER_OWNER = "customer_owner";
        public final static String POS_ID = "pos_id";
        public final static String POS_NAME = "pos_name";
        public final static String POS_NAME_DPSM = "pos_name_dpsm";
        public final static String ZONA = "zone";
        public final static String REGION = "region";
        public final static String PROVINCIA = "province";
        public final static String CIUDAD = "city";
        public final static String DIRECCION = "address";
        public final static String KAM = "kam";
        public final static String SALES_EXECUTIVE = "sales_executive";
        public final static String MERCHANDISING = "merchandising";
        public final static String SUPERVISOR = "supervisor";
        public final static String MERCADERISTA = "mercaderista";
        public final static String USER = "user";
        public final static String DPSM = "dpsm";
        public final static String STATUS = "status";
        public final static String TIPO = "tipo";
        public final static String LATITUD = "latitud";
        public final static String LONGITUD = "longitud";
        public final static String FOTO = "foto";
        public final static String SEGMENTACION = "segmentacion";
        public final static String COMPRAS = "compras";
        public final static String PASS = "pass";
        public final static String NUMERO_CONTROLLER = "numero_controller";
        public final static String FECHA_VISITA = "fecha_visita";
        public final static String DEVICE_ID = "device_id";
        public final static String PERIMETRO = "perimetro";
        public final static String DISTANCIA = "distancia";
        public final static String TERMOMETRO = "termometro";
        public final static String HORA_INICIO = "hora_inicio";
        public final static String HORA_FIN = "hora_fin";
    }

}
