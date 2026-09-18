package com.luckyecuador.app.PintucoAPP;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Adaptadores.PaginationScrollListener;
import com.luckyecuador.app.PintucoAPP.Adaptadores.PostRecyclerAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.Tabla;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.util.ArrayList;

public class PuntosActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    String ACTION_FILTER = "com.luckyecuador.app.PintucoAPP";

    ListViewAdapter dataAdapter;

    TableLayout layout_tabla;

    private ImageButton btnNuevoLocal;
    private EditText txtCodigo;
    private EditText txtNombre;

    private String user, idpdv, fecha, hora, codigo, tipo, puntoventa, formato, direccion, cliente, zona, format, nom_comercial, canal;

    private SharedPreferences sharedPref;
    //BASE SQLITE
    DatabaseHelper handler;

    final String[] lblLatitud = {"Latitud"};
    final String[] lblLongitud = {"Longitud"};

    Tabla tabla;

    private static final String TAG = "PuntosActivity";
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos);
        setToolbar();

        Init();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        LoadData();
        btnNuevoLocal = (ImageButton) findViewById(R.id.ibNuevoLocal);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        txtCodigo = (EditText) findViewById(R.id.txt_codigo);
        txtNombre = (EditText) findViewById(R.id.txt_nombre);

        Context context = this.getApplicationContext();
        sharedPref = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        btnNuevoLocal.setOnClickListener(this);

        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }// else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), LocationService.class));
        } else {
            startService(new Intent(getApplicationContext(), LocationService.class));
        }
        //locationStart();
        //Si los permisos estan otorgados, llamar al evento onClick del boton
        //clickButton();
        //}

        recyclerPDV();

        txtCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                mAdapter.clear();

                final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
                final String descripcion_codigo = txtCodigo.getText().toString();
                final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
                final String descripcion_nombre = txtNombre.getText().toString();

                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                mAdapter.clear();

                final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
                final String descripcion_codigo = txtCodigo.getText().toString();
                final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
                final String descripcion_nombre = txtNombre.getText().toString();

                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void recyclerPDV() {
        final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
        final String descripcion_codigo = txtCodigo.getText().toString();
        final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
        final String descripcion_nombre = txtNombre.getText().toString();

//        ButterKnife.bind(this);

        swipeRefresh.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostRecyclerAdapter(getApplicationContext(),new ArrayList<BasePharmaValue>());
        mRecyclerView.setAdapter(mAdapter);
        preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void preparedListItem(String por_codigo, String descripcion_codigo, String por_nombre, String descripcion_nombre) {
        final ArrayList<BasePharmaValue> pdv = handler.consultarBasePharmaValue(user, por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);

        final int size_list = pdv.size();
        final int num_items = 50;
        final int num_pages = size_list/num_items;

        if (num_pages == 0) {
            totalPage = 1;
        } else {
            totalPage = num_pages + 1;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<BasePharmaValue> items = null;
                try {
                    if (size_list > 50) {
                        Log.i("ENTRA > 50","ENTRA > 50");

                        for (int i = 0; i < num_items; i++) {
                            itemCount++;

                            Log.i("ITEM_COUNT", itemCount + "");
                            if (currentPage == PAGE_START) {
                                items = new ArrayList<>(pdv.subList(0, itemCount + 1));
                                Log.i("ITEMS_SIZE", items.size() + "");
                            } else {
                                if (itemCount >= num_pages) {
                                    items = new ArrayList<>(pdv.subList(itemCount - num_items, itemCount + 1));
                                } else {
                                    items = new ArrayList<>(pdv.subList(itemCount - num_items, itemCount + 1));
                                }
                            }
                        }
                    }else{
                        itemCount = 0;
                        Log.i("ENTRA < 50","ENTRA < 50");
                        for (int i = 0; i < size_list; i++) {
                            itemCount++;

                            Log.i("ITEM_COUNT", itemCount + "");
                            Log.i("PDV_SIZE", pdv.size() + "");
                            items = new ArrayList<>(pdv);
                            mAdapter.removeLoading();
                        }
                    }

                }catch (Exception e) {
                    Log.i("Exception Items", e.getMessage());
                }
                if (currentPage != PAGE_START) {
                    mAdapter.removeLoading();
                }

                try {
                    Log.i("SIZE", size_list + "");
                    if (size_list > 0) {
                        mAdapter.addAll(items);
                    } else {
                        Toast.makeText(getApplicationContext(), "No hay PDVs a mostrar", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {
                    Log.i("Exception Add", e.getMessage());
                }

                try {
                    swipeRefresh.setRefreshing(false);
                    if (currentPage < totalPage) {
                        mAdapter.addLoading();
                    } else {
                        isLastPage = true;
                        //mAdapter.removeLoading();
                    }
                } catch (Exception e) {
                    Log.i("Exception Swipe", e.getMessage());
                }
                isLoading = false;
            }
        }, 1);
    }

    @Override
    public void onRefresh() {
        final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
        final String descripcion_codigo = txtCodigo.getText().toString();
        final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
        final String descripcion_nombre = txtNombre.getText().toString();

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Desea cerrar sesión?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClufActivity.signed = 0;
                Intent intent = new Intent(PuntosActivity.this,LoginActivity.class);
                startActivity(intent);
                //Stop the activity
                finish();
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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        Log.i("USUARIO",user);
    }

    @Override
    public void onClick(View view) {
        if (view == btnNuevoLocal) {
            //IMPORTante para nuevo pdv
            Intent intent = new Intent(PuntosActivity.this, ImplementacionActivity.class);
            startActivity(intent);
            mostrarPopup();
            Log.i("IMPRIMIR SI", "IMPRIMIR DOS");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync_subida1) {
            if (VerificarNet.hayConexion(this)) {
                try {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.SUBIR_TODO, null);
                    Snackbar.make(findViewById(R.id.coordinatorPos),
                            Mensajes.ON_SYNC_UP, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.coordinatorPos),
                        Mensajes.ERROR_RED, Snackbar.LENGTH_LONG).show();
            }
            return true;
        }

        if (id == R.id.action_log) {
            Intent intent = new Intent(PuntosActivity.this, HistorialActivity.class);
            startActivity(intent);
            return true;
        }
/*
        if (id == R.id.action_cluf) {
            Intent intent = new Intent(PuntosActivity.this, ClufActivity.class);
            intent.putExtra("menu", "1");
            startActivity(intent);
            return true;
        }
*/
        if (id == R.id.action_version) {
            mostrarVersion();
            //Intent intent = new Intent(PuntosActivity.this, MapsActivity.class);
            //startActivity(intent);
            return true;
        }

//        if (id == R.id.action_test) {
//            Intent intent = new Intent(PuntosActivity.this, QuizActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        if (id == R.id.action_maps) {
//            Intent intent = new Intent(PuntosActivity.this, RutaActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void mostrarVersion() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.action_version))
                .setMessage(getString(R.string.user) + ": " + user + "\n" +
                        getString(R.string.version))
                .setPositiveButton("ACEPTAR", null)
                .show();
    }


    public void mostrarPopup() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.action_popup))
                .setMessage(getString(R.string.user) + ": " + user + "\n")

                .setPositiveButton("ACEPTAR", null)
                .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_popup, null);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.alertdialog_causales,null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
