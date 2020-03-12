package com.example.bookorganizerdemo.util;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookorganizerdemo.model.Book;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpRequestUtil {
    private static final String TAG = "HttpRequestUtil";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String ISBN_QUERY = "isbn:";
    private static final String TITLE_QUERY = "intitle:";
    Context context;


    public HttpRequestUtil(Context context) {
        this.context = context;
    }

    public void send(String url, final Listener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JsonUtil jsonUtil = new JsonUtil(response);
            listener.onResult(jsonUtil.getBooks());
        }, error -> {
            Log.w(TAG, "Http error: " + error.getMessage());
            listener.onResult(new ArrayList<>());
        });
        requestQueue.add(stringRequest);
    }

    public void requestByISBN(String isbn, final Listener listener) {
        send(BASE_URL + ISBN_QUERY + isbn, listener);
    }
    public void requestByTitle(String title, final Listener listener) {
        send(BASE_URL + TITLE_QUERY + StringEscapeUtils.escapeHtml4(title), listener);
    }

    public interface Listener {
        void onResult(List<Book> s);
    }
}
