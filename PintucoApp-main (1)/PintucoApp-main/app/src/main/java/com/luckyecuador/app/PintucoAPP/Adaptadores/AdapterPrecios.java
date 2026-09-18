package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterPrecios extends RecyclerView.Adapter<AdapterPrecios.ViewHolder> {
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
        private TextView catego;
        private TextView sub;
        private TextView brand;
        private TextView sku;
        private TextView regular;
        private TextView promocion;
        private TextView ofer;

        private TextView tipo;

        private TextView pvm;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblMara);
            codigo = (TextView) v.findViewById(R.id.lblCategoria);
            usuario = (TextView) v.findViewById(R.id.lblTipo);
            catego = (TextView) v.findViewById(R.id.lblProductos);
            brand = (TextView) v.findViewById(R.id.lblCantidad);
            sku = (TextView) v.findViewById(R.id.lblPcont);
            regular = (TextView) v.findViewById(R.id.lblFecha);
            promocion = (TextView) v.findViewById(R.id.lblHora);
            sub = (TextView) v.findViewById(R.id.lblSub);
            ofer = (TextView) v.findViewById(R.id.lblOfert);
            tipo = (TextView) v.findViewById(R.id.lblTipo2);
            pvm = (TextView) v.findViewById(R.id.lblPvm);
        }
    }

    public AdapterPrecios(Context context) {
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
                .inflate(R.layout.status_precios, viewGroup, false);
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
        String catego;
        String brand;
        String sku;
        String regular;
        String promocion;
        String sub;
        String ofer;
        String tipo;
        String pvm;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.POS_NAME));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.USUARIO));
        catego = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.CATEGORIA));
        sub = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.SUBCATEGORIA));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.BRAND));
        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.SKU_CODE));
        regular = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.PREGULAR));
        promocion = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.PPROMOCION));
        ofer = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.POFERTA));
        tipo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.TIPO));
        pvm = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.PVM));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.catego.setText(catego);
        viewHolder.sub.setText(sub);
        viewHolder.brand.setText(brand);
        viewHolder.sku.setText(sku);
        viewHolder.regular.setText(regular);
        viewHolder.promocion.setText(promocion);
        viewHolder.ofer.setText(ofer);
        viewHolder.tipo.setText(tipo);
        viewHolder.pvm.setText(pvm);

    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}