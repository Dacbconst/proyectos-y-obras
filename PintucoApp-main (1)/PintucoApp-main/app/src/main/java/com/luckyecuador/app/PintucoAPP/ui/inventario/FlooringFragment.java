
package com.luckyecuador.app.PintucoAPP.ui.inventario;

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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.Base_Inventario;
import com.luckyecuador.app.PintucoAPP.Clase.Base_portafolio_productos;
import com.luckyecuador.app.PintucoAPP.Clase.Flooring;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.ui.canjes.CanjesFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FlooringFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener{

    ArrayList<Flooring> sesion = new ArrayList<Flooring>();
    ArrayList<Base_Inventario> sesion2 = new ArrayList<Base_Inventario>();
    ArrayList<Base_Inventario> type_name_copy = new ArrayList<Base_Inventario>();

    String rev = "";
    String norev = "";


    ArrayList<Flooring> flooringList;
    String skuSelected = "";

    private boolean seInsertoSKU = false;

    TextView txtfechav;
    TextView empty;
    RecyclerView listview;
    //ListView listDescripcion;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spGramaje;
    private int skuRevelados = 0;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    //private Spinner spPresentacion;
    private Spinner spMarca;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    private EditText txtDescripcion;
    private String semanaDelMesActual;
    private int semanaActualM;
    private ImageButton btnBuscador;

    public AlertDialog adMsj;
    public ProgressDialog progressDialog;

    private Button btnGuardar;

    private Handler handler2 = new Handler();
    private Runnable runnable;

    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, format, presentacion;
    private String id_pdv,user,codigo_pdv, punto_venta,celular,fecha,hora;
    private String fechaventas, producto, fecha_producto, cantidad_producto, poferta, sku, cuotas, vcuotas;

    private final String manufacturer = "AkzoNobel S.A";
    private final String modulo = "INVENTARIO";

    DatabaseHelper handler;

    ArrayList<Base_Inventario> listProductos;
    ArrayList<Flooring> skuRelevadosSemanaAnterior;
    ArrayList<Flooring> skuRelevadosSemanaActual;
    // List<String> listDescripcionL;

    List<String> filterProducts;
    List<String> filterTargets;

    public String venta, souv, causal, otros, canal, subcanal,gramaje;
    private SharedPreferences sharedPref;

    CustomAdapterInventario3 dataAdapter;
    //  CustomAdapterInventario dataAdapter;
    ListViewAdapter dataAdapters;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;


    String[] valueOfTextViewSku;
    String[] valueOfEditText;
    String[] valueOfSpinner;
    String[] valueOfSpinner2;
    String[] valueOfSpinnerT;
    String[] valueOfEditText2;
    String[] valueOfSpinner3;
    String[] valueOfEditText3;
    String[] valueOfEditText4;
    String[] valueOfEditText5;
    String[] valueOfSpinner4;
//    String[] valueOfTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_flooring, container, false);

        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Inventario + Agotados");


        //setActionBar();
        LoadData();

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();



        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spGramaje = (Spinner) rootView.findViewById(R.id.spGramaje);
      //  btnBuscador = (ImageButton) rootView.findViewById(R.id.btnBuscador);
      //  btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        //spPresentacion = (Spinner)findViewById(R.id.spPresentacion);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);
        btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);

        //startService(new Intent(getContext(), MyService.class));

        /*listview = (RecyclerView)findViewById(R.id.lvSKUCode);
        listview.setHasFixedSize(true);*/

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        //listDescripcion = (ListView)findViewById(R.id.lvDescripcion);
        filtrarCategoria(manufacturer);

        obtenerSemanaDelMesActual();

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Inventario");

/*
        Toast.makeText(getContext(), ""+semanaDelMesActual.charAt(semanaDelMesActual.length()
                -1), Toast.LENGTH_SHORT).show();
*/

    //    btnGuardar.setOnClickListener(this);


/*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String item = (String) listview.getItemAtPosition(position);
                System.out.println(item);
                //txtDescripcion.setText(item);

            }
        });
        */

        listview = (RecyclerView) rootView.findViewById(R.id.lvSKUCode);
