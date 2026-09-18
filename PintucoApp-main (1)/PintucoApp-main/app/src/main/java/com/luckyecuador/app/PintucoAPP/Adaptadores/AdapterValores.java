package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 18/04/2018.
 */

public class AdapterValores extends RecyclerView.Adapter<AdapterValores.ViewHolder> {

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
     //   private TextView fechainicio;
        private TextView marca;
        private TextView categoria;
        private TextView tipo;
        private TextView productos;
        private TextView pcont;
        private TextView pcuotas;
        private TextView cuotas;
        private TextView ventrada;
        private TextView fpago;
      //  private TextView promo;
        private TextView fecha;
        private TextView pvp;
        private TextView pvc;
        private TextView poferta;
        //private TextView hora;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            // fechainicio = (TextView) v.findViewById(R.id.lblfechainicial);
            marca = (TextView) v.findViewById(R.id.lblMara);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            tipo = (TextView) v.findViewById(R.id.lblTipo);
            productos = (TextView) v.findViewById(R.id.lblProductos);
            pcont = (TextView) v.findViewById(R.id.lblPcont);
            pcuotas = (TextView) v.findViewById(R.id.lblPcuotas);
            cuotas = (TextView) v.findViewById(R.id.lblVcuotas);
            ventrada = (TextView) v.findViewById(R.id.lblVentrada);
            fpago = (TextView) v.findViewById(R.id.lblFpago);
            // promo = (TextView) v.findViewById(R.id.lblPromocion);
            fecha = (TextView) v.findViewById(R.id.lblFecha);
            pvp = (TextView) v.findViewById(R.id.lblPVP);
            pvc = (TextView) v.findViewById(R.id.lblPVC);
            poferta = (TextView) v.findViewById(R.id.lblPOferta);
            // hora = (TextView) v.findViewById(R.id.lblHora);
        }
    }

    public AdapterValores(Context context) {
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
                .inflate(R.layout.row8, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String consulta;

        String estado;
      //  String fechainicio;
        String marca;
        String categoria;
        String tipo;
        String productos;
        String pcont;
        String pcuotas;
        String cuotas;
        String ventrada;
        String fpago;
        //String promo;
        String fecha;
        String pvp;
        String pvc;
        String poferta;
        //String hora;


        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if (consulta.equals("1")) {
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }


        marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.FECHA));
        categoria= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.HORA));
        tipo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.BRAND));
        productos = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.POS_NAME));
        pcont = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.SKU_CODE));
        pcuotas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.CODIFICA));
        cuotas = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.AUSENCIA));
        ventrada = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.RESPONSABLE));
        fpago = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.RAZONES));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.USUARIO));
        pvp = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.PVP));
        pvc = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.PVC));
        poferta = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.POFERTA));
        //hora = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.PHARMA_ID));



        viewHolder.estado.setText(estado);
        viewHolder.marca.setText(marca);
        viewHolder.categoria.setText(categoria);
        viewHolder.tipo.setText(tipo);
        viewHolder.productos.setText(productos);
        viewHolder.pcont.setText(pcont);
        viewHolder.pcuotas.setText(pcuotas);
        viewHolder.cuotas.setText(cuotas);
        viewHolder.ventrada.setText(ventrada);
        viewHolder.fpago.setText(fpago);
        viewHolder.fecha.setText(fecha);
        viewHolder.pvp.setText(pvp);
        viewHolder.pvc.setText(pvc);
        viewHolder.poferta.setText(poferta);
        //viewHolder.hora.setText(hora);

    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
