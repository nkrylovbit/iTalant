package ru.vnn.nick.italent.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import java.nio.file.Path;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.App;
import ru.vnn.nick.italent.activities.ApplyActivity;
import ru.vnn.nick.italent.activities.ApplyActivity_;
import ru.vnn.nick.italent.activities.MainActivity_;
import ru.vnn.nick.italent.dto.EmailPassword;
import ru.vnn.nick.italent.dto.UserJSON;
import ru.vnn.nick.italent.utils.Methods;

import static ru.vnn.nick.italent.utils.ActivityUtils.switchActivity;
import static ru.vnn.nick.italent.utils.Methods.PREF_FILE;

public class HttpUtils {
    private static final int UNAUTHORIZED = 401;

    public static void signup (String username, String password, Context context) {
        App
                .getAuthClient()
                .register(new EmailPassword(username, password))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        if (response.isSuccessful()) {
                            String userpass = username + ":" + password;

                            SharedPreferences mcpPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mcpPreferences.edit();
                            editor.putString("username", username).apply();


                            String auth = "Basic " +
                                    Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT | Base64.NO_WRAP);

                            signin(auth, context, false);

                            switchActivity(ApplyActivity_.class, context);
                            ((AppCompatActivity) context).finish();
                        } else {
                            Toast.makeText(
                                    context,
                                    "Registration error, please try again",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(
                                context,
                                "Network error, please try again",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    public static void signin (String auth, Context context, boolean withSwitch) {
        App
                .getAuthClient()
                .login(auth)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences mcpPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mcpPreferences.edit();

                            if (!Methods.getCookies(App.getAppContext()).isEmpty()) {
                                editor.putString("wasUser", "yes").apply();
                            }

                            if (withSwitch) {
                                switchActivity(MainActivity_.class, context);
                                ((AppCompatActivity) context).finish();
                            }
                        } else {
                            if (response.code() == UNAUTHORIZED) {
                                Toast.makeText(
                                        context,
                                        "Incorrect login or password",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(
                                context,
                                "Network error, please try again",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    public static void checkCode (String code, Context context) {
        App.getAuthClient().checkCode(Integer.parseInt(code)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ((ApplyActivity_) context).finalStep();
                } else {
                    Toast.makeText(context, "Incorrect code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void updateUser (UserJSON userJSON, Context context) {
        App.getUserClient().updateUser(userJSON).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    switchActivity(MainActivity_.class, context);
                    ((AppCompatActivity) context).finish();
                } else {
                    Toast.makeText(context, "Server error, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
