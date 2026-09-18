package com.luckyecuador.app.PintucoAPP.Utils;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.HistorialActivity;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlertInformationRetention {

    private String user, supervisor, fecha, hora, descripcion;
    private SharedPreferences sharedPreferences;

    public void checkStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR, Constantes.NODATA);

        boolean hasRetainedInformation = checkRetainedInformation(context);

        Log.i("INFO_RETENTION", "Información retenida (NO ENVIADA): " + hasRetainedInformation);

        if (hasRetainedInformation) {
            obtenerFecha();

            descripcion = "Información retenida detectada en el dispositivo";
            Log.i("INFO_RETENTION", descripcion);

            ContentValues values_audit = new ContentValues();
            values_audit.put(ContractNotificacion.Columnas.USER, user);
            values_audit.put(ContractNotificacion.Columnas.DESCRIPCION, descripcion);
            values_audit.put(ContractNotificacion.Columnas.SUPERVISOR, supervisor);
            values_audit.put(ContractNotificacion.Columnas.FECHA, fecha);
            values_audit.put(ContractNotificacion.Columnas.HORA, hora);
            values_audit.put(Constantes.PENDIENTE_INSERCION, 1);

            context.getContentResolver().insert(ContractNotificacion.CONTENT_URI, values_audit);
            SyncAdapter.sincronizarAhora(context, true, Constantes.insertNot, null);

            mostrarMsgInformacionRetenida(context);
        } else {
            Log.i("INFO_RETENTION", "No hay información retenida (NO ENVIADA)");
        }
    }

    /**
     * Verifica si existe información retenida NO ENVIADA en las tablas locales
     * (PENDIENTE_INSERCION = 1)
     */
    private boolean checkRetainedInformation(Context context) {

        // UBICACIÓN DE MODULOS QUE SE ENCUENTRA A DIA 02/03/2026 :v

        boolean hasPreciosRetained = checkTableForRetainedData(
                context,
                ContractInsertPrecios.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );

        boolean hasSOSRetained = checkTableForRetainedData(
                context,
                ContractInsertShare.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );

        boolean hasLogisticoRetained = checkTableForRetainedData(
                context,
                ContractInsertLogisticoRelevo.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );

        boolean hasExhibicionesRetained = checkTableForRetainedData(
                context,
                ContractInsertExh.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );

        boolean hasPromocionesRetained = checkTableForRetainedData(
                context,
                ContractInsertPromocion.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );

        boolean hasInventarioAgotadosRetained = checkTableForRetainedData(
                context,
                InsertFlooring.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );


        boolean hasVentasRetained = checkTableForRetainedData(
                context,
                ContractInsertVentas.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );


        boolean hasPropensosYProdMalEstRetained = checkTableForRetainedData(
                context,
                ContractInsertPropensosYProdMalEst.CONTENT_URI,
                Constantes.PENDIENTE_INSERCION
        );


        return hasPreciosRetained || hasSOSRetained || hasLogisticoRetained || hasExhibicionesRetained || hasPromocionesRetained
                || hasInventarioAgotadosRetained || hasVentasRetained || hasPropensosYProdMalEstRetained;
    }

    /**
     * Verifica una tabla específica para datos PENDIENTES de sincronización
     * (PENDIENTE_INSERCION = 1 - NO ENVIADO)
     */
    private boolean checkTableForRetainedData(Context context, android.net.Uri contentUri, String pendingColumn) {
        Cursor cursor = null;
        try {
            String[] projection = new String[]{pendingColumn};
            String selection = pendingColumn + " = ?";
            String[] selectionArgs = new String[]{"1"};

            cursor = context.getContentResolver().query(
                    contentUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            int count = cursor != null ? cursor.getCount() : 0;
            Log.i("INFO_RETENTION", "Tabla: " + contentUri + " - Registros NO ENVIADOS: " + count);

            return count > 0;
        } catch (Exception e) {
            Log.e("INFO_RETENTION", "Error verificando tabla: " + contentUri, e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void obtenerFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        hora = hour.format(currentLocalTime);
    }

    public void mostrarMsgInformacionRetenida(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alertdialog_msg_informacion_retenida, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);

        Button btnAceptar = view.findViewById(R.id.btn_acept);
        Button btnConsultar = view.findViewById(R.id.btn_consultar);

        TextView txtModulosLista = view.findViewById(R.id.txt_modulos_lista);

        String listaModulos = obtenerModulosConInformacionRetenida(context);

        if (!listaModulos.isEmpty()) {
            txtModulosLista.setText(listaModulos);
        } else {
            txtModulosLista.setText("No se encontraron módulos con información retenida");
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, HistorialActivity.class);
//                i.putExtra("test_id",test_id);
//                i.putExtra("nomTest",nomTest);
                context.startActivity(i);
                dialog.dismiss();
            }
        });
    }

    private String obtenerModulosConInformacionRetenida(Context context) {
        StringBuilder modulos = new StringBuilder();

        if (checkTableForRetainedData(context, ContractInsertPrecios.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• PRECIOS\n");
        }

        if (checkTableForRetainedData(context, ContractInsertShare.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• SHARE OF SHELF\n");
        }

        if (checkTableForRetainedData(context, ContractInsertLogisticoRelevo.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• LOGISTICO\n");
        }

        if (checkTableForRetainedData(context, ContractInsertExh.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• EXHIBICIONES\n");
        }

        if (checkTableForRetainedData(context, ContractInsertPromocion.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• PROMOCIONES\n");
        }

        if (checkTableForRetainedData(context, InsertFlooring.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• INVENTARIO + AGOTADOS\n");
        }

        if (checkTableForRetainedData(context, ContractInsertVentas.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• VENTAS\n");
        }

        if (checkTableForRetainedData(context, ContractInsertPropensosYProdMalEst.CONTENT_URI, Constantes.PENDIENTE_INSERCION)) {
            modulos.append("• PROPENSOS\n O PROD. MAL EST.\n");
        }

        return modulos.toString().trim();
    }

}