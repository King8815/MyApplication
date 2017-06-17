package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.myapplication.Utils.MD5Utils;
import com.example.user.myapplication.charpter01.LostProtectActivity;
import com.example.user.myapplication.phonesafe.SecurityPhoneActivity;
import com.example.user.myapplication.com.example.user.Adapter.GridViewAdapter;
import com.example.user.myapplication.com.example.user.Adapter.MyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/6/6.
 */
public class MainActivity extends Activity {
    int image[] = {R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app,
            R.mipmap.trojan, R.mipmap.sysoptimize, R.mipmap.taskmanager,
            R.mipmap.netmanager, R.mipmap.atools, R.mipmap.settings};
    String title[] = {"手机防盗", "通讯卫士", "软件管家",
            "手机杀毒", "缓存清理", "进程管理",
            "流量统计", "高级工具", "设置中心"};
    private List<Map<String, Object>> listitems = new ArrayList<Map<String, Object>>();//创建一个list列表
    private GridViewAdapter adapter;
    private long exitTime;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences affirm_preferences = null;
    private SharedPreferences.Editor editor = null;
    private SharedPreferences.Editor affirm_editor = null;
    private MyDialog myDialog;
    private MyDialog affirm_dialog;
    private boolean isPwd_OK = true;
    private EditText first_edit;
    private EditText again_edit;
    private EditText affirm_edit;

    private String first_edit_pwd = null;
    private String again_edit_pwd = null;
    private String affirm_edit_pwd = null;

    private static final String TAG = "home";
//    通过循环的方式将图片的id和文字防盗Map中，然后添加到list中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        sharedPreferences = getSharedPreferences("isPwd_OK", MODE_WORLD_READABLE);
        affirm_preferences = getSharedPreferences("isPwd_success", MODE_WORLD_READABLE);

        for (int i = 0; i < image.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("title", title[i]);
            listitems.add(map);
        }
//        构建gridview的适配器
       /* SimpleAdapter simpleAdapter = new SimpleAdapter(this,//上下文关联
                listitems,//
                R.layout.gridview_items,
                new String[]{"title", "image"},
                new int[]{R.id.grid_view_title, R.id.grid_view_image});*/
        /*
        * 同样可以使用下列方法构建适配器
        * String [] from ={"image","text"};
        * int [] to = {R.id.image,R.id.text};
        *  sim_adapter = new SimpleAdapter(this, listitems, R.layout.item, from, to);
        * */
//        gridView.setAdapter(simpleAdapter);

        adapter = new GridViewAdapter(this, listitems);
        gridView.setAdapter(adapter);
//        设置gridview中的控件的监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // parent代表gridview，view代表gridview中的对象，position代表对象的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // 手机防盗
                    case 0:
                        // 对密码提示框edittext初始化
//                        Toast.makeText(MainActivity.this, "手机防盗按键测试", Toast.LENGTH_LONG).show();
                        isPwd_OK = sharedPreferences.getBoolean("isPwd_OK", true);
                        // 输入密码正确此时，将isPwd_OK值设置为false，并且保存在sharedPreferences中，以便于进行第二次进入的判断
                        if (isPwd_OK) {
                            // 第一次进入界面没有设置密码的情况
                            Log.i(TAG, "first dialog");
                            showPwdDlg();
                        } else {
                            // 正确输入密码后，弹出的确认密码框
                            showAffirmDlg();
                        }

                        break;
                    // 通讯卫士
                    case 1:
                        startActivity(SecurityPhoneActivity.class);
                    break;
                    // 软件管家
                    case 2:
                        break;
                    // 手机杀毒
                    case 3:
                        break;
                    // 缓存管理
                    case 4:

                        break;
                    // 进程管理
                    case 5:

                        break;
                    // 流量统计
                    case 6:

                        break;
                    // 高级工具
                    case 7:

