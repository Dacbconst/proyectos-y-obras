package com.luckyecuador.app.PintucoAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StatusGeneral extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_general);

        WebView wvMedidor = findViewById(R.id.wvStatusGeneral);
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
        String status_url = "https://app.powerbi.com/view?r=eyJrIjoiMGE0MDVkNDYtYjkyMC00MTJmLWI1MmUtM2U2NmRjODg3MmM0IiwidCI6IjY3Mzk4ZjkyLWJiNDYtNDQ5MS04OGYyLWU4ZWRkNTg0MDFiZCIsImMiOjR9";
        wvMedidor.loadUrl(status_url);

    }
}