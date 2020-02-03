package ru.vnn.nick.italent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;

import ru.vnn.nick.italent.fragments.FragmentWithParams;


public class InterlocutionsFragment extends Fragment implements FragmentWithParams {
    public InterlocutionsFragment () {}

    public static Fragment newInstance () {
        return new InterlocutionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_interlocutions, container, false);
//
//        final ScrollView sv = view.findViewById(R.id.sv_interlocutions);
//
//        TextView tv = new TextView(view.getContext());
//        tv.setText("No messages");
//
//        sv.addView(tv);

        return view;
    }

    @Override
    public void provideParams(Map params) {

    }
}
