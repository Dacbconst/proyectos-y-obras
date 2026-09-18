package com.luckyecuador.app.PintucoAPP;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
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

import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ImpulsoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<BasePortafolioProductos> type_name_copy = new ArrayList<BasePortafolioProductos>();
    TextView txtfechav;
    ImageButton btnFecha;
    TextView empty;
    ListView listview;
    //ListView listDescripcion;
    private Spinner spCategoria;
    private Spinner spSubCategoria;
    //private Spinner spSubcategoria;
    /*private Spinner spSubcategoria;
    private Spinner spSegmento;*/
    //private Spinner spPresentacion;
    private Spinner spMarca;
    private Spinner spSKU;
    /*private Spinner spTamano;
    private Spinner spCantidad;*/
    //private EditText txtSKUCode;
    private EditText txtDescripcion;
    private EditText txtAsignada;
    private EditText txtVendida;
    private EditText txtAdicional;
    private EditText txtCumplimiento;
    private EditText txtImpulsadora;
    private EditText txtObservaciones;
    private TextView textView;
    private Button btnGuardar;

    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion, format, presentacion, sku;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora, marca;

    private final String manufacturer = "Alicorp";

    DatabaseHelper handler;

    ArrayList<BasePortafolioProductos> listProductos;
   // List<String> listDescripcionL;

    List<String> filterProducts;
    List<String> filterTargets;

    String venta;
    private SharedPreferences sharedPref;

    CustomAdapterPresenciaMinima dataAdapter;
    ListViewAdapter dataAdapters;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera
    public ImageButton btnCamera;
    public ImageView imageView;
    Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Impulso";
    String path;
    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impulso);
        setToolbar();

        //setActionBar();
        LoadData();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        //startService(new Intent(getApplicationContext(), MyService.class));

        spCategoria = (Spinner)findViewById(R.id.spCategoria);
        spSubCategoria = (Spinner)findViewById(R.id.spSubCategoria);
        //spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        /*spSubcategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spSegmento = (Spinner)findViewById(R.id.spSegmento);*/
        //spPresentacion = (Spinner)findViewById(R.id.spPresentacion);
        spMarca = (Spinner)findViewById(R.id.spMarca);
        spSKU = (Spinner)findViewById(R.id.spSKU);
        /*spTamano = (Spinner)findViewById(R.id.spTamaño);
        spCantidad = (Spinner)findViewById(R.id.spCantidad);*/
        //txtSKUCode = (EditText) findViewById(R.id.txtSKUCode);
        txtDescripcion  = (EditText)findViewById(R.id.txtDescripcionSKU);
        txtAsignada = (EditText)findViewById(R.id.txtAsignada);
        txtVendida = (EditText)findViewById(R.id.txtVendida);
        txtAdicional  = (EditText)findViewById(R.id.txtAdicional);
        txtCumplimiento  = (EditText)findViewById(R.id.txtCumplimiento);
        txtImpulsadora  = (EditText)findViewById(R.id.txtImpulsadora);
        txtObservaciones  = (EditText)findViewById(R.id.txtObservaciones);
        textView = (TextView) findViewById(R.id.textView);

        imageView = (ImageView) findViewById(R.id.ivFotoExhibiciones);
        btnCamera = (ImageButton) findViewById(R.id.ibCargarFotoExhibiciones);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        listview = (ListView)findViewById(R.id.lvSKUCode);
        btnCamera.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        listview = (ListView)findViewById(R.id.lvSKUCode);

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

        ListView listView = (ListView) findViewById(R.id.lvSKUCode);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        txtVendida.setEnabled(false);
        txtAdicional.setEnabled(false);

        txtAsignada.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtAsignada.getText().toString().trim().isEmpty()) {
                    txtVendida.setEnabled(true);
                }else{
                    txtVendida.setEnabled(false);
                    txtVendida.setText("");
                    txtCumplimiento.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                cumplimiento();
            }
        });

        txtVendida.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtVendida.getText().toString().trim().isEmpty()) {
                    txtAdicional.setEnabled(true);
                }else{
                    txtAdicional.setEnabled(false);
                    txtAdicional.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                cumplimiento();
            }
        });

        txtAdicional.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                cumplimiento();
            }
        });
    }

    public void cumplimiento() {
        try {
            DecimalFormat df = new DecimalFormat("#.00");

            double asignada = 0;
            double vendida = 0;
            double adicional = 0;
            double cumplimiento = 0;
            String p_cumplimiento = "";

            if (txtAsignada.getText().toString().equals("")) {
                asignada = 0;
            } else {
                asignada = Double.parseDouble(txtAsignada.getText().toString());
            }

            if (txtVendida.getText().toString().equals("")) {
                vendida = 0;
            } else {
                vendida = Double.parseDouble(txtVendida.getText().toString());
            }

            if (txtAdicional.getText().toString().equals("")) {
                adicional = 0;
            } else {
                adicional = Double.parseDouble(txtAdicional.getText().toString());
            }


            cumplimiento = vendida + adicional;
            p_cumplimiento = df.format((cumplimiento / asignada) * 100);

            txtCumplimiento.setText(p_cumplimiento + "%");

            if (Double.parseDouble(p_cumplimiento) >= 90.00) {
                textView.setText("¡EXCELENTE!");
                textView.setBackgroundColor(Color.GREEN);
            } else {
                textView.setText("Cumplimiento Mínimo del 90%");
                textView.setBackgroundColor(Color.RED);
            }
        }catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnCamera) {
            cargarImagen();
        }
        if (v == btnGuardar) {
            if (esFormularioValido()) {
                insertData();
            }
        }
    }

    public boolean esFormularioValido() {
        if (txtAsignada.getText().toString().trim().isEmpty() ||
            txtVendida.getText().toString().trim().isEmpty() ||
            txtAdicional.getText().toString().trim().isEmpty() ||
            txtCumplimiento.getText().toString().trim().isEmpty() ||
            txtImpulsadora.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void limpiarFormulario() {
        txtAsignada.setText("");
        txtVendida.setText("");
        txtAdicional.setText("");
        txtCumplimiento.setText("");
        txtImpulsadora.setText("");
        txtObservaciones.setText("");
        imageView.setImageResource(android.R.color.transparent);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
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
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ImpulsoActivity.this);
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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ImpulsoActivity.this);
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ImpulsoActivity.this);
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

    public void filtrarCategoria(String manufacturer) {
        List<String> operadores = handler.getCategoriaImpulso(manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubCategoria(String categoria, String manufacturer) {
        List<String> operadores = handler.getSubCategoriaImpulso(categoria, manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategoria.setAdapter(dataAdapter);
        spSubCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String categoria, String subcategoria, String manufacturer) {
        List<String> operadores = handler.getBrandImpulso(categoria,subcategoria,manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    public void filtrarSKU(String categoria, String subcategoria, String brand, String manufacturer) {
        List<String> operadores = handler.getSKUImpulso(categoria,subcategoria,brand,manufacturer);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSKU.setAdapter(dataAdapter);
        spSKU.setOnItemSelectedListener(this);
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
                filtrarSubCategoria(categoria, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSubCategoria) {
            try{
                subcategoria = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(categoria, subcategoria, manufacturer);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spMarca) {
            try{
                marca = adapterView.getItemAtPosition(i).toString();
                filtrarSKU(categoria, subcategoria, marca, manufacturer);
                //filtrarMarca(subcategoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSKU) {
            try{
                sku = adapterView.getItemAtPosition(i).toString();
                //showListView(logro, subcategoria, presentacion, brand, manufacturer);
                //filtrartamano(subcategoria,subcategoria,subcategoria,presentacion,contenido);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public class CustomAdapterPresenciaMinima extends ArrayAdapter<BasePortafolioProductos> implements Filterable {

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

                        if (((CheckBox)v).isChecked()) {
                            //if ( !txtfechav.getText().toString().equals("") ) {
                            if (!venta.equals("") || venta!=null) {
                                //insertData(finalv.lblSku.getText().toString(),venta);
                            }else{
                                Toast.makeText(getApplicationContext(), "Verifica el Ingreso en Inventario", Toast.LENGTH_LONG).show();
                            }

                       // }else
                       // {
                       //     Toast.makeText(getApplicationContext(), "Verifica el Ingreso en Fecha", Toast.LENGTH_LONG).show();
                        //}
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }

    }

    public void insertData() {
        try{
            if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                image = getStringImage(bitmapfinal);
            }else{
                Toast.makeText(this,"No has tomado la foto",Toast.LENGTH_LONG).show();
            }
            if (image != null && !image.equals("")) {
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
                String marca = spMarca.getSelectedItem().toString();
                String sku = spSKU.getSelectedItem().toString();
                String asignada = txtAsignada.getText().toString();
                String vendida = txtVendida.getText().toString();
                String adicional = txtAdicional.getText().toString();
                String cumplimiento = txtCumplimiento.getText().toString();
                String impulsadora = txtImpulsadora.getText().toString();
                String observacion = txtObservaciones.getText().toString();

                values.put(ContractInsertImpulso.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertImpulso.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertImpulso.Columnas.USUARIO, user);
                values.put(ContractInsertImpulso.Columnas.SUPERVISOR, punto_venta);
                values.put(ContractInsertImpulso.Columnas.FECHA, fechaser);
                values.put(ContractInsertImpulso.Columnas.HORA, horaser);
                values.put(ContractInsertImpulso.Columnas.CATEGORIA,categoria);
                values.put(ContractInsertImpulso.Columnas.BRAND,marca);
                values.put(ContractInsertImpulso.Columnas.SKU_CODE,sku);
                values.put(ContractInsertImpulso.Columnas.CANTIDAD_ASIGNADA,asignada);
                values.put(ContractInsertImpulso.Columnas.CANTIDAD_VENDIDA,vendida);
                values.put(ContractInsertImpulso.Columnas.CANTIDAD_ADICIONAL,adicional);
                values.put(ContractInsertImpulso.Columnas.CUMPLIMIENTO,cumplimiento);
                values.put(ContractInsertImpulso.Columnas.IMPULSADORA,impulsadora);
                values.put(ContractInsertImpulso.Columnas.OBSERVACION,observacion);
                values.put(ContractInsertImpulso.Columnas.FOTO,image);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertImpulso.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertImpulso, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                limpiarFormulario();
            }else {
                Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
