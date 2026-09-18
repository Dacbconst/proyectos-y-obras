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
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.R;

public class AdapterCanjes extends RecyclerView.Adapter<AdapterCanjes.ViewHolder> {

    private Cursor cursor;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView fecha;
        private TextView hora;
        private TextView codigo;
        private TextView usuario;
        private TextView categoria;
        private TextView subcategoria;
        private TextView marca;
        private TextView producto;
        private TextView tipo_combo;
        private TextView mecanica;
        private TextView armados;
        private TextView stock;
        private TextView pvc_unitario;
        private TextView pvc_combo;
        private TextView visita;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblHora);
            codigo = (TextView) v.findViewById(R.id.lblCodigo);
            usuario = (TextView) v.findViewById(R.id.lblUsuario);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            subcategoria = (TextView) v.findViewById(R.id.lblSubcategoria);
            marca = (TextView) v.findViewById(R.id.lblMarca);
            producto = (TextView) v.findViewById(R.id.lblProductos);
            tipo_combo = (TextView) v.findViewById(R.id.lblTipoCombo);
            mecanica = (TextView) v.findViewById(R.id.lblMecanica);
            armados = (TextView) v.findViewById(R.id.lblCantidad);
            stock = (TextView) v.findViewById(R.id.lblStock);
            pvc_unitario = (TextView) v.findViewById(R.id.lblPVCUnitario);
            pvc_combo = (TextView) v.findViewById(R.id.lblPVCCombo);
            visita = (TextView) v.findViewById(R.id.lblFechaRot);
        }
    }

    public AdapterCanjes(Context context) {
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
                .inflate(R.layout.status_canjes, viewGroup, false);
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
        String subcategoria;
        String marca;
        String producto;
        String tipo_combo;
        String mecanica;
        String armados;
        String stock;
        String pvc_unitario;
        String pvc_combo;
        String visita;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.POS_NAME));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.USUARIO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.CATEGORIA));
        subcategoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.SUBCATEGORIA));
        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.MARCA));
        producto = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PRODUCTO));
        tipo_combo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.TIPO_COMBO));
        mecanica = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.MECANICA));
        armados = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.COMBOS_ARMADOS));
        stock = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.STOCK));
        pvc_unitario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PVC_UNITARIO));
        pvc_combo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PVC_COMBO));
        visita = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.VISITA));

        Log.i("ESTADO", estado);

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.categoria.setText(categoria);
        viewHolder.subcategoria.setText(subcategoria);
        viewHolder.marca.setText(marca);
        viewHolder.producto.setText(producto);
        viewHolder.tipo_combo.setText(tipo_combo);
        viewHolder.mecanica.setText(mecanica);
        viewHolder.armados.setText(armados);
        viewHolder.stock.setText(stock);
        viewHolder.pvc_unitario.setText(pvc_unitario);
        viewHolder.pvc_combo.setText(pvc_combo);
        viewHolder.visita.setText(visita);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}