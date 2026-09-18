package com.luckyecuador.app.PintucoAPP;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.material.snackbar.Snackbar;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreciosPvc;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Notificaciones.AlarmNotificationReceiver;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.AsistenciaService;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService;
import com.luckyecuador.app.PintucoAPP.Session.SessionManagement;
import com.luckyecuador.app.PintucoAPP.Session.User;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    public static String MY_PREFS_NAME= "nameOfSharedPreferences";

    public String tiempo_base = "";

    String device_id;

    AlertDialog ad;

    boolean valor = true;


    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    DatabaseHelper handler;
    private EditText txtUser;
    private EditText txtPass;
    private Button btnIngresar;
    private String user, pass, name;
    String operator;

    private static final String TAG ="LoginActivity";



    String sync, fechaSync;
    private ImageButton btnSincronizar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor mEditor;

    private BroadcastReceiver receptorSync;
    LocationService LocationService = new LocationService();
    MarshMallowPermission marshMallowPermission;

    private boolean falta_salida;
    private String fecha_falta_salida;

    private int pdv = 0;
    private int productos = 0;
    private int preguntas = 0;

    private int tests = 0;
    private int promociones = 0;
    private int permitido = 0;
    private int suma = 0;
    private int rotacion = 0;
    private int tareas = 0;
    private int tiempo_almuerzo = 0;

    private int precios_pvc = 0;

    private int tipos_exhibicion = 0;

    private int justificaciones = 0;
    private int popsugerido = 0;
    private int prioritario = 0;
    private int combo_canjes = 0;
    private int causales_mci = 0;
    private int materiales_alertas = 0;
    private int pdi = 0;
    private int tipo_precios = 0;
    private int causales_prod_mal_est = 0;
    private int marcas_blancas = 0;
    private int versiones = 0;


    private int sincronizado = 0;

    private BatteryLevelReceiver batteryLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        batteryLow = new BatteryLevelReceiver();
        registerReceiver(batteryLow,new IntentFilter(Intent.ACTION_BATTERY_LOW));

        //setToolbar();
        Context context= this.getApplicationContext();
        LoadData();



        new DeveloperOptions().modalDevOptions(LoginActivity.this);
        RequestPermissions requestPermissions = new RequestPermissions(getApplicationContext(), LoginActivity.this);
        requestPermissions.showPermissionDialog();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        btnSincronizar = (ImageButton) findViewById(R.id.ib_sincronizacion);
        txtUser = (EditText) findViewById(R.id.txt_usuario);
        txtPass = (EditText) findViewById(R.id.txt_clave);

        sharedPref = context.getSharedPreferences("Nombre de Local", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sharedPref.edit();

        checkSharedPreferences();

        if (!txtUser.getText().toString().trim().isEmpty()) {
            txtPass.requestFocus();
        }else{
            txtUser.requestFocus();
        }

        showPermissionDialog();
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLogin);

        txtUser.setOnKeyListener(this);
        txtPass.setOnKeyListener(this);
        btnIngresar = (Button) findViewById(R.id.btn_empezar);
        btnIngresar.setOnClickListener(this);
        btnSincronizar.setOnClickListener(this);
        marshMallowPermission = new MarshMallowPermission(this);

        if (!marshMallowPermission.checkPermissionForReadPhoneState()) {
            marshMallowPermission.requestPermissionForReadPhoneState();
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
                        String supervisor = handler.getSupervisorByUser(user);
                        SaveData(operator, supervisor);
                    } else if (mensaje.equals("No se encontraron registros en el servidor para este usuario.")) {
                        progressDialog.dismiss();
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
                        progressDialog.setMessage("Descargando tipos de exhibicion, espere un momento...");
                   }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TESTS) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tests") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        tests = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_preg, operator);
                        progressDialog.setMessage("Descargando tipos de exhibiciones, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PREG) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Preguntas") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        preguntas = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_exhibicion, operator);
                        progressDialog.setMessage("Descargando tests, espere un momento...");
                    }

*/

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIPO_EXH) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tipo Exhibicion") || mensaje.equals("No se encontraron registros en el servidor.") ) {
                        tipos_exhibicion = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_precios_pvc, operator);
                        progressDialog.setMessage("Descargando Precios Pvc, espere un momento...");
                    }

