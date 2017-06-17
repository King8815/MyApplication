package com.example.slide_table.slider_frame.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mylibrary.utils.ToastMgr;
import com.example.slide_table.R;
import com.example.slide_table.adapter.BTDeviceAdapter;
import com.example.slide_table.bean.Weather;
import com.example.slide_table.utils.ChangeLanguageEvent;
import com.example.slide_table.utils.MyDialog;
import com.example.slide_table.utils.ShareDialog;
import com.example.slide_table.volley_http.GsonRequest;
import com.example.slide_table.volley_http.NetListener;
import com.example.slide_table.volley_logic.BaseLogic;
import com.mob.tools.utils.UIHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import publicTools.MyApplication;

/**
 * Created by mYth on 2016/8/5.
 */
public class FirstFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "first";
    private Activity mActivity;
    //    private SwipeMenuListView menuListView;
//    private SwipeMenuCreator creator;
//    private ListView change_language;
    private Button onekey_share, lang_switch, thread_test, volley_test, gson_parse, bt_develop;
    private TextView content1;
    private int checkedItem = 0;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    //第三方分享
    private ShareDialog shareDialog;
    private static final int MSG_TOAST = 0x01;
    private static final int MSG_ACTION_CCALLBACK = 0x02;
    private static final int MSG_CANCEL_NOTIFY = 0x03;

    private static final String fileName = "/sdcard/share_pic.jpg";
    public static String TEST_IMAGE;

    //Volley网络请求测试
//    private RequestQueue mRequestQueue;
    private final static String url = "http://www.weather.com.cn/data/sk/101010100.html";
    private StringRequest mStringRequest;
    private Weather weather;

    //蓝牙开发
    private static BluetoothAdapter adapter;
//    private static BluetoothDevice device;
    private List<BluetoothDevice> mlist;
    private static BluetoothSocket socket;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private MyDialog dialog;
    private Button bt_device_list_cancel;
    private ListView bt_device_list;
    private BTDeviceAdapter deviceAdapter;
    private volatile boolean mScanning;

    protected BaseLogic<Weather> logic = new BaseLogic<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mActivity = getActivity();// 关联当前activity与fragment的交互
        sharedPreferences = mActivity.getSharedPreferences("C_E", Context.MODE_PRIVATE);
//        mRequestQueue = Volley.newRequestQueue(mActivity);//创建requestQueue
        View view = inflater.inflate(R.layout.first_frament, container, false);
