package com.example.slide_table.volley_http;

import com.android.volley.VolleyError;

/**
 * Created by user on 2017/2/17.
 */
public interface NetListener<T> {
    /**
     * 请求成功时，调用此回调
     * @param response 解析后的数据
     */
    void onSuccess(T response);

    /**
     * 请求失败时，调用此回调
     * @param error
     */
    void onError(VolleyError error);
}
