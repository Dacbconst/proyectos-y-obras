package com.luckyecuador.app.PintucoAPP.ui.proyectosobras.agenda;

import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.SyncThrottle;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.core.OutDateStyle;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterAgenda;
import com.luckyecuador.app.PintucoAPP.Adaptadores.StickyHeaderDecoration;
import com.luckyecuador.app.PintucoAPP.Clase.BaseAgenda;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kotlin.Unit;

/**
 * Agenda de visitas (vista mensual estilo Google Calendar). Lee directo de la tabla
 * local de Contacto (insert_proyectos_contacto) — no hay tabla/endpoint propios de
 * agenda todavía, por eso todo aparece "pendiente, sin hora" hasta que la web lo edita.
 */
public class AgendaFragment extends Fragment implements AdapterAgenda.AccionesListener {

    private static final String TAG = "AgendaFragment";
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String[] MESES = {
            Constantes.JAN, Constantes.FEB, Constantes.MAR, Constantes.APR,
            Constantes.MAY, Constantes.JUN, Constantes.JUL, Constantes.AUG,
            Constantes.SEP, Constantes.OCT, Constantes.NOV, Constantes.DEC
    };

    private static final long TIEMPO_MAXIMO_REFRESH_MS = 12000;

    /** Pendientes = agenda de siempre. Visitados = ya se subió evidencia de proforma (ver obtenerIdsAgendamientoConEvidenciaProforma). */
    private enum SeccionAgenda { PENDIENTES, VISITADOS }

    private TextView tvMesActual;
    private CalendarView calendarView;
    private RecyclerView rvAgenda;
    private SwipeRefreshLayout swipeRefreshAgenda;
    private TextView pillAgendaPendientes;
    private TextView pillAgendaVisitados;

    private YearMonth mesVisible;
    private LocalDate fechaSeleccionada;
    private SeccionAgenda seccionActual = SeccionAgenda.PENDIENTES;
    private List<BaseAgenda> agendamientos;
    private List<BaseAgenda> agendamientosVisitados;
    private Map<LocalDate, Integer> conteoPorFecha;

    private final Handler handlerRefresh = new Handler(Looper.getMainLooper());
    private final Runnable detenerRefreshPorTiempoAgotado = () -> {
        if (swipeRefreshAgenda != null) {
            swipeRefreshAgenda.setRefreshing(false);
        }
        recargarAgenda();
    };

