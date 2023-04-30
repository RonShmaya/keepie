package com.ron.keepie.objects;

import com.ron.keepie.mytools.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUser {



    private DataManager.eUserType userType;
    private String id= "";
    private String nick_name = "";
    private String phone = "";
    private String img_uri = "";


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

    public String getNick_name() {
        return nick_name;
    }

    public MyUser setNick_name(String nick_name) {
        this.nick_name = nick_name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public MyUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public MyUser setImg_uri(String img_uri) {
        this.img_uri = img_uri;
        return this;
    }
}
