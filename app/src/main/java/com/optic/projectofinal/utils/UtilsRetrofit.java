package com.optic.projectofinal.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.optic.projectofinal.models.FCMBody;
import com.optic.projectofinal.models.FCMResponse;
import com.optic.projectofinal.models.Message;
import com.optic.projectofinal.modelsNotification.NotificationMessageDTO;
import com.optic.projectofinal.modelsNotification.WrapperNotification;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.MessageProvider;
import com.optic.projectofinal.providers.NotificationProvider;
import com.optic.projectofinal.providers.TokenProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilsRetrofit {
    private static final String TAG ="own" ;
    public static int stringToInt(String path) {
        String dev = "";
        for (int i = 0; i < path.length(); i++) {
            dev += (int) path.charAt(i);
        }

        return Integer.parseInt(dev);

    }
    public static void sendNotificationMessage(final Context context, WrapperNotification<NotificationMessageDTO> wrapperNotification,boolean isFromNotficationBar) {

        new TokenProvider().getToken(wrapperNotification.getData().getIdUserToChat()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    final String tokenUserOwnPost = documentSnapshot.getString("token");
                    if (tokenUserOwnPost != null) {
                        wrapperNotification.setTo(tokenUserOwnPost);
                        if(isFromNotficationBar){


                            ///we change idusertochat in order to
                            wrapperNotification.getData().setIdUserToChat(new AuthenticationProvider().getIdCurrentUser());
                            sendNotificationInside(context,wrapperNotification);
                        }else
                        getLastThreeMessages(context,wrapperNotification);

                    } else {
                        Toast.makeText(context, "El token del usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fallo el gete token", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private static void getLastThreeMessages(final Context context, final WrapperNotification<NotificationMessageDTO> wrapper) {
        //NotificationDTO dto=new NotificationDTO("Nuevo Mensaje", NotificationHelper.TYPE_NOTIFICATION.MESSAGE_CHAT.toString(),model.getMessage()  );

        MessageProvider mMessageProvider=new MessageProvider();
        Log.d(TAG, "getLastThreeMessages: "+wrapper.getData().getIdChat());
        mMessageProvider.getLastThreeMessagesByChat(wrapper.getData().getIdChat()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()){
                    ArrayList<Message> messagesArrayList=new ArrayList<>();
                    for(int i=queryDocumentSnapshots.getDocuments().size()-1;i>=0;i--){
                        messagesArrayList.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Message.class));
                    }

                    //verificacion si es 0 EL ARRAY NO LA PONGO

//                    messagesArrayList.add(model);
                    wrapper.getData().setMessages(messagesArrayList.toArray(new Message[]{}));
                    ///we change idusertochat in order to
                    wrapper.getData().setIdUserToChat(new AuthenticationProvider().getIdCurrentUser());
                    sendNotificationInside(context,wrapper);
                }else{
                    Log.e(TAG, "onSuccess: fail" );
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage() );
                Toast.makeText(context, "Fallo al recoger ultimos tres mensajes y notifiaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static void sendNotificationInside(final Context context,WrapperNotification wrapper ) {


        final NotificationProvider mNotificationProvider = new NotificationProvider();
        Map<String, String> data = new HashMap<>();
        String dataJSON=new Gson().toJson(wrapper.getData());
        data.put("data",dataJSON);
        Log.d(TAG, "sendNotificationInside: "+dataJSON);
        FCMBody body = new FCMBody(wrapper.getTo(), wrapper.getPriority(), "4500s", data);
        mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body() != null) {
                    System.out.println(response.body().getSuccess());
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(context, "La Notificacion se envio correctamente", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.d(TAG, "onResponse: "+"1 Fallo al enviar notificaciones " + response.message() + " - " +response.toString()+" "+response.errorBody().toString());
                        Log.d(TAG, "onResponse: "+body.toString());
                        Toast.makeText(context, "1 Fallo al enviar notificaciones " + response.message() + " - " + call.request().method(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, " 2 Fallo al enviar notificaciones " + response.message() + " - " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });
    }
}
