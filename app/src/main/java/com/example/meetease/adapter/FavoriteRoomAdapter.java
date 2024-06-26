package com.example.meetease.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetease.R;
import com.example.meetease.activity.homeScreen.mainScreen.create.BookMeetingActivity;
import com.example.meetease.appUtils.Tools;
import com.example.meetease.appUtils.VariableBag;
import com.example.meetease.dataModel.FavRoomListDataModel;
import com.example.meetease.dataModel.RoomDetailListNoUpcoming;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRoomAdapter extends RecyclerView.Adapter<FavoriteRoomAdapter.FavoriteViewHolder> {

    String checkFavourite = "0";
    List<FavRoomListDataModel> roomDetailLists, searchList;
    Context context;
    FavoriteAdapterDataClick favoriteAdapterDataClick;

    public interface FavoriteAdapterDataClick {
        void imgFavClick(FavRoomListDataModel dataModel,String position);
    }

    public void updateData(List<FavRoomListDataModel> dataModelList) {
        this.roomDetailLists = dataModelList;
        this.searchList = dataModelList;
        notifyDataSetChanged();
    }

    public void setUpInterFace(FavoriteAdapterDataClick favoriteAdapterDataClick) {
        this.favoriteAdapterDataClick = favoriteAdapterDataClick;
    }

    public void search(CharSequence charSequence, TextView textView, RecyclerView recyclerView) {

        String charString = charSequence.toString().trim();
        if (charString.isEmpty()) {
            searchList = roomDetailLists;
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            int flag = 0;
            List<FavRoomListDataModel> filterList = new ArrayList<>();
            for (FavRoomListDataModel single : roomDetailLists) {
                if (single.getRoom_name().toLowerCase().contains(charString.toLowerCase())) {
                    filterList.add(single);
                    flag = 1;
                }
            }
            if (flag == 1) {
                searchList = filterList;
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        }
        notifyDataSetChanged();
    }

    public FavoriteRoomAdapter(List<FavRoomListDataModel> roomDetailLists, Context context) {
        this.roomDetailLists = roomDetailLists;
        this.searchList = roomDetailLists;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(Tools.bindXML(R.layout.meeting_rooms_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtName.setText(searchList.get(position).getRoom_name());
        holder.txtLocation.setText(searchList.get(position).getLocation());
        holder.txtPrice.setText(VariableBag.CURRENCY + searchList.get(position).getPrice());
        Tools.DisplayImage(context, holder.imgRoom, searchList.get(position).getRoom_img());
        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(view.getContext(), BookMeetingActivity.class));
            }
        });

        checkFavourite = "1";
        if (checkFavourite.equals("0")) {
            holder.imgFavourite.setImageResource(R.drawable.baseline_favorite_border_24);
        } else {
            holder.imgFavourite.setImageResource(R.drawable.baseline_favourite_24);
        }

        holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFavourite.equals("1")) {
                    holder.imgFavourite.setImageResource(R.drawable.baseline_favorite_border_24);
                    checkFavourite = "0";
                } else {
                    holder.imgFavourite.setImageResource(R.drawable.baseline_favourite_24);
                    checkFavourite = "1";
                }
                favoriteAdapterDataClick.imgFavClick(searchList.get(position),""+searchList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        Button btnBookNow;
        ImageView imgRoom, imgFavourite;
        TextView txtName, txtLocation, txtPrice;
        RatingBar ratingBar;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
            imgRoom = itemView.findViewById(R.id.imgRooms);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            txtName = itemView.findViewById(R.id.txtName);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
