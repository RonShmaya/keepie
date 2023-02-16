package com.ron.keepie.objects;

import com.ron.keepie.mytools.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUser {


    private String id;
    private DataManager.eUserType userType;
    private ArrayList<String> myListeners = new ArrayList<>();
    private ArrayList<String> myObservables = new ArrayList<>();

    public MyUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataManager.eUserType getUserType() {
        return userType;
    }

    public MyUser setUserType(DataManager.eUserType userType) {
        this.userType = userType;
        return this;
    }

    public ArrayList<String> getMyListeners() {
        return myListeners;
    }

    public MyUser setMyListeners(ArrayList<String> myListeners) {
        this.myListeners = myListeners;
        return this;
    }

    public ArrayList<String> getMyObservables() {
        return myObservables;
    }

    public MyUser setMyObservables(ArrayList<String> myObservables) {
        this.myObservables = myObservables;
        return this;
    }
}
