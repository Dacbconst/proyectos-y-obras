package com.luckyecuador.app.PintucoAPP.ui.malla;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

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

public class MallaCodificadosFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<BasePortafolioProductos> type_name_copy = new ArrayList<BasePortafolioProductos>();
    TextView txtfechav;
    ImageButton btnFecha;
    private TextView empty;
    private ListView listview;
    //ListView listDescripcion;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    private Spinner spPresentacion;
    private Spinner spMarca;
//    private Spinner spVariante;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    private EditText txtDescripcion;
    private Button btnPropia;
    private Button btnCompetencia;

    private String categoria, subcategoria, segmento1, segmento2, brand, variante, tamano, cantidad, codigo, descripcion, format, presentacion;
    private String id_pdv,user,codigo_pdv,punto_venta,fecha,hora,canal,subcanal;

    private String tipo = "MARCA_PROPIA";
    private final String fabricante = "ALICORP";

    DatabaseHelper handler;

    ArrayList<BasePortafolioProductos> listProductos;
   // List<String> listDescripcionL;

    List<String> filterProducts;
    List<String> filterTargets;

    String venta, souv;
    private SharedPreferences sharedPref;

    CustomAdapterMallaCodificados dataAdapter;
    ListViewAdapter dataAdapters;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private final String CARPETA_RAIZ = "PintucoApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Inventario";

    String path;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_malla_codificados, container, false);

        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //setActionBar();
        LoadData();

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spPresentacion = (Spinner) rootView.findViewById(R.id.spPresentacion);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);
        btnPropia  = (Button) rootView.findViewById(R.id.btnPropia);
        btnCompetencia  = (Button) rootView.findViewById(R.id.btnCompetencia);

        //startService(new Intent(getContext(), LocationService.class));

        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        btnPropia.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);

        //listDescripcion = (ListView)findViewById(R.id.lvDescripcion);
        filtrarCategoria(tipo, fabricante);

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

