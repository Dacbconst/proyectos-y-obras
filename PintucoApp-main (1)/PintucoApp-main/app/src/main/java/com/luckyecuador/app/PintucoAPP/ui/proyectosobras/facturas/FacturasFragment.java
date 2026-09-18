package com.luckyecuador.app.PintucoAPP.ui.proyectosobras.facturas;

import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.SyncThrottle;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterFacturas;
import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.FacturaConPagos;
import com.luckyecuador.app.PintucoAPP.Clase.RegistroPagoFactura;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMoneda;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesProforma;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;

import android.text.Editable;
import android.text.TextWatcher;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Módulo "Facturas": lista las facturas ya confirmadas (filas de
 * insert_proforma con foto_factura) como eventos independientes — cada una su
 * propio monto y fecha real de captura, sin plazos ni saldo pendiente. Un
 * mismo agendamiento puede tener varias, una por cada mes que se le facturó.
 * Incluye el total facturado del usuario actual en el mes calendario en
 * curso, para seguir el umbral de comisión mensual.
 */
public class FacturasFragment extends Fragment implements AdapterFacturas.AccionesListener {

    private static final String TAG = "FacturasFragment";
    private static final long TIEMPO_MAXIMO_REFRESH_MS = 12000;

    private static final String[] NOMBRES_MESES_FACTURAS = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private RecyclerView rvFacturas;
    private SwipeRefreshLayout swipeRefreshFacturas;
    private View contenedorFacturasVacio;
    private TextView tvFacturasContador;
    private EditText etBuscarEmpresaFacturas;
    private View selectorMesFacturas;
    private TextView tvMesSeleccionadoFacturas;
    private TextView pillTodoFacturas;
    private TextView pillAPlazosFacturas;
    private TextView pillDirectoFacturas;
    private String filtroEmpresaFacturas = "";

    private enum FiltroFacturas { A_PLAZOS, DIRECTO, TODO }
    private FiltroFacturas filtroActualFacturas = FiltroFacturas.A_PLAZOS;
    // Arranca siempre en el mes calendario actual (GMT-5, igual que el resto de
    // fechas de Proforma/Facturas) — el usuario lo cambia con selectorMesFacturas.
    private int filtroMesSeleccionado;
    private int filtroAnioSeleccionado;

    private List<FacturaConPagos> ultimasFacturas = new ArrayList<>();
    private String usuarioActualSync;
    // Vive en el Fragment (no en el Adapter) para sobrevivir a cada recarga — ver AdapterProforma.
    private final Set<String> idsExpandidos = new HashSet<>();
    // Factura a plazos a la que se le está adjuntando una foto más — puente entre
    // onAdjuntarPago (elige tarjeta) y procesarFotoPago (ya con el resultado de cámara/galería).
    private FacturaConPagos facturaPendientePago;
    // Clave (FacturaConPagos.claveUnica()) de la factura recién creada en Proforma que
    // hay que abrir+scrollear apenas se renderice — se consume una sola vez (ver
    // solicitarAperturaDeFactura/scrollearAFacturaPendiente).
    private String claveFacturaPendienteDeAbrir;

    private final Handler handlerRefresh = new Handler(Looper.getMainLooper());
    private final Runnable detenerRefreshPorTiempoAgotado = () -> {
        if (swipeRefreshFacturas != null) {
            swipeRefreshFacturas.setRefreshing(false);
        }
        cargarFacturas();
    };

