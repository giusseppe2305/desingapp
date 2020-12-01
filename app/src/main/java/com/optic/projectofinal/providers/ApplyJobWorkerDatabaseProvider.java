package com.optic.projectofinal.providers;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.projectofinal.models.ApplyJob;

public class ApplyJobWorkerDatabaseProvider {
    private AuthenticationProvider authenticationProvider;
    private CollectionReference database;
    public ApplyJobWorkerDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("ApplyWorkers");
        authenticationProvider=new AuthenticationProvider();

    }
    public Query getAllById(String job){
        return database.whereEqualTo("idJob",job);
    }

    public Task<Void> addApply(ApplyJob applyJob,String idJob) {
        return database.document(idJob+authenticationProvider.getIdCurrentUser()).set(applyJob);
    }

    public Task<DocumentSnapshot> checkIfExist(String s) {
        return database.document(s).get();
    }

    public Task<Void> removeApplyJob(String idJob, String idUser) {
        return database.document(idJob+idUser).delete();
    }
}
