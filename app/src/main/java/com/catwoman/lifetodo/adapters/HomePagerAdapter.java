package com.catwoman.lifetodo.adapters;

/**
 * Created by annt on 4/10/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.catwoman.lifetodo.fragments.CategoriesFragment;
import com.catwoman.lifetodo.fragments.PlansFragment;

import java.util.ArrayList;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = { "Plans", "Categories" };
    private ArrayList<Fragment> fragments;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>();
        fragments.add(new PlansFragment());
        fragments.add(new CategoriesFragment());
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}