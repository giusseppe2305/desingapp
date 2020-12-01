package com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.optic.projectofinal.databinding.FragmentAuctionsParticipeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuctionsParticipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuctionsParticipeFragment extends Fragment {

    private FragmentAuctionsParticipeBinding binding;
    public AuctionsParticipeFragment() {
        // Required empty public constructor
    }

    public static AuctionsParticipeFragment newInstance(String param1, String param2) {
        AuctionsParticipeFragment fragment = new AuctionsParticipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAuctionsParticipeBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}