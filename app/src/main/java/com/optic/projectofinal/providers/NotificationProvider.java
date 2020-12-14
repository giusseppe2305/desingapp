package com.optic.projectofinal.providers;


import com.optic.projectofinal.models.FCMBody;
import com.optic.projectofinal.models.FCMResponse;
import com.optic.projectofinal.retrofit.IFCMApi;
import com.optic.projectofinal.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {

    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
