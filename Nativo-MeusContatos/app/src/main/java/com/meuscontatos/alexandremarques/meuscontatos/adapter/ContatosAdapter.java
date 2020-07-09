package com.meuscontatos.alexandremarques.meuscontatos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meuscontatos.alexandremarques.meuscontatos.R;
import com.meuscontatos.alexandremarques.meuscontatos.interfaces.IRecycleView;
import com.meuscontatos.alexandremarques.meuscontatos.model.Contato;

import java.util.List;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Contato> listaContatos;
    private IRecycleView iRecycleView;
    private LayoutInflater inflater;
    private Context context;

    public ContatosAdapter(List<Contato> listaContatos, IRecycleView iRecycleView, Context context) {
        this.listaContatos = listaContatos;
        this.iRecycleView = iRecycleView;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contato_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Contato contato = listaContatos.get(position);

        holder.nome.setText(contato.getNome());
        holder.email.setText(contato.getEmail());
        holder.telefone.setText(contato.getTelefone());

        //Click Aqui.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iRecycleView.IRecycleView_OnItemClickListener(contato);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, email, telefone;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.list_nome);
            email = itemView.findViewById(R.id.list_email);
            telefone = itemView.findViewById(R.id.list_telefone);
        }
    }
}
