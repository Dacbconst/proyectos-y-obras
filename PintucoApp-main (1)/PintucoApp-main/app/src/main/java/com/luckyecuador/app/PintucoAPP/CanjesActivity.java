package com.luckyecuador.app.PintucoAPP;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.InsertCanjes;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRotacion;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CanjesActivity extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

    ArrayList<BaseRotacion> type_name_copy = new ArrayList<BaseRotacion>();
    ArrayList<InsertCanjes> sesion = new ArrayList<InsertCanjes>();
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spMarca;
    private EditText txtObservaciones;
    //private Spinner spPresentacion;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;
    private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    //private EditText txtCantidad;

    String[] valueOfProductos;
    String[] valueOfTipoCombo;
    String[] valueOfMecanica;
    String[] valueOfArmados;
    String[] valueOfStock;
    String[] valueOfPvcCombo;
    String[] valueOfPvcUnitario;
    String[] valueOfVisita;
    String[] valueOfObservacion;
    String[] valueOfFoto;

   //BASE SQLITE
    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, mecanica, observaciones;
    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, tipo = "F";

    DatabaseHelper handler;

    EditText txtsearch;
    TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    RecyclerView listview;

    public ArrayList<BaseRotacion> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    CustomAdapterCanjes dataAdapter;

    private EditText txtventas, txtpregular, txtppromocion, txtcuotas, txtvcuotas;
    private String fechaventas, producto, poferta, sku, cuotas, vcuotas, format, presentacion, promocional;
    private String ciudad,retail,sucursal;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private final String CARPETA_RAIZ = "PintucoApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Rotacion";

    private String fabricante = "ALICORP";

    String path;
    Button btnCamera;
    private Button btnFoods;
    private Button btnHomeCare;
    private Button btnGallery;
    private Button btnSave;

    MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canjes);
        setToolBar();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();
        marshMallowPermission = new MarshMallowPermission(this);

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        btnCamera = (Button) findViewById(R.id.btnCameraLocal);
       // btnGallery= (Button) findViewById(R.id.btnGallery);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnFoods = (Button) findViewById(R.id.btnFoods);
        btnHomeCare = (Button) findViewById(R.id.btnHomecare);

        btnFoods.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
        btnFoods.setPadding(15, 15, 15, 15);
        btnHomeCare.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background_unselected));
        btnHomeCare.setPadding(15, 15, 15, 15);

        spCategoria = (Spinner)findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spMarca = (Spinner)findViewById(R.id.spMarca);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        //spPresentacion = (Spinner)findViewById(R.id.spPresentacion);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) findViewById(R.id.txtSKUCode);
        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
       // txtDescripcion  = (EditText)findViewById(R.id.txtDescripcionSKU);

        //lSeleccioneProducto.setText("Seleccione Producto: "+ format);

        layout_skuName = (LinearLayout)findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout)findViewById(R.id.layout_skuDescripcion);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        filtrarCodigoSKU();
        filtrarCategoria(tipo);
        //consultaGuardado();

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);

        listview = (RecyclerView) findViewById(R.id.lvSKUCode);
        listview.setHasFixedSize(true);

