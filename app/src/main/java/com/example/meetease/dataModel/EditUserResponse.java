package com.example.meetease.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditUserResponse {

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("profile_photo1")
    @Expose
    String profile_photo;

    public EditUserResponse(String message, String status, String profile_photo) {
        this.message = message;
        this.status = status;
        this.profile_photo = profile_photo;
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

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }
}
