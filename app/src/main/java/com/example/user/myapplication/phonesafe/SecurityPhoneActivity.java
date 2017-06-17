package com.example.user.myapplication.phonesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.MainActivity;
import com.example.user.myapplication.R;
import com.example.user.myapplication.phonesafe.phonesafe_adapter.BlackContactAdapter;
import com.example.user.myapplication.phonesafe.phonesafe_database.BlackNumberOperator;
import com.example.user.myapplication.phonesafe.phonesafe_entity.BlackContactInfo;

import java.util.ArrayList;
import java.util.List;

public class SecurityPhoneActivity extends Activity {
    //有黑名单时，显示的帧布局
    private FrameLayout mHaveBlackNum;
    //无黑名单时，显示的帧布局
    private FrameLayout mNoBlackNum;
    private BlackNumberOperator operator;
    private ListView mListView;
    private int pageNum = 0;
    private int pageSize = 15;
    private int totalNum;
    private List<BlackContactInfo> mList = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_phone);
        initView();
        updateData();
    }

    /**
     * 当activity回到前台时，在该方法中判断数据库总条目是否发生变化，如果变化，则将页面设置为0，
     * 清空黑名单中数据，重新添加，并更新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 在页面启动后，如果黑名单的num总数发生变化，显示不同的framelayout
         */
        if (totalNum != operator.getTotalNum()) {
            //数据发生变化，显示不同的framelayout
            if (operator.getTotalNum() > 0) {
                mHaveBlackNum.setVisibility(View.VISIBLE);
                mNoBlackNum.setVisibility(View.GONE);
            } else {
                mNoBlackNum.setVisibility(View.VISIBLE);
                mHaveBlackNum.setVisibility(View.GONE);
            }
            pageNum = 0;
            mList.clear();
            mList.addAll(operator.getPageBlackNumber(pageNum, pageSize));
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 用于数据的填充跟刷新界面
     */
    private void updateData() {
        operator = new BlackNumberOperator(SecurityPhoneActivity.this);
        totalNum = operator.getTotalNum();

        if (totalNum == 0) {
            // 数据库中没有黑名单数据
            mHaveBlackNum.setVisibility(View.GONE);
            mNoBlackNum.setVisibility(View.VISIBLE);
        } else if (totalNum > 0) {
            mHaveBlackNum.setVisibility(View.VISIBLE);
            mNoBlackNum.setVisibility(View.GONE);
            pageNum = 0;
            /*if (mList.size() > 0) {
                mList.clear();
            }*/
            mList.addAll(operator.getPageBlackNumber(pageNum, pageSize));
            if (adapter == null) {
                adapter = new BlackContactAdapter(mList, SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactAdapter.BlackContactCallBack() {
                    @Override
                    public void DataSizeChanged() {
                        updateData();
                    }
                });
                mListView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 对界面中的数据进行初始化
     */
    private void initView() {
        findViewById(R.id.rl_title).setBackgroundResource(R.color.purple);//对包括的title设置背景色
        ImageView mLeftView = (ImageView)findViewById(R.id.imgv_leftbtn);
        mLeftView.setImageResource(R.mipmap.back);
        TextView mText = (TextView) findViewById(R.id.tv_title);
        mText.setText("返回");

        mHaveBlackNum = (FrameLayout)findViewById(R.id.have_blacknum);
        mNoBlackNum = (FrameLayout)findViewById(R.id.no_blacknum);

        mListView = (ListView)findViewById(R.id.have_blacknum_list);
        /*listview的滑动监听事件，onScrollStateChanged主要是获取数据库中的数据分页显示在界面上，每页显示多少由自己定义，
        * listview向下滑动时，再次加载同样条目的数据
        * */
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://没有滑动的状态
                        //获取最后一个可见条目
                        int lastVisiblePosition = mListView.getLastVisiblePosition();
                        //如果当前条目是最后一个，则查询更多的数据
                        if (lastVisiblePosition == mList.size() -1){
                            pageNum++;
                            if (pageNum*pageSize >= totalNum){
                                Toast.makeText(SecurityPhoneActivity.this,"没有更多数据", Toast.LENGTH_SHORT).show();
                            }else {
                                mList.addAll(operator.getPageBlackNumber(pageNum, pageSize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * @param view 根据layout.xml中的控件属性，设定监听事件
     */
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_addBlackNum:
                //跳转到添加黑名单界面
                Intent intent = new Intent(this, AddBlackNumActivity.class);
                startActivity(intent);
                break;
            case R.id.imgv_leftbtn:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
