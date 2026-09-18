package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 28/02/2018.
 */

public class AdapterFlooring extends RecyclerView.Adapter<AdapterFlooring.ViewHolder> {

    private Cursor cursor;
    private Context context;


    // Instancia de escucha
    private AdapterFlooring.OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    interface OnItemClickListener {
        public void onClick(AdapterFlooring.ViewHolder holder, String idContacto);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
        private TextView inventario;
        private TextView semana;
        private TextView entrega;
        private TextView tamano;
        private TextView tipo;
        private TextView causal;
        private TextView otros;
        private TextView caducidad;
        private TextView fecha_caducidad;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblhora);
            codigo = (TextView) v.findViewById(R.id.lblcodigo);
            sector = (TextView) v.findViewById(R.id.lblMarca);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            segmento1 = (TextView) v.findViewById(R.id.lblSubcategoria);
            segmento2 = (TextView) v.findViewById(R.id.lblProductos);
            brand = (TextView) v.findViewById(R.id.lblInventario);
            tamano = (TextView) v.findViewById(R.id.lblDisplay);
            tipo = (TextView) v.findViewById(R.id.lblTipo);
            productos = (TextView) v.findViewById(R.id.lblFecha);
            inventario = (TextView) v.findViewById(R.id.lblHora);
            semana = (TextView) v.findViewById(R.id.lblSemana);
            entrega = (TextView) v.findViewById(R.id.lblEntrega);
            causal = (TextView) v.findViewById(R.id.lblCausal);
            otros = (TextView) v.findViewById(R.id.lblOtros);
            caducidad = (TextView) v.findViewById(R.id.lblFcad);
            fecha_caducidad = (TextView) v.findViewById(R.id.lblFechaCaducidad1);

        }
    }


    public AdapterFlooring(Context context) {
        this.context= context;

    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public AdapterFlooring.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.status_inventario, viewGroup, false);
        return new AdapterFlooring.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterFlooring.ViewHolder viewHolder, int i) {
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
        String inventario;
        String semana;
        String entrega;
        String segmento2;
        String brand;
        String tamano;
        String tipo;
        String causal;
        String otros;
        String fecha_cad;
        String fecha_caducidad;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }

        fecha = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.FECHA));
        hora = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.HORA));
        codigo = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.POS_NAME));
        sector = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.USUARIO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.CATEGORIA));
        segmento1 = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SUBCATEGORIA));
        segmento2 = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.FECHA_CADUCIDAD));
        brand = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.BRAND));
        productos = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE));
        inventario = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.INVENTARIOS));
        semana = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SEMANA));
        tamano = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SUGERIDOS));
        tipo = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.TIPO));
        entrega = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.ENTREGA));
        causal = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.CAUSAL));
        otros = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.OTROS));
        fecha_cad = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.FECHA_CADUCIDAD));
        fecha_caducidad = cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.FECHA_INVENTARIO));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.hora.setText(hora);
        viewHolder.codigo.setText(codigo);
        viewHolder.sector.setText(sector);
        viewHolder.categoria.setText(categoria);
        viewHolder.segmento1.setText(segmento1);
        viewHolder.segmento2.setText(segmento2);
        viewHolder.brand.setText(brand);
        viewHolder.productos.setText(productos);
        viewHolder.inventario.setText(inventario);
        viewHolder.semana.setText(semana);
        viewHolder.entrega.setText(entrega);
        viewHolder.tamano.setText(tipo);
        viewHolder.tipo.setText(otros);
        viewHolder.causal.setText(otros);
        viewHolder.otros.setText(causal);
        viewHolder.caducidad.setText(fecha_cad);
        viewHolder.fecha_caducidad.setText(fecha_caducidad);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}