//        //注册EventBus
//        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
        addListener();
    }

    private void findViews(View view) {
//        change_language = (ListView)view.findViewById(R.id.change_language);
//        menuListView = (SwipeMenuListView) view.findViewById(R.id.swipemenu);
        onekey_share = (Button) view.findViewById(R.id.button1);
        lang_switch = (Button) view.findViewById(R.id.button2);
        thread_test = (Button) view.findViewById(R.id.button3);
        volley_test = (Button) view.findViewById(R.id.button4);
        gson_parse = (Button) view.findViewById(R.id.button5);
        bt_develop = (Button) view.findViewById(R.id.button6);
        content1 = (TextView) view.findViewById(R.id.content1);
    }

    private void init() {
        /*SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //新建“open”
                SwipeMenuItem openItem = new SwipeMenuItem(mActivity.getApplicationContext());
                openItem.setBackground(R.color.whitesmoke);
                openItem.setWidth(Tools.dip2px(mActivity, 45));
                openItem.setTitle("Open");
                openItem.setTitleSize(14);
                openItem.setTitleColor(R.color.violet);
                menu.addMenuItem(openItem);

                //新建“删除”按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity.getApplicationContext());
                openItem.setBackground(R.color.whitesmoke);
                openItem.setWidth(Tools.dip2px(mActivity, 45));
                deleteItem.setIcon(R.mipmap.iv_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        menuListView.setMenuCreator(creator);*/
        //shareDialog初始化
        shareDialog = new ShareDialog(mActivity);
        //Toast初始化
        ToastMgr.init(mActivity);
        //ShareSDK初始化
        ShareSDK.initSDK(mActivity);
        //蓝牙初始化
        adapter = BluetoothAdapter.getDefaultAdapter();
        mlist = new ArrayList<BluetoothDevice>();
    }

    private void addListener() {
        onekey_share.setOnClickListener(this);//一键分享
        lang_switch.setOnClickListener(this);//语言切换
        thread_test.setOnClickListener(this);//线程测试
        volley_test.setOnClickListener(this);//Volley测试
        gson_parse.setOnClickListener(this);//Gson解析
        bt_develop.setOnClickListener(this);//蓝牙开发
        shareDialog.setSinaClickListener(this);//第三方分享监听事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //一键分享
            case R.id.button1:
                Bitmap bitmap = GetandSavaCurrentImage(mActivity);
                saveToSD(bitmap, fileName);
                shareDialog.show();
                break;
            //中英文切换
            case R.id.button2:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                String[] language = {"中文", "英文"};
                checkedItem = sharedPreferences.getInt("C_E", checkedItem);
                Log.i("wzl", "保存的选项为 = " + checkedItem);

                builder.setTitle("中英文切换");
                builder.setSingleChoiceItems(language, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem = which;
                        Log.i("wzl", "checkedItem = " + checkedItem);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //保存选中的语言
                        editor = sharedPreferences.edit();
                        editor.putInt("C_E", checkedItem);
                        editor.commit();
                        handler.sendEmptyMessage(0x00);//切换语言
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mActivity, "取消按钮", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            //线程测试
            case R.id.button3:
                handler.sendEmptyMessage(0x04);
                break;
            //volley网络测试
            case R.id.button4:
                /*mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("wzl", "测试使用+++++++++" + response);
                        ToastMgr.show("成功");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("wzl", error.getMessage(),error);
                        ToastMgr.show("失败");
                    }
                });
                MyApplication.getInstance().getRequestQueue().add(mStringRequest);*/
                logic.edit().setUrl(url);
                logic.edit().setBaseClass(Weather.class);
                logic.edit().setListener(new NetListener<Weather>() {
                    @Override
                    public void onSuccess(Weather response) {
                        Log.e("volley test success", "成功");
                        Log.d("volley test success", response.getWeatherinfo().getRain());
                        Log.d("volley test success", response.getWeatherinfo().getRadar());
                        Log.e("volley test success", response.getWeatherinfo().getNjd());
                        ToastMgr.show("测试成功！");
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.e("volley test fail", error.getMessage(), error);
                    }
                });
                logic.doGet();
                break;
            //Gson解析
            case R.id.button5:
                getWeatherInfo();
                break;
            case R.id.iv_share_sina:
                showShare();
                break;
            //蓝牙开发
            case R.id.button6:
                search_bt_device();
                break;
            case R.id.bt_dialog_cancel:
                //注销蓝牙广播
                dialog.dismiss();
                mlist.clear();
                break;
        }
    }

    /**
     * 搜索蓝牙设备，并连接某个已知的蓝牙设备
     */
    private void search_bt_device() {
        if (null == adapter) {
            ToastMgr.show("本设备不支持蓝牙功能！");
        }
        /**
         * 为更好的用户体验，在蓝牙为打开时，手动打开设备的蓝牙功能界面，并采用startActivityForResult的
         * 方法来通知用户活动的结果
         */
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0x01);
        } else {
            ToastMgr.show("蓝牙已打开！");
            show_bt_device_list();
        }
        /**
         * 不做提示，直接打开蓝牙，不推荐使用
         * adapter.enable();
         */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 用来设置本蓝牙设备的可见性，即可以被其他的蓝牙设备搜索到
         */
        if (resultCode == 0x01) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
        ToastMgr.show("蓝牙已打开，请重新搜索！");
    }

    private void show_bt_device_list() {
        dialog = new MyDialog(mActivity, R.style.wifi_dialog);
        LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.btdevice_dialog, null);
        dialog.setContentView(layout);
        dialog.show();

        bt_device_list_cancel = (Button) dialog.findViewById(R.id.bt_dialog_cancel);
        bt_device_list_cancel.setOnClickListener(this);

        //将已经存在的配对设备先加入列表
        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
        Log.e(TAG, "已绑定设备数为：" + bondedDevices.size());

        if (bondedDevices.size() != 0) {
            Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
            while (iterator.hasNext()) {
                mlist.add((BluetoothDevice) iterator.next());
            }
        }
        Log.e(TAG,"mlist:" + mlist);
        deviceAdapter = new BTDeviceAdapter(mActivity, mlist);

        bt_device_list = (ListView) dialog.findViewById(R.id.bt_device_list);
        bt_device_list.setAdapter(deviceAdapter);
        scanBTDevices();
    }

    private void scanBTDevices() {
        Log.e(TAG, "scan BT Devices!");
        adapter.startDiscovery();
    }

    /**
     * 发送request请求天气bean，并且封装到weatherBean类中
     */
    private void getWeatherInfo() {
        GsonRequest<Weather> gsonRequest = new GsonRequest<>(Request.Method.GET, url, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather response) {
//                Log.d(TAG,"请求数据成功：" + response.toString());
                Log.d(TAG, response.getWeatherinfo().getRain());
                Log.d(TAG, response.getWeatherinfo().getRadar());
                content1.setText(response.getWeatherinfo().getCityid());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
        gsonRequest.setTag("firstGsonRequest");
        MyApplication.getInstance().getRequestQueue().add(gsonRequest);
    }
    /**
     *
     * 在弹出的9宫格分享页面中，会有很多平台，如果有些平台不希望显示，可以有两种方法，
     1.删除项目引用的两个工程之一的MainLibs目录下的libs里面对应的平台的jar包,删除九格宫不要的平台，只要删除对应平台的jar就行
     2.配置ShareSDK.conf文件，不想显示的平台设置Enable="false"
     */
    /**
     * ShareSDK集成方法有两种
     * <p>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库
     * <p>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式
     * 请看“ShareSDK
     * 使用说明文档”，SDK下载目录中
     * 或者看网络集成文档
     * http://wiki.sharesdk.cn/Android_%E5%BF%AB
     * %E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * <p>
     * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc
     * /cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.xml,
     */

    private void showShare() {
        //实例化一个OnekeyShare对象
        OnekeyShare oks = new OnekeyShare();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //指定微博平台，如果不添加这行，则弹出9宫格供用户选择
        oks.setPlatform(SinaWeibo.NAME);
        //分享内容的标题
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("新浪微博分享");
        // titleUrl是标题的对应的网址链接，仅在人人网和QQ空间使用,如果没有可以不设置
//        oks.setTitleUrl("http://sharesdk.cn");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
        //权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
        //注意：本地图片和网络图片，同时设置时，只分享本地图片
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setImagePath(fileName);
        //是否直接分享（true则直接分享），false是有九格宫，true没有
        oks.setSilent(false);
        //Platform.ShareParams sina_weibo = new Platform.ShareParams();
        //sina_weibo.setText("第一次分享" + " " + "www.baidu.com");
        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //sina_weibo.setImagePath("/sdcard/share_pic.jpg");//确保SDcard下面存在此张图片
        /*oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if(SinaWeibo.NAME.equals(platform.getName())) {
                    paramsToShare.setText("初始使用" + " " + "http://www.baidu.com");
                    paramsToShare.setUrl(null);
                }
            }
        });*/
        //设置分享的文本内容,所有平台都需要这个字段
        oks.setText("新浪微博第一次分享" + " " + "http://www.baidu.com");
        //url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        /*oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (SinaWeibo.NAME.equals(platform.getName())) {
                    paramsToShare.setText("分享文本" + " " + "www.baidu.com");
                    paramsToShare.setUrl(null);
                }
            }
        });*/
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //分享完成
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 1;
                msg.obj = platform;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                //分享失败
                throwable.printStackTrace();
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 2;
                msg.obj = throwable;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                //分享取消
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 3;
                msg.obj = platform;
                handler.sendMessage(msg);
            }
        });
        oks.show(mActivity);
    }

    /**
     * 消息处理
     */
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    if (checkedItem == 0) {
                        updateLanguage(Locale.SIMPLIFIED_CHINESE);//切换中文
                        EventBus.getDefault().post(new ChangeLanguageEvent(ChangeLanguageEvent.CHINESE));
                    } else {
                        updateLanguage(Locale.ENGLISH);//切换英文
                        EventBus.getDefault().post(new ChangeLanguageEvent(ChangeLanguageEvent.ENGLISH));
                    }
                    refreshView();
                    break;
                case MSG_TOAST: {
                    String text = String.valueOf(msg.obj);
                    Log.i("wzl", "text = " + text);
                    Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
                }
                break;
                case MSG_ACTION_CCALLBACK: {
                    switch (msg.arg1) {
                        case 1: { // 成功, successful notification
                            ToastMgr.show("分享成功");
                        }
                        break;
                        case 2: { // 失败, fail notification
                            ToastMgr.show("分享失败");
                        }
                        break;
                        case 3: { // 取消, cancel notification
                            ToastMgr.show("分享取消");
                        }
                        break;
                    }
                }
                break;
                case MSG_CANCEL_NOTIFY: {
                    NotificationManager nm = (NotificationManager) msg.obj;
                    if (nm != null) {
                        nm.cancel(msg.arg1);
                    }
                }
                //线程测试
                case 0x04:
                    new Thread(runnable).start();
                    break;
                case 0x05:
                    ToastMgr.show("测试开启线程!");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0x05);
            System.out.println("currnt thread ID = " + String.valueOf(Thread.currentThread().getId()));//子线程ID
            System.out.println("currnt thread ID = " + String.valueOf(Thread.currentThread().getName()));//主线程ID
        }
    };

    @Subscribe
    public void onEvent(ChangeLanguageEvent event) {
        String language = getResources().getConfiguration().locale.getCountry();
        Log.i("wzl", "language = " + language);
        refreshView();
    }

    /**
     * @param locale
     */
    private void updateLanguage(Locale locale) {
        Resources res = getResources();//获得res资源对象
        Configuration config = res.getConfiguration();//获得设置对象
        DisplayMetrics dm = res.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等
        config.locale = locale;
        res.updateConfiguration(config, dm);
    }

    public void refreshView() {
        onekey_share.setText(getResources().getString(R.string.onekey_share));
        lang_switch.setText(getResources().getString(R.string.lang_switch));
        content1.setText(getResources().getString(R.string.first_text));
    }

    /**
     * 设置静态广播，主要用于接收蓝牙搜索的结果
     */
    @Override
    public void onResume() {
        super.onResume();
        //广播过滤
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.setPriority(Integer.MAX_VALUE);//设置广播的优先级最大

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    String action = intent.getAction();
                    Log.e(TAG,"接收到蓝牙广播！" + "action = " + action);
                    switch (action) {
                        case BluetoothDevice.ACTION_FOUND:
                            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            if (device.getName() != null && device != null) {
                                Log.i(TAG, "device Name: " + device.getName());
                                Log.i(TAG, "device Addr: " + device.getAddress());
//                                Log.i(TAG, "device Bonded: " + device.getBondState());
                                /**
                                 * 方法一
                                 * 利用Activity.runOnUiThread(Runnable)把更新ui的代码创建在Runnable中，
                                 * 然后在需要更新 ui时，把这个Runnable对象传给Activity.runOnUiThread(Runnable)。
                                 * 这样Runnable对像就能在ui程序中被调用。如果当前线程是UI线程,那么行动是立即执行。
                                 * 如果当前线程不是UI线程,操作是发布到事件队列的UI线程
                                 */
                                if (mActivity instanceof Activity) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e(TAG, "开始添加设备！");
                                            addDevice(device);
                                        }
                                    });
                                }
                                /**
                                 * 方法二
                                 */
                            }
                            break;
                        case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                            System.out.println("ACTION_DISCOVERY_FINISHED");
                            Log.e(TAG, "扫描完成！");
                            if (mActivity instanceof Activity) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "finished 开始添加设备！");
                                        Set<BluetoothDevice> paired = adapter.getBondedDevices();
                                        Log.e(TAG, "paired = " + paired);
                                        if (paired != null && paired.size() > 0) {
                                            for (BluetoothDevice bonded : paired) {
                                                if (mlist.indexOf(bonded) == -1) {
                                                    mlist.add(bonded);
                                                    deviceAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                        Log.e(TAG, "扫描完成后的list: " + mlist);
                                    }
                                });
                            }
                            Log.e(TAG, "扫描完成后的list: " + mlist);
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        //注册蓝牙搜索结果的receiver
        mActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁EventBus
