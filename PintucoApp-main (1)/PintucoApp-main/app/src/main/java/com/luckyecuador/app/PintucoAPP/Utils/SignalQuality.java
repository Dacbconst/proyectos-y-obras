package com.luckyecuador.app.PintucoAPP.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SignalQuality {
    // Clase que determina la calidad de la señal de red (WiFi o Datos móviles).

    private Context context;
    private String user,supervisor;

    // Constructor que recibe el contexto de la aplicación.
    public SignalQuality(Context context) {
        this.context = context;
    }

    // Método público que retorna la calidad de la señal actual.
    public String getSignalQuality() {

        // Obtiene el gestor de conectividad del sistema.
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Obtiene la información de la red activa.
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Verifica si hay una red activa.
        if (activeNetwork != null) {
            // Si la red activa es WiFi, llama al método correspondiente.
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return getWifiSignalQuality();
            }
            // Si la red activa son los datos móviles, llama al método correspondiente.
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getMobileSignalQuality();
            }
        }
        // Si no hay conexión activa, retorna un mensaje indicando esto.
        return "No hay conexión";
    }

    // Se obtiene la calidad de la señal WiFi.
    private String getWifiSignalQuality() {
        // Obtiene el gestor de WiFi del sistema.
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        // Calcula el nivel de señal actual utilizando el RSSI (Received Signal Strength Indicator).
        int signalLevel = WifiManager.calculateSignalLevel(wifiManager.getConnectionInfo().getRssi(), numberOfLevels);
        // Convierte el nivel de señal a una descripción legible y la retorna.
        return getQualityString(signalLevel);
    }

    // Se obtiene la calidad de la señal de los datos móviles.
    private String getMobileSignalQuality() {
        // Obtiene el gestor de telemetría del sistema.
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int signalStrength = 0;

        // Verifica si la versión de Android es P (API 28) o superior.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            // Obtiene el nivel de señal actual.
            signalStrength = telephonyManager.getSignalStrength().getLevel();
        }
        // Convierte el nivel de señal a una descripción legible y la retorna.
        return getQualityString(signalStrength);
    }

    // Se obtiene el nivel de señal en una descripción legible.
    private String getQualityString(int level) {
        // Asigna una descripción basada en el nivel de señal.
        switch (level) {
            case 0: return "Muy Baja"; // Nivel 0: Muy Baja calidad de señal.
            case 1: return "Baja";     // Nivel 1: Baja calidad de señal.
            case 2: return "Media";    // Nivel 2: Calidad de señal media.
            case 3: return "Buena";    // Nivel 3: Buena calidad de señal.
            case 4: return "Excelente"; // Nivel 4: Excelente calidad de señal.
            default: return "Desconocido"; // Cualquier otro valor se considera desconocido.
        }
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR, Constantes.NODATA);
    }



    public void sendNotificationLowSignal(){



        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String hora = hour.format(currentLocalTime);

        LoadData();

        String senal = getSignalQuality();

        if (senal.equalsIgnoreCase("Mala") || senal.equalsIgnoreCase("Muy Mala")){
            String descripcion = "El gestor cuenta con baja señal de internet o datos moviles";
            ContentValues values_audit = new ContentValues();
            values_audit.put(ContractNotificacion.Columnas.USER, user);
            values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
            values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
            values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
            values_audit.put(ContractNotificacion.Columnas.HORA, hora);
            values_audit.put(Constantes.PENDIENTE_INSERCION, 1);
           context.getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
            SyncAdapter.sincronizarAhora(context.getApplicationContext(), true, Constantes.insertNot, null);
        }
    }
}