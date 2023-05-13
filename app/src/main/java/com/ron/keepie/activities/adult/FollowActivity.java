package com.ron.keepie.activities.adult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ron.keepie.R;
import com.ron.keepie.activities.SettingActivity;
import com.ron.keepie.adapters.FollowersAdapter;
import com.ron.keepie.objects.KeepieUser;

import java.util.ArrayList;
import java.util.Arrays;

public class FollowActivity extends AppCompatActivity {
    private BottomNavigationView nav_view;
    private FollowersAdapter followersAdapter;
    private ArrayList<KeepieUser> myFollows = new ArrayList<>();
    private RecyclerView follow_LST;
    private LinearLayout follow_LAY_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        init_toolbar();
        findViews();
        myFollows = new ArrayList<>(Arrays.asList(
           new KeepieUser().setName("dsa").setPhone("0434320423") ,
           new KeepieUser().setName("dsa").setPhone("0434320423") ,
           new KeepieUser().setName("ddwewedsa").setPhone("0434320423") ,
           new KeepieUser().setName("ddsfdssa").setPhone("0434320423") ,
           new KeepieUser().setName("dsads").setPhone("0434320423") ,
           new KeepieUser().setName("dsfdsa").setPhone("0434320423")
        ));
        followersAdapter = new FollowersAdapter(this, myFollows);
        follow_LST.setAdapter(followersAdapter);
    }

    private void findViews() {
        follow_LST = findViewById(R.id.follow_LST);
        follow_LAY_empty = findViewById(R.id.follow_LAY_empty);

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