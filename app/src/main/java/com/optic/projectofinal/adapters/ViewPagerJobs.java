package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.activites.fragments.tabsFragments.jobs.JobsDoneFragment;
import com.optic.projectofinal.activites.fragments.tabsFragments.jobs.MyJobsFragment;

public class ViewPagerJobs extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    private final MyJobsFragment myJobs;
    private final JobsDoneFragment myJobsDone;


    public ViewPagerJobs(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
        myJobsDone=new JobsDoneFragment();
        myJobs=new MyJobsFragment();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return myJobs;

        return myJobsDone;
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }


}