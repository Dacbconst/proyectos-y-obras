package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterVentas extends RecyclerView.Adapter<AdapterVentas.ViewHolder> {
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

        private TextView tipo_venta;
        private TextView fecha_venta;

        private TextView tv_stock_inicial;
        private TextView stock_inicial;

        private TextView cantidad;
        private TextView regular;
        private TextView tv_valor_total;
        private TextView valor_total;
        private TextView tv_total_factura;
        private TextView total_factura;
        private TextView tv_numero_factura;
        private TextView numero_factura;
        private TextView tv_entrego_promocional;
        private TextView entrego_promocional;
        private TextView tv_promocional;
        private TextView promocional;
        private TextView tv_cant_promocional;
        private TextView cant_promocional;
        private TextView promocion;

        private TextView tv_stock_final;

        private TextView stock_final;
        private TextView ofer;

        private TextView punitario;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblMara);
            codigo = (TextView) v.findViewById(R.id.lblCategoria);
            usuario = (TextView) v.findViewById(R.id.lblTipo);
            catego = (TextView) v.findViewById(R.id.lblProductos);
            brand = (TextView) v.findViewById(R.id.lblMarca);
            sku = (TextView) v.findViewById(R.id.lblPcont);
            fecha_venta = (TextView) v.findViewById(R.id.lblFechaVentas);
            tipo_venta = (TextView) v.findViewById(R.id.lblTipoVenta);
            tv_stock_inicial = (TextView) v.findViewById(R.id.tvStockInicial);
            stock_inicial = (TextView) v.findViewById(R.id.lblStockInicial);
            tv_stock_final = (TextView) v.findViewById(R.id.tvStockFinal);
            stock_final = (TextView) v.findViewById(R.id.lblStockFinal);
            cantidad = (TextView) v.findViewById(R.id.lblFecha);
            regular = (TextView) v.findViewById(R.id.lblHora);
            tv_valor_total = (TextView) v.findViewById(R.id.tvValorTotal);
            valor_total = (TextView) v.findViewById(R.id.lblValorTotal);
            tv_total_factura = (TextView) v.findViewById(R.id.tvTotalFactura);
            total_factura = (TextView) v.findViewById(R.id.lblTotalFactura);
            tv_numero_factura = (TextView) v.findViewById(R.id.tvNumeroFactura);
            numero_factura = (TextView) v.findViewById(R.id.lblNumeroFactura);
            tv_entrego_promocional = (TextView) v.findViewById(R.id.tvEntregoPromocional);
            entrego_promocional = (TextView) v.findViewById(R.id.lblEntregoPromocional);
            tv_promocional = (TextView) v.findViewById(R.id.tvPromocional);
            promocional = (TextView) v.findViewById(R.id.lblPromocional);
            tv_cant_promocional = (TextView) v.findViewById(R.id.tvCantPromocional);
            cant_promocional = (TextView) v.findViewById(R.id.lblCantPromocional);
            sub = (TextView) v.findViewById(R.id.lblSub);
        }
    }

    public AdapterVentas(Context context) {
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
                .inflate(R.layout.status_ventas, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        DatabaseHelper handler = new DatabaseHelper(context, Provider.DATABASE_NAME,null,1);

        String consulta;

        String estado;
        String codigo;
        String fecha;
        String hora;
        String usuario;
        String catego;
        String brand;
        String sku;
        String tipo_venta;
        String fecha_ventas;
        String stock_incial;
        String cantidad;
        String regular;
        String valor_total;
        String total_factura;
        String numero_factura;
        String entrego_promocional;
        String promocional;
        String cant_promocional;
        String promocion;
        String stock_final;
        String sub;
        String ofer;
        String pos_name;
        String punitario;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if(consulta.equals("1")){
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.CODIGO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.USUARIO));
        catego = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.CATEGORIA));
        sub = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.SUBCATEGORIA));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.MARCA));
        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.SKU_CODE));
        tipo_venta = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.TIPO_VENTA));
        fecha_ventas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.FECHA_VENTA));
        stock_incial = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.STOCK_INICIAL));
        cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.CANTIDAD));
        regular = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.PREGULAR));
        promocion = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.PPROMOCION));
        stock_final = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.STOCK_FINAL));
        pos_name = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.POS_NAME));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(pos_name);
        viewHolder.usuario.setText(usuario);
        viewHolder.catego.setText(catego);
        viewHolder.sub.setText(sub);
        viewHolder.brand.setText(brand);
        viewHolder.sku.setText(sku);
        viewHolder.tipo_venta.setText(tipo_venta);
        viewHolder.fecha_venta.setText(fecha_ventas);
        viewHolder.cantidad.setText(cantidad);
        viewHolder.regular.setText(regular);



    //    viewHolder.promocion.setText(promocion);

    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}