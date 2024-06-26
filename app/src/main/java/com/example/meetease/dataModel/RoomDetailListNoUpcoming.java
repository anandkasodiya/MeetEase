package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomDetailListNoUpcoming {

    @SerializedName("room_details_id")
    @Expose
    String room_d_id;
    @SerializedName("room_name")
    @Expose
    String room_name;
    @SerializedName("room_img")
    @Expose
    String room_img;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("avg_rating")
    @Expose
    String rating;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("upcoming_status")
    @Expose
    String upcoming_status = "0";

    public RoomDetailListNoUpcoming(String room_d_id, String room_name, String room_img, String price, String location, String rating,String upcoming_status) {
        this.room_d_id = room_d_id;
        this.room_name = room_name;
        this.room_img = room_img;
        this.price = price;
        this.location = location;
        this.rating = rating;
        this.upcoming_status = upcoming_status;
    }

    public String getUpcoming_status() {
        return upcoming_status;
    }

    public void setUpcoming_status(String upcoming_status) {
        this.upcoming_status = upcoming_status;
    }

    public String getRoom_d_id() {
        return room_d_id;
    }

    public void setRoom_d_id(String room_d_id) {
        this.room_d_id = room_d_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_img() {
        return room_img;
    }

    public void setRoom_img(String room_img) {
        this.room_img = room_img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
