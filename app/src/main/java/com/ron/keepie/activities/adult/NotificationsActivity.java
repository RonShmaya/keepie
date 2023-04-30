package com.ron.keepie.activities.adult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.ron.keepie.R;
import com.ron.keepie.activities.SettingActivity;

public class NotificationsActivity extends AppCompatActivity {
    private BottomNavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        init_toolbar();
    }

    private void init_toolbar() {
        nav_view = findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.action_followers){
                go_next(FollowActivity.class);
            }
            else if(item.getItemId() == R.id.action_profile){
                go_next(SettingActivity.class);
            }
            else if(item.getItemId() == R.id.action_notify){
                go_next(NotificationsActivity.class);
            }
            else if(item.getItemId() == R.id.action_test){
               // go_next(Activity_private_account_profile.class);
            }
            return false;
        });

    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}