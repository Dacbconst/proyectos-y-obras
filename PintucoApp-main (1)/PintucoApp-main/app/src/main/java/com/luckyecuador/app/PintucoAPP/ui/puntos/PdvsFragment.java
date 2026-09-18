package com.luckyecuador.app.PintucoAPP.ui.puntos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luckyecuador.app.PintucoAPP.Adaptadores.ListViewAdapter;
import com.luckyecuador.app.PintucoAPP.Adaptadores.PaginationScrollListener;
import com.luckyecuador.app.PintucoAPP.Adaptadores.PostRecyclerAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.Tabla;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.ImplementacionActivity;
import com.luckyecuador.app.PintucoAPP.PuntosListActivity;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.RelevoTardioActivity;
import com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PdvsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdvsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String modulo;
    //INICIO UTILIDADES
    String ACTION_FILTER = "com.luckyecuador.app.PintucoAPP";

    ListViewAdapter dataAdapter;

    TableLayout layout_tabla;

    private ImageButton btnNuevoLocal;
    private EditText txtBuscador;
    private EditText txtCodigo;
    private EditText txtNombre;

    private String user, idpdv, fecha, hora, codigo, tipo, puntoventa, formato, direccion, cliente, zona, format, nom_comercial, canal;

    private SharedPreferences sharedPref;
    //BASE SQLITE
    DatabaseHelper handler;

    final String[] lblLatitud = {"Latitud"};
    final String[] lblLongitud = {"Longitud"};

    Tabla tabla;

    private static final String TAG = "PdvsActivity";
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    int itemCount = 0;
    //FIN UTILIDADES

    public PdvsFragment() {}

    public PdvsFragment(String modulo) {
        this.modulo = modulo;
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
    public static PdvsFragment newInstance(String param1, String param2) {
        PdvsFragment fragment = new PdvsFragment(Constantes.NODATA);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PDV FRAMENTS","FUNCIONAAAA");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pdvs, container, false);
        Init();

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);

        LoadData();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mRecyclerView);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        btnNuevoLocal = (ImageButton) rootView.findViewById(R.id.ibNuevoLocal);

        txtBuscador = (EditText) rootView.findViewById(R.id.txtBuscador);
        txtCodigo = (EditText) rootView.findViewById(R.id.txt_codigo);
        txtNombre = (EditText) rootView.findViewById(R.id.txt_nombre);

        Context context = this.getContext();
        sharedPref = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        btnNuevoLocal.setOnClickListener(this);

        //Verificar los permisos
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(new Intent(getActivity(), LocationService.class));
        } else {
            getActivity().startService(new Intent(getActivity(), LocationService.class));
        }

        recyclerPDV(rootView);

        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                mAdapter.clear();

//                final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
//                final String descripcion_codigo = txtCodigo.getText().toString();
//                final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
//                final String descripcion_nombre = txtNombre.getText().toString();
//
//                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);

                final String text = txtBuscador.getText().toString();
                preparedListItem2(text);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                mAdapter.clear();

//                final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
//                final String descripcion_codigo = txtCodigo.getText().toString();
//                final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
//                final String descripcion_nombre = txtNombre.getText().toString();
//
//                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);

                final String text = txtBuscador.getText().toString();
                preparedListItem2(text);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                mAdapter.clear();

