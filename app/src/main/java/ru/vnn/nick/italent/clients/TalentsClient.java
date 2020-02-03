package ru.vnn.nick.italent.clients;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.vnn.nick.italent.dto.Talent;

public interface TalentsClient {
    @GET("/api/talents/{findstring}")
    Call<List<Talent>> getTalents(@Path("findstring") String findstring);
}
