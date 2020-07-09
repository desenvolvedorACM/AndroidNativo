package br.com.arrobatecinformatica.agendacontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.database.*;
import android.util.Log;

import java.util.Date;

import br.com.arrobatecinformatica.agendacontatos.app.ContatoArrayAdapter;
import br.com.arrobatecinformatica.agendacontatos.R;
import br.com.arrobatecinformatica.agendacontatos.constants.MinhasConstants;
import br.com.arrobatecinformatica.agendacontatos.database.DataBase;
import br.com.arrobatecinformatica.agendacontatos.model.Contato;

/**
 * Created by Alexandre on 16/04/2017.
 */

public class RepositorioContato {

    private SQLiteDatabase db;

    public RepositorioContato(SQLiteDatabase db) {
        this.db = db;
    }

    public long InsereContatos(Contato contato) {

        /*DataBase dataBase = new DataBase(Context);
        SQLiteDatabase conn = dataBase.getWritableDatabase();*/

        ContentValues values = preencheContato(contato);

        long registro = db.insertOrThrow(MinhasConstants.NOMETABELA, null, values);
        Log.i(MinhasConstants.NOMETABELA, " Dados gravados: " + registro);

        return registro;
    }

    public void ExcluirContato(long id) {

        db.delete(MinhasConstants.NOMETABELA, "_id = ?", new String[]{String.valueOf(id)});

    }

    public void AlterarContatos(Contato contato) {

        ContentValues values = preencheContato(contato);
        db.update(MinhasConstants.NOMETABELA, values, "_id = ?", new String[]{String.valueOf(contato.getId())});

    }

    public ContatoArrayAdapter buscaContatos(Context context) {

        ContatoArrayAdapter adpContato = new ContatoArrayAdapter(context, R.layout.item_contato);
        //adpContato = new ArrayAdapter<Contato>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = db.query(MinhasConstants.NOMETABELA, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            Log.i(MinhasConstants.NOMETABELA, " Total dados: " + cursor.getCount());

            cursor.moveToFirst();

            while (cursor.moveToNext()) {

                Contato contato = new Contato();

                contato.setId(cursor.getLong(cursor.getColumnIndex(MinhasConstants.ID)));
                contato.setNome(cursor.getString(cursor.getColumnIndex(MinhasConstants.NOME)));
                contato.setTelefone(cursor.getString(cursor.getColumnIndex(MinhasConstants.TELEFONE)));
                contato.setTipoTelefone(cursor.getString(cursor.getColumnIndex(MinhasConstants.TIPOTELEFONE)));
                contato.setEmail(cursor.getString(cursor.getColumnIndex(MinhasConstants.EMAIL)));
                contato.setTipoEmail(cursor.getString(cursor.getColumnIndex(MinhasConstants.TIPOEMAIL)));
                contato.setEndereco(cursor.getString(cursor.getColumnIndex(MinhasConstants.ENDERECO)));
                contato.setTipoEndereco(cursor.getString(cursor.getColumnIndex(MinhasConstants.TIPOENDERECO)));
                contato.setDatasEspeciais(new Date(cursor.getLong(cursor.getColumnIndex(MinhasConstants.DATASESPECIAIS))));
                contato.setTiposDatasEspeciais(cursor.getString(cursor.getColumnIndex(MinhasConstants.TIPODATASESPECIAIS)));

                adpContato.add(contato);
            }

        }
        return adpContato;
    }

    private ContentValues preencheContato(Contato contato) {

        ContentValues values = new ContentValues();

        values.put("NOME", contato.getNome());
        values.put("TELEFONE", contato.getTelefone());
        values.put("TIPOTELEFONE", contato.getTipoTelefone());
        values.put("EMAIL", contato.getEmail());
        values.put("TIPOEMAIL", contato.getTipoEmail());
        values.put("ENDERECO", contato.getEndereco());
        values.put("TIPOENDERECO", contato.getTipoEndereco());
        values.put("DATASESPECIAIS", contato.getDatasEspeciais().getTime());
        values.put("TIPODATASESPECIAIS", contato.getTiposDatasEspeciais());
        values.put("GRUPOS", contato.getGrupos());

        return values;
    }

}
