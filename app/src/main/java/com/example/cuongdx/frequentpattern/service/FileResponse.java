package com.example.cuongdx.frequentpattern.service;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;


public class FileResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("filelist")
    private HashMap<String, Boolean> filelist;
    @SerializedName("imei")
    private String imei;

    public HashMap<String, Boolean> getFilelist() {
        return filelist;
    }

    public void setFilelist(HashMap<String, Boolean> filelist) {
        this.filelist = filelist;
    }

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
