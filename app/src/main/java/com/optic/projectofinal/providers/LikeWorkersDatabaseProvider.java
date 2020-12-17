package com.optic.projectofinal.providers;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.models.LikeWorker;
import com.optic.projectofinal.utils.Utils;

public class LikeWorkersDatabaseProvider {

    private CollectionReference database;

    public LikeWorkersDatabaseProvider(String idUser) {
        database = FirebaseFirestore.getInstance().collection("Users").document(idUser).collection("WorkersSaved");
    }

    public void doLike(String idWorker){
        chekIfExistLikeToWorker(idWorker).addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.getDocuments().size()>0){
                String idDocumentDelete=queryDocumentSnapshots.getDocuments().get(0).getId();
                database.document(idDocumentDelete).delete().addOnFailureListener(e -> Log.e(Utils.TAG_LOG,
                        "fail to delete like"));
            }else{
                LikeWorker it= new LikeWorker(idWorker);
                database.document().set(it);
            }
        }).addOnFailureListener(e -> Log.e(Utils.TAG_LOG,"fail to check if exist like "+e.getMessage()));

    }
    public Task<QuerySnapshot> chekIfExistLikeToWorker(String idWorker){
        return database.whereEqualTo("idWorker",idWorker).get();

    }
}
