package com.example.cuongdx.frequentpattern.service;

import com.example.cuongdx.frequentpattern.model.User;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface FileService {

    @POST("file")
    Call<FileResponse> file(@Query(value = "imei",encoded = true) String imei,@Query("ten") String ten,
                            @Query("lop") String lop,
                            @Query("malop") String malop,
                            @Query("masinhvien") String masinhvien,
                            @Query("mahocphan") String mahocphan, @Body RequestBody files);
    @GET("son")
    Call<User> getUser();
}
