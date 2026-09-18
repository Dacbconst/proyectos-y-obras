package com.luckyecuador.app.PintucoAPP;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class NotificacionesActivity extends AppCompatActivity {

    private String pdf_url = "";
    WebView wvPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        wvPDF = (WebView) findViewById(R.id.wvPDF);
        wvPDF.getSettings().setJavaScriptEnabled(true);
        wvPDF.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
       // pdf_url = "https://luckyecuadorweb.blob.core.windows.net/manuales/PintucoApp/CAPACITACI%C3%93N%20MARZO%20-%20ESTRATEGIA%20DE%20PRECIOS.pdf";
        //Carga url de .PDF en WebView  mediante Google Drive Viewer.
       wvPDF.loadUrl("https://drive.google.com/embeddedfolderview?id=1yGIcRsFKfqK844_5L4NME3tOFB16jiR0#list");
     //   wvPDF.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf_url);
    }
}