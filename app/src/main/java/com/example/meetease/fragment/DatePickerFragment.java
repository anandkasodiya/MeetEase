package com.example.meetease.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.example.meetease.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    DatePicker datePicker;
    Button btnCancel,btnSave;
    ButtonClick buttonClick;
    String SelectedEventDate;
    String selectDay,selectMonth,selectYear;

    public interface ButtonClick{
        void saveClick(String date,String day,String month,String year);
    }

    public void  setUpInterface(ButtonClick buttonClick){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker_fargment, container, false);
        datePicker = view.findViewById(R.id.datePicker);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);

        Calendar calendar = Calendar.getInstance();

        datePicker.setMinDate(calendar.getTimeInMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(year,month,dayOfMonth);
                    selectDay = ""+dayOfMonth;
                    selectMonth = ""+month;
                    selectYear = ""+year;
                    SelectedEventDate = sdf.format(selectedDateCalendar.getTime());
                }
            });
        } else {
            CalendarView calendarView = datePicker.getCalendarView();
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                    // Compare the selected date with the current date
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(year, month, dayOfMonth);

                    if (selectedDateCalendar.after(calendar)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SelectedEventDate = sdf.format(selectedDateCalendar.getTime());
                    } else {

                    }
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int date = datePicker.getMonth()+1;

                String builder = datePicker.getDayOfMonth() + "/" +
                        date + "/" +
                        datePicker.getYear();
                SelectedEventDate = builder;
                buttonClick.saveClick(SelectedEventDate, String.valueOf(datePicker.getDayOfMonth()), String.valueOf(date), String.valueOf(datePicker.getYear()));
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