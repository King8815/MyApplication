package com.example.slide_table.volley_http;

import com.android.volley.Request;
import com.example.slide_table.bean.MapData;

import java.io.Serializable;

/**
 * Created by user on 2017/2/17.
 */
public class Response implements Serializable {
    public int code;//0 代表成功
    public String msg;
    public Object result;

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
