package com.concurseiro.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.concurseiro.Api.apiManager;
import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelDadosTelaPergunteMestre;
import com.concurseiro.Model.modelPergunteMestre;
import com.concurseiro.Model.modelSessionUsuario;
import com.concurseiro.R;
import com.concurseiro.Tasks.TaskListener;
import com.concurseiro.Util.Conectividade;
import com.concurseiro.Util.SecurityPreferences;
import com.concurseiro.Util.apiMessages;

public class PergunteMestreActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewHolder mViewHolder = new ViewHolder();
    private SecurityPreferences preferences;
    private apiMessages Alerts;
    private Conectividade conectividade;
    private modelSessionUsuario sessionUser;

    private ProgressDialog dialog;
    private String Email;
    private String First_name;
    private String Image;
    private String isProfessor;
    private int Type;

    private int Id_Professor;
    private int Id_Usuario;
    private int Id_Pessoa;
    private int Id_Area_Disciplina;
    private String Name_Professor;
    private String UrlImageProfessor;

    private modelPergunteMestre dataPergunteMestre;
    private modelDadosTelaPergunteMestre dataTelaPergunteMestre;
    private static final int FOTO = 0;
    private static final int LINK = 1;
    private static final int FILE_ANEXO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunte_ao_mestre);

        preferences = new SecurityPreferences(PergunteMestreActivity.this);
        conectividade = new Conectividade(PergunteMestreActivity.this);
        Alerts = new apiMessages(PergunteMestreActivity.this);
        sessionUser = new modelSessionUsuario();
        dataTelaPergunteMestre = new modelDadosTelaPergunteMestre();
        dialog = new ProgressDialog(PergunteMestreActivity.this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pergunte ao mestre");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.mViewHolder.image_Professor = (ImageView) findViewById(R.id.image_professor);
        this.mViewHolder.text_Mestre = (TextView) findViewById(R.id.text_mestre);
        this.mViewHolder.edit_Pergunta = (TextView) findViewById(R.id.edit_pergunta);
        this.mViewHolder.button_Perguntar = (Button) findViewById(R.id.button_perguntar);
        this.mViewHolder.img_foto = (ImageView) findViewById(R.id.img_foto);
        this.mViewHolder.img_anexo = (ImageView) findViewById(R.id.img_anexo);
        this.mViewHolder.img_link = (ImageView) findViewById(R.id.img_link);
        this.mViewHolder.image_recupera_foto = (ImageView) findViewById(R.id.img_foto);
        this.mViewHolder.text_escreva_pergunta = (EditText) findViewById(R.id.text_escreva_pergunta);


        this.mViewHolder.img_foto.setOnClickListener(this);
        this.mViewHolder.img_anexo.setOnClickListener(this);
        this.mViewHolder.img_link.setOnClickListener(this);

        mViewHolder.button_Perguntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (mViewHolder.text_escreva_pergunta.getText().length() > 0) {

                        String Pergunta = mViewHolder.text_escreva_pergunta.getText().toString();
                        dataPergunteMestre = new modelPergunteMestre(
                                Id_Usuario,
                                Id_Professor,
                                Id_Pessoa,
                                Pergunta,
                                "",
                                "",
                                Id_Area_Disciplina,
                                0);

                        apiManager.postPerguntarParaMestre(dataPergunteMestre, new TaskListener() {
                            @Override
                            public void onSuccess(String result) {
                                boolean Successo = false;
                                try {

                                    Successo = new JSONObject(result).getBoolean("Successo");
                                    if (Successo) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mViewHolder.text_escreva_pergunta.setText("");
                                                Alerts.AlertInfo("Perguntar", "Pergunta realizada com sucesso.");
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
                    } else {
                        Toast.makeText(PergunteMestreActivity.this, "É necessário preencher o campo pergunta", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    TraTaErros("JSONException", e.getLocalizedMessage());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "PergunteMestreActivity..onStart...");

        //BUSCO OS DADOS NA SESSÂO DO ANDROID.
        if (isConnection() != -1) {
            goCarregaDadosSessionUsuario();

            if (getIntent().getExtras().containsKey(getString(R.string.Id_Pessoa))) {

                //PARAMETROS ENTRE TELA.
                Id_Pessoa = getIntent().getIntExtra(getString(R.string.Id_Pessoa), 0);
                Id_Area_Disciplina = getIntent().getIntExtra(getString(R.string.Id_Area_disciplina), 0);
                Id_Professor = getIntent().getIntExtra(getString(R.string.Id_Professor), 0);
                Name_Professor = getIntent().getStringExtra(getString(R.string.Name_Professor));
                UrlImageProfessor = getIntent().getStringExtra(getString(R.string.UrlImageProfessor));

                if (UrlImageProfessor != null) {
                    Log.i(Constants.TAG, "Tela.UrlImageProfessor " + UrlImageProfessor);

                    new AsyncTaskBaixaImageProfessor(PergunteMestreActivity.this)
                            .execute(UrlImageProfessor);
                }

                //contato = (Contato) extra.getSerializable(getString(R.string.contato));
                //dataTelaPergunteMestre = (modelDadosTelaPergunteMestre) getIntent().getSerializableExtra(getString(R.string.DadosPergunteMestre));

                Log.i(Constants.TAG, "Tela.Id_Pessoa " + Id_Pessoa);
                Log.i(Constants.TAG, "Tela.Id_Area_Disciplina " + Id_Area_Disciplina);
                Log.i(Constants.TAG, "Tela.Id_Professor " + Id_Professor);
                Log.i(Constants.TAG, "Tela.Nome_Professor " + Name_Professor);
                Log.i(Constants.TAG, "Tela.UrlImageProfessor " + UrlImageProfessor);
            }

        } else {
            Toast.makeText(PergunteMestreActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private int isConnection() {
        int temConexao = conectividade.checkConnection();
        if (temConexao == -1) {
            Toast.makeText(PergunteMestreActivity.this, "Você não está conectado à internet.Verifique sua conexão\n e tente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
        return temConexao;
    }

    private void goCarregaDadosSessionUsuario() {

        Email = preferences.getStoreString(getString(R.string.Email));
        First_name = preferences.getStoreString(getString(R.string.First_name));
        isProfessor = preferences.getStoreString(getString(R.string.IsProfessor));
        Id_Professor = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Professor)));
        Id_Usuario = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Usuario)));
        Id_Pessoa = Integer.valueOf(preferences.getStoreString(getString(R.string.Id_Pessoa)));
        Image = preferences.getStoreString(getString(R.string.Image));
        Type = Integer.valueOf(preferences.getStoreString(getString(R.string.Type)));

        sessionUser.setEmail(Email);
        sessionUser.setFirst_name(First_name);
        sessionUser.setIsProfessor(isProfessor);
        sessionUser.setId_Professor(Id_Professor);
        sessionUser.setId_Usuario(Id_Usuario);
        sessionUser.setId_Pessoa(Id_Pessoa);
        sessionUser.setImage(Image);

        Log.i(Constants.TAG, "Sessão.Email " + Email);
        Log.i(Constants.TAG, "Sessão.First_name " + First_name);
        Log.i(Constants.TAG, "Sessão.isProfessor " + isProfessor);
        Log.i(Constants.TAG, "Sessão.Id_Professor " + Id_Professor);
        Log.i(Constants.TAG, "Sessão.Id_Usuario " + Id_Usuario);
        Log.i(Constants.TAG, "Sessão.Id_Pessoa " + Id_Pessoa);
        Log.i(Constants.TAG, "Sessão.Image " + Image);
        Log.i(Constants.TAG, "Sessão.Type " + Type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(Constants.TAG, "onOptionsItemSelected " + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PergunteMestreActivity.this, EscolherMestreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.img_foto) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, FOTO);

        } else if (Id == R.id.img_link) {

        } else if (Id == R.id.img_anexo) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "selecionar arquivo"), FILE_ANEXO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FOTO) {

                Log.i(Constants.TAG, "FOTO...");
                Uri foto = data.getData();
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(foto));

                //Log.i(Constants.TAG, "Path Image: " + file.getPath());


            } else if (requestCode == FILE_ANEXO) {
                Uri selectedImageUri = data.getData();
                Bitmap _foto = (Bitmap) data.getExtras().get("data");
                Log.i(Constants.TAG, "File: " + selectedImageUri);

                this.mViewHolder.image_recupera_foto = (ImageView) findViewById(R.id.img_foto);
                this.mViewHolder.image_recupera_foto.setImageBitmap(_foto);


            }
        }
    }

    private class ViewHolder {
        Button button_Perguntar;
        TextView edit_Pergunta;
        TextView text_Mestre;
        ImageView image_Professor, img_foto, img_anexo, img_link, image_recupera_foto;
        EditText text_escreva_pergunta;
    }

    private class AsyncTaskBaixaImageProfessor extends AsyncTask<String, Bitmap, Void> {

        private Context context;

        public AsyncTaskBaixaImageProfessor(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Carregando a imagem do professor");
            dialog.setMessage("Aguarde..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            URL Uri = null;
            try {

                Uri = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) Uri.openConnection();
                InputStream stream = connection.getInputStream();
                Bitmap ImagemProf = BitmapFactory.decodeStream(stream);
                publishProgress(ImagemProf);

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
            if (values != null) {
                mViewHolder.image_Professor.setImageBitmap(values[0]);
                mViewHolder.text_Mestre.setText(Name_Professor);
                dialog.dismiss();
            }
        }
    }
}
