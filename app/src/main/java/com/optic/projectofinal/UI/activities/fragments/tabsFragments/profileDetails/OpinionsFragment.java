package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.OpinionsAdapter;
import com.optic.projectofinal.databinding.FragmentTabOpinionsBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.Opinion;
import com.optic.projectofinal.providers.JobsDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpinionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpinionsFragment extends Fragment {
    
    private String idUser;
    private FragmentTabOpinionsBinding binding;
    public OpinionsFragment(String idUser) {
        this.idUser = idUser;
    }

    public OpinionsFragment() {
        // Required empty public constructor
    }


    public static OpinionsFragment newInstance(String idUser) {
        OpinionsFragment fragment = new OpinionsFragment(idUser);

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
        binding=FragmentTabOpinionsBinding.inflate(inflater,container,false);
        binding.spinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,new String[]{"Todos ","Cerrajero","Fontanero","Jardinero"}));
        loadOpinions();

        return binding.getRoot();
    }

    private void loadOpinions() {
        new JobsDatabaseProvider().getValuationsFromUser(idUser).addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            ArrayList<Opinion> listOpinions=new ArrayList<>();
            for(DocumentSnapshot i:list){
                Job it=i.toObject(Job.class);

                Opinion opinion=new Opinion();
                opinion.setIdJob(it.getId());
                opinion.setValuationWorker(it.getValuation());
                opinion.setTitleJob(it.getTitle());
                opinion.setTimestamp(it.getTimestamp());
                opinion.setImageJob(it.getImages().get(0));
                if(it.getOpinionUserOffer()!=null)
                opinion.setMessage(it.getOpinionUserOffer());

                listOpinions.add(opinion);

            }

            binding.listOpinions.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listOpinions.setAdapter(new OpinionsAdapter(getContext(),listOpinions));
        }).addOnFailureListener(v-> Log.e(TAG_LOG, "loadOpinions: "+v.getMessage() ));

//        new UserDatabaseProvider().getOpinions(idUser).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                ArrayList<Opinion> listOpinions=new ArrayList<>();
//                for(DocumentSnapshot i:list){
//                    Opinion it=i.toObject(Opinion.class);
//                    listOpinions.add(it);
//                }
//                binding.listOpinions.setLayoutManager(new LinearLayoutManager(getContext()));
//                binding.listOpinions.setAdapter(new OpinionsAdapter(getContext(),listOpinions));
//            }
//        }).addOnFailureListener(v-> Log.e(TAG_LOG, "loadOpinions: "+v.getMessage() ));
    }
}