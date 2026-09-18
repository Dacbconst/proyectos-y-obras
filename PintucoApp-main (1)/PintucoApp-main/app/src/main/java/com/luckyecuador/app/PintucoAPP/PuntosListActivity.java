package com.luckyecuador.app.PintucoAPP;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.Clase.Asistencia;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.Base_pharma_value;
import com.luckyecuador.app.PintucoAPP.Clase.Base_versiones;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
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
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.SignalQuality;
import com.luckyecuador.app.PintucoAPP.Utils.UniqueDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class PuntosListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String ACTION_FILTER = "com.luckyecuador.app.PintucoAPP";

    FloatingActionButton fab;

    String device_id = "";

    AlertDialog ad;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitud_pdv, longitud_pdv, distance_pdv, distance,latitud=0,longitud=0;

    public String supervisor;

    BasePharmaValue pdv2;

    private FusedLocationProviderClient fusedLocationClient;

    String codigo_pdv, user;

    ProgressDialog progressDialog;
    DrawerLayout drawerLayout;

    public Double latitud_insert = 0.0, longitud_insert = 0.0;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem tPdvs, tCalendar, tMapa;
    PagerAdapter adapter;
    ArrayList<Base_versiones> nuevaVersionList;

    private BroadcastReceiver receptorSync;
    private int pdv = 0;
    private int productos = 0;
    private int preguntas = 0;

    private int tests = 0;

    private int tipos_exhibicion = 0;

    private static final int LOCATION_INTERVAL = 0; //3Minutos
    private static final float LOCATION_DISTANCE = 0;

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


    private int tipo_precios = 0;

    private int justificaciones = 0;
    private int versiones = 0;

    private int sincronizado = 0;

    private String operator;

    private String moduloActual;
    private String actualizado;

    TextView nav_header_textView;

    final String[] lblLatitud = {"Latitud"};
    final String[] lblLongitud = {"Longitud"};

    String fecha_sync,fecha;
    String sync;

    DatabaseHelper handler;

    private BatteryLevelReceiver batteryLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_list);
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        LoadData();
        SaveData();
        obtenerFecha();

        // Si venimos de marcar la salida del PDV, el registro de GPS ya se sincronizó
        // explícitamente desde insertDataRegistro(). Disparar aquí otra sincronización
        // (SUBIR_TODO) puede solaparse con esa subida en curso y duplicar el registro
        // de salida en el servidor, ya que ambas sincronizaciones leen las mismas filas
        // marcadas como "en sincronización" antes de que la primera las finalice.
        boolean vieneDeSalidaPdv = getIntent() != null && getIntent().hasExtra("salida_pdv");
        if (!vieneDeSalidaPdv) {
            subirInformacionRetenida();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        batteryLow = new BatteryLevelReceiver();
        registerReceiver(batteryLow, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        new DeveloperOptions().modalDevOptions(PuntosListActivity.this);
        new UniqueDevice().modalUniqueDevice(PuntosListActivity.this, operator);
        RequestPermissions requestPermissions = new RequestPermissions(getApplicationContext(), PuntosListActivity.this);
        requestPermissions.showPermissionDialog();

        uniqueDevice();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablalayout);
        tPdvs = findViewById(R.id.pdv);
        tCalendar = findViewById(R.id.calendar);
        tMapa = findViewById(R.id.mapa);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        // Empieza oculto (nav_menu.xml); se muestra si el backend confirma KYWI.
        verificarUsuarioKywi();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i("fusedLocationClientcd", "ENTRA");
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    //if (modulo != null && modulo.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
                      //  distancia(latitud, longitud);
                    //}
                }
            }
        });


        if (getIntent() != null && getIntent().hasExtra("salida_pdv")) {
            Log.i("asacds", "as");
            String n = getIntent().getStringExtra("salida_pdv");
            double lat = getIntent().getDoubleExtra("latitud",0.0);
            double lon = getIntent().getDoubleExtra("longitud",0.0);
            if (n.equalsIgnoreCase("salida_pdv")) {
                //obtenerCoordenadas();
                registrarInasistencias(lat,lon);
            }
        }

