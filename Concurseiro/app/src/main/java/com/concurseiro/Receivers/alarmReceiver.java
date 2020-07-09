package com.concurseiro.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import com.concurseiro.Constants.Constants;


/**
 * Created by user4 on 22/08/2017.
 */

public class alarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Date data = new Date();
        //NotificationMe notificationMe = new NotificationMe(context);

        //notificationMe.AddNotification("Ticker", "Tittle Notification", "Conteudo Msg Executando backup", "ContentInfo");

        Log.i(Constants.TAG, "Executando Alarme !");
        Toast.makeText(context, "Executando Alarme " + data, Toast.LENGTH_LONG).show();
    }
}
