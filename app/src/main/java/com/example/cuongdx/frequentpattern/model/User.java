package com.example.cuongdx.frequentpattern.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dong Son on 13-Oct-17.
 */

public class User implements Serializable{
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("lop")
    @Expose
    private String lop;
    @SerializedName("mssv")
    @Expose
    private String mssv;
    @SerializedName("malop")
    @Expose
    private String malop;
    @SerializedName("mahocphan")
    @Expose
    private String mahocphan;
    @SerializedName("imei")
    @Expose
    private String imei;


    public User(String ten, String lop, String mssv, String malop, String mahocphan, String imei) {
        this.ten = ten;
        this.lop = lop;
        this.mssv = mssv;
        this.malop = malop;
        this.mahocphan = mahocphan;
        this.imei = imei;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMahocphan() {
        return mahocphan;
    }

    public void setMahocphan(String mahocphan) {
        mahocphan = mahocphan;
    }
}
