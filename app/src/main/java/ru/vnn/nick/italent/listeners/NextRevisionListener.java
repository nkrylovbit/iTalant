package ru.vnn.nick.italent.listeners;

import android.view.View;
import android.widget.Toast;

public class NextRevisionListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Will be available after fubruary 2019.", Toast.LENGTH_SHORT).show();
    }
}
