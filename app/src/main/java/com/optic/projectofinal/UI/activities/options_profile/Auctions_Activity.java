package com.optic.projectofinal.UI.activities.options_profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ViewPagerAuctions;
import com.optic.projectofinal.databinding.ActivityAuctionsBinding;

public class Auctions_Activity extends AppCompatActivity {

    private ViewPagerAuctions adapterPager;
    private ActivityAuctionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAuctionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(R.string.auctions_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        adapterPager=  new ViewPagerAuctions(this);
        binding.viewPagerAuctions.setAdapter(adapterPager);

        new TabLayoutMediator(binding.tabLayoutAuctions, binding.viewPagerAuctions,
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