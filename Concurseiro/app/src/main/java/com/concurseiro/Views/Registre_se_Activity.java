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
import com.concurseiro.Model.modelUsuarioAssinatura;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.apiMessages;

public class Registre_se_Activity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private modelUsuarioAssinatura dataUser;
    private apiMessages Alerts;
    private ProgressDialog dialog;
    private Conectividade conectividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrese);

        dialog = new ProgressDialog(Registre_se_Activity.this);
        Alerts = new apiMessages(Registre_se_Activity.this);
        conectividade = new Conectividade(Registre_se_Activity.this);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registre-se");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.mViewHolder.edit_Usuario = (EditText) findViewById(R.id.editloginUsuario);
        this.mViewHolder.edit_Email = (EditText) findViewById(R.id.editEmail);
        this.mViewHolder.edit_Senha = (EditText) findViewById(R.id.editSenha);
        this.mViewHolder.edit_ConfirmeSenha = (EditText) findViewById(R.id.editConfirmeSenha);

        this.mViewHolder.button_cadastro = (Button) findViewById(R.id.btn_enviar_cadastro);
        this.mViewHolder.button_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected()) {

                    dataUser = new modelUsuarioAssinatura();
                    String Erros = "Os segunites campos são de preenchimento obrigatórios \n";

                    if (mViewHolder.edit_Usuario.length() == 0) {
                        Erros += "* campo Usuário";
                        Toast.makeText(Registre_se_Activity.this, Erros, Toast.LENGTH_LONG).show();
                    } else if (mViewHolder.edit_Email.length() == 0) {
                        Erros += "* campo Email";
                    } else if (mViewHolder.edit_Senha.length() == 0) {
                        Erros += "* campo Senha";
                    } else if (mViewHolder.edit_ConfirmeSenha.length() == 0) {
                        Erros += "* campo ConfirmeSenha";
                    } else {

                        dialog.setMessage("Aguarde...");
                        dialog.show();

                        dataUser.setFirst_name(mViewHolder.edit_Usuario.getText().toString());
                        dataUser.setLast_name(mViewHolder.edit_Usuario.getText().toString());
                        dataUser.setEmail(mViewHolder.edit_Email.getText().toString());
                        dataUser.setPassword(mViewHolder.edit_Senha.getText().toString());
                        dataUser.setType("2");
                        dataUser.setProfile("2");

                        //new AsyncTaskRegistrase(Registre_se_Activity.this).execute(dataUser);

                        try {

                            apiManager.postInsereUsuarioAssinatura(dataUser, new TaskListener() {
                                @Override
                                public void onSuccess(String result) {

                                    try {

                                        final Boolean Successo = new JSONObject(result).getBoolean("Successo");
                                        final String Mensagem = new JSONObject(result).getString("Mensagem");

                                        if (Successo) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alerts.AlertInfo("Sucesso", "Usuário registrado com sucesso.");
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        Log.i(Constants.TAG, "onSuccess - JSONException " + e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }

                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(final String result) {
                                    try {

                                        final String Message = new JSONObject(result).getString("Message");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Alerts.AlertError("onError", Message);
                                            }
                                        });

                                        dialog.dismiss();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i(Constants.TAG, "onError - onError " + e.getLocalizedMessage());
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Alerts.AlertError("JSONException ", e.getLocalizedMessage());
                            dialog.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(Registre_se_Activity.this, "Há uma falha na sua conexão com a internet \n não é possivel registrar-se.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyConnection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Registre_se_Activity.this, LoginCustomActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verifyConnection() {

        if (conectividade.checkConnection() == -1) {
            Toast.makeText(Registre_se_Activity.this, "Falha na sua conexão com a internet \n tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {

        if (conectividade.checkConnection() == -1) {
            return false;
        } else {
            return true;
        }
    }

    private class ViewHolder {
        Button button_cadastro;
        EditText edit_Usuario;
        EditText edit_Email;
        EditText edit_Senha;
        EditText edit_ConfirmeSenha;
    }

}
