package ru.vnn.nick.italent.utils;

import android.content.Context;
import android.content.Intent;

public class ActivityUtils {
    public static void switchActivity (Class clazz, Context context) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
}
