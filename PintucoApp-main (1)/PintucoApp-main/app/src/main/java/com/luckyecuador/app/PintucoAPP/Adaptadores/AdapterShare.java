package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class AdapterShare extends RecyclerView.Adapter<AdapterShare.ViewHolder>  {

    private Cursor cursor;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView fecha;
        private TextView hora;
        private TextView codigo;
        private TextView marca;
        private TextView ctms_per;
        private TextView ctms_mar;
        private TextView sector;
        private TextView categoria;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblCategoria);
            hora = (TextView) v.findViewById(R.id.lblsubcat);
            codigo = (TextView) v.findViewById(R.id.lblcodigo);
            marca = (TextView) v.findViewById(R.id.lblmarca);
            ctms_per = (TextView) v.findViewById(R.id.lblcantidad);
            ctms_mar = (TextView) v.findViewById(R.id.lblfecha);
            sector = (TextView) v.findViewById(R.id.lblhora);
            categoria = (TextView) v.findViewById(R.id.lblcat);
        }
    }

    public AdapterShare(Context context) {
        this.context= context;
    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.status_share, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado;
        String fecha;
        String hora;
        String codigo;
        String marca;
        String ctms_per;
        String ctms_mar;
        String sector;
        String categoria;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.HORA));
        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.POS_NAME));
        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.MARCA_SELECCIONADA));
        ctms_per = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_PERCHA));
        ctms_mar = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_MARCA));
        sector = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.SECTOR));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CATEGORIA));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.marca.setText(marca);
        viewHolder.ctms_per.setText(ctms_per);
        viewHolder.ctms_mar.setText(ctms_mar);
        viewHolder.sector.setText(sector);
        viewHolder.categoria.setText(categoria);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }

}