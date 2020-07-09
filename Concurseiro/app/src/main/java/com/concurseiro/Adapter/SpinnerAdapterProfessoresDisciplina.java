package com.concurseiro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.concurseiro.Constants.Constants;
import com.concurseiro.Model.modelTodosProfessoresDisciplina;
import com.concurseiro.R;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class SpinnerAdapterProfessoresDisciplina extends ArrayAdapter<modelTodosProfessoresDisciplina> {

    private Context context;
    private ArrayList<modelTodosProfessoresDisciplina> lstProfessoresDisciplinas;
    private LayoutInflater inflater;
    private ViewHolder mViewHolder;

    public SpinnerAdapterProfessoresDisciplina(Context context, int layout, ArrayList<modelTodosProfessoresDisciplina> lstProfessoresDisciplinas) {
        super(context, layout, lstProfessoresDisciplinas);

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lstProfessoresDisciplinas = lstProfessoresDisciplinas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        mViewHolder = new ViewHolder();
        modelTodosProfessoresDisciplina todosProfessoresDisciplina = lstProfessoresDisciplinas.get(position);


        if (lstProfessoresDisciplinas.size() == 0) {

            convertView = inflater.inflate(R.layout.item_row_spinner_professores_disciplina, parent, false);
            mViewHolder.txtNomeProfessor = (TextView) convertView.findViewById(R.id.text_nome_professor);
            mViewHolder.imgProfessor = (ImageView) convertView.findViewById(R.id.image_professor);

            mViewHolder.txtNomeProfessor.setText("Professores");
            convertView.setTag(mViewHolder);
        } else {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_row_spinner_professores_disciplina, parent, false);
                mViewHolder.rbAvaliacao = (RatingBar) convertView.findViewById(R.id.rating_avaliacao);

                mViewHolder.txtNomeProfessor = (TextView) convertView.findViewById(R.id.text_nome_professor);
                mViewHolder.imgProfessor = (ImageView) convertView.findViewById(R.id.image_professor);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            //CARGA DOS DADOS.
            mViewHolder.rbAvaliacao.setEnabled(false);
            mViewHolder.rbAvaliacao.setRating(Float.parseFloat("2.16"));
            mViewHolder.txtNomeProfessor.setText(todosProfessoresDisciplina.getFirst_name());
            if (todosProfessoresDisciplina.getImage() != null) {
                Log.i(Constants.TAG, "Imagem " + todosProfessoresDisciplina.getUrlImage());
                mViewHolder.imgProfessor.setImageBitmap(todosProfessoresDisciplina.getImage());
            }
        }


        return convertView;
    }


    private class ViewHolder {
        ImageView imgProfessor;
        TextView txtNomeProfessor;
        RatingBar rbAvaliacao;
    }
}
