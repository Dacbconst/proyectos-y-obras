package com.luckyecuador.app.PintucoAPP.ui.logistico;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.ui.logistico.alerta.LogisticoAlertaFragment;
import com.luckyecuador.app.PintucoAPP.ui.logistico.preforma.LogisticoPreformaFragment;
import com.luckyecuador.app.PintucoAPP.ui.logistico.relevo.LogisticoRelevoFragment;

import org.json.JSONException;
import org.json.JSONObject;
public class LogisticoFragment extends Fragment {
    private String punto_venta;
    private String user;
    private String codigo_pdv;
    private String tipo = "RELEVO";
    private boolean tieneRegistrosPrevios = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private boolean isListenerConfigured = false;

    private long ultimaAlertaMostrada = 0;
    private static final long TIEMPO_ENTRE_ALERTAS = 5000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main_logistico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tablalayout);
        viewPager = view.findViewById(R.id.viewpager);

        LoadData();

        TextView lPDV = view.findViewById(R.id.lPDV);
        lPDV.setText(punto_venta);

        configurarPestanasInicial();
        verificarRegistrosPrevios();
    }

    private void configurarPestanasInicial() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LogisticoRelevoFragment(), "RELEVO");
        adapter.addFragment(new LogisticoAlertaFragment(), "HISTÓRICO");
        adapter.addFragment(new LogisticoPreformaFragment(), "PREFORMA");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        configurarListenerPestanas();
    }

    private void configurarListenerPestanas() {
        if (isListenerConfigured) {
            return;
        }

        isListenerConfigured = true;

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!tieneRegistrosPrevios && tab.getPosition() == 1) {
//                    Toast.makeText(getContext(),
//                            "No hay registros relevados.",
//                            Toast.LENGTH_LONG).show();
                    mostrarAlertaConTiempo();
                    tabLayout.post(() -> {
                        TabLayout.Tab relevoTab = tabLayout.getTabAt(0);
                        if (relevoTab != null) {
                            relevoTab.select();
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (!tieneRegistrosPrevios && tab.getPosition() == 1) {
//                    Toast.makeText(getContext(),
//                            "No hay registros relevados.",
//                            Toast.LENGTH_LONG).show();
                    mostrarAlertaConTiempo();

                    tabLayout.post(() -> {
                        TabLayout.Tab relevoTab = tabLayout.getTabAt(0);
                        if (relevoTab != null) {
                            relevoTab.select();
                        }
                    });
                }
            }
        };

        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    private void mostrarAlertaConTiempo() {
        long ahora = System.currentTimeMillis();

        if (ahora - ultimaAlertaMostrada < TIEMPO_ENTRE_ALERTAS) {
            return;
        }

        ultimaAlertaMostrada = ahora;
        Toast.makeText(getContext(), "No hay registros relevados.", Toast.LENGTH_LONG).show();
    }

    private void verificarRegistrosPrevios() {
        if (!VerificarNet.hayConexion(getContext())) {
            /*Toast.makeText(getContext(), "Sin conexión a internet. No se puede verificar registros previos.", Toast.LENGTH_LONG).show();*/
            tieneRegistrosPrevios = true;
            return;
        }

        String url = "https://webecuador.azurewebsites.net/App/AppPintuco/Web/get_logistico_relevo.php";

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("usuario", user);
            jsonBody.put("codigo_pdv", codigo_pdv);
            jsonBody.put("tipo_logistico", tipo);
            jsonBody.put("solo_fecha", "1");

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String estado = jsonResponse.optString("estado", "0");

                            // estado "1" significa que hay fecha (hay registros previos)
                            // estado "2" significa que no hay registros previos
                            tieneRegistrosPrevios = "1".equals(estado);

                            if (!tieneRegistrosPrevios) {
                                /*Toast.makeText(getContext(),
                                        "La pestaña ALERTA estará bloqueada hasta que realice un relevo.",
                                        Toast.LENGTH_LONG).show();*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tieneRegistrosPrevios = true;
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        tieneRegistrosPrevios = true;
                        Toast.makeText(getContext(), "Error al verificar registros previos", Toast.LENGTH_SHORT).show();
                    }) {

                @Override
                public byte[] getBody() {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

        } catch (Exception e) {
            e.printStackTrace();
            tieneRegistrosPrevios = true;
        }
    }

    private void LoadData() {
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tabSelectedListener != null && tabLayout != null) {
            tabLayout.removeOnTabSelectedListener(tabSelectedListener);
        }
        isListenerConfigured = false;
    }
}