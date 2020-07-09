package br.com.arrobatecinformatica.agendacontatos.database;

/**
 * Created by Alexandre on 15/04/2017.
 */

import android.content.Context;
import android.database.sqlite.*;

import br.com.arrobatecinformatica.agendacontatos.constants.MinhasConstants;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        super(context, MinhasConstants.NOMEBANCO, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSQL.CreateContato());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(ScriptSQL.DropContato());
    }
}
