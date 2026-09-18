package com.luckyecuador.app.PintucoAPP.ui.proyectosobras.proforma;

import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.SyncThrottle;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterProforma;
import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.ProyectosObrasActivity;
import com.luckyecuador.app.PintucoAPP.Clase.BaseProforma;
import com.luckyecuador.app.PintucoAPP.Clase.FacturaEnProgreso;
import com.luckyecuador.app.PintucoAPP.Clase.RegistroProforma;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMoneda;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesProforma;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * "Gestión de Visitas": cruza insert_proyectos_contacto (agenda) con insert_proforma
 * (seguimiento) por id_agendamiento. Cada tarjeta se expande en acordeón con el
 * detalle (fecha de contacto, estado, evidencia, ver) de ese agendamiento.
 */
public class ProformaFragment extends Fragment implements AdapterProforma.AccionesListener {

    private static final String TAG = "ProformaFragment";

    private static final long TIEMPO_MAXIMO_REFRESH_MS = 12000;

    private static final String[] NOMBRES_MESES_PROFORMA = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private TextView tvProformaContador;
    private RecyclerView rvProforma;
    private SwipeRefreshLayout swipeRefreshProforma;
    private EditText etBuscarEmpresaProforma;
    private TextView pillActivosProforma;
    private TextView pillVencidasProforma;
    private TextView pillCompletadosProforma;
    private View selectorMesProforma;
    private TextView tvMesSeleccionadoProforma;
    private String filtroEmpresaProforma = "";

    private enum FiltroProforma { ACTIVOS, VENCIDAS, COMPLETADOS }
    private FiltroProforma filtroProforma = FiltroProforma.ACTIVOS;
    // Solo aplica dentro de "Completados" (ver renderizarListaFiltrada) — arranca
    // siempre en el mes actual, igual que el filtro nuevo de FacturasFragment.
    private int filtroMesCompletadosProforma;
    private int filtroAnioCompletadosProforma;

    private final Handler handlerRefresh = new Handler(Looper.getMainLooper());
    private final Runnable detenerRefreshPorTiempoAgotado = () -> {
        if (swipeRefreshProforma != null) {
            swipeRefreshProforma.setRefreshing(false);
        }
        cargarVisitas();
    };

    private BaseProforma visitaPendienteEvidencia;
    private RegistroProforma registroPendienteEvidencia;
    private BaseProforma visitaPendienteFactura;
    // Vive en el Fragment para sobrevivir a cada recarga (si no, la tarjeta se colapsaría al guardar)
    private final Set<String> idsExpandidos = new HashSet<>();
    // Confirmaciones inline efímeras (no se persisten) — ver AdapterProforma.
    private final Set<Integer> idsConfirmandoEliminar = new HashSet<>();
    private final Set<String> idsConfirmandoFactura = new HashSet<>();
    // Factura en curso en el panel "Confirmar factura" (foto+monto+plazos), en memoria hasta confirmar. Clave: idAgendamiento.
    private final Map<String, FacturaEnProgreso> facturasEnProgreso = new HashMap<>();
    // Visita "en construcción" de cada agendamiento, en memoria hasta "Guardar". Clave: idAgendamiento.
    private final Map<String, RegistroProforma> borradores = new HashMap<>();
    // Cambios sobre una fila YA guardada, sin confirmar aún; solo se llenan los campos tocados. Clave: idLocal.
    private final Map<Integer, RegistroProforma> cambiosPendientes = new HashMap<>();
    // Último resultado de cargarVisitas(), para revisar en onPause si quedó algo sin guardar
    private List<BaseProforma> ultimasVisitas = new ArrayList<>();

    /**
     * Solo para A PLAZOS: foto+monto que el promotor capturó al confirmar —
     * esa foto NO es foto_factura ahí (va el cotizado, ver guardarFotoFactura);
     * en cambio es la primera factura real de la lista, así que se guarda como
     * insert_pago_factura #1. El problema: ese insert necesita el id_remota de
     * la fila de insert_proforma recién creada, que todavía no existe (se
     * genera al sincronizar). Por eso queda "pendiente" acá, clave = _ID LOCAL
     * de esa fila, hasta que procesarPagosPrimeraFacturaPendientes() (enganchado
     * al ContentObserver de abajo) encuentre su id_remota. Pago directo NO usa
     * esto — ahí foto_factura es directamente la foto capturada, sin swap.
     */
    private final Map<Integer, PagoPrimeraFacturaPendiente> pagosPrimeraFacturaPendientes = new HashMap<>();

    private static class PagoPrimeraFacturaPendiente {
        final String base64;
        final Double monto;
        final String idAgendamiento;
        final String codigoPdv;
        final String usuario;
        // Total cotizado de la factura (monto_total_factura de la fila padre) — se
        // necesita acá para recalcular estado_pago apenas se suba esta cuota #1.
        final Double montoTotalFactura;

        PagoPrimeraFacturaPendiente(String base64, Double monto, String idAgendamiento, String codigoPdv, String usuario, Double montoTotalFactura) {
            this.base64 = base64;
            this.monto = monto;
            this.idAgendamiento = idAgendamiento;
            this.codigoPdv = codigoPdv;
            this.usuario = usuario;
            this.montoTotalFactura = montoTotalFactura;
        }
    }

