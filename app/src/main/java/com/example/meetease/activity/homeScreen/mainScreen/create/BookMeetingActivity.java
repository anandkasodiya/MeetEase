package com.example.meetease.activity.homeScreen.mainScreen.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.RoomDetailListNoUpcomingDataModel;
import com.example.meetease.fragment.DatePickerFragment;
import com.example.meetease.fragment.EndTimePickerFragment;
import com.example.meetease.fragment.StartTimePickerFragment;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;

import java.time.LocalDateTime;
import java.util.Calendar;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BookMeetingActivity extends AppCompatActivity {

    TextView tvDate, tvStartTime, tvEndTime;
    View date, startTime, endTime;
    ImageView ivDate, ivStartTime, ivEndDate, ivBack;
    Button btnBookNow;
    String RoomIdAllRoom, RoomNameAllRoom, RoomLocationAllRoom, RoomPriceAllRoom;
    DatePickerFragment datePickerFragment;
    StartTimePickerFragment startTimePickerFragment;
    EndTimePickerFragment endTimePickerFragment;
    PreferenceManager preferenceManager;
    RestCall restCall;
    Tools tools;
    String selectYear, selectMonth, selectDay, startMinute, startHour, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_meeting);

        tvDate = findViewById(R.id.tvDate);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        ivStartTime = findViewById(R.id.ivStartTime);
        ivEndDate = findViewById(R.id.ivEndTime);
        btnBookNow = findViewById(R.id.btnBookNow);
        date = findViewById(R.id.date);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        ivBack = findViewById(R.id.ivBack);
        ivDate = findViewById(R.id.ivDate);

        tvStartTime.setText("Select Start Time");
        tvEndTime.setText("Select End Time");
        tvDate.setText("Select Date");
        preferenceManager = new PreferenceManager(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        tools = new Tools(BookMeetingActivity.this);

        preferenceManager.setKeyValueBoolean("abc", false);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Calendar c = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFragment = new DatePickerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                datePickerFragment.show(fragmentTransaction, "#tag");
                datePickerFragment.setCancelable(false);

                datePickerFragment.setUpInterface(new DatePickerFragment.ButtonClick() {
                    @Override
                    public void saveClick(String date, String day, String month, String year) {
                        tvDate.setText(date);

                        LocalDateTime currentTime = LocalDateTime.now();
                        String timeCurrent = String.valueOf(currentTime);
                        String currentDay = "";
                        String currentMonth = "";
                        String currentYear = "";

                        currentDay = "" + timeCurrent.charAt(8) + timeCurrent.charAt(9);
                        currentMonth = "" + timeCurrent.charAt(5) + timeCurrent.charAt(6);
                        currentYear = "" + timeCurrent.charAt(0) + timeCurrent.charAt(1) + timeCurrent.charAt(2) + timeCurrent.charAt(3);
                        if (timeCurrent.charAt(8) == '0') {
                            currentDay = "" + timeCurrent.charAt(9);
                        }
                        if (timeCurrent.charAt(5) == '0') {
                            currentDay = "" + timeCurrent.charAt(6);
                        }
                        preferenceManager.setKeyValueBoolean("abc", date.equals(currentDay + "/" + currentMonth + "/" + currentYear));

                        tvStartTime.setText("Select Start Time");
                        tvEndTime.setText("Select End Time");
                        selectDay = day;
                        selectMonth = month;
                        selectYear = year;
                    }
                });
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePickerFragment = new StartTimePickerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                startTimePickerFragment.show(fragmentTransaction, "#tag");
                startTimePickerFragment.setCancelable(false);

                startTimePickerFragment.setUpInterface(new StartTimePickerFragment.ButtonClick() {
                    @Override
                    public void saveClick(String Time, String hour, String min) {
                        tvStartTime.setText(Time);
                        preferenceManager.setKeyValueString("start hour", hour);
                        preferenceManager.setKeyValueString("start minute", min);
                        tvEndTime.setText("Select End Time");
                        startHour = hour;
                        startMinute = min;
                    }
                });
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePickerFragment = new EndTimePickerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                endTimePickerFragment.show(fragmentTransaction, "#tag");
                endTimePickerFragment.setCancelable(false);

                endTimePickerFragment.setUpInterface(new EndTimePickerFragment.ButtonClick() {
                    @Override
                    public void saveClick(String Time, String hour, String min) {
                        tvEndTime.setText(Time);
                        endHour = hour;
                        endMinute = min;
                    }
                });
            }
        });

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvDate.getText().toString().equals("Select Date")) {
                    Toast.makeText(BookMeetingActivity.this, "Select Date First", Toast.LENGTH_SHORT).show();
                } else if (tvStartTime.getText().toString().equals("Select Start Time") || tvEndTime.getText().toString().isEmpty()) {
                    Toast.makeText(BookMeetingActivity.this, "Select Start Time", Toast.LENGTH_SHORT).show();
                } else if (tvEndTime.getText().toString().equals("Select End Time") || tvEndTime.getText().toString().isEmpty()) {
                    Toast.makeText(BookMeetingActivity.this, "Select End Time", Toast.LENGTH_SHORT).show();
                } else if (getIntent().getStringExtra("RoomIdAllRoom") != null) {

                    RoomIdAllRoom = getIntent().getStringExtra("RoomIdAllRoom");
                    RoomNameAllRoom = getIntent().getStringExtra("RoomNameAllRoom");
                    RoomLocationAllRoom = getIntent().getStringExtra("RoomLocationAllRoom");
                    RoomPriceAllRoom = getIntent().getStringExtra("RoomPriceAllRoom");
                    unBookedDetail();
                } else {
                    Intent intent = new Intent(BookMeetingActivity.this, CreateReservationActivity.class);
                    intent.putExtra("year", selectYear);
                    intent.putExtra("month", selectMonth);
                    intent.putExtra("day", selectDay);
                    intent.putExtra("startHour", startHour);
                    intent.putExtra("startMinute", startMinute);
                    intent.putExtra("endHour", endHour);
                    intent.putExtra("endMinute", endMinute);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

    }

    void unBookedDetail() {
        tools.showLoading();
        restCall.AvailableRoomDetails("UnbookedRoom", selectYear + "-" + selectMonth + "-" + selectDay, startHour + ":" + startMinute, endHour + ":" + endMinute)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RoomDetailListNoUpcomingDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tools.stopLoading();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tools.showCustomToast(BookMeetingActivity.this, "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });

                    }

                    @Override
                    public void onNext(RoomDetailListNoUpcomingDataModel roomDetailListNoUpcomingDataModel) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (roomDetailListNoUpcomingDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    boolean flag = false;
                                    for (int i = 0; i < roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming().size(); i++) {
                                        if (roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming().get(i).getRoom_d_id().equals(RoomIdAllRoom)) {
                                            flag = true;
                                        }
                                    }
                                    if (flag) {
                                        Intent intent = new Intent(BookMeetingActivity.this, PaymentActivity.class);

                                        intent.putExtra("RoomIdAllRoom", RoomIdAllRoom);
                                        intent.putExtra("RoomNameAllRoom", RoomNameAllRoom);
                                        intent.putExtra("RoomLocationAllRoom", RoomLocationAllRoom);
                                        intent.putExtra("RoomPriceAllRoom", RoomPriceAllRoom);

                                        int sh = Integer.parseInt(startHour), sm = Integer.parseInt(startMinute),
                                                eh = Integer.parseInt(endHour), em = Integer.parseInt(endMinute),
                                                price = Integer.parseInt(RoomPriceAllRoom);

                                        int et = (eh * 60) + em;
                                        int st = (sh * 60) + sm;
                                        int minute = et - st;
                                        int hour = 0;
                                        if (minute % 60 != 0) {
                                            hour = minute / 60;
                                            hour = hour + 1;
                                        } else {
                                            hour = minute / 60;
                                        }

                                        int totalPrice = hour * price;

                                        intent.putExtra("totalPriceAllRoom", totalPrice);
                                        intent.putExtra("sd", selectDay + "-" + selectMonth + "-" + selectYear);
                                        intent.putExtra("et", eh + ":" + em);
                                        intent.putExtra("st", sh + ":" + sm);

                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Tools.showCustomToast(BookMeetingActivity.this, "Room Not Available", findViewById(R.id.customToastLayout), getLayoutInflater());
                                    }
                                }
                            }
                        });
                    }
                });
    }
}