package com.example.slide_table.slider_frame.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wzl on 2016/8/5.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragmentsList;
    private boolean backState = false;

   public MyFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    public MyFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentsList){
        super(fragmentManager);
        this.fragmentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public boolean isBackState() {
        return backState;
    }

    public void setBackState(boolean backState) {
        this.backState = backState;
    }
}
