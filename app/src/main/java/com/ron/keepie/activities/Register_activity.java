package com.ron.keepie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;

import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.mytools.MyDB;


public class Register_activity extends AppCompatActivity {
    private MaterialButton connect_BTN_reg_adult;
    private MaterialButton connect_BTN_reg_adolescent;
    private FloatingActionButton fab_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        init_actions();


    }

    private void init_actions() {
        connect_BTN_reg_adult.setOnClickListener(view -> go_to_settings(DataManager.SETTING_TYPE_ADULT));
        connect_BTN_reg_adolescent.setOnClickListener(view -> go_to_settings(DataManager.SETTING_TYPE_CHILD) );
        fab_back.setOnClickListener(view -> go_next(Login_activity.class));
    }

    private void findViews() {
        connect_BTN_reg_adult = findViewById(R.id.connect_BTN_reg_adult);
        connect_BTN_reg_adolescent = findViewById(R.id.connect_BTN_reg_adolescent);
        fab_back = findViewById(R.id.fab_back);
    }
    private  void go_to_settings(String type) {
        Intent intent = new Intent(this, NewUserSettingActivity.class);
        intent.putExtra(DataManager.SETTING_TYPE,type);
        startActivity(intent);
        finish();
    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }



}