//        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio);

        tittle.setText(R.string.action_popup);

    }

    private void Init() {

    }

    /**
     * Apartir de aqui empezamos a obtener la direciones y coordenadas
     */
    private void locationStart() {
        registerReceiver(new ProximityReciever(), new IntentFilter(ACTION_FILTER));
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion localizacion = new Localizacion();
        localizacion.setPuntosActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        if (networkEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) localizacion);
        }
        if (gpsEnabled) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) localizacion);
        }
        //Setting up My Broadcast Intent
        Intent i = new Intent(ACTION_FILTER);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, i, PendingIntent.FLAG_MUTABLE);
        //setting up proximituMethod
        //mlocManager.addProximityAlert(lat1, long1, radius, -1, pi);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    /**
     * Aqui empieza la Clase Localizacion
     */
    public class Localizacion implements LocationListener {

        PuntosActivity PuntosActivity;

        public PuntosActivity getPuntosActivity() {
            return PuntosActivity;
        }

        public void setPuntosActivity(PuntosActivity PuntosActivity) {
            this.PuntosActivity = PuntosActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            if (!lblLatitud[0].equals(loc.getLatitude())&&!lblLongitud[0].equals(loc.getLongitude())) {
                lblLatitud[0] = ""+loc.getLatitude();
                lblLongitud[0] = ""+loc.getLongitude();
            }
            //locationManager.removeUpdates(this);
            //Toast.makeText(getApplicationContext(), "Latitud: " + lblLatitud[0].toString() + " y Longitud: " + lblLongitud[0].toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public class ProximityReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = LocationManager.KEY_PROXIMITY_ENTERING;
            Boolean entering = intent.getBooleanExtra(key, false);
            if (entering) {
//                Toast.makeText(context, "LocationReminderReceiver entering", Toast.LENGTH_SHORT).show();
                Log.i("ReceptorProximidad", "entering");
            } else {
//                Toast.makeText(context, "LocationReminderReceiver exiting", Toast.LENGTH_SHORT).show();
                Log.i("ReceptorProximidad", "exiting");
            }
        }
    }
}