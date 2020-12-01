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
import com.optic.projectofinal.models.Opinion;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseProvider {

    private static final String TAG = "own";
    private CollectionReference database;

    public UserDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("Users");
    }
    public CollectionReference getCollection(){
        return database;
    }

    public Task<Void> createUser(User miUser) {
        return database.document(miUser.getId()).set(miUser);
    }
//    public Task<Void> createUser2(User miUser) {
//        DocumentReference inter = database.document();
//        miUser.setId(inter.getId());
//        return inter.set(miUser);
//    }
    public Task<DocumentSnapshot> getUser(String idUser){
        return database.document(idUser).get();
    }
    public Task<QuerySnapshot> getOpinions(String idUser){
        return database.document(idUser).collection("Opinions").get();
    }
    public Task<Void> updateUser(String idUser, User user){
        //delete null fields
        Map<String, Object> update = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).convertValue(user, Map.class);
        return database.document(idUser).update(update);
    }

    public Task<Void> updateUserOnline(String idUser, boolean status){
        Map<String, Object> update = new HashMap<>();
        update.put("online",status);
        update.put("lastConnection",new Date().getTime());

        return database.document(idUser).update(update);
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

    public Query filterWorkers(int category,SubCategory subCategory, String priceSince, String priceUntil, Order order){
        Query query = database.whereEqualTo("professional", true).whereArrayContains("idsCategories",category);
        if(subCategory!=null){
            query.whereArrayContains("idsSubCategories",subCategory.getId());
        }
        if(priceSince!=null&&priceUntil!=null){
            query.whereGreaterThanOrEqualTo("pricePerHour",priceSince).whereLessThanOrEqualTo("pricePerHour",priceUntil);
        }
        if(order!=null){
            switch (order){
                case HIGHER_TO_LOWER:query.orderBy("pricePerHour", Query.Direction.DESCENDING);
                    break;
                case DISTANCE:
                    break;
                case LOWER_TO_HIGHER:query.orderBy("pricePerHour", Query.Direction.ASCENDING);
                    break;
            }
        }
        return query;
    }

    public Task<Void> putOpinion(String idUser, Opinion opinion) {
        return database.document(idUser).collection("Opinions").document().set(opinion);
    }



    public enum Order{
        LOWER_TO_HIGHER,
        HIGHER_TO_LOWER,
        DISTANCE
    }
}
