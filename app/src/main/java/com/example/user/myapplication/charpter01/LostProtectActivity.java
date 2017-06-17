package com.example.user.myapplication.charpter01;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.com.example.user.Adapter.MyViewPagerAdapter;

import java.util.ArrayList;

public class LostProtectActivity extends Activity {
//    显示界面的viewpager
    private ViewPager viewPager;
//    页面适配器
    private MyViewPagerAdapter myViewPagerAdapter;
//    页面列表
    private ArrayList<View> mViews;
    private EditText et_mobile;
    // 声明姓名，电话
    private String usernumber;
//    private String username;
    /*	图片资源，这里只放入3张，因为第四章带有button按钮，单独配置在guide_content_view.xml
* 可以直接加载使用
*/
    private final int layout[] = {R.layout.activity_setup1, R.layout.activity_setup2, R.layout.activity_setup3, R.layout.activity_setup4};

    //    底部导航的圆点
    private ImageView[] guideDots;
//    记录当前选中的页面位置
    private int currentIndex;

    private TelephonyManager mTelephonyManager;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_protect);
//        数据初始化
        mTelephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        sp = getSharedPreferences("", MODE_PRIVATE);
        et_mobile = (EditText) this.findViewById(R.id.edit_phone_add);//初始化添加号码的edit
//        界面初始化
        initLayout();
//        圆点初始化
        initDot();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                重写函数，设定当前页面的dot
                setCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    对显示界面的view进行初始化
    private void initLayout(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        mViews = new ArrayList<View>();
        for (int i = 0; i < layout.length; i++) {
            View view = LayoutInflater.from(LostProtectActivity.this).inflate(layout[i], null);
            mViews.add(view);
        }

//        添加页面适配器
        myViewPagerAdapter = new MyViewPagerAdapter(mViews);
        viewPager.setAdapter(myViewPagerAdapter);
    }
//    对显示界面的dot进行初始化
    private void initDot(){
        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.linear);
        guideDots = new ImageView[mViews.size()];
        
//        循环取得小圆点的图片，让每个小点都处于正常状态
        for (int i = 0; i < mViews.size(); i++) {
            guideDots[i] = (ImageView)dotLayout.getChildAt(i);
            guideDots[i].setSelected(false);//setSelected()将dot的<selector />的属性设置
        }
//        设置第一个小圆点为选中状态
        currentIndex = 0;
        guideDots[currentIndex].setSelected(true);
    }
    //	页面更新时，更新小圆点状态
    private void setCurrentDot(int position) {
        if (position < 0 || position > (mViews.size() - 1) || currentIndex == position) {
            return;
        }
        guideDots[position].setSelected(true);
        guideDots[currentIndex].setSelected(false);
        currentIndex = position;
    }

    //添加对于按钮的监听事件，跳转进入主界面
    public void onclick(View view){
        switch (view.getId()) {
//            sim卡变更提醒
            case R.id.sim_change_alert:
                Toast.makeText(this, "提醒SIM变更", Toast.LENGTH_SHORT).show();
                break;
//            GPS追踪
            case R.id.gps_trace:
                Toast.makeText(this, "GPS追踪", Toast.LENGTH_SHORT).show();
                break;
//            远程锁定
            case R.id.remote_lock:
                Toast.makeText(this, "远程锁定屏幕", Toast.LENGTH_SHORT).show();
                break;
//            远程删除数据
            case R.id.remote_del_data:
                Toast.makeText(this, "远程删除数据", Toast.LENGTH_SHORT).show();
                break;
//            绑定SIM卡
            case R.id.sim_btn:
                bindSIM();
                ((Button) view).setEnabled(false);
                break;
            case R.id.image_add_btn:
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
                String safePhone = et_mobile.getText().toString().trim();
                if ("".equals(safePhone)) {
                    Toast.makeText(this, "请输入安全号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("safephone", safePhone);
                editor.commit();
                break;
            default:
                break;
        }
    }
//    判断是否有sim信息
    private boolean isBind(){
        String simString = sp.getString("sim", null);
        if ("".equals(simString)) {
            return false;
        }
        return true;
    }
    /*
    * 绑定sim卡
    * */
    private void bindSIM(){
        if (!isBind()) {
//            第一次绑定sim卡
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("sim", simSerialNumber);
            editor.commit();
            Toast.makeText(this, "绑定sim卡成功.", Toast.LENGTH_SHORT).show();
//            已经绑定sim卡后，设定sim_btn不可选
        } else {
            Toast.makeText(this, "已经绑定成功，不需要再次绑定", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            // 获得DATA表中的名字
//            username = cursor.getString(cursor
//                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                et_mobile.setText(usernumber);
            }

        }
    }

    /*
    * 开启新的Activity，此方法比较适合较多的activity打开的时候，可以作为一种复用接口
    * */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(LostProtectActivity.this, cls);
        startActivity(intent);
        finish();
    }
}
