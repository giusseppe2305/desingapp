package com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.Swipe.SwipeRVAdapter;
import com.optic.projectofinal.Swipe.SwipeRVTouchHelper;
import com.optic.projectofinal.UI.activities.CreateJobActivity;
import com.optic.projectofinal.adapters.JobsAdapterSettings;
import com.optic.projectofinal.databinding.FragmentMyAuctionsBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAuctionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAuctionsFragment extends Fragment implements SwipeRVTouchHelper.SwipeRVTouchHelperListener {

    private static final int EDIT_JOB = 1;
    private SwipeRVAdapter adapterSwipe;
    private FragmentMyAuctionsBinding binding;
    private ArrayList<Job> listJobsPublished;
    private ArrayList<Job> listJobsInProgress;
    private ArrayList<Job> listJobsFinished;

    public MyAuctionsFragment() {
        // Required empty public constructor
    }


    public static MyAuctionsFragment newInstance(String param1, String param2) {
        MyAuctionsFragment fragment = new MyAuctionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAuctionsBinding.inflate(inflater, container, false);
        loadData();
        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        binding.listJobsPublished.setVisibility(View.VISIBLE);
                        binding.listJobsFinished.setVisibility(View.GONE);
                        binding.listJobsInProgress.setVisibility(View.GONE);
                        break;
                    case 1:
                        binding.listJobsPublished.setVisibility(View.GONE);
                        binding.listJobsFinished.setVisibility(View.GONE);
                        binding.listJobsInProgress.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        binding.listJobsPublished.setVisibility(View.GONE);
                        binding.listJobsFinished.setVisibility(View.VISIBLE);
                        binding.listJobsInProgress.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return binding.getRoot();
    }

    public void loadData() {
        new JobsDatabaseProvider().getAllJobsById(new AuthenticationProvider().getIdCurrentUser()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                listJobsPublished = new ArrayList<Job>();
                listJobsInProgress = new ArrayList<Job>();
                listJobsFinished = new ArrayList<Job>();
                for (DocumentSnapshot i : list) {
                    Job it = i.toObject(Job.class);
                    switch (it.getState()) {
                        case FINISHED:
                            listJobsFinished.add(it);
                            break;
                        case PUBLISHED:
                            listJobsPublished.add(it);
                            break;
                        case IN_PROGRESS:
                            listJobsInProgress.add(it);
                            break;
                    }
                }
                if (list.size() > 0) {
                    adapterSwipe = new SwipeRVAdapter(listJobsPublished, MyAuctionsFragment.this, Job.class);
                    setupRecyclerView();
                    binding.containerExample.setVisibility(View.GONE);

                    binding.listJobsInProgress.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listJobsInProgress.setAdapter(new JobsAdapterSettings(MyAuctionsFragment.this, listJobsInProgress, Job.State.IN_PROGRESS));

                    binding.listJobsFinished.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listJobsFinished.setAdapter(new JobsAdapterSettings(MyAuctionsFragment.this, listJobsFinished, Job.State.FINISHED));

                } else {
                    binding.listJobsPublished.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(v -> Log.e(TAG_LOG, "MyAuctionsFragment loadData: " + v.getMessage()));
    }

    private void setupRecyclerView() {
        binding.listJobsPublished.setNestedScrollingEnabled(false);
        binding.listJobsPublished.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listJobsPublished.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback simpleCallback = new SwipeRVTouchHelper(MyAuctionsFragment.this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.listJobsPublished);
        binding.listJobsPublished.setAdapter(adapterSwipe);
    }

    @Override
    public void onSwiped(int direction, int position) {
        adapterSwipe.notifyDataSetChanged();
        // Temporary store the swiped off item
        final Job job = listJobsPublished.get(position);
        //Remove the item
        //adapterSkills.removeSwipeItem(position);
        // If swipe left - delete the item
        if (direction == ItemTouchHelper.LEFT) {

            String message =String.format(getString(R.string.my_auctions_fragment_alert_message)+": %d ?",job.getTitle());
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(R.string.my_auctions_fragment_alert_title)
                    .setMessage(message)
                    .setNegativeButton(R.string.my_auctions_fragment_alert_negative_button, null)
                    .setPositiveButton(R.string.my_auctions_fragment_alert_positive_button, (dialogInterface, i) -> {
                        adapterSwipe.removeSwipeItem(position);
                        removeJob(job.getId());
                    })
                    .show();
        } else if (direction == ItemTouchHelper.RIGHT) {
            Intent i = new Intent(getContext(), CreateJobActivity.class);
            i.putExtra("idJob", job.getId());
            startActivityForResult(i, EDIT_JOB);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_JOB) {
            if (resultCode == Activity.RESULT_OK) {
                //that means that we have to refresh data
                Log.d(TAG_LOG, "we have to refresh data ");
                loadData();
            }
        }
    }

    private void removeJob(String id) {
        new JobsDatabaseProvider().deleteJob(id).addOnFailureListener(v ->
                Log.e(TAG_LOG, "MyAuctionsFragment removeJob: " + v.getMessage()));
    }
}