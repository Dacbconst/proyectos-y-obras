package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterLogisticoRelevo extends RecyclerView.Adapter<AdapterLogisticoRelevo.ViewHolder> {
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

        private TextView brand;
        private TextView sku;
        private TextView cantidad;

        private TextView causal;
        private TextView tipo_logistico;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblHora);

            codigo = (TextView) v.findViewById(R.id.lblLocal);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);

            brand = (TextView) v.findViewById(R.id.lblBrand);
            sku = (TextView) v.findViewById(R.id.lblSKU);
            cantidad = (TextView) v.findViewById(R.id.lblCantidad);

            causal = (TextView) v.findViewById(R.id.lblCausal);
            tipo_logistico = (TextView) v.findViewById(R.id.lblTipoLogistico);
        }
    }

    public AdapterLogisticoRelevo(Context context) {
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
                .inflate(R.layout.status_logistico_relevo, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;
        String estado;
        String fecha;

        String hora;
        String codigo;
        String usuario;

        String categoria;
        String brand;
        String sku;

        String cantidad;
        String causal;
        String tipo_logistico;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.HORA));
        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.CODIGO));

        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.USUARIO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.CATEGORIA));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.BRAND));

        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.SKU_CODE));
        cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.PREGULAR));
        causal = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.CAUSAL));

        tipo_logistico = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);

        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.categoria.setText(categoria);

        viewHolder.brand.setText(brand);
        viewHolder.sku.setText(sku);
        viewHolder.cantidad.setText(cantidad);

        viewHolder.causal.setText(causal);
        viewHolder.tipo_logistico.setText(tipo_logistico);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}