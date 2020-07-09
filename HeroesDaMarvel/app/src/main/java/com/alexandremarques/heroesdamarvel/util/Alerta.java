package com.alexandremarques.heroesdamarvel.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.alexandremarques.heroesdamarvel.R;

public class Alerta {

    private AlertDialog.Builder dialog;
    private Context context;

    public Alerta(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context);
    }

    private void showAlerta(String titulo, String Mensagem, int iconId) {

        dialog.setTitle(titulo);
        dialog.setCancelable(false);
        dialog.setMessage(Mensagem);
        dialog.setIcon(iconId);
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void AlertWarning(String tittle, String Msg) {
        showAlerta(tittle, Msg, android.R.drawable.ic_dialog_alert);
    }


    public void AlertError(String tittle, String Msg) {
        showAlerta(tittle, Msg, android.R.drawable.ic_delete);
    }


    public void AlertInfo(String tittle, String Msg) {
        showAlerta(tittle, Msg, R.drawable.ic_info);
    }
}
