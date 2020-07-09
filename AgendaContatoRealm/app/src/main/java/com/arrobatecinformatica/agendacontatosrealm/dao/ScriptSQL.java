package com.arrobatecinformatica.agendacontatosrealm.dao;

public class ScriptSQL {

    public static StringBuilder CreateContatos(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE Contato (id INTEGER PRIMARY KEY, nome TEXT, email TEXT, telefone TEXT)");
        return sql;
    }
}
