package ru.vnn.nick.italent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class Methods {
    public static final String PREF_FILE = "iT_Prefs";

    public static HashSet<String> getCookies(Context context) {
        SharedPreferences mcpPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
    }

    public static boolean setCookies(Context context, HashSet<String> cookies) {
        SharedPreferences mcpPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }

    public static void clearCookie (Context context) {
        SharedPreferences mcpPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        editor.remove("cookies").apply();
    }
}
