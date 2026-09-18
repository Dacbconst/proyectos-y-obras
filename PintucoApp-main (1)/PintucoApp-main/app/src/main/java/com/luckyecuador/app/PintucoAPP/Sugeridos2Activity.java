package com.luckyecuador.app.PintucoAPP;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class Sugeridos2Activity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private EditText txtCaras;
    private TextView lblOtros;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spBrand;
    private Spinner spContenido;
    private Spinner spRazones;
    private Spinner spSegmento;
    private EditText txtSkuCode;
    private Button btnGuardar;

    private TextView txtLocal;
    private TextView txtCodigoFabril;
    private TextView txtVendedor;
    //private Spinner spBrand;
    String[] valueOfEditText;
    String[] valueOfEditText2;
    String[] valueOfEditText3;
    String[] valueOfSwitch;
    String[] valueOfSwitch2;
    String[] valueOfSpinner;
    String[] valueOfTextView;
    CustomAdapterSugeridos dataAdapter;
    TextView empty;
    RecyclerView listview;

    String user,
            codigo_pdv,
            punto_venta,
            fecha,
            hora,
            sku_list;
    private String nom_comercial, vendedor, celular, format;


    ArrayList<BasePortafolioProductos> listProductos;
    String id_pdv, subcategoria, presentacion, venta, categoria, sku, canal, subcanal, contenido, canti_sug, observaciones, entrega;
    String brand;
    DatabaseHelper handler;
    private int resta;
    ImageView btnCalculo;
    ImageButton btnCamera;

    private ImageView imageView;
    private Bitmap bitmapfinal;
    Bitmap bitmap;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Share";
    String path;
    String image = "";

    float battery;
    String mensaje="";

    private final String manufacturer = "Alicorp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugeridos_2);
        setToolbar();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        LoadData();
        txtCaras=(EditText) findViewById(R.id.txtCaras);
        lblOtros=(TextView) findViewById(R.id.lblOtros);
        spCategoria =(Spinner) findViewById(R.id.spSector);
        spSubcategoria =(Spinner) findViewById(R.id.spCategoria);
        spBrand = (Spinner)findViewById(R.id.spMarca);
        spContenido = (Spinner)findViewById(R.id.spContenido);
        spRazones =(Spinner) findViewById(R.id.spRazon);
        spSegmento =(Spinner) findViewById(R.id.spSegmento);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        //spBrand =(Spinner) findViewById(R.id.spCategoria);
        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        btnCalculo=(ImageView) findViewById(R.id.btnCalculo);
        imageView = (ImageView) findViewById(R.id.ivFotoShare);
        btnCamera=(ImageButton) findViewById(R.id.ibCargarFotoShare);

        Log.i("NOMBRECOM", nom_comercial);
        Log.i("CANAL", canal);

        txtLocal = (TextView) findViewById(R.id.txtLocal);
        txtCodigoFabril = (TextView) findViewById(R.id.txtCodigoFabril);
        txtVendedor = (TextView) findViewById(R.id.txtVendedor);
        txtLocal.setText(punto_venta);
        txtCodigoFabril.setText(format);
        txtVendedor.setText(vendedor);




        //startService(new Intent(getApplicationContext(), MyService.class));

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

        txtCaras.setFilters(new InputFilter[]{new InputFilter() {
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

        txtCaras.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(6)});

        btnCalculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int sum = 0;
                    for(int i=0;i<listview.getChildCount();i++) {
                        View v=listview.getChildAt(i);
                        EditText edt = (EditText) v.findViewById(R.id.txtventa);
//
                        if (!edt.getText().toString().equals(""))
                            sum+=Integer.parseInt(String.valueOf(edt.getText()));
                    }
                    if (!txtCaras.getText().toString().equals("")) {
                        int cmTotal = Integer.parseInt(String.valueOf(txtCaras.getText()));
                        resta = cmTotal - sum;
           //             resta(resta);
                    }
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == btnCamera) {
                    cargarImagen();
                }


            }
        });

        filtrarCategoria();

        listview = (RecyclerView) findViewById(R.id.list);
        listview.setHasFixedSize(true);
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        txtCaras.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                txtCaras.setTextColor(getResources().getColor(R.color.negro));
            }
        });

        lblOtros.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                lblOtros.setTextColor(getResources().getColor(R.color.negro));
            }
        });
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

 /*   public void resultado() {
        int resta = 0;
        try{
            if (!txtCaras.getText().toString().equals("")) {
                int cmTotal = Integer.parseInt(String.valueOf(txtCaras.getText()));
                resta = cmTotal - sum();
//                resta(resta);
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }*/

   /* public int verificacion_resultado() {
        int resta = 0;
        try{
            if (!txtCaras.getText().toString().equals("")) {
                int cmTotal = Integer.parseInt(String.valueOf(txtCaras.getText()));
                resta = cmTotal - sum();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resta;
    }

    public int sum() {
        int sum = 0;
        try {
            for(int i = 0; i < listview.getChildCount(); i++) {
                View v = listview.getChildAt(i);
                EditText edt = (EditText) v.findViewById(R.id.txtventa);
                Log.i("FOR", edt.getText().toString());
                if (!edt.getText().toString().equals(""))
                    sum += Integer.parseInt(String.valueOf(edt.getText()));
            }

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return sum;
    }*/

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        vendedor =sharedPreferences.getString(Constantes.VENDEDOR,Constantes.NODATA);
        celular =sharedPreferences.getString(Constantes.CELULAR,Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        nom_comercial = sharedPreferences.getString(Constantes.NOM_COMERCIAL, Constantes.NODATA);
    }

