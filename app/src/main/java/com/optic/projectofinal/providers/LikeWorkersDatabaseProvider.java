package com.optic.projectofinal.providers;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.models.LikeWorker;

public class LikeWorkersDatabaseProvider {

    private CollectionReference database;

    public LikeWorkersDatabaseProvider(String idUser) {
        database = FirebaseFirestore.getInstance().collection("Users").document(idUser).collection("WorkersSaved");
    }

    public void doLike(String idWorker){
        chekIfExistLikeToWorker(idWorker).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    String idDocumentDelete=queryDocumentSnapshots.getDocuments().get(0).getId();
                    database.document(idDocumentDelete).delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("error","fallo al borrar like");

                        }
                    });
                }else{
                    LikeWorker it= new LikeWorker(idWorker);
                    database.document().set(it);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error","fallo al comprobar si exsite like");
            }
        });

    }
    public Task<QuerySnapshot> chekIfExistLikeToWorker(String idWorker){
        return database.whereEqualTo("idWorker",idWorker).get();

    }
}
