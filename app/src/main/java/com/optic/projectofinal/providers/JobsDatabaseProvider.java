package com.optic.projectofinal.providers;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.models.Job;

import java.util.Map;

public class JobsDatabaseProvider {

    private CollectionReference database;
    public JobsDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("Jobs");
    }

    public Query getAllJobs(){
        return database.orderBy("title");
    }
    public String getIdDocument(){
        String dev=null;
        DocumentReference inter= database.document();
        dev=inter.getId();
        inter.delete();
        return dev;
    }

    public Task<Void> createJob(Job myJob) {
        return database.document(myJob.getId()).set(myJob);
    }

    public Task<DocumentSnapshot> getJobById(String idJobSelected) {
        return database.document(idJobSelected).get();
    }

    public Task<QuerySnapshot> getAllJobsById(String idUser) {
        return database.whereEqualTo("idUserOffer",idUser).get();
    }

    public Task<Void> deleteJob(String id) {
        return database.document(id).delete();
    }

    public Task<Void> updateJob(Job myJob) {
        Map<String, Object> update = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).convertValue(myJob, Map.class);
        return database.document(myJob.getId()).update(update);
    }

}
