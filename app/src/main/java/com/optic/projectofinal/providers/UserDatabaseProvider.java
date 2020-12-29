package com.optic.projectofinal.providers;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.models.Opinion;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.utils.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class UserDatabaseProvider {

    
    private CollectionReference database;
    private AuthenticationProvider mAuth;
    public UserDatabaseProvider() {

        database = FirebaseFirestore.getInstance().collection("Users");
        mAuth=new AuthenticationProvider();
    }
    public CollectionReference getCollection(){
        return database;
    }

    public Task<Void> createUser(User miUser) {
        return database.document(miUser.getId()).set(miUser);
    }

    public Task<DocumentSnapshot> getUser(String idUser){
        return database.document(idUser).get();
    }
    public Task<QuerySnapshot> getOpinions(String idUser){
        return database.document(idUser).collection("Opinions").get();
    }
    public Task<Void> updateUser( User user){
        //delete null fields
        return database.document(user.getId()).update(Utils.convertClassToMap(user));
    }

    public void updateOnline(boolean status){
        Map<String, Object> update = new HashMap<>();
        update.put("online",status);
        update.put("lastConnection",new Date().getTime());

         database.document(mAuth.getIdCurrentUser()).update(update)
                 .addOnFailureListener(error-> Log.e(TAG_LOG, "fail to update status user connected disconnected "+error.getMessage()));
    }

    public DocumentReference getIsOnlineUser(String idCurrentUser) {
        return database.document(idCurrentUser);
    }

    public Query getAllWorkers(){
        return database.whereEqualTo("professional",true);
    }

    public Task<QuerySnapshot> getUsersBySubcategory(String sub) {
        return database.whereArrayContains("idsSubCategories",sub).get();
    }


    public Task<Void> putOpinion(String idUser, Opinion opinion) {
        return database.document(idUser).collection("Opinions").document().set(opinion);
    }

    public Task<QuerySnapshot> getAllSaveWorkersById(String id)
    {
        return database.document(id).collection("WorkersSaved").get();
    }




}
