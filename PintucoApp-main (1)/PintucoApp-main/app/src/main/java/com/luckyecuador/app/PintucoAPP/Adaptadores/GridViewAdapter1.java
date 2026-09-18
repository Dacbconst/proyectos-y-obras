package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luckyecuador.app.PintucoAPP.Clase.Base_tipo_exh;
import com.luckyecuador.app.PintucoAPP.R;

import java.util.ArrayList;

public class GridViewAdapter1 extends ArrayAdapter<Base_tipo_exh> {

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    private int selectedPosition = -1;

    public GridViewAdapter1(@NonNull Context context, ArrayList<Base_tipo_exh> base_tipo_exh) {
        super(context, 0,base_tipo_exh);
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HolderView holderView;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item_list,parent,false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }else{
            holderView = (HolderView) convertView.getTag();
        }

        Base_tipo_exh model = getItem(position);
        holderView.tipo_exh.setText(model.getExhibicion());


        holderView.tipo_exh.setChecked(model.isSelected);
        holderView.tipo_exh.setTag(new Integer(position));


        holderView.tipo_exh.setChecked(position == selectedPosition);
        View finalConvertView = convertView;
        holderView.tipo_exh.setOnClickListener(v -> {
            if (position == selectedPosition) {
                holderView.tipo_exh.setChecked(false);
                selectedPosition = -1;
            }
            else {
                selectedPosition = position;

              //  Toast.makeText(getContext(), ""+model.getExhibicion(), Toast.LENGTH_SHORT).show();


                notifyDataSetChanged();

            }
        });





        holderView.btn_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(finalConvertView.getContext(), "Cargar foto", Toast.LENGTH_SHORT).show();
            }
        });




/*
        //for default check in first item
        if(position == 0 && model.isSelected && holderView.tipo_exh.isChecked())
        {
            lastChecked = holderView.tipo_exh;
            lastCheckedPos = 0;
        }


        holderView.tipo_exh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox)v;
                int clickedPos = ((Integer)cb.getTag()).intValue();

                Toast.makeText(getContext(),""+clickedPos,Toast.LENGTH_SHORT).show();


                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                        model.setSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                } else
                    lastChecked = null;

                model.setSelected(cb.isChecked());




            }
        });
*/


        return convertView;
    }

    private static class HolderView{
        private final CheckBox tipo_exh;
        private final ImageButton btn_viewer;

        public HolderView(View view){
            tipo_exh = view.findViewById(R.id.cB_tipo_exh);
            btn_viewer = view.findViewById(R.id.btn_viewer);
        }
    }
}
