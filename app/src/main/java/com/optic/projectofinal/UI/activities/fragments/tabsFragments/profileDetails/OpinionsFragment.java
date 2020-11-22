package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.JobDoneActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpinionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpinionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpinionsFragment() {
        // Required empty public constructor
    }


    public static OpinionsFragment newInstance() {
        OpinionsFragment fragment = new OpinionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_opinions, container, false);
        superParent=vista.findViewById(R.id.superParent);
        Spinner spinner=vista.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,new String[]{"Todos ","Cerrajero","Fontanero","Jardinero"}));

        vista.findViewById(R.id.jobDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), JobDoneActivity.class));
            }
        });
        return vista;
    }
    public FrameLayout superParent;
}