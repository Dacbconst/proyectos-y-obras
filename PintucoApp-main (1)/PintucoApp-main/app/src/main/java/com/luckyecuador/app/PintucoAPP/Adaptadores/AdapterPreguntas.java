package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterPreguntas extends RecyclerView.Adapter<AdapterPreguntas.ViewHolder> {
    
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
        private TextView fecha;
        private TextView hora;
        private TextView usuario;
        private TextView correctas;
        private TextView incorrectas;
        private TextView calificacion;
        private TextView observacion;

        private TextView tiempo;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            hora = (TextView) v.findViewById(R.id.lblHora);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            correctas = (TextView) v.findViewById(R.id.lblCorrectas);
            incorrectas = (TextView) v.findViewById(R.id.lblIncorrectas);
            calificacion = (TextView) v.findViewById(R.id.lblCalificacion);
            observacion = (TextView) v.findViewById(R.id.lblObservacion);
            tiempo = (TextView)  v.findViewById(R.id.lblTiempo);
        }
    }

    public AdapterPreguntas(Context context) {
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
                .inflate(R.layout.status_preguntas, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String fecha;
        String hora;
        String usuario;
        String correctas;
        String incorrectas;
        String calificacion;
        String observacion;
        String tiempo;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.USUARIO));
        correctas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.CORRECTAS));
        incorrectas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.INCORRECTAS));
        calificacion = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.CALIFICACION));
        observacion = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.OBSERVACION));
        tiempo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreguntas.Columnas.CRONOMETO));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.usuario.setText(usuario);
        viewHolder.correctas.setText(correctas);
        viewHolder.incorrectas.setText(incorrectas);
        viewHolder.calificacion.setText(calificacion);
        viewHolder.observacion.setText(observacion);
        viewHolder.tiempo.setText(tiempo);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}