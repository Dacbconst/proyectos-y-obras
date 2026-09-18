package com.luckyecuador.app.PintucoAPP;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
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

public class ShareActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private EditText txtCaras;
    private TextView lblOtros;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spRazones;
    private Spinner spSegmento;
    private Button btnGuardar;
    //private Spinner spBrand;
    String[] valueOfEditText;
    String[] valueOfTextView;
    CustomAdapterShare dataAdapter;
    TextView empty;
    RecyclerView listview;

    String user,
    codigo_pdv,
    punto_venta,
    fecha,
    hora,
    sku_list;

    ArrayList<BasePortafolioProductos> listProductos;
    String id_pdv, subcategoria, presentacion, venta, categoria, sku, canal, subcanal;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setToolbar();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);


        LoadData();
        txtCaras=(EditText) findViewById(R.id.txtCaras);
        lblOtros=(TextView) findViewById(R.id.lblOtros);
        spCategoria =(Spinner) findViewById(R.id.spSector);
        spSubcategoria =(Spinner) findViewById(R.id.spCategoria);
        spRazones =(Spinner) findViewById(R.id.spRazon);
        spSegmento =(Spinner) findViewById(R.id.spSegmento);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        //spBrand =(Spinner) findViewById(R.id.spCategoria);
        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        btnCalculo=(ImageView) findViewById(R.id.btnCalculo);
        imageView = (ImageView) findViewById(R.id.ivFotoShare);
        btnCamera=(ImageButton) findViewById(R.id.ibCargarFotoShare);

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
                        resta(resta);
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

        listview = (RecyclerView) findViewById(android.R.id.list);
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

    public void resultado() {
        int resta = 0;
        try{
            if (!txtCaras.getText().toString().equals("")) {
                int cmTotal = Integer.parseInt(String.valueOf(txtCaras.getText()));
                resta = cmTotal - sum();
                resta(resta);
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public int verificacion_resultado() {
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
    }

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
    }

    public void resta(int total) {
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
    }

    public void filtrarCategoria() {
        List<String> operadores = handler.getSectorShare("");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoria(String sector) {
        List<String> operadores = handler.getCategoriaShare(sector);
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

    public void filtrarSegmento(String sector, String categoria) {
        List<String> operadores = handler.getSegmentoShare(sector, categoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
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
                cargarTotal(codigo_pdv, categoria, subcategoria);
                cargarOtros(codigo_pdv, categoria, subcategoria);
                showListView(categoria, subcategoria, canal, subcanal);
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

    public void showListView(String sector ,String categoria, String canal, String subcanal) {


        List<String> marcas_blancas = handler.getMarcasBlancas();
        if (!canal.equals("MAYORISTA")){
            marcas_blancas.clear();
            marcas_blancas.add("-");
        }



        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal, marcas_blancas,"");

        dataAdapter = new CustomAdapterShare(this, listProductos);
        if (dataAdapter.getItemCount() != 0) {
            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShareActivity.this);

            listview.setLayoutManager(linearLayoutManager);
            listview.setHasFixedSize(true);
            CustomAdapterShare customAdapter = new CustomAdapterShare(this.getApplicationContext(),listProductos);
            listview.setAdapter(customAdapter);

            dataAdapter = (CustomAdapterShare) listview.getAdapter();
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
            if (!razones.equalsIgnoreCase("SELECCIONE")) {
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
               // String razones = spRazones.getSelectedItem().toString();
                //String presentacion = spSegmento.getSelectedItem().toString();
                //String contenido= spBrand.getSelectedItem().toString();

                String ctms_percha = txtCaras.getText().toString().trim();
                String otros = lblOtros.getText().toString().trim();

                String image = "";

                if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                    image = getStringImage(bitmapfinal);
                }else{
                    image = "NO_FOTO";
                }

                if (!otros.substring(0,1).equals("-")) {
                    do{
                        String skuSelected = "";
                        String cantidad = "";

                        for (int i = 0; i < valueOfEditText.length; i++) {
                            cantidad = valueOfEditText[0].toString();
                        }

                        for (int i = 0; i < valueOfTextView.length; i++) {
                            skuSelected = valueOfTextView[0].toString();
                        }

                        String manufacturer = handler.getManufacturerShare(skuSelected);

                        List<String> list_edittext = new ArrayList<String>(Arrays.asList(valueOfEditText));
                        list_edittext.remove(0);
                        valueOfEditText = list_edittext.toArray(new String[0]);

                        List<String> list_textview = new ArrayList<String>(Arrays.asList(valueOfTextView));
                        list_textview.remove(0);
                        valueOfTextView = list_textview.toArray(new String[0]);

                        ContentValues values = new ContentValues();

                        values.put(ContractInsertShare.Columnas.PHARMA_ID, id_pdv);
                        values.put(ContractInsertShare.Columnas.CODIGO, codigo_pdv);
                        values.put(ContractInsertShare.Columnas.USUARIO, user);
                        values.put(ContractInsertShare.Columnas.SUPERVISOR, punto_venta);
                        values.put(ContractInsertShare.Columnas.FECHA, fechaser);
                        values.put(ContractInsertShare.Columnas.HORA, horaser);
                        values.put(ContractInsertShare.Columnas.SECTOR, categoria);
                        values.put(ContractInsertShare.Columnas.CATEGORIA, subcategoria);
                        values.put(ContractInsertShare.Columnas.RAZONES, razones);
                        values.put(ContractInsertShare.Columnas.SEGMENTO, "");
                        values.put(ContractInsertShare.Columnas.MARCA_SELECCIONADA, skuSelected);
                        values.put(ContractInsertShare.Columnas.BRAND, skuSelected);
                        values.put(ContractInsertShare.Columnas.CTMS_PERCHA, ctms_percha);
                        values.put(ContractInsertShare.Columnas.CTMS_MARCA, cantidad);
                        values.put(ContractInsertShare.Columnas.OTROS, otros);
                        values.put(ContractInsertShare.Columnas.FOTO, image);
                        values.put(ContractInsertShare.Columnas.MANUFACTURER, manufacturer);

                        values.put(Constantes.PENDIENTE_INSERCION, 1);

                        getContentResolver().insert(ContractInsertShare.CONTENT_URI, values);

                        if (VerificarNet.hayConexion(getApplicationContext())) {
                            SyncAdapter.sincronizarAhora(this, true, Constantes.insertShare, null);
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                        }
                    }while(valueOfEditText.length!=0);
                }else{
                    Toast.makeText(getApplicationContext(), "El valor de otros no puede ser negativo", Toast.LENGTH_SHORT).show();
                    lblOtros.setBackgroundColor(Color.TRANSPARENT);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public class CustomAdapterShare extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterShare.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;

        public CustomAdapterShare(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_HEADER) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
//                return new HeaderViewHolder(layoutView);
//            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_option_share, parent, false);
                return new ItemViewHolder(layoutView);
//            }
//            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BasePortafolioProductos mObject = values.get(position);
            /*if (holder instanceof HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle.setText("SKU");
                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle1.setText("STOCK ACTUAL");
                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle2.setText("SUGERIDO");
//                ((HeaderViewHolder) holder).headerTitle3.setText("CAUSAL SUGERIDO");
                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle4.setText("OBSERVACION");
                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle5.setText("CADUCIDAD");
            }else */if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).lblSku.setText(mObject.getMarca());
                ((ItemViewHolder) holder).txtventa.setText(cargarCaras(codigo_pdv, categoria, subcategoria, mObject.getMarca()));
                ((ItemViewHolder) holder).txtventa.setTextColor(getResources().getColor(R.color.verde));

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
            public EditText txtventa;
            public EditText txtSugerido;
            public EditText txtObservacion;
            public ImageButton btnCamera;
            public TextView txtFecha;
            public ImageButton btnFechaProd;

            public ItemViewHolder(View itemView) {
                super(itemView);

                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
                txtventa = (EditText) itemView.findViewById(R.id.txtventa);
                btnCamera = (ImageButton) itemView.findViewById(R.id.ibCargarFotoShare);

                if (validaPermisos()) {
                    btnCamera.setEnabled(true);
                }else{
                    btnCamera.setEnabled(false);
                }

                txtventa.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        txtventa.setTextColor(getResources().getColor(R.color.negro));
                    }
                });

                btnGuardar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        View view;
                        EditText et = null;
                        TextView tv;
                        int listLength = listview.getChildCount();
                        valueOfEditText = new String[listLength+1];
                        valueOfTextView = new String[listLength+1];
                        for (int i = 0; i < listLength; i++) {
                            String et_otros = lblOtros.getText().toString();
                            view = listview.getChildAt(i);
                            et = (EditText) view.findViewById(R.id.txtventa);
                            tv = (TextView) view.findViewById(R.id.lblSku);
                            valueOfEditText[i] = et.getText().toString();
                            valueOfTextView[i] = tv.getText().toString();

                            valueOfEditText[listLength] = et_otros;
                            valueOfTextView[listLength] = "Otros";
                        }

                        venta = txtventa.getText().toString();
                        sku = lblSku.getText().toString();
                        String razones = spRazones.getSelectedItem().toString();

//                        image = getStringImage(bitmapfinal);

                        int contador_llenos = 0;
                        if (!razones.equalsIgnoreCase("SELECCIONE")) {

                        if (txtCaras!=null && !txtCaras.getText().toString().equals("") && !lblOtros.getText().toString().equals("")) {
                            if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
                                for (int i = 0; i < listLength; i++) {
                                    view = listview.getChildAt(i);
                                    et = (EditText) view.findViewById(R.id.txtventa);
                                    if (!et.getText().toString().trim().isEmpty()) {
                                        contador_llenos++;
                                    }
                                }

                                Log.i("CONTADOR_LLENOS",contador_llenos+"");
                                Log.i("LISTLENGTH",listLength+"");

                                if (contador_llenos==listLength) {
                                    insertData(razones);
                                    for (int i = 0; i < listLength; i++) {
                                        view = listview.getChildAt(i);
                                        et = (EditText) view.findViewById(R.id.txtventa);
                                        et.setText("");
                                    }
                                    txtCaras.setText("");
                                    lblOtros.setText("");
                                }else{
                                    Toast.makeText(getApplicationContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
                                resultado();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
                        }
                        }else{
                            Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
                        }
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
}