//        listview = (RecyclerView) findViewById(R.id.lvSKUCode);
//        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_rotacion_title,null,false);
//        listview.addHeaderView(headerView,null,false);
//
//        listview.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
        btnFoods.setOnClickListener(this);
        btnHomeCare.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        //btnGallery.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            guardar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }

    public void filtrarCategoria(String tipo) {
        List<String> operadores = handler.getCategoriaRotacion(tipo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
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

    public void filtrarSubcategoria(String tipo, String categoria) {
        List<String> operadores = handler.getSubcategoriaRotacion(tipo, categoria);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String tipo, String categoria, String subcategoria) {
        List<String> operadores = handler.getMarcaRotacion(tipo, categoria, subcategoria);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    /*public void filtrarSegmento(String subcategoria, String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento2(subcategoria,subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }*/

  /*  public void filtrarMarca(String fabricante, String categoria, String subcategoria) {
        List<String> operadores = handler.getMarcaProdCad(fabricante,categoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }*/

    /*public void filtrarSegmento(String logro, String subcategoria, String status) {
        List<String> operadores = handler.getFabricantePrecios(logro,subcategoria, status);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentacion.setAdapter(dataAdapter);
        spPresentacion.setOnItemSelectedListener(this);
    }

    public void filtrartamano(String subcategoria, String subcategoria, String subcategoria, String presentacion, String contenido) {
        List<String> operadores = handler.getTamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTamano.setAdapter(dataAdapter);
        spTamano.setOnItemSelectedListener(this);
    }

    public void filtrarcantidad(String subcategoria, String subcategoria, String subcategoria, String presentacion, String contenido, String tamano) {
        List<String> operadores = handler.getCantidad(subcategoria,subcategoria,subcategoria,presentacion,contenido, tamano);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(dataAdapter);
        spCantidad.setOnItemSelectedListener(this);
    }*/

    /*public void consultaGuardado() {
        sesion = handler.getListGuardadoPrecios(codigo_pdv);
        for(int i = 0; i < sesion.size(); i++) {
            Log.i("INFO", sesion.get(i).getSku_code() + " " + sesion.get(i).getPregular() + " " + sesion.get(i).getPpromo());
        }
    }*/

    public void showListView(String tipo, String categoria, String subcategoria, String marca) {
        listProductos = handler.filtrarListRotacion(tipo, categoria, subcategoria, marca);

        dataAdapter = new CustomAdapterCanjes(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CanjesActivity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterCanjes customAdapter = new CustomAdapterCanjes(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            dataAdapter = (CustomAdapterCanjes) listview.getAdapter();
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<BaseRotacion> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
//            listview.setAdapter(new CustomAdapterEjecucionMateriales(this, data));
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
                filtrarSubcategoria(tipo, categoria);
                listview.setAdapter(null);
//                showListView(categoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSubcategoria) {
            try{
                subcategoria =adapterView.getItemAtPosition(i).toString();
                filtrarMarca(tipo, categoria, subcategoria);
                listview.setAdapter(null);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if (adapterView==spSubcategoria) {
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                filtrarSegmento(subcategoria,subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spSegmento) {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                filtrarMarca(subcategoria,subcategoria,subcategoria,presentacion);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
        if (adapterView== spMarca) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
//                showListView(fabricante,categoria,subcategoria,brand);
                showListView(tipo, categoria, subcategoria, brand);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        /*if (adapterView== spPresentacion) {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
                showListView(logro,subcategoria,brand,presentacion);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spTamano) {
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(subcategoria,subcategoria,subcategoria,presentacion,contenido,tamano);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spCantidad) {
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(subcategoria,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
                // showListDescripcion(channel,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
        //alertDialog(model);
        //}else{
        //   Toast.makeText(getApplicationContext(),Mensajes.FECHA,Toast.LENGTH_SHORT).show();
        // }
    }

    /*public void alertDialog(final String skuSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_precios, null);
        //Title
        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle("Rotacion");
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_precios,null));

        //Traer Views
        TextView lblModelo=(TextView)dialogView.findViewById(R.id.lblModelo);
        //txtventas = (EditText)dialogView.findViewById(R.id.txtVentas);
        txtpregular = (EditText)dialogView.findViewById(R.id.txtPcont);
        txtppromocion = (EditText) dialogView.findViewById(R.id.txtPcredito);
        //txtcuotas=(EditText) dialogView.findViewById(R.id.txtcuota);
        //txtvcuotas=(EditText) dialogView.findViewById(R.id.txtVCuota);

        /*imageView = (ImageView)dialogView.findViewById(R.id.imageViewPrec);
        btnCamera = (Button) dialogView.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);*/

        /*if (validaPermisos()) {
            btnCamera.setEnabled(true);
        }else{
            btnCamera.setEnabled(false);
        }*/
        /*lblModelo.setText(getString(R.string.model) +" :    "+skuSelected+"\n");

        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        fecha_rot = txtpregular.getText().toString().trim();
                        cantidad = txtppromocion.getText().toString();
                        if (!fecha_rot.equals("") && fecha_rot !=null) {
                            //insertData(skuSelected, fecha_rot, cantidad);
                        }else{
                            Toast.makeText(getApplicationContext(),"No ingresaste el precio regular",Toast.LENGTH_LONG).show();
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
    }*/

    public void insertData() {
//        if (esFormularioValido()) {
            try {
                String image = "NO_FOTO";
                if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                    image = getStringImage(bitmapfinal);
                }

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                String categoria = spCategoria.getSelectedItem().toString();
                String subcategoria = spSubcategoria.getSelectedItem().toString();
                String marca = spMarca.getSelectedItem().toString();

                BasePharmaValue pdv = handler.getPdv(codigo_pdv);

                do {
                    String producto = "";
                    String tipo_combo = "";
                    String mecanica = "";
                    String armados = "";
                    String stock = "";
                    String pvc_combos = "";
                    String pvc_unitario = "";
                    String visita = "";
                    String observacion = "";
                    String foto = "";

                    Log.i("SIZE PRODUCTOS", valueOfProductos.length + "");

                    for (int i = 0; i < valueOfProductos.length; i++) {
                        producto = valueOfProductos[0];
                    }

                    for (int i = 0; i < valueOfTipoCombo.length; i++) {
                        tipo_combo = valueOfTipoCombo[0];
                    }

                    for (int i = 0; i < valueOfMecanica.length; i++) {
                        mecanica = valueOfMecanica[0];
                    }

                    for (int i = 0; i < valueOfArmados.length; i++) {
                        armados = valueOfArmados[0];
                    }

                    for (int i = 0; i < valueOfStock.length; i++) {
                        stock = valueOfStock[0];
                    }

                    for (int i = 0; i < valueOfPvcCombo.length; i++) {
                        pvc_combos = valueOfPvcCombo[0];
                    }

                    for (int i = 0; i < valueOfPvcUnitario.length; i++) {
                        pvc_unitario = valueOfPvcUnitario[0];
                    }

                    for (int i = 0; i < valueOfVisita.length; i++) {
                        visita = valueOfVisita[0];
                    }

                    for (int i = 0; i < valueOfObservacion.length; i++) {
                        observacion = valueOfObservacion[0];
                    }

                    for (int i = 0; i < valueOfFoto.length; i++) {
                        foto = valueOfFoto[0];
                    }

                    List<String> list_productos = new ArrayList<String>(Arrays.asList(valueOfProductos));
                    list_productos.remove(0);
                    valueOfProductos = list_productos.toArray(new String[0]);

                    List<String> list_tipo_combo = new ArrayList<String>(Arrays.asList(valueOfTipoCombo));
                    list_tipo_combo.remove(0);
                    valueOfTipoCombo = list_tipo_combo.toArray(new String[0]);

                    List<String> list_mecanica = new ArrayList<String>(Arrays.asList(valueOfMecanica));
                    list_mecanica.remove(0);
                    valueOfMecanica = list_mecanica.toArray(new String[0]);

                    List<String> list_armados = new ArrayList<String>(Arrays.asList(valueOfArmados));
                    list_armados.remove(0);
                    valueOfArmados = list_armados.toArray(new String[0]);

                    List<String> list_stock = new ArrayList<String>(Arrays.asList(valueOfStock));
                    list_stock.remove(0);
                    valueOfStock = list_stock.toArray(new String[0]);

                    List<String> list_pvc_combo = new ArrayList<String>(Arrays.asList(valueOfPvcCombo));
                    list_pvc_combo.remove(0);
                    valueOfPvcCombo = list_pvc_combo.toArray(new String[0]);

                    List<String> list_pvc_unitario = new ArrayList<String>(Arrays.asList(valueOfPvcUnitario));
                    list_pvc_unitario.remove(0);
                    valueOfPvcUnitario = list_pvc_unitario.toArray(new String[0]);

                    List<String> list_visita = new ArrayList<String>(Arrays.asList(valueOfVisita));
                    list_visita.remove(0);
                    valueOfVisita = list_visita.toArray(new String[0]);

                    List<String> list_observacion = new ArrayList<String>(Arrays.asList(valueOfObservacion));
                    list_observacion.remove(0);
                    valueOfObservacion = list_observacion.toArray(new String[0]);

                    List<String> list_foto = new ArrayList<String>(Arrays.asList(valueOfFoto));
                    list_foto.remove(0);
                    valueOfFoto = list_foto.toArray(new String[0]);

                    if (producto!=null) {
                        ContentValues values = new ContentValues();

                        values.put(ContractInsertCanjes.Columnas.PHARMA_ID, pdv.getPos_id());
                        values.put(ContractInsertCanjes.Columnas.CODIGO, codigo_pdv);
                        values.put(ContractInsertCanjes.Columnas.CANAL, pdv.getChannel());
                        values.put(ContractInsertCanjes.Columnas.NOMBRE_COMERCIAL, pdv.getCustomer_owner());
                        values.put(ContractInsertCanjes.Columnas.LOCAL, pdv.getPos_name());
                        values.put(ContractInsertCanjes.Columnas.REGION, pdv.getRegion());
                        values.put(ContractInsertCanjes.Columnas.PROVINCIA, pdv.getProvince());
                        values.put(ContractInsertCanjes.Columnas.CIUDAD, pdv.getCity());
                        values.put(ContractInsertCanjes.Columnas.ZONA, pdv.getZone());
                        values.put(ContractInsertCanjes.Columnas.DIRECCION, pdv.getAddress());
                        values.put(ContractInsertCanjes.Columnas.SUPERVISOR, pdv.getSupervisor());
                        values.put(ContractInsertCanjes.Columnas.MERCADERISTA, pdv.getMercaderista());
                        values.put(ContractInsertCanjes.Columnas.USUARIO, user);
                        values.put(ContractInsertCanjes.Columnas.LATITUD, pdv.getLatitud());
                        values.put(ContractInsertCanjes.Columnas.LONGITUD, pdv.getLongitud());
                        values.put(ContractInsertCanjes.Columnas.TERRITORIO, pdv.getKam());
                        values.put(ContractInsertCanjes.Columnas.ZONA_TERRITORIO, pdv.getTipo());
                        values.put(ContractInsertCanjes.Columnas.CATEGORIA, categoria);
                        values.put(ContractInsertCanjes.Columnas.SUBCATEGORIA, subcategoria);
                        values.put(ContractInsertCanjes.Columnas.MARCA, marca);
                        values.put(ContractInsertCanjes.Columnas.PRODUCTO, producto);
                        values.put(ContractInsertCanjes.Columnas.TIPO_COMBO, tipo_combo);
                        values.put(ContractInsertCanjes.Columnas.MECANICA, mecanica);
                        values.put(ContractInsertCanjes.Columnas.COMBOS_ARMADOS, armados);
                        values.put(ContractInsertCanjes.Columnas.STOCK, stock);
                        values.put(ContractInsertCanjes.Columnas.PVC_COMBO, pvc_combos);
                        values.put(ContractInsertCanjes.Columnas.PVC_UNITARIO, pvc_unitario);
                        values.put(ContractInsertCanjes.Columnas.VISITA, visita);
                        values.put(ContractInsertCanjes.Columnas.OBSERVACIONES, observacion);
                        values.put(ContractInsertCanjes.Columnas.FOTO, foto);
                        values.put(ContractInsertCanjes.Columnas.FOTO_GUIA, image);
                        values.put(ContractInsertCanjes.Columnas.FECHA, fechaser);
                        values.put(ContractInsertCanjes.Columnas.HORA, horaser);
                        values.put(Constantes.PENDIENTE_INSERCION, 1);

                        getContentResolver().insert(ContractInsertCanjes.CONTENT_URI, values);

                        if (VerificarNet.hayConexion(getApplicationContext())) {
                            SyncAdapter.sincronizarAhora(this, true, Constantes.insertCanjes, null);
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                        }
                    }
                } while(valueOfProductos.length != 0);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
//        }
    }

    private boolean esFormularioValido() {
        if (imageView != null && imageView.getDrawable() != null) {
            if (txtObservaciones.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Debe llenar el formulario", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            return true;
        }
        return true;
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

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(INTERNET)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(INTERNET))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,INTERNET},100);
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == btnFoods) {
            tipo = "F";
            btnFoods.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
            btnFoods.setPadding(15, 15, 15, 15);
            btnHomeCare.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background_unselected));
            btnHomeCare.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo);
        }
        if (view == btnHomeCare) {
            tipo = "HC";
            btnFoods.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background_unselected));
            btnFoods.setPadding(15, 15, 15, 15);
            btnHomeCare.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
            btnHomeCare.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo);
        }
        /*if (view == btnGallery) {
            showFileChooser();
        }*/
        if (view == btnCamera) {
            imageView = (ImageView) findViewById(R.id.ivFotoRotacion);
            cargarImagen();
        }
        if (view == btnSave) {
            guardar();
        }
    }

    public void enviarDatos() {
        obtenerFecha();
        if (imageView != null && imageView.getDrawable() != null) {
            if (esFormularioValido()) {
                String image = getStringImage(bitmapfinal);

                ContentValues values = new ContentValues();

                values.put(ContractInsertCanjes.Columnas.FOTO_GUIA, image);
                values.put(ContractInsertCanjes.Columnas.OBSERVACIONES, fecha);//Extra
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertCanjes.CONTENT_URI, values);
                if (VerificarNet.hayConexion(this)) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertFoto, null);
                    Toast.makeText(getApplicationContext(), "Guardando la información en el servidor.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Guardando la información en el teléfono. \n Deberá ser sincronizada", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
        }

    }


    public class CustomAdapterCanjes extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterCanjes.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BaseRotacion> values;

        public CustomAdapterCanjes(Context context, ArrayList<BaseRotacion> values) {
            this.context = context;
            this.values = values;
        }

        public CustomAdapterCanjes() {}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_canjes, parent, false);
            return new CustomAdapterCanjes.ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BaseRotacion mObject = values.get(position);
            //        boolean isSelected = listProductos.get(position).getSegmento().equals(sku_list);
            //      ((ItemViewHolder) holder).swQuiebre.setChecked(isSelected);
       /*     ((ItemViewHolder) holder).swQuiebre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    // ((ItemViewHolder) holder).myContext = compoundButton.getContext();
                        if (isChecked) {
                            ((ItemViewHolder) holder).swQuiebre,"SI: "+compoundButton.isChecked(),Toast.LENGTH_LONG;
                        }else {
                             Toast.makeText(((ItemViewHolder) holder).swQuiebre,"NO: "+compoundButton.isChecked(),Toast.LENGTH_LONG).show();
                              }
                        }
                    });*/

          /*  if (holder instanceof HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
                ((HeaderViewHolder) holder).headerTitle.setText("Producto");
                ((HeaderViewHolder) holder).headerTitle1.setText("Quiebre/Posible Quiebre");
                ((HeaderViewHolder) holder).headerTitle2.setText("U. Disponible");
                ((HeaderViewHolder) holder).headerTitle3.setText("Sugerido");
                ((HeaderViewHolder) holder).headerTitle4.setText("Cantidad");
                ((HeaderViewHolder) holder).headerTitle5.setText("Observaciones");
            }else */if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).lblSku.setText(mObject.getProducto());

                sesion = handler.getListGuardadoCanjes(user, codigo_pdv);
                for(int i = 0; i < sesion.size(); i++) {
                    if (mObject.getProducto().equalsIgnoreCase(sesion.get(i).getProducto())) {
                        ((ItemViewHolder) holder).spTipoCombo.setSelection(getIndex(((ItemViewHolder) holder).spTipoCombo, sesion.get(i).getTipo_combo()));
                        ((ItemViewHolder) holder).spVisita.setSelection(getIndex(((ItemViewHolder) holder).spVisita, sesion.get(i).getVisita()));
                        final Handler handler = new Handler();
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((ItemViewHolder) holder).spMecanica.setSelection(getIndex(((ItemViewHolder) holder).spMecanica, sesion.get(finalI).getMecanica()));
                            }
                        }, 1000);
                        ((ItemViewHolder) holder).txt_armados.setText(sesion.get(i).getCombos_armados());
                        ((ItemViewHolder) holder).txt_stock.setText(sesion.get(i).getStock());
                        ((ItemViewHolder) holder).txt_pvc_combo.setText(sesion.get(i).getPvc_combo());
                        ((ItemViewHolder) holder).txt_pvc_unitario.setText(sesion.get(i).getPvc_unitario());
                        ((ItemViewHolder) holder).txt_observaciones.setText(sesion.get(i).getObservaciones());

                        ((ItemViewHolder) holder).txt_armados.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gris));
                        ((ItemViewHolder) holder).txt_stock.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gris));
                        ((ItemViewHolder) holder).txt_pvc_combo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gris));
                        ((ItemViewHolder) holder).txt_pvc_unitario.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gris));
                        ((ItemViewHolder) holder).txt_observaciones.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gris));
                    }
                }
            }
        }

        /*private void setItemSelected(int position) {
            TAG.sel(listview.get(position).get(0));
            selected = list.get(position).get(0);
            notifyDataSetChanged();
        }*/

        //private method of your class
        private int getIndex(Spinner spinner, String myString) {
            for (int i=0;i<spinner.getCount();i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                    return i;
                }
            }

            return 0;
        }

        private BaseRotacion getItem(int position) {
            return values.get(position);
        }
        @Override
        public int getItemCount() {
            return values.size();
        }
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

