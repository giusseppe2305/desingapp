package com.optic.projectofinal.UI.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.CategoriesAdapter;
import com.optic.projectofinal.adapters.WorkersAdapter;
import com.optic.projectofinal.databinding.FragmentWorkersBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkersFragment extends Fragment {

    private static final String TAG = "own";
    private UserDatabaseProvider mUserProvider;
    private AuthenticationProvider mAuth;
    private WorkersAdapter workersAdapter;
    private FragmentWorkersBinding binding;
    private CategoriesAdapter adapterCategories;


    public WorkersFragment() {
        // Required empty public constructor
       // setHasOptionsMenu(true);
    }


    public static WorkersFragment newInstance(String param1, String param2) {
        WorkersFragment fragment = new WorkersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkersBinding.inflate(inflater, container, false);



        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();
        adapterCategories = new CategoriesAdapter(getContext(), Utils.getListCategoriesJson(getContext()));
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(adapterCategories);


        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_activity, menu);
        menu.findItem(R.id.menuNotifications).getActionView().findViewById(R.id.containerNotifications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    ///cycle life

    @Override
    public void onStart() {
        super.onStart();
         mUserProvider.getAllWorkers().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 ArrayList<User> lisWorkers=new ArrayList<>();
                 for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                     if(!i.getString("id").equals(mAuth.getIdCurrentUser())){
                        lisWorkers.add(i.toObject(User.class));
                     }
                 }
                 workersAdapter = new WorkersAdapter(getContext(), lisWorkers);
                 binding.rvWorkers.setAdapter(workersAdapter);
                 binding.rvWorkers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
             }
         }).addOnFailureListener(runnable -> Log.e(TAG, "onStart: "+runnable.getMessage() ));


    }




}