package com.luckyecuador.app.PintucoAPP.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class WhatsAppMessage {

    public Context context;

    public WhatsAppMessage(Context context) {
        this.context = context;
    }

    public void enviarMensajeWhatsapp(String celular, String messagestr) {
        String phonestr = celular;
        if (!messagestr.isEmpty() && !phonestr.isEmpty()) {
            if (isWhatappInstalled()) {
                if (!phonestr.equals("N/A") || !phonestr.equals("NA") || !phonestr.contains("+")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+"&text="+messagestr));
                    context.startActivity(i);
                } else {
                    Toast.makeText(context,"Numero de telefono no puede ser: " + phonestr, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Por favor, verificar el número de teléfono o el mensaje, podrían estar vacios", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isWhatappInstalled() {
        PackageManager packageManager = context.getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }


}
