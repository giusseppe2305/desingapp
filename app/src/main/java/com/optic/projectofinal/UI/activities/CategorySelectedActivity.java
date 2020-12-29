package com.optic.projectofinal.UI.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.WorkersqueryAdapter;
import com.optic.projectofinal.databinding.ActivityCategorySelectedBinding;
import com.optic.projectofinal.databinding.BottomSheetLastConexionBinding;
import com.optic.projectofinal.databinding.BottomSheetOrderBinding;
import com.optic.projectofinal.databinding.BottomSheetPriceBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.modelsRetrofit.WorkerQueryModel;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.retrofit.FunctionsApi;
import com.optic.projectofinal.retrofit.RetrofitClient;
import com.optic.projectofinal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optic.projectofinal.retrofit.RetrofitClient.FIREBASE_FUNCTIONS;
import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class CategorySelectedActivity extends AppCompatActivity {
    private enum Order {
        LOWER_TO_HIGHER(2),
        HIGHER_TO_LOWER(3),
        RECENT(1),
        OLD(0);
        private final int value;

        Order(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Category categorySelected;
    private ActivityCategorySelectedBinding binding;
    private Double priceSince, priceUntil;
    private BottomSheetDialog bottomSheetDialogPrice;
    private BottomSheetPriceBinding fragmentBindingPrice;
    private BottomSheetDialog bottomSheetDialogOrder;
    private com.optic.projectofinal.databinding.BottomSheetOrderBinding fragmentBindingOrder;

    private BottomSheetDialog bottomSheetDialogLastConexion;
    private BottomSheetLastConexionBinding fragmentBindingLastConexion;
    private Order optionOrder;
    private UserDatabaseProvider mUserDatabase;

    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserProvider;
    private List<WorkerQueryModel> listWorkers;
    private WorkersqueryAdapter adapter;
    private Integer optionLastConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categorySelected = Utils.getCategoryByIdJson(this, getIntent().getIntExtra("category", 0));
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(getString(categorySelected.getIdTitle()));
        ///set init values
        optionOrder = Order.LOWER_TO_HIGHER;
        optionLastConexion = null;

        priceUntil = null;
        priceSince = null;
        //instance
        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();
        mUserDatabase = new UserDatabaseProvider();
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
        View viewLastConexion = fragmentBindingLastConexion.getRoot();
        bottomSheetDialogLastConexion.setContentView(viewLastConexion);
        createViewLastConexion();


        binding.chipPrice.setOnClickListener(view -> bottomSheetDialogPrice.show());
        binding.chipLastConexion.setOnClickListener(view -> bottomSheetDialogLastConexion.show());
        binding.chipOrder.setOnClickListener(view -> bottomSheetDialogOrder.show());
        ///setup recucler view
        listWorkers = new ArrayList<>();
        adapter = new WorkersqueryAdapter(this, listWorkers);
        binding.rvCategorySelected.setAdapter(adapter);
        binding.rvCategorySelected.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    private void createViewLastConexion() {

        fragmentBindingLastConexion.RadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int idSelected = fragmentBindingLastConexion.RadioGroup.getCheckedRadioButtonId();
            RadioButton view = fragmentBindingLastConexion.RadioGroup.findViewById(idSelected);
            optionLastConexion = Integer.parseInt(view.getTag().toString());
            binding.chipLastConexion.setText("Ultima conexion: " + view.getText().toString());
            bottomSheetDialogLastConexion.dismiss();
            doQueryAndUpdateResults();
        });
        fragmentBindingLastConexion.clear.setOnClickListener(view -> {
            optionLastConexion = null;
            binding.chipLastConexion.setText("Ultima conexion");
            bottomSheetDialogLastConexion.dismiss();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createViewOrder() {
        fragmentBindingOrder.priceLowerToHigher.setTag(Order.LOWER_TO_HIGHER);
        fragmentBindingOrder.recent.setTag(Order.RECENT);
        fragmentBindingOrder.old.setTag(Order.OLD);
        fragmentBindingOrder.priceHigherToLower.setTag(Order.HIGHER_TO_LOWER);
        fragmentBindingOrder.RadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int idSelected = fragmentBindingOrder.RadioGroup.getCheckedRadioButtonId();
            RadioButton view = fragmentBindingOrder.RadioGroup.findViewById(idSelected);
            optionOrder = (Order) view.getTag();
            binding.chipOrder.setText("Ordenar: " + view.getText().toString());
            bottomSheetDialogOrder.dismiss();
            doQueryAndUpdateResults();

        });
        fragmentBindingOrder.priceLowerToHigher.performClick();

    }

    private void createViewPrice() {
        fragmentBindingPrice.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priceSince_ = fragmentBindingPrice.priceSince.getValue();
                int priceUntil_ = fragmentBindingPrice.priceUntil.getValue();
                if (priceSince_ <= priceUntil_) {
                    priceSince = (double) priceSince_;
                    priceUntil = (double) priceUntil_;
                    doQueryAndUpdateResults();
                    binding.chipPrice.setText("Precio: " + priceSince + "€ - " + priceUntil + "€");
                    bottomSheetDialogPrice.dismiss();
                } else {
                    Toast.makeText(CategorySelectedActivity.this, "Seleccione unas opciones correctas.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    private void doQueryAndUpdateResults() {
        binding.loading.setVisibility(View.VISIBLE);
        RetrofitClient.getClient(FIREBASE_FUNCTIONS).create(FunctionsApi.class)
                .professionals(categorySelected.getId(), mAuth.getIdCurrentUser(), optionOrder.getValue(), priceSince, priceUntil, optionLastConexion).enqueue(new Callback<List<WorkerQueryModel>>() {
            @Override
            public void onResponse(Call<List<WorkerQueryModel>> call, Response<List<WorkerQueryModel>> response) {


                if (response.isSuccessful()) {
                    binding.loading.setVisibility(View.GONE);
                    listWorkers.clear();
                    if (response.body().size() > 0) {
                        Log.d(TAG_LOG, "onResponse: " + call.request().url().toString());
                        for (WorkerQueryModel it : response.body()) {
                            Log.d(TAG_LOG, "onResponse: " + it);
                        }
//                        int positionStart=listWorkers.size()+1;
//                        listWorkers.addAll(response.body());
//                        adapter.notifyItemRangeInserted(positionStart,response.body().size());

                        listWorkers.addAll(response.body());

                    } else {
                        Log.d(TAG_LOG, "onResponse: no results query");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG_LOG, "onResponse: fallo ");
                }
            }

            @Override
            public void onFailure(Call<List<WorkerQueryModel>> call, Throwable t) {
                Log.d(TAG_LOG, "onFailure: " + t.getMessage());
            }
        });
    }


}