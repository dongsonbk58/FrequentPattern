package com.example.cuongdx.frequentpattern.service;

import com.example.cuongdx.frequentpattern.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dong Son on 26-Oct-17.
 */

public class ListStudent {

    @SerializedName("student")
    @Expose
    private ArrayList<User> student = new ArrayList<User>();

    public ArrayList<User> getListStudent() {
        return student;
    }

    public void setListStudent(ArrayList<User> student) {
        this.student = student;
    }
}
