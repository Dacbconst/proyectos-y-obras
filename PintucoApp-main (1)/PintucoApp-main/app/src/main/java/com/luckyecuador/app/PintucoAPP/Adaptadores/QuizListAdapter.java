package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.Base_tests;
import com.luckyecuador.app.PintucoAPP.R;


import java.util.ArrayList;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder>
     implements View.OnClickListener {


    ArrayList<Base_tests> quizList;
    private View.OnClickListener listener;

    public QuizListAdapter(ArrayList<Base_tests> quizList){
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test,null,false);
        view.setOnClickListener(this);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {

        String estado = quizList.get(position).getActive();

        if(estado.equals("NO"))
        {
            holder.viewImg.setImageResource(R.drawable.test_generico_inactivo);
            holder.viewTest.setTextColor(Color.parseColor("#D2D5D6"));
            holder.viewTest.setText(quizList.get(position).getTest());
            holder.viewF_inicio.setText("Fecha de Inicio : " + quizList.get(position).getF_inicio());
        /*    holder.viewF_limite.setText("Fecha Limite : " + quizList.get(position).getF_limite()); */

        } else {
            holder.viewTest.setText(quizList.get(position).getTest());
            holder.viewF_inicio.setText("Fecha de Inicio : " + quizList.get(position).getF_inicio());
            holder.viewF_limite.setText("Fecha Limite : " + quizList.get(position).getF_limite());
        }







    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public void setOnClickListenner(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }else{

        }

    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView viewTest , viewF_inicio , viewF_limite;
        ImageView viewImg;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            viewImg = itemView.findViewById(R.id.imgTest);
            viewTest = itemView.findViewById(R.id.tv_nombre_test);
            viewF_inicio = itemView.findViewById(R.id.tv_fecha_inicio);
            viewF_limite = itemView.findViewById(R.id.tv_fecha_limite);
        }
    }
}
