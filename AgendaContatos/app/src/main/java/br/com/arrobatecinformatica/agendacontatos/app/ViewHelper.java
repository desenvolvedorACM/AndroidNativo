package br.com.arrobatecinformatica.agendacontatos.app;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Alexandre on 22/04/2017.
 */

public class ViewHelper {

    public static ArrayAdapter createArrayAdapter(Context ctx, Spinner spinner, String[] values) {

        ArrayAdapter ArrayAdapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, values);
        ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ArrayAdapter);

        return ArrayAdapter;
    }
}
