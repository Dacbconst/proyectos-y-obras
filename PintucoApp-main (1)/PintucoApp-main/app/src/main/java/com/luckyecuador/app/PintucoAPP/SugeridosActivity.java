package com.luckyecuador.app.PintucoAPP;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class SugeridosActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = SugeridosActivity.class.getSimpleName();

//    ArrayList<Valores> sesion = new ArrayList<Valores>();
    ArrayList<BasePortafolioProductos> codifican = new ArrayList<BasePortafolioProductos>();
    ArrayList<BasePortafolioProductos> precios = new ArrayList<BasePortafolioProductos>();
    ArrayList<BasePortafolioProductos> sesion = new ArrayList<BasePortafolioProductos>();
    ArrayList<String> no_codifica = new ArrayList<>();
    ArrayList<String> codifica = new ArrayList<>();
    ArrayList<String> combo_si_no = new ArrayList<>();
    ArrayList<String> responsable_autoservicio = new ArrayList<>();
    ArrayList<String> responsable_mayorista = new ArrayList<>();
    ArrayList<String> disponible = new ArrayList<>();

    ArrayList<String> razon_logistica = new ArrayList<>();
    ArrayList<String> razon_cadena_SSA = new ArrayList<>();
    ArrayList<String> razon_cadena_C = new ArrayList<>();
    ArrayList<String> razon_cadena_comisariato = new ArrayList<>();
    ArrayList<String> razon_cadena_tia = new ArrayList<>();
    ArrayList<String> razon_na = new ArrayList<>();
    ArrayList<String> razon_alicorp = new ArrayList<>();
    ArrayList<String> razon_pydaco = new ArrayList<>();
    ArrayList<String> razon_pdv = new ArrayList<>();
    ArrayList<String> razon_lucky = new ArrayList<>();
    ArrayList<String> seleccione = new ArrayList<>();

    ArrayList<BasePortafolioProductos> type_name_copy = new ArrayList<BasePortafolioProductos>();
    private Spinner spCategoria;
    private Spinner spSubCategoria;
    private Spinner spBrand;
    private Spinner spContenido;
    private Spinner spProducto;
    private Button btnGuardar;
    //private Spinner spSubcategoria;
    //private Spinner spSegmento;
    //private Spinner spTamano;
    //private Spinner spCantidad;

    private EditText txtprecio_contado;
    private EditText txtprecio_credito;
    private EditText txtcuotas;
    private EditText txtvalor_cuotas;
    private EditText txtSkuCode;
    private EditText txtDescripcionSKU;

    String[] valueOfSkuTextView;
    String[] valueOfQuiebreSwitch;
    String[] valueOfCantDispEditText;
    String[] valueOfSugeridoSwitch;
    String[] valueOfCantSugEditText;
    String[] valueOfObservacionesEditText;
    String[] valueOfEntregaSpinner;

    private TextView txtLocal;
    private TextView txtCodigoFabril;
    private TextView txtVendedor;

    float battery;
    String mensaje="";

    private final String manufacturer = "Alicorp";

    //TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    RecyclerView listview;
    ArrayList<BasePortafolioProductos> listProductos;
    //ArrayList<BasePortafolioProductos> mainlistProductos;

    CustomAdapterSugeridos dataAdapter;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    DatabaseHelper handler;

    private String categoria, subcategoria, producto, segmento1, segmento2, brand, contenido, tamano, cantidad, codigo, descripcion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora, format;
    String  marca, tipo, fechaventas, pcontado, pcredito, cuota, vcuota;
    String sku, quiebre, cantidad_disponible, sugerido, cantidad_sugerido, observaciones, entrega;
    private String nom_comercial, canal, subcanal, vendedor, celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugeridos);
        setToolbar();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();

        Log.i("NOMBRECOM", nom_comercial);
        Log.i("CANAL", canal);

        txtLocal = (TextView) findViewById(R.id.txtLocal);
        txtCodigoFabril = (TextView) findViewById(R.id.txtCodigoFabril);
        txtVendedor = (TextView) findViewById(R.id.txtVendedor);
        txtLocal.setText(punto_venta);
        txtCodigoFabril.setText(format);
        txtVendedor.setText(vendedor);


        //startService(new Intent(getApplicationContext(), MyService.class));

        //lSeleccioneProducto = (TextView)findViewById(R.id.lSeleccioneProducto);
        spCategoria = (Spinner)findViewById(R.id.spSector);
        spSubCategoria = (Spinner)findViewById(R.id.spCategoria);
        spBrand = (Spinner)findViewById(R.id.spMarca);
        spContenido = (Spinner)findViewById(R.id.spContenido);
        spProducto = (Spinner)findViewById(R.id.spProducto);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);

        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSkuCode = (EditText)findViewById(R.id.txtSKUCode);
        txtDescripcionSKU = (EditText)findViewById(R.id.txtDescripcionSKU);

        //lSeleccioneProducto.setText("Seleccione Producto: "+ format);

        layout_skuName = (LinearLayout)findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout)findViewById(R.id.layout_skuDescripcion);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

       // txtfechav = (TextView)findViewById(R.id.txtFecha);
       /* btnFecha = (ImageButton)findViewById(R.id.btnFecha);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogoFecha().show(getSupportFragmentManager(), "EscogerFecha");
            }
        });*/

        filtrarCategoria();

     //   llenadoArray();

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        listview = (RecyclerView)findViewById(R.id.lvSKUCode);
        listview.setHasFixedSize(true);


        //RecyclerView listView = (RecyclerView) findViewById(R.id.lvSKUCode);
        /*listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
    }

    /*private void onScrolledToBottom() {
        if (mainlistProductos.status() < listProductos.status()) {
            int x, y;
            if ((listProductos.status() - mainlistProductos.status()) >= 50) {
                x = mainlistProductos.status();
                y = x + 50;
            } else {
                x = mainlistProductos.status();
                y = x + listProductos.status() - mainlistProductos.status();
            }
            for (int i = x; i < y; i++) {
                mainlistProductos.add(listProductos.get(i));
            }
            dataAdapter.notifyDataSetChanged();
        }
    }*/
 /*   public void llenadoArray() {
        seleccione.add("Seleccione");

        disponible.add("Seleccione");
        disponible.add("Si");
        disponible.add("No");

        combo_si_no.add("Seleccione");
        combo_si_no.add("Si");
        combo_si_no.add("No");

//        codifica.add("Seleccione");
        codifica.add("Si");
        codifica.add("No");

        responsable_autoservicio.add("Seleccione");
        responsable_autoservicio.add("Logística"); //Antes Alicorp (envía a razon_cadena_SSA)
        responsable_autoservicio.add("Cadena");
        responsable_autoservicio.add("Alicorp");

        responsable_mayorista.add("Seleccione");
        responsable_mayorista.add("Pydaco"); //Antes Alicorp (envía a razon_cadena_SSA)
        responsable_mayorista.add("PDV");
        //responsable_mayorista.add("Lucky");

        razon_logistica.add("Seleccione");
        razon_logistica.add("No llega la mercaderia el día asignado");

        razon_cadena_SSA.add("Seleccione");
        razon_cadena_SSA.add("Descodificación de producto");
        razon_cadena_SSA.add("PDV Compra solo para percha");
        razon_cadena_SSA.add("PDV con Frecuencia");
        razon_cadena_SSA.add("Incremento de Rotación");
        razon_cadena_SSA.add("Incremento de Rotación por promoción");
        razon_cadena_SSA.add("Quiebre de Competencia");
        razon_cadena_SSA.add("No hay despacho en Bodega Central");

        razon_cadena_C.add("Seleccione");
        razon_cadena_C.add("Descodificación de producto");
        razon_cadena_C.add("PDV Compra solo para percha");
        razon_cadena_C.add("PDV con Frecuencia");
        razon_cadena_C.add("Incremento de Rotación");
        razon_cadena_C.add("Incremento de Rotación por promoción");
        razon_cadena_C.add("Quiebre de Competencia");
        razon_cadena_C.add("No hay despacho en Bodega Central");
        razon_cadena_C.add("No Genera Suficiente el Sistema");

        razon_cadena_comisariato.add("Seleccione");
        razon_cadena_comisariato.add("Descodificación de producto");
        razon_cadena_comisariato.add("PDV Compra solo para percha");
        razon_cadena_comisariato.add("PDV con Frecuencia");
        razon_cadena_comisariato.add("Incremento de Rotación");
        razon_cadena_comisariato.add("Incremento de Rotación por promoción");
        razon_cadena_comisariato.add("Quiebre de Competencia");
        razon_cadena_comisariato.add("No hay despacho en Bodega Central");
        razon_cadena_comisariato.add("No genera lo suficiente en el sistema (SAP)");

        razon_cadena_tia.add("Seleccione");
        razon_cadena_tia.add("Descodificación de producto");
        razon_cadena_tia.add("PDV Compra solo para percha");
        razon_cadena_tia.add("PDV con Frecuencia");
        razon_cadena_tia.add("Incremento de Rotación");
        razon_cadena_tia.add("Incremento de Rotación por promoción");
        razon_cadena_tia.add("Quiebre de Competencia");
        razon_cadena_tia.add("No hay stock en CND");

        razon_alicorp.add("Seleccione");
        razon_alicorp.add("Quiebre en Bodega Alicorp");

        razon_pydaco.add("Seleccione");
        razon_pydaco.add("Producto contaminado en Bodega");
        razon_pydaco.add("Producto no Disponible en Bodega");
        razon_pydaco.add("Retraso de Entrega");
        razon_pydaco.add("Vendedor no Visita");

        razon_pdv.add("Seleccione");
        razon_pdv.add("Descodificación de producto");
        razon_pdv.add("PDV Compra solo para percha");
        razon_pdv.add("Incremento en ventas");
        razon_pdv.add("Problema de cartera");
        razon_pdv.add("Cliente No Compra por Presupuesto");
        razon_pdv.add("Stocking en Bodega");

        razon_lucky.add("Seleccione");
        razon_lucky.add("Producto encontrado en bodega");

        no_codifica.add("NA");
    }*/


    public void filtrarCategoria() {
        List<String> operadores = handler.getCategoriaSugeridos(codigo_pdv, canal, subcanal, manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubCategoria(String categoria) {
        List<String> operadores = handler.getSubCategoriaSugeridos(categoria, codigo_pdv, canal, subcanal, manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategoria.setAdapter(dataAdapter);
        spSubCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String categoria, String subcategoria) {
        List<String> operadores = handler.getBrandValores2(categoria, subcategoria, codigo_pdv, canal, subcanal,manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }

    public void filtrarContenido(String categoria, String subcategoria, String brand) {
        List<String> operadores = handler.getContenidoSugeridos(categoria, subcategoria, brand, codigo_pdv, canal, subcanal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spContenido.setAdapter(dataAdapter);
        spContenido.setOnItemSelectedListener(this);
    }

   /* public void filtrarMarca(String logro, String subcategoria) {
        List<String> operadores = handler.getSegmento1(subcategoria,logro);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }*/

 /*   public void filtrarSegmento(String logro, String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento2(subcategoria,logro,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }*/

 /*   public void filtrarBrand(String categoria, String subcategoria) {
        List<String> operadores = handler.getBrandValores2(categoria, subcategoria, codigo_pdv, canal, subcanal,manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }*/

  /*  public void filtrarProducto(String categoria, String subcategoria) {
        List<String> operadores = handler.getProductosSugeridos2(categoria, subcategoria, codigo_pdv, canal, subcanal,manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProducto.setAdapter(dataAdapter);
        spProducto.setOnItemSelectedListener(this);
    }*/



    /*public void filtrartamano(String logro, String subcategoria, String subcategoria, String presentacion, String contenido) {
        List<String> operadores = handler.getTamano(subcategoria,logro,subcategoria,presentacion,contenido);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTamano.setAdapter(dataAdapter);
        spTamano.setOnItemSelectedListener(this);
    }

    public void filtrarcantidad(String logro, String subcategoria, String subcategoria, String presentacion, String contenido, String tamano) {
        List<String> operadores = handler.getCantidad(subcategoria,logro,subcategoria,presentacion,contenido, tamano);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(dataAdapter);
        spCantidad.setOnItemSelectedListener(this);
    }*/

    public void showListView(String categoria, String subcategoria, String brand, String contenido, String manufacturer) {
 //   public void showListView(String categoria, String subcategoria, String producto, String manufacturer) {
   //     listProductos = handler.filtrarListProductosSugeridos(categoria, subcategoria, brand, contenido, codigo_pdv, canal, subcanal, manufacturer);
        listProductos = handler.filtrarListSugProductos(categoria, subcategoria, brand, contenido, codigo_pdv, canal, subcanal, manufacturer);
        codifican = new ArrayList<BasePortafolioProductos>();
        precios = new ArrayList<BasePortafolioProductos>();

//        Con Marca
//        codifican = handler.filtrarListProductosCodificanValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
//        precios = handler.filtrarListProductosPreciosValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);

        //        Sin Marca
    //    codifican = handler.filtrarListProductosCodificanValoresSinBrand(categoria, subcategoria, codigo_pdv, canal, subcanal, manufacturer);
   //      precios = handler.filtrarListProductosPreciosValoresSinBrand(categoria, subcategoria, codigo_pdv, canal, subcanal, manufacturer);

        /*if (listProductos.status() > 30) {
            mainlistProductos = new ArrayList<>(listProductos.subList(0,30));
        } else {
            mainlistProductos = listProductos;
        }

        Log.i("List Original",listProductos.status()+"");
        Log.i("List Modificada",mainlistProductos.status()+"");*/

        dataAdapter = new CustomAdapterSugeridos(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SugeridosActivity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterSugeridos customAdapter = new CustomAdapterSugeridos(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            /*listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1))
                        onScrolledToBottom();
                }
            });*/

            //listview.setLayoutManager(new LinearLayoutManager(this));
            //listview.setAdapter(dataAdapter);
            //listview.setOnItemClickListener(this);

            dataAdapter = (CustomAdapterSugeridos) listview.getAdapter();

            txtSkuCode.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<BasePortafolioProductos>();

                    String text = s.toString();

                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
                            BasePortafolioProductos p = new BasePortafolioProductos();
                            p.setSku(listProductos.get(i).getSku());
                            type_name_filter.add(p);
                        }
                    }

                    type_name_copy = type_name_filter;
                    listUpdate(type_name_copy);
                    //filter(s.toString());
                    //dataAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //filter(s.toString());
                }
            });

        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<BasePortafolioProductos> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterSugeridos(this, data));
        } else {
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    /*public void filter(String text) {
        ArrayList<BasePortafolioProductos> filteredList = new ArrayList<>();
        for(int i = 0; i < listProductos.status(); i++) {
            Log.i("PRODUCTO",listProductos.get(i).getSku());
        }
        for (BasePortafolioProductos item : listProductos) {
            if (item.getSku().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        dataAdapter.filterList(filteredList);
    }*/

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        tipo=sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        nom_comercial = sharedPreferences.getString(Constantes.NOM_COMERCIAL, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal =sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
        vendedor =sharedPreferences.getString(Constantes.VENDEDOR,Constantes.NODATA);
        celular =sharedPreferences.getString(Constantes.CELULAR,Constantes.NODATA);

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSubCategoria(categoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView == spSubCategoria)
        {
            try{
                subcategoria =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                //filtrarBrand(categoria, subcategoria);
                filtrarBrand(categoria, subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView==spBrand) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(logro,subcategoria,subcategoria,presentacion,contenido);
                //showListView(categoria, subcategoria, producto, manufacturer);
                filtrarContenido(categoria, subcategoria, brand);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView==spContenido) {
            try{
                contenido=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(logro,subcategoria,subcategoria,presentacion,contenido);
                //showListView(categoria, subcategoria, producto, manufacturer);
                showListView(categoria, subcategoria, brand, contenido, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
       /* if (adapterView == spProducto)
        {
            try{
                producto =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                //filtrarBrand(categoria, subcategoria);
                showListView(categoria, subcategoria, producto, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
        /*if (adapterView==spSubcategoria)
        {
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                filtrarSegmento(logro,subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView==spSegmento)
        {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                filtrarMarca(logro,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

    /*    if (adapterView==spBrand) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(logro,subcategoria,subcategoria,presentacion,contenido);
                //showListView(categoria, subcategoria, brand, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

        /*if (adapterView==spTamano)
        {
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(logro,subcategoria,subcategoria,presentacion,contenido,tamano);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spCantidad) {
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(logro,subcategoria,contenido);
                // showListDescripcion(channel,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String model = parent.getItemAtPosition(position).toString();
        alertDialog(model);
    }

    public void alertDialog(final String skuSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_valores, null);
        //Title
        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle("Codificados");
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_valores,null));

        //Traer Views
        TextView lblModelo=(TextView)dialogView.findViewById(R.id.lblModelo);
        final Spinner spCodifica = (Spinner)dialogView.findViewById(R.id.spCodifica);
        final Spinner spAusencia = (Spinner)dialogView.findViewById(R.id.spAusencia);
        final Spinner spResponsable = (Spinner)dialogView.findViewById(R.id.spResponsable);
        final Spinner spRazones = (Spinner)dialogView.findViewById(R.id.spRazones);

        spCodifica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                    spAusencia.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, codifica));
                }else if (spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                    spAusencia.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else{
                    spAusencia.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spResponsable.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        /*spAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,responsable_autoservicio));
                }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,responsable_autoservicio));
                }else{
                    spResponsable.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spResponsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Alicorp")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
                }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cliente")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
                }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else{
                    spRazones.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });*/


        lblModelo.setText(getString(R.string.model) +" :    "+skuSelected+"\n");

        builder.setPositiveButton(R.string.save,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    //cantidad = txtcantidad.getText().toString();
                    //pvp=txtpvp.getText().toString();

                    String codifica = spCodifica.getSelectedItem().toString().trim();
                    String ausencia = spAusencia.getSelectedItem().toString().trim();
                    String responsable = spResponsable.getSelectedItem().toString().trim();
                    String razones = spRazones.getSelectedItem().toString().trim();
                    if (!codifica.equalsIgnoreCase("Seleccione") &&
                        !ausencia.equalsIgnoreCase("Seleccione") &&
                        !responsable.equalsIgnoreCase("Seleccione") &&
                        !razones.equalsIgnoreCase("Seleccione")) {
                        //insertData(skuSelected, codifica, ausencia, canal, descripcion);
                    }else{
                        Toast.makeText(getApplicationContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
                    }
                }
            }
        );

        builder.setNeutralButton(R.string.cancel,null);

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

    public void insertData(String skuSelected, String quiebre, String sugerido, String cant_disp, String cant_sug, String obvs) {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            String sector = spCategoria.getSelectedItem().toString();
            String categoria = spSubCategoria.getSelectedItem().toString();
            String brand = spBrand.getSelectedItem().toString();
//            String producto = spProducto.getSelectedItem().toString();
            String local = txtLocal.getText().toString();
            String codigo_fabril = txtCodigoFabril.getText().toString();
            String vendedor_asignado = txtVendedor.getText().toString();
            /*String subcategoria = spSubcategoria.getSelectedItem().toString();
            String segmento = spSegmento.getSelectedItem().toString();*/
            /*String tamano = spTamano.getSelectedItem().toString();
            String cantidad = spCantidad.getSelectedItem().toString();*/
            String segmento = handler.getSegment1Sugeridos(skuSelected);
            /*String segment2 = handler.getSegment2Valores(skuSelected);
            String tamano = handler.getTamanoValores(skuSelected);
            String cantidad = handler.getCantidadValores(skuSelected);*/

            do {
                String skuSelected2 = "";
                String cant_disp2 = "";
                String cant_sug2 = "";

//                for (int i = 0; i < valueOfSkuTextView.length; i++) {
//                    cantidad = valueOfSkuTextView[0].toString();
//                }

                for (int i = 0; i < valueOfSkuTextView.length; i++) {
                    skuSelected2 = valueOfSkuTextView[0].toString();
                }

                for (int i = 0; i < valueOfCantDispEditText.length; i++) {
                    cant_disp2 = valueOfCantDispEditText[0].toString();
                }

                for (int i = 0; i < valueOfCantSugEditText.length; i++) {
                    cant_sug2 = valueOfCantSugEditText[0].toString();
                }

                List<String> list_edittext = new ArrayList<String>(Arrays.asList(valueOfCantDispEditText));
                list_edittext.remove(0);
                valueOfCantDispEditText = list_edittext.toArray(new String[0]);

                List<String> list_edittext_can_sug = new ArrayList<String>(Arrays.asList(valueOfCantSugEditText));
                list_edittext.remove(0);
                valueOfCantSugEditText = list_edittext_can_sug.toArray(new String[0]);

                List<String> list_textview = new ArrayList<String>(Arrays.asList(valueOfSkuTextView));
                list_textview.remove(0);
                valueOfSkuTextView = list_textview.toArray(new String[0]);
                ContentValues values = new ContentValues();

                values.put(ContractInsertSugeridos.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertSugeridos.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertSugeridos.Columnas.USUARIO, user);
                values.put(ContractInsertSugeridos.Columnas.SUPERVISOR, punto_venta);
                values.put(ContractInsertSugeridos.Columnas.FECHA, fechaser);
                values.put(ContractInsertSugeridos.Columnas.HORA, horaser);
                values.put(ContractInsertSugeridos.Columnas.LOCAL, local);
                values.put(ContractInsertSugeridos.Columnas.CODIGO_FABRIL, codigo_fabril);
                values.put(ContractInsertSugeridos.Columnas.VENDEDOR_FABRIL, vendedor_asignado);
                values.put(ContractInsertSugeridos.Columnas.CATEGORIA, sector);
                values.put(ContractInsertSugeridos.Columnas.SUBCATEGORIA, categoria);
                values.put(ContractInsertSugeridos.Columnas.BRAND, brand);
                values.put(ContractInsertSugeridos.Columnas.SKU_CODE, skuSelected2);
                values.put(ContractInsertSugeridos.Columnas.QUIEBRE, quiebre);
                values.put(ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE, cant_disp2);
                values.put(ContractInsertSugeridos.Columnas.SUGERIDO, sugerido);
                values.put(ContractInsertSugeridos.Columnas.CANTIDAD, cant_sug2);
                values.put(ContractInsertSugeridos.Columnas.OBSERVACIONES, obvs);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertSugeridos.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertSugeridos, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                /*if (handler.SKUDuplicadoValores(skuSelected, codigo_pdv)) {
                    handler.eliminarSKUDuplicadoValores(skuSelected, codigo_pdv);
                }

                ContentValues values_sesion = new ContentValues();
                values_sesion.put(ContractInsertValoresSesion.Columnas.PHARMA_ID,id_pdv);
                values_sesion.put(ContractInsertValoresSesion.Columnas.CODIGO,codigo_pdv);
                values_sesion.put(ContractInsertValoresSesion.Columnas.USUARIO,user);
                values_sesion.put(ContractInsertValoresSesion.Columnas.SUPERVISOR,punto_venta);
                values_sesion.put(ContractInsertValoresSesion.Columnas.FECHA,fechaser);
                values_sesion.put(ContractInsertValoresSesion.Columnas.HORA,horaser);
                values_sesion.put(ContractInsertValoresSesion.Columnas.CANTIDAD_ASIGNADA,categoria);
                values_sesion.put(ContractInsertValoresSesion.Columnas.CATEGORIA,logro);
                values_sesion.put(ContractInsertValoresSesion.Columnas.SEGMENTO,segment1);
                values_sesion.put(ContractInsertValoresSesion.Columnas.POFERTA,segment2);
                values_sesion.put(ContractInsertValoresSesion.Columnas.MARCA,brand);
                values_sesion.put(ContractInsertValoresSesion.Columnas.TAMANO,tamano);
                values_sesion.put(ContractInsertValoresSesion.Columnas.CANTIDAD,cantidad);
                values_sesion.put(ContractInsertValoresSesion.Columnas.SKU_CODE,skuSelected);
                values_sesion.put(ContractInsertValoresSesion.Columnas.CODIFICA,index_codifica);
                values_sesion.put(ContractInsertValoresSesion.Columnas.AUSENCIA,index_ausencia);
                values_sesion.put(ContractInsertValoresSesion.Columnas.TIPO,index_responsable);
                values_sesion.put(ContractInsertValoresSesion.Columnas.DESCRIPCION,index_razones);
                values_sesion.put(ContractInsertValoresSesion.Columnas.MANUFACTURER,manufacturer);
                values_sesion.put(Constantes.PENDIENTE_INSERCION, 1);
                getContentResolver().insert(ContractInsertValoresSesion.CONTENT_URI, values_sesion);*/
            }while(valueOfSkuTextView.length!=0 || valueOfCantDispEditText.length!=0 || valueOfCantSugEditText.length!=0);
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public class CustomAdapterSugeridos extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterSugeridos.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;
        String quiebre = "NO";
        String sugerido = "NO";

        public CustomAdapterSugeridos(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sugeridos_title, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sugeridos, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BasePortafolioProductos mObject = values.get(position);
            if (holder instanceof SugeridosActivity.CustomAdapterSugeridos.HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
                ((HeaderViewHolder) holder).headerTitle.setText("Producto");
                ((HeaderViewHolder) holder).headerTitle1.setText("Quiebre/Posible Quiebre");
                ((HeaderViewHolder) holder).headerTitle2.setText("U. Disponible");
                ((HeaderViewHolder) holder).headerTitle3.setText("Sugerido");
                ((HeaderViewHolder) holder).headerTitle4.setText("Cantidad");
                ((HeaderViewHolder) holder).headerTitle5.setText("Observaciones");
                ((HeaderViewHolder) holder).headerTitle6.setText("Entrega");
            }else if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());

//                Log.i("FOTO", mObject.getFoto());
     //           Picasso.with(context).load(mObject.getFoto().toLowerCase()).into(((ItemViewHolder) holder).ivFotoProducto);
            }
        }

        private BasePortafolioProductos getItem(int position) {
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

        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public TextView headerTitle, headerTitle1, headerTitle2, headerTitle3, headerTitle4, headerTitle5, headerTitle6;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
                headerTitle1 = (TextView)itemView.findViewById(R.id.lblCodifica);
                headerTitle2 = (TextView)itemView.findViewById(R.id.lblAusencia);
                headerTitle3 = (TextView)itemView.findViewById(R.id.lblDisponible);
                headerTitle4 = (TextView)itemView.findViewById(R.id.lblResponsable);
                headerTitle5 = (TextView)itemView.findViewById(R.id.lblRazones);
                headerTitle6 = (TextView)itemView.findViewById(R.id.lblEntrega);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public ImageView ivFotoProducto;
            public TextView txt_sku;
            public Switch swQuiebre;
            public Switch swSugerido;
            public Spinner spEntrega;
            public EditText txt_cantidad_disponible;
            public EditText txt_cantidad_sugerida;
            public EditText txt_observaciones;
            public CheckBox chkGuardar;

           /* public Spinner spCodifica;
            public Spinner spAusencia;
            public Spinner spDisponible;
            public Spinner spResponsable;
            public Spinner spRazones;
            public EditText txt_precio_regular;
            public EditText txt_precio_promocion;
            public EditText txt_precio_oferta; */



            public ItemViewHolder(View itemView) {
                super(itemView);

                ivFotoProducto = (ImageView) itemView.findViewById(R.id.ivFotoProducto);
                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
                swQuiebre = (Switch)itemView.findViewById(R.id.swQuiebre);
                swSugerido = (Switch)itemView.findViewById(R.id.swSugerido);
                spEntrega = (Spinner) itemView.findViewById(R.id.spEntrega);
                txt_cantidad_disponible = (EditText)itemView.findViewById(R.id.txt_cantidad_disponible);
                txt_cantidad_sugerida = (EditText)itemView.findViewById(R.id.txt_cantidad_sugerida);
                txt_observaciones = (EditText)itemView.findViewById(R.id.txt_observaciones);

                txt_sku.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        txt_sku.setTextColor(getResources().getColor(R.color.negro));
                    }
                });

                txt_cantidad_disponible.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        txt_cantidad_disponible.setTextColor(getResources().getColor(R.color.negro));
                    }
                });

                txt_cantidad_sugerida.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        txt_cantidad_sugerida.setTextColor(getResources().getColor(R.color.negro));
                    }
                });

                //   chkGuardar = (CheckBox)itemView.findViewById(R.id.checkGuardar);

             /*   spCodifica = (Spinner)itemView.findViewById(R.id.spCodifica);
                spAusencia = (Spinner)itemView.findViewById(R.id.spAusencia);
                spDisponible = (Spinner)itemView.findViewById(R.id.spDisponible);
                spResponsable = (Spinner)itemView.findViewById(R.id.spResponsable);
                spRazones = (Spinner)itemView.findViewById(R.id.spRazones);
                txt_precio_regular = (EditText)itemView.findViewById(R.id.txt_precio_regular);
                txt_precio_promocion = (EditText)itemView.findViewById(R.id.txt_precio_promocion);
                txt_precio_oferta = (EditText)itemView.findViewById(R.id.txt_precio_oferta);*/

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

                btnGuardar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        View view;
                        TextView tvsku;
                      //  Switch quiebre;
                        EditText etcantdisp;
                      //  Switch sugerido;
                        EditText etcantsug;
                    //    EditText etobservaciones = null;
                    //    Spinner entrega;

                        if (swQuiebre.isChecked()) {
                            quiebre =  "SI";
                        }
                        if (swSugerido.isChecked()) {
                            sugerido =  "SI";
                        }

                        int listLength = listview.getChildCount();
                        valueOfSkuTextView = new String[listLength+1];
                        valueOfQuiebreSwitch = new String[listLength+1];
                        valueOfCantDispEditText = new String[listLength+1];
                        valueOfSugeridoSwitch = new String[listLength+1];
                        valueOfCantSugEditText = new String[listLength+1];
                        valueOfObservacionesEditText = new String[listLength+1];
                        valueOfEntregaSpinner = new String[listLength+1];
                        for (int i = 0; i < listLength; i++) {
                         //   String et_otros = lblOtros.getText().toString();
                            view = listview.getChildAt(i);
                            tvsku = (TextView) view.findViewById(R.id.lblSku);
                            //quiebre = (Switch) view.findViewById(R.id.swQuiebre);
                            etcantdisp = (EditText) view.findViewById(R.id.txt_cantidad_disponible);
                            //sugerido = (Switch) view.findViewById(R.id.swSugerido);
                            etcantsug = (EditText) view.findViewById(R.id.txt_cantidad_sugerida);
                      //      etobservaciones = (EditText) view.findViewById(R.id.txt_observaciones);
                      //      entrega = (Spinner) view.findViewById(R.id.spEntrega);
                            valueOfSkuTextView[i] = tvsku.getText().toString();
                            valueOfCantDispEditText[i] = etcantdisp.getText().toString();
                            valueOfCantSugEditText[i] = etcantsug.getText().toString();
                    //        valueOfObservacionesEditText[i] = etobservaciones.getText().toString();

                         //   valueOfEditText[listLength] = et_otros;
                         //   valueOfTextView[listLength] = "Otros";
                        }
                        sku = txt_sku.getText().toString();
                        cantidad_disponible = txt_cantidad_disponible.getText().toString();
                        cantidad_sugerido = txt_cantidad_sugerida.getText().toString();
                        observaciones = txt_observaciones.getText().toString();
                        String entrega = spEntrega.getSelectedItem().toString();

                //        String razones = spRazones.getSelectedItem().toString();

