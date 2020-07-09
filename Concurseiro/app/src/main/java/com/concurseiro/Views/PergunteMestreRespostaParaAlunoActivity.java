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
import android.widget.EditText;
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
import com.concurseiro.Model.modelProfessorRespondePergunta;
import com.concurseiro.Model.modelSessionUsuario;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class PergunteMestreRespostaParaAlunoActivity extends AppCompatActivity {

    private SecurityPreferences preferences;
    private apiMessages Alerts;
    private Conectividade conectividade;
    private modelSessionUsuario sessionUser;
    private ProgressDialog dialog;
    private ViewHolder mViewHolder = new ViewHolder();
    private modelProfessorRespondePergunta dataProfessorRespondePergunta;


    private String Email;
    private String First_name;
    private String ImageProfessor;
    private int Id_Professor;
    private int Id_Usuario;
    private String isProfessor;
    private int Type;

    //Dados da tela de perguntas.
    private int ID_Pergunta;
    private String Pergunta;
    private String Materia;
    private String nome_Aluno_Pergunta;
    private String UrlImagemAluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_do_mestre);

        preferences = new SecurityPreferences(PergunteMestreRespostaParaAlunoActivity.this);
        conectividade = new Conectividade(PergunteMestreRespostaParaAlunoActivity.this);
        Alerts = new apiMessages(PergunteMestreRespostaParaAlunoActivity.this);
        sessionUser = new modelSessionUsuario();
        dialog = new ProgressDialog(PergunteMestreRespostaParaAlunoActivity.this);

        this.mViewHolder.Imagem_Professor_Responde = (ImageView) findViewById(R.id.image_professor_responde);
        this.mViewHolder.Nome_Professor_Responde = (TextView) findViewById(R.id.text_nome_professor_responde);
        this.mViewHolder.Resposta_Professor = (EditText) findViewById(R.id.edit_resposta_professor);
        this.mViewHolder.button_Responder = (Button) findViewById(R.id.button_responder);

        mViewHolder.button_Responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mViewHolder.Resposta_Professor.length() > 0) {
                        dataProfessorRespondePergunta = new modelProfessorRespondePergunta(ID_Pergunta, Id_Professor, mViewHolder.Resposta_Professor.getText().toString(), "", "");
                        postProfessorRespondePergunta();
                    } else {
                        Toast.makeText(PergunteMestreRespostaParaAlunoActivity.this, "É necessário preencher o campo resposta", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                }
            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Resposta do Mestre");
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

            //mViewHolder.text_Nome_Aluno.setText(nome_Aluno_Pergunta);
            //mViewHolder.text_Sua_Pergunta.setText(Pergunta);
            //mViewHolder.text_Nome_Materia.setText(Materia);

            new AsyncTaskBaixaImagemProfessor(PergunteMestreRespostaParaAlunoActivity.this)
                    .execute();

        } else {
            Toast.makeText(PergunteMestreRespostaParaAlunoActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void postProfessorRespondePergunta() throws JSONException {

        apiManager.postProfessorRespondePergunta(dataProfessorRespondePergunta, new TaskListener() {
            @Override
            public void onSuccess(String result) {

                try {
                    Boolean Successo = new JSONObject(result).getBoolean("Successo");
                    final String Mensagem = new JSONObject(result).getString("Mensagem");
                    if (Successo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Alerts.AlertInfo("Responder", Mensagem);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", result);

                }
            }

            @Override
            public void onError(String result) {
                TraTaErros("onError", result);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(PergunteMestreRespostaParaAlunoActivity.this, ListaPergSemMestreProfessorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void goCarregaDadosSessionUsuario() {

        Email = preferences.getStoreString(getString(R.string.Email));
        First_name = preferences.getStoreString(getString(R.string.First_name));
        isProfessor = preferences.getStoreString(getString(R.string.IsProfessor));
        Id_Professor = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Professor)));
        Id_Usuario = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Usuario)));
        ImageProfessor = preferences.getStoreString(getString(R.string.Image));
        Type = Integer.valueOf(preferences.getStoreString(getString(R.string.Type)));

        Log.i(Constants.TAG, "session Email " + Email);
        Log.i(Constants.TAG, "session First_name " + First_name);
        Log.i(Constants.TAG, "session isProfessor " + isProfessor);
        Log.i(Constants.TAG, "session Id_Professor " + Id_Professor);
        Log.i(Constants.TAG, "session Id_Usuario " + Id_Usuario);
        Log.i(Constants.TAG, "session Type " + Type);

    }

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(PergunteMestreRespostaParaAlunoActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        return temConexao;
    }

    private class ViewHolder {
        ImageView Imagem_Professor_Responde;
        TextView Nome_Professor_Responde;
        EditText Resposta_Professor;
        Button button_Responder;
    }

    private class AsyncTaskBaixaImagemProfessor extends AsyncTask<Void, Bitmap, Void> {

        private Context context;
        private ProgressDialog dialog;

        public AsyncTaskBaixaImagemProfessor(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setTitle("Professor Respondendo");
            dialog.setMessage("Aguarde..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String UrlImagemAluno = null;
            if (!ImageProfessor.equals("")) {
                try {

                    if (ImageProfessor.contains("http")) {
                        UrlImagemAluno = ImageProfessor;
                    } else {
                        UrlImagemAluno = Constants.URL_SITE  + ImageProfessor;
                    }

                    URL UrlIFotoProf = new URL(UrlImagemAluno);
                    HttpURLConnection connection = (HttpURLConnection) UrlIFotoProf.openConnection();
                    InputStream stream = connection.getInputStream();
                    publishProgress(BitmapFactory.decodeStream(stream));

                } catch (IOException e) {
                    e.printStackTrace();
                    TraTaErros("IOException", e.getLocalizedMessage());
                }
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

                mViewHolder.Imagem_Professor_Responde.setImageBitmap(values[0]);
                mViewHolder.Nome_Professor_Responde.setText(First_name);
            }
        }
    }
}
