package com.luckyecuador.app.PintucoAPP.ui.logistico.alerta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.luckyecuador.app.PintucoAPP.Clase.Precio;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LogisticoAlertaFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<LogisticoRegistro> registrosDiaAnterior = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    public class LogisticoRegistro {
        private String skuCode;
        private String cantidad;


        private String cantidadActual;
        private String causal;
        private String tipoLogistico;
        private String fechaCaducado;
        private String fechaPropenso;
        private String fecha;


        public String getSkuCode() { return skuCode; }
        public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

        public String getCantidad() { return cantidad; }
        public void setCantidad(String cantidad) { this.cantidad = cantidad; }
        public String getCantidadActual() {return cantidadActual;}
        public void setCantidadActual(String cantidadActual) {this.cantidadActual = cantidadActual;}

        public String getCausal() { return causal; }
        public void setCausal(String causal) { this.causal = causal; }

        public String getTipoLogistico() { return tipoLogistico; }
        public void setTipoLogistico(String tipoLogistico) { this.tipoLogistico = tipoLogistico; }

        public String getFechaCaducado() { return fechaCaducado; }
        public void setFechaCaducado(String fechaCaducado) { this.fechaCaducado = fechaCaducado; }

        public String getFechaPropenso() { return fechaPropenso; }
        public void setFechaPropenso(String fechaPropenso) { this.fechaPropenso = fechaPropenso; }

        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
    }


//    public interface AlertaDuplicadoCallback {
//        void onAceptar(boolean noVolverAMostrar);
//        void onCancelar();
//    }

    private Spinner spCategoria;
    private Spinner spCausal;

    private Spinner spCausalAlerta;
    private Spinner spSubcategoria;
    private Button btnPropia;
    private Button btnCompetencia;

    private String categoria, subcategoria, causal;
    private String id_pdv, user, codigo_pdv, punto_venta;
    private String canal, subcanal;

    DatabaseHelper handler;
    TextView empty;
    ListView listview;
    public ArrayList<Precio> listProductos;
    CustomAdapterLogisticoAlerta dataAdapter;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;
    ProgressDialog progressDialog;

    private TextView txtInicioPromo;
    private TextView txtFechaUltimoRelevo;
    private ImageButton btnInicioPromo;

    private String tipo = "RELEVO";
    private String tipo_alerta_guardado = "ALERTA";
    private final String fabricante = "AkzoNobel S.A";

    private final String modulo = "LOGISTICA";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alerta_logistico, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        LoadData();

        spCategoria = rootView.findViewById(R.id.spCategoria);
        spCausalAlerta = rootView.findViewById(R.id.spCausalAlerta);
        btnPropia = rootView.findViewById(R.id.btnPropia);
        btnCompetencia = rootView.findViewById(R.id.btnCompetencia);
        listview = rootView.findViewById(R.id.lvSKUCode);
        txtInicioPromo = rootView.findViewById(R.id.txtInicioPromo);
        txtFechaUltimoRelevo = rootView.findViewById(R.id.txtFechaUltimoRelevo);
        btnInicioPromo = rootView.findViewById(R.id.btnInicioPromo);
        empty = rootView.findViewById(R.id.recyclerview_data_empty);

        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        layout_skuName = rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = rootView.findViewById(R.id.layout_skuDescripcion);
        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        View headerView = this.getLayoutInflater().inflate(R.layout.list_row_logistico_alerta_title, null, false);
        listview.addHeaderView(headerView, null, false);

        btnPropia.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);

//        btnInicioPromo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mostrarDatePicker();
//            }
//        });
//
//        txtInicioPromo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mostrarDatePicker();
//            }
//        });

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1); // Día anterior
        calendar.add(Calendar.DAY_OF_YEAR, 0); // Día actual
        String fechaAnterior = sdf.format(calendar.getTime());
        txtInicioPromo.setText(fechaAnterior);

        /*txtInicioPromo.setText("Consultando...");
        txtFechaUltimoRelevo.setText("Consultando último relevo...");
*/

        filtrarCategoria(tipo, fabricante);
