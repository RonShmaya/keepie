package com.ron.keepie;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.mytools.MyServices;
import com.ron.keepie.mytools.Permissions;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyServices.initHelper(this);
        new Thread(
                () -> {
                    if(Permissions.all_permissions_ok(this))
                        MyServices.getInstance().get_user_all_contacts_sync();
                }
        ).start();


    }
}
