package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavRoomDataModel {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("FevRoomList")
    @Expose
    List<FavRoomListDataModel> favRoomListlList;
    @SerializedName("status")
    @Expose
    String status;

    public FavRoomDataModel(String message, List<FavRoomListDataModel> favRoomListlList, String status) {
        this.message = message;
        this.favRoomListlList = favRoomListlList;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FavRoomListDataModel> getFavRoomListlList() {
        return favRoomListlList;
    }

    public void setFavRoomListlList(List<FavRoomListDataModel> favRoomListlList) {
        this.favRoomListlList = favRoomListlList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
