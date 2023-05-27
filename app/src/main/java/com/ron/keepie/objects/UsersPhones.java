package com.ron.keepie.objects;

import java.util.List;

public class UsersPhones {



    private List<String> phones;


    public UsersPhones() {
    }

    public UsersPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getPhones() {
        return phones;
    }

    public UsersPhones setPhones(List<String> phones) {
        this.phones = phones;
        return this;
    }
}
