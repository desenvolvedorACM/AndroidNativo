package com.alexandremarques.heroesdamarvel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.model.ModelPersonagem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 14/03/2018.
 */

public class adapterPersonagens extends BaseAdapter {

    private Context context;
    private List<ModelPersonagem> listaPersonagem;
    private LayoutInflater inflater;
    public ViewHolder mViewHolder = new ViewHolder();

    public adapterPersonagens(Context context, List<ModelPersonagem> listaPersonagem) {
        this.context = context;
        this.listaPersonagem = listaPersonagem;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaPersonagem.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPersonagem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ModelPersonagem personagem = listaPersonagem.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_row_personagem, parent, false);

            this.mViewHolder.txt_Id = (TextView) convertView.findViewById(R.id.textviewId);
            this.mViewHolder.txt_Nome = (TextView) convertView.findViewById(R.id.textNome);
            this.mViewHolder.Imagem_Personagem = (ImageView) convertView.findViewById(R.id.img_logo);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        this.mViewHolder.txt_Id.setText("ID: " + String.valueOf(personagem.getId()));
        this.mViewHolder.txt_Nome.setText("Nome: " + personagem.getName());

        //SE TENHO IMAGEM, ENTÃO EXIBE.
        if (personagem.getImagePersonagem() != null) {
            this.mViewHolder.Imagem_Personagem.setImageBitmap(personagem.getImagePersonagem());
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView Imagem_Personagem;
        TextView txt_Id, txt_Nome;
    }
}
