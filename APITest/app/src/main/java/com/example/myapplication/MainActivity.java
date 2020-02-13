package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());

        String isbn = "9781451648546";
        // String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:Potato";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            public void onResponse(String response) {
                textView.setText(response);
            }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                textView.setText(error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}
