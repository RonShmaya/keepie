package com.ron.keepie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ron.keepie.R;
import com.ron.keepie.activities.adult.FollowActivity;
import com.ron.keepie.activities.adult.NotificationsActivity;

public class SettingActivity extends AppCompatActivity {
    private CoordinatorLayout container;
    private BottomNavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init_toolbar();
        findViews();
    }

    private void findViews() {
        container = findViewById(R.id.container); // TODO: 16/04/2023 Verify if visable or not base on userType
    }


    private void init_toolbar() {
        nav_view = findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_followers) {
                go_next(FollowActivity.class);
            } else if (item.getItemId() == R.id.action_profile) {
                go_next(SettingActivity.class);
            } else if (item.getItemId() == R.id.action_notify) {
                go_next(NotificationsActivity.class);
            } else if (item.getItemId() == R.id.action_test) {
                // go_next(Activity_private_account_profile.class);
            }
            return false;
        });

    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}