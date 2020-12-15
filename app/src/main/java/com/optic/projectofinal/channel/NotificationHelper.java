package com.optic.projectofinal.channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.optic.projectofinal.R;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;
import com.optic.projectofinal.utils.Utils;

public class NotificationHelper extends ContextWrapper {
    private static final String CHANNEL_ID="com.optic.projectofinal";
    private static final String CHANNEL_NAME="SocialMedia";
    private static final String TAG = "own";
    private  int idNotification;
    public enum TYPE_NOTIFICATION {MESSAGE_CHAT};
    private NotificationManager manager;

    public NotificationHelper(Context context) {
        super(context);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            //solo es obiligatorio crea el canal en versiones superiorres o iguales a android 8
            createChannerls();
        }
    }

    public NotificationHelper(Context baseContext, int idNotification) {
        super(baseContext);
        this.idNotification = idNotification;
    }

    //obligario en versines superiores a android 8
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannerls(){
        NotificationChannel notificationChannel=new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager(){
        if (manager == null) {
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    /////versiones anteriorires
    public NotificationCompat.Builder getNotificaion(String title, String body){
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setColor(Color.GRAY)
                .setSmallIcon(R.drawable.ic_about)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }
    
    public NotificationCompat.Builder getNotificaionMessage(NotificationMessageDTO messages){
        Person person1= new Person.Builder()
                .setName("Andres")
                .setIcon(IconCompat.createWithResource(getApplicationContext(),R.mipmap.ic_launcher))
                .build();
        Log.d(TAG, "getNotificaionMessage: "+messages.getPhotoProfile());
        Uri uriPhoto= Uri.parse(messages.getPhotoProfile());
        Log.d(TAG, "getNotificaionMessage: "+uriPhoto);

        Person person2= new Person.Builder()
                .setName(messages.getNameUser())
                .setIcon(IconCompat.createWithBitmap(Utils.getBitmapFromURL(messages.getPhotoProfile())))
                .build();

        NotificationCompat.MessagingStyle messagingStyle= new NotificationCompat.MessagingStyle(person1);
//        NotificationCompat.MessagingStyle.Message message1= new NotificationCompat.MessagingStyle.Message(
//                "Ultimo mensaje",
//                new Date().getTime(),
//                person1
//        );
//        messagingStyle.addMessage(message1);
        for(Message m:messages.getMessages()){
            NotificationCompat.MessagingStyle.Message messageI= new NotificationCompat.MessagingStyle.Message(
                    m.getMessage(),
                    m.getTimestamp(),
                    person2
            );
            messagingStyle.addMessage(messageI);
        }
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_about)
                .setStyle(messagingStyle);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Notification getActiveNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] barNotifications = notificationManager.getActiveNotifications();
        for(StatusBarNotification notification: barNotifications) {
            if (notification.getId() == notificationId) {
                return notification.getNotification();
            }
        }
        return null;
    }
}
