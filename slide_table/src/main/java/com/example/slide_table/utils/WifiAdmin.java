package com.example.slide_table.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2016/11/21.
 */
public class WifiAdmin {
    private static final String TAG = "wifiAdmin";
    //定义一个WifiManager对象
    private WifiManager wifiManager;
    //定义一个WifiInfo对象
    private WifiInfo wifiInfo;
    //扫描出的网络连接列表
    private List<ScanResult> list;
    //网络连接列表
    private List<WifiConfiguration> wifiConfiguration;
    //定义一个wifiLock
    private WifiManager.WifiLock wifiLock;
    private Context context;

    //构造器
    public WifiAdmin(Context context){
        super();
        this.context = context;
        //获取wifiManager
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        //获得wifiInfo
        wifiInfo = wifiManager.getConnectionInfo();
        //IPAdd为一串整数，且SSID为带有双引号
        Log.i(TAG,"当前连接网络的IPAdd = " + wifiInfo.getIpAddress() + "; SSID = " + wifiInfo.getSSID()
                  + "; RSSI = " + wifiInfo.getRssi() + "; NetWorkId = " + wifiInfo.getNetworkId());
    }
    //判断wifi是否打开
    public boolean isWifiEnabled(){
        return wifiManager.isWifiEnabled();
    }

    //打开wifi
    public void openWifi(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    //关闭wifi
    public void closeWifi(){
        if (wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
    }

    //检查当前的wifi状态
    /**
     *
     * WifiManager.WIFI_STATE_DISABLED : WIFI网卡不可用（1）

     WifiManager.WIFI_STATE_DISABLING : WIFI网卡正在关闭（0）

     WifiManager.WIFI_STATE_ENABLED : WIFI网卡可用（3）

     WifiManager.WIFI_STATE_ENABLING : WIFI网正在打开（2） （WIFI启动需要一段时间）

     WifiManager.WIFI_STATE_UNKNOWN  : 未知网卡状态

     * @return
     */
    public int wifiState(){
        return wifiManager.getWifiState();
    }

    //锁定wifi
    public void acquireWifiLock(){
        wifiLock.acquire();
    }

    //解锁wifi
    public void releaseWifiLock(){
        // 判断时候锁定
        if (wifiLock.isHeld()) {
            wifiLock.acquire();
        }
    }

    //创建一个WifiLock
    public void creatWifiLock() {
        wifiLock = wifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return wifiConfiguration;
    }

    //指定配置好的网络进行连接
    public void connetionConfiguration(int index){
        if(index>wifiConfiguration.size()){
            return ;
        }
        //连接配置好指定ID的网络
        wifiManager.enableNetwork(wifiConfiguration.get(index).networkId, true);
    }

    //指定配置好的网络进行连接
    public void connetionConfiguration(WifiConfiguration wifiConfiguration){
        if(wifiConfiguration == null){
            return ;
        }
        //连接配置好指定ID的网络
        wifiManager.enableNetwork(wifiConfiguration.networkId, true);
    }

   /* //开始wifi扫描
    public void startScan(){
        wifiManager.startScan();
        //得到扫描结果
        list = wifiManager.getScanResults();
        //得到配置好的网络连接
        wifiConfiguration = wifiManager.getConfiguredNetworks();
    }*/
    //得到网络列表
    public List<ScanResult> getWifiList(){
        wifiManager.startScan();
        list = wifiManager.getScanResults();
        return list;
    }
    //得到配置好的网络连接
    public List<WifiConfiguration> getWifiConfiguration(){
        wifiConfiguration = wifiManager.getConfiguredNetworks();
        return wifiConfiguration;
    }
    //查看扫描结果
    @SuppressLint("UseValueOf")
    public StringBuffer lookUpScan(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<list.size();i++){
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            sb.append((list.get(i)).toString()).append("\n");
        }
        return sb;
    }
    //获得连接网络的macAdd
    public String getMacAddress(){
        return (wifiInfo==null)?"NULL":wifiInfo.getMacAddress();
    }
    //得到接入点的BSSID
    public String getBSSID(){
        return (wifiInfo==null)?"NULL":wifiInfo.getBSSID();
    }
    //得到SSID，即wifi名称
    public String getSSID(){
        return (wifiInfo==null)?"NULL":wifiInfo.getSSID();
    }
    //得到IPAddress
    public int getIpAddress(){
        return (wifiInfo==null)?0:wifiInfo.getIpAddress();
    }

    //得到连接的ID
    public int getNetWordId(){
        return (wifiInfo==null)?0:wifiInfo.getNetworkId();
    }

    //得到wifiInfo的所有信息
    public String getWifiInfo(){
        return (wifiInfo==null)?"NULL":wifiInfo.toString();
    }

    //断开指定ID的网络，仅断开
    public void disConnectionWifi(int netId){
        wifiManager.disableNetwork(netId);
        wifiManager.disconnect();
    }

    //移除指定ID的网络，包括清楚账号，密码
    public void removeNetwork(int networkId) {
        wifiManager.removeNetwork(networkId);
    }


    public WifiConfiguration isExsit(String SSID)
    {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    /*public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter);

    public void myUnregisterReceiver(BroadcastReceiver receiver);

    public void onNotifyWifiConnected();

    public void onNotifyWifiConnectFailed();*/

    public static final int TYPE_NO_PASSWD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;

    //添加一个网络并连接
    public void addNetWork(WifiConfiguration configuration){
        int wcgId = wifiManager.addNetwork(configuration);
        wifiManager.enableNetwork(wcgId, true);
        System.out.println("wcgID ----" + wcgId);
        System.out.println("enable_state ----" + wifiManager.enableNetwork(wcgId, true));
    }

    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {

        Log.v(TAG, "SSID = " + SSID + "## Password = " + password + "## Type = " + type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsit(SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        // 分为三种情况：1没有密码2用wep加密3用wpa加密
        if (type == TYPE_NO_PASSWD) {// WIFICIPHER_NOPASS
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;

        } else if (type == TYPE_WEP) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == TYPE_WPA) {   // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    public static final int WIFI_CONNECTED = 0x01;
    public static final int WIFI_CONNECT_FAILED = 0x02;
    public static final int WIFI_CONNECTING = 0x03;
    /**
     * 判断wifi是否连接成功,不是network
     *
     * @param context
     * @return
     */
    public int isWifiContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Log.v(TAG, "isConnectedOrConnecting = " + wifiNetworkInfo.isConnectedOrConnecting());
        Log.d(TAG, "wifiNetworkInfo.getDetailedState() = " + wifiNetworkInfo.getDetailedState());
        if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR
                || wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING) {
            return WIFI_CONNECTING;
        } else if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return WIFI_CONNECTED;
        } else {
            Log.d(TAG, "getDetailedState() == " + wifiNetworkInfo.getDetailedState());
            return WIFI_CONNECT_FAILED;
        }
    }

}
