package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class LocationService2 extends Service {

    Location location_r;
    BroadcastReceiver mReceiver;

    private static final String TAG = "LocationService";
    private LocationManager mLocationManager = null;

    public static final int LOCATION_INTERVAL = 1000 * 60 * 3; //3 Minutos
    private static final int LOCATION_INTERVAL_O = 1000 * 60 * 3; //3 Minutos
    public static final float LOCATION_DISTANCE = 1;

    Location location;
    double latitude;
    double longitude;

    private String user = "", supervisor = "", cargo = "", foto = "";
    private String fecha = "", hora = "", name = "";
    private String latitud = "", longitud = "", codigo_pdv = "", nombre_pdv = "", perimetro_pdv = "";
    private double latitud_pdv = 0, longitud_pdv = 0, distance_pdv = 0;
    private boolean esNuevoPDV= true, registroInsertado = false;

    // flag for GPS Status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;




    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public LocationService2() {
        super();
//        LocationInitialize();
//        getLocation();
    }

    /**
     * Try to get my current location by GPS or Network Provider
     */
    public void getLocation() {
        try {
//            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use GPS Service");
                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */

                provider_info = LocationManager.GPS_PROVIDER;

            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                this.isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use Network State to get GPS coordinates");
                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                provider_info = LocationManager.NETWORK_PROVIDER;

            }

            // Application can use GPS or Network Provider
            if (!provider_info.isEmpty()) {
                if (mLocationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    location = mLocationManager.getLastKnownLocation(provider_info);
                    updateGPSCoordinates();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to LocationManager", e);
        }
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            Log.i("LATITUDE UPDATE GPS", "" + getLatitude());
//            Log.i("LONGITUDE UPDATE GPS", "" + getLongitude());
//            Intent intent = new Intent(Constantes.ACTION_GPS_UPDATE);
//            intent.putExtra("latitude", latitude);
//            intent.putExtra("longitude", longitude);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    /**
     * GPSTracker latitude getter and setter
     * @return latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * GPSTracker longitude getter and setter
     * @return
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener() {
            super();
        }

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location loc) {
            mLastLocation.set(loc);
            loc.getLatitude();
            loc.getLongitude();
            latitud = "" + loc.getLatitude();
            longitud = "" + loc.getLongitude();

//            Log.i("LATITUDE ON LOCATION CHANGED", latitud);
//            Log.i("LONGITUDE ON LOCATION CHANGED", longitud);
//            Log.i("LATITUDE_PDV ON LOCATION CHANGED", latitud_pdv + "");
//            Log.i("LONGITUDE_PDV ON LOCATION CHANGED", longitud_pdv + "");

            LoadName();
            obtenerFecha();
//            String name = "Ana Pinzon";
            ContentValues values = new ContentValues();
            values.put(ContractInsertRastreo.Columnas.USUARIO, name);
            values.put(ContractInsertRastreo.Columnas.LATITUD, latitud);
            values.put(ContractInsertRastreo.Columnas.LONGITUD, longitud);
            values.put(ContractInsertRastreo.Columnas.FECHA, fecha);
            values.put(ContractInsertRastreo.Columnas.HORA, hora);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

//            Toast.makeText(getApplicationContext(),"Saving Location..",Toast.LENGTH_SHORT).show();
            System.out.println("Saving Location.." + latitud + "," + longitud);
            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
//            SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertGeo, null);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            LoadName();
            obtenerFecha();
//            String name = "Ana Pinzon";
            ContentValues values = new ContentValues();
            values.put(ContractInsertRastreo.Columnas.USUARIO, name);
            values.put(ContractInsertRastreo.Columnas.LATITUD, "GPS DESACTIVADO");
            values.put(ContractInsertRastreo.Columnas.LONGITUD, "GPS DESACTIVADO");
            values.put(ContractInsertRastreo.Columnas.FECHA, fecha);
            values.put(ContractInsertRastreo.Columnas.HORA, hora);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
//            SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertGeo, null);

            if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
                Log.e(TAG, "GPS Notification: GPS inactivo");
                String descripcion = "Se inactivó el GPS";
                ContentValues values_audit = new ContentValues();
                values_audit.put(ContractNotificacion.Columnas.USER, user);
                values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
                values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
                values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
                values_audit.put(ContractNotificacion.Columnas.HORA, hora);
                values_audit.put(Constantes.PENDIENTE_INSERCION, 1);
                getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
                SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            LoadName();
            obtenerFecha();
            Log.e(TAG, "onProviderEnabled: " + provider);
            if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
                Log.e(TAG, "GPS Notification: GPS activo");
                String descripcion = "Se activó el GPS";
                ContentValues values_audit = new ContentValues();
                values_audit.put(ContractNotificacion.Columnas.USER, user);
                values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
                values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
                values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
                values_audit.put(ContractNotificacion.Columnas.HORA, hora);
                values_audit.put(Constantes.PENDIENTE_INSERCION, 1);
                getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
                SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d(TAG, "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d(TAG, "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d(TAG, "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        public void LoadName() {
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            name = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
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
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        LocationInitialize();
        getLocation();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mMessageReceiver, new IntentFilter(Constantes.ACTION_PDV_LOCATION_UPDATE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1");
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("My service.")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(1, notification);
        }
        try {
            loginToFirebase();
        } catch (Exception e) {
            Log.i("ERROR TRACKER", e.getMessage());
        }
        Log.i("cdddd","entra");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String channelName = "My service 2";
        NotificationChannel chan = new NotificationChannel("4", channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "4");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name) + " byubb")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(4, notification);
        calcularTiempo();
    }

    public void calcularTiempo(){

        Log.i("calculotiempo","tiempo");

        /*int tiempo = 15;

        long milisegundos = tiempo * 60 * 1000;

        String horaInicio = "13:00:00";
        String horaFin = "17:00:00";

        // Formato de la hora
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        try {
            // Convertir las horas de inicio y fin a objetos Date
            Date dateInicio = format.parse(horaInicio);
            Date dateFin = format.parse(horaFin);

            // Calcular la diferencia de tiempo en milisegundos
            long diferenciaMillis = dateFin.getTime() - dateInicio.getTime();

            // Convertir la diferencia de tiempo a horas, minutos y segundos
            long horas = diferenciaMillis / (60 * 60 * 1000);
            long minutos = (diferenciaMillis / (60 * 1000)) % 60;
            long segundos = (diferenciaMillis / 1000) % 60;

            // Imprimir la diferencia de tiempo
            //System.out.println("Diferencia de tiempo: " + horas + " horas, " + minutos + " minutos, " + segundos + " segundos");
            String nuevoTiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
            ///
            String[] partes = nuevoTiempo.split(":");
            int horas2 = Integer.parseInt(partes[0]);
            int minutos2 = Integer.parseInt(partes[1]);
            int segundos2 = Integer.parseInt(partes[2]);

            // Convertir cada unidad a milisegundos y sumarlos
            long tiempoEnMilisegundos = horas2 * 3600000L + minutos2 * 60000L + segundos2 * 1000L;

            Log.i("fdxax",""+tiempoEnMilisegundos);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new CountDownTimer(10800000, milisegundos) {
            int c = 0;
            public void onTick(long millisUntilFinished) {
                Date currentTime = Calendar.getInstance().getTime();
                Log.i("tiempos",""+currentTime.toString());
                c++;
            }

            public void onFinish() {
                Log.i("cdvf","" + c);
            }
        }.start();*/
    }

    public void LocationInitialize() {
        initializeLocationManager();
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    mLocationListeners[1]);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//        }
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    mLocationListeners[0]);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } else {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL_O, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            }
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } else {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL_O, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            }
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
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
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
//        obtenerFecha();
//        ContentValues values_audit = new ContentValues();
//        values_audit.put(ContractNotificacion.Columnas.USER, user);
//        values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
//        values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, "DESTRUCCIÓN DEL SERVICIO");
//        values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
//        values_audit.put(ContractNotificacion.Columnas.HORA, hora);
//        values_audit.put(Constantes.PENDIENTE_INSERCION, 1);
//        getApplicationContext().getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
//        SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);
        // no need to do anything here
        //Intent broadcastIntent = new Intent();
        //broadcastIntent.setAction("restartservice");
        //broadcastIntent.setClass(this, RestartService.class);
        //this.sendBroadcast(broadcastIntent);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        FirebaseApp.initializeApp(getApplicationContext());
        Log.i(TAG, "LoginFirebase");
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "firebase auth success");
                    requestLocationUpdates();
                } else {
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }

    private void requestLocationUpdates() {
        LoadData();
        LocationRequest request = new LocationRequest();
//        LocationRequest request = LocationRequest.create();
        request.setInterval(20000);
        request.setFastestInterval(15000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);//ALTA PRECISION
//        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);//AHORRO DE BATERIA
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
//        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        final String path = getString(R.string.firebase_path) + "/" + user;
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    try{
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                        ref.push();
                        location_r = locationResult.getLastLocation();
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = mLocationManager.getLastKnownLocation(provider_info);
//                    updateGPSCoordinates();
                        if (location_r != null) {
                            Log.d(TAG, "Location Update -> " + location_r);
                            Log.d(TAG, "User -> " + user);
                            ref.setValue(location_r);
                            ref.child("user").setValue(user);
                            //agregar distancia

                            sendBroadcastMessageDataLoaded(location_r);
//                        ref.child("cargo").setValue(cargo);
//                        ref.child("foto").setValue(foto);
                        }
                    }catch (Exception e){
                        Log.e("Location", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }, null);
        }
    }

    public void LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR, Constantes.NODATA);
