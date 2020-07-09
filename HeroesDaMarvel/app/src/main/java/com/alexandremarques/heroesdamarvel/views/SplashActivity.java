package com.alexandremarques.heroesdamarvel.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alexandremarques.heroesdamarvel.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(this, 4000);
    }

    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
