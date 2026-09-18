package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;

/**
 * Created by Lucky Ecuador on 21/02/2017.
 */

public class AdapterEvidencias extends RecyclerView.Adapter<AdapterEvidencias.ViewHolder> {
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
        private TextView sub;
        private TextView brand;
        private TextView sku;
        private TextView regular;
        private TextView promocion;
        private TextView ofer;

        public ViewHolder(View v) {
            super(v);
            estado = (TextView) v.findViewById(R.id.lblEstado);
            fecha = (TextView) v.findViewById(R.id.lblfechainicial);
            hora = (TextView) v.findViewById(R.id.lblMara);
            codigo = (TextView) v.findViewById(R.id.lblPDV);
            usuario = (TextView) v.findViewById(R.id.lblTipo);
            categoria = (TextView) v.findViewById(R.id.lblCategoria);
            brand = (TextView) v.findViewById(R.id.lblCantidad);
            sku = (TextView) v.findViewById(R.id.lblPcont);
            regular = (TextView) v.findViewById(R.id.lblFecha);
            promocion = (TextView) v.findViewById(R.id.lblHora);
            sub = (TextView) v.findViewById(R.id.lblSub);
            ofer = (TextView) v.findViewById(R.id.lblOfert);
        }
    }

    public AdapterEvidencias(Context context) {
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
                .inflate(R.layout.status_evidencias, viewGroup, false);
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
        String categoria;
        String brand;
        String sku;
        String regular;
        String promocion;
        String sub;
        String ofer;
        String pos_name;

        consulta = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION));
        if(consulta.equals("1")){
            estado = "No Enviado";
        }else{
            estado = "Enviado";
        }



        codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.CODIGO));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.FECHA));
        hora= cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.HORA));
        usuario = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.USUARIO));
        categoria = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.CATEGORIA));
        sub = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.COMENTARIO));
//        brand = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.BRAND));
//        sku = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.SKU_CODE));
//        regular = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.PREGULAR));
//        promocion = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.PPROMOCION));
//        ofer = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertEvidencias.Columnas.POFERTA));

        viewHolder.estado.setText(estado);
        viewHolder.fecha.setText(fecha);
        viewHolder.categoria.setText(categoria);
        viewHolder.codigo.setText(handler.getPosNamePdv(codigo));
        viewHolder.hora.setText(hora);
        viewHolder.usuario.setText(usuario);
        viewHolder.sub.setText(sub);
//        viewHolder.brand.setText(brand);
//        viewHolder.sku.setText(sku);
//        viewHolder.regular.setText(regular);
//        viewHolder.promocion.setText(promocion);
//        viewHolder.ofer.setText(ofer);

    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}