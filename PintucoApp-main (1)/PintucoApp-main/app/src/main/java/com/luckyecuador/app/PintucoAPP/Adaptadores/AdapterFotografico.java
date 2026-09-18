package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;
import com.luckyecuador.app.PintucoAPP.R;

public class AdapterFotografico extends RecyclerView.Adapter<AdapterFotografico.ViewHolder> {
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
        private TextView codigo;
        private TextView fecha;
        private TextView hora;
        private TextView subcategoria;
        private TextView marca;
        private TextView comentario;
        private TextView categoria;

        public ViewHolder(View v) {
            super(v);

            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            hora = (TextView) v.findViewById(R.id.lblHora);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            subcategoria = (TextView) v.findViewById(R.id.lblNombrecomercial);
            marca = (TextView) v.findViewById(R.id.lblExhibicion);
            comentario = (TextView) v.findViewById(R.id.lblComentario);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
        }
    }

    public AdapterFotografico(Context context) {
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
                .inflate(R.layout.row9, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado;
        String codigo;
        String fecha;
        String hora;
        String subcategoria;
        String marca;
        String comentario;
        String categoria;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.HORA));
        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.CODIGO));
        subcategoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.SUBCATEGORIA));
        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.MARCA));
        comentario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.LOGRO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertFotografico.Columnas.CATEGORIA));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.subcategoria.setText(subcategoria);
        viewHolder.marca.setText(marca);
        viewHolder.comentario.setText(comentario);
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
