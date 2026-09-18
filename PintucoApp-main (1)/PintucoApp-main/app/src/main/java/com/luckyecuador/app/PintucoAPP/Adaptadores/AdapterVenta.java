package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 27/03/2018.
 */

public class AdapterVenta extends RecyclerView.Adapter<AdapterVenta.ViewHolder>  {

    private Cursor cursor;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView estado;
        private TextView codigo;
        private TextView tipo_factura;
        private TextView num_factura;
        private TextView monto_factura;
        private TextView fecha_venta;
        private TextView fecha;
        private TextView hora;


        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            codigo = (TextView) v.findViewById(R.id.lblCategoria);
            tipo_factura = (TextView) v.findViewById(R.id.lblsubcat);
            num_factura = (TextView) v.findViewById(R.id.lblmarca);
            monto_factura = (TextView) v.findViewById(R.id.lblproducto);
            fecha_venta = (TextView) v.findViewById(R.id.lbltotal);
            fecha = (TextView) v.findViewById(R.id.lblfecha);
            hora = (TextView) v.findViewById(R.id.lblhora);
        }
    }


    public AdapterVenta(Context context) {
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
                .inflate(R.layout.status_venta, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String codigo;
        String tipo_factura;
        String num_factura;
        String monto_factura;
        String fecha_venta;
        String fecha;
        String hora;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.CODIGO));
        tipo_factura = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.TIPO_FACTURA));
        num_factura = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.NUM_FACTURA));
        monto_factura = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.MONTO_FACTURA));
        fecha_venta = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.FECHA_VENTA));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVenta.Columnas.HORA));

        viewHolder.estado.setText(estado);
        viewHolder.codigo.setText(codigo);
        viewHolder.tipo_factura.setText(tipo_factura);
        viewHolder.num_factura.setText(num_factura);
        viewHolder.monto_factura.setText(monto_factura);
        viewHolder.fecha_venta.setText(fecha_venta);
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