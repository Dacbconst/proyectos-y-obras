package com.luckyecuador.app.PintucoAPP.ui.puntos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
//    TextView date_view;

    private String id_pdv, user, codigo_pdv, punto_venta, date, canal, format;

    DatabaseHelper handler;

    ListView listview;
    ArrayList<BasePharmaValue> listPdvs;
    ListViewAdapterCustomer dataAdapter;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PdvsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        LoadData();
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);

        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        calendar = (CalendarView) rootView.findViewById(R.id.calendar);
//        date_view = (TextView) rootView.findViewById(R.id.date_view);

        // Add Listener in calendar
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // In this Listener have one method
            // and in this method we will
            // get the value of DAYS, MONTH, YEARS
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                String dia = dayOfMonth + "";
                if (dayOfMonth < 10) {
                    dia = "0" + dayOfMonth;
                }

                String mes = (month + 1) + "";
                if (month+1 < 10) {
                    mes = "0" + (month + 1);
                }
                date = dia + "/" + mes + "/" + year;
                String fecha = year + "-" + mes + "-" + dia;
                // set this date in TextView for Display
//                date_view.setText(Date);

                alertDialog(user, fecha);
            }
        });

        return rootView;
    }

    public void alertDialog(String user, String fecha) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_visitas, null);
        builder.setView(LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_visitas, null));

        TextView tittle = (TextView) dialogView.findViewById(R.id.lblModelo);
        listview = (ListView) dialogView.findViewById(R.id.lvPdvs);
        tittle.setText(fecha);

        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_visitas_title,null,false);
        listview.addHeaderView(headerView,null,false);

        showListView(user, fecha);

        builder.setNeutralButton(R.string.cerrar, null);

        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void LoadData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
//        subcanal = sharedPreferences.getString(Constantes.CUSTOMER_OWNER,Constantes.NODATA);
    }

    public void showListView(String user, String fecha) {
        listPdvs = handler.consultarBasePharmaValueByDay(user, fecha);
        Log.i("LIST PDV", listPdvs.size() + "");
        dataAdapter = new ListViewAdapterCustomer(getContext(), listPdvs);
        if(!dataAdapter.isEmpty()) {
            listview.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onClick(View view) {
//        if (view == btnNuevoLocal) {}
    }

    public class ListViewAdapterCustomer extends ArrayAdapter<BasePharmaValue> {

        public ArrayList<BasePharmaValue> pdvs;
        public Context context;

        public ListViewAdapterCustomer(Context context, ArrayList<BasePharmaValue> pdvs){
            super(context, 0, pdvs);
            this.pdvs = pdvs;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return pdvs.size();
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class ViewHolder{
            TextView lblCodigo;
            TextView lblPosName;
            TextView lblEstado;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_row_visitas, parent, false);

                holder.lblCodigo = (TextView) convertView.findViewById(R.id.lblCodigo);
                holder.lblPosName = (TextView) convertView.findViewById(R.id.lblPosName);
                holder.lblEstado = (TextView) convertView.findViewById(R.id.lblEstado);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                if (pdvs.size() > 0) {
                    String codigo = pdvs.get(position).getPos_id();
                    holder.lblCodigo.setText(codigo);
                    holder.lblPosName.setText(pdvs.get(position).getPos_name());

                    ArrayList<String> estados = handler.getEstadoMarcacionPdv(codigo, user, date);
                    String estado = "";
                    if (estados.contains("ENTRADA") && estados.contains("SALIDA")) {
                        estado = "Ejecutado";
                        holder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else if (estados.contains("ENTRADA")) {
                        estado = "En ejecución";
                        holder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    } else {
                        estado = "Pendiente";
                        holder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                    holder.lblEstado.setText(estado);
                }
            }catch (Exception e){
                Log.i("ERROR", e.getMessage());
                Log.i("ERROR", e.getLocalizedMessage());
                Log.i("ERROR", String.valueOf(e.getCause()));
            }
            return convertView;
        }
    }
}