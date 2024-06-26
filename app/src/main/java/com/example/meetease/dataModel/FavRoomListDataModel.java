package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavRoomListDataModel {

    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("full_name")
    @Expose
    String full_name;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("mobile")
    @Expose
    String mobile;
    @SerializedName("fav_id")
    @Expose
    String fav_id;
    @SerializedName("room_details_id")
    @Expose
    String room_details_id;
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

    public FavRoomListDataModel( String user_id, String full_name, String email, String mobile, String fav_id, String room_details_id, String room_name, String room_img, String location, String price) {

        this.user_id = user_id;
        this.full_name = full_name;
        this.email = email;
        this.mobile = mobile;
        this.fav_id = fav_id;
        this.room_details_id = room_details_id;
        this.room_name = room_name;
        this.room_img = room_img;
        this.location = location;
        this.price = price;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getRoom_details_id() {
        return room_details_id;
    }

    public void setRoom_details_id(String room_details_id) {
        this.room_details_id = room_details_id;
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
