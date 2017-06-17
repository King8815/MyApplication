package com.example.slide_table.volley_http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by user on 2017/1/16.
 * 一定要改类的泛型参数,默认的是String
 * 这里之所以要是T是因为不论放什么类进来都通用
 * 通过GSON解析json格式的response。
 * Gson parser
 * Gson解析json数据成本地的java对象
 */

/**
 * 从自定义的Requet中能够直接返回我们的JavaBean对象，便于我们对数据的操作
 *由于我们需要返回JavaBean对象，但是却不知道这个JavaBean对象具体指向谁，
 * 所以这里可以传入一个泛型T,在实现这个Request的时候就指定JavaBean对象用于Gson解析。
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {
    private static final String TAG = "GSON";

    private Context context;
    private Gson gson;
    private Map<String,String> params;
    private Map<String,String> headers;
    private Response.Listener<T> listener;
    private Class<T> clazz;

    /**
     *Make a GET request and return a parsed object from JSON
     * @param method 请求的方式
     * @param url 请求的服务器的地址
     * @param clazz clazz Relevant class object, for Gson's reflection,用于GSON解析json字符串封装数据
     * @param listener 数据请求成功后的回调响应
     * @param errorListener 数据请求失败后的回调响应
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        super(method,url,errorListener);
        gson = new Gson();
        this.listener = listener;
        this.clazz =clazz;

    }

    /**
     * Make a POST request and return a parsed object from JSON.
     * @param method 请求的方式
     * @param url 请求的服务器的地址
     * @param clazz clazz Relevant class object, for Gson's reflection,用于GSON解析json字符串封装数据
     * @param params POST请求传递的参数
     * @param listener 数据请求成功后的回调响应
     * @param errorListener 数据请求失败后的回调响应
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener
                       )
    {
        super(method,url,errorListener);
        gson = new Gson();
        this.listener = listener;
        this.params = params;
        this.clazz = clazz;
    }

    /**
     *
     * @param method 请求的方式
     * @param url 请求的服务器的地址
     * @param clazz clazz Relevant class object, for Gson's reflection,用于GSON解析json字符串封装数据
     * @param headers 请求头参数，可以为null
     * @param params 请求参数，可以为null
     * @param listener 数据请求成功后的回调响应
     * @param errorListener 数据请求失败后的回调响应
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        super(method,url,errorListener);
        gson = new Gson();
        this.listener = listener;
        this.params = params;
        this.headers = headers;
        this.clazz = clazz;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return (params != null) ? params : super.getParams();
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        return (headers != null) ? headers : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Class<T> getParentClass() {
        return clazz;
    }

    public void setParentClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 解析JSON数据，并且封装到clazz中
     * 通过NetworkResponse拿到调用String类的构造方法，
     * 将二进制字节组转换为一个Java字符串，用于我们的Gson解析，最后调用Json.fromJson将这个对象Response出去
     * @param response 网络请求的JSON数据
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG,getClass().getSimpleName()+jsonString);

            return Response.success(gson.fromJson(jsonString, clazz), HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e){
          return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 用于决定我们返回的数据对象
     * @param response
     */
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
