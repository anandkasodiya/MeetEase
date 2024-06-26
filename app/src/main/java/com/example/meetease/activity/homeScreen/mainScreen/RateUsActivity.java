package com.example.meetease.activity.homeScreen.mainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.appUtils.Tools;

public class RateUsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button btnSubmit;
    TextView txtRating;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtRating = findViewById(R.id.txtRating);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userRating = (int) ratingBar.getRating();
                if (userRating == 1) {
                    Tools.showCustomToast(getApplicationContext(), "We Are Really Sorry For Your Experience.", findViewById(R.id.customToastLayout), getLayoutInflater());
                } else if (userRating == 2) {
                    Tools.showCustomToast(getApplicationContext(), "We Would Love To Hear Some Suggestions From You. ", findViewById(R.id.customToastLayout), getLayoutInflater());
                } else if (userRating == 3) {
                    Tools.showCustomToast(getApplicationContext(), "We Would Try To Make Your Experience Better Next Time.", findViewById(R.id.customToastLayout), getLayoutInflater());
                } else if (userRating == 4) {
                    Tools.showCustomToast(getApplicationContext(), "Thank you For Rating Us.", findViewById(R.id.customToastLayout), getLayoutInflater());
                } else {
                    Tools.showCustomToast(getApplicationContext(), "We Are Really Glad To Hear It From You.", findViewById(R.id.customToastLayout), getLayoutInflater());
                }
                finish();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating() == 1) {
                    txtRating.setText("Not Good");
                } else if (ratingBar.getRating() == 2) {
                    txtRating.setText("Good");
                } else if (ratingBar.getRating() == 3) {
                    txtRating.setText("Average");
                } else if (ratingBar.getRating() == 4) {
                    txtRating.setText("Excellent");
                } else {
                    txtRating.setText("I Really Liked it");
                }
            }
        });
    }
}