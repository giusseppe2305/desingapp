package com.optic.projectofinal.retrofit;

import com.optic.projectofinal.modelsRetrofit.FCMBody;
import com.optic.projectofinal.modelsRetrofit.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAASz9j20U:APA91bFHTK1tJn1-_8_KehKQ6n57p56UeXdDfNNPxvfqKqy204EejcnS5q5r64HLz3unrXzjVreXHqh4YWlb8HzJb9aNcSbq9aQ8bpe4AqvXyft6JlyCvlLuIGr9lUVAe8uoz0tMKVwd"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
