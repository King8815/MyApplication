package com.example.slide_table.slider_frame.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.utils.Tools;
import com.example.slide_table.R;
import com.example.slide_table.activity.RegisterActivity;
import com.example.slide_table.utils.ChangeLanguageEvent;

import org.w3c.dom.Text;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * Created by mYth on 2016/8/5.

/**
 * 增加handler及thread
 */
public class SecondFragment extends Fragment {
    //Mob短信验证
    private static final String appkey = "1679b77c0bbda";
    private static final String appsecret = "4b6dc76cc3991a21a608947ac51299f4";
    private EventHandler eventHandler;//操作回调

    private Activity mActivity;
    //View控件
    private Button btn_get_code;
    private Button btn_next;
    private EditText et_phone;
    private EditText et_verification_code;
    private TextView validate_in_seconds;
    //手机号码
    private String user_phone;
    //验证码
    private String code;
    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60s
    private int i = 60;
    private static final String TAG = "SMS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.second_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
        addListener();
    }

    private void findViews(View view) {
        btn_get_code = (Button)view.findViewById(R.id.btn_get_code);
        btn_next = (Button)view.findViewById(R.id.btn_next);
        et_phone = (EditText)view.findViewById(R.id.et_phone);
        et_verification_code = (EditText)view.findViewById(R.id.et_verification_code);
        validate_in_seconds = (TextView)view.findViewById(R.id.validate_in_seconds);
    }

    private void init() {
        //Mob短信验证
        SMSSDK.initSDK(mActivity, appkey, appsecret);
        eventHandler = new EventHandler(){
            /**
             * 在操作结束时被触发
             * @param event 表示操作的类型
             * @param result 表示操作的结果
             * @param data 事件操作结果，其具体取值根据参数result而定
             * 由于EventHandler开启了线程，所以不能直接在afterEvent中更新UI,需要新建一个handle处理EventHandler发送过来的消息
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.e(TAG,"result = " + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 回调完成
                    switch (event) {
                        // 验证码获取
                        case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                            Message msg = new Message();
                            //获取验证码成功
                            msg.arg1 = 0;
                            msg.obj = getResources().getString(R.string.get_ver_code_ok);
                            handler.sendMessage(msg);
                            break;
                        // 验证码验证
                        case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                            Message msg1 = new Message();
                            msg1.arg1 = 1;
                            msg1.obj = getResources().getString(R.string.ver_ok);
                            handler.sendMessage(msg1);
                            break;
                    }
                } else {
                    Message msg2 = new Message();
                    msg2.arg1 = 2;
                    msg2.obj = getResources().getString(R.string.re_ver);
                    handler.sendMessage(msg2);
                    ((Throwable) data).printStackTrace();
                }
            }
        };

        SMSSDK.registerEventHandler(eventHandler);//注册短信回调
        //注册EventBus
//        EventBus.getDefault().register(this);
    }

    private void addListener() {
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePhone();//手机号码验证
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCode();//验证码验证
            }
        });
    }

    /**
     * handler 处理方法
     */
    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.arg1)
            {
                case 0:
                    //获取验证码成功
                    Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //客户端验证成功，可以进行注册、修改密码、登陆等操作
                    Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    et_verification_code.setText("");
                    Intent intent = new Intent(mActivity, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 验证输入的手机号
     */
    private void validatePhone() {
        user_phone = et_phone.getText().toString();
        Log.i(TAG, "user_phone = " + user_phone);

        if (!Tools.validatePhone(user_phone)){
            Toast.makeText(mActivity, getResources().getString(R.string.null_phone_num), Toast.LENGTH_SHORT).show();
//            Tools.toast(mActivity,"手机号为空");
        }else {
            //填写了手机号
            if(Tools.validatePhone(user_phone)){
                //号码ok,获取验证码
                SMSSDK.getVerificationCode("86", user_phone);
                //让按钮的样式变成60秒倒计时
                changeBtnStyle();
            }else{
                Toast.makeText(mActivity, getResources().getString(R.string.input_correct_num), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 验证获取的验证码
     */
    private void validateCode() {
        //验证操作
        code = et_verification_code.getText().toString();
        if (Tools.isNull(code)){
            Toast.makeText(mActivity, getResources().getString(R.string.ver_not_null), Toast.LENGTH_SHORT).show();
        } else {
            //填写了验证码，进行验证
            SMSSDK.submitVerificationCode("86", user_phone, code);
        }
    }

    /**
     * Toast 功能简写
     * @param
     */
   /* private void Toast(final String str) {
        Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
    }*/

    /*//开启线程一般可以用于过多的加载数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
        }
    };*/
    @Subscribe
    public void onEvent(ChangeLanguageEvent event){
        refreshView();//界面中英文显示切换
    }
    /**
     * 改变按钮样式，变成60秒倒计时
     */
    private void changeBtnStyle() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (mActivity == null) {
                            break;
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_get_code.setText(getResources().getString(R.string.get_ver) + "(" + i + ")");
                                btn_get_code.setClickable(false);//设置60秒内不可点击
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_get_code.setText(getResources().getString(R.string.get_ver));
                            btn_get_code.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        //注销EventBus
//        EventBus.getDefault().unregister(this);
    }

    private void refreshView(){
        et_phone.setText(getResources().getString(R.string.input_num));
        et_verification_code.setText(getResources().getString(R.string.input_ver));
        btn_get_code.setText(getResources().getString(R.string.get_ver));
        validate_in_seconds.setText(getResources().getString(R.string.ver_resent));
        btn_next.setText(getResources().getString(R.string.next));
    }
}
