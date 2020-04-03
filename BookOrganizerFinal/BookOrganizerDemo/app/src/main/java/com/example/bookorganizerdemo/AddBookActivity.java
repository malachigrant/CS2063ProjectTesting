package com.example.bookorganizerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bookorganizerdemo.model.Book;
import com.example.bookorganizerdemo.util.HttpRequestUtil;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {

    private static final int SELECT_BOOK_CODE = 1;
    private static final int SCAN_BOOK_CODE = 2;

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
            HttpRequestUtil.Listener listener = books -> {
                if (books != null) {
                    Intent intent = new Intent(AddBookActivity.this, BookSelectionActivity.class);
                    intent.putParcelableArrayListExtra("books", new ArrayList<Parcelable>(books));
                    startActivityForResult(intent, SELECT_BOOK_CODE);
                }
            };
            if (Book.isISBN(text)) {
                httpRequestUtil.requestByISBN(text, listener);
            } else {
                httpRequestUtil.requestByTitle(text, listener);
            }
        });

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("book", new Book(mId, mTitle, mAuthor));
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        backButton.setOnClickListener(view -> {
            finish();
        });

        barcodeButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddBookActivity.this, ScannerActivity.class);
            startActivityForResult(intent, SCAN_BOOK_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_BOOK_CODE && resultCode == Activity.RESULT_OK) {
            Book result = data.getParcelableExtra("bookSelected");
            updateTextFields(result);
        } else if (requestCode == SCAN_BOOK_CODE && resultCode == Activity.RESULT_OK) {
            Book result = data.getParcelableExtra("scannedBook");
            updateTextFields(result);
        }
    }

    private void updateTextFields() {
        titleField.setText(mTitle);
        authorField.setText(mAuthor);
    }
    private void updateTextFields(Book book) {
        if (book != null) {
            mId = book.getApiId();
            mTitle = book.getTitle();
            mAuthor = book.getAuthor();
            updateTextFields();
        }
    }
}
