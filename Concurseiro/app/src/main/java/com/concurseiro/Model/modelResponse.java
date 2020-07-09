package com.concurseiro.Model;

/**
 * Created by user4 on 15/09/2017.
 */

public class modelResponse {
    public static boolean Successo;
    public static String ErroMessage;
    public static String Mensagem;
    public static int CodigoErro;

    public static boolean isSuccesso() {
        return Successo;
    }

    public static void setSuccesso(boolean successo) {
        Successo = successo;
    }

    public static String getErroMessage() {
        return ErroMessage;
    }

    public static void setErroMessage(String erroMessage) {
        ErroMessage = erroMessage;
    }

    public static String getMensagem() {
        return Mensagem;
    }

    public static void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    public static int getCodigoErro() {
        return CodigoErro;
    }

    public static void setCodigoErro(int codigoErro) {
        CodigoErro = codigoErro;
    }
}
