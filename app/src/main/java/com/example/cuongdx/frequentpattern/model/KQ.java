package com.example.cuongdx.frequentpattern.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dong Son on 26-May-18.
 */

public class KQ implements Serializable {
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("kq")
    @Expose
    private String kq;

    public KQ(String ten, String kq) {
        super();
        this.ten = ten;
        this.kq = kq;
    }
    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public String getKq() {
        return kq;
    }
    public void setKq(String kq) {
        this.kq = kq;
    }
}
