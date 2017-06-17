package com.example.slide_table.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mylibrary.utils.ToastMgr;
import com.example.slide_table.R;
import com.example.slide_table.slider_frame.adapter.MyFragmentPagerAdapter;
import com.example.slide_table.slider_frame.fragment.FifthFragment;
import com.example.slide_table.slider_frame.fragment.FirstFragment;
import com.example.slide_table.slider_frame.fragment.FourthFragment;
import com.example.slide_table.slider_frame.fragment.SecondFragment;
import com.example.slide_table.slider_frame.fragment.ThirdFragment;
import com.example.slide_table.utils.ChangeLanguageEvent;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends FragmentActivity {
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ViewPager mPager;
    private RadioGroup mRadioGroup;
    private MyFragmentPagerAdapter adapter;
    private RadioButton one_rb, two_rb, three_rb,four_rb, five_rb;
//    public boolean backState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initView();
        addListener();
    }
    private void findViews(){
        mPager = (ViewPager)findViewById(R.id.viewpager);
        mRadioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        one_rb = (RadioButton)findViewById(R.id.one_rb);
        two_rb = (RadioButton)findViewById(R.id.two_rb);
        three_rb = (RadioButton)findViewById(R.id.three_rb);
        four_rb = (RadioButton)findViewById(R.id.four_rb);
        five_rb = (RadioButton)findViewById(R.id.five_rb);
    }
    //初始化控件
    private void initView() {
        //向list中加入fragment
        mFragmentList.add(new FirstFragment());
        mFragmentList.add(new SecondFragment());
        mFragmentList.add(new ThirdFragment());
        mFragmentList.add(new FourthFragment());
        mFragmentList.add(new FifthFragment());

        //将list放入适配器
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(adapter);
        //默认选择第一个fragment
        mRadioGroup.check(R.id.one_rb);
        //注册EventBus
        EventBus.getDefault().register(this);

    }

    private void addListener() {
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.i("wzl", "position is " + position);
                switch(position)
                {
                    // position = 0
                    case 0:
                        mRadioGroup.check(R.id.one_rb);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.two_rb);
                        break;
                    case 2:
                        mRadioGroup.check(R.id.three_rb);
                        break;
                    case 3:
                        mRadioGroup.check(R.id.four_rb);
                        break;
                    case 4:
                        mRadioGroup.check(R.id.five_rb);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("wzl", "checked is " + checkedId);
                switch(checkedId)
                {
                    /**
                     * checkedId = 0
                     * setCurrentItem 第二个参数代表当前界面是否滑动
                     * */
                    case R.id.one_rb:
                        mPager.setCurrentItem(0, false);
                        break;
                    // checkedId = 1
                    case R.id.two_rb:
                        mPager.setCurrentItem(1, false);
                        break;
                    // checkedId = 2
                    case R.id.three_rb:
                        mPager.setCurrentItem(2, false);
                        break;
                    // checkedId = 3
                    case R.id.four_rb:
                        mPager.setCurrentItem(3, false);
                        break;
                    // checkedId = 4
                    case R.id.five_rb:
                        mPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        if (adapter.isBackState()){
//            mRadioGroup.check(R.id.four_rb);
//        }else {
//            mRadioGroup.check(R.id.one_rb);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点击其他地方隐藏键盘
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

   /* @Subscribe
    public void  onEventMainThread(ChangeLanguageEvent event){
//		String language=getResources().getConfiguration().locale.getCountry();
//		ToastMgr.show("language:"+language);
        refreshView();
    }*/
    @Subscribe
    public void onEvent(ChangeLanguageEvent event){
        String language = getResources().getConfiguration().locale.getCountry();
		Log.i("wzl", "language = " + language);
        refreshView();
    }

    /**
     * 接收到切换语言的指令，update当前语言
     */
    private void refreshView(){
        one_rb.setText(getString(R.string.menu));
        two_rb.setText(getString(R.string.weibo));
        three_rb.setText(getString(R.string.comment));
        four_rb.setText(getString(R.string.person));
        five_rb.setText(getString(R.string.global));
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1:
                Log.i("wzl","界面跳转测试");
                mRadioGroup.check(R.id.four_rb);
                break;
            default:
                break;
        }
    }*/
}
