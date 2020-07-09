package com.alexandremarques.heroesdamarvel.util;

import android.app.Dialog;
import android.content.Context;

import com.alexandremarques.heroesdamarvel.R;

public class CustomDialog {

    private Dialog dialog;
    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    private void showAlert(String title, int ResId) {
        dialog = new Dialog(context);
        dialog.setContentView(ResId);
        dialog.setTitle(title);
        dialog.show();
    }

    public void alertWarning() {
        showAlert("Aviso", R.layout.custom_dialog);
    }

    public void alertInfo() {
        showAlert("Informação", R.layout.custom_dialog);
    }

    public void alertError() {
        showAlert("Error", R.layout.custom_dialog);
    }
}
