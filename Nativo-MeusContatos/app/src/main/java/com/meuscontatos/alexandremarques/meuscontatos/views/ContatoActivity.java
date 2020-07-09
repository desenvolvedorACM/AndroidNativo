package com.meuscontatos.alexandremarques.meuscontatos.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.meuscontatos.alexandremarques.meuscontatos.R;
import com.meuscontatos.alexandremarques.meuscontatos.dao.ContatoDAO;
import com.meuscontatos.alexandremarques.meuscontatos.model.Contato;

public class ContatoActivity extends AppCompatActivity {

    private EditText contato_edtNome, contato_edtEmail, contato_edtTelefone;
    private Button contato_btnSalvar;
    private LinearLayout activity_contato;
    private ProgressBar contato_progressBar;
    private String nome, email, telefone;

    private Contato contato;
    private ContatoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar Contato");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        findView();

        contato = (Contato) getIntent().getSerializableExtra("Contato");

        if (contato != null) {
            contato_edtNome.setText(contato.getNome());
            contato_edtEmail.setText(contato.getEmail());
            contato_edtTelefone.setText(contato.getTelefone());
        } else {
            contato = new Contato();
        }

        contato_btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pegarValores();

                habilitarProgress(View.VISIBLE, false);

                if (TextUtils.isEmpty(nome)) {
                    habilitarProgress(View.GONE, true);
                    //Toast.makeText(getApplicationContext(), R.string.digite_seu_nome, Toast.LENGTH_LONG).show();
                    Snackbar.make(activity_contato, R.string.digite_seu_nome, Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    habilitarProgress(View.GONE, true);
                    Snackbar.make(activity_contato, R.string.digite_seu_email, Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), R.string.digite_seu_email, Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(telefone)) {
                    habilitarProgress(View.GONE, true);
                    //Toast.makeText(getApplicationContext(), R.string.digite_seu_telefone, Toast.LENGTH_LONG).show();
                    Snackbar.make(activity_contato, R.string.digite_seu_telefone, Snackbar.LENGTH_LONG).show();
                    return;
                }

                //contato = new Contato();
                dao = new ContatoDAO(getApplicationContext());

                contato.setNome(nome);
                contato.setEmail(email);
                contato.setTelefone(telefone);

                if (contato.getId() > 0) {
                    editar();
                } else {
                    inserir();
                }

            }
        });
    }

    private void editar() {
        dao.EditarContato(contato);
        Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void inserir() {
        if (dao.InserirContato(contato) > 0) {
            Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.digite_seu_telefone, Toast.LENGTH_LONG).show();
            habilitarProgress(View.VISIBLE, false);
        }
    }

    private void habilitarProgress(int visible, boolean b) {
        contato_progressBar.setVisibility(visible);
        contato_btnSalvar.setClickable(b);
    }

    private void pegarValores() {
        nome = contato_edtNome.getText().toString();
        email = contato_edtEmail.getText().toString();
        telefone = contato_edtTelefone.getText().toString();
    }

    private void findView() {
        contato_edtNome = (EditText) findViewById(R.id.contato_edtNome);
        contato_edtEmail = (EditText) findViewById(R.id.contato_edtEmail);
        contato_edtTelefone = (EditText) findViewById(R.id.contato_edtTelefone);
        contato_btnSalvar = (Button) findViewById(R.id.contato_btnSalvar);
        contato_progressBar = (ProgressBar) findViewById(R.id.contato_progressBar);
        activity_contato = (LinearLayout) findViewById(R.id.activity_contato);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contato, menu);

        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.contato_ligar:
                Intent chamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contato.getTelefone().toString()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {

                    startActivity(chamada);

                }
                return true;
            case android.R.id.home:

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //onBackPressed();

                return true;
            case R.id.contato_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{contato.getEmail()});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, R.string.enviar_email + ""));
                return true;
            case R.id.contato_apagar:
                dao = new ContatoDAO(getApplicationContext());
                dao.DeletarContato(contato);

                Intent it = new Intent(this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
