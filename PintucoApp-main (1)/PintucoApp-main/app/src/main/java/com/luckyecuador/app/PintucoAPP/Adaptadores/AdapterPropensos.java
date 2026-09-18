package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class AdapterPropensos extends RecyclerView.Adapter<AdapterPropensos.ViewHolder> {

    private Cursor cursor;
    private Context context;


    // Instancia de escucha
    private AdapterPropensos.OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    interface OnItemClickListener {
        public void onClick(AdapterFlooring.ViewHolder holder, String idContacto);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {





        private TextView tv_estado;
        private TextView tv_fecha;
        private TextView tv_hora;
        private TextView tv_codigo;
        private TextView tv_categoria;
        private TextView tv_marca;
        private TextView tv_sku;
        private TextView tv_sku_cliente;
        private TextView tv_Litros;
        private TextView tv_dias_restantes;
        private TextView tv_valorizado;
        private TextView tv_TipoConteoTotal;
        private TextView tv_Inventario;
        private TextView tv_FechaCaducidadTotal;
        private TextView tv_TipoConteoDefectuoso;
        private TextView tv_Defectuosos;
        private TextView tv_Causal;

        // Campos respectivos de un item
        private TextView estado;
        private TextView fecha;
        private TextView hora;
        private TextView codigo;
        private TextView sector;
        private TextView categoria;
        private TextView segmento1;
        private TextView segmento2;
        private TextView brand;
        private TextView productos;
        private TextView sku_cliente;
        private TextView litros;
        private TextView dias_restantes;
        private TextView valorizado;
        private TextView tipo_conteo_total;
        private TextView inventario;
        private TextView fecha_caducidad_total;
        private TextView defectuosos;
        private TextView tipo_conteo_defectuoso;
        private TextView causal;
        private TextView tamano;

        public ViewHolder(View v) {
            super(v);



            tv_estado = (TextView) v.findViewById(R.id.tv_estado);
            tv_fecha = (TextView) v.findViewById(R.id.tv_fecha);
            tv_hora = (TextView) v.findViewById(R.id.tv_hora);
            tv_codigo = (TextView) v.findViewById(R.id.tv_codigo);
            tv_categoria = (TextView) v.findViewById(R.id.tv_categoria);
            tv_marca = (TextView) v.findViewById(R.id.tv_marca);
            tv_sku = (TextView) v.findViewById(R.id.tv_sku);
            tv_TipoConteoTotal = (TextView) v.findViewById(R.id.tv_TipoConteoTotal);
            tv_Inventario = (TextView) v.findViewById(R.id.tv_Total);
            tv_FechaCaducidadTotal = (TextView) v.findViewById(R.id.tv_FechaCaducidadTotal);
            tv_TipoConteoDefectuoso = (TextView) v.findViewById(R.id.tv_TipoConteoDefectuosos);
            tv_Defectuosos = (TextView) v.findViewById(R.id.tv_TotalDefectuoso);
            tv_sku_cliente = (TextView) v.findViewById(R.id.tv_sku_cliente);
            tv_Litros = (TextView) v.findViewById(R.id.tv_Litros);
            tv_dias_restantes = (TextView) v.findViewById(R.id.tv_dias_restantes);
            tv_valorizado = (TextView) v.findViewById(R.id.tv_valorizado);
            tv_Causal = (TextView) v.findViewById(R.id.tv_Causal);


            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblhora);
            codigo = (TextView) v.findViewById(R.id.lblcodigo);
            sector = (TextView) v.findViewById(R.id.lblMarca);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            segmento1 = (TextView) v.findViewById(R.id.lblTipo);
            segmento2 = (TextView) v.findViewById(R.id.lblProductos);
            brand = (TextView) v.findViewById(R.id.lblMarca);
            tamano = (TextView) v.findViewById(R.id.lblDisplay);
            productos = (TextView) v.findViewById(R.id.lblSku);
            sku_cliente = (TextView) v.findViewById(R.id.lblSkuCliente); //mpin
            litros = (TextView) v.findViewById(R.id.lblLitros); //mpin
            dias_restantes = (TextView) v.findViewById(R.id.lblDiasRestantes); //mpin
            valorizado= (TextView) v.findViewById(R.id.lblValorizado); //mpin
            causal = (TextView) v.findViewById(R.id.lblCausal);




            tipo_conteo_total = (TextView) v.findViewById(R.id.lblTipoConteoTotal);
            inventario = (TextView) v.findViewById(R.id.lblInventario);



            fecha_caducidad_total = (TextView) v.findViewById(R.id.lblFechaCaducidadTotal);
            tipo_conteo_defectuoso = (TextView) v.findViewById(R.id.lblTipoConteoDefectuoso);
            defectuosos = (TextView) v.findViewById(R.id.lblDefectuosos);
         //   causal = (TextView) v.findViewById(R.id.lblCausal);

        }
    }


    public AdapterPropensos(Context context) {
        this.context= context;

    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public AdapterPropensos.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.status_propensos_y_prod_mal_est, viewGroup, false);
        return new AdapterPropensos.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterPropensos.ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
        String fecha;
        String hora;
        String codigo;
        String sector;
        String categoria;
        String segmento1;
        String productos;
        String tipo_conteo_total;
        String inventario;
        String fecha_caducidad_total;
        String defectuosos;
        String tipo_conteo_defectuoso;
      //  String causal;
        String segmento2;
        String brand;
        String tamano;
        String modulo;
        String litros;
        String dias_restantes;
        String valorizado;
        String sku_cliente;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if(consulta.equals("1")){
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.HORA));
        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.CODIGO));
        sector = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.SECTOR));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA));
        segmento1 = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA));
        segmento2 = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.BRAND));
        productos = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE));
        tipo_conteo_total = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL));
        inventario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.TOTAL));
        fecha_caducidad_total = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL));

        defectuosos = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS));
        tipo_conteo_defectuoso = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS));
       // causal = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.CAUSAL));
        modulo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.MODULO));
        litros = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.LITROS));
        dias_restantes = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.DIAS_RESTANTES));
        valorizado = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.VALORIZADO));
        sku_cliente = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.SKU_CLIENTE));

        tamano = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.sector.setText(sector);
        viewHolder.categoria.setText(categoria);
    //    viewHolder.segmento1.setText(segmento1);
    //    viewHolder.segmento2.setText(segmento2);
        viewHolder.brand.setText(brand);
        viewHolder.productos.setText(productos);
        viewHolder.tipo_conteo_total.setText(tipo_conteo_total);
        viewHolder.inventario.setText(inventario);
        viewHolder.fecha_caducidad_total.setText(fecha_caducidad_total);

        viewHolder.tipo_conteo_defectuoso.setText(tipo_conteo_defectuoso);
        viewHolder.defectuosos.setText(defectuosos);
      //  viewHolder.causal.setText(causal);
        viewHolder.sku_cliente.setText(sku_cliente);
        viewHolder.litros.setText(litros);
        viewHolder.dias_restantes.setText(dias_restantes);
        viewHolder.valorizado.setText(valorizado);
    //    viewHolder.tamano.setText(tamano);



        if (modulo.equalsIgnoreCase("PROPENSOS")){

            viewHolder.tv_estado.setVisibility(View.VISIBLE);
            viewHolder.estado.setVisibility(View.VISIBLE);
            viewHolder.tv_fecha.setVisibility(View.VISIBLE);
            viewHolder.fecha.setVisibility(View.VISIBLE);
            viewHolder.tv_hora.setVisibility(View.VISIBLE);
            viewHolder.hora.setVisibility(View.VISIBLE);
            viewHolder.tv_codigo.setVisibility(View.VISIBLE);
            viewHolder.codigo.setVisibility(View.VISIBLE);
            viewHolder.tv_categoria.setVisibility(View.VISIBLE);
            viewHolder.categoria.setVisibility(View.VISIBLE);
            viewHolder.tv_marca.setVisibility(View.VISIBLE);
            viewHolder.brand.setVisibility(View.VISIBLE);
            viewHolder.tv_sku.setVisibility(View.VISIBLE);
            viewHolder.productos.setVisibility(View.VISIBLE);
            viewHolder.tv_sku_cliente.setVisibility(View.VISIBLE);
            viewHolder.sku_cliente.setVisibility(View.VISIBLE);
            viewHolder.tv_Litros.setVisibility(View.VISIBLE);
            viewHolder.litros.setVisibility(View.VISIBLE);
            viewHolder.tv_dias_restantes.setVisibility(View.VISIBLE);
            viewHolder.dias_restantes.setVisibility(View.VISIBLE);
            viewHolder.tv_valorizado.setVisibility(View.VISIBLE);
            viewHolder.valorizado.setVisibility(View.VISIBLE);


            viewHolder.tv_TipoConteoDefectuoso.setVisibility(View.GONE);
            viewHolder.tipo_conteo_defectuoso.setVisibility(View.GONE);
            viewHolder.tv_Defectuosos.setVisibility(View.GONE);
            viewHolder.defectuosos.setVisibility(View.GONE);
           viewHolder.tv_Causal.setVisibility(View.GONE);
            viewHolder.causal.setVisibility(View.GONE);

            viewHolder.tv_TipoConteoTotal.setVisibility(View.VISIBLE);
            viewHolder.tipo_conteo_total.setVisibility(View.VISIBLE);
            viewHolder.tv_Inventario.setVisibility(View.VISIBLE);
            viewHolder.inventario.setVisibility(View.VISIBLE);
            viewHolder.tv_FechaCaducidadTotal.setVisibility(View.VISIBLE);
            viewHolder.fecha_caducidad_total.setVisibility(View.VISIBLE);



        } else {

            viewHolder.tv_estado.setVisibility(View.GONE);
            viewHolder.estado.setVisibility(View.GONE);
            viewHolder.tv_fecha.setVisibility(View.GONE);
            viewHolder.fecha.setVisibility(View.GONE);
            viewHolder.tv_hora.setVisibility(View.GONE);
            viewHolder.hora.setVisibility(View.GONE);
            viewHolder.tv_codigo.setVisibility(View.GONE);
            viewHolder.codigo.setVisibility(View.GONE);
            viewHolder.tv_categoria.setVisibility(View.GONE);
            viewHolder.categoria.setVisibility(View.GONE);
            viewHolder.tv_marca.setVisibility(View.GONE);
            viewHolder.brand.setVisibility(View.GONE);
            viewHolder.tv_sku.setVisibility(View.GONE);
            viewHolder.productos.setVisibility(View.GONE);
            viewHolder.tv_sku_cliente.setVisibility(View.GONE);
            viewHolder.sku_cliente.setVisibility(View.GONE);
            viewHolder.tv_Litros.setVisibility(View.GONE);
            viewHolder.litros.setVisibility(View.GONE);
            viewHolder.tv_dias_restantes.setVisibility(View.GONE);
            viewHolder.dias_restantes.setVisibility(View.GONE);
            viewHolder.tv_valorizado.setVisibility(View.GONE);
            viewHolder.valorizado.setVisibility(View.GONE);

            viewHolder.tv_TipoConteoDefectuoso.setVisibility(View.GONE);
            viewHolder.tipo_conteo_defectuoso.setVisibility(View.GONE);
            viewHolder.tv_Defectuosos.setVisibility(View.GONE);
            viewHolder.defectuosos.setVisibility(View.GONE);
            viewHolder.tv_Causal.setVisibility(View.GONE);
           viewHolder.causal.setVisibility(View.GONE);

            viewHolder.tv_TipoConteoTotal.setVisibility(View.GONE);
            viewHolder.tipo_conteo_total.setVisibility(View.GONE);
            viewHolder.tv_Inventario.setVisibility(View.GONE);
            viewHolder.inventario.setVisibility(View.GONE);
            viewHolder.tv_FechaCaducidadTotal.setVisibility(View.GONE);
            viewHolder.fecha_caducidad_total.setVisibility(View.GONE);

        }




    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
