package br.com.arrobatecinformatica.agendacontatos.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import android.database.sqlite.*;
import android.database.*;

import br.com.arrobatecinformatica.agendacontatos.database.DataBase;
import br.com.arrobatecinformatica.agendacontatos.R;
import br.com.arrobatecinformatica.agendacontatos.dominio.RepositorioContato;
import br.com.arrobatecinformatica.agendacontatos.model.Contato;
import br.com.arrobatecinformatica.agendacontatos.util.Alertas;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    private DataBase dataBase;
    private SQLiteDatabase conn;

    private RepositorioContato repContato;
    private ArrayAdapter<Contato> adpContatos;
    private FiltraDados filtraDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewHolder.edit_pesquisar = (EditText) findViewById(R.id.edit_pesquisar);
        this.mViewHolder.button_adicionar = (ImageButton) findViewById(R.id.button_adicionar);
        this.mViewHolder.lstContatos = (ListView) findViewById(R.id.lstContatos);

        this.mViewHolder.button_adicionar.setOnClickListener(this);
        this.mViewHolder.lstContatos.setOnItemClickListener(this);


        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            Alertas.AlertaAviso(this, "SQL", "Conexão criada com sucesso");

            Toast toast = Toast.makeText(this, "Conexão criada com sucesso", Toast.LENGTH_LONG);
            toast.show();

            repContato = new RepositorioContato(conn);
            //repContato.CriaContatos();

            adpContatos = repContato.buscaContatos(this);
            this.mViewHolder.lstContatos.setAdapter(adpContatos);

            filtraDados = new FiltraDados(adpContatos);
            this.mViewHolder.edit_pesquisar.addTextChangedListener(filtraDados);

        } catch (SQLException ex) {
            Alertas.AlertaErro(this, "ERRO SQL", "Erro ao criar Banco " + ex.getMessage());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.button_adicionar) {

            Intent intent = new Intent(this, CadContatoActivity.class);
            startActivityForResult(intent, 0);

            //startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        adpContatos = repContato.buscaContatos(this);
        filtraDados.setArrayAdapter(adpContatos);
        this.mViewHolder.lstContatos.setAdapter(adpContatos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contato contato = adpContatos.getItem(position);

        Intent intent = new Intent(this, CadContatoActivity.class);
        intent.putExtra(getString(R.string.contato), contato);

        startActivityForResult(intent, 0);
    }

    private static class ViewHolder {

        EditText edit_pesquisar;
        ImageButton button_adicionar;
        ListView lstContatos;
    }


    private class FiltraDados implements TextWatcher {

        private ArrayAdapter<Contato> arrayAdapter;

        public void setArrayAdapter(ArrayAdapter<Contato> arrayAdapter) {

            this.arrayAdapter = arrayAdapter;
        }

        private FiltraDados(ArrayAdapter<Contato> ArrayAdapter) {

            this.arrayAdapter = ArrayAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            arrayAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
