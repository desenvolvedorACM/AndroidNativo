package com.concurseiro.Util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by user4 on 28/08/2017.
 */

public class Conectividade {

    private Context context;
    private ConnectivityManager connectivityManager;

    public Conectividade(Context context) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public int checkConnection() {

        //2G / 3G
        if (connectivityManager.getNetworkInfo(0).isConnected()) {
            return 0;
            //WI-FI
        } else if (connectivityManager.getNetworkInfo(1).isConnected()) {
            return 1;
        } else {
            return -1;
            //SEM ACESSO NTERNET.
        }
    }

    public boolean isNetwork() {

        //2G / 3G
        if (connectivityManager.getNetworkInfo(0).isConnected()) {
            return true;
            //WI-FI
        } else if (connectivityManager.getNetworkInfo(1).isConnected()) {
            return true;
        } else {
            return false;
            //SEM ACESSO NTERNET.
        }
    }
}
