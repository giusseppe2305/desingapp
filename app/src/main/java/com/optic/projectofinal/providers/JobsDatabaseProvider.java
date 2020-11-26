package com.optic.projectofinal.providers;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class JobsDatabaseProvider {

    private CollectionReference database;
    public JobsDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("Jobs");
    }

    public Query getAllJobs(){
        return database.orderBy("title");
    }

}
