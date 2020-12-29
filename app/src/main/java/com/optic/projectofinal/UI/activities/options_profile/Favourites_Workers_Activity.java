package com.optic.projectofinal.UI.activities.options_profile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.WorkersAdapter;
import com.optic.projectofinal.databinding.ActivityFavouritesWorkersActivityBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.ArrayList;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class Favourites_Workers_Activity extends AppCompatActivity {
    
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
        getSupportActionBar().setTitle(R.string.favourites_workers_activity_title);
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
                Tasks.whenAllComplete(tasks).addOnSuccessListener(tasks1 -> {
                    for(Task<?> i: tasks1){
                        Task<DocumentSnapshot> iterated=(Task<DocumentSnapshot>)i;
                        User it = iterated.getResult().toObject(User.class);
                        listWorkersSaved.add(it);
                    }

                    workersAdapter = new WorkersAdapter(Favourites_Workers_Activity.this, listWorkersSaved);
                    binding.favouritesWorkers.setAdapter(workersAdapter);
                    binding.favouritesWorkers.setLayoutManager(new GridLayoutManager(Favourites_Workers_Activity.this,2));
                }).addOnFailureListener(v-> Log.e(TAG_LOG, "getAllSaveWorkersById onSuccess: "+v.getMessage() ));
            }
        }).addOnFailureListener(v-> Log.e(TAG_LOG, "getAllSaveWorkersById onCreate: failure"+v.getMessage() ));

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
        }).addOnFailureListener(runnable -> Log.e(TAG_LOG, "onStart: "+runnable.getMessage() ));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}