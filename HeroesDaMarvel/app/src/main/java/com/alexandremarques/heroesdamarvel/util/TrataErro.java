package com.alexandremarques.heroesdamarvel.util;

import android.content.Context;
import android.util.Log;

import com.alexandremarques.heroesdamarvel.constants.Constants;

public class TrataErro {
    private Alerta alerta;
    private Context context;

    public TrataErro(Context context) {
        this.context = context;
    }

    public void AlertaErro(String Erro) {
        alerta = new Alerta(context);
        alerta.AlertError("Trata Erro", Erro);
        Log.i(Constants.TAG, "Erro: " + Erro);
    }
}
