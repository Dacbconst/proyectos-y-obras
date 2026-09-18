package com.luckyecuador.app.PintucoAPP.ui.osa;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.InsertOsa;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ValoresFragment extends Fragment implements  AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = ValoresFragment.class.getSimpleName();

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
    ArrayList<String> razon_hularus = new ArrayList<>();
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
    ListView listview;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_valores, container, false);
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);

        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LoadData();

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        Log.i("NOMBRECOM", nom_comercial);
        Log.i("CANAL", canal);

        //startService(new Intent(getContext(), MyService.class));

        //lSeleccioneProducto = (TextView)findViewById(R.id.lSeleccioneProducto);
        spCategoria = (Spinner) rootView.findViewById(R.id.spSector);
        spSubCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        spBrand = (Spinner) rootView.findViewById(R.id.spMarca);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSkuCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcionSKU = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);

        //lSeleccioneProducto.setText("Seleccione Producto: "+ format);

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

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

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);
        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

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
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
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
        responsable_mayorista.add("Hularus"); //Antes Alicorp (envía a razon_cadena_SSA)
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

        razon_hularus.add("Seleccione");
        razon_hularus.add("Producto contaminado en Bodega");
        razon_hularus.add("Producto no Disponible en Bodega");
        razon_hularus.add("Retraso de Entrega");
        razon_hularus.add("Vendedor no Visita");

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

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    public void filtrarCategoria() {
        List<String> operadores = handler.getCategoriaValores2(codigo_pdv, canal, subcanal, manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubCategoria(String categoria) {
        List<String> operadores = handler.getSubCategoriaValores2(categoria, codigo_pdv, canal, subcanal, manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        List<String> operadores = handler.getBrandValores3(categoria, subcategoria, codigo_pdv, canal, subcanal, manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        Log.i("CADENA 1", subcanal);
        listProductos = handler.filtrarListProductos(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
        codifican = new ArrayList<BasePortafolioProductos>();
        precios = new ArrayList<BasePortafolioProductos>();

        codifican = handler.filtrarListProductosCodificanValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
        precios = handler.filtrarListProductosPreciosValores(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);

        dataAdapter = new CustomAdapterValores(getContext(), listProductos);
        if(!dataAdapter.isEmpty()){
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);
            //listview.setOnItemClickListener(this);

            txtSkuCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(txtSkuCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
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

                    /*PreciosActivity.this.dataAdapter.getFilter().filter(s);
                    dataAdapter.notifyDataSetChanged();*/
                }
            });
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<BasePortafolioProductos> data) {
        if(!data.isEmpty()){
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterValores(getContext(), data));
        }else{
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
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSubCategoria(categoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if (adapterView==spSubcategoria)
        {
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                filtrarSegmento(logro,subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView==spSegmento)
        {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                filtrarMarca(logro,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

        if (adapterView==spBrand) {
            try{
                brand = adapterView.getItemAtPosition(i).toString();
                Log.i("CADENA 2", subcanal);
                //filtrartamano(logro,subcategoria,subcategoria,presentacion,contenido);
                showListView(categoria, subcategoria, brand, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        /*if (adapterView==spTamano)
        {
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(logro,subcategoria,subcategoria,presentacion,contenido,tamano);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spCantidad) {
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(logro,subcategoria,contenido);
                // showListDescripcion(channel,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_valores, null);
        //Title
        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle("Codificados");
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_valores,null));

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
                    spAusencia.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, codifica));
                }else if (spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                    spAusencia.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else{
                    spAusencia.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spResponsable.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        /*spAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,responsable_autoservicio));
                }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else if (spAusencia.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                    spResponsable.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,responsable_autoservicio));
                }else{
                    spResponsable.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spResponsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Alicorp")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
                }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cliente")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
                }else if (spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
                }else{
                    spRazones.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
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
                        Toast.makeText(getContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
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

    public void insertData(String skuSelected, String codifica, String bodega, String percha, String responsable,
                           String razones, String index_codifica, String index_ausencia,
                           String index_responsable, String index_razones, String pvp, String pvc, String poferta,
                           String quiebre_percha, String quiebre_bodega) {
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
            String brand = spBrand.getSelectedItem().toString();
            String segmento = handler.getSegment1Valores(skuSelected);
            String plataforma = handler.getPlataformaBySku(skuSelected);

            /*
            if (visibilidadSugerido==View.GONE) {
                sugerido = "NA";
                tipo_sugerido = "NA";
            }*/

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
            values.put(ContractInsertValores.Columnas.BRAND, brand);
            values.put(ContractInsertValores.Columnas.SKU_CODE, skuSelected);
            values.put(ContractInsertValores.Columnas.CODIFICA, codifica);
            values.put(ContractInsertValores.Columnas.AUSENCIA, bodega);
            values.put(ContractInsertValores.Columnas.DISPONIBLE, percha);
            values.put(ContractInsertValores.Columnas.RESPONSABLE, responsable);
            values.put(ContractInsertValores.Columnas.RAZONES, razones);
            values.put(ContractInsertValores.Columnas.SUGERIDO, "");
            values.put(ContractInsertValores.Columnas.TIPO_SUGERIDO, "");
            values.put(ContractInsertValores.Columnas.PVP, "");
            values.put(ContractInsertValores.Columnas.PVC, "");
            values.put(ContractInsertValores.Columnas.POFERTA, "");
            values.put(ContractInsertValores.Columnas.MANUFACTURER, manufacturer);
            values.put(ContractInsertValores.Columnas.QUIEBRE_PERCHA, quiebre_percha);
            values.put(ContractInsertValores.Columnas.QUIEBRE_BODEGA, quiebre_bodega);
            values.put(ContractInsertValores.Columnas.POS_NAME, punto_venta);
            values.put(ContractInsertValores.Columnas.PLATAFORMA, plataforma);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContext().getContentResolver().insert(ContractInsertValores.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getContext())) {
                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertvalores, null);
                Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public class CustomAdapterValores extends ArrayAdapter<BasePortafolioProductos> implements Filterable {

        public ArrayList<BasePortafolioProductos> values;
        public Context context;

        public CustomAdapterValores(Context context, ArrayList<BasePortafolioProductos> values) {
            super(context, 0, values);
            this.values = values;
        }

        public class ViewHolder{
            public TextView txt_sku;
            public Spinner spCodifica;
            public Spinner spBodega;
            public EditText txtBodega;
            public Spinner spPercha;
            public EditText txtPercha;
            public Spinner spResponsable;
            public Spinner spDescripcion;
            public Spinner spTipoSugerido;
            public EditText txtSugerido;
            public EditText txt_precio_regular;
            public EditText txt_precio_promocion;
            public EditText txt_precio_oferta;
            public Button chkGuardar;
            public LinearLayout layoutSugerido;
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
                convertView = inflater.inflate(R.layout.list_row_osa, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.txt_sku = (TextView) convertView.findViewById(R.id.lblSku);

                vHolder.txt_sku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.spCodifica = (Spinner) convertView.findViewById(R.id.spCodifica);
                vHolder.spBodega = (Spinner) convertView.findViewById(R.id.spAusencia);
                vHolder.txtBodega = (EditText) convertView.findViewById(R.id.txtBodega);
                vHolder.spPercha = (Spinner) convertView.findViewById(R.id.spDisponible);
                vHolder.txtPercha = (EditText) convertView.findViewById(R.id.txtPercha);
                vHolder.spResponsable = (Spinner) convertView.findViewById(R.id.spResponsable);
                vHolder.spDescripcion = (Spinner) convertView.findViewById(R.id.spRazones);
              //  vHolder.spTipoSugerido = (Spinner) convertView.findViewById(R.id.spTipoSugerido);
              //  vHolder.txtSugerido = (EditText) convertView.findViewById(R.id.txtSugerido);
                vHolder.txt_precio_regular = (EditText) convertView.findViewById(R.id.txt_precio_regular);
                vHolder.txt_precio_promocion = (EditText) convertView.findViewById(R.id.txt_precio_promocion);
                vHolder.txt_precio_oferta = (EditText) convertView.findViewById(R.id.txt_precio_oferta);
                vHolder.chkGuardar = (Button) convertView.findViewById(R.id.checkGuardar);
              //  vHolder.layoutSugerido = (LinearLayout) convertView.findViewById(R.id.layoutSugerido);

                convertView.setTag(vHolder);
                CustomAdapterValores.ViewHolder finalVHolder = vHolder;
            } else {
                vHolder = (CustomAdapterValores.ViewHolder) convertView.getTag();
            }

            try {
                if (values.size() > 0) {
                    String sku = values.get(position).getSku();
                    vHolder.txt_sku.setText(sku);



                    vHolder.txt_precio_regular.setText(values.get(position).getPvp());

                    vHolder.txt_precio_regular.setEnabled(false);
                    vHolder.txt_precio_promocion.setEnabled(false);
                    vHolder.txt_precio_promocion.setText("NA");
                    vHolder.txt_precio_oferta.setEnabled(false);
                    vHolder.txt_precio_oferta.setText("NA");
                    vHolder.chkGuardar.setEnabled(false);

                    vHolder.txt_sku.setVisibility(View.GONE);
                    vHolder.spBodega.setVisibility(View.GONE);
                    vHolder.spPercha.setVisibility(View.GONE);
                    vHolder.spResponsable.setVisibility(View.GONE);
                    vHolder.spDescripcion.setVisibility(View.GONE);
                    vHolder.txt_precio_regular.setVisibility(View.GONE);
                    vHolder.txt_precio_promocion.setVisibility(View.GONE);
                    vHolder.txt_precio_oferta.setVisibility(View.GONE);
                    vHolder.chkGuardar.setVisibility(View.GONE);

                    Log.i("CODIFICAN", codifican.size() + "");
                    Log.i("PRECIOS", precios.size() + "");

                    if (codifican.size()>0) {
                        for(int i = 0; i < codifican.size(); i++) {
                            if (values.get(position).getSku().toString().equalsIgnoreCase(codifican.get(i).getSku())) {
                                List<String> datos = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.osa)));
                                vHolder.spBodega.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,datos));
                                vHolder.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,datos));
                                vHolder.spBodega.setEnabled(true);
                                vHolder.spPercha.setEnabled(true);
                                vHolder.spResponsable.setEnabled(true);
                                vHolder.spDescripcion.setEnabled(true);
                            }
                        }
                    }

                    if (precios.size()>0) {
                        for(int i = 0; i < precios.size(); i++) {
                            if (values.get(position).getSku().toString().equalsIgnoreCase(precios.get(i).getSku())) {
                                vHolder.txt_precio_regular.setEnabled(true);
                                vHolder.txt_precio_promocion.setEnabled(true);
                                vHolder.txt_precio_promocion.setText("");
                                vHolder.txt_precio_oferta.setEnabled(true);
                                vHolder.txt_precio_oferta.setText("");
                                vHolder.chkGuardar.setEnabled(true);

                                vHolder.txt_sku.setVisibility(View.VISIBLE);
                                vHolder.spBodega.setVisibility(View.VISIBLE);
                                vHolder.spPercha.setVisibility(View.VISIBLE);
                                vHolder.spResponsable.setVisibility(View.VISIBLE);
                                vHolder.spDescripcion.setVisibility(View.VISIBLE);
                                vHolder.chkGuardar.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        vHolder.txt_precio_regular.setEnabled(false);
                        vHolder.txt_precio_promocion.setEnabled(false);
                        vHolder.txt_precio_promocion.setText("NA");
                        vHolder.txt_precio_oferta.setEnabled(false);
                        vHolder.txt_precio_oferta.setText("NA");
                        vHolder.chkGuardar.setEnabled(false);

                        vHolder.txt_sku.setVisibility(View.GONE);
                        vHolder.spBodega.setVisibility(View.GONE);
                        vHolder.spPercha.setVisibility(View.GONE);
                        vHolder.spResponsable.setVisibility(View.GONE);
                        vHolder.spDescripcion.setVisibility(View.GONE);
                        vHolder.txt_precio_regular.setVisibility(View.GONE);
                        vHolder.txt_precio_promocion.setVisibility(View.GONE);
                        vHolder.txt_precio_oferta.setVisibility(View.GONE);
                        vHolder.chkGuardar.setVisibility(View.GONE);
                    }

                    sesion = handler.getListGuardadoOSA(codigo_pdv,user);
                    for(int i = 0; i < sesion.size(); i++) {
                        if (values.get(position).getSku().equals(sesion.get(i).getSku())) {
                          //  vHolder.txt_sku.setTextColor(ContextCompat.getColor(getContext(), R.color.rojo_alicorp));
                          //  vHolder.txt_sku.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                            vHolder.txt_sku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                            vHolder.txt_sku.setTypeface(null, Typeface.BOLD);
                       //     vHolder.txtSugerido.setText(sesion.get(i).getSugerido());
                        }
                    }

//                    String estado = handler.getUltimoSugerido(codigo_pdv, user, sku);
//                    vHolder.lblEstado.setText(estado);
//                    if (estado.equalsIgnoreCase("REALIZADO")) {
//                        vHolder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//                    }

/*
                    String estado = handler.getSkuOsa(codigo_pdv, user, sku);
                    if (estado.equalsIgnoreCase("RELEVADO")) {
                        vHolder.txt_sku.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    }
*/


                    final CustomAdapterValores.ViewHolder finalv = vHolder;
                    View finalConvertView = convertView;

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

                    vHolder.txt_precio_regular.setFilters(new InputFilter[]{new InputFilter() {
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

                    vHolder.txt_precio_promocion.setFilters(new InputFilter[]{new InputFilter() {
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

                    vHolder.txt_precio_oferta.setFilters(new InputFilter[]{new InputFilter() {
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

                    vHolder.txt_precio_regular.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
                    vHolder.txt_precio_promocion.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
                    vHolder.txt_precio_oferta.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});

//                    vHolder.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, disponible));

//                    if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
//                        vHolder.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
//                    }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
//                        vHolder.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
//                    }

                    Log.i("CANAL OSA", tipo);
                    List<String> operadores = handler.getResponsablesOSA(tipo);
                    vHolder.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
                    vHolder.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));

//                    vHolder.spCodifica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if (finalv.spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
//                                finalv.spBodega.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
//                                finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
//                                List<String> operadores = handler.getResponsablesOSA(tipo);
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));
//
////                                if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
////                                }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
////                                }
//                            }else if (finalv.spCodifica.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
//                                finalv.spBodega.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                                finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            }else{
//                                finalv.spBodega.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
//                                finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,disponible));
////                                if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
////                                }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
////                                }
//                                List<String> operadores = handler.getResponsablesOSA(tipo);
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });

//                    finalv.spBodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
//                                finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, codifica));
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, no_codifica));
//                            } else if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("NO")) {
////                                if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
////                                } else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
////                                } else {
////                                    finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
////                                    finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, seleccione));
////                                }
//                                List<String> operadores = handler.getResponsablesOSA(tipo);
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));
//                            } else {
//                                finalv.spPercha.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, disponible));
////                                if (tipo.equalsIgnoreCase(Constantes.AUTO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_autoservicio));
////                                }else if (tipo.equalsIgnoreCase(Constantes.MAYO)) {
////                                    finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, responsable_mayorista));
////                                }
//                                List<String> operadores = handler.getResponsablesOSA(tipo);
//                                finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });

                    finalv.spPercha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            List<String> datos_bodega = new ArrayList<>();
                   //         finalv.layoutSugerido.setVisibility(View.VISIBLE);
                            if (finalv.spPercha.getSelectedItem().toString().trim().equalsIgnoreCase("STOCK SANO")) {
                                datos_bodega.add("Seleccione");
                                datos_bodega.add("Quiebre");
                                datos_bodega.add("Posible quiebre");
                                datos_bodega.add("Stock sano");

              //                  finalv.txtPercha.setText("NA");
              //                  finalv.txtPercha.setVisibility(View.GONE);

                                finalv.txtPercha.setText("");
                                finalv.txtPercha.setVisibility(View.VISIBLE);


                                if (finalv.spBodega.getSelectedItem().toString().equalsIgnoreCase("STOCK SANO")) {


                                 //   finalv.txtSugerido.setText("");
                                  //  finalv.spTipoSugerido.setSelection(0);
                                 //   finalv.layoutSugerido.setVisibility(View.GONE);
                                }
                            } else if (finalv.spPercha.getSelectedItem().toString().trim().equalsIgnoreCase("QUIEBRE")) {
                                datos_bodega.add("Quiebre");

                                finalv.txtPercha.setText("0");
                                finalv.txtPercha.setVisibility(View.GONE);
                            } else if (finalv.spPercha.getSelectedItem().toString().trim().equalsIgnoreCase("POSIBLE QUIEBRE")) {
                                datos_bodega.add("Quiebre");

                                finalv.txtPercha.setText("");
                                finalv.txtPercha.setVisibility(View.VISIBLE);
                            } else if (finalv.spPercha.getSelectedItem().toString().trim().equalsIgnoreCase("SELECCIONE")) {
                                datos_bodega = seleccione;
                                finalv.txtPercha.setText("");
                                finalv.txtPercha.setVisibility(View.GONE);
                            }
                            finalv.spBodega.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, datos_bodega));
                            finalv.spBodega.setSelection(0);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {}
                    });

                    finalv.spBodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            List<String> datos_responsables = null;
                            List<String> datos_descripcion = null;
                         //   finalv.layoutSugerido.setVisibility(View.VISIBLE);

                            if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("STOCK SANO")) {
                                datos_responsables = no_codifica;
                                datos_descripcion = no_codifica;

                                finalv.txtBodega.setText("NA");
                                finalv.txtBodega.setVisibility(View.GONE);

                                if (finalv.spPercha.getSelectedItem().toString().equalsIgnoreCase("STOCK SANO")) {
                                //    finalv.txtSugerido.setText("");
                                //    finalv.spTipoSugerido.setSelection(0);
                                //    finalv.layoutSugerido.setVisibility(View.GONE);

                                    finalv.txtPercha.setText("NA");
                                    finalv.txtPercha.setVisibility(View.GONE);

                                }
                            } else if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("QUIEBRE")) {
                                datos_responsables = handler.getResponsablesOSA(tipo);
                                datos_descripcion = seleccione;

                                finalv.txtBodega.setText("0");
                                finalv.txtBodega.setVisibility(View.GONE);
                            } else if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("POSIBLE QUIEBRE")) {
                                datos_responsables = handler.getResponsablesOSA(tipo);
                                datos_descripcion = seleccione;

                                finalv.txtPercha.setText("");
                                finalv.txtPercha.setVisibility(View.VISIBLE);
                                finalv.txtBodega.setText("");
                                finalv.txtBodega.setVisibility(View.VISIBLE);
                            } else if (finalv.spBodega.getSelectedItem().toString().trim().equalsIgnoreCase("SELECCIONE")) {
                                datos_responsables = handler.getResponsablesOSA(tipo);
                                datos_descripcion = seleccione;

                                finalv.txtBodega.setText("");
                                finalv.txtBodega.setVisibility(View.GONE);
                            }

                            finalv.spResponsable.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, datos_responsables));
                            finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, datos_descripcion));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    finalv.spResponsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                            if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Logística")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_logistica));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
