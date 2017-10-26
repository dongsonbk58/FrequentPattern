package com.example.cuongdx.frequentpattern.service;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface FileService {

    @POST("file")
    Call<FileResponse> file(@Query(value = "imei", encoded = true) String imei, @Query("ten") String ten,
                            @Query("lop") String lop,
                            @Query("malop") String malop,
                            @Query("masinhvien") String masinhvien,
                            @Query("mahocphan") String mahocphan, @Body RequestBody files);

    @GET("student.json")
    Call<JsonObject> readJson();
}