//        EventBus.getDefault().unregister(this);
        //注销ShareSDK
        ShareSDK.stopSDK();
        //注销蓝牙广播
        mActivity.unregisterReceiver(receiver);
    }

    /**
     * 获取和保存当前屏幕的截图
     * 用以第三方分享
     */
    private Bitmap GetandSavaCurrentImage(Activity activity) {
        // 1.获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 2.获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 3.获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 4.允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 5.去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

    /**
     * 将截取的图片保存到sdcard中并且命名
     */
    private void saveToSD(Bitmap bmp, String fileName) {
        //判断SD卡是否存在
        if (hasSdcard()) {
            Log.i("wzl", "存储卡存在，可以调用。");
            try {
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                if (null != fos) {
                    // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    // 用完关闭
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return 检查是否存在SDcard
     */
    public static boolean hasSdcard() {
        String sdcardstate = Environment.getExternalStorageState();
        if (sdcardstate.equals(Environment.MEDIA_MOUNTED)) {
            //有存储的SDCard
            return true;
        } else {
            Log.i("wzl", "没有检测到SDCard！");
            return false;
        }
    }

    private void addDevice(final BluetoothDevice device) {
        boolean deviceFound = false;
        for (final BluetoothDevice listDev : mlist) {
            if (listDev.getAddress().equals(device.getAddress())) {
                Log.e(TAG,"新搜索的设备与list中数据相同,因此不用添加！此时的mlist为：" + mlist);
                deviceFound = true;
                break;
            }
        }
        if (!deviceFound) {
            if (mlist.indexOf(device) == -1) { //防止重复添加
                mlist.add(device);
                deviceAdapter.notifyDataSetChanged();
                Log.i(TAG, "添加成功！添加后的list为：" + mlist);
            }
        }
    }
}