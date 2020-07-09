package com.concurseiro.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.concurseiro.Constants.Constants;

/**
 * Created by Alexandre on 20/08/2017.
 */

public class SecurityPreferences {

    private final SharedPreferences mSharedPreferences;

    public SecurityPreferences(Context context) {

        this.mSharedPreferences = context.getSharedPreferences(Constants.CHAVE_SEC, Context.MODE_PRIVATE);
    }

    public void storeString(String key, String value) {

        this.mSharedPreferences.edit().putString(key, value).apply();
    }

    public String getStoreString(String key) {
        return this.mSharedPreferences.getString(key, "");
    }
}
