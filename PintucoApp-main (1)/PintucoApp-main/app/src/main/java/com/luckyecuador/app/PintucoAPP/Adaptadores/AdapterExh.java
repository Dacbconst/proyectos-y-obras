package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterExh extends RecyclerView.Adapter<AdapterExh.ViewHolder> {
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
        private TextView codigo;
        private TextView sector;
        private TextView categoria;
        private TextView brand;
        private TextView tipo_exh;
        private TextView zona_exh;
        private TextView nivel;
        private TextView tipo;
        private TextView contratada;
        private TextView user;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            hora = (TextView) v.findViewById(R.id.lblCategoria);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            sector = (TextView) v.findViewById(R.id.lblMarca);
          //  categoria =(TextView) v.findViewById(R.id.lblTipoexh);
            brand =(TextView) v.findViewById(R.id.lblNumex);
            tipo_exh =(TextView) v.findViewById(R.id.lblComentario);
            nivel = v.findViewById(R.id.lblNivel);
            tipo = v.findViewById(R.id.lblTipo);
         //   zona_exh =(TextView) v.findViewById(R.id.lblFecha);
         //   contratada =(TextView) v.findViewById(R.id.lblHora);
            user =(TextView) v.findViewById(R.id.lblUsuario);
        }
    }

    public AdapterExh(Context context) {
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
                .inflate(R.layout.row3, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado = "";

        String fecha = "";
        String hora = "";
        String codigo = "";
        String sector = "";
        String categoria = "";
        String brand = "";
        String tipo_exh = "";
        String nivel = "";
        String tipo = "";
        String zona_exh = "";
        String contratada = "";
        String user = "";

        try {
            consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
            if (consulta.equals("1")) {
                estado = "No Enviado";
            } else {
                estado = "Enviado";
            }

            fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.FECHA));
            hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.HORA));
            codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.POS_NAME));
            user = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.USUARIO));
            sector = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.CATEGORIA));
         //   categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.SUBCATEGORIA));
            brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.BRAND));
            tipo_exh = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.TIPO_EXH));
            nivel = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.NIVEL));
            tipo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.TIPO));
            zona_exh = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.ZONA_EX));
         //   contratada = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExh.Columnas.CONTRATADA));
        } catch (Exception e) {
            Log.i("EXHIBICIONES", e.getMessage());
        }

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.user.setText(user);
        viewHolder.sector.setText(sector);
      //  viewHolder.categoria.setText(categoria);
        viewHolder.brand.setText(brand);
        viewHolder.tipo_exh.setText(tipo_exh);
        viewHolder.nivel.setText(nivel);
        viewHolder.tipo.setText(tipo);
//        viewHolder.zona_exh.setText(zona_exh);
      //  viewHolder.contratada.setText(contratada);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}