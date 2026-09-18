package com.luckyecuador.app.PintucoAPP;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.InsertOsa;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ValoresActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = ValoresActivity.class.getSimpleName();

//    ArrayList<Valores> sesion = new ArrayList<Valores>();
    ArrayList<BasePortafolioProductos> codifican = new ArrayList<BasePortafolioProductos>();
    ArrayList<BasePortafolioProductos> precios = new ArrayList<BasePortafolioProductos>();
    ArrayList<InsertOsa> sesion = new ArrayList<InsertOsa>();
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
    //private Spinner spSubcategoria;
    //private Spinner spSegmento;
    private Spinner spBrand;
    //private Spinner spTamano;
    //private Spinner spCantidad;

    private EditText txtprecio_contado;
    private EditText txtprecio_credito;
    private EditText txtcuotas;
    private EditText txtvalor_cuotas;
    private EditText txtSkuCode;
    private EditText txtDescripcionSKU;

    private final String manufacturer = "Alicorp";

    //TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    RecyclerView listview;
    ArrayList<BasePortafolioProductos> listProductos;
    //ArrayList<BasePortafolioProductos> mainlistProductos;

    CustomAdapterValores dataAdapter;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    DatabaseHelper handler;

    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora, format;
    String  marca, tipo, fechaventas, pcontado, pcredito, cuota, vcuota;
    private String nom_comercial, canal, subcanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valores);
        setToolbar();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();

        Log.i("NOMBRECOM", nom_comercial);
        Log.i("CANAL", canal);

        //startService(new Intent(getApplicationContext(), MyService.class));

        //lSeleccioneProducto = (TextView)findViewById(R.id.lSeleccioneProducto);
        spCategoria = (Spinner)findViewById(R.id.spSector);
        spSubCategoria = (Spinner)findViewById(R.id.spCategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        spBrand = (Spinner)findViewById(R.id.spMarca);
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

        llenadoArray();

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        listview = (RecyclerView)findViewById(R.id.lvSKUCode);
        listview.setHasFixedSize(true);

        filtrarCategoria();

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

    public void llenadoArray() {
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
    }

    public void filtrarCategoria() {
        List<String> operadores = handler.getCategoriaValores2(codigo_pdv, canal, subcanal, manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubCategoria(String categoria) {
        List<String> operadores = handler.getSubCategoriaValores2(categoria, codigo_pdv, canal, subcanal, manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategoria.setAdapter(dataAdapter);
        spSubCategoria.setOnItemSelectedListener(this);
    }

    /*public void filtrarMarca(String logro, String subcategoria) {
        List<String> operadores = handler.getSegmento1(subcategoria,logro);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSegmento(String logro, String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento2(subcategoria,logro,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }*/

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

    public void showListView(String categoria, String subcategoria, String brand, String manufacturer) {
        listProductos = handler.filtrarListProductos(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
        codifican = new ArrayList<BasePortafolioProductos>();
        precios = new ArrayList<BasePortafolioProductos>();

        codifican = handler.filtrarListProductosCodificanValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
        precios = handler.filtrarListProductosPreciosValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);

        /*if (listProductos.status() > 30) {
            mainlistProductos = new ArrayList<>(listProductos.subList(0,30));
        } else {
            mainlistProductos = listProductos;
        }

        Log.i("List Original",listProductos.status()+"");
        Log.i("List Modificada",mainlistProductos.status()+"");*/

        dataAdapter = new CustomAdapterValores(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ValoresActivity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterValores customAdapter = new CustomAdapterValores(this.getApplicationContext(),listProductos);
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

            dataAdapter = (CustomAdapterValores) listview.getAdapter();

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
            listview.setAdapter(new CustomAdapterValores(this, data));
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
                filtrarBrand(categoria, subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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

        if (adapterView==spBrand) {
            try{
                brand=adapterView.getItemAtPosition(i).toString();
                //filtrartamano(logro,subcategoria,subcategoria,presentacion,contenido);
                showListView(categoria, subcategoria, brand, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

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
            new android.content.DialogInterface.OnClickListener() {
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

    public void insertData(String skuSelected, String codifica, String ausencia, String disponible, String responsable, String razones,
                           String index_codifica, String index_ausencia, String index_responsable, String index_razones,
                           String pvp, String pvc, String poferta) {
        try{
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
            /*String subcategoria = spSubcategoria.getSelectedItem().toString();
            String segmento = spSegmento.getSelectedItem().toString();*/
            String brand = spBrand.getSelectedItem().toString();
            /*String tamano = spTamano.getSelectedItem().toString();
            String cantidad = spCantidad.getSelectedItem().toString();*/

            String segmento = handler.getSegment1Valores(skuSelected);
            /*String segment2 = handler.getSegment2Valores(skuSelected);
            String tamano = handler.getTamanoValores(skuSelected);
            String cantidad = handler.getCantidadValores(skuSelected);*/

            ContentValues values = new ContentValues();

            values.put(ContractInsertValores.Columnas.PHARMA_ID, id_pdv);
            values.put(ContractInsertValores.Columnas.CODIGO, codigo_pdv);
            values.put(ContractInsertValores.Columnas.USUARIO, user);
            values.put(ContractInsertValores.Columnas.SUPERVISOR, punto_venta);
            values.put(ContractInsertValores.Columnas.FECHA, fechaser);
            values.put(ContractInsertValores.Columnas.HORA, horaser);
            values.put(ContractInsertValores.Columnas.SECTOR, sector);
            values.put(ContractInsertValores.Columnas.CATEGORIA, categoria);
            values.put(ContractInsertValores.Columnas.SEGMENTO1, segmento);
            //values.put(ContractInsertValores.Columnas.POFERTA,segment2);
            values.put(ContractInsertValores.Columnas.BRAND, brand);
            /*values.put(ContractInsertValores.Columnas.TAMANO,tamano);
            values.put(ContractInsertValores.Columnas.CANTIDAD,cantidad);*/
            values.put(ContractInsertValores.Columnas.SKU_CODE, skuSelected);
            values.put(ContractInsertValores.Columnas.CODIFICA, codifica);
            values.put(ContractInsertValores.Columnas.AUSENCIA, ausencia);
            values.put(ContractInsertValores.Columnas.DISPONIBLE, disponible);
            values.put(ContractInsertValores.Columnas.RESPONSABLE, responsable);
            values.put(ContractInsertValores.Columnas.RAZONES, razones);
            values.put(ContractInsertValores.Columnas.PVP, "");
            values.put(ContractInsertValores.Columnas.PVC, "");
            values.put(ContractInsertValores.Columnas.POFERTA, "");
            values.put(ContractInsertValores.Columnas.MANUFACTURER, manufacturer);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContentResolver().insert(ContractInsertValores.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertvalores, null);
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

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public class CustomAdapterValores extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterValores.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;

        public CustomAdapterValores(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_osa_title, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_osa, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BasePortafolioProductos mObject = values.get(position);

            if (holder instanceof HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
                ((HeaderViewHolder) holder).headerTitle.setText("SKU");
                ((HeaderViewHolder) holder).headerTitle1.setText("Codifica");
                ((HeaderViewHolder) holder).headerTitle2.setText("Bodega");
                ((HeaderViewHolder) holder).headerTitle3.setText("Percha");
                ((HeaderViewHolder) holder).headerTitle4.setText("Responsable");
                ((HeaderViewHolder) holder).headerTitle5.setText("Descripción");
            }else if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());
                ((ItemViewHolder) holder).txt_precio_regular.setText(mObject.getPvp());

//                ((ItemViewHolder) holder).spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                ((ItemViewHolder) holder).spAusencia.setEnabled(false);
//                ((ItemViewHolder) holder).spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                ((ItemViewHolder) holder).spDisponible.setEnabled(false);
//                ((ItemViewHolder) holder).spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                ((ItemViewHolder) holder).spResponsable.setEnabled(false);
//                ((ItemViewHolder) holder).spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                ((ItemViewHolder) holder).spRazones.setEnabled(false);

                ((ItemViewHolder) holder).txt_precio_regular.setEnabled(false);
                ((ItemViewHolder) holder).txt_precio_promocion.setEnabled(false);
                ((ItemViewHolder) holder).txt_precio_promocion.setText("NA");
                ((ItemViewHolder) holder).txt_precio_oferta.setEnabled(false);
                ((ItemViewHolder) holder).txt_precio_oferta.setText("NA");
                ((ItemViewHolder) holder).chkGuardar.setEnabled(false);

                ((ItemViewHolder) holder).txt_sku.setVisibility(View.GONE);
                ((ItemViewHolder) holder).spAusencia.setVisibility(View.GONE);
                ((ItemViewHolder) holder).spDisponible.setVisibility(View.GONE);
                ((ItemViewHolder) holder).spResponsable.setVisibility(View.GONE);
                ((ItemViewHolder) holder).spRazones.setVisibility(View.GONE);
                ((ItemViewHolder) holder).txt_precio_regular.setVisibility(View.GONE);
                ((ItemViewHolder) holder).txt_precio_promocion.setVisibility(View.GONE);
                ((ItemViewHolder) holder).txt_precio_oferta.setVisibility(View.GONE);
                ((ItemViewHolder) holder).chkGuardar.setVisibility(View.GONE);


                Log.i("CODIFICAN", codifican.size() + "");
                Log.i("PRECIOS", precios.size() + "");
//                Log.i("POSITION", values.get(position).getSku().toString() + "");

//                Log.i("SKU-OBJECT", values.get(position).getSku().toString());

                if (codifican.size()>0) {
                    for(int i = 0; i < codifican.size(); i++) {
//                        Log.i("SKU-CODIFICA", codifican.get(i).getSku());
                        if (values.get(position).getSku().toString().equalsIgnoreCase(codifican.get(i).getSku())) {
                            ((ItemViewHolder) holder).spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,combo_si_no));
                            ((ItemViewHolder) holder).spAusencia.setEnabled(true);
                            ((ItemViewHolder) holder).spDisponible.setEnabled(true);
                            ((ItemViewHolder) holder).spResponsable.setEnabled(true);
                            ((ItemViewHolder) holder).spRazones.setEnabled(true);
                        }
                    }
                }else{
//                    ((ItemViewHolder) holder).spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    ((ItemViewHolder) holder).spAusencia.setEnabled(false);
//                    ((ItemViewHolder) holder).spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    ((ItemViewHolder) holder).spDisponible.setEnabled(false);
//                    ((ItemViewHolder) holder).spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    ((ItemViewHolder) holder).spResponsable.setEnabled(false);
//                    ((ItemViewHolder) holder).spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    ((ItemViewHolder) holder).spRazones.setEnabled(false);
                }

                if (precios.size()>0) {
                    for(int i = 0; i < precios.size(); i++) {
//                        Log.i("SKU-CODIFICA", codifican.get(i).getSku());
                        if (values.get(position).getSku().toString().equalsIgnoreCase(precios.get(i).getSku())) {
                            ((ItemViewHolder) holder).txt_precio_regular.setEnabled(true);
                            ((ItemViewHolder) holder).txt_precio_promocion.setEnabled(true);
                            ((ItemViewHolder) holder).txt_precio_promocion.setText("");
                            ((ItemViewHolder) holder).txt_precio_oferta.setEnabled(true);
                            ((ItemViewHolder) holder).txt_precio_oferta.setText("");
                            ((ItemViewHolder) holder).chkGuardar.setEnabled(true);

                            ((ItemViewHolder) holder).txt_sku.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).spAusencia.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).spDisponible.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).spResponsable.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).spRazones.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).chkGuardar.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    ((ItemViewHolder) holder).txt_precio_regular.setEnabled(false);
                    ((ItemViewHolder) holder).txt_precio_promocion.setEnabled(false);
                    ((ItemViewHolder) holder).txt_precio_promocion.setText("NA");
                    ((ItemViewHolder) holder).txt_precio_oferta.setEnabled(false);
                    ((ItemViewHolder) holder).txt_precio_oferta.setText("NA");
                    ((ItemViewHolder) holder).chkGuardar.setEnabled(false);

                    ((ItemViewHolder) holder).txt_sku.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).spAusencia.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).spDisponible.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).spResponsable.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).spRazones.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).txt_precio_regular.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).txt_precio_promocion.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).txt_precio_oferta.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).chkGuardar.setVisibility(View.GONE);
                }

                sesion = handler.getListGuardadoOSA(codigo_pdv,user);
                for(int i = 0; i < sesion.size(); i++) {
                    if (mObject.getSku().equals(sesion.get(i).getSku())) {
                        ((ItemViewHolder) holder).txt_sku.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo_alicorp));

                        ((ItemViewHolder) holder).txt_sku.setTypeface(null, Typeface.BOLD);
                    }
                }
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

        /*public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
            listProductos = filteredList;
            Log.i("LIST",listProductos.status()+"");
            ValoresActivity.this.dataAdapter.notifyDataSetChanged();
        }*/

        /**
         *
         */
        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public TextView headerTitle, headerTitle1, headerTitle2, headerTitle3, headerTitle4, headerTitle5;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
                headerTitle1 = (TextView)itemView.findViewById(R.id.lblCodifica);
                headerTitle2 = (TextView)itemView.findViewById(R.id.lblAusencia);
                headerTitle3 = (TextView)itemView.findViewById(R.id.lblDisponible);
                headerTitle4 = (TextView)itemView.findViewById(R.id.lblResponsable);
                headerTitle5 = (TextView)itemView.findViewById(R.id.lblRazones);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView txt_sku;
            public Spinner spCodifica;
            public Spinner spAusencia;
            public Spinner spDisponible;
            public Spinner spResponsable;
            public Spinner spRazones;
            public CheckBox chkGuardar;
            public EditText txt_precio_regular;
            public EditText txt_precio_promocion;
            public EditText txt_precio_oferta;

            public ItemViewHolder(View itemView) {
                super(itemView);

                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
                spCodifica = (Spinner)itemView.findViewById(R.id.spCodifica);
                spAusencia = (Spinner)itemView.findViewById(R.id.spAusencia);
                spDisponible = (Spinner)itemView.findViewById(R.id.spDisponible);
                spResponsable = (Spinner)itemView.findViewById(R.id.spResponsable);
                spRazones = (Spinner)itemView.findViewById(R.id.spRazones);
                chkGuardar = (CheckBox)itemView.findViewById(R.id.checkGuardar);
                txt_precio_regular = (EditText)itemView.findViewById(R.id.txt_precio_regular);
                txt_precio_promocion = (EditText)itemView.findViewById(R.id.txt_precio_promocion);
                txt_precio_oferta = (EditText)itemView.findViewById(R.id.txt_precio_oferta);

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

                txt_precio_regular.setFilters(new InputFilter[]{new InputFilter() {
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

                txt_precio_promocion.setFilters(new InputFilter[]{new InputFilter() {
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

                txt_precio_oferta.setFilters(new InputFilter[]{new InputFilter() {
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

                txt_precio_regular.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
                txt_precio_promocion.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
                txt_precio_oferta.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});

//                Log.i("CODIFICAN", codifican.size()+"");
//
//                if (codifican.size()>0) {
//                    for(int i = 0; i < codifican.size(); i++) {
//                        Log.i("SKU-CODIFICA", codifican.get(i).getSku());
//                        Log.i("SKU-OBJECT", txt_sku.getText().toString());
//                        if (txt_sku.getText().toString().equalsIgnoreCase(codifican.get(i).getSku())) {
//                            spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,combo_si_no));
//                            spAusencia.setEnabled(true);
//                            spCodifica.setEnabled(true);
//                            spResponsable.setEnabled(true);
//                            spRazones.setEnabled(true);
//                        }else{
//                            spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spAusencia.setEnabled(false);
//                            spCodifica.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spCodifica.setEnabled(false);
//                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spResponsable.setEnabled(false);
//                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spRazones.setEnabled(false);
//                        }
//                    }
//                }else{
//                    spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    spAusencia.setEnabled(false);
//                    spCodifica.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    spCodifica.setEnabled(false);
//                    spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    spResponsable.setEnabled(false);
//                    spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                    spRazones.setEnabled(false);
//                }

                spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, disponible));

                if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
                    spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
                }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
                    spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
                }

                spCodifica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                            spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
                            if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
                            }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
                            }
                        }else if (spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                            spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                        }else{
                            spAusencia.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
                            if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
                            }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, codifica));
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
                        } /*else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, codifica));
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
                        }*/ else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                            if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
                            } else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
                            }
                        /*else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                            spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                        }*/
                            else {
                                spDisponible.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
                                spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
                            }
                     /*   if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                            if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
                            }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
                            }
                        }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                        }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                        }else{
                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                        }*/
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spDisponible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                        if (spDisponible.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
//                            if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
//                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
//                            }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
//                                spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
//                            }
//                        }else if (spDisponible.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
//                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                        }else if (spDisponible.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
//                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                        }else{
//                            spResponsable.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
//                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
//                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {}
                });

                spResponsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Logística")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_logistica));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
                                (subcanal.equals("AKI") || subcanal.equals("SUPERMAXI") || subcanal.equals("SANTA MARIA"))) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
                                (subcanal.equals("CORAL HIPERMERCADOS"))) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_C));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
                                (subcanal.equals("MI COMISARIATO"))) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_comisariato));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
                                (subcanal.equals("ALMACENES TIA"))) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_tia));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Alicorp")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_alicorp));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Pydaco")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_pydaco));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("PDV")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_pdv));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Lucky")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, razon_lucky));
                        }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                        }else{
                            spRazones.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {}

                });

                chkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (((CheckBox)v).isChecked()) {
                            String skuSelected = txt_sku.getText().toString();
                            String codifica = spCodifica.getSelectedItem().toString().trim();
                            String ausencia = spAusencia.getSelectedItem().toString().trim();
                            String disponible = spDisponible.getSelectedItem().toString().trim();
                            String responsable = spResponsable.getSelectedItem().toString().trim();
                            String razones = spRazones.getSelectedItem().toString().trim();

                            String pvp = txt_precio_regular.getText().toString().trim();
                            String pvc = txt_precio_promocion.getText().toString().trim();
                            String poferta = txt_precio_oferta.getText().toString().trim();

                            //INICIO BLOQUE - NO SE USA
                            String index_codifica = spDisponible.getSelectedItemPosition()+"";
                            String index_ausencia = spAusencia.getSelectedItemPosition()+"";
                            String index_responsable = spResponsable.getSelectedItemPosition()+"";
                            String index_razones = spRazones.getSelectedItemPosition()+"";
                            //FIN BLOQUE - NO SE USA

                            if (/*!pvp.isEmpty() &&
                                !pvc.isEmpty() &&
                                !poferta.isEmpty() &&*/
                               /* !codifica.equalsIgnoreCase("Seleccione") &&*/
                                !ausencia.equalsIgnoreCase("Seleccione") &&
                                !disponible.equalsIgnoreCase("Seleccione") &&
                                !responsable.equalsIgnoreCase("Seleccione") &&
                                !razones.equalsIgnoreCase("Seleccione")) {
                                insertData(skuSelected, codifica, ausencia, disponible, responsable, razones,index_codifica,index_ausencia,index_responsable,index_razones, pvp, pvc, poferta);
                            }else{
                                Toast.makeText(getApplicationContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
                                chkGuardar.setChecked(false);
                            }
                        }
                    }
                });
            }
        }
    }
}