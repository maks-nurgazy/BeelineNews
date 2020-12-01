package com.beeline.task.utils;

import com.beeline.task.models.Articles;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

//    @GET(Constants.API_TOP_HEADLINES)
//    Call<Articles> getArticles();

    @GET("v2/everything?q=bitcoin")
    Call<Articles> getArticles(@Query("page") int page);

}
