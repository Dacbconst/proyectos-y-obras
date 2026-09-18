package com.luckyecuador.app.PintucoAPP.Adaptadores;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.luckyecuador.app.PintucoAPP.R;

/**
 * Mantiene fijo arriba de la lista el encabezado (dia/mes) del grupo de visitas que el
 * usuario esta viendo en ese momento, como en Google Calendar o WhatsApp con las fechas
 * de los mensajes. Se dibuja "a mano" sobre el canvas del RecyclerView (no es una fila
 * real de la lista): el RecyclerView ahora es quien scrollea de verdad (ver
 * fragment_proyectos_agenda.xml), asi que un decorator clasico de sticky header funciona aqui.
 */
public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    private View vistaEncabezado;
    private final Paint fondo = new Paint();

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (!(parent.getAdapter() instanceof AdapterAgenda) || parent.getChildCount() == 0) {
            return;
        }
        AdapterAgenda adapter = (AdapterAgenda) parent.getAdapter();

        View primeraVistaVisible = parent.getChildAt(0);
        int posicionVisible = parent.getChildAdapterPosition(primeraVistaVisible);
        if (posicionVisible == RecyclerView.NO_POSITION) {
            return;
        }

        int posicionEncabezado = adapter.posicionEncabezadoPara(posicionVisible);
        if (posicionEncabezado < 0) {
            return;
        }

        // Si el encabezado real ya esta totalmente visible en su lugar normal, no hace
        // falta dibujar uno encima (se veria duplicado justo en el borde).
        View vistaHeaderReal = buscarVistaPorPosicion(parent, posicionEncabezado);
        if (vistaHeaderReal != null && vistaHeaderReal.getTop() >= 0) {
            return;
        }

        if (vistaEncabezado == null) {
            vistaEncabezado = adapter.obtenerVistaEncabezadoReutilizable(parent);
        }
        adapter.vincularEncabezado(vistaEncabezado, posicionEncabezado);

        int paddingIzq = parent.getPaddingLeft();
        int anchoDisponible = parent.getWidth() - paddingIzq - parent.getPaddingRight();
        vistaEncabezado.measure(
                View.MeasureSpec.makeMeasureSpec(anchoDisponible, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int alturaEncabezado = vistaEncabezado.getMeasuredHeight();
        vistaEncabezado.layout(0, 0, anchoDisponible, alturaEncabezado);

        // Si el siguiente dia ya esta entrando por abajo, empujamos el encabezado actual
        // hacia arriba en vez de dejar que los dos se solapen.
        int traslacionY = 0;
        int posicionSiguiente = adapter.posicionSiguienteEncabezado(posicionEncabezado);
        if (posicionSiguiente != -1) {
            View vistaSiguiente = buscarVistaPorPosicion(parent, posicionSiguiente);
            if (vistaSiguiente != null && vistaSiguiente.getTop() < alturaEncabezado) {
                traslacionY = vistaSiguiente.getTop() - alturaEncabezado;
            }
        }

        fondo.setColor(ContextCompat.getColor(parent.getContext(), R.color.colorWhite));
        c.drawRect(0, 0, parent.getWidth(), alturaEncabezado + traslacionY, fondo);

        c.save();
        c.translate(paddingIzq, traslacionY);
        vistaEncabezado.draw(c);
        c.restore();
    }

    private View buscarVistaPorPosicion(RecyclerView parent, int posicionAdapter) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View hijo = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(hijo) == posicionAdapter) {
                return hijo;
            }
        }
        return null;
    }
}
