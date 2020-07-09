package com.alexandremarques.heroesdamarvel.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.adapter.adapterPersonagens;
import com.alexandremarques.heroesdamarvel.api.ApiManager;
import com.alexandremarques.heroesdamarvel.constants.Constants;
import com.alexandremarques.heroesdamarvel.interfaces.IPersonagem;
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

public class MainActivity extends AppCompatActivity  implements IPersonagem{

    public Alerta Alert;
    public ArrayList<ModelPersonagem> listaPersonagens;
    public ModelPersonagem modelPersonagem;
    public ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Alert = new Alerta(MainActivity.this);
        listaPersonagens = new ArrayList<>();
        this.mViewHolder.listaPersonagem = (ListView) findViewById(R.id.lista_personagem);

        CarregaPersonagens();
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

    private void CarregaPersonagens() {
        new AsyncPersonagem(this, this)
                .execute();
    }

    @Override
    public void PopulaPersonagens(ArrayList<ModelPersonagem> arrayPersonagens) {

        Log.i(Constants.TAG, "onProgressUpdate =>> Total: " + arrayPersonagens.size());

        if (arrayPersonagens.size() > 0) {
            mViewHolder.listaPersonagem = (ListView) findViewById(R.id.lista_personagem);
            mViewHolder.listaPersonagem.setAdapter(new adapterPersonagens(this, listaPersonagens));

            mViewHolder.listaPersonagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ModelPersonagem detalhePersonagem = listaPersonagens.get(position);
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable(getString(R.string.DetalhePersonagem), detalhePersonagem);

                }
            });
        }
    }

    private class AsyncPersonagem extends AsyncTask<Void, ArrayList<ModelPersonagem>, Void> {

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

                        int Code = new JSONObject(result).getInt("code");
                        String Status = new JSONObject(result).getString("status");

                        JSONArray listaPersonagem = new JSONArray(new JSONObject(result)
                                .getJSONObject("data")
                                .getString("results"));


                        for (int i = 0; i < listaPersonagem.length(); i++) {

                            modelPersonagem = new ModelPersonagem();
                            JSONObject jsonDados = new JSONObject(listaPersonagem.get(i).toString());

                            String Path = jsonDados.getJSONObject("thumbnail")
                                    .getString("path");

                            String Extension = jsonDados.getJSONObject("thumbnail")
                                    .getString("extension");

                            String basePathUrl = Path + "." + Extension;

                            modelPersonagem.setId(jsonDados.getInt("id"));
                            modelPersonagem.setName(jsonDados.getString("name"));
                            modelPersonagem.setDescription(jsonDados.getString("description"));

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
        protected void onProgressUpdate(ArrayList<ModelPersonagem>[] values) {
            super.onProgressUpdate(values);

            Log.i(Constants.TAG, "onProgressUpdate =>> Total: " + values[0].size());
            iPersonagem.PopulaPersonagens(values[0]);
            dialog.dismiss();

        }
    }
}
