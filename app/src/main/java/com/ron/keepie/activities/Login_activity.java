package com.ron.keepie.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;

import java.util.Arrays;
import java.util.List;

public class Login_activity extends AppCompatActivity {
    private MaterialButton connect_BTN_login;
    private MaterialButton connect_BTN_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        connect_BTN_login = findViewById(R.id.connect_BTN_login);
        connect_BTN_register = findViewById(R.id.connect_BTN_register);
        connect_BTN_login.setOnClickListener(onClickListener);
        connect_BTN_register.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = view -> make_FirebaseAuth();

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> connect_to_user()
    ); // TODO: 04/02/2023 Verify user really entered 

    private void connect_to_user() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        go_next(ListenChatsActivity.class);
        // go_next(Register_activity.class);
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//            go_next(Register_activity.class);
//        } else {
//            Toast.makeText(this, "NOT OK", Toast.LENGTH_SHORT).show();
//        }
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
}