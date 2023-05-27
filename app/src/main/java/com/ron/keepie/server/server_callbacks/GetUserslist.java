package com.ron.keepie.server.server_callbacks;

import com.ron.keepie.objects.KeepieUser;

import java.util.List;

public interface GetUserslist {
    void get_users_list(List<KeepieUser> users);
    void failed(int status_code,String info);
}
