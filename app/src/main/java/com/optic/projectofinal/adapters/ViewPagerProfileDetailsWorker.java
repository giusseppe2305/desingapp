package com.optic.projectofinal.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails.AuctionFragment;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails.OpinionsFragment;
import com.optic.projectofinal.UI.activities.fragments.tabsFragments.profileDetails.SkillsFragmentTab;

public class ViewPagerProfileDetailsWorker extends FragmentStateAdapter {
    private AuctionFragment tercero;
    private String idUser;

    public ViewPagerProfileDetailsWorker(@NonNull FragmentActivity fragmentActivity, String idUserToSee) {

        super(fragmentActivity);
        this.idUser = idUserToSee;
        primero = SkillsFragmentTab.newInstance(idUser);
        tercero = new AuctionFragment().newInstance(idUser);
        segundo = OpinionsFragment.newInstance(idUser);

    }

    OpinionsFragment segundo;
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