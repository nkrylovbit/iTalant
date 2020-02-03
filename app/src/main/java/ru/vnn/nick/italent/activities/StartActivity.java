package ru.vnn.nick.italent.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.utils.ActivityUtils;

@EActivity(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {
    @AfterViews
    public void afterViews() {
        getWindow().setStatusBarColor(getColor(R.color.overlay));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Click(R.id.activity_start_button_signin)
    public void signin () {
        ActivityUtils.switchActivity(LoginActivity_.class, this);
        finish();
    }

    @Click(R.id.activity_start_button_signup)
    public void signup () {
        ActivityUtils.switchActivity(RegistrationActivity_.class, this);
        finish();
    }
}
