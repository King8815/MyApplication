package com.example.mylibrary.common_view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylibrary.R;

/**
 * Created by user on 2016/8/24.
 * 顶部导航栏，可作为公共类
 */
public class NavigationBar extends FrameLayout {
    private Context context;
    private ImageView imageView;
    private TextView textView;

    public NavigationBar(Context context) {
        super(context);
        this.context = context;
        initBar();
    }
    public NavigationBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context = context;
        initBar();
    }
    public NavigationBar(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
        this.context = context;
        initBar();
    }
    /**
     * 初始化navbar
     */
    private void initBar(){
        LayoutInflater.from(context).inflate(R.layout.navbar,this);
    }
    /**
     * 默认返回监听
     */
    private OnClickListener defaltListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            ((Activity) context).finish();
        }
    };

    private void setListener(){

    }
}
