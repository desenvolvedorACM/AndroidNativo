package com.concurseiro.Util;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.concurseiro.R;


/**
 * Created by user4 on 15/08/2017.
 */

public class apiCustomMessages {

    private Context context;
    private Dialog dialog;
    TextView text_msg;
    ImageView image;

    public apiCustomMessages(Context context) {
        this.context = context;
    }

    private void showAlert(String tittle, String Msg, int iconId) {
        dialog = new Dialog(context);
        dialog.setTitle(tittle);
        dialog.setContentView(R.layout.activity_custom_alert);

        //Texto
        //text_msg = (TextView) ((Activity) context).findViewById(R.id.text_msg);
        //text_msg.setText(Msg);

        //Icon
        //image = (ImageView) ((Activity) context).findViewById(R.id.image_icon);
        //image.setImageResource(iconId);

        dialog.show();
    }

    public void AlertInfo(String Msg) {
        showAlert("Aviso", Msg, R.drawable.ic_launcher_info);
    }

    public void AlertError(String Msg) {
        showAlert("Aviso", Msg, R.drawable.ic_launcher_error);
    }

    public void Alertwarnig(String Msg) {
        showAlert("Aviso", Msg, R.drawable.ic_launcher_warnig);
    }
}
