package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.fragments.tabsFragments.BlankFragment;
import com.optic.projectofinal.fragments.tabsFragments.tabFragment;

public class viewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;

    public viewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull @Override public Fragment createFragment(int position) {
        if(position==0)
        return new tabFragment().newInstance(position);
        if(position==1)
            return new BlankFragment().newInstance();

        return new tabFragment().newInstance(position);
    }

    @Override public int getItemCount() {
        return 3;
    }
}