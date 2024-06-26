package com.example.meetease.dataModel;

public class CreateReservationDataModel {

    String roomName;
    String roomLocation;
    String roomPrice;
    String roomRating;
    String roomImage;

    public CreateReservationDataModel(String roomName, String roomLocation, String roomPrice, String roomRating, String roomImage) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.roomPrice = roomPrice;
        this.roomRating = roomRating;
        this.roomImage = roomImage;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomRating() {
        return roomRating;
    }

    public void setRoomRating(String roomRating) {
        this.roomRating = roomRating;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
}
