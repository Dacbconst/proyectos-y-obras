package com.luckyecuador.app.PintucoAPP.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;

public class DeveloperOptions {

    public static boolean isDevelopmentSettingsEnabled(Context context) {
        final UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
        final boolean settingEnabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, Build.TYPE.equals("eng") ? 1 : 0) != 0;
        final boolean hasRestriction = um.hasUserRestriction(UserManager.DISALLOW_DEBUGGING_FEATURES);
        final boolean isAdmin = um.isSystemUser();
        return isAdmin && !hasRestriction && settingEnabled;
    }

    public void modalDevOptions(Activity activity) {
      new AlertChangeTime(activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Opciones de desarrollador");
        builder.setMessage("Debe deshabilitar las opciones de desarrollador para continuar usando la aplicación");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.moveTaskToBack(true);
            }
        });

        AlertDialog ad = builder.create();
        boolean isEnable = new DeveloperOptions().isDevelopmentSettingsEnabled(activity.getApplicationContext());
        if (isEnable) {
            Log.i("DEVELOPER_OPTIONS", "ENABLE");
            ad.show();
        } else {
            Log.i("DEVELOPER_OPTIONS", "DISABLE");
            if (ad.isShowing()) {
                ad.dismiss();
            }
        }
    }
}