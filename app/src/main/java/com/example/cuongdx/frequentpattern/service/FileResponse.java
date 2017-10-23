package com.example.cuongdx.frequentpattern.service;

import com.example.cuongdx.frequentpattern.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;


public class FileResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("imei")
    private String imei;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isSuccess() {

        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