//        listview.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//
//        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_flooring_title,null,false);
//        listview.addHeaderView(headerView,null,false);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public void filtrarCategoria(String manufacturer) {
        List<String> operadores = handler.getCategoriaFlooring2(codigo_pdv, canal, subcanal, manufacturer,modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void filtrarSubcategoria(String categoria, String manufacturer) {
        List<String> operadores = handler.getSubcategoriaFlooring2(categoria, codigo_pdv, canal, subcanal, manufacturer,modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    /*public void filtrarMarca(String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento1(subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }.

    public void filtrarSegmento(String subcategoria, String subcategoria, String subcategoria) {
        List<String> operadores = handler.getSegmento2(subcategoria,subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }*/

    /*public void filtrarFabricante(String logro, String subcategoria, String manufacturer) {
        List<String> operadores = handler.getPresentacionFlooring(logro,subcategoria,manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentacion.setAdapter(dataAdapter);
        spPresentacion.setOnItemSelectedListener(this);
    }*/

    public void filtrarBrand(String categoria, String subcategoria, String manufacturer) {
        List<String> operadores = handler.getBrandFlooring2(categoria, subcategoria, codigo_pdv, canal, subcanal,manufacturer,modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    public void filtrarGramaje(String categoria, String subcategoria, String marca, String manufacturer) {
        List<String> operadores = handler.getGramajeFlooring2(categoria, subcategoria, marca, codigo_pdv, canal, subcanal,manufacturer,modulo);

        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGramaje.setAdapter(dataAdapter);
        spGramaje.setOnItemSelectedListener(this);
    }


    /*public void filtrartamano(String subcategoria, String subcategoria, String subcategoria, String presentacion, String contenido) {
        List<String> operadores = handler.getTamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTamano.setAdapter(dataAdapter);
        spTamano.setOnItemSelectedListener(this);
    }

    public void filtrarcantidad(String subcategoria, String subcategoria, String subcategoria, String presentacion, String contenido, String tamano) {
        List<String> operadores = handler.getCantidad(subcategoria,subcategoria,subcategoria,presentacion,contenido, tamano);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(dataAdapter);
        spCantidad.setOnItemSelectedListener(this);
    }*/


    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        celular =sharedPreferences.getString(Constantes.CELULAR,Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal =sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(i2 + "/" + (i1+1) + "/" + i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        txtfechav.setText(outDate);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView== spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
               // filtrarSubcategoria(categoria, manufacturer);
                filtrarBrand(categoria, subcategoria, manufacturer);
            //    listview.setAdapter(null);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*
        if (adapterView== spSubcategoria) {
            try{
                subcategoria = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(categoria, subcategoria, manufacturer);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        */
        /*if (adapterView== spPresentacion) {
            try{
                presentacion = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(logro, subcategoria, manufacturer);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spSubcategoria) {
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                filtrarSegmento(subcategoria,subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spSegmento) {
            try{
                presentacion=adapterView.getItemAtPosition(i).toString();
                filtrarMarca(subcategoria,subcategoria,subcategoria,presentacion);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
        if (adapterView== spMarca) {
            try{
                brand = adapterView.getItemAtPosition(i).toString();
                filtrarGramaje(categoria,subcategoria,brand,manufacturer);

             //   showListView(categoria, subcategoria, brand, manufacturer);

             //   alertSku();

                //         Toast.makeText(getContext(),""+norev,Toast.LENGTH_LONG).show();
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView== spGramaje) {
            try{
                listProductos = null;
                gramaje = adapterView.getItemAtPosition(i).toString();
                showListView(categoria, subcategoria, brand, gramaje, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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

    public void alertSku(){

        rev = "";
        norev = "";

        if (canal.equals("MAYORISTA")){
                if (semanaActualM != 1){


                    // Obtenemos Los sku(s) insertados de la semanas anteriores a la actual
                    skuRelevadosSemanaAnterior =  handler.getSkuRelevadosInventarioSemanaAnterior(codigo_pdv,categoria,subcategoria,brand,semanaActualM);
                    skuRelevadosSemanaActual =  handler.getSkuRelevadosInventarioSemanaActual(codigo_pdv,categoria,subcategoria,brand,semanaActualM);



                    for (int  i =0;i < skuRelevadosSemanaAnterior.size() ; i++){
                        if (skuSelected.equals(skuRelevadosSemanaAnterior.get(i).getSku_code())) {
                            skuRelevadosSemanaAnterior.remove(i);
                            break;
                        }
                    }


               //     Toast.makeText(getContext(),"actual : "+skuRelevadosSemanaActual.size(),Toast.LENGTH_LONG).show();
               //     Toast.makeText(getContext()," anterior : "+skuRelevadosSemanaAnterior.size(),Toast.LENGTH_LONG).show();


                    if (skuRelevadosSemanaAnterior.size() > 0){


                        int count = 1;
                        boolean seEncuentra;
                        for (Flooring listProducto : skuRelevadosSemanaAnterior) {

                            seEncuentra = false;
                            for (Flooring flooring : skuRelevadosSemanaActual) {
                                if (listProducto.getSku_code().equals(flooring.getSku_code())) {
                                    seEncuentra = true;
                                    break;
                                }

                            }

                            if (seEncuentra) {
                                rev = rev + " " + count + ". " + listProducto.getSku_code() + ". \n";
                            } else {
                                norev = norev + " " + count + ". " + listProducto.getSku_code()+ ". \n";
                                count++;
                            }

                        }



                        norev = norev + " \n";

                        //    alertDialogSkU(norev);



                    }



                }
        }

    }

//    public void showListView(String categoria, String subcategoria, String brand,String gramaje, String manufacturer) {
////        listProductos = handler.filtrarListProductos3Flooring(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
////        dataAdapter = new CustomAdapterInventario(getContext(), listProductos);
////        if (!dataAdapter.isEmpty()) {
////            empty.setVisibility(View.INVISIBLE);
////            listview.setVisibility(View.VISIBLE);
////            listview.setAdapter(dataAdapter);
////
//        txtSKUCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<BasePortafolioProductos>();
////
////                String text = s.toString();
////
////
////                for (int i = 0; i < listProductos.size(); i++) {
////                    if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
////                        BasePortafolioProductos p = new BasePortafolioProductos();
////                        p.setSku(listProductos.get(i).getSku());
////                        type_name_filter.add(p);
////                    }
////                }
////
////                type_name_copy = type_name_filter;
////                listUpdate(type_name_copy);
////                //filter(s.toString());
////                //dataAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //filter(s.toString());
//                Handler handler = new Handler(Looper.getMainLooper()); // Asegúrate de usar el hilo principal
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Código a ejecutar después de 2 segundos
//                        System.out.println("Esto se ejecuta después de 2 segundos");
//
//                        ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<BasePortafolioProductos>();
//
//                        String text = s.toString();
//
//
//                        for (int i = 0; i < listProductos.size(); i++) {
//                            if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
//                                BasePortafolioProductos p = new BasePortafolioProductos();
//                                p.setSku(listProductos.get(i).getSku());
//                                type_name_filter.add(p);
//                            }
//                        }
//
//                        type_name_copy = type_name_filter;
//                        listUpdate(type_name_copy);
//                        //filter(s.toString());
//                        //dataAdapter.getFilter().filter(s);
//
//
//                    }
//                }, 2000); // Tiempo de retraso en milisegundos (2000 ms = 2 segundos)
//            }
//        });
////
////        }else{
////            empty.setVisibility(View.VISIBLE);
////        }
//
//     //   skuRelevados = handler.getSkuRelevadosInventario(codigo_pdv,categoria,subcategoria,brand);
//
//        listProductos = handler.filtrarListProductos3Flooring(categoria, subcategoria, brand,gramaje, codigo_pdv, canal, subcanal, manufacturer,modulo);
//
//        dataAdapter = new CustomAdapterInventario3(getContext(), listProductos);
//        if (dataAdapter.getItemCount() != 0) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//            empty.setVisibility(View.GONE);
//            listview.setLayoutManager(linearLayoutManager);
//            listview.setHasFixedSize(true);
//            CustomAdapterInventario3 customAdapter = new CustomAdapterInventario3(this.getContext(),listProductos);
//            listview.setAdapter(customAdapter);
//            listview.setVisibility(View.VISIBLE);
//
//            int productos = sesion.size();
///*
//            if ( != skuRevelados){
//                alertDialogNoRelevados();
//            }
//*/
//
//        }else{
//            listview.setVisibility(View.GONE);
//            empty.setVisibility(View.VISIBLE);
//        }
//    }
//




    public void showListView(String categoria, String subcategoria, String brand, String gramaje, String manufacturer) {
        // Ejecutar en el hilo principal
    //    Handler mainHandler = new Handler(Looper.getMainLooper());
    //    mainHandler.post(() -> {
            // Obtenemos los productos filtrados desde la base de datos
            listProductos = handler.filtrarListProductos3Flooring(categoria, subcategoria, brand, gramaje, codigo_pdv, canal, subcanal, manufacturer, modulo);

            // Configuramos el adaptador
            dataAdapter = new CustomAdapterInventario3(getContext(), listProductos);

            if (dataAdapter.getItemCount() != 0) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                empty.setVisibility(View.GONE);
                listview.setLayoutManager(linearLayoutManager);
                listview.setHasFixedSize(true);

                CustomAdapterInventario3 customAdapter = new CustomAdapterInventario3(this.getContext(), listProductos);
                listview.setAdapter(customAdapter);
                listview.setVisibility(View.VISIBLE);

/*
                btnBuscador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<>();

                       String texto =  txtSKUCode.getText().toString();



                        for (int i = 0; i < listProductos.size(); i++) {
                            if ((listProductos.get(i).getSku().toLowerCase()).contains(texto.toLowerCase())) {
                                BasePortafolioProductos p = new BasePortafolioProductos();
                                p.setSku(listProductos.get(i).getSku());
                                type_name_filter.add(p);
                            }
                        }

                        type_name_copy = type_name_filter;
                        listUpdate(type_name_copy);


                    }
                });
*/


                // Escucha de cambios en el texto
/*
                txtSKUCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                    //    mainHandler.postDelayed(() -> {
                            ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<>();

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
                    //    }, 2000); // Retraso de 2 segundos
                    }
                });
*/


                txtSKUCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    //    ProgressDialog progressDialog = new ProgressDialog(getContext());
//
//                        if (progressDialog == null) {
//                            progressDialog = new ProgressDialog(getContext());
//                            progressDialog.setMessage("Procesando...");
//                            progressDialog.setCancelable(false); // Evita que se cierre accidentalmente
//                        }
//
//                        if (!progressDialog.isShowing()) {
//                            progressDialog.show(); // Mostrar ProgressDialog cuando el usuario empieza a escribir
//                        }
//
//                        if (runnable != null) {
//                            handler2.removeCallbacks(runnable);
//                            // Cancelamos la ejecución previa si el usuario sigue escribiendo
//                        }     runnable = () -> {
//                            // Código que se ejecuta después de que el usuario deja de escribir por 1 segundo
//
//                            ArrayList<Base_Inventario> type_name_filter = new ArrayList<>();
//                            String text = s.toString().toLowerCase().trim();
//
//                            // Búsqueda en la lista de roductos
//                            for (Base_Inventario producto : listProductos) {
//                                if (producto.getSku().toLowerCase().contains(text)) {
//                                    type_name_filter.add(producto);
//
//                                }
//                            }
//
//                            // Actualizar la lista de productos en la UI
//                            type_name_copy = type_name_filter;
//                            listUpdate(type_name_copy);
//
//                            if (progressDialog.isShowing()) {
//                                progressDialog.dismiss(); // Cerrar el ProgressDialog
//                            }
//
//
//
//
//
//
//                             Log.d("TextWatcher", "Usuario terminó de escribir: " + s.toString());
//                             }; // Espera 1 segundo (1000 ms) después de la última entrada antes de ejecutar el código
//
//
//
//
//                    }
//

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (runnable != null) {
                            handler2.removeCallbacks(runnable); // Cancelamos la ejecución previa si el usuario sigue escribiendo
                        }

                        runnable = () -> {
                            // Mostrar ProgressDialog cuando se inicia el proceso (después del tiempo de espera)
                            if (progressDialog == null) {
                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Procesando...");
                                progressDialog.setCancelable(false);
                            }

                            ArrayList<Base_Inventario> type_name_filter = new ArrayList<>();
                            String text = s.toString().toLowerCase().trim();

                            // Búsqueda en la lista de roductos
                            for (Base_Inventario producto : listProductos) {
                                if (producto.getSku().toLowerCase().contains(text)) {
                                    type_name_filter.add(producto);

                                }
                            }

                            progressDialog.show(); // Mostrar después de que el usuario deja de escribir
                            // Actualizar la lista de productos en la UI
                            type_name_copy = type_name_filter;
                            listUpdate(type_name_copy);



                            // Simulación de una tarea que tarda en ejecutarse (puedes poner tu lógica real aquí)
                            handler2.postDelayed(() -> {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss(); // Cerrar ProgressDialog después de ejecutar el código
                                }
                                Log.d("TextWatcher", "Usuario terminó de escribir: " + s.toString());
                            }, 1500); // Simulamos que el proceso dura 1.5 segundos
                        };

                        // Espera 1 segundo (1000 ms) después de la última entrada antes de ejecutar el código
                        handler2.postDelayed(runnable, 1000);
                    }


                });

            } else {
                listview.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
 //       });
    }



//
//    public void showListView(String categoria, String subcategoria, String brand, String gramaje, String manufacturer) {
//        // Mostrar un indicador de carga mientras se obtienen los datos
//        empty.setVisibility(View.GONE);
//        listview.setVisibility(View.GONE);
//
//        new Thread(() -> {
//            // Operaciones costosas (cargar datos desde la base de datos)
//            ArrayList<BasePortafolioProductos> productosFiltrados = handler.filtrarListProductos3Flooring(
//                    categoria, subcategoria, brand, gramaje, codigo_pdv, canal, subcanal, manufacturer, modulo
//            );
//
//            // Actualizar la UI en el hilo principal
//            requireActivity().runOnUiThread(() -> {
//                if (productosFiltrados != null && !productosFiltrados.isEmpty()) {
//                    listProductos = productosFiltrados;
//
//                    // Configurar el RecyclerView y el adaptador
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                    listview.setLayoutManager(linearLayoutManager);
//                    listview.setHasFixedSize(true);
//
//                    CustomAdapterInventario3 customAdapter = new CustomAdapterInventario3(this.getContext(), listProductos);
//                    listview.setAdapter(customAdapter);
//
//                    // Mostrar el RecyclerView
//                    listview.setVisibility(View.VISIBLE);
//                    empty.setVisibility(View.GONE);
//
//                    // Agregar el TextWatcher
//                    txtSKUCode.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            String text = s.toString();
//
//                            new Thread(() -> {
//                                // Filtrar datos en segundo plano
//                                ArrayList<BasePortafolioProductos> filteredList = new ArrayList<>();
//                                for (BasePortafolioProductos producto : listProductos) {
//                                    if (producto.getSku().toLowerCase().contains(text.toLowerCase())) {
//                                        BasePortafolioProductos p = new BasePortafolioProductos();
//                                        p.setSku(producto.getSku());
//                                        filteredList.add(p);
//                                    }
//                                }
//
//                                // Actualizar la UI con los datos filtrados
//                                requireActivity().runOnUiThread(() -> {
//                                    type_name_copy = filteredList;
//                                    listUpdate(type_name_copy);
//                                });
//
//                            }).start();
//                        }
//                    });
//                } else {
//                    // Mostrar el mensaje vacío si no hay datos
//                    listview.setVisibility(View.GONE);
//                    empty.setVisibility(View.VISIBLE);
//                }
//            });
//        }).start();
//    }
//
//






    private void alertDialogSkU(String noRelevados, String skuSelected, String semana, String stock, String sugerido, String observacion, String fecha_caducidad) {
        if (canal.equals("MAYORISTA")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater2 = getLayoutInflater();
            View dialogView = inflater2.inflate(R.layout.alertdialog_sku_no_relevados, null);
            builder.setView(dialogView);
            TextView tvSemanaActual = (TextView) dialogView.findViewById(R.id.tvSemanaActual);


            tvSemanaActual.setText("Semana Actual : "+ semanaDelMesActual);

            TextView tvNoRelevado = (TextView) dialogView.findViewById(R.id.tvNoRelevado);
          //  tv.setText("$"+ antes2);
            tvNoRelevado.setText(noRelevados);
            Button btn = (Button) dialogView.findViewById(R.id.boton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // insertData(skuSelected,semana,stock,sugerido,observacion,fecha_caducidad);
                    adMsj.dismiss();
                    adMsj = null;
                }

            });

            if (btn.isClickable()){
                seInsertoSKU = true;
            }


            adMsj = builder.create();
            adMsj.show();
            adMsj.setCancelable(false);

        }
    }



    public void listUpdate(ArrayList<Base_Inventario> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterInventario3(getContext(), data));

        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }
/*
    public void showListDescripcion(String status, String subcategoria,String subcategoria,String presentacion, String contenido, String tamano, String cantidad) {
        listDescripcionL = handler.filtrarListDescripcion(status,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
        dataAdapter = new CustomAdapterPresenciaMinima(getContext(),listDescripcionL);
        if (!dataAdapter.isEmpty()) {
            listDescripcion.setAdapter(dataAdapter);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }
*/
    /*public class CustomAdapterPresenciaMinima extends ArrayAdapter<BasePortafolioProductos> implements Filterable {

        public ArrayList<BasePortafolioProductos> values;
        public Context context;
        boolean[] checkBoxState;

        public CustomAdapterPresenciaMinima(Context context, ArrayList<BasePortafolioProductos> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState=new boolean[values.size()];
        }

        public class ViewHolder{
            TextView lblSku;
            CheckBox check; //agregado GT
            //EditText txtunidad;
            EditText txtventa;
            EditText txtsouv;
            Spinner spCausal;
            EditText txtOtros;
           // Spinner spMotivo;
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Obtener Instancia Inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder vHolder = null;
            //Comprobar si el View existe
            //Si no existe inflarlo
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_option, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.check = (CheckBox) convertView.findViewById(R.id.checkPresencia);
             //   vHolder.txtunidad = (EditText) convertView.findViewById(R.id.txtunidad);
                vHolder.txtventa = (EditText) convertView.findViewById(R.id.txtventa);
                vHolder.txtsouv = (EditText) convertView.findViewById(R.id.txtsouv);
                vHolder.spCausal = (Spinner) convertView.findViewById(R.id.spCausal);
                vHolder.txtOtros = (EditText) convertView.findViewById(R.id.txtDetalle);

                //cargarMotivos(vHolder);

                convertView.setTag(vHolder);

                //checkGuardar.setEnabled(true);
            } else { vHolder = (ViewHolder) convertView.getTag(); }

            if (values.size() > 0) {
                //set the data to be displayed
                vHolder.lblSku.setText(values.get(position).getSku());
               // sku = vHolder.lblSku.getText().toString();
                // checkGuardar.setEnabled(false);
                final ViewHolder finalv = vHolder;

                vHolder.spCausal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!finalv.spCausal.getSelectedItem().toString().trim().equalsIgnoreCase("OTROS")) {
                            finalv.txtOtros.setEnabled(false);
                            finalv.txtOtros.setText("-");
                        }else{
                            finalv.txtOtros.setEnabled(true);
                            finalv.txtOtros.setText("");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                if (finalv.spCausal.getSelectedItem().toString().equalsIgnoreCase("OTROS")) {
                    finalv.txtOtros.setText("-");
                }else{
                    finalv.txtOtros.setText("-");
                }

                vHolder.check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                     //  unidad= finalv.txtunidad.getText().toString();
                        venta = finalv.txtventa.getText().toString();
                        souv = finalv.txtsouv.getText().toString();
                        causal = finalv.spCausal.getSelectedItem().toString();
                        otros = finalv.txtOtros.getText().toString();

                        if (((CheckBox)v).isChecked()) {
                            if (!venta.equals("") && !souv.equals("")) {
                                String sku =finalv.lblSku.getText().toString();
                                insertData(sku,venta,souv,causal,otros,"FECHA CADUCIDAD");
                            }else{
                                Toast.makeText(getContext(), "Verifica el Ingreso en Inventario", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }
    }*/

/*
    private void alertDialogNoRelevados() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView = inflater2.inflate(R.layout.alertdialog_sku_no_relevados, null);
        builder.setView(dialogView);
        TextView tv = (TextView) dialogView.findViewById(R.id.num);
        tv.setText("$");
        Button btn = (Button) dialogView.findViewById(R.id.boton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adMsj.dismiss();
                adMsj = null;
            }
        });
        adMsj = builder.create();
        adMsj.show();
        adMsj.setCancelable(false);
    }
*/

    public void insertData2() {
        //Almacenar Datos
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);
        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        String categoria = spCategoria.getSelectedItem().toString();
    //    String subcategoria = spSubcategoria.getSelectedItem().toString();
        String brand = spMarca.getSelectedItem().toString();

        int contador = 0;
        String mensaje = "";

        do {
            String sku = "";
            String istock = "";
            String isemana = "";
            String isugerido = "";
            String itipo = "";
            String icantidad = "";
            String ientrega = "";
            String iobservaciones = "";
            String ioTipoSug = "";
//            String icaducidad = "";
            String iFechaCaducidad = "";

            for (int i = 0; i < valueOfTextViewSku.length; i++) {
                sku = valueOfTextViewSku[0].toString();
                Log.i("SKU 1", sku);
            }
            for (int i = 0; i < valueOfEditText.length; i++) {
                istock = valueOfEditText[0].toString();
                Log.i("STOCK 1", istock);
            }
//            for (int i = 0; i < valueOfSpinner.length; i++) {
//                isemana = valueOfSpinner[0].toString();
//            }
            for (int i = 0; i < valueOfSpinner2.length; i++) {
                isugerido = valueOfSpinner2[0].toString();
            }
            for (int i = 0; i < valueOfSpinnerT.length; i++) {
                itipo = valueOfSpinnerT[0].toString();
            }
            for (int i = 0; i < valueOfEditText2.length; i++) {
                icantidad = valueOfEditText2[0].toString();
                Log.i("CANTIDAD 1", icantidad);
            }
            for (int i = 0; i < valueOfSpinner4.length; i++) {
                ientrega = valueOfSpinner4[0].toString();
            }
            for (int i = 0; i < valueOfEditText4.length; i++) {
                iobservaciones = valueOfEditText4[0].toString();
            }

            for (int i = 0; i < valueOfEditText5.length; i++) {
                iFechaCaducidad = valueOfEditText5[0].toString();
            }

            for (int i = 0; i < valueOfSpinner3.length; i++) {
                ioTipoSug = valueOfSpinner3[0].toString();
            }
//            for (int i = 0; i < valueOfTextView.length; i++) {
//                icaducidad = valueOfTextView[0].toString();
//            }

            List<String> list_textviewsku = new ArrayList<String>(Arrays.asList(valueOfTextViewSku));
            list_textviewsku.remove(0);
            valueOfTextViewSku = list_textviewsku.toArray(new String[0]);

            List<String> list_edittext = new ArrayList<String>(Arrays.asList(valueOfEditText));
            list_edittext.remove(0);
            valueOfEditText = list_edittext.toArray(new String[0]);

//            List<String> list_spinner = new ArrayList<String>(Arrays.asList(valueOfSpinner));
//            list_spinner.remove(0);
//            valueOfSpinner = list_spinner.toArray(new String[0]);

            List<String> list_spinner2 = new ArrayList<String>(Arrays.asList(valueOfSpinner2));
            list_spinner2.remove(0);
            valueOfSpinner2 = list_spinner2.toArray(new String[0]);

            List<String> list_spinner4 = new ArrayList<String>(Arrays.asList(valueOfSpinner4));
            list_spinner4.remove(0);
            valueOfSpinner4 = list_spinner4.toArray(new String[0]);

            List<String> list_spinnerT = new ArrayList<String>(Arrays.asList(valueOfSpinnerT));
            list_spinnerT.remove(0);
            valueOfSpinnerT = list_spinnerT.toArray(new String[0]);

            List<String> list_edittext2 = new ArrayList<String>(Arrays.asList(valueOfEditText2));
            list_edittext2.remove(0);
            valueOfEditText2 = list_edittext2.toArray(new String[0]);

            List<String> list_spinner3 = new ArrayList<String>(Arrays.asList(valueOfSpinner3));
            list_spinner3.remove(0);
            valueOfSpinner3 = list_spinner3.toArray(new String[0]);

            List<String> list_edittext3 = new ArrayList<String>(Arrays.asList(valueOfEditText3));
            list_edittext3.remove(0);
            valueOfEditText3 = list_edittext3.toArray(new String[0]);

            List<String> list_edittext4 = new ArrayList<String>(Arrays.asList(valueOfEditText4));
            list_edittext4.remove(0);
            valueOfEditText4 = list_edittext4.toArray(new String[0]);

            List<String> list_edittext5 = new ArrayList<String>(Arrays.asList(valueOfEditText5));
            list_edittext5.remove(0);
            valueOfEditText5 = list_edittext5.toArray(new String[0]);

//            List<String> list_textview = new ArrayList<String>(Arrays.asList(valueOfTextView));
//            list_textview.remove(0);
//            valueOfTextView = list_textview.toArray(new String[0]);

            String contenido = handler.getContenidoPrecios(sku);
            String sector = handler.getSectorPrecios(sku);
            String plataforma = handler.getPlataformaBySku(sku);

            if (!istock.trim().isEmpty()) {

                if(ientrega.equalsIgnoreCase("Seleccione")){
                    ientrega = "NA";
                }
                if(itipo.equalsIgnoreCase("Seleccione")){
                    itipo = "NA";
                }
                if (brand.equalsIgnoreCase("SURF")){
//                    icaducidad = "NA";
                    icantidad = "NA";
                }

                values.put(InsertFlooring.Columnas.PHARMA_ID, id_pdv);
                values.put(InsertFlooring.Columnas.CODIGO, codigo_pdv);
                values.put(InsertFlooring.Columnas.USUARIO, user);
                values.put(InsertFlooring.Columnas.SUPERVISOR, punto_venta);
                values.put(InsertFlooring.Columnas.FECHA, fechaser);
                values.put(InsertFlooring.Columnas.HORA, horaser);
                values.put(InsertFlooring.Columnas.SECTOR, sector);
                values.put(InsertFlooring.Columnas.CATEGORIA, categoria);
                values.put(InsertFlooring.Columnas.SUBCATEGORIA, "N/A");
                values.put(InsertFlooring.Columnas.PRESENTACION, "");
                values.put(InsertFlooring.Columnas.BRAND, brand);
                values.put(InsertFlooring.Columnas.CONTENIDO, contenido);
                values.put(InsertFlooring.Columnas.SKU_CODE, sku);
                values.put(InsertFlooring.Columnas.INVENTARIOS, istock);  //stock
                values.put(InsertFlooring.Columnas.SEMANA, "NA");
                values.put(InsertFlooring.Columnas.SUGERIDOS, icantidad);
                values.put(InsertFlooring.Columnas.TIPO, iobservaciones);//tipo_unidades -> stock_bodega
                values.put(InsertFlooring.Columnas.ENTREGA, ientrega);//sugeridos si/no
                if(ioTipoSug.equals("Seleccione")){
                    values.put(InsertFlooring.Columnas.CAUSAL, "N/A");//tipo_sugeridos
                }else{
                    values.put(InsertFlooring.Columnas.CAUSAL, ioTipoSug);//tipo_sugeridos
                }
                values.put(InsertFlooring.Columnas.OTROS, itipo);//tipo stock bodega
                values.put(InsertFlooring.Columnas.FECHA_CADUCIDAD, "NA"); //icaducidad
                values.put(InsertFlooring.Columnas.POS_NAME, punto_venta);
                values.put(InsertFlooring.Columnas.PLATAFORMA, plataforma);
                values.put(InsertFlooring.Columnas.FECHA_INVENTARIO, iFechaCaducidad);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContext().getContentResolver().insert(InsertFlooring.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getContext())) {
                    SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertFlooring, null);
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }
                vaciarCampos();


            }

            if (contador == 0) {
                mensaje = "_*XPLORA*_ de _*Grupo Lucky*_ te envía la siguiente información. *Cliente:* " + punto_venta + "\r\n";
            }
            Log.i("MENSAJE ANTES", mensaje + " " + ientrega);
            if (ientrega.equalsIgnoreCase("SI")){
                //istock, icantidad, ioTipoSug;
                mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&", "-").replace("#", "") + "\r\n*Cantidad Inventario:* "+ istock +"\r\n*Cantidad Sugerido:* " + icantidad + "\r\n" +"*Tipo Sugerido:* " + ioTipoSug + "\r\n";
                contador++;
                /*if (iobservaciones.equalsIgnoreCase("")) {
                    mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&", "-").replace("#", "") + "\r\n*Cantidad:* "+ icantidad +" "+ itipo +"\r\n*Despacho:* " + ientrega + "\r\n";
                    //enviarMensaje(celular, mensaje);
                }else{
                    mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&"l, "-").replace("#", "") + "\r\n*Cantidad:* "+ icantidad +" "+ itipo +"\r\n*Despacho:* " + ientrega + "\r\n*Observaciones:* " + iobservaciones +"\r\n";
                }
                contador++;*/
            }

        }while(valueOfTextViewSku.length!=0 || valueOfEditText.length!=0);
        Log.i("ANTES DE CONTADOR","SI");
        if(contador>0){
            mensaje.replace("&","-").replace("#","-");
            Log.i("MENSAJE 1", mensaje);
          //  enviarMensajeWhatsapp(mensaje);
        }

    }

    public void vaciarCampos(){
        //txtCodigo.setText("");
        spCategoria.setSelection(0);
        spSubcategoria.setSelection(0);
        spMarca.setSelection(0);
        listview.setVisibility(View.GONE);

    }

    private void enviarMensajeWhatsapp(String messagestr) {
        String phonestr = celular;
        Log.i("NUM CELUALR", celular);
        if (!messagestr.isEmpty() && !phonestr.isEmpty()) {
            if (isWhatappInstalled() || isWhatappBusinessInstalled()) {
                if (!phonestr.equals("N/A") || !phonestr.equals("NA") || !phonestr.contains("+")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+"&text="+messagestr));
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(),"Numero de telefono no puede ser: " + phonestr, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(),"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor, verificar el número de teléfono o el mensaje, podrían estar vacios", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isWhatappInstalled() {
        PackageManager packageManager = getContext().getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    private boolean isWhatappBusinessInstalled() {
        PackageManager packageManager = getContext().getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp.w4b",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

//    public boolean insertData(String skuSelected, String semana, String stock, String sugerido, String observacion, String fecha_caducidad) {
//        //Almacenar Datos
//        ContentValues values = new ContentValues();
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
//        Date currentLocalTime = cal.getTime();
//        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
//        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//        String fechaser = date.format(currentLocalTime);
//        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
//        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//        String horaser = hour.format(currentLocalTime);
//
//        String categoria = spCategoria.getSelectedItem().toString();
//        String subcategoria = spSubcategoria.getSelectedItem().toString();
//        /*String subcategoria = spSubcategoria.getSelectedItem().toString();
//        String segmento = spSegmento.getSelectedItem().toString();*/
//        String brand = spMarca.getSelectedItem().toString();
//        /*String tamano = spTamano.getSelectedItem().toString();
//        String cantidad = spCantidad.getSelectedItem().toString();*/
//
//        //String presentacion = spPresentacion.getSelectedItem().toString();
//
//
//
//            String contenido = handler.getContenidoPrecios(skuSelected);
//            String sector = handler.getSectorPrecios(skuSelected);
//            String plataforma = handler.getPlataformaBySku(skuSelected);
//
//
//
//            values.put(InsertFlooring.Columnas.PHARMA_ID, id_pdv);
//            values.put(InsertFlooring.Columnas.CODIGO, codigo_pdv);
//            values.put(InsertFlooring.Columnas.USUARIO, user);
//            values.put(InsertFlooring.Columnas.SUPERVISOR, punto_venta);
//            values.put(InsertFlooring.Columnas.FECHA, fechaser);
//            values.put(InsertFlooring.Columnas.HORA, horaser);
//            values.put(InsertFlooring.Columnas.SECTOR, sector);
//            values.put(InsertFlooring.Columnas.CATEGORIA, categoria);
//            values.put(InsertFlooring.Columnas.SUBCATEGORIA, subcategoria);
//            values.put(InsertFlooring.Columnas.PRESENTACION, "");
//            values.put(InsertFlooring.Columnas.BRAND, brand);
//            values.put(InsertFlooring.Columnas.CONTENIDO, contenido);
//            values.put(InsertFlooring.Columnas.SKU_CODE, skuSelected);
//            values.put(InsertFlooring.Columnas.INVENTARIOS, stock);
//            values.put(InsertFlooring.Columnas.SUGERIDOS, sugerido);
//            if (canal.equals("MAYORISTA")){
//                values.put(InsertFlooring.Columnas.SEMANA, semana);
//            }else{
//                values.put(InsertFlooring.Columnas.SEMANA, "");
//            }
//            values.put(InsertFlooring.Columnas.CAUSAL, "");
//            values.put(InsertFlooring.Columnas.OTROS, observacion);
//            values.put(InsertFlooring.Columnas.FECHA_CADUCIDAD, fecha_caducidad);
//            values.put(InsertFlooring.Columnas.POS_NAME, punto_venta);
//            values.put(InsertFlooring.Columnas.PLATAFORMA, plataforma);
//            values.put(Constantes.PENDIENTE_INSERCION, 1);
//
//            getContext().getContentResolver().insert(InsertFlooring.CONTENT_URI, values);
//
//
//            if (VerificarNet.hayConexion(getContext())) {
//                SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertFlooring, null);
//                Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
//            }
//
//        return true;
//    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onClick(View view) {
        /*
        if (view == btnGuardar){
        //   guardar();
        }
        */
    }
/*
    private void alertDialogSkuPorRelevar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView = inflater2.inflate(R.layout.alertdialog_sku_no_relevados, null);
        builder.setView(dialogView);
        TextView tv = (TextView) dialogView.findViewById(R.id.num);

        String skuNoRelevados = "";

        for (int i = 0; i < skuRelevados.size() ; i++){
            for (int j = 0 ; j < listProductos.size();j++){
                if (!skuRelevados.get(i).getSku().equals(listProductos.get(j).getSku())){
                    skuNoRelevados = "*" +
                }
            }
        }

        tv.setText("$"+ antes2);
        Button btn = (Button) dialogView.findViewById(R.id.boton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adMsj.dismiss();
                adMsj = null;
                finalv.spinner.setSelection(0);
            }
        });
        adMsj = builder.create();
        adMsj.show();
        adMsj.setCancelable(false);
    }
*/


//    public class CustomAdapterInventario extends ArrayAdapter<Flooring> implements Filterable {
//
//        public ArrayList<Flooring> values;
//        public Context context;
//        boolean[] checkBoxState;
//
//        public CustomAdapterInventario(Context context, ArrayList<Flooring> values) {
//            super(context, 0, values);
//            this.values = values;
//            checkBoxState = new boolean[values.size()];
//        }
//
//        public class ViewHolder{
//            TextView lblSku;
//            CheckBox checkGuardar; //agregado GT
//            TextView txtFecha;
//            EditText txtCantidad;
//            ImageButton btnFechaProd;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            // TODO Auto-generated method stub
//            return values.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            //Obtener Instancia Inflater
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            CustomAdapterInventario.ViewHolder vHolder = null;
//            //Comprobar si el View existe
//            //Si no existe inflarlo
//            if (null == convertView) {
//                convertView = inflater.inflate(R.layout.list_row_flooring, parent, false); // Modificacion (list_row_option) GT
//                //Obtener instancias de los elementos
//                vHolder = new CustomAdapterInventario.ViewHolder();
//                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
//                vHolder.checkGuardar = (CheckBox) convertView.findViewById(R.id.checkGuardar);
//                vHolder.txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);
//                vHolder.txtCantidad = (EditText) convertView.findViewById(R.id.txtCantidad);
//                vHolder.btnFechaProd = (ImageButton) convertView.findViewById(R.id.btnFechaProd);
//
//                InputFilter filter = new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        for (int i = start; i < end; ++i) {
//                            if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.]*").matcher(String.valueOf(source.charAt(i))).matches()) {
//                                return "";
//                            }
//                        }
//                        return null;
//                    }
//                };
//
//                vHolder.txtCantidad.setFilters(new InputFilter[]{new InputFilter() {
//                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        int indexPoint = dest.toString().indexOf(decimalFormatSymbols.getDecimalSeparator());
//                        if (indexPoint == -1)
//                            return source;
//
//                        int decimals = dend - (indexPoint+1);
//                        return decimals < 2 ? source : "";
//                    }
//                }
//                });
//
//                vHolder.txtCantidad.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(5)});
//
//                final CustomAdapterInventario.ViewHolder finalv = vHolder;
//
//                convertView.setTag(vHolder);
//            } else {
//                vHolder = (CustomAdapterInventario.ViewHolder) convertView.getTag();
//            }
//
//            if (values.size() > 0) {
//                vHolder.lblSku.setText(values.get(position).getSku_code());
//
//                final CustomAdapterInventario.ViewHolder finalv = vHolder;
//
//                vHolder.btnFechaProd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //finalv.txtCantidad.setText("PRUEBA");
//                        final Calendar calendar = Calendar.getInstance();
//                        int anio = calendar.get(Calendar.YEAR);
//                        int mes = calendar.get(Calendar.MONTH);
//                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog from_dateListener = new DatePickerDialog(FlooringActivity.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date date = null;
//                                try {
//                                    date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                String outDate = dateFormat.format(date);
//                                finalv.txtFecha.setText(outDate);
//                                dataAdapter.notifyDataSetChanged();
//                            }
//                        },anio,mes,dia);
//                        from_dateListener.show();
//                    }
//                });
//
//                vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        fecha_producto = finalv.txtFecha.getText().toString().trim();
//                        cantidad_producto = finalv.txtCantidad.getText().toString();
//                        sku = finalv.lblSku.getText().toString();
//                        if (((CheckBox)v).isChecked()) {
//                            if (!fecha_producto.equals("") && fecha_producto != null &&
//                                    !cantidad_producto.equals("") && cantidad_producto != null) {
//                                insertData(sku, fecha_producto, cantidad_producto);
//                            } else {
//                                Toast.makeText(getContext(), "No ingresaste la fecha o la cantidad", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                });
//            }
//            //Devolver al ListView la fila creada
//            return convertView;
//        }
//    }
//
//    public class CustomAdapterInventario extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        private final String TAG = CustomAdapterInventario.class.getSimpleName();
//        private static final int TYPE_HEADER = 0;
//        private static final int TYPE_ITEM = 1;
//        private Context context;
//        public ArrayList<BasePortafolioProductos> values;
//
//        public CustomAdapterInventario(Context context, ArrayList<BasePortafolioProductos> values) {
//            this.context = context;
//            this.values = values;
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_flooring, parent, false);
//                return new ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");
//        }
//
//        @Override
//        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//            BasePortafolioProductos mObject = values.get(position);
//            if (holder instanceof HeaderViewHolder) {
//                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
//                ((HeaderViewHolder) holder).headerTitle.setText("SKU");
//                ((HeaderViewHolder) holder).headerTitle1.setText("STOCK ACTUAL");
//                ((HeaderViewHolder) holder).headerTitle2.setText("SUGERIDO");
////                ((HeaderViewHolder) holder).headerTitle3.setText("CAUSAL SUGERIDO");
//                ((HeaderViewHolder) holder).headerTitle4.setText("OBSERVACION");
//                ((HeaderViewHolder) holder).headerTitle5.setText("CADUCIDAD");
//            }else if (holder instanceof ItemViewHolder) {
//                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());
//            }
//
//            sesion = handler.getSkuRelevadosInventario(codigo_pdv,categoria,subcategoria,brand);
//            for(int i = 0; i < sesion.size(); i++) {
//                if (mObject.getSku().equals(sesion.get(i).getSku_code())) {
//                    ((FlooringFragment.CustomAdapterInventario.ItemViewHolder) holder).txt_sku.setTextColor(ContextCompat.getColor(getContext(), R.color.rojo_alicorp));
//                     skuRevelados++;
//                    ((FlooringFragment.CustomAdapterInventario.ItemViewHolder) holder).txt_sku.setTypeface(null, Typeface.BOLD);
//                }
//            }
//
//        }
//
//        private BasePortafolioProductos getItem(int position) {
//            return values.get(position);
//        }
//        @Override
//        public int getItemCount() {
//            return values.size();
//        }
//        @Override
//        public int getItemViewType(int position) {
//            if (isPositionHeader(position))
//                return TYPE_HEADER;
//            return TYPE_ITEM;
//        }
//        private boolean isPositionHeader(int position) {
//            return position == 0;
//        }
//
////        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
////            listProductos = filteredList;
////            Log.i("LIST",listProductos.status()+"");
////            FlooringActivity.this.dataAdapter.notifyDataSetChanged();
////        }
//
//        public class HeaderViewHolder extends RecyclerView.ViewHolder{
//
//            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5;
//
//            public HeaderViewHolder(View itemView) {
//                super(itemView);
//                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
//                headerTitle1 = (TextView)itemView.findViewById(R.id.lblStock);
//                headerTitle2 = (TextView)itemView.findViewById(R.id.lblSugerido);
////                headerTitle3 = (TextView)itemView.findViewById(R.id.lblCausal);
//                headerTitle4 = (TextView)itemView.findViewById(R.id.lblDetalle);
//                headerTitle5 = (TextView)itemView.findViewById(R.id.lblCaducidad);
//            }
//
//        }
//
//        public class ItemViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView txt_sku;
//            public CheckBox chkGuardar;
//            public EditText txtStock;
//            public EditText txtSugerido;
//            //            public Spinner spCausal;
//            public EditText txtObservacion;
//            public TextView txtFecha;
//            public ImageButton btnFechaProd;
//
//            public ItemViewHolder(View itemView) {
//                super(itemView);
//
//                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
//                txtStock = (EditText) itemView.findViewById(R.id.txtCantidad);
//                txtSugerido = (EditText) itemView.findViewById(R.id.txtSugerido);
////                spCausal = (Spinner) itemView.findViewById(R.id.spCausal);
//                txtObservacion = (EditText) itemView.findViewById(R.id.txtObservacion);
//                txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
//                btnFechaProd = (ImageButton) itemView.findViewById(R.id.btnFechaProd);
//                chkGuardar = (CheckBox) itemView.findViewById(R.id.checkGuardar);
//
////                spCausal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                    @Override
////                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                        if (!spCausal.getSelectedItem().toString().trim().equalsIgnoreCase("OTROS")) {
////                            txtOtros.setEnabled(false);
////                            txtOtros.setText("-");
////                        }else{
////                            txtOtros.setEnabled(true);
////                            txtOtros.setText("");
////                        }
////                    }
////
////                    @Override
////                    public void onNothingSelected(AdapterView<?> parent) {}
////                });
//
//                btnFechaProd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //finalv.txtCantidad.setText("PRUEBA");
//                        final Calendar calendar = Calendar.getInstance();
//                        int anio = calendar.get(Calendar.YEAR);
//                        int mes = calendar.get(Calendar.MONTH);
//                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date date = null;
//                                try {
//                                    date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                String outDate = dateFormat.format(date);
//                                txtFecha.setText(outDate);
//                                dataAdapter.notifyDataSetChanged();
//                            }
//                        },anio,mes,dia);
////                        from_dateListener.getDatePicker().setSpinnersShown(true);
////                        from_dateListener.getDatePicker().setCalendarViewShown(false);
//                        from_dateListener.show();
//                    }
//                });
//
//                chkGuardar.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        if (((CheckBox)v).isChecked()) {
//                            String skuSelected = txt_sku.getText().toString();
//                            String stock = txtStock.getText().toString().trim();
//                            String sugerido = txtSugerido.getText().toString().trim();
////                            String causal = spCausal.getSelectedItem().toString().trim();
//                            String observacion = txtObservacion.getText().toString().trim();
//                            String fecha_caducidad = txtFecha.getText().toString().trim();
//
//                            if (!stock.trim().isEmpty() &&
//                                    !sugerido.trim().isEmpty() &&
//                                    !observacion.trim().isEmpty() &&
//                                    !fecha_caducidad.trim().isEmpty()) {
//                                insertData();
//                            } else {
//                                Toast.makeText(getContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
//                                chkGuardar.setChecked(false);
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }


//    public class CustomAdapterInventario2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        private final String TAG = CanjesFragment.CustomAdapterCanjes.class.getSimpleName();
//        private static final int TYPE_HEADER = 0;
//        private static final int TYPE_ITEM = 1;
//        private Context context;
//        public ArrayList<BasePortafolioProductos> values;
//
//        public CustomAdapterInventario2(Context context, ArrayList<BasePortafolioProductos> values) {
//            this.context = context;
//            this.values = values;
//        }
//
//        public CustomAdapterInventario2() {}
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            if (viewType == TYPE_HEADER) {
////                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
////                return new HeaderViewHolder(layoutView);
////            } else if (viewType == TYPE_ITEM) {
//            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_flooring2, parent, false);
//            return new ItemViewHolder(layoutView);
////            }
////            throw new RuntimeException("No match for " + viewType + ".");
//        }
//
//        @Override
//        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//            BasePortafolioProductos mObject = values.get(position);
//
//
//            if (holder instanceof ItemViewHolder) {
//                ((ItemViewHolder) holder).lblSku.setText(mObject.getSku());
//            }
//
//            sesion = handler.getSkuRelevadosInventario(codigo_pdv,categoria,subcategoria,brand);
//            for(int i = 0; i < sesion.size(); i++) {
//                if (mObject.getSku().equals(sesion.get(i).getSku_code())) {
//
//                    if (canal.equals("MAYORISTA")){
//
//                        if (sesion.get(i).getSemana().equals(semanaDelMesActual)){
//                            ((ItemViewHolder) holder).lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
//                            ((ItemViewHolder) holder).lblSku.setTypeface(null, Typeface.BOLD);
//                        }
//                    }
//                }
//            }
//
//
//        }
//
//        private BasePortafolioProductos getItem(int position) {
//            return values.get(position);
//        }
//        @Override
//        public int getItemCount() {
//            return values.size();
//        }
//        @Override
//        public int getItemViewType(int position) {
//            if (isPositionHeader(position))
//                return TYPE_HEADER;
//            return TYPE_ITEM;
//        }
//        private boolean isPositionHeader(int position) {
//            return position == 0;
//        }
//
////        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
////            listProductos = filteredList;
////            Log.i("LIST",listProductos.status()+"");
////            FlooringActivity.this.dataAdapter.notifyDataSetChanged();
////        }
//
//        public class HeaderViewHolder extends RecyclerView.ViewHolder{
//
//            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5;
//
//            public HeaderViewHolder(View itemView) {
//                super(itemView);
//                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
//                headerTitle1 = (TextView)itemView.findViewById(R.id.lblStock);
//                headerTitle2 = (TextView)itemView.findViewById(R.id.lblSugerido);
////                headerTitle3 = (TextView)itemView.findViewById(R.id.lblCausal);
//                headerTitle4 = (TextView)itemView.findViewById(R.id.lblDetalle);
//                headerTitle5 = (TextView)itemView.findViewById(R.id.lblCaducidad);
//            }
//
//        }
//
//        public class ItemViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView lblSku;
//            public Spinner spSemana;
//            public EditText txtStockActual;
//            public EditText txtSugerido;
//            public CheckBox chkGuardar;
//            public EditText txtObservacion;
//            public TextView txtFechaCaducidad;
//            public LinearLayout lySemana;
//            public TextView txtSemana;
//            public ImageButton btnFechaProd;
//            public CheckBox checkGuardar;
//
//            public ItemViewHolder(View itemView) {
//                super(itemView);
//
//                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
//                txtStockActual = (EditText) itemView.findViewById(R.id.txtStockActual);
//                txtSugerido = (EditText) itemView.findViewById(R.id.txtSugerido);
//                txtSemana = (TextView) itemView.findViewById(R.id.txtSemana);                    lySemana = (LinearLayout) itemView.findViewById(R.id.lySemana);
//                lySemana = (LinearLayout) itemView.findViewById(R.id.lySemana);
//
//
//                //  spSemana = (Spinner) itemView.findViewById(R.id.spSemana);
//                txtObservacion = (EditText) itemView.findViewById(R.id.txtObservaciones);
//            //    txtFechaCaducidad = (TextView) itemView.findViewById(R.id.txtFechaCaducidad);
//            //    btnFechaProd = (ImageButton) itemView.findViewById(R.id.btnFechaProd);
//                checkGuardar = (CheckBox) itemView.findViewById(R.id.checkGuardar);
//
//                if (canal.equals("MAYORISTA")){
//                    txtSemana.setText(semanaDelMesActual);
//                }else if (canal.equals("AUTOSERVICIO")){
//                    txtSemana.setText("");
//                }
//
//
///*
//                btnFechaProd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //finalv.txtCantidad.setText("PRUEBA");
//                        final Calendar calendar = Calendar.getInstance();
//                        int anio = calendar.get(Calendar.YEAR);
//                        int mes = calendar.get(Calendar.MONTH);
//                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date date = null;
//                                try {
//                                    date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                String outDate = dateFormat.format(date);
//                                txtFechaCaducidad.setText(outDate);
//                                dataAdapter.notifyDataSetChanged();
//                            }
//                        },anio,mes,dia);
////                        from_dateListener.getDatePicker().setSpinnersShown(true);
////                        from_dateListener.getDatePicker().setCalendarViewShown(false);
//                        from_dateListener.show();
//                    }
//                });
//
//
//*/
//
//                if (canal.equals("MAYORISTA")){
//                    lySemana.setVisibility(View.VISIBLE);
//                }else if (canal.equals("AUTOSERVICIO")){
//                    lySemana.setVisibility(View.GONE);
//                }
//                checkGuardar.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        if (((CheckBox)v).isChecked()) {
//                            skuSelected = lblSku.getText().toString();
//                            String stock = txtStockActual.getText().toString().trim();
//                            String sugerido = txtSugerido.getText().toString().trim();
//                          //  String semana = spSemana.getSelectedItem().toString().trim();
//                            String semana = txtSemana.getText().toString().trim();
//                            String observacion = txtObservacion.getText().toString().trim();
//                        //    String fecha_caducidad = txtFechaCaducidad.getText().toString().trim();
//                            String fecha_caducidad = "";
//
//                            alertSku();
//
//                            if (norev.length() == 2){
//                                norev ="";
//                            }
//
//
//                            if (esFormularioValido(skuSelected,semana,stock,sugerido,observacion,fecha_caducidad)) {
//
//                                if (!norev.isEmpty()){
//
//
//                                    alertDialogSkU(norev,skuSelected,semana,stock,sugerido,observacion,fecha_caducidad);
//
//                                 //   insertData(skuSelected,semana,stock,sugerido,observacion,fecha_caducidad);
//                                        if (seInsertoSKU){
//                                            limpiarDatos();
//                                        }
//
//                                }else{
//
//                                    /*
//                                    if(insertData(skuSelected,semana,stock,sugerido,observacion,fecha_caducidad)){
//                                      limpiarDatos();
//                                    }*/
//
//
//                                }
//
//
//
//                            } else {
//                                // Toast.makeText(getContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
//                                checkGuardar.setChecked(false);
//                            }
//                        }
//                    }
//
//                    private void limpiarDatos() {
//                        txtStockActual.setText("");
//                        txtSugerido.setText("");
//                    //    spSemana.setSelection(0);
//                        txtObservacion.setText("");
//                     //   txtFechaCaducidad.setText("");
//                        lblSku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
//                    //   checkGuardar.setChecked(false);
//
//                    }
//                });
//
//
//            }
//        }
//
//
//
//    }


    public class CustomAdapterInventario3 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterInventario3.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<Base_Inventario> values;

        public CustomAdapterInventario3(Context context, ArrayList<Base_Inventario> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View layoutView =  inflater.inflate(R.layout.list_row_flooring3, parent, false);


            return new ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");






        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Base_Inventario mObject = values.get(position);
            if (holder instanceof HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
//                ((HeaderViewHolder) holder).headerTitle.setText("SKU");
//                ((HeaderViewHolder) holder).headerTitle1.setText("STOCK ACTUAL");
//                ((HeaderViewHolder) holder).headerTitle2.setText("SUGERIDO");
////                ((HeaderViewHolder) holder).headerTitle3.setText("CAUSAL SUGERIDO");
//                ((HeaderViewHolder) holder).headerTitle4.setText("OBSERVACION");
//                ((HeaderViewHolder) holder).headerTitle5.setText("CADUCIDAD");
//                ((HeaderViewHolder) holder).headerTitle6.setText("CANTIDAD");
//                ((HeaderViewHolder) holder).headerTitle7.setText("ENTREGA");
//                ((HeaderViewHolder) holder).headerTitle8.setText("SEMANA");
            }else if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());

                if (mObject.getTieneDatos()){
                    ((ItemViewHolder) holder).llCampos.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).txtStock.setText(mObject.getStock_percha());
                    ((ItemViewHolder) holder).txtStock_Bodega.setText(mObject.getStock_bodega());
                    ((ItemViewHolder) holder).spSug.setSelection(mObject.getSeleccionSugerido());
                    ((ItemViewHolder) holder).txtSugerido.setText(mObject.getCant_sugerido());
                    ((ItemViewHolder) holder).spEntrega.setSelection(mObject.getSeleccionTipoSugerido());
                }


                ((ItemViewHolder) holder).txtStock.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                            guardarDatos(mObject, (ItemViewHolder) holder);
                    //        if (!editable.toString().isEmpty()){
                    //            mObject.setStock_percha(editable.toString());
                    //            mObject.setTieneDatos(true);
                    //       } else {
                   //             mObject.setStock_percha("");
                   //             mObject.setTieneDatos(false);
                    //        }

                    }

                });
                ((ItemViewHolder) holder).txtStock_Bodega.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                            guardarDatos(mObject, (ItemViewHolder) holder);
                    }
                });
                ((ItemViewHolder) holder).txtSugerido.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                            guardarDatos(mObject, (ItemViewHolder) holder);
                    }
                });

                sesion2 = handler.getListGuardadoInventario(codigo_pdv);
                Log.i("SESSION U", String.valueOf(sesion2));
                Log.i("SESSION U",""+sesion2.size());
                for(int i = 0; i < sesion2.size(); i++) {
 //                   Toast.makeText(getContext(),""+sesion2.size(),Toast.LENGTH_LONG).show();
                    if (mObject.getSku().equals(sesion2.get(i).getSku())) {
                        ((ItemViewHolder) holder).txt_sku.setTextColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
//                    ((FlooringFragment.CustomAdapterInventario.ItemViewHolder) holder).lblEstado.setText("REALIZADO");

                        //     ((FlooringFragment.CustomAdapterInventario3.ItemViewHolder) holder).txt_sku.setTypeface(null, Typeface.BOLD);
                    }
                }

            }





        }

        private void guardarDatos(Base_Inventario mObject,ItemViewHolder holder) {
            String stock_percha = ((ItemViewHolder) holder).txtStock.getText().toString();
            String stock_bodega = ((ItemViewHolder) holder).txtStock_Bodega.getText().toString();
            int id_sugerido = ((ItemViewHolder) holder).spSug.getSelectedItemPosition();
            String cant_sugerido = ((ItemViewHolder) holder).txtSugerido.getText().toString();
            int id_tipo_sugerido = ((ItemViewHolder) holder).spEntrega.getSelectedItemPosition();

            Log.i("cant sugerido",""+cant_sugerido);


            if (!stock_percha.isEmpty() || !stock_bodega.isEmpty() || id_sugerido != 0){
                mObject.setStock_percha(stock_percha);
                mObject.setStock_bodega(stock_bodega);
                mObject.setSeleccionSugerido(id_sugerido);
                mObject.setCant_sugerido(cant_sugerido);
                mObject.setSeleccionTipoSugerido(id_tipo_sugerido);
                mObject.setTieneDatos(true);
            } else {
                mObject.setStock_percha("");
                mObject.setStock_bodega("");
                mObject.setSeleccionSugerido(0);
                mObject.setCant_sugerido("");
                mObject.setSeleccionTipoSugerido(0);
                mObject.setTieneDatos(false);
            }

        }


        private Base_Inventario getItem(int position) {
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

            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5, headerTitle6, headerTitle7, headerTitle8;

            public HeaderViewHolder(View itemView) {
                super(itemView);
//                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
//                headerTitle1 = (TextView)itemView.findViewById(R.id.lblStock);
//                headerTitle2 = (TextView)itemView.findViewById(R.id.lblSugerido);
////                headerTitle3 = (TextView)itemView.findViewById(R.id.lblCausal);
//                headerTitle4 = (TextView)itemView.findViewById(R.id.lblDetalle);
//                headerTitle5 = (TextView)itemView.findViewById(R.id.lblCaducidad);
//                headerTitle6 = (TextView)itemView.findViewById(R.id.lblCantidadSugerido);
//                headerTitle7 = (TextView)itemView.findViewById(R.id.lblEntrega);
//                headerTitle8 = (TextView)itemView.findViewById(R.id.lblSemana);
            }

        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView txt_sku;
            //            public  TextView lblEstado;
            public Button chkGuardar;
            public EditText txtStock;
            public EditText txtStock_Bodega;
            public EditText txtSugerido;
            public Spinner spEsSugerido;
            public Spinner spSemana;
//            public Spinner spTipoUnidades;
            public Spinner spEntrega;
            public Spinner spSug;
            //            public Spinner spCausal;
            public Spinner txtObservacion;
            public TextView txtFecha;
            public TextView txtFechaCaducidad;
            public ImageButton btnFechaProd;
            public ImageButton btnFechaCaducidad;
            public LinearLayout llCampos;
            public ImageButton btnMostrar;

            public ItemViewHolder(View itemView) {
                super(itemView);

                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
//                lblEstado = (TextView) itemView.findViewById(R.id.lblEstado);
                txtStock = (EditText) itemView.findViewById(R.id.txt_stock);
                txtStock_Bodega = (EditText) itemView.findViewById(R.id.txt_stock_bodega);
//                spSemana = (Spinner) itemView.findViewById(R.id.spSemana);
                //spEsSugerido = (Spinner) itemView.findViewById(R.id.sp_sugerido);
                spSug = (Spinner) itemView.findViewById(R.id.sp_sug);
//                spTipoUnidades = (Spinner) itemView.findViewById(R.id.sp_tipo);
                txtSugerido = (EditText) itemView.findViewById(R.id.txt_cantidad_sugerida);
                spEntrega = (Spinner) itemView.findViewById(R.id.sp_entrega);
//                spCausal = (Spinner) itemView.findViewById(R.id.spCausal);
                txtObservacion = (Spinner) itemView.findViewById(R.id.sp_tiposug);
                txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
                txtFechaCaducidad = (TextView) itemView.findViewById(R.id.txtFechaCaducidad);
//                btnFechaProd = (ImageButton) itemView.findViewById(R.id.btnFechaProd);
                chkGuardar = (Button) itemView.findViewById(R.id.btnGuardar);
                btnMostrar = (ImageButton) itemView.findViewById(R.id.btnMostrar);
                btnFechaCaducidad = (ImageButton) itemView.findViewById(R.id.btnFechaCaducidad);
                llCampos = (LinearLayout) itemView.findViewById(R.id.llCamposInv);
                txtSugerido.setHint("Cantidad");

                if(brand.equalsIgnoreCase("SURF")){
                    //spEsSugerido.setEnabled(false);
                }

                btnMostrar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.rosa_lucky));

                btnMostrar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (llCampos.getVisibility() == View.VISIBLE){


                            if (!txtStock.getText().toString().isEmpty()
                                    || !txtStock_Bodega.getText().toString().isEmpty()
                                    || !spSug.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
                                Toast.makeText(getContext(),"No se puede ocultar hasta poner GUARDAR.",Toast.LENGTH_LONG).show();
                            }  else {
                                llCampos.setVisibility(View.GONE);
                                btnMostrar.setImageDrawable(getResources().getDrawable(R.drawable.icon_visibility_off));
                            }


                            // Limpiar Datos
//                            txtStock.setText("");
//                            txtStock_Bodega.setText("");
//                            spSug.setSelection(0);
//                            txtSugerido.setText("");
//                            spEntrega.setSelection(0);
                        } else {
                            llCampos.setVisibility(View.VISIBLE);
                            btnMostrar.setImageDrawable(getResources().getDrawable(R.drawable.icon_visibility));
                        }
                    }
                });

                txtFechaCaducidad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarRangoFechaCaducidad();
                    }
                });

                // Después de inicializar txtFechaCaducidad, agrega:
                txtFechaCaducidad.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        String fecha = s.toString();
                        if (fecha.length() == 10) {
                            /*if (!validarFechaEnRango(fecha)) {
                                Toast.makeText(getContext(), "Fecha fuera de rango ", Toast.LENGTH_LONG).show();*/
//                                txtFechaCaducidad.setText(""); // Limpiar el campo
                            /*}*/
                        }
                    }
                });


                btnFechaCaducidad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarRangoFechaCaducidad();
                    }
                });



                txtStock.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        String stock_und = editable.toString();
                        int stock = 0;


                        if (!stock_und.equals("")) {

                            stock = Integer.parseInt(stock_und);

                            if(stock <= 0){
                                String stock_bodega = txtStock_Bodega.getText().toString();
                                if(!stock_bodega.equals("")){
                                    if(Integer.parseInt(stock_bodega) > 0){
                                        Toast.makeText(getActivity(), "ABASTECER LA PERCHA Y TOMAR INVENTARIO", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            if(stock < 20){
                                String stock_bodega = txtStock_Bodega.getText().toString();
                                if(!stock_bodega.equals("")){
                                    if(Integer.parseInt(stock_bodega) == 0){
                                        spSug.setSelection(1);
                                        spSug.setEnabled(false);
                                    }else{
                                        //spSug.setSelection(0);
                                        spSug.setEnabled(true);
                                    }
                                }
                            }else{
                                //spSug.setSelection(0);
                                spSug.setEnabled(true);
                            }

                                /*if (stock == 0) {
                                    if(!brand.equalsIgnoreCase("SURF")) {
                                        spEsSugerido.setSelection(1);
                                    }
                                }*/
                        }

                    }


                });

                txtStock_Bodega.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String stock_und = s.toString();
                        int stock_bodega = 0;
                        if(!stock_und.equals("")){
                            stock_bodega = Integer.parseInt(stock_und);
                            if(stock_bodega > 0){
                                String stock = txtStock.getText().toString();
                                if(!stock.equals("")){
                                    if(Integer.parseInt(stock) <= 0){
                                        Toast.makeText(getActivity(), "ABASTECER LA PERCHA Y TOMAR INVENTARIO", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            /*
                            if(stock_bodega == 0){


                                String stock = txtStock.getText().toString();
                                if(!stock.equals("")){
                                    if(Integer.parseInt(stock) < 20){
                                        spSug.setSelection(1);
                                        spSug.setEnabled(false);
                                    }else{
                                        //spSug.setSelection(0);
                                        spSug.setEnabled(true);
                                    }
                                }
                            }else{
                                //spSug.setSelection(0);
                                spSug.setEnabled(true);
                            }*/

                        }
                    }
                });


//                spCausal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (!spCausal.getSelectedItem().toString().trim().equalsIgnoreCase("OTROS")) {
//                            txtOtros.setEnabled(false);
//                            txtOtros.setText("-");
//                        }else{
//                            txtOtros.setEnabled(true);
//                            txtOtros.setText("");
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {}
//                });

                txtObservacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tipo = txtObservacion.getSelectedItem().toString();
                        txtSugerido.setHint("Cantidad "+ tipo);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spSug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spSug.getSelectedItem().toString().trim().equalsIgnoreCase("Si")) {
                            spEntrega.setSelection(0);
                            spEntrega.setEnabled(true);
                            txtSugerido.setText("");
                            txtSugerido.setEnabled(true);
                        }else if (spSug.getSelectedItem().toString().trim().equalsIgnoreCase("No")){
                            spEntrega.setSelection(0);
                            spEntrega.setEnabled(false);
                            txtSugerido.setText("");
                            txtSugerido.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                /*spEsSugerido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spEsSugerido.getSelectedItem().toString().trim().equalsIgnoreCase("SI")) {
                            spEntrega.setEnabled(true);
                            txtSugerido.setEnabled(true);
                            spTipoUnidades.setEnabled(true);
                            List<String> operadores = handler.getCausales();
                            spTipoUnidades.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
                            Log.i("CAUSALES", String.valueOf(operadores.size()));
                          //  List<String> operadores = handler.getCausales();
                          //  spTipoUnidades.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));
                          //  txtSugerido.setText("");
                            txtObservacion.setEnabled(true);
                        }else{
                            ArrayList<String> operadores = new ArrayList<>();
                            operadores.add("Seleccione");
                            spTipoUnidades.setAdapter(new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, operadores));

                            spEntrega.setEnabled(false);
                            txtSugerido.setEnabled(false);
                            spTipoUnidades.setEnabled(false);
                            txtObservacion.setEnabled(false);
                            spTipoUnidades.setSelection(0);
                            spEntrega.setSelection(0);
                            txtObservacion.setSelection(0);
                            txtSugerido.setText("");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });*/

//                btnFechaProd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //finalv.txtCantidad.setText("PRUEBA");
//                        final Calendar calendar = Calendar.getInstance();
//                        int anio = calendar.get(Calendar.YEAR);
//                        int mes = calendar.get(Calendar.MONTH);
//                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//                        DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date date = null;
//                                try {
//                                    date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                String outDate = dateFormat.format(date);
//                                txtFecha.setText(outDate);
//                                dataAdapter.notifyDataSetChanged();
//                            }
//                        },anio,mes,dia);
////                        from_dateListener.getDatePicker().setSpinnersShown(true);
////                        from_dateListener.getDatePicker().setCalendarViewShown(false);
//                        from_dateListener.show();
//                    }
//                });

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override

                    //variables que corresponden a los elementos visuales del listview
                    public void onClick(View v) {
                        View view;
                        TextView skup = null;
                        EditText stockp = null;
                        EditText stockBod = null;
                        Spinner semanap = null;
                        Spinner sugeridop = null;
                        Spinner tipop = null;
                        EditText cantidadp = null;
                        Spinner entregap = null;
                        Spinner observacionp = null;
                        Spinner sug = null;
                        TextView fechaCaducidad = null;
//                        TextView caducidadp = null;

                        //variables booleanas para controlar si hay campos vacios en los datos ingresados
                       // todas comeinzan con false porque comienzan vacias
                        Boolean stockVacio = false;
                        Boolean stockEsCero = false;
                        Boolean stockEsCeroSugeridoNo = false;
                        Boolean semanaVacio = false;
                        Boolean sugeridoVacio = false;
                        Boolean tipoObsVacio = false;
                        Boolean tipoSugeridoVacio = false;
                        Boolean cantidadVacio = false;
                        Boolean cantSugeridoEsCero = false;
                        Boolean entregaVacio = false;
                        Boolean stockBodVacio = false;
                        Boolean fechaCaducidadVacio = false;
                        Boolean tipo_sb = false;
                        Boolean suge = false;
                        Boolean tipo_sug = false;
                        Boolean cant_sug = false;
                        Boolean val1 = false;
                        int stock_supera = -1;
                        Boolean sug_cero = false;
//                        Boolean caducidadVacio = false;

                        // inicializacion de alos arreglos para guardar valores, es decir se obtiene la cantidad de filas visibles en la lista que devuelve los spinners
                        // es decir que crean estos arreglos para guardar los valores que se extraen de cada fila, cada elemento
                        int tiempo_vacio = 0;

                        int listLength = listview.getChildCount();
                        valueOfTextViewSku = new String[listLength];
                        valueOfEditText = new String[listLength];
                        valueOfSpinner = new String[listLength];
                        valueOfSpinner2 = new String[listLength];
                        valueOfSpinnerT = new String[listLength];
                        valueOfEditText2 = new String[listLength];
                        valueOfSpinner3 = new String[listLength];
                        valueOfEditText3 = new String[listLength];
                        valueOfEditText4 = new String[listLength];
                        valueOfEditText5 = new String[listLength];
                        valueOfSpinner4 = new String[listLength];
//                        valueOfTextView = new String[listLength];


                        //aqui se recorre todas las filas visibles***  para obtener los datos
                        for (int i = 0; i < listLength; i++) {
                            view = listview.getChildAt(i);

                            skup = (TextView) view.findViewById(R.id.lblSku);
                            stockp = (EditText) view.findViewById(R.id.txt_stock);
                            stockBod = (EditText) view.findViewById(R.id.txt_stock_bodega);
//                            semanap = (Spinner) view.findViewById(R.id.spSemana);
                            sugeridop = (Spinner) view.findViewById(R.id.sp_sugerido);
                            tipop = (Spinner) view.findViewById(R.id.sp_tipo);
                            cantidadp = (EditText) view.findViewById(R.id.txt_cantidad_sugerida);
                            entregap = (Spinner) view.findViewById(R.id.sp_entrega);
                            observacionp = (Spinner) view.findViewById(R.id.sp_tiposug);
                            sug = (Spinner) view.findViewById(R.id.sp_sug);
//                            caducidadp = (TextView) view.findViewById(R.id.txtFecha);
                            fechaCaducidad = (TextView) view.findViewById(R.id.txtFechaCaducidad);


                            // aqui se obtienen los valores ya ACTUALES de cada campo
                            String valor_sku = skup.getText().toString();
//                            String valor_semana = semanap.getSelectedItem().toString()
                            String valor_stock = stockp.getText().toString();
                            String valor_stock_bodega = stockBod.getText().toString();
                            String valor_sugerido = sugeridop.getSelectedItem().toString();
//                            String valor_sugerido = "";
                            String valor_tipo = tipop.getSelectedItem().toString();
//                            String valor_tipo = "";
                            String valor_cantidad = cantidadp.getText().toString();
                            String valor_entrega = entregap.getSelectedItem().toString();
                            String valor_observacion = observacionp.getSelectedItem().toString();
//                            String valor_observacion =  "";
                            String valor_sug = sug.getSelectedItem().toString();
//                            String valor_caducidad = caducidadp.getText().toString();
                            String valor_fecha_caducidad = fechaCaducidad.getText().toString();


                            /// una vez obtenidos los valores, se los guardan en los arreglos para
                            valueOfTextViewSku[i] = valor_sku;
                            valueOfEditText[i] = valor_stock;
//                            valueOfSpinner[i] = valor_semana;
                            valueOfSpinner2[i] = valor_sugerido;
                            valueOfSpinnerT[i] = valor_tipo;
                            valueOfEditText2[i] = valor_cantidad;
                            valueOfSpinner3[i] = valor_entrega;
                            valueOfEditText3[i] = valor_observacion;
                            valueOfEditText4[i] = valor_stock_bodega;
                            valueOfSpinner4[i] = valor_sug;
                            valueOfEditText5[i] = valor_fecha_caducidad;
//                            valueOfTextView[i] = valor_caducidad;

                            if(!valor_stock.equalsIgnoreCase("")){
                                //VALIDACIONES NUEVAS
                                //stock percha
                                if (valueOfEditText[i].equalsIgnoreCase("")){
                                    stockVacio = true;
                                }

                                //stock bodega
                                if (valueOfEditText4[i].equalsIgnoreCase("")){
                                    stockBodVacio = true;
                                }

                                if (valueOfEditText5[i].equalsIgnoreCase("")){
                                    fechaCaducidadVacio = true;
                                }

                                //tipo stock bodega
                                if (valueOfSpinnerT[i].equalsIgnoreCase("Seleccione")){
                                    tipo_sb = true;
                                }

                                //spinner sugerido si/no
                                if (valueOfSpinner4[i].equalsIgnoreCase("Seleccione")){
                                    suge = true;
                                }


                                //VAL 1: SI STOCK BODEGA ES MAYOR A CERO ENTONCES STOCK PERCHA DEBE SER MAYOR A CERO
                                //Log.i("caasq",""+valueOfEditText4[i]);
                                //Log.i("caasq",""+valueOfEditText[i]);
                                if(!valueOfEditText4[i].equalsIgnoreCase("") && !valueOfEditText[i].equalsIgnoreCase("")){
                                    if(Integer.parseInt(valueOfEditText4[i]) > 0 && Integer.parseInt(valueOfEditText[i]) <= 0){
                                        val1 = true;
                                    }
                                }

                                if(!valueOfEditText5[i].equalsIgnoreCase("") && !valueOfEditText[i].equalsIgnoreCase("")){
                                    if(Integer.parseInt(valueOfEditText[i]) < 0){
                                        val1 = true;
                                    }
                                }

                                Log.i("caasq",""+val1);



                                //validacion SI/NO sugeridos
                                if(valueOfSpinner4[i].equalsIgnoreCase("Si")){
                                    //cant. sugeridos
                                    if (valueOfEditText2[i].equalsIgnoreCase("")){
                                        cant_sug = true;
                                    }

                                    //tipo sugeridos
                                    if (valueOfSpinner3[i].equalsIgnoreCase("Seleccione")){
                                        tipo_sug = true;
                                    }

                                    //cant. sugeridos
                                    if (!valueOfEditText2[i].equalsIgnoreCase("")){
                                        if (Integer.parseInt(valueOfEditText2[i]) < 0){
                                            sug_cero = true;
                                        }
                                    }
                                }

                                //stock percha validacion
                                int n = Integer.parseInt(valueOfEditText[i]);
                                if (n < 10){
                                    stock_supera = n;
                                }

                            }




                            //VALIDACIONES DISPONIBLES SOLO PARA LOS ITEMS LLENOS
                            Log.i("cass",""+valor_stock+" " + valor_sugerido);
                            if(!valor_stock.equalsIgnoreCase("") || !valor_sugerido.equalsIgnoreCase("Seleccione")) { /// || !valor_caducidad.equalsIgnoreCase("")

                                if (valueOfEditText[i].equalsIgnoreCase("")){
                                    stockVacio = true;
                                }

                                /*
                                if(!stockVacio){
                                    if (Integer.parseInt(valueOfEditText[i]) == 0){
                                        stockEsCero = true;
                                    }
                                }*/

//                                if (valueOfSpinner[i].equalsIgnoreCase("Semana")) {
//                                    semanaVacio = true;
//                                }
                                if(!brand.equalsIgnoreCase("SURF")){
                                    if (valueOfSpinner2[i].equalsIgnoreCase("Seleccione")) {
                                        sugeridoVacio = true;
                                    }
                                }else{
                                    valueOfEditText3[i] = "NA";

                                }

                                if (valor_sugerido.equalsIgnoreCase("SI")) {

                                    if (valueOfEditText2[i].equalsIgnoreCase("")) {
                                        cantidadVacio = true;
                                    }

                                    if(!cantidadVacio){
                                        if (Integer.parseInt(valueOfEditText2[i]) == 0){
                                            cantSugeridoEsCero = true;
                                        }
                                    }

                                    if (valueOfSpinner3[i].equalsIgnoreCase("Seleccione")) {
                                        entregaVacio = true;
                                    }
                                    if (valueOfSpinnerT[i].equalsIgnoreCase("Seleccione")) {
                                        tipoObsVacio = true;
                                    }

                                    if(valueOfEditText3[i].equalsIgnoreCase("Seleccione")){
                                        tipoSugeridoVacio = true;
                                    }

                                } else if (valor_sugerido.equalsIgnoreCase("NO")) {


                                    if(!stockVacio) {
                                        if (Integer.parseInt(valueOfEditText[i]) == 0) {
                                            stockEsCeroSugeridoNo = true;
                                        }
                                    }

                                    valueOfEditText2[i] = "NA";
                                    valueOfEditText3[i] = "NA";
                                    valueOfSpinner3[i] = "NA";
                                    valueOfSpinnerT[i] = "NA";

                                }

//                                if (!valor_stock.equalsIgnoreCase("0") && !brand.equalsIgnoreCase("SURF")) {
//                                    if (valueOfTextView[i].equalsIgnoreCase("")) {
//                                        caducidadVacio = true;
//                                    }
//                                } else {
//                                    valueOfTextView[i] = "NA";
//                                }
                            }

                        }

//                        String razones = spRazones.getSelectedItem().toString();




                        Log.i("SPINNERS", categoria +" - "+ subcategoria +" - "+ brand);
                        int contador_llenos = 0;
                        if (!categoria.equalsIgnoreCase("SELECCIONE") && !categoria.equalsIgnoreCase("")
                              //
                                //  && !subcategoria.equalsIgnoreCase("SELECCIONE") && !subcategoria.equalsIgnoreCase("")
                                && !brand.equalsIgnoreCase("SELECCIONE") && !brand.equalsIgnoreCase("")) {
                            Log.i("CANTIDAD ESTA VACIO", String.valueOf(cantidadVacio));
                            String producto = skup.getText().toString();
                            if(esFormularioValido2(producto,stockVacio, stockBodVacio, fechaCaducidadVacio, tipo_sb, suge, cant_sug, tipo_sug, val1, sug_cero)){

                                if(stock_supera != -1){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setIcon(R.drawable.icon_warning);
                                    builder.setTitle("Aviso");
                                    builder.setMessage("¡LA INFORMACIÓN REPORTADA EN ESTE PRODUCTO COMO 'STOCK " + stock_supera + "' INDICA QUE SE ENCUENTRA EN QUIEBRE!");
                                    TextView finalSkup = skup;
                                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            insertData2();
                                            finalSkup.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_revelado));
                                        }
                                    });

                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });

                                    AlertDialog ad = builder.create();
                                    ad.show();
                                }else{
                                    insertData2();

                                }
                            }
                            //if(esFormularioValido(stockVacio,stockEsCero,stockEsCeroSugeridoNo,tipoSugeridoVacio,cantidadVacio,cantSugeridoEsCero,entregaVacio,sugeridoVacio,tipoObsVacio)){  //!caducidadVacio &&
                            //    insertData();
                            //} else {

