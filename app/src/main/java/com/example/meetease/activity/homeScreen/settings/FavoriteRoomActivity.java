package com.example.meetease.activity.homeScreen.settings;

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
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.create.CreateReservationActivity;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.FavRoomDataModel;
import com.example.meetease.adapter.FavoriteRoomAdapter;
import com.example.meetease.dataModel.FavRoomListDataModel;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.example.meetease.network.UserResponse;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FavoriteRoomActivity extends AppCompatActivity {

    RecyclerView recycleFavRoom;
    EditText etvSearch;
    TextView tvNoData;
    ImageView ivClose, ivBack;
    RestCall restCall;
    SwipeRefreshLayout swipeRefreshLayout;
    PreferenceManager preferenceManager;
    Tools tools;
    FavoriteRoomAdapter favoriteRoomAdapter;
    String position1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_room);

        recycleFavRoom = findViewById(R.id.recycleFavRoom);
        etvSearch = findViewById(R.id.etvSearch);
        tvNoData = findViewById(R.id.tvNoData);
        ivClose = findViewById(R.id.ivClose);
        ivBack = findViewById(R.id.ivBack);
        swipeRefreshLayout = findViewById(R.id.swipe);
        ivClose.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);

        preferenceManager = new PreferenceManager(this);
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
                roomDetail();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
                if (favoriteRoomAdapter != null) {
                    if (!etvSearch.getText().toString().isEmpty()) {
                        ivClose.setVisibility(View.VISIBLE);
                    } else {
                        ivClose.setVisibility(View.GONE);
                    }
                    favoriteRoomAdapter.search(charSequence, tvNoData, recycleFavRoom);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (favRoomDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT)
                                        && favRoomDataModel.getFavRoomListlList() != null
                                        && favRoomDataModel.getFavRoomListlList().size() > 0) {
                                    recycleFavRoom.setLayoutManager(new LinearLayoutManager(FavoriteRoomActivity.this));
                                    favoriteRoomAdapter = new FavoriteRoomAdapter(favRoomDataModel.getFavRoomListlList(), FavoriteRoomActivity.this);
                                    recycleFavRoom.setAdapter(favoriteRoomAdapter);
                                    tvNoData.setVisibility(View.GONE);
                                    favoriteRoomAdapter.setUpInterFace(new FavoriteRoomAdapter.FavoriteAdapterDataClick() {
                                        @Override
                                        public void imgFavClick(FavRoomListDataModel dataModel, String position) {
                                            deleteFavRoom(dataModel.getRoom_details_id());
                                            position1 = position;
                                        }
                                    });
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
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
                                    if (position1.equals("1")) {
                                        recycleFavRoom.setVisibility(View.GONE);
                                        tvNoData.setVisibility(View.VISIBLE);
                                    } else {
                                        roomDetail();
                                    }

                                }
                            }
                        });
                    }
                });
    }
}