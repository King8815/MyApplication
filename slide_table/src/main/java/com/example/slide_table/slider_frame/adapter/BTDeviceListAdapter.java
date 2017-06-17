package com.example.slide_table.slider_frame.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slide_table.R;

import java.util.List;

import cn.smssdk.gui.DefaultContactViewItem;

/**
 * Created by user on 2016/9/7.
 */
public class BTDeviceListAdapter extends BaseAdapter{
    private Context context;
    List<BluetoothDevice> mDeviceList;
    LayoutInflater inflater;

    public BTDeviceListAdapter(Context context, List<BluetoothDevice> mDeviceList){
        this.context = context;
        this.mDeviceList = mDeviceList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null){
            convertView = inflater.inflate(R.layout.btdevice_list_item, null);
            holder = new ViewHolder();
            final BluetoothDevice device = mDeviceList.get(position);
            holder.bt_deviceName = (TextView)convertView.findViewById(R.id.bt_name);
            holder.bt_deviceAddress = (TextView)convertView.findViewById(R.id.bt_addr);

            holder.bt_deviceName.setText(device.getName());
            holder.bt_deviceAddress.setText(device.getAddress());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED){
                Log.e("wzl", "device name = " + device.getName());
                Log.e("wzl", "device address = " + device.getAddress());
                holder.bt_deviceName.setTextColor(Color.WHITE);
                holder.bt_deviceAddress.setTextColor(Color.WHITE);
            }
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder{
        TextView bt_deviceName;
        TextView bt_deviceAddress;
    }
}
