package com.ron.keepie.server.server_callbacks;

public interface SetUserCallback {
    void user_merged();
    void failed(int status_code,String info);
}
