package com.luckyecuador.app.PintucoAPP;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.EvidenciaMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import org.json.JSONException;
import org.json.JSONObject;

public class ImplementacionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText txtCiudad;
    private Spinner spCanal;

    private String codigo;
    private Spinner spFormato;
    private Spinner spZona;
    private EditText txtCliente;
    private EditText txtPuntoVenta;
    private EditText txtLocal;
    private EditText txtDireccion;
    private EditText txtLatitud;
    private EditText txtLongitud;

    private static final String TAG = "LOG:";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int LOCATION_INTERVAL = 0; //3Minutos
    private static final float LOCATION_DISTANCE = 0;

    public static ImageView ivFoto;
    public static ImageView ivFoto2;

    String ciudad, canal, formato, zona, cliente, puntoventa, direccion, latitud, longitud, user, image = "";;

    ImageButton ibLocalizacion, ibCargarfoto;
    Button btnGuardar;

    DatabaseHelper handler;
    Bitmap bitmap;

    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ+"PuntoNuevo";
    String path;

    ActivityResultLauncher<Intent> startActivityIntent;
    ActivityResultLauncher<Intent> startActivityIntent2;
    String current = null;


    private static final int REQUEST_CODE_DRAW_OVERLAY_PERMISSION = 1234;

    // Método para solicitar permiso de superposición de ventanas
    private void requestOverlayPermission() {
        // Verificar si ya se tiene el permiso
        if (!Settings.canDrawOverlays(this)) {
            // Si no se tiene el permiso, mostrar el diálogo de solicitud
            new AlertDialog.Builder(this)
                    .setTitle("Permitir superposición de ventanas")
                    .setMessage("Para continuar, por favor permita que la aplicación muestre superposiciones de ventanas.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Mostrar la solicitud de permiso
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY_PERMISSION);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    // Método para manejar el resultado de la solicitud de permiso
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DRAW_OVERLAY_PERMISSION) {
            // Verificar si el usuario otorgó el permiso
            if (Settings.canDrawOverlays(this)) {
                // El usuario otorgó el permiso, puedes proceder
            } else {
                // El usuario no otorgó el permiso, puedes manejar esto aquí
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implementacion);
        setToolbar();
        LoadData();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        RequestPermissions requestPermissions = new RequestPermissions(getApplicationContext(), ImplementacionActivity.this);
        requestPermissions.showPermissionDialog();

        //startService(new Intent(getApplicationContext(), MyService.class));
        //requestOverlayPermission();

        /*if (!isServiceRunning(AsistenciaService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Si la versión del SDK es Oreo o superior, utiliza startForegroundService()
                Intent serviceIntent = new Intent(this, AsistenciaService.class);
                ContextCompat.startForegroundService(this, serviceIntent);
            } else {
                // Si la versión del SDK es anterior a Oreo, utiliza startService() normal
                Intent serviceIntent = new Intent(this, AsistenciaService.class);
                startService(serviceIntent);
            }
        }*/


        txtCiudad = (EditText) findViewById(R.id.txtCiudad);
        spCanal = (Spinner) findViewById(R.id.spCanal);
        spFormato = (Spinner) findViewById(R.id.spFormato);
        spZona = (Spinner) findViewById(R.id.txtZona);

        txtCliente = (EditText) findViewById(R.id.txtCliente);
        txtPuntoVenta = (EditText) findViewById(R.id.txtPuntoVenta);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        txtLongitud = (EditText) findViewById(R.id.txtLongitud);
        txtLocal = (EditText) findViewById(R.id.txtLocal);

        ivFoto = (ImageView)findViewById(R.id.ivFoto);
        ivFoto2 = (ImageView)findViewById(R.id.ivFoto2);

        String n = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";
        String imageUrl2 = "https://developers.google.com/static/maps/documentation/android-sdk/images/multicolor-polyline.png";
        String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?"+
                "center="+ latitud +","+ longitud +
                "&zoom=15"+
                "&size=500x500"+
                "&markers=size:mid|color:red|"+ latitud +","+ longitud +
                "&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";

        Picasso.get().load(imageUrl).into(ivFoto2);

        ibLocalizacion = (ImageButton) findViewById(R.id.ibLocalizacion);
        ibCargarfoto = (ImageButton) findViewById(R.id.ibCargarFoto);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        ibLocalizacion.setOnClickListener(this);
        ibCargarfoto.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    //Setear los EditText
                    if (!txtLatitud.getText().equals(location.getLatitude())&&!txtLongitud.getText().equals(location.getLongitude()))
                    {
                        txtLatitud.setText(""+location.getLatitude());
                        txtLongitud.setText(""+location.getLongitude());
                    }

                    locationManager.removeUpdates(locationListener);
//                locationManager = null;
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) {
                //Si el GPS esta deshabilitado, abrir la ventana de activacion del GPS en el dispositivo.
                Intent newIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(newIntent);
            }
        };

        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
            }return;
        }else {
            //Si los permisos estan otorgados, llamar al evento onClick del boton
            //clickButton();
        }


        /*
        if (validaPermisos()) {
            ibCargarfoto.setEnabled(true);
        }else{
           ibCargarfoto.setEnabled(false);
        }*/

        //getTiempo(this,user,codigo);

    }

    public void getTiempo(Activity activity, String user, String id_pdv){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", user);
            map.put("id_pdv", "ALI01A0116");
            Log.i("RESPONSE 3 ", "entro 3"+user+" - " + id_pdv);
            // Crear nuevo objeto Json basado en el mapa
            JSONObject jobject = new JSONObject(map);

            //GET METHOD
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.GET_EVIDENCIAS, jobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    procesarRespuestaTiempo(response, activity);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.TIME_OUT);
                    } else if (error instanceof NoConnectionError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.NO_RED);
                    }else if (error instanceof AuthFailureError) {
                        //TODO
                    } else if (error instanceof ServerError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.SEVER_ERROR);
                    } else if (error instanceof NetworkError) {
                        Log.i("ERROR JSON USER DEVICE", Mensajes.RED_ERROR);
                    } else if (error instanceof ParseError) {
                        //TODO
                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }catch (Exception e){
            Log.i("ERROR REQUEST USER DEVICE", e.getMessage());
            e.printStackTrace();
        }
    }

    public void procesarRespuestaTiempo(JSONObject response, Activity activity){
        Log.i("RESPONSE 4 ", "entro 4");
        if (response!=null){
//            List<String> mensajeList = new ArrayList<>();
            Log.i("RESPONSE 5 ", "entro 5");
            try{
                String estado = response.getString("estado");
                Log.i("RESPONSE 5 ", "entro estado"+estado);
                switch (estado){
                    case "1": //EXITO
                        //Toast.makeText(activity, "Se ha enlazado su usuario con este dispositivo", Toast.LENGTH_SHORT).show();
                        String mensaje = response.getString("evidencias");
                        Log.i("resultadode tiempo",""+mensaje);
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON", mensaje2);
                        /*ad.show();
                        valor = false;*/
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

/*
    public void filtrarMarca() {
        List<String> operadores = handler.getMarcaPropia();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spMarca.setAdapter(dataAdapter);
        //spMarca.setOnItemSelectedListener(this);
    }
    */

/*
    public void filtrarMarca(String status) {

        List<String> operadores = handler.getCategoria(status);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }
    */

   /* public void filtrarMarca(String status, String subcategoria) {
        List<String> operadores = handler.getTipos(status,subcategoria);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(dataAdapter);
        spTipo.setOnItemSelectedListener(this);
    }*/

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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        codigo = sharedPreferences.getString(Constantes.CODIGO,Constantes.NODATA);

    }

    @Override
    public void onClick(View view) {
        if (view == ibCargarfoto) {
            opciones();
        }

        if (view == ibLocalizacion) {

            txtLatitud.setHint("Buscando..");
            txtLongitud.setHint("Buscando..");
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

        if (view== btnGuardar) {
            String ciudad = txtCiudad.getText().toString().trim();
            String canal = spCanal.getSelectedItem().toString().trim();
            String formato = spFormato.getSelectedItem().toString().trim();
            String zona = spZona.getSelectedItem().toString().trim();
            String cliente = txtCliente.getText().toString().trim();
            String puntoventa = txtPuntoVenta.getText().toString().trim();
            String local = txtLocal.getText().toString().trim();
            String direccion = txtDireccion.getText().toString().trim();
            String latitud = txtLatitud.getText().toString().trim();
            String longitud = txtLongitud.getText().toString().trim();

            if(ivFoto.getDrawable() != null) {
                Bitmap temporal = ((BitmapDrawable) ivFoto.getDrawable()).getBitmap();
                EvidenciaMark em = new EvidenciaMark();
                //Bitmap watermark = em.markSinMapa(temporal, "ciudad: GUAYAQUIL", "local:SUPERMAXI", "usuario: PRUEBA LUCKY", "fechaHora: 2021/01/01 10:25", "coordenadas: -134195, 45235265", "dirFinal: CAMILO DESTRUJE ENTRE LA 35 Y 36", Color.YELLOW, 100, 80, false);
                int mheight = (int) (temporal.getHeight() * (1024.0 / temporal.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(temporal, 1024, mheight, true);

                ivFoto.setImageBitmap(scaled);

            }


            if (esFormularioValido(latitud,longitud)){
                enviarDatos(ciudad, canal, formato, zona, cliente, puntoventa, direccion, local, latitud, longitud);
            }

            /*
            if (!latitud.trim().isEmpty() || !longitud.trim().isEmpty()) {
                enviarDatos(ciudad, canal, formato, zona, cliente, puntoventa, direccion, local, latitud, longitud);
            } else {
                Toast.makeText(this,"Ingrese Latitus y longitud", Toast.LENGTH_LONG).show();
            }
            */
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void enviarDatos(String ciudad, String canal, String formato, String zona, String cliente, String puntoventa, String direccion, String local, String latitud, String longitud) {
        try{

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                ContentValues values = new ContentValues();

                values.put(ContractInsertImplementacion.Columnas.USUARIO, user);
                values.put(ContractInsertImplementacion.Columnas.FECHA, fechaser);
                values.put(ContractInsertImplementacion.Columnas.HORA, horaser);
                values.put(ContractInsertImplementacion.Columnas.CIUDAD, ciudad);
                values.put(ContractInsertImplementacion.Columnas.CANAL, canal);
                values.put(ContractInsertImplementacion.Columnas.CLIENTE, "N/A");
                values.put(ContractInsertImplementacion.Columnas.FORMATO, "N/A");
                values.put(ContractInsertImplementacion.Columnas.ZONA, "N/A");
                values.put(ContractInsertImplementacion.Columnas.PDV, puntoventa);
                values.put(ContractInsertImplementacion.Columnas.DIRECCION, direccion);
                values.put(ContractInsertImplementacion.Columnas.LOCAL, local);
                values.put(ContractInsertImplementacion.Columnas.LATITUD, latitud);
                values.put(ContractInsertImplementacion.Columnas.LONGITUD, longitud);
                values.put(ContractInsertImplementacion.Columnas.FOTO, image);

                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertImplementacion.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true,Constantes.insertImplementacion, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE,Toast.LENGTH_SHORT).show();
                }

            finish();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ImplementacionActivity.this);
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


    private boolean esFormularioValido(String latitud,String longitud) {

        if (txtCiudad.getText().toString().isEmpty()) {
            Toast.makeText(this,"Ingrese Ciudad", Toast.LENGTH_LONG).show();
            return false;
        }

        if (spCanal.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
            Toast.makeText(this,"Escoja Canal", Toast.LENGTH_LONG).show();
            return false;
        }

        /*
        if (txtCliente.getText().toString().isEmpty()) {
            Toast.makeText(this,"Ingrese el Cliente", Toast.LENGTH_LONG).show();
            return false;
        }*/

        if (txtLocal.getText().toString().isEmpty()) {
            Toast.makeText(this,"Ingrese el Local", Toast.LENGTH_LONG).show();
            return false;
        }

        /*
        if (spFormato.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
            Toast.makeText(this,"Escoja el Formato", Toast.LENGTH_LONG).show();
            return false;
        }

        if (spZona.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
            Toast.makeText(this,"Escoja la Zona", Toast.LENGTH_LONG).show();
            return false;
        }*/

        if (txtPuntoVenta.getText().toString().isEmpty()) {
            Toast.makeText(this,"Ingrese el Punto de Venta", Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtDireccion.getText().toString().isEmpty()) {
            Toast.makeText(this,"Ingrese la Dirección", Toast.LENGTH_LONG).show();
            return false;
        }

        if (latitud.trim().isEmpty() || longitud.trim().isEmpty()) {
            Toast.makeText(this,"Ingrese Latitud y longitud", Toast.LENGTH_LONG).show();
            return false;
        }

        if (ivFoto.getDrawable() != null) { //ImageView no vacio
            Bitmap temporal = ((BitmapDrawable) ivFoto.getDrawable()).getBitmap();
            image = getStringImage(temporal);
        } else {
            Toast.makeText(this, "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (image!=null && !image.equals("")){
            return true;
        }

        return true;
    }






    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ImplementacionActivity.this);
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

//    private void cargarImagen() {
//
//        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
//        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ImplementacionActivity.this);
//        alertOpciones.setTitle("Seleccione una Opción");
//        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (opciones[i].equals("Tomar Foto")) {
//                    tomarFotografia();
//                }else{
//                    if (opciones[i].equals("Cargar Imagen")) {
//                        /*Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/");
//                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);*/
//                        openGallery();
//                    }else{
//                        dialogInterface.dismiss();
//                    }
//                }
//            }
//        });
//        alertOpciones.show();
//
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
//    }
//
//    private void tomarFotografia() {
//        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
//        boolean isCreada=fileImagen.exists();
//        String nombreImagen="";
//        if (isCreada==false) {
//            isCreada=fileImagen.mkdirs();
//        }
//
//        if (isCreada==true) {
//            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
//        }
//
//
//        path=Environment.getExternalStorageDirectory()+
//                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
//
//        File imagen=new File(path);
//
//        Intent intent=null;
//        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
//        {
//            String authorities=getApplicationContext().getPackageName()+".provider";
//            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        }else
//        {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
//        }
//        startActivityForResult(intent,COD_FOTO);
//
//        //
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==RESULT_OK) {
//
//            switch (requestCode) {
//                case COD_SELECCIONA:
//                   /* Uri miPath=data.getData();
//                    imageView.setImageURI(miPath);*/
//                    Uri filePath = data.getData();
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        //Setear el ImageView con el Bitmap
//                        scaleImage(bitmap);
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case COD_FOTO:
//                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
//                            new MediaScannerConnection.OnScanCompletedListener() {
//                                @Override
//                                public void onScanCompleted(String path, Uri uri) {
//                                    Log.i("Ruta de almacenamiento","Path: "+path);
//                                }
//                            });
//                    Bitmap bitmap= BitmapFactory.decodeFile(path);
//                    scaleImage(bitmap);
//                    break;
//            }
//        }
//
//    }
//    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
//    public void scaleImage(Bitmap bitmap) {
//        try{
//            int mheight = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
//            ivFoto.setImageBitmap(scaled);
//            bitmapfinal = ((BitmapDrawable)ivFoto.getDrawable()).getBitmap();
//
//        } catch (Exception e) {
//            androidx.appcompat.app.AlertDialog alertDialog1;
//            alertDialog1 = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext()).create();
//            alertDialog1.setTitle("Message");
//            alertDialog1.setMessage("Notificar \t "+e.toString());
//            alertDialog1.show();
//            Log.e("compressBitmap", "Error on compress file");
//        }
//    }
//
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

    private void opciones() {
        final CharSequence []opciones={"Abrir Camara","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(ImplementacionActivity.this);
        alertOpciones.setTitle("Seleccione una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Abrir Camara")) {
                    //permiso();
                    //openCamera();
                    Intent n = new Intent(ImplementacionActivity.this, CameraActivity.class);
                    n.putExtra("activity", "nuevo_pdv");
                    startActivity(n);
                }else if(opciones[i].equals("Abrir Galeria")){
                    galeria();
                }else if(opciones[i].equals("Cancelar")){
                    dialogInterface.dismiss();
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void galeria(){
        Intent gallery = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityIntent2.launch(gallery);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

/*
        if (adapterView==spMarca)
        {
            status=spMarca.getSelectedItem().toString();
            filtrarMarca(status);
        }
*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
