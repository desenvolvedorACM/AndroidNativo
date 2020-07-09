package com.meuscontatos.alexandremarques.meuscontatos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meuscontatos.alexandremarques.meuscontatos.R;
import com.meuscontatos.alexandremarques.meuscontatos.constants.Constants;
import com.meuscontatos.alexandremarques.meuscontatos.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    private Context context;
    private BancoDAO dao;

    private static final String TABLECONTATO = "Contato";

    public ContatoDAO(Context context) {
        this.context = context;
        this.dao = new BancoDAO(context);
    }

    public long InserirContato(Contato contato) {
        SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues values = pegaDadosDoContato(contato);

        long inserir = db.insertOrThrow(TABLECONTATO, null, values);
        db.close();
        return inserir;
    }

    private ContentValues pegaDadosDoContato(Contato contato) {
        ContentValues dados = new ContentValues();

        dados.put("nome", contato.getNome());
        dados.put("email", contato.getEmail());
        dados.put("telefone", contato.getTelefone());

        return dados;
    }

    public void EditarContato(Contato contato) {
        SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues values = pegaDadosDoContato(contato);
        String whereClause = "id = ?";
        String whereArgs[] = new String[] { String.valueOf(contato.getId()) };

        db.update(TABLECONTATO, values, whereClause, whereArgs);
        db.close();
    }

    public void DeletarContato(Contato contato) {
        SQLiteDatabase db = dao.getWritableDatabase();
        String whereClause = "id = ?";
        String whereArg[] = new String[]{String.valueOf(contato.getId())};

        db.delete(TABLECONTATO, whereClause, whereArg);
        db.close();
    }

    public List<Contato> BuscaContatos() {

        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLECONTATO;

        SQLiteDatabase db = dao.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()) {
            Contato contato = new Contato();

            contato.setId(c.getInt(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEmail(c.getString(c.getColumnIndex("email")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));

            contatos.add(contato);
        }

        c.close();
        return contatos;
    }

}
