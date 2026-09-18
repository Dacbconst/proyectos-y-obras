package com.luckyecuador.app.PintucoAPP.ui.productos_mal_estado;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.Base_portafolio_productos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.AlertChangeTime;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

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


public class ProductosMalEstadoFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {


    ArrayList<Base_portafolio_productos> type_name_copy = new ArrayList<Base_portafolio_productos>();
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
    private Spinner spVariante;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    private EditText txtSKUCode;
    private EditText txtDescripcion;

    private String categoria, subcategoria, segmento1, segmento2, brand, variante, tamano, cantidad, codigo, descripcion, format, presentacion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora;

    private final String manufacturer = "AkzoNobel S.A";

    DatabaseHelper handler;

    ArrayList<Base_portafolio_productos> listProductos;
    // List<String> listDescripcionL;

    List<String> filterProducts;
    List<String> filterTargets;

    String venta, souv;
    private SharedPreferences sharedPref;

    CustomAdapterProductosMalEstado dataAdapter;


    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera
    public static ImageView imageView;
    public static TextView lblEstadoFoto;
    String estado_foto = "NO FOTO";
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;


    private final String CARPETA_RAIZ = "DanecApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Inventario";

    String path;
    String modulo = "PRODUCTOS EN MAL ESTADO";
    String cadena = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.fragment_productos_mal_estado, container, false);

        LoadData();


        // Evita que el teclado no cubra al escribir
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();




        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        spPresentacion = (Spinner) rootView.findViewById(R.id.spPresentacion);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        spVariante = (Spinner) rootView.findViewById(R.id.spVariante);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);

        //startService(new Intent(getApplicationContext(), LocationService.class));

        listview = (ListView) rootView.findViewById(R.id.lvSKUCode);

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

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

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Productos en mal estado");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
        new AlertChangeTime(getActivity());
    }


    public void filtrarCategoria(String manufacturer) {
        List<String> operadores = handler.getCategoriaPropensosYMalEstado(manufacturer,modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }


    public void filtrarSubcategoria(String categoria, String manufacturer){
        List<String> operadores = handler.getSubcategoriaFlooring(categoria, manufacturer);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String categoria, String manufacturer){
        List<String> operadores = handler.getMarcaPropensosYMalEstado(categoria,manufacturer,modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

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
                filtrarBrand(categoria, manufacturer);
                listview.setAdapter(null);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
//        if(adapterView== spSubcategoria){
//            try{
//                subcategoria = adapterView.getItemAtPosition(i).toString();
//                filtrarBrand(categoria, manufacturer);
//                listview.setAdapter(null);
////                filtrarPresentacion(categoria, subcategoria, manufacturer);
//            }catch (Exception e){
//                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }

        if(adapterView== spMarca){
            try{
                brand = adapterView.getItemAtPosition(i).toString();
                showListView(categoria, brand, manufacturer);
//                filtrarVariante(categoria, subcategoria, brand, manufacturer);
//                listview.setAdapter(null);
//                showListView(categoria, subcategoria, brand, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,segmento,contenido);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if(adapterView== spVariante){
            try{
                variante = adapterView.getItemAtPosition(i).toString();
//                showListView(categoria, subcategoria, brand, variante, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,segmento,contenido);
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*if(adapterView==spTamano){
            try{
                tamano=adapterView.getItemAtPosition(i).toString();
                filtrarcantidad(subcategoria,subcategoria,subcategoria,segmento,contenido,tamano);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if(adapterView==spCantidad){
            try{
                cantidad=adapterView.getItemAtPosition(i).toString();
                showListView(subcategoria,subcategoria,subcategoria,segmento,contenido,tamano,cantidad);
               // showListDescripcion(channel,subcategoria,subcategoria,segmento,contenido,tamano,cantidad);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/
    }

    public void showListView(String categoria, String brand, String manufacturer){
        listProductos = handler.filtrarListProductosMalEstado(categoria, brand, manufacturer, modulo);
        dataAdapter = new CustomAdapterProductosMalEstado(getContext(), listProductos);
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
                    ArrayList<Base_portafolio_productos> type_name_filter = new ArrayList<Base_portafolio_productos>();

                    String text = s.toString();

                    for (int i = 0; i < listProductos.size(); i++) {
                        if ((listProductos.get(i).getSku().toLowerCase()).contains(text.toLowerCase())) {
                            Base_portafolio_productos p = new Base_portafolio_productos();
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

    public void listUpdate(ArrayList<Base_portafolio_productos> data) {
        if(!data.isEmpty()){
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterProductosMalEstado(getContext(), data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

//    public class CustomAdapterProductosMalEstado extends ArrayAdapter<Base_portafolio_productos> implements Filterable {
//
//        public ArrayList<Base_portafolio_productos> values;
//        public Context context;
//
//        public CustomAdapterProductosMalEstado(Context context, ArrayList<Base_portafolio_productos> values) {
//            super(context, 0, values);
//            this.values = values;
//        }
//
//        public class ViewHolder{
//            public TextView txt_sku;
//            public TextView lblEstado;
//            public TextView lblEstadoFoto;
//        //    public Spinner spConteoTotal;
//        //    public RadioGroup rgConteoTotal;
//        //    public EditText txtTotal;
//        //    public Spinner spConteoDefectuoso;
//            public RadioGroup rgConteoDefectuoso;
//            public EditText txtDefectuosos;
//            public TextView lblFechaCaducidadTotal;
//            public ImageButton btnFechaCaducidadTotal;
//       //     public TextView lblFechaCaducidad;
//        //    public ImageButton btnFechaCaducidad;
//            public Spinner spCausales;
//            public ImageView ivFoto;
//            public ImageButton btnCamera;
//            public ImageButton ibPreview;
//            public Button chkGuardar;
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
//            ViewHolder vHolder = null;
//            //Comprobar si el View existe
//            //Si no existe inflarlo
//            if (null == convertView) {
//                convertView = inflater.inflate(R.layout.list_row_productos_mal_estado, parent, false); // Modificacion (list_row_option) GT
//                //Obtener instancias de los elementos
//                vHolder = new ViewHolder();
//                vHolder.txt_sku = (TextView) convertView.findViewById(R.id.lblSku);
//                vHolder.lblEstado = (TextView) convertView.findViewById(R.id.lblEstado);
//                vHolder.lblEstadoFoto = (TextView) convertView.findViewById(R.id.lblEstadoFoto);
//                vHolder.rgConteoDefectuoso = (RadioGroup) convertView.findViewById(R.id.rgConteoDefectuoso);
//                vHolder.txtDefectuosos = (EditText) convertView.findViewById(R.id.txtDefectuosos);
//                vHolder.lblFechaCaducidadTotal = (TextView) convertView.findViewById(R.id.lblFechaCaducidadTotal);
//                vHolder.btnFechaCaducidadTotal = (ImageButton) convertView.findViewById(R.id.btnFechaCaducidadTotal);
//                vHolder.spCausales = (Spinner) convertView.findViewById(R.id.spCausales);
//                vHolder.ivFoto = (ImageView) convertView.findViewById(R.id.ivFoto);
//                vHolder.btnCamera = (ImageButton) convertView.findViewById(R.id.ibCargarFoto);
//                vHolder.ibPreview = (ImageButton) convertView.findViewById(R.id.ibPreview);
//                vHolder.chkGuardar = (Button) convertView.findViewById(R.id.checkGuardar);
//
//                convertView.setTag(vHolder);
//                CustomAdapterProductosMalEstado.ViewHolder finalVHolder = vHolder;
//            } else {
//                vHolder = (CustomAdapterProductosMalEstado.ViewHolder) convertView.getTag();
//            }
//
//            try {
//                if (values.size() > 0) {
//                    String sku = values.get(position).getSku();
//                    vHolder.txt_sku.setText(sku);
//
//                    String estado = handler.getEstadoPropensosYProductosMalEstado(codigo_pdv, user, sku, modulo);
//                    vHolder.lblEstado.setText(estado);
//                    if (estado.equalsIgnoreCase("REALIZADO")) {
//                        vHolder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//                    }
//
//                    final CustomAdapterProductosMalEstado.ViewHolder finalv = vHolder;
//
//                    vHolder.txtDefectuosos.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            int unidades_defectuosas = -1;
//                            if (!finalv.txtDefectuosos.getText().toString().trim().isEmpty()) {
//                                unidades_defectuosas = Integer.parseInt(finalv.txtDefectuosos.getText().toString());
//                            }
//                            Log.i("ENTRA", "TEXT CHANGED DEFECTUOSOS");
//                            filtrarCausales(finalv.spCausales, unidades_defectuosas);
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {}
//                    });
//
//
//                    vHolder.btnCamera.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            cargarImagen();
//                            imageView = finalv.ivFoto;
//                            lblEstadoFoto = finalv.lblEstadoFoto;
//                        }
//                    });
//
//                    vHolder.ibPreview.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (finalv.ivFoto.getDrawable() != null) {
//                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
//                                alertadd.setTitle("Vista Previa");
//                                LayoutInflater factory = LayoutInflater.from(ProductosMalEstadoFragment.this.getContext());
//                                final View view = factory.inflate(R.layout.vista_previa, null);
//                                ImageView dialog_imageview = (ImageView) view.findViewById(R.id.dialog_imageview);
//                                dialog_imageview.setImageDrawable(finalv.ivFoto.getDrawable());
//                                alertadd.setView(view);
//                                alertadd.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dlg, int sumthin) {}
//                                });
//                                alertadd.show();
//                            } else {
//                                Toast.makeText(getContext(), "No hay foto cargada", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    /*
//                    vHolder.btnFechaCaducidadTotal.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            cargarFechaCaducidad(finalv.lblFechaCaducidadTotal);
//                        }
//                    });*/
//
//                    View finalConvertView = convertView;
//                    vHolder.chkGuardar.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductosMalEstadoFragment.this.getContext());
//                            builder.setIcon(android.R.drawable.ic_dialog_alert);
//                            builder.setTitle("Confirmación");
//                            builder.setMessage("¿Desea guardar la información?");
//                            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    String foto = "NO_FOTO";
//                                    Bitmap bitmapFoto = null;
//
//                                    if (imageView != null && imageView.getDrawable() != null) {
//                                        //  bitmapFoto = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                                        //  foto = getStringImage(bitmapFoto);
//
//                                        int n = Resources.getSystem().getDisplayMetrics().widthPixels;
//                                        int size = 90;
//                                        if(n < 1080){
//                                            size = 35;
//                                        }
//
//                                        // Marca de Agua
//                                        String ciudad = "Ciudad: " + handler.getCityPdv(codigo_pdv);
//                                        String local = "Local: " + punto_venta;
//                                        String usuario = "Usuario: " + user;
//                                        String fechaHora = "Fecha y hora: " + fecha + " " + hora;
//
//                                        Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                                        ImageMark im = new ImageMark();
//                                        Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, size, false);
//                                        int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
//                                        Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
//
//                                        foto = getStringImage(scaled);
//                                    }
//
//                                    String producto = finalv.txt_sku.getText().toString();
//                                //    String total = finalv.txtTotal.getText().toString();
//                                    String defectuoso = finalv.txtDefectuosos.getText().toString();
//                                //    String caducidadTotal = finalv.lblFechaCaducidadTotal.getText().toString();
//                                //    String caducidad = finalv.lblFechaCaducidad.getText().toString();
//                                    String causal = finalv.spCausales.getSelectedItem().toString();
//
//                                    if (esFormularioValido(producto, defectuoso, causal, foto, finalv.rgConteoDefectuoso)) {
//
//                                        int selectedIdConteoDefectuoso = finalv.rgConteoDefectuoso.getCheckedRadioButtonId();
//                                        RadioButton radioButtonConteoDefectuoso = (RadioButton) finalConvertView.findViewById(selectedIdConteoDefectuoso);
//                                        String conteoDefectuoso = radioButtonConteoDefectuoso.getText().toString();
//
//                                        insertData(producto, conteoDefectuoso, defectuoso, causal, foto);
//
//                                        String estado = "REALIZADO";
//                                        finalv.lblEstado.setText(estado);
//                                        finalv.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//
//                                        limpiarFormulario(finalv.rgConteoDefectuoso, finalv.txtDefectuosos,
//                                                finalv.spCausales, finalv.ivFoto, finalv.lblEstadoFoto);
//                                    }
//                                }
//                            });
//
//                            builder.setNeutralButton("NO", null);
//
//                            android.app.AlertDialog ad = builder.create();
//                            ad.show();
//                        }
//                    });
//                }
//            }catch (Exception e){
//                Log.i("EXCEPTION", e.getMessage());
//            }
//            //Devolver al ListView la fila creada
//            return convertView;
//        }
//
//
//        public void filtrarCausales(Spinner causales, int unidades_defectuosas) {
//          //  List<String> operadores = handler.getCausalesInventario(unidades_defectuosas);
//            List<String> operadores = new ArrayList<>();
//
//            /*
//            operadores.add("Prueba 1");
//            if (operadores.size()==2) {
//                operadores.remove(0);
//            }*/
//
//            causales.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, operadores));
//        }
//
//        public void cargarFechaCaducidad(TextView lblFechaCaducidad) {
//            final Calendar calendar = Calendar.getInstance();
//            int anio = calendar.get(Calendar.YEAR);
//            int mes = calendar.get(Calendar.MONTH);
//            int dia = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                    Date date = null;
//                    try {
//                        date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    String outDate = dateFormat.format(date);
//                    lblFechaCaducidad.setText(outDate);
//                    dataAdapter.notifyDataSetChanged();
//                }
//            },anio,mes,dia);
//            from_dateListener.show();
//        }
//
//        public void limpiarFormulario(RadioGroup rgDefectuosas, EditText txtDefectuoso,
//                                      Spinner spCausales, ImageView ivFoto, TextView lblEstadoFoto) {
//            rgDefectuosas.clearCheck();
//            txtDefectuoso.setText("");
//            spCausales.setSelection(0);
//            ivFoto.setImageResource(0);
//            lblEstadoFoto.setText("NO FOTO");
//            lblEstadoFoto.setBackgroundColor(getResources().getColor(R.color.rojo_pintuco));
//        }
//        public boolean esFormularioValido(String producto, String defectuoso, String causal, String foto, RadioGroup rgConteoDefectuoso) {
//            int cantidad_total = -1;
//            int cantidad_defectuosa = -1;
//            if (!defectuoso.trim().isEmpty()) {
//                cantidad_defectuosa = Integer.parseInt(defectuoso);
//            }
//
//            if (cantidad_defectuosa > cantidad_total) {
//                Toast.makeText(getContext(), "La cantidad defectuosa no puede ser mayor a la cantidad total en el producto " + producto, Toast.LENGTH_LONG).show();
//                return false;
//            }
////                if (conteoDefectuoso.equalsIgnoreCase("SELECCIONE")) {
////                    Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo de conteo defectuoso del producto " + producto, Toast.LENGTH_LONG).show();
////                    return false;
////                }
//            if(rgConteoDefectuoso.getCheckedRadioButtonId() == -1){
//                Toast.makeText(getContext(),"Debe seleccionar el tipo de conteo defectuoso del producto " + producto,Toast.LENGTH_LONG).show();
//                return false;
//            }
//            if (defectuoso.trim().isEmpty()) {
//                Toast.makeText(getContext(), "Debe ingresar la cantidad defectuosa del producto " + producto, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            if (causal.equalsIgnoreCase("SELECCIONE")) {
//                Toast.makeText(getContext(), "Debe seleccionar la causal del producto " + producto, Toast.LENGTH_LONG).show();
//                return false;
//            }
    ////                if (foto == null) {
    ////                    Toast.makeText(getApplicationContext(), "No has tomado la foto del producto " + producto, Toast.LENGTH_LONG).show();
    ////                    return false;
    ////                }
//
//            return true;
//        }
//    }



    public class CustomAdapterProductosMalEstado extends ArrayAdapter<Base_portafolio_productos> implements Filterable {

        public ArrayList<Base_portafolio_productos> values;
        public Context context;

        public CustomAdapterProductosMalEstado(Context context, ArrayList<Base_portafolio_productos> values) {
            super(context, 0, values);
            this.values = values;
        }

        public class ViewHolder{
            public TextView txt_sku;
            public TextView lblEstado;
            public TextView lblEstadoFoto;
            //    public Spinner spConteoTotal;
            //    public RadioGroup rgConteoTotal;
            //    public EditText txtTotal;
            //    public Spinner spConteoDefectuoso;
            public RadioGroup rgConteoDefectuoso;
            public EditText txtDefectuosos;
            //   public TextView lblFechaCaducidadTotal;
            //    public ImageButton btnFechaCaducidadTotal;
            //    public TextView lblFechaCaducidad;
            //     public ImageButton btnFechaCaducidad;
            public Spinner spCausales;
            public ImageView ivFoto;
            public ImageButton btnCamera;
            public ImageButton ibPreview;
            public RadioGroup rgPDVLogistica;
            public LinearLayout llPDVLogistica;
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
                convertView = inflater.inflate(R.layout.list_row_productos_mal_estado, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.txt_sku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.lblEstado = (TextView) convertView.findViewById(R.id.lblEstado);
                vHolder.lblEstadoFoto = (TextView) convertView.findViewById(R.id.lblEstadoFoto);
//                vHolder.spConteoTotal = (Spinner) convertView.findViewById(R.id.spConteoTotal);
//                vHolder.rgConteoTotal = (RadioGroup) convertView.findViewById(R.id.rgConteoTotal);
//                vHolder.txtTotal = (EditText) convertView.findViewById(R.id.txtTotal);
//                vHolder.spConteoDefectuoso = (Spinner) convertView.findViewById(R.id.spConteoDefectuoso);

                vHolder.rgConteoDefectuoso = (RadioGroup) convertView.findViewById(R.id.rgConteoDefectuoso);
                vHolder.txtDefectuosos = (EditText) convertView.findViewById(R.id.txtDefectuosos);
                vHolder.rgPDVLogistica = (RadioGroup) convertView.findViewById(R.id.rgPDVLogistica);
                vHolder.llPDVLogistica = (LinearLayout) convertView.findViewById(R.id.llPDVLogistica);

//                vHolder.lblFechaCaducidadTotal = (TextView) convertView.findViewById(R.id.lblFechaCaducidadTotal);
//                vHolder.btnFechaCaducidadTotal = (ImageButton) convertView.findViewById(R.id.btnFechaCaducidadTotal);
//
//                vHolder.lblFechaCaducidad = (TextView) convertView.findViewById(R.id.lblFechaCaducidad);
//                vHolder.btnFechaCaducidad = (ImageButton) convertView.findViewById(R.id.btnFechaCaducidad);

                vHolder.spCausales = (Spinner) convertView.findViewById(R.id.spCausales);
                vHolder.ivFoto = (ImageView) convertView.findViewById(R.id.ivFoto);
                vHolder.btnCamera = (ImageButton) convertView.findViewById(R.id.ibCargarFoto);
                vHolder.ibPreview = (ImageButton) convertView.findViewById(R.id.ibPreview);
                vHolder.chkGuardar = (Button) convertView.findViewById(R.id.checkGuardar);

                convertView.setTag(vHolder);
                CustomAdapterProductosMalEstado.ViewHolder finalVHolder = vHolder;
            } else {
                vHolder = (CustomAdapterProductosMalEstado.ViewHolder) convertView.getTag();
            }

            try {
                if (values.size() > 0) {
                    String sku = values.get(position).getSku();
                    vHolder.txt_sku.setText(sku);

                    String estado = handler.getEstadoPropensosYProductosMalEstado(codigo_pdv, user, sku, modulo);
                    vHolder.lblEstado.setText(estado);
                    if (estado.equalsIgnoreCase("REALIZADO")) {
                        vHolder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    }

                    CustomAdapterProductosMalEstado.ViewHolder finalVHolder1 = vHolder;
                    vHolder.spCausales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String causal = adapterView.getItemAtPosition(i).toString();
                            if (!causal.equalsIgnoreCase("SELECCIONE")) {
                                finalVHolder1.llPDVLogistica.setVisibility(View.VISIBLE);
                                finalVHolder1.rgPDVLogistica.clearCheck();
                            } else {
                                finalVHolder1.llPDVLogistica.setVisibility(View.GONE);
                                finalVHolder1.rgPDVLogistica.clearCheck();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    final CustomAdapterProductosMalEstado.ViewHolder finalv = vHolder;



                    vHolder.txtDefectuosos.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            int unidades_defectuosas = -1;
                            if (!finalv.txtDefectuosos.getText().toString().trim().isEmpty()) {
                                unidades_defectuosas = Integer.parseInt(finalv.txtDefectuosos.getText().toString());
                            }
                            Log.i("ENTRA", "TEXT CHANGED DEFECTUOSOS");
                            filtrarCausales(finalv.spCausales, unidades_defectuosas);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });

//                    vHolder.txtTotal.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            int unidades_defectuosas = -1;
//                            if (!finalv.txtDefectuosos.getText().toString().trim().isEmpty()) {
//                                unidades_defectuosas = Integer.parseInt(finalv.txtDefectuosos.getText().toString());
//                            }
//                            Log.i("ENTRA", "TEXT CHANGED TOTAL");
//                            filtrarCausales(finalv.spCausales, unidades_defectuosas);
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {}
//                    });

                    vHolder.btnCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cargarImagen();
                            imageView = finalv.ivFoto;
                            lblEstadoFoto = finalv.lblEstadoFoto;
                        }
                    });

                    vHolder.ibPreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (finalv.ivFoto.getDrawable() != null) {
                                AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
                                alertadd.setTitle("Vista Previa");
                                LayoutInflater factory = LayoutInflater.from(getContext());
                                final View view = factory.inflate(R.layout.vista_previa, null);
                                ImageView dialog_imageview = (ImageView) view.findViewById(R.id.dialog_imageview);
                                dialog_imageview.setImageDrawable(finalv.ivFoto.getDrawable());
                                alertadd.setView(view);
                                alertadd.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {}
                                });
                                alertadd.show();
                            } else {
                                Toast.makeText(getContext(), "No hay foto cargada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    /*
                    vHolder.btnFechaCaducidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cargarFechaCaducidad(finalv.lblFechaCaducidad);
                        }
                    });

                    vHolder.btnFechaCaducidadTotal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cargarFechaCaducidad(finalv.lblFechaCaducidadTotal);
                        }
                    });
                    */




                    View finalConvertView = convertView;
                    vHolder.chkGuardar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                            builder.setIcon(android.R.drawable.ic_dialog_alert);
                            builder.setTitle("Confirmación");
                            builder.setMessage("¿Desea guardar la información?");
                            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String foto = "NO_FOTO";
                                    Bitmap bitmapFoto = null;

                                    if (imageView != null && imageView.getDrawable() != null) {
                                        //  bitmapFoto = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        //  foto = getStringImage(bitmapFoto);

                                        int n = Resources.getSystem().getDisplayMetrics().widthPixels;
                                        int size = 90;
                                        if(n < 1080){
                                            size = 35;
                                        }

                                        // Marca de Agua
                                        String ciudad = "Ciudad: " + handler.getCityPdv(codigo_pdv);
                                        String local = "Local: " + punto_venta;
                                        String usuario = "Usuario: " + user;
                                        String fechaHora = "Fecha y hora: " + fecha + " " + hora;

                                        Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        ImageMark im = new ImageMark();
                                        Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, size, false);
                                        int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
                                        Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

                                        foto = getStringImage(scaled);
                                    }

                                    String producto = finalv.txt_sku.getText().toString();
                                    //    String total = finalv.txtTotal.getText().toString();
                                    String defectuoso = finalv.txtDefectuosos.getText().toString();
                                    //    String caducidadTotal = finalv.lblFechaCaducidadTotal.getText().toString();
                                    //    String caducidad = finalv.lblFechaCaducidad.getText().toString();
                                    String causal = finalv.spCausales.getSelectedItem().toString();

                                    if (esFormularioValido(producto, defectuoso, causal, foto, finalv.rgConteoDefectuoso,finalv.rgPDVLogistica,finalv.ivFoto)) {
                                        //    int selectedIdConteoTotal = finalv.rgConteoTotal.getCheckedRadioButtonId();
                                        //    RadioButton radioButtonConteoTotal = (RadioButton) finalConvertView.findViewById(selectedIdConteoTotal);
                                        //    String conteoTotal = radioButtonConteoTotal.getText().toString();

                                        int selectedIdConteoDefectuoso = finalv.rgConteoDefectuoso.getCheckedRadioButtonId();
                                        RadioButton radioButtonConteoDefectuoso = (RadioButton) finalConvertView.findViewById(selectedIdConteoDefectuoso);
                                        String conteoDefectuoso = radioButtonConteoDefectuoso.getText().toString();

                                        int selectedIdPDVLogistica = finalv.rgPDVLogistica.getCheckedRadioButtonId();
                                        RadioButton radioButtonPDVLogistica = (RadioButton) finalConvertView.findViewById(selectedIdPDVLogistica);
                                        String pdvLogistica = radioButtonPDVLogistica.getText().toString();

                                        insertData(producto , conteoDefectuoso, defectuoso,pdvLogistica, causal, foto);

                                        String estado = "REALIZADO";
                                        finalv.lblEstado.setText(estado);
                                        finalv.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

                                        limpiarFormulario(finalv.rgConteoDefectuoso,finalv.rgPDVLogistica, finalv.txtDefectuosos,
                                                finalv.spCausales, finalv.ivFoto, finalv.lblEstadoFoto);
                                    }
                                }
                            });

                            builder.setNeutralButton("NO", null);

                            android.app.AlertDialog ad = builder.create();
                            ad.show();
                        }
                    });
                }
            }catch (Exception e){
                Log.i("EXCEPTION", e.getMessage());
            }
            //Devolver al ListView la fila creada
            return convertView;
        }

        public void filtrarCausales(Spinner causales, int unidades_defectuosas) {
            List<String> operadores = handler.getCausalesProdMalEst(unidades_defectuosas);
            if (operadores.size()==2) {
                operadores.remove(0);
            }
            causales.setAdapter(new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, operadores));
        }

        public void cargarFechaCaducidad(TextView lblFechaCaducidad) {
            final Calendar calendar = Calendar.getInstance();
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog from_dateListener = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                    lblFechaCaducidad.setText(outDate);
                    dataAdapter.notifyDataSetChanged();
                }
            },anio,mes,dia);
            from_dateListener.show();
        }

        public void limpiarFormulario(RadioGroup rgDefectuosas,RadioGroup rgPDVLogistica, EditText txtDefectuoso,Spinner spCausales, ImageView ivFoto, TextView lblEstadoFoto) {

            rgDefectuosas.clearCheck();
            rgPDVLogistica.clearCheck();
            txtDefectuoso.setText("");
            spCausales.setSelection(0);
            ivFoto.setImageResource(0);
            lblEstadoFoto.setText("NO FOTO");
            lblEstadoFoto.setBackgroundColor(getResources().getColor(R.color.rojo_pintuco));
        }

        public boolean esFormularioValido(String producto, String defectuoso, String causal, String foto, RadioGroup rgConteoDefectuoso,RadioGroup rgPDVLogistica, ImageView imageView) {
            int cantidad_total = -1;
            int cantidad_defectuosa = -1;
            if (!defectuoso.trim().isEmpty()) {
                cantidad_defectuosa = Integer.parseInt(defectuoso);
            }

            /*
            if (!total.trim().isEmpty()) {
                cantidad_total = Integer.parseInt(total);
            }
            */
//                if (conteoTotal.equalsIgnoreCase("SELECCIONE")) {
//                    Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo de conteo total del producto " + producto, Toast.LENGTH_LONG).show();
//                    return false;
//                }

            /*
            if(rgConteoTotal.getCheckedRadioButtonId() == -1){
                Toast.makeText(getApplicationContext(),"Debe seleccionar el tipo de conteo total del producto " + producto,Toast.LENGTH_LONG).show();
                return false;
            }

            if (total.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Debe ingresar el total del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            */

            /*
            if (cantidad_defectuosa > cantidad_total) {
                Toast.makeText(getContext(), "La cantidad defectuosa no puede ser mayor a la cantidad total en el producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            */
//                if (conteoDefectuoso.equalsIgnoreCase("SELECCIONE")) {
//                    Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo de conteo defectuoso del producto " + producto, Toast.LENGTH_LONG).show();
//                    return false;
//                }

            if(rgConteoDefectuoso.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(),"Debe seleccionar el tipo de conteo defectuoso del producto " + producto,Toast.LENGTH_LONG).show();
                return false;
            }
            if (defectuoso.trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar la cantidad defectuosa del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            /*
            if (caducidadTotal.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Debe seleccionar la fecha de caducidad del producto total: " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            if (caducidad.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Debe seleccionar la fecha de caducidad del producto defectuoso: " + producto, Toast.LENGTH_LONG).show();
                return false;
            }
            */
            if (causal.equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar la causal del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }

            if (rgPDVLogistica.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getContext(), "Debe seleccionar la logistica o PDV del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }

            if (imageView.getDrawable() == null) {
                Toast.makeText(getContext(), "Debe tomar la foto del producto " + producto, Toast.LENGTH_LONG).show();
                return false;
            }

//                if (foto == null) {
//                    Toast.makeText(getApplicationContext(), "No has tomado la foto del producto " + producto, Toast.LENGTH_LONG).show();
//                    return false;
//                }

            return true;
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
                    //    imageView.setImageBitmap(bitmap);
                    //    lblEstadoFoto.setText("FOTO OK");
                    //    lblEstadoFoto.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
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
            lblEstadoFoto.setText("FOTO OK");
            lblEstadoFoto.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
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
                    //  tomarFotografia();
                    Intent n = new Intent(getContext(), CameraActivity.class);
                    n.putExtra("activity", "productos_mal_estado");
                    startActivity(n);
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

    public void insertData(String producto, String conteoDefectuoso, String defectuoso,String PDVLogistica ,String causal, String foto) {

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
//        String subcategoria = spSubcategoria.getSelectedItem().toString();
        String brand = spMarca.getSelectedItem().toString();

        String presentacion = handler.getPresentacionPropensos(producto);
        String contenido = handler.getContenidoPrecios(producto);
        String sector = handler.getSectorPrecios(producto);

        String plataforma = handler.getPlataformaBySku(producto);

        values.put(ContractInsertPropensosYProdMalEst.Columnas.PHARMA_ID, id_pdv);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CODIGO, codigo_pdv);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.USUARIO, user);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR, punto_venta);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA, fechaser);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.HORA, horaser);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SECTOR, sector);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA, categoria);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION, presentacion);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.BRAND, brand);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO, contenido);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE, producto);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.INVENTARIOS, "");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SOUVENIRS, PDVLogistica);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL,"N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TOTAL, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS, conteoDefectuoso);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS, defectuoso);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CAUSAL, causal);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FOTO, foto);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.PLATAFORMA, plataforma);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.MODULO, modulo);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContext().getContentResolver().insert(ContractInsertPropensosYProdMalEst.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPropensosYProdMalEst, null);
            Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}