//        Toast.makeText(getApplicationContext(), "ACTUALIZADO: " + actualizado, Toast.LENGTH_SHORT).show();


        if (actualizado.equalsIgnoreCase("NO")) {
            handler.updateCourse();
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.ACTUALIZADO, "SI");
            editor.commit();
        }

        fab = findViewById(R.id.fab);

        navigationView.setNavigationItemSelectedListener(this);

        nav_header_textView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_textView);
        nav_header_textView.setText(operator);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount(), fab);
        pager.setAdapter(adapter);

        showPermissionDialog();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.i("LOCATION 1", "SE EJECUTA");
            startService(new Intent(getApplicationContext(), LocationService.class));
        } else {
            Log.i("LOCATION 2", "SE EJECUTA");
            startForegroundService(new Intent(getApplicationContext(), LocationService.class));
        }

        receptorSync = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                try {
                    final String mensaje = intent.getStringExtra("extra.mensaje");
                    if (mensaje.equalsIgnoreCase(Mensajes.SYNC_FINALIZADA_PDV) ||
                            mensaje.equalsIgnoreCase(Mensajes.SYNC_NOREQUERIDA + "PuntoVenta")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_floo, operator);
                        progressDialog.setMessage("Descargando productos, espere un momento...");
                        pdv = 1;
                        permitido = 1;
                    } else if (mensaje.equals("No se encontraron registros en el servidor para este usuario.")) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            Log.i("PROGRESS_DIALOG", "CIERRE");
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "No se encontraron registros en el servidor para este usuario.", Toast.LENGTH_SHORT).show();
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROD) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Productos")) {
                        productos = 1;
//                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_preg, operator);
//                        progressDialog.setMessage("Descargando test, espere un momento...");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_promociones, operator);
                        progressDialog.setMessage("Descargando promociones, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROMO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Promociones")) {
                        promociones = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_exhibicion, operator);
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
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_precios_pvc, operator);
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
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_mci, operator);
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
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_precios, operator);
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
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_justificaciones, operator);
                        progressDialog.setMessage("Descargando justificaciones, espere un momento...");

//                        verificarLogin(user, pass);
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_JUST) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Justificación") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        justificaciones = 1;
                        Log.i("entra justificacion","entra justificacion");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_versiones, operator);
                        progressDialog.setMessage("Descargando Causales Productos en mal estado, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_VERSIONES) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Versiones") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        versiones = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tiempo_almuerzo, operator);
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIEMPO_ALMUERZO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tiempo Almuerzo") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        tiempo_almuerzo = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_prod_mal_est, operator);
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_PROD_MAL_EST) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "CausalesProdMalEst") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        causales_prod_mal_est = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_rangos_precios, operator);
                        progressDialog.setMessage("Descargando Rangos de Precios, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "RangosPrecios") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_rangos_precios_sku, operator);
                        progressDialog.setMessage("Descargando Rangos de Precios por SKU, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS_SKU) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "RangosPreciosSku") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_asistencia, operator);
                        progressDialog.setMessage("Descargando causales de asistencia, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_asistencia_atraso, operator);
                        progressDialog.setMessage("Descargando causales de asistencia atraso, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA_ATRASO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia Atraso") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_contactos, operator);
                        progressDialog.setMessage("Descargando Contactos, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CONTACTOS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Contactos")) {
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_proforma, operator);
                        progressDialog.setMessage("Descargando Proforma, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PROFORMA) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Proforma")) {
                        progressDialog.dismiss();
                        new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Descarga de información - (Listado de los Pdv's)");
                        Toast.makeText(getApplicationContext(), Mensajes.SYNC_FINALIZADA, Toast.LENGTH_SHORT).show();
                    }

                    Log.i("PDV", pdv + "");
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

//        new AlertChangeTime(PuntosListActivity.this);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImplementacionActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
//                startActivity(intent);
            }
        });

        //Verificar los permisos
    /*    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }*/// else {
        //     startService(new Intent(getApplicationContext(), MyService.class));
