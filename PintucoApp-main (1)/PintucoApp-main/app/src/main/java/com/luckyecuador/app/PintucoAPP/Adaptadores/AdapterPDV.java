package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class AdapterPDV extends RecyclerView.Adapter<AdapterPDV.ViewHolder> {

    private Cursor cursor;
    private Context context;

    interface OnItemClickListener {
        public void onClick(AdapterGps.ViewHolder holder, String idContacto);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView estadovisita;
        private TextView novedades;
        private TextView fecha;
        private TextView hora;


        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            estadovisita = (TextView) v.findViewById(R.id.lblvisita);
            novedades = (TextView) v.findViewById(R.id.lblnovedades);
            fecha = (TextView) v.findViewById(R.id.lblfecha);
            hora = (TextView) v.findViewById(R.id.lblhora);
        }
    }


    public AdapterPDV(Context context) {
        this.context= context;

    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public AdapterPDV.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.status_pdv, viewGroup, false);
        return new AdapterPDV.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterPDV.ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado;

        String estadov;
        String novedades;
        String fecha;
        String hora;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

//        estadov = cursor.getString(cursor.getColumnIndexOrThrow(ContractNotificacion.Columnas.ESTADOVISITA));
//        novedades= cursor.getString(cursor.getColumnIndexOrThrow(ContractNotificacion.Columnas.NOVEDADES));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractNotificacion.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractNotificacion.Columnas.HORA));

        viewHolder.estado.setText(estado);
//        viewHolder.estadovisita.setText(estadov);
//        viewHolder.novedades.setText(novedades);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }




}
