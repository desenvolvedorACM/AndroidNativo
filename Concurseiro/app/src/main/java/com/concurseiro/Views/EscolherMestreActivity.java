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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.concurseiro.Adapter.SpinnerAdapterDisciplinasFilha;
import com.concurseiro.Adapter.SpinnerAdapterDisciplinasPai;
import com.concurseiro.Adapter.SpinnerAdapterProfessoresDisciplina;
import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelDadosTelaPergunteMestre;
import com.concurseiro.Model.modelDisciplinasFilha;
import com.concurseiro.Model.modelDisciplinasPai;
import com.concurseiro.Model.modelSessionUsuario;
import com.concurseiro.Model.modelTodosProfessoresDisciplina;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class EscolherMestreActivity extends AppCompatActivity {

    private SecurityPreferences preferences;
    private apiMessages Alerts;
    private Conectividade conectividade;
    private modelSessionUsuario sessionUser;

    private ArrayList<modelDisciplinasPai> lstdisciplinasPai;
    private ArrayList<modelDisciplinasFilha> lstdisciplinasFilhas;
    private ArrayList<modelTodosProfessoresDisciplina> lstTodosProfessoresDisciplina;

    private SpinnerAdapterDisciplinasPai adpDisciplinasPai;
    private SpinnerAdapterDisciplinasFilha adpDisciplinasFilha;
    private SpinnerAdapterProfessoresDisciplina adpProfessoresDisciplina;

    private ViewHolder mViewHolder;
    private ProgressDialog dialog;

    private String Email;
    private String First_name;
    private String Image;
    private String isProfessor;
    private int Id_Professor;
    private int Id_Usuario;
    private int Id_Pessoa;
    private int Type;
    private int ID_Area_disciplina;

    private Bitmap ImageProfessor;
    private String Name_Professor;
    private String UrlImageProfessor;
    private modelDadosTelaPergunteMestre dataTelaPergunteMestre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_mestre);

        preferences = new SecurityPreferences(EscolherMestreActivity.this);
        conectividade = new Conectividade(EscolherMestreActivity.this);
        Alerts = new apiMessages(EscolherMestreActivity.this);
        sessionUser = new modelSessionUsuario();
        dialog = new ProgressDialog(EscolherMestreActivity.this);

        lstdisciplinasPai = new ArrayList<>();
        lstdisciplinasFilhas = new ArrayList<>();
        lstTodosProfessoresDisciplina = new ArrayList<>();
        mViewHolder = new ViewHolder();


        this.mViewHolder.spnTodasDisciplinas = (Spinner) findViewById(R.id.spnDisciplinasPai);
        this.mViewHolder.spnTodasDisciplinasFilhas = (Spinner) findViewById(R.id.spnDisciplinasFilhas);
        this.mViewHolder.spnTodosProfessoresDisciplina = (Spinner) findViewById(R.id.spnProfessoresDisciplinaSel);
        this.mViewHolder.button_Feito = (Button) findViewById(R.id.button_feito);

        mViewHolder.button_Feito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (Id_Professor != 0) {
                Intent intent = new Intent(EscolherMestreActivity.this, PergunteMestreActivity.class);

                intent.putExtra(getString(R.string.Id_Pessoa), Id_Pessoa);
                intent.putExtra(getString(R.string.Id_Area_disciplina), ID_Area_disciplina);
                intent.putExtra(getString(R.string.Id_Professor), Id_Professor);
                intent.putExtra(getString(R.string.Name_Professor), Name_Professor);
                intent.putExtra(getString(R.string.UrlImageProfessor), UrlImageProfessor);

                if (UrlImageProfessor != null) {
                    intent.putExtra(getString(R.string.ImageProfessor), UrlImageProfessor);
                }

                //intent.putExtra(getString(R.string.DadosPergunteMestre), dataTelaPergunteMestre);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //}
            }
        });


        lstdisciplinasPai.clear();
        lstdisciplinasPai.add(new modelDisciplinasPai(0, "Disciplinas"));

        lstTodosProfessoresDisciplina.clear();
        lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(0, "Professores", "", "", null));

        adpProfessoresDisciplina = new SpinnerAdapterProfessoresDisciplina(EscolherMestreActivity.this, R.layout.item_row_spinner_professores_disciplina, lstTodosProfessoresDisciplina);
        mViewHolder.spnTodosProfessoresDisciplina.setAdapter(adpProfessoresDisciplina);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Escolha o mestre");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "EscolherMestreActivity onStart...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() != -1) {
            goCarregaDadosSessionUsuario();
            goCarregaTodasDisciplinasPai();

        } else {
            Toast.makeText(EscolherMestreActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "MenuActivity onResume...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() == -1) {
            Toast.makeText(EscolherMestreActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }


    private void goCarregaTodasDisciplinasPai() {

        try {

            dialog.setTitle("Carregando disciplinas");
            dialog.setMessage("Agurade..");
            dialog.show();

            apiManager.getTodasDisciplinasPai(new TaskListener() {
                @Override
                public void onSuccess(String result) {
                    try {

                        if (!result.isEmpty()) {

                            Boolean Sucesso = new JSONObject(result).getBoolean("Successo");
                            if (Sucesso) {

                                JSONArray lstAreaDisciplinasPai = new JSONArray(new JSONObject(result).getString("lstAreaDisciplinasPai"));
                                for (int i = 0; i < lstAreaDisciplinasPai.length(); i++) {
                                    JSONObject jsonDisciplinasPai = new JSONObject(lstAreaDisciplinasPai.get(i).toString());
                                    //Log.i(Constants.TAG, "Dados: " + jsonDisciplinasPai.toString());

                                    lstdisciplinasPai.add(new modelDisciplinasPai(jsonDisciplinasPai.getInt("Id_Area_Disciplina_Pai"), jsonDisciplinasPai.getString("Descricao")));
                                }


                                if (lstdisciplinasPai.size() > 0) {
                                    //String[] itens = new String[]{"Ciências Exatas e da Terra", "Biologia", "Teste"};

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            //ArrayAdapter<modelDisciplinasPai> adpDisciplinas = new ArrayAdapter<modelDisciplinasPai>(EscolherMestreActivity.this, android.R.layout.simple_spinner_dropdown_item, lstdisciplinasPai);
                                            //adpDisciplinas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                            adpDisciplinasPai = new SpinnerAdapterDisciplinasPai(EscolherMestreActivity.this, R.layout.item_row_spinner_disciplinaspai, lstdisciplinasPai);
                                            mViewHolder.spnTodasDisciplinas.setAdapter(adpDisciplinasPai);
                                            mViewHolder.spnTodasDisciplinas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    Log.i(Constants.TAG, "Id Pai: " + lstdisciplinasPai.get(position).getId_Area_Disciplina_Pai());
                                                    goCarregaTodasDisciplinasFilhas(lstdisciplinasPai.get(position).getId_Area_Disciplina_Pai());
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    });
                                }

                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                            TraTaErros("Erro generico", "Sem Acesso SQL..");
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        TraTaErros("JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(final String result) {
                    dialog.dismiss();
                    TraTaErros("JSONException", result);
                }
            });

        } catch (final JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
            TraTaErros("JSONException", e.getLocalizedMessage());
        }

    }

    private void goCarregaTodasDisciplinasFilhas(long Id_Area_disciplina) {


        try {

            //dialog.setTitle("Carregando disciplinas");
            //dialog.setMessage("Agurade..");
            //dialog.show();

            apiManager.getTodasDisciplinasFilhas(Id_Area_disciplina, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        lstdisciplinasFilhas.clear();
                        lstdisciplinasFilhas.add(new modelDisciplinasFilha(0, "Disciplinas"));

                        if (!result.isEmpty()) {

                            Boolean Sucesso = new JSONObject(result).getBoolean("Successo");
                            if (Sucesso) {

                                JSONArray lstAreaDisciplinasFilha = new JSONArray(new JSONObject(result).getString("lstAreaDisciplinasFilha"));
                                for (int i = 0; i < lstAreaDisciplinasFilha.length(); i++) {

                                    JSONObject jsonDisciplinasFilhas = new JSONObject(lstAreaDisciplinasFilha.get(i).toString());
                                    //Log.i(Constants.TAG, "Dados: " + jsonDisciplinasPai.toString());
                                    lstdisciplinasFilhas.add(new modelDisciplinasFilha(jsonDisciplinasFilhas.getInt("Id_Area_Disciplina"), jsonDisciplinasFilhas.getString("Descricao")));

                                }

                                if (lstdisciplinasFilhas.size() > 0) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adpDisciplinasFilha = new SpinnerAdapterDisciplinasFilha(EscolherMestreActivity.this, R.layout.item_row_spinner_disciplinaspai, lstdisciplinasFilhas);
                                            mViewHolder.spnTodasDisciplinasFilhas.setAdapter(adpDisciplinasFilha);

                                            mViewHolder.spnTodasDisciplinasFilhas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    int Id_area_disciplina = lstdisciplinasFilhas.get(position).getId_Area_Disciplina();
                                                    Log.i(Constants.TAG, "ID Filha: " + Id_area_disciplina);

                                                    if (Id_area_disciplina > 0) {
                                                        goCarregaTodosProfessoresDisciplina(lstdisciplinasFilhas.get(position).getId_Area_Disciplina());
                                                    }

                                                    //new AsyncTaskProfessoresDisciplina(EscolherMestreActivity.this)
                                                    //.execute(lstdisciplinasFilhas.get(position).getId_Area_Disciplina());
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        } else {
                            TraTaErros("Erro generico", "Sem Acesso SQL..");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        TraTaErros("JSONException", e.getLocalizedMessage());
                    }

                }

                @Override
                public void onError(String result) {
                    dialog.dismiss();
                    TraTaErros("JSONException", result);
                }
            });
        } catch (final JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
            TraTaErros("JSONException", e.getLocalizedMessage());
        }
    }

    private void goCarregaTodosProfessoresDisciplina(int Id_Area_disciplina) {

        dialog.setTitle("Carregando professores");
        dialog.setMessage("Aguarde..");
        dialog.show();

        try {

            ID_Area_disciplina = Id_Area_disciplina;
            lstTodosProfessoresDisciplina.clear();
            lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(0, "Professores", "", "", null));

            //if(Id_Area_disciplina != 0) {}
            apiManager.getTodosProfessoresDisciplina(Id_Area_disciplina, new TaskListener() {
                @Override
                public void onSuccess(String result) {
                    Boolean Sucesso = null;
                    URL Uri = null;
                    String UrlImagemProfessor;

                    try {

                        if (!result.isEmpty()) {

                            Sucesso = new JSONObject(result).getBoolean("Successo");
                            if (Sucesso) {

                                JSONArray lstProfessoresDisciplina = new JSONArray(new JSONObject(result).getString("lstProfessoresDisciplina"));
                                for (int i = 0; i < lstProfessoresDisciplina.length(); i++) {
                                    JSONObject dados = new JSONObject(lstProfessoresDisciplina.get(i).toString());

                                    String ImagemBaixar = dados.getString("Image");
                                    if (!ImagemBaixar.contains("null")) {

                                        try {

                                            if (ImagemBaixar.contains("http")) {
                                                Uri = new URL(dados.getString("Image"));
                                                UrlImagemProfessor = dados.getString("Image");
                                            } else {
                                                Uri = new URL(Constants.URL_SITE + dados.getString("Image"));
                                                UrlImagemProfessor = Constants.URL_SITE + dados.getString("Image");
                                            }

                                            //Log.i(Constants.TAG, "Url baixar: " + Uri);
                                            HttpURLConnection connection = (HttpURLConnection) Uri.openConnection();
                                            InputStream stream = connection.getInputStream();
                                            Bitmap ImagemProf = BitmapFactory.decodeStream(stream);

                                            lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(dados.getInt("Id_Professor"), dados.getString("First_name"), dados.getString("Email"), UrlImagemProfessor, ImagemProf));

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            dialog.dismiss();
                                            TraTaErros("IOException", e.getLocalizedMessage());
                                        }
                                    } else {
                                        lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(dados.getInt("Id_Professor"), dados.getString("First_name"), dados.getString("Email"), dados.getString("Image"), null));
                                        dialog.dismiss();
                                    }

                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adpProfessoresDisciplina = new SpinnerAdapterProfessoresDisciplina(EscolherMestreActivity.this, R.layout.item_row_spinner_professores_disciplina, lstTodosProfessoresDisciplina);
                                        mViewHolder.spnTodosProfessoresDisciplina.setAdapter(adpProfessoresDisciplina);

                                        mViewHolder.spnTodosProfessoresDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                Id_Professor = lstTodosProfessoresDisciplina.get(position).getId_Professor();
                                                ImageProfessor = lstTodosProfessoresDisciplina.get(position).getImage();
                                                Name_Professor = lstTodosProfessoresDisciplina.get(position).getFirst_name();
                                                UrlImageProfessor = lstTodosProfessoresDisciplina.get(position).getUrlImage();

                                                Log.i(Constants.TAG, "Id_Professor " + Id_Professor);
                                                Log.i(Constants.TAG, "Name_Professor " + Name_Professor);
                                                Log.i(Constants.TAG, "UrlImageProfessor " + UrlImageProfessor);

                                                //DADOS TRANSFERIR OUTRA TELA.
                                                   /*
                                                   dataTelaPergunteMestre = new modelDadosTelaPergunteMestre();
                                                    dataTelaPergunteMestre.setId_Professor(Id_Professor);
                                                    dataTelaPergunteMestre.setID_Area_disciplina(ID_Area_disciplina);
                                                    dataTelaPergunteMestre.setId_Pessoa(122);
                                                    dataTelaPergunteMestre.setImageProfessor(ImageProfessor);
                                                    dataTelaPergunteMestre.setNome_Professor(Name_Professor);
                                                    */

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            TraTaErros("Erro generico", "Sem Acesso SQL..");
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        TraTaErros("JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    dialog.dismiss();
                    TraTaErros("onError", result);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            dialog.dismiss();
            TraTaErros("JSONException", e.getLocalizedMessage());
        }

    }

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(EscolherMestreActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
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

    private void goCarregaDadosSessionUsuario() {

        Email = preferences.getStoreString(getString(R.string.Email));
        First_name = preferences.getStoreString(getString(R.string.First_name));
        isProfessor = preferences.getStoreString(getString(R.string.IsProfessor));
        Id_Pessoa = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Pessoa)));
        Id_Professor = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Professor)));
        Id_Usuario = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Usuario)));
        Image = preferences.getStoreString(getString(R.string.Image));
        Type = Integer.valueOf(preferences.getStoreString(getString(R.string.Type)));

        sessionUser.setEmail(Email);
        sessionUser.setFirst_name(First_name);
        sessionUser.setIsProfessor(isProfessor);
        sessionUser.setId_Professor(Id_Professor);
        sessionUser.setId_Usuario(Id_Usuario);
        sessionUser.setImage(Image);

        Log.i(Constants.TAG, "Email " + Email);
        Log.i(Constants.TAG, "First_name " + First_name);
        Log.i(Constants.TAG, "isProfessor " + isProfessor);
        Log.i(Constants.TAG, "Id_Pessoa " + Id_Pessoa);
        Log.i(Constants.TAG, "Id_Professor " + Id_Professor);
        Log.i(Constants.TAG, "Id_Usuario " + Id_Usuario);
        Log.i(Constants.TAG, "Image " + Image);
        Log.i(Constants.TAG, "Type " + Type);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(Constants.TAG, "onOptionsItemSelected " + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(EscolherMestreActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class ViewHolder {
        Spinner spnTodasDisciplinas, spnTodasDisciplinasFilhas, spnTodosProfessoresDisciplina;
        Button button_Feito;
    }


    private class AsyncTaskProfessoresDisciplina extends AsyncTask<Integer, ArrayList<modelTodosProfessoresDisciplina>, Void> {

        private Context context;
        private ProgressDialog dialog;

        public AsyncTaskProfessoresDisciplina(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setTitle("Carregando prefessores");
            dialog.setMessage("Aguarde..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            try {

                Log.i(Constants.TAG, "Id disciplina filha " + params[0]);
                apiManager.getTodosProfessoresDisciplina(params[0], new TaskListener() {
                    @Override
                    public void onSuccess(String result) {

                        Boolean Sucesso = null;
                        try {

                            if (!result.isEmpty()) {

                                lstTodosProfessoresDisciplina.clear();
                                Sucesso = new JSONObject(result).getBoolean("Successo");
                                if (Sucesso) {
                                    JSONArray lstProfessoresDisciplina = new JSONArray(new JSONObject(result).getString("lstProfessoresDisciplina"));
                                    for (int i = 0; i < lstProfessoresDisciplina.length(); i++) {
                                        JSONObject dados = new JSONObject(lstProfessoresDisciplina.get(i).toString());

                                        if (dados.getString("Image").isEmpty() && dados.getString("Image") != null) {

                                            try {

                                                URL Uri = new URL(dados.getString("Image"));
                                                HttpURLConnection connection = (HttpURLConnection) Uri.openConnection();
                                                InputStream stream = connection.getInputStream();
                                                Bitmap ImagemProf = BitmapFactory.decodeStream(stream);

                                                lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(dados.getInt("Id_Professor"), dados.getString("First_name"), dados.getString("Email"), dados.getString("Image"), ImagemProf));

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                dialog.dismiss();
                                                TraTaErros("IOException", e.getLocalizedMessage());
                                            }
                                        } else {
                                            lstTodosProfessoresDisciplina.add(new modelTodosProfessoresDisciplina(dados.getInt("Id_Professor"), dados.getString("First_name"), dados.getString("Email"), dados.getString("Image"), null));
                                        }

                                    }

                                    publishProgress(lstTodosProfessoresDisciplina);
                                    dialog.dismiss();
                                }
                            } else {
                                TraTaErros("Erro generico", "Sem Acesso SQL..");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            TraTaErros("JSONException", e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onError(String result) {
                        dialog.dismiss();
                        TraTaErros("onError", result);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                TraTaErros("JSONException", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(ArrayList<modelTodosProfessoresDisciplina>... values) {
            super.onProgressUpdate(values);

            if (values[0].size() != 0) {
                adpProfessoresDisciplina = new SpinnerAdapterProfessoresDisciplina(EscolherMestreActivity.this, R.layout.item_row_spinner_professores_disciplina, lstTodosProfessoresDisciplina);
                mViewHolder.spnTodosProfessoresDisciplina.setAdapter(adpProfessoresDisciplina);

            }
        }

    }


    /*@Override
    public boolean onSupportNavigateUp() {

        Log.i(Constants.TAG, "onSupportNavigateUp - Voltar ");
        finish();
        //onBackPressed();
        return true;
    }*/
}
