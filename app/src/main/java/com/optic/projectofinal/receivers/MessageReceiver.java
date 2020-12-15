package com.optic.projectofinal.receivers;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;
import com.optic.projectofinal.modelsNotification.WrapperNotification;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.NotificationProvider;
import com.optic.projectofinal.providers.TokenProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.UtilsRetrofit;

import java.util.Date;

import static com.optic.projectofinal.channel.NotificationHelper.TYPE_NOTIFICATION.MESSAGE_CHAT;
import static com.optic.projectofinal.services.MyFirebaseMessagingService.NOTIFICATION_REPLY;


public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "own";
    private NotificationMessageDTO dto;

    private TokenProvider mTokenProvider;
    private NotificationProvider mNotificationProvider;
    private AuthenticationProvider mAuth;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String dataJSON = intent.getStringExtra("data");
        Log.d(TAG, "onReceive: entra");
        if (dataJSON != null) {
            Log.d(TAG, "onReceive: "+dataJSON);
            dto = new Gson().fromJson(dataJSON, NotificationMessageDTO.class);
        }

        mAuth = new AuthenticationProvider();
        mTokenProvider = new TokenProvider();
        mNotificationProvider = new NotificationProvider();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(dto.getIdNotification());

        String message = getMessageText(intent).toString();

        sendMessage(message);
    }

    private void sendMessage(String messageText) {
        final Message model = new Message();
        model.setIdChat(dto.getIdChat());
        model.setIdsUserFrom(mAuth.getIdCurrentUser());
        model.setIdUserTo(dto.getIdUserToChat());
        model.setTimestamp(new Date().getTime());
        model.setViewed(false);
        model.setMessage(messageText);
        new MessageProvider().create(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete() && task.isSuccessful()) {

                    new UserDatabaseProvider().getUser(mAuth.getIdCurrentUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO("Nuevo Mensaje", MESSAGE_CHAT, model.getMessage());
                                notificationMessageDTO.setIdChat(model.getIdChat());
                                notificationMessageDTO.setNameUser(documentSnapshot.getString("name") + " " + documentSnapshot.getString("lastName"));
                                notificationMessageDTO.setPhotoProfile(documentSnapshot.getString("profileImage"));
                                notificationMessageDTO.setIdUserToChat(dto.getIdUserToChat());
                                notificationMessageDTO.setMessages(new Message[]{model});


                                String code = model.getIdsUserFrom().substring(model.getIdsUserFrom().length() - 3);
                                notificationMessageDTO.setIdNotification(UtilsRetrofit.stringToInt(code));
                                WrapperNotification<NotificationMessageDTO> wrapperNotification = new WrapperNotification<>(notificationMessageDTO);

                                ///first we update seenmesages

                                UtilsRetrofit.sendNotificationMessage(context, wrapperNotification, true);
                                //
                            }
                        }
                    });

                } else {
                    Log.e(TAG, "onComplete error messagereceiver not complete");
                }
            }
        });


    }


    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NOTIFICATION_REPLY);
        }
        return null;
    }
}
