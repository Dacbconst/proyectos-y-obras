package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.BaseAgenda;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.R;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class AdapterAgenda extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ENCABEZADO = 0;
    private static final int TYPE_SIN_VISITAS = 1;
    private static final int TYPE_EVENTO = 2;

    public interface AccionesListener {
        void onVerDetalle(BaseAgenda evento);
    }

    private final List<Fila> filas;
    private final AccionesListener listener;

    public AdapterAgenda(List<Fila> filas, AccionesListener listener) {
        this.filas = filas;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (filas.get(position).tipo) {
            case ENCABEZADO_DIA:
                return TYPE_ENCABEZADO;
            case SIN_VISITAS:
                return TYPE_SIN_VISITAS;
            default:
                return TYPE_EVENTO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_ENCABEZADO:
                return new EncabezadoViewHolder(inflater.inflate(R.layout.item_agenda_encabezado, parent, false));
            case TYPE_SIN_VISITAS:
                return new SinVisitasViewHolder(inflater.inflate(R.layout.item_agenda_vacio, parent, false));
            default:
                return new EventoViewHolder(inflater.inflate(R.layout.item_agenda_evento, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Fila fila = filas.get(position);
        if (holder instanceof EncabezadoViewHolder) {
            ((EncabezadoViewHolder) holder).bind(fila.fecha);
        } else if (holder instanceof EventoViewHolder) {
            ((EventoViewHolder) holder).bind(fila.evento);
            holder.itemView.setOnClickListener(v -> listener.onVerDetalle(fila.evento));
        } else if (holder instanceof SinVisitasViewHolder) {
            ((SinVisitasViewHolder) holder).bind(fila.mensaje);
        }
    }

    @Override
    public int getItemCount() {
        return filas.size();
    }

    /**
     * Retrocede desde la posicion dada hasta encontrar la fila de encabezado de dia
     * (puede ser la misma posicion). La usa {@link StickyHeaderDecoration} para saber
     * que dia mostrar fijo arriba de la lista mientras el usuario scrollea.
     */
    int posicionEncabezadoPara(int posicion) {
        for (int i = posicion; i >= 0; i--) {
            if (filas.get(i).tipo == Fila.Tipo.ENCABEZADO_DIA) {
                return i;
            }
        }
        return -1;
    }

    /** Posicion del proximo encabezado de dia despues del dado, o -1 si es el ultimo del mes visible. */
    int posicionSiguienteEncabezado(int posicionEncabezadoActual) {
        for (int i = posicionEncabezadoActual + 1; i < filas.size(); i++) {
            if (filas.get(i).tipo == Fila.Tipo.ENCABEZADO_DIA) {
                return i;
            }
        }
        return -1;
    }

    /** Vista de encabezado reutilizable para dibujar "a mano" sobre el canvas (no es una fila real de la lista). */
    View obtenerVistaEncabezadoReutilizable(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_encabezado, parent, false);
    }

    void vincularEncabezado(View vista, int posicionEncabezado) {
        new EncabezadoViewHolder(vista).bind(filas.get(posicionEncabezado).fecha);
    }

    private static String nombreDiaSemana(LocalDate fecha) {
        switch (fecha.getDayOfWeek()) {
            case SUNDAY:
                return Constantes.SUN;
            case MONDAY:
                return Constantes.MON;
            case TUESDAY:
                return Constantes.TUE;
            case WEDNESDAY:
                return Constantes.WED;
            case THURSDAY:
                return Constantes.THU;
            case FRIDAY:
                return Constantes.FRI;
            default:
                return Constantes.SAT;
        }
    }

    private static String nombreMesAbreviado(LocalDate fecha) {
        String[] abreviaturas = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        return abreviaturas[fecha.getMonthValue() - 1];
    }

    /**
     * Fila de la lista de agenda: encabezado de dia, marcador de dia vacio o evento.
     * Es solo de presentacion, no se mapea desde el backend (ver {@link BaseAgenda}).
     */
    public static class Fila {

        public enum Tipo {
            ENCABEZADO_DIA,
            SIN_VISITAS,
            EVENTO
        }

        final Tipo tipo;
        final LocalDate fecha;
        final BaseAgenda evento;
        final String mensaje;

        private Fila(Tipo tipo, LocalDate fecha, BaseAgenda evento, String mensaje) {
            this.tipo = tipo;
            this.fecha = fecha;
            this.evento = evento;
            this.mensaje = mensaje;
        }

        public static Fila encabezado(LocalDate fecha) {
            return new Fila(Tipo.ENCABEZADO_DIA, fecha, null, null);
        }

        public static Fila sinVisitas(String mensaje) {
            return new Fila(Tipo.SIN_VISITAS, null, null, mensaje);
        }

        public static Fila evento(BaseAgenda evento) {
            return new Fila(Tipo.EVENTO, null, evento, null);
        }
    }

    static class EncabezadoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDiaNumero;
        private final TextView tvDiaMesNombre;
        private final TextView tvDiaSemanaNombre;

        EncabezadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiaNumero = itemView.findViewById(R.id.tvDiaNumero);
            tvDiaMesNombre = itemView.findViewById(R.id.tvDiaMesNombre);
            tvDiaSemanaNombre = itemView.findViewById(R.id.tvDiaSemanaNombre);
        }

        void bind(LocalDate fecha) {
            tvDiaNumero.setText(String.format(Locale.getDefault(), "%d", fecha.getDayOfMonth()));
            tvDiaMesNombre.setText(nombreMesAbreviado(fecha));
            tvDiaSemanaNombre.setText(nombreDiaSemana(fecha));
        }
    }

    static class SinVisitasViewHolder extends RecyclerView.ViewHolder {
        // item_agenda_vacio.xml es un TextView como raíz, no hay que buscar un id hijo
        private final TextView tvMensaje;

        SinVisitasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMensaje = (TextView) itemView;
        }

        void bind(String mensaje) {
            tvMensaje.setText(mensaje);
        }
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHoraEvento;
        private final TextView tvDuracionEvento;
        private final TextView tvTituloEvento;
        private final TextView tvEmpresaEvento;
        private final TextView tvDescripcionEvento;
        private final TextView tvEstadoEvento;

        EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoraEvento = itemView.findViewById(R.id.tvHoraEvento);
            tvDuracionEvento = itemView.findViewById(R.id.tvDuracionEvento);
            tvTituloEvento = itemView.findViewById(R.id.tvTituloEvento);
            tvEmpresaEvento = itemView.findViewById(R.id.tvEmpresaEvento);
            tvDescripcionEvento = itemView.findViewById(R.id.tvDescripcionEvento);

            tvEstadoEvento = itemView.findViewById(R.id.tvEstadoEvento);
        }

        void bind(BaseAgenda evento) {
            tvTituloEvento.setText(evento.getTitulo());

            String empresa = evento.getEmpresa();
            boolean sinEmpresa = empresa == null || empresa.trim().isEmpty();
            tvEmpresaEvento.setVisibility(sinEmpresa ? View.GONE : View.VISIBLE);
            if (!sinEmpresa) {
                tvEmpresaEvento.setText(empresa);
            }

            // "hora" es el mismo campo para las dos partes: la propone el promotor al
            // agendar y el analista la deja o la cambia desde la web al asignar
            // técnico. El técnico sigue sin asignar hasta que la web lo haga — eso lo
            // sigue reflejando el chip de estado ("PENDIENTE TÉCNICO"), no esta hora.
            boolean sinHora = evento.getHora() == null || evento.getHora().trim().isEmpty();
            if (sinHora) {
                tvHoraEvento.setText(R.string.agenda_hora_por_definir);
                tvDuracionEvento.setVisibility(View.GONE);
            } else {
                tvHoraEvento.setText(evento.getHora());
                tvDuracionEvento.setVisibility(View.VISIBLE);
            }

            // Tercera línea: la dirección que el usuario escribió al crear el contacto.
            String direccion = evento.getDescripcion();
            if (direccion == null || direccion.trim().isEmpty()) {
                tvDescripcionEvento.setText(R.string.agenda_descripcion_pendiente_asignar);
            } else {
                tvDescripcionEvento.setText(direccion);
            }

            aplicarEstado(tvEstadoEvento, evento);
        }

        /**
         * La web es la única que decide el estado de la cita (pendiente, confirmado,
         * reagendada, cancelada, o cualquier otro que agreguen a futuro) escribiendo
         * directo en la columna estado_agenda — la app solo lo refleja, no decide nada.
         * Un valor desconocido se muestra tal cual (capitalizado) con el estilo neutro
         * de "pendiente", para no ocultar información si la web agrega un estado nuevo.
         * "Visitado" es local (evidencia de proforma ya subida) y pisa cualquier
         * estado_agenda que traiga la web: si ya se visitó, ese es el dato que importa.
         */
        private void aplicarEstado(TextView tvEstado, BaseAgenda evento) {
            if (evento.visitado) {
                tvEstado.setText(R.string.agenda_estado_visitado);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_confirmado);
                return;
            }

            String estadoAgenda = evento.getEstado();
            String estado = estadoAgenda == null ? "" : estadoAgenda.trim().toLowerCase(Locale.getDefault());

            if (Constantes.ESTADO_AGENDA_CONFIRMADO.equals(estado)) {
                tvEstado.setText(R.string.agenda_estado_confirmado);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_confirmado);
            } else if (Constantes.ESTADO_AGENDA_REAGENDADA.equals(estado)) {
                tvEstado.setText(R.string.agenda_estado_cita_reagendada);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_pendiente);
            } else if (Constantes.ESTADO_AGENDA_CANCELADA.equals(estado)) {
                tvEstado.setText(R.string.agenda_estado_cita_cancelada);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_cancelada);
            } else if (Constantes.ESTADO_AGENDA_VENCIDA.equals(estado)) {
                tvEstado.setText(R.string.agenda_estado_vencida);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_cancelada);
            } else if (Constantes.ESTADO_AGENDA_COMPLETADA.equals(estado)) {
                tvEstado.setText(R.string.agenda_estado_completada);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_confirmado);
            } else if (Constantes.ESTADO_AGENDA_PENDIENTE.equals(estado) || estado.isEmpty()) {
                tvEstado.setText(R.string.agenda_estado_pendiente);
                tvEstado.setBackgroundResource(R.drawable.bg_chip_pendiente);
            } else {
                // Estado nuevo que la web empezó a usar y la app todavía no conoce.
                tvEstado.setText(estadoAgenda.substring(0, 1).toUpperCase(Locale.getDefault()) + estadoAgenda.substring(1));
                tvEstado.setBackgroundResource(R.drawable.bg_chip_pendiente);
            }
        }
    }
}
