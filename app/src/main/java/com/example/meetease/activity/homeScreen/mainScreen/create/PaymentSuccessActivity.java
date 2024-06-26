package com.example.meetease.activity.homeScreen.mainScreen.create;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.UpComingMeetingActivity;
import com.example.meetease.appUtils.Tools;

public class PaymentSuccessActivity extends AppCompatActivity {

    TextView txtTimer;
    ImageView ivPaymentGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        txtTimer = findViewById(R.id.txtTimer);
        ivPaymentGif = findViewById(R.id.ivPaymentGif);

        Glide.with(this)
                .load(R.drawable.background_gif)
                .into(ivPaymentGif);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                txtTimer.setText(l / 1000 + " Seconds");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(PaymentSuccessActivity.this, UpComingMeetingActivity.class));
                finish();
            }
        }.start();
    }
}