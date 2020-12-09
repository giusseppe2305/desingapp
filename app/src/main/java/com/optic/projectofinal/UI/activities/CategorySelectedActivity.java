package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.WorkersAdapter;
import com.optic.projectofinal.databinding.ActivityCategorySelectedBinding;
import com.optic.projectofinal.databinding.BottomSheetLastConexionBinding;
import com.optic.projectofinal.databinding.BottomSheetOrderBinding;
import com.optic.projectofinal.databinding.BottomSheetPriceBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.SubCategory;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;

public class CategorySelectedActivity extends AppCompatActivity {
    private static final String TAG = "own";
    private Category categorySelected;
    private ActivityCategorySelectedBinding binding;
    private String priceSince,priceUntil;
    private BottomSheetDialog bottomSheetDialogPrice;
    private BottomSheetPriceBinding fragmentBindingPrice;
    private BottomSheetDialog bottomSheetDialogOrder;
    private com.optic.projectofinal.databinding.BottomSheetOrderBinding fragmentBindingOrder;

    private BottomSheetDialog bottomSheetDialogLastConexion;
    private BottomSheetLastConexionBinding fragmentBindingLastConexion;
    private UserDatabaseProvider.Order optionOrder;
    private UserDatabaseProvider mUserDatabase;

    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserProvider;
    private WorkersAdapter workersAdapterFirebase;
    private int optionLastConexion;

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
         optionLastConexion = -1;

        priceUntil=null;
        priceSince=null;
        //instance
        mAuth=new AuthenticationProvider();
        mUserProvider=new UserDatabaseProvider();
        mUserDatabase=new UserDatabaseProvider();
        //recyclerview

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

        ///Last conexion
         bottomSheetDialogLastConexion = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
         fragmentBindingLastConexion = BottomSheetLastConexionBinding.inflate(getLayoutInflater());
        View viewLastConexion= fragmentBindingLastConexion.getRoot();
        bottomSheetDialogLastConexion.setContentView(viewLastConexion);
        createViewLastConexion();


        binding.chipPrice.setOnClickListener(view ->bottomSheetDialogPrice.show());
        binding.chipLastConexion.setOnClickListener(view -> bottomSheetDialogLastConexion.show());
        binding.chipOrder.setOnClickListener(view -> bottomSheetDialogOrder.show());
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




    public void selectionCategoryDone(SubCategory iterated) {
        doQueryAndUpdateResults();


    }
    private void doQueryAndUpdateResults() {
        createQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<User> listWorkers=new ArrayList<>();
                for(DocumentSnapshot i:queryDocumentSnapshots.getDocuments()){
                    if(!i.getString("id").equals(mAuth.getIdCurrentUser())){
                        listWorkers.add(i.toObject(User.class));
                    }
                }
                Log.e(TAG, "onSuccess: "+listWorkers.size());
                workersAdapterFirebase = new WorkersAdapter(CategorySelectedActivity.this, listWorkers);
                binding.rvCategorySelected.setAdapter(workersAdapterFirebase);
                binding.rvCategorySelected.setLayoutManager(new LinearLayoutManager(CategorySelectedActivity.this,RecyclerView.VERTICAL,false));
            }
        }).addOnFailureListener(v-> Log.e(TAG, "doQueryAndUpdateResults: "+v.getMessage() ));


    }

    private Query createQuery() {
       return mUserDatabase.filterWorkers(categorySelected.getId(),priceSince,priceUntil, optionOrder);
    }

    ///cycle life
    @Override
    public void onStart() {
        super.onStart();
//        doQueryAndUpdateResults();

    }


}