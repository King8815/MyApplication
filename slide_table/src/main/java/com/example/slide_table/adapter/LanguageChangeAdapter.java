package com.example.slide_table.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slide_table.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mYth on 2016/9/22.
 */
public class LanguageChangeAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;


    public LanguageChangeAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.language_change, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title_lang);
            holder.imageView = (ImageView) convertView.findViewById(R.id.select_img);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        ImageView imageView;
    }
}
