package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.R;

public class AdapterMaterialesRecibidos extends RecyclerView.Adapter<AdapterMaterialesRecibidos.ViewHolder> {

    private Cursor cursor;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView fecha;
        private TextView hora;
        private TextView codigo;
        private TextView usuario;
        private TextView alerta;
        private TextView tipo;
        private TextView producto;
        private TextView cantidad;
        private TextView estado_material;
        private TextView prioridad;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblHora);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            alerta = (TextView) v.findViewById(R.id.lblAlerta);
            tipo = (TextView) v.findViewById(R.id.lblTipo);
            producto = (TextView) v.findViewById(R.id.lblProductos);
            cantidad = (TextView) v.findViewById(R.id.lblCantidad);
            estado_material = (TextView) v.findViewById(R.id.lblEstadoMaterial);
            prioridad = (TextView) v.findViewById(R.id.lblPrioridad);
        }
    }

    public AdapterMaterialesRecibidos(Context context) {
        this.context= context;
    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.status_materiales_recibidos, viewGroup, false);
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
        String alerta;
        String tipo;
        String producto;
        String cantidad;
        String estado_material;
        String prioridad;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.CODIGO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.USUARIO));
        alerta = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.ALERTA));
        tipo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.TIPO));
        producto = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.MATERIAL));
        cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.CANTIDAD));
        estado_material = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.ESTADO_MATERIAL));
        prioridad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertMaterialesRecibidos.Columnas.PRIORIDAD));

        Log.i("ESTADO", estado);

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.alerta.setText(alerta);
        viewHolder.tipo.setText(tipo);
        viewHolder.producto.setText(producto);
        viewHolder.cantidad.setText(cantidad);
        viewHolder.estado_material.setText(estado_material);
        viewHolder.prioridad.setText(prioridad);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}