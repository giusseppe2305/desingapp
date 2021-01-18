package com.optic.projectofinal.providers;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.utils.Utils;

public class JobsDatabaseProvider {

    private CollectionReference database;
    public JobsDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("Jobs");
    }

    public Query getAll(){
        return database.orderBy("timestamp", Query.Direction.DESCENDING);
    }
    public String getIdDocument(){
        DocumentReference inter= database.document();
        String dev=inter.getId();
        inter.delete();
        return dev;
    }

    public Task<Void> createJob(Job myJob) {
        return database.document(myJob.getId()).set(myJob);
    }

    public Task<DocumentSnapshot> getJobById(String idJobSelected) {
        return database.document(idJobSelected).get();
    }
    public Query getAllJobs(){
        return database.orderBy("timestamp");
    }
    public Task<QuerySnapshot> getAllJobsById(String idUser) {
        return database.whereEqualTo("idUserOffer",idUser).get();
    }
    public Task<QuerySnapshot> getAllJobsById(String idUser,Job.State state) {
        return database.whereEqualTo("idUserOffer",idUser).whereEqualTo("state",state.toString()).get();
    }
    public Task<Void> deleteJob(String id) {
        return database.document(id).delete();
    }

    public Task<Void> updateJob(Job myJob) {
        return database.document(myJob.getId()).update(Utils.convertClassToMap(myJob));
    }

    public Task<QuerySnapshot> getValuationsFromUser(String idUser) {
        return database.whereEqualTo("idUserApply",idUser).whereEqualTo("state",Job.State.FINISHED).get();
    }
}
