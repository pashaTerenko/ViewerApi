package com.terenko.viewerapi.API;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API {

    @GET("api/v1/hot")
    Call<List<String>> getPostId();

    @GET("api/v1/post/{id}")
    Call<Responce> getPost(@Path("id") String id);
}
