package com.ron.keepie.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;
import com.ron.keepie.callbacks.Callback_find_account;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.objects.MyUser;

public class Enter_app_activity extends AppCompatActivity {
    private LottieAnimationView enter_app_lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app);
        //MyDB.getInstance().setCallback_find_user(callback_find_account);
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
            // MyDB.getInstance().isAccountExists(userType , FirebaseAuth.getInstance().getCurrentUser().getUid());
            //TODO: change it
            go_next(ListenChatsActivity.class);
        }
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private Callback_find_account callback_find_account = new Callback_find_account() {
        @Override
        public void account_found(MyUser account) {
            DataManager.getDataManager().set_account(account);
            if (account.getUserType() == DataManager.eUserType.observable) {
                //go_next(Activity_bar_account.class);
            } else if (account.getUserType() == DataManager.eUserType.observer) {
                // TODO: 09/01/2023 final proj - verify pages
                //go_next(Activity_bar_account.class);
            }
        }

        @Override
        public void account_not_found() {
            FirebaseAuth.getInstance().signOut();
            //go_next(Register_Activity.class); nooo
        }

        @Override
        public void error() {
            FirebaseAuth.getInstance().signOut();
            //go_next(Activity_Login.class);
        }
    };

}