package com.ron.keepie.server.server_callbacks;

import com.ron.keepie.objects.KeepieUser;

public interface GetUserCallback {
    void get_user(KeepieUser user);
    void not_found_user();
    void failed(int status_code,String info);
}
