package br.com.arrobatecinformatica.agendacontatos.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.arrobatecinformatica.agendacontatos.R;
import br.com.arrobatecinformatica.agendacontatos.model.Contato;

/**
 * Created by Alexandre on 27/05/2017.
 */

public class ContatoBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contato> lstContatos;
    LayoutInflater inflater;

    public ContatoBaseAdapter(Context context, ArrayList<Contato> lstContatos) {
        this.context = context;
        this.lstContatos = lstContatos;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lstContatos.size();
    }

    @Override
    public Contato getItem(int position) {
        return lstContatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VielHolder mVielHolder = new VielHolder();
        Contato contato = lstContatos.get(position);
        View layout;

        if (convertView == null) {

            layout = inflater.inflate(R.layout.item_contato, parent, false);

            mVielHolder.text_cor = (TextView) layout.findViewById(R.id.text_cor);
            mVielHolder.text_nome = (TextView) layout.findViewById(R.id.text_nome);
            mVielHolder.text_telefone = (TextView) layout.findViewById(R.id.text_telefone);
            mVielHolder.text_email = (TextView) layout.findViewById(R.id.text_email);

        } else {
            layout = convertView;
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
