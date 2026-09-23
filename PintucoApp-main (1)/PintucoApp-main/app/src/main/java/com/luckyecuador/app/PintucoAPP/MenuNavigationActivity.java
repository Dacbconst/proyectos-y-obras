package com.luckyecuador.app.PintucoAPP;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.Clase.Asistencia;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioPrioritario;
import com.luckyecuador.app.PintucoAPP.Clase.Base_pharma_value;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService;
import com.luckyecuador.app.PintucoAPP.Session.SessionManagement;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.BatteryLevel;
import com.luckyecuador.app.PintucoAPP.Utils.ControlesGps;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.SignalQuality;
import com.luckyecuador.app.PintucoAPP.databinding.ActivityMenuNavigationBinding;
import androidx.core.content.res.ResourcesCompat;
import com.luckyecuador.app.PintucoAPP.ui.precios.PreciosFragment;
import com.luckyecuador.app.PintucoAPP.ui.propensos.PropensosFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MenuNavigationActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private static final int DIALOG_ALERT = 0 ;

    public String tiempo_base = "", tiempo_espera = "", tiempo_notificacion= "";

    AlertDialog ad;
    AlertDialog ad2;

    ProgressDialog progressDialog;
    AlertDialog alert_marcacion;

    AlertDialog adJustificacion = null;
    ActionBarDrawerToggle toggle;

    String ACTION_FILTER = "versiones.luckyec.com.cronometro";
    
    ListViewAdapterExh dataAdapter;
    ListViewAdapterPop dataAdapterpop;
    ArrayList<BasePortafolioPrioritario> selectedItems = new ArrayList<BasePortafolioPrioritario>();

    private ListView listViewPrioritario;
    private ListView listViewPop;
    private TextView tvPrioritario;
    private TextView tvPop;
    private TextView lDistancia;

    private EditText txtotraJustificacion;

    private int radioButtonSelected;

    public Double latitud_insert = 0.0, longitud_insert = 0.0;

    private String justificacion = null;

    private String causalJustificacion;

    private String descJustificacion;

    private String supervisor;


    private int pharmavalue = 0;
    private int productos = 0;
    private int preguntas = 0;

    private int tests = 0;

    private int tipos_exhibicion = 0;

    private int promociones = 0;
    private int permitido = 0;
    private int suma = 0;
    private int rotacion = 0;

    private int precios_pvc = 0;
    private int tareas = 0;
    private int popsugerido = 0;
    private int prioritario = 0;
    private int combo_canjes = 0;
    private int causales_mci = 0;
    private int materiales_alertas = 0;
    private int pdi = 0;
    private int causales_prod_mal_est = 0;
    private int tiempo_almuerzo = 0;
    private int versiones = 0;

    private int tipo_precios = 0;

    private int justificaciones = 0;


    private RadioGroup radioGroup;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuNavigationBinding binding;

    NavigationView navigationView;

    private String id_pdv, user, codigo_pdv, channel, punto_venta, fecha, hora, canal, device_id, device_name, perimetro_pdv, id_ruta, tipo_relevo, pref_pdv;

    DatabaseHelper handler;
    String tipo_modulo = "";
    DrawerLayout drawerLayout;

    private String tipo_registro, causal, modulo;
    // Hora capturada al abrir alertDialog(), reusada en insertDataRegistro() para que esAtrasado y la hora guardada nunca queden desincronizados.
    private Date horaAperturaDialogo;
    private boolean falta_salida;
    private boolean almuerzo_en_curso = false;

//    String latitude, longitude;
//    final String[] lblLatitud = {"Latitud"};
//    final String[] lblLongitud = {"Longitud"};

//    private boolean interno;
    private boolean show_prioritarios = false;

    //Photo Camera
    public static ImageView imageView;
    public static ImageView imageViewJust;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    long lengthbmp;
    int TAKE_PHOTO_CODE = 0;
    private int PICK_IMAGE_REQUEST = 1;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;


    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Jornada";
    String path;
    String textDistancia = "";
    ImageButton btnCamera;
    Button btnGallery;
    MarshMallowPermission marshMallowPermission;
    private BroadcastReceiver receptorSync;

    BasePharmaValue pdv = new BasePharmaValue();

    private double latitud_pdv, longitud_pdv, distance_pdv;
    private double latitud = 0, longitud = 0, distance;
    private int contador = 0;
    private String causal_fuera_pdv = "";
    private boolean salida = false;

    ActivityResultLauncher<Intent> startActivityIntent;
    ActivityResultLauncher<Intent> startActivityIntent2;
    String current = null;

    private boolean tiene_entrada = false;
    private String hora_inicio_visita = "";

    private boolean tiene_justificacion_entrada = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int LOCATION_INTERVAL = 0; //3Minutos
    private static final float LOCATION_DISTANCE = 0;

    String fecha_sync;
    String sync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        LoadData();
        Bundle extras = getIntent().getExtras();


        subirInformacionRetenida();

        if (extras != null) {
//            interno = extras.getBoolean(Constantes.INTERNO, false);
            show_prioritarios = extras.getBoolean(Constantes.SHOW_PRIORITARIOS, false);
            modulo = extras.getString(Constantes.MODULO, Constantes.NODATA);
//            channel = extras.getString(Constantes.CANAL);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.MODULO, modulo);
            editor.commit();
        }




        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();



        this.falta_salida = false;
        Log.i("1. FALTA SALIDA ON CREATE", String.valueOf(falta_salida));
        obtenerFecha();
        Log.i("2. FALTA SALIDA ON CREATE", String.valueOf(falta_salida));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_GPS_UPDATE), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_GPS_UPDATE), Context.RECEIVER_NOT_EXPORTED);
        }
        new DeveloperOptions().modalDevOptions(MenuNavigationActivity.this);
        RequestPermissions requestPermissions = new RequestPermissions(getApplicationContext(), MenuNavigationActivity.this);
        requestPermissions.showPermissionDialog();


        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerLayout = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_osa,
                R.id.nav_precios,
                R.id.nav_sod,
                R.id.nav_promo,
                R.id.nav_exh,
                R.id.nav_logros,
                R.id.nav_sms,
                R.id.nav_tareas,
                R.id.nav_inv,
                R.id.nav_canjes,
                R.id.nav_mci,
                R.id.nav_almuerzo,
                R.id.nav_ejecucion_materiales,
                R.id.nav_pdi,
                R.id.nav_ventas,
                R.id.nav_propensos,
                R.id.nav_producto_mal_estado,
                R.id.nav_impulso,
                R.id.nav_malla_codificados,
                R.id.nav_evidencias,
                R.id.nav_logistico)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_menu_navigation);
//            if (fragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .remove(fragment)
//                        .commitAllowingStateLoss();
//            }
//        });





        TextView tvCodigo = (TextView) headerLayout.findViewById(R.id.nav_header_codigo);
        lDistancia = (TextView) headerLayout.findViewById(R.id.nav_header_distancia);

        tvCodigo.setText("Código: " + codigo_pdv);

        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        } else {
//            locationStart();
            //Si los permisos estan otorgados, llamar al evento onClick del boton
            //clickButton();
        }

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

      //  getLocation();

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
//        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            startLocationService();
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST);
//        }

        if (!isMyServiceRunning(LocationService.class)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(getApplicationContext(), LocationService.class));
            } else {
                startForegroundService(new Intent(getApplicationContext(), LocationService.class));
            }
        }

//        pdv = handler.getPdv(codigo_pdv);
//
//        distance_pdv = Double.parseDouble(pdv.getDistancia());
//        latitud_pdv = Double.parseDouble(pdv.getLatitud());
//        longitud_pdv = Double.parseDouble(pdv.getLongitud());
//        entrada();
//        if (interno) {
//            validadorPDV();
//            tipo_relevo = "Interno";
//        }

        if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
            validadorPDV();
        }


        /*fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i("fusedLocationClient", "ENTRA");
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
                        distancia(latitud, longitud);
                    }
                }
            }
        });*/

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        Log.i("fusedLocationClient", "ENTRA");

                        if (location != null) {

                            if (ControlesGps.esUbicacionFalsa(location)) {
                                mostrarAlertaFakeGps();
                                return;
                            }

                            latitud = location.getLatitude();
                            longitud = location.getLongitude();

                            if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
                                distancia(latitud, longitud);
                            }
                        }
                    }
                });


//        if (show_prioritarios) {
//            mostrarPopup("Información");
//        }

        device_id = getDeviceId(getApplicationContext());
        device_name = getDeviceName();
        Log.i("DEVICE ID", device_id);
        Log.i("DEVICE NAME", device_name);

        hideItem(channel);
        //registrarInasistencias(); //oncreate



        // Boton adicional para la salida del pdv
        navigationView.getMenu().findItem(R.id.nav_salida_pdv).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                alertDialogSalidaPDV();
                return false;
            }
        });


Log.i("antes de tiempos", "si");
        getTiempo(this,user,codigo_pdv);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    latitud_insert = location.getLatitude();
                    longitud_insert = location.getLongitude();
                    locationManager.removeUpdates(locationListener);
                }catch (Exception e) {
                    Log.e("MenuNavigationActivity", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) {}
        };

        checkFechayHora();
        SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);

        obtenerCoordenadas();



        receptorSync = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                try {
                    final String mensaje = intent.getStringExtra("extra.mensaje");
                    if (mensaje.equalsIgnoreCase(Mensajes.SYNC_FINALIZADA_PROD) ||
                            mensaje.equalsIgnoreCase(Mensajes.SYNC_NOREQUERIDA + "Productos")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_promociones, user);
                        progressDialog.setMessage("Descargando Tipos de Exhibicion, espere un momento...");
                        productos = 1;
                        permitido = 1;
                    } else if (mensaje.equals("No se encontraron registros en el servidor para este usuario.")) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            Log.i("PROGRESS_DIALOG", "CIERRE");
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "No se encontraron registros en el servidor para este usuario.", Toast.LENGTH_SHORT).show();
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROD) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Productos")) {
                        productos = 1;
//                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_preg, operator);
//                        progressDialog.setMessage("Descargando test, espere un momento...");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_promociones, user);
                        progressDialog.setMessage("Descargando promociones, espere un momento...");
                    }
*/
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROMO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Promociones")) {
                        promociones = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_exhibicion, user);
                        progressDialog.setMessage("Descargando Tipos de Exhibicion, espere un momento...");
                    }


