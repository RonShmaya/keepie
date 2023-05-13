package com.ron.keepie.whatsup_objects;

public class UserStatus {
    private boolean connected;
    private String phone;
    private String img="";
    private MyTime last_seen = new MyTime();


    public UserStatus() {
    }

    public boolean isConnected() {
        return connected;
    }

    public UserStatus setConnected(boolean connected) {
        this.connected = connected;
        return this;
    }

    public MyTime getLast_seen() {
        return last_seen;
    }

    public UserStatus setLast_seen(MyTime last_seen) {
        this.last_seen = last_seen;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserStatus setPhone(String phone) {
        this.phone = phone;
        return this;
    }


    public String getImg() {
        return img;
    }

    public UserStatus setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "connected=" + connected +
                ", phone='" + phone + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}