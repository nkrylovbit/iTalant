package ru.vnn.nick.italent.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.dto.UserJSON;
import ru.vnn.nick.italent.net.HttpUtils;

import static ru.vnn.nick.italent.utils.Methods.PREF_FILE;

@EActivity(R.layout.activity_apply_profile)
public class ApplyActivity extends AppCompatActivity {
    @ViewById(R.id.activity_apply_et_code)
    EditText code;

    @ViewById(R.id.activity_apply_et_firstname)
    EditText firstname;

    @ViewById(R.id.activity_apply_et_lastname)
    EditText lastname;

    @ViewById(R.id.activity_apply_et_description)
    EditText description;

    @ViewById(R.id.activity_apply_ll_code)
    LinearLayout llCode;

    @ViewById(R.id.activity_apply_ll_aboutme)
    LinearLayout llAboutMe;

    @AfterViews
    public void afterViews(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @AfterTextChange(R.id.activity_apply_et_code)
    public void verifyCode () {
        if (code.getText().toString().length() == 4) {
            HttpUtils.checkCode(code.getText().toString(), this);
        }
    }

    public void finalStep () {
        llCode.setVisibility(View.GONE);
        llAboutMe.setVisibility(View.VISIBLE);
    }

    @Click(R.id.activity_apply_button_onboard)
    public void onBoard() {
        if (firstname.getText().toString().isEmpty() ||
                lastname.getText().toString().isEmpty() ||
                description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please, fill all fields", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        SharedPreferences mcpPreferences = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        String username = mcpPreferences.getString("username", "");

        UserJSON userJSON = new UserJSON();
        userJSON.setUsername(username);
        userJSON.setFirstname(firstname.getText().toString());
        userJSON.setLastname(lastname.getText().toString());
        userJSON.setDescription(description.getText().toString());

        HttpUtils.updateUser(userJSON, this);
    }
}
