package com.luckyecuador.app.PintucoAPP;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_DISTANCE;
import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_INTERVAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.EvidenciaMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class    FotograficoPdvActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageButton img;
    public static ImageView imageView;
    public static ImageView ivFoto2;

    static DatabaseHelper handler;

    static String user, supervisor, codigo, pdv_selected = "SELECCIONE", dir="", imageUrl = "";

    static ProgressDialog progressDialog;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private  static boolean falta_salida;

    public static Double latitud = 0.0, longitud = 0.0;

    public static Geocoder geocoder;

    public static List<Address> addresses;

    static String image;
    private static Bitmap scaled;  //se define aqui para usarla en varias funciones
    private static String DIRECTORIO;
    private static String USUARIO;
    private static String FILE_NAME;
    private static String fechaser;
    private static String horaser;
    private static Uri uriOriginal;

    Button btnGuardar;
    LinearLayout linearPdv;

    static Spinner spPdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografico_pdv);
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        Log.i("FOTOGRAFICO ACTIVITY", "ONCREATE");
        img = (ImageButton) findViewById(R.id.btnCamera);
        imageView = (ImageView) findViewById(R.id.imagen);
        ivFoto2 = (ImageView)findViewById(R.id.ivFoto2);
        spPdv = (Spinner) findViewById(R.id.spPdv);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        linearPdv = (LinearLayout) findViewById(R.id.linearPdv);

        LoadData();

        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    if (latitud != null && longitud != null) {

                        //Setear los EditText
                        if (!latitud.equals(location.getLatitude()) && !longitud.equals(location.getLongitude())) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();

                            try {
                                if (VerificarNet.hayConexion(getApplicationContext())) {
                                    addresses = geocoder.getFromLocation(latitud, longitud, 1);
                                    if(latitud != 0 && longitud != 0){
                                        if(addresses.size()>0){
                                            dir = ""+ addresses.get(0).getAddressLine(0);
                                            //direccion.setText(dir);
                                        }
                                    }
                                } else {
                                        Toast.makeText(getApplicationContext(), Mensajes.NO_RED, Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception ex){
                                Log.i("ERROR",""+ex.getMessage());
                            }
                        }

                    }

                    locationManager.removeUpdates(locationListener);
//                locationManager = null;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Si el GPS esta deshabilitado, abrir la ventana de activacion del GPS en el dispositivo.
                Intent newIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(newIntent);
            }
        };


        obtenerCoordenadas();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerCoordenadas();
                if (!falta_salida){
                    pdv_selected = spPdv.getSelectedItem().toString();
                }

                //if (latitud != 0 && longitud != 0){

                    if (!pdv_selected.equalsIgnoreCase("SELECCIONE")){
                        /*Display display = getWindowManager().getDefaultDisplay();
                    DisplayMetrics displayMetrics = new DisplayMetrics();

                    display.getMetrics(displayMetrics);
                    float density = getResources().getDisplayMetrics().density;

                     float dpHeight = displayMetrics.heightPixels / density;
                     float dpWidth = (float) (displayMetrics.widthPixels / 1.85);
                     dpHeight = (float) (dpHeight-(dpHeight*.3));
                     //dpWidth = (float) (dpWidth);

                     Log.i("dimensiones2", "h:" + dpHeight+ " w:"+dpWidth);*/
                        /*int w = (int) (1440/2.5);
                    int h = (int) (1920/3.5);

                    String tamano = "400";
                    String imageUrl22 = "https://maps.googleapis.com/maps/api/staticmap?"+
                            "center="+ latitud +","+ longitud +
                            "&zoom=19"+
                            "&size="+tamano+"x"+tamano+
                            "&scale=2"+
                            "&markers=size:big|color:red|"+ latitud +","+ longitud +
                            "&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";

                    Log.i("url mapstatic", imageUrl22);
                    Picasso.get().load(imageUrl22).resize(w, h).into(ivFoto2);*/

                        Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                        intent.putExtra("activity","foto_pdv");
                        startActivity(intent);
                    } else {
                        Toast.makeText(FotograficoPdvActivity.this, "Seleccione un Local", Toast.LENGTH_SHORT).show();
                    }



                /*} else {
                    Toast.makeText(FotograficoPdvActivity.this, "No se puede obtener las coordenadas, espere un momento o reinicie su GPS", Toast.LENGTH_LONG).show();
                }*/




            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!falta_salida){ // para fotografico fuera del pdv
                    pdv_selected = spPdv.getSelectedItem().toString();
                }

                if(esFormularioValido()){

                    /*int n = Resources.getSystem().getDisplayMetrics().widthPixels;
                    int size = 45;
                    if(n < 1080){
                        size = 35;
                    }

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                    Date currentLocalTime = cal.getTime();
                    DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                    date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                    String fechaser = date.format(currentLocalTime);

                    DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                    hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                    String horaser = hour.format(currentLocalTime);


                    String ciudad = "Ciudad: " + handler.getCityPdvByName(spPdv.getSelectedItem().toString());
                    String local = "Local: " + spPdv.getSelectedItem().toString();
                    String usuarios = "Usuario: " + user;
                    String direccion = "" + dir;
                    String coordenadas = "coordenadas: " + latitud + ", " + longitud;
                    String fechaHora = "Fecha y hora: " + fechaser + " " + horaser;

                    Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    EvidenciaMark im = new EvidenciaMark();
                    Bitmap watermark = im.markSinMapa(temporal, ciudad, local, usuarios, coordenadas, direccion, fechaHora, Color.YELLOW, 100, size,false);
                    int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
                    imageView.setImageBitmap(scaled);
                    image = getStringImage(scaled);*/

                    insertData();
                }
            }
        });


        filtrarPdvs();
    }



    public static void scaleImageUri(Uri uri, Drawable drawable){
        uriOriginal = uri;
        imageView.setImageURI(uri);


        Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        int w = temporal.getWidth();
        int h = temporal.getHeight();


        Log.i("BITMAP FOTOGRAFICO", String.valueOf(imageView.getDrawable()));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fechaser = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        horaser = hour.format(currentLocalTime);

        if (!falta_salida){ // para fotografico fuera del pdv
            pdv_selected = spPdv.getSelectedItem().toString();
        }

        String ciudad = "Ciudad: " + handler.getCityPdvByName(pdv_selected);
        String local = "Local: " + pdv_selected;
        String usuarios = "Usuario: " + user;
        String direccion = "" + dir;
        String coordenadas = latitud + ", " + longitud;
        String fechaHora = "Fecha y hora: " + fechaser + " " + horaser;



        Log.i(TAG, "medidas, w:"+ivFoto2.getWidth()+" h:"+ivFoto2.getHeight());

        /*String tamano = "400";
        String imageUrl22 = "https://maps.googleapis.com/maps/api/staticmap?"+
                "center="+ latitud +","+ longitud +
                "&zoom=19"+
                "&size="+tamano+"x"+tamano+
                "&scale=2"+
                "&markers=size:big|color:red|"+ latitud +","+ longitud +
                "&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";

        Log.i("url mapstatic", imageUrl22);
        Picasso.get().load(imageUrl22).resize(400, 400).into(ivFoto2);


        try {
            Thread.sleep(5000);

        } catch (Exception e){
            Log.i("no se pudo", e.getMessage());
        }*/



        EvidenciaMark im = new EvidenciaMark();
        final Bitmap[] watermark = new Bitmap[1];
        im.mark2(latitud, longitud, temporal, ciudad, local, usuarios, fechaHora, coordenadas, direccion, Color.YELLOW, 100, 100, false, ivFoto2, drawable, new EvidenciaMark.BitmapCallback() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                watermark[0] = bitmap;

                int mheight = (int) (watermark[0].getHeight() * (1024.0 / watermark[0].getWidth()));
                scaled = Bitmap.createScaledBitmap(watermark[0], 1024, mheight, true);

                imageView.setImageBitmap(scaled);
                image = getStringImage(scaled);
            }
        });



        /*int w = temporal.getWidth();
        int h = temporal.getHeight();
        float top = (float) (h-(h*.8));//TAMAÑO DEL CONTENEDOR DE INFO
        float limite = (float) (w-(w*.8));

        Log.i("dimensionres", "w:"+limite+" h:"+top);



        try{
            Thread.sleep(3000);
        } catch (Exception e){
            Log.i("excepcion", e.getMessage());
        }

        EvidenciaMark im2 = new EvidenciaMark();
        Bitmap watermark2 = im2.mark3(watermark, ciudad, local, usuarios, fechaHora, coordenadas, direccion, Color.YELLOW, 100, 100,false, ivFoto2);*/



