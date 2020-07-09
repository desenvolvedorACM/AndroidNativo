package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class LoginApiActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private SecurityPreferences preferences;
    private Conectividade conectividade;
    private ProgressDialog dialog;
    private apiMessages Alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_api);

        preferences = new SecurityPreferences(LoginApiActivity.this);
        conectividade = new Conectividade(LoginApiActivity.this);
        dialog = new ProgressDialog(LoginApiActivity.this);
        Alerts = new apiMessages(LoginApiActivity.this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Logar concurseiros");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        this.mViewHolder.usuario = (EditText) findViewById(R.id.edit_usuario);
        this.mViewHolder.senha = (EditText) findViewById(R.id.edit_senha);
        this.mViewHolder.button_entrar = (Button) findViewById(R.id.button_entrar);

        this.mViewHolder.button_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!conectividade.isNetwork()) {
                    Toast.makeText(LoginApiActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                } else {

                    try {

                        if (mViewHolder.usuario.getText().toString().equals("") || mViewHolder.senha.getText().toString().equals("")) {
                            Alerts.AlertWarning("Login", "Preencha os campos usuário e senha.");
                        } else {

                            dialog.setTitle("Login API");
                            dialog.setMessage("Aguarde...");
                            dialog.show();

                            apiManager.postVerificaLogin(mViewHolder.usuario.getText().toString(), mViewHolder.senha.getText().toString(), new TaskListener() {
                                @Override
                                public void onSuccess(String result) {

                                    final String Type;
                                    final String Profile;
                                    final String isProfessor;
                                    final int Id_Pessoa;
                                    final int Id_Usuario;
                                    final int Id_Professor;
                                    final String Email;
                                    final String First_name;
                                    final String Last_name;
                                    final String Image;

                                    final Boolean Successo;
                                    final String Mensagem;
                                    final String ErroMessage;

                                    try {

                                        Successo = new JSONObject(result).getBoolean("Successo");
                                        Mensagem = new JSONObject(result).getString("Mensagem");
                                        ErroMessage = new JSONObject(result).getString("ErroMessage");

                                        if (Successo) {

                                            Type = new JSONObject(result).getString("Type");
                                            Profile = new JSONObject(result).getString("Profile");
                                            isProfessor = "N";  //new JSONObject(result).getString("Professor");
                                            Id_Pessoa = new JSONObject(result).getInt("Id_Pessoa");
                                            Id_Usuario = new JSONObject(result).getInt("Id_Usuario");
                                            Email = new JSONObject(result).getString("Email");
                                            //First_name = new JSONObject(result).getString("First_name");
                                            //Last_name = new JSONObject(result).getString("Last_name");
                                            //Image = new JSONObject(result).getString("Image");

                                            Log.i(Constants.TAG, "**** LOGADO COM API ***");
                                            Log.i(Constants.TAG, "E-Mail: " + Email);

                                            //DADOS NA SESSÃO.
                                            preferences.storeString(getString(R.string.Email), Email);
                                            //preferences.storeString(getString(R.string.First_name), First_name);
                                            preferences.storeString(getString(R.string.IsProfessor), isProfessor);
                                            //preferences.storeString(getString(R.string.Id_Professor), String.valueOf(Id_Professor));
                                            preferences.storeString(getString(R.string.Id_Usuario), String.valueOf(Id_Usuario));
                                            //preferences.storeString(getString(R.string.Image), Image);


                                            Intent intent = new Intent(LoginApiActivity.this, MenuActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                            finish();

                                        } else {
                                            TraTaErros("Erro autenticação", Mensagem);
                                        }

                                        dialog.dismiss();

                                    } catch (final JSONException e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        TraTaErros("JSONException", e.getLocalizedMessage());
                                    }

                                }

                                @Override
                                public void onError(final String result) {
                                    TraTaErros("onError", result);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        TraTaErros("JSONException", e.getLocalizedMessage());
                        Log.i(Constants.TAG, "onError " + e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(LoginApiActivity.this, LoginCustomActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //onBackPressed();
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

    private class ViewHolder {
        Button button_entrar;
        EditText senha, usuario;
    }
}
