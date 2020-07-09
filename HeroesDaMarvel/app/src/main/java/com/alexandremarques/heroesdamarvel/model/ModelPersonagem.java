package com.alexandremarques.heroesdamarvel.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Alexandre on 14/03/2018.
 */

public class ModelPersonagem implements Serializable {
    private int Id;
    private String Name;
    private String Description;
    private String ImageUrl;
    private Bitmap ImagePersonagem;

    public ModelPersonagem() {}

    public ModelPersonagem(int id, String name, String description, String imageUrl, Bitmap imagePersonagem) {
        Id = id;
        Name = name;
        Description = description;
        ImageUrl = imageUrl;
        ImagePersonagem = imagePersonagem;
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
