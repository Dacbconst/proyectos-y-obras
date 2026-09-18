package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationDismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "DISMISS_NOTIFICATION".equals(intent.getAction())) {
            // Si se recibe la acción de cerrar la notificación, detener la notificación en primer plano
            /*Intent serviceIntent = new Intent(context, AsistenciaService.class);
            context.stopService(serviceIntent);*/
        }
    }
}
