package com.luckyecuador.app.PintucoAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.luckyecuador.app.PintucoAPP.Session.SessionManagement;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;

public class ClufActivity extends AppCompatActivity {

    public static Integer signed = 0;
    Button btnNoAcepto;
    Button btnAcepto;
    Button btnOk;

    WebView wvCluf;

    SharedPreferences sharedPref;
    SharedPreferences.Editor mEditor;

    private String cluf = "", menu = "0", user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluf);

        menu = getIntent().getStringExtra("menu");
        user = getIntent().getStringExtra("user");
        sharedPref = getApplicationContext().getSharedPreferences("Nombre de Local", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sharedPref.edit();

        checkSharedPreferences();

        if (cluf.equals("1") && menu.equals("0")) {
            moveToMain();
        }

        wvCluf = (WebView) findViewById(R.id.wbCluf);
//        wvCluf.getSettings().setJavaScriptEnabled(true);
//        wvCluf.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        wvCluf.loadUrl("https://webecuador.azurewebsites.net/App/XploraEcuador/ClufPintucoApp.html");
        //Ruta para el html: assets/html/xxxx.html
        wvCluf.loadUrl("file:///android_asset/html/clufapp.html");

        btnOk = (Button) findViewById(R.id.btnOk);
        btnNoAcepto = (Button) findViewById(R.id.btnNoAcepto);
        if (signed == 1) {
            btnNoAcepto.setVisibility(View.INVISIBLE);
        }
        btnAcepto = (Button) findViewById(R.id.btnAcepto);
        btnNoAcepto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                sessionManagement.removeSession();
                moveToLogin();
            }
        });

        btnAcepto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClufActivity.signed = 1;
                mEditor.putString(getString(R.string.cluf),"1");
                mEditor.commit();
                moveToMain();
            }
        });

        if (menu.equals("1")) {
            btnOk.setVisibility(View.VISIBLE);
            btnAcepto.setVisibility(View.GONE);
            btnNoAcepto.setVisibility(View.GONE);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMain();
            }
        });
    }

    private void checkSharedPreferences() {
        cluf = sharedPref.getString(getString(R.string.cluf),"0");
    }

    private void moveToMain() {
        new GuardarLog(ClufActivity.this).saveLog(user, "", "Ingreso al app");
        Intent intent = new Intent(ClufActivity.this, PuntosListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void moveToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}