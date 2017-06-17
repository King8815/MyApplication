package com.example.slide_table.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.slide_table.R;

/**
 * Created by user on 2016/10/18.
 */
public class ShareDialog extends Dialog{
    private Dialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private ImageView iv_share_sina, iv_share_qqzone, iv_share_wxfriend;

    public ShareDialog(Context context){
        super(context);
        init(context);
    }
    private void init(Context context){
        View view = inflater.from(context).inflate(R.layout.bottom_share_dialog, null);
        dialog = new Dialog(context, R.style.shareDialog_style);
        dialog.setContentView(view);

        RelativeLayout rl_dialog_bg = (RelativeLayout)view.findViewById(R.id.rl_dialog_bg);
        rl_dialog_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //设置铺满
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);//设置显示位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标

        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
		lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
       /* //设置铺满
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay()
                .getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);*/

        iv_share_sina = (ImageView) view.findViewById(R.id.iv_share_sina);
        iv_share_qqzone = (ImageView) view.findViewById(R.id.iv_share_qqzone);
        iv_share_wxfriend = (ImageView) view.findViewById(R.id.iv_share_wxfriend);
    }

    /**
     * 设置新浪微博监听事件
     */
    public void setSinaClickListener(View.OnClickListener clickListener){
        iv_share_sina.setOnClickListener(clickListener);
    }
    /**
     * 设置QQ空间监听事件
     */
    public void setQQZoneClickListener(View.OnClickListener clickListener){
        iv_share_qqzone.setOnClickListener(clickListener);
    }
    /**
     * 设置微信朋友圈监听事件
     */
    public void setWXFriendClickListener(View.OnClickListener clickListener){
        iv_share_wxfriend.setOnClickListener(clickListener);
    }
    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
