package com.example.cuongdx.frequentpattern.model;

import java.io.Serializable;

/**
 * Created by Dong Son on 13-Oct-17.
 */

public class User implements Serializable{
    private String ten;
    private String lop;
    private String mssv;
    private String malop;
    private String Mahocphan;

    public User(String ten, String lop, String mssv, String malop, String mahocphan) {
        this.ten = ten;
        this.lop = lop;
        this.mssv = mssv;
        this.malop = malop;
        this.Mahocphan = mahocphan;
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

    public String getMahocphan() {
        return Mahocphan;
    }

    public void setMahocphan(String mahocphan) {
        Mahocphan = mahocphan;
    }
}
