package com.optic.projectofinal.UI.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ResourcesAdapter;
import com.optic.projectofinal.adapters.ViewPagerProfileDetails;
import com.optic.projectofinal.adapters.ViewPagerProfileDetailsWorker;
import com.optic.projectofinal.databinding.ActivityProfileDetailBinding;
import com.optic.projectofinal.models.Job;
import com.optic.projectofinal.models.Resource;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.JobsDatabaseProvider;
import com.optic.projectofinal.providers.LikeWorkersDatabaseProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.PermissionCall;
import com.optic.projectofinal.utils.Utils;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;

public class ProfileDetailsActivity extends AppCompatActivity {

    private LikeWorkersDatabaseProvider likeProvider;
    private static final String TAG = "own";
    private FragmentStateAdapter adapterPager;
    private ActivityProfileDetailBinding binding;
    private UserDatabaseProvider userDatabaseProvider;
    private JobsDatabaseProvider jobsDatabaseProvider;
    private AuthenticationProvider authenticationProvider;
    private String idUserToSee;
    private PermissionCall permissionCall;
    private ArrayList<Resource> listResources;
    private ResourcesAdapter adapterResource;
    private boolean likeWorker;
    private String imageProfileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ///

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        getSupportActionBar().setTitle("pruena");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        authenticationProvider = new AuthenticationProvider();
        //get intetn
        if (getIntent().getStringExtra("idUserToSee") != null) {
            idUserToSee = getIntent().getStringExtra("idUserToSee");
        } else {
            idUserToSee = authenticationProvider.getIdCurrentUser();
        }
        //insntance
        jobsDatabaseProvider=new JobsDatabaseProvider();
        userDatabaseProvider = new UserDatabaseProvider();
        likeProvider = new LikeWorkersDatabaseProvider(authenticationProvider.getIdCurrentUser());
        permissionCall=new PermissionCall(this);


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

        binding.contentAppBar.openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileDetailsActivity.this, ChatConversationActivity.class);
                i.putExtra("idUserToChat", idUserToSee);
                startActivity(i);
            }
        });
        //////
