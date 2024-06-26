package com.example.meetease.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StartTimePickerFragment extends DialogFragment {

    TimePicker timePicker;
    Button btnCancel, btnSave;
    String SelectedStartTime;

    ButtonClick buttonClick;
    SimpleDateFormat sdf;
    String selectMin, selectHour;

    public interface ButtonClick {
        void saveClick(String Time, String hour, String min);
    }

    public void setUpInterface(ButtonClick buttonClick) {
        this.buttonClick = buttonClick;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);

        timePicker = view.findViewById(R.id.timePicker);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        PreferenceManager preferenceManager = new PreferenceManager(getContext());

        timePicker.setIs24HourView(true);
        if (preferenceManager.getKeyValueBoolean("abc")){
            timePicker.setHour(Calendar.getInstance().getTime().getHours());
            timePicker.setMinute(Calendar.getInstance().getTime().getMinutes());
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (preferenceManager.getKeyValueBoolean("abc")){
                    if (hour<Calendar.getInstance().getTime().getHours()){
                        timePicker.setHour(Calendar.getInstance().getTime().getHours());
                    }else if (hour==Calendar.getInstance().getTime().getHours()&&minute<Calendar.getInstance().getTime().getMinutes()){
                        timePicker.setMinute(Calendar.getInstance().getTime().getMinutes());
                    }else {
                        String builder = timePicker.getHour() + ":" +
                                timePicker.getMinute();
                        SelectedStartTime = builder;

                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hour);
                        selectedTime.set(Calendar.MINUTE, minute);
                        sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        SelectedStartTime = sdf.format(selectedTime.getTime());
                    }
                }
                else {
                    String builder = timePicker.getHour() + ":" +
                            timePicker.getMinute();
                    SelectedStartTime = builder;

                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour);
                    selectedTime.set(Calendar.MINUTE, minute);
                    sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    SelectedStartTime = sdf.format(selectedTime.getTime());
                }


            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String builder = timePicker.getHour() + ":" +
                        timePicker.getMinute();
                SelectedStartTime = builder;
                buttonClick.saveClick(SelectedStartTime, String.valueOf(timePicker.getHour()), String.valueOf(timePicker.getMinute()));
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}