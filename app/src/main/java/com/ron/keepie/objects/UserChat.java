package com.ron.keepie.objects;

import androidx.annotation.Nullable;

public class UserChat {
    // TODO: 09/02/2023 photo
    private String phone = "";
    private String name = "";
    private String img = "";
    private String contact_name = "";
    private boolean typing;
    private int unread;

    public UserChat() {
    }

    public String getPhone() {
        return phone;
    }

    public UserChat setPhone(String phone) {
        this.phone = phone;
        return this;
    }


    public boolean isTyping() {
        return typing;
    }

    public UserChat setTyping(boolean typing) {
        this.typing = typing;
        return this;
    }


    public String getName() {
        return name;
    }

    public UserChat setName(String name) {
        this.name = name;
        return this;
    }

    public int getUnread() {
        return unread;
    }

    public UserChat setUnread(int unread) {
        this.unread = unread;
        return this;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UserChat))
            return false;
        UserChat other = (UserChat) obj;
        return this.phone.equals(other.phone);
    }

    public String getContact_name() {
        return contact_name;
    }

    public UserChat setContact_name(String contact_name) {
        this.contact_name = contact_name;
        return this;
    }

    public String getImg() {
        return img;
    }

    public UserChat setImg(String img) {
        this.img = img;
        return this;
    }
}
