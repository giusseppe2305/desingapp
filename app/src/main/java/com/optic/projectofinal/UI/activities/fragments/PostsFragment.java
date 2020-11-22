package com.optic.projectofinal.UI.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.FragmentPostsBinding;
import com.optic.projectofinal.utils.Utils;
import com.optic.projectofinal.UI.activities.ProfileDetailsActivity;
import com.optic.projectofinal.adapters.CategoriesAdapter;
import com.optic.projectofinal.adapters.PostsAdapter;
import com.optic.projectofinal.models.Category;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {



    private FragmentPostsBinding binding;
    private CategoriesAdapter adapterCategories;

    public PostsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPostsBinding.inflate(inflater,container,false);
        View vista = binding.getRoot();


        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.appbar.ownToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.post_fragment_title));



        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));


        binding.rvPosts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rvPosts.setAdapter(new PostsAdapter(getContext()));



        Type typeOfObjectsList = new TypeToken<ArrayList<Category>>() {}.getType();
        List<Category> myCategoriesList=new Gson().fromJson(Utils.getArrayCategoriosJson(getContext()),typeOfObjectsList);
        adapterCategories = new CategoriesAdapter(getContext(),myCategoriesList);
        binding.rvCategories.setAdapter(adapterCategories);


        return vista;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main_activity,menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSeeProfile:{
                startActivity(new Intent(getContext(), ProfileDetailsActivity.class));
                break;
            }
        }
        return false;
    }

}