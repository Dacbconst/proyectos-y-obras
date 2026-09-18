package com.luckyecuador.app.PintucoAPP.ui.logistico.relevo;

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
import android.widget.Filterable;
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
import androidx.fragment.app.Fragment;

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
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Clase.Precio;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class LogisticoRelevoFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    ArrayList<Precio> type_name_copy = new ArrayList<Precio>();
    ArrayList<Precio> sesion = new ArrayList<Precio>();

    public static ArrayList<Base_precio_pvc>productos;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spCausalRelevo;
    private Spinner spFabricante;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    private Spinner spMarca;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    private EditText txtDescripcion;
    private Button btnPropia;
    private Button btnCompetencia;

    public static List<String> valores;
    public static List<String> valores2;


    private String categoria, subcategoria, segmento1, segmento2, brand,antes2, tamano, cantidad, codigo, descripcion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora;


    private final String modulo = "LOGISTICA";


    DatabaseHelper handler;

    TextView txtfechav, lSeleccioneProducto;

    TextView empty;
    ListView listview;

    public ArrayList<Precio> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    LogisticoRelevoFragment.CustomAdapterLogisticoRelevo dataAdapter;

    private EditText txtventas, txtpregular, txtppromocion, txtcuotas, txtvcuotas, txtCantidad;
    private String fechaventas, producto, pregular, ppromocion, sku, cuotas, vcuotas, format, presentacion, tipo_precio;


    private String ciudad,retail,sucursal;
    private String canal, subcanal;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    ProgressDialog progressDialog;


    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="SonyApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Ventas";
    String path;
    Button btnCamera;

    MarshMallowPermission marshMallowPermission;

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    private String tipo = "RELEVO";
    private final String fabricante = "AkzoNobel S.A";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_relevo_logistico, container, false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        LoadData();
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();
        marshMallowPermission = new MarshMallowPermission(getActivity());

        valores = new ArrayList<>();
        valores2 = new ArrayList<>();
        productos = new ArrayList<>();

        //startService(new Intent(getContext(), MyService.class));



        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);

        //lSeleccioneProducto = (TextView)findViewById(R.id.lSeleccioneProducto);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        //   spFabricante = (Spinner) rootView.findViewById(R.id.spPresentacion);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);
        btnPropia  = (Button) rootView.findViewById(R.id.btnPropia);
        btnCompetencia  = (Button) rootView.findViewById(R.id.btnCompetencia);
        spCausalRelevo  = (Spinner) rootView.findViewById(R.id.spCausalRelevo);



        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

        //    if(canal.equals("MAYORISTA")){
        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_logistico_title,null,false);
        listview.addHeaderView(headerView,null,false);
        //    }else if (subcanal.equals("AKI")){
        //        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_precios_title_2,null,false);
        //        listview.addHeaderView(headerView,null,false);
        //    }else if (subcanal.equals("SANTA MARIA")){
        //        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_precios_title_2,null,false);
        //        listview.addHeaderView(headerView,null,false);
        //    }else{
        //        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_precios_title,null,false);
        //        listview.addHeaderView(headerView,null,false);
        //    }

        //lSeleccioneProducto.setText("Seleccione Producto: "+ format);

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        //filtrarCodigoSKU();

        consultaGuardado();

        /*
        if (productos.isEmpty()){
            if (VerificarNet.hayConexion(getContext())) {

                progressDialog = new ProgressDialog(getContext());
                //     progressDialog.setTitle("Espere por favor..");
                progressDialog.setMessage("Cargando...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
*/

        //    consultarPvc();

        //    Toast.makeText(getContext(),""+codigo_pdv,Toast.LENGTH_LONG).show();

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

        filtrarCategoria(tipo, fabricante);
//        filtrarCausalesRelevo();


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

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Logístico - Relevo");

        return rootView;
    }


