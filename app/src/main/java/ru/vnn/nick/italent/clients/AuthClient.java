package ru.vnn.nick.italent.clients;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.vnn.nick.italent.dto.EmailPassword;
import ru.vnn.nick.italent.dto.AuthToken;

public interface AuthClient {

    @Headers("Content-Type: application/json")
    @POST("loginAction")
    Call<Void> login(@Header("Authorization") String auth);

    @POST("signup")
    @Headers("Content-Type: application/json")
    Call<Void> register(@Body EmailPassword emailPassword);

    @POST("signup/code")
    Call<Void> checkCode(@Body int code);
}
