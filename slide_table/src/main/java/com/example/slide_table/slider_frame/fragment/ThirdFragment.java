package com.example.slide_table.slider_frame.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.slide_table.R;
import com.example.slide_table.adapter.WifiListAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by user on 2016/8/5.
 */
public class ThirdFragment extends Fragment{
    private Activity mActivity;
    private static Button wifi_search;
    private static ListView listView;
    private static Button wifi_rescan;

    private volatile boolean mScanning;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private WifiConfiguration wifiConfiguration;
    private List<ScanResult> list = null;
    private ScanResult mScanResult;
    private WifiListAdapter adapter;
    private StringBuffer sb = new StringBuffer();
    private static IntentFilter mFilter;
    //广播接收器，用来接收消息并做响应的处理工作
    private static BroadcastReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mActivity = getActivity();
        wifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);//获取wifiManager
        View view = inflater.inflate(R.layout.third_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        addListener();
    }

    private void findViews(View view) {
        wifi_search = (Button)view.findViewById(R.id.wifi_search);
        wifi_rescan = (Button)view.findViewById(R.id.wifi_rescan);
        listView = (ListView)view.findViewById(R.id.wifi_list);
//        adapter = new WifiListAdapter(mActivity, list);
//        listView.setAdapter(adapter);
    }

    /**
     * 蓝牙搜索的监听事件
     */
    private void addListener(){
        wifi_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前wifi是否打开
                if(wifiManager.isWifiEnabled()){
//                    //wifiManager.startScan();
//                    list = wifiManager.getScanResults();
//                    adapter = new WifiListAdapter(mActivity, list,);
//                    listView.setAdapter(adapter);
                    Toast("请开启wifi");
                }else {
                    Toast("请开启wifi");
                    /*wifiManager.setWifiEnabled(true);//设置开启关闭
                    //开启wifi需要一段时间
                    while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {

                        try {

                            Thread.currentThread();

                            Thread.sleep(100);

                        } catch (InterruptedException ie) {

                        }

                    }*/
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * Toast提示 功能简写
     * @param str
     */
    private void Toast(String str){
        Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }

    //添加一个网络并连接
    public int addNetwork(WifiConfiguration g) {
        //添加网络
        int wcgID = wifiManager.addNetwork(g);
        //设置添加的网络可用
        wifiManager.enableNetwork(wcgID, true);
        return wcgID;
    }

}
