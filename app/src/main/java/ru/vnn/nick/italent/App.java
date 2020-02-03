package ru.vnn.nick.italent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.vnn.nick.italent.clients.AuthClient;
import ru.vnn.nick.italent.clients.MediaClient;
import ru.vnn.nick.italent.clients.PostClient;
import ru.vnn.nick.italent.clients.TalentsClient;
import ru.vnn.nick.italent.clients.UserClient;
import ru.vnn.nick.italent.net.AddCookiesInterceptor;
import ru.vnn.nick.italent.net.ReceivedCookiesInterceptor;

public class App extends Application {

    private static AuthClient authClient;
    public static final String MY_PREFS_NAME = "ITalentPrefsFile";
    private static Context context;
    private static final String url = "http://10.17.1.51:8080/italent-1.1/";

    public static PostClient getPostClient() {
        return postClient;
    }

    private static PostClient postClient;

    public static TalentsClient getTalentsClient () { return talentsClient; }

    private static TalentsClient talentsClient;

    private static UserClient userClient;

    public static UserClient getUserClient() { return userClient; }

    public static MediaClient getMediaClient() {
        return mediaClient;
    }

    private static MediaClient mediaClient;
    private Retrofit retrofit;
    private static String token;

    public static Integer getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt("id", 1);
    }

    private static Integer userId;

    @Override
    public void onCreate() {
        super.onCreate();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
//                .baseUrl(getString(R.string.server_url))
//                .baseUrl("http://10.17.1.44:8080/italent-1.1/")
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        authClient = retrofit.create(AuthClient.class);
        postClient = retrofit.create(PostClient.class);
        mediaClient = retrofit.create(MediaClient.class);
        userClient = retrofit.create(UserClient.class);
        talentsClient = retrofit.create(TalentsClient.class);

        context = getApplicationContext();

//        Picasso picassoInstance = new Picasso.Builder(this)
//                .loggingEnabled(BuildConfig.DEBUG)
//                .listener(new Picasso.Listener() {
//                    @Override
//                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                        Log.e("!!!", "uri was {" + uri + "} and exception = " + exception, exception);
//                    }
//                })
//                .defaultBitmapConfig(Bitmap.Config.RGB_565)
//                .indicatorsEnabled(BuildConfig.DEBUG)
//                .build();
//        Picasso.setSingletonInstance(picassoInstance);
    }

    public static AuthClient getAuthClient() {
        return authClient;
    }

    public static void updateToken(String token) {
        App.token = token;
    }

    public static void resetToken() {
        App.token = null;
    }

    public static void switchActivity (Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);

        context.startActivity(intent);
    }

    public static Context getAppContext() {
        return context;
    }

    public static String beUrl () {
        return url;
    }
}
