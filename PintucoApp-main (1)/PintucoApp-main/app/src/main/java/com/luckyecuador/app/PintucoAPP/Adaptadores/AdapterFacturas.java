package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luckyecuador.app.PintucoAPP.Clase.FacturaConPagos;
import com.luckyecuador.app.PintucoAPP.Clase.RegistroPagoFactura;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMoneda;

import java.util.List;
import java.util.Set;

/**
 * Módulo "Facturas": una tarjeta por cada factura (item_factura_card.xml).
 * En la pill "Todo" la lista viene intercalada con separadores de mes
 * (item_facturas_separador_mes.xml, ver FacturasFragment) — de ahí que
 * {@code items} sea {@code List<Object>} en vez de {@code List<FacturaConPagos>}
 * y el adapter tenga dos tipos de vista. Mismo acordeón de una sola tarjeta a
 * la vez que usa AdapterProforma.
 */
public class AdapterFacturas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_SEPARADOR = 0;
    private static final int TIPO_FACTURA = 1;

    /** Fila estática "Junio 2026 · $250.00" — el texto ya viene armado desde FacturasFragment. */
    public static class SeparadorMes {
        public final String etiqueta;

        public SeparadorMes(String etiqueta) {
            this.etiqueta = etiqueta;
        }
    }

    public interface AccionesListener {
        void onVerFotoFactura(FacturaConPagos factura);
        void onVerFotoPago(RegistroPagoFactura pago);
        // "Adjuntar otra factura" — solo en tarjetas a plazos, sin límite de cantidad.
        void onAdjuntarPago(FacturaConPagos factura);
        // Cierre manual e irreversible: corta el cobro antes de llegar al total cotizado.
        void onCerrarFactura(FacturaConPagos factura);
    }

    private final List<Object> items;
    private final AccionesListener listener;
    // Vive en el Fragment (no aquí) para que sobreviva a cada recarga — ver AdapterProforma.
    private final Set<String> idsExpandidos;
    // "Todo" es una vista histórica de solo lectura (mezcla todos los meses) — ahí
    // no se ofrece "Adjuntar otra factura", solo en la pill "A plazos" (acotada al
    // mes que se está trabajando). Ver FacturasFragment.renderizarListaFiltrada().
    private final boolean permiteAdjuntarFactura;

    public AdapterFacturas(List<Object> items, AccionesListener listener, Set<String> idsExpandidos, boolean permiteAdjuntarFactura) {
        this.items = items;
        this.listener = listener;
        this.idsExpandidos = idsExpandidos;
        this.permiteAdjuntarFactura = permiteAdjuntarFactura;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof SeparadorMes ? TIPO_SEPARADOR : TIPO_FACTURA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TIPO_SEPARADOR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facturas_separador_mes, parent, false);
            return new SeparadorViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factura_card, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof SeparadorViewHolder) {
            ((SeparadorViewHolder) holder).tvSeparador.setText(((SeparadorMes) item).etiqueta);
            return;
        }
        FacturaConPagos factura = (FacturaConPagos) item;
        FacturaViewHolder facturaHolder = (FacturaViewHolder) holder;
        String clave = factura.claveUnica();
        boolean expandido = idsExpandidos.contains(clave);
        facturaHolder.bind(factura, expandido, listener, permiteAdjuntarFactura);

        facturaHolder.headerFactura.setOnClickListener(v -> {
            boolean yaExpandido = idsExpandidos.contains(clave);
            idsExpandidos.clear();
            if (!yaExpandido) {
                idsExpandidos.add(clave);
            }
            notifyDataSetChanged();
        });
    }

    /**
     * Posición de la tarjeta con esta clave única en la lista YA FILTRADA, o -1 si
     * no está (p. ej. quedó fuera del filtro/mes actual) — usado para el scroll
     * automático tras "Enviar factura" en Proforma (ver FacturasFragment).
     */
    public int indexOfClave(String clave) {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof FacturaConPagos && ((FacturaConPagos) item).claveUnica().equals(clave)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SeparadorViewHolder extends RecyclerView.ViewHolder {
        final TextView tvSeparador;

        SeparadorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeparador = (TextView) itemView;
        }
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        private final View headerFactura;
        private final View contenedorDetalleFactura;
        private final TextView tvFacturaLabel;
        private final TextView tvFechaAgendaFactura;
        private final TextView tvEmpresaFactura;
        private final TextView tvLugarFactura;
        private final FrameLayout contenedorFotoFactura;
        private final ImageView ivFotoFacturaThumb;
        private final TextView tvFacturaDetalle;
        private final TextView tvFacturaMontoPagado;
        private final TextView tvFacturaMontoTotal;
        private final LinearLayout contenedorMesesPagos;
        private final View btnAdjuntarPago;
        private final TextView tvBotonAdjuntarPago;
        private final TextView btnCierreFactura;

        FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            headerFactura = itemView.findViewById(R.id.headerFactura);
            contenedorDetalleFactura = itemView.findViewById(R.id.contenedorDetalleFactura);
            tvFacturaLabel = itemView.findViewById(R.id.tvFacturaLabel);
            tvFechaAgendaFactura = itemView.findViewById(R.id.tvFechaAgendaFactura);
            tvEmpresaFactura = itemView.findViewById(R.id.tvEmpresaFactura);
            tvLugarFactura = itemView.findViewById(R.id.tvLugarFactura);
            contenedorFotoFactura = itemView.findViewById(R.id.contenedorFotoFactura);
            ivFotoFacturaThumb = itemView.findViewById(R.id.ivFotoFacturaThumb);
            tvFacturaDetalle = itemView.findViewById(R.id.tvFacturaDetalle);
            tvFacturaMontoPagado = itemView.findViewById(R.id.tvFacturaMontoPagado);
            tvFacturaMontoTotal = itemView.findViewById(R.id.tvFacturaMontoTotal);
            contenedorMesesPagos = itemView.findViewById(R.id.contenedorMesesPagos);
            btnAdjuntarPago = itemView.findViewById(R.id.btnAdjuntarPago);
            tvBotonAdjuntarPago = itemView.findViewById(R.id.tvBotonAdjuntarPago);
            btnCierreFactura = itemView.findViewById(R.id.btnCierreFactura);
        }

        void bind(FacturaConPagos factura, boolean expandido, AccionesListener listener, boolean permiteAdjuntarFactura) {
            tvFechaAgendaFactura.setText(factura.noRequiereVisita
                    ? itemView.getResources().getString(R.string.proforma_no_requiere_visita_valor)
                    : factura.fechaAgenda);
            tvEmpresaFactura.setText(factura.empresa);
            tvLugarFactura.setText(factura.lugar);
            contenedorDetalleFactura.setVisibility(expandido ? View.VISIBLE : View.GONE);

            boolean esAPlazos = factura.esAPlazos();
            tvFacturaLabel.setText(esAPlazos ? R.string.factura_label_plazos_nueva : R.string.factura_label_directa);

            String detalle = factura.fechaFactura != null ? factura.fechaFactura : "";
            if (esAPlazos) {
                // Plan informativo ("a cuántos meses se acordó") — ya no limita cuántas
                // facturas se puedan subir, eso ahora es libre (ver btnAdjuntarPago).
                detalle += (detalle.isEmpty() ? "" : " · ")
                        + itemView.getResources().getString(R.string.proforma_factura_meses_valor, factura.plazoMeses);
            }
            tvFacturaDetalle.setText(detalle);

            // A plazos: fotoFactura/montoTotalFactura ya es el cotizado (se copia solo
            // al confirmar, ver ProformaFragment). Directa: es la foto/monto real que
            // se capturó, sin más — no hay nada más que mostrar.
            mostrarFoto(ivFotoFacturaThumb, factura.fotoFactura);
            contenedorFotoFactura.setOnClickListener(v -> listener.onVerFotoFactura(factura));
            tvFacturaMontoPagado.setText(factura.montoTotalFactura == null
                    ? "" : UtilidadesMoneda.formatearComoMoneda(factura.montoTotalFactura));

            contenedorMesesPagos.removeAllViews();
            if (esAPlazos) {
                // La suma de las facturas ya subidas (arranca con la primera, que
                // Proforma ya sube sola al confirmar) — sin cotizado, no arriba.
                boolean tieneHistorialPagos = !factura.pagos.isEmpty();
                if (tieneHistorialPagos) {
                    tvFacturaMontoTotal.setVisibility(View.VISIBLE);
                    tvFacturaMontoTotal.setText(itemView.getResources().getString(R.string.factura_monto_facturado,
                            UtilidadesMoneda.formatearComoMoneda(factura.getMontoPagado())));
                } else {
                    tvFacturaMontoTotal.setVisibility(View.GONE);
                }

                for (RegistroPagoFactura pago : factura.pagos) {
                    View filaPago = LayoutInflater.from(itemView.getContext())
                            .inflate(R.layout.item_factura_pago, contenedorMesesPagos, false);
                    ImageView ivFotoPagoThumb = filaPago.findViewById(R.id.ivFotoPagoThumb);
                    TextView tvMesTituloMonto = filaPago.findViewById(R.id.tvMesTituloMonto);
                    TextView tvMesFechaHora = filaPago.findViewById(R.id.tvMesFechaHora);

                    mostrarFoto(ivFotoPagoThumb, pago.fotoPago);
                    String montoPago = pago.montoPago == null ? "" : UtilidadesMoneda.formatearComoMoneda(pago.montoPago);
                    tvMesTituloMonto.setText(itemView.getResources().getString(
                            R.string.factura_comprobante_monto, pago.numeroCuota, montoPago));
                    tvMesFechaHora.setText(pago.fechaPago);
                    filaPago.setOnClickListener(v -> listener.onVerFotoPago(pago));

                    contenedorMesesPagos.addView(filaPago);
                }
            } else {
                // Directa: sin resumen y sin lista abajo — un solo monto y una sola foto, nada más.
                tvFacturaMontoTotal.setVisibility(View.GONE);
            }

            // Sin límite de cantidad, pero solo en la pill "A plazos" (acotada al mes
            // que se está trabajando) — "Todo" es historial de solo lectura, ahí no
            // se ofrece adjuntar. Directo es un evento único, tampoco aplica. Tampoco
            // si ya se cerró (a mano o porque la suma de cuotas alcanzó el total).
            boolean mostrarBoton = esAPlazos && permiteAdjuntarFactura && !factura.estaCerrada();
            btnAdjuntarPago.setVisibility(mostrarBoton ? View.VISIBLE : View.GONE);
            if (mostrarBoton) {
                tvBotonAdjuntarPago.setText(R.string.factura_btn_adjuntar_pago);
                btnAdjuntarPago.setOnClickListener(v -> listener.onAdjuntarPago(factura));
            } else {
                btnAdjuntarPago.setOnClickListener(null);
            }

            // Cierre manual: solo mientras siga a plazos, sin cerrar todavía y con
            // id_remota (necesario para escribir motivo_cierre_pago en la fila padre).
            // Igual que "Adjuntar pago": nada de esto se ofrece en "Todo" (historial de
            // solo lectura), ni el botón activo ni la etiqueta "Factura cerrada".
            // (no repite "!estaCerrada()" acá: el "if" de abajo ya cubre ese caso primero,
            // así que si llegamos a este "else if" con esAPlazos+permiteAdjuntarFactura
            // en true, estaCerrada() ya es false por descarte).
            boolean puedeCerrarFactura = esAPlazos && permiteAdjuntarFactura
                    && factura.idRemotaFactura != null;
            if (esAPlazos && permiteAdjuntarFactura && factura.estaCerrada()) {
                btnCierreFactura.setVisibility(View.VISIBLE);
                btnCierreFactura.setText(R.string.proforma_chip_factura_cerrada);
                btnCierreFactura.setOnClickListener(null);
            } else if (puedeCerrarFactura) {
                btnCierreFactura.setVisibility(View.VISIBLE);
                btnCierreFactura.setText(R.string.proforma_btn_cierre_factura);
                btnCierreFactura.setOnClickListener(v -> listener.onCerrarFactura(factura));
            } else {
                btnCierreFactura.setVisibility(View.GONE);
                btnCierreFactura.setOnClickListener(null);
            }
        }

        /** Misma lógica de blob-URL vs base64 que el resto de fotos de Proforma. */
        private void mostrarFoto(ImageView destino, String valor) {
            if (valor == null || valor.trim().isEmpty()) {
                destino.setImageDrawable(null);
                return;
            }
            boolean esUrl = valor.startsWith("http://") || valor.startsWith("https://")
                    || valor.startsWith("Proforma/") || valor.startsWith("Factura/") || valor.startsWith("PagoFactura/");
            if (esUrl) {
                String url = valor.startsWith("http") ? valor : Constantes.PROFORMA_BLOB_BASE_URL + valor;
                Glide.with(itemView.getContext()).load(url).into(destino);
                return;
            }
            try {
                byte[] bytes = Base64.decode(valor, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                destino.setImageBitmap(bitmap);
            } catch (Exception ignored) {
                destino.setImageDrawable(null);
            }
        }
    }
}
