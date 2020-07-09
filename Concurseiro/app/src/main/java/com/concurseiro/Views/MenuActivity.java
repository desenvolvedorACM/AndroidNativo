package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //VAR UTEIS.
    private SecurityPreferences preferences;
    private ViewHolder mViewHolder = new ViewHolder();
    private apiMessages Alerts;
    private Conectividade conectividade;
    private modelSessionUsuario sessionUser;

    //DADOS SESSION.
    private int Id_Professor;
    private int Id_Usuario;
    private int Id_Pessoa;
    private int Type;
    private String Email;
    private String First_name;
    private String Image;
    private String isProfessor;
    private Double saldoPessoa;
    private String Uf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Bundle extra = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Categorias");
        }


        preferences = new SecurityPreferences(MenuActivity.this);
        conectividade = new Conectividade(MenuActivity.this);
        Alerts = new apiMessages(MenuActivity.this);
        sessionUser = new modelSessionUsuario();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        this.mViewHolder.foto_perfil = (ImageView) findViewById(R.id.image_foto_perfil);
        this.mViewHolder.foto_bandeira_sp = (ImageView) findViewById(R.id.image_bandeira_sp);
        this.mViewHolder.text_nome = (TextView) findViewById(R.id.text_nome_sobre_nome);
        this.mViewHolder.text_estado = (TextView) findViewById(R.id.text_estado);
        this.mViewHolder.txt_medalha = (TextView) findViewById(R.id.text_medalha);
        this.mViewHolder.txt_trofeu = (TextView) findViewById(R.id.text_trofeu);


        this.mViewHolder.btn_assine_portal = (ImageView) findViewById(R.id.button_assine_portal);
        this.mViewHolder.btn_assine_portal.setOnClickListener(this);

        this.mViewHolder.btn_obtenha_credito = (ImageView) findViewById(R.id.button_obtenha_credito);
        this.mViewHolder.btn_obtenha_credito.setOnClickListener(this);


        this.mViewHolder.button_pergunte_ao_mestre = (Button) findViewById(R.id.button_pergunte_ao_mestre);
        this.mViewHolder.button_pergunte_ao_mestre.setOnClickListener(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "MenuActivity onStart...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() != -1) {
            String Email = preferences.getStoreString(getString(R.string.Email));
            //SE NÃO ESTIVER LOGADO.
            if (!Email.equals("")) {
                getSessionUsuario();

                new AsyncTaskSessionUsuario(MenuActivity.this)
                        .execute();
            } else {
                goLoginScreen();
            }
        }
    }

    private void goRecuperaSaldoPessoa() {

        try {
            apiManager.getRecuperaSaldoPessoa(Id_Pessoa, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    boolean Successo = false;
                    try {
                        Successo = new JSONObject(result).getBoolean("Successo");
                        if (Successo) {
                            saldoPessoa = new JSONObject(result).getDouble("Saldo");
                            Log.i(Constants.TAG, "Saldo da pessoa: " + saldoPessoa);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        TraTaErros("getRecuperaSaldoPessoa-JSONException", e.getLocalizedMessage());
                    }

                }

                @Override
                public void onError(String result) {
                    TraTaErros("getRecuperaSaldoPessoa-onError", result);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            TraTaErros("getRecuperaSaldoPessoa-JSONException", e.getLocalizedMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "MenuActivity onResume...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() == -1) {
            Toast.makeText(MenuActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        //getIntent().getExtras().containsKey(getString(R.string.Email))
        //Bundle extra = getIntent().getExtras();
        /*if (getIntent().getExtras().containsKey(getString(R.string.modelSessionUsuario))) {
            sessionUser = (modelSessionUsuario) getIntent().getExtras().getSerializable(getString(R.string.modelSessionUsuario));
        }*/
    }


    private void getSessionUsuario() {

        try {

            Email = preferences.getStoreString(getString(R.string.Email));
            Log.i(Constants.TAG, "Email do usuário logado.. " + Email);
            //sessionUser.setEmail(getIntent().getExtras().getString(getString(R.string.Email)));

            apiManager.postVerificaLoginFaceTwitter(Email, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    final Boolean Successo;
                    final String Mensagem;
                    final String Type;
                    final String Profile;

                    try {

                        Successo = new JSONObject(result).getBoolean("Successo");
                        Mensagem = new JSONObject(result).getString("Mensagem");

                        if (Successo) {
                            Type = new JSONObject(result).getString("Type");
                            Profile = new JSONObject(result).getString("Profile");
                            isProfessor = new JSONObject(result).getString("isProfessor");
                            Id_Pessoa = new JSONObject(result).getInt("Id_Pessoa");
                            Id_Usuario = new JSONObject(result).getInt("Id_Usuario");
                            Id_Professor = new JSONObject(result).getInt("Id_Professor");
                            Email = new JSONObject(result).getString("Email");
                            First_name = new JSONObject(result).getString("First_name");
                            Image = new JSONObject(result).getString("Image");
                            Uf = new JSONObject(result).getString("UF");


                            //DADOS NA SESSÃO.
                            preferences.storeString(getString(R.string.Email), Email);
                            preferences.storeString(getString(R.string.First_name), First_name);
                            preferences.storeString(getString(R.string.IsProfessor), isProfessor);
                            preferences.storeString(getString(R.string.Id_Pessoa), String.valueOf(Id_Pessoa));
                            preferences.storeString(getString(R.string.Id_Professor), String.valueOf(Id_Professor));
                            preferences.storeString(getString(R.string.Id_Usuario), String.valueOf(Id_Usuario));
                            preferences.storeString(getString(R.string.Image), Image);
                            preferences.storeString(getString(R.string.Type), Type);
                            preferences.storeString(getString(R.string.Profile), Profile);

                            Log.i(Constants.TAG, "getSessionUsuario.Email: " + Email);
                            Log.i(Constants.TAG, "getSessionUsuario.Id_Pessoa: " + Id_Pessoa);
                            Log.i(Constants.TAG, "getSessionUsuario.Id_Usuario: " + Id_Usuario);
                            Log.i(Constants.TAG, "getSessionUsuario.Id_Professor: " + Id_Professor);
                            Log.i(Constants.TAG, "getSessionUsuario.First_name: " + First_name);
                            Log.i(Constants.TAG, "getSessionUsuario.UF: " + Uf);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mViewHolder.text_nome = (TextView) findViewById(R.id.text_nome_sobre_nome);
                                    mViewHolder.text_estado = (TextView) findViewById(R.id.text_estado);
                                    mViewHolder.foto_bandeira_sp = (ImageView) findViewById(R.id.image_bandeira_sp);

                                    mViewHolder.text_nome.setText(First_name);
                                    mViewHolder.text_estado.setText(Uf);

                                    mViewHolder.foto_bandeira_sp.setImageResource(goBuscaBandeiraEstado(Uf));
                                }
                            });

                            goRecuperaTotalMedalhas();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        TraTaErros("JSONException", e.getLocalizedMessage());
                        Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    TraTaErros("onError", result);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
            TraTaErros("JSONException", e.getLocalizedMessage());
            Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
        }
    }

    private void goRecuperaRankingUsuario() {

        try {
            apiManager.getRecuperaRankingUsuario(Id_Usuario, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {
                        boolean Sucesso = new JSONObject(result).getBoolean("Successo");
                        int TotalPontos = new JSONObject(result).getInt("TotalPontos");

                        if (Sucesso) {
                            Log.i(Constants.TAG, "TotalPontos: " + TotalPontos);
                            //mViewHolder.txt_trofeu.setText(TotalTrofeus);

                            goRecuperaSaldoPessoa();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        TraTaErros("goRecuperaRankingUsuario=>JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    TraTaErros("goRecuperaRankingUsuario=>onError", result);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            TraTaErros("goRecuperaRankingUsuario=>JSONException", e.getLocalizedMessage());
        }
    }

    private void goRecuperaTotalTrofeus() {
        try {
            apiManager.getRecuperaTotalTrofeus(Id_Usuario, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {
                        boolean Sucesso = new JSONObject(result).getBoolean("Successo");
                        final int TotalTrofeus = new JSONObject(result).getInt("TotalTrofeus");

                        if (Sucesso) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mViewHolder.txt_trofeu = (TextView) findViewById(R.id.text_trofeu);
                                    mViewHolder.txt_trofeu.setText(String.valueOf(TotalTrofeus));
                                }
                            });

                            goRecuperaRankingUsuario();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        TraTaErros("goRecuperaTotalTrofeus=>JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    TraTaErros("goRecuperaTotalTrofeus=>onError", result);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            TraTaErros("goRecuperaTotalTrofeus=>JSONException", e.getLocalizedMessage());
        }
    }

    private void goRecuperaTotalMedalhas() {

        try {
            apiManager.getRecuperaTotalMedalhas(Id_Usuario, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {
                        boolean Sucesso = new JSONObject(result).getBoolean("Successo");
                        final int TotalMedalhas = new JSONObject(result).getInt("TotalMedalhas");

                        if (Sucesso) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mViewHolder.txt_medalha = (TextView) findViewById(R.id.text_medalha);
                                    mViewHolder.txt_medalha.setText(String.valueOf(TotalMedalhas));
                                }
                            });

                            goRecuperaTotalTrofeus();
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
        } catch (JSONException e) {
            e.printStackTrace();
            TraTaErros("JSONException", e.getLocalizedMessage());
        }
    }

    private int goBuscaBandeiraEstado(String uf) {

        int resId;
        switch (uf) {
            case "RS":
                resId = R.drawable.ic_rs;
                break;
            case "AC":
                resId = R.drawable.ic_ac;
                break;
            case "AL":
                resId = R.drawable.ic_al;
                break;
            case "AM":
                resId = R.drawable.ic_am;
                break;
            case "AP":
                resId = R.drawable.ic_ap;
                break;
            case "BA":
                resId = R.drawable.ic_ba;
                break;
            case "CE":
                resId = R.drawable.ic_ce;
                break;
            case "DF":
                resId = R.drawable.ic_df;
                break;
            case "ES":
                resId = R.drawable.ic_es;
                break;
            case "GO":
                resId = R.drawable.ic_go;
                break;
            case "MA":
                resId = R.drawable.ic_ma;
                break;
            case "MG":
                resId = R.drawable.ic_mg;
                break;
            case "MS":
                resId = R.drawable.ic_ms;
                break;
            case "MT":
                resId = R.drawable.ic_mt;
                break;
            case "PA":
                resId = R.drawable.ic_pa;
                break;
            case "PB":
                resId = R.drawable.ic_pb;
                break;
            case "PE":
                resId = R.drawable.ic_pe;
                break;
            case "PI":
                resId = R.drawable.ic_pi;
                break;
            case "PR":
                resId = R.drawable.ic_pr;
                break;
            case "RN":
                resId = R.drawable.ic_rn;
                break;
            case "RO":
                resId = R.drawable.ic_ro;
                break;
            case "RR":
                resId = R.drawable.ic_rr;
                break;
            case "SC":
                resId = R.drawable.ic_sc;
                break;
            case "SE":
                resId = R.drawable.ic_se;
                break;
            case "SP":
                resId = R.drawable.ic_sp;
                break;
            case "TO":
                resId = R.drawable.ic_to;
                break;
            default:
                resId = R.drawable.ic_sp;

        }

        return resId;
    }

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(MenuActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        return temConexao;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {

            Intent intent = new Intent(this, ListaPergSemMestreProfessorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_pagamento) {

        } else if (id == R.id.nav_mensagens) {

        } else if (id == R.id.nav_notificacoes) {

        } else if (id == R.id.nav_config) {

        } else if (id == R.id.nav_logout) {

            //DESLOGA FACE E EM-MAIL.
            preferences.storeString(getString(R.string.Email), "");
            LoginManager.getInstance().logOut();

            TwitterCore.getInstance().getSessionManager().clearActiveSession();

            Intent intent = new Intent(MenuActivity.this, LoginCustomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginCustomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_pergunte_ao_mestre) {

            //SE FOR ALUNO, PERGUNTAS.
            if (isProfessor.contains("S")) {
                Intent intent = new Intent(MenuActivity.this, ListaPergSemMestreProfessorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Log.i(Constants.TAG, "Id_Pessoa: " + Id_Pessoa + "--" + "SaldoPessoa: " + saldoPessoa);
                if (Id_Pessoa > 0 && saldoPessoa > 0) {
                    Intent intent = new Intent(MenuActivity.this, EscolherMestreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MenuActivity.this, ObterCreditosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        } else if (id == R.id.button_obtenha_credito) {

            Intent intent = new Intent(MenuActivity.this, ObterCreditosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.button_assine_portal) {
            Alerts.AlertWarning("Assinar portal", "Funcionalidade não disponivel ainda!");
        }
    }

    private class AsyncTaskSessionUsuario extends AsyncTask<Void, Bitmap, Void> {

        private Context context;
        private ProgressDialog dialog;

        public AsyncTaskSessionUsuario(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Aguarde...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final String ImagemPerfil;

            try {

                //Type = 1 API
                //Type = 3 PORTAL
                //Type = 3 APP
                Image = preferences.getStoreString(getString(R.string.Image));
                Log.i(Constants.TAG, "Url imagem a baixar :" + Image);

                if (!Image.contains("http")) {
                    ImagemPerfil = Constants.URL_SITE + Image;
                } else {
                    ImagemPerfil = Image;
                }

                URL uri = new URL(ImagemPerfil);
                HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                InputStream stream = connection.getInputStream();

                publishProgress(BitmapFactory.decodeStream(stream));
                dialog.dismiss();

            } catch (IOException e) {
                e.printStackTrace();
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
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);

            mViewHolder.foto_perfil = (ImageView) findViewById(R.id.image_foto_perfil);
            dialog.dismiss();

            if (values != null) {
                //mViewHolder.foto_perfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mViewHolder.foto_perfil.setImageBitmap(values[0]);
            }
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

    private class ViewHolder {
        ImageView foto_perfil, foto_bandeira_sp, btn_obtenha_credito, btn_assine_portal;
        Button button_pergunte_ao_mestre;
        TextView text_nome, text_estado, txt_medalha, txt_trofeu;
    }
}
