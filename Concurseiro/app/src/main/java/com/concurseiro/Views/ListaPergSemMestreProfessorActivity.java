package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.concurseiro.Adapter.ArrayAdapterPergSemMestre;
import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelPerguntasSemMestre;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class ListaPergSemMestreProfessorActivity extends AppCompatActivity {

    private ArrayAdapterPergSemMestre adapter;
    private ArrayList<modelPerguntasSemMestre> listPerguntas;
    private apiMessages Alerts;
    private SecurityPreferences preferences;
    private Conectividade conectividade;

    private String Email;
    private String First_name;
    private String Image;
    private String isProfessor;
    private int Id_Professor;
    private int Id_Usuario;
    private int Type;

    //Dados Passar entre telas.
    private int Id_Pergunta;
    private String Pergunta;
    private String Materia;
    private String nome_Aluno_Pergunta;
    private String UrlImagemAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_perguntas_prof);

        listPerguntas = new ArrayList<modelPerguntasSemMestre>();
        Alerts = new apiMessages(ListaPergSemMestreProfessorActivity.this);
        preferences = new SecurityPreferences(ListaPergSemMestreProfessorActivity.this);
        conectividade = new Conectividade(ListaPergSemMestreProfessorActivity.this);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Perguntas das suas disciplinas");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "ListaPergSemMestreProfessorActivity --  onStart...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() != -1) {
            goCarregaDadosSessionUsuario();
            new AsyncTaskListaPerguntas(ListaPergSemMestreProfessorActivity.this)
                    .execute();
        } else {
            Toast.makeText(ListaPergSemMestreProfessorActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnection() == -1) {
            Toast.makeText(ListaPergSemMestreProfessorActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }

    private void loadPerguntasSemMestreDisciplina() {

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        adapter = new ArrayAdapterPergSemMestre(ListaPergSemMestreProfessorActivity.this, listPerguntas);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(R.color.colorTitle);
                // set width of an option (px)
                item1.setWidth(150);
                //item1.setTitle("Action 1");
                //item1.setTitleSize(18);
                item1.setIcon(R.mipmap.ic_done);
                //item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                item2.setBackground(R.color.colorTitle);
                item2.setWidth(150);
                //item2.setTitle("Action 2");
                //item2.setTitleSize(18);
                // item2.setTitleColor(Color.WHITE);
                item2.setIcon(R.drawable.ic_baloon3);
                menu.addMenuItem(item2);

                SwipeMenuItem item3 = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                item3.setBackground(R.color.colorTitle);
                item3.setWidth(150);
                //item2.setTitle("Action 3");
                item3.setIcon(R.mipmap.ic_clear);
                // item3.setTitleSize(18);
                // item3.setTitleColor(Color.WHITE);
                menu.addMenuItem(item3);
            }
        };


        //set MenuCreator
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                // Toast.makeText(getApplicationContext(), "sem ação escolha uma  "+ value , Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                //int Id_Professor = listPerguntas.get(position).getId_Professor();
                Id_Pergunta = listPerguntas.get(position).getId_Pergunta();
                Pergunta = listPerguntas.get(position).getPergunta();
                nome_Aluno_Pergunta = listPerguntas.get(position).getFirst_name();
                UrlImagemAluno = listPerguntas.get(position).getImage();
                Materia = "Matemática";


                Log.i(Constants.TAG, "Id_Pergunta" + Id_Pergunta);
                Log.i(Constants.TAG, "Pergunta" + Pergunta);

                switch (index) {
                    case 0:
                        goPergunteMestreAceitaPergunta();
                        break;
                    case 1:
                        goPergunteMestreRespostaAluno();
                        break;
                    case 2:
                        goSoltaPergunta();
                        break;
                    default:
                        Alerts.AlertInfo("Aviso", "Nenhuma opção selecionada ");
                        break;
                }

                return false;
            }
        });
    }

    private void goSoltaPergunta() {
        try {
            Log.i(Constants.TAG, "Id_Pergunta " + Id_Pergunta);
            Log.i(Constants.TAG, "Id_Professor " + Id_Professor);

            postSoltaPergunta(Id_Pergunta, Id_Professor);
        } catch (final JSONException e) {
            e.printStackTrace();
            TraTaErros("JSONException", e.getLocalizedMessage());
        }
    }

    private void goPergunteMestreRespostaAluno() {

        Intent intent = new Intent(ListaPergSemMestreProfessorActivity.this, PergunteMestreRespostaParaAlunoActivity.class);
        intent.putExtra(getString(R.string.Id_Pergunta), Id_Pergunta);
        intent.putExtra(getString(R.string.Pergunta), Pergunta);
        intent.putExtra(getString(R.string.nome_Aluno_Pergunta), nome_Aluno_Pergunta);
        intent.putExtra(getString(R.string.UrlImagemAluno), UrlImagemAluno);
        intent.putExtra(getString(R.string.Materia), Materia);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goPergunteMestreAceitaPergunta() {

        Intent intent = new Intent(ListaPergSemMestreProfessorActivity.this, PergunteMestreAceitePerguntaActivity.class);
        intent.putExtra(getString(R.string.Id_Pergunta), Id_Pergunta);
        intent.putExtra(getString(R.string.Pergunta), Pergunta);
        intent.putExtra(getString(R.string.nome_Aluno_Pergunta), nome_Aluno_Pergunta);
        intent.putExtra(getString(R.string.UrlImagemAluno), UrlImagemAluno);
        intent.putExtra(getString(R.string.Materia), Materia);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void postSoltaPergunta(int Id_Pergunta, int Id_Professor) throws JSONException {

        apiManager.postSoltaPergunta(Id_Pergunta, Id_Professor, new TaskListener() {
            @Override
            public void onSuccess(String result) {
                boolean Successo;
                try {

                    Successo = new JSONObject(result).getBoolean("Successo");
                    final String Mensagem = new JSONObject(result).getString("Mensagem");

                    if (Successo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncTaskListaPerguntas(ListaPergSemMestreProfessorActivity.this)
                                        .execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                }

            }

            @Override
            public void onError(final String result) {
                TraTaErros("onError", result);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(ListaPergSemMestreProfessorActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class AsyncTaskListaPerguntas extends AsyncTask<Void, ArrayList<modelPerguntasSemMestre>, Void> {

        private Context context;
        private ProgressDialog dialog;
        private Boolean Successo;
        private JSONArray ArrayPerguntasSemProfessor;
        private modelPerguntasSemMestre modelPerguntas;


        public AsyncTaskListaPerguntas(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Aguarde, carregando as perguntas...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                PergunteMestreTodasPerguntasSemProfessor();

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                TraTaErros("JSONException", e.getLocalizedMessage());
            }

            return null;
        }


        private void PergunteMestreTodasPerguntasSemProfessor() throws JSONException {

            apiManager.getPergunteMestreTodasPerguntasSemProfessor(Id_Professor, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        listPerguntas.clear();
                        Successo = new JSONObject(result).getBoolean("Successo");
                        if (Successo) {

                            ArrayPerguntasSemProfessor = new JSONArray(new JSONObject(result).getString("lstPerguntasSemProfessor"));
                            for (int i = 0; i < ArrayPerguntasSemProfessor.length(); i++) {

                                modelPerguntas = new modelPerguntasSemMestre();
                                JSONObject jsonDados = new JSONObject(ArrayPerguntasSemProfessor.get(i).toString());


                                //CARREGO TODOS OS CAMPOS.
                                modelPerguntas.setId_Pergunta(jsonDados.getInt("Id_Pergunta"));
                                modelPerguntas.setDataHoraP(jsonDados.getString("DataHoraP"));
                                modelPerguntas.setDataHoraR(jsonDados.getString("DataHoraR"));
                                modelPerguntas.setPergunta(jsonDados.getString("Pergunta"));
                                modelPerguntas.setConcluida(jsonDados.getString("Concluida"));


                                JSONObject jsonUsuario = new JSONObject(jsonDados.getString("Usuario"));
                                JSONObject jsonProfessor = new JSONObject(jsonDados.getString("Professor"));
                                JSONObject jsonPerguntasInfo = new JSONObject(jsonDados.getString("PerguntasInfo"));

                                modelPerguntas.setId_Professor(jsonPerguntasInfo.getInt("Id_Professor"));
                                modelPerguntas.setFirst_name(jsonUsuario.getString("First_name"));
                                modelPerguntas.setLast_name(jsonUsuario.getString("Last_name"));


                                try {

                                    //CARREGO AS IMAGENS
                                    String ImagemUsuario = jsonUsuario.getString("Image");
                                    if (!ImagemUsuario.equals("null")) {

                                        if (ImagemUsuario.contains("http")) {
                                            modelPerguntas.setImage(jsonUsuario.getString("Image"));
                                        } else {
                                            modelPerguntas.setImage(Constants.URL_SITE + jsonUsuario.getString("Image"));
                                        }

                                        URL uri = new URL(modelPerguntas.getImage());
                                        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                                        InputStream stream = connection.getInputStream();
                                        modelPerguntas.setImageAluno(BitmapFactory.decodeStream(stream));
                                    }


                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    Log.i(Constants.TAG, "MalformedURLException: " + e.getLocalizedMessage());
                                    TraTaErros("MalformedURLException", e.getLocalizedMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.i(Constants.TAG, "IOException: " + e.getLocalizedMessage());
                                    TraTaErros("IOException", e.getLocalizedMessage());
                                }

                                //Log.i(Constants.TAG, "Image: " + Constants.URL_SITE  +  jsonUsuario.getString("Image"));
                                listPerguntas.add(modelPerguntas);
                            }

                            publishProgress(listPerguntas);

                        }

                    } catch (final JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                        TraTaErros("IOException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    dialog.dismiss();
                    TraTaErros("onError", result);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(ArrayList<modelPerguntasSemMestre>... lstTodasPerguntasSemProfessor) {
            super.onProgressUpdate(lstTodasPerguntasSemProfessor);

            if (lstTodasPerguntasSemProfessor.length > 0) {
                loadPerguntasSemMestreDisciplina();
                dialog.dismiss();
            }
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
            Toast.makeText(ListaPergSemMestreProfessorActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
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
}
