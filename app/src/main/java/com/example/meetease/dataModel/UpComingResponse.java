package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpComingResponse {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("upcoming_bookings")
    @Expose
    List<UpComingListResponse> upComingListResponses;

    public UpComingResponse(String message, String status, List<UpComingListResponse> upComingListResponses) {
        this.message = message;
        this.status = status;
        this.upComingListResponses = upComingListResponses;
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

    public List<UpComingListResponse> getUpComingListResponses() {
        return upComingListResponses;
    }

    public void setUpComingListResponses(List<UpComingListResponse> upComingListResponses) {
        this.upComingListResponses = upComingListResponses;
    }
}
