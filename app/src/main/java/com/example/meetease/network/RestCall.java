package com.example.meetease.network;


import com.example.meetease.dataModel.EditUserResponse;
import com.example.meetease.dataModel.FavRoomDataModel;
import com.example.meetease.dataModel.LoginDataModel;
import com.example.meetease.dataModel.RoomDetailDataModel;
import com.example.meetease.dataModel.RoomDetailListNoUpcomingDataModel;
import com.example.meetease.dataModel.UpComingResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Single;

public interface RestCall {

    @FormUrlEncoded
    @POST("Controller/UserController.php")
    Single<LoginDataModel> LoginUser(
            @Field("tag") String tag,
            @Field("email") String email,
            @Field("password") String password,
            @Field("flag") String flag);

    @FormUrlEncoded
    @POST("Controller/UserController.php")
    Single<UserResponse> AddUser(
            @Field("tag") String tag,
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("fcm_token") String fcm_token,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("Controller/RoomController.php")
    Single<RoomDetailDataModel> RoomDetails(
            @Field("tag") String tag);

    @Multipart
    @POST("Controller/UserController.php")
    Single<EditUserResponse> EditUser(
            @Part("tag") RequestBody tag,
            @Part("user_id") RequestBody user_id,
            @Part("full_name") RequestBody full_name,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part profile_photo1);

    @FormUrlEncoded
    @POST("Controller/UserController.php")
    Single<UserResponse> ResetPassword(
            @Field("tag") String tag,
            @Field("user_id") String user_id,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("Controller/BookingController.php")
    Single<UserResponse> RoomBooking(
            @Field("tag") String tag,
            @Field("user_id") String user_id,
            @Field("room_details_id") String room_d_id,
            @Field("booking_date") String booking_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("total_price") String total_price);

    @FormUrlEncoded
    @POST("Controller/BookingController.php")
    Single<RoomDetailListNoUpcomingDataModel> AvailableRoomDetails(
            @Field("tag") String tag,
            @Field("booking_date") String booking_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time);

    @FormUrlEncoded
    @POST("Controller/FavRoomController.php")
    Single<UserResponse> AddFavRoom(
            @Field("tag") String tag,
            @Field("room_details_id") String room_detail_id,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/FavRoomController.php")
    Single<UserResponse> DeleteFavRoom(
            @Field("tag") String tag,
            @Field("room_details_id") String room_detail_id,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/FavRoomController.php")
    Single<FavRoomDataModel> GetFevRoom(
            @Field("tag") String tag,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/UserController.php")
    Single<UserResponse> deleteUser(
            @Field("tag") String tag,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/OldNewBooking.php")
    Single<UpComingResponse> UpcomingBookings(
            @Field("tag") String tag,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/OldNewBooking.php")
    Single<UpComingResponse> CloseBooking(
            @Field("tag") String tag,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Controller/UserController.php")
    Single<UserResponse> UpdateToken(
            @Field("tag") String tag,
            @Field("fcm_token") String token,
            @Field("platform") String platform,
            @Field("user_id") String user_id);
}