//        scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
//
//        imageView.setImageBitmap(scaled);

        /////
//        int w = mheight;
//        Log.i("la W", w+"");
//        double tamano_ajustado = (w/20);
//        String tamano = String.valueOf(tamano_ajustado);
//        String[] split = tamano.split("\\.");
//        tamano = split[0];
//        Log.i("tamaño", tamano);
//        String imageUrl22 = "https://maps.googleapis.com/maps/api/staticmap?"+
//                "center="+ latitud +","+ longitud +
//                "&zoom=17.75"+
//                "&size="+tamano+"x"+tamano+
//                "&scale=2"+
//                "&markers=size:big|color:red|"+ latitud +","+ longitud +
//                "&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";
//
//        Log.i("url mapstatic", imageUrl22);
//        Picasso.get().load(imageUrl22).into(ivFoto2);
//
//        try{
//            Thread.sleep(4000);
//        } catch (Exception e){
//
//        }
//
//        Log.i("MEDIDAD", "w: "+ ivFoto2.getWidth()+" H:"+ivFoto2.getHeight());
//
//        Bitmap watermark2 = im.mark3(watermark, ciudad, local, usuarios, fechaHora, coordenadas, direccion, Color.YELLOW, 100, 100,false, ivFoto2);

        /*int mheight2 = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
        scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight2, true);*/

    }


    private void obtenerCoordenadas() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    public void LoadData() {
        Log.i("FOTOGRAFICO ACTIVITY", "LOADDATA");

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);
        codigo = sharedPreferences.getString(Constantes.CODIGO,Constantes.NODATA);
        falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA,false);
        //usuario = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        Log.i("FALTA SALIDA FOTOGRAFICO", falta_salida+"");
        if (falta_salida){
            linearPdv.setVisibility(View.GONE);
            pdv_selected = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
            Log.i("FOTOGRAFICO DENTRO DE PDV", "o " + pdv_selected);
        }
    }

    public void insertData(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        String id_pdv = handler.getIdPdv2(pdv_selected.trim());
        String canal = handler.getCanalPDV(id_pdv);

        ContentValues values = new ContentValues();

        values.put(ContractInsertPdvFotografico.Columnas.PHARMA_ID, id_pdv);
        values.put(ContractInsertPdvFotografico.Columnas.CODIGO, pdv_selected);
        values.put(ContractInsertPdvFotografico.Columnas.USUARIO, user);
        values.put(ContractInsertPdvFotografico.Columnas.SUPERVISOR, supervisor);
        values.put(ContractInsertPdvFotografico.Columnas.FECHA, fechaser);
        values.put(ContractInsertPdvFotografico.Columnas.HORA, horaser);
        values.put(ContractInsertPdvFotografico.Columnas.CANAL, canal);
        values.put(ContractInsertPdvFotografico.Columnas.CODIGO_PDV, id_pdv);
        values.put(ContractInsertPdvFotografico.Columnas.MERCADERISTA, user);
        values.put(ContractInsertPdvFotografico.Columnas.FOTO, image);

        values.put(Constantes.PENDIENTE_INSERCION, 1);

        guardarFotoEnGaleria(); //con marca de agua

        getContentResolver().insert(ContractInsertPdvFotografico.CONTENT_URI, values);
        vaciarCampos();
        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true,Constantes.insertPdvFotografico, null);
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE,Toast.LENGTH_SHORT).show();
        }

    }

    public void vaciarCampos(){
        spPdv.setSelection(0);
        imageView.setImageDrawable(null);
    }

    public void guardarFotoEnGaleria(){
        String user2 = user.replace(" ","_");
        String fechaS = fechaser.replace("/","");
        String horaS = horaser.replace(":","");

        USUARIO = "FOTOGRAFICO_"+ user2 + "_" + fechaS + "_" + horaS;
        FILE_NAME = USUARIO + ".jpeg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, FILE_NAME);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Fotografico/");
            //values.put(MediaStore.MediaColumns.IS_PENDING, 1);
        } else {
            File directory = Environment.getExternalStoragePublicDirectory("Fotografico");
            File file = new File(directory, FILE_NAME);

            if (!directory.exists()){directory.mkdir();}

            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }


        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try(OutputStream output = getContentResolver().openOutputStream(uri)) {
            Log.i("VACIO foto", output + " -- "+ scaled);
            scaled.compress(Bitmap.CompressFormat.JPEG, 100,output);
            Toast.makeText(getApplicationContext(),"Se Guardó en Galería: Fotografico",Toast.LENGTH_LONG).show();

            //borrar de galeria la foto sin marca de agua
            File fdelete = new File(Objects.requireNonNull(getFilePath(uriOriginal)));

            if (fdelete.exists()) {
                try {
                    fdelete.delete();
                } catch (Exception e) {
                    Log.i("Borrar foto", e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFilePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            return picturePath;
        }
        return null;
    }


    public static String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void filtrarPdvs() {
        List<String> operadores = handler.getPdvs();
        if(operadores.size() == 2){
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPdv.setAdapter(dataAdapter);
        spPdv.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean esFormularioValido(){

        if (pdv_selected.equals("Seleccione")){
            Toast.makeText(this, "Debe seleccionar un Pdv", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(imageView.getDrawable() == null){
            Toast.makeText(this, "Debe tomar una foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}