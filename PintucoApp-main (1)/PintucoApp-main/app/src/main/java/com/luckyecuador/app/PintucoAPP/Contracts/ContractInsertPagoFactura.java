package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Tabla insert_pago_factura — un pago/cuota por fila, de una factura a plazos
 * (fila de insert_proforma con foto_factura + plazo_meses > 0). ID_PROFORMA
 * guarda el id_remota de esa fila de factura, no el del agendamiento directo,
 * por si en el futuro hay más de una cotización por agendamiento.
 */
public class ContractInsertPagoFactura {

    public static final String INSERT_PAGO_FACTURA = "insert_pago_factura";

    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PAGO_FACTURA;

    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PAGO_FACTURA;

    public final static Uri CONTENT_URI = Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PAGO_FACTURA);

    public static final UriMatcher uriMatcher;

    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PAGO_FACTURA, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PAGO_FACTURA + "/#", SINGLE_ROW);
    }

    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String ID_PROFORMA = "id_proforma";
        public final static String ID_AGENDAMIENTO = "id_agendamiento";
        public final static String CODIGO_PDV = "codigo_pdv";
        public final static String USUARIO = "usuario";
        public final static String NUMERO_CUOTA = "numero_cuota";
        public final static String MONTO_PAGO = "monto_pago";
        public final static String FOTO_PAGO = "foto_pago";
        public final static String FECHA_PAGO = "fecha_pago";
        public final static String OBSERVACION = "observacion";

    }
}
