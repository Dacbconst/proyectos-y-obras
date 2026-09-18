package com.luckyecuador.app.PintucoAPP;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VisualizarCertificadoActivity extends AppCompatActivity {

    private WebView webView;
    private String id;
    private String nombre;
    private String ruta;
    ProgressBar progressBar;
    PDFView pdfView;
    Toolbar toolbar;
    private  String URL_SERVIDOR = "https://webecuador.azurewebsites.net/App/CtaSamsung/AppSamsungEvaluaciones/Site/data/materiales/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_certificado);

        setToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("test_id", "NoData");
            nombre = extras.getString("nombre", "NoData");
            ruta = extras.getString("ruta","NoData");


        }


        pdfView = findViewById(R.id.pdfview);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



            File file = new File(ruta);
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .load();

            progressBar.setVisibility(View.GONE);





/*
        File file = new File(Environment.getExternalStorageDirectory(),"archivo.pdf");

        pdfView.fromFile(file).load();
*/



/*
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl( "https://docs.google.com/gview?embedded=true&url="  + ruta_archivo);
*/




    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }



}