                        break;
                    // 设置中心
                    case 8:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param view 根据layout.xml中的控件属性，设定监听事件
     */
    public void onclick(View view) {
        switch (view.getId()) {
            // 首次弹出提示框
            case R.id.ok_btn:
//                Toast.makeText(this, getResources().getString(R.string.ok_btn), Toast.LENGTH_SHORT).show();
                // 获取密码输入框中内容
                first_edit_pwd = first_edit.getText().toString().trim();
                again_edit_pwd = again_edit.getText().toString().trim();
                Log.i(TAG, "first_edit_pwd　＝　" + first_edit_pwd);
                Log.i(TAG, "again_edit_pwd　＝　" + again_edit_pwd);
                // 对输入的密码进行判断
                if (first_edit_pwd == null || again_edit_pwd == null || !first_edit_pwd.equals(again_edit_pwd)) {
                    // 输入密码进行判断
                    first_edit.setText("");//清空密码框
                    again_edit.setText("");
                    first_edit.requestFocus();// 让第一个输入密码框获得焦点
                    Toast.makeText(this, "请输入正确密码", Toast.LENGTH_SHORT).show();
                } else if (first_edit_pwd.equals(again_edit_pwd)) {
                    // 两次输入的密码一致
                    Toast.makeText(this, "输入密码一致，请记住密码", Toast.LENGTH_SHORT).show();
                    // 存入数据
                    editor = sharedPreferences.edit();
                    editor.putBoolean("isPwd_OK", false);
                    editor.commit();
                    // 存入密码
                    affirm_editor = affirm_preferences.edit();
                    affirm_editor.putString("isPwd_success", MD5Utils.encode(again_edit_pwd));
                    affirm_editor.commit();

                    // 数据保存结束后打开密码确认提示框
                    myDialog.dismiss();
                    showAffirmDlg();
                }
                break;
            case R.id.cancel_btn:
                Toast.makeText(this, "取消密码提示框", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
                break;
            // 密码提示框确认输入
            case R.id.affirm_ok_btn:
                affirm_edit_pwd = affirm_edit.getText().toString().trim();
//                Log.i(TAG, "affirm_edit_pwd = " + affirm_edit_pwd);
//                Log.i(TAG, "pwd = " + affirm_preferences.getString("isPwd_success", null));
//                Log.i(TAG,MD5Utils.encode(affirm_edit_pwd));
                if ("".equals(affirm_edit_pwd)) {
                    Toast.makeText(this, "密码为空，请重新输入密码", Toast.LENGTH_SHORT).show();
                    affirm_edit.setText("");
                    affirm_edit.requestFocus();
                } else if (!affirm_preferences.getString("isPwd_success", null).equals(MD5Utils.encode(affirm_edit_pwd)) ){
                    Log.i(TAG, "密码错误，请重新输入");
                    affirm_edit.setText("");
                    affirm_edit.requestFocus();
                } else {
                    Toast.makeText(this, "输入密码正确", Toast.LENGTH_SHORT).show();
                    affirm_dialog.dismiss();
                    startActivity(LostProtectActivity.class);
                }
                break;
            case R.id.affirm_cancel_btn:
                affirm_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /*
    * 判断用户是否首次输入密码
    * */
    private void showPwdDlg() {
        myDialog = new MyDialog(this, R.style.Pwd_Show);
        LinearLayout pwd_show = (LinearLayout) getLayoutInflater().inflate(R.layout.pwd_show, null);
        myDialog.setContentView(pwd_show);
//        initEditText 采用我们要获取的view的紧邻的父级的ViewGroup中调用findViewById方法,edittext属于父属性pwd_show中
        first_edit = (EditText)pwd_show.findViewById(R.id.first_enter);
        again_edit = (EditText)pwd_show.findViewById(R.id.again_enter);
        Log.i(TAG, "first_edit" +first_edit);
        Log.i(TAG, "again_edit" +again_edit);
        myDialog.show();
    }

    /*
    *  用户首次设置密码成功后，弹出需要确认的密码框
    * */
    private void showAffirmDlg() {
        affirm_dialog = new MyDialog(this, R.style.Pwd_Show);
        LinearLayout pwd_affirm_show = (LinearLayout) getLayoutInflater().inflate(R.layout.pwd_affirm_show, null);
        affirm_dialog.setContentView(pwd_affirm_show);
        // initEditText 采用我们要获取的view的紧邻的父级的ViewGroup中调用findViewById方法
        affirm_edit = (EditText)pwd_affirm_show.findViewById(R.id.affirm_enter);
        Log.i(TAG, "affirm_edit" +affirm_edit);
        affirm_dialog.show();
    }
    /*
    * 开启新的Activity，此方法比较适合较多的activity打开的时候，可以作为一种复用接口
    * */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 * 两次按下返回键，弹出提示信息
	 * keyCode是否为返回键，event是否为按下
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.exitShow), Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