//    @Override
//    public void onDetach() {
//        super.onDetach();
//        spCategoria.setSelection(0);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        binding = null;
    }

    public void resetFragmentState() {

        spCategoria.setSelection(0);

    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());

        //    spCategoria.setSelection(0);


    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        //spCategoria.setAdapter(null);
        List<String> operadores = handler.getCategoriaLogistico(tipo, fabricante, codigo_pdv, canal, subcanal,modulo);

        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }



    public void filtrarCodigoSKU() {
        /*listview.getAdapter();

        txtSKUCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int fabricante, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int fabricante) {
                dataAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });*/

    }



    public void filtrarMarca(String categoria, String subcategoria, String tipo, String fabricante) {
        /*
        List<String> marcas_blancas = handler.getMarcasBlancas();
        if (!canal.equals("MAYORISTA")){
            marcas_blancas.clear();
            marcas_blancas.add("-");
        }*/

        List<String> operadores = handler.getMarcaLogistico(categoria, subcategoria, tipo, fabricante, codigo_pdv, canal, subcanal/*, marcas_blancas*/, modulo);

        if (operadores.size()==2) {
            operadores.remove(0);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }



    public void consultaGuardado() {
        sesion = handler.getListGuardadoPrecios(codigo_pdv);
        for(int i = 0; i < sesion.size(); i++) {
            Log.i("INFO", sesion.get(i).getSku_code() + " " + sesion.get(i).getPvp() + " " + sesion.get(i).getPvc());
        }
    }

    public void showListView(String categoria, String subcategoria, String brand,String tipo, String fabricante) {
        listProductos = handler.filtrarListProductosLogistico(categoria,brand,tipo,fabricante,codigo_pdv,canal,subcanal,modulo);
        dataAdapter = new LogisticoRelevoFragment.CustomAdapterLogisticoRelevo(getContext(), listProductos);
        if (!dataAdapter.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);

            txtSKUCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(txtSKUCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
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

                    /*PreciosActivity.this.dataAdapter.getFilter().filter(s);
                    dataAdapter.notifyDataSetChanged();*/
                }
            });
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<Precio> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new LogisticoRelevoFragment.CustomAdapterLogisticoRelevo(getContext(), data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView== spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                //    filtrarSubcategoria(categoria, tipo, fabricante);
                filtrarMarca(categoria,subcategoria,tipo,fabricante);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView== spMarca) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();

                // Obtenemos Los precios PVC
                productos = handler.getPreciosPvc(user,codigo_pdv,canal,categoria,brand);

                Log.i("1234567",""+productos.size());

                /*
                for (int j = 0 ; j < productos.size() ; j++){
                    Log.i("producto "+(j + 0)+"",""+productos.get(j).getPvc());
                }*/

                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
                // filtrarFabricante(categoria, subcategoria, brand, tipo, fabricante);

                showListView(categoria,subcategoria,brand,tipo,fabricante);

            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
