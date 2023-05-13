package com.ron.keepie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.ron.keepie.R;

public class TempActivity extends AppCompatActivity {
    // TODO: 13/05/2023 start service is here
    // TODO: 13/05/2023 handle with server errors by status code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Intent intent = new Intent(this, WhatsAppService.class);
        intent.setAction(WhatsAppService.START_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}