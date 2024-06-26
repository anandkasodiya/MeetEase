package com.example.meetease.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetease.R;
import com.example.meetease.appUtils.PreferenceManager;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiptFragment extends DialogFragment {

    TextView txtName, txtLocation, txtPrice, txtSelectedDate, txtTimeSlot, txtFinalPrice;
    ImageView imgPdf, imgCancel;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "pdf_download_channel";
    private NotificationManager notificationManager;
    int progress = 0;
    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtSelectedDate = view.findViewById(R.id.txtSelectedDate);
        txtTimeSlot = view.findViewById(R.id.txtTimeSlot);
        txtFinalPrice = view.findViewById(R.id.txtFinalPrice);

        preferenceManager = new PreferenceManager(getContext());

        String price = getArguments().getString("roomPrice");

        txtName.setText(getArguments().getString("roomName"));
        txtLocation.setText(getArguments().getString("roomLocation"));
        txtPrice.setText(VariableBag.CURRENCY + price);
        txtSelectedDate.setText(getArguments().getString("selectedDate"));
        txtTimeSlot.setText(getArguments().getString("startTime") + " - " + getArguments().getString("endTime"));

        String startTime = getArguments().getString("startTime");
        String endTime = getArguments().getString("endTime");

        String startHour = "", startMin = "", endHour = "", endMin = "";
        boolean checkMin = false;

        for (int i = 0; i < startTime.length(); i++) {
            if (!checkMin) {
                if (startTime.charAt(i) == ':') {
                    checkMin = true;
                } else {
                    startHour += startTime.charAt(i);
                }
            } else {
                if (startTime.charAt(i) == ':') {
                    break;
                } else {
                    startMin += startTime.charAt(i);
                }
            }
        }

        checkMin = false;

        for (int i = 0; i < endTime.length(); i++) {
            if (!checkMin) {
                if (endTime.charAt(i) == ':') {
                    checkMin = true;
                } else {
                    endHour += endTime.charAt(i);
                }
            } else {
                if (endTime.charAt(i) == ':') {
                    break;
                }
                endMin += endTime.charAt(i);
            }
        }

        int endTimeTemp = (Integer.parseInt(endHour) * 60) + Integer.parseInt(endMin);
        int startTimeTemp = (Integer.parseInt(startHour) * 60) + Integer.parseInt(startMin);
        int minuteTemp = endTimeTemp - startTimeTemp;
        int hourTemp = 0;
        if (minuteTemp % 60 != 0) {
            hourTemp = minuteTemp / 60;
            hourTemp = hourTemp + 1;
        } else {
            hourTemp = minuteTemp / 60;
        }

        txtFinalPrice.setText(VariableBag.CURRENCY + hourTemp * Integer.parseInt(price));

        imgPdf = view.findViewById(R.id.imgPdf);
        imgCancel = view.findViewById(R.id.imgCancel);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PDF Download", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        imgPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfDocument pdfDocument = new PdfDocument();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();

                Paint appNamePaint = new Paint();
                appNamePaint.setTextSize(20);
                appNamePaint.setColor(Color.BLACK);
                canvas.drawText("MeetEase", 20, 50, appNamePaint);

                Paint titlePaint = new Paint();
                titlePaint.setTextSize(16);
                titlePaint.setColor(Color.BLACK);

                Paint textPaint = new Paint();
                textPaint.setTextSize(12);
                textPaint.setColor(Color.BLACK);

                int startY = 100;

                canvas.drawText("Booking Receipt", 20, startY, titlePaint);
                startY += 20;

                drawText(canvas, "UserName:", preferenceManager.getKeyValueString(VariableBag.full_name, ""), 20, startY, textPaint);
                drawText(canvas, "Email:", preferenceManager.getKeyValueString(VariableBag.email, ""), 20, startY += 20, textPaint);
                drawText(canvas, "Name:", txtName.getText().toString(), 20, startY += 20, textPaint);
                drawText(canvas, "Location:", txtLocation.getText().toString(), 20, startY += 20, textPaint);
                drawText(canvas, "Price:", txtPrice.getText().toString(), 20, startY += 20, textPaint);
                drawText(canvas, "Date:", txtSelectedDate.getText().toString(), 20, startY += 20, textPaint);
                drawText(canvas, "Time:", txtTimeSlot.getText().toString(), 20, startY += 20, textPaint);

                startY += 10;
                canvas.drawLine(20, startY, 280, startY, textPaint);

                drawText(canvas, "Payable Amount:", txtFinalPrice.getText().toString(), 20, startY += 20, textPaint);

                pdfDocument.finishPage(page);

                File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                String fileName = "booking_receipt_" + System.currentTimeMillis() + ".pdf";
                File filePath = new File(directory, fileName);

                try {
                    FileOutputStream outputStream = new FileOutputStream(filePath);
                    pdfDocument.writeTo(outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pdfDocument.close();

                Tools.showCustomToast(getContext(), "PDF saved: " + filePath, view.findViewById(R.id.customToastLayout), getLayoutInflater());
                dismiss();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setContentTitle("Generating PDF")
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.logo)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

                notificationManager.notify(NOTIFICATION_ID, builder.build());
                progress = 0;
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        progress += 10;
                        if (progress < 100) {
                            builder.setProgress(100, progress, false);
                            notificationManager.notify(NOTIFICATION_ID, builder.build());
                            sendEmptyMessageDelayed(0, 500); // Simulating a delay
                        } else {
                            builder.setContentText("Download complete")
                                    .setProgress(0, 0, false);
                            notificationManager.notify(NOTIFICATION_ID, builder.build());
                            dismiss();
                        }
                    }
                };
                handler.sendEmptyMessageDelayed(0, 500);
            }
        });

        return view;
    }

    private void drawText(Canvas canvas, String label, String value, float x, float y, Paint paint) {
        canvas.drawText(label, x, y, paint);
        canvas.drawText(value, x + 150, y, paint); // Adjust the X position for the value
    }
}