package com.concurseiro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.concurseiro.Model.modelDisciplinasPai;
import com.concurseiro.R;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class SpinnerAdapterDisciplinasPai extends ArrayAdapter<modelDisciplinasPai> {

    private Context context;
    private ArrayList<modelDisciplinasPai> lstDisciplinasPai;
    private int layout;
    LayoutInflater inflater;

    public SpinnerAdapterDisciplinasPai(Context context, int layout, ArrayList<modelDisciplinasPai> lstDisciplinasPai) {
        super(context, layout, lstDisciplinasPai);
        this.context = context;
        this.lstDisciplinasPai = lstDisciplinasPai;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        //modelDisciplinasPai disciplinasPai = lstDisciplinasPai.get(position);
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        modelDisciplinasPai disciplinasPai = lstDisciplinasPai.get(position);

        View layout = inflater.inflate(R.layout.item_row_spinner_disciplinaspai, parent, false);
        TextView txtDecricao = (TextView) layout.findViewById(R.id.text_disciplina_pai);

        if (lstDisciplinasPai.size() == 0) {
            txtDecricao.setText("Disciplinas");
        } else {
            txtDecricao.setText(disciplinasPai.getDescricao());
        }
        return layout;
    }
}
