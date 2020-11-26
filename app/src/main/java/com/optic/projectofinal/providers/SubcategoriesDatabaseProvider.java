package com.optic.projectofinal.providers;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SubcategoriesDatabaseProvider {

    private CollectionReference database;
    public SubcategoriesDatabaseProvider() {
        database = FirebaseFirestore.getInstance().collection("Subcategories");
    }

    public Task<QuerySnapshot> getAllByCategory(String idCategory){
        return database.whereEqualTo("idCategory",idCategory).get();
    }

}
