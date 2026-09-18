package com.luckyecuador.app.PintucoAPP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

public class General extends AppCompatActivity {

    private String status_url = "";
    private WebView wvMedidor;
    private SwipeRefreshLayout refreshLayout;

    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, canal, pref_pdv;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        pref_pdv = sharedPreferences.getString(Constantes.PDV,Constantes.NODATA);
        Log.i("ENTRO AL STATUS", "SIII");
        LoadData();
        new DeveloperOptions().modalDevOptions(this);
        RequestPermissions requestPermissions = new RequestPermissions(getApplicationContext(), this);
        requestPermissions.showPermissionDialog();

        progressDialog = new ProgressDialog(General.this,R.style.MyAlertDialogStyle);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Tiempo estimado (1 min)");
        progressDialog.setCancelable(false);
        progressDialog.show();

        wvMedidor = (WebView) findViewById(R.id.wvStatus);


        wvMedidor.getSettings().setJavaScriptEnabled(true);
        wvMedidor.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(General.this, "cargado", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }
        });

        Log.i("INFO", codigo_pdv + " - " + punto_venta + " - " + user);

        String usuario = user.replace(" ","+");

        status_url = "https://webecuador.azurewebsites.net/App/CtaPintuco/MedidorGestion/general.php" +
                "?plataforma=FOODS"+
                "&usuario=" + user;
        Log.i("URL STATUS", status_url);
        wvMedidor.loadUrl(status_url);

    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
    }

}