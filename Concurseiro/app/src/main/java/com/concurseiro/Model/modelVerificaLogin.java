package com.concurseiro.Model;

/**
 * Created by Alexandre on 13/09/2017.
 */

public class modelVerificaLogin {
    public modelVerificaLogin() {
    }

    public static String Email;
    public static String Password;

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }
}
