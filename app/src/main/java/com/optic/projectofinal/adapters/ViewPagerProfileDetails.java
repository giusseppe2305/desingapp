package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails.AuctionFragment;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails.OpinionsFragment;

public class ViewPagerProfileDetails extends FragmentStateAdapter {
    private AuctionFragment segundo;
    private OpinionsFragment  primero;

    private String idUser;

    public ViewPagerProfileDetails(@NonNull FragmentActivity fragmentActivity, String idUserToSee) {

        super(fragmentActivity);
        this.idUser = idUserToSee;
        segundo = new AuctionFragment().newInstance(idUser);
        primero = OpinionsFragment.newInstance(idUser);

    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return primero;
        return segundo;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}