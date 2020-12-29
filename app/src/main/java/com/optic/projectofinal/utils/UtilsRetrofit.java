package com.optic.projectofinal.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;
import com.optic.projectofinal.modelsNotification.WrapperNotification;
import com.optic.projectofinal.modelsRetrofit.FCMBody;
import com.optic.projectofinal.modelsRetrofit.FCMResponse;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.NotificationProvider;
import com.optic.projectofinal.providers.TokenProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class UtilsRetrofit {

    public static int stringToInt(String path) {
        StringBuilder dev = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            dev.append((int) path.charAt(i));
        }

        return Integer.parseInt(dev.toString());

    }
    public static void sendNotificationMessage( WrapperNotification<NotificationMessageDTO> wrapperNotification,boolean isFromNotificationBar) {

        new TokenProvider().getToken(wrapperNotification.getData().getIdUserToChat()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                final String tokenUserOwnPost = documentSnapshot.getString("token");
                if (tokenUserOwnPost != null) {
                    wrapperNotification.setTo(tokenUserOwnPost);
                    if(isFromNotificationBar){


                        ///we change idusertochat in order to
                        wrapperNotification.getData().setIdUserToChat(new AuthenticationProvider().getIdCurrentUser());
                        sendNotificationInside(wrapperNotification);
                    }else
                    getLastThreeMessages(wrapperNotification);

                } else {
                    Log.e(TAG_LOG, "the tokens user not exist" );
                }
            }
        }).addOnFailureListener(e-> Log.e(TAG_LOG, "failure to get token value "+e.getMessage() ));

    }
    private static void getLastThreeMessages( final WrapperNotification<NotificationMessageDTO> wrapper) {

        MessageProvider mMessageProvider=new MessageProvider();
        mMessageProvider.getLastThreeMessagesByChat(wrapper.getData().getIdChat()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()){
                ArrayList<Message> messagesArrayList=new ArrayList<>();
                for(int i=queryDocumentSnapshots.getDocuments().size()-1;i>=0;i--){
                    messagesArrayList.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Message.class));
                }


                wrapper.getData().setMessages(messagesArrayList.toArray(new Message[]{}));
                ///we change idusertochat in order to when the user receive object has the correct id
                wrapper.getData().setIdUserToChat(new AuthenticationProvider().getIdCurrentUser());
                sendNotificationInside(wrapper);
            }else{
                Log.e(TAG_LOG, "onSuccess: getLastThreeMessages queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()" );
            }
        }).addOnFailureListener(e -> Log.e(TAG_LOG, "ailure at get three last messages: "+e.getMessage() ));
    }
    private static void sendNotificationInside(WrapperNotification wrapper ) {


        final NotificationProvider mNotificationProvider = new NotificationProvider();
        Map<String, String> data = new HashMap<>();
        String dataJSON=new Gson().toJson(wrapper.getData());
        data.put("data",dataJSON);
        FCMBody body = new FCMBody(wrapper.getTo(), wrapper.getPriority(), "4500s", data);
        mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(@NotNull Call<FCMResponse> call, @NotNull Response<FCMResponse> response) {
                if (response.body() != null) {
                    System.out.println(response.body().getSuccess());
                    if (response.body().getSuccess() == 1) {
                        Log.d(TAG_LOG, "the notification was send successfully");
                    } else {
                        Log.e(TAG_LOG, "1 the notification was send failurelly, receive body was send but malformed"+body.toString());


                    }
                } else {
                    Log.e(TAG_LOG, " 2  the notification was send failurelly not body " + response.message() + " - " + response.errorBody() );
                }
            }

            @Override
            public void onFailure(@NotNull Call<FCMResponse> call, Throwable t) {
                Log.e(TAG_LOG, " 3  the notification was send failurelly not was send " + t.getMessage() );

            }
        });
    }
}