//                final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
//                final String descripcion_codigo = txtCodigo.getText().toString();
//                final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
//                final String descripcion_nombre = txtNombre.getText().toString();
//
//                preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);

                final String text = txtBuscador.getText().toString();
                preparedListItem2(text);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        return rootView;
    }

    private void recyclerPDV(ViewGroup rootView) {
//        final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
//        final String descripcion_codigo = txtCodigo.getText().toString();
//        final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
//        final String descripcion_nombre = txtNombre.getText().toString();
        final String text = txtBuscador.getText().toString();

//        ButterKnife.bind(this, rootView);

        swipeRefresh.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostRecyclerAdapter(getContext(),new ArrayList<BasePharmaValue>());
        mRecyclerView.setAdapter(mAdapter);
//        preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
        preparedListItem2(text);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem2(text);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

//    private void preparedListItem(String por_codigo, String descripcion_codigo, String por_nombre, String descripcion_nombre) {
//        final ArrayList<BasePharmaValue> pdv = handler.consultarBasePharmaValue(user, por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
//
//        final int size_list = pdv.size();
//        final int num_items = 50;
//        final int num_pages = size_list/num_items;
//
//        if (num_pages == 0) {
//            totalPage = 1;
//        } else {
//            totalPage = num_pages + 1;
//        }
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<BasePharmaValue> items = null;
//                try {
//                    if (size_list > 50) {
//                        Log.i("ENTRA > 50","ENTRA > 50");
//
//                        for (int i = 0; i < num_items; i++) {
//                            itemCount++;
//
//                            Log.i("ITEM_COUNT", itemCount + "");
//                            if (currentPage == PAGE_START) {
//                                items = new ArrayList<>(pdv.subList(0, itemCount + 1));
//                                Log.i("ITEMS_SIZE", items.size() + "");
//                            } else {
//                                if (itemCount >= num_pages) {
//                                    items = new ArrayList<>(pdv.subList(itemCount - num_items, itemCount + 1));
//                                } else {
//                                    items = new ArrayList<>(pdv.subList(itemCount - num_items, itemCount + 1));
//                                }
//                            }
//                        }
//                    }else{
//                        itemCount = 0;
//                        Log.i("ENTRA < 50","ENTRA < 50");
//                        for (int i = 0; i < size_list; i++) {
//                            itemCount++;
//
//                            Log.i("ITEM_COUNT", itemCount + "");
//                            Log.i("PDV_SIZE", pdv.size() + "");
//                            items = new ArrayList<>(pdv);
//                            mAdapter.removeLoading();
//                        }
//                    }
//
//                }catch (Exception e) {
//                    Log.i("Exception Items", e.getMessage());
//                }
//                if (currentPage != PAGE_START) {
//                    mAdapter.removeLoading();
//                }
//
//                try {
//                    Log.i("SIZE", size_list + "");
//                    if (size_list > 0) {
//                        mAdapter.addAll(items);
//                    } else {
//                        Toast.makeText(getContext(), "No hay PDVs a mostrar", Toast.LENGTH_LONG).show();
//                    }
//                }catch (Exception e) {
//                    Log.i("Exception Add", e.getMessage());
//                }
//
//                try {
//                    swipeRefresh.setRefreshing(false);
//                    if (currentPage < totalPage) {
//                        mAdapter.addLoading();
//                    } else {
//                        isLastPage = true;
//                        //mAdapter.removeLoading();
//                    }
//                }catch (Exception e) {
//                    Log.i("Exception Swipe", e.getMessage());
//                }
//                isLoading = false;
//            }
//        }, 1);
//    }

    private void preparedListItem2(String text) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(currentLocalTime);

        ArrayList<BasePharmaValue> pdv = new ArrayList<>();

        if (getActivity() instanceof PuntosListActivity) {
            pdv = handler.consultarBasePharmaValue2(user, text, fecha);
        } else if (getActivity() instanceof RelevoTardioActivity) {
            pdv = handler.consultarBasePharmaValueTardio(user, text);
        }


//        if (modulo.equals(Constantes.MODULO_PUNTOS_PRINCIPAL)) {
//            pdv = handler.consultarBasePharmaValue2(user, text, fecha);
//        } else if (modulo.equals(Constantes.MODULO_PUNTOS_TARDIO)) {
//            pdv = handler.consultarBasePharmaValueTardio(user, text);
//        }

        Log.i("PDV SIZE: ", pdv.size() + "");

        final int size_list = pdv.size();
        final int num_items = 50;
        final int num_pages = size_list/num_items;

        if (num_pages == 0) {
            totalPage = 1;
        } else {
            totalPage = num_pages + 1;
        }

        ArrayList<BasePharmaValue> finalPdv = pdv;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<BasePharmaValue> items = null;
                try {
                    if (size_list > 50) {
                        Log.i("ENTRA > 50","ENTRA > 50");

                        for (int i = 0; i < num_items; i++) {
                            itemCount++;

                            Log.i("ITEM_COUNT", itemCount + "");
                            if (currentPage == PAGE_START) {
                                items = new ArrayList<>(finalPdv.subList(0, itemCount + 1));
                                Log.i("ITEMS_SIZE", items.size() + "");
                            } else {
                                if (itemCount >= num_pages) {
                                    items = new ArrayList<>(finalPdv.subList(itemCount - num_items, itemCount + 1));
                                } else {
                                    items = new ArrayList<>(finalPdv.subList(itemCount - num_items, itemCount + 1));
                                }
                            }
                        }
                    }else{
                        itemCount = 0;
                        Log.i("ENTRA < 50","ENTRA < 50");
                        for (int i = 0; i < size_list; i++) {
                            itemCount++;

                            Log.i("ITEM_COUNT", itemCount + "");
                            Log.i("PDV_SIZE", finalPdv.size() + "");
                            items = new ArrayList<>(finalPdv);
                            mAdapter.removeLoading();
                        }
                    }

                }catch (Exception e) {
                    Log.i("Exception Items", e.getMessage());
                }
                if (currentPage != PAGE_START) {
                    mAdapter.removeLoading();
                }

                try {
                    Log.i("SIZE", size_list + "");
                    if (size_list > 0) {
                        mAdapter.addAll(items);
                    } else {
                        Toast.makeText(getContext(), "No hay PDVs a mostrar", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {
                    Log.i("Exception Add", e.getMessage());
                }

                try {
                    swipeRefresh.setRefreshing(false);
                    if (currentPage < totalPage) {
                        mAdapter.addLoading();
                    } else {
                        isLastPage = true;
                        //mAdapter.removeLoading();
                    }
                }catch (Exception e) {
                    Log.i("Exception Swipe", e.getMessage());
                }
                isLoading = false;
            }
        }, 1);
    }

    @Override
    public void onRefresh() {
//        final String por_codigo = ContractPharmaValue.Columnas.POS_ID;
//        final String descripcion_codigo = txtCodigo.getText().toString();
//        final String por_nombre = ContractPharmaValue.Columnas.POS_NAME;
//        final String descripcion_nombre = txtNombre.getText().toString();

        final String text = txtBuscador.getText().toString();

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();

//        preparedListItem(por_codigo, descripcion_codigo, por_nombre, descripcion_nombre);
        preparedListItem2(text);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        Log.i("USUARIO",user);
    }

    @Override
    public void onClick(View view) {
        if (view == btnNuevoLocal) {
            //IMPORTante para nuevo pdv
            Intent intent = new Intent(getContext(), ImplementacionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void Init() {}
}