package com.concurseiro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.concurseiro.Model.modelDisciplinasFilha;
import com.concurseiro.R;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class SpinnerAdapterDisciplinasFilha extends ArrayAdapter<modelDisciplinasFilha> {

    private Context context;
    private ArrayList<modelDisciplinasFilha> lstDisciplinasFilha;
    private LayoutInflater inflater;

    public SpinnerAdapterDisciplinasFilha(Context context, int layout, ArrayList<modelDisciplinasFilha> lstDisciplinasFilha) {
        super(context, layout, lstDisciplinasFilha);
        this.context = context;
        this.lstDisciplinasFilha = lstDisciplinasFilha;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        modelDisciplinasFilha disciplinasFilhas = lstDisciplinasFilha.get(position);
        View layout = inflater.inflate(R.layout.item_row_spinner_disciplinaspai, parent, false);
        TextView txtDecricao = (TextView) layout.findViewById(R.id.text_disciplina_pai);

        if (lstDisciplinasFilha.size() == 0) {
            txtDecricao.setText("Disciplinas");
        } else {
            txtDecricao.setText(disciplinasFilhas.getDescricao());
        }
        return layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        //modelDisciplinasPai disciplinasPai = lstDisciplinasPai.get(position);
        return getDropDownView(position, convertView, parent);
    }

}
