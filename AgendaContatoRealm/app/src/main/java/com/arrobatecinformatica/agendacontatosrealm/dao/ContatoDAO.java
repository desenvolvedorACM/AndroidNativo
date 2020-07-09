package com.arrobatecinformatica.agendacontatosrealm.dao;

import android.content.ContentValues;
import android.content.Context;

import com.arrobatecinformatica.agendacontatosrealm.model.Contato;

import java.util.List;

import io.realm.Realm;

public class ContatoDAO {

    //private Context context;
    //private BancoDAO dao;
    private Realm realm;
    //private static final String TABLECONTATO = "Contato";

    public ContatoDAO(Context context) {
        //this.context = context;
        //this.dao = new BancoDAO(context);
        realm = Realm.getDefaultInstance();
    }

    public long InserirContato(Contato contato) {
        /*SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues values = pegaDadosDoContato(contato);

        long inserir = db.insertOrThrow(TABLECONTATO, null, values);
        db.close();*/
        realm.beginTransaction();
        realm.copyToRealm(contato);
        realm.commitTransaction();
        //realm.close();

        return 0;
    }


    public void EditarContato(Contato contato) {
        /*SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues values = pegaDadosDoContato(contato);
        String whereClause = "id = ?";
        String whereArgs[] = new String[]{String.valueOf(contato.getId())};

        db.update(TABLECONTATO, values, whereClause, whereArgs);
        db.close();*/

        realm.beginTransaction();
        realm.copyToRealm(contato);
        realm.commitTransaction();
        realm.close();
    }

    public void DeletarContato(Contato contato) {
        /*SQLiteDatabase db = dao.getWritableDatabase();
        String whereClause = "id = ?";
        String whereArg[] = new String[]{String.valueOf(contato.getId())};

        db.delete(TABLECONTATO, whereClause, whereArg);
        db.close();*/

        realm.beginTransaction();
        contato.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public List<Contato> BuscaContatos() {

        List<Contato> contatos = realm.where(Contato.class)
                .findAll();

        //String sql = "SELECT * FROM " + TABLECONTATO;
        //SQLiteDatabase db = dao.getReadableDatabase();
        //Cursor c = db.rawQuery(sql, null);

        /*while (c.moveToNext()) {
            Contato contato = new Contato();

            contato.setId(c.getInt(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEmail(c.getString(c.getColumnIndex("email")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));

            contatos.add(contato);
        }*/

        //c.close();
        return contatos;

    }

    private ContentValues pegaDadosDoContato(Contato contato) {
        ContentValues dados = new ContentValues();

        dados.put("nome", contato.getNome());
        dados.put("email", contato.getEmail());
        dados.put("telefone", contato.getTelefone());

        return dados;
    }

}
