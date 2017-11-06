package com.example.cuongdx.frequentpattern.service;

import com.example.cuongdx.frequentpattern.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;


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
