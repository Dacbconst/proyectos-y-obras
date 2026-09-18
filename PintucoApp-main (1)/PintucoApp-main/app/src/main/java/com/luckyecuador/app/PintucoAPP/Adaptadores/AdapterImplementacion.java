package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 18/04/2018.
 */

public class AdapterImplementacion extends RecyclerView.Adapter<AdapterImplementacion.ViewHolder> {

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
        private TextView tipoimple;
        private TextView categoria;
       // private TextView tipo;
        private TextView marca;
        private TextView modo;
        private TextView fecha;
        private TextView hora;



        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            tipoimple = (TextView) v.findViewById(R.id.lblTipoIm);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
           // tipo = (TextView) v.findViewById(R.id.lblTipo);
            marca = (TextView) v.findViewById(R.id.lblMarca);
            modo= (TextView) v.findViewById(R.id.lblModo);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            hora = (TextView) v.findViewById(R.id.lblHora);

        }
    }


    public AdapterImplementacion (Context context) {
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
                .inflate(R.layout.row7, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String tipoim;
        String categoria;
      //  String tipo;
        String marca;
        String modo;
        String fecha;
        String hora;


        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        tipoim = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.FECHA));
        categoria= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.HORA));
        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.PDV));
        modo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.DIRECCION));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.LATITUD));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImplementacion.Columnas.LONGITUD));



        viewHolder.estado.setText(estado);
        viewHolder.tipoimple.setText(tipoim);
        viewHolder.categoria.setText(categoria);
        viewHolder.marca.setText(marca);
        viewHolder.modo.setText(modo);
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
