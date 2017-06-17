package com.example.mylibrary.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by mYth on 2016/8/11.
 * Toast提示管理类
 */
public class ToastMgr{
    private static Toast toast;
    private ToastMgr(){

    }
    /**
     * 程序初始化的时候调用，只需要调用一次
     */
    public static void init(Context context){
        View view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        init(context, view);
    }
    /**
     * 程序初始化的时候调用，只需要调用一次
     */
    public static void init(Context context, View view){
        toast = new Toast(context);
        toast.setView(view);
    }
    public static void show(CharSequence text, int duration){
        if (toast == null){
            Log.e("ToastMgr", "ToastMgr is not initialized, please call init once before you call this method");
            return;
        }
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }
    public static void show(int resid, int duration){
        if (toast == null){
            Log.e("ToastMgr", "ToastMgr is not initialized, please call init once before you call this method");
            return;
        }
        toast.setText(resid);
        toast.setDuration(duration);
        toast.show();
    }
    public static void show(CharSequence text){
        show(text, Toast.LENGTH_SHORT);
    }
    public static void show(int resid){
        show(resid, Toast.LENGTH_SHORT);
    }
}
