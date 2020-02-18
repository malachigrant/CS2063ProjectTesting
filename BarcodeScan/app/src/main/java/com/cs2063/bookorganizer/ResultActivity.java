package com.cs2063.bookorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ResultActivity extends Activity {

    TextView infoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");

        infoText = findViewById(R.id.info);
        infoText.setMovementMethod(new ScrollingMovementMethod());
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        infoText.setText(response);
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                infoText.setText(error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}
