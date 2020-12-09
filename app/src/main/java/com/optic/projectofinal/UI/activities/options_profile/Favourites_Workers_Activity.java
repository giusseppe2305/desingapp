package com.optic.projectofinal.UI.activities.options_profile;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.adapters.WorkersAdapter;
import com.optic.projectofinal.databinding.ActivityFavouritesWorkersActivityBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

public class Favourites_Workers_Activity extends AppCompatActivity {
    private static final String TAG = "own";
    private ActivityFavouritesWorkersActivityBinding binding;
    private UserDatabaseProvider mUserProvider;
    private AuthenticationProvider mAuth;
    private WorkersAdapter workersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFavouritesWorkersActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle("Favoritos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();

        mUserProvider.getAllSaveWorkersById(mAuth.getIdCurrentUser()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshot) {
                ArrayList<User> listWorkersSaved=new ArrayList<>();
                ArrayList<Task<DocumentSnapshot>> tasks=new ArrayList<Task<DocumentSnapshot>>();
                for(DocumentSnapshot it:documentSnapshot.getDocuments()){
                    tasks.add(mUserProvider.getUser(it.getString("idWorker")));
                }
                Tasks.whenAllComplete(tasks).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                    @Override
                    public void onSuccess(List<Task<?>> tasks) {
                        for(Task<?> i:tasks){
                            Task<DocumentSnapshot> iterated=(Task<DocumentSnapshot>)i;
                            listWorkersSaved.add(iterated.getResult().toObject(User.class));
                        }
                        workersAdapter = new WorkersAdapter(Favourites_Workers_Activity.this, listWorkersSaved);
                        binding.favouritesWorkers.setAdapter(workersAdapter);
                        binding.favouritesWorkers.setLayoutManager(new GridLayoutManager(Favourites_Workers_Activity.this,2));
                    }
                }).addOnFailureListener(v-> Log.e(TAG, "getAllSaveWorkersById onSuccess: "+v.getMessage() ));
            }
        }).addOnFailureListener(v-> Log.e(TAG, "getAllSaveWorkersById onCreate: failure"+v.getMessage() ));

        mUserProvider.getAllWorkers().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<User> lisWorkers=new ArrayList<>();
                for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                    if(!i.getString("id").equals(mAuth.getIdCurrentUser())){
                        lisWorkers.add(i.toObject(User.class));
                    }
                }

            }
        }).addOnFailureListener(runnable -> Log.e(TAG, "onStart: "+runnable.getMessage() ));
    }
}