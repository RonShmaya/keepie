package com.ron.keepie.activities.child;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ron.keepie.R;

import com.ron.keepie.activities.SettingActivity;
import com.ron.keepie.adapters.ConnectiosAdapter;
import com.ron.keepie.adapters.SearchingAdapter;

import com.ron.keepie.callbacks.Callback_permissions;
import com.ron.keepie.dialogs.ImageDialog;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.mytools.MyServices;
import com.ron.keepie.mytools.Permissions;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;
import com.ron.keepie.objects.UsersPhones;
import com.ron.keepie.server.TrackServerCommunicator;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.GetUserCallback;

import com.ron.keepie.server.server_callbacks.GetUserslist;
import com.ron.keepie.server.server_callbacks.TrackCallback;
import com.ron.keepie.whatsup_objects.MyContact;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

public class SearchConnectionsActivity extends AppCompatActivity {


    private KeepieUser myUser;
    private ArrayList<MyContact> find_lst = new ArrayList<>();
    private ArrayList<MyContact> all_contacts = new ArrayList<>();
    private SearchView speaking_SV;
    private RecyclerView search_LST;
    private LinearLayout follow_LAY_empty;
    private FloatingActionButton fab_home;
    private RecyclerView connections_LST;
    private SearchingAdapter searchingAdapter;
    private ScrollView scroll_search;
    private ScrollView scroll_connect;
    private ConnectiosAdapter connectiosAdapter;
    private HashMap<String, String> my_contacts = new HashMap<>();
    private List<Track> tracks = new ArrayList<>();
    private ArrayList<KeepieUser> users_tracking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_connections);
        myUser = DataManager.getDataManager().getMy_current_user();
        UserServerCommunicator.getInstance().setGetUserCallback(userCallback);
        UserServerCommunicator.getInstance().setGetUsersListCallback(usersListCallback);
        TrackServerCommunicator.getInstance().setTrackCallback(trackCallback);
        TrackServerCommunicator.getInstance().getTracks(myUser.getPhone(),true);
        findViews();

        searchingAdapter = new SearchingAdapter(this, find_lst);
        searchingAdapter.setChatListener(userListener);
        search_LST.setAdapter(searchingAdapter);


        connectiosAdapter = new ConnectiosAdapter(this, users_tracking);
        connections_LST.setAdapter(connectiosAdapter);

        init_actions();

        Permissions.initHelper(this);
        if (!Permissions.getPermissions().all_permissions_ok()) {
            Permissions.getPermissions().setCallback_permissions(callback_permissions);
            Permissions.getPermissions().requestContacts();
            return;
        }
        pull_contacts();

    }

    private void pull_contacts() {
        HashMap<String, String> my_contacts = MyServices.getInstance().get_user_all_contacts_sync();
        my_contacts.forEach(
                (ph, name) -> {
                    all_contacts.add(new MyContact().setName(name).setPhone(ph));
                }
        );
    }

    private void findViews() {
        search_LST = findViewById(R.id.search_LST);
        follow_LAY_empty = findViewById(R.id.follow_LAY_empty);
        fab_home = findViewById(R.id.fab_home);
        connections_LST = findViewById(R.id.connections_LST);
        speaking_SV = findViewById(R.id.speaking_SV);
        scroll_search = findViewById(R.id.scroll_search);
        scroll_connect = findViewById(R.id.scroll_connect);
        fab_home.setOnClickListener(v -> go_next(SettingActivity.class));
    }


    private void init_actions() {
        visibility_change(View.GONE, View.VISIBLE);
        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        visibility_change(View.VISIBLE, View.GONE);
                    }
                    else{
                        speaking_SV.setQuery("", false);
                        visibility_change(View.GONE, View.VISIBLE);
                    }
                });
        speaking_SV.setOnQueryTextListener(onQueryTextListener);

    }
    private void visibility_change(int searchVisibility, int connectVisibility) {
        scroll_search.setVisibility(searchVisibility);
        scroll_connect.setVisibility(connectVisibility);

    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            find_lst.clear();
            if (s.isEmpty()) {
                searchingAdapter.notifyDataSetChanged();
                return true;
            }

            find_lst.addAll(all_contacts
                    .stream()
                    .filter(myContact -> myContact.getName().contains(s))
                    .collect(Collectors.toList()));
            searchingAdapter.notifyDataSetChanged();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        visibility_change(View.GONE, View.VISIBLE);

    }

    private SearchingAdapter.UserListener userListener = new SearchingAdapter.UserListener() {
        @Override
        public void clicked(MyContact contact, int position) {

            boolean is_already_connected = tracks.stream().anyMatch(trk -> trk.getPhone_adult().equals(contact.getPhone()));

            if (is_already_connected) {
                Toast.makeText(SearchConnectionsActivity.this, "Track Already Sent", Toast.LENGTH_SHORT).show();

                Optional<KeepieUser> op_user = users_tracking.stream().filter(usr -> usr.getPhone().equals(contact.getPhone())).findFirst();
                new ImageDialog().show(SearchConnectionsActivity.this, contact.getPhone(), op_user.isPresent() ? op_user.get().getImage() : "");

            } else {
                if(contact.getPhone().equals(myUser.getPhone())){
                    Toast.makeText(SearchConnectionsActivity.this, "It's your number...", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserServerCommunicator.getInstance().getUserDetails(contact.getPhone());
            }
        }
    };

    private  GetUserCallback userCallback = new GetUserCallback() {

        @Override
        public void get_user(KeepieUser user) {
            Toast.makeText(SearchConnectionsActivity.this, "Send Succeed...", Toast.LENGTH_SHORT).show();

            TrackServerCommunicator.getInstance().createTrack(
                    new Track()
                            .setPhone_child(myUser.getPhone())
                            .setPhone_adult(user.getPhone())
                            .setApproved(false)
                            .setDenied(false)
            );
        }

        @Override
        public void not_found_user() {
            Toast.makeText(SearchConnectionsActivity.this, "This contact didn't have an account ", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(SearchConnectionsActivity.this, "Some Error occurred during searching account " + status_code + " " + info, Toast.LENGTH_SHORT).show();
        }
    };

    private GetUserslist usersListCallback = new GetUserslist() {
        @Override
        public void get_users_list(List<KeepieUser> users) {
            SearchConnectionsActivity.this.users_tracking.clear();
            SearchConnectionsActivity.this.users_tracking.addAll(users);
            searchingAdapter.setKeepieUsers(users);
            HashMap<KeepieUser,Track> users_track_map = new HashMap<>();

            for (KeepieUser us: users) {
                tracks.stream().filter(trac -> trac.getPhone_adult().equals(us.getPhone())).findFirst().ifPresent(track ->
                        users_track_map.put(us,track));
            }
            if(users.isEmpty())
                follow_LAY_empty.setVisibility(View.VISIBLE);
            else
                follow_LAY_empty.setVisibility(View.GONE);
            connectiosAdapter.setUser_tracks(users_track_map);
            connectiosAdapter.notifyDataSetChanged();
            Log.d("MyLog", Arrays.toString(users.toArray()));

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(SearchConnectionsActivity.this, "Error Occurred "+ status_code+ ": "+info , Toast.LENGTH_SHORT).show();
        }
    };
    
    private TrackCallback trackCallback = new TrackCallback() {
        @Override
        public void get_tracks(List<Track> tracks) {
            SearchConnectionsActivity.this.tracks = tracks;
            if(!tracks.isEmpty())
                UserServerCommunicator.getInstance().getUsersList(
                        new UsersPhones(
                                tracks.stream().map(trk -> trk.getPhone_adult()).collect(Collectors.toList())
                        )
                );
            if(tracks.isEmpty())
                follow_LAY_empty.setVisibility(View.VISIBLE);
            else
                follow_LAY_empty.setVisibility(View.GONE);
            Log.d("MyLog", "trackCallback "+Arrays.toString(tracks.toArray()));
        }

        @Override
        public void track_updated() {
        }

        @Override
        public void track_created() {
            TrackServerCommunicator.getInstance().getTracks(myUser.getPhone(),true);
        }

        @Override
        public void track_deleted() {

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(SearchConnectionsActivity.this, "Error Occurred "+ status_code+ ": "+info , Toast.LENGTH_SHORT).show();
        }
    };

    private Callback_permissions callback_permissions = () -> pull_contacts();


}