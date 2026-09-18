package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterAlmuerzo extends RecyclerView.Adapter<AdapterAlmuerzo.ViewHolder> {
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
        private TextView usuario;
        private TextView fecha;
        private TextView hora_ini_almuezo;
        private TextView hora_fin_almuerzo;


        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            hora_ini_almuezo = (TextView) v.findViewById(R.id.lblInicioAlmuerzo);
            hora_fin_almuerzo = (TextView) v.findViewById(R.id.lblFinAlmuerzo);

        }
    }

    public AdapterAlmuerzo(Context context) {
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
                .inflate(R.layout.status_almuerzo, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        DatabaseHelper handler = new DatabaseHelper(context, Provider.DATABASE_NAME,null,1);

        String consulta;

        String estado;
        String usuario;
        String fecha;
        String hora_ini_almuerzo;
        String hora_fin_almuerzo;


        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if(consulta.equals("1")){
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAlmuerzo.Columnas.USUARIO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAlmuerzo.Columnas.FECHA));
        hora_ini_almuerzo= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAlmuerzo.Columnas.HORA_INI_ALMUERZO));
        hora_fin_almuerzo= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertAlmuerzo.Columnas.HORA_FIN_ALMUERZO));


        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.usuario.setText(usuario);
        viewHolder.hora_ini_almuezo.setText(hora_ini_almuerzo);
        viewHolder.hora_fin_almuerzo.setText(hora_fin_almuerzo);


    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}