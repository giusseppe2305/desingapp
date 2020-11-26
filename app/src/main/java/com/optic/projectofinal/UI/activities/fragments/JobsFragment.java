package com.optic.projectofinal.UI.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.JobsAdapterFirebase;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.JobsDatabaseProvider;

public class JobsFragment extends Fragment {


    private RecyclerView rvJobs;
    private JobsAdapterFirebase mJobsAdapter;
    private JobsDatabaseProvider mJobsProvider;
    public JobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.fragment_jobs, container, false);
        rvJobs=vista.findViewById(R.id.rvJobs);
        LinearLayoutManager linear= new LinearLayoutManager(getContext());
        rvJobs.setLayoutManager(linear);

        mJobsProvider=new JobsDatabaseProvider();
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query=mJobsProvider.getAllJobs();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                System.out.println(task.getResult().getDocuments().size());
            }
        });///comprobar no sea nulo
        if(query!=null){
            FirestoreRecyclerOptions<Job> options= new FirestoreRecyclerOptions.Builder<Job>().setQuery(query, Job.class).build();
            mJobsAdapter = new JobsAdapterFirebase(getContext(),options);
            rvJobs.setAdapter(mJobsAdapter);
            mJobsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mJobsAdapter !=null){
            mJobsAdapter.stopListening();
        }
    }

}