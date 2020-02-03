package ru.vnn.nick.italent.clients;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.vnn.nick.italent.dto.User;
import ru.vnn.nick.italent.dto.UserJSON;

public interface UserClient {
    @GET("/api/users")
    Call<List<User>> getUsers();

    @GET("/api/users/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("user/")
    Call<UserJSON> getMyUser();

    @POST("user/")
    Call<Void> updateUser (@Body UserJSON userJSON);
}
