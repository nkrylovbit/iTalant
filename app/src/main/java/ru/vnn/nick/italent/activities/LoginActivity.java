package ru.vnn.nick.italent.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.net.HttpUtils;
import ru.vnn.nick.italent.utils.ActivityUtils;

@EActivity(R.layout.login_page)
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @ViewById(R.id.activity_login_et_username)
    EditText username;

    @ViewById(R.id.activity_login_et_password)
    EditText password;


    @AfterViews
    public void afterViews() {
        getWindow().setStatusBarColor(getColor(R.color.overlay));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Click(R.id.activity_login_tv_signup)
    public void openRegistrationActivity(View view) {
        ActivityUtils.switchActivity(RegistrationActivity_.class, this);
    }

    @Click(R.id.activity_login_button_login)
    public void login(View view) {
        final String userpass = username.getText().toString() + ":" + password.getText().toString();
        final String auth = "Basic " +
                Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT | Base64.NO_WRAP);

        if (username.getText() != null && password.getText() != null) {
            HttpUtils.signin(auth, this, true);
        }
    }
}
