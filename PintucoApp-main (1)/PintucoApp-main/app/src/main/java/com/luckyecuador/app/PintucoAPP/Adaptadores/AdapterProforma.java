package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.BaseProforma;
import com.luckyecuador.app.PintucoAPP.Clase.FacturaEnProgreso;
import com.luckyecuador.app.PintucoAPP.Clase.RegistroProforma;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMoneda;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Tarjeta de "Gestión de Visitas": un encabezado morado siempre visible
 * (fecha de agenda, empresa, local, pill de estado general) que al tocarlo
 * expande/colapsa el historial de envíos de proforma de ese agendamiento —
 * una fila numerada por cada visita ya guardada con evidencia, en orden
 * creciente de fecha, más las acciones de registrar nueva proforma / enviar
 * factura al final. Solo una tarjeta puede estar expandida a la vez.
 *
 * En la pill "Completados", la lista viene intercalada con separadores de
 * sección ("Completados" / "Cerrados", ver ProformaFragment) — de ahí que
 * {@code visitas} sea {@code List<Object>} en vez de {@code List<BaseProforma>}
 * y el adapter tenga dos tipos de vista, mismo patrón que AdapterFacturas.
 */
public class AdapterProforma extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_SEPARADOR = 0;
    private static final int TIPO_VISITA = 1;

    /** Fila estática "Completados"/"Cerrados" — el texto ya viene armado desde ProformaFragment. */
    public static class SeparadorSeccion {
        public final String etiqueta;

        public SeparadorSeccion(String etiqueta) {
            this.etiqueta = etiqueta;
        }
    }

    public interface AccionesListener {
        void onCambiarEstado(BaseProforma visita, RegistroProforma registro);
        void onEvidencia(BaseProforma visita, RegistroProforma registro);
        void onVer(BaseProforma visita, RegistroProforma registro);
        void onGuardarVisita(BaseProforma visita, RegistroProforma registro);
        void onSolicitarCambioEstado(BaseProforma visita, RegistroProforma registro);
        void onGuardarCambiosRegistro(BaseProforma visita, RegistroProforma registro);
        // Toca el recuadro de "adjuntar foto" dentro del panel de confirmar
        // factura — abre la cámara/galería y deja el resultado en fotoBase64 de
        // FacturaEnProgreso (no guarda nada todavía, eso es onConfirmarFactura).
        void onFotoFactura(BaseProforma visita);
        // Ícono de lupa sobre la vista previa ya cargada en el panel de confirmar
        // factura — abre esa misma foto a pantalla completa.
        void onZoomFotoFactura(String fotoBase64);
        // Botón final "Confirmar factura": valida y recién ahí guarda la fila
        // de factura completa (foto + monto de lo cobrado esta vez).
        void onConfirmarFactura(BaseProforma visita);
        void onSeleccionarMesesFactura(BaseProforma visita);
        void onInfoFase(BaseProforma visita, int faseActual);
        void onVerLugarCompleto(String lugar);
        void onVerEmpresaCompleta(String empresa);
        void onEliminarRegistro(BaseProforma visita, RegistroProforma registro);
        // Cierre manual e irreversible: la negociación de proforma se da por
        // cerrada sin llegar a facturar (se perdió el negocio).
        void onCerrarProforma(BaseProforma visita);
    }

    private final List<Object> visitas;
    private final AccionesListener listener;
    // Viven en el Fragment (no aquí) para que sobrevivan a cada recarga de la
    // lista — si no, la tarjeta se colapsaría (o se perdería una confirmación
    // en curso) cada vez que el usuario guarda algo o llega un cambio de sync.
    private final Set<String> idsExpandidos;
    private final Set<Integer> idsConfirmandoEliminar;
    private final Set<String> idsConfirmandoFactura;
    private final Map<String, FacturaEnProgreso> facturasEnProgreso;

    public AdapterProforma(List<Object> visitas, AccionesListener listener, Set<String> idsExpandidos,
                            Set<Integer> idsConfirmandoEliminar, Set<String> idsConfirmandoFactura,
                            Map<String, FacturaEnProgreso> facturasEnProgreso) {
        this.visitas = visitas;
        this.listener = listener;
        this.idsExpandidos = idsExpandidos;
        this.idsConfirmandoEliminar = idsConfirmandoEliminar;
        this.idsConfirmandoFactura = idsConfirmandoFactura;
        this.facturasEnProgreso = facturasEnProgreso;
    }

    @Override
    public int getItemViewType(int position) {
        return visitas.get(position) instanceof SeparadorSeccion ? TIPO_SEPARADOR : TIPO_VISITA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TIPO_SEPARADOR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facturas_separador_mes, parent, false);
            return new SeparadorViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proforma_visita, parent, false);
        return new VisitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = visitas.get(position);
        if (holder instanceof SeparadorViewHolder) {
            ((SeparadorViewHolder) holder).tvSeparador.setText(((SeparadorSeccion) item).etiqueta);
            return;
        }
        BaseProforma visita = (BaseProforma) item;
        VisitaViewHolder visitaHolder = (VisitaViewHolder) holder;
        boolean expandido = idsExpandidos.contains(visita.getIdAgendamiento());
        visitaHolder.bind(visita, expandido, listener, idsConfirmandoEliminar, idsConfirmandoFactura, facturasEnProgreso, this);

        visitaHolder.headerAgendamiento.setOnClickListener(v -> {
            // Acordeón de una sola tarjeta a la vez: al abrir una se cierran todas
            // las demás (en vez de solo agregar/quitar el propio id del set).
            boolean yaExpandido = idsExpandidos.contains(visita.getIdAgendamiento());
            idsExpandidos.clear();
            if (!yaExpandido) {
                idsExpandidos.add(visita.getIdAgendamiento());
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return visitas.size();
    }

    static class SeparadorViewHolder extends RecyclerView.ViewHolder {
        final TextView tvSeparador;

        SeparadorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeparador = (TextView) itemView;
        }
    }

    static class VisitaViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout headerAgendamiento;
        private final TextView tvFechaAgendaHeader;
        private final TextView tvEmpresaHeader;
        private final TextView tvLugarHeader;
        private final View tvPendienteSync;
        private final TextView tvEstadoGeneralHeader;
        private final View contenedorDetalle;
        private final LinearLayout contenedorRegistros;
        private final View btnGuardarVisita;
        private final TextView tvBotonGuardarVisita;
        private final TextView btnNuevaProforma;
        private final TextView btnCierreProforma;
        private final View tarjetaAlertaProforma;
        private final TextView tvAlertaProformaTexto;
        private final TextView btnAlertaTomarFotoNuevo;
        private final View tarjetaVencidaProforma;
        private final View divisorSeccionFactura;
        private final View tituloSeccionFactura;
        private final TextView btnEnviarFactura;
        private final View contenedorConfirmarFactura;
        private final View contenedorAdjuntarFotoFactura;
        private final ImageView ivPreviewFotoFactura;
        private final ImageView ivZoomFotoFactura;
        private final View contenedorAdjuntarFotoVacio;
        private final EditText etMontoTotalFactura;
        private final CheckBox cbPagoDirectoFactura;
        private final CheckBox cbPagoPlazosFactura;
        private final View llMesesFactura;
        private final View selectorMesesFactura;
        private final TextView tvMesesSeleccionados;
        private final TextView btnCancelarFactura;
        private final TextView btnConfirmarFactura;
        // La factura en curso del ítem actualmente enlazado a este ViewHolder —
        // el TextWatcher de etMontoTotalFactura se engancha una sola vez (en el
        // constructor, no en bind()) porque la vista se recicla; este campo es lo
        // que le dice a ese watcher a cuál FacturaEnProgreso escribirle en cada
        // momento, según qué visita esté enlazada ahora.
        private FacturaEnProgreso facturaActual;
        // true solo mientras bindConfirmarFactura está escribiendo la propuesta
        // automática en etMontoTotalFactura — le dice al TextWatcher (enganchado
        // una sola vez, más abajo) que ese cambio no es una edición real del
        // promotor, para no perder el color gris de "sugerencia" antes de tiempo.
        private boolean aplicandoSugerenciaMonto = false;

        VisitaViewHolder(@NonNull View itemView) {
            super(itemView);
            headerAgendamiento = itemView.findViewById(R.id.headerAgendamiento);
            tvFechaAgendaHeader = itemView.findViewById(R.id.tvFechaAgendaHeader);
            tvEmpresaHeader = itemView.findViewById(R.id.tvEmpresaHeader);
            tvLugarHeader = itemView.findViewById(R.id.tvLugarHeader);
            tvPendienteSync = itemView.findViewById(R.id.tvPendienteSync);
            tvEstadoGeneralHeader = itemView.findViewById(R.id.tvEstadoGeneralHeader);
            contenedorDetalle = itemView.findViewById(R.id.contenedorDetalle);
            contenedorRegistros = itemView.findViewById(R.id.contenedorRegistros);
            btnGuardarVisita = itemView.findViewById(R.id.btnGuardarVisita);
            tvBotonGuardarVisita = itemView.findViewById(R.id.tvBotonGuardarVisita);
            btnNuevaProforma = itemView.findViewById(R.id.btnNuevaProforma);
            btnCierreProforma = itemView.findViewById(R.id.btnCierreProforma);
            tarjetaAlertaProforma = itemView.findViewById(R.id.tarjetaAlertaProforma);
            tvAlertaProformaTexto = itemView.findViewById(R.id.tvAlertaProformaTexto);
            btnAlertaTomarFotoNuevo = itemView.findViewById(R.id.btnAlertaTomarFotoNuevo);
            tarjetaVencidaProforma = itemView.findViewById(R.id.tarjetaVencidaProforma);
            divisorSeccionFactura = itemView.findViewById(R.id.divisorSeccionFactura);
            tituloSeccionFactura = itemView.findViewById(R.id.tituloSeccionFactura);
            btnEnviarFactura = itemView.findViewById(R.id.btnEnviarFactura);
            contenedorConfirmarFactura = itemView.findViewById(R.id.contenedorConfirmarFactura);
            contenedorAdjuntarFotoFactura = itemView.findViewById(R.id.contenedorAdjuntarFotoFactura);
            ivPreviewFotoFactura = itemView.findViewById(R.id.ivPreviewFotoFactura);
            ivZoomFotoFactura = itemView.findViewById(R.id.ivZoomFotoFactura);
            contenedorAdjuntarFotoVacio = itemView.findViewById(R.id.contenedorAdjuntarFotoVacio);
            etMontoTotalFactura = itemView.findViewById(R.id.etMontoTotalFactura);
            cbPagoDirectoFactura = itemView.findViewById(R.id.cbPagoDirectoFactura);
            cbPagoPlazosFactura = itemView.findViewById(R.id.cbPagoPlazosFactura);
            llMesesFactura = itemView.findViewById(R.id.llMesesFactura);
            selectorMesesFactura = itemView.findViewById(R.id.selectorMesesFactura);
            tvMesesSeleccionados = itemView.findViewById(R.id.tvMesesSeleccionados);
            btnCancelarFactura = itemView.findViewById(R.id.btnCancelarFactura);
            btnConfirmarFactura = itemView.findViewById(R.id.btnConfirmarFactura);

            UtilidadesMoneda.aplicarFormatoMoneda(etMontoTotalFactura, valor -> {
                if (facturaActual == null) {
                    return;
                }
                facturaActual.montoTotalFactura = valor == null ? null : String.valueOf(valor);
                if (!aplicandoSugerenciaMonto) {
                    // El promotor tocó el campo de verdad (tecleó o el reformateo de
                    // 2 decimales corrió al perder el foco) — deja de ser sugerencia.
                    facturaActual.montoTotalFacturaEsSugerido = false;
                    etMontoTotalFactura.setTextColor(itemView.getResources().getColor(R.color.plomo_pintuco));
                }
            });
        }

        /** Única fuente de verdad para el texto del selector de meses — la usan
         *  tanto bindConfirmarFactura (al reabrir o recargar la tarjeta) como el
         *  click listener de "Pago a plazos"/"Pago directo". */
        private void actualizarTextoMeses(FacturaEnProgreso f) {
            tvMesesSeleccionados.setText(f.plazoMeses == null
                    ? itemView.getResources().getString(R.string.proforma_factura_seleccionar_meses)
                    : itemView.getResources().getString(R.string.proforma_factura_meses_valor, f.plazoMeses));
        }

        void bind(BaseProforma visita, boolean expandido, AccionesListener listener,
                   Set<Integer> idsConfirmandoEliminar, Set<String> idsConfirmandoFactura,
                   Map<String, FacturaEnProgreso> facturasEnProgreso, RecyclerView.Adapter<?> adapter) {
            tvFechaAgendaHeader.setText(visita.isNoRequiereVisita()
                    ? itemView.getResources().getString(R.string.proforma_no_requiere_visita_valor)
                    : visita.getFechaAgenda());
            tvEmpresaHeader.setText(visita.getEmpresa());
            tvLugarHeader.setText(visita.getLugarMostrado());
            // "Empresa" y "Lugar" quedan truncados a una línea para no desalinear
            // el encabezado — un toque muestra el texto completo en un diálogo.
            tvEmpresaHeader.setOnClickListener(v -> listener.onVerEmpresaCompleta(visita.getEmpresa()));
            tvLugarHeader.setOnClickListener(v -> listener.onVerLugarCompleto(visita.getLugarMostrado()));

            boolean algunoPendiente = false;
            for (RegistroProforma registro : visita.getRegistros()) {
                if (registro.isPendienteSync()) {
                    algunoPendiente = true;
                    break;
                }
            }
            tvPendienteSync.setVisibility(algunoPendiente ? View.VISIBLE : View.GONE);

            contenedorDetalle.setVisibility(expandido ? View.VISIBLE : View.GONE);

            // Fase actual = la del último registro guardado (los borradores no cuentan);
            // el borrador (si existe) queda aparte para servir de destino a "+ Nueva proforma".
            // La fila que "necesita foto" (corrección solicitada o ronda vacía) y la
            // "última fila visible" (ya completa, con evidencia) se buscan aparte, en
            // vez de asumir que necesariamente es la última de la lista — si el
            // servidor llegara a colar una fila de más después, no debe tapar una
            // corrección pendiente ni hacer parecer que ya hay monto validado.
            int faseActual = 3;
            RegistroProforma borradorDisponible = null;
            RegistroProforma filaActivaEsperandoFoto = null;
            RegistroProforma ultimaFilaVisible = null;
            for (RegistroProforma r : visita.getRegistros()) {
                if (r.esBorrador()) {
                    borradorDisponible = r;
                    continue;
                }
                faseActual = r.faseActual;
                if (r.esFilaFactura()) {
                    // Una factura "gasta" la ronda de proforma que la originó: la
                    // ronda siguiente (si el promotor arranca otra para facturarle
                    // de nuevo el mes que viene) necesita su propia validación de
                    // oficina, no puede heredar el monto_validado de la anterior.
                    filaActivaEsperandoFoto = null;
                    ultimaFilaVisible = null;
                    continue;
                }
                if (r.necesitaFoto()) {
                    filaActivaEsperandoFoto = r;
                } else {
                    ultimaFilaVisible = r;
                }
            }

            boolean facturaEnviada = visita.estaCompletado();
            // Cierre manual e irreversible (botón "Cierre Proforma"): la negociación se
            // dio por cerrada sin llegar a facturar — bloquea "+ Nueva proforma", "Enviar
            // factura" y el reintento de foto por igual, ninguna acción de la tarjeta
            // debe seguir habilitada después de esto.
            boolean proformaCerrada = visita.proformaCerrada();
            boolean vencidaSinSubir = !facturaEnviada && !proformaCerrada && visita.vencidaSinSubir();
            boolean rondaActivaEsperaFoto = filaActivaEsperandoFoto != null && !proformaCerrada;
            boolean rechazado = rondaActivaEsperaFoto && !facturaEnviada
                    && Constantes.ESTADO_PROFORMA_RECHAZADO.equalsIgnoreCase(filaActivaEsperandoFoto.estadoProforma);
            boolean correccionSolicitada = rondaActivaEsperaFoto && filaActivaEsperandoFoto.esCorreccionSolicitada();

            // La oficina valida (le pone monto) cada envío antes de dejar que se
            // acumule otro: mientras la última ronda ya completa no tenga
            // monto_validado, "+ Nueva proforma" y "Enviar factura" quedan
            // visibles pero deshabilitados (gris) — no atado a una fila que el
            // propio analista ya dejó explícitamente esperando foto (esa sí se
            // puede llenar directo, es él quien la habilitó).
            boolean tieneMontoValidado = ultimaFilaVisible != null && ultimaFilaVisible.montoValidado != null;
            boolean habilitadoNuevaProforma = rondaActivaEsperaFoto || ultimaFilaVisible == null || tieneMontoValidado;
            boolean habilitadoFactura = tieneMontoValidado && !proformaCerrada;

            // Pill de fase del agendamiento (header, visible sin expandir): número de
            // fase mientras siga en curso. Una vez completada la visita ya no se
            // muestra — la tarjeta solo aparece en el pill "Completados" de la lista,
            // así que repetir "Completado" en cada encabezado es redundante.
            tvEstadoGeneralHeader.setVisibility(facturaEnviada ? View.GONE : View.VISIBLE);
            if (!facturaEnviada) {
                tvEstadoGeneralHeader.setText(itemView.getResources()
                        .getString(R.string.proforma_info_fase_titulo, faseActual));
                tvEstadoGeneralHeader.setBackgroundResource(R.drawable.bg_chip_proforma_proceso);
                tvEstadoGeneralHeader.setTextColor(itemView.getResources().getColor(R.color.proforma_proceso_texto));
            }

            // "+ Registrar nueva proforma" (dashed) vs. tarjeta de alerta ámbar
            // (corrección solicitada o foto rechazada) — mutuamente excluyentes,
            // ambas cubren la misma fila activa esperando foto. Se oculta por
            // completo si la visita ya está completada (factura enviada): la
            // pill "Completados" debe quedar de solo lectura, sin acciones.
            boolean mostrarAlerta = rondaActivaEsperaFoto && (correccionSolicitada || rechazado);
            boolean mostrarBotonNueva = !facturaEnviada && !mostrarAlerta && !proformaCerrada && !vencidaSinSubir;
            tarjetaVencidaProforma.setVisibility(vencidaSinSubir ? View.VISIBLE : View.GONE);

            // Chip "Cierre Proforma": solo mientras la negociación siga en fase de
            // cotización (nunca si ya se facturó, ese ciclo tiene su propio cierre
            // en el módulo Facturas) y solo si hay algo guardado que cerrar.
            boolean hayAlgoQueCerrar = visita.getUltimoRegistroGuardado() != null;
            if (facturaEnviada || !hayAlgoQueCerrar) {
                btnCierreProforma.setVisibility(View.GONE);
                btnCierreProforma.setOnClickListener(null);
            } else if (proformaCerrada) {
                btnCierreProforma.setVisibility(View.VISIBLE);
                btnCierreProforma.setText(R.string.proforma_chip_proforma_cerrada);
                btnCierreProforma.setOnClickListener(null);
            } else {
                btnCierreProforma.setVisibility(View.VISIBLE);
                btnCierreProforma.setText(R.string.proforma_btn_cierre_proforma);
                btnCierreProforma.setOnClickListener(v -> listener.onCerrarProforma(visita));
            }

            btnNuevaProforma.setVisibility(mostrarBotonNueva ? View.VISIBLE : View.GONE);
            if (mostrarBotonNueva) {
                btnNuevaProforma.setEnabled(habilitadoNuevaProforma);
                btnNuevaProforma.setBackgroundResource(habilitadoNuevaProforma
                        ? R.drawable.bg_boton_dashed_proforma : R.drawable.bg_boton_dashed_inactivo);
                btnNuevaProforma.setTextColor(itemView.getResources().getColor(habilitadoNuevaProforma
                        ? R.color.proforma_titulo : R.color.gris));
                if (habilitadoNuevaProforma) {
                    // Si hay una fila ya con su propia foto pendiente por asignar (ronda
                    // vacía nueva o corrección), se usa esa fila; si no, un borrador nuevo
                    // (creado en cargarRegistrosProforma) para no pisar la anterior.
                    RegistroProforma destino = rondaActivaEsperaFoto ? filaActivaEsperandoFoto : borradorDisponible;
                    btnNuevaProforma.setOnClickListener(v -> listener.onEvidencia(visita, destino));
                } else {
                    btnNuevaProforma.setOnClickListener(null);
                }
            }

            tarjetaAlertaProforma.setVisibility(mostrarAlerta ? View.VISIBLE : View.GONE);
            if (mostrarAlerta) {
                RegistroProforma destino = filaActivaEsperandoFoto;
                int numeroAlerta = calcularNumero(visita, destino);
                String texto = itemView.getResources().getString(
                        correccionSolicitada ? R.string.proforma_alerta_correccion_texto
                                : R.string.proforma_alerta_rechazado_texto,
                        numeroAlerta, destino.getFechaContacto());
                tvAlertaProformaTexto.setText(texto);
                btnAlertaTomarFotoNuevo.setOnClickListener(v -> listener.onEvidencia(visita, destino));
            }

            // Sección factura: siempre visible en paralelo a "+ Nueva proforma" hasta
            // cerrar el ciclo, pero deshabilitada (gris) hasta que la oficina le
            // ponga monto a la última ronda enviada — mismo candado que "+ Nueva
            // proforma", nunca se salta directo a factura sin pasar por ahí. Una vez
            // completada la visita (facturaEnviada) O cerrada manualmente sin llegar a
            // facturar (proformaCerrada), la sección entera (título + divisor incluidos)
            // desaparece: dejar el título "FACTURA" sin nada debajo, o un botón
            // deshabilitado, se veía como que todavía quedaba algo pendiente por hacer.
            boolean ocultarSeccionFactura = facturaEnviada || proformaCerrada;
            divisorSeccionFactura.setVisibility(ocultarSeccionFactura ? View.GONE : View.VISIBLE);
            tituloSeccionFactura.setVisibility(ocultarSeccionFactura ? View.GONE : View.VISIBLE);
            boolean confirmandoFactura = idsConfirmandoFactura.contains(visita.getIdAgendamiento());
            btnEnviarFactura.setVisibility(!ocultarSeccionFactura && !confirmandoFactura ? View.VISIBLE : View.GONE);
            contenedorConfirmarFactura.setVisibility(!ocultarSeccionFactura && confirmandoFactura ? View.VISIBLE : View.GONE);

            if (!ocultarSeccionFactura) {
                btnEnviarFactura.setEnabled(habilitadoFactura);
                btnEnviarFactura.setBackgroundResource(habilitadoFactura
                        ? R.drawable.bg_boton_factura_ambar : R.drawable.bg_boton_factura_inactivo);
                int colorFactura = itemView.getResources()
                        .getColor(habilitadoFactura ? R.color.proforma_proceso_texto : R.color.gris);
                btnEnviarFactura.setTextColor(colorFactura);
                btnEnviarFactura.setCompoundDrawableTintList(ColorStateList.valueOf(colorFactura));
                btnEnviarFactura.setOnClickListener(habilitadoFactura ? v -> {
                    idsConfirmandoFactura.add(visita.getIdAgendamiento());
                    adapter.notifyDataSetChanged();
                } : null);
                btnCancelarFactura.setOnClickListener(v -> {
                    idsConfirmandoFactura.remove(visita.getIdAgendamiento());
                    facturasEnProgreso.remove(visita.getIdAgendamiento());
                    adapter.notifyDataSetChanged();
                });
                // Candado anti doble-tap: la vista puede venir reciclada ya deshabilitada.
                btnConfirmarFactura.setEnabled(true);
                btnConfirmarFactura.setOnClickListener(v -> {
                    btnConfirmarFactura.setEnabled(false);
                    listener.onConfirmarFactura(visita);
                });

                if (confirmandoFactura) {
                    bindConfirmarFactura(visita, facturasEnProgreso, listener, adapter, ultimaFilaVisible);
                } else {
                    facturaActual = null;
                }
            }

            contenedorRegistros.removeAllViews();
            int numero = 0;
            for (RegistroProforma registro : visita.getRegistros()) {
                // El borrador y la fila activa esperando foto ya están representados
                // arriba (botón dashed o tarjeta de alerta) — no se listan dos veces.
                if (registro.esBorrador() || registro.necesitaFoto()) {
                    continue;
                }
                numero++;
                View filaView = LayoutInflater.from(itemView.getContext())
                        .inflate(R.layout.item_proforma_registro, contenedorRegistros, false);
                boolean esUltimoGuardado = registro == ultimaFilaVisible;
                bindRegistro(filaView, visita, registro, numero, esUltimoGuardado, listener,
                        idsConfirmandoEliminar, adapter);
                contenedorRegistros.addView(filaView);
            }

            bindBotonGuardar(visita, listener, ocultarSeccionFactura);
        }

        /**
         * Contenido del panel "Confirmar factura" mientras está abierto: foto
         * (con su vista previa una vez elegida) y el monto de lo que se está
         * cobrando esta vez. Todo se guarda en memoria en {@code facturasEnProgreso}
         * hasta que se toca "Confirmar factura".
         */
        private void bindConfirmarFactura(BaseProforma visita, Map<String, FacturaEnProgreso> facturasEnProgreso,
                                           AccionesListener listener, RecyclerView.Adapter<?> adapter,
                                           RegistroProforma ultimaFilaVisible) {
            FacturaEnProgreso f = facturasEnProgreso.computeIfAbsent(visita.getIdAgendamiento(), id -> new FacturaEnProgreso());
            facturaActual = f;

            // Propone el último monto validado de la proforma como punto de partida
            // del monto de factura — el promotor lo ve en gris y puede aceptarlo tal
            // cual o sobrescribirlo. Solo se propone una vez: si lo borra a propósito,
            // no se le vuelve a insistir en el siguiente rebind (checkbox, meses, etc).
            if (f.montoTotalFactura == null && !f.montoSugeridoYaPropuesto
                    && ultimaFilaVisible != null && ultimaFilaVisible.montoValidado != null) {
                f.montoSugeridoYaPropuesto = true;
                f.montoTotalFacturaEsSugerido = true;
                aplicandoSugerenciaMonto = true;
                etMontoTotalFactura.setText(UtilidadesMoneda.formatearParaEditar(ultimaFilaVisible.montoValidado));
                aplicandoSugerenciaMonto = false;
            }

            if (f.fotoBase64 != null) {
                contenedorAdjuntarFotoVacio.setVisibility(View.GONE);
                ivPreviewFotoFactura.setVisibility(View.VISIBLE);
                ivZoomFotoFactura.setVisibility(View.VISIBLE);
                try {
                    byte[] bytes = Base64.decode(f.fotoBase64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivPreviewFotoFactura.setImageBitmap(bitmap);
                } catch (Exception ignored) {
                    // La foto siempre viene de nuestra propia captura (base64 fresco) — no
                    // debería fallar; si llegara a pasar, se queda con el placeholder.
                }
            } else {
                contenedorAdjuntarFotoVacio.setVisibility(View.VISIBLE);
                ivPreviewFotoFactura.setVisibility(View.GONE);
                ivZoomFotoFactura.setVisibility(View.GONE);
            }
            contenedorAdjuntarFotoFactura.setOnClickListener(v -> listener.onFotoFactura(visita));
            ivZoomFotoFactura.setOnClickListener(v -> listener.onZoomFotoFactura(f.fotoBase64));

            // Comparar por VALOR (no por texto formateado) contra lo que ya hay en el
            // campo: si son el mismo monto, se deja el texto tal como está. Este bind
            // se repite seguido (checkbox de plazos, selector de meses, cualquier
            // notifyDataSetChanged), y si comparáramos strings, el "$3556.00" que dejó
            // el formato de 2 decimales al salir del campo (aplicarFormatoMoneda) se
            // pisaba con el "$3556" sin decimales de formatearParaEditar en el
            // siguiente rebind — el monto nunca se veía formateado.
            Double montoEnMemoria = UtilidadesMoneda.parsearMonedaSegura(f.montoTotalFactura);
            Double montoEnCampo = UtilidadesMoneda.parsearMonedaSegura(etMontoTotalFactura.getText().toString());
            if (!Objects.equals(montoEnMemoria, montoEnCampo)) {
                // Guardado bajo el mismo flag que la sugerencia inicial: este re-sync de
                // texto es programático (rebind), no una edición real del promotor, así
                // que NO debe apagar montoTotalFacturaEsSugerido (ver watcher del
                // constructor). Sin esto, cualquier notifyDataSetChanged (p. ej. tocar
                // el checkbox de plazos) podía "confirmar" la sugerencia sin que el
                // promotor haya tipeado nada.
                aplicandoSugerenciaMonto = true;
                etMontoTotalFactura.setText(UtilidadesMoneda.formatearParaEditar(f.montoTotalFactura));
                aplicandoSugerenciaMonto = false;
            }
            // El color se reaplica en cada bind (no solo cuando cambia el texto)
            // porque un rebind sin cambio de monto igual necesita mantener el gris.
            etMontoTotalFactura.setTextColor(itemView.getResources().getColor(
                    f.montoTotalFacturaEsSugerido ? R.color.gris_claro : R.color.plomo_pintuco));

            // "Pago directo" y "¿Pago a plazos?" son mutuamente excluyentes (una
            // factura es de un tipo o del otro, nunca ninguno o ambos) — se
            // renderizan siempre a partir de f.pagoAPlazos como única fuente de
            // verdad, así que cualquier toque en cualquiera de los dos fuerza el
            // valor correspondiente y se re-dibuja con notifyDataSetChanged, en
            // vez de confiar en el toggle nativo de cada CheckBox por separado.
            cbPagoDirectoFactura.setChecked(Boolean.FALSE.equals(f.pagoAPlazos));
            cbPagoPlazosFactura.setChecked(Boolean.TRUE.equals(f.pagoAPlazos));
            llMesesFactura.setVisibility(Boolean.TRUE.equals(f.pagoAPlazos) ? View.VISIBLE : View.GONE);
            cbPagoDirectoFactura.setOnClickListener(v -> {
                f.pagoAPlazos = false;
                f.plazoMeses = null;
                adapter.notifyDataSetChanged();
            });
            cbPagoPlazosFactura.setOnClickListener(v -> {
                f.pagoAPlazos = true;
                adapter.notifyDataSetChanged();
            });

            actualizarTextoMeses(f);
            selectorMesesFactura.setOnClickListener(v -> listener.onSeleccionarMesesFactura(visita));
        }

        /** Posición ordinal (1-based) de un registro dentro del historial ya guardado. */
        private int calcularNumero(BaseProforma visita, RegistroProforma objetivo) {
            int contador = 0;
            for (RegistroProforma r : visita.getRegistros()) {
                if (r.esBorrador()) {
                    continue;
                }
                contador++;
                if (r == objetivo) {
                    return contador;
                }
            }
            return contador;
        }

        /**
         * Un único botón "Guardar" por TARJETA (no por fila): la lámina original
         * solo tenía uno, así que cambiar el estado de una fila ya guardada (el
         * lápiz) o completar las tres secciones del borrador habilitan ESTE
         * mismo botón en vez de mostrar uno nuevo en cada fila. Prioridad: si el
         * borrador ya está completo, confirma la visita nueva; si no, busca una
         * fila ya guardada con un cambio pendiente (estado) y confirma esa.
         * Oculto por completo si la visita ya está completada (factura enviada) o si
         * la proforma se cerró sin llegar a facturar — en ambos casos no queda nada
         * por guardar, solo lectura.
         */
        private void bindBotonGuardar(BaseProforma visita, AccionesListener listener, boolean ocultar) {
            btnGuardarVisita.setVisibility(ocultar ? View.GONE : View.VISIBLE);
            if (ocultar) {
                return;
            }

            RegistroProforma borrador = null;
            RegistroProforma conCambioPendiente = null;
            for (RegistroProforma registro : visita.getRegistros()) {
                if (registro.esBorrador()) {
                    borrador = registro;
                } else if (conCambioPendiente == null && registro.tieneCambiosPendientes()) {
                    conCambioPendiente = registro;
                }
            }

            boolean borradorCompleto = borrador != null && borrador.tieneLasTresSeccionesCompletas();
            boolean activo = borradorCompleto || conCambioPendiente != null;

            btnGuardarVisita.setEnabled(activo);
            if (activo) {
                tvBotonGuardarVisita.setBackgroundResource(R.drawable.bg_chip_proforma_realizado);
                tvBotonGuardarVisita.setTextColor(itemView.getResources().getColor(R.color.proforma_realizado_texto));
                // Candado anti doble-tap: deshabilita antes de invocar el guardado real.
                if (borradorCompleto) {
                    RegistroProforma destino = borrador;
                    btnGuardarVisita.setOnClickListener(v -> {
                        btnGuardarVisita.setEnabled(false);
                        listener.onGuardarVisita(visita, destino);
                    });
                } else {
                    RegistroProforma destino = conCambioPendiente;
                    btnGuardarVisita.setOnClickListener(v -> {
                        btnGuardarVisita.setEnabled(false);
                        listener.onGuardarCambiosRegistro(visita, destino);
                    });
                }
            } else {
                tvBotonGuardarVisita.setBackgroundResource(R.drawable.bg_chip_proforma_guardar_inactivo);
                tvBotonGuardarVisita.setTextColor(itemView.getResources().getColor(R.color.gris));
                btnGuardarVisita.setOnClickListener(null);
            }
        }

        private void bindRegistro(View filaView, BaseProforma visita, RegistroProforma registro, int numero,
                                   boolean esUltimoGuardado, AccionesListener listener,
                                   Set<Integer> idsConfirmandoEliminar, RecyclerView.Adapter<?> adapter) {
            View contenedorNormal = filaView.findViewById(R.id.contenedorFilaNormalRegistro);
            View contenedorConfirmar = filaView.findViewById(R.id.contenedorConfirmarEliminarRegistro);

            boolean confirmandoEliminar = registro.idLocal != null && idsConfirmandoEliminar.contains(registro.idLocal);
            contenedorNormal.setVisibility(confirmandoEliminar ? View.GONE : View.VISIBLE);
            contenedorConfirmar.setVisibility(confirmandoEliminar ? View.VISIBLE : View.GONE);

            if (confirmandoEliminar) {
                TextView tvConfirmarTexto = filaView.findViewById(R.id.tvConfirmarEliminarTexto);
                TextView btnConfirmarNo = filaView.findViewById(R.id.btnConfirmarEliminarNo);
                TextView btnConfirmarSi = filaView.findViewById(R.id.btnConfirmarEliminarSi);
                tvConfirmarTexto.setText(filaView.getResources().getString(R.string.proforma_eliminar_confirmar_titulo, numero));
                btnConfirmarNo.setOnClickListener(v -> {
                    idsConfirmandoEliminar.remove(registro.idLocal);
                    adapter.notifyDataSetChanged();
                });
                btnConfirmarSi.setOnClickListener(v -> listener.onEliminarRegistro(visita, registro));
                return;
            }

            TextView tvNumero = filaView.findViewById(R.id.tvNumeroRegistro);
            TextView tvFilaNumero = filaView.findViewById(R.id.tvFilaNumeroRegistro);
            TextView tvFecha = filaView.findViewById(R.id.tvFechaContactoRegistro);
            LinearLayout chipEstado = filaView.findViewById(R.id.chipEstadoRegistro);
            View ivEditarEstado = filaView.findViewById(R.id.ivEditarEstadoRegistro);
            View ivChevronEstado = filaView.findViewById(R.id.ivChevronEstadoRegistro);
            TextView btnVer = filaView.findViewById(R.id.btnVerRegistro);
            ImageView ivEliminar = filaView.findViewById(R.id.ivEliminarRegistro);

            boolean esFilaFactura = registro.esFilaFactura();

            tvNumero.setText(String.valueOf(numero));
            tvFilaNumero.setText(filaView.getResources().getString(
                    esFilaFactura ? R.string.proforma_fila_numero_factura : R.string.proforma_fila_numero, numero));
            // El monto (si ya se registró) queda como histórico junto a la fecha, en
            // el mismo gris — no todas las filas antiguas tienen uno. La fila de
            // factura usa monto_total_factura (y el plazo, si es a cuotas) en vez de
            // monto_validado, que ahí siempre queda vacío.
            StringBuilder fechaLinea = new StringBuilder(registro.getFechaContacto());
            if (esFilaFactura) {
                if (registro.montoTotalFactura != null) {
                    fechaLinea.append(" · ").append(formatearMonto(registro.montoTotalFactura));
                }
                if (registro.esFacturaAPlazos()) {
                    fechaLinea.append(" · ").append(filaView.getResources()
                            .getString(R.string.proforma_factura_meses_valor, registro.plazoMeses));
                }
            } else if (registro.montoValidado != null) {
                fechaLinea.append(" · ").append(formatearMonto(registro.montoValidado));
            }
            tvFecha.setText(fechaLinea);

            // El chip de estado (pendiente/en_proceso/realizado) se ocultó por
            // completo: no lo lee ningún filtro del panel web ni ninguna lógica
            // de fase de la app (confirmado, ver project_proforma_monto_validado_compartido),
            // así que dejó de aportar información real al promotor.
            chipEstado.setVisibility(View.GONE);
            ivChevronEstado.setVisibility(View.GONE);
            ivEditarEstado.setVisibility(View.GONE);
            chipEstado.setOnClickListener(null);

            btnVer.setOnClickListener(v -> listener.onVer(visita, registro));

            boolean puedeEliminar = esUltimoGuardado && registro.puedeEliminarse() && !esFilaFactura;
            ivEliminar.setVisibility(puedeEliminar ? View.VISIBLE : View.GONE);
            ivEliminar.setOnClickListener(puedeEliminar ? v -> {
                idsConfirmandoEliminar.add(registro.idLocal);
                adapter.notifyDataSetChanged();
            } : null);
        }

        private static String formatearMonto(double valor) {
            return java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(valor);
        }

    }
}