/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PREG) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Preguntas")) {
                        preguntas = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tests, operator);
                        progressDialog.setMessage("Descargando tests, espere un momento...");
                    }


                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TESTS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tests")) {
                        tests = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_exhibicion, operator);
                        progressDialog.setMessage("Descargando tipos de exhibiciones, espere un momento...");
                    }
*/

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIPO_EXH) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tipo Exhibicion")) {
                        tipos_exhibicion = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_precios_pvc, user);
                        progressDialog.setMessage("Descargando Precios Pvc, espere un momento...");
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_ROTACION) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Rotacion")) {
                        rotacion = 1;
//                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tareas, operator);
//                        progressDialog.setMessage("Descargando tareas, espere un momento...");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_pop_sugerido, operator);
                        progressDialog.setMessage("Descargando pop sugerido, espere un momento...");
                    }

//                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TAREAS) ||
//                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tarea")) {
//                        tareas = 1;
//                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_pop_sugerido, operator);
//                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_POPSUGERIDO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Pop_sugerido")) {
                        popsugerido = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_prioritario, operator);
                        progressDialog.setMessage("Descargando portafolio prioritario, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PRIORITARIO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Portafolio Prioritario")) {
                        prioritario = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_combo_canjes, operator);
                        progressDialog.setMessage("Descargando canjes, espere un momento...");
                    }*/

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PRECIOS_PVC) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Precios Pvc")) {
                        precios_pvc = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_mci, user);
                        progressDialog.setMessage("Descargando causales MCI, espere un momento...");
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_MCI) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales MCI")) {
                        causales_mci = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_osa, operator);
                        progressDialog.setMessage("Descargando causales osa, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_OSA) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales OSA")) {
                        causales_mci = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_materiales_alertas, operator);
                        progressDialog.setMessage("Descargando materiales alertas, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_MATERIALES_ALERTAS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Materiales Alertas")) {
                        materiales_alertas = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_precios_pvc, operator);
                        progressDialog.setMessage("Descargando precios pvc, espere un momento...");
                    }
*/
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_MCI) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales MCI") || mensaje.equalsIgnoreCase("No se encontraron registros en el servidor.")) {
                        causales_mci = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_precios, user);
                        progressDialog.setMessage("Descargando tipo precios, espere un momento...");
//                        verificarLogin(user, pass);
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PDI) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "PDI")) {
                        pdi = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_precios, operator);
                        progressDialog.setMessage("Decargando tipo precios, espere un momento...");

//                        verificarLogin(user, pass);
                    }
*/
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIPO_PRECIO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tipo_precios") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        tipo_precios = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_justificaciones, user);
                        progressDialog.setMessage("Descargando justificaciones, espere un momento...");

