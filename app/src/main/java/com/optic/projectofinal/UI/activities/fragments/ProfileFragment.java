package com.optic.projectofinal.UI.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.UI.activities.options_profile.Auctions_Activity;
import com.optic.projectofinal.UI.activities.options_profile.Favourites_Workers_Activity;
import com.optic.projectofinal.UI.activities.options_profile.JobsActivity;
import com.optic.projectofinal.UI.activities.options_profile.SettingsActivity;
import com.optic.projectofinal.databinding.ActivityProfileBinding;
import com.optic.projectofinal.models.BasicInformationUser;
import com.optic.projectofinal.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private Toolbar mToolbar;
    private ActivityProfileBinding binding;
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
        binding=ActivityProfileBinding.inflate(inflater,container,false);


        binding.optionAuctions.setOnClickListener(view -> startActivity(new Intent(getContext(), Auctions_Activity.class)));
        binding.optionFavouritesWorkers.setOnClickListener(view -> startActivity(new Intent(getContext(), Favourites_Workers_Activity.class)));
        binding.optionJobs.setOnClickListener(view -> startActivity(new Intent(getContext(), JobsActivity.class)));
        binding.optionSeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileDetailsActivity.class));

            }
        });
        binding.optionSettings.findViewById(R.id.option_settings).setOnClickListener(view -> startActivity(new Intent(getContext(), SettingsActivity.class)));
        BasicInformationUser basicInformationUser=Utils.getPersistentBasicUserInformation(getContext());

        Glide.with(getContext()).load(basicInformationUser.getPhotoUser()).apply(Utils.getOptionsGlide(false)).into(binding.photoProfile);
        binding.fullNameUser.setText(basicInformationUser.getName()+" "+basicInformationUser.getLastName());
        return binding.getRoot();
    }



}