package com.optic.projectofinal.UI.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.SliderAdapterExample;
import com.optic.projectofinal.adapters.SliderItem;
import com.optic.projectofinal.databinding.ActivityJobDoneBinding;
import com.optic.projectofinal.models.Category;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.providers.SubcategoriesDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class JobDoneActivity extends AppCompatActivity {

    private ActivityJobDoneBinding binding;
    private ArrayList<SliderItem> imagesSlider;
    private SliderAdapterExample adapterSlider;
    private static final String TAG = "own";
    private UserDatabaseProvider mUserProvider;
    private JobsDatabaseProvider jobsDatabaseProvider;
    private SubcategoriesDatabaseProvider subcategoriesDatabaseProvider;
    private String idJobSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJobDoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idJobSelected=getIntent().getStringExtra("idJob");
        ///instance objects
        subcategoriesDatabaseProvider = new SubcategoriesDatabaseProvider();
        jobsDatabaseProvider = new JobsDatabaseProvider();
        mUserProvider = new UserDatabaseProvider();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("trabajo hecho");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///pre config slider
        imagesSlider = new ArrayList<>();
        adapterSlider = new SliderAdapterExample(this, imagesSlider);
        loadJobData();

        SliderView sliderView = binding.imageSlider;
        sliderView.setSliderAdapter(adapterSlider);
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


    }
    private void loadJobData() {
        jobsDatabaseProvider.getJobById(idJobSelected).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Job myJob = documentSnapshot.toObject(Job.class);
                    loadUserAppliedData(myJob.getIdUserApply());
                    loadUserOfferData(myJob.getIdUserOffer());


                    binding.description.setText(myJob.getDescription());
                    binding.title.setText(myJob.getTitle());
                    //
                    for (String i : myJob.getImages()) {
                        adapterSlider.addItem(new SliderItem(i));
                    }
                    ///
                    Category category = Utils.getCategoryByIdJson(JobDoneActivity.this, myJob.getCategory());
                    category.setTitleString(JobDoneActivity.this);
                    binding.chipCategory.setText(category.getTitleString());
                    subcategoriesDatabaseProvider.getSubCategoryById(myJob.getSubcategory()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                binding.chipSubCategory.setText(documentSnapshot.getString("name"));
                            } else {
                                Log.e(TAG, "onSuccess: JobOfferedActivity->loadJobData");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: JobOfferedActivity->loadJobData");
                        }
                    });
                    //valuation
                    binding.rating1.setRating((float) myJob.getValuation().getAmiability());
                    binding.rating2.setRating((float) myJob.getValuation().getSpeedEndJob());
                    binding.rating3.setRating((float) myJob.getValuation().getPunctuality());
                    binding.rating4.setRating((float) myJob.getValuation().getSpeedContact());

                } else {
                    Log.e(TAG, "onSuccess:else JobOfferedActivity->getJobById");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: JobOfferedActivity->loadJobData");
            }
        });


    }

    private void loadUserOfferData(String idUserOffer) {
        mUserProvider.getUser(idUserOffer).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(JobDoneActivity.this).load(documentSnapshot.getString("profileImage")).apply(Utils.getOptionsGlide(false)).into(binding.photoUserOffer);
            }
        });
    }

    private void loadUserAppliedData(String idUserApply) {
        mUserProvider.getUser(idUserApply).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(JobDoneActivity.this).load(documentSnapshot.getString("profileImage")).apply(Utils.getOptionsGlide(false)).into(binding.photoUserApplied);
            }
        });
    }


}