//                                if (stockVacio) {
//                                    Toast.makeText(getContext(), "Llene todos los campos stock", Toast.LENGTH_SHORT).show();
//                                }
//                               if (semanaVacio) {
//                                   Toast.makeText(getContext(), "Llene todos los campos semana", Toast.LENGTH_SHORT).show();
//
//                                if(stockEsCero){
//                                    Toast.makeText(getContext(), "El stock no deber ser cero", Toast.LENGTH_SHORT).show();
//                                }
//
//                                if (cantidadVacio) {
//                                    Toast.makeText(getContext(), "Llene todos los campos cantidad sugerido", Toast.LENGTH_SHORT).show();
//                                }
//                                if (sugeridoVacio) {
//                                    Toast.makeText(getContext(), "Seleccione sugerido", Toast.LENGTH_SHORT).show();
//                                }
//                                if (tipoVacio) {
//                                    Toast.makeText(getContext(), "Seleccione tipo", Toast.LENGTH_SHORT).show();
//                                }
//                                if (entregaVacio) {
//                                    Toast.makeText(getContext(), "Llene todos los tiempos de entrega", Toast.LENGTH_SHORT).show();
//                                }
//                               if (caducidadVacio) {
//                                    Toast.makeText(getContext(), "Llene todos los campos caducidad", Toast.LENGTH_SHORT).show();
//                               }
                            //}

                        }else{
                            Toast.makeText(getContext(),"Ingrese una categoria, subcategoria y marca.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    private boolean esFormularioValido2(String sku,boolean stockVacio, boolean stockBodVacio, boolean fechaCaducidadVacio, boolean tipo_sb, boolean suge,
                                                        boolean cant_sug, boolean tipo_sug, boolean val1, Boolean sug_cero){

                        Log.i("valorescas",""+stockVacio);
                        Log.i("valorescas",""+stockBodVacio);
                        Log.i("valorescas",""+fechaCaducidadVacio);
                        Log.i("valorescas",""+tipo_sb);
                        Log.i("valorescas",""+suge);
                        Log.i("valorescas",""+cant_sug);
                        Log.i("valorescas",""+tipo_sug);
                        Log.i("valorescas",""+val1);

                        if (fechaCaducidadVacio) {
                            Toast.makeText(getContext(), "Llene el campo Fecha Caducidad", Toast.LENGTH_SHORT).show();
                            return false;
                        }

//                        if (!fechaCaducidadVacio && !validarFechaEnRango(valueOfEditText5[0])) {
//                            Toast.makeText(getContext(), "Verificar formato o rango de fecha", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }

                        if (stockVacio) {
                            Toast.makeText(getContext(), "Llene el campo: Stock Percha", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        /*
                        if (tipo_sb) {
                            Toast.makeText(getContext(), "Llene el campo: Tipo Stock Bodega", Toast.LENGTH_SHORT).show();
                            return false;
                        }*/

                        if (suge) {
                            Toast.makeText(getContext(), "Llene el campo: Sugerido", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if (stockBodVacio) {
                            Toast.makeText(getContext(), "Llene el campo Stock Bodega", Toast.LENGTH_SHORT).show();
                            return false;
                        }


                        if(val1){
                            Toast.makeText(getContext(), "ABASTECER LA PERCHA Y TOMAR INVENTARIO", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if(cant_sug){
                            Toast.makeText(getContext(), "Llene el campo Cant. Sugeridos", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if(sug_cero){
                            Toast.makeText(getContext(), "Cant. Sugeridos debe ser mayor a cero ", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if(tipo_sug){
                            Toast.makeText(getContext(), "Llene el campo Tipo Sugeridos ", Toast.LENGTH_SHORT).show();
                            return false;
                        }




                        return true;
                    }


                    private boolean esFormularioValido(Boolean stockVacio, Boolean stockEsCero,Boolean stockEsCeroSugeridoNo,Boolean tipoSugeridoVacio, Boolean cantidadVacio,Boolean cantSugeridoEsCero, Boolean entregaVacio, Boolean sugeridoVacio, Boolean tipoObsVacio) {

                        if (stockVacio) {
                            Toast.makeText(getContext(), "Llene el campo Stock Percha", Toast.LENGTH_SHORT).show();
                            return false;
                        }
/*
                        if(stockEsCero){
                            Toast.makeText(getContext(), "El stock no debe ser cero", Toast.LENGTH_SHORT).show();
                            return false;
                        }
*/

                        if(stockEsCeroSugeridoNo){
                            Toast.makeText(getContext(), "El stock no debe ser cero", Toast.LENGTH_SHORT).show();
                            return false;
                        }


                        /*if (sugeridoVacio) {
                            Toast.makeText(getContext(), "Seleccione sugerido", Toast.LENGTH_SHORT).show();
                            return false;
                        }*/

                        if (tipoObsVacio) {
                            Toast.makeText(getContext(), "Seleccione tipo de observacion", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if(tipoSugeridoVacio){
                            Toast.makeText(getContext(), "Seleccione el tipo de sugerido", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if (cantidadVacio) {
                            Toast.makeText(getContext(), "Llene todos los campos cantidad sugerido", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if (cantSugeridoEsCero) {
                            Toast.makeText(getContext(), "La cantidad de sugerido no debe ser cero", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if (entregaVacio) {
                            Toast.makeText(getContext(), "Llene todos los tiempos de entrega", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        return true;
                    }


                });

//                chkGuardar.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        if (((CheckBox)v).isChecked()) {
//                            String skuSelected = txt_sku.getText().toString();
//                            String stock = txtStock.getText().toString().trim();
//                            String semana = spSemana.getSelectedItem().toString().trim();
//                            String esSugerido = spEsSugerido.getSelectedItem().toString().trim();
//                            String sugerido = txtSugerido.getText().toString().trim();  //cantidad sugerido
//                            String entrega = spEntrega.getSelectedItem().toString().trim();
////                            String causal = spCausal.getSelectedItem().toString().trim();
//                            String observacion = txtObservacion.getText().toString().trim();
//                            String fecha_caducidad = txtFecha.getText().toString().trim();
//
//                            if (Integer.parseInt(stock) <= 10000){
//                                if (esSugerido.equalsIgnoreCase("SI")){
//                                    if (!stock.trim().isEmpty() &&
//                                            !semana.trim().isEmpty() &&
//                                            !sugerido.trim().isEmpty() &&
//                                            !semana.equals("Seleccione") &&
//                                            !entrega.equals("Seleccione Tiempo") &&
//                                            !observacion.trim().isEmpty() &&
//                                            !fecha_caducidad.trim().isEmpty()) {
//                                        insertData(skuSelected, semana, esSugerido, fecha_caducidad, sugerido, entrega, "", observacion, stock);
//                                    } else {
//                                        Toast.makeText(getContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
//                                        chkGuardar.setChecked(false);
//                                    }
//                                } else if (esSugerido.equalsIgnoreCase("NO")) {
//                                    if (!stock.trim().isEmpty() &&
//                                            !semana.trim().isEmpty() &&
//                                            !observacion.trim().isEmpty() &&
//                                            !fecha_caducidad.trim().isEmpty()) {
//                                        insertData(skuSelected, semana, esSugerido, fecha_caducidad, "-", "-", "", observacion, stock);
//                                    } else {
//                                        Toast.makeText(getContext(),"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
//                                        chkGuardar.setChecked(false);
//                                    }
//                                } else {
//                                    Toast.makeText(getContext(),"Debe seleccionar sugerido",Toast.LENGTH_LONG).show();
//                                    chkGuardar.setChecked(false);
//                                }
//                            } else {
//                                Toast.makeText(getContext(),"El stock no puede ser mayor a 10 000",Toast.LENGTH_LONG).show();
//                                chkGuardar.setChecked(false);
//                            }
//
//
//                        }
//                    }
//                });
            }

//            private boolean validarFechaEnRango(String fecha) {
//                if (fecha == null || fecha.isEmpty()) {
//                    return false;
//                }
//
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                sdf.setLenient(false);
//
//                try {
//                    Date date = sdf.parse(fecha);
//
//                    // ===== INICIO DEL MES ANTERIOR (mismo que en mostrarRangoFechaCaducidad) =====
//                    Calendar inicioMesAnterior = Calendar.getInstance();
////                    inicioMesAnterior.add(Calendar.MONTH, -3); // Retrocede 1 mes
////                    inicioMesAnterior.set(Calendar.DAY_OF_MONTH, 1);
////                    inicioMesAnterior.set(Calendar.HOUR_OF_DAY, 0);
////                    inicioMesAnterior.set(Calendar.MINUTE, 0);
////                    inicioMesAnterior.set(Calendar.SECOND, 0);
////                    inicioMesAnterior.set(Calendar.MILLISECOND, 0);
//
//                    Calendar inicioAnio = Calendar.getInstance();
//                    inicioAnio.add(Calendar.YEAR, inicioAnio.getActualMinimum(Calendar.JANUARY)); // Retrocede 1 mes
//                    inicioAnio.set(Calendar.DAY_OF_YEAR, 1);
//                    inicioAnio.set(Calendar.HOUR_OF_DAY, 0);
//                    inicioAnio.set(Calendar.MINUTE, 0);
//                    inicioAnio.set(Calendar.SECOND, 0);
//                    inicioAnio.set(Calendar.MILLISECOND, 0);
//
//                    Calendar finAnio = Calendar.getInstance();
//                    finAnio.set(Calendar.DAY_OF_YEAR, finAnio.getActualMaximum(Calendar.DAY_OF_YEAR)); // Último día del año
//                    finAnio.set(Calendar.HOUR_OF_DAY, 23);
//                    finAnio.set(Calendar.MINUTE, 59);
//                    finAnio.set(Calendar.SECOND, 59);
//                    finAnio.set(Calendar.MILLISECOND, 999);
//
//                    Calendar fechaValidar = Calendar.getInstance();
//                    fechaValidar.setTime(date);
//
//                    return !fechaValidar.before(inicioAnio) && !fechaValidar.after(finAnio);
//                } catch (ParseException e) {
//                    return false;
//                }
//            }

//            private void mostrarRangoFechaCaducidad() {
//                Calendar calendar = Calendar.getInstance();
//
//                Calendar inicioAnio = Calendar.getInstance();
//                inicioAnio.set(Calendar.DAY_OF_YEAR, 1);
//                inicioAnio.set(Calendar.HOUR_OF_DAY, 0);
//                inicioAnio.set(Calendar.MINUTE, 0);
//                inicioAnio.set(Calendar.SECOND, 0);
//                inicioAnio.set(Calendar.MILLISECOND, 0);
//
//                Calendar finAnio = Calendar.getInstance();
//                finAnio.set(Calendar.DAY_OF_YEAR, finAnio.getActualMaximum(Calendar.DAY_OF_YEAR));
//                finAnio.set(Calendar.HOUR_OF_DAY, 23);
//                finAnio.set(Calendar.MINUTE, 59);
//                finAnio.set(Calendar.SECOND, 59);
//                finAnio.set(Calendar.MILLISECOND, 999);
//
//                int año = calendar.get(Calendar.YEAR);
//                int mes = calendar.get(Calendar.MONTH);
//                int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        getContext(),
//                        (view, year, month, dayOfMonth) -> {
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                            Calendar selectedDate = Calendar.getInstance();
//                            selectedDate.set(year, month, dayOfMonth);
//                            txtFechaCaducidad.setText(sdf.format(selectedDate.getTime()));
//                        },
//                        año, mes, dia
//                );
//
//                datePickerDialog.getDatePicker().setMinDate(inicioAnio.getTimeInMillis());
//                datePickerDialog.getDatePicker().setMaxDate(finAnio.getTimeInMillis());
//                datePickerDialog.show();
//            }


        private void mostrarRangoFechaCaducidad() {
            Calendar calendar = Calendar.getInstance();

            int año = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        txtFechaCaducidad.setText(sdf.format(selectedDate.getTime()));
                    },
                    año, mes, dia
            );


            datePickerDialog.show();
        }

        }
    }



    public void obtenerSemanaDelMesActual(){

        if (canal.equals("MAYORISTA")){

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat dateform = new SimpleDateFormat("dd/MM/yyy");
            dateform.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fecha_actual = dateform.format(currentLocalTime);

            try {


                Date date = dateform.parse(fecha_actual);
                cal.setTime(date);

                switch (cal.get(Calendar.WEEK_OF_MONTH)) {
                    case 1:
                        semanaDelMesActual = "Semana 1";
                        semanaActualM = 1;
                        break;
                    case 2:
                        semanaDelMesActual = "Semana 2";
                        semanaActualM = 2;
                        break;
                    case 3:
                        semanaDelMesActual = "Semana 3";
                        semanaActualM = 3;
                        break;
                    case 4:
                        semanaDelMesActual = "Semana 4";
                        semanaActualM = 4;
                        break;
                    case 5:
                        semanaDelMesActual = "Semana 5";
                        semanaActualM = 5;
                        break;
                    case 6:
                        semanaDelMesActual = "Semana 5";
                        semanaActualM = 5;
                        break;
                }

            //    Toast.makeText(getContext(), ""+semanaDelMesActual, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean esFormularioValido(String producto,String semana,String stock,String sugerido,String observacion,String fechaCaducidad) {
/*
        if (semana.equalsIgnoreCase("Seleccione Semana")) {
            Toast.makeText(getContext(),"Debe escoger la semana del producto: "+producto,Toast.LENGTH_SHORT).show();
            return false;
        }
*/

        if (stock.equalsIgnoreCase("") || stock.isEmpty()) {
            Toast.makeText(getContext(),"Debe agregar el stock, En caso de no disponer, Agregar 0 del producto: "+producto,Toast.LENGTH_LONG).show();
            return false;
        }

        if (stock.equalsIgnoreCase("0")) {
            if (sugerido.equalsIgnoreCase("0")){
                Toast.makeText(getContext(),"Debe agregar sugerido: "+producto,Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (sugerido.equalsIgnoreCase("") || sugerido.isEmpty()) {
            Toast.makeText(getContext(),"Debe agregar el stock sugerido del producto: "+producto,Toast.LENGTH_SHORT).show();
            return false;
        }


        if (stock.equalsIgnoreCase("00") || stock.equalsIgnoreCase("000") || stock.equalsIgnoreCase("0000") || stock.equalsIgnoreCase("00000") || stock.equalsIgnoreCase("000000")){
            Toast.makeText(getContext(),"Debe agregar el stock, En caso de no disponer, Agregar 0 del producto: "+producto,Toast.LENGTH_LONG).show();
            return false;
        }


/*
        if (sugerido.equalsIgnoreCase("00") || sugerido.equalsIgnoreCase("000") || sugerido.equalsIgnoreCase("0000") || sugerido.equalsIgnoreCase("00000") || sugerido.equalsIgnoreCase("000000")){
            Toast.makeText(getContext(),"sugerido no puede ser cero "+producto,Toast.LENGTH_SHORT).show();
            return false;
        }
  */
/*
        if (observacion.equalsIgnoreCase("") || observacion.isEmpty()) {
            Toast.makeText(getContext(),"Debe agregar una observacion",Toast.LENGTH_SHORT).show();
            return false;
        }
*/
        /*
        if (fechaCaducidad.equalsIgnoreCase("") || fechaCaducidad.isEmpty()) {
            Toast.makeText(getContext(),"Debe agregar una la fecha de caudcidad del producto: "+producto,Toast.LENGTH_SHORT).show();
            return false;
        }*/


        return true;
    }

//
//    public void guardar() {
//
//        View view = null;
//        TextView tv_producto;
//        Spinner sp_semana;
//        EditText txt_stock_actual;
//        EditText txt_sugerido;
//        TextView tv_fecha_caducidad;
//        EditText txt_observaciones;
//        CheckBox check;
//        flooringList = new ArrayList<>();
//
//        int values = 0, validos = 0, checked = 0;
//
//        int listLength = listview.getChildCount();
//
//        for (int i = 0; i < listLength; i++) {
//            view = listview.getChildAt(i);
//            tv_producto = (TextView) view.findViewById(R.id.lblSku);
//         //   sp_semana = (Spinner) view.findViewById(R.id.spSemana);
//            txt_stock_actual = (EditText) view.findViewById(R.id.txtStockActual);
//            txt_sugerido = (EditText) view.findViewById(R.id.txtSugerido);
//            txt_observaciones = (EditText) view.findViewById(R.id.txtObservaciones);
//            tv_fecha_caducidad = (TextView) view.findViewById(R.id.txtFechaCaducidad);
//            check = (CheckBox) view.findViewById(R.id.checkGuardar);
//
//            if (check.isChecked()) {
//                checked++;
//
//
//                String producto = tv_producto.getText().toString();
//            //    String semana = sp_semana.getSelectedItem().toString().trim();
//                String stockActual = txt_stock_actual.getText().toString();
//                String sugerido = txt_sugerido.getText().toString();
//                String observaciones = txt_observaciones.getText().toString();
//                String fecha_caducidad = tv_fecha_caducidad.getText().toString();
//
//
//                if (esFormularioValido(producto,semana,stockActual,sugerido,observaciones,fecha_caducidad)) {
//                    Flooring flooring = new Flooring();
//                    flooring.setSku_code(producto);
//                    flooring.setSemana(semana);
//                    flooring.setStockActual(stockActual);
//                    flooring.setSugerido(sugerido);
//                    flooring.setObservaciones(observaciones);
//                    flooring.setFecha_caducidad(fecha_caducidad);
//                    flooringList.add(flooring);
//
//                    validos++;
//                } else {
//                    check.setChecked(false);
//                }
//            }
//            values++;
//        }
//
//        Log.i("VALUES", values + "");
//        Log.i("CHECKED", checked + "");
//        Log.i("VALIDOS", validos + "");
//
//        if (checked > 0 && validos == checked) {
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
//            builder.setIcon(android.R.drawable.ic_dialog_alert);
//            builder.setTitle("Confirmación");
//            builder.setMessage("¿Desea guardar?");
//            View finalView = view;
//            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    insertData();
//                    limpiarFormulario(finalView);
//                }
//
//                private void limpiarFormulario(View view) {
//
//
//                   Spinner spSemana = view.findViewById(R.id.spSemana);
//                   EditText txtStock = view.findViewById(R.id.txtStockActual);
//                   EditText txtSugerido = view.findViewById(R.id.txtSugerido);
//                   EditText txtObservaciones = view.findViewById(R.id.txtObservaciones);
//                   TextView tvFechaCaducidad = view.findViewById(R.id.txtFechaCaducidad);
//                   CheckBox check = view.findViewById(R.id.checkGuardar);
//
//                   spSemana.setSelection(0);
//                   txtStock.setText("");
//                   txtSugerido.setText("");
//                   txtObservaciones.setText("");
//                   tvFechaCaducidad.setText("");
//                   check.setChecked(false);
//
//
//                }
///*
//                private void limpiarFormulario() {
//                    sp_semana.setSelection(0);
//                }
//
// */
//            });
//
//            builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {}
//            });
//
//            android.app.AlertDialog ad = builder.create();
//            ad.show();
//        } else {
//            Toast.makeText(getContext(),"Debe seleccionar algo para guardar", Toast.LENGTH_LONG).show();
//        }
//    }
//

}
