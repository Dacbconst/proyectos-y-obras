package com.luckyecuador.app.PintucoAPP;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.ui.puntos.CalendarFragment;
import com.luckyecuador.app.PintucoAPP.ui.puntos.MapsFragment;
import com.luckyecuador.app.PintucoAPP.ui.puntos.PdvsFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private int tabsNumber;
    FloatingActionButton fab;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, FloatingActionButton fab) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.fab = fab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                String modulo = Constantes.MODULO_PUNTOS_PRINCIPAL;
                return new PdvsFragment(modulo);
            case 1:
                return new CalendarFragment();
            case 2:
                return new MapsFragment(fab);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
