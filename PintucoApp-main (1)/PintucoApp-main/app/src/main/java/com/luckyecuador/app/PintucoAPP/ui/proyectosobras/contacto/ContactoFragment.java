package com.luckyecuador.app.PintucoAPP.ui.proyectosobras.contacto;

import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.SyncThrottle;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ContactoFragment extends Fragment {

    private static final String[] DOMINIOS_MAIL = {
            "gmail.com", "hotmail.com", "outlook.com", "yahoo.com"
    };

    private static final String SUBCHANNEL_KYWI = "COMERCIAL KYWI S.A.";

    // Desactivado a pedido del negocio: ver bloques comentados más abajo (GPS y botón Buscar)
    // private static final int PERMISO_UBICACION_REQUEST = 4501;

    private EditText etContacto;
    private EditText etEmpresa;
    private AutoCompleteTextView etMail;
    private ArrayAdapter<String> adapterSugerenciasMail;

    private EditText etDireccion;
    // private FusedLocationProviderClient fusedLocationClient;
    private boolean direccionSeleccionadaProgramaticamente = false;
    private String latitudDireccion;
    private String longitudDireccion;
    private EditText ettelefono;
    private EditText etTelefonoConvencional;
    private EditText etAgendamiento;
    private EditText etHoraTentativa;
    private CheckBox cbNoRequiereVisita;
    private View llAgendamientoCampos;
    private EditText etPdvContacto;
    private View layoutPdvContacto;
    private TextView btnGuardarContacto;

    private String fecha;
    private String usuario;
    private String pdv;
    private String codigoPdv;
    private String ciudadPdv;

    private final List<String> nombresPdvs = new ArrayList<>();
    private final List<String> codigosPdvs = new ArrayList<>();
    private final List<String> ciudadesPdvs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyectos_contacto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarCamposAutomaticos(view);

        etContacto = view.findViewById(R.id.etContacto);
        etEmpresa = view.findViewById(R.id.etEmpresa);
        etMail = view.findViewById(R.id.etMail);
        etDireccion = view.findViewById(R.id.etDireccion);
        ettelefono = view.findViewById(R.id.ettelefono);
        etTelefonoConvencional = view.findViewById(R.id.etTelefonoConvencional);
        etAgendamiento = view.findViewById(R.id.etAgendamiento);
        etHoraTentativa = view.findViewById(R.id.etHoraTentativa);
        cbNoRequiereVisita = view.findViewById(R.id.cbNoRequiereVisita);
        llAgendamientoCampos = view.findViewById(R.id.llAgendamientoCampos);
        etPdvContacto = view.findViewById(R.id.etPdvContacto);
        layoutPdvContacto = view.findViewById(R.id.layoutPdvContacto);

        btnGuardarContacto = view.findViewById(R.id.btnGuardarContacto);

        // fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        configurarAutocompletadoMail();
        view.findViewById(R.id.ivBuscarDireccion).setOnClickListener(v -> buscarDireccionManual());
        // Botón de GPS desactivado a pedido del negocio (ver método comentado arriba)
        // view.findViewById(R.id.ivGpsDireccion).setOnClickListener(v -> obtenerUbicacionActual());
        configurarValidacionesTiempoReal();

        layoutPdvContacto.setOnClickListener(v -> mostrarSelectorPdv());
        etPdvContacto.setOnClickListener(v -> mostrarSelectorPdv());
        cargarPdvsDisponibles();

        etAgendamiento.setOnClickListener(v -> mostrarSelectorFecha());
        view.findViewById(R.id.ivAgendamientoFecha).setOnClickListener(v -> mostrarSelectorFecha());
        etHoraTentativa.setOnClickListener(v -> mostrarSelectorHoraTentativa());
        view.findViewById(R.id.ivHoraTentativa).setOnClickListener(v -> mostrarSelectorHoraTentativa());
        cbNoRequiereVisita.setOnClickListener(v -> alternarCamposAgendamiento());
        // Candado anti doble-tap: sin esto, dos taps duplican el contacto.
        btnGuardarContacto.setOnClickListener(v -> {
            btnGuardarContacto.setEnabled(false);
            guardar();
        });
    }

    /**
     * Al volver a esta pestaña (el ViewPager no destruye fragments al cambiar de tab):
     * refresca la lista de PDVs y dispara el mismo sync de fondo (throttled) que ya
     * usan Agenda/Proforma/Facturas, para no depender solo del swipe-to-refresh.
     */
    @Override
    public void onResume() {
        super.onResume();
        cargarPdvsDisponibles();
        if (SyncThrottle.puedeSincronizar() && VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), false, Constantes.bajar_contactos, usuario);
        }
    }

    private void cargarCamposAutomaticos(View view) {
        SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        usuario = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = formatoFecha.format(calendar.getTime());

        ((EditText) view.findViewById(R.id.etFechaContacto)).setText(fecha);
        ((EditText) view.findViewById(R.id.etUsuarioContacto)).setText(usuario);
    }

    private void cargarPdvsDisponibles() {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("usuario", usuario);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.GET_PDVS_CONTACTO,
                    this::procesarRespuestaPdvs,
                    error -> error.printStackTrace()) {

                @Override
                public byte[] getBody() {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void procesarRespuestaPdvs(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String estado = jsonResponse.optString("estado", "0");

            if (!"1".equals(estado)) {
                return;
            }

            JSONArray pdvs = jsonResponse.getJSONArray("pdvs");
            nombresPdvs.clear();
            codigosPdvs.clear();
            ciudadesPdvs.clear();

            for (int i = 0; i < pdvs.length(); i++) {
                JSONObject pdvJson = pdvs.getJSONObject(i);
                nombresPdvs.add(pdvJson.getString("pos_name"));
                codigosPdvs.add(pdvJson.getString("pos_id"));
                // city: para la marca de agua en fotos sin depender de "pharmavalue" (ruta de Pharma, no esta)
                ciudadesPdvs.add(pdvJson.optString("city", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarSelectorPdv() {
        if (nombresPdvs.isEmpty()) {
            Toast.makeText(getContext(), R.string.contacto_error_pdvs_no_disponibles, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] opciones = nombresPdvs.toArray(new String[0]);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.contacto_hint_pdv)
                .setItems(opciones, (dialog, posicion) -> {
                    pdv = nombresPdvs.get(posicion);
                    codigoPdv = codigosPdvs.get(posicion);
                    ciudadPdv = posicion < ciudadesPdvs.size() ? ciudadesPdvs.get(posicion) : null;
                    etPdvContacto.setText(pdv);
                    layoutPdvContacto.setBackgroundResource(R.drawable.bg_input_proyectos);
                })
                .show();
    }

    /*
     * GPS desactivado a pedido del negocio; queda comentado para reactivarlo luego.
     * El botón "Buscar" (buscarDireccionManual, abajo) sigue activo.
     *
     * private void obtenerUbicacionActual() {
     *     boolean tienePermiso = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
     *             || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
     *
     *     if (!tienePermiso) {
     *         requestPermissions(
     *                 new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
     *                 PERMISO_UBICACION_REQUEST);
     *         return;
     *     }
     *
     *     Toast.makeText(getContext(), R.string.contacto_obteniendo_ubicacion, Toast.LENGTH_SHORT).show();
     *
     *     fusedLocationClient.getLastLocation()
     *             .addOnSuccessListener(location -> {
     *                 if (location == null) {
     *                     Toast.makeText(getContext(), R.string.contacto_error_ubicacion, Toast.LENGTH_SHORT).show();
     *                     return;
     *                 }
     *                 latitudDireccion = String.valueOf(location.getLatitude());
     *                 longitudDireccion = String.valueOf(location.getLongitude());
     *                 completarDireccionDesdeCoordenadas(location.getLatitude(), location.getLongitude());
     *             })
     *             .addOnFailureListener(error -> {
     *                 Log.e("Direccion", "Fallo al obtener ubicación: " + error.getMessage(), error);
     *                 Toast.makeText(getContext(), R.string.contacto_error_ubicacion, Toast.LENGTH_SHORT).show();
     *             });
     * }
     *
     * private void completarDireccionDesdeCoordenadas(double latitud, double longitud) {
     *     String direccionTexto;
     *     try {
     *         Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
     *         List<Address> resultados = geocoder.getFromLocation(latitud, longitud, 1);
     *         direccionTexto = (resultados != null && !resultados.isEmpty())
     *                 ? resultados.get(0).getAddressLine(0)
     *                 : (latitud + ", " + longitud);
     *     } catch (Exception e) {
     *         Log.e("Direccion", "Fallo el geocoder inverso: " + e.getMessage(), e);
     *         direccionTexto = latitud + ", " + longitud;
     *     }
     *
     *     direccionSeleccionadaProgramaticamente = true;
     *     InputFilter[] filtrosOriginales = etDireccion.getFilters();
     *     etDireccion.setFilters(new InputFilter[0]);
     *     etDireccion.setText(direccionTexto);
     *     etDireccion.setFilters(filtrosOriginales);
     *     etDireccion.setSelection(etDireccion.getText().length());
     *     direccionSeleccionadaProgramaticamente = false;
     * }
     */

    /** El usuario confirma que quiere geocodificar la dirección escrita, para validarla antes de guardar. */
    private void buscarDireccionManual() {
        String direccionTexto = etDireccion.getText().toString().trim();
        if (direccionTexto.isEmpty()) {
            mostrarErrorTemporal(etDireccion, getString(R.string.contacto_error_falta_direccion));
            return;
        }
        if (!VerificarNet.hayConexion(getContext())) {
            Toast.makeText(getContext(), R.string.contacto_error_sin_conexion_busqueda, Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progreso = new ProgressDialog(requireContext());
        progreso.setMessage(getString(R.string.contacto_buscando_direccion));
        progreso.setCancelable(false);
        progreso.show();

        buscarCoordenadasEnSegundoPlano(direccionTexto, (encontrada, direccionSugerida) -> {
            progreso.dismiss();
            if (!encontrada) {
                Toast.makeText(getContext(), R.string.contacto_direccion_no_encontrada, Toast.LENGTH_LONG).show();
                return;
            }
            if (direccionSugerida != null && !direccionSugerida.trim().equalsIgnoreCase(direccionTexto)) {
                mostrarDialogoQuisisteDecir(direccionSugerida);
            } else {
                Toast.makeText(getContext(), R.string.contacto_direccion_encontrada, Toast.LENGTH_LONG).show();
            }
        });
    }

    /** El Geocoder interpretó la dirección distinto a lo escrito (típico error de tipeo); se ofrece la corrección sin obligar a aceptarla. */
    private void mostrarDialogoQuisisteDecir(String direccionSugerida) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.contacto_quisiste_decir_titulo)
                .setMessage(getString(R.string.contacto_quisiste_decir_mensaje, direccionSugerida))
                .setPositiveButton(R.string.contacto_quisiste_decir_usar, (dialog, which) -> {
                    direccionSeleccionadaProgramaticamente = true;
                    InputFilter[] filtrosOriginales = etDireccion.getFilters();
                    etDireccion.setFilters(new InputFilter[0]);
                    etDireccion.setText(direccionSugerida);
                    etDireccion.setFilters(filtrosOriginales);
                    etDireccion.setSelection(etDireccion.getText().length());
                    direccionSeleccionadaProgramaticamente = false;
                })
                .setNegativeButton(R.string.contacto_quisiste_decir_mantener, (dialog, which) -> {
                    // Rechazó la sugerencia: esas coordenadas no son las del texto que dejó
                    latitudDireccion = null;
                    longitudDireccion = null;
                })
                .show();
    }

    /** Geocodifica en un hilo aparte: el Geocoder hace una llamada de red y no puede correr en el hilo principal. */
    private void buscarCoordenadasEnSegundoPlano(String direccionTexto, CallbackBusquedaDireccion callback) {
        Context contexto = requireContext();
        new Thread(() -> {
            boolean encontrada = false;
            String direccionSugerida = null;
            try {
                Geocoder geocoder = new Geocoder(contexto, Locale.getDefault());
                List<Address> resultados = geocoder.getFromLocationName(direccionTexto + ", Ecuador", 1);
                if (resultados != null && !resultados.isEmpty()) {
                    Address resultado = resultados.get(0);
                    String lineaDireccion = resultado.getAddressLine(0);
                    if (esPlusCode(lineaDireccion)) {
                        // Plus Code = el Geocoder no encontró una dirección real; se trata como "no encontrada"
                        encontrada = false;
                    } else {
                        latitudDireccion = String.valueOf(resultado.getLatitude());
                        longitudDireccion = String.valueOf(resultado.getLongitude());
                        direccionSugerida = lineaDireccion;
                        encontrada = true;
                    }
                }
            } catch (Exception e) {
                Log.e("Direccion", "Fallo la búsqueda de dirección: " + e.getMessage(), e);
            }
            boolean resultadoFinal = encontrada;
            String sugeridaFinal = direccionSugerida;
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> callback.onResultado(resultadoFinal, sugeridaFinal));
            }
        }).start();
    }

    private interface CallbackBusquedaDireccion {
        void onResultado(boolean encontrada, String direccionSugerida);
    }

    private static final Pattern PATRON_PLUS_CODE =
            Pattern.compile("^[23456789CFGHJMPQRVWX]{4,8}\\+[23456789CFGHJMPQRVWX]{2,3}\\b", Pattern.CASE_INSENSITIVE);

    /** Detecta si la dirección que devolvió el Geocoder es un Plus Code (ej. "PRR7+GCR ...") en vez de una dirección real. */
    private boolean esPlusCode(String direccion) {
        return direccion != null && PATRON_PLUS_CODE.matcher(direccion.trim()).find();
    }

    /*
     * Desactivado junto con el GPS (ver bloque comentado arriba).
     *
     * @Override
     * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     *     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     *     if (requestCode == PERMISO_UBICACION_REQUEST) {
     *         boolean concedido = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
     *         if (concedido) {
     *             obtenerUbicacionActual();
     *         } else {
     *             Toast.makeText(getContext(), R.string.contacto_error_permiso_ubicacion, Toast.LENGTH_SHORT).show();
     *         }
     *     }
     * }
     */

    /**
     * El cálculo va dentro del Filter (antes un TextWatcher aparte forzaba el dropdown
     * y competía con el ciclo propio de AutoCompleteTextView, cerrándolo justo después
     * de abrirlo). Un solo mecanismo evita esa pelea.
     */
    private void configurarAutocompletadoMail() {
        adapterSugerenciasMail = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>()) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        List<String> sugerencias = calcularSugerenciasMail(constraint == null ? "" : constraint.toString());
                        FilterResults resultados = new FilterResults();
                        resultados.values = sugerencias;
                        resultados.count = sugerencias.size();
                        return resultados;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        clear();
                        if (results != null && results.values != null) {
                            addAll((List<String>) results.values);
                        }
                        notifyDataSetChanged();
                    }
                };
            }
        };
        etMail.setAdapter(adapterSugerenciasMail);
        etMail.setThreshold(1);
    }

    /** Sugerencias de dominio para lo escrito antes/después del "@" — vacío si aún no hay "@" o si lo ya escrito ya es un correo completo y válido. */
    private List<String> calcularSugerenciasMail(String texto) {
        int posArroba = texto.indexOf("@");
        if (posArroba < 0) {
            return new ArrayList<>();
        }

        String usuario = texto.substring(0, posArroba);
        String dominioEscrito = texto.substring(posArroba + 1).toLowerCase(Locale.getDefault());

        List<String> sugerencias = new ArrayList<>();
        for (String dominio : DOMINIOS_MAIL) {
            if (dominio.startsWith(dominioEscrito)) {
                sugerencias.add(usuario + "@" + dominio);
            }
        }

        boolean yaCompleto = sugerencias.size() == 1 && sugerencias.get(0).equalsIgnoreCase(texto);
        return yaCompleto ? new ArrayList<>() : sugerencias;
    }

    private static final long DURACION_ALERTA_MS = 2500;

    /**
     * Muestra el borde rojo + globo de error y lo cierra solo después de un momento,
     * en vez de dejarlo fijo en pantalla.
     */
    private void mostrarErrorTemporal(EditText editText, String mensaje) {
        editText.setError(mensaje);
        editText.postDelayed(() -> {
            if (mensaje.equals(String.valueOf(editText.getError()))) {
                editText.setError(null);
            }
        }, DURACION_ALERTA_MS);
    }

    /** Más estricto que el patrón básico de Android: exige >=2 caracteres antes del @ y sin puntos al inicio/final ni dobles. */
    private boolean esEmailValido(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        String parteLocal = email.substring(0, email.indexOf("@"));
        return parteLocal.length() >= 2
                && !parteLocal.startsWith(".")
                && !parteLocal.endsWith(".")
                && !parteLocal.contains("..");
    }

    private void configurarValidacionesTiempoReal() {
        // CONTACTO: solo letras, espacios, apóstrofe y guion (con tildes/ñ incluidas)
        configurarFiltroCaracteres(etContacto, R.string.contacto_error_caracteres_invalidos,
                c -> Character.isLetter(c) || c == ' ' || c == '\'' || c == '-');
        configurarMayusculasAutomaticas(etContacto);

        // EMPRESA: Se quita el filtro restrictivo para permitir cualquier tipo de carácter
        configurarMayusculasAutomaticas(etEmpresa);

        // DIRECCION: sin filtro ni mayúsculas automáticas (Google trae su propio formato)

        // Si edita la dirección a mano tras usar el GPS, esas coordenadas ya no aplican
        etDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!direccionSeleccionadaProgramaticamente) {
                    latitudDireccion = null;
                    longitudDireccion = null;
                }
            }
        });

        // Si escribió la dirección a mano y sale del campo sin usar GPS/Buscar, se geocodifica en silencio
        etDireccion.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco && latitudDireccion == null && !esCampoVacio(etDireccion)
                    && VerificarNet.hayConexion(getContext())) {
                buscarCoordenadasEnSegundoPlano(etDireccion.getText().toString().trim(), (encontrada, direccionSugerida) -> {
                    // silencioso: si falla, el respaldo de guardar() lo vuelve a intentar
                });
            }
        });

        configurarValidacionMail();
        configurarValidacionTelefono();
        configurarValidacionTelefonoConvencional();
    }

    /** Bloquea al teclear cualquier carácter no permitido y muestra el error apenas se intenta. */
    private void configurarFiltroCaracteres(EditText editText, int mensajeErrorResId, CaracterPermitido permitido) {
        InputFilter filtro = (source, start, end, dest, dstart, dend) -> {
            StringBuilder resultado = new StringBuilder();
            boolean hayInvalidos = false;
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (permitido.esValido(c)) {
                    resultado.append(c);
                } else {
                    hayInvalidos = true;
                }
            }
            if (hayInvalidos) {
                editText.post(() -> mostrarErrorTemporal(editText, getString(mensajeErrorResId)));
            }
            if (resultado.length() == end - start) {
                return null;
            }
            return resultado.toString();
        };
        editText.setFilters(new InputFilter[]{filtro});

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    editText.setError(null);
                }
            }
        });
    }

    private interface CaracterPermitido {
        boolean esValido(char c);
    }

    /** Rechaza valores que son solo símbolos/espacios (ej. "- -") aunque pasen el filtro de caracteres permitidos. */
    private boolean tieneSuficientesCaracteres(String texto, int minimo, CaracterPermitido contable) {
        int contador = 0;
        for (char c : texto.toCharArray()) {
            if (contable.esValido(c)) {
                contador++;
            }
        }
        return contador >= minimo;
    }

    /**
     * Convierte el texto a mayúsculas a medida que se escribe, preservando tildes/ñ
     * y la posición del cursor.
     */
    private void configurarMayusculasAutomaticas(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean actualizando = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (actualizando) {
                    return;
                }
                String mayusculas = s.toString().toUpperCase(Locale.getDefault());
                if (!mayusculas.equals(s.toString())) {
                    actualizando = true;
                    int cursor = editText.getSelectionStart();
                    s.replace(0, s.length(), mayusculas);
                    editText.setSelection(Math.min(Math.max(cursor, 0), s.length()));
                    actualizando = false;
                }
            }
        });
    }

    private void configurarValidacionMail() {
        etMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString().trim();
                if (texto.isEmpty()) {
                    etMail.setError(null);
                } else if (texto.contains("@") && texto.indexOf("@") == texto.length() - 1) {
                    // Aún está escribiendo el dominio justo después del @, no marcar error todavía
                    etMail.setError(null);
                } else if (texto.contains("@") && !esEmailValido(texto)) {
                    mostrarErrorTemporal(etMail, getString(R.string.contacto_error_mail_invalido));
                } else {
                    etMail.setError(null);
                }
            }
        });
    }

    /** Celular: sin prefijo fijo (no todas las provincias usan 09 en la práctica), solo 10 dígitos. */
    private void configurarValidacionTelefono() {
        InputFilter filtroTelefono = (source, start, end, dest, dstart, dend) -> {
            String resultante = dest.toString().substring(0, dstart)
                    + source.subSequence(start, end)
                    + dest.toString().substring(dend);

            if (resultante.length() > 10) {
                ettelefono.post(() -> mostrarErrorTemporal(ettelefono, getString(R.string.contacto_error_telefono_invalido)));
                return "";
            }
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        ettelefono.setFilters(new InputFilter[]{filtroTelefono});

        ettelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString();
                if (texto.isEmpty() || texto.length() < 10) {
                    ettelefono.setError(null);
                    return;
                }
                if (!texto.matches("\\d{10}")) {
                    mostrarErrorTemporal(ettelefono, getString(R.string.contacto_error_telefono_invalido));
                } else {
                    ettelefono.setError(null);
                }
            }
        });
    }

    /**
     * Opcional, sin validación de formato (el fijo no tiene un patrón único por
     * provincia en Ecuador) — solo dígitos, máximo 10. LengthFilter se repite aquí
     * porque setFilters() reemplaza el que pone android:maxLength en la XML.
     */
    private void configurarValidacionTelefonoConvencional() {
        InputFilter filtroSoloDigitos = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        etTelefonoConvencional.setFilters(new InputFilter[]{filtroSoloDigitos, new InputFilter.LengthFilter(10)});
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (datePicker, anio, mes, dia) -> {
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dia, mes + 1, anio);
                    etAgendamiento.setText(fechaSeleccionada);
                    etAgendamiento.setError(null);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // No permitir agendar visitas en una fecha que ya pasó
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    /** true si la fecha de agendamiento es hoy o está vacía (vacío cuenta como "hoy" por seguridad). */
    private boolean esFechaAgendamientoHoy() {
        String fechaTexto = etAgendamiento.getText().toString().trim();
        if (fechaTexto.isEmpty()) {
            return true;
        }
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar seleccionada = Calendar.getInstance();
            seleccionada.setTime(formato.parse(fechaTexto));
            Calendar hoy = Calendar.getInstance();
            return seleccionada.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
                    && seleccionada.get(Calendar.DAY_OF_YEAR) == hoy.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            return true;
        }
    }

    /** Ajusta el mínimo de minutos; hay que mover el value ANTES, porque NumberPicker no admite min > value actual. */
    private void ajustarMinutoMinimo(NumberPicker minutoPicker, int minutoMinimo) {
        if (minutoPicker.getValue() < minutoMinimo) {
            minutoPicker.setValue(minutoMinimo);
        }
        minutoPicker.setMinValue(minutoMinimo);
    }

    /**
     * Hora tentativa (solo sugerencia, no confirma la cita). Selector propio en vez del reloj
     * estándar porque este no permite limitar el rango a 6:00 AM-11:00 PM. Si la fecha es hoy,
     * además no deja elegir una hora ya pasada; en fechas futuras el rango es completo.
     */
    private void mostrarSelectorHoraTentativa() {
        final int HORA_MIN = 6;
        final int HORA_MAX = 23;

        boolean esHoy = esFechaAgendamientoHoy();
        Calendar ahora = Calendar.getInstance();
        int horaActual = ahora.get(Calendar.HOUR_OF_DAY);
        int minutoActual = ahora.get(Calendar.MINUTE);

        int horaMin = HORA_MIN;
        if (esHoy) {
            horaMin = Math.max(HORA_MIN, horaActual);
            if (horaMin > HORA_MAX) {
                Toast.makeText(requireContext(), R.string.contacto_error_sin_horas_disponibles_hoy, Toast.LENGTH_LONG).show();
                return;
            }
        }
        final int horaMinFinal = horaMin; // copia effectively-final para la lambda de abajo

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_hora_tentativa_picker, null);

        NumberPicker horaPicker = view.findViewById(R.id.horaPicker);
        NumberPicker minutoPicker = view.findViewById(R.id.minutoPicker);
        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);

        String[] horasFormateadas = new String[HORA_MAX - horaMin + 1];
        for (int h = horaMin; h <= HORA_MAX; h++) {
            int horaVisible = (h > 12) ? h - 12 : h;
            String ampm = (h < 12) ? "AM" : "PM";
            horasFormateadas[h - horaMin] = String.format(Locale.getDefault(), "%02d %s", horaVisible, ampm);
        }

        horaPicker.setMinValue(horaMin);
        horaPicker.setMaxValue(HORA_MAX);
        horaPicker.setDisplayedValues(horasFormateadas);

        minutoPicker.setMinValue(0);
        minutoPicker.setMaxValue(59);
        minutoPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));

        final boolean restringeMinutosEnHoraMin = esHoy && horaMin == horaActual;

        int horaInicial = horaActual;
        int minutoInicial = minutoActual;
        if (horaInicial < horaMin) {
            horaInicial = horaMin;
            minutoInicial = restringeMinutosEnHoraMin ? minutoActual : 0;
        } else if (horaInicial > HORA_MAX) {
            horaInicial = HORA_MAX;
            minutoInicial = 0;
        }
        horaPicker.setValue(horaInicial);
        if (restringeMinutosEnHoraMin && horaInicial == horaMin) {
            ajustarMinutoMinimo(minutoPicker, minutoActual);
        }
        minutoPicker.setValue(minutoInicial);

        horaPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (restringeMinutosEnHoraMin && newVal == horaMinFinal) {
                ajustarMinutoMinimo(minutoPicker, minutoActual);
            } else {
                ajustarMinutoMinimo(minutoPicker, 0);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnAceptar.setOnClickListener(v -> {
            Calendar seleccionada = Calendar.getInstance();
            seleccionada.set(Calendar.HOUR_OF_DAY, horaPicker.getValue());
            seleccionada.set(Calendar.MINUTE, minutoPicker.getValue());

            Calendar ajustada = ajustarHoraPorConflicto(seleccionada);

            SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            etHoraTentativa.setText(formatoHora.format(ajustada.getTime()));

            if (ajustada.get(Calendar.HOUR_OF_DAY) != seleccionada.get(Calendar.HOUR_OF_DAY)
                    || ajustada.get(Calendar.MINUTE) != seleccionada.get(Calendar.MINUTE)) {
                Toast.makeText(requireContext(),
                        getString(R.string.contacto_hora_ajustada, formatoHora.format(ajustada.getTime())),
                        Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Evita agendamientos demasiado seguidos (ej. 8:00 y 8:10 pm). Compara solo contra
     * las visitas propias de ese día y solo corre la hora hacia adelante, nunca atrás.
     */
    private Calendar ajustarHoraPorConflicto(Calendar seleccionada) {
        final int SEPARACION_MINIMA_MINUTOS = 45;

        String fechaAgendamiento = etAgendamiento.getText().toString().trim();
        if (fechaAgendamiento.isEmpty()) {
            return seleccionada;
        }

        List<Integer> minutosOcupados = new ArrayList<>();
        SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try (Cursor cursor = requireContext().getContentResolver().query(
                ContractInsertProyectosContacto.CONTENT_URI, null, null, null, null)) {
            if (cursor != null) {
                int colFecha = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO);
                int colUsuario = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.USUARIO);
                int colHora = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.HORA);
                int colActivar = cursor.getColumnIndexOrThrow(ContractInsertProyectosContacto.Columnas.ACTIVAR);

                while (cursor.moveToNext()) {
                    if (!fechaAgendamiento.equals(cursor.getString(colFecha))) {
                        continue;
                    }
                    if (usuario != null && !usuario.equals(cursor.getString(colUsuario))) {
                        continue;
                    }
                    String activar = cursor.getString(colActivar);
                    if ("NO".equalsIgnoreCase(activar)) {
                        continue;
                    }
                    String horaGuardada = cursor.getString(colHora);
                    if (horaGuardada == null || horaGuardada.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        Date parseada = formatoHora.parse(horaGuardada.trim());
                        Calendar c = Calendar.getInstance();
                        c.setTime(parseada);
                        minutosOcupados.add(c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE));
                    } catch (ParseException e) {
                        Log.w("ContactoFragment", "Hora guardada con formato inesperado: " + horaGuardada);
                    }
                }
            }
        }

        if (minutosOcupados.isEmpty()) {
            return seleccionada;
        }
        Collections.sort(minutosOcupados);

        int minutos = seleccionada.get(Calendar.HOUR_OF_DAY) * 60 + seleccionada.get(Calendar.MINUTE);
        boolean huboConflicto;
        do {
            huboConflicto = false;
            for (int ocupado : minutosOcupados) {
                if (Math.abs(minutos - ocupado) < SEPARACION_MINIMA_MINUTOS) {
                    minutos = ocupado + SEPARACION_MINIMA_MINUTOS;
                    huboConflicto = true;
                }
            }
        } while (huboConflicto && minutos < 24 * 60);

        // No cruzar al día siguiente: si se pasa de medianoche, se deja en la última hora del mismo día
        if (minutos >= 24 * 60) {
            minutos = 24 * 60 - 1;
        }

        if (minutos == seleccionada.get(Calendar.HOUR_OF_DAY) * 60 + seleccionada.get(Calendar.MINUTE)) {
            return seleccionada;
        }

        Calendar resultado = (Calendar) seleccionada.clone();
        resultado.set(Calendar.HOUR_OF_DAY, minutos / 60);
        resultado.set(Calendar.MINUTE, minutos % 60);
        return resultado;
    }

    /** "No requiere visita": pasa directo a Proforma sin hora/técnico (ver ProformaFragment.cargarVisitasDesdeRegistro). */
    private void alternarCamposAgendamiento() {
        boolean noRequiereVisita = cbNoRequiereVisita.isChecked();
        llAgendamientoCampos.setVisibility(noRequiereVisita ? View.GONE : View.VISIBLE);
        if (noRequiereVisita) {
            etAgendamiento.setText("");
            etHoraTentativa.setText("");
        }
    }

    private void guardar() {
        if (!validarFormulario()) {
            btnGuardarContacto.setEnabled(true);
            return;
        }

        // Último intento de geocodificar antes de guardar; si falla, se guarda solo el texto igual
        if (latitudDireccion == null && !esCampoVacio(etDireccion) && VerificarNet.hayConexion(getContext())) {
            ProgressDialog progreso = new ProgressDialog(requireContext());
            progreso.setMessage(getString(R.string.contacto_buscando_direccion));
            progreso.setCancelable(false);
            progreso.show();

            buscarCoordenadasEnSegundoPlano(etDireccion.getText().toString().trim(), (encontrada, direccionSugerida) -> {
                progreso.dismiss();
                guardarRegistro();
                btnGuardarContacto.setEnabled(true);
            });
            return;
        }

        guardarRegistro();
        btnGuardarContacto.setEnabled(true);
    }

    private void guardarRegistro() {
        ContentValues values = new ContentValues();
        values.put(ContractInsertProyectosContacto.Columnas.CODIGO_PDV, codigoPdv);
        values.put(ContractInsertProyectosContacto.Columnas.PDV, pdv);
        values.put(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV, ciudadPdv);
        values.put(ContractInsertProyectosContacto.Columnas.USUARIO, usuario);
        values.put(ContractInsertProyectosContacto.Columnas.FECHA, fecha);
        values.put(ContractInsertProyectosContacto.Columnas.CONTACTO, etContacto.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.EMPRESA, etEmpresa.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.MAIL, etMail.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.DIRECCION, etDireccion.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.LATITUD, latitudDireccion);
        values.put(ContractInsertProyectosContacto.Columnas.LONGITUD, longitudDireccion);
        values.put(ContractInsertProyectosContacto.Columnas.TELEFONO, ettelefono.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL, etTelefonoConvencional.getText().toString().trim());
        values.put(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO, etAgendamiento.getText().toString().trim());
        String horaTentativa = etHoraTentativa.getText().toString().trim();
        if (!horaTentativa.isEmpty()) {
            values.put(ContractInsertProyectosContacto.Columnas.HORA, horaTentativa);
        }
        values.put(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA, cbNoRequiereVisita.isChecked() ? "SI" : "NO");
        values.put(ContractInsertProyectosContacto.Columnas.TITULO, getString(R.string.contacto_titulo_default));
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        requireContext().getContentResolver().insert(ContractInsertProyectosContacto.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertProyectosContacto, null);
            Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getContext(), R.string.contacto_msg_guardado, Toast.LENGTH_LONG).show();
        limpiarFormulario();
    }

    private boolean validarFormulario() {
        boolean pdvSeleccionado = codigoPdv != null && !codigoPdv.trim().isEmpty();

        if (!pdvSeleccionado && esCampoVacio(etContacto) && esCampoVacio(etEmpresa) && esCampoVacio(etMail)
                && esCampoVacio(etDireccion) && esCampoVacio(ettelefono) && esCampoVacio(etAgendamiento)) {
            Toast.makeText(getContext(), R.string.contacto_error_formulario_vacio, Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean esValido = true;

        if (!pdvSeleccionado) {
            layoutPdvContacto.setBackgroundResource(R.drawable.bg_input_proyectos_error);
            esValido = false;
        }

        String contacto = etContacto.getText().toString().trim();
        if (contacto.isEmpty()) {
            mostrarErrorTemporal(etContacto, getString(R.string.contacto_error_falta_contacto));
            esValido = false;
        } else if (!tieneSuficientesCaracteres(contacto, 2, Character::isLetter) || contacto.contains("  ")) {
            mostrarErrorTemporal(etContacto, getString(R.string.contacto_error_contacto_invalido));
            esValido = false;
        }

        String empresa = etEmpresa.getText().toString().trim();
        if (empresa.isEmpty()) {
            mostrarErrorTemporal(etEmpresa, getString(R.string.contacto_error_falta_empresa));
            esValido = false;
        }

        String mail = etMail.getText().toString().trim();
        if (mail.isEmpty()) {
            mostrarErrorTemporal(etMail, getString(R.string.contacto_error_falta_mail));
            esValido = false;
        } else if (!esEmailValido(mail)) {
            mostrarErrorTemporal(etMail, getString(R.string.contacto_error_mail_invalido));
            esValido = false;
        }

        String direccion = etDireccion.getText().toString().trim();
        if (direccion.isEmpty()) {
            mostrarErrorTemporal(etDireccion, getString(R.string.contacto_error_falta_direccion));
            esValido = false;
        } else if (direccion.length() < 5) {
            mostrarErrorTemporal(etDireccion, getString(R.string.contacto_error_direccion_muy_corta));
            esValido = false;
        }

        String telefono = ettelefono.getText().toString().trim();
        if (telefono.isEmpty()) {
            mostrarErrorTemporal(ettelefono, getString(R.string.contacto_error_falta_telefono));
            esValido = false;
        } else if (!telefono.matches("\\d{10}")) {
            mostrarErrorTemporal(ettelefono, getString(R.string.contacto_error_telefono_invalido));
            esValido = false;
        }

        if (!cbNoRequiereVisita.isChecked() && esCampoVacio(etAgendamiento)) {
            mostrarErrorTemporal(etAgendamiento, getString(R.string.contacto_error_falta_agendamiento));
            esValido = false;
        }

        return esValido;
    }

    private boolean esCampoVacio(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void limpiarFormulario() {
        etContacto.setText("");
        etEmpresa.setText("");
        etMail.setText("");
        etDireccion.setText("");
        latitudDireccion = null;
        longitudDireccion = null;
        ettelefono.setText("");
        etTelefonoConvencional.setText("");
        etAgendamiento.setText("");
        etHoraTentativa.setText("");
        cbNoRequiereVisita.setChecked(false);
        llAgendamientoCampos.setVisibility(View.VISIBLE);

        pdv = null;
        codigoPdv = null;
        ciudadPdv = null;
        etPdvContacto.setText("");
        layoutPdvContacto.setBackgroundResource(R.drawable.bg_input_proyectos);
    }
}
