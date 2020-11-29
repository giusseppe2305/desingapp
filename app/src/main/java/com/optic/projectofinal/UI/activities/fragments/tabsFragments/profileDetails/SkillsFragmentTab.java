package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.adapters.SkillsAdapter;
import com.optic.projectofinal.databinding.FragmentTabSkillsBinding;
import com.optic.projectofinal.models.Skill;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillsFragmentTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillsFragmentTab extends Fragment {

    private static final String TAG = "own";
    private  String idUser;
    private FragmentTabSkillsBinding binding;

    public SkillsFragmentTab() {
        // Required empty public constructor
    }

    public SkillsFragmentTab(String idUser) {
        this.idUser=idUser;
    }

    public static SkillsFragmentTab newInstance(String idUser) {
        SkillsFragmentTab fragment = new SkillsFragmentTab(idUser);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this ragment
        binding=FragmentTabSkillsBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laodSkills();

    }

    private void laodSkills() {
        new UserDatabaseProvider().getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!=null){
                    ArrayList<Skill> listSkills=documentSnapshot.toObject(User.class).getSkills();

                    if(listSkills!=null&&listSkills.size()>0){
                        binding.rvSkilsList.setLayoutManager(new LinearLayoutManager(getContext()));
                        SkillsAdapter adapterSkills = new SkillsAdapter(getContext(), listSkills);
                        binding.rvSkilsList.setAdapter(adapterSkills);
                    }
                }
            }
        }).addOnFailureListener(runnable -> Log.e(TAG, "laodSkills: "+runnable.getMessage() ));

    }
}