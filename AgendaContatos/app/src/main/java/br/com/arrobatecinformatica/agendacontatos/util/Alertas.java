package br.com.arrobatecinformatica.agendacontatos.util;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Alexandre on 18/04/2017.
 */

public class Alertas {

    public static void Show(Context ctx, String tittle, String msg, int iconId) {

        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setTitle(tittle);
        dlg.setIcon(iconId);
        dlg.setMessage(msg);
        dlg.setNeutralButton("OK", null);
        dlg.show();
    }

    public static void AlertaAviso(Context ctx, String tittle, String msg) {

        Show(ctx, tittle, msg, android.R.drawable.ic_dialog_info);
    }

    public static void AlertaErro(Context ctx, String tittle, String msg) {

        Show(ctx, tittle, msg, android.R.drawable.ic_delete);
    }

    public static void AlertaSucesso(Context ctx, String tittle, String msg) {

        Show(ctx, tittle, msg, android.R.drawable.ic_dialog_alert);
    }
}
