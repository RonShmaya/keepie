package com.ron.keepie.mytools;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MyServices {
    private static MyServices _instance = null;
    private final int PHONE_LEN = 13;
    private Context context;
    private HashMap<String, String> my_contacts = new HashMap<>();

    private MyServices(Context context) {
        this.context = context.getApplicationContext();

    }

    public Context getContext() {
        return context;
    }

    public static void initHelper(Context context) {
        if (_instance == null) {
            _instance = new MyServices(context);
        }
    }

    public static MyServices getInstance(){
        return _instance;
    }

    private HashMap<String, String> get_all_contacts(Context appCompatActivity){
        Cursor phones = appCompatActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String phone = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (phone.contains("+972")) {
                phone = phone.replaceAll("[^\\d]", "");
                phone = "+" + phone;
                if (phone.length() == PHONE_LEN)
                    this.my_contacts.put(phone, name);
            } else if (phone.startsWith("05")) {
                phone = phone.replaceAll("[^\\d]", "");
                phone = phone.replaceFirst("0", "+972");
                if (phone.length() == PHONE_LEN)
                    this.my_contacts.put(phone, name);
            }
        }
        phones.close();
        return this.my_contacts;
    }
    public synchronized HashMap<String, String> get_user_all_contacts_sync(){
        if(!this.my_contacts.isEmpty())
            return this.my_contacts;
        return get_all_contacts(this.context);
    }
}
