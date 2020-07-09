package com.concurseiro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.concurseiro.Model.modelPerguntasSemMestre;
import com.concurseiro.R;


public class ArrayAdapterPergSemMestre extends ArrayAdapter<modelPerguntasSemMestre> {

    private LayoutInflater inflater;
    private Context context;
    private ViewHolder mViewHolder = new ViewHolder();
    private List<modelPerguntasSemMestre> lstPerguntas;

    public ArrayAdapterPergSemMestre(Context context, List<modelPerguntasSemMestre> lstPerguntas) {
        super(context, 0, lstPerguntas);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lstPerguntas = lstPerguntas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;
        modelPerguntasSemMestre dataPerguntasSemMestre = lstPerguntas.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_row_perguntas, parent, false);

            this.mViewHolder.txtNome = (TextView) convertView.findViewById(R.id.text_nome_aluno);
            this.mViewHolder.txtPergunta = (TextView) convertView.findViewById(R.id.text_pergunta);
            this.mViewHolder.imageAluno = (ImageView) convertView.findViewById(R.id.image_aluno);

            convertView.setTag(mViewHolder);
            layout = convertView;
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.txtNome.setText(dataPerguntasSemMestre.getFirst_name());
        mViewHolder.txtPergunta.setText(dataPerguntasSemMestre.getPergunta());
        mViewHolder.imageAluno.setImageBitmap(dataPerguntasSemMestre.getImageAluno());

        //Log.i(Constants.TAG, "Image: " + getItem(position).getImage());

        if (!dataPerguntasSemMestre.getConcluida().contains("S")) {
            convertView.setBackgroundColor(Color.YELLOW);
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageAluno;
        TextView txtNome, txtPergunta;
    }
}