    private final ContentObserver observador = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
            cargarVisitas();
            if (swipeRefreshProforma != null) {
                swipeRefreshProforma.setRefreshing(false);
            }
            if (!pagosPrimeraFacturaPendientes.isEmpty()) {
                procesarPagosPrimeraFacturaPendientes();
            }
        }
    };

    /**
     * "observador" de arriba solo vive entre onResume/onPause (mientras la pestaña
     * Proforma está visible) — pero guardarFotoFactura() salta AUTOMÁTICAMENTE a la
     * pestaña Facturas apenas se crea una factura a plazos, antes de que el sync traiga
     * el id_remota. Eso dispara onPause casi de inmediato y desregistra "observador",
     * así que el primer pago (insert_pago_factura #1) nunca se llegaba a subir: quedaba
     * pendiente en memoria para siempre. Este observer se registra en onCreate/onDestroy
     * — vive todo el tiempo que exista el Fragment (FragmentPagerAdapter no lo destruye
     * al cambiar de pestaña), así que sí procesa el pago aunque la pestaña esté oculta.
     */
    private final ContentObserver observadorPagosPendientes = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (!pagosPrimeraFacturaPendientes.isEmpty()) {
                procesarPagosPrimeraFacturaPendientes();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertProforma.CONTENT_URI, true, observadorPagosPendientes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireContext().getContentResolver().unregisterContentObserver(observadorPagosPendientes);
    }

    /**
     * SyncAdapter.sincronizarAhora() guarda el tipo de sync en un campo ESTÁTICO y solo
     * pide al SO que lo ejecute (async): dos llamadas seguidas hacen que la segunda pise
     * a la primera. Por eso se espera el broadcast de Contactos antes de pedir Proforma,
     * nunca los dos juntos. También evita que el spinner dependa solo del timeout de 12s,
     * ya que SyncAdapter no notifica el ContentObserver cuando el servidor no tiene nada nuevo.
     */
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
                handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
                if (swipeRefreshProforma != null) {
                    swipeRefreshProforma.setRefreshing(false);
                }
                cargarVisitas();
            }
        }
    };
    private String usuarioActualSync;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null && visitaPendienteEvidencia != null) {
                        procesarFotoEvidencia(uri);
                    }
                }
            });

    private final ActivityResultLauncher<String> galeriaLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && visitaPendienteEvidencia != null) {
                    procesarFotoEvidencia(uri);
                }
            });

    private final ActivityResultLauncher<Intent> cameraFacturaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null && visitaPendienteFactura != null) {
                        procesarFotoFactura(uri);
                    }
                }
            });

    private final ActivityResultLauncher<String> galeriaFacturaLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && visitaPendienteFactura != null) {
                    procesarFotoFactura(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyectos_proforma, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProformaContador = view.findViewById(R.id.tvProformaContador);
        rvProforma = view.findViewById(R.id.rvProforma);
        rvProforma.setLayoutManager(new LinearLayoutManager(requireContext()));

        etBuscarEmpresaProforma = view.findViewById(R.id.etBuscarEmpresaProforma);
        etBuscarEmpresaProforma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtroEmpresaProforma = s.toString().trim().toLowerCase(Locale.getDefault());
                renderizarListaFiltrada();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Calendar hoy = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        filtroMesCompletadosProforma = hoy.get(Calendar.MONTH);
        filtroAnioCompletadosProforma = hoy.get(Calendar.YEAR);

        selectorMesProforma = view.findViewById(R.id.selectorMesProforma);
        tvMesSeleccionadoProforma = view.findViewById(R.id.tvMesSeleccionadoProforma);
        actualizarTextoMesSeleccionadoProforma();
        selectorMesProforma.setOnClickListener(v -> mostrarSelectorMesAnioProforma());

        pillActivosProforma = view.findViewById(R.id.pillActivosProforma);
        pillVencidasProforma = view.findViewById(R.id.pillVencidasProforma);
        pillCompletadosProforma = view.findViewById(R.id.pillCompletadosProforma);
        pillActivosProforma.setOnClickListener(v -> {
            filtroProforma = FiltroProforma.ACTIVOS;
            selectorMesProforma.setVisibility(View.GONE);
            actualizarEstiloPillsProforma();
            renderizarListaFiltrada();
        });
        pillVencidasProforma.setOnClickListener(v -> {
            filtroProforma = FiltroProforma.VENCIDAS;
            selectorMesProforma.setVisibility(View.GONE);
            actualizarEstiloPillsProforma();
            renderizarListaFiltrada();
        });
        pillCompletadosProforma.setOnClickListener(v -> {
            filtroProforma = FiltroProforma.COMPLETADOS;
            selectorMesProforma.setVisibility(View.VISIBLE);
            actualizarEstiloPillsProforma();
            renderizarListaFiltrada();
        });

        swipeRefreshProforma = view.findViewById(R.id.swipeRefreshProforma);
        swipeRefreshProforma.setOnRefreshListener(this::actualizarDesdeElServidor);
        // Igual que en Agenda: el RecyclerView es el que de verdad scrollea, así que
        // el swipe-to-refresh debe preguntarle a él si ya está en el tope antes de
        // disparar el refresh; sin esto se activaría aunque la lista esté a la mitad.
        swipeRefreshProforma.setOnChildScrollUpCallback((parent, child) -> rvProforma.canScrollVertically(-1));

        cargarVisitas();
    }

    /**
     * Deslizar con el dedo: pide al servidor lo último de Contacto (hora, técnico,
     * fecha de agenda) y de Proforma (estado, evidencia, detalle de otros
     * dispositivos), sin esperar al ciclo normal de sincronización.
     */
    private void actualizarDesdeElServidor() {
        actualizarDesdeElServidor(false);
    }

    /** silencioso = true: sync de fondo al entrar al tab, sin Toast de error ni bloquear la vista. */
    private void actualizarDesdeElServidor(boolean silencioso) {
        if (!VerificarNet.hayConexion(getContext())) {
            if (!silencioso) {
                Toast.makeText(getContext(), R.string.agenda_error_sin_conexion_refresh, Toast.LENGTH_SHORT).show();
            }
            swipeRefreshProforma.setRefreshing(false);
            return;
        }

        SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        usuarioActualSync = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

        // También en el sync silencioso (entrar al tab): sin esto la lista vieja se
        // pinta primero y luego, de golpe, se reemplaza por la real cuando termina el
        // sync — se veía como que tarjetas activas "aparecían y desaparecían" solas.
        swipeRefreshProforma.setRefreshing(true);

        // Solo se dispara Contactos acá; Proforma se encadena en receptorSync (ver arriba)
        SyncAdapter.sincronizarAhora(requireContext(), false, Constantes.bajar_contactos, usuarioActualSync);

        // Respaldo por si algún paso de la cadena no notifica: el spinner no queda girando para siempre
        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
        handlerRefresh.postDelayed(detenerRefreshPorTiempoAgotado, TIEMPO_MAXIMO_REFRESH_MS);
    }

    /** El ViewPager no destruye fragments al cambiar de pestaña, así que sin esto la lista quedaría congelada. */
    @Override
    public void onResume() {
        super.onResume();
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertProyectosContacto.CONTENT_URI, true, observador);
        requireContext().getContentResolver().registerContentObserver(
                ContractInsertProforma.CONTENT_URI, true, observador);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receptorSync, new IntentFilter(Intent.ACTION_SYNC));
        cargarVisitas();
        if (SyncThrottle.puedeSincronizar()) {
            actualizarDesdeElServidor(true);
            // actualizarDesdeElServidor solo baja datos; esto reintenta subir lo que quedó
            // con PENDIENTE_INSERCION=1 por falta de conexión al guardar (común en campo)
            sincronizarSiHayConexion();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().getContentResolver().unregisterContentObserver(observador);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receptorSync);
        handlerRefresh.removeCallbacks(detenerRefreshPorTiempoAgotado);
        avisarSiHayCambiosSinGuardar();
    }

    private void cargarVisitas() {
        ultimasVisitas = cargarVisitasDesdeRegistro();
        renderizarListaFiltrada();
    }

    /**
     * Filtra ultimasVisitas por empresa/pill; avisarSiHayCambiosSinGuardar() sigue
     * mirando la lista completa. La pill "Completados" además filtra por el mes
     * seleccionado en selectorMesProforma (arranca en el mes actual) — el contador
     * de esa pill siempre refleja ese mes, no el total histórico de completados.
     */
    private void renderizarListaFiltrada() {
        List<BaseProforma> filtradas = new ArrayList<>();
        int activos = 0;
        int vencidas = 0;
        int completadosDelMes = 0;
        for (BaseProforma visita : ultimasVisitas) {
            // Cerrada a mano (sin llegar a facturar) cuenta igual que facturada:
            // ambas son "ya no hay nada más que hacer acá", van a Completados.
            boolean completado = visita.estaCompletado() || visita.proformaCerrada();
            boolean vencidaSinSubir = !completado && visita.vencidaSinSubir();
            boolean completadoEnMesFiltrado = completado && estaCompletadoEnMesFiltrado(visita);
            if (completado) {
                if (completadoEnMesFiltrado) {
                    completadosDelMes++;
                }
            } else if (vencidaSinSubir) {
                vencidas++;
            } else {
                activos++;
            }

            boolean pasaFiltro;
            switch (filtroProforma) {
                case COMPLETADOS:
                    pasaFiltro = completadoEnMesFiltrado;
                    break;
                case VENCIDAS:
                    pasaFiltro = vencidaSinSubir;
                    break;
                default:
                    pasaFiltro = !completado && !vencidaSinSubir;
                    break;
            }
            if (!pasaFiltro) {
                continue;
            }
            if (!filtroEmpresaProforma.isEmpty()) {
                String empresa = visita.getEmpresa();
                if (empresa == null || !empresa.toLowerCase(Locale.getDefault()).contains(filtroEmpresaProforma)) {
                    continue;
                }
            }
            filtradas.add(visita);
        }

        rvProforma.setAdapter(new AdapterProforma(construirItemsConSeparadoresProforma(filtradas), this, idsExpandidos,
                idsConfirmandoEliminar, idsConfirmandoFactura, facturasEnProgreso));
        tvProformaContador.setText(String.format(Locale.getDefault(),
                getString(R.string.proforma_registros_contador), filtradas.size()));
        pillActivosProforma.setText(getString(R.string.proforma_pill_activos, activos));
        pillVencidasProforma.setText(getString(R.string.proforma_pill_vencidas, vencidas));
        pillCompletadosProforma.setText(getString(R.string.proforma_pill_completados, completadosDelMes));
    }

    /**
     * Solo en la pill "Completados" (decisión validada: Activos queda como lista
     * plana, es el worklist activo del día a día, sin agrupar): separa en 2
     * secciones con separador gris — "Completados" (llegó a facturarse) y
     * "Cerrados" (se cortó a mano con "Cierre Proforma", sin llegar a facturar).
     * Mutuamente excluyentes: una vez cerrada, "Enviar factura" queda
     * deshabilitado (ver AdapterProforma.habilitadoFactura), así que nunca
     * llega a estaCompletado() después de haberse cerrado.
     */
    private List<Object> construirItemsConSeparadoresProforma(List<BaseProforma> lista) {
        List<Object> items = new ArrayList<>();
        if (filtroProforma != FiltroProforma.COMPLETADOS) {
            items.addAll(lista);
            return items;
        }
        List<BaseProforma> completadas = new ArrayList<>();
        List<BaseProforma> cerradas = new ArrayList<>();
        for (BaseProforma visita : lista) {
            if (visita.estaCompletado()) {
                completadas.add(visita);
            } else {
                cerradas.add(visita);
            }
        }
        agregarSeccionProforma(items, R.string.facturas_seccion_completados, completadas);
        agregarSeccionProforma(items, R.string.facturas_seccion_cerrados, cerradas);
        return items;
    }

    private void agregarSeccionProforma(List<Object> items, int etiquetaResId, List<BaseProforma> grupo) {
        if (grupo.isEmpty()) {
            return;
        }
        items.add(new AdapterProforma.SeparadorSeccion(getString(etiquetaResId)));
        items.addAll(grupo);
    }

    private void actualizarTextoMesSeleccionadoProforma() {
        Calendar calendario = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        calendario.set(filtroAnioCompletadosProforma, filtroMesCompletadosProforma, 1);
        SimpleDateFormat formato = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String texto = formato.format(calendario.getTime());
        tvMesSeleccionadoProforma.setText(texto.substring(0, 1).toUpperCase(Locale.getDefault()) + texto.substring(1));
    }

    /**
     * Mismo diálogo (mes/año, sin día) que FacturasFragment.mostrarSelectorMesAnio().
     * El rango va del mes del primer registro completado (no tiene sentido ofrecer
     * meses sin datos) hasta el mes calendario actual (no hay forma de tener
     * completados en el futuro).
     */
    private void mostrarSelectorMesAnioProforma() {
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_month_year_picker, null);
        NumberPicker mesPicker = vista.findViewById(R.id.monthPicker);
        NumberPicker anioPicker = vista.findViewById(R.id.yearPicker);
        Button btnAceptar = vista.findViewById(R.id.btnAceptar);
        Button btnCancelar = vista.findViewById(R.id.btnCancelar);

        Calendar hoy = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        int anioActual = hoy.get(Calendar.YEAR);
        int mesActual = hoy.get(Calendar.MONTH);

        Calendar fechaMinima = calcularFechaMinimaCompletadosProforma();
        int anioMinimo = fechaMinima != null ? fechaMinima.get(Calendar.YEAR) : anioActual;
        int mesMinimo = fechaMinima != null ? fechaMinima.get(Calendar.MONTH) : mesActual;
        if (anioMinimo > anioActual || (anioMinimo == anioActual && mesMinimo > mesActual)) {
            anioMinimo = anioActual;
            mesMinimo = mesActual;
        }

        anioPicker.setMinValue(anioMinimo);
        anioPicker.setMaxValue(anioActual);
        int anioInicial = Math.max(anioMinimo, Math.min(filtroAnioCompletadosProforma, anioActual));
        anioPicker.setValue(anioInicial);

        int anioMinimoFinal = anioMinimo;
        int mesMinimoFinal = mesMinimo;
        ajustarRangoMesPickerProforma(mesPicker, anioInicial, anioMinimoFinal, mesMinimoFinal, anioActual, mesActual,
                filtroMesCompletadosProforma + 1);
        anioPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                ajustarRangoMesPickerProforma(mesPicker, newVal, anioMinimoFinal, mesMinimoFinal, anioActual, mesActual,
                        mesPicker.getValue()));

        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(vista).create();
        btnAceptar.setOnClickListener(v -> {
            filtroMesCompletadosProforma = mesPicker.getValue() - 1;
            filtroAnioCompletadosProforma = anioPicker.getValue();
            actualizarTextoMesSeleccionadoProforma();
            renderizarListaFiltrada();
            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /** Ajusta el rango de mesPicker al año elegido: acota por abajo en el año mínimo y por arriba en el actual. */
    private void ajustarRangoMesPickerProforma(NumberPicker mesPicker, int anioSeleccionado, int anioMinimo, int mesMinimo,
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
        mesPicker.setDisplayedValues(Arrays.copyOfRange(NOMBRES_MESES_PROFORMA, minMes - 1, maxMes));
        mesPicker.setValue(Math.max(minMes, Math.min(valorPreferido, maxMes)));
    }

    /** Mes/año del registro completado más antiguo en ultimasVisitas, o null si todavía no hay ninguno. */
    private Calendar calcularFechaMinimaCompletadosProforma() {
        Calendar minima = null;
        for (BaseProforma visita : ultimasVisitas) {
            if (!visita.estaCompletado()) {
                continue;
            }
            RegistroProforma ultimo = visita.getUltimoRegistroGuardado();
            Date parseada = ultimo != null ? parsearFechaFlexible(ultimo.getFechaContacto()) : null;
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

    /**
     * fecha_contacto (columna fecha_proforma) llega en dd/MM/yyyy (capturado en
     * el equipo) o yyyy-MM-dd (lo que devuelve MySQL al bajar sin convertir) —
     * mismo problema ya resuelto para fecha_agendamiento en
     * UtilidadesProyectosContacto.deFechaSql. Se prueban los dos formatos.
     */
    private static final String[] FORMATOS_FECHA_PROFORMA = {"dd/MM/yyyy", "yyyy-MM-dd"};

    private Date parsearFechaFlexible(String fecha) {
        if (fecha == null) {
            return null;
        }
        for (String patron : FORMATOS_FECHA_PROFORMA) {
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

    /** true = la última fila guardada (la factura, si estaCompletado()) cae en el mes/año filtrado. */
    private boolean estaCompletadoEnMesFiltrado(BaseProforma visita) {
        RegistroProforma ultimo = visita.getUltimoRegistroGuardado();
        Date parseada = ultimo != null ? parsearFechaFlexible(ultimo.getFechaContacto()) : null;
        if (parseada == null) {
            return false;
        }
        Calendar fecha = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        fecha.setTime(parseada);
        return fecha.get(Calendar.MONTH) == filtroMesCompletadosProforma && fecha.get(Calendar.YEAR) == filtroAnioCompletadosProforma;
    }

    private void actualizarEstiloPillsProforma() {
        aplicarEstiloPill(pillActivosProforma, filtroProforma == FiltroProforma.ACTIVOS);
        aplicarEstiloPill(pillVencidasProforma, filtroProforma == FiltroProforma.VENCIDAS);
        aplicarEstiloPill(pillCompletadosProforma, filtroProforma == FiltroProforma.COMPLETADOS);
    }

    private void aplicarEstiloPill(TextView pill, boolean activa) {
        pill.setBackgroundResource(activa ? R.drawable.bg_chip_pill_activo : R.drawable.bg_chip_pill_inactivo);
        pill.setTextColor(getResources().getColor(activa ? R.color.proforma_titulo : R.color.plomo_pintuco));
    }

    /** Avisa si sale de la pantalla dejando un borrador o cambio de estado sin tocar "Guardar". */
    private void avisarSiHayCambiosSinGuardar() {
        List<String> fechasConCambios = new ArrayList<>();
        for (BaseProforma visita : ultimasVisitas) {
            for (RegistroProforma registro : visita.getRegistros()) {
                boolean borradorConAlgo = registro.esBorrador()
                        && (registro.getEstadoProforma() != null
                            || registro.getEvidencia() != null
                            || (registro.getCaracteristicaVisita() != null && !registro.getCaracteristicaVisita().trim().isEmpty()));
                if (borradorConAlgo || registro.tieneCambiosPendientes()) {
                    if (visita.getFechaAgenda() != null) {
                        fechasConCambios.add(visita.getFechaAgenda());
                    }
                    break;
                }
            }
        }

        if (!fechasConCambios.isEmpty() && isAdded()) {
            Toast.makeText(getContext(),
                    getString(R.string.proforma_cambios_pendientes_mensaje, TextUtils.join(", ", fechasConCambios)),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Aparece en Proforma con hora+técnico confirmados por el analista, o directo si
     * no_requiere_visita = "SI". Cruza con insert_proforma por id_agendamiento; sin fila
     * ahí, los campos quedan en sus valores por defecto (pendiente, sin evidencia).
     */
    private List<BaseProforma> cargarVisitasDesdeRegistro() {
        List<BaseProforma> visitas = new ArrayList<>();

        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProyectosContacto.CONTENT_URI, null, null, null, null)) {

            if (cursor == null) {
                return visitas;
            }

            int colIdRemota = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
            int colCodigoPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.CODIGO_PDV);
            int colUsuario = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.USUARIO);
            int colFecha = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA);
            int colEmpresa = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.EMPRESA);
            int colDireccion = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.DIRECCION);
            int colLatitud = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LATITUD);
            int colLongitud = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LONGITUD);
            int colFechaAgendamiento = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO);
            int colLugar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.LUGAR);
            int colPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.PDV);
            int colCiudadPdv = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV);
            int colHora = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.HORA);
            int colTecnico = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.TECNICO);
            int colActivar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.ACTIVAR);
            int colNoRequiereVisita = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA);
            int colEstadoAgenda = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA);

            while (cursor.moveToNext()) {
                String activar = limpiar(cursor.getString(colActivar));
                if ("NO".equalsIgnoreCase(activar)) {
                    continue;
                }

                String hora = limpiar(cursor.getString(colHora));
                String tecnico = limpiar(cursor.getString(colTecnico));
                boolean noRequiereVisita = "SI".equalsIgnoreCase(limpiar(cursor.getString(colNoRequiereVisita)));
                if (!noRequiereVisita && (hora == null || tecnico == null)) {
                    // Todavía no confirmado por el analista: no aparece en Proforma.
                    continue;
                }

                BaseProforma visita = new BaseProforma();
                visita.idAgendamiento = cursor.getString(colIdRemota);
                visita.codigoPdv = cursor.getString(colCodigoPdv);
                visita.usuario = cursor.getString(colUsuario);
                visita.fechaContactoCreacion = cursor.getString(colFecha);
                visita.empresa = cursor.getString(colEmpresa);
                visita.direccion = cursor.getString(colDireccion);
                visita.latitud = cursor.getString(colLatitud);
                visita.longitud = cursor.getString(colLongitud);
                visita.fechaAgenda = cursor.getString(colFechaAgendamiento);
                visita.lugar = limpiar(cursor.getString(colLugar));
                visita.pdv = limpiar(cursor.getString(colPdv));
                visita.ciudadPdv = limpiar(cursor.getString(colCiudadPdv));
                visita.estadoAgenda = limpiar(cursor.getString(colEstadoAgenda));
                visita.noRequiereVisita = noRequiereVisita;

                visita.registros = cargarRegistrosProforma(visita.idAgendamiento);

                visitas.add(visita);
            }
        }

        return visitas;
    }

    /**
     * Todas las visitas/contactos de este agendamiento (puede haber varias a lo
     * largo del tiempo, igual que Exhibiciones permite varias fotos por PDV),
     * ordenadas de la más antigua a la más reciente ("filas crecientes").
     */
    private List<RegistroProforma> cargarRegistrosProforma(String idAgendamiento) {
        List<RegistroProforma> registros = new ArrayList<>();
        if (idAgendamiento == null) {
            return registros;
        }

        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProforma.CONTENT_URI, null,
                ContractInsertProforma.Columnas.ID_AGENDAMIENTO + "=?",
                new String[]{idAgendamiento},
                ContractInsertProforma.Columnas._ID + " ASC")) {

            if (cursor == null) {
                return registros;
            }

            int colId = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas._ID);
            int colIdRemota = cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA);
            int colFechaProforma = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FECHA_PROFORMA);
            int colEstado = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ESTADO_PROFORMA);
            int colEvidencia = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.EVIDENCIA);
            int colCaracteristica = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA);
            int colAcompanamiento = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO);
            int colPendienteInsercion = cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION);
            int colFaseActual = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FASE_ACTUAL);
            int colFotoFactura = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FOTO_FACTURA);
            int colMontoValidado = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MONTO_VALIDADO);
            int colObservacionesAuditoria = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA);
            int colFechaAuditoria = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.FECHA_AUDITORIA);
            int colMontoTotalFactura = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA);
            int colPlazoMeses = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.PLAZO_MESES);
            int colEstadoPago = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.ESTADO_PAGO);
            int colMotivoCierre = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MOTIVO_CIERRE);
            int colMotivoCierrePago = cursor.getColumnIndexOrThrow(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO);

            while (cursor.moveToNext()) {
                RegistroProforma registro = new RegistroProforma();
                registro.idLocal = cursor.getInt(colId);
                registro.idRemota = limpiar(cursor.getString(colIdRemota));
                registro.fechaContacto = limpiar(cursor.getString(colFechaProforma));
                registro.estadoProforma = limpiar(cursor.getString(colEstado));
                registro.evidencia = limpiar(cursor.getString(colEvidencia));
                registro.caracteristicaVisita = limpiar(cursor.getString(colCaracteristica));
                registro.acompanamientoTecnico = limpiar(cursor.getString(colAcompanamiento));
                registro.pendienteSync = cursor.getInt(colPendienteInsercion) == 1;
                if (!cursor.isNull(colFaseActual)) {
                    registro.faseActual = cursor.getInt(colFaseActual);
                }
                registro.fotoFactura = limpiar(cursor.getString(colFotoFactura));
                registro.observacionesAuditoria = limpiar(cursor.getString(colObservacionesAuditoria));
                registro.fechaAuditoria = limpiar(cursor.getString(colFechaAuditoria));
                if (!cursor.isNull(colMontoValidado)) {
                    registro.montoValidado = cursor.getDouble(colMontoValidado);
                }
                if (!cursor.isNull(colMontoTotalFactura)) {
                    registro.montoTotalFactura = cursor.getDouble(colMontoTotalFactura);
                }
                if (!cursor.isNull(colPlazoMeses)) {
                    registro.plazoMeses = cursor.getInt(colPlazoMeses);
                }
                registro.estadoPago = limpiar(cursor.getString(colEstadoPago));
                registro.motivoCierre = limpiar(cursor.getString(colMotivoCierre));
                registro.motivoCierrePago = limpiar(cursor.getString(colMotivoCierrePago));
                RegistroProforma pendiente = cambiosPendientes.get(registro.idLocal);
                if (pendiente != null) {
                    registro.estadoPendiente = pendiente.estadoPendiente;
                    registro.evidenciaPendiente = pendiente.evidenciaPendiente;
                    registro.montoValidadoPendiente = pendiente.montoValidadoPendiente;
                    registro.caracteristicaPendiente = pendiente.caracteristicaPendiente;
                    registro.acompanamientoPendiente = pendiente.acompanamientoPendiente;
                    registro.fotoFacturaPendiente = pendiente.fotoFacturaPendiente;
                }
                registros.add(registro);
            }
        }

        Collections.sort(registros, Comparator.comparingLong(this::obtenerClaveOrdenRegistro));

        // Fase actual de la tarjeta = la del último registro guardado (borradores no cuentan)
        int faseActualCard = 3;
        for (RegistroProforma r : registros) {
            faseActualCard = r.faseActual;
        }

        // Si la última fila (la "activa") ya espera foto (ronda vacía o corrección), ella
        // misma hace de borrador — no agregar otra fila encima pidiendo foto también.
        RegistroProforma ultimaReal = registros.isEmpty() ? null : registros.get(registros.size() - 1);
        boolean filaActivaYaEsperaFoto = ultimaReal != null && ultimaReal.necesitaFoto();

        // El borrador de la siguiente visita se ofrece siempre que no haya una fila
        // activa esperando foto — incluida justo después de una factura: un mismo
        // agendamiento puede facturarse varias veces a lo largo de distintos meses,
        // así que el ciclo nunca se cierra para siempre (ver BaseProforma.estaCompletado()).
        if (!filaActivaYaEsperaFoto) {
            int fase = faseActualCard;
            RegistroProforma borrador = borradores.computeIfAbsent(idAgendamiento, id -> new RegistroProforma());
            borrador.faseActual = fase;
            registros.add(borrador);
        } else {
            borradores.remove(idAgendamiento);
        }

        return registros;
    }

    /** Orden real del historial: id_remota, no fecha_proforma (día sin hora, formato inconsistente entre dd/MM/yyyy y yyyy-MM-dd). */
    private long obtenerClaveOrdenRegistro(RegistroProforma registro) {
        if (registro.idRemota != null && !registro.idRemota.trim().isEmpty()) {
            try {
                return Long.parseLong(registro.idRemota.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return Long.MAX_VALUE;
    }

    private String limpiar(String valor) {
        return (valor == null || valor.trim().isEmpty() || valor.trim().equalsIgnoreCase("null")) ? null : valor;
    }

    private String fechaHoy() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        return formato.format(calendar.getTime());
    }

    // ----- AdapterProforma.AccionesListener -----
    // Si la fila ya existe en insert_proforma se ACTUALIZA; si es el "borrador" se
    // INSERTA como visita nueva — ver guardarRegistro().

    /**
     * Solo aplica al borrador: elegir estado queda en memoria hasta "Guardar". Ya guardada,
     * el estado se cambia con el lápiz (onSolicitarCambioEstado), que pide confirmación.
     */
    @Override
    public void onCambiarEstado(BaseProforma visita, RegistroProforma registro) {
        // Ronda vacía nueva también elige directo (nada guardado que proteger);
        // una corrección solicitada sí tiene estado real, pasa por el lápiz.
        boolean seleccionableDirecto = registro.esBorrador()
                || (registro.necesitaFoto() && !registro.esCorreccionSolicitada());
        if (!seleccionableDirecto) {
            return;
        }
        mostrarSelectorEstado(codigoElegido -> {
            if (registro.esBorrador()) {
                registro.estadoProforma = codigoElegido;
            } else {
                obtenerOcrearPendiente(registro.idLocal).estadoPendiente = codigoElegido;
            }
            cargarVisitas();
        });
    }

    /**
     * Lápiz junto al estado de una visita ya guardada: cambiar un dato que ya
     * se reportó no debería ser un toque accidental, así que se confirma antes
     * de mostrar el selector. El nuevo estado queda pendiente (no se guarda
     * todavía) hasta que el usuario toque el "Guardar" que aparece en la fila.
     */
    @Override
    public void onSolicitarCambioEstado(BaseProforma visita, RegistroProforma registro) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_confirmar_cambiar_estado_titulo)
                .setMessage(R.string.proforma_confirmar_cambiar_estado_mensaje)
                .setPositiveButton(R.string.proforma_confirmar_aceptar, (dialog, which) ->
                        mostrarSelectorEstado(codigoElegido -> {
                            obtenerOcrearPendiente(registro.idLocal).estadoPendiente = codigoElegido;
                            cargarVisitas();
                        }))
                .setNegativeButton(R.string.proforma_confirmar_cancelar, null)
                .show();
    }

    /** Reutiliza la misma instancia "pendiente" entre toques (estado, evidencia, detalle...). */
    private RegistroProforma obtenerOcrearPendiente(Integer idLocal) {
        return cambiosPendientes.computeIfAbsent(idLocal, id -> new RegistroProforma());
    }

    /** "Guardar" de una fila ya guardada con cambios pendientes: solo confirma lo que cambió, no exige todo completo. */
    @Override
    public void onGuardarCambiosRegistro(BaseProforma visita, RegistroProforma registro) {
        ContentValues valores = new ContentValues();
        if (registro.estadoPendiente != null) {
            valores.put(ContractInsertProforma.Columnas.ESTADO_PROFORMA, registro.estadoPendiente);
        }
        if (registro.evidenciaPendiente != null) {
            valores.put(ContractInsertProforma.Columnas.EVIDENCIA, registro.evidenciaPendiente);
            // Solo una ronda vacía nueva mueve la fecha a hoy; una corrección reenvía la misma ronda
            if (registro.necesitaFoto() && !registro.esCorreccionSolicitada()) {
                valores.put(ContractInsertProforma.Columnas.FECHA_PROFORMA, fechaHoy());
            }
        }
        if (registro.caracteristicaPendiente != null) {
            valores.put(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA, registro.caracteristicaPendiente);
        }
        if (registro.acompanamientoPendiente != null) {
            valores.put(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO, registro.acompanamientoPendiente);
        }
        if (registro.montoValidadoPendiente != null) {
            valores.put(ContractInsertProforma.Columnas.MONTO_VALIDADO, registro.montoValidadoPendiente);
        }
        boolean esFoto = registro.evidenciaPendiente != null;
        cambiosPendientes.remove(registro.idLocal);
        guardarRegistro(visita, registro, valores);
        Toast.makeText(getContext(),
                esFoto ? R.string.proforma_msg_guardado : R.string.proforma_msg_estado_actualizado,
                Toast.LENGTH_SHORT).show();
    }

    private interface SelectorEstadoListener {
        void onEstadoElegido(String codigoElegido);
    }

    private void mostrarSelectorEstado(SelectorEstadoListener callback) {
        String[] opciones = {
                getString(R.string.proforma_estado_pendiente),
                getString(R.string.proforma_estado_en_proceso),
                getString(R.string.proforma_estado_realizado)
        };
        String[] codigos = {
                Constantes.ESTADO_PROFORMA_PENDIENTE,
                Constantes.ESTADO_PROFORMA_EN_PROCESO,
                Constantes.ESTADO_PROFORMA_REALIZADO
        };

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_header_estado)
                .setItems(opciones, (dialog, posicion) -> callback.onEstadoElegido(codigos[posicion]))
                .show();
    }

    @Override
    public void onEvidencia(BaseProforma visita, RegistroProforma registro) {
        visitaPendienteEvidencia = visita;
        registroPendienteEvidencia = registro;
        CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccione una Opción")
                .setItems(opciones, (dialog, posicion) -> {
                    if (opciones[posicion].equals("Tomar Foto")) {
                        Intent intent = new Intent(requireContext(), CameraActivity.class);
                        intent.putExtra("activity", "proforma");
                        cameraLauncher.launch(intent);
                    } else if (opciones[posicion].equals("Cargar Imagen")) {
                        galeriaLauncher.launch("image/*");
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /** La fila de factura no tiene formulario asociado — se ve directo a pantalla completa. */
    @Override
    public void onVer(BaseProforma visita, RegistroProforma registro) {
        if (registro.esFilaFactura()) {
            mostrarFotoPantallaCompleta(registro.fotoFactura);
        } else {
            mostrarDialogoVer(visita, registro);
        }
    }

    /** Cubre subir foto al agendamiento equivocado. Solo si la auditoría web no tocó la fila (RegistroProforma.puedeEliminarse()). */
    @Override
    public void onEliminarRegistro(BaseProforma visita, RegistroProforma registro) {
        if (registro.idLocal == null || !registro.puedeEliminarse()) {
            return;
        }
        Integer idLocal = registro.idLocal;
        String idRemota = registro.idRemota;
        requireContext().getContentResolver().delete(
                ContractInsertProforma.CONTENT_URI,
                ContractInsertProforma.Columnas._ID + "=?",
                new String[]{String.valueOf(idLocal)});
        cambiosPendientes.remove(idLocal);
        idsConfirmandoEliminar.remove(idLocal);
        if (idRemota != null && !idRemota.trim().isEmpty()) {
            // Ya sincronizado: hay que borrarlo también en el servidor (depende de eliminar_proforma.php)
            SyncAdapter.eliminarProformaRemota(requireContext(), idRemota, usuarioActualSync);
        }
        cargarVisitas();
        Toast.makeText(getContext(), R.string.proforma_msg_envio_eliminado, Toast.LENGTH_SHORT).show();
    }

    /**
     * Botón "Cierre Proforma": negocio perdido sin llegar a facturar. Pone
     * estado_proforma=rechazado (sí sincroniza) para que la web lo detecte;
     * observaciones_auditoria NO se toca, es exclusiva de la auditoría/web.
     * motivo_cierre queda como marca local (BaseProforma.proformaCerrada()).
     */
    @Override
    public void onCerrarProforma(BaseProforma visita) {
        RegistroProforma ultimo = visita.getUltimoRegistroGuardado();
        if (ultimo == null || ultimo.idLocal == null) {
            // No debería pasar (el chip solo se muestra si hayAlgoQueCerrar en
            // AdapterProforma), pero si la tarjeta cambió de fondo justo antes del
            // toque, es mejor avisar que quedarse en silencio sin cerrar nada.
            Toast.makeText(getContext(), R.string.proforma_error_nada_que_cerrar, Toast.LENGTH_SHORT).show();
            return;
        }
        UtilidadesProforma.mostrarDialogoCerrarProceso(requireContext(), observacion -> {
            ContentValues valores = new ContentValues();
            valores.put(ContractInsertProforma.Columnas.MOTIVO_CIERRE, observacion);
            valores.put(ContractInsertProforma.Columnas.ESTADO_PROFORMA, Constantes.ESTADO_PROFORMA_RECHAZADO);
            valores.put(Constantes.PENDIENTE_INSERCION, 1);
            valores.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
            requireContext().getContentResolver().update(ContractInsertProforma.CONTENT_URI, valores,
                    ContractInsertProforma.Columnas._ID + "=?", new String[]{String.valueOf(ultimo.idLocal)});
            sincronizarSiHayConexion();
            cargarVisitas();
            Toast.makeText(getContext(), R.string.proforma_msg_cerrado, Toast.LENGTH_SHORT).show();
        });
    }

    /** "Guardar" del borrador: evidencia y detalle son obligatorios; el estado se auto-asigna (sin selector). */
    @Override
    public void onGuardarVisita(BaseProforma visita, RegistroProforma registro) {
        List<String> faltantes = new ArrayList<>();
        if (registro.getEvidencia() == null) {
            faltantes.add(getString(R.string.proforma_validacion_evidencia));
        }
        boolean tieneDetalle = registro.getCaracteristicaVisita() != null
                && !registro.getCaracteristicaVisita().trim().isEmpty();
        if (!tieneDetalle) {
            faltantes.add(getString(R.string.proforma_validacion_detalle));
        }

        if (!faltantes.isEmpty()) {
            mostrarAlertaCamposFaltantes(faltantes);
            return;
        }

        if (registro.getEstadoProforma() == null) {
            registro.estadoProforma = Constantes.ESTADO_PROFORMA_EN_PROCESO;
        }
        guardarRegistro(visita, registro, new ContentValues());
        Toast.makeText(getContext(), R.string.proforma_msg_guardado, Toast.LENGTH_SHORT).show();
    }

    private void mostrarAlertaCamposFaltantes(List<String> faltantes) {
        StringBuilder lista = new StringBuilder();
        for (String item : faltantes) {
            lista.append("• ").append(item).append("\n");
        }
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_validacion_titulo)
                .setMessage(getString(R.string.proforma_validacion_mensaje_intro, lista.toString().trim()))
                .setPositiveButton(R.string.proforma_validacion_aceptar, null)
                .show();
    }

    @Override
    public void onFotoFactura(BaseProforma visita) {
        visitaPendienteFactura = visita;
        CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Foto de factura")
                .setItems(opciones, (dialog, posicion) -> {
                    if (opciones[posicion].equals("Tomar Foto")) {
                        Intent intent = new Intent(requireContext(), CameraActivity.class);
                        intent.putExtra("activity", "proforma");
                        cameraFacturaLauncher.launch(intent);
                    } else if (opciones[posicion].equals("Cargar Imagen")) {
                        galeriaFacturaLauncher.launch("image/*");
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onZoomFotoFactura(String fotoBase64) {
        mostrarFotoPantallaCompleta(fotoBase64);
    }

    @Override
    public void onInfoFase(BaseProforma visita, int faseActual) {
        int mensaje;
        if (faseActual >= 5) {
            mensaje = R.string.proforma_info_fase5;
        } else if (faseActual == 4) {
            mensaje = R.string.proforma_info_fase4;
        } else {
            mensaje = R.string.proforma_info_fase3;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.proforma_info_fase_titulo, faseActual))
                .setMessage(mensaje)
                .setPositiveButton(R.string.proforma_dialog_aceptar, null)
                .show();
    }

    /** "Lugar" del encabezado queda truncado a una línea; acá se ve completo. */
    @Override
    public void onVerLugarCompleto(String lugar) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_dialog_lugar_titulo)
                .setMessage(lugar)
                .setPositiveButton(R.string.proforma_dialog_aceptar, null)
                .show();
    }

    /** "Empresa" del encabezado queda truncada a una línea; acá se ve completa. */
    @Override
    public void onVerEmpresaCompleta(String empresa) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_dialog_empresa_titulo)
                .setMessage(empresa)
                .setPositiveButton(R.string.proforma_dialog_aceptar, null)
                .show();
    }

    private void procesarFotoFactura(Uri uri) {
        Context contexto = requireContext();
        android.content.SharedPreferences prefs = contexto.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String user       = prefs.getString(Constantes.USER, Constantes.NODATA);
        // "Local"/"Ciudad" salen del PDV elegido al registrar el contacto (visita.pdv/ciudadPdv),
        // no de "lugar" (lo edita la web, casi siempre vacío) ni de "pharmavalue" (ruta de Pharma)
        String puntoVenta = visitaPendienteFactura.pdv;
        String ciudadPdv = visitaPendienteFactura.ciudadPdv;

        new Thread(() -> {
            try {
                Bitmap original;
                try (InputStream input = contexto.getContentResolver().openInputStream(uri)) {
                    original = BitmapFactory.decodeStream(input);
                }
                if (original == null) throw new IllegalStateException("No se pudo leer la imagen seleccionada");

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
                    requireActivity().runOnUiThread(() -> mostrarPreviewFotoFactura(escalada, base64));
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

    private void mostrarPreviewFotoFactura(Bitmap foto, String base64) {
        if (!isAdded()) return;
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_proforma_preview_foto, null);
        ImageView ivPreview = vista.findViewById(R.id.ivPreviewFoto);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomPreviewFoto);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarPreview);
        TextView btnReintentar = vista.findViewById(R.id.btnReintentarPreview);
        TextView btnUsar = vista.findViewById(R.id.btnUsarPreview);
        ivPreview.setImageBitmap(foto);
        ivZoom.setOnClickListener(v -> mostrarFotoPantallaCompleta(base64));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .setCancelable(false)
                .create();

        ivCerrar.setOnClickListener(v -> dialog.dismiss());
        btnReintentar.setOnClickListener(v -> {
            dialog.dismiss();
            onFotoFactura(visitaPendienteFactura);
        });
        btnUsar.setOnClickListener(v -> {
            dialog.dismiss();
            // Queda en memoria (panel "Confirmar factura") hasta tocar "Confirmar factura"
            facturasEnProgreso.computeIfAbsent(visitaPendienteFactura.getIdAgendamiento(), id -> new FacturaEnProgreso())
                    .fotoBase64 = base64;
            cargarVisitas();
        });
        dialog.show();
    }

    /** Máximo de 2 dígitos para el plazo "Otros" — mismo límite que el resto de la app usa para meses. */
    private static final int PLAZO_MESES_OTROS_MAX = 99;

    @Override
    public void onSeleccionarMesesFactura(BaseProforma visita) {
        Integer[] opciones = {3, 6, 9, 12, null}; // null = "Otros" (valor digitado a mano)
        CharSequence[] textos = new CharSequence[opciones.length];
        for (int i = 0; i < opciones.length; i++) {
            textos[i] = opciones[i] == null
                    ? getString(R.string.proforma_factura_meses_otros)
                    : getString(R.string.proforma_factura_meses_valor, opciones[i]);
        }
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_factura_label_meses)
                .setItems(textos, (dialog, posicion) -> {
                    if (opciones[posicion] == null) {
                        mostrarDialogoMesesPersonalizado(visita);
                        return;
                    }
                    facturasEnProgreso.computeIfAbsent(visita.getIdAgendamiento(), id -> new FacturaEnProgreso())
                            .plazoMeses = opciones[posicion];
                    cargarVisitas();
                })
                .show();
    }

    /** Sub-diálogo de "Otros": un solo campo numérico de máximo 2 dígitos (1-99 meses). */
    private void mostrarDialogoMesesPersonalizado(BaseProforma visita) {
        EditText input = new EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(2)});
        input.setHint(R.string.proforma_factura_dialog_meses_hint);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.proforma_factura_dialog_meses_titulo)
                .setView(input)
                .setPositiveButton(R.string.proforma_dialog_guardar, (dialog, which) -> {
                    String texto = input.getText().toString().trim();
                    int meses = texto.isEmpty() ? 0 : Integer.parseInt(texto);
                    if (meses <= 0 || meses > PLAZO_MESES_OTROS_MAX) {
                        Toast.makeText(getContext(), R.string.proforma_factura_error_meses_invalido, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    facturasEnProgreso.computeIfAbsent(visita.getIdAgendamiento(), id -> new FacturaEnProgreso())
                            .plazoMeses = meses;
                    cargarVisitas();
                })
                .setNegativeButton(R.string.proforma_dialog_cancelar, null)
                .show();
    }

    /** Botón final "Confirmar factura": valida todo lo armado en el panel y recién acá guarda de verdad. */
    @Override
    public void onConfirmarFactura(BaseProforma visita) {
        // Candado anti doble-envío: si ya facturó, no crea otra fila.
        if (visita.estaCompletado()) {
            idsConfirmandoFactura.remove(visita.getIdAgendamiento());
            facturasEnProgreso.remove(visita.getIdAgendamiento());
            cargarVisitas();
            return;
        }
        FacturaEnProgreso f = facturasEnProgreso.get(visita.getIdAgendamiento());
        if (f == null || f.fotoBase64 == null) {
            Toast.makeText(getContext(), R.string.proforma_factura_error_falta_foto, Toast.LENGTH_SHORT).show();
            return;
        }
        Double montoTotal = UtilidadesMoneda.parsearMonedaSegura(f.montoTotalFactura);
        if (montoTotal == null || f.montoTotalFacturaEsSugerido) {
            Toast.makeText(getContext(), R.string.proforma_factura_error_falta_monto, Toast.LENGTH_SHORT).show();
            return;
        }
        if (f.pagoAPlazos == null) {
            Toast.makeText(getContext(), R.string.proforma_factura_error_falta_tipo_pago, Toast.LENGTH_SHORT).show();
            return;
        }
        if (f.pagoAPlazos && f.plazoMeses == null) {
            Toast.makeText(getContext(), R.string.proforma_factura_error_falta_meses, Toast.LENGTH_SHORT).show();
            return;
        }
        idsConfirmandoFactura.remove(visita.getIdAgendamiento());
        facturasEnProgreso.remove(visita.getIdAgendamiento());
        guardarFotoFactura(visita, f.fotoBase64, montoTotal, f.pagoAPlazos ? f.plazoMeses : null);
    }

    /**
     * La factura es su propia fila nueva en insert_proforma (no pisa la última ronda) —
     * queda como tarjeta nueva en Facturas.
     *
     * Pago directo: foto_factura es la foto que el promotor tomó ACÁ, tal cual —
     * sube normal por insert_proforma.php (blob "Factura/..."). No tocar esto:
     * si se le pone la evidencia de otra ronda en vez de una foto recién
     * capturada, el servidor termina subiendo una cadena de texto (una ruta)
     * como si fuera imagen nueva.
     *
     * Pago a plazos: acá sí, foto_factura pasa a ser la evidencia de la última
     * ronda YA COTIZADA (ver BaseProforma.getUltimaRondaCotizada) y
     * monto_total_factura su montoValidado, copiados automáticamente. Lo que el
     * promotor capturó es el comprobante real: se sube como insert_pago_factura
     * #1 apenas esta fila consiga su id_remota (ver
     * procesarPagosPrimeraFacturaPendientes), y las siguientes se agregan desde
     * el módulo Facturas sin límite (ver AdapterFacturas).
     */
    private void guardarFotoFactura(BaseProforma visita, String base64, Double montoTotalFactura, Integer plazoMeses) {
        boolean esAPlazos = plazoMeses != null;
        RegistroProforma ultimoRegistro = esAPlazos ? visita.getUltimaRondaCotizada() : null;
        boolean hayCotizado = ultimoRegistro != null && ultimoRegistro.evidencia != null && ultimoRegistro.montoValidado != null;

        ContentValues nuevo = new ContentValues();
        nuevo.put(ContractInsertProforma.Columnas.ID_AGENDAMIENTO, visita.idAgendamiento);
        nuevo.put(ContractInsertProforma.Columnas.CODIGO_PDV, visita.codigoPdv);
        nuevo.put(ContractInsertProforma.Columnas.USUARIO, visita.usuario);
        nuevo.put(ContractInsertProforma.Columnas.FECHA_PROFORMA, fechaHoy());
        nuevo.put(ContractInsertProforma.Columnas.ESTADO_PROFORMA, Constantes.ESTADO_PROFORMA_REALIZADO);
        nuevo.put(ContractInsertProforma.Columnas.FASE_ACTUAL, 5);
        if (esAPlazos && hayCotizado) {
            nuevo.put(ContractInsertProforma.Columnas.FOTO_FACTURA, ultimoRegistro.evidencia);
            nuevo.put(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA, ultimoRegistro.montoValidado);
        } else {
            nuevo.put(ContractInsertProforma.Columnas.FOTO_FACTURA, base64);
            nuevo.put(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA, montoTotalFactura);
        }
        if (plazoMeses != null) {
            nuevo.put(ContractInsertProforma.Columnas.PLAZO_MESES, plazoMeses);
        }
        nuevo.put(Constantes.PENDIENTE_INSERCION, 1);
        nuevo.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
        Uri uriFactura = requireContext().getContentResolver().insert(ContractInsertProforma.CONTENT_URI, nuevo);
        Integer idLocalFactura = uriFactura != null ? (int) ContentUris.parseId(uriFactura) : null;

        if (esAPlazos && hayCotizado && idLocalFactura != null) {
            pagosPrimeraFacturaPendientes.put(idLocalFactura, new PagoPrimeraFacturaPendiente(
                    base64, montoTotalFactura, visita.idAgendamiento, visita.codigoPdv, visita.usuario,
                    ultimoRegistro.montoValidado));
        }

        sincronizarSiHayConexion();
        cargarVisitas();
        Toast.makeText(getContext(), R.string.proforma_msg_factura_guardada, Toast.LENGTH_SHORT).show();

        // Continuidad visual: salta a Facturas y abre esa tarjeta (una sola vez,
        // ver ProyectosObrasActivity.abrirFacturaEnFacturas) para que el promotor
        // no se pierda preguntándose a cuál cliente le acaba de facturar.
        if (idLocalFactura != null && getActivity() instanceof ProyectosObrasActivity) {
            ((ProyectosObrasActivity) getActivity()).abrirFacturaEnFacturas("L" + idLocalFactura, esAPlazos);
        }
    }

    /** Revisa cada factura a plazos recién creada: en cuanto consiga id_remota, sube su primera factura real (ver guardarFotoFactura). */
    private void procesarPagosPrimeraFacturaPendientes() {
        List<Integer> resueltos = new ArrayList<>();
        for (Map.Entry<Integer, PagoPrimeraFacturaPendiente> entry : pagosPrimeraFacturaPendientes.entrySet()) {
            String idRemotaFactura = consultarIdRemotaProforma(entry.getKey());
            if (idRemotaFactura == null) {
                continue;
            }
            guardarPrimeraFacturaComoPago(idRemotaFactura, entry.getValue());
            resueltos.add(entry.getKey());
        }
        for (Integer idLocal : resueltos) {
            pagosPrimeraFacturaPendientes.remove(idLocal);
        }
    }

    private String consultarIdRemotaProforma(int idLocal) {
        try (Cursor c = requireContext().getContentResolver().query(
                ContractInsertProforma.CONTENT_URI,
                new String[]{Constantes.ID_REMOTA},
                ContractInsertProforma.Columnas._ID + "=?",
                new String[]{String.valueOf(idLocal)}, null)) {
            if (c != null && c.moveToFirst()) {
                String idRemota = c.getString(0);
                return (idRemota == null || idRemota.trim().isEmpty()) ? null : idRemota;
            }
        }
        return null;
    }

    private void guardarPrimeraFacturaComoPago(String idRemotaFactura, PagoPrimeraFacturaPendiente p) {
        ContentValues nuevo = new ContentValues();
        nuevo.put(ContractInsertPagoFactura.Columnas.ID_PROFORMA, idRemotaFactura);
        nuevo.put(ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO, p.idAgendamiento);
        nuevo.put(ContractInsertPagoFactura.Columnas.CODIGO_PDV, p.codigoPdv);
        nuevo.put(ContractInsertPagoFactura.Columnas.USUARIO, p.usuario);
        nuevo.put(ContractInsertPagoFactura.Columnas.NUMERO_CUOTA, 1);
        nuevo.put(ContractInsertPagoFactura.Columnas.MONTO_PAGO, p.monto);
        nuevo.put(ContractInsertPagoFactura.Columnas.FOTO_PAGO, p.base64);
        nuevo.put(ContractInsertPagoFactura.Columnas.FECHA_PAGO, fechaHoy());
        nuevo.put(Constantes.PENDIENTE_INSERCION, 1);
        nuevo.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
        requireContext().getContentResolver().insert(ContractInsertPagoFactura.CONTENT_URI, nuevo);
        UtilidadesProforma.actualizarEstadoPago(requireContext().getContentResolver(), idRemotaFactura, p.montoTotalFactura);
        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(requireContext(), true, Constantes.insertPagoFactura, null);
        }
    }

    /**
     * Decodifica la foto, le pone marca de agua (misma mecánica de
     * EvidenciaMark/CameraActivity usada en Fotográfico PDV) y la guarda como
     * base64 en la fila de insert_proforma. Corre en un hilo aparte porque
     * decodificar+marcar una foto grande no debe bloquear el hilo principal.
     */
    private void procesarFotoEvidencia(Uri uri) {
        Context contexto = requireContext();
        android.content.SharedPreferences prefs = contexto.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String user       = prefs.getString(Constantes.USER, Constantes.NODATA);
        // "Local"/"Ciudad" salen del PDV elegido al registrar el contacto (visita.pdv/ciudadPdv),
        // no de "lugar" (lo edita la web, casi siempre vacío) ni de "pharmavalue" (ruta de Pharma)
        String puntoVenta = visitaPendienteEvidencia.pdv;
        String ciudadPdv  = visitaPendienteEvidencia.ciudadPdv;

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
                SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                formatoFechaHora.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaHora = formatoFechaHora.format(cal.getTime());

                String ciudad  = "Ciudad: "     + (ciudadPdv != null ? ciudadPdv : "");
                String local   = "Local: "      + puntoVenta;
                String usuario = "Usuario: "    + user;
                String fechaHoraEtiqueta = "Fecha y hora: " + fechaHora;

                ImageMark marcador = new ImageMark();
                Bitmap marcada = marcador.mark(original, ciudad, local, usuario, fechaHoraEtiqueta,
                        Color.YELLOW, 100, 85, false);

                int alturaEscalada = (int) (marcada.getHeight() * (1024.0 / marcada.getWidth()));
                Bitmap escalada = Bitmap.createScaledBitmap(marcada, 1024, alturaEscalada, true);
                String base64 = CameraActivity.getStringImage(escalada);

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> mostrarPreviewFoto(escalada, base64));
                }
            } catch (Exception e) {
                Log.e(TAG, "Fallo al procesar la foto de evidencia: " + e.getMessage(), e);
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), R.string.proforma_error_evidencia, Toast.LENGTH_LONG).show());
                }
            }
        }).start();
    }

    /** Muestra la foto ya marcada con agua para confirmar antes de guardar (evita subir la equivocada de la galería). */
    private void mostrarPreviewFoto(Bitmap foto, String base64) {
        if (!isAdded()) {
            return;
        }
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_proforma_preview_foto, null);
        ImageView ivPreview = vista.findViewById(R.id.ivPreviewFoto);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomPreviewFoto);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarPreview);
        TextView btnReintentar = vista.findViewById(R.id.btnReintentarPreview);
        TextView btnUsar = vista.findViewById(R.id.btnUsarPreview);

        ivPreview.setImageBitmap(foto);
        ivZoom.setOnClickListener(v -> mostrarFotoPantallaCompleta(base64));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .setCancelable(false)
                .create();

        ivCerrar.setOnClickListener(v -> dialog.dismiss());
        btnReintentar.setOnClickListener(v -> {
            dialog.dismiss();
            onEvidencia(visitaPendienteEvidencia, registroPendienteEvidencia);
        });
        btnUsar.setOnClickListener(v -> {
            dialog.dismiss();
            guardarEvidencia(visitaPendienteEvidencia, registroPendienteEvidencia, base64);
        });

        dialog.show();
    }

    /**
     * No se guarda progresivamente: en el borrador el campo se pisa directo; en una fila ya
     * guardada que necesita foto hay que pasar por "cambios pendientes" o se pierde en el
     * próximo cargarVisitas(). Al quedar la foto lista se abre el panel de detalle, que
     * habilita el "Guardar" de la tarjeta (RegistroProforma.tieneLasTresSeccionesCompletas()).
     */
    private void guardarEvidencia(BaseProforma visita, RegistroProforma registro, String base64) {
        boolean esBorrador = registro.esBorrador();
        Integer idLocal = registro.idLocal;
        if (esBorrador) {
            registro.evidencia = base64;
        } else {
            RegistroProforma pendiente = obtenerOcrearPendiente(registro.idLocal);
            pendiente.evidenciaPendiente = base64;
            if (registro.esCorreccionSolicitada()) {
                // Al reenviar la foto el estado vuelve solo a en_proceso, sin que el promotor lo elija
                pendiente.estadoPendiente = Constantes.ESTADO_PROFORMA_EN_PROCESO;
            }
        }
        cargarVisitas();

        // cargarVisitas() reconstruye todo desde el cursor: hay que rebuscar visita/registro
        BaseProforma visitaActualizada = buscarVisitaPorAgendamiento(visita.idAgendamiento);
        RegistroProforma registroActualizado = visitaActualizada == null ? null
                : buscarRegistroEnVisita(visitaActualizada, esBorrador, idLocal);
        if (registroActualizado != null) {
            mostrarDialogoVer(visitaActualizada, registroActualizado);
        } else {
            Toast.makeText(getContext(), R.string.proforma_msg_visita_lista_falta_guardar, Toast.LENGTH_LONG).show();
        }
    }

    private BaseProforma buscarVisitaPorAgendamiento(String idAgendamiento) {
        if (idAgendamiento == null) {
            return null;
        }
        for (BaseProforma v : ultimasVisitas) {
            if (idAgendamiento.equals(v.idAgendamiento)) {
                return v;
            }
        }
        return null;
    }

    private RegistroProforma buscarRegistroEnVisita(BaseProforma visita, boolean esBorrador, Integer idLocal) {
        for (RegistroProforma r : visita.getRegistros()) {
            if (esBorrador ? r.esBorrador() : idLocal != null && idLocal.equals(r.idLocal)) {
                return r;
            }
        }
        return null;
    }

    /**
     * true = el valor ya es la ruta de blob que dejó insert_proforma.php al subirlo
     * (no un base64 local sin sincronizar aún) — hay que cargarlo como imagen de red
     * en vez de decodificarlo como base64, o "Ver" no muestra nada.
     */
    private boolean esUrlDeFoto(String valor) {
        return valor != null && (valor.startsWith("http://") || valor.startsWith("https://")
                || valor.startsWith("Proforma/") || valor.startsWith("Factura/"));
    }

    private String urlCompletaFoto(String valor) {
        return valor.startsWith("http://") || valor.startsWith("https://")
                ? valor : Constantes.PROFORMA_BLOB_BASE_URL + valor;
    }

    /** Abre cualquier foto (evidencia, preview recién tomado o factura) a pantalla completa con una "X" para cerrar. */
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

    /**
     * En el borrador es un formulario (evidencia + característica + acompañamiento); "Aceptar"
     * solo deja el detalle en memoria, el compromiso real es el "Guardar" de la tarjeta. En una
     * fila ya guardada es solo lectura, con una "X" para cerrar en vez de Cancelar/Aceptar.
     */
    private void mostrarDialogoVer(BaseProforma visita, RegistroProforma registro) {
        View vista = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_proforma_ver, null);

        ImageView ivPreview = vista.findViewById(R.id.ivPreviewEvidencia);
        ImageView ivZoom = vista.findViewById(R.id.ivZoomEvidencia);
        TextView tvSinEvidencia = vista.findViewById(R.id.tvSinEvidencia);
        EditText etMontoPropuesto = vista.findViewById(R.id.etMontoPropuesto);
        EditText etCaracteristica = vista.findViewById(R.id.etCaracteristicaVisita);
        RadioGroup rgAcompanamiento = vista.findViewById(R.id.rgAcompanamientoTecnico);
        RadioButton rbSi = vista.findViewById(R.id.rbAcompanamientoSi);
        RadioButton rbNo = vista.findViewById(R.id.rbAcompanamientoNo);
        View filaBotones = vista.findViewById(R.id.filaBotonesVer);
        TextView btnCancelar = vista.findViewById(R.id.btnCancelarVer);
        TextView btnGuardar = vista.findViewById(R.id.btnGuardarVer);
        ImageView ivCerrar = vista.findViewById(R.id.ivCerrarVer);

        // La pendiente (foto recién tomada) tiene prioridad sobre la persistida — si no, el
        // caso que abre este diálogo justo después de tomar la foto mostraba "sin evidencia"
        String evidenciaAMostrar = registro.getEvidenciaMostrada();
        if (evidenciaAMostrar != null) {
            tvSinEvidencia.setVisibility(View.GONE);
            ivZoom.setVisibility(View.VISIBLE);
            ivZoom.setOnClickListener(v -> mostrarFotoPantallaCompleta(evidenciaAMostrar));
            if (esUrlDeFoto(evidenciaAMostrar)) {
                // Ya subida y bajada del servidor: viene como ruta de blob, se carga como imagen de red
                Glide.with(requireContext()).load(urlCompletaFoto(evidenciaAMostrar)).into(ivPreview);
            } else {
                try {
                    byte[] bytes = Base64.decode(evidenciaAMostrar, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivPreview.setImageBitmap(bitmap);
                } catch (Exception e) {
                    ivPreview.setVisibility(View.GONE);
                    tvSinEvidencia.setVisibility(View.VISIBLE);
                    ivZoom.setVisibility(View.GONE);
                }
            }
        } else {
            ivPreview.setVisibility(View.GONE);
            tvSinEvidencia.setVisibility(View.VISIBLE);
            ivZoom.setVisibility(View.GONE);
        }

        // Editable directo: borrador. Editable vía pendiente: ronda vacía nueva (no una
        // corrección, que solo pide evidencia+estado, no característica/acompañamiento).
        boolean editableDirecto = registro.esBorrador();
        boolean editablePendiente = !editableDirecto && registro.necesitaFoto() && !registro.esCorreccionSolicitada();
        boolean soloLectura = !editableDirecto && !editablePendiente;

        // Snapshot de lo que había ANTES de abrir el diálogo — aplicarFormatoMoneda
        // escribe en vivo (cada tecla, y de nuevo al perder el foco por el reformateo
        // a 2 decimales), así que sin este respaldo un simple toque al campo seguido
        // de "Cancelar" dejaba un cambio pendiente fantasma (restaurado en el
        // OnCancelListener de más abajo).
        RegistroProforma pendienteExistente = editablePendiente ? cambiosPendientes.get(registro.idLocal) : null;
        Double montoValidadoOriginal = editableDirecto ? registro.montoValidado : null;
        String montoValidadoPendienteOriginal = pendienteExistente != null ? pendienteExistente.montoValidadoPendiente : null;

        etMontoPropuesto.setText(UtilidadesMoneda.formatearParaEditar(registro.getMontoValidadoMostrado()));
        UtilidadesMoneda.aplicarFormatoMoneda(etMontoPropuesto, valor -> {
            if (editableDirecto) {
                registro.montoValidado = valor;
            } else {
                obtenerOcrearPendiente(registro.idLocal).montoValidadoPendiente = valor == null ? null : String.valueOf(valor);
            }
        });

        etCaracteristica.setText(registro.getCaracteristicaMostrado());
        boolean conAcompanamiento = "SI".equalsIgnoreCase(registro.getAcompanamientoMostrado());
        rbSi.setChecked(conAcompanamiento);
        rbNo.setChecked(!conAcompanamiento);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(vista)
                .create();
        // "Cancelar", la "X" (si aplica), el botón atrás y tocar fuera del diálogo
        // deben dejar todo exactamente como estaba — nunca a medio camino.
        dialog.setOnCancelListener(d -> {
            if (editableDirecto) {
                registro.montoValidado = montoValidadoOriginal;
            } else if (editablePendiente) {
                RegistroProforma pendiente = cambiosPendientes.get(registro.idLocal);
                if (pendiente != null) {
                    pendiente.montoValidadoPendiente = montoValidadoPendienteOriginal;
                }
            }
        });
        if (soloLectura) {
            // Ya guardada: nada editable acá (el estado se cambia con el lápiz); solo "X" para cerrar
            etMontoPropuesto.setEnabled(false);
            etMontoPropuesto.setFocusable(false);
            etCaracteristica.setEnabled(false);
            etCaracteristica.setFocusable(false);
            rbSi.setEnabled(false);
            rbNo.setEnabled(false);
            filaBotones.setVisibility(View.GONE);
            ivCerrar.setVisibility(View.VISIBLE);
            ivCerrar.setOnClickListener(v -> dialog.dismiss());
        } else {
            // Nunca guarda en la base directo: solo deja el detalle en memoria, por eso dice "Aceptar"
            btnGuardar.setText(R.string.proforma_dialog_aceptar);
            // cancel() (no dismiss()) para que dispare el mismo OnCancelListener que
            // el botón atrás / tocar fuera y así restaure el monto en todos los casos.
            btnCancelar.setOnClickListener(v -> dialog.cancel());
            btnGuardar.setOnClickListener(v -> {
                String caracteristica = etCaracteristica.getText().toString().trim();
                Double montoValidado = UtilidadesMoneda.parsearMonedaSegura(etMontoPropuesto.getText().toString());
                boolean faltaCaracteristica = caracteristica.isEmpty();
                boolean faltaMonto = montoValidado == null;
                if (faltaCaracteristica) {
                    mostrarErrorTemporal(etCaracteristica, getString(R.string.proforma_error_falta_caracteristica));
                }
                if (faltaMonto) {
                    mostrarErrorTemporal(etMontoPropuesto, getString(R.string.proforma_error_falta_monto));
                }
                if (faltaCaracteristica || faltaMonto) {
                    return;
                }
                String acompanamiento = rbSi.isChecked() ? "SI" : "NO";
                if (editableDirecto) {
                    registro.montoValidado = montoValidado;
                    registro.caracteristicaVisita = caracteristica;
                    registro.acompanamientoTecnico = acompanamiento;
                } else {
                    RegistroProforma pendiente = obtenerOcrearPendiente(registro.idLocal);
                    pendiente.montoValidadoPendiente = String.valueOf(montoValidado);
                    pendiente.caracteristicaPendiente = caracteristica;
                    pendiente.acompanamientoPendiente = acompanamiento;
                }
                dialog.dismiss();
                cargarVisitas();
                Toast.makeText(getContext(), R.string.proforma_msg_detalle_listo_falta_guardar, Toast.LENGTH_LONG).show();
            });
        }

        dialog.show();
    }

    /**
     * Borrador (idLocal null) → INSERT como visita nueva. Fila existente → UPDATE.
     * El UPDATE filtra por _ID sobre la URI general, no por una URI de "fila única": el
     * Provider interpreta ese segmento como idRemota, que una fila recién creada no tiene
     * todavía — con eso el update no afectaba ninguna fila y nada se guardaba.
     */
    private void guardarRegistro(BaseProforma visita, RegistroProforma registro, ContentValues camposExtra) {
        if (registro.esBorrador()) {
            ContentValues nuevo = new ContentValues();
            nuevo.put(ContractInsertProforma.Columnas.ID_AGENDAMIENTO, visita.idAgendamiento);
            nuevo.put(ContractInsertProforma.Columnas.CODIGO_PDV, visita.codigoPdv);
            nuevo.put(ContractInsertProforma.Columnas.USUARIO, visita.usuario);
            nuevo.put(ContractInsertProforma.Columnas.FECHA_PROFORMA, fechaHoy());
            // Valores del borrador en memoria por defecto; camposExtra (detalle del diálogo "Ver") se aplica encima
            nuevo.put(ContractInsertProforma.Columnas.ESTADO_PROFORMA,
                    registro.getEstadoProforma() != null ? registro.getEstadoProforma() : Constantes.ESTADO_PROFORMA_PENDIENTE);
            // La foto ya fue enviada al crear → entra directo a fase 4
            nuevo.put(ContractInsertProforma.Columnas.FASE_ACTUAL, 4);
            if (registro.getEvidencia() != null) {
                nuevo.put(ContractInsertProforma.Columnas.EVIDENCIA, registro.getEvidencia());
            }
            if (registro.getCaracteristicaVisita() != null) {
                nuevo.put(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA, registro.getCaracteristicaVisita());
            }
            if (registro.getMontoValidadoMostrado() != null) {
                nuevo.put(ContractInsertProforma.Columnas.MONTO_VALIDADO, registro.getMontoValidadoMostrado());
            }
            if (registro.getAcompanamientoTecnico() != null) {
                nuevo.put(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO, registro.getAcompanamientoTecnico());
            }
            nuevo.putAll(camposExtra);
            nuevo.put(Constantes.PENDIENTE_INSERCION, 1);
            nuevo.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
            requireContext().getContentResolver().insert(ContractInsertProforma.CONTENT_URI, nuevo);
            // Ya se comprometió: la próxima recarga debe crear un borrador nuevo, no reutilizar este
            borradores.remove(visita.idAgendamiento);
        } else {
            ContentValues actualizar = new ContentValues(camposExtra);
            actualizar.put(Constantes.PENDIENTE_INSERCION, 1);
            actualizar.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);
            requireContext().getContentResolver().update(
                    ContractInsertProforma.CONTENT_URI,
                    actualizar,
                    ContractInsertProforma.Columnas._ID + "=?",
                    new String[]{String.valueOf(registro.idLocal)});

            // Limpiar memoria tras persistir
            cambiosPendientes.remove(registro.idLocal);
        }

        sincronizarSiHayConexion();
        cargarVisitas();
    }

    private static final long DURACION_ERROR_MS = 2500;

    /** Borde rojo + globo de error que se autolimpia — mismo patrón que ContactoFragment. */
    private void mostrarErrorTemporal(EditText editText, String mensaje) {
        editText.setError(mensaje);
        editText.postDelayed(() -> {
            if (mensaje.equals(String.valueOf(editText.getError()))) {
                editText.setError(null);
            }
        }, DURACION_ERROR_MS);
    }

    private void sincronizarSiHayConexion() {
        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertProforma, null);
        }
    }

}
