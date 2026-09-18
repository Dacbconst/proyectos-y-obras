package com.luckyecuador.app.PintucoAPP.Utils;

import static com.luckyecuador.app.PintucoAPP.TimeChangeBroadcastReceiver.isTimeAutomatic;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlertChangeTime {

    String fecha,hora,user,supervisor;
    Context mcontext;
    boolean autotime = false;

    public AlertChangeTime(Context context) {

        mcontext = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Fecha y Hora");
        builder.setMessage("La fecha y la hora han sido cambiadas, por favor, colocarlas en automático");
        builder.setPositiveButton("OK", null);

        AlertDialog ad = builder.create();
/*
        boolean autotime = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            autotime = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            autotime = Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
*/
        obtenerFecha();
        LoadData();


            autotime = isTimeAutomatic(context);

        boolean finalAutotime = autotime;
        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                // Cuando cambie a manual
                insertarNotificacion();

                Button button = ((AlertDialog) ad).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        context.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));

                        // Cuando cambie a automatico
                        autotime = isTimeAutomatic(context);
                            insertarNotificacion();

                        if (finalAutotime){
                            ad.dismiss();
                        }

                    }


                });



            }




        });

        if (!autotime) {
            Log.i("AUTOTIME", "DISABLE");
            ad.show();

        } else {
            Log.i("AUTOTIME", "ENABLE");
            if (ad.isShowing()) {
                ad.dismiss();

            }
        }

    }

    public void LoadData(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);
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
    }

    private void insertarNotificacion() {

        String descripcion = "El gestor cambió la obtención de fecha/hora a AUTOTIME";
        if (!autotime) {
            descripcion = descripcion.replace("AUTOTIME", "manual");
        } else {
            descripcion = descripcion.replace("AUTOTIME", "automático");
        }

        ContentValues values_audit = new ContentValues();
        values_audit.put(ContractNotificacion.Columnas.USER, user);
        values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
        values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
        values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
        values_audit.put(ContractNotificacion.Columnas.HORA, hora);
        values_audit.put(Constantes.PENDIENTE_INSERCION, 1);

        mcontext.getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);

        if (VerificarNet.hayConexion(mcontext)) {
            SyncAdapter.sincronizarAhora(mcontext, true,Constantes.insertNot, null);
        //    Toast.makeText(mcontext, Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        }else{
         //   Toast.makeText(mcontext, Mensajes.ON_SYNC_DEVICE,Toast.LENGTH_SHORT).show();
        }

    }




}
