package com.luckyecuador.app.PintucoAPP;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

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

import com.luckyecuador.app.PintucoAPP.Clase.BaseRotacion;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RotacionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{


    ArrayList<BaseRotacion> type_name_copy = new ArrayList<BaseRotacion>();
    ArrayList<BaseRotacion> BaseRotacion = new ArrayList<BaseRotacion>();
    private Spinner spCategoria;
    private EditText txtObservaciones;
   // private Spinner spSubcategoria;
    //private Spinner spPresentacion;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    //private Spinner spMarca;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    //private EditText txtCantidad;

   //BASE SQLITE
    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, mecanica, observaciones;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora;

    DatabaseHelper handler;

    EditText txtsearch;
    TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    ListView listview;

    public ArrayList<BaseRotacion> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    RotacionActivity.CustomAdapterRotacion dataAdapter;

    private EditText txtventas, txtpregular, txtppromocion, txtcuotas, txtvcuotas;
    private String fechaventas, producto, poferta, sku, cuotas, vcuotas, format, presentacion, promocional;
    private String ciudad,retail,sucursal;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

      //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"BaseRotacion";

    private String fabricante = "ALICORP";

    String path;
    Button btnCamera;
    private Button btnGallery;
    private Button btnSave;

    MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotacion);
        setToolBar();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();
        marshMallowPermission = new MarshMallowPermission(this);

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        spCategoria = (Spinner)findViewById(R.id.spCategoria);
        btnCamera = (Button) findViewById(R.id.btnCameraLocal);
       // btnGallery= (Button) findViewById(R.id.btnGallery);
        btnSave= (Button) findViewById(R.id.btnSave);
        imageView= (ImageView) findViewById(R.id.ivFotoRotacion);
       // spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
       // spMarca = (Spinner)findViewById(R.id.spMarca);
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



        //consultaGuardado();

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);//

       filtrarCategoria();

        listview = (ListView) findViewById(R.id.lvSKUCode);
        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_rotacion_title,null,false);
        listview.addHeaderView(headerView,null,false);

        listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }

    public void filtrarCategoria() {
//        List<String> operadores = handler.getCategoriaRotacion();
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategoria.setAdapter(dataAdapter);
//        spCategoria.setOnItemSelectedListener(this);
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

   /* public void filtrarSubcategoria(String fabricante, String categoria) {
        List<String> operadores = handler.getSubcategoriaProdCad(fabricante,categoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }*/

    /*public void filtrarMarca(String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento1(subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
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

    public void showListView(String categoria) {
//        listProductos = handler.filtrarListRotacion(categoria);
        dataAdapter = new CustomAdapterRotacion(this, listProductos);
        if (!dataAdapter.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);
            //listview.setOnItemClickListener(this);

            txtSKUCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<BaseRotacion> type_name_filter = new ArrayList<BaseRotacion>();

                    String text = s.toString();

                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getProducto().toLowerCase()).contains(text.toLowerCase())) {
                            BaseRotacion p = new BaseRotacion();
                            p.setProducto(listProductos.get(i).getProducto());
                            type_name_filter.add(p);
                        }
                    }

                    type_name_copy = type_name_filter;
                    listUpdate(type_name_copy);
                    //filter(s.toString());
                    //dataAdapter.getFilter().filter(s);



                    dataAdapter.getFilter().filter(txtSKUCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                   /* ArrayList<BaseRotacion> type_name_filter = new ArrayList<>();

                    String text = s.toString();

                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getProducto().toLowerCase()).contains(text.toLowerCase())) {
                            BaseRotacion p = new BaseRotacion();
                            p.setProducto(listProductos.get(i).getProducto());
                            type_name_filter.add(p);
                        }
                    }

                    type_name_copy = type_name_filter;
                    listUpdate(type_name_copy);*/

                    /*PreciosActivity.this.dataAdapter.getFilter().filter(s);
                    dataAdapter.notifyDataSetChanged();*/
                }
            });
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<BaseRotacion> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterRotacion(this, data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView== spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                //filtrarCategoria(categoria);
                showListView(categoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
       /* if (adapterView== spSubcategoria) {
            try{
                subcategoria =adapterView.getItemAtPosition(i).toString();
                filtrarMarca(fabricante,categoria, subcategoria);
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
        /*if (adapterView== spMarca) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
                showListView(fabricante,categoria,subcategoria,brand);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

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
        builder.setTitle("BaseRotacion");
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_precios,null));

        //Traer Views
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
       /* lblModelo.setText(getString(R.string.model) +" :    "+skuSelected+"\n");

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



    public void insertData(String skuSelected,String fecha_rot, String cantidad, String mecanica) {
        if (esFormularioValido()) {
            try {
                String image = "NO_FOTO";
                if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                    image = getStringImage(bitmapfinal);
                }/* else {
                    Toast.makeText(this, "Toma una foto para enviar datos", Toast.LENGTH_LONG).show();
                }*/
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
                String observaciones = txtObservaciones.getText().toString();
                /*String subcategoria = spSubcategoria.getSelectedItem().toString();
                String brand = spMarca.getSelectedItem().toString();*/
                // String manufacturer = handler.getManufacturerPrecios(categoria);

                values.put(ContractInsertRotacion.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertRotacion.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertRotacion.Columnas.USUARIO, user);
                values.put(ContractInsertRotacion.Columnas.SUPERVISOR, punto_venta);
                values.put(ContractInsertRotacion.Columnas.FECHA, fechaser);
                values.put(ContractInsertRotacion.Columnas.HORA, horaser);
                values.put(ContractInsertRotacion.Columnas.CATEGORIA, categoria);
                values.put(ContractInsertRotacion.Columnas.PRODUCTO, skuSelected);
                values.put(ContractInsertRotacion.Columnas.PROMOCIONAL, "");
                values.put(ContractInsertRotacion.Columnas.MECANICA, mecanica);
                values.put(ContractInsertRotacion.Columnas.PESO, "");
                values.put(ContractInsertRotacion.Columnas.CANTIDAD, cantidad);
                values.put(ContractInsertRotacion.Columnas.FECHA_ROT, fecha_rot);
                values.put(ContractInsertRotacion.Columnas.FOTO_GUIA, image);
                values.put(ContractInsertRotacion.Columnas.OBSERVACIONES, observaciones);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertRotacion.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertRotacion, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RotacionActivity.this);
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

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RotacionActivity.this);
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


    public void onClick(View view) {
        /*if (view == btnGallery) {
            showFileChooser();
        }*/
        if (view == btnCamera) {
            cargarImagen();

        }
        if (view == btnSave) {
            enviarDatos();
        }
    }

    public void enviarDatos() {
        obtenerFecha();
        if (imageView != null && imageView.getDrawable() != null) {
            if (esFormularioValido()) {
                String image = getStringImage(bitmapfinal);

                ContentValues values = new ContentValues();

                values.put(ContractInsertRotacion.Columnas.FOTO_GUIA, image);
                values.put(ContractInsertRotacion.Columnas.OBSERVACIONES, fecha);//Extra
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertRotacion.CONTENT_URI, values);
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


    public class CustomAdapterRotacion extends ArrayAdapter<BaseRotacion> implements Filterable {

        public ArrayList<BaseRotacion> values;
        public Context context;
        boolean[] checkBoxState;

        public CustomAdapterRotacion(Context context, ArrayList<BaseRotacion> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState = new boolean[values.size()];
        }

        public class ViewHolder{
            TextView lblSku;
            CheckBox checkGuardar; //agregado GT
            TextView txtFecha;
            EditText txtCantidad;
            Spinner spMecanica;
            ImageButton btnFechaRotacion;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return values.size();
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Obtener Instancia Inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder vHolder = null;
            //Comprobar si el View existe
            //Si no existe inflarlo
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_rotacion, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.spMecanica = (Spinner)convertView.findViewById(R.id.spMecanica);
                vHolder.txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);
                vHolder.checkGuardar = (CheckBox) convertView.findViewById(R.id.checkGuardar);
                vHolder.txtCantidad = (EditText) convertView.findViewById(R.id.txtCantidad);
                vHolder.btnFechaRotacion = (ImageButton) convertView.findViewById(R.id.btnFechaRotacion);

                InputFilter filter = new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; ++i) {
                            if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                                return "";
                            }
                        }
                        return null;
                    }
                };

                /*vHolder.txtFecha.setFilters(new InputFilter[]{new InputFilter() {
                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        int indexPoint = dest.toString().indexOf(decimalFormatSymbols.getDecimalSeparator());
                        if (indexPoint == -1)
                            return source;

                        int decimals = dend - (indexPoint+1);
                        return decimals < 2 ? source : "";
                    }
                }
                });*/

                vHolder.txtCantidad.setFilters(new InputFilter[]{new InputFilter() {
                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        int indexPoint = dest.toString().indexOf(decimalFormatSymbols.getDecimalSeparator());
                        if (indexPoint == -1)
                            return source;

                        int decimals = dend - (indexPoint+1);
                        return decimals < 2 ? source : "";
                    }
                }
                });

                //vHolder.txtFecha.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
                vHolder.txtCantidad.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});

                final ViewHolder finalv = vHolder;

                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            if (values.size() > 0) {
                String producto = values.get(position).getProducto();
                vHolder.lblSku.setText(values.get(position).getProducto());

                final ViewHolder finalv = vHolder;


                vHolder.spMecanica.setVisibility(View.VISIBLE);
                ArrayList<String> mecanica = handler.filtrarListMecanicasRotacion(producto, categoria);
                vHolder.spMecanica.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, mecanica));



                vHolder.btnFechaRotacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //finalv.txtCantidad.setText("PRUEBA");
                        final Calendar calendar = Calendar.getInstance();
                        int anio = calendar.get(Calendar.YEAR);
                        int mes = calendar.get(Calendar.MONTH);
                        int dia = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog from_dateListener = new DatePickerDialog(RotacionActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = null;
                                try {
                                    date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String outDate = dateFormat.format(date);
                                finalv.txtFecha.setText(outDate);
//                                dataAdapter.notifyDataSetChanged();
                            }
                        },anio,mes,dia);
                        from_dateListener.show();
                    }
                });

                vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        fecha = finalv.txtFecha.getText().toString().trim();
                        cantidad = finalv.txtCantidad.getText().toString();
                        final String mecanica = finalv.spMecanica.getSelectedItem().toString().trim();
                        sku = finalv.lblSku.getText().toString();
                        if (((CheckBox)v).isChecked()) {
                            if (!fecha.equals("") && fecha != null &&
                            !mecanica.equalsIgnoreCase("Seleccione") && !sku.equals("") && sku != null &&
                                    !cantidad.equals("") && cantidad != null) {
                                insertData(sku, fecha, cantidad, mecanica);
                            } else {
                                Toast.makeText(getApplicationContext(), "No ingresaste la fecha o la cantidad", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }
    }

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RotacionActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        /* Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);*/
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
        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
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

        //
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                   /* Uri miPath=data.getData();
                    imageView.setImageURI(miPath);*/
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        //Setear el ImageView con el Bitmap
                        scaleImage(bitmap);
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
                    scaleImage(bitmap);
                    break;
            }
        }

    }


    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
    public void scaleImage(Bitmap bitmap) {
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
            Log.e("compressBitmap", "Error on compress file: " + e.getMessage());
        }
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





