package com.ron.keepie.activities.adult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.ron.keepie.R;
import com.ron.keepie.activities.MyFirebaseMessagingService;
import com.ron.keepie.activities.SettingActivity;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.SetUserCallback;

public class ShieldActivity extends AppCompatActivity {
    private MaterialButton admin_data;
    private MaterialButton admin_alert;
    private BottomNavigationView nav_view;
    private KeepieUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiled);
        findViews();
        init_toolbar();
        myUser = DataManager.getDataManager().getMy_current_user();
        if(myUser.getPhone().equals("+972542262095")){
            admin_data.setVisibility(View.VISIBLE);
            admin_alert.setVisibility(View.VISIBLE);
            UserServerCommunicator.getInstance().setSetUserCallback(setUserCallback);
        }
    }

    private void findViews() {
        admin_data = findViewById(R.id.admin_data);
        admin_data.setOnClickListener(view -> {
            MyFirebaseMessagingService.add_topic("test");
            UserServerCommunicator.getInstance().admin_data();
        });
        admin_alert = findViewById(R.id.admin_alert);
        admin_alert.setOnClickListener(view -> {
            MyFirebaseMessagingService.add_topic("test");
            UserServerCommunicator.getInstance().admin_alert();
        });
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
                go_next(ShieldActivity.class);
            }
            return false;
        });

    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
    private SetUserCallback setUserCallback= new SetUserCallback() {
        @Override
        public void user_merged() {
            Toast.makeText(ShieldActivity.this, "OK", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(ShieldActivity.this, "Error: "+status_code+" -> "+info, Toast.LENGTH_LONG).show();
        }
    };
}