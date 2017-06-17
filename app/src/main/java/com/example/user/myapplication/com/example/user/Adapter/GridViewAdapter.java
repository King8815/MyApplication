package com.example.user.myapplication.com.example.user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/6/6.
 */
public class GridViewAdapter extends BaseAdapter{
//    private ArrayList<String> mNameList = new ArrayList<String>();
//    private ArrayList<Drawable> mDrawableList = new ArrayList<Drawable>();
    private List<Map<String, Object>> listitems = new ArrayList<Map<String, Object>>();
    private LayoutInflater mInflater;
    private Context context;
//    LinearLayout.LayoutParams params;

    public GridViewAdapter(Context context, List<Map<String, Object>> listitems){
//        this.mNameList = mNameList;
//        this.mDrawableList = mDrawableList;
        this.context = context;
        this.listitems = listitems;
        mInflater = LayoutInflater.from(context);

//        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
    }
    @Override
    public int getCount() {
        return listitems.size();
    }

    @Override
    public Object getItem(int position) {
        return listitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView titleView;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_items, null);
            imageView = (ImageView) convertView.findViewById(R.id.grid_view_image);
            titleView = (TextView) convertView.findViewById(R.id.grid_view_title);

            //获取自定义的实例对象
            imageView.setImageResource((Integer) listitems.get(position).get("image"));
            titleView.setText(listitems.get(position).get("title").toString());
        }
        return convertView;
    }
}