//        filtrarCausales();
        filtrarCausales(null);

        spCausalAlerta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (registrosDiaAnterior != null && !registrosDiaAnterior.isEmpty()) {
                    String causalSeleccionada = parent.getItemAtPosition(position).toString();
                    filtrarRegistrosPorCausal(causalSeleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (VerificarNet.hayConexion(getContext())) {
            consultarFechaUltimoRelevoInicial();
        } else {
            /*txtInicioPromo.setText("Sin conexión");*/
            /*txtFechaUltimoRelevo.setText("Sin conexión a internet");*/
        }

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Logístico - Alerta");

        return rootView;
    }

    @Override
    public void onRefresh() {


        if (categoria != null && !categoria.isEmpty()) {
            if (VerificarNet.hayConexion(getContext())) {
                consultarUltimoRelevo(categoria, tipo);
            } else {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void filtrarRegistrosPorCausal(String causalSeleccionada) {
        if (registrosDiaAnterior == null || registrosDiaAnterior.isEmpty()) {
            return;
        }

        ArrayList<LogisticoRegistro> registrosFiltrados = new ArrayList<>();

        if (causalSeleccionada.equals("Todos")) {

            registrosFiltrados.addAll(registrosDiaAnterior);
        } else {

            for (LogisticoRegistro registro : registrosDiaAnterior) {
                if (registro.getCausal() != null && registro.getCausal().equals(causalSeleccionada)) {
                    registrosFiltrados.add(registro);
                }
            }
        }


        if (registrosFiltrados.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
            empty.setText("No hay registros con la causal: " + causalSeleccionada);
            listview.setAdapter(null);
        } else {
            empty.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);


            dataAdapter = new CustomAdapterLogisticoAlerta(getContext(), registrosFiltrados, handler);
            listview.setAdapter(dataAdapter);
        }
    }


    public void consultarUltimoRelevo(String categoria, String tipoLogistico) {
        try {
            String url = "https://webecuador.azurewebsites.net/App/AppPintuco/Web/get_logistico_relevo.php";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("usuario", user);
            jsonBody.put("categoria", categoria);
            jsonBody.put("tipo_logistico", tipoLogistico);
            jsonBody.put("codigo_pdv", codigo_pdv);

            /*String fechaRelevo = txtInicioPromo.getText().toString();
            jsonBody.put("fecha", fechaRelevo);*/

            Log.d("DEBUG", "Consultando registros para categoría: " + categoria/* + ", fecha: " + fechaRelevo*/);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        /*progressDialog.dismiss();*/
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            procesarRespuestaRegistros(jsonResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        /*progressDialog.dismiss();*/
                        error.printStackTrace();
                    }) {

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

        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void consultarFechaUltimoRelevoInicial() {
        if (!VerificarNet.hayConexion(getContext())) {
            /*txtFechaUltimoRelevo.setText("Sin conexión");*/
            return;
        }

        String url = "https://webecuador.azurewebsites.net/App/AppPintuco/Web/get_logistico_relevo.php";

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("usuario", user);
            jsonBody.put("codigo_pdv", codigo_pdv);
            jsonBody.put("tipo_logistico", tipo);
            jsonBody.put("solo_fecha", "1");

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String estado = jsonResponse.optString("estado", "0");

                            if ("1".equals(estado) && jsonResponse.has("fecha_ultimo_relevo")) {
                                String fechaUltimoRelevo = jsonResponse.getString("fecha_ultimo_relevo");

                                txtInicioPromo.setText(fechaUltimoRelevo);
                                txtFechaUltimoRelevo.setText("Último relevo: " + fechaUltimoRelevo);

//                                Toast.makeText(getContext(), "Último relevo: " + fechaUltimoRelevo, Toast.LENGTH_LONG).show();

                                Log.d("DEBUG", "Fecha último relevo: " + fechaUltimoRelevo);
                            } else {
                                txtFechaUltimoRelevo.setText("No hay relevos previos");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                String fechaHoy = sdf.format(Calendar.getInstance().getTime());
                                txtInicioPromo.setText(fechaHoy);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("DEBUG", "Error: " + error.toString());
                        txtFechaUltimoRelevo.setText("Error al consultar");
                    }) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void procesarRespuestaRegistros(JSONObject response) {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        if (response != null) {
            try {
                Log.d("DEBUG", "Procesando respuesta: " + response.toString());

                String estado = response.optString("estado", "0");

                if ("1".equals(estado)) {
                    registrosDiaAnterior.clear();
                    if (response.has("registros")) {
                        JSONArray datos = response.getJSONArray("registros");
                        Log.d("DEBUG", "Registros recibidos: " + datos.length());

                        // Esta es la fecha del ÚLTIMO RELEVO (anterior a hoy)
                        String fechaRelevo = response.optString("fecha_relevo", "");
                        String fechaHoy = response.optString("fecha_hoy", "");

                        if (!fechaRelevo.isEmpty()) {
                            txtFechaUltimoRelevo.setText("Último relevo: " + fechaRelevo);
                            txtFechaUltimoRelevo.setVisibility(View.VISIBLE);
                            txtInicioPromo.setText(fechaRelevo);
                        }

                        for (int i = 0; i < datos.length(); i++) {
                            JSONObject item = datos.getJSONObject(i);
                            LogisticoRegistro registro = new LogisticoRegistro();

                            registro.setSkuCode(item.optString("sku_code", ""));
                            registro.setCantidad(item.optString("cantidad_anterior", "0"));
                            registro.setCantidadActual(item.optString("cantidad_actual", "0")); // Esto viene de hoy
                            registro.setCausal(item.optString("causal", ""));
                            registro.setTipoLogistico(item.optString("tipo_logistico", ""));
                            registro.setFechaCaducado(item.optString("fecha_prod_caducado", ""));
                            registro.setFechaPropenso(item.optString("fecha_prod_propenso", ""));
                            registro.setFecha(item.optString("fecha", ""));

                            registrosDiaAnterior.add(registro);
                        }

                        filtrarCausales(registrosDiaAnterior);
                        mostrarRegistrosEnListView();
                        spCausalAlerta.setSelection(0);

                        String mensaje = datos.length() + " registros cargados del último relevo ";
                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }

                } else if ("2".equals(estado)) {
                    String mensaje = response.optString("mensaje", "No hay registros");
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();

                    listview.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("No hay registros relevados para esta categoría");

                    registrosDiaAnterior.clear();
                    listview.setAdapter(null);

                    List<String> soloTodos = new ArrayList<>();
                    soloTodos.add("Todos");
                    ArrayAdapter<String> adapterCausalVacio = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, soloTodos);
                    adapterCausalVacio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCausalAlerta.setAdapter(adapterCausalVacio);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private void procesarRespuestaRegistros(JSONObject response) {
//        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
//            swipeRefresh.setRefreshing(false);
//        }
//        if (response != null) {
//            try {
//                Log.d("DEBUG", "Procesando respuesta: " + response.toString());
//
//                String estado = response.optString("estado", "0");
//
//                /**
//                 * 1 Hay registros
//                 * 2 No hay registros
//                 * 0 Error
//                 */
//
//                if ("1".equals(estado)) {
//                    registrosDiaAnterior.clear();
//                    if (response.has("registros")) {
//                        JSONArray datos = response.getJSONArray("registros");
//                        Log.d("DEBUG", "Registros recibidos: " + datos.length());
//
//                        String fechaRelevo = response.optString("fecha_relevo", "");
//                        String fechaHoy = response.optString("fecha_hoy", "");
//                        /*if (datos.length() > 0) {
//                            JSONObject primerRegistro = datos.getJSONObject(0);
//                            String fechaRelevo = primerRegistro.optString("fecha", "");*/
//
//                        if (!fechaRelevo.isEmpty()) {
//                            txtFechaUltimoRelevo.setText("Último relevo: " + fechaRelevo);
//                            txtFechaUltimoRelevo.setVisibility(View.VISIBLE);
//                            txtInicioPromo.setText(fechaRelevo);
//                            /*Log.d("DEBUG", "*** FECHA ÚLTIMO RELEVO: " + fechaRelevo + " ***");*/
//                        }
//                        /*}*/
//
//
//                        for (int i = 0; i < datos.length(); i++) {
//                            JSONObject item = datos.getJSONObject(i);
//                            LogisticoRegistro registro = new LogisticoRegistro();
//
//                            registro.setSkuCode(item.optString("sku_code", ""));
//                            registro.setCantidad(item.optString("cantidad_anterior" , "0"));
//                            registro.setCantidadActual(item.optString("cantidad_actual", "0"));
//
//                            registro.setCausal(item.optString("causal", ""));
//                            registro.setTipoLogistico(item.optString("tipo_logistico", ""));
//                            registro.setFechaCaducado(item.optString("fecha_prod_caducado", ""));
//                            registro.setFechaPropenso(item.optString("fecha_prod_propenso", ""));
//                            registro.setFecha(item.optString("fecha", ""));
//
//                            registrosDiaAnterior.add(registro);
//
////                            Log.d("DEBUG", "Registro " + i + ": " + registro.getSkuCode() + " - " + registro.getCantidad());
//                            Log.d("DEBUG", "Registro " + i + ": " + registro.getSkuCode() +
//                                    " - Ayer: " + registro.getCantidad() +
//                                    " - Hoy: " + registro.getCantidadActual());
//                        }
//
//                        filtrarCausales(registrosDiaAnterior);
//
//                        mostrarRegistrosEnListView();
//
//                        spCausalAlerta.setSelection(0);
//
//
//                        String mensaje = datos.length() + " registros cargados del ultimo relevo";
//                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
//
//                    } /*else {
//                        Toast.makeText(getContext(), "Formato de respuesta incorrecto", Toast.LENGTH_LONG).show();
//                        empty.setVisibility(View.VISIBLE);
//                        listview.setVisibility(View.GONE);
//                        empty.setText("Error en formato de datos");
//                    }*/
//
//                } else if ("2".equals(estado)) {
//
//                    String mensaje = response.optString("mensaje", "No hay registros");
//                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
//
//                    listview.setVisibility(View.GONE);
//                    empty.setVisibility(View.VISIBLE);
//                    empty.setText("No hay registros relevados para esta categoría");
//
//                    registrosDiaAnterior.clear();
//                    if (listProductos != null) {
//                        listProductos.clear();
//                    }
//                    listview.setAdapter(null);
//                    if (dataAdapter != null) {
//                        dataAdapter = null;
//                    }
//
//                } else {
//                    String mensaje = response.optString("mensaje", "Error desconocido");
//                    /*Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();*/
//
//                    listview.setVisibility(View.GONE);
//                    empty.setVisibility(View.VISIBLE);
//                    empty.setText("Error al cargar registros");
//                }
//
//            } catch (JSONException e) {
//                Log.e("DEBUG", "Error procesando JSON: " + e.getMessage());
//                e.printStackTrace();
//                Toast.makeText(getContext(), "Error al procesar respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(getContext(), "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void manejarErrorWebService(VolleyError error) {
//        String errorMessage = "Error desconocido";
//
//        if (error.networkResponse != null) {
//            int statusCode = error.networkResponse.statusCode;
//            String data = new String(error.networkResponse.data);
//
//            Log.e("WEBSERVICE_ERROR_DETAIL", "Status Code: " + statusCode);
//            Log.e("WEBSERVICE_ERROR_DETAIL", "Response Data: " + data);
//
//            switch (statusCode) {
//                case 404:
//                    errorMessage = "Error 404 - Recurso no encontrado. Verifica la URL o los parámetros.";
//                    break;
//                case 500:
//                    errorMessage = "Error 500 - Error interno del servidor.";
//                    break;
//                case 400:
//                    errorMessage = "Error 400 - Solicitud incorrecta. Verifica los parámetros.";
//                    break;
//                default:
//                    errorMessage = "Error HTTP " + statusCode;
//            }
//        } else if (error instanceof TimeoutError) {
//            errorMessage = "Timeout - El servidor tardó demasiado en responder";
//        } else if (error instanceof NoConnectionError) {
//            errorMessage = "No hay conexión a internet";
//        } else if (error instanceof ServerError) {
//            errorMessage = "Error del servidor";
//        } else if (error instanceof NetworkError) {
//            errorMessage = "Error de red";
//        }
//
//        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
//    }

    private void mostrarRegistrosEnListView() {
        if (registrosDiaAnterior.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
            empty.setText("No hay registros");
            listview.setAdapter(null);
        } else {
            empty.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);

            dataAdapter = new CustomAdapterLogisticoAlerta(getContext(), registrosDiaAnterior, handler);
            listview.setAdapter(dataAdapter);
        }
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
        Log.d("LOAD_DATA", "Código PDV cargado: " + codigo_pdv);
        Log.d("LOAD_DATA", "Usuario cargado: " + user);
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        List<String> operadores = handler.getCategoriaLogisticoAlerta(tipo, fabricante, codigo_pdv, canal, subcanal, modulo);

        if (operadores.size() == 2) {
            operadores.remove(0);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
//        spCategoria.post(() -> spCategoria.setSelection(0));
    }


//    public void filtrarCausales() {
//        List<String> operadores = handler.getCausalLogisticoAlerta();
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // *** USAR spCausalAlerta en lugar de spCausal ***
//        spCausalAlerta.setAdapter(dataAdapter);
//        // Si necesitas listener para causales, descomenta:
//        // spCausalAlerta.setOnItemSelectedListener(this);
//    }

    public void filtrarCausales(ArrayList<LogisticoRegistro> registros) {
        List<String> operadores = new ArrayList<>();
        operadores.add("Todos");
        if (registros != null && !registros.isEmpty()) {
            for (LogisticoRegistro registro : registros) {
                String causal = registro.getCausal();
                if (causal != null && !causal.isEmpty() && !operadores.contains(causal)) {
                    operadores.add(causal);
                }
            }
        } /*else {
            List<String> operadores = handler.getCausalLogisticoAlerta();
        }*/

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // *** USAR spCausalAlerta en lugar de spCausal ***
        spCausalAlerta.setAdapter(dataAdapter);
        // Si necesitas listener para causales, descomenta:
        // spCausalAlerta.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//        if (adapterView == spCategoria && position > 0) {
//            categoria = adapterView.getItemAtPosition(position).toString();
//
//            registrosDiaAnterior.clear();
//            if (listProductos != null) {
//                listProductos.clear();
//            }
//
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Consultando registros del día anterior...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//
//            if (VerificarNet.hayConexion(getContext())) {

//                consultarRegistrosDiaAnterior(categoria, tipo);
//            } else {
//                progressDialog.dismiss();
//                Toast.makeText(getContext(), "No hay conexión para cargar registros anteriores", Toast.LENGTH_SHORT).show();
//                empty.setVisibility(View.VISIBLE);
//                listview.setVisibility(View.GONE);
//                empty.setText("Sin conexión a internet");
//            }
//        }

        if (adapterView == spCategoria) {
            if (position == 0) {

                registrosDiaAnterior.clear();
                if (listProductos != null) listProductos.clear();
                if (dataAdapter != null) dataAdapter.notifyDataSetChanged();
                listview.setAdapter(null);
                if (dataAdapter != null) {
                    dataAdapter = null;
                }

                empty.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                empty.setText("Seleccione una categoría");

                List<String> operadores = new ArrayList<>();
                operadores.add("Todos");
                ArrayAdapter<String> adapterCausal = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, operadores);
                adapterCausal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCausalAlerta.setAdapter(adapterCausal);

                categoria = "";

            } else if (position > 0) {
                categoria = adapterView.getItemAtPosition(position).toString();
//                filtrarCausales();

                registrosDiaAnterior.clear();
                if (listProductos != null) {
                    listProductos.clear();
                }

                listview.setAdapter(null);
                if (dataAdapter != null) {
                    dataAdapter = null;
                }
                /*progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Consultando último relevo...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
*/
                if (VerificarNet.hayConexion(getContext())) {
//                    consultarRegistrosDiaAnterior(categoria, tipo);
                    consultarUltimoRelevo(categoria, tipo);
                } else {
                    /*progressDialog.dismiss();*/
                    if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
                        swipeRefresh.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "No hay conexión para cargar registros relevado", Toast.LENGTH_SHORT).show();
                    empty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    empty.setText("Sin conexión a internet");
                }


            }
        }


    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
    }

    public void insertData(String skuSelected, String cantidad, String causal, String tipoLogistico,
                           String foto, String comentario, String fechaCaducado, String fechaPropenso) {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();

            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            ContentValues values = new ContentValues();

            String categoria = spCategoria.getSelectedItem().toString();
            String causalSeleccionada = spCausalAlerta.getSelectedItem().toString();

            if (causalSeleccionada.equals("Seleccione")) {
                Toast.makeText(getContext(), "Debe seleccionar una causal", Toast.LENGTH_SHORT).show();
                return;
            }

            values.put(ContractInsertLogisticoRelevo.Columnas.PHARMA_ID, id_pdv);
            values.put(ContractInsertLogisticoRelevo.Columnas.CODIGO, codigo_pdv);
            values.put(ContractInsertLogisticoRelevo.Columnas.USUARIO, user);
            values.put(ContractInsertLogisticoRelevo.Columnas.SUPERVISOR, punto_venta);
            values.put(ContractInsertLogisticoRelevo.Columnas.FECHA, fechaser);
            values.put(ContractInsertLogisticoRelevo.Columnas.HORA, horaser);
            values.put(ContractInsertLogisticoRelevo.Columnas.CATEGORIA, categoria);
            values.put(ContractInsertLogisticoRelevo.Columnas.SKU_CODE, skuSelected);
            values.put(ContractInsertLogisticoRelevo.Columnas.PREGULAR, cantidad);
            values.put(ContractInsertLogisticoRelevo.Columnas.CAUSAL, causal);
            values.put(ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO, tipo_alerta_guardado);
            values.put(ContractInsertLogisticoRelevo.Columnas.FOTO, foto);
            values.put(ContractInsertLogisticoRelevo.Columnas.COMENTARIO, comentario);
            values.put(ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_CADUCADO, fechaCaducado);
            values.put(ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_PROPENSO, fechaPropenso);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContext().getContentResolver().insert(ContractInsertLogisticoRelevo.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertLogisticoRelevo, null);
                Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnPropia) {
            tipo = "RELEVO";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        } else if (v == btnCompetencia) {
            tipo = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
    }

//    private void mostrarAlertaDuplicado(Context context, final AlertaDuplicadoCallback callback) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View dialogView = inflater.inflate(R.layout.alert_dialog_duplicado_logistico_relevo, null);
//
//        builder.setView(dialogView);
//        final AlertDialog dialog = builder.create();
//
//        CheckBox checkNoMostrar = dialogView.findViewById(R.id.checkBox);
//        Button btnAceptar = dialogView.findViewById(R.id.btn_acept);
//        Button btnCancelar = dialogView.findViewById(R.id.btn_cancelar);
//
//        btnAceptar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean noVolverAMostrar = checkNoMostrar.isChecked();
//                dialog.dismiss();
//                if (callback != null) {
//                    callback.onAceptar(noVolverAMostrar);
//                }
//            }
//        });
//
//        btnCancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (callback != null) {
//                    callback.onCancelar();
//                }
//            }
//        });
//
//        dialog.setCancelable(false);
//        dialog.show();
//    }

//    private void mostrarDialogoCausalesMultiples(Context context, final CustomAdapterLogisticoAlerta.ViewHolder viewHolder) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View dialogView = inflater.inflate(R.layout.alertdialog_causales_logistico, null);
//
//        builder.setView(dialogView);
//        final AlertDialog dialog = builder.create();
//
//        LinearLayout containerCausales = dialogView.findViewById(R.id.containerCausales);
//        final LinearLayout containerCaducado = dialogView.findViewById(R.id.containerCaducado);
//        final LinearLayout containerPropenso = dialogView.findViewById(R.id.containerPropenso);
//
//        final EditText etFechaCaducado = dialogView.findViewById(R.id.etFechaCaducado);
//        final EditText etFechaPropenso = dialogView.findViewById(R.id.etFechaPropenso);
//        final TextView tvFechaCaducadoSeleccionada = dialogView.findViewById(R.id.tvFechaCaducadoSeleccionada);
//        final TextView tvFechaPropensoSeleccionada = dialogView.findViewById(R.id.tvFechaPropensoSeleccionada);
//
//        Button btnCalendarioCaducado = dialogView.findViewById(R.id.btnCalendarioCaducado);
//        Button btnCalendarioPropenso = dialogView.findViewById(R.id.btnCalendarioPropenso);
//        Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);
//        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
//

//        final List<String> causalesSeleccionadasTemp = new ArrayList<>();
//        final String[] fechaCaducadoTemp = {""};
//        final String[] fechaPropensoTemp = {""};
//

//        List<String> causales = handler.getCausalLogisticoRelevo();
//

//        /*for (String causal : causales) {
//            if ("Seleccione".equals(causal)) continue;
//
//            AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
//            checkBox.setText(causal);
//            checkBox.setTextSize(14);
//            checkBox.setTextColor(Color.WHITE);
//            checkBox.setPadding(10, 10, 10, 10);
//            checkBox.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
//
//

//            if (viewHolder.causalSeleccionada.contains(causal)) {
//                checkBox.setChecked(true);
//                causalesSeleccionadasTemp.add(causal);
//
//                // Mostrar fechas si ya estaban seleccionadas
//                if (causal.equals("PRODUCTO CADUCADO") && !viewHolder.fechaCaducado.isEmpty()) {
//                    containerCaducado.setVisibility(View.VISIBLE);
//                    etFechaCaducado.setText(viewHolder.fechaCaducado);
//                    fechaCaducadoTemp[0] = viewHolder.fechaCaducado;
//                    tvFechaCaducadoSeleccionada.setText("Fecha caducado: " + viewHolder.fechaCaducado);
//                    tvFechaCaducadoSeleccionada.setVisibility(View.VISIBLE);
//                }
//
//                if (causal.equals("PRODUCTO PROPENSO") && !viewHolder.fechaPropenso.isEmpty()) {
//                    containerPropenso.setVisibility(View.VISIBLE);
//                    etFechaPropenso.setText(viewHolder.fechaPropenso);
//                    fechaPropensoTemp[0] = viewHolder.fechaPropenso;
//                    tvFechaPropensoSeleccionada.setText("Fecha propenso: " + viewHolder.fechaPropenso);
//                    tvFechaPropensoSeleccionada.setVisibility(View.VISIBLE);
//                }
//            }
//

//            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if (isChecked) {
//                    // Desmarcar otros checkboxes (solo una selección)
//                    for (int i = 0; i < containerCausales.getChildCount(); i++) {
//                        View child = containerCausales.getChildAt(i);
//                        if (child instanceof AppCompatCheckBox && child != buttonView) {
//                            ((AppCompatCheckBox) child).setChecked(false);
//                        }
//                    }
//

//                    causalesSeleccionadasTemp.clear();
//                    causalesSeleccionadasTemp.add(causal);
//
//                    // Mostrar calendario específico
//                    if (causal.equals("PRODUCTO CADUCADO")) {
//                        containerCaducado.setVisibility(View.VISIBLE);
//                        containerPropenso.setVisibility(View.GONE);
//                    } else if (causal.equals("PRODUCTO PROPENSO")) {
//                        containerPropenso.setVisibility(View.VISIBLE);
//                        containerCaducado.setVisibility(View.GONE);
//                    } else {
//                        containerCaducado.setVisibility(View.GONE);
//                        containerPropenso.setVisibility(View.GONE);
//                    }
//                } else {
//                    causalesSeleccionadasTemp.remove(causal);
//
//                    if (causal.equals("PRODUCTO CADUCADO")) {
//                        containerCaducado.setVisibility(View.GONE);
//                        fechaCaducadoTemp[0] = "";
//                        etFechaCaducado.setText("");
//                        tvFechaCaducadoSeleccionada.setVisibility(View.GONE);
//                    } else if (causal.equals("PRODUCTO PROPENSO")) {
//                        containerPropenso.setVisibility(View.GONE);
//                        fechaPropensoTemp[0] = "";
//                        etFechaPropenso.setText("");
//                        tvFechaPropensoSeleccionada.setVisibility(View.GONE);
//                    }
//                }
//            });
//
//            containerCausales.addView(checkBox);
//        }*/
//
//        // Botones para abrir calendario
//        btnCalendarioCaducado.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                    (view, selectedYear, selectedMonth, selectedDay) -> {
//                        String fechaFormateada = String.format("%02d/%02d/%04d",
//                                selectedDay, selectedMonth + 1, selectedYear);
//
//                        etFechaCaducado.setText(fechaFormateada);
//                        fechaCaducadoTemp[0] = fechaFormateada;
//                        tvFechaCaducadoSeleccionada.setText("Fecha caducado: " + fechaFormateada);
//                        tvFechaCaducadoSeleccionada.setVisibility(View.VISIBLE);
//                    }, year, month, day);
//
//            datePickerDialog.show();
//        });
//
//        btnCalendarioPropenso.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                    (view, selectedYear, selectedMonth, selectedDay) -> {
//                        String fechaFormateada = String.format("%02d/%02d/%04d",
//                                selectedDay, selectedMonth + 1, selectedYear);
//
//                        etFechaPropenso.setText(fechaFormateada);
//                        fechaPropensoTemp[0] = fechaFormateada;
//                        tvFechaPropensoSeleccionada.setText("Fecha propenso: " + fechaFormateada);
//                        tvFechaPropensoSeleccionada.setVisibility(View.VISIBLE);
//                    }, year, month, day);
//
//            datePickerDialog.show();
//        });
//
//        // Click en EditText también abre calendario
//        etFechaCaducado.setOnClickListener(v -> btnCalendarioCaducado.performClick());
//        etFechaPropenso.setOnClickListener(v -> btnCalendarioPropenso.performClick());
//
//        // Botón aceptar
//        btnAceptar.setOnClickListener(v -> {
//            if (causalesSeleccionadasTemp.isEmpty()) {
//                Toast.makeText(context, "Seleccione al menos una causal", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (causalesSeleccionadasTemp.contains("PRODUCTO CADUCADO") &&
//                    fechaCaducadoTemp[0].isEmpty()) {
//                Toast.makeText(context, "Seleccione fecha para producto caducado", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (causalesSeleccionadasTemp.contains("PRODUCTO PROPENSO") &&
//                    fechaPropensoTemp[0].isEmpty()) {
//                Toast.makeText(context, "Seleccione fecha para producto propenso", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Construir string con la causal seleccionada
//            String causalSeleccionada = causalesSeleccionadasTemp.get(0); // Solo una selección
//
//            // Guardar en el ViewHolder
//            viewHolder.causalSeleccionada = causalSeleccionada;
//            viewHolder.fechaCaducado = fechaCaducadoTemp[0];
//            viewHolder.fechaPropenso = fechaPropensoTemp[0];
//
//            // Actualizar spinner
//            ArrayAdapter<String> adapter = (ArrayAdapter<String>) viewHolder.spCausalRelevo.getAdapter();
//            adapter.clear();
//            adapter.add(causalSeleccionada);
//            adapter.notifyDataSetChanged();
//            viewHolder.spCausalRelevo.setSelection(0);
//
//            dialog.dismiss();
//        });
//
//        btnCancelar.setOnClickListener(view -> dialog.dismiss());
//
//        dialog.show();
//    }


    public class CustomAdapterLogisticoAlerta extends ArrayAdapter<LogisticoRegistro> {
        private Context context;
        private ArrayList<LogisticoRegistro> registros;
        private DatabaseHelper handler;


        public CustomAdapterLogisticoAlerta(Context context,
                                            ArrayList<LogisticoRegistro> registros,
                                            DatabaseHelper handler) {
            super(context, 0, registros);
            this.context = context;
            this.registros = registros;
            this.handler = handler;
        }

        public class ViewHolder {
            TextView lblSku;
            TextView txtCantidadAnterior;
            TextView txtCantidadActual;
//            Spinner spCausalRelevo;
            TextView txtCausalRelevo;
            String causalSeleccionada = "";
            String fechaCaducado = "";
            String fechaPropenso = "";
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder vHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_row_logistico_alerta_registros, parent, false);

                vHolder = new ViewHolder();
                vHolder.lblSku = convertView.findViewById(R.id.lblSku);
                vHolder.txtCantidadAnterior = convertView.findViewById(R.id.txtCantidad);
                vHolder.txtCantidadActual = convertView.findViewById(R.id.txtCantidadActual);
//                vHolder.spCausalRelevo = convertView.findViewById(R.id.spCausalRelevo);
                vHolder.txtCausalRelevo = convertView.findViewById(R.id.txtCausalRelevo);

                /*
                 Configurar spinner
                 */
//                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
//                        context,
//                        android.R.layout.simple_spinner_item,
//                        new ArrayList<String>()
//                );
//                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                vHolder.spCausalRelevo.setAdapter(spinnerAdapter);


                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            if (position < registros.size()) {
                LogisticoRegistro registro = registros.get(position);

                // SKU
                vHolder.lblSku.setText(registro.getSkuCode());

                // Cantidad ANTERIOR (del día anterior) - ej: 8
                vHolder.txtCantidadAnterior.setText(registro.getCantidad());

                // Cantidad ACTUAL (de hoy) - ej: 10 (o 0 si no hay registro hoy)
                vHolder.txtCantidadActual.setText(registro.getCantidadActual());

                // Causal
                if (registro.getCausal() != null && !registro.getCausal().isEmpty()) {
                    vHolder.causalSeleccionada = registro.getCausal();
                    vHolder.txtCausalRelevo.setText(registro.getCausal());
//                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) vHolder.spCausalRelevo.getAdapter();
//                    adapter.clear();
//                    adapter.add(registro.getCausal());
//                    adapter.notifyDataSetChanged();
                } else {
//                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) vHolder.spCausalRelevo.getAdapter();
//                    adapter.clear();
//                    adapter.add("Seleccione");
//                    adapter.notifyDataSetChanged();

                    vHolder.txtCausalRelevo.setText("Seleccione");
                }

                // Fechas
                vHolder.fechaCaducado = registro.getFechaCaducado();
                vHolder.fechaPropenso = registro.getFechaPropenso();

                final ViewHolder finalVHolder = vHolder;

                // Listener para spinner
                /*vHolder.spCausalRelevo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            mostrarDialogoCausalesMultiples(context, finalVHolder);
                        }
                        return false;
                    }
                });*/
            }



            return convertView;
        }

        @Override
        public int getCount() {
            return registros != null ? registros.size() : 0;
        }
    }
}