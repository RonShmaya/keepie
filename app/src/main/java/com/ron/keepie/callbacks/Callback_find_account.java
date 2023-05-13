package com.ron.keepie.callbacks;


import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.whatsup_objects.MyUser;

public interface Callback_find_account  {
    void account_found(MyUser account);
    void account_not_found();
    void error();
}