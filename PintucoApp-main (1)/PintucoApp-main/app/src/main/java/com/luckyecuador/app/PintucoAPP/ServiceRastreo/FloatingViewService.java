package com.luckyecuador.app.PintucoAPP.ServiceRastreo;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.luckyecuador.app.PintucoAPP.R;

public class FloatingViewService extends Service {
    private WindowManager windowManager;
    private View floatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // Infla el diseño de la vista flotante
        floatingView = LayoutInflater.from(this).inflate(R.layout.item_post, null);

        // Configura los parámetros de la vista flotante
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // Configura la posición de la vista flotante
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // Agrega la vista flotante al WindowManager
        windowManager.addView(floatingView, params);

        // Agrega un listener para abrir la actividad cuando se toque la vista flotante
        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad correspondiente aquí
                // Por ejemplo:
               /* Intent intent = new Intent(FloatingViewService.this, YourActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Remueve la vista flotante
                if (floatingView != null && windowManager != null) {
                    windowManager.removeView(floatingView);
                }
                // Detiene el servicio
                stopSelf();*/
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remueve la vista flotante si existe
        if (floatingView != null && windowManager != null) {
            windowManager.removeView(floatingView);
        }
    }

}
