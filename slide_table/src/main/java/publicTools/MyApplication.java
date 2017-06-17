package publicTools;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by user on 2017/1/5.
 */
public class MyApplication extends Application {
    /**
     * log or request TAG
     */
    private static final String TAG = MyApplication.class.getSimpleName();
    /**
     * A singleton instance of the application class for easy access in other
     * places
      */
    private static MyApplication instance;

    private RequestQueue requestQueue;
    /**
     * 4种网络数据请求
     *
     * private StringRequest stringRequest;
     * private JsonObjectRequest jsonObjectRequest;
     * private ImageRequest imageRequest;
     */

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//实例初始化
    }

    /**
     *
     * @return 实例对象
     */
    public static MyApplication getInstance() {
        if (null == instance){
            instance = new MyApplication();
        }
        return instance;
    }

    /**
     *
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        if (null == requestQueue){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param request
     * @param tag
     * @param <T>
     */
    public <T> void addToRequest(Request<T> request, String tag){
        //set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding tag to request : %s", request.getUrl());

        getRequestQueue().add(request);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     * @param request
     * @param <T>
     */
    public <T> void addToRequest(Request<T> request){
        //set the default tag
        request.setTag(TAG);

        getRequestQueue().add(request);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequest(Object tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }
}
