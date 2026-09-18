package com.luckyecuador.app.PintucoAPP.Utils;

import android.location.Location;
import android.os.Build;

public class ControlesGps {

    public static boolean esUbicacionFalsa(Location location) {
        if (location == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (location.isFromMockProvider()) {
                return true;
            }
        }

        /*if (location.getAccuracy() > 100) {
            return true;
        }*/

        return false;
    }

    public static boolean esDispositivoRoot() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }
}
