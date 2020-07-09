package com.concurseiro.Model;

import android.graphics.Bitmap;

/**
 * Created by Alexandre on 12/10/2017.
 */

public class modelTodosProfessoresDisciplina {

    private int Id_Professor;
    private String First_name;
    private String Email;
    private String UrlImage;
    private Bitmap Image;

    public modelTodosProfessoresDisciplina() {

    }

    public modelTodosProfessoresDisciplina(int id_Professor, String first_name, String email, String urlImage, Bitmap image) {
        Id_Professor = id_Professor;
        First_name = first_name;
        Email = email;
        UrlImage = urlImage;
        Image = image;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }
}