//        locationStart();
        //Si los permisos estan otorgados, llamar al evento onClick del boton
        //clickButton();
        //}

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        /*locationListener = new LocationListener() {
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
        };*/
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {

                    if (ControlesGps.esUbicacionFalsa(location)) {
                        mostrarAlertaFakeGps();
                        return;
                    }

                    latitud_insert = location.getLatitude();
                    longitud_insert = location.getLongitude();
                    locationManager.removeUpdates(locationListener);

                } catch (Exception e) {
                    Log.e("MenuNavigationActivity", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };

        if (ControlesGps.esDispositivoRoot()) {
            mostrarAlertaRoot();
            return; // detenemos ejecución
        }

        checkFechayHora();
    //    SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);

        verificarNuevaVersion();
      // alertDialogNuevaVersion();
        obtenerCoordenadas();
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

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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

    private void verificarNuevaVersion() {

        // Obtenemos la version actual
        String versionStr = getString(R.string.version);
        String fechaVersionActual = "";

        if (versionStr.contains(": ")) {
            fechaVersionActual = versionStr.split(": ")[1];
        } else if (versionStr.contains(":")) {
            fechaVersionActual = versionStr.split(":")[1];
        } else {
            fechaVersionActual = versionStr;
        }

        nuevaVersionList = handler.getNuevaVersion(fechaVersionActual);

        // Si existe una nueva version
        if (nuevaVersionList.size() > 0){

            // Crear y configurar el ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Cargando");
            progressDialog.setMessage("Por favor espera...");
            progressDialog.setCancelable(false); // Evitar que el usuario lo cierre manualmente
            progressDialog.show(); // Mostrar el ProgressDialog

            // Subimos toda la informacion que tenga retenida
            SyncAdapter.sincronizarAhora(PuntosListActivity.this, true, Constantes.SUBIR_TODO, null);


            Handler handler = new Handler();

            // Programar el cierre del ProgressDialog después de 5 segundos
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Cerrar el ProgressDialog
                    if (progressDialog.isShowing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    alertDialogNuevaVersion(nuevaVersionList);
                }
            }, 5000); // 5 segundos

        }



    }

    public void alertDialogNuevaVersion(ArrayList<Base_versiones> nuevaVersionList){

        LayoutInflater myLayout = LayoutInflater.from(this);
        View dialogView = myLayout.inflate(R.layout.alertdialog_nueva_version, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Obtenemos informacion de la version
        String fecha_nueva_version = nuevaVersionList.get(0).getFecha_version();
        String parts[] = fecha_nueva_version.split("-");
        // Convertimos a dd/mm/yyyy para mostrarlo en msg
        String formatoddmmyyyy = parts[2] + "/" + parts[1] + "/" + parts[0];
        String link_aplicativo = nuevaVersionList.get(0).getLink_aplicativo();
        String tamañoApk = nuevaVersionList.get(0).getTamaño();

        if (tamañoApk == null){
            tamañoApk = "No Disponible";
        }

        Button btnDescargar = dialogView.findViewById(R.id.btnDescargar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        TextView tv_nueva_version = dialogView.findViewById(R.id.tv_nueva_version);
        TextView tv_tamaño = dialogView.findViewById(R.id.tv_tamanio);

        tv_nueva_version.setText(formatoddmmyyyy);
        tv_tamaño.setText(tamañoApk);


        AlertDialog ad = builder.create();
        ad.show();

        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogConfirmNuevaVersion(link_aplicativo);
            }

        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

    }

    private void alertDialogConfirmNuevaVersion(String link_aplicativo) {
        LayoutInflater myLayout = LayoutInflater.from(this);
        View dialogView = myLayout.inflate(R.layout.alertdialog_conf_nueva_version, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);


        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancel);

        AlertDialog ad2 = builder.create();
        ad2.show();

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse(link_aplicativo);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                ad2.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad2.dismiss();
            }
        });

    }


    public void uniqueDevice() {

        handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);
        device_id = getDeviceId(getApplicationContext());
        Log.i("DEVICE_ID", device_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(PuntosListActivity.this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
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

                new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Cierre de Sesión");
                ClufActivity.signed = 0;
                SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                sessionManagement.removeSession();
                moveToLogin();


            }
        });
        ad = builder.create();

        boolean isUserDevice = handler.esDispositivoDelUsuario(device_id);

        if (!isUserDevice) {
            if (VerificarNet.hayConexion(getApplicationContext()) && (operator!=null || !operator.trim().isEmpty())) {
                getUserDevice(PuntosListActivity.this, operator);
            }
            //ad.show();
        } else {
            if (ad.isShowing()) {
                ad.dismiss();
            }

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
                        //ad.show();
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
    protected void onStart() {
        super.onStart();
      //  obtenerCoordenadas();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.dashboard, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager) PuntosListActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(PuntosListActivity.this.getComponentName()));
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

    private void subirInformacionRetenida() {
        // Obtenemos la calidad de señal de red sea por Wifi o Datos
        String señal = new SignalQuality(getApplicationContext()).getSignalQuality();
        Log.i("Señal","Calidad de la señal de Internet: "+ señal);

        if (señal.equalsIgnoreCase("Buena") || señal.equalsIgnoreCase("Excelente")){
            //subir informacion retenida automaticamente
            SyncAdapter.sincronizarAhora(PuntosListActivity.this, true, Constantes.SUBIR_TODO, null);
            Log.i("Noti","Se envio la informacion retenida");
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        Log.i("PERMISOS 2", "PERMISOS 2");
        if (context != null && permissions != null) {
            Log.i("PERMISOS 3", "PERMISOS 3");
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("PERMISO", permission + " DENEGADO");
                    return false;
                }
            }
        }
        return true;
    }

    public void registrarInasistencias(Double latitud, Double longitud){
        ArrayList<Asistencia> asistencias = handler.getNoRegistradosFaltantes();
        //aqui ya se traen los registros que quedaron en false que son de horas menos de la actual
        //po ende se registraran al servidor como no validados
        for(int i = 0 ;i<asistencias.size(); i++){
            insertDataRegistroAsistencias(latitud,longitud,asistencias.get(i).getHora());
        }
    }

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

                pdv2 = handler.getPdv(codigo_pdv);
                distance_pdv = Double.parseDouble(pdv2.getDistancia());
                latitud_pdv = Double.parseDouble(pdv2.getLatitud());
                longitud_pdv = Double.parseDouble(pdv2.getLongitud());

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
            values.put(ContractInsertAsistencia.Columnas.FOTO, "");
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

    private void showPermissionDialog() {
        Log.i("PERMISOS", "PERMISOS");
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS
            };
            // If you have access to the external storage, do whatever you need
            if (Environment.isExternalStorageManager()){
                // If you don't have access, launch a new activity to show the user the system's dialog
                // to allow access to the external storage
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS
            };
        } else {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS
            };
        }

