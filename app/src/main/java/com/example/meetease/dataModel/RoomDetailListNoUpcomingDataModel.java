package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailListNoUpcomingDataModel {


    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("UnBookList")
    @Expose
    List<RoomDetailListNoUpcoming> roomDetailListNoUpcoming;
    @SerializedName("status")
    @Expose
    String status;

    public RoomDetailListNoUpcomingDataModel(String message, List<RoomDetailListNoUpcoming> roomDetailListNoUpcoming, String status) {
        this.message = message;
        this.roomDetailListNoUpcoming = roomDetailListNoUpcoming;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RoomDetailListNoUpcoming> getRoomDetailListNoUpcoming() {
        return roomDetailListNoUpcoming;
    }

    public void setRoomDetailListNoUpcoming(List<RoomDetailListNoUpcoming> roomDetailListNoUpcoming) {
        this.roomDetailListNoUpcoming = roomDetailListNoUpcoming;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
