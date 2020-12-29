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

import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.CategoriesAdapter;
import com.optic.projectofinal.adapters.WorkersqueryAdapter;
import com.optic.projectofinal.databinding.FragmentWorkersBinding;
import com.optic.projectofinal.modelsRetrofit.WorkerQueryModel;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.retrofit.FunctionsApi;
import com.optic.projectofinal.retrofit.RetrofitClient;
import com.optic.projectofinal.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optic.projectofinal.retrofit.RetrofitClient.FIREBASE_FUNCTIONS;
import static com.optic.projectofinal.utils.Utils.TAG_LOG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkersFragment extends Fragment {


    private UserDatabaseProvider mUserProvider;
    private AuthenticationProvider mAuth;
    private WorkersqueryAdapter workersAdapter;
    private FragmentWorkersBinding binding;
    private CategoriesAdapter adapterCategories;


    public WorkersFragment() {
        // Required empty public constructor
       // setHasOptionsMenu(true);
    }


    public static WorkersFragment newInstance() {
        return new WorkersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkersBinding.inflate(inflater, container, false);



        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();
        adapterCategories = new CategoriesAdapter(getContext(), Utils.getListCategoriesJson(getContext()));
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(adapterCategories);

        binding.loading.setVisibility(View.VISIBLE);
        RetrofitClient.getClient(FIREBASE_FUNCTIONS).create(FunctionsApi.class)
                .getAllProfessionals(mAuth.getIdCurrentUser()).enqueue(new Callback<List<WorkerQueryModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<WorkerQueryModel>> call, @NotNull Response<List<WorkerQueryModel>> response) {
                Log.d(TAG_LOG, "onResponse: entro");
                if(response.isSuccessful()){
                    for(WorkerQueryModel it:response.body())
                    {
                        Log.d(TAG_LOG, "onResponse: "+it.toString());
                    }
                    binding.loading.setVisibility(View.GONE);

                    workersAdapter = new WorkersqueryAdapter(getContext(), response.body());
                    binding.rvWorkers.setAdapter(workersAdapter);
                    binding.rvWorkers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                }else{
                    Log.d(TAG_LOG, "onResponse: fallo ");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<WorkerQueryModel>> call, @NotNull Throwable t) {
                Log.d(TAG_LOG, "onFailure: "+t.getMessage());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_activity, menu);
        menu.findItem(R.id.menuNotifications).getActionView().findViewById(R.id.containerNotifications).setOnClickListener(view -> {

        });




//
//        mUserProvider.getAllWorkers().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                ArrayList<User> lisWorkers=new ArrayList<>();
//                for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
//                    if(!i.getString("id").equals(mAuth.getIdCurrentUser())){
//                        lisWorkers.add(i.toObject(User.class));
//                    }
//                }
//                workersAdapter = new WorkersAdapter(getContext(), lisWorkers);
//                binding.rvWorkers.setAdapter(workersAdapter);
//                binding.rvWorkers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//            }
//        }).addOnFailureListener(runnable -> Log.e(TAG_LOG, "onStart: "+runnable.getMessage() ));
    }

}