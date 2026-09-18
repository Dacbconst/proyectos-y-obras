package com.luckyecuador.app.PintucoAPP;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.ViewPagerAdapter;
import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.agenda.AgendaFragment;
import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.contacto.ContactoFragment;
import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.facturas.FacturasFragment;
import com.luckyecuador.app.PintucoAPP.ui.proyectosobras.proforma.ProformaFragment;

public class ProyectosObrasActivity extends AppCompatActivity {

    // Orden fijo de configurarPestanas() — Facturas es la última pestaña.
    private static final int TAB_FACTURAS = 3;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FacturasFragment facturasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_proyectos_obras);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tabLayout = findViewById(R.id.tablalayout);
        viewPager = findViewById(R.id.viewpager);

        configurarPestanas();
    }

    private void configurarPestanas() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactoFragment(), getString(R.string.tab_contacto));
        adapter.addFragment(new AgendaFragment(), getString(R.string.tab_agenda));
        adapter.addFragment(new ProformaFragment(), getString(R.string.tab_proforma));
        facturasFragment = new FacturasFragment();
        adapter.addFragment(facturasFragment, getString(R.string.tab_facturas));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Continuidad visual tras "Enviar factura" en Proforma: salta a la pestaña
     * Facturas y deja la tarjeta recién creada abierta/scrolleada (una sola vez,
     * ver FacturasFragment.solicitarAperturaDeFactura) — así el promotor no se
     * pregunta "¿a cuál le hice factura ahorita?". Después de este salto, la
     * navegación entre pestañas vuelve a ser completamente libre.
     */
    public void abrirFacturaEnFacturas(String claveFactura, boolean esAPlazos) {
        facturasFragment.solicitarAperturaDeFactura(claveFactura, esAPlazos);
        viewPager.setCurrentItem(TAB_FACTURAS);
    }
}
