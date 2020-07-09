package com.concurseiro.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.concurseiro.R;

/**
 * Created by user4 on 23/08/2017.
 */

public class NotificationMe {

    private static Context context;
    private static NotificationManager notificationManager;

    public NotificationMe(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void AddNotification(String Ticker, String ContentTitle, String ContentText, String ContentInfo) {

        //
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.journaldev.com/"));
        //Intent intent  = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //stackBuilder.addParentStack(NotificationActivity.class);
        //stackBuilder.addNextIntent(intent);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context);

        notificationCompat.setTicker(Ticker);
        notificationCompat.setContentTitle(ContentTitle);
        notificationCompat.setContentText(ContentText);
        notificationCompat.setContentIntent(pendingIntent);
        notificationCompat.setAutoCancel(true);
        notificationCompat.setContentInfo(ContentInfo);
        notificationCompat.setNumber(100);

        //ICONE PEQUENO E GRANDE.
        notificationCompat.setSmallIcon(R.mipmap.ic_launcher);
        notificationCompat.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        // primeiro cria a intenção.
        //Notification notification = notificationCompat.build();

        notificationManager.notify(R.mipmap.ic_launcher, notificationCompat.build());
    }
}
