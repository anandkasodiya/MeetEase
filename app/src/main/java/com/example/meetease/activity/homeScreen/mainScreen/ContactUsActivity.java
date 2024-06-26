package com.example.meetease.activity.homeScreen.mainScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;

public class ContactUsActivity extends AppCompatActivity {

    ImageView ivBack;
    Button btnSendMessage;
    EditText etvMessage, etvPhone, etvEmail, etvLastName, etvFirstName;
    CardView cvPhone, cvWhatsapp;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ivBack = findViewById(R.id.ivBack);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        etvMessage = findViewById(R.id.etvMessage);
        etvPhone = findViewById(R.id.etvPhone);
        etvEmail = findViewById(R.id.etvEmail);
        etvLastName = findViewById(R.id.etvLastName);
        etvFirstName = findViewById(R.id.etvFirstName);
        cvPhone = findViewById(R.id.cvPhone);
        cvWhatsapp = findViewById(R.id.cvWhatsapp);

        preferenceManager = new PreferenceManager(ContactUsActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = etvFirstName.getText().toString().trim();
                String lname = etvLastName.getText().toString().trim();
                String email = etvEmail.getText().toString().trim();
                String phone = etvPhone.getText().toString().trim();
                String message = etvMessage.getText().toString().trim();

                if (fname.isEmpty()) {
                    etvFirstName.setError("Enter First Name");
                    etvFirstName.requestFocus();
                } else if (lname.isEmpty()) {
                    etvLastName.setError("Enter Last Name");
                    etvLastName.requestFocus();
                } else if (email.isEmpty()) {
                    etvEmail.setError("Enter Email Address");
                    etvEmail.requestFocus();
                } else if (phone.isEmpty()) {
                    etvPhone.setError("Enter Phone Number");
                    etvPhone.requestFocus();
                } else if (message.isEmpty()) {
                    etvMessage.setError("Enter Message");
                    etvMessage.requestFocus();
                } else {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"akachwala24@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us Message");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, fname + " " + lname + "\n"
                            + " " + email + "\n"
                            + " " + phone + "\n\n"
                            + " " + message);

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Tools.showCustomToast(getApplicationContext(), "No email app found", findViewById(R.id.customToastLayout), getLayoutInflater());
                    }
                }
            }
        });

        cvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = "8460190852";
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                startActivity(dialIntent);
            }
        });

        cvWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = "8460190852";
                String message = "Hello,\nHope You are having a Good day.\nMyself " + preferenceManager.getKeyValueString(VariableBag.full_name, "");

                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNo + "&text=" + message);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                } else {
                    Tools.showCustomToast(getApplicationContext(), "WhatsApp is not installed", findViewById(R.id.customToastLayout), getLayoutInflater());
                }
            }
        });

    }
}