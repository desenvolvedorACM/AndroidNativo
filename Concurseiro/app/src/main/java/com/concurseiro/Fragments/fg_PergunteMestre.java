package com.concurseiro.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.concurseiro.R;

/**
 * Created by Alexandre on 14/11/2017.
 */

public class fg_PergunteMestre extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fg_activity_pergunte_mestre, container, false);

        return layout;
    }
}
