package com.luckyecuador.app.PintucoAPP.Adaptadores;


import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.R;


public class SugeridosAdapter extends RecyclerView.Adapter<SugeridosAdapter.ViewHolder>  {

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
        private TextView local;
        private TextView codigo_fabril;
        private TextView vendedor_fabril;
        private TextView catego;
        private TextView sub;
        private TextView brand;
        private TextView sku;
        private TextView quiebre;
        private TextView disponible;
        private TextView sugerido;
        private TextView cantidad;
        private TextView observaciones;
        private TextView regular;
        private TextView promocion;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblMara);
            codigo = (TextView) v.findViewById(R.id.lblCategoria);
            usuario = (TextView) v.findViewById(R.id.lblTipo);
            local = (TextView) v.findViewById(R.id.lblLocal);
            codigo_fabril = (TextView) v.findViewById(R.id.lblCodigoFabril);
            vendedor_fabril = (TextView) v.findViewById(R.id.lblVendedor);
            catego = (TextView) v.findViewById(R.id.lblProductos);
            sub = (TextView) v.findViewById(R.id.lblSub);
       //     brand = (TextView) v.findViewById(R.id.lblCantidad);
            sku = (TextView) v.findViewById(R.id.lblPcont);
            quiebre = (TextView) v.findViewById(R.id.lblQuiebre);
            disponible = (TextView) v.findViewById(R.id.lblUDisponible);
            sugerido = (TextView) v.findViewById(R.id.lblSugerido);
            cantidad = (TextView) v.findViewById(R.id.lblCCantidad);
            observaciones = (TextView) v.findViewById(R.id.lblObservaciones);
        }
    }

    public SugeridosAdapter(Context context) {
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
                .inflate(R.layout.status_sugeridos, viewGroup, false);
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
        String local;
        String codigo_fabril;
        String vendedor_fabril;
        String catego;
        String sub;
        String brand;
        String sku;
        String quiebre;
        String disponible;
        String sugerido;
        String cantidad;
        String observaciones;
        String regular;
        String promocion;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.CODIGO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.USUARIO));
        local = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.LOCAL));
        codigo_fabril = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.CODIGO_FABRIL));
        vendedor_fabril = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.VENDEDOR_FABRIL));
        catego = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.CATEGORIA));
        sub = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.SUBCATEGORIA));
      //  brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.BRAND));
        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.SKU_CODE));
        quiebre = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.QUIEBRE));
        disponible = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE));
        sugerido = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.SUGERIDO));
        cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.CANTIDAD));
        observaciones = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.OBSERVACIONES));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.usuario.setText(usuario);
        viewHolder.local.setText(local);
        viewHolder.codigo_fabril.setText(codigo_fabril);
        viewHolder.vendedor_fabril.setText(vendedor_fabril);
        viewHolder.catego.setText(catego);
        viewHolder.sub.setText(sub);
      //  viewHolder.brand.setText(brand);
        viewHolder.sku.setText(sku);
        viewHolder.quiebre.setText(quiebre);
        viewHolder.disponible.setText(disponible);
        viewHolder.sugerido.setText(sugerido);
        viewHolder.cantidad.setText(cantidad);
        viewHolder.observaciones.setText(observaciones);
     /*   viewHolder.regular.setText(regular);
        viewHolder.promocion.setText(promocion);*/
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
