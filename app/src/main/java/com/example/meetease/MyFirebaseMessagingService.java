package com.example.meetease;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.meetease.activity.homeScreen.HomeScreenActivity;
import com.example.meetease.activity.homeScreen.mainScreen.create.DetailsActivity;
import com.example.meetease.activity.homeScreen.settings.AvailableRoomsActivity;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    RestCall restCall;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);

        FirebaseAnalytics.getInstance(this);
        Log.d("my payload1", "Message data payload: " + remoteMessage.getData().get("roomId"));

        String roomId = remoteMessage.getData().get("roomId");

        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNotification(title, body, roomId, DetailsActivity.class);
        } else {
            // If there is no data, still show a notification (without roomId)
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNotification(title, body, roomId, HomeScreenActivity.class);
        }
    }

    private void showNotification(String title, String body, String roomId, Class myClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(MyFirebaseMessagingService.this);
        }

        Intent resultIntent = new Intent(MyFirebaseMessagingService.this, myClass);
        if (roomId != null) {
            resultIntent.putExtra("RoomID123", roomId);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyFirebaseMessagingService.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "meeting_notification")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.bg)
                .setContentIntent(resultPendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) MyFirebaseMessagingService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel("meeting_notification", "meeting_notification", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Channel for meeting notifications");
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
        Log.i("my mess", "onMessageSent: " + msgId);
    }
}

