package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 18/04/2018.
 */

public class AdapterMallaCodificados extends RecyclerView.Adapter<AdapterMallaCodificados.ViewHolder> {

    private Cursor cursor;
    private Context context;

    // Instancia de escucha
    private OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    interface OnItemClickListener {
        public void onClick(ViewHolder holder, String idContacto);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView marca;
        private TextView categoria;
        private TextView tipo;
        private TextView productos;
        private TextView pcont;
        private TextView pcuotas;
        private TextView fecha;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            //fechainicio = (TextView) v.findViewById(R.id.lblfechainicial);
            marca = (TextView) v.findViewById(R.id.lblMara);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            tipo = (TextView) v.findViewById(R.id.lblTipo);
            productos = (TextView) v.findViewById(R.id.lblProductos);
            pcont = (TextView) v.findViewById(R.id.lblPcont);
            pcuotas = (TextView) v.findViewById(R.id.lblPcuotas);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
        }
    }


    public AdapterMallaCodificados(Context context) {
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
                .inflate(R.layout.status_malla_codificados, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String marca;
        String categoria;
        String tipo;
        String productos;
        String pcont;
        String pcuotas;
        String fecha;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if(consulta.equals("1")){
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }


        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.FECHA));
        categoria= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.HORA));
        tipo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.BRAND));
        productos = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.POS_NAME));
        pcont = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.SKU_CODE));
        pcuotas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.CODIFICA));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMallaCodificados.Columnas.USUARIO));

        viewHolder.estado.setText(estado);
        viewHolder.marca.setText(marca);
        viewHolder.categoria.setText(categoria);
        viewHolder.tipo.setText(tipo);
        viewHolder.productos.setText(productos);
        viewHolder.pcont.setText(pcont);
        viewHolder.pcuotas.setText(pcuotas);
        viewHolder.fecha.setText(fecha);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
