package com.alexandremarques.heroesdamarvel.model;

import android.graphics.Bitmap;

/**
 * Created by Alexandre on 14/03/2018.
 */

public class ModelPersonagem {
    private int Id;
    private String Name;
    private String Description;
    private String ImageUrl;
    private Bitmap ImagePersonagem;

    public ModelPersonagem() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Bitmap getImagePersonagem() {
        return ImagePersonagem;
    }

    public void setImagePersonagem(Bitmap imagePersonagem) {
        ImagePersonagem = imagePersonagem;
    }
}