//                                    (subcanal.equals("AKI") || subcanal.equals("SUPERMAXI") || subcanal.equals("SANTA MARIA"))) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_SSA));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
//                                    (subcanal.equals("CORAL HIPERMERCADOS"))) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_C));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
//                                    (subcanal.equals("MI COMISARIATO"))) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_comisariato));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Cadena") &&
//                                    (subcanal.equals("ALMACENES TIA"))) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_cadena_tia));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Alicorp")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_alicorp));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Hularus")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_hularus));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("PDV")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_pdv));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("Lucky")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, razon_lucky));
//                            }else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,no_codifica));
//                            }else{
//                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,seleccione));
//                            }

                            if (!finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                                List<String> operadores = handler.getRazonesOSA(tipo, finalv.spResponsable.getSelectedItem().toString());
                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
                            } else if (finalv.spResponsable.getSelectedItem().toString().trim().equalsIgnoreCase("NA")) {
                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, no_codifica));
                            } else {
                                finalv.spDescripcion.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, seleccione));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {}

                    });

                    vHolder.chkGuardar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String skuSelected = finalv.txt_sku.getText().toString();
                            String codifica = finalv.spCodifica.getSelectedItem().toString().trim();
                            String bodega = finalv.spBodega.getSelectedItem().toString().trim();
                            String percha = finalv.spPercha.getSelectedItem().toString().trim();
                            String responsable = finalv.spResponsable.getSelectedItem().toString().trim();
                            String descripcion = finalv.spDescripcion.getSelectedItem().toString().trim();
               //             String sugerido = finalv.txtSugerido.getText().toString().trim();
               //             String tipo_sugerido = finalv.spTipoSugerido.getSelectedItem().toString().trim();
                            String quiebre_percha = finalv.txtPercha.getText().toString().trim();
                            String quiebre_bodega = finalv.txtBodega.getText().toString().trim();

                            String pvp = finalv.txt_precio_regular.getText().toString().trim();
                            String pvc = finalv.txt_precio_promocion.getText().toString().trim();
                            String poferta = finalv.txt_precio_oferta.getText().toString().trim();

                          //  int visibilidadSugerido = finalv.layoutSugerido.getVisibility();

                            //INICIO BLOQUE - NO SE USA
                            String index_codifica = finalv.spPercha.getSelectedItemPosition()+"";
                            String index_ausencia = finalv.spBodega.getSelectedItemPosition()+"";
                            String index_responsable = finalv.spResponsable.getSelectedItemPosition()+"";
                            String index_razones = finalv.spDescripcion.getSelectedItemPosition()+"";
                            //FIN BLOQUE - NO SE USA

                            if (esFormularioValido(bodega, percha, responsable, descripcion, skuSelected, quiebre_percha, quiebre_bodega)) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                builder.setTitle("Confirmación");
                                builder.setMessage("¿Desea guardar la información?");
                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //GUARDAR DATA
                                        insertData(skuSelected, codifica, bodega, percha, responsable,
                                                descripcion, index_codifica,
                                                index_ausencia, index_responsable, index_razones, pvp,
                                                pvc, poferta, quiebre_percha, quiebre_bodega);
                                        //LIMPIAR FORMULARIO
                                        limpiarFormulario(finalv.spCodifica, finalv.spBodega, finalv.spPercha,
                                                finalv.spResponsable, finalv.spDescripcion, finalv.txt_precio_regular,
                                                finalv.txt_precio_promocion, finalv.txt_precio_oferta,
                                                finalv.spTipoSugerido, finalv.txtSugerido,
                                                finalv.txtPercha, finalv.txtBodega,finalv.txt_sku);
                                    }
                                });

                                builder.setNeutralButton("NO", null);

                                android.app.AlertDialog ad = builder.create();
                                ad.show();
                            }
                        }
                    });
                }
            }catch (Exception e){
                Log.i("EXCEPTION", e.getMessage());
            }
            //Devolver al ListView la fila creada
            return convertView;
        }

        public void limpiarFormulario(Spinner spCodifica, Spinner spBodega, Spinner spPercha, Spinner spResponsable,
                                      Spinner spDescripcion, EditText txt_precio_regular, EditText txt_precio_promocion,
                                      EditText txt_precio_oferta, Spinner spTipoSugerido, EditText txtSugerido, EditText txtPercha, EditText txtBodega,TextView txtSku) {
            spCodifica.setSelection(0);
            spBodega.setSelection(0);
            spPercha.setSelection(0);
            spResponsable.setSelection(0);
            spDescripcion.setSelection(0);
        //    spTipoSugerido.setSelection(0);
        //    txtSugerido.setText("");
            txt_precio_regular.setText("");
            txt_precio_promocion.setText("");
            txt_precio_oferta.setText("");
            txtPercha.setText("");
            txtBodega.setText("");
            txtSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));

         //   layoutSugerido.setVisibility(View.VISIBLE);
        }

        public boolean esFormularioValido(String bodega, String percha, String responsable, String descripcion, String producto, String quiebre_percha, String quiebre_bodega) {
            if (percha.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar si el producto " + producto + ", se encuentra o no en percha", Toast.LENGTH_LONG).show();
                return false;
            }




            if (bodega.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar si el producto " + producto + ", se encuentra o no en bodega", Toast.LENGTH_LONG).show();
                return false;
            }
            if (responsable.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar el responsable del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            if (descripcion.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar la descripción del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            /*
            if (visibilidadSugerido==View.VISIBLE && sugerido.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar un sugerido en el producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            if (visibilidadSugerido==View.VISIBLE && tipo_sugerido.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar el tipo de sugerido en el producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            */

            if (!(percha.equalsIgnoreCase("STOCK SANO") && bodega.equalsIgnoreCase("STOCK SANO"))) {

                if (!quiebre_percha.trim().isEmpty()) {


                    if (percha.equalsIgnoreCase("POSIBLE QUIEBRE") && (Integer.parseInt(quiebre_percha) == 0)) {
                        Toast.makeText(getContext(), "Las unidades deben ser mayor a 0 " + producto, Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if (percha.equalsIgnoreCase("STOCK SANO") && (Integer.parseInt(quiebre_percha) == 0)) {
                        Toast.makeText(getContext(), "Las unidades deben ser mayor a 0 " + producto, Toast.LENGTH_LONG).show();
                        return false;
                    }


                } else {
                    Toast.makeText(getContext(), "Debe ingresar las unidades para el posible quiebre en percha del producto " + producto, Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                return true;
            }




            if (quiebre_bodega.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar las unidades para el posible quiebre en bodega del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }
}