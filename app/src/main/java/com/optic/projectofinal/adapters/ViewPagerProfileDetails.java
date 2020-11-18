package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.activites.fragments.tabsFragments.profileDetails.AuctionFragment;
import com.optic.projectofinal.activites.fragments.tabsFragments.profileDetails.OpinionsFragment;
import com.optic.projectofinal.activites.fragments.tabsFragments.profileDetails.SkillsFragmentTab;

public class ViewPagerProfileDetails extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;
    private  AuctionFragment tercero;

    public ViewPagerProfileDetails(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
        primero=SkillsFragmentTab.newInstance();
        tercero=new AuctionFragment().newInstance();
        segundo= OpinionsFragment.newInstance();
    }

    OpinionsFragment  segundo;
    SkillsFragmentTab primero;
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return primero;
        if (position == 1)
            return segundo;

        return tercero;
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}