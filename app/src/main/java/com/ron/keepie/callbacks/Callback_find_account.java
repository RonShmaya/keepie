package com.ron.keepie.callbacks;


import com.ron.keepie.objects.MyUser;

public interface Callback_find_account  {
    void account_found(MyUser account);
    void account_not_found();
    void error();
}