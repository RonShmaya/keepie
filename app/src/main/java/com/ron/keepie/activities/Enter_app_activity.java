package com.ron.keepie.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;
import com.ron.keepie.activities.adult.FollowActivity;
import com.ron.keepie.activities.child.SearchConnectionsActivity;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.GetUserCallback;



// TODO: 18/05/2023 server as poetry run!
// TODO: 18/05/2023 change logo
// TODO: 13/05/2023 how to listen notifications of sychronized like broadca or kafka
// TODO: 13/05/2023 start to manage chats -> python go alone to the firebase? chats messages -> app sends to the server, or server read chats alone
// TODO: 13/05/2023 last page - make it WOW + on manifest
// TODO: 18/05/2023 search page add notification to the adult?

public class Enter_app_activity extends AppCompatActivity {
    private LottieAnimationView enter_app_lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app);
        UserServerCommunicator.getInstance().setGetUserCallback(userCallback);
        findViews();
        decide_page_to_open();
    }

    private void findViews() {
        enter_app_lottie = findViewById(R.id.enter_app_lottie);
    }

    private void decide_page_to_open() {
        enter_app_lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                verifyUser();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void verifyUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            go_next(Login_activity.class); // no user connected
        } else {
            UserServerCommunicator.getInstance().getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
    private GetUserCallback userCallback = new GetUserCallback() {

        @Override
        public void get_user(KeepieUser user) {

            DataManager.getDataManager().set_account(user);
            if(user.isIs_child()){
                go_next(SearchConnectionsActivity.class);
            }
            else{
                go_next(FollowActivity.class);
            }

        }

        @Override
        public void not_found_user() {
            Toast.makeText(Enter_app_activity.this, "User didn't founded", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            go_next(Login_activity.class);
        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(Enter_app_activity.this, "Some Error occurred during searching account... "+status_code + " "+ info, Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            go_next(Login_activity.class);
        }
    };

}