/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_ROTACION) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Rotacion") ||
                        mensaje.equals("No se encontraron registros en el servidor SMS.") ||
                        mensaje.equals("No se encontraron registros en el servidor.")) {
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
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Pop_sugerido") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        popsugerido = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_prioritario, operator);
                        progressDialog.setMessage("Descargando prioritarios, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PRIORITARIO) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Portafolio Prioritario") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        prioritario = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_combo_canjes, operator);
                        progressDialog.setMessage("Descargando canjes, espere un momento...");
                    }*/

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PRECIOS_PVC) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Precios Pvc") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        precios_pvc = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_mci, operator);
                        progressDialog.setMessage("Descargando causales MCI, espere un momento...");
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_MCI) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales MCI") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        causales_mci = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_causales_osa, operator);
                        progressDialog.setMessage("Descargando causales osa, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_OSA) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales OSA") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        causales_mci = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_materiales_alertas, operator);
                        progressDialog.setMessage("Descargando materiales alertas, espere un momento...");
                    }

                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_MATERIALES_ALERTAS) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Materiales Alertas") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        materiales_alertas = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_precios_pvc, operator);
                        progressDialog.setMessage("Descargando precios pvc, espere un momento...");
                    }
*/
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_CAUSALES_MCI) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Causales MCI") || mensaje.equalsIgnoreCase("No se encontraron registros en el servidor.")) {
                        causales_mci = 1;
                        Log.i("entra tipo causales","entra tipo causales");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_precios, operator);
                        progressDialog.setMessage("Descargando tipo precios, espere un momento...");
//                        verificarLogin(user, pass);
                    }
