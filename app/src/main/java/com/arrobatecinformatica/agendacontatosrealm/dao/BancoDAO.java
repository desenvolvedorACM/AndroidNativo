package com.arrobatecinformatica.agendacontatosrealm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arrobatecinformatica.agendacontatosrealm.constants.Constants;

public class BancoDAO extends SQLiteOpenHelper {

    /*private static final String NOMEBANCO = "Agenda";
    private static final int VERSAOBANCO = 1;

    private static final String TABLECONTATO = "CREATE TABLE Contato (id INTEGER PRIMARY KEY, " +
            "nome TEXT, email TEXT, telefone TEXT)";*/

    public BancoDAO(Context context) {
        super(context, Constants.NOMEBANCO, null, Constants.VERSAOBANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSQL.CreateContatos().toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contato");
    }
}
