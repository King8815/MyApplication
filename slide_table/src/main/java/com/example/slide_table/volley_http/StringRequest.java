package com.example.slide_table.volley_http;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by user on 2017/1/19.
 */
public class StringRequest extends Request<String>{
    private static Context context;
    private Response.Listener<String> listener;

    public StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(method, url,errorListener);
        this.listener = listener;
    }
    public StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, url, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(string,HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}
