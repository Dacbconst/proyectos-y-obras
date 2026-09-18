package com.luckyecuador.app.PintucoAPP.ui.ventas;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import static androidx.appcompat.content.res.AppCompatResources.getColorStateList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DecimalFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.FileProvider;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Clase.Ventas;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class VentasFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    ArrayList<Ventas> type_name_copy = new ArrayList<Ventas>();
    ArrayList<Ventas> sesion = new ArrayList<Ventas>();
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spMarca;
    private EditText txtSKUCode;
    private EditText txtDescripcion;

    private final boolean HABILITAR_SYNC_VENTAS = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String categoria, subcategoria, marca;
    private String id_pdv, user, codigo_pdv, punto_venta, supervisor, usuario, fecha, hora, canal, subcanal;

    DatabaseHelper handler;

    EditText txtNomImp;
    ImageButton btnFecha;
    TextView empty;
    ListView listview;

    public ArrayList<Ventas> listProductos;
    CustomAdapterVentas dataAdapter;

    private EditText txtFechaVenta;
    private String fechaventas, nombImp, cantidad, tipoVenta, pregular, ppromocion, vstock_inicial, vstock_final, poferta, sku, format, cuenta;

    private String tipo = "MARCA_PROPIA";

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;
    LinearLayout linearLayoutImpulso;

    public static ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private final String CARPETA_RAIZ = "SonyApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Ventas";
    String path;
    private final String fabricante = "AkzoNobel S.A";
    private final String modulo = "VENTAS";

    MarshMallowPermission marshMallowPermission;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ventas, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();
        marshMallowPermission = new MarshMallowPermission(getActivity());

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        LoadData();
        new GuardarLog(getContext()).saveLog(user, codigo_pdv, "Ingreso a Ventas");
        marshMallowPermission = new MarshMallowPermission(getActivity());

        txtNomImp = (EditText) rootView.findViewById(R.id.txtNombreImp);
        txtFechaVenta = (EditText) rootView.findViewById(R.id.txtFecha);
        btnFecha = (ImageButton) rootView.findViewById(R.id.btnFecha);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);
        linearLayoutImpulso = (LinearLayout) rootView.findViewById(R.id.linearLayoutImpulso);
        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);
        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);
        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (marca != null && categoria != null && !marca.isEmpty() && !categoria.isEmpty()) {
                    sincronizarUltimoPrecioVentas();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFechaVenta.setText(sdf.format(Calendar.getInstance().getTime()));
        txtFechaVenta.setFocusable(false);
        txtFechaVenta.setClickable(true);
        txtFechaVenta.setCursorVisible(false);
        txtFechaVenta.setKeyListener(null);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarDatePickerConRangoSemanal(); }
        });
        txtFechaVenta.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarDatePickerConRangoSemanal(); }
        });

        Log.i("LOG", "fab: " + fabricante);
        filtrarCategoria();

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return rootView;
    }

    private void sincronizarUltimoPrecioVentas() {
        Log.i("VENTAS_SYNC", "1. Iniciando sincronizarUltimoPrecioVentas...");

        if (!VerificarNet.hayConexion(getContext())) {
            Log.e("VENTAS_SYNC", "Sin conexión.");
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            return;
        }

        try {
            JSONObject jobject = new JSONObject();
            jobject.put("usuario", user);
            jobject.put("codigo", codigo_pdv);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constantes.GET_ULTIMO_PRECIO_VENTAS,
                    jobject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.has(Constantes.ULTIMO_PRECIO_VENTAS_RESULT)) {
                                    JSONArray array = response.getJSONArray(Constantes.ULTIMO_PRECIO_VENTAS_RESULT);
                                    handler.guardarUltimoPrecioVentasLocal(array, user, codigo_pdv);
                                    if (marca != null && categoria != null && !marca.isEmpty() && !categoria.isEmpty()) {
                                        showListView(fabricante, categoria, marca);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e("VENTAS_SYNC", "Error JSON: " + e.getMessage());
                            } finally {
                                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VENTAS_SYNC", "Error Volley: " + error.getMessage());
                            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

        } catch (JSONException e) {
            Log.e("VENTAS_SYNC", "Error JSON: " + e.getMessage());
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void mostrarDatePickerConRangoSemanal() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        txtFechaVenta.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
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
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR, Constantes.NODATA);
    }

    public void filtrarCategoria() {
        List<String> operadores = handler.getCategoriaVentas(canal, subcanal, tipo, fabricante, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void filtrarMarca(String categoria, String modulo) {
        List<String> operadores = handler.getMarcaVentas(canal, subcanal, categoria, tipo, fabricante, modulo);
        if (operadores.size() == 2) operadores.remove(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spCategoria) {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarMarca(categoria, modulo);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (adapterView == spMarca) {
            try {
                marca = adapterView.getItemAtPosition(i).toString();
                showListView(fabricante, categoria, marca);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {}

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME VENTAS");
        new DeveloperOptions().modalDevOptions(getActivity());
        // Refrescar la lista al volver al fragmento para ver cambios hechos en el módulo de Precios
        if (marca != null && categoria != null) {
            showListView(fabricante, categoria, marca);
        }
    }

    public void showGenericAlert(String title, String description) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alertdialog_generico, null);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            TextView tvTitulo = dialogView.findViewById(R.id.tvTitulo);
            TextView tvDescription = dialogView.findViewById(R.id.tvDescripcion);
            Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);

            tvTitulo.setText(title);
            tvDescription.setText(description);

            btnAceptar.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        } catch (Exception e) {
            Log.e("VentasFragment", "Error showing alert: " + e.getMessage());
        }
    }

    public void showListView(String fabricante, String categoria, String marca) {
        listProductos = handler.filtrarListProductosVentas(fabricante, categoria, tipo, marca, modulo);

        ArrayList<Base_precio_pvc> preciosVentas = handler.getUltimoPrecioVentasDesdeLocal(user, codigo_pdv);

        if (preciosVentas != null && listProductos != null) {
            for (Ventas v : listProductos) {
                for (Base_precio_pvc bp : preciosVentas) {
                    if (v.getSku().trim().equalsIgnoreCase(bp.getSku().trim())) {
                        v.setPrecio_unitario(bp.getPvc() != null ? bp.getPvc().trim() : "");
                        break;
                    }
                }
            }
        }

        dataAdapter = new CustomAdapterVentas(getContext(), listProductos);
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
                    ArrayList<Ventas> filtro = new ArrayList<Ventas>();
                    String text = s.toString();
                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
                            Ventas v = new Ventas();
                            v.setSku(listProductos.get(i).getSku());
                            filtro.add(v);
                        }
                    }
                    type_name_copy = filtro;
                    listUpdate(type_name_copy);
                }
            });
        } else {
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<Ventas> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterVentas(getContext(), data));
        } else {
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public class CustomAdapterVentas extends ArrayAdapter<Ventas> implements Filterable {

        public ArrayList<Ventas> values;
        public Context context;
        boolean[] checkBoxState;
        boolean[] editedState;

        private void ajustarTamanioTexto(EditText et, String texto) {
            String parteEntera = texto.replace("$", "").split("\\.")[0];
            float size = parteEntera.length() >= 3 ? 12f : 14f;
            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }

        public CustomAdapterVentas(Context context, ArrayList<Ventas> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState = new boolean[values.size()];
            editedState = new boolean[values.size()];
        }

        public class ViewHolder {
            TextView lblSku;
            TextView lblStockInicial, lblCantidad, lblPrecio, lblStockFinal;
            AppCompatCheckBox checkGuardar;
            EditText txt_precio_regular;
            EditText txt_stock_inicial;
            EditText txt_stock_final;
            EditText txt_precio_promocion;
            EditText txt_cantidad;
            Spinner spTipoVenta;
            EditText txt_precio_oferta;
            public ImageButton btnCamera;
            public ImageView ivFoto;
        }

        @Override public int getViewTypeCount() { return values.size(); }
        @Override public int getItemViewType(int position) { return position; }

        @SuppressLint("SuspiciousIndentation")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder vHolder = null;

            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_ventas, parent, false);
                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.lblStockInicial = (TextView) convertView.findViewById(R.id.lblStockInicial);
                vHolder.lblCantidad = (TextView) convertView.findViewById(R.id.lblCantidad);
                vHolder.lblPrecio = (TextView) convertView.findViewById(R.id.lblPrecio);
                vHolder.lblStockFinal = (TextView) convertView.findViewById(R.id.lblStockFinal);
                vHolder.spTipoVenta = (Spinner) convertView.findViewById(R.id.spTipoVenta);
                vHolder.txt_cantidad = (EditText) convertView.findViewById(R.id.txt_cantidad);
                vHolder.txt_stock_inicial = (EditText) convertView.findViewById(R.id.txt_stock_inicial);
                vHolder.txt_stock_final = (EditText) convertView.findViewById(R.id.txt_stock_final);
                vHolder.txt_precio_regular = (EditText) convertView.findViewById(R.id.txt_precio_regular);
                vHolder.btnCamera = (ImageButton) convertView.findViewById(R.id.ibCargarFoto);
                vHolder.ivFoto = (ImageView) convertView.findViewById(R.id.ivFoto);
                vHolder.checkGuardar = (AppCompatCheckBox) convertView.findViewById(R.id.checkGuardar);

                ViewHolder finalVHolder = vHolder;

                ColorStateList colorStateList = getResources().getColorStateList(R.color.checkbox_tint, null);
                CompoundButtonCompat.setButtonTintList(vHolder.checkGuardar, colorStateList);

                vHolder.txt_stock_inicial.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calcularResultado(finalVHolder); }
                    @Override public void afterTextChanged(Editable s) {}
                });

                vHolder.txt_cantidad.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calcularResultado(finalVHolder); }
                    @Override public void afterTextChanged(Editable s) {}
                });

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

                vHolder.txt_precio_regular.setFilters(new InputFilter[]{filter});
                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            try {
                if (values.size() > 0) {
                    vHolder.lblSku.setText(values.get(position).getSku());

                    if (vHolder.txt_precio_regular.getTag() instanceof TextWatcher) {
                        vHolder.txt_precio_regular.removeTextChangedListener((TextWatcher) vHolder.txt_precio_regular.getTag());
                    }

                    String precioActual = values.get(position).getPrecio_unitario();
                    if (precioActual != null && !precioActual.isEmpty()) {
                        try {
                            double val = Double.parseDouble(precioActual.replace(",", "."));
                            precioActual = decimalFormat.format(val).replace(",", ".");
                        } catch (Exception ignored) {}
                    }
                    final int pos = position;
                    final ViewHolder finalV = vHolder;

                    if (!editedState[pos]) {
                        vHolder.txt_precio_regular.setText("");
                        vHolder.txt_precio_regular.setHint(precioActual != null && !precioActual.isEmpty() ? "$" + precioActual : "$");
                        vHolder.txt_precio_regular.setHintTextColor(Color.parseColor("#828282"));
                        values.get(pos).setPrecio_unitario("");
                    } else {
                        vHolder.txt_precio_regular.setText("$" + precioActual);
                        vHolder.txt_precio_regular.setTextColor(Color.BLACK);
                    }
                    ajustarTamanioTexto(vHolder.txt_precio_regular, vHolder.txt_precio_regular.getText().toString());

                    TextWatcher tw = new TextWatcher() {
                        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                        @Override public void afterTextChanged(Editable s) {
                            String str = s.toString();
                            if (str.isEmpty()) {
                                editedState[pos] = false;
                                values.get(pos).setPrecio_unitario("");
                            } else {
                                editedState[pos] = true;
                                finalV.txt_precio_regular.setTextColor(Color.BLACK);
                                if (!str.startsWith("$")) {
                                    finalV.txt_precio_regular.removeTextChangedListener(this);
                                    String clean = str.replace("$", "");
                                    finalV.txt_precio_regular.setText("$" + clean);
                                    finalV.txt_precio_regular.setSelection(finalV.txt_precio_regular.getText().length());
                                    finalV.txt_precio_regular.addTextChangedListener(this);
                                    values.get(pos).setPrecio_unitario(clean);
                                } else {
                                    values.get(pos).setPrecio_unitario(str.replace("$", ""));
                                }
                            }
                        }
                    };
                    vHolder.txt_precio_regular.addTextChangedListener(tw);
                    vHolder.txt_precio_regular.setTag(tw);

                    vHolder.txt_precio_regular.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                String str = finalV.txt_precio_regular.getText().toString();
                                if (!str.isEmpty()) {
                                    String clean = str.replace("$", "");
                                    try {
                                        double val = Double.parseDouble(clean.replace(",", "."));
                                        String formatted = "$" + decimalFormat.format(val).replace(",", ".");
                                        finalV.txt_precio_regular.removeTextChangedListener((TextWatcher) finalV.txt_precio_regular.getTag());
                                        finalV.txt_precio_regular.setText(formatted);
                                        finalV.txt_precio_regular.addTextChangedListener((TextWatcher) finalV.txt_precio_regular.getTag());
                                        ajustarTamanioTexto(finalV.txt_precio_regular, formatted);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                    });

                    sesion = handler.getListGuardadoPrecios3(codigo_pdv);
                    for (int i = 0; i < sesion.size(); i++) {
                        if (values.get(position).getSku().equals(sesion.get(i).getSku())) {
                            vHolder.lblSku.setTextColor(getResources().getColor(R.color.verde));
                        }
                    }

                    final ViewHolder finalv = vHolder;

                    vHolder.btnCamera.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) { cargarImagen(); imageView = finalv.ivFoto; }
                    });

                    vHolder.ivFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (finalv.ivFoto.getDrawable() != null) {
                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
                                alertadd.setTitle("Vista Previa");
                                final View view = LayoutInflater.from(getContext()).inflate(R.layout.vista_previa, null);
                                ((ImageView) view.findViewById(R.id.dialog_imageview)).setImageDrawable(finalv.ivFoto.getDrawable());
                                alertadd.setView(view);
                                alertadd.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {}
                                });
                                alertadd.show();
                            } else {
                                Toast.makeText(getContext(), "No hay foto cargada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            nombImp = txtNomImp.getText().toString();
                            fechaventas = txtFechaVenta.getText().toString();
                            tipoVenta = finalv.spTipoVenta.getSelectedItem().toString();
                            cantidad = finalv.txt_cantidad.getText().toString();
                            pregular = finalv.txt_precio_regular.getText().toString().trim().replace("$", "");
                            vstock_inicial = finalv.txt_stock_inicial.getText().toString();
                            vstock_final = finalv.txt_stock_final.getText().toString();
                            sku = finalv.lblSku.getText().toString();
                            String contenido = values.get(position).getContenido();

                            if (((CheckBox) v).isChecked()) {
                                if (esValidoFormulario(sku, contenido, nombImp, fechaventas, tipoVenta, cantidad, pregular, ppromocion, vstock_inicial, vstock_final)) {

                                    String foto = "NO_FOTO";
                                    if (imageView != null && imageView.getDrawable() != null) {
                                        int n = Resources.getSystem().getDisplayMetrics().widthPixels;
                                        int size = n < 1080 ? 35 : 90;
                                        Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        ImageMark im = new ImageMark();
                                        Bitmap watermark = im.mark(temporal,
                                                "Ciudad: " + handler.getCityPdv(codigo_pdv),
                                                "Local: " + punto_venta,
                                                "Usuario: " + user,
                                                "Fecha y hora: " + fecha + " " + hora,
                                                Color.YELLOW, 100, size, false);
                                        int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
                                        foto = getStringImage(Bitmap.createScaledBitmap(watermark, 1024, mheight, true));
                                    }

                                    insertData(sku, nombImp, fechaventas, tipoVenta, cantidad, pregular, ppromocion, poferta, vstock_inicial, vstock_final, foto);

                                    finalv.lblSku.setTextColor(getResources().getColor(R.color.verde));
                                    finalv.spTipoVenta.setSelection(0);
                                    finalv.txt_stock_inicial.setText("");
                                    finalv.txt_cantidad.setText("");
                                    finalv.txt_stock_final.setText("");
                                    // txt_precio_regular NO se limpia — mantiene el precio unitario visible
                                    finalv.ivFoto.setImageResource(0);

                                } else {
                                    finalv.checkGuardar.setChecked(false);
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.i("EXCEPTION", e.getMessage());
            }

            return convertView;
        }

        private void calcularResultado(ViewHolder vholder) {
            String v1 = vholder.txt_stock_inicial.getText().toString();
            String v2 = vholder.txt_cantidad.getText().toString();
            try {
                if (!v1.isEmpty() && !v2.isEmpty()) {
                    int resultado = Integer.parseInt(v1) - Integer.parseInt(v2);
                    vholder.txt_stock_final.setTextColor(getResources().getColor(resultado < 0 ? R.color.rojo : R.color.gris));
                    vholder.txt_stock_final.setText("" + resultado);
                } else {
                    vholder.txt_stock_final.setText("");
                }
            } catch (NumberFormatException e) {
                vholder.txt_stock_final.setText("Error");
            }
        }

        public String getStringImage(Bitmap bmp) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }

        public void insertData(String skuSelected, String nombImp, String fechaventa, String tipoVenta, String cantidad, String pregular, String ppromocion, String poferta, String vstock_inicial, String vstock_final, String image) {
            try {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);
                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                String fechaVentaSeleccionada = fechaventa.trim();
                if (fechaVentaSeleccionada.isEmpty()) {
                    Toast.makeText(getContext(), "Debe seleccionar una fecha de venta", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    sdf.setLenient(false);
                    sdf.parse(fechaVentaSeleccionada);
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                pregular = pregular.equals("") ? "0" : pregular;

                ContentValues values = new ContentValues();
                values.put(ContractInsertVentas.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertVentas.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertVentas.Columnas.USUARIO, user);
                values.put(ContractInsertVentas.Columnas.SUPERVISOR, supervisor);
                values.put(ContractInsertVentas.Columnas.FECHA, fechaser);
                values.put(ContractInsertVentas.Columnas.HORA, horaser);
                values.put(ContractInsertVentas.Columnas.FECHA_VENTA, fechaVentaSeleccionada);
                values.put(ContractInsertVentas.Columnas.CATEGORIA, spCategoria.getSelectedItem().toString());
                values.put(ContractInsertVentas.Columnas.SUBCATEGORIA, "N/A");
                values.put(ContractInsertVentas.Columnas.PRESENTACION, handler.getPresentacionVentas(skuSelected));
                values.put(ContractInsertVentas.Columnas.POFERTA, "N/A");
                values.put(ContractInsertVentas.Columnas.MARCA, spMarca.getSelectedItem().toString());
                values.put(ContractInsertVentas.Columnas.SKU_CODE, skuSelected);
                values.put(ContractInsertVentas.Columnas.TIPO_VENTA, "N/A");
                values.put(ContractInsertVentas.Columnas.CANTIDAD, cantidad);
                values.put(ContractInsertVentas.Columnas.STOCK_INICIAL, "N/A");
                values.put(ContractInsertVentas.Columnas.PREGULAR, pregular);
                values.put(ContractInsertVentas.Columnas.PPROMOCION, "N/A");
                values.put(ContractInsertVentas.Columnas.STOCK_FINAL, "N/A");
                values.put(ContractInsertVentas.Columnas.MANUFACTURER, fabricante);
                values.put(ContractInsertVentas.Columnas.POS_NAME, handler.getPosNamePdv(codigo_pdv));
                values.put(ContractInsertVentas.Columnas.FOTO, image);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                // 1. Guardar en la tabla de Ventas
                getContext().getContentResolver().insert(ContractInsertVentas.CONTENT_URI, values);

                // 2. Sincronización Bidireccional: Actualizar también la tabla de Precios con el nuevo precio unitario
                ContentValues priceValues = new ContentValues();
                String brand = spMarca.getSelectedItem().toString();
                String category = spCategoria.getSelectedItem().toString();
                String plataforma = handler.getPlataformaBySku(skuSelected);

                priceValues.put(ContractInsertPrecios.Columnas.PHARMA_ID, id_pdv);
                priceValues.put(ContractInsertPrecios.Columnas.CODIGO, codigo_pdv);
                priceValues.put(ContractInsertPrecios.Columnas.USUARIO, user);
                priceValues.put(ContractInsertPrecios.Columnas.SUPERVISOR, supervisor);
                priceValues.put(ContractInsertPrecios.Columnas.FECHA, fechaser);
                priceValues.put(ContractInsertPrecios.Columnas.HORA, horaser);
                priceValues.put(ContractInsertPrecios.Columnas.CATEGORIA, category);
                priceValues.put(ContractInsertPrecios.Columnas.BRAND, brand);
                priceValues.put(ContractInsertPrecios.Columnas.SKU_CODE, skuSelected);
                priceValues.put(ContractInsertPrecios.Columnas.PPROMOCION, pregular); // PVC compartido
                priceValues.put(ContractInsertPrecios.Columnas.MANUFACTURER, fabricante);
                priceValues.put(ContractInsertPrecios.Columnas.POS_NAME, handler.getPosNamePdv(codigo_pdv));
                priceValues.put(ContractInsertPrecios.Columnas.PLATAFORMA, plataforma);
                priceValues.put(ContractInsertPrecios.Columnas.TIPO, "VENTAS_MOD");
                priceValues.put(Constantes.PENDIENTE_INSERCION, 1);

                getContext().getContentResolver().insert(ContractInsertPrecios.CONTENT_URI, priceValues);

                if (VerificarNet.hayConexion(getContext())) {
                    SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertVentas, null);
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private boolean esValidoFormulario(String skuSelected, String contenido, String nombImp, String fechaVenta, String tipoVenta, String cantidad, String s_pregular, String ppromocion, String vstock_inicial, String vstock_final) {

            if (fechaVenta.isEmpty()) {
                showGenericAlert("Campo Requerido", "Debe seleccionar una fecha");
                return false;
            }
            if (cantidad.isEmpty()) {
                showGenericAlert("Campo Requerido", "Ingrese la cantidad");
                return false;
            }

            float v_cantidad = Float.parseFloat(cantidad);
            if (v_cantidad == 0) {
                showGenericAlert("Valor Inválido", "La cantidad no debe ser cero");
                return false;
            }

            if (s_pregular.trim().isEmpty()) {
                showGenericAlert("Campo Requerido", "Ingrese el precio unitario");
                return false;
            } else if (s_pregular.substring(0, 1).equals(".")) {
                showGenericAlert("Formato Inválido", "Formato de precio unitario incorrecto");
                return false;
            }

            float v_regular = Float.parseFloat(s_pregular.replace(",", "."));
            if (v_regular == 0) {
                showGenericAlert("Valor Inválido", "El precio no debe ser cero");
                return false;
            }
            if (v_regular > 500) {
                showGenericAlert("Precio Excedido", "El límite máximo es de $500");
                return false;
            }

            try {
                BaseRangosPreciosSku rangoEspecial = handler.getRangoPreciosBySku(skuSelected);
                if (rangoEspecial != null) {
                    double vRnd = Math.round(v_regular * 100.0) / 100.0;
                    double minimoRnd = Math.round(rangoEspecial.getMinimo() * 100.0) / 100.0;
                    double maximoRnd = Math.round(rangoEspecial.getMaximo() * 100.0) / 100.0;
                    if (vRnd < minimoRnd || vRnd > maximoRnd) {
                        showGenericAlert("Precio Unitario Fuera De Escala", "Rango permitido para esta presentación");
                        return false;
                    }
                } else {
                    ArrayList<BaseRangosPrecios> rangos = handler.getAllRangosPrecios();
                    for (BaseRangosPrecios rango : rangos) {
                        if (contenido != null && contenido.toLowerCase().contains(rango.getPresentacion().toLowerCase())) {
                            double vRnd = Math.round(v_regular * 100.0) / 100.0;
                            double minimoRnd = Math.round(rango.getMinimo() * 100.0) / 100.0;
                            double maximoRnd = Math.round(rango.getMaximo() * 100.0) / 100.0;
                            if (vRnd < minimoRnd || vRnd > maximoRnd) {
                                showGenericAlert("Precio Unitario Fuera De Escala", "Rango permitido para esta presentación");
                                return false;
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("VentasFragment", "Error en validación de rangos: " + e.getMessage());
            }

            double punitario;
            try {
                punitario = Double.parseDouble(s_pregular.replace(",", "."));
            } catch (NumberFormatException e) {
                showGenericAlert("Error", "Precio no válido");
                return false;
            }

            // VALIDACIÓN SOLO EN PVC — oferta ya no valida rango
            try {
                BaseRangosPreciosSku rangoEspecial = handler.getRangoPreciosBySku(skuSelected);
                if (rangoEspecial != null) {
                    double pRnd = Math.round(punitario * 100.0) / 100.0;
                    double minimoRnd = Math.round(rangoEspecial.getMinimo() * 100.0) / 100.0;
                    double maximoRnd = Math.round(rangoEspecial.getMaximo() * 100.0) / 100.0;
                    if (pRnd < minimoRnd || pRnd > maximoRnd) {
                        showGenericAlert("Precio Fuera De Escala", "Validar Precio Unitario Ingresado");
                        return false;
                    }
                } else {
                    ArrayList<BaseRangosPrecios> rangos = handler.getAllRangosPrecios();
                    for (BaseRangosPrecios rango : rangos) {
                        if (contenido != null && contenido.toLowerCase().contains(rango.getPresentacion().toLowerCase())) {
                            double pRnd = Math.round(punitario * 100.0) / 100.0;
                            double minimoRnd = Math.round(rango.getMinimo() * 100.0) / 100.0;
                            double maximoRnd = Math.round(rango.getMaximo() * 100.0) / 100.0;
                            if (pRnd < minimoRnd || pRnd > maximoRnd) {
                                showGenericAlert("Precio Fuera De Escala", "Validar Precio Unitario Ingresado");
                                return false;
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("VentasFragment", "Error en validación de rangos: " + e.getMessage());
            }

            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        scaleImage(imageView, bitmap);
                    } catch (IOException e) { e.printStackTrace(); }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, null);
                    scaleImage(imageView, BitmapFactory.decodeFile(path));
                    break;
            }
        }
    }

    public void scaleImage(ImageView imageView, Bitmap bitmap) {
        try {
            int mheight = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1024, mheight, true));
            bitmapfinal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error: " + e.toString());
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Seleccione una Opción")
                .setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("Tomar Foto")) {
                            Intent n = new Intent(getContext(), CameraActivity.class);
                            n.putExtra("activity", "ventas");
                            startActivity(n);
                        } else if (opciones[i].equals("Cargar Imagen")) {
                            openGallery();
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                }).show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
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
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("¿Desea configurar los permisos de forma manual?")
                .setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("si")) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                }).show();
    }
}