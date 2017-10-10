package com.example.cuongdx.frequentpattern.service;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface FileService {

    @POST("file")
//    Call<FileResponse> file(@Body RequestBody files);
    Call<FileResponse> file( @Query(value = "imei",encoded = true) String imei, @Body RequestBody files);
    @POST("/file/")
    public void insertUser(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            Callback<Response> callback);
}
