package com.example.automoto.Interface;

import static com.example.automoto.Constant.CONTENT_TYPE;
import static com.example.automoto.Constant.SERVER_KEY;

import com.example.automoto.Model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key="+SERVER_KEY, "CONTENT-TYPE:"+CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);

}
