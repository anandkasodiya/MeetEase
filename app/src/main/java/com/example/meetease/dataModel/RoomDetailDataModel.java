package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailDataModel {

    @SerializedName("RoomList")
    @Expose
    List<RoomDetailList> roomDetailList;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("status")
    @Expose
    String status;

    public RoomDetailDataModel(ArrayList<RoomDetailList> roomDetailList, String message, String status) {
        this.roomDetailList = roomDetailList;
        this.message = message;
        this.status = status;
    }

    public List<RoomDetailList> getRoomDetailList() {
        return roomDetailList;
    }

    public void setRoomDetailList(ArrayList<RoomDetailList> roomDetailList) {
        this.roomDetailList = roomDetailList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
