package com.optic.projectofinal.UI.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.adapters.CategoriesAdapter;
import com.optic.projectofinal.adapters.WorkersAdapterFirebase;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkersFragment extends Fragment {

    private UserDatabaseProvider mUserProvider;
    private WorkersAdapterFirebase workersAdapterFirebase;
   // private FragmentWorkersBinding binding;
    private CategoriesAdapter adapterCategories;
    private AuthenticationProvider mAuth;
    private RecyclerView mRecyclerView;

    public WorkersFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
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
//        binding = FragmentWorkersBinding.inflate(inflater, container, false);
//        View vista = binding.getRoot();
        View mView=inflater.inflate(R.layout.fragment_workers,container,false);
        mRecyclerView=mView.findViewById(R.id.rvWorkers);



        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();

        RecyclerView rvCategories=mView.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        Type typeOfObjectsList = new TypeToken<ArrayList<Category>>() {
        }.getType();
        List<Category> myCategoriesList = new Gson().fromJson(Utils.getArrayCategoriosJson(getContext()), typeOfObjectsList);
        adapterCategories = new CategoriesAdapter(getContext(), myCategoriesList);
        rvCategories.setAdapter(adapterCategories);


        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_activity, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSeeProfile: {
                startActivity(new Intent(getContext(), ProfileDetailsActivity.class));
                break;
            }
        }
        return false;
    }
    ///cycle life

    @Override
    public void onStart() {
        super.onStart();
        Query query = mUserProvider.getAllWorkers();///comprobar no sea nulo

            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
            workersAdapterFirebase = new WorkersAdapterFirebase(getContext(), options);
            mRecyclerView.setAdapter(workersAdapterFirebase);
            workersAdapterFirebase.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (workersAdapterFirebase != null) {
            workersAdapterFirebase.stopListening();
        }
    }


}