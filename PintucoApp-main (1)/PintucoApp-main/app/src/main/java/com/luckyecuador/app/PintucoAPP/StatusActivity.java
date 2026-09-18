package com.luckyecuador.app.PintucoAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StatusActivity extends AppCompatActivity {

    private String status_url = "";
    WebView wvMedidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        wvMedidor = (WebView) findViewById(R.id.wvPremiosPDF);
        wvMedidor.getSettings().setJavaScriptEnabled(true);
        wvMedidor.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        status_url = "https://webecuador.azurewebsites.net/App/CtaPintuco/MedidorGestion/index.php";
        wvMedidor.loadUrl(status_url);
    }
}