//                        verificarLogin(user, pass);
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_JUST) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Justificación") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        justificaciones = 1;
                        Log.i("entra justificacion","entra justificacion");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_versiones, user);
                        progressDialog.setMessage("Descargando Causales Productos en mal estado, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_VERSIONES) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Versiones") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        versiones = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tiempo_almuerzo, user);
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIEMPO_ALMUERZO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tiempo Almuerzo") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        tiempo_almuerzo = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_prod_mal_est, user);
                    }



                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_PROD_MAL_EST) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "CausalesProdMalEst") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        causales_prod_mal_est = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_rangos_precios, user);
                        progressDialog.setMessage("Descargando Rangos de Precios, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "RangosPrecios") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_rangos_precios_sku, user);
                        progressDialog.setMessage("Descargando Rangos de Precios por SKU, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS_SKU) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "RangosPreciosSku") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_asistencia, user);
                        progressDialog.setMessage("Descargando causales de asistencia, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_asistencia_atraso, user);
                        progressDialog.setMessage("Descargando causales de asistencia atraso, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA_ATRASO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia Atraso") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_contactos, user);
                        progressDialog.setMessage("Descargando Contactos, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CONTACTOS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Contactos")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_proforma, user);
                        progressDialog.setMessage("Descargando Proforma, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROFORMA) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Proforma")) {
                        progressDialog.dismiss();
                        new GuardarLog(MenuNavigationActivity.this).saveLog(user, "", "Descarga de información - (Listado de los Pdv's)");
                        Toast.makeText(getApplicationContext(), Mensajes.SYNC_FINALIZADA, Toast.LENGTH_SHORT).show();
                    }



                  //  Log.i("PDV", pdv + "");
                    Log.i("PRODUCTOS", productos + "");
                    //    Log.i("PREGUNTAS", preguntas + "");
                    //    Log.i("TESTS", tests + "");
                    Log.i("TIPOS EXHIBICION", tipos_exhibicion + "");
                    Log.i("PROMOCIONES", promociones + "");
                    //    Log.i("ROTACION", rotacion + "");
                    //        Log.i("TAREAS", tareas + "");
                    //    Log.i("POPSUGERIDO", popsugerido + "");
                    //    Log.i("PRIORITARIO", prioritario + "");
                    //    Log.i("COMBO_CANJES", combo_canjes + "");
                    //    Log.i("CAUSALES_MCI", causales_mci + "");
                    //    Log.i("MATERIALES_ALERTAS", materiales_alertas + "");
                    Log.i("PRECIOS_PVC", precios_pvc + "");
                    //    Log.i("PDI", pdi + "");
                    Log.i("TIPO_PRECIOS", tipo_precios + "");
                    Log.i("JUSTIFICACIONES", justificaciones + "");
                    Log.i("VERSIONES", versiones + "");
                    Log.i("TIEMPO ALMUERZO", tiempo_almuerzo + "");
                    Log.i("CAUSALES PRODUCTOS EN MAL ESTADO", causales_prod_mal_est + "");
                    Log.i("MENSAJE", mensaje);
                } catch (Exception e) {
                    Log.i("Receptor", e.getMessage());
                }
            }
        };





        if (ControlesGps.esDispositivoRoot()) {
            mostrarAlertaRoot();
            return; // detenemos ejecución
        }



    }

    private void mostrarAlertaRoot() {

        new AlertDialog.Builder(this)
                .setTitle("Dispositivo no permitido")
                .setMessage("No se permite el uso de la aplicación en dispositivos rooteados.")
                .setCancelable(false)
                .setPositiveButton("Cerrar", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    private void mostrarAlertaFakeGps() {

        new AlertDialog.Builder(this)
                .setTitle("Ubicación falsa detectada")
                .setMessage("No se permite el uso de aplicaciones de GPS falso.")
                .setCancelable(false)
                .setPositiveButton("Cerrar", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        obtenerCoordenadas();
        //registrarInasistencias(); //onRestart
    }

    public class getLocationTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getLocation();
            return null;
        }
    }

    private void getLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    locationManager.removeUpdates(locationListener);
                }catch (Exception e) {
                    Log.e("MenuNavigationActivity", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) {}
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
        } catch (SecurityException ex) {
            Log.i("POST RECYCLER", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("POST RECYCLER", "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
        } catch (SecurityException ex) {
            Log.i("POST RECYCLER", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("POST RECYCLER", "gps provider does not exist " + ex.getMessage());
        }
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void validadorPDV() {
        try {
            if (!tiene_entrada && !tiene_justificacion_entrada) {

                // Toast.makeText(this, "prueba", Toast.LENGTH_SHORT).show();
             //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                progressDialog = new ProgressDialog(MenuNavigationActivity.this, R.style.MyAlertDialogStyle);
                progressDialog.setTitle("Distancia PDV");
                progressDialog.setMessage("Validando distancia al PDV");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");

        new DeveloperOptions().modalDevOptions(MenuNavigationActivity.this);
        uniqueDevice();
//        new UniqueDevice().modalUniqueDevice(MenuActivity.this, user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_GPS_UPDATE), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_GPS_UPDATE));
        }

        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Intent.ACTION_SYNC);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorSync, filtroSync);

        checkFechayHora();
        SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);


//        if (interno) {
//            validadorPDV();
//        }
    }

    private void moveToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void subirInformacionRetenida() {

        // Obtenemos la calidad de señal de red sea por Wifi o Datos
        String señal = new SignalQuality(getApplicationContext()).getSignalQuality();
        Log.i("Señal","Calidad de la señal de Internet: "+ señal);

        if (señal.equalsIgnoreCase("Buena") || señal.equalsIgnoreCase("Excelente")){
            //subir informacion retenida automaticamente
            SyncAdapter.sincronizarAhora(this, true, Constantes.SUBIR_TODO, null);
            Log.i("Noti","Se envio la informacion retenida");
        }
    }

    public void uniqueDevice() {

        handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);
        device_id = getDeviceId(getApplicationContext());
        Log.i("DEVICE_IDfc", device_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuNavigationActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("Sesión única");
        builder.setMessage("Este dispositivo no está atado a su usuario, por favor, contáctese con su supervisor");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //moveTaskToBack(true);
                //activity.finish();
                /*Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                //activity.startActivity(intent);

                /*Intent n = new Intent(PuntosListActivity.this, LoginActivity.class);
                finish();
                startActivity(n);*/

                new GuardarLog(getApplicationContext()).saveLog(user, "", "Cierre de Sesión");
                ClufActivity.signed = 0;
                SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                sessionManagement.removeSession();
                moveToLogin();


            }
        });
        ad = builder.create();

        boolean isUserDevice = handler.esDispositivoDelUsuario(device_id);

        if (!isUserDevice) {
            if (VerificarNet.hayConexion(getApplicationContext()) && (user!=null || !user.trim().isEmpty())) {
                getUserDevice(MenuNavigationActivity.this, user);
            }
            //ad.show();
        } else {
            if (ad.isShowing()) {
                ad.dismiss();
            }

            //Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserDevice(Activity activity, String user){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("user", user);

            // Crear nuevo objeto Json basado en el mapa
            JSONObject jobject = new JSONObject(map);

            //GET METHOD
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.GET_USER_DEVICE, jobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    procesarRespuestaGetUserDevice(response, activity,user);
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

    public void procesarRespuestaGetUserDevice(JSONObject response, Activity activity, String user){
        if (response!=null){
//            List<String> mensajeList = new ArrayList<>();
            try{
                String estado = response.getString("estado");
                switch (estado){
                    case "1": //EXITO
                        JSONArray mensaje = response.getJSONArray("device_id");
                        for(int i = 0; i < mensaje.length(); i++){
                            JSONObject jb1 = mensaje.getJSONObject(i);
//                            mensajeList.add(jb1.getString("device_id"));
                            String id = jb1.getString("device_id");
                            Log.i("RESPONSE", mensaje.toString());
                            if (id.equals("null")){
                                ContentValues values = new ContentValues();
                                values.put(ContractPharmaValue.Columnas.DEVICE_ID, device_id);
                                new TareaEditarDeviceId(activity.getApplicationContext().getContentResolver(), values, user).execute(ContractPharmaValue.CONTENT_URI);
                                UpdateUserDevice(activity,user,device_id );
                            }else{
                                if(!id.equals(device_id)){
                                    ad.show();
                                }
                            }
                            //Log.i("RESPONSE 2", mensaje);
                            //ContentValues values = new ContentValues();
                            //values.put(ContractPharmaValue.Columnas.DEVICE_ID, id);
                            //new TareaEditarDeviceId(activity.getApplicationContext().getContentResolver(), values, user).execute(ContractPharmaValue.CONTENT_URI);
                        }
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON", mensaje2);
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void UpdateUserDevice(Activity activity, String user, String device_id){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("user", user);
            map.put("device_id", device_id);
            Log.i("RESPONSE 3 ", "entro 3");
            // Crear nuevo objeto Json basado en el mapa
            JSONObject jobject = new JSONObject(map);

            //GET METHOD
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.UPDATE_USER_DEVICE, jobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    procesarRespuestaUpdateUserDevice(response, activity);
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


    public void procesarRespuestaUpdateUserDevice(JSONObject response, Activity activity){
        Log.i("RESPONSE 4 ", "entro 4");
        if (response!=null){
//            List<String> mensajeList = new ArrayList<>();
            Log.i("RESPONSE 5 ", "entro 5");
            try{
                String estado = response.getString("estado");
                switch (estado){
                    case "1": //EXITO
                        JSONArray mensaje = response.getJSONArray("mensaje");
                        for(int i = 0; i < mensaje.length(); i++){
                            JSONObject jb1 = mensaje.getJSONObject(i);
                            //String id = jb1.getString("device_id");
                            Log.i("resultado",""+jb1.toString());
                        }
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON", mensaje2);
                        break;
                    case "3": //FALLIDO
                        String mensaje3 = response.getString("mensaje");
                        Log.i("YA EXISTE EL CODIGO", mensaje3);
                        ad.show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    static class TareaEditarDeviceId extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;
        private final String user;

        public TareaEditarDeviceId(ContentResolver resolver, ContentValues valores, String user) {
            this.resolver = resolver;
            this.valores = valores;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Uri uri = args[0];
            if (null != uri) {
                String selectQuery = ContractPharmaValue.Columnas.USER + "=?";
                String[] val = new String[]{user};
                resolver.update(uri, valores, selectQuery, val);
            }
            return null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorSync);
    }

    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if(intent.hasExtra(Constantes.LATITUD)){
                latitud = intent.getDoubleExtra(Constantes.LATITUD, 0);
                longitud = intent.getDoubleExtra(Constantes.LONGITUD, 0);
                distancia(latitud, longitud);
            }
        }
    };



    private void distancia(double latitud, double longitud) {
        pdv = handler.getPdv(codigo_pdv);
        distance_pdv = Double.parseDouble(pdv.getDistancia());
        latitud_pdv = Double.parseDouble(pdv.getLatitud());
        longitud_pdv = Double.parseDouble(pdv.getLongitud());

        MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
        MarkerOptions place2 = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Posicion PDV");

        distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
        distance = (int)Math.round(distance);

       // String textDistancia = "";
        if (distance >= distance_pdv) {
            contador = 0;
            causal_fuera_pdv = " - SALIDA FUERA DEL PERÍMETRO A " + distance + " MTS. DEL PDV";
            textDistancia = "Fuera del rango del PDV: " + distance + " mts.";

            if (alert_marcacion!=null && alert_marcacion.isShowing() && !salida) {
                alert_marcacion.dismiss();
            }


            /*if (modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
                if (!tiene_entrada && !tiene_justificacion_entrada){
                    // (adJustificacion == null) Es para evitar que cierto tiempo que se ejecuta el location service
                    // Aparezca otro alertdialog
                       if (adJustificacion == null) alertDialogJustificar(modulo,"Entrada");
                  }
            }*/

            tipo_relevo = "Externo";
            if(modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
                Toast.makeText(this, "Fuera del rango del PDV: " + distance + " mts.", Toast.LENGTH_SHORT).show();
                closeActivity();
            }
        } else {

            textDistancia = "Dentro del rango del PDV: " + distance + " mts.";
            causal_fuera_pdv = "";
            if (contador == 0 && modulo != null &&
                (modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL) ||
                 modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO))) {
                entrada();
            }
            tipo_relevo = "Interno";
            contador++;
        }
        lDistancia.setText(textDistancia);
        try {
            Thread.sleep(2000);
            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void hideItem(String canal) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        Log.i("CANAL", canal);
        if (canal.equalsIgnoreCase("MAYORISTA")) {
//            nav_Menu.findItem(R.id.nav_impulso).setVisible(false);
            nav_Menu.findItem(R.id.nav_osa).setVisible(false);
            nav_Menu.findItem(R.id.nav_canjes).setVisible(false);
            nav_Menu.findItem(R.id.nav_malla_codificados).setVisible(false);
            nav_Menu.findItem(R.id.nav_evidencias).setVisible(false);
            nav_Menu.findItem(R.id.nav_mci).setVisible(false);
            nav_Menu.findItem(R.id.nav_ejecucion_materiales).setVisible(false);
            nav_Menu.findItem(R.id.nav_logros).setVisible(false);
            nav_Menu.findItem(R.id.nav_sms).setVisible(false);
            nav_Menu.findItem(R.id.nav_tareas).setVisible(false);
            nav_Menu.findItem(R.id.nav_impulso).setVisible(false);
            nav_Menu.findItem(R.id.nav_fotografico).setVisible(false);

        }

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            nav_Menu.findItem(R.id.nav_osa).setVisible(false);
            nav_Menu.findItem(R.id.nav_canjes).setVisible(false);
            nav_Menu.findItem(R.id.nav_malla_codificados).setVisible(false);
            nav_Menu.findItem(R.id.nav_evidencias).setVisible(false);
            nav_Menu.findItem(R.id.nav_mci).setVisible(false);
            nav_Menu.findItem(R.id.nav_ejecucion_materiales).setVisible(false);
            nav_Menu.findItem(R.id.nav_logros).setVisible(false);
            nav_Menu.findItem(R.id.nav_sms).setVisible(false);
            nav_Menu.findItem(R.id.nav_tareas).setVisible(false);
            nav_Menu.findItem(R.id.nav_impulso).setVisible(false);
            nav_Menu.findItem(R.id.nav_fotografico).setVisible(false);

        }

        if (user.toUpperCase().contains("IMPULSO")) {
            nav_Menu.findItem(R.id.nav_sod).setVisible(false);
            nav_Menu.findItem(R.id.nav_exh).setVisible(false);
            nav_Menu.findItem(R.id.nav_canjes).setVisible(false);
            nav_Menu.findItem(R.id.nav_malla_codificados).setVisible(false);
            nav_Menu.findItem(R.id.nav_evidencias).setVisible(false);
            nav_Menu.findItem(R.id.nav_mci).setVisible(false);
            nav_Menu.findItem(R.id.nav_ejecucion_materiales).setVisible(false);
            nav_Menu.findItem(R.id.nav_logros).setVisible(false);
            nav_Menu.findItem(R.id.nav_sms).setVisible(false);
            nav_Menu.findItem(R.id.nav_tareas).setVisible(false);
            nav_Menu.findItem(R.id.nav_fotografico).setVisible(false);
        }
        if (canal.equalsIgnoreCase("IMPULSO")) {
            nav_Menu.findItem(R.id.nav_sod).setVisible(false);
            nav_Menu.findItem(R.id.nav_exh).setVisible(false);
            nav_Menu.findItem(R.id.nav_canjes).setVisible(false);
            nav_Menu.findItem(R.id.nav_malla_codificados).setVisible(false);
            nav_Menu.findItem(R.id.nav_evidencias).setVisible(false);
            nav_Menu.findItem(R.id.nav_mci).setVisible(false);
            nav_Menu.findItem(R.id.nav_ejecucion_materiales).setVisible(false);
            nav_Menu.findItem(R.id.nav_logros).setVisible(false);
            nav_Menu.findItem(R.id.nav_sms).setVisible(false);
            nav_Menu.findItem(R.id.nav_tareas).setVisible(false);
            nav_Menu.findItem(R.id.nav_fotografico).setVisible(false);
        }
    }

    private void entrada() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(currentLocalTime);


        SharedPreferences spEntrada = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean reasignado = spEntrada.getBoolean("reasignado_" + codigo_pdv, false);
        boolean tiene_entrada = !reasignado && handler.tieneEntrada(codigo_pdv, user, fecha);
        boolean tiene_justificacion_entrada = handler.tieneJustificacionEntrada(codigo_pdv,user,fecha);

        if (!tiene_entrada && !tiene_justificacion_entrada) {

            if (VerificarNet.hayConexion(getApplicationContext())){

                alertDialog("Entrada");

            }else{

                alertDialogJustificar(modulo,"Entrada");

            }

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

        /*if (!tiene_entrada && interno) {
            alertDialog("Entrada");
        }/* else {
            mostrarPopup("Información");
        }*/

    }

//    private void startLocationService() {
//        try {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                Intent intent = new Intent(MenuNavigationActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_START_FOREGROUND_SERVICE);
//                startService(intent);
//                Log.i("ENTRA1","ENTRA1");
//                //startService(new Intent(getApplicationContext(), LocationService.class));
//            } else {
//                Intent intent = new Intent(MenuNavigationActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_START_FOREGROUND_SERVICE);
//                startForegroundService(intent);
//                Log.i("ENTRA2","ENTRA2");
//            }
//        }catch (Exception e){
//            Log.i("LocationService",e.getMessage());
//        }
//    }
//
//    private void stopLocationService() {
//        try {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                Intent intent = new Intent(MenuNavigationActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startService(intent);
//                //startService(new Intent(getApplicationContext(), LocationService.class));
//            } else {
//                Intent intent = new Intent(MenuNavigationActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startForegroundService(intent);
//            }
//        } catch (Exception e){
//            Log.i("LocationService",e.getMessage());
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync_subida1) {
            if (VerificarNet.hayConexion(this)) {
                try {
                    new GuardarLog(MenuNavigationActivity.this).saveLog(user, "", "Subida de información retenida - Interna (Dentro del Pdv)");
                    SyncAdapter.sincronizarAhora(this, true, Constantes.SUBIR_TODO, null);
                    Snackbar.make(findViewById(R.id.drawer_layout), Mensajes.ON_SYNC_UP, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.drawer_layout), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(MenuNavigationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), Mensajes.ERROR_RED, Snackbar.LENGTH_LONG).show();
            }
            return true;
        }


        if (item.getItemId() == R.id.action_sync_bajada) {
            if (VerificarNet.hayConexion(this)) {
                if (!user.equals("")) {
                    AsyncTaskBajarOper bajarOper = new AsyncTaskBajarOper();
                    bajarOper.execute();
                }else{
                    Toast.makeText(getApplicationContext(),Mensajes.SYNC_NO_USER ,Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(drawerLayout, Mensajes.ERROR_RED , Snackbar.LENGTH_LONG).setAction("Action", null).show();//                Toast.makeText(getApplicationContext(),"No hay conexión de Internet.",Toast.LENGTH_LONG).show();
            }
        }




        if (id == R.id.action_log) {
            Intent intent = new Intent(MenuNavigationActivity.this, HistorialActivity.class);
            startActivity(intent);
            return true;
        }
/*
        if (id == R.id.action_cluf) {
            Intent intent = new Intent(MenuNavigationActivity.this, ClufActivity.class);
            intent.putExtra("menu", "1");
            startActivity(intent);
            return true;
        }
*/
        if (id == R.id.action_version) {
            mostrarVersion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mostrarVersion() {

        String fecha = getString(R.string.version).split(": ")[1];
        String tamaño = handler.getTamañoVersion(fecha);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.action_version))
                .setMessage(getString(R.string.user) + ": " + user + "\n" +
                        getString(R.string.version) + "\n" +
                        "Tamaño: " + tamaño)
                .setPositiveButton("ACEPTAR", null)
                .show();
    }


    public void checkFechayHora(){
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String horaActual = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String minActual = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
        String amPm = new SimpleDateFormat("a", Locale.getDefault()).format(new Date());
        Log.i("fechaSync","" + fecha_sync);
        Log.i("fechaSync","" + amPm);
        Log.i("fechaSync","" + horaActual);
        Log.i("fechaSync","" + sync);
        if (fechaActual.equalsIgnoreCase(fecha_sync)){
            if(amPm.equals("pm")){
                if (Integer.parseInt(horaActual) > 19){
                    Log.i("fechaSync","hora mayor a 8");
                    Log.i("fechaSync","" + sync);
                    if(!sync.equals("SI")){
                        cerrarSesion("1");
                    }
                }else{
                    //guardarNuevaFecha(fechaActual);
                    //cerrarSesion();
                    Log.i("fechaSync","hora menor a 8");
                }
            }
        }else{
            //en caso de no abrir en un dia se cierra al sgt
            Log.i("fechaSync","fecha distinta a la ultima sync");

            /*SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.SYNC, "SI");
            editor.commit();*/

            cerrarSesion("2");
        }
    }


    public void cerrarSesion(String num){

        if(num.equals("1")){
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.SYNC, "SI");
            editor.commit();
        }else{
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.SYNC, "NO");
            editor.commit();
        }

        new GuardarLog(MenuNavigationActivity.this).saveLog(user, "", "Cierre de Sesión");
        ClufActivity.signed = 0;
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.removeSession();
        moveToLogin();
    }


    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        channel =sharedPreferences.getString(Constantes.CANAL,Constantes.NODATA);
        falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA, false);
        Log.i("SHARED PREFERENCE", String.valueOf(falta_salida));
        id_ruta = sharedPreferences.getString(Constantes.ID_RUTA, Constantes.NODATA);
        modulo = sharedPreferences.getString(Constantes.MODULO, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);

        fecha_sync = sharedPreferences.getString(Constantes.FECHA_SYNC, Constantes.NODATA);
        sync = sharedPreferences.getString(Constantes.SYNC, Constantes.NODATA);
        tiene_entrada = handler.tieneEntrada(codigo_pdv, user, fecha);
        tiene_justificacion_entrada = handler.tieneJustificacionEntrada(codigo_pdv, user, fecha);
        almuerzo_en_curso = sharedPreferences.getBoolean(Constantes.ALMUERZO_EN_CURSO,false);

        if (tiene_entrada || tiene_justificacion_entrada) {
            falta_salida = handler.visitaEnCurso(codigo_pdv, user, fecha) && handler.faltaJustificacionSalida(codigo_pdv,user,fecha);
        }

        if (codigo_pdv != null && !codigo_pdv.equals(Constantes.NODATA)) {
            pdv = handler.getPdv(codigo_pdv);
        }
//        Toast.makeText(this, "Falta Salida LD: "+falta_salida, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void mostrarPopup(final String informacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_popup, null);
        //Title
        //builder.setIcon(android.R.drawable.ic_menu_set_as);
        //builder.setTitle(tipo_registro.toUpperCase());
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_popup,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
//        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);

        tittle.setText(informacion.toUpperCase());

        TextView lblSegmentacion=(TextView) dialogView.findViewById(R.id.lblSegmentacion);
        TextView lblCompras=(TextView) dialogView.findViewById(R.id.lblCompras);
        listViewPrioritario = (ListView) dialogView.findViewById(R.id.listView);
        listViewPop = (ListView) dialogView.findViewById(R.id.listViewPop);
        tvPrioritario = (TextView) dialogView.findViewById(R.id.data_empty);
        tvPop = (TextView) dialogView.findViewById(R.id.data_empty2);

        showListView();
        showListViewPop();

        String Segmentacion = handler.getSegmentacionMenu();
        String Compras = handler.getComprasMenu();

        lblSegmentacion.setText(Segmentacion);
        lblCompras.setText(Compras);

        listViewPrioritario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ENTRA", "ENTRA");
                String selectedItemSku = ((TextView)view.findViewById(R.id.lblSku)).getText().toString();
                // String selectedItemNumExh = ((EditText)view.findViewById(R.id.lblNumExh)).getText().toString();

                BasePortafolioPrioritario listPort = new BasePortafolioPrioritario();
                listPort.setSku(selectedItemSku);
                // listSku.setNumExh(selectedItemNumExh);

                for (int j = 0; j < selectedItems.size(); j++) {
                    if (selectedItems.get(j).getSku().contains(selectedItemSku)) {
                        selectedItems.remove(j);
                        //  ((CheckBox) view.findViewById(R.id.respuesta)).setChecked(false);
                    }else{
                        // selectedItems.add(listSku);
                        //  ((CheckBox) view.findViewById(R.id.respuesta)).setChecked(true);
                    }
                }
            }
        });

        builder.setPositiveButton(R.string.next,null);
        builder.setNeutralButton(R.string.cancel,null);

        //builder.setNegativeButton(R.string.cancel,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        ////pButton.setTextColor(Color.rgb(79,195,247));
        //pButton.setBackgroundColor(Color.rgb(79,195,247));
        pButton.setPadding(4,2,4,2);
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        ////cButton.setTextColor(Color.rgb(79,195,247));
        //cButton.setBackgroundColor(Color.rgb(79,195,247));
        cButton.setPadding(4,2,4,2);
    }

    public void showListView() {
        List<String> lista_prioritario = handler.filtrarListPortafolioPrioritario(canal, codigo_pdv);
        if (lista_prioritario.size()>0) {
            dataAdapter = new ListViewAdapterExh(this, lista_prioritario);
            listViewPrioritario.setAdapter(dataAdapter);
        } else {
            tvPrioritario.setVisibility(View.VISIBLE);
            listViewPrioritario.setVisibility(View.GONE);
        }
    }

    public void showListViewPop() {
        List<String> listPop = handler.filtrarListPop(canal, codigo_pdv);
        if (listPop.size()>0) {
            dataAdapterpop = new ListViewAdapterPop(this,listPop);
            listViewPop.setAdapter(dataAdapterpop);
        } else {
            tvPop.setVisibility(View.VISIBLE);
            listViewPop.setVisibility(View.GONE);
        }
    }
    public void alertDialog(final String tipo_registro) {
        LoadData();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        obtenerHoraReferenciaYMostrarDialogo(tipo_registro);
    }

    // Pide la hora al servidor antes de decidir atraso (en vez de confiar solo en el reloj del
    // celular, que puede estar momentáneamente desincronizado); si no hay conexión o el servidor
    // no responde rápido, cae al reloj local (mismo comportamiento de siempre, offline-first).
    private void obtenerHoraReferenciaYMostrarDialogo(final String tipo_registro) {
        if (!VerificarNet.hayConexion(getApplicationContext())) {
            mostrarDialogoCausales(tipo_registro, Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTime());
            return;
        }

        // Cubre la espera de la consulta al servidor: sin esto, con señal mala la pantalla
        // parece "trabada" varios segundos y el técnico puede tocar el botón repetido.
        progressDialog = new ProgressDialog(MenuNavigationActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Verificando hora...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constantes.GET_HORA_SERVIDOR, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Date horaServidor = parsearHoraServidor(response);
                mostrarDialogoCausales(tipo_registro, horaServidor != null ? horaServidor : Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTime());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.i("HORA_SERVIDOR", "No se pudo obtener la hora del servidor, se usa la del equipo: " + error);
                mostrarDialogoCausales(tipo_registro, Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTime());
            }
        });
        // Timeout corto y sin reintentos: si el servidor tarda, se cae al reloj local rápido en vez de dejar al técnico esperando.
        req.setRetryPolicy(new DefaultRetryPolicy(4000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    private Date parsearHoraServidor(JSONObject response) {
        try {
            if (response == null || !"1".equals(response.optString("estado"))) {
                return null;
            }
            SimpleDateFormat formatoServidor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            formatoServidor.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            return formatoServidor.parse(response.getString("fecha") + " " + response.getString("hora"));
        } catch (Exception e) {
            Log.i("HORA_SERVIDOR", "Respuesta del servidor no se pudo interpretar: " + e.getMessage());
            return null;
        }
    }

    private void mostrarDialogoCausales(final String tipo_registro, Date horaReferencia) {
        this.tipo_registro = tipo_registro;
        if (tipo_registro.equalsIgnoreCase("SALIDA") || tipo_registro.equalsIgnoreCase("JUSTIFICACION")) {
            salida = true;
        }

        // Se captura una sola vez acá (servidor si se pudo, si no el reloj local); insertDataRegistro() reusa este mismo instante como hora final.
        this.horaAperturaDialogo = horaReferencia;

        // Lógica de tiempo GMT-5 comparando con hora_inicio del PDV
        boolean esAtrasado = false;
        if (tipo_registro.equalsIgnoreCase("ENTRADA")) {
            try {
                String horaProgramada = pdv.getHora_inicio();

                if (horaProgramada != null && !horaProgramada.trim().isEmpty()) {
                    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                    hourFormat.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                    String horaActual = hourFormat.format(horaReferencia);

                    Date dActual = hourFormat.parse(horaActual);
                    Date dProgramada = hourFormat.parse(horaProgramada.trim());

                    esAtrasado = dActual.after(dProgramada);

                    if (esAtrasado) {
                        this.tipo_registro = "ENTRADA TARDIA";
                    }

                    Log.i("ALERT_DIALOG", "HoraActual: " + horaActual
                            + " | HoraProgramada: " + horaProgramada
                            + " | Atrasado: " + esAtrasado);
                } else {
                    Log.i("ALERT_DIALOG", "Sin hora programada en el PDV, se asume puntual.");
                    esAtrasado = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                esAtrasado = false;
            }
        }

        // Inflar layout según puntualidad
        int layoutId = esAtrasado ? R.layout.alertdialog_causales_atrasos : R.layout.alertdialog_causales;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);

        builder.setIcon(android.R.drawable.ic_menu_set_as);

        // Traer Views
        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);
        imageView = (ImageView) dialogView.findViewById(R.id.ivFotoRegistro);
        btnCamera = (ImageButton) dialogView.findViewById(R.id.btnCamera);

        // Llenar RadioGroup dinámicamente desde BD
        DatabaseHelper db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);
        List<String> causales = esAtrasado
                ? db.getCausalesAsistenciaAtraso()
                : db.getCausalesAsistencia();

        if (!causales.isEmpty() && causales.get(0).equals("Seleccione")) {
            causales.remove(0);
        }

        for (int i = 0; i < causales.size(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setId(View.generateViewId());
            rb.setText(causales.get(i));
            rb.setLayoutParams(new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT));
            rb.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_light_italic));
            radioGroup.addView(rb);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                opciones();
            }
        });

        // Color y texto del título según puntualidad
        if (tipo_registro.equalsIgnoreCase("ENTRADA")) {
            if (esAtrasado) {
                tittle.setText("ENTRADA ATRASO");
                tittle.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tittle.setText(tipo_registro.toUpperCase());
                tittle.setBackground(getDrawable(R.drawable.entrada_background));
            }
        } else if (tipo_registro.equalsIgnoreCase("SALIDA")) {
            tittle.setText(tipo_registro.toUpperCase());
            tittle.setBackground(getDrawable(R.drawable.salida_background));
        } else {
            tittle.setText(tipo_registro.toUpperCase());
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (imageView.getDrawable() != null) {
                    android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(MenuNavigationActivity.this);
                    alertadd.setTitle("Vista Previa");
                    LayoutInflater factory = LayoutInflater.from(MenuNavigationActivity.this);
                    final View dialog_view = factory.inflate(R.layout.vista_previa, null);
                    ImageView dialog_imageview = (ImageView) dialog_view.findViewById(R.id.dialog_imageview);
                    dialog_imageview.setImageDrawable(imageView.getDrawable());
                    alertadd.setView(dialog_view);
                    alertadd.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {}
                    });
                    alertadd.show();
                } else {
                    Toast.makeText(MenuNavigationActivity.this, "No hay foto cargada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton(R.string.save,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (esFormularioValido()) {
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                            causal = radioButton.getText().toString();
                            insertDataRegistro(latitud, longitud, causal);
                        }
                    }
                }
        );

        builder.setNeutralButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (tipo_registro.equalsIgnoreCase("ENTRADA")) {
                    Log.i("PRESIONASTE CANCELAR", "SALIENDO");
                    Intent intent = new Intent(MenuNavigationActivity.this, PuntosListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        builder.setView(dialogView);
        alert_marcacion = builder.create();
        alert_marcacion.show();

        Button button = alert_marcacion.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new CustomListener(alert_marcacion, dialogView));

        Button cButton = alert_marcacion.getButton(DialogInterface.BUTTON_NEUTRAL);
        cButton.setPadding(4, 2, 4, 2);
    }

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cancelar"};
//        final CharSequence[] opciones={"Tomar Foto","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    Intent n = new Intent(MenuNavigationActivity.this, CameraActivity.class);
                    n.putExtra("activity", "marcacion");
                    startActivity(n);

                } else {
                    if (opciones[i].equals("Cargar Imagen")) {
                        /* Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);*/
                        //  openGallery();
                        //  Intent n = new Intent(context, GaleriaActivity.class);
                        //   context.startActivity(n);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }


    public boolean esFormularioValido() {
        if(radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(),"No selecccionó una causal",Toast.LENGTH_LONG).show();
            return false;
        }
        if (imageView.getDrawable() == null) {
            Toast.makeText(getApplicationContext(),"No tomó una foto",Toast.LENGTH_LONG).show();
            return false;
        }
        if (latitud==0 && longitud==0) {
            Toast.makeText(getApplicationContext(), "Genere las Coordenadas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getTiempo(Activity activity, String user, String id_pdv){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", user);
            map.put("id_pdv", id_pdv);
            Log.i("RESPONSE 3 ", "entro 3" + user +" - 0" + id_pdv);
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
        Log.i("RESPONSE 4 ", "tiempo traido de la base, cada cuanto aparece la alerta");
        if (response!=null){
            Log.i("RESPONSE 5 ", "entro 5");
            try{
                String estado = response.getString("estado");
                Log.i("RESPONSE 5 ", "entro estado"+estado);
                switch (estado){
                    case "1": //EXITO
                        //Toast.makeText(activity, "Se ha enlazado su usuario con este dispositivo", Toast.LENGTH_SHORT).show();
                        JSONArray mensaje = response.getJSONArray("evidencias");
                        Log.i("resultadode",""+mensaje);
                        JSONObject jb1 = mensaje.getJSONObject(0);
                        //String id = jb1.getString("device_id");
                        tiempo_base = jb1.getString("tiempo");
                        tiempo_espera = jb1.getString("tiempo_espera");
                        tiempo_notificacion = jb1.getString("tiempo_notificacion");

                        SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(Constantes.TIEMPO_BASE, tiempo_base);
                        editor.putString(Constantes.TIEMPO_ESPERA, tiempo_espera);
                        editor.putString(Constantes.TIEMPO_NOTIFICACION, tiempo_notificacion);
                        editor.commit();

                        Log.i("ttfr",""+tiempo_base);
                        Log.i("tiempo de espera",""+tiempo_espera);
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON tbase", mensaje2);
                        /*ad.show();
                        valor = false;*/
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void registrarInasistencias(){
        Log.i("ENtro a registrarInasistencia","true");
        ArrayList<Asistencia> asistencias = handler.getNoRegistrados();
        //aqui ya se traen los registros que quedaron en false que son de horas menos de la actual
        //cuando se sale del app y regresa, se enviaran como no validados los registros menor a la hoa actual que no se han enviado
        for(int i = 0 ;i<asistencias.size(); i++){
            Log.i("menuactibity latitud",""+latitud);
            Log.i("menuactibity longitud",""+longitud);
            insertDataRegistroAsistencias(latitud,longitud,asistencias.get(i).getHora());
        }
    }

    public void registrarInasistenciasFaltantes(){
        ArrayList<Asistencia> asistencias = handler.getNoRegistradosFaltantes();
        //aqui ya se traen todos los registros que quedaron en false
        //esto es en el caso de marcar salida antes de la hora_fin, todos los registros de asistencia hasta la hora fin seran subidos como no validados
        for(int i = 0 ;i<asistencias.size(); i++){
            Log.i("tiempocsa",""+latitud_insert);
            Log.i("tiempocsa",""+longitud_insert);
            insertDataRegistroAsistencias(latitud_insert,longitud_insert,asistencias.get(i).getHora());
        }
    }

    //no validados
    public boolean insertDataRegistroAsistencias(double latitud, double longitud, String hora) {
        try {

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            DateFormat hour_2 = new SimpleDateFormat("HH:mm");
            hour_2.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser_ac = hour_2.format(currentLocalTime);


            if (latitud != 0.0 && longitud != 0.0){

                pdv = handler.getPdv(codigo_pdv);
                distance_pdv = Double.parseDouble(pdv.getDistancia());
                latitud_pdv = Double.parseDouble(pdv.getLatitud());
                longitud_pdv = Double.parseDouble(pdv.getLongitud());

                MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
                MarkerOptions place2 = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Posicion PDV");

                distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
                distance = Double.valueOf(Math.round(distance));

            }else{
                distance = 0.0;
            }

            /*String ciudad = "Ciudad: " + city;
            String local = "Local: " + pos_name;
            String usuario = "Usuario: " + usuarioCursor;
            String fechaHora = "Fecha y hora: " + fechaser + " " + horaser;

            Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ImageMark im = new ImageMark();
            Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
            int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

            String image = getStringImage(scaled);*/

            //handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);


            Base_pharma_value bpv = handler.getInfoPDV(codigo_pdv);

            ContentValues values = new ContentValues();

            values.put(ContractInsertAsistencia.Columnas.IDPDV, codigo_pdv);
            values.put(ContractInsertAsistencia.Columnas.USUARIO, user);
            values.put(ContractInsertAsistencia.Columnas.VERSION,getResources().getString(R.string.version));
            values.put(ContractInsertAsistencia.Columnas.LATITUDE, latitud + "");
            values.put(ContractInsertAsistencia.Columnas.LONGITUDE, longitud + "");
            values.put(ContractInsertAsistencia.Columnas.FOTO, "");
            values.put(ContractInsertAsistencia.Columnas.FECHA, fechaser);
            values.put(ContractInsertAsistencia.Columnas.HORA, hora);
            values.put(ContractInsertAsistencia.Columnas.DISTANCIA, distance);
            values.put(ContractInsertAsistencia.Columnas.POS_NAME, bpv.getPos_name());
            values.put(ContractInsertAsistencia.Columnas.BATERIA, new BatteryLevel().nivelBateria(getApplicationContext()));
            values.put(ContractInsertAsistencia.Columnas.SUPERVISOR, supervisor);
            values.put(ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA, "NO VALIDADO");
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            getContentResolver().insert(ContractInsertAsistencia.CONTENT_URI, values);

            Log.i("horaact",""+horaser_ac);
            ContentValues values2 = new ContentValues();
            values2.put(ContractAsistenciasLocal.Columnas.REGISTRADO, "enviado");
            values2.put(ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION, "NO VALIDADO");
            new TareaEditarAsistenciaLocal(getApplicationContext().getContentResolver(), values2, hora).execute(ContractAsistenciasLocal.CONTENT_URI);

            //Toast.makeText(this, "Registrada su Asistencia " + horaser + "; Coordenadas: " + latitud + ", " + longitud, Toast.LENGTH_SHORT).show();

            if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertAsistencia, null);
                Toast.makeText(this, Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    static class TareaEditarAsistenciaLocal extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;
        private final String hora;

        public TareaEditarAsistenciaLocal(ContentResolver resolver, ContentValues valores, String hora) {
            this.resolver = resolver;
            this.valores = valores;
            this.hora = hora;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Uri uri = args[0];
            if (null != uri) {
                String selectQuery = ContractAsistenciasLocal.Columnas.HORA + "=?";
                String[] val = new String[]{hora};
                resolver.update(uri, valores, selectQuery, val);
            }
            return null;
        }

    }
    public void alertDialogConfirmar(String modulo){

        LayoutInflater myLayout = LayoutInflater.from(this);
        View dialogView = myLayout.inflate(R.layout.alertdialog_confirmacion_justificacion, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setIcon(android.R.drawable.ic_dialog_alert);


        TextView tvtext = dialogView.findViewById(R.id.text);
        TextView tvPDV = dialogView.findViewById(R.id.tvPDV);
        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        AlertDialog ad = builder.create();

        if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA")) {
            tvtext.setText("¿Estás séguro de la razón seleccionada?");
        }


        tvPDV.setText(punto_venta);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Evita registros duplicados por doble toque mientras se procesa la marcación
                if (!view.isEnabled()) {
                    return;
                }
                view.setEnabled(false);

                tipo_registro = "JUSTIFICACION";
                insertDataRegistro(latitud,longitud,"");

                if (salida) {
                    Log.i("entro a SaliDa", "true");
                  //  alertDialogAgradecimientos();
                      closeActivity();
                    salida = false;
                }

                ad.dismiss();
                adJustificacion.dismiss();
                txtotraJustificacion.setText("");


                }

        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });


        ad.show();
    }

    public void alertDialogJustificar(String modulo,String tipo){

        obtenerFecha();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater myLayout = this.getLayoutInflater();
        View dialogView = myLayout.inflate(R.layout.alertdialog_justificacion, null);

        //  builder.setView(dialogView);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("Alerta Fuera de Perímetro");
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_justificacion,null));

        radioGroup = dialogView.findViewById(R.id.radioGroup);
        txtotraJustificacion = dialogView.findViewById(R.id.txtOtraJustificacion);
        ImageButton btnCamera = dialogView.findViewById(R.id.btnCamera);
        imageView = dialogView.findViewById(R.id.ivFotoRegistro);
        TextView tvText = dialogView.findViewById(R.id.tv_msg);
        TextView tvText2 = dialogView.findViewById(R.id.tv_msg2);
        tvText.setVisibility(View.VISIBLE);
        tvText2.setVisibility(View.VISIBLE);

        txtotraJustificacion.setHint("Ingrese razón");



        List<String> justificaciones = new ArrayList<>();


        if (tipo.equalsIgnoreCase("Entrada")){
            justificaciones.add("VERIFICAR ENTRADA");
            salida = false;
        }

        if (tipo.equalsIgnoreCase("Salida")){
            justificaciones.add("VERIFICAR SALIDA");
            tvText.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
            salida = true;
        }

        /*
        if (!tiene_entrada || !tiene_justificacion_entrada){
            justificaciones.add("VERIFICAR ENTRADA");
            salida = false;
        }else{
            justificaciones.add("VERIFICAR SALIDA");
            salida = true;
        }*/


        int contadorId = 1;

        for (String item : justificaciones) {
            // Creamos el radiobutton y le damos estilos
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setText(item);
            //Soporta todas las versiones
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_light_italic);
            radioButton.setTypeface(typeface);
            radioButton.setId(contadorId);
            radioGroup.addView(radioButton);
            contadorId++;
        }


        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cargarImagen();
            }
        });


        //cuando se halla seleccionado
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioButtonSelected = checkedId;
                // Verificar que este seleccionado un radiobutton
                if (radioButtonSelected != -1){
                    // Obtenemos el radiobbutton seleccionado
                    RadioButton radioButtonSelected = dialogView.findViewById(checkedId);
                    //Obtenemos la justificacion
                    justificacion = radioButtonSelected.getText().toString();
                    // En caso , que se seleccione "OTRAS" se activara el editext para agregar una justificacion
                    if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA") || justificacion.equalsIgnoreCase("VERIFICAR SALIDA")){

                        txtotraJustificacion.setVisibility(View.VISIBLE);
                    }else{
                        txtotraJustificacion.setVisibility(View.GONE);
                    }

                }

            }


        });


            /*
            ibLocalizacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obtenerCoordenadas();
                }
            });*/

        builder.setPositiveButton("Guardar",null);


        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (tipo.equalsIgnoreCase("Entrada")){

                    dialog.dismiss();
                    radioButtonSelected = -1;
                    Intent intent = new Intent(MenuNavigationActivity.this, PuntosListActivity.class);
                    startActivity(intent);
                    //Stop the activity
                    finish();

                }


            }
        });


        builder.setView(dialogView);
        adJustificacion = builder.create();
        adJustificacion.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button btnGuardar = ((AlertDialog)adJustificacion).getButton(AlertDialog.BUTTON_POSITIVE);
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(validarFormulario()){
                            descJustificacion = txtotraJustificacion.getText().toString();
                            alertDialogConfirmar(modulo);
                        }
                    }
                });

            }
        });


        adJustificacion.show();

        Button button = adJustificacion.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new CustomListener(adJustificacion, dialogView));

        Button cButton = adJustificacion.getButton(DialogInterface.BUTTON_NEUTRAL);
        cButton.setPadding(4, 2, 4, 2);
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

    private void insertarNotificacion(String causal) {

        String descripcion = "Se justificó el pdv "+ punto_venta + " - " +  causal;



        ContentValues values_audit = new ContentValues();
        values_audit.put(ContractNotificacion.Columnas.USER, user);
        values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
        values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
        values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
        values_audit.put(ContractNotificacion.Columnas.HORA, hora);
        values_audit.put(Constantes.PENDIENTE_INSERCION, 1);

        getApplicationContext().getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(getApplicationContext(), true,Constantes.insertNot, null);
            Log.i("insertarNotificacion", Mensajes.ON_SYNC_SERVER);
        }else{
            Log.i("insertarNotificacion", Mensajes.ON_SYNC_DEVICE);
        }

    }


    public boolean validarFormulario(){


        // Comprobamos que se haya marcado alguna de las opciones
        if (radioButtonSelected == -1) {
            Toast.makeText(getApplicationContext(),"Debe escoger una justificación",Toast.LENGTH_SHORT).show();
            return false;
        }


        if (txtotraJustificacion.getText().toString().equalsIgnoreCase("") || txtotraJustificacion.getText().toString().isEmpty() || txtotraJustificacion.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "Debe escribir su justificación.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageView.getDrawable() == null){
            Toast.makeText(getApplicationContext(),"No tomó una foto",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    class CustomListener implements View.OnClickListener {

        private final Dialog dialog;
        private final View dialogView;

        public CustomListener(Dialog dialog, View dialogView){
            this.dialog = dialog;
            this.dialogView = dialogView;
        }

        @Override
        public void onClick(View v) {
            // Evita registros duplicados por doble toque mientras se procesa la marcación
            if (!v.isEnabled()) {
                return;
            }
            if (esFormularioValido()) {
                v.setEnabled(false);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                causal = radioButton.getText().toString();
                Log.i("NO SALE NADA","ENTRA10000");
                if(insertDataRegistro(latitud, longitud, causal)){
                    dialog.dismiss();

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    if (tipo_registro.equalsIgnoreCase("SALIDA")) {
                      //  alertDialogAgradecimientos();
                        closeActivity();
                    }
                } else {
                    v.setEnabled(true);
                }
            }
        }
    }


//    public void alertDialogSalidaPDV(){
//        LoadData();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.drawable.icon_warning);
//        builder.setTitle("Salir PDV");
//        builder.setMessage("¿Desea salir PDV?");
//        Log.i("almuerzo en curso",""+almuerzo_en_curso);
//        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.i("FALTA SALIDA", String.valueOf(falta_salida));
//                // En caso, que haya finalizado el almuerzo
//                if (!almuerzo_en_curso) {
//                    //    Toast.makeText(MenuNavigationActivity.this, "Falta Salida: "+falta_salida, Toast.LENGTH_SHORT).show();
//                    if (falta_salida) {
//
//                        if (VerificarNet.hayConexion(MenuNavigationActivity.this)) {
//
//                            if (textDistancia.contains("Fuera del rango")) {
//                                alertDialogJustificar(modulo, "Salida");
//                            } else {
//                                alertDialog("Salida");
//                            }
//
//                        } else {
//
//                            alertDialogJustificar(modulo, "Salida");
//
//                        }
//
//                    } else {
//                        // alertDialogAgradecimientos();
//                        closeActivity();
//                    }
//
//            } else {
//                    Toast.makeText(getApplicationContext(),"Debe finalizar su almuerzo",Toast.LENGTH_LONG).show();
//                }
//
////                Intent intent = new Intent(MenuNavigationActivity.this, PuntosListActivity.class);
////                startActivity(intent);
////                //Stop the activity
////                finish();
//            }
//        });
//
//        builder.setNeutralButton("NO",null);
//
//        AlertDialog ad = builder.create();
//        ad.show();
//    }



public void alertDialogSalidaPDV() {
    LoadData();

    Log.i("DEBUG", "almuerzo_en_curso = " + almuerzo_en_curso);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setIcon(R.drawable.icon_warning);
    builder.setTitle("Salir PDV");
    builder.setMessage("¿Desea salir PDV?");

    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i("FALTA SALIDA", String.valueOf(falta_salida));

            try {
                if (!almuerzo_en_curso) {
                    if (falta_salida) {
                        if (VerificarNet.hayConexion(MenuNavigationActivity.this)) {
                            if (textDistancia.contains("Fuera del rango")) {
                                alertDialogJustificar(modulo, "Salida");
                            } else {
                                alertDialog("Salida");
                            }
                        } else {
                            alertDialogJustificar(modulo, "Salida");
                        }
                    } else {
                        closeActivity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe finalizar su almuerzo", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("ERROR alertDialogSalidaPDV", e.getMessage());
                Toast.makeText(getApplicationContext(), "Error al procesar la salida", Toast.LENGTH_SHORT).show();
            }
        }
    });

    builder.setNeutralButton("NO", null);
    builder.create().show();
}






    @Override
    public void onBackPressed() {
            alertDialogSalidaPDV();
    }

    private void closeActivity() {
        Log.i("MODULO CLOSE ACTIVITY", modulo);
        if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
            Intent intent = new Intent(MenuNavigationActivity.this, PuntosListActivity.class);
            intent.putExtra("salida_pdv","salida_pdv");
            Log.i("activitymenu latitud",""+latitud);
            Log.i("activitymenu longitud",""+longitud);
            //String n1 = String.valueOf(latitud);
            //String n2 = String.valueOf(longitud);
            intent.putExtra("latitud",latitud);
            intent.putExtra("longitud",longitud);
            startActivity(intent);
        } else if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO)) {
            Intent intent = new Intent(MenuNavigationActivity.this, RelevoTardioActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void sendBroadcastMessageDataLoaded(String tipo_registro) {
        double latitud_pdv = this.latitud_pdv;
        double longitud_pdv = this.longitud_pdv;
        String codigo_pdv = this.codigo_pdv;
        String nombre_pdv = this.punto_venta;
        String perimetro_pdv = this.perimetro_pdv;
        double distance_pdv = this.distance_pdv;
        if (tipo_registro.equalsIgnoreCase("SALIDA")) {
            latitud_pdv = 0;
            longitud_pdv = 0;
            codigo_pdv = "";
            nombre_pdv = "";
            perimetro_pdv = "";
            distance_pdv = 0;
        }
        Intent intent = new Intent(Constantes.ACTION_PDV_LOCATION_UPDATE);
        intent.putExtra(Constantes.LATITUD_PDV_ACTUAL, latitud_pdv);
        intent.putExtra(Constantes.LONGITUDE_PDV_ACTUAL, longitud_pdv);
        intent.putExtra(Constantes.CODIGO_PDV_ACTUAL, codigo_pdv);
        intent.putExtra(Constantes.NOMBRE_PDV_ACTUAL, nombre_pdv);
        intent.putExtra(Constantes.PERIMETRO_PDV_ACTUAL, perimetro_pdv);
        intent.putExtra(Constantes.DISTANCE_PDV_ACTUAL, distance_pdv);
        sendBroadcast(intent);
    }

    public boolean insertDataRegistro(double latitud, double longitud, String causal) {
        try {
            Log.i("insertDataRegistro", "Registrando " + tipo_registro + " " + latitud + " " + longitud);
            obtenerFecha();
            String ciudad = "Ciudad: " + pdv.getCity();
            String local = "Local: " + pdv.getPos_name();
            String usuario = "Usuario: " + user;
            String fechaHora = "Fecha y hora: " + fecha + " " + hora;

            Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ImageMark im = new ImageMark();
            Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
            int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

            String image = getStringImage(scaled);

            String registro = tipo_registro.toUpperCase();

            // Reusa la hora de alertDialog(); si no vino de ahí, cae al reloj en vivo como antes.
            Date currentLocalTime = (horaAperturaDialogo != null)
                    ? horaAperturaDialogo
                    : Calendar.getInstance(TimeZone.getTimeZone("GMT-5")).getTime();
            horaAperturaDialogo = null;
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            guardarFotoMarcacionEnGaleria(scaled, registro, user, fechaser, horaser);

            //String causalFinal = causal.substring(3);

            if (salida) {
                causal = causal + causal_fuera_pdv;
            }

            sendBroadcastMessageDataLoaded(registro);

            if (justificacion != null){

                if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA") || justificacion.equalsIgnoreCase("VERIFICAR SALIDA")){

                    causalJustificacion = justificacion;
                    causal = causalJustificacion + ": " + descJustificacion + " - " + textDistancia;

                    insertarNotificacion(causal);

                }

            }

/*
            if (id_ruta.equals("NoDisponible")){

                id_ruta = handler.getIdPdvInsert(codigo_pdv,fecha);
            }*/

          // id_ruta = id_ruta.equals("NoDisponible") ? handler.getIdPdv(codigo_pdv) : id_ruta;

            ContentValues values = new ContentValues();

            values.put(ContractInsertGps.Columnas.IDPDV, codigo_pdv);
            values.put(ContractInsertGps.Columnas.USUARIO, user);
            values.put(ContractInsertGps.Columnas.TIPO, registro);
            values.put(ContractInsertGps.Columnas.VERSION, getString(R.string.version));
            values.put(ContractInsertGps.Columnas.LATITUDE, latitud + "");
            values.put(ContractInsertGps.Columnas.LONGITUDE, longitud + "");
            values.put(ContractInsertGps.Columnas.FOTO, image);
            values.put(ContractInsertGps.Columnas.FECHA, fechaser);
            values.put(ContractInsertGps.Columnas.HORA, horaser);
            values.put(ContractInsertGps.Columnas.CAUSAL, causal);
            values.put(ContractInsertGps.Columnas.DISTANCIA, distance);
            values.put(ContractInsertGps.Columnas.TIPO_RELEVO, tipo_relevo);
            values.put(ContractInsertGps.Columnas.POS_NAME, punto_venta);
            values.put(Constantes.ID_REMOTA_RUTA, id_ruta);
            values.put(Constantes.PENDIENTE_INSERCION, 1);
            getContentResolver().insert(ContractInsertGps.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertGps, null);
                Toast.makeText(getApplicationContext(), "Registrada su " + registro, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Registrada su " + registro + " (sin conexión, se enviará luego)", Toast.LENGTH_SHORT).show();
            }

            /*
            if (registro.equalsIgnoreCase("ENTRADA") || registro.equalsIgnoreCase("JUSTIFICACION")) {
                faltaSalida(true, fechaser);
//                    mostrarPopup("Información");
            } else {
                faltaSalida(false, fechaser);
            }*/

            justificacion = (justificacion == null ? "" : justificacion);

            if (registro.equalsIgnoreCase("ENTRADA") || registro.equalsIgnoreCase("ENTRADA TARDIA") || justificacion.contains("VERIFICAR ENTRADA")) {
                faltaSalida(true, fechaser);
                tiene_entrada = true;
                SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constantes.HORA_INICIO_ALMUERZO, horaser);
                editor.putBoolean("reasignado_" + codigo_pdv, false);
                editor.commit();
                hora_inicio_visita = horaser;
            } else {
                faltaSalida(false, fechaser);
            }

//            Log.i("EL REGISTRO ,",registro);
//            if(registro.equalsIgnoreCase("ENTRADA") || justificacion.contains("VERIFICAR ENTRADA")){
//                vaciarTabla();
//                Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
//                cal.set(Calendar.SECOND, 0);
//
//                DateFormat hour2 = new SimpleDateFormat("HH:mm:ss");
//                hour2.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//
//                String horaser2 = hour.format(cal2.getTime());
//
//                SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString(Constantes.HORA_ENTRADA, horaser2);
//                editor.putString(Constantes.HORA_MARCACION, Constantes.NODATA);
//                editor.commit();
//
//                SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
//                String hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA,Constantes.NODATA);
//                Log.i("hora entrada ..0", hora_entrada);


//                if (!isServiceRunning(AsistenciaService.class)) {
//
//                    if (!tiempo_base.equals("")){
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            // Si la versión del SDK es Oreo o superior, utiliza startForegroundService()
//                            Intent serviceIntent = new Intent(this, AsistenciaService.class);
//                            serviceIntent.putExtra("tiempo",tiempo_base); //el intervalo
//                            serviceIntent.putExtra("hora_entrada",hora_entrada);
//                            serviceIntent.putExtra("tiempo_espera",tiempo_espera);
//                            serviceIntent.putExtra("tiempo_notificacion",tiempo_notificacion);
//                            ContextCompat.startForegroundService(this, serviceIntent);
//                        } else {
//                            // Si la versión del SDK es anterior a Oreo, utiliza startService() normal
//                            Intent serviceIntent = new Intent(this, AsistenciaService.class);
//                            serviceIntent.putExtra("tiempo",tiempo_base);
//                            serviceIntent.putExtra("hora_entrada",hora_entrada);
//                            serviceIntent.putExtra("tiempo_espera",tiempo_espera);
//                            serviceIntent.putExtra("tiempo_notificacion",tiempo_notificacion);
//                            startService(serviceIntent);
//                        }
//
//                    } else {
//                        Toast.makeText(this, "No se pudo iniciar el servicio de Asistencia", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//
//
//            }else if(registro.equalsIgnoreCase("SALIDA") || justificacion.contains("VERIFICAR SALIDA")){
//                if (isServiceRunning(AsistenciaService.class)) {
//                    Log.i("ccddda","stop service");
//                    Intent intent = new Intent(this, AsistenciaService.class);
//                    stopService(intent);
//                }
//
//                //alertDialogAgradecimientos();
//
//                Log.i("ccddda","salida");
//                //registrarInasistenciasFaltantes();
//                //vaciarTabla();
//            }

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
            }

            Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            cal.set(Calendar.SECOND, 0);

            DateFormat hour2 = new SimpleDateFormat("HH:mm:ss");
            hour2.setTimeZone(TimeZone.getTimeZone("GMT-5"));

            String horaser2 = hour.format(cal2.getTime());

            SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.HORA_ENTRADA, horaser2);
            editor.commit();

            vaciarTabla();*/


            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    public void alertDialogAgradecimientos(){
        LayoutInflater myLayout = LayoutInflater.from(getApplicationContext());
        View dialogView = myLayout.inflate(R.layout.alertdialog_agradecimientos, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmar);
        ad2 = builder.create();
        ad2.show();
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity(); //final
            }

        });

    }


    public void vaciarTabla(){
        String selection =  Constantes.PENDIENTE_INSERCION + "=?";
        String[] selectionArgs = new String[]{"1"};
        getContentResolver().delete(ContractAsistenciasLocal.CONTENT_URI,selection,selectionArgs);
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

    private void faltaSalida(boolean salida, String fecha_falta_salida) {
        this.falta_salida = salida;
      //  Toast.makeText(this, "Falta salida: "+falta_salida, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constantes.FALTA_SALIDA, salida);
        editor.putString(Constantes.FECHA_FALTA_SALIDA, fecha_falta_salida);
        editor.commit();
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    private void opciones(){
        final CharSequence []opciones={"Abrir Camara","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(MenuNavigationActivity.this);
        alertOpciones.setTitle("Seleccione una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Abrir Camara")) {
                    //permiso();
                    //openCamera();
                    Intent n = new Intent(MenuNavigationActivity.this, CameraActivity.class);
                    n.putExtra("activity", "marcacion");
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

//    //Tomar Foto
//    private void takePhoto(){
//        if (!marshMallowPermission.checkPermissionForCamera()) {
//            marshMallowPermission.requestPermissionForCamera();
//        } else {
//            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
//                marshMallowPermission.requestPermissionForExternalStorage();
//            } else {
//                //Abrir Camera
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
//            }
//        }
//    }
//
//    //SELECT FROM GALLERY
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==RESULT_OK){
//
//            switch (requestCode){
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
//
//    private void cargarImagen() {
//        //final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
//        final CharSequence[] opciones={"Tomar Foto","Cancelar"};
//        final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(this);
//        alertOpciones.setTitle("Seleccione una Opción");
//        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (opciones[i].equals("Tomar Foto")){
//                    tomarFotografia();
//                }else{
//                    if (opciones[i].equals("Cargar Imagen")){
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
//    }
//
//    private void openGallery(){
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
//        if(isCreada==false){
//            isCreada=fileImagen.mkdirs();
//        }
//
//        if(isCreada==true){
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
//        ////
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            String authorities=getApplicationContext().getPackageName()+".provider";
//            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        }else{
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
//        }
//        startActivityForResult(intent,COD_FOTO);
//
//        ////
//    }
//
//    //Permite hacer la imagen mas pequeña
//    public void scaleImage(Bitmap bitmap) {
//        try{
//            obtenerFecha();
//            String ciudad = "Ciudad: " + pdv.getCity();
//            String local = "Local: " + pdv.getPos_name();
//            String usuario = "Usuario: " + user;
//            String fechaHora = "Fecha y hora: " + fecha + " " + hora;
//
////            Point point = new Point(200, 200);
////            Bitmap watermark = applyWaterMarkEffect(bitmap, "Water mark text", 20, 20, Color.GREEN, 80, 50, true);
////            Bitmap watermark = mark(bitmap, ciudad, local, usuario, fechaHora, Color.GREEN, 100, 40, false);
//            ImageMark im = new ImageMark();
//            Bitmap watermark = im.mark(bitmap, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
//            int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
//            imageView.setImageBitmap(scaled);
//            bitmapfinal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
    public String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Guarda en la galería la foto final (con marca de agua) de la marcación de entrada/salida
    private void guardarFotoMarcacionEnGaleria(Bitmap scaled, String registro, String usuario, String fechaser, String horaser) {
        String registroSan = registro.replace(" ", "_");
        String usuarioSan = usuario.replace(" ", "_");
        String fechaSan = fechaser.replace("/", "-");
        String horaSan = horaser.replace(":", "-");

        String fileName = "MARCACION_" + registroSan + "_" + usuarioSan + "_" + fechaSan + "_" + horaSan + ".jpeg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MarcacionPintuco/");
        } else {
            File directory = Environment.getExternalStoragePublicDirectory("MarcacionPintuco");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream output = getContentResolver().openOutputStream(uri)) {
            scaled.compress(Bitmap.CompressFormat.JPEG, 100, output);
            Log.i("guardarFotoMarcacion", "Foto guardada en galería: MarcacionPintuco/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    //Metodo para verificar size de la imagen
//    public String getSizeImage(Bitmap bmp){
//        String mensaje;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        //Comprime la Imagen tipo, calidad y outputstream
//        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
//        byte[] imageBytes = baos.toByteArray();
//        //Convierte a KB:
//        lengthbmp = imageBytes.length/1024;
//        //Solo permite imagenes hasta 243KB
//        if (lengthbmp<600){
//            mensaje = "1";
//        }else{
//            mensaje = "0";
//        }
//        return mensaje;
//    }

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

    public class ListViewAdapterExh extends ArrayAdapter<String> {

        private List<String> sku;
        // private String num_exhibicion;
        public Context context;

        public ListViewAdapterExh(Context context, List<String> sku) {
            super(context, 0, sku);
            this.sku = sku;
            //   this.num_exhibicion = num_exhibicion;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = convertView;

            if (null == convertView) {
                v = inflater.inflate(R.layout.list_row_prioritario, parent, false);
            }

            final TextView lblSku = (TextView) v.findViewById(R.id.lblSku);
//            final EditText lblNumExh = (EditText) v.findViewById(R.id.lblNumExh);
//            final CheckBox btnSave = (CheckBox) v.findViewById(R.id.respuesta);

            if (sku.size() > 0) {
                lblSku.setText(sku.get(position));

            }
            return v;
        }
    }





    private class AsyncTaskBajarOper extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_floo, user);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {}

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MenuNavigationActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Sincronizando");
            progressDialog.setMessage("Validando credenciales, espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }











    public class ListViewAdapterPop extends ArrayAdapter<String> {

        private List<String> popsugerido;
        // private String num_exhibicion;
        public Context context;

        public ListViewAdapterPop(Context context, List<String> popsugerido) {
            super(context, 0, popsugerido);
            this.popsugerido = popsugerido;
            //   this.num_exhibicion = num_exhibicion;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = convertView;

            if (null == convertView) {
                v = inflater.inflate(R.layout.list_row_popsugerido, parent, false);
            }

            final TextView lblPOPSugerido = (TextView) v.findViewById(R.id.lblPOPSugerido);
//            final EditText lblNumExh = (EditText) v.findViewById(R.id.lblNumExh);
//            final CheckBox btnSave = (CheckBox) v.findViewById(R.id.respuesta);

            if (popsugerido.size() > 0) {
                lblPOPSugerido.setText(popsugerido.get(position));

            }
            return v;
        }
    }

//    private void locationStart() {
//        //registerReceiver(new ProximityReciever(), new IntentFilter(ACTION_FILTER));
//        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Localizacion Local = new Localizacion();
//        Local.setMenuNavigationActivity(this);
//        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        final boolean networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (!gpsEnabled) {
//            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(settingsIntent);
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
//            return;
//        }
//        if (networkEnabled) {
//            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
//        }
//        if (gpsEnabled) {
//            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
//        }
//
//        //Setting up My Broadcast Intent
//        Intent i = new Intent(ACTION_FILTER);
//        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, i, PendingIntent.FLAG_MUTABLE);
//
//        //setting up proximituMethod
//        //mlocManager.addProximityAlert(lat1, long1, radius, -1, pi);
//    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                locationStart();
                return;
            }
        }
    }

//    /**
//     * Aqui empieza la Clase Localizacion
//     */
//    public class Localizacion implements LocationListener {
//
//        MenuNavigationActivity MenuNavigationActivity;
//        private String fecha,hora,name;
//
//        public MenuNavigationActivity getMenuNavigationActivity() {
//            return MenuNavigationActivity;
//        }
//
//        public void setMenuNavigationActivity(MenuNavigationActivity MenuNavigationActivity) {
//            this.MenuNavigationActivity = MenuNavigationActivity;
//        }
//
//        @Override
//        public void onLocationChanged(Location loc) {
//            if(!lblLatitud[0].equals(loc.getLatitude())&&!lblLongitud[0].equals(loc.getLongitude())){
//                lblLatitud[0] = ""+loc.getLatitude();
//                lblLongitud[0] = ""+loc.getLongitude();
//            }
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            // Este metodo se ejecuta cuando el GPS es desactivado
//            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
//            LoadName();
//            obtenerFecha();
////            String name = "Ana Pinzon";
//            ContentValues values = new ContentValues();
//            values.put(ContractInsertRastreo.Columnas.USUARIO, name);
//            values.put(ContractInsertRastreo.Columnas.LATITUD, "GPS DESACTIVADO");
//            values.put(ContractInsertRastreo.Columnas.LONGITUD, "GPS DESACTIVADO");
//            values.put(ContractInsertRastreo.Columnas.FECHA, fecha);
//            values.put(ContractInsertRastreo.Columnas.HORA, hora);
//            values.put(Constantes.PENDIENTE_INSERCION, 1);
//
////            Toast.makeText(getApplicationContext(),"Saving Location..",Toast.LENGTH_SHORT).show();
//            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            // Este metodo se ejecuta cuando el GPS es activado
//            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    Log.d("debug", "LocationProvider.AVAILABLE");
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
//                    break;
//            }
//        }
//
//        public void LoadName(){
////            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
//            name = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
//        }
//
//        public void obtenerFecha(){
//            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
//            Date currentLocalTime = cal.getTime();
//            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
//            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//            fecha = date.format(currentLocalTime);
//
//            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
//            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//            hora = hour.format(currentLocalTime);
////            return localTime;
//        }
//    }
//
//    public class ProximityReciever extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String key = LocationManager.KEY_PROXIMITY_ENTERING;
//
//            Boolean entering = intent.getBooleanExtra(key, false);
//
//            if (entering) {
//                Toast.makeText(context, "LocationReminderReceiver entering", Toast.LENGTH_SHORT).show();
//                Log.i("ReceptorProximidad", "entering");
//            } else {
//                Toast.makeText(context, "LocationReminderReceiver exiting", Toast.LENGTH_SHORT).show();
//                Log.i("ReceptorProximidad", "exiting");
//            }
//        }
//    }

}