package com.example.meetease.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetease.R;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.UpComingListResponse;
import com.example.meetease.fragment.FilterFragment;
import com.example.meetease.fragment.ReceiptFragment;

import java.util.List;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.UpComingViewHolder> {

    List<UpComingListResponse> upComingListResponses;
    Context context;
    ReceiptFragment receiptFragment;
    FragmentManager fragmentManager;

    public UpComingAdapter(List<UpComingListResponse> upComingListResponses, Context context, FragmentManager fragmentManager) {
        this.upComingListResponses = upComingListResponses;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public UpComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpComingViewHolder(Tools.bindXML(R.layout.upcoming_meeting_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtName.setText(upComingListResponses.get(position).getRoom_name());
        holder.txtPrice.setText(VariableBag.CURRENCY + upComingListResponses.get(position).getPrice() + "/Hour");
        holder.txtLocation.setText(upComingListResponses.get(position).getLocation());
        holder.txtSelectedDate.setText(upComingListResponses.get(position).getBooking_date());
        holder.btnViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiptFragment = new ReceiptFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                receiptFragment.show(fragmentTransaction, "#tag");
                receiptFragment.setCancelable(false);

                Bundle bundle = new Bundle();
                bundle.putString("roomName", upComingListResponses.get(position).getRoom_name());
                bundle.putString("roomLocation", upComingListResponses.get(position).getLocation());
                bundle.putString("roomPrice", upComingListResponses.get(position).getPrice());
                bundle.putString("selectedDate", upComingListResponses.get(position).getBooking_date());
                bundle.putString("startTime", upComingListResponses.get(position).getStart_time());
                bundle.putString("endTime", upComingListResponses.get(position).getEnd_time());

                receiptFragment.setArguments(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upComingListResponses.size();
    }

    class UpComingViewHolder extends RecyclerView.ViewHolder {
        Button btnViewInfo;
        ImageView imgRoom, imgFavourite;
        TextView txtName, txtLocation, txtPrice, txtSelectedDate;

        public UpComingViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewInfo = itemView.findViewById(R.id.btnViewInfo);
            imgRoom = itemView.findViewById(R.id.imgRooms);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            txtName = itemView.findViewById(R.id.txtName);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtSelectedDate = itemView.findViewById(R.id.txtSelectedDate);

            txtName.setSelected(true);
        }
    }
}
