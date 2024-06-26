package com.example.meetease.activity.homeScreen.mainScreen.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileShowActivity extends AppCompatActivity {
    TextView txtName, txtEmail, txtMobile, txtAppName, txtVersion;
    ImageView ivBack;
    PreferenceManager preferenceManager;
    CircleImageView imageProfile;
    CardView cvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtAppName = findViewById(R.id.txtAppName);
        cvProfile = findViewById(R.id.cvProfile);
        imageProfile = findViewById(R.id.imageProfile);
        txtVersion = findViewById(R.id.txtVersion);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        preferenceManager = new PreferenceManager(ProfileShowActivity.this);

        Tools.DisplayImage(this,imageProfile,preferenceManager.getKeyValueString(VariableBag.image,""));

        txtName.setSelected(true);
        txtEmail.setSelected(true);
        txtMobile.setSelected(true);

        txtName.setText(preferenceManager.getKeyValueString(VariableBag.full_name, ""));
        txtEmail.setText(preferenceManager.getKeyValueString(VariableBag.email, ""));
        txtMobile.setText(preferenceManager.getKeyValueString(VariableBag.mobile, ""));
        txtVersion.setText(VariableBag.appVersion);
        txtAppName.setText(VariableBag.appName);

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileShowActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.DisplayImage(ProfileShowActivity.this,imageProfile,preferenceManager.getKeyValueString(VariableBag.image,""));
    }
}