package br.com.arrobatecinformatica.agendacontatos.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.com.arrobatecinformatica.agendacontatos.R;

/**
 * Created by Alexandre on 25/04/2017.
 */

public class Splash_Activity extends AppCompatActivity implements Runnable {

    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(this, 4000);
    }

    @Override
    public void run() {

        //Intent intent = new Intent(this, MainActivity.class);
        startActivity(new Intent(this, MainActivity.class));
    }
}
