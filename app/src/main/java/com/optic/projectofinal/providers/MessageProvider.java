package com.optic.projectofinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.projectofinal.models.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageProvider {
    private CollectionReference mcollection;
    private String idChat;

    public MessageProvider() {
        mcollection= FirebaseFirestore.getInstance().collection("Messages");
        this.idChat=idChat;
    }
    public Task<Void> create(Message msg){
        DocumentReference document= mcollection.document();
        msg.setId(document.getId());
        return document.set(msg);
    }

    public Query getMessageByChat(String idChat) {
        return mcollection.whereEqualTo("idChat",idChat).orderBy("timestamp", Query.Direction.ASCENDING);
    }
    public Query getMessageByChatAndSender(String idChat, String idUserFrom) {
        return mcollection.whereEqualTo("idChat",idChat).whereEqualTo("idsUserFrom",idUserFrom).whereEqualTo("viewed",false);
    }
    public Query getLastThreeMessagesByChat(String idChat) {
        return mcollection.whereEqualTo("idChat",idChat).whereEqualTo("viewed",false).orderBy("timestamp", Query.Direction.ASCENDING).limit(3);
    }
    public void updateViewd(String idDocument){
       Map<String, Object> valor= new HashMap<>();
       valor.put("viewed",true);
       mcollection.document(idDocument).update(valor);
    }
    public DocumentReference getMessage(String idMessage){
        return mcollection.document(idMessage);
    }


}
