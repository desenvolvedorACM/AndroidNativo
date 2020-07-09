package com.alexandremarques.heroesdamarvel.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.adapter.rvAdapterPersonagens;
import com.alexandremarques.heroesdamarvel.adapter.rvAdapterStories;
import com.alexandremarques.heroesdamarvel.api.ApiManager;
import com.alexandremarques.heroesdamarvel.constants.Constants;
import com.alexandremarques.heroesdamarvel.model.ModelPersonagem;
import com.alexandremarques.heroesdamarvel.model.ModelStories;
import com.alexandremarques.heroesdamarvel.tasks.TaskListener;
import com.alexandremarques.heroesdamarvel.util.Alerta;
import com.alexandremarques.heroesdamarvel.util.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetalhePersonagemActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private Alerta alertDialog = new Alerta(this);
    private List<ModelStories> listStories;

    private RecyclerView rvStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_personagem);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }

        listStories = new ArrayList<>();
        this.mViewHolder.editTextdescricao = (EditText) findViewById(R.id.edit_descricao);
        this.mViewHolder.textViewerro = (TextView) findViewById(R.id.textView_erro);

        carregaDetalhesPersonagem();
    }

    private void carregaDetalhesPersonagem() {

        Intent intent = getIntent();
        ModelPersonagem Personagemm = (ModelPersonagem) intent.getSerializableExtra(getString(R.string.Personagem));


        if (getIntent().getExtras().containsKey(getString(R.string.Personagem))) {

            ModelPersonagem Personagem = (ModelPersonagem) getIntent()
                    .getExtras().getSerializable(getString(R.string.Personagem));

            //final String descPersonagem = getIntent().getExtras().getString(getString(R.string.DescPersonagem), "");

            mViewHolder.editTextdescricao.setText(Personagem.getDescription());

            new AsyncTaskStories(this)
                    .execute(Personagem.getId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ViewHolder {
        TextView textViewerro;
        EditText editTextdescricao;
    }

    private void TraTaErros(final String Tipo, final String Erro) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(Constants.TAG, "Erro" + Erro);
                alertDialog.AlertError("Erro", Tipo + "\n" + Erro);
            }
        });
    }

    private class AsyncTaskStories extends AsyncTask<Integer, List<ModelStories>, Void> {

        private Context context;
        private ProgressDialog dialog;
        private CustomDialog dialogCustom;

        public AsyncTaskStories(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Integer... idPersonagem) {

            ApiManager.getDetalhePersonagem(idPersonagem[0], new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        int code = new JSONObject(result).getInt("code");
                        String Status = new JSONObject(result).getString("status");

                        if (code == 200) {

                            JSONArray arrayResults = new JSONArray(new JSONObject(result)
                                    .getJSONObject("data")
                                    .getString("results"));

                            for (int i = 0; i < arrayResults.length(); i++) {

                                ModelStories stories = new ModelStories();
                                JSONObject jsonStories = arrayResults.getJSONObject(i);

                                stories.setId(jsonStories.getInt("id"));
                                stories.setDescription(jsonStories.getString("description"));
                                stories.setTitle(jsonStories.getString("title"));

                                listStories.add(stories);
                            }
                        }

                        publishProgress(listStories);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mViewHolder.textViewerro.setText(result);
                        dialogCustom.alertError();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onError(final String result) {
                    Log.i(Constants.TAG, "Erro: " + result);

                    mViewHolder.textViewerro.setText(result);
                    dialogCustom.alertError();
                    dialog.dismiss();
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogCustom = new CustomDialog(context);

            dialog = new ProgressDialog(context);
            dialog.setTitle("Marvel");
            dialog.setMessage("Aguarde, carregando os detalhes...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<ModelStories>... values) {
            super.onProgressUpdate(values);

            if (values[0].size() > 0) {

                rvStories = (RecyclerView) findViewById(R.id.rvStories);
                rvStories.setHasFixedSize(true);
                rvStories.setLayoutManager(new LinearLayoutManager(getBaseContext()));

                rvStories.setAdapter(new rvAdapterStories(listStories));
            }

            dialog.dismiss();
        }
    }
}
