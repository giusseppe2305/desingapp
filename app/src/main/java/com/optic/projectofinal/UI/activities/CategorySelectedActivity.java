package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.SubCategoriesAdapterFirebase;
import com.optic.projectofinal.adapters.WorkersAdapterFirebase;
import com.optic.projectofinal.databinding.ActivityCategorySelectedBinding;
import com.optic.projectofinal.databinding.BottomSheetLastConexionBinding;
import com.optic.projectofinal.databinding.BottomSheetOrderBinding;
import com.optic.projectofinal.databinding.BottomSheetPriceBinding;
import com.optic.projectofinal.databinding.BottomSheetSubcategoryBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class CategorySelectedActivity extends AppCompatActivity {
    private Category categorySelected;
    private ActivityCategorySelectedBinding binding;
    private String priceSince,priceUntil;
    private BottomSheetDialog bottomSheetDialogPrice;
    private BottomSheetPriceBinding fragmentBindingPrice;
    private BottomSheetDialog bottomSheetDialogOrder;
    private com.optic.projectofinal.databinding.BottomSheetOrderBinding fragmentBindingOrder;
    private BottomSheetDialog bottomSheetDialogCategory;
    private BottomSheetSubcategoryBinding fragmentBindingCategory;
    private BottomSheetDialog bottomSheetDialogLastConexion;
    private BottomSheetLastConexionBinding fragmentBindingLastConexion;
    private UserDatabaseProvider.Order optionOrder;
    private int optionLastConexion;
    private UserDatabaseProvider mUserDatabase;
    private SubcategoriesDatabaseProvider mSubCategoriesDatabase;
    private SubCategory optionSubCategory;
    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserProvider;
    private WorkersAdapterFirebase workersAdapterFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categorySelected = Utils.getCategoryByIdJson(this, getIntent().getIntExtra("category", 0));
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(getString(categorySelected.getIdTitle()));
        ///set init values
        optionOrder= UserDatabaseProvider.Order.LOWER_TO_HIGHER;
        optionLastConexion=-1;
        optionSubCategory=null;
        priceUntil=null;
        priceSince=null;
        //instance
        mAuth=new AuthenticationProvider();
        mUserProvider=new UserDatabaseProvider();
        mSubCategoriesDatabase=new SubcategoriesDatabaseProvider();
        mUserDatabase=new UserDatabaseProvider();
        //recyclerview
        binding.rvCategorySelected.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        ///price
         bottomSheetDialogPrice = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
         fragmentBindingPrice = BottomSheetPriceBinding.inflate(getLayoutInflater());
        View viewPrice = fragmentBindingPrice.getRoot();
        bottomSheetDialogPrice.setContentView(viewPrice);
        createViewPrice();

        ///Order
         bottomSheetDialogOrder = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
         fragmentBindingOrder = BottomSheetOrderBinding.inflate(getLayoutInflater());
        View viewOrder = fragmentBindingOrder.getRoot();
        bottomSheetDialogOrder.setContentView(viewOrder);
        createViewOrder();
        ///Category
         bottomSheetDialogCategory = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
         fragmentBindingCategory = BottomSheetSubcategoryBinding.inflate(getLayoutInflater());
        View viewCategory = fragmentBindingCategory.getRoot();
        bottomSheetDialogCategory.setContentView(viewCategory);
        createViewCategory();
        ///Last conexion
         bottomSheetDialogLastConexion = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
         fragmentBindingLastConexion = BottomSheetLastConexionBinding.inflate(getLayoutInflater());
        View viewLastConexion= fragmentBindingLastConexion.getRoot();
        bottomSheetDialogLastConexion.setContentView(viewLastConexion);
        createViewLastConexion();


        binding.chipPrice.setOnClickListener(view ->bottomSheetDialogPrice.show());
        binding.chipLastConexion.setOnClickListener(view -> bottomSheetDialogLastConexion.show());
        binding.chipOrder.setOnClickListener(view -> bottomSheetDialogOrder.show());
        binding.chipSubCategory.setOnClickListener(view -> bottomSheetDialogCategory.show());
    }

    private void createViewLastConexion() {

        fragmentBindingLastConexion.RadioGroup.setOnCheckedChangeListener((radioGroup,i)->{
            int idSelected=fragmentBindingLastConexion.RadioGroup.getCheckedRadioButtonId();
            RadioButton view=fragmentBindingLastConexion.RadioGroup.findViewById(idSelected);
            optionLastConexion= Integer.parseInt(view.getTag().toString());
            binding.chipLastConexion.setText("Ultima conexion: "+view.getText().toString());
            bottomSheetDialogLastConexion.dismiss();
        });
        fragmentBindingLastConexion.clear.setOnClickListener(view -> {
            optionLastConexion=-1;
            binding.chipLastConexion.setText("Ultima conexion");
            bottomSheetDialogLastConexion.dismiss();});

    }

    private void createViewCategory() {
        fragmentBindingCategory.rvSubCategoriesList.setLayoutManager(new LinearLayoutManager(CategorySelectedActivity.this, RecyclerView.VERTICAL,false));
        SubCategoriesAdapterFirebase adapter = new SubCategoriesAdapterFirebase(CategorySelectedActivity.this, new ArrayList<SubCategory>());
        fragmentBindingCategory.rvSubCategoriesList.setAdapter(adapter);
        loadCategories(adapter);
        fragmentBindingCategory.closeFragment.setOnClickListener(v->bottomSheetDialogCategory.dismiss());
    }

    private void createViewOrder() {
        fragmentBindingOrder.priceLowerToHigher.setTag(UserDatabaseProvider.Order.LOWER_TO_HIGHER);
        fragmentBindingOrder.distance.setTag(UserDatabaseProvider.Order.DISTANCE);
        fragmentBindingOrder.priceHigherToLower.setTag(UserDatabaseProvider.Order.HIGHER_TO_LOWER);
        fragmentBindingOrder.RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int idSelected = fragmentBindingOrder.RadioGroup.getCheckedRadioButtonId();
                RadioButton view=fragmentBindingOrder.RadioGroup.findViewById(idSelected);
                optionOrder= (UserDatabaseProvider.Order) view.getTag();
                binding.chipOrder.setText("Ordenar: "+view.getText().toString());
                bottomSheetDialogOrder.dismiss();
                doQueryAndUpdateResults();

            }
        });
        fragmentBindingOrder.priceLowerToHigher.performClick();

    }

    private void createViewPrice() {
        fragmentBindingPrice.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priceSince_ = fragmentBindingPrice.priceSince.getValue();
                int priceUntil_ = fragmentBindingPrice.priceUntil.getValue();
               if(priceSince_<=priceUntil_)
               {
                   priceSince=String.valueOf(priceSince_);
                   priceUntil=String.valueOf(priceUntil_);
                   doQueryAndUpdateResults();
                   binding.chipPrice.setText("Precio: "+priceSince+"€ - "+priceUntil+"€");
                   bottomSheetDialogPrice.dismiss();
               }else{
                   Toast.makeText(CategorySelectedActivity.this, "Seleccione unas opciones correctas.", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }


    private void loadCategories(SubCategoriesAdapterFirebase adapter){
        mSubCategoriesDatabase.getAllByCategory(String.valueOf(categorySelected.getId())).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null){
                    for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                        final SubCategory own=new SubCategory();
                        own.setId(i.getId());
                        own.setName(i.getString("name"));
                        mUserDatabase.getUsersBySubcategory(i.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots!=null){
                                    int size=queryDocumentSnapshots.getDocuments().size();
                                    if(size>0){
                                        own.setSize(size);
                                        adapter.getList().add(own);
                                        adapter.notifyDataSetChanged();
                                    }
                                }else{
                                    Log.e("own", "getUsersBySubcategory -> onSuccess->else: ");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("own", "getUsersBySubcategory -> onFailure: ");
                            }
                        });

                    }
                }else{
                    Log.e("own", "getAllSubcategories -> onSuccess->else: ");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("own", "getAllSubcategories -> onFailure: ");

            }
        });
    }

    public void selectionCategoryDone(SubCategory iterated) {
        bottomSheetDialogCategory.dismiss();
        doQueryAndUpdateResults();
        optionSubCategory=iterated;
        binding.chipSubCategory.setText("Subcategoria: "+iterated.getName());
    }
    private void doQueryAndUpdateResults() {
        Query query = createQuery();///comprobar no sea nulo
        if(workersAdapterFirebase!=null){
            workersAdapterFirebase.startListening();
        }else{
            if (query != null) {
                Toast.makeText(CategorySelectedActivity.this, "Entra on start", Toast.LENGTH_SHORT).show();
                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
                workersAdapterFirebase = new WorkersAdapterFirebase(CategorySelectedActivity.this, options);
                binding.rvCategorySelected.setAdapter(workersAdapterFirebase);
                workersAdapterFirebase.startListening();
            }
        }
    }

    private Query createQuery() {
       return mUserDatabase.filterWorkers(categorySelected.getId(),optionSubCategory,priceSince,priceUntil, optionOrder);
    }

    ///cycle life
    @Override
    public void onStart() {
        super.onStart();
        doQueryAndUpdateResults();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (workersAdapterFirebase != null) {
            workersAdapterFirebase.stopListening();
        }
    }

}