//        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
//            listProductos = filteredList;
//            Log.i("LIST",listProductos.status()+"");
//            FlooringActivity.this.dataAdapter.notifyDataSetChanged();
//        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5, headerTitle6;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
                headerTitle1 = (TextView)itemView.findViewById(R.id.lblStock);
                headerTitle2 = (TextView)itemView.findViewById(R.id.lblSugerido);
//                headerTitle3 = (TextView)itemView.findViewById(R.id.lblCausal);
                headerTitle4 = (TextView)itemView.findViewById(R.id.lblDetalle);
                headerTitle5 = (TextView)itemView.findViewById(R.id.lblCaducidad);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener, View.OnClickListener {

            public TextView lblSku;
            public Spinner spTipoCombo;
            public Spinner spMecanica;
            public EditText txt_armados;
            public EditText txt_stock;
            public EditText txt_pvc_combo;
            public EditText txt_pvc_unitario;
            public Spinner spVisita;
            public EditText txt_observaciones;
            public ImageButton ib_foto;
            public CheckBox check;

            public ItemViewHolder(View itemView) {
                super(itemView);

                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
                spTipoCombo = (Spinner) itemView.findViewById(R.id.spTipoCombo);
                spMecanica = (Spinner) itemView.findViewById(R.id.spMecanica);
                txt_armados = (EditText) itemView.findViewById(R.id.txt_armados);
                txt_stock = (EditText) itemView.findViewById(R.id.txt_stock);
                txt_pvc_combo = (EditText) itemView.findViewById(R.id.txt_pvc_combo);
                txt_pvc_unitario = (EditText) itemView.findViewById(R.id.txt_pvc_unitario);
                spVisita = (Spinner) itemView.findViewById(R.id.spVisita);
                txt_observaciones = (EditText) itemView.findViewById(R.id.txt_observaciones);
                check = (CheckBox) itemView.findViewById(R.id.check);
                ib_foto = (ImageButton) itemView.findViewById(R.id.ibCargarFoto);

                txt_armados.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.negro));
                txt_stock.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.negro));
                txt_pvc_combo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.negro));
                txt_pvc_unitario.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.negro));
                txt_observaciones.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.negro));

                filtrarTipoCombo();

                if (validaPermisos()) {
                    btnCamera.setEnabled(true);
                }else{
                    btnCamera.setEnabled(false);
                }

                textWatcher(txt_armados);
                textWatcher(txt_stock);
                textWatcher(txt_pvc_combo);
                textWatcher(txt_pvc_unitario);
                textWatcher(txt_observaciones);

                ib_foto.setOnClickListener(this);
            }

            public void filtrarTipoCombo() {
                List<String> operadores = handler.getTipoComboCanjes();
                spTipoCombo.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, operadores));
                spTipoCombo.setOnItemSelectedListener(this);
            }

            public void filtrarMecanica(String tipo_combo) {
                List<String> operadores = handler.getMecanica(tipo_combo);
                spMecanica.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, operadores));
                spMecanica.setOnItemSelectedListener(this);
            }

            private void textWatcher(EditText et) {
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        et.setTextColor(ContextCompat.getColor(context, R.color.negro));
                    }
                });
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView== spTipoCombo) {
                    try{
                        String tipo = adapterView.getItemAtPosition(i).toString();
                        filtrarMecanica(tipo);
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}

            @Override
            public void onClick(View v) {
                if (v == ib_foto) {
                    imageView = (ImageView) itemView.findViewById(R.id.ivFoto);
                    cargarImagen();
                }
            }
        }
    }

    public void guardar() {
        View view;
        TextView tv_producto;
        Spinner sp_tipo_combo;
        Spinner sp_mecanica;
        EditText txt_armados;
        EditText txt_stock;
        EditText txt_pvc_combo;
        EditText txt_pvc_unitario;
        Spinner sp_visita;
        EditText txt_observaciones;
        ImageView iv_foto;
        CheckBox check;

        int values = 0, validos = 0, checked = 0;

        int listLength = listview.getChildCount();
        valueOfProductos = new String[listLength];
        valueOfTipoCombo = new String[listLength];
        valueOfMecanica = new String[listLength];
        valueOfArmados = new String[listLength];
        valueOfStock = new String[listLength];
        valueOfPvcCombo = new String[listLength];
        valueOfPvcUnitario = new String[listLength];
        valueOfVisita = new String[listLength];
        valueOfObservacion = new String[listLength];
        valueOfFoto = new String[listLength];

        for (int i = 0; i < listLength; i++) {
            view = listview.getChildAt(i);
            tv_producto = (TextView) view.findViewById(R.id.lblSku);
            sp_tipo_combo = (Spinner) view.findViewById(R.id.spTipoCombo);
            sp_mecanica = (Spinner) view.findViewById(R.id.spMecanica);
            txt_armados = (EditText) view.findViewById(R.id.txt_armados);
            txt_stock = (EditText) view.findViewById(R.id.txt_stock);
            txt_pvc_combo = (EditText) view.findViewById(R.id.txt_pvc_combo);
            txt_pvc_unitario = (EditText) view.findViewById(R.id.txt_pvc_unitario);
            sp_visita = (Spinner) view.findViewById(R.id.spVisita);
            txt_observaciones = (EditText) view.findViewById(R.id.txt_observaciones);
            iv_foto = (ImageView) view.findViewById(R.id.ivFoto);
            check = (CheckBox) view.findViewById(R.id.check);

            if (check.isChecked()) {
                checked++;
                String foto = null;
                Bitmap bitmapFoto = null;

                if (iv_foto != null && iv_foto.getDrawable() != null) {
                    bitmapFoto = ((BitmapDrawable) iv_foto.getDrawable()).getBitmap();
                    foto = getStringImage(bitmapFoto);
                }

                String producto = tv_producto.getText().toString();
                String tipo_combo = sp_tipo_combo.getSelectedItem().toString();
                String mecanica = sp_mecanica.getSelectedItem().toString();
                String armados = txt_armados.getText().toString();
                String stock = txt_stock.getText().toString();
                String pvc_combo = txt_pvc_combo.getText().toString();
                String pvc_unitario = txt_pvc_unitario.getText().toString();
                String visita = sp_visita.getSelectedItem().toString();
                String observacion = txt_observaciones.getText().toString();

                if (esFormularioValido(producto, tipo_combo, mecanica, armados, stock,
                    pvc_combo, pvc_unitario, visita, observacion, foto)) {
                    valueOfProductos[i] = producto;
                    valueOfTipoCombo[i] = tipo_combo;
                    valueOfMecanica[i] = mecanica;
                    valueOfArmados[i] = armados;
                    valueOfStock[i] = stock;
                    valueOfPvcCombo[i] = pvc_combo;
                    valueOfPvcUnitario[i] = pvc_unitario;
                    valueOfVisita[i] = visita;
                    valueOfObservacion[i] = observacion;
                    valueOfFoto[i] = foto;
                    validos++;
                } else {
                    check.setChecked(false);
                }
            }
            values++;
        }

        Log.i("VALUES", values + "");
        Log.i("CHECKED", checked + "");
        Log.i("VALIDOS", validos + "");

        if (checked > 0 && validos == checked) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CanjesActivity.this);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Confirmación");
            builder.setMessage("¿Desea guardar?");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    insertData();
                }
            });

            builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            android.app.AlertDialog ad = builder.create();
            ad.show();
        } else {
            Toast.makeText(getApplicationContext(),"Debe seleccionar algo para guardar", Toast.LENGTH_LONG).show();
        }
    }

    public boolean esFormularioValido(String producto, String tipo_combo, String mecanica, String armados,
                                      String stock, String pvc_combo, String pvc_unitario, String visita,
                                      String observacion, String foto) {
        if (tipo_combo.toUpperCase().contains("SELECCIONE")) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo de combo de " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mecanica.toUpperCase().contains("SELECCIONE")) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar la mecánica de " + producto
                    , Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pvc_combo.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar el PVC Combo " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pvc_unitario.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar el PVC Unitario " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (visita.toUpperCase().contains("SELECCIONE")) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar la visita " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (observacion.trim().isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Debe ingresar la observación " + producto, Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (Double.parseDouble(pvc_unitario) == 0) {
            Toast.makeText(getApplicationContext(), "El precio unitario no puede ser 0 en el producto: " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(pvc_unitario) > Double.parseDouble(pvc_combo)) {
            Toast.makeText(getApplicationContext(), "El precio unitario no puede ser mayor al precio combo en el producto: " + producto, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (foto == null) {
            Toast.makeText(this,"No has tomado la foto del producto " + producto,Toast.LENGTH_LONG).show();
            return false;
        }

        imageView = (ImageView) findViewById(R.id.ivFotoRotacion);
        if (imageView == null || imageView.getDrawable() == null) {
            Toast.makeText(this,"No has tomado la foto guía",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        scaleImage(imageView, bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    scaleImage(imageView, bitmap);
                    break;
            }
        }
    }

    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
    public void scaleImage(ImageView imageView, Bitmap bitmap) {
        try{
            int mheight = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            androidx.appcompat.app.AlertDialog alertDialog1;
            alertDialog1 = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(CanjesActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        openGallery();
                    }else{
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
        startActivityForResult(Intent.createChooser(intent,  "Seleccionar una imagen"), COD_SELECCIONA);
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
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
    }

    //Metodo que sube la imagen al servidor
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen canal, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //String to bitmap
    public Bitmap StringToBitMap(String encodedString) {
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    //Tomar Foto

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
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(CanjesActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        androidx.appcompat.app.AlertDialog.Builder dialogo=new androidx.appcompat.app.AlertDialog.Builder(CanjesActivity.this);
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

    public void obtenerFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        hora = hour.format(currentLocalTime);
//            return localTime;
    }

}