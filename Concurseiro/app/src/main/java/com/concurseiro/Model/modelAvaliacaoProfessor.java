package com.concurseiro.Model;

/**
 * Created by Alexandre on 12/11/2017.
 */

public class modelAvaliacaoProfessor {
    public int Id_Perg_Resp;
    public int Id_Professor;
    public float Avaliacao;

    public modelAvaliacaoProfessor() {
    }

    public int getId_Perg_Resp() {
        return Id_Perg_Resp;
    }

    public void setId_Perg_Resp(int id_Perg_Resp) {
        Id_Perg_Resp = id_Perg_Resp;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public float getAvaliacao() {
        return Avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        Avaliacao = avaliacao;
    }
}
