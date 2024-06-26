package com.example.meetease.activity.entryModule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetease.R;

public class GuideActivity extends AppCompatActivity {

    Button btnNext, btnSkip,btnPrevious;
    ImageView imgSteps, ivBack;
    TextView txtSteps;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        btnNext = findViewById(R.id.btnNext);
        imgSteps = findViewById(R.id.imgSteps);
        txtSteps = findViewById(R.id.txtSteps);
        btnSkip = findViewById(R.id.btnSkip);
        ivBack = findViewById(R.id.ivBack);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setVisibility(View.GONE);
        updateImage();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos>0){
                    pos--;
                    updateImage();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                updateImage();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
    void updateImage(){
        if (pos == 0) {
            txtSteps.setText("Step 1:\nClick 'Create a New Reservation'.");
            imgSteps.setImageResource(R.drawable.home_screen_image);
            btnPrevious.setVisibility(View.GONE);
        }if (pos == 1) {
            txtSteps.setText("Step 2:\nSelect Your Desired Date & Time For the Meeting Room.");
            imgSteps.setImageResource(R.drawable.selector_screen_image);
            btnPrevious.setVisibility(View.VISIBLE);
        } else if (pos == 2) {
            txtSteps.setText("Step 3:\nSelect the Meeting Room of your choice from the List.");
            imgSteps.setImageResource(R.drawable.show_room_screen_image);
        } else if (pos == 3) {
            txtSteps.setText("Step 4:\nIf you want to, Choose the requirements in the filter.");
            imgSteps.setImageResource(R.drawable.filter_screen_image);
        } else if (pos == 4) {
            txtSteps.setText("Step 5:\nSelect the Meeting Room to see the Details.");
            imgSteps.setImageResource(R.drawable.show_room_detail_screen);
        } else if (pos == 5) {
            txtSteps.setText("Step 6:\nComplete the Payment Process.");
            imgSteps.setImageResource(R.drawable.final_booking);
            btnNext.setText("Next");
        } else if (pos == 6) {
            txtSteps.setText("Step 7:\nCheck the booking status in the Upcoming Meetings.");
            imgSteps.setImageResource(R.drawable.booking_done);
            btnNext.setText("Finish");
        } else if (pos == 7){
            finish();
        }
    }
}