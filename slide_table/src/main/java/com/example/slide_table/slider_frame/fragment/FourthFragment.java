package com.example.slide_table.slider_frame.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mylibrary.utils.ToastMgr;
import com.example.slide_table.R;
import com.example.slide_table.activity.MainActivity;
import com.example.slide_table.adapter.WifiListAdapter;
import com.example.slide_table.utils.MyDialog;
import com.example.slide_table.utils.WifiAdmin;

import java.util.List;

/**
 * Created by user on 2016/8/5.
 */
public class FourthFragment extends Fragment{
    private EditText wifi_name,wifi_pwd;
    private Button connectSpeaker,wifi_list_search,wifi_connect,wifi_dialog_cancel;
    private ListView listView;
    private static final String TAG = "wifi";
    private Activity mActivity;
    private boolean isCheck = true;

    //wifi
    private WifiManager wifiManager;
    private List<ScanResult> list;
    private List<ScanResult> mList;
    private ScanResult scanResult;
    private WifiInfo wifiInfo;
    private List<WifiConfiguration> config;
    private WifiListAdapter adapter;
    private WifiAdmin wifiAdmin;
    private String[] wifi_str;
    private int wifiIndex;
    private String wifi_ssid = null;
    private String wifiPwd = null;
    private MyDialog dialog;

    //网信号变化广播
    private BroadcastReceiver receiver;
    private IntentFilter mFilter;
    //标志
    private boolean isStep1;
    private boolean isStep2;
    private boolean isStep3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mActivity = getActivity();
//        wifiManager = (WifiManager)mActivity.getSystemService(Context.WIFI_SERVICE);
        wifiAdmin = new WifiAdmin(mActivity);
        View view = inflater.inflate(R.layout.fourth_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        addListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCheck){
            Log.i(TAG,"界面包含信息---------");
            isCheck = false;
            if (wifiAdmin.wifiState() == WifiManager.WIFI_STATE_ENABLED) {
                String ssId = wifiAdmin.getSSID();
                Log.i(TAG,"当前环境wifi的名称ssId = " + ssId.substring(1,4));
                if (ssId.substring(1,4).equals("ras")) {
                    isStep1 = true;
                } else {
                    isStep1 = false;
                }
            }
        }

        //网络信号广播
        mFilter = new IntentFilter();
        //为BroadcastReceiver指定action，即要监听的消息名字
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);//wifi信号的rssi值发生变化时

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //从这里的log中可以看到是否接受到了广播 7
                System.out.println("intent = " + intent);
                handleEvent(intent);
            }
        };
        //注册网络变化广播
