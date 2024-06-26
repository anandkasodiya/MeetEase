package com.example.meetease.activity.entryModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.activity.homeScreen.HomeScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    TextView txtAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txtAppVersion = findViewById(R.id.txtAppVersion);

        txtAppVersion.setText(VariableBag.appVersion);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferenceManager preferenceManager = new PreferenceManager(SplashScreenActivity.this);
                if (preferenceManager.getKeyValueBoolean(VariableBag.SessionManage)) {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        }, 2000);
    }
}