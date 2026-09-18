package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class ProximityReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
//            Toast.makeText(context, "LocationReminderReceiver entering", Toast.LENGTH_SHORT).show();
            Log.i("ReceptorProximidad", "entering");
        } else {
//            Toast.makeText(context, "LocationReminderReceiver exiting", Toast.LENGTH_SHORT).show();
            Log.i("ReceptorProximidad", "exiting");
        }
    }
}