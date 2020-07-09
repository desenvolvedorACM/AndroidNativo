package com.concurseiro.Model;

/**
 * Created by Alexandre on 13/09/2017.
 */

public class modelPergunteMestre {

    private int Id_Usuario;
    private int Id_Professor;
    private int Id_Pessoa;
    private String Pergunta;
    private String End_Foto_Pergunta;
    private String End_Url_Pergunta;
    private int Id_Area_Disciplina;
    private float PrecoPergunta;


    public modelPergunteMestre(int id_Usuario, int id_Professor, int id_Pessoa, String pergunta, String end_Foto_Pergunta, String end_Url_Pergunta, int id_Area_Disciplina, float precoPergunta) {
        Id_Usuario = id_Usuario;
        Id_Professor = id_Professor;
        Id_Pessoa = id_Pessoa;
        Pergunta = pergunta;
        End_Foto_Pergunta = end_Foto_Pergunta;
        End_Url_Pergunta = end_Url_Pergunta;
        Id_Area_Disciplina = id_Area_Disciplina;
        PrecoPergunta = precoPergunta;
    }

    public int getId_Usuario() {
        return Id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        Id_Usuario = id_Usuario;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public String getPergunta() {
        return Pergunta;
    }

    public void setPergunta(String pergunta) {
        Pergunta = pergunta;
    }

    public String getEnd_Foto_Pergunta() {
        return End_Foto_Pergunta;
    }

    public void setEnd_Foto_Pergunta(String end_Foto_Pergunta) {
        End_Foto_Pergunta = end_Foto_Pergunta;
    }

    public String getEnd_Url_Pergunta() {
        return End_Url_Pergunta;
    }

    public void setEnd_Url_Pergunta(String end_Url_Pergunta) {
        End_Url_Pergunta = end_Url_Pergunta;
    }

    public int getId_Area_Disciplina() {
        return Id_Area_Disciplina;
    }

    public void setId_Area_Disciplina(int id_Area_Disciplina) {
        Id_Area_Disciplina = id_Area_Disciplina;
    }

    public float getPrecoPergunta() {
        return PrecoPergunta;
    }

    public void setPrecoPergunta(float precoPergunta) {
        PrecoPergunta = precoPergunta;
    }

    public int getId_Pessoa() {
        return Id_Pessoa;
    }

    public void setId_Pessoa(int id_Pessoa) {
        Id_Pessoa = id_Pessoa;
    }
}
