package com.luckyecuador.app.PintucoAPP.ui.precios;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Clase.Precio;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.DigitsInputFilter;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class PreciosFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    ArrayList<Precio> type_name_copy = new ArrayList<Precio>();
    ArrayList<Precio> sesion = new ArrayList<Precio>();
    private Spinner spCategoria;
    private Spinner spSubcategoria;

    public static ArrayList<Base_precio_pvc> productos;
    private Spinner spFabricante;
    private Spinner spMarca;
    private EditText txtSKUCode;
    private EditText txtDescripcion;
    private Button btnPropia;
    private Button btnCompetencia;

    public static List<String> valores;
    public static List<String> valores2;

    private String categoria, subcategoria, segmento1, segmento2, brand, antes2, tamano, cantidad, codigo, descripcion;
    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora;

    private final String modulo = "PRECIOS";
    private final boolean HABILITAR_SYNC_PVC = true;

    private SwipeRefreshLayout swipeRefreshLayout;

    public AlertDialog adMsj;

    DatabaseHelper handler;

    EditText txtsearch;
    TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    ListView listview;

    public ArrayList<Precio> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    CustomAdapterPrecios dataAdapter;

    private EditText txtventas, txtpregular, txtppromocion, txtcuotas, txtvcuotas;
    private String fechaventas, producto, pregular, ppromocion, sku, cuotas, vcuotas, format, presentacion, tipo_precio;
    private String poferta;
    private String pvm;
    private String ciudad, retail, sucursal;
    private String canal, subcanal;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    ProgressDialog progressDialog;

    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private final String CARPETA_RAIZ = "SonyApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Ventas";
    String path;
    Button btnCamera;

    MarshMallowPermission marshMallowPermission;

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private String tipo = "MARCA_PROPIA";
    private final String fabricante = "AkzoNobel S.A";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_precios, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        LoadData();
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();
        marshMallowPermission = new MarshMallowPermission(getActivity());

        valores = new ArrayList<>();
        valores2 = new ArrayList<>();
        productos = new ArrayList<>();

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);

        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);
        btnPropia = (Button) rootView.findViewById(R.id.btnPropia);
        btnCompetencia = (Button) rootView.findViewById(R.id.btnCompetencia);
        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_precios_title_2, null, false);
        listview.addHeaderView(headerView, null, false);

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        consultaGuardado();

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (brand != null && categoria != null && !brand.isEmpty() && !categoria.isEmpty()) {
                    sincronizarUltimoPvc();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        filtrarCategoria(tipo, fabricante);

        if (HABILITAR_SYNC_PVC) {
            sincronizarUltimoPvc();
        }

        ListView listView = (ListView) rootView.findViewById(R.id.lvSKUCode);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnPropia.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Precios");

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void resetFragmentState() {
        spCategoria.setSelection(0);
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME PRECIOS");
        new DeveloperOptions().modalDevOptions(getActivity());
        // Refrescar datos al volver para sincronizar con cambios en Ventas
        if (brand != null && categoria != null) {
            productos = handler.getUltimoPvcDesdeInsertPrecios(user, codigo_pdv, categoria, brand);
            showListView(categoria, subcategoria, brand, tipo, fabricante);
        }
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
    }

    private void sincronizarUltimoPvc() {
        Log.i("PVC_SYNC", "1. Iniciando sincronizarUltimoPvc...");
        Log.i("PVC_SYNC", "2. Usuario: " + user + " | Codigo PDV: " + codigo_pdv);

        if (!VerificarNet.hayConexion(getContext())) {
            Log.e("PVC_SYNC", "Sin conexión.");
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            return;
        }

        Log.i("PVC_SYNC", "3. Conexión OK.");

        HashMap<String, String> map = new HashMap<>();
        map.put("usuario", user);
        map.put("codigo", codigo_pdv);
        JSONObject jobject = new JSONObject(map);

        Log.i("PVC_SYNC", "4. URL: " + Constantes.GET_ULTIMO_PVC);
        Log.i("PVC_SYNC", "5. Payload: " + jobject.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                Constantes.GET_ULTIMO_PVC, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("PVC_SYNC", "6. Respuesta: " + response.toString());
                        try {
                            if (response.has(Constantes.ULTIMO_PVC_RESULT)) {
                                JSONArray array = response.getJSONArray(Constantes.ULTIMO_PVC_RESULT);
                                Log.i("PVC_SYNC", "7. Registros: " + array.length());
                                handler.guardarUltimoPvcLocal(array, user, codigo_pdv);
                                Log.i("PVC_SYNC", "8. Sincronización completada.");

                                if (brand != null && categoria != null && !brand.isEmpty() && !categoria.isEmpty()) {
                                    productos = handler.getUltimoPvcDesdeInsertPrecios(user, codigo_pdv, categoria, brand);
                                    showListView(categoria, subcategoria, brand, tipo, fabricante);
                                    Log.i("PVC_SYNC", "9. Lista refrescada.");
                                }
                            } else {
                                Log.e("PVC_SYNC", "JSON sin clave: " + Constantes.ULTIMO_PVC_RESULT);
                            }
                        } catch (JSONException e) {
                            Log.e("PVC_SYNC", "Error JSON: " + e.getMessage());
                        } finally {
                            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PVC_SYNC", "ERROR VOLLEY: " + error.getMessage());
                        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
        Log.i("PVC_SYNC", "Petición agregada a Volley.");
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        List<String> operadores = handler.getCategoriaPrecios(tipo, fabricante, codigo_pdv, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void filtrarCodigoSKU() {}

    public void filtrarSubcategoria(String categoria, String tipo, String fabricante) {
        List<String> operadores = handler.getSubcategoriaPrecios(categoria, tipo, fabricante, codigo_pdv, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String categoria, String subcategoria, String tipo, String fabricante) {
        List<String> operadores = handler.getMarcaPrecios(categoria, subcategoria, tipo, fabricante, codigo_pdv, canal, subcanal, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    public void filtrarFabricante(String categoria, String subcategoria, String marca, String tipo, String fabricante) {
        List<String> operadores = handler.getFabricantePrecios(categoria, subcategoria, marca, tipo, fabricante, codigo_pdv, canal, subcanal);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFabricante.setAdapter(dataAdapter);
        spFabricante.setOnItemSelectedListener(this);
    }

    public void consultaGuardado() {
        sesion = handler.getListGuardadoPrecios(codigo_pdv);
        for (int i = 0; i < sesion.size(); i++) {
            Log.i("INFO", sesion.get(i).getSku_code() + " " + sesion.get(i).getPvp() + " " + sesion.get(i).getPvc());
        }
    }

    public void showListView(String categoria, String subcategoria, String brand, String tipo, String fabricante) {
        listProductos = handler.filtrarListProductos2Precios(categoria, brand, tipo, fabricante, codigo_pdv, canal, subcanal, modulo);

        if (productos != null && listProductos != null) {
            for (Precio p : listProductos) {
                for (Base_precio_pvc bp : productos) {
                    if (p.getSku_code().trim().equalsIgnoreCase(bp.getSku().trim())) {
                        p.setPvc(bp.getPvc() != null ? bp.getPvc().trim() : "");
                        Log.i("PVC_CRUCE", "Match: " + p.getSku_code() + " → PVC=" + p.getPvc());
                        break;
                    }
                }
            }
        }

        dataAdapter = new CustomAdapterPrecios(getContext(), listProductos);
        if (!dataAdapter.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);

            txtSKUCode.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(txtSKUCode.getText().toString());
                }
                @Override public void afterTextChanged(Editable s) {
                    ArrayList<Precio> type_name_filter = new ArrayList<Precio>();
                    String text = s.toString();
                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getSku_code().toLowerCase()).contains(text.toLowerCase())) {
                            Precio p = new Precio();
                            p.setSku_code(listProductos.get(i).getSku_code());
                            p.setPvp(listProductos.get(i).getPvp());
                            type_name_filter.add(p);
                        }
                    }
                    type_name_copy = type_name_filter;
                    listUpdate(type_name_copy);
                }
            });
        } else {
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<Precio> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterPrecios(getContext(), data));
        } else {
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spCategoria) {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarMarca(categoria, subcategoria, tipo, fabricante);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView == spMarca) {
            try {
                brand = adapterView.getItemAtPosition(i).toString();
                Log.i("PVC_DEBUG", "user=" + user + " codigo=" + codigo_pdv + " categoria=" + categoria + " brand=" + brand);
                productos = handler.getUltimoPvcDesdeInsertPrecios(user, codigo_pdv, categoria, brand);
                Log.i("PVC_DEBUG", "productos.size()=" + productos.size());
                showListView(categoria, subcategoria, brand, tipo, fabricante);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String model = adapterView.getItemAtPosition(i).toString();
        alertDialog(model);
    }

    public void alertDialog(final String skuSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_precios, null);
        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle("Precios");
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_precios, null));

        TextView lblModelo = (TextView) dialogView.findViewById(R.id.lblModelo);
        txtpregular = (EditText) dialogView.findViewById(R.id.txtPcont);
        txtppromocion = (EditText) dialogView.findViewById(R.id.txtPcredito);

        lblModelo.setText(getString(R.string.model) + " :    " + skuSelected + "\n");

        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        pregular = txtpregular.getText().toString().trim();
                        ppromocion = txtppromocion.getText().toString().trim();

                        if (pregular.isEmpty()) {
                            Toast.makeText(getContext(), "No ingresaste el precio regular", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (ppromocion.isEmpty()) {
                            Toast.makeText(getContext(), "No ingresaste el precio promoción (PVC)", Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            double precioPVC = Double.parseDouble(ppromocion.replace(",", "."));
                            boolean rangoValidado = true;

                            BaseRangosPreciosSku rangoEspecial = handler.getRangoPreciosBySku(skuSelected);
                            if (rangoEspecial != null) {
                                double precioRnd = Math.round(precioPVC * 100.0) / 100.0;
                                double minimoRnd = Math.round(rangoEspecial.getMinimo() * 100.0) / 100.0;
                                double maximoRnd = Math.round(rangoEspecial.getMaximo() * 100.0) / 100.0;
                                if (precioRnd < minimoRnd || precioRnd > maximoRnd) {
                                    rangoValidado = false;
                                }
                            } else {
                                ArrayList<BaseRangosPrecios> rangos = handler.getAllRangosPrecios();
                                for (BaseRangosPrecios rango : rangos) {
                                    if (skuSelected.toLowerCase().contains(rango.getPresentacion().toLowerCase())) {
                                        double precioRnd = Math.round(precioPVC * 100.0) / 100.0;
                                        double minimoRnd = Math.round(rango.getMinimo() * 100.0) / 100.0;
                                        double maximoRnd = Math.round(rango.getMaximo() * 100.0) / 100.0;
                                        if (precioRnd < minimoRnd || precioRnd > maximoRnd) {
                                            rangoValidado = false;
                                        }
                                        break;
                                    }
                                }
                            }

                            if (rangoValidado) {
                                insertData(skuSelected, pregular, ppromocion, "N/A", "N/A", "MARCA_PROPIA");
                            } else {
                                showGenericAlert("Precio Fuera De Escala", "Validar Precio Ingresado");
                            }

                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Ingrese un precio válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        builder.setNeutralButton(R.string.cancel, null);
        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(Color.WHITE);
        pButton.setPadding(4, 2, 4, 2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        cButton.setTextColor(Color.WHITE);
        cButton.setPadding(4, 2, 4, 2);
    }

    public void showGenericAlert(String titulo, String descripcion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_generico, null);
        builder.setView(dialogView);

        TextView tvTitulo = dialogView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = dialogView.findViewById(R.id.tvDescripcion);
        Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);

        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);

        final AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void insertData(String skuSelected, String pregular, String ppromocion, String poferta, String pvm, String tipo) {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            ContentValues values = new ContentValues();

            String categoria = spCategoria.getSelectedItem().toString();
            String brand = spMarca.getSelectedItem().toString();
            String manufacturer = handler.getManufacturerPrecios(categoria, "", brand, skuSelected);
            String plataforma = handler.getPlataformaBySku(skuSelected);

            values.put(ContractInsertPrecios.Columnas.PHARMA_ID, id_pdv);
            values.put(ContractInsertPrecios.Columnas.CODIGO, codigo_pdv);
            values.put(ContractInsertPrecios.Columnas.USUARIO, user);
            values.put(ContractInsertPrecios.Columnas.SUPERVISOR, punto_venta);
            values.put(ContractInsertPrecios.Columnas.FECHA, fechaser);
            values.put(ContractInsertPrecios.Columnas.HORA, horaser);
            values.put(ContractInsertPrecios.Columnas.CATEGORIA, categoria);
            values.put(ContractInsertPrecios.Columnas.SUBCATEGORIA, "N/A");
            values.put(ContractInsertPrecios.Columnas.PRESENTACION, fabricante);
            values.put(ContractInsertPrecios.Columnas.POFERTA, poferta);
            values.put(ContractInsertPrecios.Columnas.BRAND, brand);
            values.put(ContractInsertPrecios.Columnas.SKU_CODE, skuSelected);
            values.put(ContractInsertPrecios.Columnas.PREGULAR, "N/A");
            values.put(ContractInsertPrecios.Columnas.PPROMOCION, ppromocion);
            values.put(ContractInsertPrecios.Columnas.MANUFACTURER, manufacturer);
            values.put(ContractInsertPrecios.Columnas.POS_NAME, punto_venta);
            values.put(ContractInsertPrecios.Columnas.PLATAFORMA, plataforma);
            values.put(ContractInsertPrecios.Columnas.PVM, "N/A");
            values.put(ContractInsertPrecios.Columnas.TIPO, "N/A");
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContext().getContentResolver().insert(ContractInsertPrecios.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPrecio, null);
                Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dia + "/" + (mes + 1) + "/" + año);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);
        txtfechav.setText(outDate);
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
                    tomarFotografia();
                } else {
                    if (opciones[i].equals("Cargar Imagen")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (!isCreada) isCreada = fileImagen.mkdirs();
        if (isCreada) nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";

        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    imageView.setImageURI(miPath);
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

    private boolean validarPVP(String pregular) {
        return !pregular.equals(".") && !pregular.equals("0") && !pregular.equals("00") &&
                !pregular.equals("0.0") && !pregular.equals("0.00") && !pregular.equals("00.0") &&
                !pregular.equals("00.00") && !pregular.equals("0.");
    }

    private boolean validarPVC(String ppromocion) {
        return !ppromocion.equals(".") && !ppromocion.equals("0") && !ppromocion.equals("00") &&
                !ppromocion.equals("0.0") && !ppromocion.equals("0.00") && !ppromocion.equals("00.0") &&
                !ppromocion.equals("00.00") && !ppromocion.equals("0.");
    }

    public void scaleImage(Bitmap bitmap) {
        try {
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

    @Override
    public void onClick(View v) {
        if (v == btnPropia) {
            tipo = "MARCA_PROPIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
        if (v == btnCompetencia) {
            tipo = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
    }

    public void consultarPvc() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", user);
            JSONObject jobject = new JSONObject(map);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "https://webecuador.azurewebsites.net/App/AppPintuco/Web/get_productos_pvc.php?usuario=" + user, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            respuestaPvc(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError) {
                                Toast.makeText(getContext(), Mensajes.TIME_OUT, Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError) {
                                Toast.makeText(getContext(), Mensajes.NO_RED, Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getContext(), Mensajes.SEVER_ERROR, Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getContext(), Mensajes.RED_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void respuestaPvc(JSONObject response) {
        if (response != null) {
            try {
                String estado = response.getString("estado");
                switch (estado) {
                    case "1":
                        productos = new ArrayList<>();
                        JSONArray mensaje = response.getJSONArray("productos_pvc");
                        for (int i = 0; i < mensaje.length(); i++) {
                            JSONObject jb1 = mensaje.getJSONObject(i);
                            Base_precio_pvc precio_pvc = new Base_precio_pvc();
                            precio_pvc.setUsuario(jb1.getString("usuario"));
                            precio_pvc.setCodigo_pdv(jb1.getString("codigo_pdv"));
                            precio_pvc.setCategoria(jb1.getString("categoria"));
                            precio_pvc.setSubcategoria(jb1.getString("subcategoria"));
                            precio_pvc.setMarca(jb1.getString("marca"));
                            precio_pvc.setSku(jb1.getString("sku"));
                            precio_pvc.setPvc(jb1.getString("pvc"));
                            productos.add(precio_pvc);
                        }
                        break;
                    case "2":
                        String mensaje2 = response.getString("mensaje");
                        Toast.makeText(getContext(), mensaje2, Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "VACIO", Toast.LENGTH_LONG).show();
        }
    }

    public class CustomAdapterPrecios extends ArrayAdapter<Precio> implements Filterable {

        public ArrayList<Precio> values;
        public Context context;
        boolean[] checkBoxState;
        boolean[] editedState;

        private void ajustarTamanioTexto(EditText et, String texto) {
            String parteEntera = texto.replace("$", "").split("\\.")[0];
            float size = parteEntera.length() >= 3 ? 12f : 14f;
            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }

        public CustomAdapterPrecios(Context context, ArrayList<Precio> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState = new boolean[values.size()];
            editedState = new boolean[values.size()];
        }

        public class ViewHolder {
            TextView lblSku;
            AppCompatCheckBox checkGuardar;
            EditText txt_precio_regular;
            EditText txt_precio_promocion;
            EditText txt_precio_oferta;
            Spinner spTipos;
            EditText txt_pvm;
            Spinner sp;
        }

        @Override public int getViewTypeCount() { return values.size(); }
        @Override public int getItemViewType(int position) { return position; }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder vHolder = null;

            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_precios_2, parent, false);

                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.checkGuardar = (AppCompatCheckBox) convertView.findViewById(R.id.checkGuardar);
                vHolder.txt_precio_regular = (EditText) convertView.findViewById(R.id.txt_precio_regular);
                vHolder.txt_precio_promocion = (EditText) convertView.findViewById(R.id.txt_precio_promocion);
                vHolder.txt_precio_oferta = (EditText) convertView.findViewById(R.id.txt_precio_oferta);
                vHolder.txt_pvm = (EditText) convertView.findViewById(R.id.txt_pvm);
                vHolder.spTipos = (Spinner) convertView.findViewById(R.id.spTipos);

                ColorStateList colorStateList = getResources().getColorStateList(R.color.checkbox_tint, null);
                CompoundButtonCompat.setButtonTintList(vHolder.checkGuardar, colorStateList);

                List<String> operadores2 = handler.getTipoPrecios(subcanal);
                if (operadores2.size() == 2) operadores2.remove(0);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores2);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vHolder.spTipos.setAdapter(dataAdapter);

                InputFilter filter = new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String textActual = dest.toString();
                        String textoResultante = textActual.substring(0, dstart) + source.toString() + textActual.substring(dend);
                        String[] parts = textoResultante.replace("$", "").split("\\.");
                        if (parts.length > 0 && parts[0].length() > 3) return "";
                        if (parts.length > 1 && parts[1].length() > 2) return "";
                        return null;
                    }
                };

                vHolder.txt_precio_promocion.setFilters(new InputFilter[]{filter});
                vHolder.txt_precio_oferta.setFilters(new InputFilter[]{filter});

                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            if (values.size() > 0) {
                vHolder.lblSku.setText(values.get(position).getSku_code());
                vHolder.txt_precio_regular.setText(values.get(position).getPvp().trim());

                if (vHolder.txt_precio_promocion.getTag() instanceof TextWatcher) {
                    vHolder.txt_precio_promocion.removeTextChangedListener((TextWatcher) vHolder.txt_precio_promocion.getTag());
                }

                String currentPvc = values.get(position).getPvc();
                if (currentPvc != null && !currentPvc.isEmpty()) {
                    try {
                        double val = Double.parseDouble(currentPvc.replace(",", "."));
                        currentPvc = decimalFormat.format(val).replace(",", ".");
                    } catch (Exception ignored) {}
                }
                final int pos = position;
                final ViewHolder finalV = vHolder;

                if (!editedState[pos]) {
                    vHolder.txt_precio_promocion.setText("");
                    vHolder.txt_precio_promocion.setHint(currentPvc != null && !currentPvc.isEmpty() ? "$" + currentPvc : "$");
                    vHolder.txt_precio_promocion.setHintTextColor(Color.parseColor("#828282"));
                    values.get(pos).setPvc(""); // Consider it empty for validation if not edited
                } else {
                    vHolder.txt_precio_promocion.setText("$" + currentPvc);
                    vHolder.txt_precio_promocion.setTextColor(Color.BLACK);
                }
                ajustarTamanioTexto(vHolder.txt_precio_promocion, vHolder.txt_precio_promocion.getText().toString());

                TextWatcher tw = new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        if (str.isEmpty()) {
                            editedState[pos] = false;
                            values.get(pos).setPvc("");
                        } else {
                            editedState[pos] = true;
                            finalV.txt_precio_promocion.setTextColor(Color.BLACK);
                            if (!str.startsWith("$")) {
                                finalV.txt_precio_promocion.removeTextChangedListener(this);
                                String clean = str.replace("$", "");
                                finalV.txt_precio_promocion.setText("$" + clean);
                                finalV.txt_precio_promocion.setSelection(finalV.txt_precio_promocion.getText().length());
                                finalV.txt_precio_promocion.addTextChangedListener(this);
                                values.get(pos).setPvc(clean);
                            } else {
                                values.get(pos).setPvc(str.replace("$", ""));
                            }
                        }
                    }
                };
                vHolder.txt_precio_promocion.addTextChangedListener(tw);
                vHolder.txt_precio_promocion.setTag(tw);

                vHolder.txt_precio_promocion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            String str = finalV.txt_precio_promocion.getText().toString();
                            if (!str.isEmpty()) {
                                String clean = str.replace("$", "");
                                try {
                                    double val = Double.parseDouble(clean.replace(",", "."));
                                    String formatted = "$" + decimalFormat.format(val).replace(",", ".");
                                    finalV.txt_precio_promocion.removeTextChangedListener((TextWatcher) finalV.txt_precio_promocion.getTag());
                                    finalV.txt_precio_promocion.setText(formatted);
                                    finalV.txt_precio_promocion.addTextChangedListener((TextWatcher) finalV.txt_precio_promocion.getTag());
                                    ajustarTamanioTexto(finalV.txt_precio_promocion, formatted);
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                });

                // Configuración para txt_precio_oferta (sin historial por ahora, solo prefix)
                if (vHolder.txt_precio_oferta.getTag() instanceof TextWatcher) {
                    vHolder.txt_precio_oferta.removeTextChangedListener((TextWatcher) vHolder.txt_precio_oferta.getTag());
                }
                vHolder.txt_precio_oferta.setText("");
                vHolder.txt_precio_oferta.setHint("$");
                vHolder.txt_precio_oferta.setHintTextColor(Color.parseColor("#BDBDBD"));
                ajustarTamanioTexto(vHolder.txt_precio_oferta, "");

                TextWatcher twOferta = new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        if (str.isEmpty()) {
                            // Dejar vacío para mostrar hint
                        } else if (!str.startsWith("$")) {
                            finalV.txt_precio_oferta.removeTextChangedListener(this);
                            String clean = str.replace("$", "");
                            finalV.txt_precio_oferta.setText("$" + clean);
                            finalV.txt_precio_oferta.setSelection(finalV.txt_precio_oferta.getText().length());
                            finalV.txt_precio_oferta.addTextChangedListener(this);
                        }
                    }
                };
                vHolder.txt_precio_oferta.addTextChangedListener(twOferta);
                vHolder.txt_precio_oferta.setTag(twOferta);

                vHolder.txt_precio_oferta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            String str = finalV.txt_precio_oferta.getText().toString();
                            if (!str.isEmpty()) {
                                String clean = str.replace("$", "");
                                try {
                                    double val = Double.parseDouble(clean.replace(",", "."));
                                    String formatted = "$" + decimalFormat.format(val).replace(",", ".");
                                    finalV.txt_precio_oferta.removeTextChangedListener((TextWatcher) finalV.txt_precio_oferta.getTag());
                                    finalV.txt_precio_oferta.setText(formatted);
                                    finalV.txt_precio_oferta.addTextChangedListener((TextWatcher) finalV.txt_precio_oferta.getTag());
                                    ajustarTamanioTexto(finalV.txt_precio_oferta, formatted);
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                });

                sesion = handler.getListGuardadoPrecios2(codigo_pdv);
                for (int i = 0; i < sesion.size(); i++) {
                    if (values.get(position).getSku_code().equals(sesion.get(i).getSku_code())) {
                        vHolder.lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                        vHolder.lblSku.setTypeface(null, Typeface.BOLD);
                    }
                }

                final ViewHolder finalv = vHolder;

                finalv.spTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (finalv.spTipos.getSelectedItem().toString().equals("N/A")) {
                            finalv.txt_pvm.setText("");
                            finalv.txt_pvm.setEnabled(false);
                        } else {
                            finalv.txt_pvm.setEnabled(true);
                        }
                    }
                    @Override public void onNothingSelected(AdapterView<?> parent) {}
                });

                vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (productos.size() > 0) {
                            for (Base_precio_pvc precio_pvc : productos) {
                                if (precio_pvc.getSku().trim().equals(finalv.lblSku.getText().toString().trim())) {
                                    if (!precio_pvc.getPvc().trim().equals(finalv.txt_precio_promocion.getText().toString())) {
                                        antes2 = precio_pvc.getPvc();
                                    } else {
                                        antes2 = null;
                                    }
                                }
                            }
                        }

                        pregular = finalv.txt_precio_regular.getText().toString().trim().replace("$", "");
                        ppromocion = finalv.txt_precio_promocion.getText().toString().trim().replace("$", "");
                        poferta = finalv.txt_precio_oferta.getText().toString().replace("$", "");
                        pvm = finalv.txt_pvm.getText().toString().replace("$", "");
                        sku = finalv.lblSku.getText().toString().trim();
                        String contenido = values.get(position).getContenido();
                        tipo_precio = finalv.spTipos.getSelectedItem().toString();

                        String originalPvc = "";
                        if (productos != null) {
                            for (Base_precio_pvc p : productos) {
                                if (p.getSku().trim().equals(sku)) {
                                    originalPvc = p.getPvc().trim();
                                    break;
                                }
                            }
                        }

                        if (((CheckBox) v).isChecked()) {
                            if (esFormularioValido(sku, contenido, pregular, ppromocion, poferta, pvm, tipo_precio, originalPvc)) {
                                if (antes2 != null) {
                                    alertDialogantes();
                                } else {
                                    insertData(sku, pregular, ppromocion, poferta, pvm, tipo_precio);
                                    finalv.lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                                    antes2 = null;
                                }
                            } else {
                                finalv.checkGuardar.setChecked(false);
                            }
                        }
                    }

                    private void alertDialogantes() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater2 = getLayoutInflater();
                        View dialogView = inflater2.inflate(R.layout.alertdialog_pvc_pvp, null);
                        builder.setView(dialogView);
                        TextView tv = (TextView) dialogView.findViewById(R.id.num);
                        String formatAntes = antes2;
                        if (formatAntes != null && !formatAntes.startsWith("$")) {
                            formatAntes = "$" + formatAntes;
                        }
                        tv.setText(formatAntes);
                        Button btn = (Button) dialogView.findViewById(R.id.boton);
                        Button btnCancelar = (Button) dialogView.findViewById(R.id.btnCancelar);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                insertData(sku, pregular, ppromocion, poferta, pvm, tipo_precio);
                                antes2 = null;
                                adMsj.dismiss();
                                adMsj = null;
                                finalv.lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                            }
                        });

                        btnCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adMsj.dismiss();
                                adMsj = null;
                                antes2 = null;
                                finalv.checkGuardar.setChecked(false);
                            }
                        });

                        adMsj = builder.create();
                        adMsj.show();
                    }
                });
            }
            return convertView;
        }

        private boolean esFormularioValido(String skuSelected, String contenido, String s_pvp, String s_pvc, String s_oferta, String s_pvm, String tipo, String originalPvc) {

            if (s_pvc.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el Precio PVC", Toast.LENGTH_LONG).show();
                return false;
            }
            if (s_pvc.trim().equals("0") || s_pvc.trim().equals("0.0") || s_pvc.trim().equals("0.00")) {
                Toast.makeText(getContext(), "ERROR: El precio PVC no puede ser 0.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (s_oferta.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el Precio oferta", Toast.LENGTH_LONG).show();
                return false;
            }
            if (s_pvc.startsWith(".") || s_oferta.startsWith(".")) {
                Toast.makeText(getContext(), "Formato de precio inválido (no puede empezar con punto)", Toast.LENGTH_LONG).show();
                return false;
            }

            double pvc, oferta;
            try {
                pvc = Double.parseDouble(s_pvc.replace(",", "."));
                oferta = Double.parseDouble(s_oferta.replace(",", "."));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Precio ingresado no válido", Toast.LENGTH_LONG).show();
                return false;
            }

            if (pvc <= 0) {
                Toast.makeText(getContext(), "El PVC debe ser mayor a 0", Toast.LENGTH_LONG).show();
                return false;
            }

            boolean fueEditado = !s_pvc.trim().equals(originalPvc.trim());

            if (fueEditado) {
                if (pvc > 500 || oferta > 500) {
                    Toast.makeText(getContext(), "El límite máximo permitido es $500", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (oferta > pvc) {
                    Toast.makeText(getContext(), "El Precio oferta no puede ser mayor al PVC", Toast.LENGTH_LONG).show();
                    return false;
                }

                // VALIDACIÓN SOLO EN PVC
                try {
                    BaseRangosPreciosSku rangoEspecial = handler.getRangoPreciosBySku(skuSelected);
                    if (rangoEspecial != null) {
                        double pvcRnd = Math.round(pvc * 100.0) / 100.0;
                        double minimoRnd = Math.round(rangoEspecial.getMinimo() * 100.0) / 100.0;
                        double maximoRnd = Math.round(rangoEspecial.getMaximo() * 100.0) / 100.0;
                        if (pvcRnd < minimoRnd || pvcRnd > maximoRnd) {
                            showGenericAlert("Precio Fuera De Escala", "Validar Precio Ingresado");
                            return false;
                        }
                    } else {
                        ArrayList<BaseRangosPrecios> rangos = handler.getAllRangosPrecios();
                        for (BaseRangosPrecios rango : rangos) {
                            if (contenido != null && contenido.toLowerCase().contains(rango.getPresentacion().toLowerCase())) {
                                double pvcRnd = Math.round(pvc * 100.0) / 100.0;
                                double minimoRnd = Math.round(rango.getMinimo() * 100.0) / 100.0;
                                double maximoRnd = Math.round(rango.getMaximo() * 100.0) / 100.0;
                                if (pvcRnd < minimoRnd || pvcRnd > maximoRnd) {
                                    showGenericAlert("Precio Fuera De Escala", "Validar Precio Ingresado");
                                    return false;
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("PreciosFragment", "Error en validación de rangos: " + e.getMessage());
                }
            } else {
                Log.i("PreciosFragment", "Precio no editado, se permite guardar el valor original.");
            }

            return true;
        }
    }
}