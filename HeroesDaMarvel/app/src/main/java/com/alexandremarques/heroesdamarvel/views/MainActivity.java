package com.alexandremarques.heroesdamarvel.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.adapter.adapterPersonagens;
import com.alexandremarques.heroesdamarvel.adapter.rvAdapterPersonagens;
import com.alexandremarques.heroesdamarvel.api.ApiManager;
import com.alexandremarques.heroesdamarvel.constants.Constants;
import com.alexandremarques.heroesdamarvel.interfaces.IPersonagem;
import com.alexandremarques.heroesdamarvel.interfaces.IRecycleView_OnItemClickListener;
import com.alexandremarques.heroesdamarvel.model.ModelPersonagem;
import com.alexandremarques.heroesdamarvel.tasks.TaskListener;
import com.alexandremarques.heroesdamarvel.util.Alerta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPersonagem, IRecycleView_OnItemClickListener {

    private Alerta Alert;
    private List<ModelPersonagem> listaPersonagens;
    private ModelPersonagem modelPersonagem;
    private ViewHolder mViewHolder = new ViewHolder();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Alert = new Alerta(this);
        listaPersonagens = new ArrayList<>();

        CarregaPersonagens();
    }

    @Override
    public void RecycleView_OnItemClickListener(final ModelPersonagem modelPersonagem) {
        //Bundle bundle = new Bundle();
        //bundle.putSerializable(getString(R.string.DetalhePersonagem), detalhePersonagem);

        //ModelPersonagem personagem = modelPersonagem;

        Intent intent = new Intent(getApplicationContext(), DetalhePersonagemActivity.class);
        intent.putExtra(getString((R.string.Personagem)), modelPersonagem);
        startActivity(intent);

        //intent.putExtra(getString(R.string.IdPersonagem), personagem.getId());
        //intent.putExtra(getString(R.string.DescPersonagem), personagem.getDescription());
    }


    private void CarregaPersonagens() {
        new AsyncPersonagem(this, this)
                .execute();
    }

    @Override
    public void PopulaPersonagens(List<ModelPersonagem> arrayPersonagens) {

        Log.i(Constants.TAG, "onProgressUpdate =>> Total: " + arrayPersonagens.size());

        if (arrayPersonagens.size() > 0) {

            recyclerView = (RecyclerView) findViewById(R.id.lista_personagem);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(new rvAdapterPersonagens(listaPersonagens, this));
        }
    }

    private class AsyncPersonagem extends AsyncTask<Void, List<ModelPersonagem>, Void> {

        private Context context;
        private IPersonagem iPersonagem;
        private ProgressDialog dialog;

        public AsyncPersonagem(Context context, IPersonagem iPersonagem) {
            this.context = context;
            this.iPersonagem = iPersonagem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(context);
            dialog.setTitle("Marvel");
            dialog.setMessage("Aguarde, carregando os personagens da marvel...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiManager.getPersonagens(new TaskListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        int code = new JSONObject(result).getInt("code");
                        String Status = new JSONObject(result).getString("status");

                        if (code == 200) {

                            JSONArray listaPersonagem = new JSONArray(new JSONObject(result)
                                    .getJSONObject("data")
                                    .getString("results"));


                            for (int i = 0; i < listaPersonagem.length(); i++) {

                                modelPersonagem = new ModelPersonagem();
                                JSONObject jsonPersonagem = listaPersonagem.getJSONObject(i); //new JSONObject(listaPersonagem.get(i));

                                String Path = jsonPersonagem.getJSONObject("thumbnail")
                                        .getString("path");

                                String Extension = jsonPersonagem.getJSONObject("thumbnail")
                                        .getString("extension");

                                String basePathUrl = Path + "." + Extension;

                                modelPersonagem.setId(jsonPersonagem.getInt("id"));
                                modelPersonagem.setName(jsonPersonagem.getString("name"));
                                modelPersonagem.setDescription(jsonPersonagem.getString("description"));

                                if (basePathUrl != "") {

                                    try {

                                        //REALIZO O DOWNLOAD DA IMAGEM.
                                        URL Uri = new URL(basePathUrl);
                                        HttpURLConnection connection = (HttpURLConnection) Uri.openConnection();
                                        InputStream stream = connection.getInputStream();

                                        modelPersonagem.setImagePersonagem(BitmapFactory.decodeStream(stream));

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                        TraTaErros("onError => JSONException", e.getLocalizedMessage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        TraTaErros("onError => JSONException", e.getLocalizedMessage());
                                    }
                                }

                                listaPersonagens.add(modelPersonagem);
                            }

                        }

                        publishProgress(listaPersonagens);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        TraTaErros("onError => JSONException", result);
                    }
                }

                @Override
                public void onError(final String result) {
                    dialog.dismiss();
                    TraTaErros("onError => OkHttp", result);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<ModelPersonagem>[] values) {
            super.onProgressUpdate(values);

            Log.i(Constants.TAG, "onProgressUpdate =>> Total: " + values[0].size());
            iPersonagem.PopulaPersonagens(values[0]);
            dialog.dismiss();

        }
    }

    private void TraTaErros(final String Tipo, final String Erro) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Alert.AlertError("Erro", Tipo + "\n" + Erro);
                Log.i(Constants.TAG, "Erro" + Erro);
            }
        });
    }

    private static class ViewHolder {
        ListView listaPersonagem;
    }
}
