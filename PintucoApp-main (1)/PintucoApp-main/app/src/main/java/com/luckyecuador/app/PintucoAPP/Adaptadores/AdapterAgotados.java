package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class AdapterAgotados extends RecyclerView.Adapter<AdapterAgotados.ViewHolder>  {

    private Cursor cursor;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Campos respectivos de un item
        private TextView estado;
        private TextView categoria;
        private TextView subcat;
        private TextView marca;
        private TextView producto;
        private TextView total;
        private TextView target;
        private TextView fecha;
        private TextView hora;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            subcat = (TextView) v.findViewById(R.id.lblsubcat);
            marca = (TextView) v.findViewById(R.id.lblmarca);
            producto = (TextView) v.findViewById(R.id.lblproducto);
            total = (TextView) v.findViewById(R.id.lbltotal);
            target=(TextView) v.findViewById(R.id.lbltarget);
            fecha = (TextView) v.findViewById(R.id.lblfecha);
            hora = (TextView) v.findViewById(R.id.lblhora);
        }
    }

    public AdapterAgotados(Context context) {
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
                .inflate(R.layout.status_agotado, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String categoria;
        String subcat;
        String marca;
        String producto;
        String total;
        String target;
        String fecha;
        String hora;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.PHARMA_ID));
        subcat=     cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.CODIGO));
        marca=    cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.USUARIO));
        producto=    cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.SUPERVISOR));
        total= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.TIEMPO_INICIO));
        target=cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.TIEMPO_FIN));
        fecha=   cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.FECHA));
        hora=   cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAgotados.Columnas.HORA));

        viewHolder.estado.setText(estado);
        viewHolder.categoria.setText(categoria);
        viewHolder.subcat.setText(subcat);
        viewHolder.marca.setText(marca);
        viewHolder.producto.setText(producto);
        viewHolder.total.setText(total);
        viewHolder.target.setText(target);
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
