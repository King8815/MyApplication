package com.example.mylibrary.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by user on 2016/8/12.
 */
public class MyDialog extends Dialog{
    private Context context;

    public MyDialog(Context context) {
        super(context);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }
}
