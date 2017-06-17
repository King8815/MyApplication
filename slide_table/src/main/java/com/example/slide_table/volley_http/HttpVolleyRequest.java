package com.example.slide_table.volley_http;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Iterator;

import publicTools.MyApplication;

/**
 * Created by user on 2017/1/18.
 */
public class HttpVolleyRequest<T> {
    private Activity activity;
    private NetListener<T> listener;

    public HttpVolleyRequest(){}

    public HttpVolleyRequest(Activity activity){
        this.activity = activity;
    }

    /**
     * Volley Get请求方法
     * @param url 请求服务器的地址
     * @param parentClass 解析的父类
     * @param listener 请求响应
     */
    public void HttpVolleyRequestGet(String url, Class<T> parentClass, NetListener<T> listener){
        GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.GET,url,parentClass,SuccessListener(),ErrorListener());
        MyApplication.getInstance().getRequestQueue().add(gsonRequest);
        this.listener = listener;
    }

    public void HttpVolleyRequestPost(String url, Class<T> parentClass, HashMap<String, String> map, NetListener<T> listener){
        GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.POST, url, parentClass, map, SuccessListener(), ErrorListener());
        MyApplication.getInstance().getRequestQueue().add(gsonRequest);
        this.listener = listener;
    }
    /**
     * 将接口函数进行重构
     * 请求成功回调函数
     * @return
     */
    private Response.Listener<T> SuccessListener(){
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (response == null) {
                    return;
                } else {
                    Log.d("request","测试成功");
                    listener.onSuccess(response);
                }
            }
        };
    }

    /**
     * 请求失败回调函数，返回response错误提示消息
     * @return
     */
    private Response.ErrorListener ErrorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null){
                    Log.e("request",error.getMessage(),error);
                    listener.onError(error);
                }
            }
        };
    }

    /**
     * 根据Map 转成parmas字符串
     * @param
     * @return
     */
    public String getParams(HashMap<String, String> map) {
        String str1 = "";
        if (null == map) {
            return str1;
        }
        // 参数为空
        if (map.isEmpty()) {
            return str1;
        }
        Iterator<String> localIterator = map.keySet().iterator();
        while (true) {
            if (!localIterator.hasNext()) {
                return str1.substring(0, -1 + str1.length());
            }
            String str2 = localIterator.next();
            str1 = str1 + str2 + "_" + map.get(str2) + ",";
        }
    }
}
