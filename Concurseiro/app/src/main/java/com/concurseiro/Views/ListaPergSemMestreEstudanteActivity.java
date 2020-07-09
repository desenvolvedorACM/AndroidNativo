package com.concurseiro.Views;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.concurseiro.Adapter.RVPerguntasSemMestreEstudante;
import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelPerguntasSemMestre;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class ListaPergSemMestreEstudanteActivity extends AppCompatActivity {

    private ArrayList<modelPerguntasSemMestre> listPerguntas;
    private apiMessages Alerts;
    private SecurityPreferences preferences;
    private Conectividade conectividade;
    private RecyclerView listViewEPergEstudante;
    private LinearLayoutManager LayoutManager;

    private String Email;
    private String First_name;
    private String Image;
    private String isProfessor;
    private int Id_Professor;
    private int Id_Usuario;
    private int Type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_perg_sem_mestre_estudante);

        listPerguntas = new ArrayList<modelPerguntasSemMestre>();
        Alerts = new apiMessages(ListaPergSemMestreEstudanteActivity.this);
        preferences = new SecurityPreferences(ListaPergSemMestreEstudanteActivity.this);
        conectividade = new Conectividade(ListaPergSemMestreEstudanteActivity.this);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Perguntas do estudante");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        goCarregaDadosSessionUsuario();
        new AsyncTaskListaPerguntasEstudante(ListaPergSemMestreEstudanteActivity.this)
                .execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnection() == -1) {
            Toast.makeText(ListaPergSemMestreEstudanteActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(ListaPergSemMestreEstudanteActivity.this, MenuActivity.class);
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

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(ListaPergSemMestreEstudanteActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        return temConexao;
    }

    private class AsyncTaskListaPerguntasEstudante extends AsyncTask<Void, ArrayList<modelPerguntasSemMestre>, Void> {

        private Context context;
        private ProgressDialog dialog;
        private Boolean Successo;
        private JSONArray lstPerguntasUsuario;
        private modelPerguntasSemMestre modelPerguntas;


        public AsyncTaskListaPerguntasEstudante(Context context) {
            this.context = context;
            this.dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Aguarde, carregando as perguntas...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                if (isProfessor.equals("N")) {
                    PergunteMestreTodasPerguntasSemMestreEstudante();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
            }

            return null;
        }


        private void PergunteMestreTodasPerguntasSemMestreEstudante() throws JSONException {

            apiManager.getTodasPerguntasUsuario(Id_Usuario, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        listPerguntas.clear();
                        Successo = new JSONObject(result).getBoolean("Successo");
                        if (Successo) {

                            lstPerguntasUsuario = new JSONArray(new JSONObject(result).getString("lstPerguntasUsuario"));
                            for (int i = 0; i < lstPerguntasUsuario.length(); i++) {

                                modelPerguntas = new modelPerguntasSemMestre();
                                JSONObject jsonDados = new JSONObject(lstPerguntasUsuario.get(i).toString());

                                //CARREGO TODOS OS CAMPOS.
                                modelPerguntas.setId_Pergunta(jsonDados.getInt("Id_Pergunta"));
                                modelPerguntas.setDataHoraP(jsonDados.getString("DataHoraP"));
                                modelPerguntas.setDataHoraR(jsonDados.getString("DataHoraR"));
                                modelPerguntas.setPergunta(jsonDados.getString("Pergunta"));
                                modelPerguntas.setConcluida(jsonDados.getString("Concluida"));

                                JSONObject jsonUsuario = new JSONObject(jsonDados.getString("Usuario"));
                                JSONObject jsonProfessor = new JSONObject(jsonDados.getString("Professor"));

                                modelPerguntas.setFirst_name(jsonUsuario.getString("First_name"));
                                modelPerguntas.setLast_name(jsonUsuario.getString("Last_name"));


                                //CARREGO AS IMAGENS
                                modelPerguntas.setImage(jsonUsuario.getString("Image"));

                                try {

                                    URL uri = new URL(modelPerguntas.getImage());
                                    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                                    InputStream stream = connection.getInputStream();
                                    modelPerguntas.setImageAluno(BitmapFactory.decodeStream(stream));


                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    Log.i(Constants.TAG, "MalformedURLException: " + e.getLocalizedMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.i(Constants.TAG, "IOException: " + e.getLocalizedMessage());
                                }

                                //Log.i(Constants.TAG, "Image: " + Constants.URL_SITE  +  jsonUsuario.getString("Image"));
                                listPerguntas.add(modelPerguntas);
                            }

                            publishProgress(listPerguntas);

                        }

                    } catch (final JSONException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Alerts.AlertError("JSONException", e.getLocalizedMessage());
                            }
                        });

                        Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                        dialog.dismiss();
                    }
                }

                @Override
                public void onError(String result) {

                    try {

                        final String ErroMessage = new JSONObject(result).getString("ErroMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Alerts.AlertInfo("onError", ErroMessage);
                            }
                        });

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                        dialog.dismiss();
                    }
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(ArrayList<modelPerguntasSemMestre>... lstPergSemProfessor) {
            super.onProgressUpdate(lstPergSemProfessor);

            if (lstPergSemProfessor.length > 0) {

                loadPerguntasSemMestreEstudante();
                dialog.dismiss();
            }
        }

    }

    private void loadPerguntasSemMestreEstudante() {

        LayoutManager = new LinearLayoutManager(ListaPergSemMestreEstudanteActivity.this);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listViewEPergEstudante = (RecyclerView) findViewById(R.id.listViewEPergEstudante);
        listViewEPergEstudante.setHasFixedSize(true);
        listViewEPergEstudante.setLayoutManager(LayoutManager);

        listViewEPergEstudante.setAdapter(new RVPerguntasSemMestreEstudante(ListaPergSemMestreEstudanteActivity.this, listPerguntas));
    }
}