//        ListView listView = (ListView) findViewById(R.id.lvSKUCode);
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        List<String> operadores = handler.getCategoriaMallaCodificados(tipo, fabricante, canal, subcanal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoria(String categoria, String tipo, String manufacturer){
        List<String> operadores = handler.getSubcategoriaMallaCodificados(categoria, tipo, manufacturer, canal, subcanal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    /*public void filtrarMarca(String subcategoria, String subcategoria){
        List<String> operadores = handler.getSegmento1(subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoria(String subcategoria, String subcategoria, String subcategoria){
        List<String> operadores = handler.getSegmento2(subcategoria,subcategoria,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }*/

    public void filtrarPresentacion(String categoria, String subcategoria, String manufacturer){
        List<String> operadores = handler.getPresentacionFlooring(categoria,subcategoria,manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentacion.setAdapter(dataAdapter);
        spPresentacion.setOnItemSelectedListener(this);
    }

//    public void filtrarBrand(String categoria, String subcategoria, String presentacion, String manufacturer){
//        List<String> operadores = handler.getBrandFlooring(categoria,subcategoria,presentacion,manufacturer);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spMarca.setAdapter(dataAdapter);
//        spMarca.setOnItemSelectedListener(this);
//    }

    public void filtrarBrand(String categoria, String subcategoria, String tipo, String manufacturer){
        List<String> operadores = handler.getBrand2MallaCodificados(categoria, subcategoria, tipo, manufacturer, canal, subcanal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    /*public void filtrarVariante(String categoria, String subcategoria, String marca, String manufacturer){
        List<String> operadores = handler.getVarianteFlooring(categoria,subcategoria,marca,manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVariante.setAdapter(dataAdapter);
        spVariante.setOnItemSelectedListener(this);
    }*/

    /*public void filtrartamano(String subcategoria, String subcategoria, String subcategoria, String segmento, String contenido){
        List<String> operadores = handler.getTamano(subcategoria,subcategoria,subcategoria,segmento,contenido);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTamano.setAdapter(dataAdapter);
        spTamano.setOnItemSelectedListener(this);
    }

    public void filtrarcantidad(String subcategoria, String subcategoria, String subcategoria, String segmento, String contenido, String tamano){
        List<String> operadores = handler.getCantidad(subcategoria,subcategoria,subcategoria,segmento,contenido, tamano);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(dataAdapter);
        spCantidad.setOnItemSelectedListener(this);
    }*/


    public void LoadData(){
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
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
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
        if(adapterView== spCategoria){
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSubcategoria(categoria, tipo, fabricante);
                listview.setAdapter(null);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if(adapterView== spSubcategoria){
            try{
                subcategoria = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(categoria, subcategoria, tipo, fabricante);
                listview.setAdapter(null);
//                filtrarPresentacion(categoria, subcategoria, manufacturer);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
//        if(adapterView== spPresentacion){
//            try{
//                presentacion = adapterView.getItemAtPosition(i).toString();
//                filtrarBrand(categoria, subcategoria, presentacion, manufacturer);
//                //filtrarMarca(subcategoria,subcategoria);
//            }catch (Exception e){
//                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
        /*if(adapterView==spSubcategoria){
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                filtrarSubcategoria(subcategoria,subcategoria,subcategoria);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if(adapterView==spSegmento){
            try{
                segmento=adapterView.getItemAtPosition(i).toString();
                filtrarMarca(subcategoria,subcategoria,subcategoria,segmento);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
        if(adapterView== spMarca){
            try {
                brand = adapterView.getItemAtPosition(i).toString();
                showListView(categoria, subcategoria, brand, tipo, fabricante);
//                showListView(categoria, subcategoria, brand, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,segmento,contenido);
            } catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if(adapterView== spVariante){
            try{
                variante = adapterView.getItemAtPosition(i).toString();
                showListView(categoria, subcategoria, brand, variante, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,segmento,contenido);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
        /*if(adapterView==spTamano){
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(subcategoria,subcategoria,subcategoria,segmento,contenido,tamano);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if(adapterView==spCantidad){
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(subcategoria,subcategoria,subcategoria,segmento,contenido,tamano,cantidad);
               // showListDescripcion(channel,subcategoria,subcategoria,segmento,contenido,tamano,cantidad);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
    }

//    public void showListView(String categoria, String subcategoria, String brand, String manufacturer){
//        listProductos = handler.filtrarListProductos3Flooring(categoria,subcategoria,brand,manufacturer);
//        dataAdapter = new CustomAdapterPresenciaMinima(this,listProductos);
//        if(!dataAdapter.isEmpty()){
//            listview.setAdapter(dataAdapter);
//
//            for(int i=0 ; i<dataAdapter.getCount() ; i++){
//                Object obj = dataAdapter.getItem(i);
//                Log.i("DATA_ADAPTER",obj+"");
//            }
//
//            txtSKUCode.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    //dataAdapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    ArrayList<BasePortafolioProductos> type_name_filter = new ArrayList<BasePortafolioProductos>();
//
//                    String text = s.toString();
//
//                    for (int i = 0; i < listProductos.size(); i++) {
//                        if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
//                            BasePortafolioProductos p = new BasePortafolioProductos();
//                            p.setSku(listProductos.get(i).getSku());
//                            type_name_filter.add(p);
//                        }
//                    }
//
//                    type_name_copy = type_name_filter;
//                    listUpdate(type_name_copy);
//                }
//            });
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void listUpdate(ArrayList<BasePortafolioProductos> data) {
//        if(!data.isEmpty()){
//            listview.setVisibility(View.VISIBLE);
//            empty.setVisibility(View.INVISIBLE);
//            listview.setAdapter(new CustomAdapterPresenciaMinima(this, data));
//        }else{
//            listview.setVisibility(View.GONE);
//            empty.setVisibility(View.VISIBLE);
//        }
//    }

//    public void showListView(String categoria, String subcategoria, String brand, String variante, String manufacturer){
//        listProductos = handler.filtrarListProductos3Flooring(categoria,subcategoria,brand,variante,manufacturer);
//        Log.i("List Original",listProductos.size()+"");
//        dataAdapter = new CustomAdapterMallaCodificados(this, listProductos);
//        if (dataAdapter.getItemCount() != 0) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FlooringActivity.this);
//            listview.setLayoutManager(linearLayoutManager);
//            listview.setHasFixedSize(true);
//            CustomAdapterMallaCodificados customAdapter = new CustomAdapterMallaCodificados(this.getContext(),listProductos);
//            listview.setAdapter(customAdapter);
//        } else {
//            empty.setVisibility(View.VISIBLE);
//        }
//    }

    public void showListView(String categoria, String subcategoria, String brand, String tipo, String manufacturer){
        listProductos = handler.filtrarListProductosMallaCodificados(categoria, subcategoria, brand, tipo, manufacturer, canal, subcanal);
        dataAdapter = new CustomAdapterMallaCodificados(getContext(), listProductos);
        if(!dataAdapter.isEmpty()){
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);
            //listview.setOnItemClickListener(this);

            txtSKUCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(txtSKUCode.getText().toString());
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
            listview.setAdapter(new CustomAdapterMallaCodificados(getContext(), data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnPropia) {
            tipo = "MARCA_PROPIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
        if (v == btnCompetencia) {
            tipo = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);
        }
    }

/*
    public void showListDescripcion(String marca, String subcategoria,String subcategoria,String segmento, String contenido, String tamano, String cantidad){
        listDescripcionL = handler.filtrarListDescripcion(marca,subcategoria,subcategoria,segmento,contenido,tamano,cantidad);
        dataAdapter = new CustomAdapterPresenciaMinima(this,listDescripcionL);
        if(!dataAdapter.isEmpty()){
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

                vHolder.check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                     //  unidad= finalv.txtunidad.getText().toString();
                        venta = finalv.txtventa.getText().toString();
                        souv = finalv.txtsouv.getText().toString();

                        if(((CheckBox)v).isChecked()) {
                            //if ( !txtfechav.getText().toString().equals("") ) {
                            if((!venta.equals("") || venta!=null) && (!souv.equals("") || souv!=null)){
                                String sku =finalv.lblSku.getText().toString();
                                insertData(sku,venta,souv);
                            }else{
                                Toast.makeText(getContext(), "Verifica el Ingreso en Inventario", Toast.LENGTH_LONG).show();
                            }

                       // }else
                       // {
                       //     Toast.makeText(getContext(), "Verifica el Ingreso en Fecha", Toast.LENGTH_LONG).show();
                        //}
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }

    }*/

    public class CustomAdapterMallaCodificados extends ArrayAdapter<BasePortafolioProductos> implements Filterable {

        public ArrayList<BasePortafolioProductos> values;
        public Context context;

        public CustomAdapterMallaCodificados(Context context, ArrayList<BasePortafolioProductos> values) {
            super(context, 0, values);
            this.values = values;
        }

        public class ViewHolder{
            public TextView txt_sku;
            public TextView lblEstado;
            public Spinner spCodifica;
            public EditText txtObservaciones;
            public EditText txtOtroSku;
            public Button chkGuardar;
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
                convertView = inflater.inflate(R.layout.list_row_malla_codificados, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.txt_sku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.lblEstado = (TextView) convertView.findViewById(R.id.lblEstado);
                vHolder.spCodifica = (Spinner) convertView.findViewById(R.id.spCodifica);
                vHolder.txtObservaciones = (EditText) convertView.findViewById(R.id.txtObservaciones);
                vHolder.txtOtroSku = (EditText) convertView.findViewById(R.id.txtOtroSku);
                vHolder.chkGuardar = (Button) convertView.findViewById(R.id.checkGuardar);

                convertView.setTag(vHolder);
                ViewHolder finalVHolder = vHolder;
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            try {
                if (values.size() > 0) {
                    String sku = values.get(position).getSku();
                    vHolder.txt_sku.setText(sku);

                    String estado = handler.getEstadoMallaCodificados(codigo_pdv, user, sku);
                    vHolder.lblEstado.setText(estado);
                    if (estado.equalsIgnoreCase("REALIZADO")) {
                        vHolder.lblEstado.setBackgroundColor(getResources().getColor(R.color.color_revelado));
                        vHolder.txt_sku.setTextColor(getResources().getColor(R.color.color_revelado));
                    }

                    if (sku.equalsIgnoreCase("OTROS")) {
                        vHolder.txtOtroSku.setVisibility(View.VISIBLE);
                        vHolder.txt_sku.setVisibility(View.GONE);
                    }

                    final ViewHolder finalv = vHolder;

                    View finalConvertView = convertView;
                    vHolder.chkGuardar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String producto = finalv.txt_sku.getText().toString();
                            String producto_otros = finalv.txtOtroSku.getText().toString();
                            String observacion = finalv.txtObservaciones.getText().toString();

                            if (producto.equalsIgnoreCase("OTROS")) {
                                producto = producto_otros;
                            }

                            if (esFormularioValido(producto, finalv.spCodifica, observacion)) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                builder.setTitle("Confirmación");
                                builder.setMessage("¿Desea guardar la información?");
                                String finalProducto = producto;
                                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        int selectedIdConteoTotal = finalv.rgCodifica.getCheckedRadioButtonId();
//                                        RadioButton radioButtonConteoTotal = (RadioButton) finalConvertView.findViewById(selectedIdConteoTotal);
//                                        String codifica = radioButtonConteoTotal.getText().toString();
                                        String codifica = finalv.spCodifica.getSelectedItem().toString();

                                        insertData(finalProducto, codifica, observacion);

                                        String estado = "REALIZADO";
                                        finalv.lblEstado.setText(estado);
                                        finalv.lblEstado.setBackgroundColor(getResources().getColor(R.color.color_revelado));
                                        finalv.txt_sku.setTextColor(getResources().getColor(R.color.color_revelado));

                                        limpiarFormulario(finalv.spCodifica, finalv.txtObservaciones, finalv.txtOtroSku);
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

        public boolean esFormularioValido(String producto, Spinner spCodifica, String observacion) {
            if (producto.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar el nombre del producto", Toast.LENGTH_LONG).show();
                return false;
            }
            if(spCodifica.getSelectedItem().toString().equalsIgnoreCase("SELECCIONE")){
                Toast.makeText(getContext(),"Debe seleccionar si codifica o no el producto: " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
//            if (observacion.trim().isEmpty()) {
//                Toast.makeText(getContext(), "Debe ingresar una observación en el producto: " + producto, Toast.LENGTH_LONG).show();
//                return false;
//            }

            return true;
        }

        public void limpiarFormulario(Spinner spCodifica, EditText txtObservacion, EditText txtOtroSku) {
            spCodifica.setSelection(0);
            txtObservacion.setText("");
            txtOtroSku.setText("");
        }
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                        scaleImage(imageView, bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
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
            alertDialog1 = new androidx.appcompat.app.AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(getContext());
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
            String authorities=getContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,imagen);
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
            if (grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", getContext().getPackageName(),null);
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
        androidx.appcompat.app.AlertDialog.Builder dialogo=new androidx.appcompat.app.AlertDialog.Builder(getContext());
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
    
    public void insertData(String producto, String codifica, String observacion) {
        //Almacenar Datos
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
        String brand = spMarca.getSelectedItem().toString();

        String sector = "";
        String segmento = "";
        String fabricante = "";
        BasePortafolioProductos bpp = handler.getProducto(producto);
        if (bpp != null) {
            sector = bpp.getSector();
            segmento = bpp.getSegmento();
            fabricante = bpp.getFabricante();
        }

        ContentValues values = new ContentValues();

        values.put(ContractInsertMallaCodificados.Columnas.PHARMA_ID, id_pdv);
        values.put(ContractInsertMallaCodificados.Columnas.CODIGO, codigo_pdv);
        values.put(ContractInsertMallaCodificados.Columnas.USUARIO, user);
        values.put(ContractInsertMallaCodificados.Columnas.SUPERVISOR, punto_venta);
        values.put(ContractInsertMallaCodificados.Columnas.FECHA, fechaser);
        values.put(ContractInsertMallaCodificados.Columnas.HORA, horaser);
        values.put(ContractInsertMallaCodificados.Columnas.SECTOR, sector);
        values.put(ContractInsertMallaCodificados.Columnas.CATEGORIA, categoria);
        values.put(ContractInsertMallaCodificados.Columnas.SUBCATEGORIA, subcategoria);
        values.put(ContractInsertMallaCodificados.Columnas.SEGMENTO1, segmento);
        values.put(ContractInsertMallaCodificados.Columnas.BRAND, brand);
        values.put(ContractInsertMallaCodificados.Columnas.SKU_CODE, producto);
        values.put(ContractInsertMallaCodificados.Columnas.CODIFICA, codifica);
        values.put(ContractInsertMallaCodificados.Columnas.OBSERVACION, observacion);
        values.put(ContractInsertMallaCodificados.Columnas.MANUFACTURER, fabricante);
        values.put(ContractInsertMallaCodificados.Columnas.POS_NAME, punto_venta);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContext().getContentResolver().insert(ContractInsertMallaCodificados.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertMallaCodificados, null);
            Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}