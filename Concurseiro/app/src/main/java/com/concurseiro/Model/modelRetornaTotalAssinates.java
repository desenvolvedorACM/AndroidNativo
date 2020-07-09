package com.concurseiro.Model;

/**
 * Created by Alexandre on 13/09/2017.
 */

public class modelRetornaTotalAssinates {

    public modelRetornaTotalAssinates() {
    }


    public boolean Successo;
    public Integer TotalAssinantes;
    public String ErroMessage;
    public String Mesangem;
    public int CodigoErro;

    public boolean isSuccesso() {
        return Successo;
    }

    public void setSuccesso(boolean successo) {
        Successo = successo;
    }

    public Integer getTotalAssinantes() {
        return TotalAssinantes;
    }

    public void setTotalAssinantes(Integer totalAssinantes) {
        TotalAssinantes = totalAssinantes;
    }

    public String getErroMessage() {
        return ErroMessage;
    }

    public void setErroMessage(String erroMessage) {
        ErroMessage = erroMessage;
    }

    public String getMesangem() {
        return Mesangem;
    }

    public void setMesangem(String mesangem) {
        Mesangem = mesangem;
    }

    public int getCodigoErro() {
        return CodigoErro;
    }

    public void setCodigoErro(int codigoErro) {
        CodigoErro = codigoErro;
    }
}
