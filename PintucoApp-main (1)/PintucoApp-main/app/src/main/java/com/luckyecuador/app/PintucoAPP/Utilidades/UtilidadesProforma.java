package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesProforma {

    /** Callback del diálogo "Cerrar Proceso" — el llamador decide a qué columna escribe. */
    public interface OnConfirmarCierre {
        void confirmar(String observacion);
    }

    private static final long DURACION_ERROR_MS = 2500;

    /** Borde rojo + globo de error que se autolimpia — mismo patrón que ContactoFragment/ProformaFragment. */
    private static void mostrarErrorTemporal(EditText editText, String mensaje) {
        editText.setError(mensaje);
        editText.postDelayed(() -> {
            if (mensaje.equals(String.valueOf(editText.getError()))) {
                editText.setError(null);
            }
        }, DURACION_ERROR_MS);
    }

    /**
     * Diálogo compacto "Cerrar Proceso" (observación final obligatoria, máx. 250, antes de
     * un cierre irreversible) — compartido entre ProformaFragment ("Cierre Proforma") y
     * FacturasFragment ("Cierre Factura"); el llamador decide a qué columna escribe.
     */
    public static void mostrarDialogoCerrarProceso(Context context, OnConfirmarCierre onConfirmar) {
        View vista = LayoutInflater.from(context).inflate(R.layout.dialog_proforma_cerrar_proceso, null);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarProcesoDialog);
        EditText etObservaciones = vista.findViewById(R.id.etObservacionesCierre);
        TextView tvContador = vista.findViewById(R.id.tvContadorObservacionesCierre);
        TextView btnConfirmar = vista.findViewById(R.id.btnConfirmarCierre);
        TextView btnCancelar = vista.findViewById(R.id.btnCancelarCierre);

        tvContador.setText(context.getString(R.string.proforma_cerrar_proceso_contador, 0));
        etObservaciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvContador.setText(context.getString(R.string.proforma_cerrar_proceso_contador, s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // No cancelable: perder sin querer una observación de 250 caracteres con un
        // toque afuera (mismo criterio que FacturasFragment.mostrarDetallePago) es
        // peor que obligar a tocar "Cancelar" explícitamente.
        AlertDialog dialog = new AlertDialog.Builder(context).setView(vista).setCancelable(false).create();
        ivCerrar.setOnClickListener(v -> dialog.dismiss());
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnConfirmar.setOnClickListener(v -> {
            String observacion = etObservaciones.getText().toString().trim();
            if (observacion.isEmpty()) {
                mostrarErrorTemporal(etObservaciones, context.getString(R.string.proforma_cerrar_proceso_error_vacio));
                return;
            }
            // Deshabilitar antes de dismiss(): un doble-toque rápido no debe disparar
            // onConfirmar dos veces — el SyncAdapter usa un campo estático y no tolera
            // sincronizarAhora() llamado dos veces seguidas.
            btnConfirmar.setEnabled(false);
            dialog.dismiss();
            onConfirmar.confirmar(observacion);
        });
        dialog.show();
    }

    // Indices para las columnas indicadas en PROJECTION_INSERTPROFORMA
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_ID_AGENDAMIENTO = 2;
    public static final int COLUMNA_CODIGO_PDV = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_FECHA_PROFORMA = 5;
    public static final int COLUMNA_ESTADO_PROFORMA = 6;
    public static final int COLUMNA_EVIDENCIA = 7;
    public static final int COLUMNA_CARACTERISTICA_VISITA = 8;
    public static final int COLUMNA_ACOMPANAMIENTO_TECNICO = 9;
    public static final int COLUMNA_FASE_ACTUAL = 10;
    public static final int COLUMNA_FOTO_FACTURA = 11;
    public static final int COLUMNA_FECHA_FACTURA = 12;
    public static final int COLUMNA_MONTO_VALIDADO = 13;
    public static final int COLUMNA_OBSERVACIONES_AUDITORIA = 14;
    public static final int COLUMNA_FECHA_AUDITORIA = 15;
    public static final int COLUMNA_MONTO_TOTAL_FACTURA = 16;
    public static final int COLUMNA_PLAZO_MESES = 17;
    public static final int COLUMNA_ESTADO_PAGO = 18;
    public static final int COLUMNA_MOTIVO_CIERRE = 19;
    public static final int COLUMNA_MOTIVO_CIERRE_PAGO = 20;

    /**
     * Copia los datos de un registro de Proforma almacenados en un cursor
     * hacia un JSONObject, para la subida al servidor.
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();

        String idRemota = c.getString(COLUMNA_ID_REMOTA);
        String idAgendamiento = c.getString(COLUMNA_ID_AGENDAMIENTO);
        String codigoPdv = c.getString(COLUMNA_CODIGO_PDV);
        String usuario = c.getString(COLUMNA_USUARIO);
        String fechaProforma = c.getString(COLUMNA_FECHA_PROFORMA);
        String estadoProforma = c.getString(COLUMNA_ESTADO_PROFORMA);
        String evidencia = c.getString(COLUMNA_EVIDENCIA);
        String caracteristicaVisita = c.getString(COLUMNA_CARACTERISTICA_VISITA);
        String acompanamientoTecnico = c.getString(COLUMNA_ACOMPANAMIENTO_TECNICO);
        String faseActual = c.getString(COLUMNA_FASE_ACTUAL);
        String fotoFactura = c.getString(COLUMNA_FOTO_FACTURA);
        String fechaFactura = c.getString(COLUMNA_FECHA_FACTURA);
        String montoValidado = c.getString(COLUMNA_MONTO_VALIDADO);
        String montoTotalFactura = c.getString(COLUMNA_MONTO_TOTAL_FACTURA);
        String plazoMeses = c.getString(COLUMNA_PLAZO_MESES);
        String estadoPago = c.getString(COLUMNA_ESTADO_PAGO);
        String motivoCierre = c.getString(COLUMNA_MOTIVO_CIERRE);
        String motivoCierrePago = c.getString(COLUMNA_MOTIVO_CIERRE_PAGO);

        try {
            // Si ya tiene id_remota, el PHP debe hacer UPDATE en vez de INSERT — si no,
            // cada edición posterior (cambiar estado, evidencia, característica) crea
            // una fila nueva en el servidor en vez de actualizar la existente.
            jObject.put(Constantes.ID_REMOTA, idRemota);
            jObject.put(ContractInsertProforma.Columnas.ID_AGENDAMIENTO, idAgendamiento);
            jObject.put(ContractInsertProforma.Columnas.CODIGO_PDV, codigoPdv);
            jObject.put(ContractInsertProforma.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertProforma.Columnas.FECHA_PROFORMA, fechaProforma);
            jObject.put(ContractInsertProforma.Columnas.ESTADO_PROFORMA, estadoProforma);
            jObject.put(ContractInsertProforma.Columnas.EVIDENCIA, evidencia);
            jObject.put(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA, caracteristicaVisita);
            jObject.put(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO, acompanamientoTecnico);
            jObject.put(ContractInsertProforma.Columnas.FASE_ACTUAL, faseActual != null ? faseActual : "3");
            if (fotoFactura != null && !fotoFactura.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.FOTO_FACTURA, fotoFactura);
            }
            if (fechaFactura != null && !fechaFactura.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.FECHA_FACTURA, fechaFactura);
            }
            // monto_validado ya no es exclusivo de oficina: el promotor también
            // puede escribirlo desde el diálogo "Detalle de la visita".
            if (montoValidado != null && !montoValidado.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.MONTO_VALIDADO, montoValidado);
            }
            if (montoTotalFactura != null && !montoTotalFactura.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA, montoTotalFactura);
            }
            if (plazoMeses != null && !plazoMeses.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.PLAZO_MESES, plazoMeses);
            }
            if (estadoPago != null && !estadoPago.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.ESTADO_PAGO, estadoPago);
            }
            if (motivoCierre != null && !motivoCierre.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.MOTIVO_CIERRE, motivoCierre);
            }
            if (motivoCierrePago != null && !motivoCierrePago.isEmpty()) {
                jObject.put(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO, motivoCierrePago);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-Proforma", String.valueOf(jObject));

        return jObject;
    }

    /**
     * Reemplazo seguro de optString para columnas que pueden venir NULL del
     * servidor (mismo problema que ya se corrigió en UtilidadesProyectosContacto:
     * optString devuelve el texto literal "null" en vez de vacío).
     */
    public static String optStringSeguro(JSONObject json, String key) {
        return optStringSeguro(json, key, "");
    }

    public static String optStringSeguro(JSONObject json, String key, String valorPorDefecto) {
        if (json == null || !json.has(key) || json.isNull(key)) {
            return valorPorDefecto;
        }
        return json.optString(key, valorPorDefecto);
    }

    /** Suma monto_pago de todas las cuotas ya subidas (insert_pago_factura) de una factura a plazos. */
    public static double sumarPagos(ContentResolver resolver, String idProformaFactura) {
        double total = 0;
        try (Cursor c = resolver.query(ContractInsertPagoFactura.CONTENT_URI,
                new String[]{ContractInsertPagoFactura.Columnas.MONTO_PAGO},
                ContractInsertPagoFactura.Columnas.ID_PROFORMA + "=?",
                new String[]{idProformaFactura}, null)) {
            if (c != null) {
                int col = c.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.MONTO_PAGO);
                while (c.moveToNext()) {
                    if (!c.isNull(col)) {
                        total += c.getDouble(col);
                    }
                }
            }
        }
        return total;
    }

    /**
     * pendiente = nada pagado aún; completado = ya cubrió (o superó) el total cotizado;
     * en_proceso = a medias. Compara en centavos (no el double crudo): sumar montos en
     * punto flotante puede dejar el acumulado a una fracción de centavo del total exacto
     * (ej. 3999.9999999999995 en vez de 4000.00) y el ">=" nunca dispararía.
     */
    public static String calcularEstadoPago(Double montoTotalFactura, double sumaPagos) {
        if (sumaPagos <= 0) {
            return Constantes.ESTADO_PAGO_PENDIENTE;
        }
        if (montoTotalFactura != null) {
            long centavosPagados = Math.round(sumaPagos * 100);
            long centavosTotal = Math.round(montoTotalFactura * 100);
            if (centavosPagados >= centavosTotal) {
                return Constantes.ESTADO_PAGO_COMPLETADO;
            }
        }
        return Constantes.ESTADO_PAGO_EN_PROCESO;
    }

    /** true = ya se cerró a mano desde "Cierre Factura" (motivo_cierre_pago no vacío). */
    private static boolean tieneMotivoCierrePago(ContentResolver resolver, String idProformaFactura) {
        try (Cursor c = resolver.query(ContractInsertProforma.CONTENT_URI,
                new String[]{ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO},
                Constantes.ID_REMOTA + "=?", new String[]{idProformaFactura}, null)) {
            if (c != null && c.moveToFirst()) {
                String motivo = c.getString(0);
                return motivo != null && !motivo.trim().isEmpty();
            }
        }
        return false;
    }

    /**
     * Recalcula y persiste estado_pago de la fila de factura (insert_proforma,
     * identificada por su id_remota) tras subir un pago — auto-cierre: una vez
     * "completado" deja de pedirse foto nueva (ver FacturasFragment/ProformaFragment).
     * No hace nada si la factura ya se cerró a mano (motivo_cierre_pago): ese cierre
     * es irreversible y no debe pisarse con un recálculo automático posterior.
     */
    public static void actualizarEstadoPago(ContentResolver resolver, String idProformaFactura, Double montoTotalFactura) {
        if (tieneMotivoCierrePago(resolver, idProformaFactura)) {
            return;
        }
        double sumaPagos = sumarPagos(resolver, idProformaFactura);
        String estadoPago = calcularEstadoPago(montoTotalFactura, sumaPagos);
        ContentValues valores = new ContentValues();
        valores.put(ContractInsertProforma.Columnas.ESTADO_PAGO, estadoPago);
        valores.put(Constantes.PENDIENTE_INSERCION, 1);
        valores.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
        resolver.update(ContractInsertProforma.CONTENT_URI, valores,
                Constantes.ID_REMOTA + "=?", new String[]{idProformaFactura});
    }

}
