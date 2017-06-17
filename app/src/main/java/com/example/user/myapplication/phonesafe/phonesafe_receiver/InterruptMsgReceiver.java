package com.example.user.myapplication.phonesafe.phonesafe_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import com.example.user.myapplication.phonesafe.phonesafe_database.BlackNumberOperator;

/**
 * Created by user on 2016/7/19.
 * author: Brain
 * description:在广播中获取到手机号与黑名单中数据进行校验，根据黑名单中拦截模式进行拦截
 */
public class InterruptMsgReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumMode = sp.getBoolean("BlackNumStatus", true);
        if (!BlackNumMode){
            //黑名单拦截关闭
            return;
        }
        //如果是黑名单中的数据，则终止广播
        BlackNumberOperator operator = new BlackNumberOperator(context);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object object: objects) {
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
            String senderNum = msg.getOriginatingAddress();//获得发送者的电话号码
            String body = msg.getMessageBody();
            if (senderNum.startsWith("+86")){
                senderNum = senderNum.substring(3, senderNum.length());//将发送者的电话号码进行剪切
            }
            int mode = operator.getBlackContactMode(senderNum);
            if (mode == 2 || mode == 3){
                //需要拦截短信，拦截广播
                abortBroadcast();
            }
        }
    }
}
