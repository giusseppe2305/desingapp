package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions.AuctionsParticipeFragment;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.auctions.MyAuctionsFragment;

public class ViewPagerAuctions extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 1;
    private final AuctionsParticipeFragment auctionsParticipe;
    private final MyAuctionsFragment myAuctions;

    public ViewPagerAuctions(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
        myAuctions = new MyAuctionsFragment();
        auctionsParticipe = new AuctionsParticipeFragment();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //if (position == 0)
            return myAuctions;

        //return auctionsParticipe;

    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }


}