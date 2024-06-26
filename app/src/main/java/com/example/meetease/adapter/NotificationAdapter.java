package com.example.meetease.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetease.R;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.dataModel.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationDataViewHolder> {

    Context context;
    private List<NotificationModel> notificationList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationDataViewHolder(Tools.bindXML(R.layout.notification_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationDataViewHolder holder, int position) {
        holder.txtNotificationName.setText(notificationList.get(position).getTitle());
        holder.txtNotificationDescription.setText(notificationList.get(position).getDescription());
        holder.txtNotificationTime.setText(notificationList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationDataViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotificationName, txtNotificationDescription, txtNotificationTime;

        public NotificationDataViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNotificationName = itemView.findViewById(R.id.txtNotificationName);
            txtNotificationDescription = itemView.findViewById(R.id.txtNotificationDescription);
            txtNotificationTime = itemView.findViewById(R.id.txtNotificationTime);

        }
    }
}
