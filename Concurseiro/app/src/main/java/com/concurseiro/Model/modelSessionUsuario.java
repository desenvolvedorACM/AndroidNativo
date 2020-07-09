package com.concurseiro.Model;

import java.io.Serializable;

/**
 * Created by Alexandre on 26/09/2017.
 */

public class modelSessionUsuario implements Serializable {

    private  String Type;
    private  String Profile;
    private  String isProfessor;
    private  int Id_Pessoa;
    private  int Id_Usuario;
    private  int Id_Professor;
    private  String Email;
    private  String First_name;
    private  String Image;


    public modelSessionUsuario() {
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getIsProfessor() {
        return isProfessor;
    }

    public void setIsProfessor(String isProfessor) {
        this.isProfessor = isProfessor;
    }

    public int getId_Pessoa() {
        return Id_Pessoa;
    }

    public void setId_Pessoa(int id_Pessoa) {
        Id_Pessoa = id_Pessoa;
    }

    public int getId_Usuario() {
        return Id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        Id_Usuario = id_Usuario;
    }

    public int getId_Professor() {
        return Id_Professor;
    }

    public void setId_Professor(int id_Professor) {
        Id_Professor = id_Professor;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
