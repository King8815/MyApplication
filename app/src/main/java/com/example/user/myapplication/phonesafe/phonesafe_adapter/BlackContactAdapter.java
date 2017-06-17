package com.example.user.myapplication.phonesafe.phonesafe_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.phonesafe.phonesafe_database.BlackNumberOperator;
import com.example.user.myapplication.phonesafe.phonesafe_entity.BlackContactInfo;

import java.util.List;

/**
 * Created by user on 2016/7/16.
 * author: Brain
 * description:通过ListView显示黑名单列表时，使用适配器进行数据匹配
 */
public class BlackContactAdapter extends BaseAdapter {
    private List<BlackContactInfo> mList;
    private Context context;
    private BlackNumberOperator operator;
    private BlackContactCallBack callBack;
    public void setCallBack(BlackContactCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * Adapter的构造方法，该方法接收两个参数，List<BlackContactInfo>和contact
     * @param mList 表示从主界面传递过来的黑名单数据集合，加载到页面上的数据都从该集合中取出
     * @param context
     */
    public BlackContactAdapter(List<BlackContactInfo> mList, Context context) {
        super();
        this.context = context;
        this.mList = mList;
        operator = new BlackNumberOperator(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_blackcontact, null);//找到listitem
            holder = new ViewHolder();
            holder.mNameView = (TextView) convertView.findViewById(R.id.title_black_name);
            holder.mModeView = (TextView) convertView.findViewById(R.id.info_black_mode);
            holder.mContactView = (View) convertView.findViewById(R.id.view_black_icon);
            holder.mDeleteView = (View) convertView.findViewById(R.id.view_black_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNameView.setText(mList.get(position).contactName + "(" + mList.get(position).phoneNumber + ")");
        holder.mModeView.setText(mList.get(position).getModeString(mList.get(position).mode));
        holder.mNameView.setTextColor(context.getResources().getColor(R.color.purple));
        holder.mModeView.setTextColor(context.getResources().getColor(R.color.purple));
        holder.mContactView.setBackgroundResource(R.mipmap.brightpurple_contact_icon);
        /**
         * 定义每一个条目中删除按钮的点击事件，点击“删除”按钮，当前条目在数据中被删除，然后刷新当前界面
         */
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean delete = operator.delete(mList.get(position));
                if (delete){
                    mList.remove(mList.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    //如果数据库中没有数据，则执行回调函数
                    if (operator.getTotalNum() == 0){
                        callBack.DataSizeChanged();
                    }
                }else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }
    // 静态内部类，作用是ListView中的控件只加载一次，优化加载速度及内存消耗
    static class ViewHolder {
        TextView mNameView;
        TextView mModeView;
        View mContactView;
        View mDeleteView;
    }
    public interface BlackContactCallBack{
        void DataSizeChanged();
    }
}
