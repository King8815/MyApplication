package com.example.slide_table.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by user on 2016/6/7.
 */
public class MyDialog extends Dialog{
    // 这种适合通用
    public MyDialog(Context context) {
        super(context);
    }
    // 自定义提醒框样式
    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

