package com.alexandremarques.heroesdamarvel.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandremarques.heroesdamarvel.R;
import com.alexandremarques.heroesdamarvel.model.ModelPersonagem;
import com.alexandremarques.heroesdamarvel.model.ModelStories;

import java.util.List;

public class rvAdapterStories extends RecyclerView.Adapter<rvAdapterStories.ViewHolder> {

    private List<ModelStories> listStories;

    public rvAdapterStories(List<ModelStories> listStories) {
        this.listStories = listStories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_row_stories, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ModelStories modelStories = listStories.get((position));

        holder.textId.setText(String.valueOf(modelStories.getId()));
        holder.textTitle.setText(modelStories.getTitle());
    }

    @Override
    public int getItemCount() {
        return listStories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textId;
        final TextView textTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            textId = (TextView) itemView.findViewById(R.id.textViewId);
            textTitle = (TextView) itemView.findViewById(R.id.textViewTitle);

        }
    }
}
