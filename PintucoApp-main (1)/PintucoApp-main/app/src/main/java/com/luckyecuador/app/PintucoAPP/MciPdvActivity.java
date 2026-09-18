package com.luckyecuador.app.PintucoAPP;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MciPdvActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    //BASE SQLITE
    DatabaseHelper handler;

    //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    private long lengthbmp;
    //Photo Camera
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"ExhibicionAntDesp";
    String path;

    //private EditText txtCodigo;
    private EditText txtComentario;
    private Spinner spExhibicion;
    private Spinner spLocal;
    private Button btnCamera;
    private Button btnGallery;
    private Button btnSave;

    private String fecha, hora, user, id_pdv;

    String codigo, local, exhibicion, comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mci_pdv);
        setToolbar();

        LoadData();

        //startService(new Intent(getApplicationContext(), MyService.class));

        imageView = (ImageView)findViewById(R.id.imageView);
        btnCamera = (Button) findViewById(R.id.btnCameraLocal);
        btnGallery= (Button) findViewById(R.id.btnGallery);
        btnSave= (Button) findViewById(R.id.btnSave);

        //txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtComentario = (EditText) findViewById(R.id.txtComentario);

        txtComentario.setSingleLine(false);  //add this
        txtComentario.setLines(4);
        txtComentario.setMaxLines(5);
        txtComentario.setGravity(Gravity.LEFT | Gravity.TOP);
        txtComentario.setHorizontalScrollBarEnabled(false); //this

        //txtCodigo.addTextChangedListener(this);

        spExhibicion = (Spinner) findViewById(R.id.spExhibicion);
        spLocal = (Spinner) findViewById(R.id.spLocal);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        //filtrarLocal("5001470");

        validaPermisos();

        filtrarLocal(codigo);
        filtrarCausal();
    }

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
            (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) &&
            (checkSelfPermission(ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
            (checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
            (checkSelfPermission(INTERNET)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
            (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
            (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) ||
            (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) ||
            (shouldShowRequestPermissionRationale(INTERNET))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,INTERNET},100);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(MciPdvActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,INTERNET},100);
            }
        });
        dialogo.show();
    }

    /*
     *   TOOLBAR
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {
        /*if (view == btnGallery) {
            showFileChooser();
        }*/
        if (view == btnCamera) {
            cargarImagen();
        }
        if (view == btnSave) {
            enviarDatos();
        }
    }

    /**/
    public void enviarDatos() {
        obtenerFecha();
        if (imageView != null && imageView.getDrawable() != null) {
            if (esFormularioValido()) {
                String image = getStringImage(bitmapfinal);

                obtenervaloresseleccinados();

                BasePharmaValue pdv = handler.getPdv(codigo);

                ContentValues values = new ContentValues();

                values.put(ContractInsertMCIPdv.Columnas.PHARMA_ID, pdv.getPos_id());
                values.put(ContractInsertMCIPdv.Columnas.CODIGO, codigo);
                values.put(ContractInsertMCIPdv.Columnas.CANAL, pdv.getChannel());
                values.put(ContractInsertMCIPdv.Columnas.NOMBRE_COMERCIAL, pdv.getCustomer_owner());
                values.put(ContractInsertMCIPdv.Columnas.LOCAL, pdv.getPos_name());
                values.put(ContractInsertMCIPdv.Columnas.REGION, pdv.getRegion());
                values.put(ContractInsertMCIPdv.Columnas.PROVINCIA, pdv.getProvince());
                values.put(ContractInsertMCIPdv.Columnas.CIUDAD, pdv.getCity());
                values.put(ContractInsertMCIPdv.Columnas.ZONA, pdv.getZone());
                values.put(ContractInsertMCIPdv.Columnas.DIRECCION, pdv.getAddress());
                values.put(ContractInsertMCIPdv.Columnas.SUPERVISOR, pdv.getSupervisor());
                values.put(ContractInsertMCIPdv.Columnas.MERCADERISTA, pdv.getMercaderista());
                values.put(ContractInsertMCIPdv.Columnas.USUARIO, user);
                values.put(ContractInsertMCIPdv.Columnas.LATITUD, pdv.getLatitud());
                values.put(ContractInsertMCIPdv.Columnas.LONGITUD, pdv.getLongitud());
                values.put(ContractInsertMCIPdv.Columnas.TERRITORIO, pdv.getKam());
                values.put(ContractInsertMCIPdv.Columnas.ZONA_TERRITORIO, pdv.getTipo());
                values.put(ContractInsertMCIPdv.Columnas.CAUSAL, exhibicion);
                values.put(ContractInsertMCIPdv.Columnas.OBSERVACIONES, comentario);
                values.put(ContractInsertMCIPdv.Columnas.FOTO, image);
                values.put(ContractInsertMCIPdv.Columnas.FECHA, fecha);
                values.put(ContractInsertMCIPdv.Columnas.HORA, hora);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertMCIPdv.CONTENT_URI, values);
                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertMci, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                    vaciarCampos();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                    vaciarCampos();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean esFormularioValido() {
        if (spExhibicion.getSelectedItem().toString().equalsIgnoreCase("SELECCIONE")) {
            Toast.makeText(getApplicationContext(),"Debe seleccionar una causal", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**/
    public void vaciarCampos() {
        //txtCodigo.setText("");
        txtComentario.setText("");
        spExhibicion.setSelection(0);
        imageView.setImageResource(android.R.color.transparent);
    }


    public void obtenervaloresseleccinados() {
        exhibicion = spExhibicion.getSelectedItem().toString();
        local = spLocal.getSelectedItem().toString();
        comentario = txtComentario.getText().toString();
        //codigo = txtCodigo.getText().toString();
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(MciPdvActivity.this);
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
            Point point = new Point(200, 200);
            Bitmap watermark = applyWaterMarkEffect(bitmap, "Water mark text", 20, 20, Color.GREEN, 80, 50, true);
            Bitmap watermark2 = mark(bitmap, "Water mark text", point, Color.GREEN, 80, 50, true);
            Bitmap watermark3 = mark(bitmap, "TEST LUCKY ECUADOR");
            int mheight = (int) ( watermark3.getHeight() * (1024.0 / watermark3.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(watermark3, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("AA000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.25));
        canvas.drawRect(left, top,right,bottom,bgPaint);

        canvas.drawText(watermark, 10 , h-15, paint);

        return result;
    }


    public static Bitmap mark(Bitmap src, String watermark, Point location, int color, int alpha, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);
        canvas.drawText(watermark, location.x, location.y, paint);

        return result;
    }

    public Bitmap applyWaterMarkEffect(Bitmap src, String watermark, int x, int y, int color, int alpha, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);
        canvas.drawText(watermark, x, y, paint);

        return result;
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        codigo = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        Log.i("USER FOTO ACTIVITY:",user);
    }

    public void obtenerFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        hora = hour.format(currentLocalTime);
//            return localTime;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //codigo = txtCodigo.getText().toString().toUpperCase().trim();
        filtrarLocal(codigo);
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    public void filtrarLocal(String codigo) {
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        List<String> operadores = handler.getNombreCom(codigo);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocal.setAdapter(dataAdapter);
        spLocal.setOnItemSelectedListener(this);
    }

    public void filtrarCausal() {
        List<String> operadores = handler.getCausalMCI();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExhibicion.setAdapter(dataAdapter);
        spExhibicion.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}