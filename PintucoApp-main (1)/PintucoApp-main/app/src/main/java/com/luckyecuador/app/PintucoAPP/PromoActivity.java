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
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PromoActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    ImageButton btnIniPromo;
    ImageButton btnFinPromo;

    ArrayList<String> array_canal = new ArrayList<>();

    private Spinner spCategoria;
    //private Spinner spBrand;
    private Spinner spTipoPromocion;
    private Spinner spDescripcionPromocion;
    //private Spinner spVigencia;
    private Spinner spSubcategoria;
    private EditText txtMecanica;
    private Spinner spMarca;
    private Spinner spSKU;
    private TextView txtInicioPromo;
    private TextView txtFinPromo;
    private TextView txtAnterior;
    private TextView txtActual;
    private CheckBox chkStock;
    private Button btnGuardar;
    //private ImageButton btnFecha;

    DatabaseHelper handler;

    TextView empty;
    ListView listview;

    //List<String> listProductos;
    ListViewAdapter dataAdapter;

    String id_pdv, categoria, brand, fechaventas, tipo_promocion, vigencia, mecanica, mecanica_generalizada, observaciones, subcategoria, marca;
    private String user,codigo_pdv, punto_venta,fecha,hora,format,canal,subcanal;

    private ImageView imageView;
    private Bitmap bitmapfinal;
    Bitmap bitmap;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Competencia";
    String path;
    ImageButton btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        setToolbar();
        LoadData();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        //startService(new Intent(getApplicationContext(), MyService.class));

        declaracionElementos();

        btnCamera.setOnClickListener(this);
        btnIniPromo.setOnClickListener(this);
        btnFinPromo.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        
        filtrarCategoria();
   //     filtrarTipoPromocion(canal);
        filtrarDescripcionPromocion(canal);

        array_canal.add(canal);
        //spSKU.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, array_canal));

        //filtrarMarca();
        //llenarSpinners();

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
    }

    private void declaracionElementos() {
        spCategoria=(Spinner) findViewById(R.id.spCategoria);
        spSKU =(Spinner) findViewById(R.id.spSKU);
        //spBrand=(Spinner) findViewById(R.id.spBrand);
        spSubcategoria =(Spinner) findViewById(R.id.spSubcategoria);
        spMarca=(Spinner) findViewById(R.id.spMarca);

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        listview = (ListView)findViewById(R.id.lvSKUCode);

     //   spTipoPromocion = (Spinner)findViewById(R.id.spTipoPromocion);
        spDescripcionPromocion = (Spinner)findViewById(R.id.spDescripcionPromocion);
        //spVigencia = (Spinner)dialogView.findViewById(R.id.spVigencia);
        txtMecanica = (EditText) findViewById(R.id.txtMecanica);
        txtInicioPromo = (TextView) findViewById(R.id.txtInicioPromo);
        txtFinPromo = (TextView) findViewById(R.id.txtFinPromo);
        txtAnterior = (TextView) findViewById(R.id.txtPVCanterior);
        txtActual = (TextView) findViewById(R.id.txtPVCactual);
        chkStock = (CheckBox) findViewById(R.id.chkStock);

        btnIniPromo = (ImageButton) findViewById(R.id.btnInicioPromo);
        btnFinPromo = (ImageButton) findViewById(R.id.btnFinPromo);

        imageView = (ImageView) findViewById(R.id.ivFotoExhibiciones);
        btnCamera = (ImageButton) findViewById(R.id.ibCargarFotoExhibiciones);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void filtrarCategoria() {
//        List<String> operadores = handler.getCategoriaPromocion(canal,subcanal);
//        if (operadores.size()==2) {
//            operadores.remove(0);
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategoria.setAdapter(dataAdapter);
//        spCategoria.setOnItemSelectedListener(this);
    }

    /*public void filtrarSubCategoria(String status, String logro, String subcategoria, String presentacion) {
        List<String> operadores = handler.getSector2Promocion(status,logro,subcategoria,presentacion);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSector.setAdapter(dataAdapter);
        spSector.setOnItemSelectedListener(this);
    }*/

    public void filtrarSubcategoria(String categoria) {
//        List<String> operadores = handler.getSubcategoriaPromocion(categoria,canal,subcanal);
//        if (operadores.size()==2) {
//            operadores.remove(0);
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spSubcategoria.setAdapter(dataAdapter);
//        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String categoria, String subcategoria) {
//        List<String> operadores = handler.getMarcaPromocion(categoria,subcategoria,canal,subcanal);
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

    public void filtrarSKU(String categoria, String subcategoria, String marca) {
//        List<String> operadores = handler.getSKUPromocion2(categoria, subcategoria, marca, canal, subcanal);
//        if (operadores.size()==2) {
//            operadores.remove(0);
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spSKU.setAdapter(dataAdapter);
//        spSKU.setOnItemSelectedListener(this);
    }

    public void filtrarTipoPromocion(String canal) {
        List<String> operadores = handler.getTipoPromocion(canal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoPromocion.setAdapter(dataAdapter);
        spTipoPromocion.setOnItemSelectedListener(this);
    }

    public void filtrarDescripcionPromocion(String canal) {
        List<String> operadores = handler.getDescripcionPromocion(canal);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        for(int i=0; i < operadores.size(); i++) {
            if (operadores.get(i).equalsIgnoreCase("NA")) {
                txtMecanica.setEnabled(false);
        //         btnIniPromo.setEnabled(false);
       //          btnFinPromo.setEnabled(false);
       //          chkStock.setEnabled(false);
                txtAnterior.setEnabled(false);
                txtActual.setEnabled(false);
                btnCamera.setEnabled(false);
            }else{
                txtMecanica.setEnabled(true);
        //        btnIniPromo.setEnabled(true);
        //        btnFinPromo.setEnabled(true);
        //        chkStock.setEnabled(true);
                txtAnterior.setEnabled(true);
                txtActual.setEnabled(true);
                btnCamera.setEnabled(true);
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDescripcionPromocion.setAdapter(dataAdapter);
        spDescripcionPromocion.setOnItemSelectedListener(this);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String model = adapterView.getItemAtPosition(i).toString();
        //alertDialog(model);
    }

    public void inicioPromo() {
        final Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog from_dateListener = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                txtInicioPromo.setText(outDate);
            }
        },anio,mes,dia);
        from_dateListener.show();
    }

    public void finPromo() {
        final Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog from_dateListener = new DatePickerDialog(PromoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                txtFinPromo.setText(outDate);
            }
        },anio,mes,dia);
        from_dateListener.show();
    }

    public void insertData(String categoria, String subcategoria, String marca, String canal, String tipo_promocion, String descripcion,
                           String mecanica, String pvc_anterior, String pvc_actual, String sku) {
        try{
            if (descripcion.equalsIgnoreCase("NA")) {
                if (!sku.equalsIgnoreCase("SELECCIONE") &&
                    !tipo_promocion.equalsIgnoreCase("SELECCIONE")) {
                    String foto = "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";
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                    Date currentLocalTime = cal.getTime();
                    DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                    date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                    String fechaser = date.format(currentLocalTime);

                    DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                    hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                    String horaser = hour.format(currentLocalTime);

                    ContentValues values = new ContentValues();

                    String manufacurer = handler.getManufacturerPromocion( subcategoria, marca);

                    values.put(ContractInsertPromocion.Columnas.PHARMA_ID, id_pdv);
                    values.put(ContractInsertPromocion.Columnas.CODIGO, codigo_pdv);
                    values.put(ContractInsertPromocion.Columnas.USUARIO, user);
                    values.put(ContractInsertPromocion.Columnas.SUPERVISOR, punto_venta);
                    values.put(ContractInsertPromocion.Columnas.FECHA, fechaser);
                    values.put(ContractInsertPromocion.Columnas.HORA, horaser);
                    values.put(ContractInsertPromocion.Columnas.CATEGORIA, categoria);
                    values.put(ContractInsertPromocion.Columnas.SUBCATEGORIA, subcategoria);
                    values.put(ContractInsertPromocion.Columnas.MARCA, marca);
                    values.put(ContractInsertPromocion.Columnas.CANAL, canal);
                    values.put(ContractInsertPromocion.Columnas.TIPO_PROMOCION, tipo_promocion);
                    values.put(ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION, descripcion);
                    values.put(ContractInsertPromocion.Columnas.MECANICA, mecanica);
                  //  values.put(ContractInsertPromocion.Columnas.INI_PROMO, ini_promo);
                   // values.put(ContractInsertPromocion.Columnas.FIN_PROMO, fin_promo);
                  //  values.put(ContractInsertPromocion.Columnas.AGOTAR_STOCK, stock);
                    values.put(ContractInsertPromocion.Columnas.PVC_ANTERIOR, pvc_anterior);
                    values.put(ContractInsertPromocion.Columnas.PVC_ACTUAL, pvc_actual);
                    values.put(ContractInsertPromocion.Columnas.FOTO, foto);
                    values.put(ContractInsertPromocion.Columnas.MANUFACTURER, manufacurer);
                    values.put(ContractInsertPromocion.Columnas.SKU, sku);
                    values.put(Constantes.PENDIENTE_INSERCION, 1);

                    getContentResolver().insert(ContractInsertPromocion.CONTENT_URI, values);

                    if (VerificarNet.hayConexion(getApplicationContext())) {
                        SyncAdapter.sincronizarAhora(this, true, Constantes.insertPromocion, null);
                        Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }else{
                if (!mecanica.equals("") && mecanica !=null && !mecanica.trim().isEmpty() &&
                    !pvc_anterior.equals("") && pvc_anterior !=null && !pvc_anterior.trim().isEmpty() &&
                    !pvc_actual.equals("") && pvc_actual !=null && !pvc_actual.trim().isEmpty() &&
                    !sku.equalsIgnoreCase("SELECCIONE") &&
                    !tipo_promocion.equalsIgnoreCase("SELECCIONE")) {
                    String image = "";
                    if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                        image = getStringImage(bitmapfinal);
                    } else {
                        Toast.makeText(this, "Toma una foto para enviar datos", Toast.LENGTH_LONG).show();
                    }
                    if (image != null && !image.equals("")) {
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                        Date currentLocalTime = cal.getTime();
                        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                        String fechaser = date.format(currentLocalTime);

                        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                        String horaser = hour.format(currentLocalTime);

                        ContentValues values = new ContentValues();

                        String manufacurer = handler.getManufacturerPromocion( subcategoria, marca);

                        values.put(ContractInsertPromocion.Columnas.PHARMA_ID, id_pdv);
                        values.put(ContractInsertPromocion.Columnas.CODIGO, codigo_pdv);
                        values.put(ContractInsertPromocion.Columnas.USUARIO, user);
                        values.put(ContractInsertPromocion.Columnas.SUPERVISOR, punto_venta);
                        values.put(ContractInsertPromocion.Columnas.FECHA, fechaser);
                        values.put(ContractInsertPromocion.Columnas.HORA, horaser);
                        values.put(ContractInsertPromocion.Columnas.CATEGORIA, categoria);
                        values.put(ContractInsertPromocion.Columnas.SUBCATEGORIA, subcategoria);
                        values.put(ContractInsertPromocion.Columnas.MARCA, marca);
                        values.put(ContractInsertPromocion.Columnas.CANAL, canal);
                        values.put(ContractInsertPromocion.Columnas.TIPO_PROMOCION, tipo_promocion);
                        values.put(ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION, descripcion);
                        values.put(ContractInsertPromocion.Columnas.MECANICA, mecanica);
                     //   values.put(ContractInsertPromocion.Columnas.INI_PROMO, ini_promo);
                     //   values.put(ContractInsertPromocion.Columnas.FIN_PROMO, fin_promo);
                     //   values.put(ContractInsertPromocion.Columnas.AGOTAR_STOCK, stock);
                        values.put(ContractInsertPromocion.Columnas.PVC_ANTERIOR, pvc_anterior);
                        values.put(ContractInsertPromocion.Columnas.PVC_ACTUAL, pvc_actual);
                        values.put(ContractInsertPromocion.Columnas.FOTO, image);
                        values.put(ContractInsertPromocion.Columnas.MANUFACTURER, manufacurer);
                        values.put(ContractInsertPromocion.Columnas.SKU, sku);
                        values.put(Constantes.PENDIENTE_INSERCION, 1);

                        getContentResolver().insert(ContractInsertPromocion.CONTENT_URI, values);

                        if (VerificarNet.hayConexion(getApplicationContext())) {
                            SyncAdapter.sincronizarAhora(this, true, Constantes.insertPromocion, null);
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
                }
            }

            //finish();

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100) {
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PromoActivity.this);
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PromoActivity.this);
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

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PromoActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        /*Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView==spCategoria) {
            try{
                categoria=adapterView.getItemAtPosition(i).toString();
                filtrarSubcategoria(categoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSubcategoria) {
            try{
                subcategoria =adapterView.getItemAtPosition(i).toString();
                filtrarMarca(categoria,subcategoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spMarca) {
            try{
                marca =adapterView.getItemAtPosition(i).toString();
                filtrarSKU(categoria,subcategoria,marca);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        /*
        if (adapterView==spTipoPromocion) {
            try{
                tipo_promocion=adapterView.getItemAtPosition(i).toString();
               // filtrarDescripcionPromocion(canal, tipo_promocion);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

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

    @Override
    public void onClick(View view) {
        if (view == btnCamera) {
            cargarImagen();
        }
        if (view == btnIniPromo) {
            inicioPromo();
        }
        if (view == btnFinPromo) {
            finPromo();
        }
        if (view == btnGuardar) {
            guardar();
        }
    }

    private void guardar() {
       // String stock;

        /*
        if (chkStock.isChecked()) {
            stock = "SI";
        }else{
            stock = "NO";
        }*/

        String categoria = spCategoria.getSelectedItem().toString();
        String subcategoria = spSubcategoria.getSelectedItem().toString();
        String marca = spMarca.getSelectedItem().toString();
      //  String tipo_promocion = spTipoPromocion.getSelectedItem().toString();
        String descricion_promocion = spDescripcionPromocion.getSelectedItem().toString();
        String mecanica = txtMecanica.getText().toString().trim();
      //  String ini_promo = txtInicioPromo.getText().toString();
      //  String fin_promo = txtFinPromo.getText().toString();
        String pvc_anterior = txtAnterior.getText().toString().trim();
        String pvc_actual = txtActual.getText().toString().trim();
        String sku = spSKU.getSelectedItem().toString().trim();

        //if (//!num_items.equals("") && num_items !=null && !num_items.trim().isEmpty() &&
            //!ini_promo.equals("") && ini_promo !=null && !ini_promo.trim().isEmpty() &&
            //!fin_promo.equals("") && fin_promo !=null && !fin_promo.trim().isEmpty() &&
            //!subcategoria.equalsIgnoreCase("SELECCIONE") &&
            //!tipo_promocion.equalsIgnoreCase("SELECCIONE") //&& !mecanica_generalizada.equalsIgnoreCase("SELECCIONE") &&
            /*!mecanica.equalsIgnoreCase("SELECCIONE")) {*/
            insertData(categoria, subcategoria, marca, canal, tipo_promocion, descricion_promocion, mecanica, pvc_anterior, pvc_actual, sku);
        /*}else{
            Toast.makeText(getApplicationContext(),"Ingrese todos los campos de la Promoción",Toast.LENGTH_LONG).show();
        }*/
    }
}
