package mobiledev.unb.ca.recyclerviewlab.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import mobiledev.unb.ca.recyclerviewlab.model.Course;
import mobiledev.unb.ca.recyclerviewlab.model.Book;

public class JsonUtils {

    private static final String BOOKS_JSON_FILE = "Books.json";

    private static final String KEY_ITEMS = "items";
    private static final String KEY_BOOK_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subtitle";

    private ArrayList<Book> bookArray;

    // Initializer to read our data source (JSON file) into an array of book objects
    public JsonUtils(Context context) {
        processJSON(context);
    }

    private void processJSON(Context context) {
        bookArray = new ArrayList<>();

        try {
            // Create a JSON Object from file contents String
            JSONObject jsonObject = new JSONObject(loadJSONFromAssets(context));

            // Create a JSON Array from the JSON Object
            // This array is the "courses" array mentioned in the lab write-up
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);

            for (int i=0; i < jsonArray.length(); i++) {
                // Create a JSON Object from individual JSON Array element
                JSONObject elementObject = jsonArray.getJSONObject(i);

                JSONObject volumeInfo = elementObject.getJSONObject("volumeInfo");

                // Get data from individual JSON Object
                Book book = new Book.Builder(elementObject.getString(KEY_BOOK_ID),
                        volumeInfo.getString(KEY_TITLE))
                        //volumeInfo.getString(KEY_SUBTITLE))
                        .build();

                // Add new Course to courses ArrayList
                bookArray.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open(BOOKS_JSON_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    // Getter method for courses ArrayList
    public ArrayList<Book> getBooks() {
        return bookArray;
    }
}
