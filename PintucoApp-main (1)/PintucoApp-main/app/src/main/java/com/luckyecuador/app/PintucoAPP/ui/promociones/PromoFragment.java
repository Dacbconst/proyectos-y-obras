package com.luckyecuador.app.PintucoAPP.ui.promociones;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PromoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {

    ImageButton btnIniPromo;
    ImageButton btnFinPromo;

    ArrayList<String> array_canal = new ArrayList<>();

    private Spinner spCategoria;
    private Spinner spTipoPromocion;
    private Spinner spDescripcionPromocion;
    private Spinner spSubcategoria;
    private EditText txtMecanica;
    private EditText txtMargenDscto;

    private EditText txtProducto;
    private Spinner spMarca;
    private Spinner spSKU;
    private TextView txtInicioPromo;
    private TextView txtFinPromo;
    private TextView txtAnterior;
    private TextView txtActual;

    private TextView tv_Mecanica;
    private CheckBox chkStock;
    private Button btnGuardar;
    LinearLayout layoutPVCanterior;
    LinearLayout layoutPVCactual;
    private Button btnPropia;
    private Button btnCompetencia;
    private Button btnGenerarMecanica;

    DatabaseHelper handler;

    TextView empty;
    ListView listview;

    ListViewAdapter dataAdapter;

    String id_pdv, categoria, brand, fechaventas, tipo_promocion, vigencia, mecanica_db, mecanica, mecanica_generalizada, observaciones, subcategoria, marca;
    private String user, codigo_pdv, punto_venta, fecha, hora, format, sku, canal, subcanal, descripcionPromocion;

    // Lista de PVC anteriores bajados del servidor, para cruzar con el SKU seleccionado
    private ArrayList<Base_precio_pvc> listaPvcAnteriores = new ArrayList<>();

    // SWITCH PARA SINCRONIZACIÓN
    private final boolean HABILITAR_SYNC_PROMO = true;

    public static ImageView imageView;
    private Bitmap bitmapfinal;
    Bitmap bitmap;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private final String CARPETA_RAIZ = "PintucoApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Competencia";
    String path;
    ImageButton btnCamera;

    private String tipo = "MARCA_PROPIA";
    private final String fabricante = "AkzoNobel S.A";
    private final String modulo = "PROMOCIONES";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_promo, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LoadData();

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);

        declaracionElementos(rootView);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String textActual = dest.toString();
                String textoResultante = textActual.substring(0, dstart) + source.toString() + textActual.substring(dend);
                String[] parts = textoResultante.split("\\.");
                if (parts.length > 0 && parts[0].length() > 3) return "";
                if (parts.length > 1 && parts[1].length() > 2) return "";
                return null;
            }
        };

        txtAnterior.setFilters(new InputFilter[]{filter});
        txtActual.setFilters(new InputFilter[]{filter});

        btnCamera.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnPropia.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);
        btnGenerarMecanica.setOnClickListener(this);

        filtrarCategoria(tipo, fabricante);
        filtrarDescripcionPromocion(canal);

        // Sincronizar PVC anteriores al entrar al módulo
        if (HABILITAR_SYNC_PROMO) {
            sincronizarPvcAnteriorPromo();
        }

        txtActual.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
        });

        array_canal.add(canal);

        ListView listView = (ListView) rootView.findViewById(R.id.lvSKUCode);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Promociones");

        return rootView;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SINCRONIZACIÓN: baja el último pvc_anterior por SKU desde insert_promociones
    // del servidor y lo guarda/actualiza en insert_precios local con CATEGORIA=SYNC_PROMO.
    // Cuando el usuario selecciona el SKU en spSKU, se cruza y rellena txtAnterior.
    // ─────────────────────────────────────────────────────────────────────────
    private void sincronizarPvcAnteriorPromo() {
        Log.i("PROMO_SYNC", "1. Iniciando sincronizarPvcAnteriorPromo...");
        Log.i("PROMO_SYNC", "2. usuario=" + user + " | codigo=" + codigo_pdv);

        if (!VerificarNet.hayConexion(getContext())) {
            Log.e("PROMO_SYNC", "Sin conexión, se omite sincronización.");
            // Aunque no haya conexión, cargamos lo que hay en local
            listaPvcAnteriores = handler.getUltimoPvcAnteriorPromoDesdeLocal(user, codigo_pdv);
            Log.i("PROMO_SYNC", "Cargados desde local: " + listaPvcAnteriores.size() + " registros.");
            return;
        }

        try {
            JSONObject jobject = new JSONObject();
            jobject.put("usuario", user);
            jobject.put("codigo", codigo_pdv);

            Log.i("PROMO_SYNC", "3. URL: " + Constantes.GET_ULTIMO_PVC_ANTERIOR_PROMO);
            Log.i("PROMO_SYNC", "4. Payload: " + jobject.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constantes.GET_ULTIMO_PVC_ANTERIOR_PROMO,
                    jobject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("PROMO_SYNC", "5. Respuesta: " + response.toString());
                            try {
                                if (response.has(Constantes.ULTIMO_PVC_ANTERIOR_PROMO_RESULT)) {
                                    JSONArray array = response.getJSONArray(Constantes.ULTIMO_PVC_ANTERIOR_PROMO_RESULT);
                                    Log.i("PROMO_SYNC", "6. Registros recibidos: " + array.length());
                                    handler.guardarUltimoPvcAnteriorPromoLocal(array, user, codigo_pdv);
                                    Log.i("PROMO_SYNC", "7. Guardado/actualizado local completado.");
                                    // Recargar la lista en memoria para que esté lista al seleccionar SKU
                                    listaPvcAnteriores = handler.getUltimoPvcAnteriorPromoDesdeLocal(user, codigo_pdv);
                                    Log.i("PROMO_SYNC", "8. Lista en memoria: " + listaPvcAnteriores.size() + " registros.");
                                } else {
                                    Log.e("PROMO_SYNC", "JSON sin clave: " + Constantes.ULTIMO_PVC_ANTERIOR_PROMO_RESULT);
                                    // Aunque no haya datos del servidor, cargamos local
                                    listaPvcAnteriores = handler.getUltimoPvcAnteriorPromoDesdeLocal(user, codigo_pdv);
                                }
                            } catch (JSONException e) {
                                Log.e("PROMO_SYNC", "Error JSON: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("PROMO_SYNC", "Error Volley: " + error.getMessage());
                            // En caso de error de red, cargamos lo que haya en local
                            listaPvcAnteriores = handler.getUltimoPvcAnteriorPromoDesdeLocal(user, codigo_pdv);
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            Log.i("PROMO_SYNC", "Petición enviada a Volley.");

        } catch (JSONException e) {
            Log.e("PROMO_SYNC", "Error creando JSON: " + e.getMessage());
        }
    }

    // Cruza el SKU seleccionado con listaPvcAnteriores y rellena txtAnterior si encuentra match
    private void cruzarPvcAnteriorConSku(String skuSeleccionado) {
        if (listaPvcAnteriores == null || listaPvcAnteriores.isEmpty()) return;
        for (Base_precio_pvc bp : listaPvcAnteriores) {
            if (bp.getSku() != null && bp.getSku().trim().equalsIgnoreCase(skuSeleccionado.trim())) {
                if (bp.getPvc() != null && !bp.getPvc().isEmpty() && !bp.getPvc().equals("0")) {
                    // Solo rellena si el campo está habilitado (algunas descripciones lo deshabilitan)
                    if (txtAnterior.isEnabled()) {
                        txtAnterior.setText(bp.getPvc().trim());
                        Log.i("PROMO_SYNC", "PVC anterior autocompletado: " + skuSeleccionado + " → " + bp.getPvc());
                    }
                }
                break;
            }
        }
    }

    private void obtenerMecanica() {
        String sku = spSKU.getSelectedItem().toString();
        String descripcionPromocion = spDescripcionPromocion.getSelectedItem().toString();
        String producto = txtProducto.getText().toString();
        String pvcAnterior = txtAnterior.getText().toString();
        String pvcActual = txtActual.getText().toString();
        String margenDscto = obtenerMargenDscto();

        if (!sku.equalsIgnoreCase("Seleccione Sku")) {
            if (!pvcAnterior.isEmpty() && !pvcActual.isEmpty()) {
                if (descripcionPromocion.equalsIgnoreCase("Descuento en precio")) {
                    if (!margenDscto.equalsIgnoreCase("0")) {
                        margenDscto = margenDscto.substring(1, margenDscto.length() - 1);
                        String mecanica = "Descuento En Precio: " + sku + " Con el " + margenDscto + "% " + "Precio Anterior: $" + pvcAnterior + " Precio Actual: $" + pvcActual;
                        tv_Mecanica.setText(mecanica);
                    }
                }
                if (descripcionPromocion.equalsIgnoreCase("Cambio de imagen / empaque")) {
                    String mecanica = "Cambio De Imagen: " + sku + " A " + pvcActual;
                    tv_Mecanica.setText(mecanica);
                }
            } else {
                Toast.makeText(getContext(), "Debes ingresar el pvc anterior y pvc actual", Toast.LENGTH_SHORT).show();
            }

            if (descripcionPromocion.equalsIgnoreCase("Producto gratis")) {
                tv_Mecanica.setText("Producto gratis: Por la compra de " + sku + " Lleva Gratis " + producto);
            }
            if (descripcionPromocion.equalsIgnoreCase("Promocional gratis")) {
                tv_Mecanica.setText("Promocional gratis: Por la compra de: " + sku + " Lleva Gratis " + producto);
            }
            if (descripcionPromocion.equalsIgnoreCase("Canje / Cartilla")) {
                tv_Mecanica.setText("Canje : " + sku + " " + producto);
            }
            if (descripcionPromocion.equalsIgnoreCase("Cupones")) {
                tv_Mecanica.setText("Cupones : " + sku + " " + producto);
            }
            if (descripcionPromocion.equalsIgnoreCase("Combo Descuento") || descripcionPromocion.equalsIgnoreCase("Combo descuento") || descripcionPromocion.equalsIgnoreCase("Combo dscto")) {
                tv_Mecanica.setText("Combo Descuento: Por la compra de " + sku + " a " + pvcActual + ", Gratis " + producto);
            }
            if (descripcionPromocion.equalsIgnoreCase("2do a mitad de precio")) {
                tv_Mecanica.setText("2do a mitad de precio: Por la Compra de " + sku + ", Lleve el 2do a mitad de precio. ");
            }
            if (descripcionPromocion.equalsIgnoreCase("3 x 2")) {
                tv_Mecanica.setText("Combo 3X2: Por La Compra De Dos " + sku + ", Lleve Gratis El 3Ero A " + producto);
            }
        } else {
            Toast.makeText(getContext(), "Debes escoger el SKU", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    private void declaracionElementos(ViewGroup rootView) {
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSKU = (Spinner) rootView.findViewById(R.id.spSKU);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);
        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

        spTipoPromocion = (Spinner) rootView.findViewById(R.id.spTipoPromocion);
        spDescripcionPromocion = (Spinner) rootView.findViewById(R.id.spDescripcionPromocion);
        txtProducto = (EditText) rootView.findViewById(R.id.txtProducto);
        txtMargenDscto = (EditText) rootView.findViewById(R.id.txtMargenDscto);
        txtAnterior = (TextView) rootView.findViewById(R.id.txtPVCanterior);
        txtActual = (TextView) rootView.findViewById(R.id.txtPVCactual);
        tv_Mecanica = (TextView) rootView.findViewById(R.id.tv_Mecanica);

        imageView = (ImageView) rootView.findViewById(R.id.ivFotoExhibiciones);
        btnCamera = (ImageButton) rootView.findViewById(R.id.ibCargarFotoExhibiciones);
        btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);
        btnGenerarMecanica = (Button) rootView.findViewById(R.id.btnGenerarMecanica);

        layoutPVCanterior = (LinearLayout) rootView.findViewById(R.id.layoutPVCanterior);
        layoutPVCactual = (LinearLayout) rootView.findViewById(R.id.layoutPVCactual);
        btnPropia = (Button) rootView.findViewById(R.id.btnPropia);
        btnCompetencia = (Button) rootView.findViewById(R.id.btnCompetencia);
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        Log.i("cdsa", "" + subcanal);
        List<String> operadores = handler.getCategoriaPromocion(tipo, fabricante, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void filtrarSubcategoria(String categoria, String tipo, String fabricante) {
        List<String> operadores = handler.getSubcategoriaPromocion(categoria, tipo, fabricante, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String categoria, String subcategoria, String tipo, String fabricante) {
        List<String> marcas_blancas = handler.getMarcasBlancas();
        if (!canal.equals("MAYORISTA")) {
            marcas_blancas.clear();
            marcas_blancas.add("-");
        }
        List<String> operadores = handler.getMarcaPromocion(categoria, subcategoria, tipo, fabricante, canal, subcanal, marcas_blancas, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    public void filtrarSKU(String categoria, String subcategoria, String marca, String tipo, String fabricante) {
        List<String> operadores = handler.getSKUPromocion2(categoria, subcategoria, marca, tipo, fabricante, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spSKU.setAdapter(dataAdapter);
        spSKU.setOnItemSelectedListener(this);
    }

    public void filtrarTipoPromocion(String canal) {
        List<String> operadores = handler.getTipoPromocion(canal);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void filtrarDescripcionPromocion(String canal) {
        List<String> operadores = handler.getDescripcionPromocion(canal);
        if (operadores.size() == 2) operadores.remove(0);
        for (int i = 0; i < operadores.size(); i++) {
            if (operadores.get(i).equalsIgnoreCase("NA")) {
                txtAnterior.setEnabled(false);
                txtActual.setEnabled(false);
                btnCamera.setEnabled(false);
            } else {
                txtAnterior.setEnabled(true);
                txtActual.setEnabled(true);
                btnCamera.setEnabled(true);
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDescripcionPromocion.setAdapter(dataAdapter);
        spDescripcionPromocion.setOnItemSelectedListener(this);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String model = adapterView.getItemAtPosition(i).toString();
    }

    public void inicioPromo() {
        try {
            final Calendar calendar = Calendar.getInstance();
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String outDate = dateFormat.format(date);
                        txtInicioPromo.setText(outDate);

                        if (!txtFinPromo.getText().toString().trim().isEmpty()) {
                            String fInicio = txtInicioPromo.getText().toString();
                            String fFin = txtFinPromo.getText().toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date inicio = sdf.parse(fInicio);
                            Date fin = sdf.parse(fFin);
                            if (inicio.after(fin)) {
                                txtFinPromo.setText("");
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, anio, mes, dia);
            from_dateListener.show();
        } catch (Exception e) {
            System.out.println("INI PROMO: " + e.getMessage());
        }
    }

    public void finPromo() {
        try {
            final Calendar calendar = Calendar.getInstance();
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outDate = dateFormat.format(date);
                    txtFinPromo.setText(outDate);
                }
            }, anio, mes, dia);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter.setLenient(false);
            String oldTime = txtInicioPromo.getText().toString();
            Date oldDate = formatter.parse(oldTime);
            long oldMillis = oldDate.getTime();
            from_dateListener.getDatePicker().setMinDate(oldMillis);
            from_dateListener.show();
        } catch (Exception e) {
            System.out.println("FIN PROMO: " + e.getMessage());
        }
    }

    public String generar_mecanica() {
        String mecanica = mecanica_db;
        String sku = spSKU.getSelectedItem().toString();
        String descripcionPromocion = spDescripcionPromocion.getSelectedItem().toString();
        String producto = txtProducto.getText().toString();
        String pvcAnterior = txtAnterior.getText().toString();
        String pvcActual = txtActual.getText().toString();

        if (descripcionPromocion.toUpperCase().contains("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar la descripción de la promoción", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mecanica.contains("_SKU_") && !sku.toUpperCase().contains("SELECCIONE")) {
            mecanica = mecanica.replace("_SKU_", sku);
        } else if (mecanica.contains("_SKU_")) {
            Toast.makeText(getContext(), "Debe seleccionar el sku", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mecanica.contains("_PRODUCTO_") && !producto.trim().isEmpty()) {
            mecanica = mecanica.replace("_PRODUCTO_", producto);
        } else if (mecanica.contains("_PRODUCTO_")) {
            Toast.makeText(getContext(), "Debe ingresar el producto", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mecanica.contains("_PVC_ANTERIOR_") && !pvcAnterior.trim().isEmpty()) {
            mecanica = mecanica.replace("_PVC_ANTERIOR_", pvcAnterior);
        } else if (mecanica.contains("_PVC_ANTERIOR_")) {
            Toast.makeText(getContext(), "Debe ingresar el pvc anterior", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mecanica.contains("_PVC_ACTUAL_") && !pvcActual.trim().isEmpty()) {
            mecanica = mecanica.replace("_PVC_ACTUAL_", pvcActual);
        } else if (mecanica.contains("_PVC_ACTUAL_")) {
            Toast.makeText(getContext(), "Debe ingresar el pvc actual", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mecanica.contains("_DESCUENTO_")) {
            String margenDscto = obtenerMargenDscto();
            mecanica = mecanica.replace("_DESCUENTO_", margenDscto);
        }

        return mecanica;
    }

    public void insertData(String categoria, String subcategoria, String marca, String canal, String tipo_promocion, String descripcion, String producto,
                           String mecanica, String pvc_anterior, String pvc_actual, String margen_dscto, String sku) {
        try {
            String image = "NO_FOTO";
            if (imageView != null && imageView.getDrawable() != null) {
                obtenerFecha();
                String ciudad = "Ciudad: " + handler.getCityPdv(codigo_pdv);
                String local = "Local: " + punto_venta;
                String usuario = "Usuario: " + user;
                String fechaHora = "Fecha y hora: " + fecha + " " + hora;
                Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ImageMark im = new ImageMark();
                Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
                int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
                image = getStringImage(scaled);
            }

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            ContentValues values = new ContentValues();

            String manufacurer = handler.getManufacterPromocionBySku(sku);
            String plataforma = handler.getPlataformaBySku(sku);
            categoria = handler.getCategoriaBySku(sku);

            values.put(ContractInsertPromocion.Columnas.PHARMA_ID, id_pdv);
            values.put(ContractInsertPromocion.Columnas.CODIGO, codigo_pdv);
            values.put(ContractInsertPromocion.Columnas.USUARIO, user);
            values.put(ContractInsertPromocion.Columnas.SUPERVISOR, punto_venta);
            values.put(ContractInsertPromocion.Columnas.FECHA, fechaser);
            values.put(ContractInsertPromocion.Columnas.HORA, horaser);
            values.put(ContractInsertPromocion.Columnas.CATEGORIA, categoria);
            values.put(ContractInsertPromocion.Columnas.SUBCATEGORIA, "N/A");
            values.put(ContractInsertPromocion.Columnas.MARCA, "N/A");
            values.put(ContractInsertPromocion.Columnas.CANAL, canal);
            values.put(ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION, descripcion);
            values.put(ContractInsertPromocion.Columnas.PRODUCTO, producto);
            values.put(ContractInsertPromocion.Columnas.MECANICA, mecanica);
            values.put(ContractInsertPromocion.Columnas.PVC_ANTERIOR, pvc_anterior);
            values.put(ContractInsertPromocion.Columnas.PVC_ACTUAL, pvc_actual);
            values.put(ContractInsertPromocion.Columnas.MARGEN_DSCTO, margen_dscto);
            values.put(ContractInsertPromocion.Columnas.FOTO, image);
            values.put(ContractInsertPromocion.Columnas.MANUFACTURER, manufacurer);
            values.put(ContractInsertPromocion.Columnas.SKU, sku);
            values.put(ContractInsertPromocion.Columnas.POS_NAME, punto_venta);
            values.put(ContractInsertPromocion.Columnas.PLATAFORMA, plataforma);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContext().getContentResolver().insert(ContractInsertPromocion.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPromocion, null);
                Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean esFormularioValido(String categoria, String subcategoria, String marca, String canal,
                                       String tipo_promocion, String descripcion, String producto, String mecanica,
                                       String s_pvc_anterior, String s_pvc_actual, String margen_dscto, String sku) {

        if (categoria.equalsIgnoreCase("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar una categoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sku.equalsIgnoreCase("SELECCIONE SKU")) {
            Toast.makeText(getContext(), "Debe seleccionar un sku", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descripcion.equalsIgnoreCase("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar una descripción de la promoción", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!descripcion.equalsIgnoreCase("NA")) {

            if (producto.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Debe ingresar un producto", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (s_pvc_anterior.trim().isEmpty() && layoutPVCanterior.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), "Debe ingresar el PVC anterior", Toast.LENGTH_SHORT).show();
                return false;
            }

            double pvc_anterior = Double.parseDouble(s_pvc_anterior);
            if (pvc_anterior > 500 && layoutPVCanterior.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), "El limite máximo del PVC anterior es de $500", Toast.LENGTH_LONG).show();
                return false;
            }

            if (s_pvc_actual.trim().isEmpty() && layoutPVCactual.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), "Debe ingresar PVC actual", Toast.LENGTH_SHORT).show();
                return false;
            }

            double pvc_actual = Double.parseDouble(s_pvc_actual);
            if (pvc_actual > 500 && layoutPVCactual.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), "El limite máximo del PVC actual es de $500", Toast.LENGTH_LONG).show();
                return false;
            }

            if (imageView.getDrawable() == null) {
                Toast.makeText(getContext(), "Debe tomar una foto", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (mecanica_db.contains("_PRODUCTO_") && producto.trim().isEmpty() ||
                    mecanica_db.contains("_PVC_ANTERIOR_") && s_pvc_anterior.trim().isEmpty() ||
                    mecanica_db.contains("_PVC_ACTUAL_") && s_pvc_actual.trim().isEmpty()) {
                Toast.makeText(getContext(), obtenerMensaje(mecanica_db), Toast.LENGTH_SHORT).show();
                return false;
            }

            if ((mecanica_db.contains("_PVC_ANTERIOR_") || mecanica_db.contains("_PVC_ACTUAL_")) &&
                    !s_pvc_anterior.trim().isEmpty() && !s_pvc_actual.trim().isEmpty()) {

                float pvcActualValue = Float.parseFloat(s_pvc_actual);
                float pvcAnteriorValue = Float.parseFloat(s_pvc_anterior);

                if (txtAnterior.isEnabled()) {
                    if (pvcActualValue > pvcAnteriorValue) {
                        Toast.makeText(getContext(), "El pvc actual no puede ser mayor que el pvc anterior", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                if (txtActual.isEnabled()) {
                    if (pvcActualValue == pvcAnteriorValue) {
                        Toast.makeText(getContext(), "El pvc actual no puede ser igual que el pvc anterior", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                if (txtActual.isEnabled()) {
                    if (pvcActualValue == 0) {
                        Toast.makeText(getContext(), "El Pvc Actual no puede ser igual a $0.00", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (txtAnterior.isEnabled()) {
                    if (pvcAnteriorValue == 0) {
                        Toast.makeText(getContext(), "El Pvc Anterior no puede ser igual a $0.00", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

            if (mecanica.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Debe generar la mécanica.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        if ((getContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) return true;
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (!(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    Intent n = new Intent(getContext(), CameraActivity.class);
                    n.putExtra("activity", "promo");
                    startActivity(n);
                } else {
                    if (opciones[i].equals("Cargar Imagen")) {
                        openGallery();
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
    }

    public void obtenerFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);
        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        hora = hour.format(currentLocalTime);
    }

    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (!isCreada) isCreada = fileImagen.mkdirs();
        if (isCreada) nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";

        path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, COD_FOTO);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == spCategoria) {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSKU(categoria, subcategoria, marca, tipo, fabricante);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView == spDescripcionPromocion) {
            try {
                txtAnterior.setEnabled(true);
                txtAnterior.setText("");
                txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));
                txtActual.setEnabled(true);
                txtActual.setText("");
                txtActual.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));
                txtProducto.setText("");
                txtProducto.setEnabled(true);
                txtProducto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));

                descripcionPromocion = adapterView.getItemAtPosition(i).toString();
                tv_Mecanica.setText("");
                mecanica_db = handler.getMecanicaPromo(descripcionPromocion, canal);

                if (descripcionPromocion.equalsIgnoreCase("Descuento en precio") ||
                        descripcionPromocion.equalsIgnoreCase("Cambio de imagen / empaque") ||
                        descripcionPromocion.equalsIgnoreCase("2do a mitad de precio")) {
                    txtProducto.setEnabled(false);
                    txtProducto.setText("N/A");
                    txtProducto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                } else {
                    txtProducto.setEnabled(true);
                    txtProducto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));
                }

                if (descripcionPromocion.equalsIgnoreCase("2do a mitad de precio")) {
                    txtProducto.setEnabled(false);
                    txtProducto.setText("N/A");
                    txtProducto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                    txtAnterior.setEnabled(false);
                    txtAnterior.setText("0");
                    txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                    txtActual.setEnabled(false);
                    txtActual.setText("0");
                    txtActual.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                }

                if (descripcionPromocion.equalsIgnoreCase("Combo Descuento") || descripcionPromocion.equalsIgnoreCase("Combo descuento") || descripcionPromocion.equalsIgnoreCase("Combo dscto")) {
                    txtAnterior.setEnabled(false);
                    txtAnterior.setText("0");
                    txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                }

                if (descripcionPromocion.equalsIgnoreCase("3 x 2")) {
                    txtAnterior.setEnabled(false);
                    txtAnterior.setText("0");
                    txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                }

                if (descripcionPromocion.equalsIgnoreCase("Cambio de imagen / empaque")) {
                    txtProducto.setEnabled(false);
                    txtProducto.setText("N/A");
                    txtProducto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                }

                if (descripcionPromocion.equalsIgnoreCase("Producto gratis") ||
                        descripcionPromocion.equalsIgnoreCase("Promocional gratis") ||
                        descripcionPromocion.equalsIgnoreCase("Canje / Cartilla") ||
                        descripcionPromocion.equalsIgnoreCase("Cupones")) {
                    txtAnterior.setEnabled(false);
                    txtAnterior.setText("0");
                    txtActual.setEnabled(false);
                    txtActual.setText("0");
                    txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                    txtActual.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.editext_disabled));
                }

                estadoCamposFormulario();

                // Después de ajustar los campos según la descripción,
                // intentar autocompletar el PVC anterior con el SKU actual
                if (sku != null && !sku.isEmpty()) {
                    cruzarPvcAnteriorConSku(sku);
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView == spSKU) {
            try {
                sku = adapterView.getItemAtPosition(i).toString();
                spDescripcionPromocion.setSelection(0);
                txtProducto.setText("");
                txtAnterior.setText("");
                txtActual.setText("");
                txtMargenDscto.setText("");
                tv_Mecanica.setText("");

                // AUTOCOMPLETAR PVC ANTERIOR con el último valor registrado para este SKU
                cruzarPvcAnteriorConSku(sku);

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                        scaleImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    scaleImage(bitmap);
                    break;
            }
        }
    }

    public void scaleImage(Bitmap bitmap) {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            hora = hour.format(currentLocalTime);

            int mheight = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            AlertDialog alertDialog1 = new AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t " + e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private void estadoCamposFormulario() {
        txtProducto.setEnabled(true);
        txtAnterior.setEnabled(true);
        txtActual.setEnabled(true);
        txtProducto.setText("");
        txtAnterior.setText("");
        txtActual.setText("");

        if (!mecanica_db.contains("_PRODUCTO_")) {
            txtProducto.setEnabled(false);
            txtProducto.setText("N/A");
        }
        if (!mecanica_db.contains("_PVC_ANTERIOR_")) {
            txtAnterior.setEnabled(false);
            txtAnterior.setText("0");
        }
        if (!mecanica_db.contains("_PVC_ACTUAL_")) {
            txtActual.setEnabled(false);
            txtActual.setText("0");
        }
    }

    private String obtenerMensaje(String mecanica) {
        if (mecanica.contains("_PRODUCTO_")) return "Debe ingresar el producto";
        else if (mecanica.contains("_PVC_ANTERIOR_")) return "Debe ingresar el pvc anterior";
        else if (mecanica.contains("_PVC_ACTUAL_")) return "Debe ingresar el pvc actual";
        return "";
    }

    @Override
    public void onClick(View view) {
        if (view == btnPropia) {
            tipo = "MARCA_PROPIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
        if (view == btnCompetencia) {
            tipo = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
        if (view == btnCamera) {
            cargarImagen();
        }
        if (view == btnGenerarMecanica) {
            String mecanica = generar_mecanica();
            tv_Mecanica.setText(mecanica != null ? mecanica : "");
        }
        if (view == btnIniPromo) {
            inicioPromo();
        }
        if (view == btnFinPromo) {
            String iniPromo = txtInicioPromo.getText().toString();
            if (!iniPromo.trim().isEmpty()) {
                finPromo();
            } else {
                Toast.makeText(getContext(), "Primero debe seleccionar un inicio de promoción", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == btnGuardar) {
            guardar();
        }
    }

    private void guardar() {
        String categoria = spCategoria.getSelectedItem().toString();
        String descricion_promocion = spDescripcionPromocion.getSelectedItem().toString();
        String producto = txtProducto.getText().toString();
        String pvc_anterior = txtAnterior.getText().toString().trim();
        String pvc_actual = txtActual.getText().toString().trim();
        String margen_dscto = txtMargenDscto.getText().toString().trim();
        String sku = spSKU.getSelectedItem().toString().trim();
        mecanica = tv_Mecanica.getText().toString();

        if (esFormularioValido(categoria, subcategoria, marca, canal, tipo_promocion, descricion_promocion, producto, mecanica, pvc_anterior, pvc_actual, margen_dscto, sku)) {
            insertData(categoria, subcategoria, marca, canal, tipo_promocion, descricion_promocion, producto, mecanica, pvc_anterior, pvc_actual, margen_dscto, sku);
            limpiarDatos();
        } else {
            tv_Mecanica.setText("");
        }
    }

    private void limpiarDatos() {
        spCategoria.setSelection(0);
        spSubcategoria.setSelection(0);
        spMarca.setSelection(0);
        spSKU.setSelection(0);
        spDescripcionPromocion.setSelection(0);
        txtAnterior.setText("");
        txtAnterior.setEnabled(true);
        txtActual.setText("");
        txtActual.setEnabled(true);
        txtProducto.setText("");
        txtProducto.setEnabled(true);
        txtMargenDscto.setText("");
        tv_Mecanica.setText("");
        imageView.setImageResource(0);
        txtAnterior.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));
        txtActual.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_background));
    }

    @Override
    public void onFocusChange(View view, boolean b) {}

    private String obtenerMargenDscto() {
        String margenDscto = "0";
        if (!txtAnterior.getText().toString().isEmpty() && !txtActual.getText().toString().isEmpty()) {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.FLOOR);
            Double pvcAnterior = Double.parseDouble(txtAnterior.getText().toString());
            Double pvcActual = Double.parseDouble(txtActual.getText().toString());
            margenDscto = String.valueOf(Math.round((pvcActual / pvcAnterior - 1) * 100));
            txtMargenDscto.setText(margenDscto + '%');
        }
        return margenDscto + '%';
    }
}