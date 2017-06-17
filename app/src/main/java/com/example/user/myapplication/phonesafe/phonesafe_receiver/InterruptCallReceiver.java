package com.example.user.myapplication.phonesafe.phonesafe_receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.user.myapplication.phonesafe.phonesafe_database.BlackNumberOperator;

import java.lang.reflect.Method;

public class InterruptCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumMode = sp.getBoolean("BlackNumStatus", true);
        if (!BlackNumMode) {
            //黑名单拦截关闭
            return;
        }
        BlackNumberOperator operator = new BlackNumberOperator(context);
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String mIncomingNum = "";
            //如果是来电
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (manager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNum = intent.getStringExtra("incoming_number");
                    int blackContactMode = operator.getBlackContactMode(mIncomingNum);
                    if (blackContactMode == 1 || blackContactMode == 3) {
                        //观察（另外一个应用程序数据库的变化）呼叫记录的变化
                        //如果呼叫记录生成了，则把呼叫记录删除
                        Uri uri = Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver(uri, true, new CallHistoryObserver(new Handler(), mIncomingNum, context));
                    }
                    break;
            }
        }
    }

    /**
     * 通过内容观察者观察数据库变化
     */
    private class CallHistoryObserver extends ContentObserver {
        private String incomingNum;
        private Context context;

        public CallHistoryObserver(Handler handler, String incomingNum, Context context) {
            super(handler);
            this.context = context;
            this.incomingNum = incomingNum;
        }

        //观察到数据库内容发生变化时，调用的方法
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.i("CallHistoryObserver", "呼叫记录数据库的内容发生了变化");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallHistory(incomingNum, context);
        }
    }

    /**
     * 清楚呼叫记录
     * @param incomingNum
     * @param context
     */
    public void deleteCallHistory(String incomingNum, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, "number=?", new String[]{incomingNum}, "_id desc limit 1");
        if (cursor.moveToNext()) {
            String id = cursor.getString(0);
            resolver.delete(uri, "_id=?", new String[]{id});
        }
    }
    /**
     * 挂断电话，需要复制两个AIDL
     */
    public void endCall(Context context){
        try {
            Class clazz = context.getClassLoader().loadClass(
                    "android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony itelephony = ITelephony.Stub.asInterface(iBinder);
            itelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}