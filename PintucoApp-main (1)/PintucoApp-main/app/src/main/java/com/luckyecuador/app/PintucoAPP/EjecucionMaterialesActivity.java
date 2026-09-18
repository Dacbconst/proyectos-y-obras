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
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.luckyecuador.app.PintucoAPP.Clase.BaseAlertas;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRotacion;
import com.luckyecuador.app.PintucoAPP.Clase.InsertCanjes;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

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
import java.util.TimeZone;

public class EjecucionMaterialesActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

    ArrayList<BaseRotacion> type_name_copy = new ArrayList<BaseRotacion>();
    ArrayList<InsertCanjes> sesion = new ArrayList<InsertCanjes>();
    private Spinner spAlerta;
    private Spinner spTipo;
//    private Spinner spMarca;
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
    private String tipo, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, mecanica, observaciones;
    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora;
    private String alerta = "POP";
//    private String tipo;

    DatabaseHelper handler;

    EditText txtsearch;
    TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    RecyclerView listview;

    public ArrayList<BaseAlertas> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    CustomAdapterEjecucionMateriales dataAdapter;

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

    private EditText lPDV;
    private EditText lPDVNombre;

    MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecucion_materiales);
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

        spAlerta = (Spinner)findViewById(R.id.spAlerta);
        spTipo = (Spinner)findViewById(R.id.spTipo);
//        spMarca = (Spinner)findViewById(R.id.spMarca);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        //spPresentacion = (Spinner)findViewById(R.id.spPresentacion);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) findViewById(R.id.txtSKUCode);
        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
       // txtDescripcion  = (EditText)findViewById(R.id.txtDescripcionSKU);

        //lSeleccioneProducto.setText("Seleccione Producto: "+ format);

        layout_skuName = (LinearLayout) findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) findViewById(R.id.layout_skuDescripcion);

        lPDV = (EditText) findViewById(R.id.lPDV);
        lPDVNombre = (EditText) findViewById(R.id.lPDVNombre);

        lPDV.setText(codigo_pdv.toUpperCase());
        lPDVNombre.setText(punto_venta.toUpperCase());

        imageView = (ImageView) findViewById(R.id.ivFoto);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        filtrarCodigoSKU();
