package com.example.slide_table.volley_logic;

import android.util.Log;

import com.example.slide_table.volley_http.HttpVolleyRequest;

/**
 * 基础逻辑，内部包含一个NetworkParams<T>
 * Created by user on 2017/2/17.
 */
public class BaseLogic<T> {

    private NetworkParams<T> params = new NetworkParams<T>();

    public BaseLogic(){};


    /**
     * 获取网络参数并进行编辑
     * @return
     */
    public NetworkParams<T> edit(){
        return params;
    }

    /*public NetworkParams<T> getParams() {
        return params;
    }*/

    /**
     * 强制性设置网络参数
     * @param params
     */
    public void setParams(NetworkParams<T> params) {
        this.params = params;
    }

    /**
     * 发送Get请求
     */
    public void  doGet(){
        HttpVolleyRequest<T> request = new HttpVolleyRequest<>();
        request.HttpVolleyRequestGet(params.getUrl(),params.getBaseClass(),params.getListener());
        Log.e("request", params.getUrl());
    }

    /**
     * 发送Post请求
     */
    public void doPost(){
        HttpVolleyRequest<T> request = new HttpVolleyRequest<>();
        request.HttpVolleyRequestPost(params.getUrl(),params.getBaseClass(),params.getParams(),params.getListener());
        Log.e("URL", params.getUrl());
        Log.e("params", params.getParams().toString());
    }

}
