package com.ron.keepie.objects;

public class Track {



    private String phone_child = "";
    private String phone_adult = "";
    private boolean approved;
    private boolean denied;

    public Track() {
    }

    public String getPhone_child() {
        return phone_child;
    }

    public Track setPhone_child(String phone_child) {
        this.phone_child = phone_child;
        return this;
    }

    public String getPhone_adult() {
        return phone_adult;
    }

    public Track setPhone_adult(String phone_adult) {
        this.phone_adult = phone_adult;
        return this;
    }

    public boolean isApproved() {
        return approved;
    }

    public Track setApproved(boolean approved) {
        this.approved = approved;
        return this;
    }

    public boolean isDenied() {
        return denied;
    }

    public Track setDenied(boolean denied) {
        this.denied = denied;
        return this;
    }

    @Override
    public String toString() {
        return "Track{" +
                "phone_child='" + phone_child + '\'' +
                ", phone_adult='" + phone_adult + '\'' +
                ", approved=" + approved +
                ", denied=" + denied +
                '}';
    }
}
