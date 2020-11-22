package com.optic.projectofinal.UI.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.UI.activities.options_profile.Auctions_Activity;
import com.optic.projectofinal.UI.activities.options_profile.Favourites_Workers_Activity;
import com.optic.projectofinal.UI.activities.options_profile.JobsActivity;
import com.optic.projectofinal.UI.activities.options_profile.SettingsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private Toolbar mToolbar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View vista = inflater.inflate(R.layout.activity_profile, container, false);
        mToolbar=vista.findViewById(R.id.ownToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Perfil");

       vista.findViewById(R.id.option_auctions).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                startActivity(new Intent(getContext(), Auctions_Activity.class));
           }
       });
        vista.findViewById(R.id.option_favourites_workers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Favourites_Workers_Activity.class));

            }
        });
        vista.findViewById(R.id.option_jobs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), JobsActivity.class));

            }
        });
        vista.findViewById(R.id.option_see_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileDetailsActivity.class));

            }
        });
        vista.findViewById(R.id.option_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingsActivity.class));

            }
        });

        return vista;
    }



}