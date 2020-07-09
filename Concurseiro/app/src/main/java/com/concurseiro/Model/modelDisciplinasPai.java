package com.concurseiro.Model;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class modelDisciplinasPai {

    private int Id_Area_Disciplina_Pai;
    private String Descricao;

    public modelDisciplinasPai(int id_Area_Disciplina_Pai, String descricao) {
        Descricao = descricao;
        Id_Area_Disciplina_Pai = id_Area_Disciplina_Pai;
    }

    public String getDescricao() {
        return Descricao;
    }

    public int getId_Area_Disciplina_Pai() {
        return Id_Area_Disciplina_Pai;
    }
}
