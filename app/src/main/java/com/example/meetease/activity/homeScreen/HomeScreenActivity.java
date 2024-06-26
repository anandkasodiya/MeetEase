package com.example.meetease.activity.homeScreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.NotificationActivity;
import com.example.meetease.activity.homeScreen.mainScreen.PreviousMeetingActivity;
import com.example.meetease.activity.homeScreen.mainScreen.profile.ProfileActivity;
import com.example.meetease.activity.homeScreen.mainScreen.profile.ProfileShowActivity;
import com.example.meetease.activity.homeScreen.mainScreen.RateUsActivity;
import com.example.meetease.activity.homeScreen.settings.FavoriteRoomActivity;
import com.example.meetease.activity.homeScreen.settings.security.SecurityActivity;
import com.example.meetease.activity.homeScreen.mainScreen.UpComingMeetingActivity;
import com.example.meetease.activity.homeScreen.mainScreen.create.BookMeetingActivity;
import com.example.meetease.activity.homeScreen.mainScreen.ContactUsActivity;
import com.example.meetease.activity.homeScreen.settings.AvailableRoomsActivity;
import com.example.meetease.activity.homeScreen.settings.FaqActivity;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.activity.entryModule.GuideActivity;
import com.example.meetease.activity.entryModule.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.concurrent.Executor;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    View scrollView, favoriteRooms, availableRooms, security, howToBookRoom,
            inviteFriend, helpAndSupport, logout, layoutAddReservation, layoutUpcomingMeeting,
            layoutPreviousMeeting, layoutUserProfile, layoutContactUs, layoutRateUs;
    ImageView ivSettingProfile, ivNotification, ivSetting;
    TextView tvSettingName, tvSettingEmail, tvTrans, txtHelloName;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;
    PreferenceManager preferenceManager;
    FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        preferenceManager = new PreferenceManager(this);
        layoutUserProfile = findViewById(R.id.layoutUserProfile);
        layoutAddReservation = findViewById(R.id.layoutAddReservation);
        layoutUpcomingMeeting = findViewById(R.id.layoutUpcomingMeeting);
        layoutPreviousMeeting = findViewById(R.id.layoutPreviousMeeting);
        layoutContactUs = findViewById(R.id.layoutContactUs);
        layoutRateUs = findViewById(R.id.layoutRateUs);
        favoriteRooms = findViewById(R.id.favoriteRooms);
        availableRooms = findViewById(R.id.availableRooms);
        security = findViewById(R.id.security);
        tvTrans = findViewById(R.id.tvTrans);
        txtHelloName = findViewById(R.id.txtHelloName);
        scrollView = findViewById(R.id.scrollView);
        howToBookRoom = findViewById(R.id.howToBookRoom);
        inviteFriend = findViewById(R.id.inviteFriend);
        helpAndSupport = findViewById(R.id.helpAndSupport);
        logout = findViewById(R.id.logout);
        ivSetting = findViewById(R.id.ivSetting);
        ivSettingProfile = findViewById(R.id.ivSettingProfile);
        ivNotification = findViewById(R.id.ivNotification);
        tvSettingName = findViewById(R.id.tvSettingName);
        tvSettingEmail = findViewById(R.id.tvSettingEmail);

        Tools.DisplayImage(this, ivSettingProfile, preferenceManager.getKeyValueString(VariableBag.image, ""));

        ivSettingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, ProfileActivity.class));
            }
        });

        tvSettingEmail.setSelected(true);
        tvSettingName.setSelected(true);

        scrollView.setVisibility(View.GONE);
        tvTrans.setVisibility(View.GONE);
        txtHelloName.setSelected(true);
        auth = FirebaseAuth.getInstance();

        ivSetting.setOnClickListener(this);
        logout.setOnClickListener(this);
        howToBookRoom.setOnClickListener(this);
        layoutContactUs.setOnClickListener(this);
        helpAndSupport.setOnClickListener(this);
        layoutUserProfile.setOnClickListener(this);
        security.setOnClickListener(this);
        ivNotification.setOnClickListener(this);
        availableRooms.setOnClickListener(this);
        layoutAddReservation.setOnClickListener(this);
        layoutUpcomingMeeting.setOnClickListener(this);
        layoutRateUs.setOnClickListener(this);
        tvTrans.setOnClickListener(this);
        inviteFriend.setOnClickListener(this);
        layoutPreviousMeeting.setOnClickListener(this);
        favoriteRooms.setOnClickListener(this);

        biometric();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvSettingName.setText(preferenceManager.getKeyValueString(VariableBag.full_name, ""));
        tvSettingEmail.setText(preferenceManager.getKeyValueString(VariableBag.email, ""));
        txtHelloName.setText("Hello, " + preferenceManager.getKeyValueString(VariableBag.full_name, ""));
        Tools.DisplayImage(this, ivSettingProfile, preferenceManager.getKeyValueString(VariableBag.image, ""));
    }

    @Override
    public void onBackPressed() {
        if (scrollView.getVisibility() == View.VISIBLE) {
            scrollView.setVisibility(View.GONE);
            tvTrans.setVisibility(View.GONE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);

            View view = getLayoutInflater().inflate(R.layout.dialog_alert, null);
            TextView txtBody = view.findViewById(R.id.txtBody);
            txtBody.setText("Are You Sure You Want To Exit?");
            builder.setView(view);

            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ivSetting) {
            scrollView.setVisibility(View.VISIBLE);
            tvTrans.setVisibility(View.VISIBLE);

            Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            scrollView.startAnimation(slideInAnimation);
            ivSetting.startAnimation(slideInAnimation);
            tvTrans.startAnimation(slideInAnimation);
        }

        if (view == favoriteRooms) {
            changeScreen(FavoriteRoomActivity.class);
        }

        if (view == ivNotification) {
            changeScreen(NotificationActivity.class);
        }

        if (view == layoutPreviousMeeting) {
            changeScreen(PreviousMeetingActivity.class);
        }

        if (view == security) {
            changeScreen(SecurityActivity.class);
        }

        if (view == tvTrans) {
            scrollView.setVisibility(View.GONE);
            tvTrans.setVisibility(View.GONE);
        }

        if (view == layoutUpcomingMeeting) {
            changeScreen(UpComingMeetingActivity.class);
        }

        if (view == inviteFriend) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Join MeetEase using my invitation link: " + "https://example.com/invite?userId=" + preferenceManager.getKeyValueString(VariableBag.user_id, ""));
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Share invite link using");
            startActivity(shareIntent);
        }

        if (view == availableRooms) {
            changeScreen(AvailableRoomsActivity.class);
        }

        if (view == layoutAddReservation) {
            changeScreen(BookMeetingActivity.class);
        }

        if (view == howToBookRoom) {
            changeScreen(GuideActivity.class);
        }

        if (view == layoutUserProfile) {
            changeScreen(ProfileShowActivity.class);
        }

        if (view == layoutContactUs) {
            changeScreen(ContactUsActivity.class);
        }

        if (view == helpAndSupport) {
            changeScreen(FaqActivity.class);
        }

        if (view == layoutRateUs) {
            changeScreen(RateUsActivity.class);
        }

        if (view == logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);

            View view1 = getLayoutInflater().inflate(R.layout.dialog_alert, null);
            TextView txtBody = view1.findViewById(R.id.txtBody);
            txtBody.setText("Are You Sure You Want To Logout?");
            builder.setView(view1);

            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                preferenceManager.setKeyValueBoolean(VariableBag.SessionManage, false);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    for (UserInfo userInfo : currentUser.getProviderData()) {
                        if ("google.com".equals(userInfo.getProviderId())) {
                            auth.signOut();
                        } else {
                        }
                    }
                }
                changeScreen(LoginActivity.class);
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    void changeScreen(Class classActivity) {
        Intent intent = new Intent(HomeScreenActivity.this, classActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    void biometric() {
        executor = ContextCompat.getMainExecutor(this);
        if (preferenceManager.getKeyValueBoolean(VariableBag.SecuritySwitchCheck)) {
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock MeetEase")
                    .setDescription("Use Password,Pattern or Fingerprint to Unlock.")
                    .setDeviceCredentialAllowed(true)
                    .setNegativeButtonText(null)
                    .build();

            biometricPrompt.authenticate(promptInfo);
        }
    }
}