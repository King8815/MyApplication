package com.example.slide_table.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.slide_table.R;
import com.example.slide_table.adapter.WifiListAdapter;

import java.util.List;

/**
 * Created by user on 2016/11/23.
 */
public class WifiListDialog extends Dialog{
    private Dialog dialog;
    private Context context;
    private List<ScanResult> list;
    private WifiListAdapter adapter;
    private LayoutInflater inflater;
    private ListView listView;

    public WifiListDialog(Context context) {
        super(context);
        init(context);
    }

    public WifiListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init(Context context) {
        View view = inflater.from(context).inflate(R.layout.wifi_list_item, null);
        dialog = new Dialog(context, R.style.CommonDialog);
        dialog.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        dialog.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog.dismiss();
    }

    @Override
    public void create() {
        super.create();
    }
}
