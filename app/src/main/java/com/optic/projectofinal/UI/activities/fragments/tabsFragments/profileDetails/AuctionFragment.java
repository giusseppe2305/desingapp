package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.AuctionsAdapter;
import com.optic.projectofinal.databinding.FragmentTabAuctionsBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.JobsDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuctionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuctionFragment extends Fragment {


    private static final String TAG = "own";
    private  String idUser;
    private FragmentTabAuctionsBinding binding;
    public AuctionFragment() {
        // Required empty public constructor
    }

    public AuctionFragment(String idUser) {
        this.idUser=idUser;
    }


    public static AuctionFragment newInstance(String idUser) {
        AuctionFragment fragment = new AuctionFragment(idUser);

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
        binding =FragmentTabAuctionsBinding.inflate(inflater, container, false);
        binding.spinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,new String[]{"Fecha (Ascedente)","Fecha (Descendiente)","Precio (Ascendente)"}));
        
        loadAuctions();
        return binding.getRoot();
    }

    private void loadAuctions() {
        new JobsDatabaseProvider().getAllJobsById(idUser).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                ArrayList<Job> listJobs=new ArrayList<>();
                for(DocumentSnapshot i:list){
                    listJobs.add(i.toObject(Job.class));
                }
                binding.listAuctions.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.listAuctions.setAdapter(new AuctionsAdapter(getContext(),listJobs));
            }
        }).addOnFailureListener(v-> Log.e(TAG, "loadAuctions: "+v.getMessage() ));
    }

}