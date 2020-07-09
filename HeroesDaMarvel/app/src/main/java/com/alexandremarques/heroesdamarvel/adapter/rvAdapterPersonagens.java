package com.alexandremarques.heroesdamarvel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.interfaces.IRecycleView_OnItemClickListener;
import com.alexandremarques.heroesdamarvel.model.ModelPersonagem;

import java.util.List;

public class rvAdapterPersonagens extends RecyclerView.Adapter<rvAdapterPersonagens.ViewHolder> {

    private List<ModelPersonagem> listaPersonagem;
    private IRecycleView_OnItemClickListener clickRecyclerViewInterface;

    public rvAdapterPersonagens(List<ModelPersonagem> listaPersonagems, IRecycleView_OnItemClickListener clickRecyclerViewInterface) {
        this.listaPersonagem = listaPersonagems;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_personagem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ModelPersonagem modelPersonagem = listaPersonagem.get((position));

        holder.textviewID.setText(String.valueOf(modelPersonagem.getId()));
        holder.txt_Nome.setText(modelPersonagem.getName());
        holder.Imagem_Personagem.setImageBitmap(modelPersonagem.getImagePersonagem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecyclerViewInterface.RecycleView_OnItemClickListener(modelPersonagem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPersonagem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView Imagem_Personagem;
        final TextView txt_Nome;
        final TextView textviewID;

        public ViewHolder(View itemView) {
            super(itemView);

            Imagem_Personagem = (ImageView) itemView.findViewById(R.id.img_logo);
            textviewID = (TextView) itemView.findViewById(R.id.textviewId);
            txt_Nome = (TextView) itemView.findViewById(R.id.textNome);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    clickRecyclerViewInterface.RecycleView_OnItemClickListener(listaPersonagem.get(itemPosition));
                }
            });*/
        }
    }
}
