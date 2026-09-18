package com.luckyecuador.app.PintucoAPP.Timeline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.LogUser;
import com.luckyecuador.app.PintucoAPP.R;

import java.util.List;

public class HVAdapter extends RecyclerView.Adapter<HVAdapter.HVViewHolder> {

    List<LogUser> mdata;

    public HVAdapter(List<LogUser> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public HVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hv,parent,false);
        return new HVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HVViewHolder holder, int position) {

        holder.tvTitle.setText(mdata.get(position).getHora());
        holder.tvDescripcion.setText(mdata.get(position).getAccion());

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class HVViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvDescripcion;

        public HVViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
