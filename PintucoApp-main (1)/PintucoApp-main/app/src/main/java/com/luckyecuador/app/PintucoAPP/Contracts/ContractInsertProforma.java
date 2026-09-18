package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Tabla insert_proforma — una o varias filas por cada agendamiento de
 * insert_proyectos_contacto (relación vía ID_AGENDAMIENTO, que guarda el
 * id_remota del agendamiento). Permite múltiples visitas/evidencias por
 * agendamiento a lo largo del tiempo.
 */
public class ContractInsertProforma {

    public static final String INSERT_PROFORMA = "insert_proforma";

    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PROFORMA;

    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PROFORMA;

    public final static Uri CONTENT_URI = Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PROFORMA);

    public static final UriMatcher uriMatcher;

    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROFORMA, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROFORMA + "/#", SINGLE_ROW);
    }

    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String ID_AGENDAMIENTO = "id_agendamiento";
        public final static String CODIGO_PDV = "codigo_pdv";
        public final static String USUARIO = "usuario";
        public final static String FECHA_PROFORMA = "fecha_proforma";
        public final static String ESTADO_PROFORMA = "estado_proforma";
        public final static String EVIDENCIA = "evidencia";
        public final static String CARACTERISTICA_VISITA = "caracteristica_visita";
        public final static String ACOMPANAMIENTO_TECNICO = "acompanamiento_tecnico";
        public final static String FASE_ACTUAL = "fase_actual";
        public final static String FOTO_FACTURA = "foto_factura";
        public final static String FECHA_FACTURA = "fecha_factura";
        public final static String MONTO_VALIDADO = "monto_validado";
        public final static String OBSERVACIONES_AUDITORIA = "observaciones_auditoria";
        public final static String FECHA_AUDITORIA = "fecha_auditoria";
        // Solo pobladas en la fila de factura: total acordado y plazo en meses
        // (NULL o 0 = pago único).
        public final static String MONTO_TOTAL_FACTURA = "monto_total_factura";
        public final static String PLAZO_MESES = "plazo_meses";
        // Solo en la fila de factura: progreso de pago para que la web pueda
        // filtrar sin tener que contar filas de insert_pago_factura — lo
        // calcula y escribe la app cada vez que sube la factura o un pago
        // ("pendiente" = sin pagos, "en_proceso" = a plazos con algunos pagos
        // pero no todos, "completado" = ya se subieron los que hacían falta).
        public final static String ESTADO_PAGO = "estado_pago";
        // Cierre manual e irreversible del proceso, cada uno con su propia
        // observación obligatoria (ver ProformaFragment "Cerrar Proceso"):
        // MOTIVO_CIERRE = se cierra la negociación de proforma sin llegar a
        // facturar (botón en "ENVÍOS DE PROFORMA"); MOTIVO_CIERRE_PAGO = se
        // corta el cobro de una factura a plazos antes de llegar al monto
        // total cotizado (botón en "FACTURA"). No confundir con el cierre
        // automático de estado_pago=completado, que no pide observación.
        public final static String MOTIVO_CIERRE = "motivo_cierre";
        public final static String MOTIVO_CIERRE_PAGO = "motivo_cierre_pago";

    }
}
