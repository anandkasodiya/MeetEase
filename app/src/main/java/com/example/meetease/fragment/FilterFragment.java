package com.example.meetease.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.settings.AvailableRoomsActivity;
import com.example.meetease.adapter.AllRoomsAdapter;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.RoomDetailDataModel;
import com.example.meetease.network.RestCall;
import com.example.meetease.network.RestClient;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FilterFragment extends DialogFragment {

    Button btnApply, btnReset;
    RatingBar ratingBar;
    TextView txtSelectedRange;
    RangeSlider priceRangeSlider;
    Spinner citySpinner;
    String selectedCity = "";
    String rating = "";
    String price = "";
    RestCall restCall;
    FilterApply filterApply;
    Tools tools;
    List<String> cityList = new ArrayList<>();

    public interface FilterApply {
        void filterList(String city, String Price, String Rating);

        void reset();
    }

    public void setUpInterface(FilterApply filterApply) {
        this.filterApply = filterApply;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        tools = new Tools(getContext());
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        ratingBar = view.findViewById(R.id.ratingBar);
        priceRangeSlider = view.findViewById(R.id.priceRangeSlider);
        citySpinner = view.findViewById(R.id.citySpinner);
        txtSelectedRange = view.findViewById(R.id.txtSelectedRange);

        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        cityList.add("Select City");
        roomDetail();
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        priceRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                price = Tools.floatToInt(value) + "";
                txtSelectedRange.setText("0 - " + price);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = Tools.floatToInt(v) + "";
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCity.equals("Select City")){
                    selectedCity = "";
                }
                filterApply.filterList(selectedCity, price, rating);
                dismiss();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterApply.reset();
                dismiss();
            }
        });

        return view;
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    @Override
                    public void onNext(RoomDetailDataModel roomDetailDataModel) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                if (roomDetailDataModel.getStatus().equals(VariableBag.SUCCESS_RESULT) && roomDetailDataModel.getRoomDetailList() != null && !roomDetailDataModel.getRoomDetailList().isEmpty()) {
                                    List<String> tempCityList = new ArrayList<>();
                                    tempCityList.add("Select City");
                                    for (int i=0;i<roomDetailDataModel.getRoomDetailList().size();i++){
                                        boolean flag = false;
                                        for (int p=0;p<tempCityList.size();p++){
                                            if (roomDetailDataModel.getRoomDetailList().get(i).getLocation().equals(tempCityList.get(p))) {
                                                flag = true;
                                            }
                                        }
                                        if (!flag){
                                            tempCityList.add(roomDetailDataModel.getRoomDetailList().get(i).getLocation());
                                            cityList.add(roomDetailDataModel.getRoomDetailList().get(i).getLocation());
                                        }

                                    }
                                }
                            }
                        });
                    }
                });
    }
}