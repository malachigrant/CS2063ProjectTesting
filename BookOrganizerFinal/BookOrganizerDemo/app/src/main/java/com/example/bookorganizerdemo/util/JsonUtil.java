package com.example.bookorganizerdemo.util;

import android.util.Log;

import com.example.bookorganizerdemo.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    List<Book> mBooks = new ArrayList<>();
    public JsonUtil(String str) {
        try {
            JSONObject json = new JSONObject(str);
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String id = item.getString("id");
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String author = volumeInfo.has("authors") ? join(volumeInfo.getJSONArray("authors"), ", ") : "";

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String cover = imageLinks.getString("smallThumbnail");
                Book book = new Book(id, title, author, cover);
                mBooks.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String join(JSONArray arr, String sep) {
        String result = "";
        try {
            for (int i = 0; i < arr.length(); i++) {
                result += (i == 0 ? "" : sep) + arr.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Book> getBooks() {
        return mBooks;
    }
}
