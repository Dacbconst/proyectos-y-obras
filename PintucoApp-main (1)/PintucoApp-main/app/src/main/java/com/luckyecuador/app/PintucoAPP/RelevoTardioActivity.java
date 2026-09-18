package com.luckyecuador.app.PintucoAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.ui.puntos.PdvsFragment;

public class RelevoTardioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevo_tardio);

        String modulo = Constantes.MODULO_PUNTOS_TARDIO;


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.MODULO_ACTUAL,Constantes.MODULO_PUNTOS_TARDIO);
        editor.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new PdvsFragment(modulo)).commit();
    }


    @Override
    public void onBackPressed() {
        moveToMain();
    }


    private void moveToMain() {
        Intent intent = new Intent(RelevoTardioActivity.this, PuntosListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}