/*
        if (adapterView== spFabricante) {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                Log.i("PRESENTACION",presentacion);
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
                showListView(categoria,subcategoria,brand,presentacion);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        */
        /*if (adapterView==spTamano) {
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(subcategoria,subcategoria,subcategoria,presentacion,contenido,tamano);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spCantidad) {
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(subcategoria,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
                // showListDescripcion(channel,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //if (!fechaventas.equals("") && fechaventas!=null) {
        String model = adapterView.getItemAtPosition(i).toString();
        alertDialog(model);
        //}else{
        //   Toast.makeText(getContext(),Mensajes.FECHA,Toast.LENGTH_SHORT).show();
        // }
    }

    public void alertDialog(final String skuSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_precios, null);

        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle("Precios");
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_precios,null));


        TextView lblModelo=(TextView)dialogView.findViewById(R.id.lblModelo);
        //txtventas = (EditText)dialogView.findViewById(R.id.txtVentas);
        txtpregular = (EditText)dialogView.findViewById(R.id.txtPcont);
        txtppromocion = (EditText) dialogView.findViewById(R.id.txtPcredito);
        //txtcuotas=(EditText) dialogView.findViewById(R.id.txtcuota);
        //txtvcuotas=(EditText) dialogView.findViewById(R.id.txtVCuota);

       /* imageView = (ImageView)dialogView.findViewById(R.id.imageViewPrec);
        btnCamera = (Button) dialogView.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);*/

        /*if (validaPermisos()) {
            btnCamera.setEnabled(true);
        }else{
            btnCamera.setEnabled(false);
        }*/
        lblModelo.setText(getString(R.string.model) +" :    "+skuSelected+"\n");

        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        pregular = txtpregular.getText().toString().trim();
                        ppromocion = txtppromocion.getText().toString();
                        if (!pregular.equals("") && pregular !=null) {
                            //insertData(skuSelected, pregular, ppromocion);
                        }else{
                            Toast.makeText(getContext(),"No ingresaste el precio regular",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        builder.setNeutralButton(R.string.cancel,null);

        //builder.setNegativeButton(R.string.cancel,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(Color.WHITE);
        //pButton.setBackgroundColor(Color.rgb(79,195,247));
        pButton.setPadding(4,2,4,2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        cButton.setTextColor(Color.WHITE);
        //cButton.setBackgroundColor(Color.rgb(79,195,247));
        cButton.setPadding(4,2,4,2);
    }


    public void insertData(String skuSelected, String cantidad, String causal, String tipoLogistico, String foto, String comentario, String fechaCaducado, String fechaPropenso) {
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
            String brand = spMarca.getSelectedItem().toString();

            values.put(ContractInsertLogisticoRelevo.Columnas.PHARMA_ID, id_pdv);
            values.put(ContractInsertLogisticoRelevo.Columnas.CODIGO, codigo_pdv);
            values.put(ContractInsertLogisticoRelevo.Columnas.USUARIO, user);

            values.put(ContractInsertLogisticoRelevo.Columnas.SUPERVISOR, punto_venta);
            values.put(ContractInsertLogisticoRelevo.Columnas.FECHA, fechaser);
            values.put(ContractInsertLogisticoRelevo.Columnas.HORA, horaser);

            values.put(ContractInsertLogisticoRelevo.Columnas.CATEGORIA, categoria);
            values.put(ContractInsertLogisticoRelevo.Columnas.BRAND, brand);
            values.put(ContractInsertLogisticoRelevo.Columnas.SKU_CODE, skuSelected);

            values.put(ContractInsertLogisticoRelevo.Columnas.PREGULAR, cantidad);
            values.put(ContractInsertLogisticoRelevo.Columnas.CAUSAL, causal);
            values.put(ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO, tipoLogistico);

            values.put(ContractInsertLogisticoRelevo.Columnas.FOTO, "NO_FOTO");
            values.put(ContractInsertLogisticoRelevo.Columnas.COMENTARIO, "N/A");
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    @Override
    public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dia + "/" + (mes+1) + "/" + año);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        txtfechav.setText(outDate);
    }

   /* private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100) {
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if (isCreada==false) {
            isCreada=fileImagen.mkdirs();
        }
        if (isCreada==true) {
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }

        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            String authorities=getContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(getContext(), authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
        //
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //OutOfMemoryError
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imageView.setImageURI(miPath);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    scaleImage(bitmap);
                    break;
            }
        }
    }

    private boolean validarPVP(String pregular) {
        return  !pregular.equals(".") && !pregular.equals("0") &&
                !pregular.equals("00") && !pregular.equals("0.0") &&
                !pregular.equals("0.00") && !pregular.equals("00.0") &&
                !pregular.equals("00.00") && !pregular.equals("0.");
    }

    private boolean validarPVC(String ppromocion) {
        return !ppromocion.equals(".") && !ppromocion.equals("0") &&
                !ppromocion.equals("00") && !ppromocion.equals("0.0") &&
                !ppromocion.equals("0.00") && !ppromocion.equals("00.0") &&
                !ppromocion.equals("00.00") && !ppromocion.equals("0.");
    }



    public void scaleImage(Bitmap bitmap) {
        try{
            int mheight = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onClick(View v) {

        if (v == btnPropia) {
            tipo = "RELEVO";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
        /*if (v == btnCompetencia) {
            tipo = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }*/


    }



    public class CustomAdapterLogisticoRelevo extends ArrayAdapter<Precio> implements Filterable {

        public ArrayList<Precio> values;
        public Context context;
        boolean[] checkBoxState;

        public CustomAdapterLogisticoRelevo(Context context, ArrayList<Precio> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState = new boolean[values.size()];
        }


        public class ViewHolder{
            TextView lblSku;
            TextView lblCantidad;
            EditText txtCantidad;
            AppCompatCheckBox checkGuardar;
//            AppCompatCheckBox spCausalRelevo;
            //EditText txtunidad;
//            EditText txt_precio_regular;
//            EditText txt_precio_promocion;
//            EditText txt_precio_oferta;
//
//            //            Spinner spTipos;
            Spinner spCausalRelevo;
//

            private boolean spinnerInicializado = false;

            Spinner sp;

            private String causalSeleccionada = "";
            private String fechaCaducado = "";
            private String fechaPropenso = "";
        }

        @Override
        public int getViewTypeCount() {

            return values.size();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }


        /*@NonNull
        @Override
        public Filter getFilter() {
            return precioFilter;
        }

        private Filter precioFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Precio> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(values);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Precio item : values) {
                        if (item.getSku_code().toLowerCase().contains(filterPattern)) {
                            suggestions.add(item);
                        }
                    }
                }
                results.values = suggestions;
                results.fabricante = suggestions.status();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List) results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Precio) resultValue).getSku_code();
            }
        };*/

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LogisticoRelevoFragment.CustomAdapterLogisticoRelevo.ViewHolder vHolder = null;

            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_logistico_registros, parent, false);

                vHolder = new LogisticoRelevoFragment.CustomAdapterLogisticoRelevo.ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.txtCantidad = (EditText) convertView.findViewById(R.id.txtCantidad);
                vHolder.checkGuardar = (AppCompatCheckBox) convertView.findViewById(R.id.checkGuardar);
                vHolder.spCausalRelevo = (Spinner) convertView.findViewById(R.id.spCausalRelevo);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        new ArrayList<String>()
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vHolder.spCausalRelevo.setAdapter(adapter);


                vHolder.spinnerInicializado = false;

                convertView.setTag(vHolder);
            } else {
                vHolder = (LogisticoRelevoFragment.CustomAdapterLogisticoRelevo.ViewHolder) convertView.getTag();
            }


                if (values.size() > 0) {
                    vHolder.lblSku.setText(values.get(position).getSku_code());

                    final LogisticoRelevoFragment.CustomAdapterLogisticoRelevo.ViewHolder finalv = vHolder;

                    /* Contenido del spinner */
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) vHolder.spCausalRelevo.getAdapter();
                    adapter.clear();

                    if (!finalv.causalSeleccionada.isEmpty()) {
                        if (finalv.causalSeleccionada.length() > 30) {
                            adapter.add(finalv.causalSeleccionada.substring(0, 30) + "...");
                        } else {
                            adapter.add(finalv.causalSeleccionada);
                        }
//                        adapter.add("Cambiar causal...");
                    } else {
                        adapter.add("Seleccione");
                    }
                    adapter.notifyDataSetChanged();

                    vHolder.spCausalRelevo.setOnItemSelectedListener(null);

                    vHolder.spinnerInicializado = false;

                    vHolder.spCausalRelevo.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {

                                finalv.spinnerInicializado = true;
                                mostrarDialogoCausalesMultiples(getContext(), finalv);
                                /* PARA OCULTAR SPINNER LUEGO DE SELECCIONAR CAUSAL */
                                return true;
                            }
                            return false;
                        }
                    });


                    vHolder.txtCantidad.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });

                    vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox)v).isChecked()) {
                                String skuSeleccionado = finalv.lblSku.getText().toString();
                                String cantidad = finalv.txtCantidad.getText().toString().trim();
                                String causal = finalv.causalSeleccionada;
                                String tipoLogistico = tipo;

                                if (cantidad.isEmpty()) {
                                    Toast.makeText(getContext(), "Debe ingresar una cantidad", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                    return;
                                }

                                try {
                                    double cantidadNum = Double.parseDouble(cantidad);
                                    if (cantidadNum <= 0) {
                                        Toast.makeText(getContext(), "La cantidad debe ser mayor a 0", Toast.LENGTH_LONG).show();
                                        finalv.checkGuardar.setChecked(false);
                                        return;
                                    }
                                } catch (NumberFormatException e) {
                                    Toast.makeText(getContext(), "Ingrese un número válido para la cantidad", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                    return;
                                }

                                if (finalv.causalSeleccionada.isEmpty()) {
                                    Toast.makeText(getContext(), "Debe seleccionar al menos una causal", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                    return;
                                }

                                if (finalv.causalSeleccionada.contains("PRODUCTO CADUCADO") &&
                                        finalv.fechaCaducado.isEmpty()) {
                                    Toast.makeText(getContext(), "Debe seleccionar fecha para producto caducado", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                    return;
                                }

                                if (finalv.causalSeleccionada.contains("PRODUCTO PROPENSO") &&
                                        finalv.fechaPropenso.isEmpty()) {
                                    Toast.makeText(getContext(), "Debe seleccionar fecha para producto propenso", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                    return;
                                }

                                boolean esDuplicadoExacto = handler.registroDuplicadoLogisticoRelevo(
                                        skuSeleccionado,
                                        cantidad,
                                        causal,
                                        tipoLogistico
                                );

                                if (esDuplicadoExacto) {

                                    mostrarAlertaDuplicado(getContext(), new AlertaDuplicadoCallback() {
                                        @Override
                                        public void onAceptar(boolean noVolverAMostrar) {
                                            insertData(
                                                    skuSeleccionado,
                                                    cantidad,
                                                    finalv.causalSeleccionada,
                                                    tipo,
                                                    "N/A",
                                                    "N/A",
                                                    finalv.fechaCaducado,
                                                    finalv.fechaPropenso
                                            );
//                                            finalv.checkGuardar.setChecked(true);
//                                            finalv.checkGuardar.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.fuchsia)));

                                            finalv.lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                                            finalv.lblSku.setTypeface(null, Typeface.BOLD);
                                        }

                                        @Override
                                        public void onCancelar() {
                                            // Cuando se cancela, simplemente desmarcar el checkbox
                                            finalv.checkGuardar.setChecked(false);
                                        }
                                    });

                                    return;
                                }


                                insertData(
                                        skuSeleccionado,
                                        cantidad,
                                        finalv.causalSeleccionada,
                                        tipo,
                                        "N/A",
                                        "N/A",
                                        finalv.fechaCaducado,
                                        finalv.fechaPropenso
                                );

//                                finalv.checkGuardar.setChecked(true);
//                                finalv.checkGuardar.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.fuchsia)));

                                finalv.lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                                finalv.lblSku.setTypeface(null, Typeface.BOLD);

//                                finalv.checkGuardar.setEnabled(false);
//                                finalv.spCausalRelevo.setEnabled(false);
//                                finalv.txtCantidad.setEnabled(false);

//                                ArrayAdapter<String> adapter = (ArrayAdapter<String>) finalv.spCausalRelevo.getAdapter();
//                                adapter.clear();
//                                adapter.add("Seleccione");
//                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }


            return convertView;
        }

        private void mostrarAlertaDuplicado(Context context, final AlertaDuplicadoCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.alert_dialog_duplicado_logistico_relevo, null);

            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();

            CheckBox checkNoMostrar = dialogView.findViewById(R.id.checkBox);
            Button btnAceptar = dialogView.findViewById(R.id.btn_acept);
            Button btnCancelar = dialogView.findViewById(R.id.btn_cancelar);

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean noVolverAMostrar = checkNoMostrar.isChecked();
                    dialog.dismiss();
                    if (callback != null) {
                        callback.onAceptar(noVolverAMostrar);
                    }
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (callback != null) {
                        callback.onCancelar();
                    }
                }
            });

            dialog.setCancelable(false);
            dialog.show();
        }


        private void mostrarDialogoCausalesMultiples(Context context, final ViewHolder viewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.alertdialog_causales_logistico, null);

            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();

            LinearLayout containerCausales = dialogView.findViewById(R.id.containerCausales);
            final LinearLayout containerCaducado = dialogView.findViewById(R.id.containerCaducado);
            final LinearLayout containerPropenso = dialogView.findViewById(R.id.containerPropenso);

            final EditText etFechaCaducado = dialogView.findViewById(R.id.etFechaCaducado);
            final EditText etFechaPropenso = dialogView.findViewById(R.id.etFechaPropenso);
            final TextView tvFechaCaducadoSeleccionada = dialogView.findViewById(R.id.tvFechaCaducadoSeleccionada);
            final TextView tvFechaPropensoSeleccionada = dialogView.findViewById(R.id.tvFechaPropensoSeleccionada);

            Button btnCalendarioCaducado = dialogView.findViewById(R.id.btnCalendarioCaducado);
            Button btnCalendarioPropenso = dialogView.findViewById(R.id.btnCalendarioPropenso);
            Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);
            Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);


            final List<String> causalesSeleccionadasTemp = new ArrayList<>();
            final String[] fechaCaducadoTemp = {""};
            final String[] fechaPropensoTemp = {""};


            List<String> causales = handler.getCausalLogisticoRelevo();


            for (String causal : causales) {
                if ("Seleccione".equals(causal)) continue;

                AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
                checkBox.setText(causal);
                checkBox.setTextSize(14);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setPadding(10, 10, 10, 10);
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

                if (viewHolder.causalSeleccionada.contains(causal)) {
                    checkBox.setChecked(true);
                    causalesSeleccionadasTemp.add(causal);

                    if (causal.equals("PRODUCTO CADUCADO") && !viewHolder.fechaCaducado.isEmpty()) {
                        containerCaducado.setVisibility(View.VISIBLE);
                        etFechaCaducado.setText(viewHolder.fechaCaducado);
                        fechaCaducadoTemp[0] = viewHolder.fechaCaducado;

//                        tvFechaCaducadoSeleccionada.setText("Fecha caducado: " + viewHolder.fechaCaducado);

                        /* DIAS TRANSCURRIDOS*/
                        String diasTexto = calcularDiasTranscurridos(viewHolder.fechaCaducado);
                        tvFechaCaducadoSeleccionada.setText("Días transcurridos: "  + diasTexto);

                        tvFechaCaducadoSeleccionada.setVisibility(View.VISIBLE);
                    }

                    if (causal.equals("PRODUCTO PROPENSO") && !viewHolder.fechaPropenso.isEmpty()) {
                        containerPropenso.setVisibility(View.VISIBLE);
                        etFechaPropenso.setText(viewHolder.fechaPropenso);
                        fechaPropensoTemp[0] = viewHolder.fechaPropenso;
//                        tvFechaPropensoSeleccionada.setText("Fecha propenso: " + viewHolder.fechaPropenso);
                        String diasTexto = calcularDiasTranscurridos(viewHolder.fechaPropenso);
                        tvFechaPropensoSeleccionada.setText("Días transcurridos: "  + diasTexto);

                        tvFechaPropensoSeleccionada.setVisibility(View.VISIBLE);
                    }
                }

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
//                        if (!causalesSeleccionadasTemp.contains(causal)) {
//                            causalesSeleccionadasTemp.add(causal);
//                        }

                        for (int i = 0; i < containerCausales.getChildCount(); i++) {
                            View child = containerCausales.getChildAt(i);
                            if (child instanceof AppCompatCheckBox && child != buttonView) {
                                ((AppCompatCheckBox) child).setChecked(false);
                            }
                        }


                        causalesSeleccionadasTemp.clear();
                        causalesSeleccionadasTemp.add(causal);

                        if (causal.equals("PRODUCTO CADUCADO")) {
                            containerCaducado.setVisibility(View.VISIBLE);
                        } else if (causal.equals("PRODUCTO PROPENSO")) {
                            containerPropenso.setVisibility(View.VISIBLE);
                        }
                    } else {
                        causalesSeleccionadasTemp.remove(causal);

                        if (causal.equals("PRODUCTO CADUCADO")) {
                            containerCaducado.setVisibility(View.GONE);
                            fechaCaducadoTemp[0] = "";
                            etFechaCaducado.setText("");
                            tvFechaCaducadoSeleccionada.setVisibility(View.GONE);
                        } else if (causal.equals("PRODUCTO PROPENSO")) {
                            containerPropenso.setVisibility(View.GONE);
                            fechaPropensoTemp[0] = "";
                            etFechaPropenso.setText("");
                            tvFechaPropensoSeleccionada.setVisibility(View.GONE);
                        }
                    }
                });

                containerCausales.addView(checkBox);
            }

            btnCalendarioCaducado.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String fechaFormateada = String.format("%02d/%02d/%04d",
                                    selectedDay, selectedMonth + 1, selectedYear);

                            etFechaCaducado.setText(fechaFormateada);
                            fechaCaducadoTemp[0] = fechaFormateada;
