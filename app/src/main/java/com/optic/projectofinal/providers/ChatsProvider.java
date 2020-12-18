package com.optic.projectofinal.providers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.projectofinal.models.Chat;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsProvider {
    private CollectionReference mCollection;
    public ChatsProvider(){
        mCollection= FirebaseFirestore.getInstance().collection("Chats");
    }
    public void create(Chat mChat)
    {
        mCollection.document(mChat.getIdUserFrom()+mChat.getIdUserTo() ).set(mChat);
    }

    public Query getAllChatsFromUser(String idCurrentUser) {
        return mCollection.whereArrayContains("idsChats",idCurrentUser).orderBy("timestamp", Query.Direction.DESCENDING);
    }
    public Query getChatFromUserToAndUserFrom(String userTo, String userFrom){
        //check if both combination are an id of chat
        ArrayList<String> ids= new ArrayList<>();
        ids.add(userFrom+userTo);
        ids.add(userTo+userFrom);

        return mCollection.whereIn("idChat",ids);
    }
    public DocumentReference getChatById(String id){
        return mCollection.document(id);
    }
    public void updateIsWritting(String idChat, String idUser, boolean value){
        final Map<String, Object> update = new HashMap<>();
        update.put("isTyping", value?FieldValue.arrayUnion(idUser):FieldValue.arrayRemove(idUser));

        mCollection.document(idChat).update(update).addOnFailureListener(e-> Log.e(Utils.TAG_LOG, "updateIsWritting: fail to update typing "+e.getMessage() ));
    }
    public void updateLastMessageOnChat(final String idCurrentChat, final String idMessage, final Context c) {
        Map<String, Object> update=new HashMap<>();
        update.put("idLastMessage",idMessage);
        mCollection.document(idCurrentChat).update(update).addOnFailureListener(e ->
                Log.e(Utils.TAG_LOG, "fail to update id last message on chat"+e.getMessage() ));
    }

    public Task<Void> deleteChat(String id) {
        return mCollection.document(id).delete();
    }
}
