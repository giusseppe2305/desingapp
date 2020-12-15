package com.optic.projectofinal.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.optic.projectofinal.UI.activities.ChatConversationActivity;
import com.optic.projectofinal.channel.NotificationHelper;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;

import java.util.Map;
import java.util.Random;

import static com.optic.projectofinal.channel.NotificationHelper.TYPE_NOTIFICATION.MESSAGE_CHAT;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "own";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("own", "onMessageReceived: ");
        Map<String, String> data = remoteMessage.getData();

//        String title= data.get("title");
//        String body= data.get("body");
        String dataJSON=data.get("data");
        String option=Utils.getOptionNotificationFromJSON(dataJSON);
        Log.d(TAG, "onMessageReceived: "+dataJSON);
        Log.d(TAG, "onMessageReceived: "+option);
        if (option.equals(MESSAGE_CHAT.toString())) {
            shownNotificationMessage(dataJSON);
        }

//        if(title!=null){
//            if(title.equals("Nuevo Mensaje")){
//                shownNotificationMessage(data);
//            }else
//                shownNotification(title,body);
//        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("own", "onNewToken: " + s);
//        Token token = new Token(s)  ;
//        new TokenProvider().create(s);

    }

    private void shownNotification(String title, String body) {
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificaion(title, body);
        notificationHelper.getManager().notify(new Random().nextInt(10000), builder.build());
    }

    private void shownNotificationMessage(String data) {
        NotificationMessageDTO dto=new Gson().fromJson(data,NotificationMessageDTO.class);
        Log.d(TAG, "shownNotificationMessage: "+dto.toString());



        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificaionMessage(dto);

        Intent intentToChat=new Intent(this, ChatConversationActivity.class);
        intentToChat.putExtra("idUserToChat", dto.getIdUserToChat());
        intentToChat.putExtra("idChat",dto.getIdChat());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intentToChat  , PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notificationHelper.getManager().notify(dto.getIdNotification(), builder.build());
    }
}