//                            tvFechaCaducadoSeleccionada.setText("Fecha caducado: " + fechaFormateada);
                            String diasTexto = calcularDiasTranscurridos(fechaFormateada);
                            tvFechaCaducadoSeleccionada.setText("Días transcurridos: " + diasTexto);

                            tvFechaCaducadoSeleccionada.setVisibility(View.VISIBLE);

                        }, year, month, day);

                datePickerDialog.show();
            });

            btnCalendarioPropenso.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String fechaFormateada = String.format("%02d/%02d/%04d",
                                    selectedDay, selectedMonth + 1, selectedYear);

                            etFechaPropenso.setText(fechaFormateada);
                            fechaPropensoTemp[0] = fechaFormateada;
//                            tvFechaPropensoSeleccionada.setText("Fecha propenso: " + fechaFormateada);
                            String diasTexto = calcularDiasTranscurridos(fechaFormateada);
                            tvFechaPropensoSeleccionada.setText("Días transcurridos: " + diasTexto);

                            tvFechaPropensoSeleccionada.setVisibility(View.VISIBLE);
                        }, year, month, day);

                datePickerDialog.show();
            });

            etFechaCaducado.setOnClickListener(v -> btnCalendarioCaducado.performClick());
            etFechaPropenso.setOnClickListener(v -> btnCalendarioPropenso.performClick());

            btnAceptar.setOnClickListener(v -> {
                if (causalesSeleccionadasTemp.isEmpty()) {
                    Toast.makeText(context, "Seleccione al menos una causal", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (causalesSeleccionadasTemp.contains("PRODUCTO CADUCADO") &&
                        fechaCaducadoTemp[0].isEmpty()) {
                    Toast.makeText(context, "Seleccione fecha para producto caducado", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (causalesSeleccionadasTemp.contains("PRODUCTO PROPENSO") &&
                        fechaPropensoTemp[0].isEmpty()) {
                    Toast.makeText(context, "Seleccione fecha para producto propenso", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder causalesString = new StringBuilder();
                for (String causal : causalesSeleccionadasTemp) {
                    if (causalesString.length() > 0) {
                        causalesString.append(", ");
                    }
                    causalesString.append(causal);
                }


//                if (!fechaCaducadoTemp[0].isEmpty()) {
//                    causalesString.append(" (Caducidad: ").append(fechaCaducadoTemp[0]).append(")");
//                }
//
//                if (!fechaPropensoTemp[0].isEmpty()) {
//                    causalesString.append(" (Propenso: ").append(fechaPropensoTemp[0]).append(")");
//                }

                viewHolder.causalSeleccionada = causalesString.toString();
                viewHolder.fechaCaducado = fechaCaducadoTemp[0];
                viewHolder.fechaPropenso = fechaPropensoTemp[0];


                ArrayAdapter<String> adapter = (ArrayAdapter<String>) viewHolder.spCausalRelevo.getAdapter();
                adapter.clear();

                String resumen;
                if (causalesSeleccionadasTemp.size() == 1) {
                    resumen = causalesSeleccionadasTemp.get(0);
                } else {
                    resumen = causalesSeleccionadasTemp.size() + " causales";
                }

                adapter.add(resumen);
//                adapter.add("Cambiar causal...");
                adapter.notifyDataSetChanged();
                viewHolder.spCausalRelevo.setSelection(0);

                dialog.dismiss();

            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }


        private String calcularDiasTranscurridos(String fechaSeleccionada) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fechaSel = sdf.parse(fechaSeleccionada);


                Calendar calActual = Calendar.getInstance();
                calActual.set(Calendar.HOUR_OF_DAY, 0);
                calActual.set(Calendar.MINUTE, 0);
                calActual.set(Calendar.SECOND, 0);
                calActual.set(Calendar.MILLISECOND, 0);
                Date fechaActual = calActual.getTime();

                long diff = fechaSel.getTime() - fechaActual.getTime();
                long dias = diff / (24 * 60 * 60 * 1000);

                if (dias > 0) {
                    return "+" + dias + " días";
                } else if (dias < 0) {
                    return dias + " días";
                } else {
                    return "Hoy";
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }

        private boolean esFormularioValido(String s_pvp, String s_pvc, String s_oferta, String s_pvm, String tipo) {

            /*
            if (s_pvp.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el PVP", Toast.LENGTH_LONG).show();
                return false;
            }else{
                if(s_pvp.substring(0,1).equals(".")){
                    Toast.makeText(getContext(), "formato de PVP inválido", Toast.LENGTH_LONG).show();
                    return false;
                }
            }*/

            if (s_oferta.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el Precio oferta", Toast.LENGTH_LONG).show();
                return false;
            }else{
                if(s_oferta.substring(0,1).equals(".")){
                    Toast.makeText(getContext(), "formato de Precio oferta inválido", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            double oferta = Double.parseDouble(s_oferta);

            if (oferta > 500){
                Toast.makeText(getContext(), "El limite máximo del Precio oferta es de $500", Toast.LENGTH_LONG).show();
                return false;
            }




            if (s_pvc.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el PVC", Toast.LENGTH_LONG).show();
                return false;
            }else{
                if(s_pvc.substring(0,1).equals(".")){
                    Toast.makeText(getContext(), "formato de PVC inválido", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            double pvc = Double.parseDouble(s_pvc);

            if (pvc > 500){
                Toast.makeText(getContext(), "El limite máximo del PVC es de $500", Toast.LENGTH_LONG).show();
                return false;
            }


            if (pvc < 0) {
                Toast.makeText(getContext(), "El PVC no debe ser menor a 0", Toast.LENGTH_LONG).show();
                return false;
            }

            if (pvc == 0) {
                Toast.makeText(getContext(), "El PVC no puede ser 0", Toast.LENGTH_LONG).show();
                return false;
            }


            if (oferta > pvc) {
                Toast.makeText(getContext(), "El Precio oferta no puede ser mayor a PVC", Toast.LENGTH_LONG).show();
                return false;
            }




            return true;
        }

    }

    public interface AlertaDuplicadoCallback {
        void onAceptar(boolean noVolverAMostrar);
        void onCancelar();
    }
}