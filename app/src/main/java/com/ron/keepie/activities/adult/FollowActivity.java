package com.ron.keepie.activities.adult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ron.keepie.R;
import com.ron.keepie.activities.MyFirebaseMessagingService;
import com.ron.keepie.activities.SettingActivity;
import com.ron.keepie.activities.child.SearchConnectionsActivity;
import com.ron.keepie.adapters.ConnectiosAdapterAdult;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;
import com.ron.keepie.objects.UsersPhones;
import com.ron.keepie.server.TrackServerCommunicator;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.GetUserslist;
import com.ron.keepie.server.server_callbacks.TrackCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FollowActivity extends AppCompatActivity {
    private BottomNavigationView nav_view;
    private ConnectiosAdapterAdult followersAdapter;
    private KeepieUser myUser;
    private ArrayList<KeepieUser> myFollows = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();
    private RecyclerView follow_LST;
    private LinearLayout follow_LAY_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        myUser = DataManager.getDataManager().getMy_current_user();
        UserServerCommunicator.getInstance().setGetUsersListCallback(usersListCallback);
        TrackServerCommunicator.getInstance().setTrackCallback(trackCallback);
        TrackServerCommunicator.getInstance().getTracks(myUser.getPhone(), false);
        init_toolbar();
        findViews();

        followersAdapter = new ConnectiosAdapterAdult(this, myFollows);
        followersAdapter.setListener(clickedUser);
        follow_LST.setAdapter(followersAdapter);

    }

    private void findViews() {
        follow_LST = findViewById(R.id.follow_LST);
        follow_LAY_empty = findViewById(R.id.follow_LAY_empty);

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

    private GetUserslist usersListCallback = new GetUserslist() {
        @Override
        public void get_users_list(List<KeepieUser> users) {
            found_users(users);

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(FollowActivity.this, "Error Occurred " + status_code + ": " + info, Toast.LENGTH_SHORT).show();
        }
    };

    private void found_users(List<KeepieUser> users) {
        this.myFollows.clear();
        this.myFollows.addAll(users);
        HashMap<KeepieUser, Track> users_track_map = new HashMap<>();
        for (KeepieUser us : users) {
            tracks.stream().filter(trac -> trac.getPhone_child().equals(us.getPhone())).findFirst().ifPresent(track ->
                    users_track_map.put(us, track));
        }
        followersAdapter.setUser_tracks(users_track_map);
        followersAdapter.notifyDataSetChanged();

        Log.d("MyLog", Arrays.toString(users.toArray()));
    }

    private TrackCallback trackCallback = new TrackCallback() {
        @Override
        public void get_tracks(List<Track> tracks) {
            tracks = tracks.stream().filter(trk -> !trk.isDenied()).collect(Collectors.toList());
            found_tracks(tracks);
        }

        @Override
        public void track_updated() {
            followersAdapter.notifyDataSetChanged();
            MyFirebaseMessagingService.add_topic(current_track.getPhone_child().substring(1));
        }

        @Override
        public void track_created() {
        }

        @Override
        public void track_deleted() {
            followersAdapter.notifyDataSetChanged();
            MyFirebaseMessagingService.remove_topic(current_track.getPhone_child().substring(1));
            if(myFollows.isEmpty())
                follow_LAY_empty.setVisibility(View.VISIBLE);

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(FollowActivity.this, "Error Occurred " + status_code + ": " + info, Toast.LENGTH_SHORT).show();
        }
    };

    private void found_tracks(List<Track> tracks) {
        this.tracks = tracks;
        if (!tracks.isEmpty())
            UserServerCommunicator.getInstance().getUsersList(
                    new UsersPhones(
                            tracks.stream().map(trk -> trk.getPhone_child()).collect(Collectors.toList())
                    )
            );
        if(this.tracks.isEmpty())
            follow_LAY_empty.setVisibility(View.VISIBLE);
        Log.d("MyLog", "trackCallback " + Arrays.toString(tracks.toArray()));
    }

    private ConnectiosAdapterAdult.ClickedUser clickedUser = new ConnectiosAdapterAdult.ClickedUser() {

        @Override
        public void change_status(KeepieUser contact, int position, boolean is_approved, Track track) {
            action_user_clicked(contact, position, is_approved, track);
        }
    };
    private Track current_track;
    private void action_user_clicked(KeepieUser contact, int position, boolean is_approved, Track track) {
        if(track == null){
            Log.d("MyLog","this is very bad");
            return;
        }
        current_track= track;
        if (is_approved) {
            track.setApproved(true);
            TrackServerCommunicator.getInstance().updateTrack(track);
        } else {
            TrackServerCommunicator.getInstance().deleteTrack(track.getPhone_child(),track.getPhone_adult());
            myFollows.remove(contact);

        }
    }

}