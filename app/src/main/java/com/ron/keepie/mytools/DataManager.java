package com.ron.keepie.mytools;

import com.ron.keepie.R;
import com.ron.keepie.objects.MyUser;

public class DataManager {


    public static enum eUserType {observer, observable }
    private MyUser my_current_user = null;
    private static DataManager _instance = new DataManager();


    private DataManager() {
    }


    public static DataManager getDataManager() {
        return _instance;
    }

    public DataManager set_account(MyUser account) {
        my_current_user = account;
        return this;
    }

}