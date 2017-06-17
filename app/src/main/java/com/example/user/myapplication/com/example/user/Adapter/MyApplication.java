package com.example.user.myapplication.com.example.user.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 2016/6/18.
 */
public class MyApplication extends Application{
    private static MyApplication instance;
    private List<Activity> mList = null;

    private MyApplication(){
        mList = new LinkedList<Activity>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        correctSIM();
    }

    /**
     * 单例模式中唯一获取的MyApplication实例
     * @return 实例化对象
     */
    public static MyApplication getInstance(){
        if (null == instance){
            instance = new MyApplication();
        }
        return instance;
    }

    /**
     * 将activity添加到MyApplication容器中
     * @param activity
     */
    public void addActivity(Activity activity){
        if (mList != null && mList.size() > 0){
            if (!mList.contains(activity)){
                mList.add(activity);
            }
        }else {
            mList.add(activity);
        }
    }

    /**
     * 遍历所有的activity，并且推出
     */
    public void exit(){
        if (mList != null && mList.size() >0){
            for (Activity activity : mList){
                activity.finish();
            }
        }
        System.exit(0);
    }
    public void correctSIM() {
//        检查SIM卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//        获取防盗保护的状态
        boolean protecting = sp.getBoolean("protecting", true);
        if (protecting) {
//            得到绑定的SIM卡号
            String bindsim = sp.getString("config", "");
//            得到手机现在的SIM卡号
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//            为了测试在手机序列号上data 模拟SIM卡被更换的情况
            String realSIM = telephonyManager.getSimSerialNumber();
            if (bindsim.equals(realSIM)) {
                Log.i("MyApplication","SIM卡未发生变化，还是您的手机");
            }else {
                Log.i("MyApplication","SIM卡变化了.");
//                由于系统版本的原因，这里的发短信可能与其他手机版本不兼容
                String safephone = sp.getString("safephone", "");
                if ("".equals(safephone)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safephone, null,"你亲友的手机SIM已经被更换，请知悉!", null, null);
                }
            }
        }
    }
}
