package ru.vnn.nick.italent.clients;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MediaClient {
    @GET("{path}")
    Call<ResponseBody> getMedia(@Path("path") String path);
}
