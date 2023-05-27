package com.ron.keepie.objects;

import java.util.Objects;

public class KeepieUser {



    private boolean is_child;
    private String name = "";
    private String phone = "";
    private String image = "";


    public KeepieUser() {
    }

    public KeepieUser(String phone) {
        this.phone = phone;
    }

    public KeepieUser(boolean is_child, String name, String phone, String image) {
        this.is_child = is_child;
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public boolean isIs_child() {
        return is_child;
    }

    public KeepieUser setIs_child(boolean is_child) {
        this.is_child = is_child;
        return this;
    }

    public String getName() {
        return name;
    }

    public KeepieUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public KeepieUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getImage() {
        return image;
    }

    public KeepieUser setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public String toString() {
        return "KeepieUser{" +
                "is_child=" + is_child +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeepieUser that = (KeepieUser) o;
        return phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
