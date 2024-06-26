package com.example.meetease.activity.homeScreen.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.create.BookMeetingActivity;
import com.example.meetease.adapter.AllRoomsAdapter;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.RoomDetailDataModel;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class AvailableRoomsActivity extends AppCompatActivity {

    RecyclerView recyclerViewAllRooms;
    TextView tvNoData;
    RestCall restCall;
    ImageView ivBack;
    Tools tools;
    AllRoomsAdapter allRoomsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rooms);

        recyclerViewAllRooms = findViewById(R.id.recyclerViewAllRooms);
        swipeRefreshLayout = findViewById(R.id.swipe);
        tvNoData = findViewById(R.id.tvNoData);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvNoData.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                roomDetail();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tools = new Tools(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);

        roomDetail();
    }

    void roomDetail() {
        tools.showLoading();
        restCall.RoomDetails("GetRoomDetails")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RoomDetailDataModel>() {
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
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onNext(RoomDetailDataModel roomDetailDataModel) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (roomDetailDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT) && roomDetailDataModel.getRoomDetailList() != null && !roomDetailDataModel.getRoomDetailList().isEmpty()) {
                                    tvNoData.setVisibility(View.GONE);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AvailableRoomsActivity.this);
                                    recyclerViewAllRooms.setLayoutManager(layoutManager);
                                    allRoomsAdapter = new AllRoomsAdapter(roomDetailDataModel.getRoomDetailList(), AvailableRoomsActivity.this);
                                    recyclerViewAllRooms.setAdapter(allRoomsAdapter);
                                }
                            }
                        });
                    }
                });
    }
}