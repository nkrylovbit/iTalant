package ru.vnn.nick.italent.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
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

@EActivity(R.layout.registration_page)
public class RegistrationActivity extends AppCompatActivity {
    String TAG = "RegistrationActivity";

    @ViewById(R.id.activity_registration_et_username)
    EditText username;

    @ViewById(R.id.activity_registration_et_password)
    EditText password;

    @AfterViews
    public void afterViews() {
        getWindow().setStatusBarColor(getColor(R.color.overlay));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Click(R.id.activity_registration_button_signin)
    public void openLoginActivity(View view) {
        ActivityUtils.switchActivity(LoginActivity_.class, this);
    }

    @Click(R.id.activity_registration_button_signup)
    public void register(View view) {
        if (username.getText() != null && password.getText() != null) {
            HttpUtils.signup(username.getText().toString(), password.getText().toString(), this);
        }
    }
}
