package com.optic.projectofinal.UI.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ViewPagerProfileDetails;
import com.optic.projectofinal.databinding.ActivityProfileDetailBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Overla;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

public class ProfileDetailsActivity extends AppCompatActivity {


    private static final String TAG = "own";
    private Overla overlay;
    private ViewPagerProfileDetails adapterPager;
    private ActivityProfileDetailBinding binding;
    private UserDatabaseProvider userDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private String idUserToSee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overlay = new Overla(this);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);
        //get intetn
        idUserToSee=getIntent().getStringExtra("idUserToSee");
        //insntance
        userDatabaseProvider=new UserDatabaseProvider();
        authenticationProvider=new AuthenticationProvider();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        binding.tabs.setBackgroundColor(Color.RED);

        binding.contentProfileDetail.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //View vista=adapterPager.getView(position);
//                    int w=View.MeasureSpec.makeMeasureSpec(vista.getWidth(), View.MeasureSpec.EXACTLY);
//                    int h=View.MeasureSpec.makeMeasureSpec(vista.getHeight(), View.MeasureSpec.UNSPECIFIED);
                //viewPager.measure(w,h);
                //  Toast.makeText(ScrollingActivity.this, "hola "+vista.getHeight()+"/", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                //viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

        });
        adapterPager = new ViewPagerProfileDetails(this);
        binding.contentProfileDetail.viewPager.setAdapter(adapterPager);


        new TabLayoutMediator(binding.tabs, binding.contentProfileDetail.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText("Habilidades");
                            BadgeDrawable bg = tab.getOrCreateBadge();
                            bg.setBackgroundColor(Color.RED);
                            bg.setVisible(true);
                            bg.setNumber(13);
                        } else if (position == 1) {
                            tab.setText("Opiniones");
                        } else {
                            tab.setText("Subastas");
                        }
                    }
                }).attach();

        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        getSupportActionBar().setTitle("pruena");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarLayout.setExpandedTitleColor(Color.TRANSPARENT);

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
                    toolBarLayout.setTitle("Title");
                    isShow = true;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.teal_200));
                } else if (isShow) {
                    toolBarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.WHITE);
                }
            }
        });

        binding.contentAppBar.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView iv = (ImageView) view;
                new StfalconImageViewer.Builder<String>(ProfileDetailsActivity.this, new String[]{iv.getTag().toString()}, new ImageLoader<String>() {
                    @Override
                    public void loadImage(ImageView imageView, String image) {
                        Picasso.get().load(R.drawable.prueba).into(imageView);
                    }
                }).withOverlayView(overlay).withHiddenStatusBar(false).show();
            }
        });

        loadUserData();

    }

    private void loadUserData() {
        userDatabaseProvider.getUser(idUserToSee).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    User mUser=documentSnapshot.toObject(User.class);
                    binding.contentAppBar.nameUser.setText(mUser.getName());
                    binding.contentAppBar.lastNameUser.setText(mUser.getLastName());
                    binding.contentAppBar.phoneNumber.setText(String.valueOf(mUser.getPhoneNumber()));
                    binding.contentAppBar.about.setText(mUser.getAbout());
                    if(mUser.isVerified()){
                        binding.contentAppBar.verified.setVisibility(View.VISIBLE);
                    }
                    binding.contentAppBar.imageProfileStatus.setImageResource(mUser.isOnline()?R.color.teal_200:R.color.black);
                    Glide.with(ProfileDetailsActivity.this).load("https://www.trecebits.com/wp-content/uploads/2019/02/Persona-1-445x445.jpg").centerCrop().into(binding.contentAppBar.imageProfile);
                    Glide.with(ProfileDetailsActivity.this).load(mUser.getCoverPageImage()).centerCrop().into(binding.contentAppBar.coverPageImage);


                }else{
                    Log.e(TAG, "ProfileDetailsActivity loadUserData onSuccess: " );
                }
            }
        }).addOnFailureListener(v-> Log.e(TAG, "ProfileDetailsActivity->addOnFailureListener ->loadUserData: " ));
    }


}