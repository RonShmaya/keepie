package com.ron.keepie.mytools;

import com.ron.keepie.objects.KeepieUser;

public class DataManager {


    public static final String SETTING_TYPE = "SETTING_TYPE";
    public static final String SETTING_TYPE_CHILD = "SETTING_TYPE_CHILD";
    public static final String SETTING_TYPE_ADULT = "SETTING_TYPE_ADULT";
    private KeepieUser my_current_user = null;
    private static DataManager _instance = new DataManager();


    private DataManager() {
    }


    public static DataManager getDataManager() {
        return _instance;
    }

    public DataManager set_account(KeepieUser account) {
        my_current_user = account;
        return this;
    }

    public KeepieUser getMy_current_user() {
        return my_current_user;
    }
}