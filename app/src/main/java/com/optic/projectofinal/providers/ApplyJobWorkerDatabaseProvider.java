package com.optic.projectofinal.providers;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ApplyJobWorkerDatabaseProvider {

    private CollectionReference database;
    public ApplyJobWorkerDatabaseProvider(String idJob) {
        database = FirebaseFirestore.getInstance().collection("Jobs").document(idJob).collection("ApplyWorkers");
    }

    public Query getAll(){
        return database.orderBy("price");
    }

}
