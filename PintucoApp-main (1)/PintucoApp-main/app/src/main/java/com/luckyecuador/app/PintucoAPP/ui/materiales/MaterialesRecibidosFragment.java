package com.luckyecuador.app.PintucoAPP.ui.materiales;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyecuador.app.PintucoAPP.R;


public class MaterialesRecibidosFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_materiales_recibidos, container, false);
    }
}