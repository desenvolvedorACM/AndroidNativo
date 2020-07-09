package com.concurseiro.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.concurseiro.R;

/**
 * Created by user4 on 15/08/2017.
 */

public class apiMessages {

    private AlertDialog.Builder dialog;
    private Context context;

    public apiMessages(Context context) {
        this.context = context;
    }

    private void showAlert(String tittle, String Msg, int iconId) {

        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(tittle);
        dialog.setMessage(Msg);
        dialog.setCancelable(false);
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
        showAlert(tittle, Msg, R.mipmap.ic_action_warning);
    }

    public void AlertError(String tittle, String Msg) {
        showAlert(tittle, Msg, R.drawable.ic_launcher_error);
    }

    public void AlertInfo(String tittle, String Msg) {
        showAlert(tittle, Msg, R.drawable.ic_launcher_info);
    }

    public void AlertConfirm(String tittle, String Msg) {

        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(tittle);
        dialog.setMessage(Msg);
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.alert_dark_frame);
        dialog
                .setPositiveButton("Confirma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                    }
                })
                .setNegativeButton("Cancela ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        dialog.show();
    }
}
