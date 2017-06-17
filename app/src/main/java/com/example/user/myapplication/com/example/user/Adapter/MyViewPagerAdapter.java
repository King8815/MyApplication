package com.example.user.myapplication.com.example.user.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by user on 2016/6/11.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    //	定义界面的列表
    private ArrayList<View> views;

    public MyViewPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

     /* (non-Javadoc)
     * @
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView(views.get(position));
    }

    /* (non-Javadoc)
     * @
     * 获得当前的要显示的页面数量
     */
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
