package com.example.meetease.activity.homeScreen.settings.security;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.activity.entryModule.SignUpActivity;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.example.meetease.network.UserResponse;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class SecurityActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    SwitchCompat switchOnOff;
    ImageView ivBack;
    ConstraintLayout lytChangePassword, lytDeleteUser, lytForgotPassword;
    RestCall restCall;
    Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        preferenceManager = new PreferenceManager(this);
        switchOnOff = findViewById(R.id.switchOnOff);
        ivBack = findViewById(R.id.ivBack);
        lytChangePassword = findViewById(R.id.lytChangePassword);
        lytDeleteUser = findViewById(R.id.lytDeleteUser);
        lytForgotPassword = findViewById(R.id.lytForgotPassword);

        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager = new PreferenceManager(SecurityActivity.this);
        tools = new Tools(SecurityActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lytChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecurityActivity.this, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        lytForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecurityActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        lytDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecurityActivity.this);

                View view1 = getLayoutInflater().inflate(R.layout.dialog_password_verify, null);
                EditText etvPassword = view1.findViewById(R.id.etvPassword);
                builder.setView(view1);


                builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etvPassword.getText().toString().equals(preferenceManager.getKeyValueString(VariableBag.password, ""))) {
                            dialog.dismiss();
                            deleteUser(preferenceManager.getKeyValueString(VariableBag.user_id, ""));
                        } else {
                            Tools.showCustomToast(getApplicationContext(), "Incorrect password. Please try again.", findViewById(R.id.customToastLayout), getLayoutInflater());
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        switchOnOff.setChecked(preferenceManager.getKeyValueBoolean(VariableBag.SecuritySwitchCheck));

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                preferenceManager.setKeyValueBoolean(VariableBag.SecuritySwitchCheck, status);
            }
        });
    }


    private void deleteUser(String userId) {
        tools.showLoading();
        restCall.deleteUser("deleteuserU", userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tools.stopLoading();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tools.showCustomToast(getApplicationContext(), "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        tools.stopLoading();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userResponse.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    Tools.showCustomToast(getApplicationContext(), "Account Deleted Successfully", findViewById(R.id.customToastLayout), getLayoutInflater());
                                    startActivity(new Intent(SecurityActivity.this, SignUpActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                }
                            }
                        });
                    }
                });
    }
}