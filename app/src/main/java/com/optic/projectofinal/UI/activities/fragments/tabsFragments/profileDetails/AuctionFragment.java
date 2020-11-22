package com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.optic.projectofinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuctionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuctionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuctionFragment() {
        // Required empty public constructor
    }


    public static AuctionFragment newInstance() {
        AuctionFragment fragment = new AuctionFragment();

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
        View vista=inflater.inflate(R.layout.fragment_auctions, container, false);
        superParent=vista.findViewById(R.id.superParent);
        Spinner spinner=vista.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,new String[]{"Fecha (Ascedente)","Fecha (Descendiente)","Precio (Ascendente)"}));
        return vista;
    }
    public FrameLayout superParent;
}