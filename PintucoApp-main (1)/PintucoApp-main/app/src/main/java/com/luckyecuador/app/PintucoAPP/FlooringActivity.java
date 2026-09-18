package com.luckyecuador.app.PintucoAPP;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.Base_Inventario;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FlooringActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    ArrayList<Base_Inventario> sesion = new ArrayList<Base_Inventario>();
    TextView txtfechav;
    ImageButton btnFecha;
    TextView empty;
    RecyclerView listview;
    //ListView listDescripcion;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    //private Spinner spPresentacion;
    private Spinner spMarca;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    private EditText txtDescripcion;

    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, format, presentacion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora;
    private String fechaventas, producto, fecha_producto, cantidad_producto, poferta, sku, cuotas, vcuotas;

    private final String manufacturer = "Alicorp";

    DatabaseHelper handler;

    ArrayList<BasePortafolioProductos> listProductos;
   // List<String> listDescripcionL;

    List<String> filterProducts;
    List<String> filterTargets;

    public String venta, souv, causal, otros, canal, subcanal;
    private SharedPreferences sharedPref;

    CustomAdapterInventario dataAdapter;
    ListViewAdapter dataAdapters;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flooring);
        setToolbar();

        //setActionBar();
        LoadData();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        spCategoria = (Spinner)findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        //spPresentacion = (Spinner)findViewById(R.id.spPresentacion);
        spMarca = (Spinner)findViewById(R.id.spMarca);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText)findViewById(R.id.txtDescripcionSKU);

        //startService(new Intent(getApplicationContext(), MyService.class));

        /*listview = (RecyclerView)findViewById(R.id.lvSKUCode);
        listview.setHasFixedSize(true);*/

        layout_skuName = (LinearLayout)findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout)findViewById(R.id.layout_skuDescripcion);

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        //listDescripcion = (ListView)findViewById(R.id.lvDescripcion);
        filtrarCategoria(manufacturer);

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

        listview = (RecyclerView) findViewById(R.id.lvSKUCode);
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
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void filtrarCategoria(String manufacturer) {
//        List<String> operadores = handler.getCategoriaFlooring2(codigo_pdv, canal, subcanal, manufacturer);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategoria.setAdapter(dataAdapter);
//        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoria(String categoria, String manufacturer) {
//        List<String> operadores = handler.getSubcategoriaFlooring2(categoria, codigo_pdv, canal, subcanal, manufacturer);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spSubcategoria.setAdapter(dataAdapter);
//        spSubcategoria.setOnItemSelectedListener(this);
    }

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

    /*public void filtrarFabricante(String logro, String subcategoria, String manufacturer) {
        List<String> operadores = handler.getPresentacionFlooring(logro,subcategoria,manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentacion.setAdapter(dataAdapter);
        spPresentacion.setOnItemSelectedListener(this);
    }*/

    public void filtrarBrand(String categoria, String subcategoria, String manufacturer) {
//        List<String> operadores = handler.getBrandFlooring2(categoria, subcategoria, codigo_pdv, canal, subcanal,manufacturer);
//        if (operadores.size()==2) {
//            operadores.remove(0);
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spMarca.setAdapter(dataAdapter);
//        spMarca.setOnItemSelectedListener(this);
    }

    /*public void filtrartamano(String subcategoria, String subcategoria, String subcategoria, String presentacion, String contenido) {
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
                filtrarSubcategoria(categoria, manufacturer);
                listview.setAdapter(null);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSubcategoria) {
            try{
                subcategoria = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(categoria, subcategoria, manufacturer);
                listview.setAdapter(null);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if (adapterView== spPresentacion) {
            try{
                presentacion = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(logro, subcategoria, manufacturer);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spSubcategoria) {
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
                listview.setAdapter(null);
                brand = adapterView.getItemAtPosition(i).toString();
                showListView(categoria, subcategoria, brand, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if (adapterView==spTamano) {
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

    public void showListView(String categoria, String subcategoria, String brand, String manufacturer) {
//        listProductos = handler.filtrarListProductos3Flooring(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
//        dataAdapter = new CustomAdapterInventario(this, listProductos);
//        if (!dataAdapter.isEmpty()) {
//            empty.setVisibility(View.INVISIBLE);
//            listview.setVisibility(View.VISIBLE);
//            listview.setAdapter(dataAdapter);
//
//            txtSKUCode.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
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
//                    //filter(s.toString());
//                    //dataAdapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    //filter(s.toString());
//                }
//            });
//
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }

//        listProductos = handler.filtrarListProductos3Flooring(categoria, subcategoria, brand, codigo_pdv, canal, subcanal, manufacturer);
//        dataAdapter = new CustomAdapterInventario(this, listProductos);
//        if (dataAdapter.getItemCount() != 0) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FlooringActivity.this);
//            listview.setLayoutManager(linearLayoutManager);
//            listview.setHasFixedSize(true);
//            CustomAdapterInventario customAdapter = new CustomAdapterInventario(this.getApplicationContext(),listProductos);
//            listview.setAdapter(customAdapter);
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
    }

    public void listUpdate(ArrayList<BasePortafolioProductos> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterInventario(this, data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }
/*
    public void showListDescripcion(String status, String subcategoria,String subcategoria,String presentacion, String contenido, String tamano, String cantidad) {
        listDescripcionL = handler.filtrarListDescripcion(status,subcategoria,subcategoria,presentacion,contenido,tamano,cantidad);
        dataAdapter = new CustomAdapterPresenciaMinima(this,listDescripcionL);
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
                                Toast.makeText(getApplicationContext(), "Verifica el Ingreso en Inventario", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }
    }*/

    public void insertData(String skuSelected, String fecha_caducidad, String sugerido, String causal, String observacion, String inventario) {
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
        String subcategoria = spSubcategoria.getSelectedItem().toString();
        /*String subcategoria = spSubcategoria.getSelectedItem().toString();
        String segmento = spSegmento.getSelectedItem().toString();*/
        String brand = spMarca.getSelectedItem().toString();
        /*String tamano = spTamano.getSelectedItem().toString();
        String cantidad = spCantidad.getSelectedItem().toString();*/

        //String presentacion = spPresentacion.getSelectedItem().toString();
        String contenido = handler.getContenidoPrecios(skuSelected);
        String sector = handler.getSectorPrecios(skuSelected);

        values.put(InsertFlooring.Columnas.PHARMA_ID, id_pdv);
        values.put(InsertFlooring.Columnas.CODIGO, codigo_pdv);
        values.put(InsertFlooring.Columnas.USUARIO, user);
        values.put(InsertFlooring.Columnas.SUPERVISOR, punto_venta);
        values.put(InsertFlooring.Columnas.FECHA, fechaser);
        values.put(InsertFlooring.Columnas.HORA, horaser);
        values.put(InsertFlooring.Columnas.SECTOR,sector);
        values.put(InsertFlooring.Columnas.CATEGORIA,categoria);
        values.put(InsertFlooring.Columnas.SUBCATEGORIA,subcategoria);
        values.put(InsertFlooring.Columnas.PRESENTACION,"");
        values.put(InsertFlooring.Columnas.BRAND,brand);
        values.put(InsertFlooring.Columnas.CONTENIDO,contenido);
        values.put(InsertFlooring.Columnas.SKU_CODE,skuSelected);
        values.put(InsertFlooring.Columnas.INVENTARIOS,inventario);
        values.put(InsertFlooring.Columnas.SUGERIDOS,sugerido);
        values.put(InsertFlooring.Columnas.CAUSAL,"");
        values.put(InsertFlooring.Columnas.OTROS,observacion);
        values.put(InsertFlooring.Columnas.FECHA_CADUCIDAD,fecha_caducidad);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(InsertFlooring.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true, Constantes.insertFlooring, null);
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

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
//                                Toast.makeText(getApplicationContext(), "No ingresaste la fecha o la cantidad", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                });
//            }
//            //Devolver al ListView la fila creada
//            return convertView;
//        }
//    }

    public class CustomAdapterInventario extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterInventario.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;

        public CustomAdapterInventario(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_flooring, parent, false);
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
                ((HeaderViewHolder) holder).headerTitle1.setText("STOCK ACTUAL");
                ((HeaderViewHolder) holder).headerTitle2.setText("SUGERIDO");
//                ((HeaderViewHolder) holder).headerTitle3.setText("CAUSAL SUGERIDO");
                ((HeaderViewHolder) holder).headerTitle4.setText("OBSERVACION");
                ((HeaderViewHolder) holder).headerTitle5.setText("CADUCIDAD");
            }else if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());
            }

            sesion = handler.getListGuardadoInventario(codigo_pdv);
            for(int i = 0; i < sesion.size(); i++) {
                if (mObject.getSku().equals(sesion.get(i).getSku())) {
                    ((FlooringActivity.CustomAdapterInventario.ItemViewHolder) holder).txt_sku.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo_alicorp));

                    ((FlooringActivity.CustomAdapterInventario.ItemViewHolder) holder).txt_sku.setTypeface(null, Typeface.BOLD);
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