    private final ContentObserver observador = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
            cargarFacturas();
            if (swipeRefreshFacturas != null) {
                swipeRefreshFacturas.setRefreshing(false);
            }
        }
    };

    /** Misma cadena de sync que ProformaFragment — ver SyncAdapter.sincronizarAhora(). */
    private final BroadcastReceiver receptorSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mensaje = intent.getStringExtra(Mensajes.EXTRA_MENSAJE);
            if (mensaje == null) {
                return;
            }
            if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CONTACTOS) || mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Contactos")) {
                SyncAdapter.sincronizarAhora(requireContext(), false, Constantes.bajar_proforma, usuarioActualSync);
            } else if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROFORMA) || mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Proforma")) {
                SyncAdapter.sincronizarAhora(requireContext(), false, Constantes.bajar_pago_factura, usuarioActualSync);
            } else if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PAGO_FACTURA) || mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "PagoFactura")) {
                handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
                if (swipeRefreshFacturas != null) {
                    swipeRefreshFacturas.setRefreshing(false);
                }
                cargarFacturas();
            }
        }
    };

    private final ActivityResultLauncher<Intent> cameraPagoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null && facturaPendientePago != null) {
                        procesarFotoPago(uri);
                    }
                }
            });

    private final ActivityResultLauncher<String> galeriaPagoLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && facturaPendientePago != null) {
                    procesarFotoPago(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyectos_facturas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFacturas = view.findViewById(R.id.rvFacturas);
        rvFacturas.setLayoutManager(new LinearLayoutManager(requireContext()));
        contenedorFacturasVacio = view.findViewById(R.id.contenedorFacturasVacio);
        tvFacturasContador = view.findViewById(R.id.tvFacturasContador);

        etBuscarEmpresaFacturas = view.findViewById(R.id.etBuscarEmpresaFacturas);
        etBuscarEmpresaFacturas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtroEmpresaFacturas = s.toString().trim().toLowerCase(Locale.getDefault());
                renderizarListaFiltrada();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Calendar hoy = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        filtroMesSeleccionado = hoy.get(Calendar.MONTH);
        filtroAnioSeleccionado = hoy.get(Calendar.YEAR);

        selectorMesFacturas = view.findViewById(R.id.selectorMesFacturas);
        tvMesSeleccionadoFacturas = view.findViewById(R.id.tvMesSeleccionadoFacturas);
        actualizarTextoMesSeleccionado();
        selectorMesFacturas.setOnClickListener(v -> mostrarSelectorMesAnio());

        pillTodoFacturas = view.findViewById(R.id.pillTodoFacturas);
        pillAPlazosFacturas = view.findViewById(R.id.pillAPlazosFacturas);
        pillDirectoFacturas = view.findViewById(R.id.pillDirectoFacturas);
        pillTodoFacturas.setOnClickListener(v -> seleccionarFiltroFacturas(FiltroFacturas.TODO));
        pillAPlazosFacturas.setOnClickListener(v -> seleccionarFiltroFacturas(FiltroFacturas.A_PLAZOS));
        pillDirectoFacturas.setOnClickListener(v -> seleccionarFiltroFacturas(FiltroFacturas.DIRECTO));

        swipeRefreshFacturas = view.findViewById(R.id.swipeRefreshFacturas);
        swipeRefreshFacturas.setOnRefreshListener(this::actualizarDesdeElServidor);
        swipeRefreshFacturas.setOnChildScrollUpCallback((parent, child) -> rvFacturas.canScrollVertically(-1));

        cargarFacturas();
    }

    private void actualizarTextoMesSeleccionado() {
        Calendar calendario = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        calendario.set(filtroAnioSeleccionado, filtroMesSeleccionado, 1);
        SimpleDateFormat formato = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String texto = formato.format(calendario.getTime());
        tvMesSeleccionadoFacturas.setText(texto.substring(0, 1).toUpperCase(Locale.getDefault()) + texto.substring(1));
    }

    /**
     * Mismo diálogo (mes/año, sin día) que ya usa PropensosFragment para fecha de caducidad.
     * El rango va del mes de la factura más antigua (no tiene sentido ofrecer meses sin
     * datos) hasta el mes calendario actual (no hay forma de tener facturas en el futuro).
     */
    private void mostrarSelectorMesAnio() {
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_month_year_picker, null);
        NumberPicker mesPicker = vista.findViewById(R.id.monthPicker);
        NumberPicker anioPicker = vista.findViewById(R.id.yearPicker);
        Button btnAceptar = vista.findViewById(R.id.btnAceptar);
        Button btnCancelar = vista.findViewById(R.id.btnCancelar);

        Calendar hoy = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        int anioActual = hoy.get(Calendar.YEAR);
        int mesActual = hoy.get(Calendar.MONTH);

        Calendar fechaMinima = calcularFechaMinimaFacturas();
        int anioMinimo = fechaMinima != null ? fechaMinima.get(Calendar.YEAR) : anioActual;
        int mesMinimo = fechaMinima != null ? fechaMinima.get(Calendar.MONTH) : mesActual;
        if (anioMinimo > anioActual || (anioMinimo == anioActual && mesMinimo > mesActual)) {
            anioMinimo = anioActual;
            mesMinimo = mesActual;
        }

        anioPicker.setMinValue(anioMinimo);
        anioPicker.setMaxValue(anioActual);
        int anioInicial = Math.max(anioMinimo, Math.min(filtroAnioSeleccionado, anioActual));
        anioPicker.setValue(anioInicial);

        int anioMinimoFinal = anioMinimo;
        int mesMinimoFinal = mesMinimo;
        ajustarRangoMesPickerFacturas(mesPicker, anioInicial, anioMinimoFinal, mesMinimoFinal, anioActual, mesActual,
                filtroMesSeleccionado + 1);
        anioPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                ajustarRangoMesPickerFacturas(mesPicker, newVal, anioMinimoFinal, mesMinimoFinal, anioActual, mesActual,
                        mesPicker.getValue()));

        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(vista).create();
        btnAceptar.setOnClickListener(v -> {
            filtroMesSeleccionado = mesPicker.getValue() - 1;
            filtroAnioSeleccionado = anioPicker.getValue();
            actualizarTextoMesSeleccionado();
            renderizarListaFiltrada();
            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /** Ajusta el rango de mesPicker al año elegido: acota por abajo en el año mínimo y por arriba en el actual. */
    private void ajustarRangoMesPickerFacturas(NumberPicker mesPicker, int anioSeleccionado, int anioMinimo, int mesMinimo,
                                                int anioActual, int mesActual, int valorPreferido) {
        int minMes = (anioSeleccionado == anioMinimo) ? mesMinimo + 1 : 1;
        int maxMes = (anioSeleccionado == anioActual) ? mesActual + 1 : 12;
        // Resetea a 1–12 antes de acotar: evita un estado intermedio minValue > maxValue
        // si el rango anterior (de otro año) no se solapa con el nuevo.
        mesPicker.setDisplayedValues(null);
        mesPicker.setMinValue(1);
        mesPicker.setMaxValue(12);
        mesPicker.setMinValue(minMes);
        mesPicker.setMaxValue(maxMes);
        mesPicker.setDisplayedValues(Arrays.copyOfRange(NOMBRES_MESES_FACTURAS, minMes - 1, maxMes));
        mesPicker.setValue(Math.max(minMes, Math.min(valorPreferido, maxMes)));
    }

    /** Mes/año de la factura más antigua en ultimasFacturas, o null si todavía no hay ninguna. */
    private Calendar calcularFechaMinimaFacturas() {
        Calendar minima = null;
        for (FacturaConPagos factura : ultimasFacturas) {
            Date parseada = parsearFechaFactura(factura.fechaFactura);
            if (parseada == null) {
                continue;
            }
            if (minima == null || parseada.before(minima.getTime())) {
                minima = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                minima.setTime(parseada);
            }
        }
        return minima;
    }

    private void seleccionarFiltroFacturas(FiltroFacturas filtro) {
        filtroActualFacturas = filtro;
        actualizarEstiloPillsFacturas();
        renderizarListaFiltrada();
    }

    private void actualizarEstiloPillsFacturas() {
        aplicarEstiloPill(pillTodoFacturas, filtroActualFacturas == FiltroFacturas.TODO);
        aplicarEstiloPill(pillAPlazosFacturas, filtroActualFacturas == FiltroFacturas.A_PLAZOS);
        aplicarEstiloPill(pillDirectoFacturas, filtroActualFacturas == FiltroFacturas.DIRECTO);
    }

    private void aplicarEstiloPill(TextView pill, boolean activo) {
        pill.setBackgroundResource(activo ? R.drawable.bg_chip_pill_activo : R.drawable.bg_chip_pill_inactivo);
        pill.setTextColor(getResources().getColor(activo ? R.color.proforma_titulo : R.color.plomo_pintuco));
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertProforma.CONTENT_URI, true, observador);
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertPagoFactura.CONTENT_URI, true, observador);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receptorSync, new IntentFilter(Intent.ACTION_SYNC));
        cargarFacturas();
        if (SyncThrottle.puedeSincronizar()) {
            actualizarDesdeElServidor(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().getContentResolver().unregisterContentObserver(observador);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receptorSync);
        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
    }

    private void actualizarDesdeElServidor() {
        actualizarDesdeElServidor(false);
    }

    private void actualizarDesdeElServidor(boolean silencioso) {
        if (!VerificarNet.hayConexion(getContext())) {
            if (!silencioso) {
                Toast.makeText(getContext(), R.string.agenda_error_sin_conexion_refresh, Toast.LENGTH_SHORT).show();
            }
            swipeRefreshFacturas.setRefreshing(false);
            return;
        }

        SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        usuarioActualSync = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

        // Contactos → Proforma → PagoFactura se encadenan en receptorSync.
        SyncAdapter.sincronizarAhora(requireContext(), false, Constantes.bajar_contactos, usuarioActualSync);

        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
        handlerRefresh.postDelayed(detenerRefreshPorTiempoAgotado, TIEMPO_MAXIMO_REFRESH_MS);
    }

    private void cargarFacturas() {
        ultimasFacturas = cargarFacturasDesdeRegistro();
        renderizarListaFiltrada();
    }

    /**
     * Llamado UNA SOLA VEZ desde ProformaFragment justo después de guardar una
     * factura nueva ("Enviar factura") — mantiene el flujo visual llevando al
     * promotor directo a esa tarjeta en vez de dejarlo perdido en la lista.
     * Fuerza el filtro/mes que garantiza que la tarjeta recién creada quede
     * visible (sin esto, si la pill activa no coincidía con el tipo de la
     * factura o el mes seleccionado no era el actual, la tarjeta simplemente no
     * aparecería en la lista filtrada), la deja expandida, y hace scroll hasta
     * ella. Después de este llamado, la navegación vuelve a ser completamente
     * libre (no hay nada más que dispare esto de nuevo).
     */
    public void solicitarAperturaDeFactura(String clave, boolean esAPlazos) {
        if (rvFacturas == null) {
            return;
        }
        claveFacturaPendienteDeAbrir = clave;
        idsExpandidos.clear();
        idsExpandidos.add(clave);

        filtroEmpresaFacturas = "";
        if (etBuscarEmpresaFacturas != null) {
            etBuscarEmpresaFacturas.setText("");
        }
        Calendar hoy = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        filtroMesSeleccionado = hoy.get(Calendar.MONTH);
        filtroAnioSeleccionado = hoy.get(Calendar.YEAR);
        actualizarTextoMesSeleccionado();
        filtroActualFacturas = esAPlazos ? FiltroFacturas.A_PLAZOS : FiltroFacturas.DIRECTO;
        actualizarEstiloPillsFacturas();

        cargarFacturas();
        scrollearAFacturaPendiente();
    }

    /** Consume claveFacturaPendienteDeAbrir: la busca en la lista ya renderizada y hace scroll hasta ella. */
    private void scrollearAFacturaPendiente() {
        if (claveFacturaPendienteDeAbrir == null) {
            return;
        }
        String clave = claveFacturaPendienteDeAbrir;
        claveFacturaPendienteDeAbrir = null;

        RecyclerView.Adapter<?> adapterActual = rvFacturas.getAdapter();
        if (!(adapterActual instanceof AdapterFacturas)) {
            return;
        }
        int posicion = ((AdapterFacturas) adapterActual).indexOfClave(clave);
        if (posicion < 0) {
            return;
        }
        rvFacturas.post(() -> {
            RecyclerView.LayoutManager lm = rvFacturas.getLayoutManager();
            if (lm instanceof LinearLayoutManager) {
                ((LinearLayoutManager) lm).scrollToPositionWithOffset(posicion, 0);
            }
        });
    }

    /**
     * "Todo" es la única pill que ignora el filtro de mes (por eso va al final):
     * las otras dos (A plazos / Directo) siempre filtran por el mes seleccionado
     * en selectorMesFacturas, así que su cantidad refleja ese mes; "Todo" cuenta
     * sobre TODOS los meses, para que quede claro que ahí no aplica el mes.
     */
    private void renderizarListaFiltrada() {
        List<FacturaConPagos> conEmpresa = new ArrayList<>();
        for (FacturaConPagos factura : ultimasFacturas) {
            if (!filtroEmpresaFacturas.isEmpty()) {
                String empresa = factura.empresa;
                if (empresa == null || !empresa.toLowerCase(Locale.getDefault()).contains(filtroEmpresaFacturas)) {
                    continue;
                }
            }
            conEmpresa.add(factura);
        }

        int cantidadAPlazos = 0;
        int cantidadDirecto = 0;
        List<FacturaConPagos> filtradas = new ArrayList<>();
        for (FacturaConPagos factura : conEmpresa) {
            if (filtroActualFacturas == FiltroFacturas.TODO) {
                filtradas.add(factura);
                continue;
            }
            if (!estaEnMesFiltrado(factura.fechaFactura)) {
                continue;
            }
            FiltroFacturas categoria = factura.esAPlazos() ? FiltroFacturas.A_PLAZOS : FiltroFacturas.DIRECTO;
            if (categoria == FiltroFacturas.A_PLAZOS) {
                cantidadAPlazos++;
            } else {
                cantidadDirecto++;
            }
            if (categoria == filtroActualFacturas) {
                filtradas.add(factura);
            }
        }
        Collections.sort(filtradas, (a, b) ->
                parsearFechaParaOrdenar(b.fechaFactura).compareTo(parsearFechaParaOrdenar(a.fechaFactura)));

        rvFacturas.setAdapter(new AdapterFacturas(construirItemsConSeparadores(filtradas), this, idsExpandidos,
                filtroActualFacturas != FiltroFacturas.TODO));
        boolean vacio = filtradas.isEmpty();
        contenedorFacturasVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        rvFacturas.setVisibility(vacio ? View.GONE : View.VISIBLE);
        tvFacturasContador.setText(getString(R.string.facturas_contador, filtradas.size()));
        pillAPlazosFacturas.setText(getString(R.string.facturas_pill_a_plazos, cantidadAPlazos));
        pillDirectoFacturas.setText(getString(R.string.facturas_pill_directo, cantidadDirecto));
        pillTodoFacturas.setText(getString(R.string.facturas_pill_todo, conEmpresa.size()));

        // "Todo" mira todos los meses, así que el selector de mes no tiene sentido ahí.
        boolean esTodo = filtroActualFacturas == FiltroFacturas.TODO;
        selectorMesFacturas.setVisibility(esTodo ? View.GONE : View.VISIBLE);
    }

    /**
     * "Todo" (ya viene ordenada de más reciente a más antigua): intercala un
     * separador gris "Junio 2026 · $250.00" antes de cada grupo de mes, con el
     * total de ese grupo (suma de lo que cada tarjeta ya muestra como monto
     * principal — pagos si tiene, si no montoTotalFactura).
     *
     * "A plazos": se agrupa por estado — Activos (sigue admitiendo comprobantes),
     * Completado (la suma de comprobantes llegó sola al total cotizado) y
     * Cerrados (se cortó a mano desde "Cierre Factura"), en ese orden, solo las
     * secciones que tengan al menos una tarjeta.
     *
     * "Directo": una única sección "Completados" (ahí no hay otro estado posible).
     */
    private List<Object> construirItemsConSeparadores(List<FacturaConPagos> lista) {
        List<Object> items = new ArrayList<>();
        if (filtroActualFacturas == FiltroFacturas.TODO) {
            List<FacturaConPagos> grupoActual = new ArrayList<>();
            String claveGrupoActual = null;
            for (FacturaConPagos factura : lista) {
                String clave = claveMes(factura.fechaFactura);
                if (claveGrupoActual != null && !claveGrupoActual.equals(clave)) {
                    items.add(new AdapterFacturas.SeparadorMes(etiquetaSeparadorMes(grupoActual)));
                    items.addAll(grupoActual);
                    grupoActual = new ArrayList<>();
                }
                claveGrupoActual = clave;
                grupoActual.add(factura);
            }
            if (!grupoActual.isEmpty()) {
                items.add(new AdapterFacturas.SeparadorMes(etiquetaSeparadorMes(grupoActual)));
                items.addAll(grupoActual);
            }
            return items;
        }

        if (filtroActualFacturas == FiltroFacturas.DIRECTO) {
            agregarSeccion(items, R.string.facturas_seccion_completados, lista);
            return items;
        }

        // A_PLAZOS
        List<FacturaConPagos> activos = new ArrayList<>();
        List<FacturaConPagos> completados = new ArrayList<>();
        List<FacturaConPagos> cerrados = new ArrayList<>();
        for (FacturaConPagos factura : lista) {
            if (factura.estaCerradaManualmente()) {
                cerrados.add(factura);
            } else if (factura.estaCompletadaAutomaticamente()) {
                completados.add(factura);
            } else {
                activos.add(factura);
            }
        }
        agregarSeccion(items, R.string.facturas_seccion_activos, activos);
        agregarSeccion(items, R.string.facturas_seccion_completado, completados);
        agregarSeccion(items, R.string.facturas_seccion_cerrados, cerrados);
        return items;
    }

    private void agregarSeccion(List<Object> items, int etiquetaResId, List<FacturaConPagos> grupo) {
        if (grupo.isEmpty()) {
            return;
        }
        items.add(new AdapterFacturas.SeparadorMes(getString(etiquetaResId)));
        items.addAll(grupo);
    }

    /** Año-mes como clave de agrupación, o una constante fija si la fecha no se pudo parsear. */
    private String claveMes(String fechaFactura) {
        Date fecha = parsearFechaFactura(fechaFactura);
        if (fecha == null) {
            return "SIN_FECHA";
        }
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH);
    }

    private String etiquetaSeparadorMes(List<FacturaConPagos> grupo) {
        double total = 0;
        for (FacturaConPagos f : grupo) {
            total += !f.pagos.isEmpty() ? f.getMontoPagado() : (f.montoTotalFactura != null ? f.montoTotalFactura : 0);
        }
        Date fecha = parsearFechaFactura(grupo.get(0).fechaFactura);
        String tituloMes;
        if (fecha == null) {
            tituloMes = getString(R.string.facturas_sin_fecha);
        } else {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            cal.setTime(fecha);
            SimpleDateFormat formato = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String texto = formato.format(cal.getTime());
            tituloMes = texto.substring(0, 1).toUpperCase(Locale.getDefault()) + texto.substring(1);
        }
        return getString(R.string.facturas_separador_mes, tituloMes, UtilidadesMoneda.formatearComoMoneda(total));
    }

    /**
     * fecha_proforma llega en dos formatos posibles: dd/MM/yyyy (lo escribe la
     * app al capturar, ver ProformaFragment.fechaHoy()) o yyyy-MM-dd — el que
     * devuelve MySQL al bajar del servidor sin convertir (mismo problema ya
     * resuelto para fecha_agendamiento en UtilidadesProyectosContacto.deFechaSql,
     * que dice explícitamente: sin la conversión, la fila "desaparece" de la
     * vista que filtra por fecha — exactamente el síntoma acá). Se prueban los
     * dos formatos en orden antes de dar la fecha por no-parseable.
     */
    private static final String[] FORMATOS_FECHA_FACTURA = {"dd/MM/yyyy", "yyyy-MM-dd"};

    private Date parsearFechaFactura(String fecha) {
        if (fecha == null) {
            return null;
        }
        for (String patron : FORMATOS_FECHA_FACTURA) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat(patron, Locale.getDefault());
                formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                formato.setLenient(false);
                return formato.parse(fecha.trim());
            } catch (ParseException ignored) {
                // Prueba el siguiente formato.
            }
        }
        return null;
    }

    /** Convierte a una fecha comparable para ordenar. Si no se puede parsear, la manda al final (menos reciente). */
    private Date parsearFechaParaOrdenar(String fecha) {
        Date parseada = parsearFechaFactura(fecha);
        return parseada != null ? parseada : new Date(0);
    }

    /** true = fechaFactura cae en filtroMesSeleccionado/filtroAnioSeleccionado. */
    private boolean estaEnMesFiltrado(String fechaFactura) {
        Date parseada = parsearFechaFactura(fechaFactura);
        if (parseada == null) {
            return false;
        }
        Calendar fecha = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        fecha.setTime(parseada);
        return fecha.get(Calendar.MONTH) == filtroMesSeleccionado && fecha.get(Calendar.YEAR) == filtroAnioSeleccionado;
    }

    /**
     * Cruza insert_proyectos_contacto (encabezado: fecha/empresa/lugar) con las
     * filas de insert_proforma ya confirmadas como factura (foto_factura
     * presente) — cada una su propio evento independiente —, y les suma
     * cualquier pago histórico ya subido en insert_pago_factura (facturas
     * viejas a plazos; el flujo nuevo ya no escribe ahí).
     */
    private List<FacturaConPagos> cargarFacturasDesdeRegistro() {
        List<FacturaConPagos> facturas = new ArrayList<>();

        Map<String, String[]> infoAgendamiento = new HashMap<>();
        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProyectosContacto.CONTENT_URI, null, null, null, null)) {
            if (cursor != null) {
                int colIdRemota = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
                int colFechaAgendamiento = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO);
                int colEmpresa = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.EMPRESA);
                int colLugar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LUGAR);
                int colPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.PDV);
                int colCiudadPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV);
                int colNoRequiereVisita = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA);
                while (cursor.moveToNext()) {
                    String idRemota = cursor.getString(colIdRemota);
                    if (idRemota == null) {
                        continue;
                    }
                    infoAgendamiento.put(idRemota, new String[]{
                            cursor.getString(colFechaAgendamiento),
                            cursor.getString(colEmpresa),
                            cursor.getString(colLugar),
                            cursor.getString(colPdv),
                            cursor.getString(colCiudadPdv),
                            cursor.getString(colNoRequiereVisita)
                    });
                }
            }
        }

        Map<String, List<RegistroPagoFactura>> pagosPorFactura = new HashMap<>();
        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertPagoFactura.CONTENT_URI, null, null, null,
                ContractInsertPagoFactura.Columnas.NUMERO_CUOTA + " ASC")) {
            if (cursor != null) {
                int colId = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas._ID);
                int colIdRemota = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
                int colIdProforma = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.ID_PROFORMA);
                int colNumeroCuota = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.NUMERO_CUOTA);
                int colMontoPago = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.MONTO_PAGO);
                int colFotoPago = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.FOTO_PAGO);
                int colFechaPago = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.FECHA_PAGO);
                int colObservacion = cursor.getColumnIndexOrThrow(ContractInsertPagoFactura.Columnas.OBSERVACION);
                int colPendienteInsercion = cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION);
                while (cursor.moveToNext()) {
                    RegistroPagoFactura pago = new RegistroPagoFactura();
                    pago.idLocal = cursor.getInt(colId);
                    pago.idRemota = limpiar(cursor.getString(colIdRemota));
                    pago.idProforma = limpiar(cursor.getString(colIdProforma));
                    if (!cursor.isNull(colNumeroCuota)) {
                        pago.numeroCuota = cursor.getInt(colNumeroCuota);
                    }
                    if (!cursor.isNull(colMontoPago)) {
                        pago.montoPago = cursor.getDouble(colMontoPago);
                    }
                    pago.fotoPago = limpiar(cursor.getString(colFotoPago));
                    pago.fechaPago = limpiar(cursor.getString(colFechaPago));
                    pago.observacion = limpiar(cursor.getString(colObservacion));
                    pago.pendienteSync = cursor.getInt(colPendienteInsercion) == 1;

                    if (pago.idProforma == null) {
                        continue;
                    }
                    pagosPorFactura.computeIfAbsent(pago.idProforma, k -> new ArrayList<>()).add(pago);
                }
            }
        }

        // Trae toda fila ya confirmada como factura (foto_factura presente) — cada
        // una es su propia tarjeta independiente, incluso varias del mismo
        // agendamiento (ya no se deduplica por agendamiento: un mismo cliente
        // puede facturarse varias veces en distintos meses).
        String selection = ContractInsertProforma.Columnas.FOTO_FACTURA + " IS NOT NULL AND "
                + ContractInsertProforma.Columnas.FOTO_FACTURA + " != ''";
        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProforma.CONTENT_URI, null, selection, null, null)) {
            if (cursor != null) {
                int colId = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas._ID);
                int colIdRemota = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
                int colIdAgendamiento = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ID_AGENDAMIENTO);
                int colCodigoPdv = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.CODIGO_PDV);
                int colUsuario = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.USUARIO);
                int colFotoFactura = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FOTO_FACTURA);
                // fecha_factura casi nunca la llena la app (solo llega si baja del
                // servidor) — fecha_proforma sí se graba siempre, al momento real de
                // capturar la foto (ver ProformaFragment.guardarFotoFactura), así que
                // es la fuente confiable para mostrar/ordenar/agrupar por mes.
                int colFechaProforma = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FECHA_PROFORMA);
                int colMontoTotalFactura = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA);
                int colPlazoMeses = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.PLAZO_MESES);
                int colEstadoPago = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ESTADO_PAGO);
                int colMotivoCierrePago = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO);

                while (cursor.moveToNext()) {
                    FacturaConPagos factura = new FacturaConPagos();
                    factura.idLocal = cursor.getInt(colId);
                    factura.idRemotaFactura = limpiar(cursor.getString(colIdRemota));
                    factura.idAgendamiento = limpiar(cursor.getString(colIdAgendamiento));
                    factura.codigoPdv = cursor.getString(colCodigoPdv);
                    factura.usuario = cursor.getString(colUsuario);
                    factura.fotoFactura = limpiar(cursor.getString(colFotoFactura));
                    factura.fechaFactura = limpiar(cursor.getString(colFechaProforma));
                    if (!cursor.isNull(colMontoTotalFactura)) {
                        factura.montoTotalFactura = cursor.getDouble(colMontoTotalFactura);
                    }
                    if (!cursor.isNull(colPlazoMeses)) {
                        factura.plazoMeses = cursor.getInt(colPlazoMeses);
                    }
                    factura.estadoPago = limpiar(cursor.getString(colEstadoPago));
                    factura.motivoCierrePago = limpiar(cursor.getString(colMotivoCierrePago));

                    String[] info = factura.idAgendamiento != null ? infoAgendamiento.get(factura.idAgendamiento) : null;
                    if (info != null) {
                        factura.fechaAgenda = info[0];
                        factura.empresa = info[1];
                        factura.lugar = info[2];
                        factura.pdv = info[3];
                        factura.ciudadPdv = info[4];
                        factura.noRequiereVisita = "SI".equalsIgnoreCase(info[5]);
                    }

                    if (factura.idRemotaFactura != null) {
                        List<RegistroPagoFactura> pagos = pagosPorFactura.get(factura.idRemotaFactura);
                        if (pagos != null) {
                            factura.pagos = pagos;
                        }
                    }

                    facturas.add(factura);
                }
            }
        }

        return facturas;
    }

    private String limpiar(String valor) {
        return (valor == null || valor.trim().isEmpty() || valor.trim().equalsIgnoreCase("null")) ? null : valor;
    }

    // ----- AdapterFacturas.AccionesListener -----

    @Override
    public void onVerFotoFactura(FacturaConPagos factura) {
        mostrarFotoPantallaCompleta(factura.fotoFactura);
    }

    /** Ver un pago ya subido: mismo diálogo, pero solo-lectura (misma mecánica que "Ver" en Proforma). */
    @Override
    public void onVerFotoPago(RegistroPagoFactura pago) {
        if (!isAdded()) {
            return;
        }
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pago_factura, null);
        ImageView ivPreview = vista.findViewById(R.id.ivPreviewPago);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomPago);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarPago);
        EditText etMontoPago = vista.findViewById(R.id.etMontoPago);
        EditText etObservacionPago = vista.findViewById(R.id.etObservacionPago);
        View filaBotones = vista.findViewById(R.id.filaBotonesPago);

        mostrarFotoEnImageView(ivPreview, pago.fotoPago);
        ivZoom.setOnClickListener(v -> mostrarFotoPantallaCompleta(pago.fotoPago));

        etMontoPago.setText(pago.montoPago == null ? "" : UtilidadesMoneda.formatearParaEditar((double) pago.montoPago));
        etMontoPago.setEnabled(false);
        etMontoPago.setFocusable(false);
        etObservacionPago.setText(pago.observacion);
        etObservacionPago.setEnabled(false);
        etObservacionPago.setFocusable(false);
        filaBotones.setVisibility(View.GONE);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .create();
        ivCerrar.setVisibility(View.VISIBLE);
        ivCerrar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /** "Adjuntar otra factura" — sin límite de cantidad, solo en tarjetas a plazos. */
    @Override
    public void onAdjuntarPago(FacturaConPagos factura) {
        if (factura.idRemotaFactura == null) {
            Toast.makeText(getContext(), R.string.factura_error_sin_sincronizar, Toast.LENGTH_SHORT).show();
            return;
        }
        if (factura.estaCerrada()) {
            Toast.makeText(getContext(), R.string.factura_error_ya_cerrada, Toast.LENGTH_SHORT).show();
            return;
        }
        facturaPendientePago = factura;
        CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.factura_dialog_foto_pago_titulo)
                .setItems(opciones, (dialog, posicion) -> {
                    if (opciones[posicion].equals("Tomar Foto")) {
                        Intent intent = new Intent(requireContext(), CameraActivity.class);
                        intent.putExtra("activity", "proforma");
                        cameraPagoLauncher.launch(intent);
                    } else if (opciones[posicion].equals("Cargar Imagen")) {
                        galeriaPagoLauncher.launch("image/*");
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void procesarFotoPago(Uri uri) {
        Context contexto = requireContext();
        SharedPreferences prefs = contexto.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String user = prefs.getString(Constantes.USER, Constantes.NODATA);
        // "Local" y "Ciudad" salen del PDV elegido en "Seleccione PDV" al registrar
        // el contacto (factura.pdv/factura.ciudadPdv) — no de "lugar" (lo edita la
        // web, casi siempre vacío).
        String puntoVenta = facturaPendientePago.pdv;
        String ciudadPdv = facturaPendientePago.ciudadPdv;

        new Thread(() -> {
            try {
                Bitmap original;
                try (InputStream input = contexto.getContentResolver().openInputStream(uri)) {
                    original = BitmapFactory.decodeStream(input);
                }
                if (original == null) {
                    throw new IllegalStateException("No se pudo leer la imagen seleccionada");
                }

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                fmt.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaHora = fmt.format(cal.getTime());

                String ciudad = "Ciudad: " + (ciudadPdv != null ? ciudadPdv : "");

                ImageMark marcador = new ImageMark();
                Bitmap marcada = marcador.mark(original, ciudad, "Local: " + puntoVenta,
                        "Usuario: " + user, "Fecha y hora: " + fechaHora, Color.YELLOW, 100, 85, false);

                int alturaEscalada = (int) (marcada.getHeight() * (1024.0 / marcada.getWidth()));
                Bitmap escalada = Bitmap.createScaledBitmap(marcada, 1024, alturaEscalada, true);
                String base64 = CameraActivity.getStringImage(escalada);

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> mostrarDetallePago(escalada, base64));
                }
            } catch (Exception e) {
                Log.e(TAG, "Fallo al procesar la foto de factura: " + e.getMessage(), e);
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), R.string.proforma_error_evidencia, Toast.LENGTH_LONG).show());
                }
            }
        }).start();
    }

    /**
     * Reutiliza el mismo diálogo de "Ver" (dialog_pago_factura.xml) pero en modo
     * edición: la foto recién tomada (con zoom), el monto de esta factura y una
     * observación opcional. Sin monto sugerido — ya no hay un total fijo dividido
     * entre cuotas, cada factura nueva es su propio monto libre.
     */
    private void mostrarDetallePago(Bitmap foto, String base64) {
        if (!isAdded()) {
            return;
        }
        FacturaConPagos factura = facturaPendientePago;
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pago_factura, null);
        ImageView ivPreview = vista.findViewById(R.id.ivPreviewPago);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomPago);
        EditText etMontoPago = vista.findViewById(R.id.etMontoPago);
        EditText etObservacionPago = vista.findViewById(R.id.etObservacionPago);
        TextView btnCancelar = vista.findViewById(R.id.btnCancelarPago);
        TextView btnGuardar = vista.findViewById(R.id.btnGuardarPago);

        ivPreview.setImageBitmap(foto);
        ivZoom.setOnClickListener(v -> mostrarFotoPantallaCompleta(base64));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .setCancelable(false)
                .create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnGuardar.setOnClickListener(v -> {
            Double monto = UtilidadesMoneda.parsearMonedaSegura(etMontoPago.getText().toString());
            if (monto == null) {
                etMontoPago.setError(getString(R.string.factura_error_falta_monto_pago));
                return;
            }
            // Candado anti doble-tap: dos taps aquí duplican una cuota de pago real.
            btnGuardar.setEnabled(false);
            String observacion = etObservacionPago.getText().toString().trim();
            dialog.dismiss();
            guardarPago(factura, base64, monto, observacion.isEmpty() ? null : observacion);
        });
        dialog.show();
    }

    /** Cada factura nueva (a partir de la segunda) es su propia fila en insert_pago_factura, sin límite. */
    private void guardarPago(FacturaConPagos factura, String base64, Double monto, String observacion) {
        ContentValues nuevo = new ContentValues();
        nuevo.put(ContractInsertPagoFactura.Columnas.ID_PROFORMA, factura.idRemotaFactura);
        nuevo.put(ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO, factura.idAgendamiento);
        nuevo.put(ContractInsertPagoFactura.Columnas.CODIGO_PDV, factura.codigoPdv);
        nuevo.put(ContractInsertPagoFactura.Columnas.USUARIO, factura.usuario);
        nuevo.put(ContractInsertPagoFactura.Columnas.NUMERO_CUOTA, factura.siguienteNumeroCuota());
        nuevo.put(ContractInsertPagoFactura.Columnas.MONTO_PAGO, monto);
        nuevo.put(ContractInsertPagoFactura.Columnas.FOTO_PAGO, base64);
        nuevo.put(ContractInsertPagoFactura.Columnas.FECHA_PAGO, fechaHoy());
        if (observacion != null) {
            nuevo.put(ContractInsertPagoFactura.Columnas.OBSERVACION, observacion);
        }
        nuevo.put(Constantes.PENDIENTE_INSERCION, 1);
        nuevo.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
        requireContext().getContentResolver().insert(ContractInsertPagoFactura.CONTENT_URI, nuevo);
        UtilidadesProforma.actualizarEstadoPago(requireContext().getContentResolver(), factura.idRemotaFactura, factura.montoTotalFactura);

        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPagoFactura, null);
        }
        cargarFacturas();
        Toast.makeText(getContext(), R.string.factura_msg_pago_guardado, Toast.LENGTH_SHORT).show();
    }

    /**
     * Botón "Cierre Factura": corta el cobro de esta factura a plazos antes de llegar al total
     * cotizado. No toca estado_proforma (ese solo lo usa "Cierre Proforma", ver
     * ProformaFragment.onCerrarProforma) ni observaciones_auditoria (exclusiva de la
     * auditoría/web) — acá la señal para la web es estado_pago=cerrado.
     */
    @Override
    public void onCerrarFactura(FacturaConPagos factura) {
        if (factura.idRemotaFactura == null) {
            Toast.makeText(getContext(), R.string.factura_error_sin_sincronizar, Toast.LENGTH_SHORT).show();
            return;
        }
        UtilidadesProforma.mostrarDialogoCerrarProceso(requireContext(), observacion -> {
            ContentValues valores = new ContentValues();
            valores.put(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO, observacion);
            valores.put(ContractInsertProforma.Columnas.ESTADO_PAGO, Constantes.ESTADO_PAGO_CERRADO);
            valores.put(Constantes.PENDIENTE_INSERCION, 1);
            valores.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
            requireContext().getContentResolver().update(ContractInsertProforma.CONTENT_URI, valores,
                    Constantes.ID_REMOTA + "=?", new String[]{factura.idRemotaFactura});
            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertProforma, null);
            }
            cargarFacturas();
            Toast.makeText(getContext(), R.string.proforma_msg_cerrado, Toast.LENGTH_SHORT).show();
        });
    }

    private String fechaHoy() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        return formato.format(calendar.getTime());
    }

    private boolean esUrlDeFoto(String valor) {
        return valor != null && (valor.startsWith("http://") || valor.startsWith("https://")
                || valor.startsWith("Proforma/") || valor.startsWith("Factura/") || valor.startsWith("PagoFactura/"));
    }

    private String urlCompletaFoto(String valor) {
        return valor.startsWith("http://") || valor.startsWith("https://")
                ? valor : Constantes.PROFORMA_BLOB_BASE_URL + valor;
    }

    /** Carga cualquier foto (base64 fresco o ruta de blob ya sincronizada) en un ImageView normal. */
    private void mostrarFotoEnImageView(ImageView destino, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            destino.setImageDrawable(null);
            return;
        }
        if (esUrlDeFoto(valor)) {
            Glide.with(requireContext()).load(urlCompletaFoto(valor)).into(destino);
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

    /** Igual que en ProformaFragment: abre cualquier foto (factura o pago) a pantalla completa con una "X" para cerrar. */
    private void mostrarFotoPantallaCompleta(String valor) {
        if (!isAdded() || valor == null || valor.trim().isEmpty()) {
            return;
        }
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_proforma_zoom_foto, null);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomFoto);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarZoom);

        if (esUrlDeFoto(valor)) {
            Glide.with(requireContext()).load(urlCompletaFoto(valor)).into(ivZoom);
            Dialog dialogUrl = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialogUrl.setContentView(vista);
            ivCerrar.setOnClickListener(v -> dialogUrl.dismiss());
            dialogUrl.show();
            return;
        }
        try {
            byte[] bytes = Base64.decode(valor, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ivZoom.setImageBitmap(bitmap);

            Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(vista);
            ivCerrar.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.proforma_error_evidencia, Toast.LENGTH_SHORT).show();
        }
    }
}