//        filtrarAlerta();
        filtrarTipo(alerta);
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

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }

    public void filtrarAlerta() {
        List<String> operadores = handler.getAlertas();
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAlerta.setAdapter(dataAdapter);
        spAlerta.setOnItemSelectedListener(this);
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

    public void filtrarTipo(String alerta) {
        List<String> operadores = handler.getTipoEjecucionMateriales(alerta);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(dataAdapter);
        spTipo.setOnItemSelectedListener(this);
    }

    /*public void filtrarMarca(String tipo, String categoria, String subcategoria) {
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

    public void filtrarSegmento(String subcategoria, String subcategoria, String subcategoria) {
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

    public void showListView(String alerta, String tipo) {
        listProductos = handler.filtrarListEjecucionMateriales(alerta, tipo);

        for (int i=0; i<listProductos.size(); i++) {
            Log.i("MATERIAL", listProductos.get(i).getMaterial());
        }

        dataAdapter = new CustomAdapterEjecucionMateriales(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EjecucionMaterialesActivity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterEjecucionMateriales customAdapter = new CustomAdapterEjecucionMateriales(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            dataAdapter = (CustomAdapterEjecucionMateriales) listview.getAdapter();
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
//        if (adapterView== spAlerta) {
//            try{
//                alerta = adapterView.getItemAtPosition(i).toString();
//                filtrarTipo(alerta);
//            }catch (Exception e) {
//                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
        if (adapterView == spTipo) {
            try{
                tipo = adapterView.getItemAtPosition(i).toString();
                showListView(alerta, tipo);
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
        }
        if (adapterView== spMarca) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
//                showListView(fabricante,categoria,subcategoria,brand);
                showListView(tipo, alerta, subcategoria, brand);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView== spPresentacion) {
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

    public void insertData(String tipo, String material, String estado, String prioridad, String observacion) {
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

                BasePharmaValue pdv = handler.getPdv(codigo_pdv);
                
                ContentValues values = new ContentValues();

                values.put(ContractInsertEjecucionMateriales.Columnas.PHARMA_ID, pdv.getPos_id());
                values.put(ContractInsertEjecucionMateriales.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertEjecucionMateriales.Columnas.CANAL, pdv.getChannel());
                values.put(ContractInsertEjecucionMateriales.Columnas.NOMBRE_COMERCIAL, pdv.getCustomer_owner());
                values.put(ContractInsertEjecucionMateriales.Columnas.LOCAL, pdv.getPos_name());
                values.put(ContractInsertEjecucionMateriales.Columnas.REGION, pdv.getRegion());
                values.put(ContractInsertEjecucionMateriales.Columnas.PROVINCIA, pdv.getProvince());
                values.put(ContractInsertEjecucionMateriales.Columnas.CIUDAD, pdv.getCity());
                values.put(ContractInsertEjecucionMateriales.Columnas.ZONA, pdv.getZone());
                values.put(ContractInsertEjecucionMateriales.Columnas.DIRECCION, pdv.getAddress());
                values.put(ContractInsertEjecucionMateriales.Columnas.SUPERVISOR, pdv.getSupervisor());
                values.put(ContractInsertEjecucionMateriales.Columnas.MERCADERISTA, pdv.getMercaderista());
                values.put(ContractInsertEjecucionMateriales.Columnas.USUARIO, user);
                values.put(ContractInsertEjecucionMateriales.Columnas.LATITUD, pdv.getLatitud());
                values.put(ContractInsertEjecucionMateriales.Columnas.LONGITUD, pdv.getLongitud());
                values.put(ContractInsertEjecucionMateriales.Columnas.TERRITORIO, pdv.getKam());
                values.put(ContractInsertEjecucionMateriales.Columnas.ZONA_TERRITORIO, pdv.getTipo());
                values.put(ContractInsertEjecucionMateriales.Columnas.TIPO, tipo);
                values.put(ContractInsertEjecucionMateriales.Columnas.MATERIAL, material);
                values.put(ContractInsertEjecucionMateriales.Columnas.ESTADO_MATERIAL, estado);
                values.put(ContractInsertEjecucionMateriales.Columnas.PRIORIDAD, prioridad);
                values.put(ContractInsertEjecucionMateriales.Columnas.OBSERVACIONES, observacion);
                values.put(ContractInsertEjecucionMateriales.Columnas.FOTO, image);
                values.put(ContractInsertEjecucionMateriales.Columnas.FECHA, fechaser);
                values.put(ContractInsertEjecucionMateriales.Columnas.HORA, horaser);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertEjecucionMateriales.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertEjecucionMateriales, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
//        }
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
//        if (view == btnFoods) {
//            tipo = "F";
//            btnFoods.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
//            btnFoods.setPadding(15, 15, 15, 15);
//            btnHomeCare.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background_unselected));
//            btnHomeCare.setPadding(15, 15, 15, 15);
//            filtrarAlerta();
//        }
//        if (view == btnHomeCare) {
//            tipo = "HC";
//            btnFoods.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background_unselected));
//            btnFoods.setPadding(15, 15, 15, 15);
//            btnHomeCare.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button_background));
//            btnHomeCare.setPadding(15, 15, 15, 15);
//            filtrarAlerta();
//        }
        /*if (view == btnGallery) {
            showFileChooser();
        }*/
        if (view == btnCamera) {
//            imageView = (ImageView) findViewById(R.id.ivFotoRotacion);
            cargarImagen();
        }
//        if (view == btnSave) {
//            enviarDatos();
//        }
    }

    public class CustomAdapterEjecucionMateriales extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterEjecucionMateriales.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BaseAlertas> values;

        public CustomAdapterEjecucionMateriales(Context context, ArrayList<BaseAlertas> values) {
            this.context = context;
            this.values = values;
        }

        public CustomAdapterEjecucionMateriales() {}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_ejecucion_materiales, parent, false);
            return new ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BaseAlertas mObject = values.get(position);
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
                ((ItemViewHolder) holder).lblSku.setText(mObject.getMaterial());
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

        private BaseAlertas getItem(int position) {
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

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView lblSku;
            public Spinner spEstado;
            public Spinner spPrioridad;
            public ImageButton ib_foto;
            public CheckBox check;

            public ItemViewHolder(View itemView) {
                super(itemView);

                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
                spEstado = (Spinner) itemView.findViewById(R.id.spEstado);
                spPrioridad = (Spinner) itemView.findViewById(R.id.spPrioridad);
                check = (CheckBox) itemView.findViewById(R.id.check);
                ib_foto = (ImageButton) itemView.findViewById(R.id.ibCargarFoto);

                if (validaPermisos()) {
                    btnCamera.setEnabled(true);
                }else{
                    btnCamera.setEnabled(false);
                }

                check.setOnClickListener(this);
                ib_foto.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v == check) {
                    if (((CheckBox)v).isChecked()) {
                        String tipo = spTipo.getSelectedItem().toString();
                        String material = lblSku.getText().toString();
                        String estado = spEstado.getSelectedItem().toString();
                        String prioridad = spPrioridad.getSelectedItem().toString();
                        String observacion = txtObservaciones.getText().toString();
                        if (esFormularioValido(tipo, material, estado, prioridad, observacion)) {
                            alertDialogGuardar("Confirmación", "¿Desea guardar?", tipo, material, estado, prioridad, observacion);
                        } else {
                            check.setChecked(false);
                        }
                    }
                }
                if (v == ib_foto) {
//                    imageView = (ImageView) itemView.findViewById(R.id.ivFoto);
                    cargarImagen();
                }
            }
        }
    }

    private void alertDialogGuardar(String titulo, String mensaje, String tipo,
                                    String material, String estado, String prioridad, String observacion) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EjecucionMaterialesActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Desea guardar?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertData(tipo, material, estado, prioridad, observacion);
            }
        });

        builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        android.app.AlertDialog ad = builder.create();
        ad.show();
    }


    public boolean esFormularioValido(String tipo, String material, String estado,
                                      String prioridad, String observacion) {
        if (tipo.toUpperCase().contains("SELECCIONE")) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo de " + material, Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (estado.toUpperCase().contains("SELECCIONE")) {
//            Toast.makeText(getApplicationContext(), "Debe seleccionar el estado de " + material, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (prioridad.toUpperCase().contains("SELECCIONE")) {
//            Toast.makeText(getApplicationContext(), "Debe seleccionar la prioridad de " + material, Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (observacion.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar la observación " + material, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageView == null || imageView.getDrawable() == null) {
            Toast.makeText(this,"No has tomado la foto",Toast.LENGTH_LONG).show();
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
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(EjecucionMaterialesActivity.this);
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
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(EjecucionMaterialesActivity.this);
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
        androidx.appcompat.app.AlertDialog.Builder dialogo=new androidx.appcompat.app.AlertDialog.Builder(EjecucionMaterialesActivity.this);
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