package com.alexandremarques.heroesdamarvel.util;

import android.util.Log;

import com.alexandremarques.heroesdamarvel.constants.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeraMD5 {

    public static String calcMD5(String s) {

        try {

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            //System.out.println("MD5: " + new BigInteger(1,m.digest()).toString(16));

            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            // exception
            Log.i(Constants.TAG, "Erro: " + e.getLocalizedMessage());
        }

        return "";
    }
}
