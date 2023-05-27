package com.ron.keepie.activities.adult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.reflect.TypeToken;
import com.ron.keepie.R;
import com.ron.keepie.activities.SettingActivity;
import com.ron.keepie.adapters.NotificationAdapter;
import com.ron.keepie.mytools.MySP;
import com.ron.keepie.objects.Notification;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsActivity extends AppCompatActivity {
    private BottomNavigationView nav_view;
    private LinearLayout note_LAY_empty;
    private RecyclerView note_LST;
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notifications = MySP.get_my_SP().getArray(MySP.NOTIFICATION,new TypeToken<ArrayList<Notification>>() {} );
        findViews();
        init_toolbar();
        if(notifications.isEmpty()){
            update_showing(View.VISIBLE, View.GONE);
        }
        else{
            update_showing(View.GONE, View.VISIBLE);
        }
        notificationAdapter = new NotificationAdapter(this, notifications);
        notificationAdapter.setChatListener(userListener);
        note_LST.setAdapter(notificationAdapter);


    }
    public void update_showing(int vis_empty, int vis_list){
        note_LAY_empty.setVisibility(vis_empty);
        note_LST.setVisibility(vis_list);
    }
    private void findViews() {
        note_LST = findViewById(R.id.note_LST);
        note_LAY_empty = findViewById(R.id.note_LAY_empty);
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
    private  NotificationAdapter.UserListener userListener = (notification, position) -> {
        notifications.remove(notification);
        MySP.get_my_SP().putArray(MySP.NOTIFICATION, notifications);
        notificationAdapter.notifyDataSetChanged();
        if(notifications.isEmpty()){
            update_showing(View.VISIBLE, View.GONE);
        }
    };

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}