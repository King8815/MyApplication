package com.example.user.myapplication.com.example.user.Adapter;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by user on 2016/6/21.
 */
/*定义特殊的广播接受者*/
public class MyDeviceAdminReceiver extends DeviceAdminReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