//        adapterPager = new ViewPagerProfileDetailsWorker(this, idUserToSee);
//        binding.contentProfileDetail.viewPager.setAdapter(adapterPager);
//
//        new TabLayoutMediator(binding.tabs, binding.contentProfileDetail.viewPager,
//                new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        if (position == 0) {
//                            tab.setText("Habilidades");
//                            BadgeDrawable bg = tab.getOrCreateBadge();
//                            bg.setBackgroundColor(Color.RED);
//                            bg.setVisible(true);
//                            bg.setNumber(13);
//                        } else if (position == 1) {
//                            tab.setText("Opiniones");
//                        } else {
//                            tab.setText("Subastas");
//                        }
//                    }
//                }).attach();

        loadUserData();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         permissionCall.executeOnRequestPermission(requestCode,permissions,grantResults);
    }

    private void loadUserData() {
        userDatabaseProvider.getUser(idUserToSee).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User mUser = documentSnapshot.toObject(User.class);
                    //ling viewpager to tabs
                    getIntermediaryViewPager(mUser.isProfessional());
                    ///set rating value
                    jobsDatabaseProvider.getValuationsFromUser(idUserToSee).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            float totalAmountValuation=0;
                            for(DocumentSnapshot it:queryDocumentSnapshots.getDocuments()){
                                totalAmountValuation+=it.toObject(Job.class).getValuation().getAverageTotal();
                            }
                            if(totalAmountValuation>0){
                                binding.contentAppBar.numOpinions.setText("("+queryDocumentSnapshots.getDocuments().size()+")");
                                binding.contentAppBar.valuated.setRating(totalAmountValuation/queryDocumentSnapshots.getDocuments().size());
                            }
                        }
                    }).addOnFailureListener(v-> Log.e(TAG, "loadUserData onSuccess: addOnFailureListener" ));


                    binding.contentAppBar.nameUser.setText(mUser.getName());
                    binding.contentAppBar.lastNameUser.setText(mUser.getLastName());

                    if(mUser.getPhoneNumber()!=0&&String.valueOf(mUser.getPhoneNumber()).length()>0){
                        binding.contentAppBar.btnCall.setOnClickListener(v->{
                            permissionCall.call(String.valueOf(mUser.getPhoneNumber()));

                        });
                        binding.contentAppBar.phoneNumber.setText(String.valueOf(mUser.getPhoneNumber()));
                    }else{
                        binding.contentAppBar.btnCall.setVisibility(View.GONE);
                        binding.contentAppBar.phoneNumber.setVisibility(View.GONE);
                    }


                    binding.contentAppBar.about.setText(mUser.getAbout());
                    if (mUser.isVerified()) {
                        binding.contentAppBar.verified.setVisibility(View.VISIBLE);
                    }
                    binding.contentAppBar.imageProfileStatus.setImageResource(mUser.isOnline() ? R.color.teal_200 : R.color.black);
                    Glide.with(ProfileDetailsActivity.this).load(mUser.getProfileImage()).apply(Utils.getOptionsGlide(true)).into(binding.contentAppBar.imageProfile);
                    imageProfileUser=mUser.getProfileImage();
                    Glide.with(ProfileDetailsActivity.this).load(mUser.getCoverPageImage()).apply(Utils.getOptionsGlide(true)).into(binding.contentAppBar.coverPageImage);
                    ///gallery
                    if (mUser.getProfileImage() != null) {
                        binding.contentAppBar.imageProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ImageView iv = (ImageView) view;
                                new StfalconImageViewer.Builder<String>(ProfileDetailsActivity.this, new String[]{iv.getTag().toString()}, new ImageLoader<String>() {
                                    @Override
                                    public void loadImage(ImageView imageView, String image) {
                                        Glide.with(ProfileDetailsActivity.this).load(mUser.getProfileImage()).apply(Utils.getOptionsGlide(true)).into(imageView);
                                    }
                                }).withHiddenStatusBar(false).show();
                            }
                        });
                    }
                    if (mUser.getCoverPageImage() != null) {
                        binding.contentAppBar.coverPageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ImageView iv = (ImageView) view;
                                new StfalconImageViewer.Builder<String>(ProfileDetailsActivity.this, new String[]{iv.getTag().toString()}, new ImageLoader<String>() {
                                    @Override
                                    public void loadImage(ImageView imageView, String image) {
                                        Glide.with(ProfileDetailsActivity.this).load(mUser.getCoverPageImage()).apply(Utils.getOptionsGlide(true)).into(imageView);
                                    }
                                }).withHiddenStatusBar(false).show();
                            }
                        });
                    }


                    //resources
                    if (mUser.getResources() != null && mUser.getResources().size() > 0) {
                        listResources = Utils.createListResourcesByIds(ProfileDetailsActivity.this, mUser.getResources());
                        adapterResource = new ResourcesAdapter(ProfileDetailsActivity.this, listResources);
                        binding.contentAppBar.listResources.setLayoutManager(new LinearLayoutManager(ProfileDetailsActivity.this, RecyclerView.HORIZONTAL, false));
                        binding.contentAppBar.listResources.setAdapter(adapterResource);
                    }

                } else {
                    Log.e(TAG, "ProfileDetailsActivity loadUserData onSuccess: ");
                }
            }
        }).addOnFailureListener(v -> Log.e(TAG, "ProfileDetailsActivity->addOnFailureListener ->loadUserData: "));
    }

    public void getIntermediaryViewPager(boolean isWorker) {
        if (isWorker) {
            adapterPager = new ViewPagerProfileDetailsWorker(this, idUserToSee);
            binding.contentProfileDetail.viewPager.setAdapter(adapterPager);

            new TabLayoutMediator(binding.tabs, binding.contentProfileDetail.viewPager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            if (position == 0) {
                                tab.setText("Habilidades");
//                                BadgeDrawable bg = tab.getOrCreateBadge();
//                                bg.setBackgroundColor(Color.RED);
//                                bg.setVisible(true);
//                                bg.setNumber(13);
                            } else if (position == 1) {
                                tab.setText("Opiniones");
                            } else {
                                tab.setText("Subastas");
                            }
                        }
                    }).attach();
        } else {
            adapterPager = new ViewPagerProfileDetails(this, idUserToSee);
            binding.contentProfileDetail.viewPager.setAdapter(adapterPager);
            new TabLayoutMediator(binding.tabs, binding.contentProfileDetail.viewPager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            if (position == 0) {
                                tab.setText("Opiniones");
                            } else if (position == 1) {
                                tab.setText("Subastas");
                            }
                        }
                    }).attach();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_detail, menu);
        if(idUserToSee==authenticationProvider.getIdCurrentUser()){
            menu.findItem(R.id.btnMenuFavourite).setVisible(false);
            binding.contentAppBar.containerCallChat.setVisibility(View.GONE);
        }else{
            likeProvider.chekIfExistLikeToWorker(idUserToSee).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.getDocuments().size()>0){
                        //exist like
                        Utils.changeTintIconToolbar(menu.findItem(R.id.btnMenuFavourite),getResources().getColor(R.color.checkMessage));
                        likeWorker=true;
                    }else
                    {
                        Utils.changeTintIconToolbar(menu.findItem(R.id.btnMenuFavourite),getResources().getColor(R.color.grey_200));
                        //not exist like
                        likeWorker=false;
                    }

                    menu.findItem(R.id.btnMenuFavourite).setEnabled(true);
                }
            }).addOnFailureListener(v-> Log.e(TAG, "onCreateOptionsMenu: "+v.getMessage() ));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnMenuShare:
                    share();
                    return true;

            case R.id.btnMenuFavourite:
                likeWorker(item);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void likeWorker(MenuItem item) {
        likeWorker=likeWorker;
        if(likeWorker){
            Utils.changeTintIconToolbar(item,getResources().getColor(R.color.checkMessage));

        }else{
            Utils.changeTintIconToolbar(item,getResources().getColor(R.color.grey_200));

        }
        likeProvider.doLike(idUserToSee);
    }

    private void share() {
        Utils.generateDynamicLink(this,idUserToSee,"Conoces a: "+binding.contentAppBar.nameUser.getText().toString(),"description ",
               imageProfileUser!=null?imageProfileUser:"https://static.thenounproject.com/png/17241-200.png","Comparte a este trabajador");
    }
}