/*
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_PDI) ||
                        mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "PDI") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        pdi = 1;
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_tipo_precios, operator);
                        progressDialog.setMessage("Descargando tipo precios, espere un momento...");
                    }
*/
                    if (mensaje.equals(Mensajes.SYNC_FINALIZADA_TIPO_PRECIO) ||
                            mensaje.equals(Mensajes.SYNC_NOREQUERIDA + "Tipo_precios") || mensaje.equals("No se encontraron registros en el servidor.")) {
                        tipo_precios = 1;
                        Log.i("entra tipo precios","entra tipo precios");
                        SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_justificaciones, operator);
                        progressDialog.setMessage("Descargando justificaciones, espere un momento...");
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
                        Log.i(TAG, "CausalesProdMalEst finalizado. Iniciando descarga de Rangos de Precios...");
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
                        progressDialog.setMessage("Validando información, espere un momento...");
                        modalUniqueDevice(user);
                        new GuardarLog(LoginActivity.this).saveLog(user, "", "Descarga de información - (Login)");
                        Toast.makeText(getApplicationContext(), Mensajes.SYNC_FINALIZADA, Toast.LENGTH_SHORT).show();
                    }

                    Log.i("PDV", pdv + "");
                    Log.i("PRODUCTOS", productos + "");
                    Log.i("PROMOCIONES", promociones + "");
                //    Log.i("TESTS", tests + "");
                //    Log.i("PREGUNTAS", preguntas + "");
                    Log.i("TIPOS DE EXHIBICIONES", tipos_exhibicion + "");
                //    Log.i("ROTACION", rotacion + "");
                 //   Log.i("TAREAS", tareas + "");
                //    Log.i("POPSUGERIDO", popsugerido + "");
                //    Log.i("PRIORITARIO", prioritario + "");
                //    Log.i("COMBO_CANJES", combo_canjes + "");
                //    Log.i("CAUSALES_MCI", causales_mci + "");
                //    Log.i("MATERIALES_ALERTAS", materiales_alertas + "");
                    Log.i("PRECIOS_PVC", precios_pvc + "");
                //    Log.i("PDI", pdi + "");
                    Log.i("TIPO PRECIO", tipo_precios + "");
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
        Log.i("DEVICE ID", getDeviceId(getApplicationContext()));
    }

    public void modalUniqueDevice(String user) {
        this.user = user;

        Log.i("USER_DEVICE", user);
        handler = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);
        device_id = getDeviceId(getApplicationContext());
        Log.i("DEVICE_ID", device_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("Sesión única");
        builder.setMessage("Este dispositivo no está atado a su usuario, por favor, contáctese con su supervisor");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                ad.dismiss();
            }
        });
        ad = builder.create();

        String isUserDevice = handler.getDispositivoDelUsuario(user);
        Log.i("USER_DEVICE2", "Codigo es "+isUserDevice);
        if (!isUserDevice.equals("")){
            if (isUserDevice.equals(device_id)) {
                Log.i("USER_DEVICE2", "mismo codigo");
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }

                //return "no existe";
            } else {
                if (ad.isShowing()) {
                    ad.dismiss();
                }
                Log.i("USER_DEVICE2", "codigo diferente");
                //return "existe";
                ad.show();
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }

                valor = false;
            }
        }else{
            Log.i("USER_DEVICE2", "Enviar a actualizar, no existe codigo aun " + user);
            //new UniqueDevice().UpdateUserDevice(LoginActivity.this,user,device_id);
            UpdateUserDevice(LoginActivity.this,user,device_id);
            if (progressDialog != null){
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

        }

    }

    public void UpdateUserDevice(Activity activity, String user, String device_id){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("user", user);
            map.put("device_id", device_id);
            Log.i("RESPdsONSE 3 ", "entro 3 usurio:" + user + "clave" + device_id);
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
                Log.i("RESPONSE 5 ", "entro estado"+estado);
                switch (estado){
                    case "1": //EXITO
                        Toast.makeText(activity, "Se ha enlazado su usuario con este dispositivo", Toast.LENGTH_SHORT).show();
                        String mensaje = response.getString("mensaje");
                        Log.i("resultadodexa","msj: "+mensaje);
                        ContentValues values = new ContentValues();
                        values.put(ContractPharmaValue.Columnas.DEVICE_ID, device_id);
                        new TareaEditarDeviceId(activity.getApplicationContext().getContentResolver(), values, user).execute(ContractPharmaValue.CONTENT_URI);
                        /*for(int i = 0; i < mensaje.length(); i++){
                            JSONObject jb1 = mensaje.getJSONObject(i);
                            //String id = jb1.getString("device_id");
                            Log.i("resultado",""+jb1.toString());
                        }*/
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("resultadodexa", mensaje2);
                        /*ad.show();
                        valor = false;*/
                        break;
                    case "3": //FALLIDO
                        String mensaje3 = response.getString("mensaje");
                        Log.i("resultadodexa", mensaje3);
                        ad.show();
                        valor = false;

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

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void checkSharedPreferences() {
        name = sharedPref.getString(getString(R.string.name),"");
        txtUser.setText(name);
    }

    public void getTiempo(Activity activity, String user, String id_pdv){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", user);
            map.put("id_pdv", id_pdv);
            Log.i("RESPONSE 3 ", "entro 3" + user +" - 0" + id_pdv);
            JSONObject jobject = new JSONObject(map);

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
                        JSONArray mensaje = response.getJSONArray("evidencias");
                        Log.i("resultadode",""+mensaje);
                        JSONObject jb1 = mensaje.getJSONObject(0);
                        //String id = jb1.getString("device_id");
                        tiempo_base = jb1.getString("tiempo");
                        Log.i("ttfr",""+tiempo_base);
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

//    private void stopLocationService() {
//        try {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                Intent intent = new Intent(LoginActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startService(intent);
//                //startService(new Intent(getApplicationContext(), LocationService.class));
//            } else {
//                Intent intent = new Intent(LoginActivity.this, LocationService.class);
//                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
//                startForegroundService(intent);
//            }
//        }catch (Exception e){
//            Log.i("LocationService",e.getMessage());
//        }
//    }

    private boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i.next();
            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                serviceRunning = true;
                if(runningServiceInfo.foreground){
                    //service run in foreground
                    Log.i("SERVICE","SI");
                }
            }
        }
        return serviceRunning;
    }


    public void consultarPvc(){
        try{
            HashMap<String, String> map = new HashMap<>(); //
            //map.put("cod_tarea", "6BYM3");
            map.put("usuario",user);
            //Log.i("usuariosl",""+punto_venta);
            // Crear nuevo objeto Json basado en el mapa
            JSONObject jobject = new JSONObject(map);

            //GET METHOD
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "https://webecuador.azurewebsites.net/App/AppPintuco/Web/get_productos_pvc.php?usuario="+user, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    respuestaPvc(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError) {
                                Toast.makeText(LoginActivity.this, Mensajes.TIME_OUT,Toast.LENGTH_SHORT).show();
                                Log.i("apro","entro2");
                            } else if (error instanceof NoConnectionError) {
                                //TODO
                                Toast.makeText(LoginActivity.this, Mensajes.NO_RED,Toast.LENGTH_SHORT).show();
                                Log.i("apro","entro3");
                            }else if (error instanceof AuthFailureError) {
                                Log.i("apro","entro4");
                                //TODO
                            } else if (error instanceof ServerError) {
                                Log.i("apro","entro5");
                                //TODO
                                Toast.makeText(LoginActivity.this, Mensajes.SEVER_ERROR,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Log.i("apro","entro6");
                                //TODO
                                Toast.makeText(LoginActivity.this,Mensajes.RED_ERROR,Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Log.i("apro","entro7");
                                Log.i("exhs",""+error.getMessage());
                                //TODO
                            }
                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
        }catch (Exception e){
            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void respuestaPvc(JSONObject response){
        if (response!=null){
            try{
                //Obtener atributo estado
                String estado = response.getString("estado");
                switch (estado){
                    case "1": //EXITO

                        DatabaseHelper dHelper = new DatabaseHelper(getApplicationContext(), Provider.DATABASE_NAME, null, 1);
                        SQLiteDatabase db = dHelper.getWritableDatabase();


                        // Elimina si se encuentra registros
                        dHelper.eliminarPrductosPvc();


                        JSONArray mensaje = response.getJSONArray("productos_pvc");

                        for(int i=0;i<mensaje.length();i++){

                            JSONObject jb1 = mensaje.getJSONObject(i);

                            // Guardando Datos de manera Local
                            ContentValues values = new ContentValues();
                            values.put(ContractPreciosPvc.Columnas.USUARIO, jb1.getString("usuario"));
                            values.put(ContractPreciosPvc.Columnas.FECHA,jb1.getString("fecha"));
                            values.put(ContractPreciosPvc.Columnas.CODIGO, jb1.getString("codigo_pdv"));
                            values.put(ContractPreciosPvc.Columnas.CATEGORIA, jb1.getString("categoria"));
                            values.put(ContractPreciosPvc.Columnas.SUBCATEGORIA,jb1.getString("subcategoria"));
                            values.put(ContractPreciosPvc.Columnas.MARCA, jb1.getString("marca"));//Extra
                            values.put(ContractPreciosPvc.Columnas.SKU, jb1.getString("sku"));//Extra
                            values.put(ContractPreciosPvc.Columnas.PVC,jb1.getString("pvc"));

                            db.insert(ContractPreciosPvc.PRECIOS_PVC, null,values);

                        }
                        Log.i("productospvc",""+mensaje);
                        progressDialog.dismiss();
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("apro","entro22");
                        Toast.makeText(LoginActivity.this, mensaje2,Toast.LENGTH_LONG).show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }else{
            Toast.makeText(LoginActivity.this,"VACIO",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea salir de la aplicación?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar aplicativo
                try {
//                    stopLocationService();
                    moveTaskToBack(true); // I don't think you're looking for this.
                } catch (Exception e){
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

    /*public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }*/

    @Override
    public void onClick(View view) {
        if (view==btnIngresar) {
            if (esFormularioValido()) {
                user = limpiarTexto(txtUser.getText().toString().toUpperCase().trim());
                pass = txtPass.getText().toString().trim();
                verificarLogin(user, pass);
            }
        }
        if (view==btnSincronizar) {
            sincronizar();
        }
    }

    private boolean esFormularioValido() {
        if (txtUser.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar un usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtPass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

//    public void verificarLogin(String user1) {
//        try{
//            handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
//            boolean resp = handler.verificarLogin(user1);
//            if (resp) {
//                if (pass.equals("1234")) {
//                    String name = txtUser.getText().toString();
//                    mEditor.putString(getString(R.string.name),name);
//                    if (!this.name.equalsIgnoreCase(name)) {
//                        mEditor.putString(getString(R.string.cluf), "0");
//                    }
//                    mEditor.commit();
//                    Intent intent = new Intent(LoginActivity.this, ClufActivity.class);
//                    intent.putExtra("menu", "0");
//                    startActivity(intent);
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                        startService(new Intent(getApplicationContext(), MyService.class));
//                    } else {
//                        startForegroundService(new Intent(getApplicationContext(), MyService.class));
//                    }
//                    startAlarm(true,true);
//                }else{
//                    String name = txtUser.getText().toString();
//                    mEditor.putString(getString(R.string.name),"");
//                    mEditor.commit();
//                    Toast.makeText(this,Mensajes.ERROR_LOGIN,Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Toast.makeText(this,Mensajes.ERROR_LOGIN,Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e) {
//            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String userID = sessionManagement.gerSession();

        if (!userID.trim().isEmpty()) {
            moveToMainActivity();
        }
    }

    public void verificarLogin(String user1, String pass) {
        try {
            handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
            /*if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_Oper, operator);
            }*/
//            boolean resp = handler.verificarLogin(user1, pass);
//            if (resp) {
//               String name = txtUser.getText().toString();
//             mEditor.putString(getString(R.string.name),name);
//               if (!this.name.equalsIgnoreCase(name)) {
//                   mEditor.putString(getString(R.string.cluf), "0");
//             }
//                mEditor.commit();
//                new GuardarLog(LoginActivity.this).saveLog(name, "", "Login correcto");
//
//               User user = new User(name);
//                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
//               sessionManagement.saveSession(user);
//                Intent intent = new Intent(LoginActivity.this, ClufActivity.class);
//               intent.putExtra("menu", "0");
//               intent.putExtra("user", this.user);
//                startActivity(intent);

             /*   if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    startService(new Intent(getApplicationContext(), MyService.class));
                } else {
                    startForegroundService(new Intent(getApplicationContext(), MyService.class));
                }
                startAlarm(true,true);*/
//           }else{
//               String name = txtUser.getText().toString();
//                mEditor.putString(getString(R.string.name),"");
//                mEditor.commit();
//                Toast.makeText(this,Mensajes.ERROR_LOGIN,Toast.LENGTH_SHORT).show();
//           }


            boolean resp = handler.verificarLogin(user1, pass);
            Log.i("logincc",""+ fechaSync);
            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            fechaSync = sharedPreferences.getString(Constantes.FECHA_SYNC, Constantes.NODATA);
            if(fechaSync.equals("") || fechaSync.equals("NoDisponible") || fechaSync == null){
                Toast.makeText(this, "Primero debe sincronizar", Toast.LENGTH_SHORT).show();
            }else{
                if (resp) {
                    String name = txtUser.getText().toString();
                    mEditor.putString(getString(R.string.name),name);
                    if (!this.name.equalsIgnoreCase(name)) {
                        mEditor.putString(getString(R.string.cluf), "0");
                    }
                    mEditor.commit();

                    new GuardarLog(LoginActivity.this).saveLog(name, "", "Login correcto");

                    User user = new User(name);
                    SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                    sessionManagement.saveSession(user);

                    Intent intent = new Intent(LoginActivity.this, ClufActivity.class);
                    intent.putExtra("menu", "0");
                    intent.putExtra("user", this.user);
                    startActivity(intent);

             /*   if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    startService(new Intent(getApplicationContext(), MyService.class));
                } else {
                    startForegroundService(new Intent(getApplicationContext(), MyService.class));
                }
                startAlarm(true,true);*/
                }else{
                    String name = txtUser.getText().toString();
                    mEditor.putString(getString(R.string.name),"");
                    mEditor.commit();
                    Toast.makeText(this,Mensajes.ERROR_LOGIN,Toast.LENGTH_SHORT).show();
                }
            }








        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA, false);
        fecha_falta_salida = sharedPreferences.getString(Constantes.FECHA_FALTA_SALIDA, Constantes.NODATA);
        sync = sharedPreferences.getString(Constantes.SYNC, Constantes.NODATA);
        fechaSync = sharedPreferences.getString(Constantes.FECHA_SYNC, Constantes.NODATA);
    }

    public void guardarFecha(){

        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String HoraActual = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.FECHA_SYNC, fechaActual);
        editor.putString(Constantes.HORA_SYNC, HoraActual);
        /*if(!sync.equals("SI")){
            editor.putString(Constantes.SYNC, "NO");
        }*/
        editor.commit();
    }

    public static String limpiarTexto(String texto) {
        if (texto == null) return "";

        /*
            ELIMINA ESPACIOS
            REEMPLAZA TILDES Y CARACTERES ESPECIALES

         */

        texto = texto.trim();

        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);

        textoNormalizado = textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        textoNormalizado = textoNormalizado.replace("ñ", "n").replace("Ñ", "N");

        textoNormalizado = textoNormalizado.replaceAll("[^a-zA-Z0-9 ]", "");

        textoNormalizado = textoNormalizado.replaceAll("\\s+", " ").trim();

        return textoNormalizado;
    }


    private void moveToMainActivity() {
        new GuardarLog(LoginActivity.this).saveLog(user, "", "Ingreso al app");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(currentLocalTime);

        if (falta_salida && fecha.equalsIgnoreCase(fecha_falta_salida)) {

            SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String hora_entrada = sharedPreferences.getString(Constantes.HORA_ENTRADA,Constantes.NODATA);

            SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.ACTIVIDAD, "cerrado");
            editor.commit();

/*            if (!isServiceRunning(AsistenciaService.class)) {
                Log.i("cdcdcds","creo"+hora_entrada);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Si la versión del SDK es Oreo o superior, utiliza startForegroundService()
                    Intent serviceIntent = new Intent(this, AsistenciaService.class);
                    serviceIntent.putExtra("hora_entrada",hora_entrada);
                    ContextCompat.startForegroundService(this, serviceIntent);
                } else {
                    // Si la versión del SDK es anterior a Oreo, utiliza startService() normal
                    Intent serviceIntent = new Intent(this, AsistenciaService.class);
                    serviceIntent.putExtra("hora_entrada",hora_entrada);
                    startService(serviceIntent);
                }
            }else{
                Log.i("cdcdcds","NO creo");
            }*/

            //Log.i("ascc",""+coidgoPdv);
            Intent intent = new Intent(LoginActivity.this, MenuNavigationActivity.class);
            //intent.putExtra("tiempo",coidgoPdv);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.i("MOVE TO MAIN ACTIVITY", "MENU");
        } else {
            Intent intent = new Intent(LoginActivity.this, PuntosListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.i("MOVE TO MAIN ACTIVITY", "PUNTOS");
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

    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        // SET TIME HERE
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE,20);

        myIntent = new Intent(LoginActivity.this, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        if (!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void SaveData(String user, String supervisor) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.USER, user);
        editor.putString(Constantes.SUPERVISOR, supervisor);
        editor.putString(Constantes.MODULO_ACTUAL,Constantes.MODULO_PUNTOS_PRINCIPAL);
        editor.commit();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_ENTER  && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            //"enter" key has ignored
            if ( ((EditText)view).getLineCount() >=1 )
                return true;
        }
        return false;
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bajada, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bajada) {
            if (VerificarNet.hayConexion(this)) {
                alertDialog();
            } else {
                Snackbar.make(coordinatorLayout, Mensajes.ERROR_RED , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                Toast.makeText(getApplicationContext(),"No hay conexión de Internet.",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*public void sincronizacion(View view) {
        if (VerificarNet.hayConexion(this)) {
            alertDialog();
        } else {
            Snackbar.make(coordinatorLayout, Mensajes.ERROR_RED , Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
//                Toast.makeText(getApplicationContext(),"No hay conexión de Internet.",Toast.LENGTH_LONG).show();
        }
    }*/

    /*public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_sync, null);
        //Title
        builder.setIcon(android.R.drawable.ic_menu_set_as);
        builder.setTitle(R.string.app_name);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_sync,null));

        //Traer Views
        user = (EditText)dialogView.findViewById(R.id.txtOperador);
        btnPdv = (ImageButton)dialogView.findViewById(R.id.btnPdvSync);
        btnProductos =(ImageButton)dialogView.findViewById(R.id.btnProductosSync);

        progressBar = (ProgressBar) dialogView.findViewById(R.id.barra);

        btnPdv.setOnClickListener(this);

        btnProductos.setOnClickListener(this);

        builder.setNegativeButton(R.string.salir,null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Intent.ACTION_SYNC);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorSync, filtroSync);
        new DeveloperOptions().modalDevOptions(LoginActivity.this);
        if (user!=null) {
          //  new UniqueDevice().modalUniqueDevice(LoginActivity.this, user);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorSync);
    }

    /*private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }*/

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

    public void sincronizar() {
        if (VerificarNet.hayConexion(this)) {
            user = limpiarTexto(txtUser.getText().toString().toUpperCase().trim());
            pass = txtPass.getText().toString().trim();
            if (!user.equals("") && user!=null) {
                guardarFecha();
                //Sincronizar datos de bajada
                operator = user.toUpperCase().trim();
                AsyncTaskBajarOper bajarOper = new AsyncTaskBajarOper();
                bajarOper.execute();
            }else{
                Toast.makeText(getApplicationContext(),Mensajes.SYNC_NO_USER ,Toast.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(coordinatorLayout, Mensajes.ERROR_RED , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void rotarImagen(View view, int rotacion) {
        RotateAnimation animation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        if (rotacion==1) {
            animation.setDuration(1000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.RESTART);
            view.startAnimation(animation);
            //view.clearAnimation();
        }else if (rotacion==0) {
            animation.setDuration(1000);
            animation.setRepeatCount(Animation.ABSOLUTE);
            animation.setRepeatMode(Animation.RESTART);
            view.startAnimation(animation);
            //view.clearAnimation();
        }
    }

    private class AsyncTaskBajarOper extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.bajar_Oper, operator);
                Thread.sleep(100000);
                resp = "Slept for 10 seconds";
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Sincronizando");
            progressDialog.setMessage("Validando credenciales, espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

}