/*    public void resta(int total) {
        try{
            if (total<0) {
                lblOtros.setBackgroundColor(Color.RED);
                lblOtros.setTextColor(Color.BLACK);
                Toast.makeText(getApplicationContext(),"Valor de celdas menor al Total",Toast.LENGTH_SHORT).show();
            }else{
                lblOtros.setBackgroundColor(Color.TRANSPARENT);
            }
            lblOtros.setText(String.valueOf(total));

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarTotal(String codigo, String categoria, String subcategoria) {
        //Log.i("CODIGO", codigo_pdv);
        String totalUniverso = handler.getTotalUniverso(codigo, categoria, subcategoria);
        txtCaras.setText(totalUniverso);
        txtCaras.setTextColor(getResources().getColor(R.color.verde));
    }

    public void cargarOtros(String codigo, String categoria, String subcategoria) {
        //Log.i("CODIGO", codigo_pdv);
        String otros = handler.getOtros(codigo, categoria, subcategoria);
        lblOtros.setText(otros);
        lblOtros.setTextColor(getResources().getColor(R.color.verde));
    }

    public String cargarCaras(String codigo, String categoria, String subcategoria, String marca) {
        //Log.i("CODIGO", codigo_pdv);
        String totalCaras = handler.getTotalCaras(codigo, categoria, subcategoria, marca);
        return totalCaras;
    }*/

    public void filtrarCategoria() {
        List<String> operadores = handler.getCategoriaSugeridos(codigo_pdv);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoria(String categoria) {
        List<String> operadores = handler.getSubcategoriaSms(categoria, codigo_pdv);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String categoria, String subcategoria) {
        List<String> operadores = handler.getBrandSugeridos(categoria, subcategoria, codigo_pdv, canal, subcanal);
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

    public void filtrarSegmento(String sector, String categoria) {
        List<String> operadores = handler.getSegmentoShare(sector, categoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
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

    /*public void filtrarMarca(String logro, String subcategoria) {
        List<String> operadores = handler.getBrandShare(subcategoria,logro);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }*/

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
//            imageView.setImageBitmap(scaled);

            try {
                for(int i = 0; i < listview.getChildCount(); i++) {
                    View v = listview.getChildAt(i);
                    EditText edt = (EditText) v.findViewById(R.id.txtventa);
                    ImageView iv = (ImageView) v.findViewById(R.id.ivFotoExhibiciones);
                    Log.i("FOR", edt.getText().toString());
                    if (edt.getText().toString().equals(sku_list)) {
//                        sum += Integer.parseInt(String.valueOf(edt.getText()));
                        iv.setImageBitmap(scaled);
                        bitmapfinal = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                        Log.i("BITMAP:", bitmapfinal.toString());
                    }
                }//
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

//            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(this).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
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
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //String to bitmap
//    public Bitmap StringToBitMap(String encodedString) {
//        try{
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        }catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView== spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSubcategoria(categoria);
                listview.setAdapter(null);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView== spSubcategoria)
        {
            try{
                subcategoria =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                //filtrarSegmento(logro, subcategoria);
             //   cargarTotal(codigo_pdv, categoria, subcategoria);
             //   cargarOtros(codigo_pdv, categoria, subcategoria);
                filtrarBrand(categoria, subcategoria);
                listview.setAdapter(null);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView== spBrand)
        {
            try{
                brand =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                //filtrarSegmento(logro, subcategoria);
             //   cargarTotal(codigo_pdv, categoria, subcategoria);
             //   cargarOtros(codigo_pdv, categoria, subcategoria);
                filtrarContenido(categoria, subcategoria, brand);
                listview.setAdapter(null);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

          if (adapterView== spContenido)
        {
            try{
                contenido =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                //filtrarSegmento(logro, subcategoria);
             //   cargarTotal(codigo_pdv, categoria, subcategoria);
             //   cargarOtros(codigo_pdv, categoria, subcategoria);
                showListView(categoria, subcategoria, brand, contenido);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        /*if (adapterView== spSegmento)
        {
            try{
                presentacion =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                showListView(logro, subcategoria, presentacion);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

        /*if (adapterView==spBrand) {
            contenido = spBrand.getSelectedItem().toString();
            showListView(logro, subcategoria);
        }*/

    }

    /*public void showListView(String sector ,String logro, String segmento) {
        listProductos = handler.filtrarListProductos2Share(sector,logro, segmento);

        dataAdapter = new CustomAdapterPresenciaMinima(this,listProductos);
        listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, listProductos.size()*85));
        listview.setAdapter(dataAdapter);

        if (!dataAdapter.isEmpty()) {
            listview.setAdapter(dataAdapter);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }*/
//    public void showListView(String sector ,String categoria, String canal, String subcanal) {
//        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal);
//
//        dataAdapter = new CustomAdapterPresenciaMinima(this,listProductos);
//        listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, listProductos.size()*100));
//        listview.setAdapter(dataAdapter);
//
//        if (!dataAdapter.isEmpty()) {
//            listview.setAdapter(dataAdapter);
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
//    }

/*    public void showListView(String sector ,String categoria, String canal, String subcanal) {
        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal);

        dataAdapter = new CustomAdapterSugeridos(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Sugeridos2Activity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterSugeridos customAdapter = new CustomAdapterSugeridos(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            dataAdapter = (CustomAdapterSugeridos) listview.getAdapter();
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }*/

    public void showListView(String categoria ,String subcategoria, String brand, String contenido) {
        listProductos = handler.filtrarListSugProductos4(categoria, subcategoria, brand, contenido, codigo_pdv, canal, subcanal);

        dataAdapter = new CustomAdapterSugeridos(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {

            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Sugeridos2Activity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterSugeridos customAdapter = new CustomAdapterSugeridos(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            dataAdapter = (CustomAdapterSugeridos) listview.getAdapter();
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

//    public class CustomAdapterPresenciaMinima extends ArrayAdapter<String> {
//
//        public List<String> values;
//        public Context context;
//        boolean[] checkBoxState;
//
//        public CustomAdapterPresenciaMinima(Context context, List<String> values) {
//            super(context, 0, values);
//            this.values = values;
//            checkBoxState=new boolean[values.size()];
//        }
//
//        public class ViewHolder{
//            TextView lblSku;
//            CheckBox check; // agregado GT
//            EditText txtventa;
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
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            //Obtener Instancia Inflater
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            ViewHolder vHolder = null;
//            //Comprobar si el View existe
//            //Si no existe inflarlo
//            if (null == convertView) {
//                convertView = inflater.inflate(R.layout.list_row_option_share, parent, false); // Modificacion (list_row_option) GT
//
//                //Obtener instancias de los elementos
//                vHolder = new ViewHolder();
//                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
//                vHolder.check = (CheckBox) convertView.findViewById(R.id.checkPresencia);
//                vHolder.check.setVisibility(View.GONE);
//                //   vHolder.txtunidad = (EditText) convertView.findViewById(R.id.txtunidad);
//                vHolder.txtventa = (EditText) convertView.findViewById(R.id.txtventa);
//
//                vHolder.txtventa.setHint("Caras/Percha");
//                //cargarMotivos(vHolder);
//                convertView.setTag(vHolder);
//
//                //checkGuardar.setEnabled(true);
//            } else { vHolder = (ViewHolder) convertView.getTag(); }
//
//            if (values.size() > 0) {
//                //set the data to be displayed
//                vHolder.lblSku.setText(values.get(position));
//
//                vHolder.txtventa.setText(cargarCaras(codigo_pdv, categoria, subcategoria, values.get(position)));
//                vHolder.txtventa.setTextColor(getResources().getColor(R.color.verde));
//                // sku = vHolder.lblSku.getText().toString();
//                // checkGuardar.setEnabled(false);
//                final ViewHolder finalv =vHolder;
//
//                ViewHolder finalVHolder = vHolder;
//                vHolder.txtventa.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        finalVHolder.txtventa.setTextColor(getResources().getColor(R.color.negro));
//                    }
//                });
//
//                btnGuardar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        View view;
//                        EditText et = null;
//                        TextView tv;
//                        int listLength = listview.getChildCount();
//                        valueOfEditText = new String[listLength+1];
//                        valueOfTextView = new String[listLength+1];
//                        for (int i = 0; i < listLength; i++) {
//                            String et_otros = lblOtros.getText().toString();
//                            view = listview.getChildAt(i);
//                            et = (EditText) view.findViewById(R.id.txtventa);
//                            tv = (TextView) view.findViewById(R.id.lblSku);
//                            valueOfEditText[i] = et.getText().toString();
//                            valueOfTextView[i] = tv.getText().toString();
//
//                            valueOfEditText[listLength] = et_otros;
//                            valueOfTextView[listLength] = "Otros";
//                        }
//
//                        venta = finalv.txtventa.getText().toString();
//                        sku = finalv.lblSku.getText().toString();
//
//                        int contador_llenos = 0;
//
//                        if (txtCaras!=null && !txtCaras.getText().toString().equals("") && !lblOtros.getText().toString().equals("")) {
//                            if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
//                                for (int i = 0; i < listLength; i++) {
//                                    view = listview.getChildAt(i);
//                                    et = (EditText) view.findViewById(R.id.txtventa);
//                                    if (!et.getText().toString().trim().isEmpty()) {
//                                        contador_llenos++;
//                                    }
//                                }
//
//                                Log.i("CONTADOR_LLENOS",contador_llenos+"");
//                                Log.i("LISTLENGTH",listLength+"");
//
//                                if (contador_llenos==listLength) {
//                                    insertData();
//                                    for (int i = 0; i < listLength; i++) {
//                                        view = listview.getChildAt(i);
//                                        et = (EditText) view.findViewById(R.id.txtventa);
//                                        et.setText("");
//                                    }
//                                    txtCaras.setText("");
//                                    lblOtros.setText("");
//                                }else{
//                                    Toast.makeText(getContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
//                                resultado();
//                            }
//                        }else{
//                            Toast.makeText(getApplicationContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//            //Devolver al ListView la fila creada
//            return convertView;
//        }
//    }

    public void insertData(String razones) {
        try {
       //     if (!razones.equalsIgnoreCase("SELECCIONE")) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                String sector = spCategoria.getSelectedItem().toString();
                String categoria = spSubcategoria.getSelectedItem().toString();
                String brand = spBrand.getSelectedItem().toString();
                // String razones = spRazones.getSelectedItem().toString();
                //String presentacion = spSegmento.getSelectedItem().toString();
                //String contenido= spBrand.getSelectedItem().toString();

                String ctms_percha = txtCaras.getText().toString().trim();
                String otros = lblOtros.getText().toString().trim();

                String local = txtLocal.getText().toString();
                String codigo_fabril = txtCodigoFabril.getText().toString();
                String vendedor_asignado = txtVendedor.getText().toString();

                String image = "";

                if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                    image = getStringImage(bitmapfinal);
                }else{
                    image = "NO_FOTO";
                }

         //       if (!otros.substring(0,1).equals("-")) {
                int contador = 0;
                String mensaje = "";
                do {
                    String skuSelected = "";
                    String cantidad = "";
                    String cantidad_sug = "";
                    String quiebre = "";
                    String sugerido = "";
                    String entrega = "";
                    String obser = "";

                    Log.i("CONTADOR", "" + contador);

                    for (int i = 0; i < valueOfEditText.length; i++) {
                        cantidad = valueOfEditText[0].toString();
                    }

                    for (int i = 0; i < valueOfEditText2.length; i++) {
                        cantidad_sug = valueOfEditText2[0].toString();
                    }

                    for (int i = 0; i < valueOfEditText3.length; i++) {
                        obser = valueOfEditText3[0].toString();
                    }

                    for (int i = 0; i < valueOfSwitch.length; i++) {
                        quiebre = valueOfSwitch[0].toString();
                    }

                    for (int i = 0; i < valueOfSwitch2.length; i++) {
                        sugerido = valueOfSwitch2[0].toString();
                    }

                    for (int i = 0; i < valueOfSpinner.length; i++) {
                        entrega = valueOfSpinner[0].toString();
                    }

                    for (int i = 0; i < valueOfTextView.length; i++) {
                        skuSelected = valueOfTextView[0].toString();
                    }

                    String manufacturer = handler.getManufacturerShare(skuSelected);

                    List<String> list_edittext = new ArrayList<String>(Arrays.asList(valueOfEditText));
                    list_edittext.remove(0);
                    valueOfEditText = list_edittext.toArray(new String[0]);

                    List<String> list_edittext2 = new ArrayList<String>(Arrays.asList(valueOfEditText2));
                    list_edittext2.remove(0);
                    valueOfEditText2 = list_edittext2.toArray(new String[0]);

                    List<String> list_edittext3 = new ArrayList<String>(Arrays.asList(valueOfEditText3));
                    list_edittext3.remove(0);
                    valueOfEditText3 = list_edittext3.toArray(new String[0]);

                    List<String> list_switch = new ArrayList<String>(Arrays.asList(valueOfSwitch));
                    list_switch.remove(0);
                    valueOfSwitch = list_switch.toArray(new String[0]);

                    List<String> list_switch2 = new ArrayList<String>(Arrays.asList(valueOfSwitch2));
                    list_switch2.remove(0);
                    valueOfSwitch2 = list_switch2.toArray(new String[0]);

                    List<String> list_spinner = new ArrayList<String>(Arrays.asList(valueOfSpinner));
                    list_spinner.remove(0);
                    valueOfSpinner = list_spinner.toArray(new String[0]);

                    List<String> list_textview = new ArrayList<String>(Arrays.asList(valueOfTextView));
                    list_textview.remove(0);
                    valueOfTextView = list_textview.toArray(new String[0]);

                    if (!cantidad.trim().isEmpty() || !cantidad_sug.trim().isEmpty() || !obser.trim().isEmpty()) {
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
                        values.put(ContractInsertSugeridos.Columnas.SKU_CODE, skuSelected);
                        values.put(ContractInsertSugeridos.Columnas.QUIEBRE, quiebre);
                        values.put(ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE, cantidad);
                        values.put(ContractInsertSugeridos.Columnas.SUGERIDO, sugerido);
                        values.put(ContractInsertSugeridos.Columnas.CANTIDAD, cantidad_sug);
                        values.put(ContractInsertSugeridos.Columnas.OBSERVACIONES, obser);

                        values.put(Constantes.PENDIENTE_INSERCION, 1);

                        getContentResolver().insert(ContractInsertSugeridos.CONTENT_URI, values);

                        if (VerificarNet.hayConexion(getApplicationContext())) {
                            SyncAdapter.sincronizarAhora(this, true, Constantes.insertSugeridos, null);
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                        }

                        if (contador == 0) {
                            mensaje = "HOLA! Te saluda _*" + user + "*_ de Lucky. *Cliente:* " + format + " - " + punto_venta + "\r\n";
                        }
                        if (contador <= 4) {
                            mensaje = mensaje + "\r\n*Sugerido:* " + skuSelected.replace("&", "-").replace("#", "") + "\r\n*Cantidad:* " + cantidad_sug + " Unidades\r\n*Despacho:* " + entrega + "\r\n";
                            //enviarMensaje(celular, mensaje);
                        }else{
                            Toast.makeText(getApplicationContext(), "Solo es posible enviar hasta 5 sku", Toast.LENGTH_SHORT).show();
                        }
                        contador++;
                    }
                }while(valueOfEditText.length!=0 || valueOfEditText2.length!=0 || valueOfEditText3.length!=0);
                Log.i("MENSAJE", mensaje);
                enviarMensajeWhatsapp(mensaje);
           /*     }else{
                    Toast.makeText(getApplicationContext(), "El valor de otros no puede ser negativo", Toast.LENGTH_SHORT).show();
                    lblOtros.setBackgroundColor(Color.TRANSPARENT);
                }*/

        /*    }else{
                Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
            }*/
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public class CustomAdapterSugeridos extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterSugeridos.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;

        public CustomAdapterSugeridos(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sugeridos_2, parent, false);
            return new ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BasePortafolioProductos mObject = values.get(position);
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
                ((ItemViewHolder) holder).lblSku.setText(mObject.getSku());
            //    ((CustomAdapterSugeridos.ItemViewHolder) holder).txtventa.setText(cargarCaras(codigo_pdv, categoria, subcategoria, mObject.getMarca()));
                ((ItemViewHolder) holder).txt_cantidad_disponible.setTextColor(getResources().getColor(R.color.verde));
                ((ItemViewHolder) holder).txt_cantidad_sugerida.setTextColor(getResources().getColor(R.color.verde));
                ((ItemViewHolder) holder).txt_observaciones.setTextColor(getResources().getColor(R.color.verde));
            }
        }

    /*    private void setItemSelected(int position) {
            TAG.sel(listview.get(position).get(0));
            selected = list.get(position).get(0);
            notifyDataSetChanged();
        }*/

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

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView lblSku;
            public CheckBox chkGuardar;
            public EditText txt_cantidad_disponible;
            public EditText txt_cantidad_sugerida;
            public EditText txt_observaciones;
            public Spinner spEntrega;
            public Switch swQuiebre;
            public Switch swSugerido;
            public EditText txtSugerido;
            public EditText txtObservacion;
            public ImageButton btnCamera;
            public TextView txtFecha;
            public ImageButton btnFechaProd;
            public Context myContext;


            public ItemViewHolder(View itemView) {
                super(itemView);

                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
                txt_cantidad_disponible = (EditText) itemView.findViewById(R.id.txt_cantidad_disponible);
                txt_cantidad_sugerida = (EditText) itemView.findViewById(R.id.txt_cantidad_sugerida);
                txt_observaciones = (EditText) itemView.findViewById(R.id.txt_observaciones);
                spEntrega = (Spinner) itemView.findViewById(R.id.spEntrega);
                swQuiebre = (Switch)itemView.findViewById(R.id.swQuiebre);
                swSugerido = (Switch)itemView.findViewById(R.id.swSugerido);
                btnCamera = (ImageButton) itemView.findViewById(R.id.ibCargarFotoShare);

                if (validaPermisos()) {
                    btnCamera.setEnabled(true);
                }else{
                    btnCamera.setEnabled(false);
                }


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

                txt_observaciones.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        txt_observaciones.setTextColor(getResources().getColor(R.color.negro));
                    }
                });

                txt_cantidad_disponible.setEnabled(false);
                swQuiebre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        // ((ItemViewHolder) holder).myContext = compoundButton.getContext();
                        if (isChecked) {
                            swQuiebre.setText("Quiebre");
                            txt_cantidad_disponible.setEnabled(true);
                            if (swQuiebre.equals("Quiebre")) {
                                txt_cantidad_disponible.setText(venta);
                            }else{
                                txt_cantidad_disponible.setText("");
                            }
                        }else {
                            swQuiebre.setText("Posible Quiebre");
                            txt_cantidad_disponible.setEnabled(true);
                            if (swQuiebre.equals("Posible Quiebre")) {
                                txt_cantidad_disponible.setText("");
                            }else{
                                txt_cantidad_disponible.setText(venta);
                            }
                        }
                    }
                });

                txt_cantidad_sugerida.setEnabled(false);
                swSugerido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        // ((ItemViewHolder) holder).myContext = compoundButton.getContext();
                        if (isChecked) {
                            txt_cantidad_sugerida.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Sugerido: SI", Toast.LENGTH_LONG).show();
                            if (swSugerido.equals("SI")) {
                                txt_cantidad_sugerida.setText(canti_sug);
                            }else{
                                txt_cantidad_sugerida.setText("");
                            }

                        }else {
                            txt_cantidad_sugerida.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Sugerido: NO", Toast.LENGTH_LONG).show();
                            if (swSugerido.equals(isChecked)) {
                                txt_cantidad_sugerida.setText("");
                            } else {
                                txt_cantidad_sugerida.setText(canti_sug);
                            }
                        }
                    }
                });

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view;
                        EditText et = null;
                        EditText cd = null;
                        EditText ob = null;
                        Switch swq = null;
                        Switch sws = null;
                        Spinner te = null;
                        TextView tv;
                        int tiempo_vacio = 0;

                        int listLength = listview.getChildCount();
                        valueOfEditText = new String[listLength];
                        valueOfEditText2 = new String[listLength];
                        valueOfEditText3 = new String[listLength];
                        valueOfSwitch = new String[listLength];
                        valueOfSwitch2 = new String[listLength];
                        valueOfSpinner = new String[listLength];
                        valueOfTextView = new String[listLength];
                  //      Log.i("LIST LENGTH", listLength+"");
                        for (int i = 0; i < listLength; i++) {
                      //      if (i>0) {
                                String q = "Posible Quiebre";
                                String n = "SI";
                                String et_otros = lblOtros.getText().toString();
                    //            Log.i("LENGTH", ""+i);
                                view = listview.getChildAt(i);
                                et = (EditText) view.findViewById(R.id.txt_cantidad_disponible);
                                cd = (EditText) view.findViewById(R.id.txt_cantidad_sugerida);
                                ob = (EditText) view.findViewById(R.id.txt_observaciones);
                                swq = (Switch) view.findViewById(R.id.swQuiebre);
                                sws = (Switch) view.findViewById(R.id.swSugerido);
                                te = (Spinner) view.findViewById(R.id.spEntrega);
                                tv = (TextView) view.findViewById(R.id.lblSku);

                                String cantidad_disponible = et.getText().toString();
                                String cantidad_sugerida = cd.getText().toString();
                                String tiempo_entrega = te.getSelectedItem().toString();

                                valueOfTextView[i] = tv.getText().toString();
                                valueOfEditText[i] = cantidad_disponible;
                                valueOfEditText2[i] = cantidad_sugerida;
                                valueOfEditText3[i] = ob.getText().toString();
                                valueOfSpinner[i] = tiempo_entrega;

                                if (!cantidad_disponible.equals("") || !cantidad_sugerida.equals("")) {
                                    if (tiempo_entrega.contains("Seleccione")) {
                                        tiempo_vacio++;
                                    }
                                }

                                if (!(swq.isChecked())) {
                                    q = "Quiebre";
                                }
                                if (!(sws.isChecked())) {
                                    n =  "NO";
                                }
                                valueOfSwitch[i] = q;
                                valueOfSwitch2[i] = n;
                            //    valueOfEditText[listLength] = et_otros;
                            //    valueOfTextView[listLength] = "Otros";
                         //   }
                        }
                  /*      Log.i("ET LENGTH", valueOfEditText.length+"");
                        Log.i("CD LENGTH", valueOfEditText2.length+"");
                        Log.i("OB LENGTH", valueOfEditText3.length+"");*/

                        venta = txt_cantidad_disponible.getText().toString();
                        canti_sug = txt_cantidad_sugerida.getText().toString();
                        observaciones = txt_observaciones.getText().toString();
                        sku = lblSku.getText().toString();
                        entrega = spEntrega.getSelectedItem().toString();//

                        String razones = spRazones.getSelectedItem().toString();

//                        image = getStringImage(bitmapfinal);

                        int contador_llenos = 0;
                  //      if (!entrega.equalsIgnoreCase("SELECCIONE")) {

                            if (categoria!=null && !categoria.equalsIgnoreCase("")) {
                                if (tiempo_vacio==0) {
                                    insertData(razones);
                                } else {
                                    Toast.makeText(getApplicationContext(),"Debe seleccionar el tiempo de entrega", Toast.LENGTH_LONG).show();
                                }

//                                celular =
////                                Toast.makeText(getApplicationContext(),producto,Toast.LENGTH_LONG).show();
//                                        mensaje="Te saluda "+user+" de Lucky. "+format+"\r\n"+punto_venta+"\r\nSugerido: "+sku+"\r\nCantidad: "+canti_sug;
//                                    Toast.makeText(getApplicationContext(),"Ingrese el número destino",Toast.LENGTH_LONG).show();
//                                } else {
//                                    nivelBateria();
////                                    enviarMensaje(celular,mensaje+battery);
//                                    enviarMensaje(celular,mensaje);
//                                }
               //         if (!razones.equalsIgnoreCase("SELECCIONE")) {

                        /*    if (txtCaras!=null && !txtCaras.getText().toString().equals("") && !lblOtros.getText().toString().equals("")) {
                                if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
                                    for (int i = 0; i < listLength; i++) {
                                        view = listview.getChildAt(i);
                                        et = (EditText) view.findViewById(R.id.txt_cantidad_disponible);
                                        if (!et.getText().toString().trim().isEmpty()) {
                                            contador_llenos++;
                                        }
                                    }

                                    Log.i("CONTADOR_LLENOS",contador_llenos+"");
                                    Log.i("LISTLENGTH",listLength+"");
*/
                               /*     if (contador_llenos==listLength) {
                                        insertData(razones);
                                        for (int i = 0; i < listLength; i++) {
                                            view = listview.getChildAt(i);
                                            et = (EditText) view.findViewById(R.id.txt_cantidad_disponible);
                                            et.setText("");
                                        }
                                     //   txtCaras.setText("");
                                    //    lblOtros.setText("");//
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
                                    }*/
                        /*        }else{
                                    Toast.makeText(getApplicationContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
                                  //  resultado();
                                }*/
                            }else{
                                Toast.makeText(getApplicationContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
                            }
                    /*    }else{
                            Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sku_list = lblSku.getText().toString();
                        cargarImagen();
//                        Toast.makeText(getApplicationContext(), lblSku.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    private void sendMessage(String message) {
        // Creating new intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        // Give your message here
        intent.putExtra(Intent.EXTRA_TEXT, message);
        // Checking whether Whatsapp
        // is installed or not
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Por favor, instala whatsapp primero.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Starting Whatsapp
        startActivity(intent);
    }

    private void enviarMensajeWhatsapp(String messagestr) {
        String phonestr = celular;
        if (!messagestr.isEmpty() && !phonestr.isEmpty()) {
            if (isWhatappInstalled()) {
                if (!phonestr.equals("N/A") || !phonestr.equals("NA") || !phonestr.contains("+")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+"&text="+messagestr));
                    startActivity(i);
                } else {
                    Toast.makeText(this,"Numero de telefono no puede ser: " + phonestr, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, verificar el número de teléfono o el mensaje, podrían estar vacios", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isWhatappInstalled() {
        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    private void enviarMensaje (String numero, String longMessage) {
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mensaje = sms.divideMessage(longMessage);
            sms.sendMultipartTextMessage(numero,null, mensaje,null,null);
           // sms.sendTextMessage(numero,null,mensaje,null,null);
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
