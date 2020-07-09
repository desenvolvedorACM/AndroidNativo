package com.concurseiro.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import com.concurseiro.Constants.Constants;
import com.concurseiro.R;

public class LoginActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        this.mViewHolder.button_facebook = (LoginButton) findViewById(R.id.button_facebook);
        this.mViewHolder.button_twiter = (TwitterLoginButton) findViewById(R.id.button_twiter);
        this.mViewHolder.button_login_api = (Button) findViewById(R.id.button_login_mail);
        this.mViewHolder.text_registrase = (TextView) findViewById(R.id.text_registrase);

        this.mViewHolder.text_registrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registre_se_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //AUTENTICAÇÃO DO FACEBOOK.
        goLoginFaceBook();


        //AUTENTICAÇÃO DO TWITER.
        goLoginTwiter();


        //AUTENTICAÇÃO NA API DO CONCURSEIRO.
        mViewHolder.button_login_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginAPI();
            }
        });

    }

    private void goLoginAPI() {

        //DADOS PARA A PROXIMA TELA.
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("TipoLogin", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goLoginTwiter() {
        this.mViewHolder.button_twiter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

                //TOKEN
                TwitterAuthToken authToken = session.getAuthToken();
                final String token = authToken.token;
                final String secret = authToken.secret;

                //DATA
                final Long userId = session.getUserId();
                final String Email = session.getUserName();

                Log.i(Constants.TAG, "**** LOGADO COM TWITTER ***");
                Log.i(Constants.TAG, "userId: " + userId);
                Log.i(Constants.TAG, "Nome :" + Email);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "failure " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goLoginFaceBook() {

        this.mViewHolder.button_facebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();
        this.mViewHolder.button_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {

                                // Application code
                                if (response.getError() != null) {
                                    Toast.makeText(LoginActivity.this, "Erro " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {

                                    // get email and id of the user
                                    String Id = me.optString("id");
                                    String Email = me.optString("email");

                                    Log.i(Constants.TAG, "E-Mail: " + Email);
                                    Log.i(Constants.TAG, "Id: " + Id);

                                    //DADOS PARA A PROXIMA TELA.
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                                    intent.putExtra("Id", Id);
                                    intent.putExtra("Email", Email);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
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
                Toast.makeText(LoginActivity.this, R.string.login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, R.string.login_error + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        this.mViewHolder.button_twiter.onActivityResult(requestCode, resultCode, data);
    }

    private class ViewHolder {
        LoginButton button_facebook;
        TwitterLoginButton button_twiter;
        Button button_login_api;
        TextView text_registrase;
    }
}
