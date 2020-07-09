package com.concurseiro.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Alexandre on 15/10/2017.
 */

public class modelDadosTelaPergunteMestre implements Serializable {

    private int Id_Pessoa;
    private int ID_Area_disciplina;
    private int Id_Professor;
    private Bitmap ImageProfessor;
    private String Nome_Professor;

    public modelDadosTelaPergunteMestre() {
    }

    public int getId_Pessoa() {
        return Id_Pessoa;
    }

    public void setId_Pessoa(int id_Pessoa) {
        Id_Pessoa = id_Pessoa;
    }

    public int getID_Area_disciplina() {
        return ID_Area_disciplina;
    }

    public void setID_Area_disciplina(int ID_Area_disciplina) {
        this.ID_Area_disciplina = ID_Area_disciplina;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public Bitmap getImageProfessor() {
        return ImageProfessor;
    }

    public void setImageProfessor(Bitmap imageProfessor) {
        ImageProfessor = imageProfessor;
    }

    public String getNome_Professor() {
        return Nome_Professor;
    }

    public void setNome_Professor(String nome_Professor) {
        Nome_Professor = nome_Professor;
    }
}
