package com.ron.keepie.callbacks;

import com.ron.keepie.whatsup_objects.UserStatus;

public interface Callback_user_status {
    void get_user_status(UserStatus userStatus);
    void not_found();
    void error();
}
