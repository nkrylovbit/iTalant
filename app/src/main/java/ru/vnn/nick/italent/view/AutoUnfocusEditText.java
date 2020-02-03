package ru.vnn.nick.italent.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class AutoUnfocusEditText extends android.support.v7.widget.AppCompatEditText {

    private final Handler handler = new Handler();
    private final Runnable onBackPressed = new Runnable() {
        @Override
        public void run() {
            clearFocus();
        }
    };
    private static final Set<Integer> allowedKeyCodes = new HashSet<>(Arrays.asList(KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_BACK));

    public AutoUnfocusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public final boolean onKeyPreIme(int keyCode, KeyEvent event) {
        clearFocusForKeyCode(keyCode);

        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        clearFocusForKeyCode(keyCode);

        return super.onKeyDown(keyCode, event);
    }

    private void clearFocusForKeyCode(final int keyCode) {
        if (allowedKeyCodes.contains(keyCode)) {
            handler.post(onBackPressed);
        }
    }
}