//        cargo = sharedPreferences.getString(Constantes.S_CARGO, Constantes.NODATA);
//        foto = sharedPreferences.getString(Constantes.S_FOTO, Constantes.NODATA);
        Log.i("USER_SERVICE", user);
//        Log.i("CARGO_SERVICE", cargo);
//        Log.i("FOTO_SERVICE", foto);
    }

    public void sendBroadcastMessageDataLoaded(Location location_r) {
        Log.i("LATITUD DEL PDV", latitud_pdv + "");
        Log.i("LONGITUD DEL PDV", longitud_pdv + "");
        Log.i("CODIGO DEL PDV", codigo_pdv + "");
        Log.i("PERIMETRO DEL PDV", perimetro_pdv + "");
        if (latitud_pdv!=0 && longitud_pdv!=0 && (codigo_pdv!=null || !codigo_pdv.trim().isEmpty())) {
            /*LatLng city = new LatLng(location_r.getLatitude(), location_r.getLongitude());

            List<String> list = Arrays.asList(perimetro_pdv.split(";"));
            System.out.println(list);

            List<LatLng> pts = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String string = list.get(i).toString();
                String[] parts = string.split(",");
                String latitude = parts[0];
                String longitude = parts[1];
                pts.add(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)));
            }
//            pts.add(new LatLng(-2.240819,-79.895252));
//            pts.add(new LatLng(-2.240593,-79.893482));
//            pts.add(new LatLng(-2.242298,-79.893211));
//            pts.add(new LatLng(-2.242577,-79.895132));

//        LatLngBounds bounds = new LatLngBounds(pts.get(3), pts.get(1));

            boolean contains1 = PolyUtil.containsLocation(city.latitude, city.longitude, pts, true);
            System.out.println("contains1: " + contains1);

//            boolean contains2 = bounds.contains(city);
//            System.out.println("contains2: " + contains2);*/

            MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
            MarkerOptions place2 = new MarkerOptions().position(new LatLng(location_r.getLatitude(), location_r.getLongitude())).title("Posicion PDV");
            double distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
            distance = (int)Math.round(distance);
            if (distance >= distance_pdv && esNuevoPDV) {
//            if (distance >= distance_pdv && esNuevoPDV && !registroInsertado) {
                Log.i("FUERA DEL PDV", "FUERA DEL PDV");
                String descripcion = "El gestor se encuentra fuera del perímetro del punto " + nombre_pdv;
                ContentValues values_audit = new ContentValues();
                values_audit.put(ContractNotificacion.Columnas.USER, user);
                values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
                values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
                values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
                values_audit.put(ContractNotificacion.Columnas.HORA, hora);
                values_audit.put(Constantes.PENDIENTE_INSERCION, 1);
                getApplicationContext().getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
                SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertNot, null);
                registroInsertado = true;
            } else if (distance <= distance_pdv) {
                Log.i("DENTRO DEL PDV", "DENTRO DEL PDV");
            }
            Log.i("DISTANCIA AL PDV", distance + "");
        }
        Intent intent = new Intent(Constantes.ACTION_GPS_UPDATE);
        intent.putExtra(Constantes.LATITUD, location_r.getLatitude());
        intent.putExtra(Constantes.LONGITUD, location_r.getLongitude());
        sendBroadcast(intent);
    }

    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.hasExtra(Constantes.LATITUD_PDV_ACTUAL) && intent.hasExtra(Constantes.LONGITUDE_PDV_ACTUAL) &&
                intent.hasExtra(Constantes.CODIGO_PDV_ACTUAL) && intent.hasExtra(Constantes.NOMBRE_PDV_ACTUAL) &&
                intent.hasExtra(Constantes.PERIMETRO_PDV_ACTUAL) && intent.hasExtra(Constantes.DISTANCE_PDV_ACTUAL)) {
                String old_codigo_pdv = codigo_pdv;
                latitud_pdv = intent.getDoubleExtra(Constantes.LATITUD_PDV_ACTUAL, 0);
                longitud_pdv = intent.getDoubleExtra(Constantes.LONGITUDE_PDV_ACTUAL, 0);
                codigo_pdv = intent.getStringExtra(Constantes.CODIGO_PDV_ACTUAL);
                nombre_pdv = intent.getStringExtra(Constantes.NOMBRE_PDV_ACTUAL);
                perimetro_pdv = intent.getStringExtra(Constantes.PERIMETRO_PDV_ACTUAL);
                distance_pdv = 50 + (intent.getDoubleExtra(Constantes.DISTANCE_PDV_ACTUAL, 0));//SE SUMA 50 MTS PARA LA ALERTA
                if (old_codigo_pdv.equalsIgnoreCase(codigo_pdv)) {
                    Log.i("PDV", "EL MISMO");
                    esNuevoPDV = false;
                } else {
                    Log.i("PDV", "NUEVO");
                    registroInsertado = false;
                    esNuevoPDV = true;
                }
            } else {
                Log.i("LOCATION SERVICE BROADCASTRECEIVER", "NO HAY DATA");
            }
        }
    };
}