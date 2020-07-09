package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelSessionUsuario;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class PergunteMestreAceitePerguntaActivity extends AppCompatActivity {

    private SecurityPreferences preferences;
    private apiMessages Alerts;
    private Conectividade conectividade;
    private modelSessionUsuario sessionUser;
    private ProgressDialog dialog;
    private ViewHolder mViewHolder = new ViewHolder();


    private String Email;
    private String First_name;
    private String Image;
    private int Id_Professor;
    private int Id_Usuario;
    private String isProfessor;
    private int Type;

    //Dados Passar entre telas.
    private int ID_Pergunta;
    private String Pergunta;
    private String Materia;
    private String nome_Aluno_Pergunta;
    private String UrlImagemAluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunte_mestre_aceite_pergunta);

        preferences = new SecurityPreferences(PergunteMestreAceitePerguntaActivity.this);
        conectividade = new Conectividade(PergunteMestreAceitePerguntaActivity.this);
        Alerts = new apiMessages(PergunteMestreAceitePerguntaActivity.this);
        dialog = new ProgressDialog(PergunteMestreAceitePerguntaActivity.this);


        this.mViewHolder.text_Nome_Aluno = (TextView) findViewById(R.id.text_nome_aluno);
        this.mViewHolder.text_Nome_Materia = (TextView) findViewById(R.id.text_nome_materia);
        this.mViewHolder.text_Sua_Pergunta = (TextView) findViewById(R.id.text_sua_pergunta);
        this.mViewHolder.image_Foto_Aluno = (ImageView) findViewById(R.id.image_foto_aluno);

        this.mViewHolder.Button_aceitar = (Button) findViewById(R.id.button_aceitar);
        this.mViewHolder.Button_passar = (Button) findViewById(R.id.button_passar);

        mViewHolder.Button_aceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postPegaPergunta(Id_Professor, ID_Pergunta);
                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                }
            }
        });


        mViewHolder.Button_passar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Perguntas");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "PergunteMestreAceitePerguntaActivity --  onStart...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() != -1) {
            goCarregaDadosSessionUsuario();

            ID_Pergunta = getIntent().getIntExtra(getString(R.string.Id_Pergunta), 0);
            Pergunta = getIntent().getStringExtra(getString(R.string.Pergunta));
            nome_Aluno_Pergunta = getIntent().getStringExtra(getString(R.string.nome_Aluno_Pergunta));
            UrlImagemAluno = getIntent().getStringExtra(getString(R.string.UrlImagemAluno));
            Materia = getIntent().getStringExtra(getString(R.string.Materia));


            Log.i(Constants.TAG, "ID_Pergunta:. " + ID_Pergunta);
            Log.i(Constants.TAG, "Pergunta:. " + Pergunta);
            Log.i(Constants.TAG, "nome_Aluno_Pergunta:. " + nome_Aluno_Pergunta);
            Log.i(Constants.TAG, "UrlImagemAluno:. " + UrlImagemAluno);
            Log.i(Constants.TAG, "Materia:. " + Materia);

            mViewHolder.text_Nome_Aluno.setText(nome_Aluno_Pergunta);
            mViewHolder.text_Sua_Pergunta.setText(Pergunta);
            mViewHolder.text_Nome_Materia.setText(Materia);

            new AsyncTaskBaixaImagemAluno(PergunteMestreAceitePerguntaActivity.this)
                    .execute();

        } else {
            Toast.makeText(PergunteMestreAceitePerguntaActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void postPegaPergunta(int Id_Professor, int Id_Pergunta) throws JSONException {

        dialog.setTitle("Aceite pergunta");
        dialog.setMessage("Aguarde..");
        dialog.show();

        apiManager.postPegaPergunta(Id_Professor, Id_Pergunta, new TaskListener() {
            @Override
            public void onSuccess(String result) {

                try {

                    boolean Successo = new JSONObject(result).getBoolean("Successo");
                    final String Mensagem = new JSONObject(result).getString("Mensagem");

                    if (Successo) {
                        dialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Alerts.AlertInfo("Aceite pergunta", Mensagem);

                                Intent intent = new Intent(PergunteMestreAceitePerguntaActivity.this, ListaPergSemMestreProfessorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                }

            }

            @Override
            public void onError(String result) {
                TraTaErros("onError", result);
            }
        });
    }

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(PergunteMestreAceitePerguntaActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        return temConexao;
    }

    private void TraTaErros(final String Tipo, final String Erro) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Alerts.AlertError("Erro", Tipo + "\n" + Erro);
                Log.i(Constants.TAG, "Erro" + Erro);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(PergunteMestreAceitePerguntaActivity.this, ListaPergSemMestreProfessorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goCarregaDadosSessionUsuario() {

        Email = preferences.getStoreString(getString(R.string.Email));
        First_name = preferences.getStoreString(getString(R.string.First_name));
        isProfessor = preferences.getStoreString(getString(R.string.IsProfessor));
        Id_Professor = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Professor)));
        Id_Usuario = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Usuario)));
        Image = preferences.getStoreString(getString(R.string.Image));
        Type = Integer.valueOf(preferences.getStoreString(getString(R.string.Type)));

        Log.i(Constants.TAG, "session Email " + Email);
        Log.i(Constants.TAG, "session First_name " + First_name);
        Log.i(Constants.TAG, "session isProfessor " + isProfessor);
        Log.i(Constants.TAG, "session Id_Professor " + Id_Professor);
        Log.i(Constants.TAG, "session Id_Usuario " + Id_Usuario);
        Log.i(Constants.TAG, "session Type " + Type);

    }

    private class ViewHolder {
        Button Button_passar, Button_aceitar;
        TextView text_Nome_Materia, text_Nome_Aluno, text_Sua_Pergunta;
        ImageView image_Foto_Aluno;
    }

    private class AsyncTaskBaixaImagemAluno extends AsyncTask<Void, Bitmap, Void> {

        private Context context;
        private ProgressDialog Dialog;

        public AsyncTaskBaixaImagemAluno(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(context);
            dialog.setTitle("Baixando imagem aluno");
            dialog.setMessage("Aguarde..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL Uri = null;
            try {

                Uri = new URL(UrlImagemAluno);
                HttpURLConnection connection = (HttpURLConnection) Uri.openConnection();
                InputStream stream = connection.getInputStream();
                publishProgress(BitmapFactory.decodeStream(stream));
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                TraTaErros("IOException", e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);

            if (values[0] != null) {
                mViewHolder.image_Foto_Aluno.setImageBitmap(values[0]);
            }
        }
    }
}
