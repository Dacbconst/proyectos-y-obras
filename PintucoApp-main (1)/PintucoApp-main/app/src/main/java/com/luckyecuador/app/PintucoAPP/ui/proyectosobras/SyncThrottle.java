package com.luckyecuador.app.PintucoAPP.ui.proyectosobras;

/**
 * Throttle compartido entre AgendaFragment y ProformaFragment para el sync
 * real que se dispara al entrar a cada tab (además de la recarga local que ya
 * hacía onResume). Ambos encadenan el mismo primer paso (bajar_contactos), así
 * que sin este límite alternar tabs rápido dispararía el mismo sync dos veces
 * seguidas — y con muchos agendamientos, una espera de red en cada cambio de
 * pestaña que el usuario nunca pidió.
 */
public class SyncThrottle {

    private static final long INTERVALO_MINIMO_MS = 15000;
    private static volatile long ultimoIntento = 0L;

    private SyncThrottle() {
    }

    public static synchronized boolean puedeSincronizar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoIntento < INTERVALO_MINIMO_MS) {
            return false;
        }
        ultimoIntento = ahora;
        return true;
    }
}