    /** Se dispara cuando SyncAdapter aplica cambios reales sobre Contacto (bajó algo nuevo de la web): refrescar. */
    private final ContentObserver observadorContactos = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
            recargarAgenda();
            if (swipeRefreshAgenda != null) {
                swipeRefreshAgenda.setRefreshing(false);
            }
        }
    };

    /** Cuando el servidor no tenía nada nuevo, SyncAdapter no dispara el ContentObserver, solo este broadcast — sin escucharlo el spinner dependía solo del timeout de 12s. */
    private final BroadcastReceiver receptorSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mensaje = intent.getStringExtra(Mensajes.EXTRA_MENSAJE);
            if (mensaje == null) {
                return;
            }
            if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CONTACTOS) || mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Contactos")) {
                handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
                if (swipeRefreshAgenda != null) {
                    swipeRefreshAgenda.setRefreshing(false);
                }
                recargarAgenda();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyectos_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMesActual = view.findViewById(R.id.tvMesActual);
        calendarView = view.findViewById(R.id.calendarView);
        rvAgenda = view.findViewById(R.id.rvAgenda);
        swipeRefreshAgenda = view.findViewById(R.id.swipeRefreshAgenda);
        pillAgendaPendientes = view.findViewById(R.id.pillAgendaPendientes);
        pillAgendaVisitados = view.findViewById(R.id.pillAgendaVisitados);

        rvAgenda.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAgenda.addItemDecoration(new StickyHeaderDecoration());

        fechaSeleccionada = LocalDate.now();
        mesVisible = YearMonth.now();

        configurarCalendario();

        pillAgendaPendientes.setOnClickListener(v -> cambiarSeccion(SeccionAgenda.PENDIENTES));
        pillAgendaVisitados.setOnClickListener(v -> cambiarSeccion(SeccionAgenda.VISITADOS));

        view.findViewById(R.id.ivMesAnterior).setOnClickListener(v -> irAMes(mesVisible.minusMonths(1)));
        view.findViewById(R.id.ivMesSiguiente).setOnClickListener(v -> irAMes(mesVisible.plusMonths(1)));
        tvMesActual.setOnClickListener(v -> mostrarSelectorMes());

        swipeRefreshAgenda.setOnRefreshListener(this::actualizarDesdeElServidor);
        // El RecyclerView es el que de verdad scrollea; sin preguntarle esto, el refresh se activaría a mitad de lista
        swipeRefreshAgenda.setOnChildScrollUpCallback((parent, child) -> rvAgenda.canScrollVertically(-1));

        recargarAgenda();
    }

    /** Deslizar con el dedo: pide al servidor lo último de Contacto y refresca, sin esperar el ciclo normal. */
    private void actualizarDesdeElServidor() {
        actualizarDesdeElServidor(false);
    }

    /** silencioso = true: sync de fondo al entrar al tab, sin Toast de error ni bloquear la vista. */
    private void actualizarDesdeElServidor(boolean silencioso) {
        if (!VerificarNet.hayConexion(getContext())) {
            if (!silencioso) {
                Toast.makeText(getContext(), R.string.agenda_error_sin_conexion_refresh, Toast.LENGTH_SHORT).show();
            }
            swipeRefreshAgenda.setRefreshing(false);
            return;
        }

        SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

        SyncAdapter.sincronizarAhora(requireContext(), false, Constantes.bajar_contactos, usuario);

        // Respaldo por si SyncAdapter no dispara el ContentObserver: el spinner no queda girando para siempre
        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
        handlerRefresh.postDelayed(detenerRefreshPorTiempoAgotado, TIEMPO_MAXIMO_REFRESH_MS);
    }

    /** El ViewPager no destruye fragments al cambiar de pestaña; sin este onResume, un contacto recién guardado no aparecería. */
    @Override
    public void onResume() {
        super.onResume();
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertProyectosContacto.CONTENT_URI, true, observadorContactos);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receptorSync, new IntentFilter(Intent.ACTION_SYNC));
        recargarAgenda();
        if (SyncThrottle.puedeSincronizar()) {
            actualizarDesdeElServidor(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().getContentResolver().unregisterContentObserver(observadorContactos);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receptorSync);
        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
    }

    private void recargarAgenda() {
        cargarAgendamientosDesdeRegistro();
        conteoPorFecha = calcularConteoPorFecha();
        actualizarPillsAgenda();
        actualizarTituloMes();
        renderizarAgenda();
        calendarView.notifyCalendarChanged();
    }

    /** Cambia entre Agenda (pendientes) y Visitados sin volver a leer la base: ya están ambas listas en memoria. */
    private void cambiarSeccion(SeccionAgenda seccion) {
        if (seccion == seccionActual) {
            return;
        }
        seccionActual = seccion;
        conteoPorFecha = calcularConteoPorFecha();
        actualizarPillsAgenda();
        renderizarAgenda();
        calendarView.notifyCalendarChanged();
    }

    private List<BaseAgenda> listaActiva() {
        return seccionActual == SeccionAgenda.VISITADOS ? agendamientosVisitados : agendamientos;
    }

    /** Los contadores de las pills reflejan solo mesVisible, igual que la lista de abajo — no el total histórico. */
    private void actualizarPillsAgenda() {
        // El listener de scroll del calendario puede disparar antes de la primera carga (agendamientos aún null)
        if (agendamientos == null || agendamientosVisitados == null) {
            return;
        }
        pillAgendaPendientes.setText(getString(R.string.agenda_pill_pendientes, contarEnMes(agendamientos, mesVisible)));
        pillAgendaVisitados.setText(getString(R.string.agenda_pill_visitados, contarEnMes(agendamientosVisitados, mesVisible)));
        aplicarEstiloPillAgenda(pillAgendaPendientes, seccionActual == SeccionAgenda.PENDIENTES);
        aplicarEstiloPillAgenda(pillAgendaVisitados, seccionActual == SeccionAgenda.VISITADOS);
    }

    private int contarEnMes(List<BaseAgenda> lista, YearMonth mes) {
        int total = 0;
        for (BaseAgenda evento : lista) {
            LocalDate fecha = parsearFecha(evento.getFecha());
            if (fecha != null && YearMonth.from(fecha).equals(mes)) {
                total++;
            }
        }
        return total;
    }

    private void aplicarEstiloPillAgenda(TextView pill, boolean activa) {
        pill.setBackgroundResource(activa ? R.drawable.bg_chip_pill_activo : R.drawable.bg_chip_pill_inactivo);
        pill.setTextColor(getResources().getColor(activa ? R.color.proforma_titulo : R.color.plomo_pintuco));
    }

    private void configurarCalendario() {
        YearMonth inicio = mesVisible.minusMonths(12);
        YearMonth fin = mesVisible.plusMonths(12);
        calendarView.setup(inicio, fin, DayOfWeek.MONDAY);
        calendarView.setOutDateStyle(OutDateStyle.EndOfRow);
        calendarView.scrollToMonth(mesVisible);

        calendarView.setMonthScrollListener(calendarMonth -> {
            YearMonth mesAnterior = mesVisible;
            mesVisible = calendarMonth.getYearMonth();
            actualizarTituloMes();
            tvMesActual.setEnabled(true);

            if (!mesVisible.equals(mesAnterior)) {
                // Respeta la fecha ya elegida si ya está en este mes; si no, cae al día 1
                if (!YearMonth.from(fechaSeleccionada).equals(mesVisible)) {
                    fechaSeleccionada = mesVisible.atDay(1);
                }
                actualizarPillsAgenda();
                renderizarAgenda();
                calendarView.notifyCalendarChanged();
            }
            return Unit.INSTANCE;
        });

        calendarView.setDayBinder(new MonthDayBinder<DiaViewContainer>() {
            @NonNull
            @Override
            public DiaViewContainer create(@NonNull View view) {
                return new DiaViewContainer(view);
            }

            @Override
            public void bind(@NonNull DiaViewContainer container, CalendarDay data) {
                container.bind(data);
            }
        });
    }

    private void irAMes(YearMonth mes) {
        tvMesActual.setEnabled(false);
        calendarView.smoothScrollToMonth(mes);
    }

    private void mostrarSelectorMes() {
        String[] nombresMeses = new String[12];
        for (int i = 0; i < 12; i++) {
            nombresMeses[i] = MESES[i];
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(String.valueOf(mesVisible.getYear()))
                .setItems(nombresMeses, (dialog, indiceMes) ->
                        calendarView.smoothScrollToMonth(YearMonth.of(mesVisible.getYear(), indiceMes + 1)))
                .show();
    }

    private void actualizarTituloMes() {
        tvMesActual.setText(String.format(Locale.getDefault(), "%s %d",
                MESES[mesVisible.getMonthValue() - 1].toUpperCase(Locale.getDefault()), mesVisible.getYear()));
    }

    private void seleccionarDia(LocalDate dia) {
        LocalDate anterior = fechaSeleccionada;
        fechaSeleccionada = dia;
        calendarView.notifyDateChanged(anterior);
        calendarView.notifyDateChanged(dia);
        renderizarAgenda();
    }

    /** Día "fuera de mes" con visita: navega el calendario a su mes real (la lista solo renderiza mesVisible). */
    private void irADiaDeOtroMes(LocalDate dia) {
        fechaSeleccionada = dia;
        calendarView.smoothScrollToMonth(YearMonth.from(dia));
    }

    private void renderizarAgenda() {
        List<AdapterAgenda.Fila> filas = new ArrayList<>();
        LocalDate cursor = mesVisible.atDay(1);
        LocalDate finDeMes = mesVisible.atEndOfMonth();
        int posicionDiaSeleccionado = -1;

        while (!cursor.isAfter(finDeMes)) {
            List<BaseAgenda> eventosDelDia = eventosDelDia(cursor);
            if (!eventosDelDia.isEmpty()) {
                if (cursor.equals(fechaSeleccionada)) {
                    posicionDiaSeleccionado = filas.size();
                }
                filas.add(AdapterAgenda.Fila.encabezado(cursor));
                for (BaseAgenda evento : eventosDelDia) {
                    filas.add(AdapterAgenda.Fila.evento(evento));
                }
            }
            cursor = cursor.plusDays(1);
        }

        if (filas.isEmpty()) {
            int mensaje = seccionActual == SeccionAgenda.VISITADOS
                    ? R.string.agenda_sin_visitados
                    : R.string.agenda_sin_visitas;
            filas.add(AdapterAgenda.Fila.sinVisitas(getString(mensaje)));
        }

        rvAgenda.setAdapter(new AdapterAgenda(filas, this));

        // Si el día elegido tiene visitas, se hace scroll hasta ahí (el calendario queda fijo arriba)
        int posicionFinal = posicionDiaSeleccionado;
        if (posicionFinal >= 0) {
            rvAgenda.post(() -> desplazarHastaFila(posicionFinal));
        }
    }

    private void desplazarHastaFila(int posicion) {
        RecyclerView.LayoutManager layoutManager = rvAgenda.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(requireContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(posicion);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    private List<BaseAgenda> eventosDelDia(LocalDate dia) {
        List<BaseAgenda> resultado = new ArrayList<>();
        for (BaseAgenda evento : listaActiva()) {
            LocalDate fechaEvento = parsearFecha(evento.getFecha());
            if (dia.equals(fechaEvento)) {
                resultado.add(evento);
            }
        }
        return resultado;
    }

    private Map<LocalDate, Integer> calcularConteoPorFecha() {
        Map<LocalDate, Integer> conteo = new HashMap<>();
        for (BaseAgenda evento : listaActiva()) {
            LocalDate fecha = parsearFecha(evento.getFecha());
            if (fecha != null) {
                conteo.put(fecha, conteo.getOrDefault(fecha, 0) + 1);
            }
        }
        return conteo;
    }

    /**
     * Las columnas de agenda (titulo, hora, tecnico, estado_agenda) viven en la misma
     * tabla de Contacto. Si "titulo" está vacío (la web no lo editó), se usa contacto+empresa.
     */
    private void cargarAgendamientosDesdeRegistro() {
        List<BaseAgenda> eventos = new ArrayList<>();
        List<BaseAgenda> eventosVisitados = new ArrayList<>();
        Set<String> idsConEvidenciaProforma = obtenerIdsAgendamientoConEvidenciaProforma();

        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProyectosContacto.CONTENT_URI, null, null, null, null)) {

            if (cursor == null) {
                agendamientos = eventos;
                agendamientosVisitados = eventosVisitados;
                return;
            }

            int colId = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
            int colEmpresa = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.EMPRESA);
            int colFechaAgendamiento = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO);
            int colTitulo = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.TITULO);
            int colHora = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.HORA);
            int colDireccion = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.DIRECCION);
            int colEstadoAgenda = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA);
            int colActivar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.ACTIVAR);
            int colReagendado = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.REAGENDADO);
            int colCodigoPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.CODIGO_PDV);
            int colPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.PDV);
            int colContacto = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.CONTACTO);
            int colMail = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.MAIL);
            int colTelefono = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.TELEFONO);
            int colTelefonoConvencional = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL);
            int colTecnico = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.TECNICO);
            int colLugar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LUGAR);
            int colLatitud = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LATITUD);
            int colLongitud = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LONGITUD);
            int colFechaRegistro = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA_REGISTRO);

            while (cursor.moveToNext()) {
                String fechaAgendamiento = cursor.getString(colFechaAgendamiento);
                if (fechaAgendamiento == null || fechaAgendamiento.trim().isEmpty()) {
                    continue;
                }

                // "Eliminar" desde la web es borrado lógico (activar='NO'): la cita desaparece de Agenda
                String activar = limpiarNulo(cursor.getString(colActivar));
                if ("NO".equalsIgnoreCase(activar)) {
                    continue;
                }

                // idRemota puede venir null en registros creados en el celular que aún no bajaron su respuesta
                String id = limpiarNulo(cursor.getString(colId));

                // Ya se mandó la primera foto de proforma: cuenta como asistencia. Ya no desaparece
                // de Agenda, se mueve a la sección "Visitados" (ver SeccionAgenda).
                boolean visitado = id != null && idsConEvidenciaProforma.contains(id);

                String empresa = limpiarNulo(cursor.getString(colEmpresa));
                String tituloGuardado = limpiarNulo(cursor.getString(colTitulo));
                String titulo = tituloGuardado == null || tituloGuardado.trim().isEmpty()
                        ? getString(R.string.contacto_titulo_default)
                        : tituloGuardado;

                // "hora" es el mismo campo para promotor y analista, ya en formato hh:mm a
                String hora = limpiarNulo(cursor.getString(colHora));
                String direccion = limpiarNulo(cursor.getString(colDireccion));
                String estadoAgenda = limpiarNulo(cursor.getString(colEstadoAgenda));
                if (estadoAgenda == null || estadoAgenda.trim().isEmpty()) {
                    estadoAgenda = Constantes.ESTADO_AGENDA_PENDIENTE;
                }
                String reagendado = limpiarNulo(cursor.getString(colReagendado));

                BaseAgenda evento = new BaseAgenda(id, fechaAgendamiento, hora, titulo, empresa, direccion, estadoAgenda, activar, reagendado);
                evento.visitado = visitado;
                evento.codigoPdv = cursor.getString(colCodigoPdv);
                evento.pdv = limpiarNulo(cursor.getString(colPdv));
                evento.contacto = limpiarNulo(cursor.getString(colContacto));
                evento.mail = limpiarNulo(cursor.getString(colMail));
                evento.telefono = limpiarNulo(cursor.getString(colTelefono));
                evento.telefonoConvencional = limpiarNulo(cursor.getString(colTelefonoConvencional));
                evento.tecnico = limpiarNulo(cursor.getString(colTecnico));
                evento.latitud = cursor.getString(colLatitud);
                evento.longitud = cursor.getString(colLongitud);
                evento.fechaRegistro = limpiarNulo(cursor.getString(colFechaRegistro));
                // "lugar" es la dirección que pone la Web; "direccion" es la del promotor (tercera línea de la card)
                evento.lugar = limpiarNulo(cursor.getString(colLugar));

                (visitado ? eventosVisitados : eventos).add(evento);
            }
        }

        agendamientos = eventos;
        agendamientosVisitados = eventosVisitados;
    }

    /** Ids con al menos una fila en insert_proforma con evidencia. Consulta aparte para no hacer N+1. */
    private Set<String> obtenerIdsAgendamientoConEvidenciaProforma() {
        Set<String> ids = new HashSet<>();

        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProforma.CONTENT_URI,
                new String[]{ContractInsertProforma.Columnas.ID_AGENDAMIENTO, ContractInsertProforma.Columnas.EVIDENCIA},
                ContractInsertProforma.Columnas.EVIDENCIA + " IS NOT NULL AND " + ContractInsertProforma.Columnas.EVIDENCIA + " != ''",
                null, null)) {

            if (cursor == null) {
                return ids;
            }

            int colIdAgendamiento = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ID_AGENDAMIENTO);
            while (cursor.moveToNext()) {
                String idAgendamiento = limpiarNulo(cursor.getString(colIdAgendamiento));
                if (idAgendamiento != null) {
                    ids.add(idAgendamiento);
                }
            }
        }

        return ids;
    }

    /** Ventana con los datos completos del agendamiento que no caben en la tarjeta resumida. */
    @Override
    public void onVerDetalle(BaseAgenda evento) {
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_proforma_detalle_visita, null);

        TextView tvTitulo = vista.findViewById(R.id.tvDetalleTitulo);
        TextView tvEstado = vista.findViewById(R.id.tvDetalleEstado);
        TextView tvRegistrado = vista.findViewById(R.id.tvDetalleRegistrado);
        TextView tvFecha = vista.findViewById(R.id.tvDetalleFecha);
        TextView tvHora = vista.findViewById(R.id.tvDetalleHora);
        TextView tvTecnico = vista.findViewById(R.id.tvDetalleTecnico);
        TextView tvContacto = vista.findViewById(R.id.tvDetalleContacto);
        TextView tvEmpresa = vista.findViewById(R.id.tvDetalleEmpresa);
        TextView tvTelefono = vista.findViewById(R.id.tvDetalleTelefono);
        TextView tvCorreo = vista.findViewById(R.id.tvDetalleCorreo);
        TextView tvDireccion = vista.findViewById(R.id.tvDetalleDireccion);
        TextView tvPdv = vista.findViewById(R.id.tvDetallePdv);
        View filaTelefono = vista.findViewById(R.id.filaAccionTelefono);
        View filaCorreo = vista.findViewById(R.id.filaAccionCorreo);
        View filaDireccion = vista.findViewById(R.id.filaAccionDireccion);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarDetalleVisita);
        ImageView ivEliminar = vista.findViewById(R.id.ivEliminarAgendamiento);
        android.widget.Switch switchEditar = vista.findViewById(R.id.switchModoEdicion);
        ImageView ivLapizContacto = vista.findViewById(R.id.ivLapizContacto);
        ImageView ivLapizEmpresa = vista.findViewById(R.id.ivLapizEmpresa);
        ImageView ivLapizTelefono = vista.findViewById(R.id.ivLapizTelefono);
        ImageView ivLapizCorreo = vista.findViewById(R.id.ivLapizCorreo);
        ImageView ivLapizDireccion = vista.findViewById(R.id.ivLapizDireccion);
        View btnGuardarCambios = vista.findViewById(R.id.btnGuardarCambiosDetalle);
        View bloqueNoEditable = vista.findViewById(R.id.bloqueNoEditable);

        tvTitulo.setText(evento.getTitulo());
        aplicarEstadoDetalleBadge(tvEstado, evento);
        tvRegistrado.setText(getString(R.string.proforma_detalle_registrado, formatearFechaRegistro(evento.fechaRegistro)));

        boolean pendienteDeAgendar = evento.getHora() == null;
        tvFecha.setText(pendienteDeAgendar ? getString(R.string.proforma_detalle_pendiente) : evento.getFecha());
        tvHora.setText(pendienteDeAgendar ? getString(R.string.proforma_detalle_pendiente) : evento.getHora());
        tvTecnico.setText(evento.tecnico == null ? getString(R.string.proforma_detalle_pendiente) : evento.tecnico);

        // Ya visitado: la tarjeta queda puramente informativa, sin switch de edición ni borrado.
        if (evento.visitado) {
            switchEditar.setVisibility(View.GONE);
            ivEliminar.setVisibility(View.GONE);
        } else {
            switchEditar.setVisibility(View.VISIBLE);
            // Borrar solo tiene sentido si nadie de oficina asignó técnico (obligatorio en la web,
            // así que su ausencia es la señal confiable — "hora" ya puede venir del propio promotor)
            boolean tecnicoAsignado = evento.tecnico != null && !evento.tecnico.trim().isEmpty();
            ivEliminar.setVisibility(!tecnicoAsignado ? View.VISIBLE : View.GONE);
        }

        tvContacto.setText(evento.contacto);
        tvEmpresa.setText(evento.getEmpresa());

        // Valores en memoria para el modo edición (null = sin cambio en ese campo)
        final String[] editContacto = {null};
        final String[] editEmpresa = {null};
        final String[] editTelefono = {null};
        final String[] editCorreo = {null};
        final String[] editDireccion = {null};

        Runnable actualizarBotonGuardar = () -> {
            boolean hayCambios = editContacto[0] != null || editEmpresa[0] != null
                    || editTelefono[0] != null || editCorreo[0] != null || editDireccion[0] != null;
            btnGuardarCambios.setVisibility(hayCambios ? View.VISIBLE : View.GONE);
        };

        String telefono = construirTelefonoMostrado(evento);
        tvTelefono.setText(telefono != null ? telefono : getString(R.string.proforma_detalle_sin_telefono));
        tvCorreo.setText(evento.mail != null ? evento.mail : getString(R.string.proforma_detalle_sin_correo));
        tvDireccion.setText(evento.lugar != null ? evento.lugar : getString(R.string.proforma_detalle_sin_direccion));
        tvPdv.setText(evento.pdv != null ? evento.pdv : evento.codigoPdv);

        // Valores originales para detectar ediciones que en realidad no cambiaron nada
        // (ej. borrar el campo y volver a escribir el mismo valor)
        final String originalContacto = tvContacto.getText().toString();
        final String originalEmpresa = tvEmpresa.getText().toString();
        final String originalTelefono = tvTelefono.getText().toString();
        final String originalCorreo = tvCorreo.getText().toString();
        final String originalDireccion = tvDireccion.getText().toString();

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .create();

        // Modo lectura: acciones de contacto (marcar, email, mapa)
        Runnable aplicarModoLectura = () -> {
            switchEditar.setChecked(false);
            int[] lapices = {android.view.View.GONE};
            ivLapizContacto.setVisibility(View.GONE);
            ivLapizEmpresa.setVisibility(View.GONE);
            ivLapizTelefono.setVisibility(View.GONE);
            ivLapizCorreo.setVisibility(View.GONE);
            ivLapizDireccion.setVisibility(View.GONE);
            bloqueNoEditable.setAlpha(1f);
            tvTecnico.setAlpha(1f);
            tvPdv.setAlpha(1f);
            if (telefono != null) {
                filaTelefono.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + evento.telefono))));
            } else {
                filaTelefono.setOnClickListener(null);
            }
            if (evento.mail != null) {
                filaCorreo.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + evento.mail))));
            } else {
                filaCorreo.setOnClickListener(null);
            }
            if (evento.lugar != null) {
                String urlMapa = construirUrlMapa(evento);
                filaDireccion.setOnClickListener(urlMapa == null ? null : v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlMapa))));
            } else {
                filaDireccion.setOnClickListener(null);
            }
        };

        // Modo edición: lápices visibles, no-editables atenuados, acciones reemplazadas por edición
        Runnable aplicarModoEdicion = () -> {
            ivLapizContacto.setVisibility(View.VISIBLE);
            ivLapizEmpresa.setVisibility(View.VISIBLE);
            ivLapizTelefono.setVisibility(View.VISIBLE);
            ivLapizCorreo.setVisibility(View.VISIBLE);
            ivLapizDireccion.setVisibility(View.VISIBLE);
            bloqueNoEditable.setAlpha(0.4f);
            tvTecnico.setAlpha(0.4f);
            tvPdv.setAlpha(0.4f);
            filaTelefono.setOnClickListener(null);
            filaCorreo.setOnClickListener(null);
            filaDireccion.setOnClickListener(null);
        };

        // Lápices: diálogo con EditText para cada campo editable
        ivLapizContacto.setOnClickListener(v -> mostrarEditorCampo(getString(R.string.proforma_detalle_contacto),
                editContacto[0] != null ? editContacto[0] : tvContacto.getText().toString(),
                nuevo -> {
                    editContacto[0] = nuevo.equals(originalContacto) ? null : nuevo;
                    tvContacto.setText(nuevo);
                    actualizarBotonGuardar.run();
                }));

        ivLapizEmpresa.setOnClickListener(v -> mostrarEditorCampo(getString(R.string.proforma_detalle_empresa),
                editEmpresa[0] != null ? editEmpresa[0] : tvEmpresa.getText().toString(),
                nuevo -> {
                    editEmpresa[0] = nuevo.equals(originalEmpresa) ? null : nuevo;
                    tvEmpresa.setText(nuevo);
                    actualizarBotonGuardar.run();
                }));

        ivLapizTelefono.setOnClickListener(v -> mostrarEditorCampo(getString(R.string.proforma_detalle_telefono),
                editTelefono[0] != null ? editTelefono[0] : tvTelefono.getText().toString(),
                nuevo -> {
                    editTelefono[0] = nuevo.equals(originalTelefono) ? null : nuevo;
                    tvTelefono.setText(nuevo);
                    actualizarBotonGuardar.run();
                }));

        ivLapizCorreo.setOnClickListener(v -> mostrarEditorCampo(getString(R.string.proforma_detalle_correo),
                editCorreo[0] != null ? editCorreo[0] : tvCorreo.getText().toString(),
                nuevo -> {
                    editCorreo[0] = nuevo.equals(originalCorreo) ? null : nuevo;
                    tvCorreo.setText(nuevo);
                    actualizarBotonGuardar.run();
                }));

        ivLapizDireccion.setOnClickListener(v -> mostrarEditorCampo(getString(R.string.proforma_detalle_direccion),
                editDireccion[0] != null ? editDireccion[0] : tvDireccion.getText().toString(),
                nuevo -> {
                    editDireccion[0] = nuevo.equals(originalDireccion) ? null : nuevo;
                    tvDireccion.setText(nuevo);
                    actualizarBotonGuardar.run();
                }));

        switchEditar.setOnCheckedChangeListener((btn, checked) -> {
            if (checked) aplicarModoEdicion.run();
            else aplicarModoLectura.run();
        });

        // Guardar cambios en la base local y sincronizar
        btnGuardarCambios.setOnClickListener(v -> {
            android.content.ContentValues valores = new android.content.ContentValues();
            if (editContacto[0] != null) valores.put(ContractInsertProyectosContacto.Columnas.CONTACTO, editContacto[0]);
            if (editEmpresa[0] != null)  valores.put(ContractInsertProyectosContacto.Columnas.EMPRESA,  editEmpresa[0]);
            if (editTelefono[0] != null) valores.put(ContractInsertProyectosContacto.Columnas.TELEFONO, editTelefono[0]);
            if (editCorreo[0] != null)   valores.put(ContractInsertProyectosContacto.Columnas.MAIL,     editCorreo[0]);
            if (editDireccion[0] != null) valores.put(ContractInsertProyectosContacto.Columnas.LUGAR,   editDireccion[0]);
            valores.put(Constantes.PENDIENTE_INSERCION, 1);
            valores.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
            requireContext().getContentResolver().update(
                    ContractInsertProyectosContacto.CONTENT_URI,
                    valores,
                    Constantes.ID_REMOTA + "=?",
                    new String[]{evento.id});
            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertProyectosContacto, null);
            }
            editContacto[0] = null; editEmpresa[0] = null;
            editTelefono[0] = null; editCorreo[0] = null; editDireccion[0] = null;
            btnGuardarCambios.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.detalle_cambios_guardados, Toast.LENGTH_SHORT).show();
        });

        // Soft-delete local vía activar="NO" (mismo criterio que el servidor). Se avisa con
        // delete_agendamiento.php en vez del sync genérico de Contacto, que solo sabe INSERTAR
        // y hubiera creado una fila duplicada en vez de marcar activar='NO' en la existente.
        ivEliminar.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle(R.string.detalle_eliminar_titulo)
                .setMessage(R.string.detalle_eliminar_mensaje)
                .setNegativeButton(R.string.proforma_dialog_cancelar, null)
                .setPositiveButton(R.string.detalle_eliminar_confirmar, (d, w) -> {
                    android.content.ContentValues valores = new android.content.ContentValues();
                    valores.put(ContractInsertProyectosContacto.Columnas.ACTIVAR, "NO");
                    requireContext().getContentResolver().update(
                            ContractInsertProyectosContacto.CONTENT_URI,
                            valores,
                            Constantes.ID_REMOTA + "=?",
                            new String[]{evento.id});
                    if (evento.id != null && VerificarNet.hayConexion(getContext())) {
                        eliminarAgendamientoRemoto(evento.id);
                    }
                    dialog.dismiss();
                    recargarAgenda();
                    Toast.makeText(getContext(), R.string.detalle_eliminar_hecho, Toast.LENGTH_SHORT).show();
                })
                .show());

        // Cerrar: advertencia si hay cambios sin guardar
        ivCerrar.setOnClickListener(v -> {
            boolean hayCambios = editContacto[0] != null || editEmpresa[0] != null
                    || editTelefono[0] != null || editCorreo[0] != null || editDireccion[0] != null;
            if (hayCambios) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.detalle_sin_guardar_titulo)
                        .setMessage(R.string.detalle_sin_guardar_mensaje)
                        .setPositiveButton(R.string.detalle_sin_guardar_salir, (d, w) -> dialog.dismiss())
                        .setNegativeButton(R.string.detalle_sin_guardar_quedar, null)
                        .show();
            } else {
                dialog.dismiss();
            }
        });

        aplicarModoLectura.run();
        dialog.show();
    }

    /** Avisa al servidor que marque activar='NO'. Fire-and-forget: el borrado local ya se aplicó antes de llamar esto. */
    private void eliminarAgendamientoRemoto(String idRemota) {
        try {
            JSONObject body = new JSONObject();
            body.put("id", idRemota);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constantes.DELETE_AGENDAMIENTO,
                    body,
                    response -> Log.i(TAG, "Agendamiento " + idRemota + " eliminado en el servidor"),
                    error -> Log.e(TAG, "Error eliminando agendamiento " + idRemota + " en el servidor: " + error.getMessage(), error));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
        } catch (JSONException e) {
            Log.e(TAG, "Fallo al armar la petición de eliminar agendamiento: " + e.getMessage(), e);
        }
    }

    private interface CampoEditadoListener {
        void onEditado(String nuevoValor);
    }

    private void mostrarEditorCampo(String etiqueta, String valorActual, CampoEditadoListener callback) {
        android.widget.EditText et = new android.widget.EditText(requireContext());
        et.setText(valorActual);
        et.setSelection(et.getText().length());
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        et.setPadding(pad, pad / 2, pad, pad / 2);

        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.detalle_editar_campo, etiqueta))
                .setView(et)
                .setNegativeButton(R.string.proforma_dialog_cancelar, null)
                .setPositiveButton(R.string.proforma_dialog_aceptar, (d, w) -> {
                    String nuevo = et.getText().toString().trim();
                    if (!nuevo.isEmpty()) callback.onEditado(nuevo);
                })
                .show();
    }

    /** Mismo criterio de colores/textos que aplicarEstado() de AdapterAgenda. */
    private void aplicarEstadoDetalleBadge(TextView tvEstado, BaseAgenda evento) {
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
            tvEstado.setText(estadoAgenda.substring(0, 1).toUpperCase(Locale.getDefault()) + estadoAgenda.substring(1));
            tvEstado.setBackgroundResource(R.drawable.bg_chip_pendiente);
        }
    }

    private String construirTelefonoMostrado(BaseAgenda evento) {
        if (evento.telefono == null) {
            return evento.telefonoConvencional;
        }
        return evento.telefonoConvencional == null
                ? evento.telefono
                : evento.telefono + " / " + evento.telefonoConvencional;
    }

    private String construirUrlMapa(BaseAgenda evento) {
        if (evento.latitud == null || evento.longitud == null) {
            return null;
        }
        // Algunas filas traen la coma como separador decimal (configuración
        // regional del servidor) — Google Maps necesita el punto.
        String lat = evento.latitud.replace(',', '.');
        String lon = evento.longitud.replace(',', '.');
        return "https://www.google.com/maps/place/" + lat + "," + lon;
    }

    /** "yyyy-MM-dd HH:mm:ss" (como lo pone MySQL solo) → "dd/MM/yyyy HH:mm". */
    private String formatearFechaRegistro(String fechaRegistroSql) {
        if (fechaRegistroSql == null) {
            return getString(R.string.proforma_detalle_pendiente);
        }
        try {
            SimpleDateFormat origen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat destino = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return destino.format(origen.parse(fechaRegistroSql));
        } catch (ParseException e) {
            return fechaRegistroSql;
        }
    }

    /** Filas viejas pueden tener el texto literal "null" guardado en vez de vacío real — se limpia para no mostrarlo. */
    private String limpiarNulo(String valor) {
        return (valor == null || valor.trim().isEmpty() || valor.trim().equalsIgnoreCase("null")) ? null : valor;
    }

    /**
     * SimpleDateFormat, no DateTimeFormatter: este último es estricto con ceros a la
     * izquierda ("5/7/2026" desde la Web le fallaba y la visita desaparecía de Agenda).
     */
    private LocalDate parsearFecha(String fecha) {
        if (fecha == null) {
            return null;
        }
        try {
            SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA, new Locale("es", "EC"));
            Date fechaParseada = formato.parse(fecha.trim());
            return fechaParseada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    private static final int CANTIDAD_DP_PUNTO = 5;
    private static final int CANTIDAD_DP_BADGE = 13;

    private class DiaViewContainer extends ViewContainer {
        private final TextView tvDia;
        private final TextView tvBadge;

        DiaViewContainer(@NonNull View view) {
            super(view);
            tvDia = view.findViewById(R.id.tvCalendarDayText);
            tvBadge = view.findViewById(R.id.tvCalendarDayBadge);
        }

        void bind(CalendarDay data) {
            tvDia.setText(String.format(Locale.getDefault(), "%d", data.getDate().getDayOfMonth()));

            if (data.getPosition() != DayPosition.MonthDate) {
                tvDia.setAlpha(0.3f);
                tvDia.setBackgroundResource(0);
                tvDia.setTextColor(getResources().getColor(R.color.plomo_pintuco));
                // Se muestra el badge y se deja tocar si ese día de otro mes tiene visita
                int cantidadOtroMes = conteoPorFecha.getOrDefault(data.getDate(), 0);
                actualizarBadge(cantidadOtroMes);
                getView().setOnClickListener(cantidadOtroMes > 0 ? v -> irADiaDeOtroMes(data.getDate()) : null);
                return;
            }

            tvDia.setAlpha(1f);
            boolean seleccionado = data.getDate().equals(fechaSeleccionada);
            tvDia.setBackgroundResource(seleccionado ? R.drawable.bg_circulo_dia_seleccionado : 0);
            tvDia.setTextColor(getResources().getColor(seleccionado ? R.color.colorWhite : R.color.azul_pintuco));

            int cantidad = conteoPorFecha.getOrDefault(data.getDate(), 0);
            actualizarBadge(cantidad);

            getView().setOnClickListener(v -> seleccionarDia(data.getDate()));
        }

        /** Con 1 visita, punto chico de siempre. Con 2+, se agranda y muestra el número. */
        private void actualizarBadge(int cantidad) {
            if (cantidad <= 0) {
                tvBadge.setVisibility(View.INVISIBLE);
                return;
            }

            tvBadge.setVisibility(View.VISIBLE);
            float densidad = getResources().getDisplayMetrics().density;
            if (cantidad == 1) {
                tvBadge.setText(null);
                int tamanoPx = (int) (CANTIDAD_DP_PUNTO * densidad);
                tvBadge.getLayoutParams().width = tamanoPx;
                tvBadge.getLayoutParams().height = tamanoPx;
            } else {
                tvBadge.setText(cantidad > 9 ? "9+" : String.valueOf(cantidad));
                int tamanoPx = (int) (CANTIDAD_DP_BADGE * densidad);
                tvBadge.getLayoutParams().width = tamanoPx;
                tvBadge.getLayoutParams().height = tamanoPx;
            }
            tvBadge.requestLayout();
        }
    }
}
