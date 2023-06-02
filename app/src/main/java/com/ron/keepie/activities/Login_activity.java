package com.ron.keepie.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;
import com.ron.keepie.activities.adult.FollowActivity;
import com.ron.keepie.activities.child.SearchConnectionsActivity;
import com.ron.keepie.dialogs.AdminDialog;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.GetUserCallback;

import java.util.Arrays;
import java.util.List;

public class Login_activity extends AppCompatActivity {
    private MaterialButton connect_BTN_login;
    private MaterialButton connect_BTN_register;
    private LottieAnimationView enter_app_lottie;
    private boolean is_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserServerCommunicator.getInstance().setGetUserCallback(userCallback);
        findViews();
    }

    private void findViews() {
        connect_BTN_login = findViewById(R.id.connect_BTN_login);
        connect_BTN_register = findViewById(R.id.connect_BTN_register);
        enter_app_lottie = findViewById(R.id.enter_app_lottie);
        connect_BTN_login.setOnClickListener(onClickListenerLog);
        connect_BTN_register.setOnClickListener(onClickListenerReg);

    }

    private View.OnClickListener onClickListenerReg = view -> {
        is_register = true;
        make_FirebaseAuth();
    };
    private View.OnClickListener onClickListenerLog = view -> {
        is_register = false;
        make_FirebaseAuth();
    };

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> connect_to_user()
    );

    private void connect_to_user() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        if (is_register){
            go_next(Register_activity.class);
            return;
        }
        UserServerCommunicator.getInstance().getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private void make_FirebaseAuth() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)      // Set logo drawable
                .setTheme(R.style.Theme_Keepie)      // Set theme
                .setTosAndPrivacyPolicyUrls("https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0", "https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0")
                .build();
        signInLauncher.launch(signInIntent);
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
            Toast.makeText(Login_activity.this, "User didn't founded...", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(Login_activity.this, "Some Error occurred during searching account "+status_code + " "+ info, Toast.LENGTH_LONG).show();
            enter_app_lottie.setOnClickListener(view -> new AdminDialog().show(Login_activity.this));

        }
    };
}