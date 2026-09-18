package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.DataBase.Consultas;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterAsistencias extends RecyclerView.Adapter<AdapterAsistencias.ViewHolder> {
    private Cursor cursor;
    private Context context;

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
        private TextView registro;
        private TextView coord;
        private TextView causal;
        private TextView fecha;
        private TextView hora;


        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            registro = (TextView) v.findViewById(R.id.lblRegistro);
            coord = (TextView) v.findViewById(R.id.lblCoord);
            causal = (TextView) v.findViewById(R.id.lblCausal);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            hora = (TextView) v.findViewById(R.id.lblHora);
        }
    }


    /**
     * Obtiene el valor de la columna 'idContacto' basado en la posición actual del cursor
     * @param posicion Posición actual del cursor
     * @return Identificador del contacto
     */
    private String obtenerIdContacto(int posicion) {
        if (cursor != null) {
            if (cursor.moveToPosition(posicion)) {
                return Consultas.obtenerString(cursor, ContractInsertAsistencia.Columnas._ID);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    public AdapterAsistencias(Context context) {
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
                .inflate(R.layout.row11, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado;
        String codigo;
        String registro;
        String coord;
        String causal;
        String fecha;
        String hora;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.IDPDV));
        registro = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.POS_NAME));
        coord = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.USUARIO));
        causal = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAsistencia.Columnas.HORA));

        viewHolder.estado.setText(estado);
        viewHolder.codigo.setText(codigo);
        viewHolder.registro.setText(registro);
        viewHolder.coord.setText(coord);
        viewHolder.causal.setText(causal);
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