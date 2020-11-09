package com.optic.projectofinal.fragments.tabsFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optic.projectofinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabFragment extends Fragment {


    public tabFragment() {
        // Required empty public constructor
    }

    public static tabFragment newInstance(Integer counter) {
        tabFragment fragment = new tabFragment();
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
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textViewCounter = view.findViewById(R.id.tv_counter);
        textViewCounter.setText("hola\r\nholahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "holahola\n" +
                "hola");
    }
}