//        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
//            listProductos = filteredList;
//            Log.i("LIST",listProductos.status()+"");
//            FlooringActivity.this.dataAdapter.notifyDataSetChanged();
//        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5;

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

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView txt_sku;
            public CheckBox chkGuardar;
            public EditText txtStock;
            public EditText txtSugerido;
//            public Spinner spCausal;
            public EditText txtObservacion;
            public TextView txtFecha;
            public ImageButton btnFechaProd;

            public ItemViewHolder(View itemView) {
                super(itemView);

                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
                txtStock = (EditText) itemView.findViewById(R.id.txtCantidad);
                txtSugerido = (EditText) itemView.findViewById(R.id.txtSugerido);
//                spCausal = (Spinner) itemView.findViewById(R.id.spCausal);
                txtObservacion = (EditText) itemView.findViewById(R.id.txtObservacion);
                txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
                btnFechaProd = (ImageButton) itemView.findViewById(R.id.btnFechaProd);
                chkGuardar = (CheckBox) itemView.findViewById(R.id.checkGuardar);

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

                btnFechaProd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //finalv.txtCantidad.setText("PRUEBA");
                        final Calendar calendar = Calendar.getInstance();
                        int anio = calendar.get(Calendar.YEAR);
                        int mes = calendar.get(Calendar.MONTH);
                        int dia = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog from_dateListener = new DatePickerDialog(FlooringActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                                txtFecha.setText(outDate);
                                dataAdapter.notifyDataSetChanged();
                            }
                        },anio,mes,dia);
                        from_dateListener.show();
                    }
                });

                chkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (((CheckBox)v).isChecked()) {
                            String skuSelected = txt_sku.getText().toString();
                            String stock = txtStock.getText().toString().trim();
                            String sugerido = txtSugerido.getText().toString().trim();
//                            String causal = spCausal.getSelectedItem().toString().trim();
                            String observacion = txtObservacion.getText().toString().trim();
                            String fecha_caducidad = txtFecha.getText().toString().trim();

                            if (!stock.trim().isEmpty() &&
                                !sugerido.trim().isEmpty() &&
                                !observacion.trim().isEmpty() &&
                                !fecha_caducidad.trim().isEmpty()) {
                                insertData(skuSelected, fecha_caducidad, sugerido, "", observacion, stock);
                            } else {
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
