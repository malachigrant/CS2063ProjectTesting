package com.example.bookorganizerdemo.util;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpRequestUtil {
    String TAG = "HttpRequestUtil";
    Context context;

    public HttpRequestUtil(Context context) {
        this.context = context;
    }

    public void send(String url, final Listener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> listener.onResult(response), error -> {
                    Log.w(TAG, "Http error: " + error.getMessage());
                    listener.onResult(null);
                });
        requestQueue.add(stringRequest);
    }

    public interface Listener {
        void onResult(String s);
    }
}
