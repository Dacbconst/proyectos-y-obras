package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterImpulso extends RecyclerView.Adapter<AdapterImpulso.ViewHolder> {
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
        private TextView brand;
        private TextView sku;
        private TextView asig;
        private TextView vend;
        private TextView adic;
        private TextView cumpl;
        private TextView imp;
        private TextView obs;

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
            asig = (TextView) v.findViewById(R.id.lblAsig);
            vend = (TextView) v.findViewById(R.id.lblVend);
            adic = (TextView) v.findViewById(R.id.lblAdic);
            cumpl = (TextView) v.findViewById(R.id.lblCumpl);
            imp = (TextView) v.findViewById(R.id.lblImp);
            obs = (TextView) v.findViewById(R.id.lblObs);
        }
    }

    public AdapterImpulso(Context context) {
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
                .inflate(R.layout.status_impulso, viewGroup, false);
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
        String asig;
        String vend;
        String adic;
        String cumpl;
        String imp;
        String obs;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.POS_NAME));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.USUARIO));
        catego = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.CATEGORIA));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.BRAND));
        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.SKU_CODE));
        asig = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.CANTIDAD_ASIGNADA));
        vend = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.CANTIDAD_VENDIDA));
        adic = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.CANTIDAD_ADICIONAL));
        cumpl = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.CUMPLIMIENTO));
        imp = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.IMPULSADORA));
        obs = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertImpulso.Columnas.OBSERVACION));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.catego.setText(catego);
        viewHolder.brand.setText(brand);
        viewHolder.sku.setText(sku);
        viewHolder.asig.setText(asig);
        viewHolder.vend.setText(vend);
        viewHolder.adic.setText(adic);
        viewHolder.cumpl.setText(cumpl);
        viewHolder.imp.setText(imp);
        viewHolder.obs.setText(obs);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}