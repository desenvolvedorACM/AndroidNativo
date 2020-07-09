package com.meuscontatos.alexandremarques.meuscontatos.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meuscontatos.alexandremarques.meuscontatos.R;
import com.meuscontatos.alexandremarques.meuscontatos.adapter.ContatosAdapter;
import com.meuscontatos.alexandremarques.meuscontatos.dao.ContatoDAO;
import com.meuscontatos.alexandremarques.meuscontatos.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
