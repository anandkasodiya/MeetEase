package com.example.meetease.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.create.BookMeetingActivity;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.dataModel.RoomDetailList;

import java.util.List;

public class AllRoomsAdapter extends RecyclerView.Adapter<AllRoomsAdapter.AllRoomsDataViewHolder> {

    List<RoomDetailList> roomDetailLists;
    Context context;

    public AllRoomsAdapter(List<RoomDetailList> roomDetailLists, Context context) {
        this.roomDetailLists = roomDetailLists;
        this.context = context;
    }

    @NonNull
    @Override
    public AllRoomsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllRoomsDataViewHolder(Tools.bindXML(R.layout.all_rooms_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull AllRoomsDataViewHolder holder, int position) {
        holder.txtName.setText(roomDetailLists.get(position).getRoom_name());
        holder.txtLocation.setText(roomDetailLists.get(position).getLocation());

        Tools.DisplayImage(context, holder.imgRoom, roomDetailLists.get(position).getRoom_img());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookMeetingActivity.class);
                intent.putExtra("RoomIdAllRoom",roomDetailLists.get(position).getRoom_d_id());
                intent.putExtra("RoomNameAllRoom",roomDetailLists.get(position).getRoom_name());
                intent.putExtra("RoomLocationAllRoom",roomDetailLists.get(position).getLocation());
                intent.putExtra("RoomPriceAllRoom",roomDetailLists.get(position).getPrice());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomDetailLists.size();
    }

    public static class AllRoomsDataViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocation, txtName;
        ImageView imgRoom;

        public AllRoomsDataViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            imgRoom = itemView.findViewById(R.id.imgRoom);

            txtName.setSelected(true);
        }
    }
}