//        String[] PERMISSIONS = {
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                android.Manifest.permission.INTERNET,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.CAMERA,
//                android.Manifest.permission.SEND_SMS
//        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.mCargar) {
            if (VerificarNet.hayConexion(this)) {
                try {
                    new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Subida de información retenida - (Listado de los Pdv's)");
                    SyncAdapter.sincronizarAhora(this, true, Constantes.SUBIR_TODO, null);
                    Snackbar.make(findViewById(R.id.coordinatorPos), Mensajes.ON_SYNC_UP, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.coordinatorPos), Mensajes.ERROR_RED, Snackbar.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.mDescargar) {
            if (VerificarNet.hayConexion(this)) {
                if (!operator.equals("")) {
                    AsyncTaskBajarOper bajarOper = new AsyncTaskBajarOper();
                    bajarOper.execute();
                }else{
                    Toast.makeText(getApplicationContext(),Mensajes.SYNC_NO_USER ,Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(drawerLayout, Mensajes.ERROR_RED , Snackbar.LENGTH_LONG).setAction("Action", null).show();//                Toast.makeText(getApplicationContext(),"No hay conexión de Internet.",Toast.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.mVersion) {
            mostrarVersion();
        }

        if (item.getItemId() == R.id.mMateriales) {
            Intent intent = new Intent(PuntosListActivity.this, MaterialesRecibidosActivity.class);
            startActivity(intent);
        }

        /*
        if (item.getItemId() == R.id.mEjecucion) {
            Intent intent = new Intent(PuntosListActivity.this, EjecucionMaterialesActivity.class);
            startActivity(intent);
        }*/

        if (item.getItemId() == R.id.mTest) {

            Intent intent=new Intent(PuntosListActivity.this, StartQuizActivity.class);
            startActivity(intent);


/*
            String canal = handler.getChannelSegmentPreguntas(operator);

            ArrayList<Base_tests> listArrayQuiz = new ArrayList<>();
            listArrayQuiz = (ArrayList<Base_tests>) handler.getAllQuizByCanal(canal,operator);

            Log.i("CANAL", canal);

            if (listArrayQuiz.size() > 0) {
                Intent intent=new Intent(PuntosListActivity.this, StartQuizActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(drawerLayout, "No hay un test asignado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
*/
        }
        if (item.getItemId() == R.id.mStatus) {
            Intent intent = new Intent(PuntosListActivity.this, HistorialActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mLineaTiempo) {
            new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Ingreso al Linea de tiempo");
            Intent intent = new Intent(PuntosListActivity.this, TimelineActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mProyectosObras) {
            Intent intent = new Intent(PuntosListActivity.this, ProyectosObrasActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mStatusGeneral) {
            new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Ingreso al Status General");
            Intent intent = new Intent(PuntosListActivity.this, General.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mFotografico) {
            Intent intent = new Intent(PuntosListActivity.this, FotograficoPdvActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.mRelevoTardio) {
            new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Ingreso a Relevo tardio");
            Intent intent = new Intent(PuntosListActivity.this, RelevoTardioActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mNotificaciones) {
            new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Ingreso a otificaciones");
            Intent intent = new Intent(getApplicationContext(), NotificacionesActivity.class);
            startActivity(intent);
        }


        if (item.getItemId() == R.id.mAlmuerzo) {
            // Verificar si ya uso su tiempo de almuerzo
            Boolean tiene_almuerzo = handler.tieneAlmuerzo(operator,fecha);

            //    if (!tiene_almuerzo){
            Intent intent = new Intent(PuntosListActivity.this, AlmuerzoActivity.class);
            startActivity(intent);
            //    } else {
            //        Toast.makeText(getApplicationContext(),"Ya ha utilizado su tiempo de almuerzo",Toast.LENGTH_LONG).show();
            //    }


        }



        if (item.getItemId() == R.id.mPortafolio) {
            Intent intent = new Intent(getApplicationContext(), PortafolioPDFActivity.class);
            startActivity(intent);
        }


        if (item.getItemId() == R.id.mCerrarSesion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Cerrar Sesión");
            builder.setMessage("¿Desea cerrar sesión?");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Cierre de Sesión");
                    ClufActivity.signed = 0;
                    SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                    sessionManagement.removeSession();
                    moveToLogin();
                }
            });

            builder.setNeutralButton("NO",null);

            AlertDialog ad = builder.create();
            ad.show();

            Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
            Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        }
        return false;
    }

    public void mostrarVersion() {

        String fecha = getString(R.string.version).split(": ")[1];
        String tamaño = handler.getTamañoVersion(fecha);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_warning)
                .setTitle(getString(R.string.action_version))
                .setMessage(getString(R.string.user) + ": " + operator + "\n" +
                            getString(R.string.version) + "\n" +
                            "Tamaño: " + tamaño)
                .setPositiveButton("ACEPTAR", null)
                .show();
    }

    public void obtenerFecha() {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);

    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        operator = sharedPreferences.getString(Constantes.USER, Constantes.NODATA).toUpperCase();
        moduloActual = sharedPreferences.getString(Constantes.MODULO_ACTUAL, Constantes.NODATA).toUpperCase();
        actualizado = sharedPreferences.getString(Constantes.ACTUALIZADO, "NO").toUpperCase();
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);
        fecha_sync = sharedPreferences.getString(Constantes.FECHA_SYNC, Constantes.NODATA);
        sync = sharedPreferences.getString(Constantes.SYNC, Constantes.NODATA);
    }

    // Consulta get_usuario_kywi.php (lvi_rutero) para decidir si mostrar "Proyectos y Obras".
    private void verificarUsuarioKywi() {
        String url = Constantes.GET_USUARIO_KYWI + "?user=" + Uri.encode(user);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    boolean esKywi = response.optBoolean("es_kywi", false);
                    navigationView.getMenu().findItem(R.id.mProyectosObras).setVisible(esKywi);
                },
                error -> Log.e("KywiCheck", "Error consultando get_usuario_kywi.php", error));

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void SaveData() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.MODULO_ACTUAL,Constantes.MODULO_PUNTOS_PRINCIPAL);
        editor.commit();
    }


    private void moveToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Intent.ACTION_SYNC);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorSync, filtroSync);
        new DeveloperOptions().modalDevOptions(PuntosListActivity.this);
        new UniqueDevice().modalUniqueDevice(PuntosListActivity.this, operator);
      //  checkFechayHora();
      //  SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorSync);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea salir de la aplicación?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar aplicativo
                try {
//                    stopLocationService();
                    new GuardarLog(getApplicationContext()).saveLog(operator, "", "Cierre de la Aplicación");
                    moveTaskToBack(true); // I don't think you're looking for this.
                } catch (Exception e) {
                    Log.i("Salir", e.getMessage());
                }
            }
        });

        builder.setNeutralButton("NO",null);

        AlertDialog ad = builder.create();
        ad.show();

        Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        //pButton.setTextColor(Color.rgb(79, 195, 247));
        Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        //cButton.setTextColor(Color.rgb(79, 195, 247));
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
            editor.putString(Constantes.FECHA_SYNC, "");
            editor.putString(Constantes.HORA_SYNC, "");
            editor.commit();
        }else{
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.SYNC, "NO");
            editor.putString(Constantes.FECHA_SYNC, "");
            editor.putString(Constantes.HORA_SYNC, "");
            editor.commit();
        }

        new GuardarLog(PuntosListActivity.this).saveLog(operator, "", "Cierre de Sesión");
        ClufActivity.signed = 0;
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.removeSession();
        moveToLogin();
    }








//    private void stopLocationService() {
//        try {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                Intent intent = new Intent(PuntosListActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startService(intent);
//                //startService(new Intent(getApplicationContext(), LocationService.class));
//            } else {
//                Intent intent = new Intent(PuntosListActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startForegroundService(intent);
//            }
//        }catch (Exception e) {
//            Log.i("LocationService",e.getMessage());
//        }
//    }

    private class AsyncTaskBajarOper extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_Oper, operator);
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
            progressDialog = new ProgressDialog(PuntosListActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Sincronizando");
            progressDialog.setMessage("Validando credenciales, espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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

}