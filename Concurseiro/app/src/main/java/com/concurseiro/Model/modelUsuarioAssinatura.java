package com.concurseiro.Model;

/**
 * Created by Alexandre on 13/09/2017.
 */

public class modelUsuarioAssinatura {

    private  String First_name;
    private  String Last_name;
    private  String Password;
    private  String Email;
    private  String Facebook_token;
    private  String Facebook_id;
    private  String Type;
    private  String Profile;
    private  String Image;

    public modelUsuarioAssinatura() {
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFacebook_token() {
        return Facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        Facebook_token = facebook_token;
    }

    public String getFacebook_id() {
        return Facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        Facebook_id = facebook_id;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
