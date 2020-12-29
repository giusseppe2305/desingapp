package com.optic.projectofinal.UI.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.UI.activities.CreateEditJobActivity;
import com.optic.projectofinal.adapters.JobsAdapter;
import com.optic.projectofinal.databinding.FragmentJobsBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.utils.ItemDecoration;
import com.todou.nestrefresh.base.OnRefreshListener;

import java.util.ArrayList;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class JobsFragment extends Fragment {


    private FragmentJobsBinding binding;
    private JobsAdapter jobsAdapter;
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
        binding = FragmentJobsBinding.inflate(inflater, container, false);



        mJobsProvider = new JobsDatabaseProvider();

        binding.btnCreateJob.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateEditJobActivity.class));
        });

        LinearLayoutManager linear = new LinearLayoutManager(getContext());
        binding.rvJobs.setLayoutManager(linear);
        binding.rvJobs.addItemDecoration(new ItemDecoration(getContext(),RecyclerView.VERTICAL,10));
//        jobsAdapter=new JobsAdapter(getContext(),listJobs );
//        binding.rvJobs.setAdapter(jobsAdapter);

        binding.viewRefreshHeader.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.viewRefreshHeader.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },2000);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mJobsProvider.getAll().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Job> listJobs=new ArrayList<Job>();
                for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments())
                {
                    if(i.getString("state").equals(Job.State.PUBLISHED.toString())&&!i.getString("idUserOffer").equals(new AuthenticationProvider().getIdCurrentUser()))
                    listJobs.add(i.toObject(Job.class));
                }

                jobsAdapter=new JobsAdapter(getContext(),listJobs );
                binding.rvJobs.setAdapter(jobsAdapter);
            }
        }).addOnFailureListener(v-> Log.e(TAG_LOG, "onStart: "+v.getMessage() ));
    }
}