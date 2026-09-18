package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.R;

public class AdapterRotacion extends RecyclerView.Adapter<AdapterRotacion.ViewHolder> {

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
        private TextView usuario;
        private TextView categoria;
        private TextView producto;
        private TextView mecanica;
        private TextView cantidad;
        private TextView fecha_rot;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblHora);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            producto = (TextView) v.findViewById(R.id.lblProductos);
            mecanica = (TextView) v.findViewById(R.id.lblMecanica);
            cantidad = (TextView) v.findViewById(R.id.lblCantidad);
            fecha_rot = (TextView) v.findViewById(R.id.lblFechaRot);
        }
    }

    public AdapterRotacion(Context context) {
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
                .inflate(R.layout.status_rotacion, viewGroup, false);
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
        String usuario;
        String categoria;
        String producto;
        String mecanica;
        String cantidad;
        String fecha_rot;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.CODIGO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.USUARIO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.CATEGORIA));
        producto = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.PRODUCTO));
        mecanica = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.MECANICA));
        cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.CANTIDAD));
        fecha_rot = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertRotacion.Columnas.FECHA_ROT));


        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.categoria.setText(categoria);
        viewHolder.producto.setText(producto);
        viewHolder.mecanica.setText(mecanica);
        viewHolder.cantidad.setText(cantidad);
        viewHolder.fecha_rot.setText(fecha_rot);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}