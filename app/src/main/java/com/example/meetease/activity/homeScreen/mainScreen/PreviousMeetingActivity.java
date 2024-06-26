package com.example.meetease.activity.homeScreen.mainScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.UpComingListResponse;
import com.example.meetease.dataModel.UpComingResponse;
import com.example.meetease.adapter.PreviousMeetingAdapter;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class PreviousMeetingActivity extends AppCompatActivity {

    RecyclerView recyclerviewPreviousMeeting;
    PreviousMeetingAdapter previousMeetingAdapter;
    EditText etvSearch;
    TextView tvNoData;
    ImageView ivClose, ivBack;
    RestCall restCall;
    Tools tools;
    SwipeRefreshLayout swipeRefreshLayout;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_meeting);

        tools = new Tools(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        recyclerviewPreviousMeeting = findViewById(R.id.recyclerviewPreviousMeeting);
        etvSearch = findViewById(R.id.etvSearch);
        tvNoData = findViewById(R.id.tvNoData);
        ivClose = findViewById(R.id.ivClose);
        ivBack = findViewById(R.id.ivBack);
        swipeRefreshLayout = findViewById(R.id.swipe);

        preferenceManager = new PreferenceManager(this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                roomDetail();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ivClose.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);

        ivClose.setOnClickListener(view -> {
            ivClose.setVisibility(View.GONE);
            etvSearch.setText("");
        });
        etvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (previousMeetingAdapter != null) {
                    if (!etvSearch.getText().toString().isEmpty()) {
                        ivClose.setVisibility(View.VISIBLE);
                    } else {
                        ivClose.setVisibility(View.GONE);
                    }
                    previousMeetingAdapter.search(charSequence, tvNoData, recyclerviewPreviousMeeting);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tools.showLoading();
        roomDetail();

    }

    void roomDetail() {
        tools.showLoading();
        restCall.CloseBooking("ClosedBookings", preferenceManager.getKeyValueString(VariableBag.user_id, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<UpComingResponse>() {
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
                    public void onNext(UpComingResponse upComingResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (upComingResponse.getStatus().equals(VariableBag.SUCCESS_RESULT) && upComingResponse.getUpComingListResponses() != null && upComingResponse.getUpComingListResponses().size() > 0) {
                                    tvNoData.setVisibility(View.GONE);

                                    List<UpComingListResponse> reversedList = new ArrayList<>(upComingResponse.getUpComingListResponses());
                                    Collections.reverse(reversedList);

                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PreviousMeetingActivity.this);
                                    recyclerviewPreviousMeeting.setLayoutManager(layoutManager);
                                    previousMeetingAdapter = new PreviousMeetingAdapter(reversedList, PreviousMeetingActivity.this);
                                    recyclerviewPreviousMeeting.setAdapter(previousMeetingAdapter);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
    }
}