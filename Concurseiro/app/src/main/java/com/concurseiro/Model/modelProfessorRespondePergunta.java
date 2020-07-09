package com.concurseiro.Model;

/**
 * Created by user4 on 15/09/2017.
 */

public class modelProfessorRespondePergunta {
    public int Id_Pergunta;
    public int Id_Professor;
    public String Resposta;
    public String End_Foto_Resposta;
    public String End_Url_Resposta;


    public modelProfessorRespondePergunta(int id_Pergunta, int id_Professor, String resposta, String end_Foto_Resposta, String end_Url_Resposta) {
        Id_Pergunta = id_Pergunta;
        Id_Professor = id_Professor;
        Resposta = resposta;
        End_Foto_Resposta = end_Foto_Resposta;
        End_Url_Resposta = end_Url_Resposta;
    }

    public int getId_Pergunta() {
        return Id_Pergunta;
    }

    public void setId_Pergunta(int id_Pergunta) {
        Id_Pergunta = id_Pergunta;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public String getResposta() {
        return Resposta;
    }

    public void setResposta(String resposta) {
        Resposta = resposta;
    }

    public String getEnd_Foto_Resposta() {
        return End_Foto_Resposta;
    }

    public void setEnd_Foto_Resposta(String end_Foto_Resposta) {
        End_Foto_Resposta = end_Foto_Resposta;
    }

    public String getEnd_Url_Resposta() {
        return End_Url_Resposta;
    }

    public void setEnd_Url_Resposta(String end_Url_Resposta) {
        End_Url_Resposta = end_Url_Resposta;
    }
}
