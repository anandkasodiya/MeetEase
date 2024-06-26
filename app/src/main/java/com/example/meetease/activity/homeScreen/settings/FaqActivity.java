package com.example.meetease.activity.homeScreen.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.ContactUsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FaqActivity extends AppCompatActivity {

    TextView txtQ1, txtQ2, txtQ3, txtQ4;
    TextView txtA1, txtA2, txtA3, txtA4;
    LinearLayout lytQ1, lytQ2, lytQ3, lytQ4;
    ImageView imgUpDown1, imgUpDown2, imgUpDown3, imgUpDown4, ivBack;
    Button btnContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);

        lytQ1 = findViewById(R.id.lytQ1);
        lytQ2 = findViewById(R.id.lytQ2);
        lytQ3 = findViewById(R.id.lytQ3);
        lytQ4 = findViewById(R.id.lytQ4);

        txtQ1 = findViewById(R.id.txtQ1);
        txtQ2 = findViewById(R.id.txtQ2);
        txtQ3 = findViewById(R.id.txtQ3);
        txtQ4 = findViewById(R.id.txtQ4);

        txtA1 = findViewById(R.id.txtA1);
        txtA2 = findViewById(R.id.txtA2);
        txtA3 = findViewById(R.id.txtA3);
        txtA4 = findViewById(R.id.txtA4);

        imgUpDown1 = findViewById(R.id.imgUpDown1);
        imgUpDown2 = findViewById(R.id.imgUpDown2);
        imgUpDown3 = findViewById(R.id.imgUpDown3);
        imgUpDown4 = findViewById(R.id.imgUpDown4);

        ivBack = findViewById(R.id.ivBack);

        btnContactUs = findViewById(R.id.btnContactUs);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FaqActivity.this, ContactUsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        txtQ1.setText("How do I book a boardroom using the app?");
        lytQ1.setOnClickListener(new View.OnClickListener() {
            boolean hideShow;

            @Override
            public void onClick(View view) {
                if (!hideShow) {
                    txtA1.setText("You can create a Reservation by Selecting Create a New Reservation. " +
                            "Select Desired Date and time Slot and Check the Availability for the rooms and " +
                            "Select any from the Room List.");
                    txtA1.setVisibility(View.VISIBLE);
                    hideShow = true;
                    imgUpDown1.setImageResource(R.drawable.up);
                } else {
                    txtA1.setVisibility(View.GONE);
                    hideShow = false;
                    imgUpDown1.setImageResource(R.drawable.down);
                }
            }
        });

        txtQ2.setText("Can I book a Meeting Room for recurring meetings?");
        lytQ2.setOnClickListener(new View.OnClickListener() {
            boolean hideShow;

            @Override
            public void onClick(View view) {
                if (!hideShow) {
                    txtA2.setText("Yes, of Course.");
                    txtA2.setVisibility(View.VISIBLE);
                    hideShow = true;
                    imgUpDown2.setImageResource(R.drawable.up);
                } else {
                    txtA2.setVisibility(View.GONE);
                    hideShow = false;
                    imgUpDown2.setImageResource(R.drawable.down);
                }
            }
        });

        txtQ3.setText("Is there a maximum duration for meeting room bookings?");
        lytQ3.setOnClickListener(new View.OnClickListener() {
            boolean hideShow;

            @Override
            public void onClick(View view) {
                if (!hideShow) {
                    txtA3.setText("No, There is no Maximum duration for bookings.");
                    txtA3.setVisibility(View.VISIBLE);
                    hideShow = true;
                    imgUpDown3.setImageResource(R.drawable.up);
                } else {
                    txtA3.setVisibility(View.GONE);
                    hideShow = false;
                    imgUpDown3.setImageResource(R.drawable.down);
                }
            }
        });

        txtQ4.setText("Can I see the availability of Meeting Rooms in real-time?");
        lytQ4.setOnClickListener(new View.OnClickListener() {
            boolean hideShow;

            @Override
            public void onClick(View view) {
                if (!hideShow) {
                    txtA4.setText("Yes, Go to Create a New Reservation and Select Your Desired Date and Time.");
                    txtA4.setVisibility(View.VISIBLE);
                    hideShow = true;
                    imgUpDown4.setImageResource(R.drawable.up);
                } else {
                    txtA4.setVisibility(View.GONE);
                    hideShow = false;
                    imgUpDown4.setImageResource(R.drawable.down);
                }
            }
        });
    }
}