package com.example.slide_table.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.slide_table.R;

import java.util.List;

/**
 * Created by user on 2017/6/9.
 */

public class BTDeviceAdapter extends BaseAdapter{
    private static final String TAG = "first";
    private Context context;
    private LayoutInflater mInflater;
    private List<BluetoothDevice> list;
    private BluetoothDevice device;

    private static class ViewHolder{
        TextView btNameView;
        TextView btAddrView;
        TextView btBondedView;
    }

    //构造函数
    public BTDeviceAdapter(final Context context, final List<BluetoothDevice> list){
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }
    //device list大小
    @Override
    public int getCount() {
        return list.size();
    }
    //device在list中的位置
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    //device在list中的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        device = list.get(position);
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            // view创建
            convertView = mInflater.inflate(R.layout.btdevice_list_item, null);
            holder = new ViewHolder();

            holder.btNameView = (TextView) convertView.findViewById(R.id.bt_name);
            holder.btAddrView = (TextView) convertView.findViewById(R.id.bt_addr);
            holder.btBondedView = (TextView) convertView.findViewById(R.id.bt_bonded);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //view属性设置
        holder.btNameView.setText(device.getName());
        holder.btAddrView.setText(device.getAddress());

        if (device.getBondState() == BluetoothDevice.BOND_BONDED){
            Log.i(TAG, "设备：" + device.getName() + "已绑定");
            holder.btBondedView.setVisibility(View.VISIBLE);
            holder.btBondedView.setTextColor(Color.RED);
        }else {
            holder.btBondedView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
}
