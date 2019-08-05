package com.appproteam.sangha.bitdimo.View.Adapter;
;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter  extends FragmentPagerAdapter {
    ArrayList<Fragment> listFragment = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {


        return listFragment.get(position);

    }
    public void addFragment(Fragment mFragment)
    {
        listFragment.add(mFragment);
    }
    @Override
    public int getCount() {
        return listFragment.size();
    }
}
