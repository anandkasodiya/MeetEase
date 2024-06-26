package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpComingListResponse {

    @SerializedName("room_booking_id")
    @Expose
    String room_booking_id;
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("room_details_id")
    @Expose
    String room_details_id;
    @SerializedName("booking_date")
    @Expose
    String booking_date;
    @SerializedName("start_time")
    @Expose
    String start_time;
    @SerializedName("end_time")
    @Expose
    String end_time;
    @SerializedName("upcoming_status")
    @Expose
    String upcoming_status;
    @SerializedName("room_name")
    @Expose
    String room_name;
    @SerializedName("room_img")
    @Expose
    String room_img;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("avg_rating")
    @Expose
    String avg_rating;

    public UpComingListResponse(String room_booking_id, String user_id, String room_details_id, String booking_date, String start_time, String end_time, String upcoming_status, String room_name, String room_img, String location, String price,String avg_rating) {
        this.room_booking_id = room_booking_id;
        this.user_id = user_id;
        this.room_details_id = room_details_id;
        this.booking_date = booking_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.upcoming_status = upcoming_status;
        this.room_name = room_name;
        this.room_img = room_img;
        this.location = location;
        this.price = price;
        this.avg_rating = avg_rating;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getRoom_booking_id() {
        return room_booking_id;
    }

    public void setRoom_booking_id(String room_booking_id) {
        this.room_booking_id = room_booking_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRoom_details_id() {
        return room_details_id;
    }

    public void setRoom_details_id(String room_details_id) {
        this.room_details_id = room_details_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUpcoming_status() {
        return upcoming_status;
    }

    public void setUpcoming_status(String upcoming_status) {
        this.upcoming_status = upcoming_status;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
