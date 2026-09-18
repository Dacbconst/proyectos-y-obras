package com.luckyecuador.app.PintucoAPP;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PortafolioPDFActivity extends AppCompatActivity {

    private String pdf_url = "";
    WebView wvPremiosPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portafolio_pdf);

        wvPremiosPDF = (WebView) findViewById(R.id.wvPremiosPDF);
        wvPremiosPDF.getSettings().setJavaScriptEnabled(true);
        wvPremiosPDF.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        pdf_url = "https://luckyecuadorweb.blob.core.windows.net/pdf/Alicorp/PORTAFOLIO_PDF.pdf";
        //Carga url de .PDF en WebView  mediante Google Drive Viewer.
        wvPremiosPDF.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf_url);
    }
}