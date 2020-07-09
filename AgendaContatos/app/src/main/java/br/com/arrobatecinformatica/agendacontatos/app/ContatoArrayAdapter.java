package br.com.arrobatecinformatica.agendacontatos.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.arrobatecinformatica.agendacontatos.R;
import br.com.arrobatecinformatica.agendacontatos.model.Contato;

/**
 * Created by Alexandre on 23/04/2017.
 */

public class ContatoArrayAdapter extends ArrayAdapter<Contato> {

    private int resource;
    private Context context;
    private LayoutInflater inflater;

    public ContatoArrayAdapter(Context context, int resource) {
        super(context, resource);

        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        //View v = null;
        VielHolder mVielHolder = new VielHolder();
        Contato contato = getItem(position);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_contato, parent, false);

            mVielHolder.text_cor = (TextView) convertView.findViewById(R.id.text_cor);
            mVielHolder.text_nome = (TextView) convertView.findViewById(R.id.text_nome);
            mVielHolder.text_telefone = (TextView) convertView.findViewById(R.id.text_telefone);
            mVielHolder.text_email = (TextView) convertView.findViewById(R.id.text_email);

            convertView.setTag(mVielHolder);
            //convertView = view;

        } else {

            mVielHolder = (VielHolder) convertView.getTag();
            //view = convertView;
        }

        if (contato.getNome().toUpperCase().startsWith("A")) {
            mVielHolder.text_cor.setBackgroundColor(context.getResources().getColor(R.color.corAzul));
            //convertView.setBackgroundColor(Color.BLUE);
        } else if (contato.getNome().toUpperCase().startsWith("B")) {
            mVielHolder.text_cor.setBackgroundColor(context.getResources().getColor(R.color.corVermelha));
            //convertView.setBackgroundColor(Color.RED);
        } else {
            mVielHolder.text_cor.setBackgroundColor(context.getResources().getColor(R.color.corAmarela));
            //convertView.setBackgroundColor(Color.YELLOW);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        mVielHolder.text_nome.setText(contato.getNome());
        mVielHolder.text_telefone.setText(contato.getTelefone());
        mVielHolder.text_email.setText(contato.getEmail());


        return convertView;
    }

    static class VielHolder {
        TextView text_cor;
        TextView text_nome;
        TextView text_telefone;
        TextView text_email;
    }
}
