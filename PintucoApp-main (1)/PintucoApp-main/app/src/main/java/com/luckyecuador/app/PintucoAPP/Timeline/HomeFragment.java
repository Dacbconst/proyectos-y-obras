package com.luckyecuador.app.PintucoAPP.Timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.R;

import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    Date actual = Calendar.getInstance().getTime();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myInflatedView =  inflater.inflate(R.layout.fragment_home,container,false);
        TextView tvDia = (TextView) myInflatedView.findViewById(R.id.tvDia);
        tvDia.setText(actual.toString().substring(8,10));

        TextView tvNombreDia = (TextView) myInflatedView.findViewById(R.id.tvNombreDia);
        tvNombreDia.setText(actual.toString().substring(0,3));
        switch(tvNombreDia.getText().toString()) {
            case "Sun":
                tvNombreDia.setText(Constantes.SUN);
                break;
            case "Mon":
                tvNombreDia.setText(Constantes.MON);
                break;
            case "Tue":
                tvNombreDia.setText(Constantes.TUE);
                break;
            case "Wed":
                tvNombreDia.setText(Constantes.WED);
                break;
            case "Thu":
                tvNombreDia.setText(Constantes.THU);
                break;
            case "Fri":
                tvNombreDia.setText(Constantes.FRI);
                break;
            case "Sat":
                tvNombreDia.setText(Constantes.SAT);
                break;
            default:
                //
                break;
        }

        TextView tvMes = (TextView) myInflatedView.findViewById(R.id.tvMes);
        tvMes.setText(actual.toString().substring(4,7));
        switch(tvMes.getText().toString()) {
            case "Jan":
                tvMes.setText(Constantes.JAN);
                break;
            case "Feb":
                tvMes.setText(Constantes.FEB);
                break;
            case "Mar":
                tvMes.setText(Constantes.MAR);
                break;
            case "Apr":
                tvMes.setText(Constantes.APR);
                break;
            case "May":
                tvMes.setText(Constantes.MAY);
                break;
            case "Jun":
                tvMes.setText(Constantes.JUN);
                break;
            case "Jul":
                tvMes.setText(Constantes.JUL);
                break;
            case "Aug":
                tvMes.setText(Constantes.AUG);
                break;
            case "Sep":
                tvMes.setText(Constantes.SEP);
                break;
            case "Oct":
                tvMes.setText(Constantes.OCT);
                break;
            case "Nov":
                tvMes.setText(Constantes.NOV);
                break;
            case "Dec":
                tvMes.setText(Constantes.DEC);
                break;
            default:
                //
                break;
        }
        return myInflatedView;
    }
}