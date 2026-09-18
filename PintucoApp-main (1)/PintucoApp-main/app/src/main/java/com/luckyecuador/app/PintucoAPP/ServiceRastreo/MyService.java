package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lucky Ecuador on 28/12/2016.
 */

public class MyService extends Service {

    private static final String TAG = "LOG:";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 60 * 1; //3Minutos
    private static final int LOCATION_INTERVAL_O = 1000 * 60 * 1; //5Segundos
    private static final float LOCATION_DISTANCE = 1;
    PowerManager.WakeLock wl;

    public MyService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire();
//        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();

        initializeLocationManager();
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
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } else {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL_O, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            }
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Service Destroyed");
        wl.release();
    }


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public class MyLocationListener implements LocationListener {

        private String fecha,hora,name;
        private String latitud, longitud;

        public MyLocationListener() {}

        public MyLocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la detecci—n de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            latitud = ""+loc.getLatitude();
            longitud = ""+loc.getLongitude();
//            txtCoordenadas.setText(Text);

            //obtenerFecha();

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
            System.out.println("Saving Location.." + latitud +","+ longitud);
            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
            //SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertGeo, null);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // GPS es desactivado
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

//            Toast.makeText(getApplicationContext(),"Saving Location..",Toast.LENGTH_SHORT).show();
            getContentResolver().insert(ContractInsertRastreo.CONTENT_URI, values);
//            txtCoordenadas.setText("GPS Desactivado");
            //SyncAdapter.sincronizarAhora(getApplicationContext(), true, Constantes.insertGeo, null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // GPS es activado
            Log.e(TAG, "onProviderEnabled: " + provider);
//            txtCoordenadas.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este mŽtodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizaci—n (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temp˜ralmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
            Log.e(TAG, "onStatusChanged: " + provider);
        }

        public void LoadName(){
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            name = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        }

        public void obtenerFecha(){
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

        public String getLatitud() {
            return latitud;
        }

        public void setLatitud(String latitud) {
            this.latitud = latitud;
        }

        public String getLongitud() {
            return longitud;
        }

        public void setLongitud(String longitud) {
            this.longitud = longitud;
        }
    }

    MyLocationListener[] mLocationListeners = new MyLocationListener[] {
            new MyLocationListener(LocationManager.GPS_PROVIDER),
            new MyLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public static boolean checkPermission(final Context context) {
        return androidx.core.app.ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}