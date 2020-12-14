package com.optic.projectofinal.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.optic.projectofinal.channel.NotificationHelper;
import com.optic.projectofinal.models.Message;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("own", "onMessageReceived: ");
        Map<String,String> data=remoteMessage.getData();

        String title= data.get("title");
        String body= data.get("body");


        if(title!=null){
            if(title.equals("Nuevo Mensaje")){
                shownNotificationMessage(data);
            }else
                shownNotification(title,body);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("own", "onNewToken: "+s);
//        Token token = new Token(s)  ;
//        new TokenProvider().create(s);

    }
    private void shownNotification(String title,String body){
        NotificationHelper notificationHelper=new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder= notificationHelper.getNotificaion(title,body);
        notificationHelper.getManager().notify(new Random().nextInt(10000),builder.build());
    }
    private void shownNotificationMessage(Map<String,String> data){
        int idNotification= Integer.parseInt(data.get("idNotification"));
        String title= data.get("title");
        String body= data.get("body");
        String messagesJSON= data.get("messages");
        Gson gson=new Gson();
        Message[] misMensajes=gson.fromJson(messagesJSON,Message[].class);

        NotificationHelper notificationHelper=new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder= notificationHelper.getNotificaionMessage(misMensajes);
        notificationHelper.getManager().notify(idNotification,builder.build());
    }
}
