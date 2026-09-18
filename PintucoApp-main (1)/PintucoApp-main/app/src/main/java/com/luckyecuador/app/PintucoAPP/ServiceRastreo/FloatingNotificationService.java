package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.luckyecuador.app.PintucoAPP.R;

public class FloatingNotificationService extends Service {

    private WindowManager windowManager;
    private View floatingView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inflar la vista flotante
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_notification, null);

        // Configurar el diseño y el comportamiento de la vista flotante

        // Obtener el WindowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Configurar los parámetros de la vista flotante
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // Agregar la vista flotante al WindowManager
        windowManager.addView(floatingView, params);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remover la vista flotante al detener el servicio
        if (floatingView != null && windowManager != null) {
            windowManager.removeView(floatingView);
        }
    }

}
