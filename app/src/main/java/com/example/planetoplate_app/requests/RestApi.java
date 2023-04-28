package com.example.planetoplate_app.request_models;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {

    String BASE_URL = "http://192.168.0.121:8080";

    @POST("/api/auth/signup/")
    Call<ResponseBody> getConfirmCode(@Body UserInfo info);
}