//        mActivity.registerReceiver(receiver,mFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void findViews(View view) {
        connectSpeaker = (Button)view.findViewById(R.id.connectSpeaker);
        wifi_list_search = (Button)view.findViewById(R.id.wifi_list_search);
        wifi_connect = (Button)view.findViewById(R.id.wifi_connect);
        wifi_name = (EditText)view.findViewById(R.id.ssid);
        wifi_pwd = (EditText)view.findViewById(R.id.pwd);
    }

    private void addListener() {
        //连接由Speaker开启的wifi名称，成功后并返回
        connectSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectWifi();
            }
        });
        //搜索当前wifi列表
        wifi_list_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWifiListDialog();
            }
        });
        //SSID 输入框的监听事件,检测是否选择当前可以使用的wifi名称
        wifi_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wifi_ssid = s.toString();
                Log.i(TAG,"wifi_name = " + wifi_ssid);
                if(wifi_ssid != null){
                    isStep2 = true;
                }else {
                    isStep2 = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //检测是否输入wifi密码
        wifi_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wifiPwd = s.toString();
                Log.i(TAG, "wifiPwd 输入密码为 = " + wifiPwd);
                if (wifiPwd.length() > 4){
                    isStep3 = true;
                }else {
                    isStep3 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        wifi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"isStep1 = " + isStep1 + "; isStep2 = " + isStep2 + "; isStep3 = " + isStep3);
                if (isStep1 && isStep2 && isStep3){
                        //showDownLoadDialog();
                        handler.sendEmptyMessage(0x03);
                }else if (!isStep1) {
                    Toast("请先连接到RPI");
                } else if (!isStep2) {
                    Toast("请输入WIFI名称");
                } else if (!isStep3) {
                    Toast("请输入WIFI密码");
                }
            }
        });
    }
    private void connectWifi() {
		/*
		 * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
		 */
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
        isCheck = true;
    }

    private void showWifiListDialog(){
        dialog = new MyDialog(mActivity, R.style.wifi_dialog);
       // wifiAdmin = new WifiAdmin(mActivity);
        LinearLayout linearLayout = (LinearLayout)mActivity.getLayoutInflater().inflate(R.layout.wifi_dialog, null);
        dialog.setContentView(linearLayout);
        wifi_dialog_cancel = (Button)dialog.findViewById(R.id.wifi_dialog_cancel);
        listView = (ListView)dialog.findViewById(R.id.list_wifi);

        if (wifiAdmin != null){
            if (wifiAdmin.isWifiEnabled()){
                Log.i(TAG,"当前连接的WI-FI名称为：" + wifiAdmin.getSSID());//打印出的结果为：“ABC”,带有双引号“”
                //每次加载前清空list和adapter
                if (list != null){
                    Log.i(TAG,"clear------");
                    list.clear();
                    Log.i(TAG,"clear清理后，list大小 = " + list.size());
                }

                list = wifiAdmin.getWifiList();
                Log.i(TAG, "list大小 = " + list.size() + "\n;list内容：" + list);
                /**
                 * 加入for循环是为了将手机已经连接的wifi SSID移出list，同时例当前手机连接SSID：A，搜索到wifi列表为：A,B,C,D
                 * ialog对话框重新搜索wifi列表只显示B，C，D
                 * 使用i = list.size()-1，而非使用for (int i = 0; i <= list.size()-1; i++)是为了防止数组越界
                 */
                for(int i = 0; i < list.size()-1; i++) {//外循环是循环的次数
                    for (int j = list.size() - 1; j > i; j--) {//内循环是外循环一次比较的次数
//                        scanResult = list.get(j);
//                        Log.i(TAG, "scanResuslt = " + scanResult);
                        if (list.get(i).SSID == list.get(j).SSID && list.get(i).BSSID == list.get(j).BSSID) {
//                            scanResult.SSID.equals(removeDoubleQuotes(wifiAdmin.getSSID()))
                            list.remove(j);
                        }
                    }
//                    Log.i(TAG,"排序和移出======" + list.size());
                }
                adapter = new WifiListAdapter(mActivity, list);
                listView.setAdapter(adapter);
                dialog.show();
            }else {
                Toast("请先打开wifi!");
            }
        }

        //对wifi dialog中的listview及button加入监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null){
                    wifi_ssid = list.get(position).SSID;
                    wifi_name.setText(wifi_ssid);
                    Log.i(TAG,"listview点击事件：name = " + wifi_ssid);
                }
                dialog.dismiss();
            }
        });

        wifi_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case 0x01:

                    break;
                case 0x02:
                    break;
                case 0x03:
                    new Thread(runnable).start();
                    break;
                default:
                    break;
            }
        }
    };

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                Toast("连接测试！");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    private void handleEvent(Intent intent){
        String action = intent.getAction();
        switch(action)
        {
            case WifiManager.RSSI_CHANGED_ACTION:
                Log.i(TAG,"update wifi signal++++++++++");
                Toast("信号强度发送变化");
                updateWifiSearchList();
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                Toast("wifi状态发生变化");
                updateWifiSearchList();
                break;
         /*   case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                Toast("wifi搜索结果发生变化");
                break;
            case WifiManager.NETWORK_IDS_CHANGED_ACTION:
                Toast("NetworkID发生变化");
                break;
            case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                Toast("客户端发生变化");
                break;*/
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                Toast("网络状态发生变化");
                updateWifiSearchList();
                break;
            default:
                break;
        }
    }

    private void updateWifiSearchList() {
        //每次加载前清空list和adapter
        Log.i(TAG, "update wifiList clear------");
        if (wifiAdmin.isWifiEnabled()){
            if (list != null){
                Log.i(TAG, "update wifiList clear111111111");
                list.clear();
                Log.i(TAG, "update wifiList clear111111111+ list清理后 = " + list.size());
            }

            if (dialog != null && dialog.isShowing()){
                list = wifiAdmin.getWifiList();

                for (int i = list.size()-1; i >= 0; i--){
//                    scanResult = list.get(i);
//                    Log.i(TAG,"scanResuslt = " + scanResult);
                    if (scanResult.SSID.equals(removeDoubleQuotes(wifiAdmin.getSSID()))){
                        list.remove(i);
                    }
                }
                adapter = new WifiListAdapter(mActivity, list);
                listView.setAdapter(adapter);
            }else {
                Log.i(TAG, "还未打开dialog");
            }

        }else {
            Toast("已经失去网络连接，请重新连接wifi");
            dialog.dismiss();
        }

    }
    /**
     * Toast提示 功能简写
     * @param str
     */
    private void Toast(String str){
        Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 摧毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private static String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }

}
