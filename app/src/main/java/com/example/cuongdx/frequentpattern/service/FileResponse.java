package com.example.cuongdx.frequentpattern.service;

import com.google.gson.annotations.SerializedName;


public class FileResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {

        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
