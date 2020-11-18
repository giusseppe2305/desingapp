package com.optic.projectofinal.activites.options_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ViewPagerAuctions;
import com.optic.projectofinal.adapters.ViewPagerProfileDetails;

public class Auctions_Activity extends AppCompatActivity {

    private ViewPagerAuctions adapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.TOOLBAR);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subastas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout_auctions);
        ViewPager2 viewPager = findViewById(R.id.view_pager_auctions);
        adapterPager=  new ViewPagerAuctions(this);
        viewPager.setAdapter(adapterPager);


        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText("Mis subastas");

                        }else if (position == 1){
                            tab.setText("Subastas que participo");
                        }
                    }
                }).attach();
    }
}