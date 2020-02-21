package com.example.bookorganizerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookorganizerdemo.model.Book;
import com.example.bookorganizerdemo.util.HttpRequestUtil;
import com.example.bookorganizerdemo.util.JsonUtil;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String ISBN_QUERY = "isbn:";
    private static final String TITLE_QUERY = "intitle:";

    private static final int SELECT_BOOK_CODE = 1;

    Button barcodeButton;
    Button searchButton;
    Button backButton;
    Button addButton;
    EditText editText;
    EditText titleField;
    EditText authorField;
    HttpRequestUtil httpRequestUtil;

    String mId = "";
    String mTitle = "";
    String mAuthor = "";

    private boolean isISBN(String str) {
        str = str.trim();
        if (str.length() == 10 && str.matches("^\\d{9}[\\d|X]")) {
            return true;
        } else if (str.length() == 13 && str.matches("^\\d{13}")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        barcodeButton = findViewById(R.id.barcodeButton);
        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.back);
        addButton = findViewById(R.id.addBook);
        editText = findViewById(R.id.editText);
        titleField = findViewById(R.id.titleField);
        authorField = findViewById(R.id.authorField);

        httpRequestUtil = new HttpRequestUtil(this);

        searchButton.setOnClickListener(view -> {
            String text = editText.getText().toString();
            httpRequestUtil.send(BASE_URL + (isISBN(text) ? ISBN_QUERY : TITLE_QUERY) + StringEscapeUtils.escapeHtml4(editText.getText().toString()),
                    s -> {
                        JsonUtil jsonUtil = new JsonUtil(s);
                        List<Book> books = jsonUtil.getBooks();
                        Intent intent = new Intent(AddBookActivity.this, BookSelectionActivity.class);
                        intent.putParcelableArrayListExtra("books", new ArrayList(books));
                        startActivityForResult(intent, SELECT_BOOK_CODE);
                    });
        });

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("book", new Book(mId, mTitle, mAuthor));
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_BOOK_CODE && resultCode == Activity.RESULT_OK) {
            Book result = data.getParcelableExtra("bookSelected");
            mId = result.getID();
            mTitle = result.getTitle();
            mAuthor = result.getAuthor();
            updateTextFields();
        }
    }

    private void updateTextFields() {
        titleField.setText(mTitle);
        authorField.setText(mAuthor);
    }
}
