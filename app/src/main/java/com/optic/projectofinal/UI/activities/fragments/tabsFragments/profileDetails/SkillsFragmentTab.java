package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.SkillsAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillsFragmentTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillsFragmentTab extends Fragment {


    public SkillsFragmentTab() {
        // Required empty public constructor
    }

    public static SkillsFragmentTab newInstance() {
        SkillsFragmentTab fragment = new SkillsFragmentTab();
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

        View vista=inflater.inflate(R.layout.fragment_tab, container, false);

        return vista;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvSkilsList = view.findViewById(R.id.rvSkilsList);
        rvSkilsList.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Integer> lista = new ArrayList<>(5);
        SkillsAdapter adapterSkills = new SkillsAdapter(getContext(), lista);
        rvSkilsList.setAdapter(adapterSkills);



    }
}