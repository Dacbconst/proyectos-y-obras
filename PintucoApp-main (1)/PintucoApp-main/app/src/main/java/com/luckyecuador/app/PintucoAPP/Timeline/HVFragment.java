package com.luckyecuador.app.PintucoAPP.Timeline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.Clase.LogUser;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HVFragment extends Fragment {

    DatabaseHelper handler;
    RecyclerView RvHv;
    HVAdapter adapter;
    List<LogUser> items;
    private String operator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RvHv = view.findViewById(R.id.recyclerview_hv);
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        LoadData();

        String fecha = getFecha();
        // Crear lista de items
        items = handler.getLog(operator, fecha);
        adapter = new HVAdapter(items);

        RvHv.setLayoutManager(new LinearLayoutManager(getContext()));
        RvHv.setAdapter(adapter);
    }

    public HVFragment() {
        // Required empty public constructor
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        operator = sharedPreferences.getString(Constantes.USER, Constantes.NODATA).toUpperCase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hv, container, false);
    }

    public String getFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        return fechaser;
    }
}