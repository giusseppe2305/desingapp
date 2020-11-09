package com.optic.projectofinal.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.optic.projectofinal.R;
import com.optic.projectofinal.Utils;
import com.optic.projectofinal.adapters.CategoriesAdapter;
import com.optic.projectofinal.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {


    private RecyclerView rvCategories;
    private CategoriesAdapter adapterCategories;

    public PostsFragment() {
        // Required empty public constructor
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
        View vista = inflater.inflate(R.layout.fragment_posts, container, false);
        rvCategories = (RecyclerView) vista.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));


        Type typeOfObjectsList = new TypeToken<ArrayList<Category>>() {}.getType();
        List<Category> myCategoriesList=new Gson().fromJson(Utils.getArrayCategoriosJson(getContext()),typeOfObjectsList);
        adapterCategories = new CategoriesAdapter(getContext(),myCategoriesList);

        rvCategories.setAdapter(adapterCategories);


        return vista;
    }
}