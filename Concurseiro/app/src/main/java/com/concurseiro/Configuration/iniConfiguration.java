package com.concurseiro.Configuration;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import com.concurseiro.Constants.Constants;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;

/**
 * Created by user4 on 16/08/2017.
 */

public class iniConfiguration extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //CONFIGURAÇÕES DO FACEBOOK.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        //CONFIGURAÇÕES DO TWITER.
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        //CONFIGURAÇÕES DO Realm.
        /*RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);*/

        //CONFIGURAÇÕES DO OneSignal.

    }
}
