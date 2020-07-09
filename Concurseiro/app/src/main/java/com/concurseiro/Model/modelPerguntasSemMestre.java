package com.concurseiro.Model;

import android.graphics.Bitmap;

/**
 * Created by user4 on 15/09/2017.
 */

public class modelPerguntasSemMestre {
    public int Id_Pergunta;
    public int Id_Professor;
    public String First_name;
    public String Last_name;
    public String Image;
    public Bitmap ImageAluno;
    public String DataHoraP;
    public String DataHoraR;
    public String Pergunta;
    public String Resposta;
    public String Concluida;

    public modelPerguntasSemMestre() {
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

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Bitmap getImageAluno() {
        return ImageAluno;
    }

    public void setImageAluno(Bitmap imageAluno) {
        ImageAluno = imageAluno;
    }

    public String getDataHoraP() {
        return DataHoraP;
    }

    public void setDataHoraP(String dataHoraP) {
        DataHoraP = dataHoraP;
    }

    public String getDataHoraR() {
        return DataHoraR;
    }

    public void setDataHoraR(String dataHoraR) {
        DataHoraR = dataHoraR;
    }

    public String getPergunta() {
        return Pergunta;
    }

    public void setPergunta(String pergunta) {
        Pergunta = pergunta;
    }

    public String getResposta() {
        return Resposta;
    }

    public void setResposta(String resposta) {
        Resposta = resposta;
    }

    public String getConcluida() {
        return Concluida;
    }

    public void setConcluida(String concluida) {
        Concluida = concluida;
    }
}
