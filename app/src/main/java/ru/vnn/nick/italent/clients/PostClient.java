package ru.vnn.nick.italent.clients;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.vnn.nick.italent.dto.Post;

public interface PostClient {

    @GET("/api/posts")
    Call<List<Post>> getPosts();

    @GET("posts")
    Call<List<Post>> getPosts(@Query("username") String username);

    @Multipart
    @POST("upload/file")
    Call<Map<String, Object>> uploadMedia (@Part MultipartBody.Part file,
                                           @Part("description") RequestBody description);//@Body RequestBody photo);

    @POST("posts/save")
    Call<Map> uploadPost (@Body Map<String, Object> post);

    @GET("posts/")
    Call<List<Map<String, Object>>> getAllPosts ();
}
