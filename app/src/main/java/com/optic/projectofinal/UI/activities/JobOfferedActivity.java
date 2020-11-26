package com.optic.projectofinal.UI.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ApplyJobAdapterFirebase;
import com.optic.projectofinal.adapters.SliderAdapterExample;
import com.optic.projectofinal.databinding.ActivityJobOfferedBinding;
import com.optic.projectofinal.models.ApplyJob;
import com.optic.projectofinal.providers.ApplyJobWorkerDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class JobOfferedActivity extends AppCompatActivity {
    private ActivityJobOfferedBinding binding;
    private ApplyJobAdapterFirebase adapter;
    private ApplyJobWorkerDatabaseProvider mApplyJobWorker;
    private UserDatabaseProvider mUserProvider;
    private String idJobSelected;
    private String idUserCreateJobSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set binding
        binding=ActivityJobOfferedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //set toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///get intetn
        idJobSelected=getIntent().getStringExtra("idJobSelected");
        idUserCreateJobSelected=getIntent().getStringExtra("idUserCreateJobSelected");
        ///instance objects
        mApplyJobWorker=new ApplyJobWorkerDatabaseProvider(idJobSelected);
        mUserProvider=new UserDatabaseProvider();
        ///set recyvler view config
        binding.applyWorkers.setLayoutManager(new LinearLayoutManager(this));
        /// set config slider
        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapterExample(this));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(false);
        //set title appbar when is collapsed
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.MAGENTA);
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarLayout.setTitle(getString(R.string.job_offered_activity_title));
                    isShow = true;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.teal_200));
                } else if (isShow) {
                    binding.toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.WHITE);

                }
            }
        });
        //load data profile
        loadUserData();
        binding.btnSeeProfileDetail.setOnClickListener(v->{
            Intent i=new Intent(JobOfferedActivity.this,ProfileDetailsActivity.class);
            i.putExtra("idUser",idUserCreateJobSelected);
            startActivity(i);
        });

    }

    private void loadUserData(){
        mUserProvider.getUser(idUserCreateJobSelected).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!=null&&documentSnapshot.exists()){
                    String name=documentSnapshot.getString("name");
                    String lastName=documentSnapshot.getString("lastName");
                    String profileImage=documentSnapshot.getString("profileImage");
                    System.out.println("nombrere "+name);
                    binding.nameUser.setText(name);
                    binding.lastNameUser.setText(lastName);
                    Glide.with(JobOfferedActivity.this).load(profileImage).placeholder(R.drawable.loading).centerInside().into(binding.ivPhotoProfile);
                }else{
                    Log.e("own", "onSuccess: JobOfferedActivity->loadUserData" );
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("own", "onFailure: JobOfferedActivity->loadUserData" );
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        Query query=mApplyJobWorker.getAll();
        ///comprobar no sea nulo
        if(query!=null){
            FirestoreRecyclerOptions<ApplyJob> options= new FirestoreRecyclerOptions.Builder<ApplyJob>().setQuery(query, ApplyJob.class).build();
            adapter = new ApplyJobAdapterFirebase(JobOfferedActivity.this,options);
            binding.applyWorkers.setAdapter(adapter);
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter !=null){
            adapter.stopListening();
        }
    }
}