package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelSessionUsuario;
import com.concurseiro.Model.modelUsuarioAssinatura;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;


public class LoginCustomActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewHolder mViewHolder = new ViewHolder();
    private CallbackManager callbackManager;
    private TwitterAuthClient mTwitterAuthClient;
    private SecurityPreferences preferences;
    private Conectividade conectividade;
    private apiMessages Alerts;
    //private ProgressDialog dialog;
    private modelUsuarioAssinatura dataUser;
    private modelSessionUsuario sessionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);

        getSupportActionBar().hide();


        this.mViewHolder.button_facebook = (Button) findViewById(R.id.button_facebook);
        this.mViewHolder.button_twiter = (Button) findViewById(R.id.button_twitter);
        this.mViewHolder.button_login_mail = (Button) findViewById(R.id.button_login_mail);

        this.mViewHolder.text_titulo_concurseiro = (TextView) findViewById(R.id.text_titulo_concurseiro);
        this.mViewHolder.text_totalconcurso = (TextView) findViewById(R.id.text_totalconcurso);


        this.mViewHolder.button_facebook.setOnClickListener(this);
        this.mViewHolder.button_twiter.setOnClickListener(this);
        this.mViewHolder.button_login_mail.setOnClickListener(this);

        this.mViewHolder.text_registrase = (TextView) findViewById(R.id.text_registrase);
        this.mViewHolder.text_registrase.setOnClickListener(this);

        mTwitterAuthClient = new TwitterAuthClient();
        preferences = new SecurityPreferences(LoginCustomActivity.this);
        conectividade = new Conectividade(LoginCustomActivity.this);
        Alerts = new apiMessages(LoginCustomActivity.this);
        //dialog = new ProgressDialog(LoginCustomActivity.this);

        Log.i(Constants.TAG, "LoginCustomActivity-onCreate...");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(Constants.TAG, "LoginCustomActivity-onStart...");
        if (isConnection() != -1) {
            verificaUsuarioLogado();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "LoginCustomActivity-onResume...");
    }


    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(LoginCustomActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

        return temConexao;
    }

    private void verificaUsuarioLogado() {

        String Email = preferences.getStoreString(getString(R.string.Email));
        Log.i(Constants.TAG, "Email da pessoa logada... " + Email);

        //SE JÁ TEM USUÁRIO LOGADO.
        if (!Email.equals("")) {
            goMenuScreen();
        } else {
            new AsyncTaskTotalAssinantes(LoginCustomActivity.this)
                    .execute();
        }
    }

    private void goMenuScreen() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goLoginAPI() {

        //DADOS PARA A PROXIMA TELA.
        Intent intent = new Intent(LoginCustomActivity.this, LoginApiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goLoginTwitter() {

        Log.i(Constants.TAG, "goLoginTwitter .........");
        mTwitterAuthClient.authorize(this, new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

                //TOKEN
                TwitterAuthToken authToken = session.getAuthToken();
                final String token = authToken.token;
                final String secret = authToken.secret;

                //DATA
                final Long userId = session.getUserId();
                final String Email = session.getUserName();

                TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, true)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {

                                String body = result.response.body().toString();
                                User user = result.data;

                                try {

                                    //VERIFICA SE O EMAIL DO TWITTER JA EXISTE NO SISTEMA
                                    //dialog.setMessage("Aguarde...");
                                    //dialog.show();

                                    Log.i(Constants.TAG, "**** LOGADO COM TWITTER ***");
                                    Log.i(Constants.TAG, "userId: " + userId);
                                    Log.i(Constants.TAG, "user.name " + user.name);
                                    Log.i(Constants.TAG, "user.email " + user.email);
                                    Log.i(Constants.TAG, "profileImageUrl " + user.profileImageUrl);

                                    //Log.i(Constants.TAG, "des" + user.description);
                                    //Log.d("followers ", String.valueOf(user.followersCount));
                                    //Log.d("createdAt", user.createdAt);

                                    dataUser = new modelUsuarioAssinatura();
                                    dataUser.setFirst_name(user.name);
                                    dataUser.setLast_name(user.name);
                                    dataUser.setEmail(user.email);
                                    dataUser.setFacebook_token(token);
                                    dataUser.setFacebook_id(String.valueOf(userId));
                                    dataUser.setImage(user.profileImageUrl);
                                    dataUser.setPassword("");
                                    dataUser.setType("2");
                                    dataUser.setProfile("3");

                                    goSessionUser(dataUser.getEmail());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i(Constants.TAG, "Exception " + e.getLocalizedMessage());
                                }

                            }

                            @Override
                            public void failure(final TwitterException exception) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Alerts.AlertInfo("failure", exception.getLocalizedMessage());
                                        Log.i(Constants.TAG, "failure " + exception.getLocalizedMessage());
                                    }
                                });
                            }
                        });


            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                Toast.makeText(LoginCustomActivity.this, R.string.login_error + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goSessionUser(String Email) throws JSONException {

        final Intent intent = new Intent(LoginCustomActivity.this, MenuActivity.class);

        sessionUser = new modelSessionUsuario();
        apiManager.postVerificaLoginFaceTwitter(Email, new TaskListener() {
            @Override
            public void onSuccess(String result) {
                final Boolean Successo;
                final String Mensagem;
                final String Type;
                final String Profile;
                final String isProfessor;
                final int Id_Pessoa;
                final int Id_Usuario;
                final int Id_Professor;
                final String Email;
                final String First_name;
                final String Image;

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

                        sessionUser.setType(Type);
                        sessionUser.setProfile(Profile);
                        sessionUser.setIsProfessor(isProfessor);
                        sessionUser.setId_Pessoa(Id_Pessoa);
                        sessionUser.setId_Usuario(Id_Usuario);
                        sessionUser.setId_Professor(Id_Professor);
                        sessionUser.setEmail(Email);
                        sessionUser.setFirst_name(First_name);
                        sessionUser.setImage(Image);


                        //DADOS NA SESSÃO.
                        preferences.storeString(getString(R.string.Email), Email);
                        preferences.storeString(getString(R.string.First_name), First_name);
                        preferences.storeString(getString(R.string.IsProfessor), isProfessor);
                        preferences.storeString(getString(R.string.Id_Pessoa), String.valueOf(Id_Pessoa));
                        preferences.storeString(getString(R.string.Id_Professor), String.valueOf(Id_Professor));
                        preferences.storeString(getString(R.string.Id_Usuario), String.valueOf(Id_Usuario));
                        preferences.storeString(getString(R.string.Image), Image);

                        //intent.putExtra(getString(R.string.modelSessionUsuario), sessionUser);
                        Intent intent = new Intent(LoginCustomActivity.this, MenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        goInsereUsuarioAssinatura();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                    Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onError(final String result) {
                TraTaErros("onError", result);
            }
        });

    }

    private void goLoginFaceBook() {

        LoginManager.getInstance().logInWithReadPermissions(LoginCustomActivity.this, Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {

                                // Application code
                                if (response.getError() != null) {
                                    Toast.makeText(LoginCustomActivity.this, R.string.login_error + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {

                                    try {


                                        //By Profile Class
                                        Profile profile = Profile.getCurrentProfile();
                                        if (profile != null) {

                                            // get email and id of the user
                                            String Id = me.optString("id");
                                            String Email = me.optString("email");

                                            Log.i(Constants.TAG, "**** LOGADO COM FACEBOOK ***");
                                            Log.i(Constants.TAG, "E-Mail: " + Email);
                                            Log.i(Constants.TAG, "Id: " + Id);


                                            String facebook_id = profile.getId();
                                            String f_name = profile.getFirstName();
                                            String m_name = profile.getMiddleName();
                                            String l_name = profile.getLastName();
                                            String full_name = profile.getName();
                                            String profile_image = profile.getProfilePictureUri(400, 400).toString();

                                            Log.i(Constants.TAG, "E-facebook_id: " + facebook_id);
                                            Log.i(Constants.TAG, "f_name: " + f_name);
                                            Log.i(Constants.TAG, "m_name: " + m_name);
                                            Log.i(Constants.TAG, "l_name: " + l_name);
                                            Log.i(Constants.TAG, "full_name: " + full_name);
                                            Log.i(Constants.TAG, "profile_image: " + profile_image);

                                            dataUser = new modelUsuarioAssinatura();
                                            dataUser.setFirst_name(f_name);
                                            dataUser.setLast_name(l_name);
                                            dataUser.setEmail(Email);
                                            dataUser.setFacebook_id(facebook_id);

                                            final AccessToken token = AccessToken.getCurrentAccessToken();
                                            if (token != null) {
                                                dataUser.setFacebook_token(token.getToken());
                                            }

                                            dataUser.setImage(profile_image);
                                            dataUser.setPassword("");
                                            dataUser.setType("2");
                                            dataUser.setProfile("3");

                                            goSessionUser(dataUser.getEmail());

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i(Constants.TAG, "Exception " + e.getLocalizedMessage());
                                    }
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email");

                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                Toast.makeText(LoginCustomActivity.this, R.string.login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginCustomActivity.this, R.string.login_error + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int _id = v.getId();
        if (_id == R.id.text_registrase) {
            if (isConnection() != -1) {
                Intent intent = new Intent(LoginCustomActivity.this, Registre_se_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        } else if (_id == R.id.button_facebook) {

            if (isConnection() != -1) {
                //AUTENTICAÇÃO DO FACEBOOK.
                goLoginFaceBook();
            }

        } else if (_id == R.id.button_twitter) {

            if (isConnection() != -1) {
                //AUTENTICAÇÃO DO TWITER.
                goLoginTwitter();
            }
        } else {
            goLoginAPI();
        }
    }


    private void goInsereUsuarioAssinatura() {

        try {

            apiManager.postInsereUsuarioAssinatura(dataUser, new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        final Boolean Successo = new JSONObject(result).getBoolean("Successo");
                        final String Mensagem = new JSONObject(result).getString("Mensagem");

                        if (Successo) {
                            // Alerts.AlertInfo("Sucesso", "Usuário registrado com sucesso.");
                            Log.i(Constants.TAG, "Usuário registrado com sucesso");
                            Intent intent = new Intent(LoginCustomActivity.this, MenuActivity.class);
                            //intent.putExtra(getString(R.string.Email), dataUser.getEmail());

                            preferences.storeString(getString(R.string.Email), dataUser.getEmail());
                            preferences.storeString(getString(R.string.Id_Pessoa), String.valueOf(0));
                            preferences.storeString(getString(R.string.Id_Usuario), String.valueOf(0));
                            preferences.storeString(getString(R.string.Id_Professor), String.valueOf(0));
                            preferences.storeString(getString(R.string.IsProfessor), "N");

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    } catch (final JSONException e) {
                        e.printStackTrace();
                        Log.i(Constants.TAG, "onSuccess - JSONException " + e.getLocalizedMessage());
                    }

                }

                @Override
                public void onError(final String result) {
                    TraTaErros("onError-postInsereUsuarioAssinatura", result);
                }
            });

        } catch (final JSONException e) {
            e.printStackTrace();
            TraTaErros("JSONException-postInsereUsuarioAssinatura", e.getLocalizedMessage());
            Log.i(Constants.TAG, "JSONException " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
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
        Button button_login_mail, button_facebook, button_twiter;
        TextView text_registrase;
        TextView text_totalconcurso;
        TextView text_titulo_concurseiro;
    }

    private class AsyncTaskTotalAssinantes extends AsyncTask<Void, Integer, Void> {
        private Context context;
        private ProgressDialog dialog;

        public AsyncTaskTotalAssinantes(Context context) {
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

            loadTotalAssinantes();
            return null;
        }

        private void loadTotalAssinantes() {

            try {

                apiManager.getTotalAssinates(new TaskListener() {

                    @Override
                    public void onSuccess(String result) {

                        final Boolean Successo;
                        final Integer TotalAssinantes;

                        try {

                            Successo = new JSONObject(result).getBoolean("Successo");
                            TotalAssinantes = new JSONObject(result).getInt("TotalAssinantes");

                            if (Successo) {
                                publishProgress(TotalAssinantes);
                            }

                        } catch (final JSONException e) {
                            e.printStackTrace();
                            TraTaErros("JSONException", e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onError(final String result) {
                        TraTaErros("onError", result);
                    }
                });

            } catch (final JSONException e) {
                e.printStackTrace();
                TraTaErros("JSONException", e.getLocalizedMessage());
            }

        }

        @Override
        protected void onPostExecute(Void value) {
            super.onPostExecute(value);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.dismiss();

            Integer TotalAssinantes = values[0];
            Log.i(Constants.TAG, "Total de assinantes " + TotalAssinantes);

            if (TotalAssinantes < 10) {
                mViewHolder.text_totalconcurso.setText(String.valueOf("0" + TotalAssinantes));
            } else {
                mViewHolder.text_totalconcurso.setText(String.valueOf(TotalAssinantes));
            }

        }
    }
}
