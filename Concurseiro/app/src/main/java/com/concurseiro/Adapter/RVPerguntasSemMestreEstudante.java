package com.concurseiro.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.concurseiro.Model.modelPerguntasSemMestre;
import com.concurseiro.R;

/**
 * Created by user4 on 15/09/2017.
 */

public class RVPerguntasSemMestreEstudante extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<modelPerguntasSemMestre> lstPerguntasSemMestre;
    private ViewHolder mViewHolder;
    private LayoutInflater inflater;


    public RVPerguntasSemMestreEstudante(Context context, ArrayList<modelPerguntasSemMestre> lstPerguntasSemMestre) {
        this.context = context;
        this.lstPerguntasSemMestre = lstPerguntasSemMestre;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.item_row_perguntas, parent, false);
        mViewHolder = new ViewHolder(layout);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        mViewHolder = (ViewHolder) holder;
        modelPerguntasSemMestre modelDados = lstPerguntasSemMestre.get(position);

        mViewHolder.txtNome.setText(modelDados.getFirst_name());
        mViewHolder.txtPergunta.setText(modelDados.getPergunta());
        mViewHolder.imageAluno.setImageBitmap(modelDados.getImageAluno());
    }

    @Override
    public int getItemCount() {
        return lstPerguntasSemMestre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAluno;
        TextView txtNome, txtPergunta;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNome = (TextView) itemView.findViewById(R.id.text_nome_aluno);
            txtPergunta = (TextView) itemView.findViewById(R.id.text_pergunta);
            imageAluno = (ImageView) itemView.findViewById(R.id.image_aluno);
        }
    }
}
