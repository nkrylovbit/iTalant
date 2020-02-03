package ru.vnn.nick.italent.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;

import ru.vnn.nick.italent.App;
import ru.vnn.nick.italent.activities.MainActivity;
import ru.vnn.nick.italent.utils.Methods;

import static ru.vnn.nick.italent.utils.Methods.PREF_FILE;

public class InitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mcpPreferences = this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

//        Methods.clearCookie(App.getAppContext());

        Intent intent = new Intent(
                InitActivity.this, // MainActivity_.class
                mcpPreferences.getString("wasUser", null) == null ? StartActivity_.class :
                needToLogin() ? LoginActivity_.class : MainActivity_.class
        );

        startActivity(intent);
        finish();
    }

    private boolean needToLogin () {
        if (Methods.getCookies(App.getAppContext()).isEmpty()) {
            return true;
        }

        return !Methods.getCookies(App.getAppContext()).contains("remember-me");

    }
}
