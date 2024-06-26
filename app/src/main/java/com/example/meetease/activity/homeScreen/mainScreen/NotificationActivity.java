package com.example.meetease.activity.homeScreen.mainScreen;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.adapter.NotificationAdapter;
import com.example.meetease.dataModel.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView recyclerViewNotification;
    TextView tvNoData;
    SwipeRefreshLayout swipe;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ivBack = findViewById(R.id.ivBack);
        tvNoData = findViewById(R.id.tvNoData);
        swipe = findViewById(R.id.swipe);
        recyclerViewNotification = findViewById(R.id.recyclerViewNotification);

        askNotificationPermission();

        tvNoData.setVisibility(View.GONE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        String msg = token;
                        Log.d("token", msg);

                        NotificationModel notification = new NotificationModel("TitleDemo1", "DescriptionDemo1", "NotificationTimeDemo1");
                        notificationList.add(notification);
                        notification = new NotificationModel("TitleDemo2", "DescriptionDemo2", "NotificationTimeDemo2");
                        notificationList.add(notification);
                        notification = new NotificationModel("TitleDemo3", "DescriptionDemo3", "NotificationTimeDemo3");
                        notificationList.add(notification);
                        notificationAdapter.notifyDataSetChanged();
                    }
                });

        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationList);
        recyclerViewNotification.setAdapter(notificationAdapter);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}