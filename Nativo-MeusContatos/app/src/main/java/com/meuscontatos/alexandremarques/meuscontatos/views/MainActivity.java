package com.meuscontatos.alexandremarques.meuscontatos.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.meuscontatos.alexandremarques.meuscontatos.R;
import com.meuscontatos.alexandremarques.meuscontatos.adapter.ContatosAdapter;
import com.meuscontatos.alexandremarques.meuscontatos.constants.Constants;
import com.meuscontatos.alexandremarques.meuscontatos.dao.BancoDAO;
import com.meuscontatos.alexandremarques.meuscontatos.dao.ContatoDAO;
import com.meuscontatos.alexandremarques.meuscontatos.interfaces.IRecycleView;
import com.meuscontatos.alexandremarques.meuscontatos.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IRecycleView {

    public Contato contato;
    public ContatoDAO dao;
    private ContatosAdapter mAdapter;

    private List<Contato> contatoList = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new ContatoDAO(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lista de Contatos");
        }

        /*for (int i = 11; i < 15; i++) {

            contato = new Contato(i, "Alexandre-" + i, "alexandreacm.marques@gmail.com", "51985080632");
            long inserir = dao.InserirContato(contato);
            Log.i(Constants.TAG, "Contato: " + inserir);

        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        contatoList = dao.BuscaContatos();
        mAdapter = new ContatosAdapter(contatoList, this, this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContatosAdapter(contatoList, this, this));
    }

    @Override
    public void IRecycleView_OnItemClickListener(Contato modelContato) {
        //Toast.makeText(this, "Email: " + modelContato.getEmail(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ContatoActivity.class);
        intent.putExtra("Contato", modelContato);
        startActivity(intent);
    }
}
