package com.example.meetease.activity.homeScreen.mainScreen.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.FavRoomDataModel;
import com.example.meetease.dataModel.FavRoomListDataModel;
import com.example.meetease.dataModel.RoomDetailList;
import com.example.meetease.dataModel.RoomDetailListNoUpcoming;
import com.example.meetease.dataModel.RoomDetailListNoUpcomingDataModel;
import com.example.meetease.adapter.CreateReservationAdapter;
import com.example.meetease.fragment.FilterFragment;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.example.meetease.network.UserResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CreateReservationActivity extends AppCompatActivity {

    RecyclerView recyclerViewMeetingRooms;
    CreateReservationAdapter createReservationAdapter;
    EditText etvSearch;
    TextView tvNoData;
    List<FavRoomListDataModel> favApiList = new ArrayList<>();
    Boolean flag = false;
    PreferenceManager preferenceManager;
    ImageView ivClose, img_filter, ivBack;
    FilterFragment filterFragment;
    SwipeRefreshLayout swipeRefreshLayout;
    RestCall restCall;
    Tools tools;
    List<RoomDetailListNoUpcoming> apiList = new ArrayList<>();
    public int year, month, day, startHour, startMinute, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reservation);

        preferenceManager = new PreferenceManager(this);
        recyclerViewMeetingRooms = findViewById(R.id.recyclerViewMeetingRooms);
        etvSearch = findViewById(R.id.etvSearch);
        tvNoData = findViewById(R.id.tvNoData);
        img_filter = findViewById(R.id.img_filter);
        ivClose = findViewById(R.id.ivClose);
        ivBack = findViewById(R.id.ivBack);
        swipeRefreshLayout = findViewById(R.id.swipe);
        tools = new Tools(this);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);

        roomDetail();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tools.showLoading();
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

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment = new FilterFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                filterFragment.show(fragmentTransaction, "#tag");
                filterFragment.setCancelable(false);

                filterFragment.setUpInterface(new FilterFragment.FilterApply() {
                    @Override
                    public void filterList(String city, String Price, String Rating) {

                        if (apiList != null && apiList.size() > 0) {
                            if (!city.isEmpty() && !Price.isEmpty() && !Rating.isEmpty()) {
                                updateData(ratingFilter(priceFilter(cityFilter(apiList, city), Price), Rating));
                            } else if (!city.isEmpty() && !Price.isEmpty() && Rating.isEmpty()) {
                                updateData(priceFilter(cityFilter(apiList, city), Price));
                            } else if (!city.isEmpty() && Price.isEmpty() && !Rating.isEmpty()) {
                                updateData(ratingFilter(cityFilter(apiList, city), Rating));
                            } else if (city.isEmpty() && !Price.isEmpty() && !Rating.isEmpty()) {
                                updateData(ratingFilter(priceFilter(apiList, Price), Rating));
                            } else if (!city.isEmpty() && Price.isEmpty() && Rating.isEmpty()) {
                                updateData(cityFilter(apiList, city));
                            } else if (city.isEmpty() && !Price.isEmpty() && Rating.isEmpty()) {
                                updateData(priceFilter(apiList, Price));
                            } else if (city.isEmpty() && Price.isEmpty() && !Rating.isEmpty()) {
                                updateData(ratingFilter(apiList, Rating));
                            } else {
                                updateData(apiList);
                            }
                        }
                    }

                    @Override
                    public void reset() {
                        if (apiList != null && apiList.size() > 0) {
                            createReservationAdapter.updateData(apiList);
                        }
                    }
                });
            }
        });

        Intent intent = getIntent();
        year = Integer.parseInt(intent.getExtras().getString("year", "0"));
        month = Integer.parseInt(intent.getExtras().getString("month", "0"));
        day = Integer.parseInt(intent.getExtras().getString("day", "0"));
        startHour = Integer.parseInt(intent.getExtras().getString("startHour", "0"));
        startMinute = Integer.parseInt(intent.getExtras().getString("startMinute", "0"));
        endHour = Integer.parseInt(intent.getExtras().getString("endHour", "0"));
        endMinute = Integer.parseInt(intent.getExtras().getString("endMinute", "0"));

        etvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (createReservationAdapter != null) {
                    if (!etvSearch.getText().toString().isEmpty()) {
                        ivClose.setVisibility(View.VISIBLE);
                    } else {
                        ivClose.setVisibility(View.GONE);
                    }
                    createReservationAdapter.search(charSequence, tvNoData, recyclerViewMeetingRooms);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    List<RoomDetailListNoUpcoming> cityFilter(List<RoomDetailListNoUpcoming> list, String city) {
        List<RoomDetailListNoUpcoming> filteredListCity = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLocation().equals(city)) {
                filteredListCity.add(list.get(i));
            }
        }
        return filteredListCity;
    }

    List<RoomDetailListNoUpcoming> priceFilter(List<RoomDetailListNoUpcoming> list, String Price) {
        List<RoomDetailListNoUpcoming> filteredListPrice = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (Integer.parseInt(list.get(i).getPrice()) <= Integer.parseInt(Price)) {
                filteredListPrice.add(list.get(i));
            }
        }
        return filteredListPrice;
    }

    List<RoomDetailListNoUpcoming> ratingFilter(List<RoomDetailListNoUpcoming> list, String Rating) {
        List<RoomDetailListNoUpcoming> filteredListRating = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRating().equals(Rating)) {
                filteredListRating.add(list.get(i));
            }
        }
        return filteredListRating;
    }

    void updateData(List<RoomDetailListNoUpcoming> list) {
        if (list.size() > 0) {
            tvNoData.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
        createReservationAdapter.updateData(list);
    }

    private void AvailableRoomDetails() {

        restCall.AvailableRoomDetails("UnbookedRoom", year + "-" + month + "-" + day, startHour + ":" + startMinute, endHour + ":" + endMinute)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<RoomDetailListNoUpcomingDataModel>() {
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
                    public void onNext(RoomDetailListNoUpcomingDataModel roomDetailListNoUpcomingDataModel) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                apiList = roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming();
                                tools.stopLoading();
                                if (roomDetailListNoUpcomingDataModel.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_RESULT)
                                        && roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming() != null
                                        && roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming().size() > 0) {
                                    if (!flag) {
                                        AvailableRoomDetails();
                                        flag = true;
                                    } else {
                                        tools.stopLoading();
                                        tvNoData.setVisibility(View.GONE);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreateReservationActivity.this);
                                        recyclerViewMeetingRooms.setLayoutManager(layoutManager);

                                        for (int p = 0; p < roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming().size(); p++) {
                                            apiList.get(p).setUpcoming_status("0");
                                            for (int k = 0; k < favApiList.size(); k++) {
                                                if (favApiList.get(k).getUser_id().equals(preferenceManager.getKeyValueString(VariableBag.user_id, ""))) {
                                                    if (roomDetailListNoUpcomingDataModel.getRoomDetailListNoUpcoming().get(p).getRoom_d_id().equals(favApiList.get(k).getRoom_details_id())) {
                                                        apiList.get(p).setUpcoming_status("1");
                                                    }
                                                }
                                            }
                                        }

                                        createReservationAdapter = new CreateReservationAdapter(apiList, CreateReservationActivity.this);
                                        recyclerViewMeetingRooms.setAdapter(createReservationAdapter);
                                        createReservationAdapter.setUpInterFace(new CreateReservationAdapter.CreateReservationAdapterDataClick() {
                                            @Override
                                            public void bookDataClick(RoomDetailListNoUpcoming createReservationDataModel) {
                                                Intent intent = new Intent(CreateReservationActivity.this, DetailsActivity.class);
                                                intent.putExtra("roomName", createReservationDataModel.getRoom_name());
                                                intent.putExtra("roomPrice", createReservationDataModel.getPrice());
                                                intent.putExtra("roomLocation", createReservationDataModel.getLocation());
                                                intent.putExtra("roomRating", createReservationDataModel.getRating());
                                                intent.putExtra("roomImage", createReservationDataModel.getRoom_img());
                                                intent.putExtra("roomId", createReservationDataModel.getRoom_d_id());
                                                intent.putExtra("bookingDate", day + "-" + month + "-" + year);
                                                intent.putExtra("bookingStartTime", startHour + ":" + startMinute);
                                                intent.putExtra("bookingEndTime", endHour + ":" + endMinute);

                                                int endTime = (endHour * 60) + endMinute;
                                                int startTime = (startHour * 60) + startMinute;
                                                int minute = endTime - startTime;
                                                int hour = 0;
                                                if (minute % 60 != 0) {
                                                    hour = minute / 60;
                                                    hour = hour + 1;
                                                } else {
                                                    hour = minute / 60;
                                                }
                                                intent.putExtra("totalTime", hour);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void imgFavClick(RoomDetailListNoUpcoming dataModel, String checkFavourite) {
                                                if (checkFavourite.equals("1")) {
                                                    addFavRoom(dataModel.getRoom_d_id());
                                                } else {
                                                    deleteFavRoom(dataModel.getRoom_d_id());
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                });
    }

    void roomDetail() {
        tools.showLoading();
        restCall.GetFevRoom("GetFavRoom", preferenceManager.getKeyValueString(VariableBag.user_id, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<FavRoomDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                tvNoData.setVisibility(View.VISIBLE);
                                Tools.showCustomToast(getApplicationContext(), "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });
                    }

                    @Override
                    public void onNext(FavRoomDataModel favRoomDataModel) {
                        tools.stopLoading();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (favRoomDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    AvailableRoomDetails();
                                    tvNoData.setVisibility(View.GONE);
                                    if (favRoomDataModel.getFavRoomListlList() != null) {
                                        favApiList = favRoomDataModel.getFavRoomListlList();
                                    }
                                } else {
                                    AvailableRoomDetails();
                                }

                            }
                        });
                    }
                });
    }

    void addFavRoom(String roomId) {
        restCall.AddFavRoom("AddFavRoom", roomId, preferenceManager.getKeyValueString(VariableBag.user_id, ""))
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
                                Tools.showCustomToast(getApplicationContext(), "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userResponse.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    Tools.showCustomToast(CreateReservationActivity.this, "Added Room To Favourites", findViewById(R.id.customToastLayout), getLayoutInflater());
                                }
                            }
                        });
                    }
                });
    }

    void deleteFavRoom(String roomId) {
        restCall.DeleteFavRoom("DeleteFavRoom", roomId, preferenceManager.getKeyValueString(VariableBag.user_id, ""))
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
                                Tools.showCustomToast(getApplicationContext(), "No Internet", findViewById(R.id.customToastLayout), getLayoutInflater());
                            }
                        });
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userResponse.getStatus().equals(VariableBag.SUCCESS_RESULT)) {
                                    Tools.showCustomToast(CreateReservationActivity.this, "Deleted Room From Favourites", findViewById(R.id.customToastLayout), getLayoutInflater());
                                }
                            }
                        });
                    }
                });
    }

}