//                        image = getStringImage(bitmapfinal);

                        int contador_llenos = 0;
                        if (!entrega.equalsIgnoreCase("SELECCIONE")) {

                            if (txt_cantidad_sugerida!=null && !txt_cantidad_sugerida.getText().toString().equals("")) {

                                insertData(sku, quiebre, sugerido, cantidad_disponible, cantidad_sugerido, observaciones);

                             /*   if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
                                    for (int i = 0; i < listLength; i++) {
                                        view = listview.getChildAt(i);
                                        etcantdisp = (EditText) view.findViewById(R.id.txtventa);
                                        if (!etcantdisp.getText().toString().trim().isEmpty()) {
                                            contador_llenos++;
                                        }
                                    }

                                    Log.i("CONTADOR_LLENOS",contador_llenos+"");
                                    Log.i("LISTLENGTH",listLength+"");

                                    if (contador_llenos==listLength) {
                                        insertData(razones);
                                        for (int i = 0; i < listLength; i++) {
                                            view = listview.getChildAt(i);
                                            etcantdisp = (EditText) view.findViewById(R.id.txtventa);
                                            etcantdisp.setText("");
                                        }
                                        txtCaras.setText("");
                                        lblOtros.setText("");
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
                                    resultado();
                                }*/
                            }else{
                                Toast.makeText(getApplicationContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

         /*       chkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (((CheckBox)v).isChecked()) {
                            if (swQuiebre.isChecked()) {
                                quiebre =  "SI";
                            }
                            if (swSugerido.isChecked()) {
                                sugerido =  "SI";
                            }
                            String skuSelected = txt_sku.getText().toString();
                            String cant_disp = txt_cantidad_disponible.getText().toString().trim();
                            String cant_sug = txt_cantidad_sugerida.getText().toString().trim();
                            String obvs = txt_observaciones.getText().toString().trim();
                            String entrega = spEntrega.getSelectedItem().toString();

                       //     String substring = txt_sku.getText().toString().substring(1,20);
//                            txt_sku.setText(substring);



                          /*  String codifica = spCodifica.getSelectedItem().toString().trim();
                            String ausencia = spAusencia.getSelectedItem().toString().trim();
                            String disponible = spDisponible.getSelectedItem().toString().trim();
                            String responsable = spResponsable.getSelectedItem().toString().trim();
                            String razones = spRazones.getSelectedItem().toString().trim();

                            String pvp = txt_precio_regular.getText().toString().trim();
                            String pvc = txt_precio_promocion.getText().toString().trim();
                            String poferta = txt_precio_oferta.getText().toString().trim();*/

                            //INICIO BLOQUE - NO SE USA
                         /*   String index_codifica = spDisponible.getSelectedItemPosition()+"";
                            String index_ausencia = spAusencia.getSelectedItemPosition()+"";
                            String index_responsable = spResponsable.getSelectedItemPosition()+"";
                            String index_razones = spRazones.getSelectedItemPosition()+"";*/
                            //FIN BLOQUE - NO SE USA

                       /*     if (/*!pvp.isEmpty() &&
                                !pvc.isEmpty() &&
                                !poferta.isEmpty() &&*/
                                /* !codifica.equalsIgnoreCase("Seleccione") &&*/
                            /*        !cant_disp.isEmpty() && !cant_sug.isEmpty() && !obvs.isEmpty()) {
                                insertData(skuSelected, quiebre, sugerido, cant_disp, cant_sug, obvs);
//                                celular =
//                                Toast.makeText(getApplicationContext(),producto,Toast.LENGTH_LONG).show();
                                mensaje="Te saluda "+user+" de Lucky. Cliente "+format+"\r\n"+punto_venta+"\r\nSugerido: "+producto+"\r\nCantidad: "+cant_sug+" Unidades\r\nDespacho: "+entrega;
                                if (celular=="") {
                                    Toast.makeText(getApplicationContext(),"Ingrese el número destino",Toast.LENGTH_LONG).show();
                                } else {
                                    nivelBateria();
//                                    enviarMensaje(celular,mensaje+battery);
                                    enviarMensaje(celular,mensaje);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
                                chkGuardar.setChecked(false);
                            }
                        }
                    }
                });*/
            }
        }
    }

    private void enviarMensaje (String numero, String mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            Toast.makeText(getApplicationContext(),"Mensaje enviado exitosamente",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Mensaje NO enviado, error de datos "+numero,Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void nivelBateria () {
        //      Bateria Inicio
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        battery = (level / (float)scale)*100;
        //      Bateria Fin

    }
}