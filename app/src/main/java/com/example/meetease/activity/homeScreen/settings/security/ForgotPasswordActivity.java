package com.example.meetease.activity.homeScreen.settings.security;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.LoginDataModel;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.example.meetease.network.UserResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etvPhoneNo, etvNewPassword, etvConfirmPassword;
    LinearLayout lytOtp;
    EditText etvOtp6, etvOtp5, etvOtp4, etvOtp3, etvOtp2, etvOtp1;
    Button btnSend, btnSave, btnCheckOtp;
    ImageView ivBack;
    TextView tvCode;
    String verificationId, userId = "";
    CountryCodePicker countryPicker;
    PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    Tools tools;
    RestCall restCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etvPhoneNo = findViewById(R.id.etvPhoneNo);
        lytOtp = findViewById(R.id.lytOtp);
        etvOtp6 = findViewById(R.id.etvOtp6);
        etvOtp5 = findViewById(R.id.etvOtp5);
        etvOtp4 = findViewById(R.id.etvOtp4);
        etvOtp3 = findViewById(R.id.etvOtp3);
        etvOtp2 = findViewById(R.id.etvOtp2);
        etvOtp1 = findViewById(R.id.etvOtp1);
        etvNewPassword = findViewById(R.id.etvNewPassword);
        etvConfirmPassword = findViewById(R.id.etvConfirmPassword);
        btnSend = findViewById(R.id.btnSend);
        btnSave = findViewById(R.id.btnSave);
        btnCheckOtp = findViewById(R.id.btnCheckOtp);
        ivBack = findViewById(R.id.ivBack);
        tvCode = findViewById(R.id.tvCode);
        countryPicker = findViewById(R.id.countryPicker);

        preferenceManager = new PreferenceManager(ForgotPasswordActivity.this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        tools = new Tools(this);

        countryPicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                tvCode.setText("+ " + countryPicker.getSelectedCountryCode());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etvOtp1.addTextChangedListener(new GenericTextWatcher(null, etvOtp1, etvOtp2));
        etvOtp2.addTextChangedListener(new GenericTextWatcher(etvOtp1, etvOtp2, etvOtp3));
        etvOtp3.addTextChangedListener(new GenericTextWatcher(etvOtp2, etvOtp3, etvOtp4));
        etvOtp4.addTextChangedListener(new GenericTextWatcher(etvOtp3, etvOtp4, etvOtp5));
        etvOtp5.addTextChangedListener(new GenericTextWatcher(etvOtp4, etvOtp5, etvOtp6));
        etvOtp6.addTextChangedListener(new GenericTextWatcher(etvOtp5, etvOtp6, null));

        btnSend.setText("Send");
        lytOtp.setVisibility(View.GONE);
        etvNewPassword.setVisibility(View.GONE);
        etvConfirmPassword.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnCheckOtp.setVisibility(View.GONE);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etvPhoneNo.getText().toString().isEmpty()) {
                    etvPhoneNo.setError("Enter Mobile Number");
                    etvPhoneNo.requestFocus();
                } else {
                    loginUser(etvPhoneNo.getText().toString());

                }
            }
        });

        btnCheckOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.showLoading();

                if (etvOtp1.getText().toString().isEmpty() || etvOtp2.getText().toString().isEmpty() || etvOtp3.getText().toString().isEmpty() || etvOtp4.getText().toString().isEmpty() || etvOtp5.getText().toString().isEmpty() || etvOtp6.getText().toString().isEmpty()) {
                    Tools.showCustomToast(ForgotPasswordActivity.this, "Enter Otp", findViewById(R.id.customToastLayout), getLayoutInflater());
                } else {
                    verifyCode(etvOtp1.getText().toString() + etvOtp2.getText().toString() + etvOtp3.getText().toString() + etvOtp4.getText().toString() + etvOtp5.getText().toString() + etvOtp6.getText().toString());
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etvNewPassword.getText().toString().isEmpty()) {
                    etvNewPassword.setError("Enter Password");
                    etvNewPassword.requestFocus();
                } else if (etvConfirmPassword.getText().toString().isEmpty()) {
                    etvConfirmPassword.setError("Enter Confirm Password");
                    etvConfirmPassword.requestFocus();
                } else if (!Tools.isValidPassword(etvNewPassword.getText().toString())) {
                    etvNewPassword.setError("Password Must Consist Of Minimum length of 7 with At-least 1 UpperCase, 1 LowerCase, 1 Number & 1 Special Character");
                    etvNewPassword.requestFocus();
                } else if (!etvNewPassword.getText().toString().equals(etvConfirmPassword.getText().toString())) {
                    etvConfirmPassword.setError("Confirm Password Doesn't Match");
                    etvConfirmPassword.requestFocus();
                } else {
                    editPassword();
                }
            }
        });
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            lytOtp.setVisibility(View.VISIBLE);
            tools.stopLoading();
            btnSend.setText("Resend");
            btnCheckOtp.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            tools.stopLoading();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            tools.stopLoading();
        }

    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            btnCheckOtp.setVisibility(View.GONE);
                            btnSend.setVisibility(View.GONE);
                            etvPhoneNo.setEnabled(false);
                            etvOtp1.setEnabled(false);
                            etvOtp2.setEnabled(false);
                            etvOtp3.setEnabled(false);
                            etvOtp4.setEnabled(false);
                            etvOtp5.setEnabled(false);
                            etvOtp6.setEnabled(false);
                            etvNewPassword.setVisibility(View.VISIBLE);
                            etvConfirmPassword.setVisibility(View.VISIBLE);
                            btnSave.setVisibility(View.VISIBLE);
                            tools.stopLoading();
                        } else {
                            tools.stopLoading();
                            Tools.showCustomToast(ForgotPasswordActivity.this, "Enter Correct OTP", findViewById(R.id.customToastLayout), getLayoutInflater());
                        }
                    }
                });
    }

    void editPassword() {
        tools.showLoading();
        restCall.ResetPassword("UpdatePassword", userId, etvNewPassword.getText().toString())
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
                                    preferenceManager.setKeyValueString(VariableBag.password, etvNewPassword.getText().toString());
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                }
                            }
                        });
                    }
                });
    }

    public class GenericTextWatcher implements TextWatcher {
        private final EditText currentEditText;
        private final EditText nextEditText;
        private final EditText beforeEdittext;

        public GenericTextWatcher(EditText beforeEdittext, EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
            this.beforeEdittext = beforeEdittext;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1) {
                if (nextEditText != null) {
                    nextEditText.requestFocus();
                }
            }
        }
    }

    private void loginUser(String mobileNumber) {
        tools.showLoading();
        restCall.LoginUser("LoginUser", mobileNumber, "", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<LoginDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                Tools.showCustomToast(getApplicationContext(), "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });
                    }

                    @Override
                    public void onNext(LoginDataModel loginDataModel) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (loginDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    userId = loginDataModel.getUser_id();
                                    Tools.showCustomToast(getApplicationContext(), "OTP Sent...", findViewById(R.id.customToastLayout), getLayoutInflater());
                                    String phone = "+" + countryPicker.getSelectedCountryCode() + mobileNumber;
                                    tools.showLoading();
                                    sendVerificationCode(phone);
                                } else {
                                    Tools.showCustomToast(ForgotPasswordActivity.this, "Mobile Number Doesn't Exist", findViewById(R.id.customToastLayout), getLayoutInflater());
                                }

                            }
                        });
                    }
                });
    }

}