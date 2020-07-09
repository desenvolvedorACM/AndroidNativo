package com.concurseiro.Model;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class modelDisciplinasFilha {

    public int Id_Area_Disciplina;
    public String Descricao;

    public modelDisciplinasFilha(int id_Area_Disciplina, String descricao) {
        Id_Area_Disciplina = id_Area_Disciplina;
        Descricao = descricao;
    }

    public int getId_Area_Disciplina() {
        return Id_Area_Disciplina;
    }

    public void setId_Area_Disciplina(int id_Area_Disciplina) {
        Id_Area_Disciplina = id_Area_Disciplina;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }
}
