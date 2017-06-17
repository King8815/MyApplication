package com.example.slide_table.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slide_table.R;
import com.example.slide_table.utils.WifiAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/11/17.
 */
public class WifiListAdapter extends BaseAdapter {
    private static final String TAG = "wifi";
    private Context context;
    private List<ScanResult> list;
    private LayoutInflater mInflater;

    public WifiListAdapter(Context context, List<ScanResult> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * wifi list大小
     *
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * wifi list中的位置
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * wifi list位置的Id
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position    item位置
     * @param convertView item显示内容
     * @param parent      父框架
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wifi_list_item, null);
            holder.title_textView = (TextView) convertView.findViewById(R.id.wifi_title);
            holder.wifiLevel_imageView = (ImageView) convertView.findViewById(R.id.wifi_level);

            /**
             * 将手机已经连接的wifi SSID移出list，例当前手机连接SSID：A，搜索到wifi列表为：A,B,C,D
             * dialog对话框重新搜索wifi列表只显示B，C，D
             */
            ScanResult scanResult = list.get(position);
            String cap = scanResult.capabilities;//加密方式
            int signal_level = scanResult.level;//信号强度
            Log.i(TAG, "wifi名称：" + scanResult.SSID + "; 加密方式：" + cap + "; 信号强度：" + signal_level + "; BSSID：" + scanResult.BSSID);

            //判断当前wifi名称是否为空，且搜索到的wifi名称是否与当前连接的wifi名称相同
            if (!TextUtils.isEmpty(scanResult.SSID)) {
                holder.title_textView.setText(scanResult.SSID);
                //根据信号强度和加密方式选择不同的图标
                if (Math.abs(signal_level) > 100) {
                    if (cap.contains("WPA") || cap.contains("wpa") || cap.contains("WEP") || cap.contains("wep")) {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_lock_none);
                    } else {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_none);
                    }
                } else if (Math.abs(signal_level) >= 80 && Math.abs(signal_level) < 100) {
                    if (cap.contains("WPA") || cap.contains("wpa") || cap.contains("WEP") || cap.contains("wep")) {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_lock_0);
                    } else {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_0);
                    }
                } else if (Math.abs(signal_level) >= 70 && Math.abs(signal_level) < 80) {
                    if (cap.contains("WPA") || cap.contains("wpa") || cap.contains("WEP") || cap.contains("wep")) {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_lock_1);
                    } else {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_1);
                    }
                } else if (Math.abs(signal_level) > 50 && Math.abs(signal_level) < 70) {
                    if (cap.contains("WPA") || cap.contains("wpa") || cap.contains("WEP") || cap.contains("wep")) {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_lock_2);
                    } else {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_2);
                    }
                } else if (Math.abs(signal_level) >= 0 && Math.abs(signal_level) <= 50) {
                    if (cap.contains("WPA") || cap.contains("wpa") || cap.contains("WEP") || cap.contains("wep")) {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_lock_3);
                    } else {
                        holder.wifiLevel_imageView.setImageResource(R.mipmap.wifi_signal_3);
                    }
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView wifiLevel_imageView;
        TextView title_textView;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 去掉两头的双引号
     *
     * @param string
     * @return
     */
    static String removeDoubleQuotes(String string) {
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

    /**
     * 给两头加上双引号
     *
     * @param string
     * @return
     */
    public static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }
}
