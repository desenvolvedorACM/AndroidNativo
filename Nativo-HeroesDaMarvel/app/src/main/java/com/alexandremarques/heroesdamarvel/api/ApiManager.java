package com.alexandremarques.heroesdamarvel.api;

import android.util.Log;

import com.alexandremarques.heroesdamarvel.constants.Constants;
import com.alexandremarques.heroesdamarvel.tasks.TaskListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alexandre on 14/03/2018.
 */

public class ApiManager {
    private static OkHttpClient client = new OkHttpClient();
    private static MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public static void getPersonagens(TaskListener listener) {
        String UrlRequest = String.format("%s?ts=1&apikey=e3b2d0626528ef89c05b6b521a37043c&hash=a85f9e85b8ae4057ea57239932756e41", Constants.BaseApiUrl);

        Request request = new Request
                .Builder()
                .header("Content-type", "application/json")
                .url(UrlRequest)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getDetalhePersonagem(int characterId, TaskListener listener) {
        String UrlRequest = String.format("%s/%s?ts=1&apikey=e3b2d0626528ef89c05b6b521a37043c&hash=a85f9e85b8ae4057ea57239932756e41", Constants.BaseApiUrl, characterId);

        Request request = new Request
                .Builder()
                .header("Content-type", "application/json")
                .url(UrlRequest)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }


    public static class generalCallBack implements Callback {

        private TaskListener listener;

        public generalCallBack(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, IOException e) {

            if (listener != null) {
                listener.onError(e.getLocalizedMessage());
                Log.i(Constants.TAG, e.getLocalizedMessage());
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (response.isSuccessful()) {
                listener.onSuccess(response.body().string());
            } else {
                switch (response.code()) {

                    case 500:
                        listener.onError("Erro 500, Contate o suporte tecnico");
                        break;
                    case 504:
                        listener.onError("Erro 504, Contate o suporte tecnico");
                        break;
                    case 400:
                        listener.onError(response.body().string());
                        break;
                    case 409:
                        listener.onError("Missing API Key / Missing Hash");
                        break;
                    case 401:
                        listener.onError("Invalid Referer / Invalid Hash");
                        break;
                    default:
                        listener.onSuccess(response.body().toString());
                        break;
                }
            }
        }
    }
}
