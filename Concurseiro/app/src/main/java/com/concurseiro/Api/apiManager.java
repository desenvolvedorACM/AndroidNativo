package com.concurseiro.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelAvaliacaoProfessor;
import com.concurseiro.Model.modelPergunteMestre;
import com.concurseiro.Model.modelProfessorRespondePergunta;
import com.concurseiro.Model.modelUsuarioAssinatura;
import com.concurseiro.Tasks.TaskListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user4 on 16/08/2017.
 */

public class apiManager {

    final static MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    final static OkHttpClient client = new OkHttpClient();


    public static void postVerificaLogin(String email, String password, final TaskListener listener) throws JSONException {
        //String Url = "http://integracaousuario.arrobatecinformatica.com.br/Home/TodosUsuarios";
        String urlMethod = String.format("%s/VerificaLogin/", Constants.URL_SERVER);
        Log.i(Constants.TAG, "urlMethod " + urlMethod);

        JSONObject object = new JSONObject();
        object.put("Email", email);
        object.put("Password", password);

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postVerificaLoginFaceTwitter(String email, final TaskListener listener) throws JSONException {
        //String Url = "http://integracaousuario.arrobatecinformatica.com.br/Home/TodosUsuarios";
        String urlMethod = String.format("%s/verificaloginfacetwitter/", Constants.URL_SERVER);
        Log.i(Constants.TAG, "urlMethod " + urlMethod);

        JSONObject object = new JSONObject();
        object.put("Email", email);
        //object.put("Password", password);

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postInsereUsuarioAssinatura(modelUsuarioAssinatura UsuarioAssinatura, final TaskListener listener) throws JSONException {
        String urlMethod = String.format("%s/insereusuarioassinatura/", Constants.URL_SERVER);
        JSONObject object = new JSONObject();

        if (UsuarioAssinatura != null) {
            object.put("First_name", UsuarioAssinatura.getFirst_name());
            object.put("Last_name", UsuarioAssinatura.getLast_name());
            object.put("Password", UsuarioAssinatura.getPassword());
            object.put("Email", UsuarioAssinatura.getEmail());
            object.put("Facebook_token", UsuarioAssinatura.getFacebook_token());
            object.put("Facebook_id", UsuarioAssinatura.getFacebook_id());
            object.put("Image", UsuarioAssinatura.getImage());
            object.put("Type", UsuarioAssinatura.getType());
            object.put("Profile", UsuarioAssinatura.getProfile());
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postPegaPergunta(int Id_Professor, int Id_Pergunta, final TaskListener listener) throws JSONException {
        String urlMethod = String.format("%s/pegapergunta/", Constants.URL_SERVER);
        JSONObject object = new JSONObject();

        if (Id_Professor != 0) {
            object.put("Id_Professor", Id_Professor);
            object.put("Id_Pergunta", Id_Pergunta);
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postSoltaPergunta(int Id_Pergunta, int Id_Professor, final TaskListener listener) throws JSONException {
        String urlMethod = String.format("%s/soltapergunta/", Constants.URL_SERVER);
        JSONObject object = new JSONObject();

        if (Id_Professor != 0) {
            object.put("Id_Pergunta", Id_Pergunta);
            object.put("Id_Professor", Id_Professor);
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postProfessorRespondePergunta(modelProfessorRespondePergunta professorRespPergunta, final TaskListener listener) throws JSONException {
        String urlMethod = String.format("%s/professorrespondepergunta/", Constants.URL_SERVER);
        JSONObject object = new JSONObject();

        if (professorRespPergunta != null) {

            object.put("Id_Pergunta", professorRespPergunta.getId_Pergunta());
            object.put("Id_Professor", professorRespPergunta.getId_Professor());
            object.put("Resposta", professorRespPergunta.getResposta());
            object.put("End_Foto_Resposta", professorRespPergunta.getEnd_Foto_Resposta());
            object.put("End_Url_Resposta", professorRespPergunta.getEnd_Url_Resposta());
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postPerguntarParaMestre(modelPergunteMestre modelPergunteMestre, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/insereperguntapromestre/", Constants.URL_SERVER);
        JSONObject object = new JSONObject();

        if (modelPergunteMestre != null) {
            object.put("Id_Usuario", modelPergunteMestre.getId_Usuario());
            object.put("Id_Professor", modelPergunteMestre.getId_Professor());
            object.put("Pergunta", modelPergunteMestre.getPergunta());
            object.put("End_Foto_Pergunta", modelPergunteMestre.getEnd_Foto_Pergunta());
            object.put("End_Url_Pergunta", modelPergunteMestre.getEnd_Url_Pergunta());
            object.put("Id_Area_Disciplina", modelPergunteMestre.getId_Area_Disciplina());
            object.put("PrecoPergunta", modelPergunteMestre.getPrecoPergunta());
            object.put("Id_Pessoa", modelPergunteMestre.getId_Pessoa());
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void postInsereAvaliacaoProfessor(modelAvaliacaoProfessor mdataAvaliacaoProfessor, final TaskListener listener) throws JSONException {
        String urlMethod = String.format("%s/insereavaliacaoprofessor/", Constants.URL_SERVER);

        JSONObject object = new JSONObject();

        if (mdataAvaliacaoProfessor != null) {
            object.put("Id_Perg_Resp", mdataAvaliacaoProfessor.getId_Perg_Resp());
            object.put("Id_Professor", mdataAvaliacaoProfessor.getId_Professor());
            object.put("Avaliacao", mdataAvaliacaoProfessor.getAvaliacao());
        }

        RequestBody body = RequestBody.create(JSON, object.toString());

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .post(body)
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTotalAssinates(final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/totalassinantes", Constants.URL_SERVER);

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getDisciplinaDoProfessor(int id_professor, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/disciplinasdoprofessor/%s", Constants.URL_SERVER, id_professor);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodasDisciplinasPai(final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todasdisciplinaspai/", Constants.URL_SERVER);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodasDisciplinasFilhas(long Id_Area_Disciplina_Pai, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todasdisciplinasfilhas/%s", Constants.URL_SERVER, Id_Area_Disciplina_Pai);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodosProfessoresDisciplina(int Id_Area_Disciplina, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todosprofessoresdisciplina/%s", Constants.URL_SERVER, Id_Area_Disciplina);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodasPerguntasUsuario(int Id_usuario, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todasperguntasusuario/%s", Constants.URL_SERVER, Id_usuario);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getPergunteMestreTodasPerguntasSemProfessor(int Id_Professor, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/perguntemestretodasperguntassemprofessor/%s", Constants.URL_SERVER, Id_Professor);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodasPerguntasProfessorDisciplina(int Id_Professor, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todasperguntasprofessordisciplina/%s", Constants.URL_SERVER, Id_Professor);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getTodasPerguntasProfessorEspecifico(int Id_Professor, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/todasperguntasprofessorespecifico/%s", Constants.URL_SERVER, Id_Professor);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getRecuperaPrecoProduto(final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/recuperaprecoproduto/", Constants.URL_SERVER);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getRecuperaTotalMedalhas(int Id_Usuario, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/recuperatotalmedalhas/%s/", Constants.URL_SERVER, Id_Usuario);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getRecuperaRankingUsuario(int Id_Usuario, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/recuperarankingusuario/%s/", Constants.URL_SERVER, Id_Usuario);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getRecuperaTotalTrofeus(int Id_Usuario, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/recuperatotaltrofeus/%s/", Constants.URL_SERVER, Id_Usuario);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getMediaAvaliacaoProfessor(int Id_Usuario, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/mediaavaliacaoprofessor/%s/", Constants.URL_SERVER, Id_Usuario);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    public static void getRecuperaSaldoPessoa(int Id_pessoa, final TaskListener listener) throws JSONException {

        String urlMethod = String.format("%s/recuperasaldopessoa/%s", Constants.URL_SERVER, Id_pessoa);
        Log.i(Constants.TAG, "Url: " + urlMethod);

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .url(urlMethod)
                .get()
                .build();

        client.newCall(request).enqueue(new generalCallBack(listener));
    }

    private static class generalCallBack implements Callback {

        private TaskListener listener;

        public generalCallBack(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, IOException e) {

            if (listener != null) {
                Log.i(Constants.TAG, " getMessage " + e.getMessage());
                Log.i(Constants.TAG, " getStackTrace " + e.getStackTrace());
                Log.i(Constants.TAG, " getCause " + e.getCause());
                listener.onError(e.getLocalizedMessage());
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (listener != null) {

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
                        default:
                            listener.onSuccess(response.body().toString());
                            break;
                    }
                }
            }
        }
    }
}
