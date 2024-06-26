package com.example.meetease.activity.homeScreen.mainScreen.create;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.UpComingMeetingActivity;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.example.meetease.network.UserResponse;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {

    TextView txtName, txtLocation, txtPrice, txtSelectedDate, txtTimeSlot, txtFinalPrice;
    Button btnPay;
    ImageView ivBack;
    RestCall restCall;
    Tools tools;
    PreferenceManager preferenceManager;
    int totalTime;
    String roomName, roomPrice, roomLocation, roomRating, roomId, bookingDate, bookingStartTime, bookingEndTime;
    String RoomIdAllRoom, RoomNameAllRoom, RoomLocationAllRoom, RoomPriceAllRoom;
    int totalPriceAllRoom, totalPrice;
    String selectedDate, endTimeAllRoom, startTimeAllRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        txtName = findViewById(R.id.txtName);
        txtLocation = findViewById(R.id.txtLocation);
        txtPrice = findViewById(R.id.txtPrice);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtTimeSlot = findViewById(R.id.txtTimeSlot);
        txtFinalPrice = findViewById(R.id.txtFinalPrice);
        btnPay = findViewById(R.id.btnPay);
        ivBack = findViewById(R.id.ivBack);

        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        roomPrice = intent.getStringExtra("roomPrice");
        roomLocation = intent.getStringExtra("roomLocation");
        roomRating = intent.getStringExtra("roomRating");
        roomId = intent.getStringExtra("roomId");
        bookingDate = intent.getStringExtra("bookingDate");
        bookingStartTime = intent.getStringExtra("bookingStartTime");
        bookingEndTime = intent.getStringExtra("bookingEndTime");
        totalTime = intent.getIntExtra("totalTime", 0);

        RoomIdAllRoom = intent.getStringExtra("RoomIdAllRoom");
        RoomNameAllRoom = intent.getStringExtra("RoomNameAllRoom");
        RoomLocationAllRoom = intent.getStringExtra("RoomLocationAllRoom");
        RoomPriceAllRoom = intent.getStringExtra("RoomPriceAllRoom");
        totalPriceAllRoom = intent.getIntExtra("totalPriceAllRoom", 0);
        selectedDate = intent.getStringExtra("sd");
        endTimeAllRoom = intent.getStringExtra("et");
        startTimeAllRoom = intent.getStringExtra("st");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        preferenceManager = new PreferenceManager(this);
        tools = new Tools(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);

        if (intent.getStringExtra("roomId") != null) {
            totalPrice = Integer.parseInt(roomPrice) * totalTime;

            txtName.setText(roomName);
            txtLocation.setText(roomLocation);
            txtPrice.setText(roomPrice + VariableBag.CURRENCY);
            txtSelectedDate.setText(bookingDate);
            txtTimeSlot.setText(bookingStartTime + " - " + bookingEndTime);
            txtFinalPrice.setText("" + VariableBag.CURRENCY + totalPrice);
            btnPay.setText(" Pay    --->    " + VariableBag.CURRENCY + totalPrice);

        } else if (intent.getStringExtra("RoomIdAllRoom") != null) {

            txtName.setText(RoomNameAllRoom);
            txtLocation.setText(RoomLocationAllRoom);
            txtPrice.setText(RoomPriceAllRoom + VariableBag.CURRENCY);
            txtSelectedDate.setText(selectedDate);
            txtTimeSlot.setText(startTimeAllRoom + " - " + endTimeAllRoom);
            txtFinalPrice.setText(VariableBag.CURRENCY + "" + totalPriceAllRoom);
            btnPay.setText(" Pay    --->    " + VariableBag.CURRENCY + totalPriceAllRoom);
        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomBooking();
            }
        });
    }

    private void roomBooking() {
        tools.showLoading();

        String ST = "", ET = "";
        String RID = "";
        Intent intent = getIntent();
        if (intent.getStringExtra("roomId") != null) {
            ST = bookingStartTime;
            ET = bookingEndTime;
            RID = roomId;
        } else if (intent.getStringExtra("RoomIdAllRoom") != null) {
            ST = startTimeAllRoom;
            ET = endTimeAllRoom;
            RID = RoomIdAllRoom;
        }

        restCall.RoomBooking("AddTimeBooking", preferenceManager.getKeyValueString(VariableBag.user_id, ""), RID, txtSelectedDate.getText().toString(), ST, ET, txtFinalPrice.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<UserResponse>() {
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
                    public void onNext(UserResponse userResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                Tools.showCustomToast(getApplicationContext(), userResponse.getMessage(), findViewById(R.id.customToastLayout), getLayoutInflater());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    createNotificationChannel(PaymentActivity.this);
                                }
                                if (userResponse.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    Intent resultIntent = new Intent(PaymentActivity.this, UpComingMeetingActivity.class);
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(PaymentActivity.this);
                                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                                    Notification notification = new NotificationCompat.Builder(PaymentActivity.this, "meeting_notification")
                                            .setContentTitle("Congratulation")
                                            .setContentText("Your Meeting Room Is Booked - " + roomName)
                                            .setSmallIcon(R.drawable.bg)
                                            .setContentIntent(resultPendingIntent)
                                            .build();
                                    NotificationManager notificationManager = (NotificationManager) PaymentActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, notification);
                                    startActivity(new Intent(PaymentActivity.this, PaymentSuccessActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel("meeting_notification", "meeting_notification", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Channel for meeting notifications");
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
