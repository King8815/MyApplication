package com.example.slide_table.volley_logic;

import com.example.slide_table.volley_http.NetListener;

import java.util.HashMap;

/**
 * 网络封装的请求类，可以使用链式方式调用
 * Created by user on 2017/2/17.
 */
public class NetworkParams<T> {
    /** 请求参数 */
    private HashMap<String, String> params;
    /** 请求网址 */
    private String url;
    /** 解析基类 */
    private Class<T> baseClass;
    /** 解析子类 */
    private Class<T> childClass;
    /** 请求回调 */
    private NetListener<T> listener;

    /**
     * 重置请求参数，不会重置url等其他参数
     * @return
     */
    public NetworkParams<T> resetParams(){
        if (params != null){
            params.clear();
        }
        return this;
    }

    /**
     * 重置所有参数
     * @return
     */
    public NetworkParams<T> resetAll(){
        if (params != null){
            params.clear();
        }
        url = "";
        listener = null;
        return this;
    }

    /**
     * 增加参数
     * @param key
     * @param value
     * @return
     */
    public NetworkParams<T> addParams(String key, String value){
        if (params == null){
            params = new HashMap<String, String>();
        }
        params.put(key, value);
        return this;
    }

    /**
     * 获取请求参数
     * @return
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * 设置请求参数
     * @param params
     */
    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    /**
     * 获取请求地址
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求地址
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取解析基类
     * @return
     */
    public Class<T> getBaseClass() {
        return baseClass;
    }

    /**
     * 设置解析基类
     * @param baseClass
     */
    public void setBaseClass(Class<T> baseClass) {
        this.baseClass = baseClass;
    }

    /**
     * 获取解析子类
     * @return
     */
    public Class<T> getChildClass() {
        return childClass;
    }

    /**
     * 设置解析子类
     * @param childClass
     */
    public void setChildClass(Class<T> childClass) {
        this.childClass = childClass;
    }

    /**
     * 获取请求回调
     * @return
     */
    public NetListener<T> getListener() {
        return listener;
    }

    /**
     * 设置请求回调
     * @param listener
     */
    public void setListener(NetListener<T> listener) {
        this.listener = listener;
    }
}
