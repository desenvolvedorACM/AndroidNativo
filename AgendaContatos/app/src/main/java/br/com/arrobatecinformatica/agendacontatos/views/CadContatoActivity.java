package br.com.arrobatecinformatica.agendacontatos.views;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import java.text.DateFormat;
import java.util.Calendar;

import br.com.arrobatecinformatica.agendacontatos.app.ViewHelper;
import br.com.arrobatecinformatica.agendacontatos.R;
import br.com.arrobatecinformatica.agendacontatos.database.DataBase;
import br.com.arrobatecinformatica.agendacontatos.dominio.RepositorioContato;
import br.com.arrobatecinformatica.agendacontatos.model.Contato;
import br.com.arrobatecinformatica.agendacontatos.util.Alertas;
import br.com.arrobatecinformatica.agendacontatos.util.FormataData;

public class CadContatoActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    private ArrayAdapter<String> adpTipoEmail;
    private ArrayAdapter<String> adpTipoTelefone;
    private ArrayAdapter<String> adpTipoEndereco;
    private ArrayAdapter<String> adpTipoDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repContato;
    private Contato contato;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_contato);


        this.mViewHolder.edit_nome = (EditText) findViewById(R.id.edit_nome);
        this.mViewHolder.edit_telefone = (EditText) findViewById(R.id.edit_telefone);
        this.mViewHolder.spn_tipo_telefone = (Spinner) findViewById(R.id.spn_tipo_telefone);
        this.mViewHolder.edit_email = (EditText) findViewById(R.id.edit_email);
        this.mViewHolder.spn_tipo_email = (Spinner) findViewById(R.id.spn_tipo_email);
        this.mViewHolder.edit_endereco = (EditText) findViewById(R.id.edit_endereco);
        this.mViewHolder.spn_tipo_endereco = (Spinner) findViewById(R.id.spn_tipo_endereco);
        this.mViewHolder.edit_datasespeciais = (EditText) findViewById(R.id.edit_datasespeciais);
        this.mViewHolder.spn_datas_especiais = (Spinner) findViewById(R.id.spn_datas_especiais);
        this.mViewHolder.edit_grupo = (EditText) findViewById(R.id.edit_grupo);

        this.mViewHolder.button_salvar = (Button) findViewById(R.id.button_salvar);
        this.mViewHolder.button_salvar.setOnClickListener(this);


        //this.mViewHolder.edit_datasespeciais.setOnClickListener(this);
        this.mViewHolder.edit_datasespeciais.setOnClickListener(new ExibeDataLitener());
        this.mViewHolder.edit_datasespeciais.setOnFocusChangeListener(new ExibeDataLitener());

        //impede digite no campo data.
        this.mViewHolder.edit_datasespeciais.setKeyListener(null);

        //****************************************************
        //* Arrays.
        //****************************************************
        String[] arrayMails = new String[]{"Casa", "trabalho", "Outros"};
        String[] arrayTelefone = new String[]{"Celular", "Casa", "trabalho", "Outros"};
        String[] arrayEndereco = new String[]{"Casa", "trabalho", "Outros"};
        String[] arrayDatasEspecial = new String[]{"Aniversario", "Casamento", "Outros"};


        this.adpTipoEmail = ViewHelper.createArrayAdapter(this, this.mViewHolder.spn_tipo_email, arrayMails);
        this.adpTipoTelefone = ViewHelper.createArrayAdapter(this, this.mViewHolder.spn_tipo_telefone, arrayTelefone);
        this.adpTipoEndereco = ViewHelper.createArrayAdapter(this, this.mViewHolder.spn_tipo_endereco, arrayEndereco);
        this.adpTipoDatasEspeciais = ViewHelper.createArrayAdapter(this, this.mViewHolder.spn_datas_especiais, arrayDatasEspecial);


        /*this.adpTipoEmail = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayMails);
        this.adpTipoEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.adpTipoTelefone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTelefone);
        this.adpTipoTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.adpTipoEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayEndereco);
        this.adpTipoEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.adpTipoDatasEspeciais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayDatasEspecial);
        this.adpTipoDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        this.mViewHolder.spn_tipo_email.setAdapter(adpTipoEmail);
        this.mViewHolder.spn_tipo_telefone.setAdapter(adpTipoTelefone);
        this.mViewHolder.spn_tipo_endereco.setAdapter(adpTipoEndereco);
        this.mViewHolder.spn_datas_especiais.setAdapter(adpTipoDatasEspeciais);*/

        //Contato valor = (Contato) getIntent().getExtras().getSerializable(getString(R.string.contato));
        Bundle extra = getIntent().getExtras();

        if (extra != null && extra.containsKey(getString(R.string.contato))) {

            contato = (Contato) extra.getSerializable(getString(R.string.contato));
            preecheDados();

        } else {
            contato = new Contato();
        }

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repContato = new RepositorioContato(conn);

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

    private void preecheDados() {

        this.mViewHolder.edit_nome.setText(contato.getNome());
        this.mViewHolder.edit_telefone.setText(contato.getTelefone());
        this.mViewHolder.spn_tipo_telefone.setSelection(Integer.parseInt(contato.getTipoTelefone()));
        this.mViewHolder.edit_email.setText(contato.getEmail());
        this.mViewHolder.spn_tipo_email.setSelection(Integer.parseInt(contato.getTipoEmail()));
        this.mViewHolder.edit_endereco.setText(contato.getEndereco());
        this.mViewHolder.spn_tipo_endereco.setSelection(Integer.parseInt(contato.getTipoEndereco()));

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(contato.getDatasEspeciais());

        this.mViewHolder.edit_datasespeciais.setText(dt);
        this.mViewHolder.spn_datas_especiais.setSelection(Integer.parseInt(contato.getTiposDatasEspeciais()));
        this.mViewHolder.edit_grupo.setText(contato.getGrupos());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadcontato, menu);

        if (contato.getId() != 0) {

            menu.getItem(1).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnu_salvar:
                GravarContatos();
                break;

            case R.id.mnu_excluir:
                excluirContato();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void excluirContato() {

        try {
            repContato.ExcluirContato(contato.getId());
            finish();

        } catch (Exception ex) {

            Alertas.AlertaErro(this, "ERRO excluirContato", "Erro ao criar Banco " + ex.getMessage());
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.button_salvar) {
            GravarContatos();

        }
    }


    private void exibeData() {

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new SelecionaDataLitener(), ano, mes, dia);
        dpd.show();
    }


    private class ExibeDataLitener implements View.OnClickListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus) {
                exibeData();
            }
        }
    }

    private class SelecionaDataLitener implements DatePickerDialog.OnDateSetListener {

        EditText edit_datasespeciais;

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            String dt = FormataData.DateToString(year, month, dayOfMonth);


            edit_datasespeciais = (EditText) findViewById(R.id.edit_datasespeciais);
            this.edit_datasespeciais.setText(dt);
            contato.setDatasEspeciais(FormataData.getDate(year, month, dayOfMonth));
        }
    }


    private void GravarContatos() {

        try {


            contato.setNome(this.mViewHolder.edit_nome.getText().toString());
            contato.setTelefone(this.mViewHolder.edit_telefone.getText().toString());
            contato.setEmail(this.mViewHolder.edit_email.getText().toString());
            contato.setEndereco(this.mViewHolder.edit_endereco.getText().toString());

            //contato.setDatasEspeciais(new Date());
            contato.setEndereco(this.mViewHolder.edit_endereco.getText().toString());

            contato.setTiposDatasEspeciais(String.valueOf(this.mViewHolder.spn_datas_especiais.getSelectedItemPosition()));
            contato.setTipoTelefone(String.valueOf(this.mViewHolder.spn_tipo_telefone.getSelectedItemPosition()));
            contato.setTipoEmail(String.valueOf(this.mViewHolder.spn_tipo_email.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(this.mViewHolder.spn_tipo_endereco.getSelectedItemPosition()));


            if (contato.getId() == 0) {

                if (!contato.getNome().isEmpty()) {

                    long retorno = repContato.InsereContatos(contato);

                    if (retorno > 0) {
                        toast = Toast.makeText(this, "Dados gravados com sucesso", Toast.LENGTH_LONG);
                        toast.show();

                        finish();

                    } else {

                        Alertas.AlertaErro(this, "ERRO GRAVAR", "Ero ao gravar");
                    }


                } else {

                    toast = Toast.makeText(this, "Campo nome obrigatório!", Toast.LENGTH_LONG);
                    toast.show();
                }

            } else {

                repContato.AlterarContatos(contato);
                toast = Toast.makeText(this, "Dados alterados com sucesso", Toast.LENGTH_LONG);
                toast.show();

                finish();

            }

        } catch (Exception ex) {

            Alertas.AlertaErro(this, "ERRO SQL", "Erro ao criar Banco " + ex.getMessage());
        }

    }

    private static class ViewHolder {

        EditText edit_nome;
        EditText edit_telefone;
        Spinner spn_tipo_telefone;
        EditText edit_email;
        Spinner spn_tipo_email;
        EditText edit_endereco;
        Spinner spn_tipo_endereco;
        EditText edit_datasespeciais;
        Spinner spn_datas_especiais;
        EditText edit_grupo;
        Button button_salvar;

    }
}
