package com.optic.projectofinal.UI.activities.options_profile;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.optic.projectofinal.R;
import com.optic.projectofinal.adapters.ViewPagerJobs;

public class JobsActivity extends AppCompatActivity {


    private ViewPagerJobs adapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);


        Toolbar toolbar = (Toolbar) findViewById(R.id.ownToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.jobs_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout_jobs);
        ViewPager2 viewPager = findViewById(R.id.view_pager_jobs);
        adapterPager=  new ViewPagerJobs(this);
        viewPager.setAdapter(adapterPager);


        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText(R.string.jobs_activity_jobs_received);

                        }
                        else if (position == 1){
                            tab.setText(R.string.jobs_activity_jobs_done);
                        }
                    }
                }).attach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}