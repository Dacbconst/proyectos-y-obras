package com.luckyecuador.app.PintucoAPP;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.luckyecuador.app.PintucoAPP.Timeline.HVFragment;
import com.luckyecuador.app.PintucoAPP.Timeline.HomeFragment;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
//        getSupportActionBar().hide();
        setHomeFragment();
        setHVFragment();
    }

    private void setHVFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_detail, new HVFragment()).commit();
    }

    private void setHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }
}