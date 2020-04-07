package com.example.bookorganizerdemo;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bookorganizerdemo.model.Book;
import com.example.bookorganizerdemo.util.HttpRequestUtil;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {

    private static final int SELECT_BOOK_CODE = 1;
    private static final int SCAN_BOOK_CODE = 2;

    Button barcodeButton;
    Button searchButton;
    Button addButton;
    EditText editText;
    HttpRequestUtil httpRequestUtil;
    BookDataFragment bookDataFragment;


    int mId = -1;
    String mApiId = "";
    String mTitle = "";
    String mAuthor = "";
    String mCover = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        barcodeButton = findViewById(R.id.barcodeButton);
        searchButton = findViewById(R.id.searchButton);
        addButton = findViewById(R.id.addBook);
        editText = findViewById(R.id.editText);

        FragmentManager fragmentManager = getSupportFragmentManager();
        bookDataFragment = (BookDataFragment) fragmentManager.findFragmentByTag("Book");
        if (bookDataFragment == null) {
            bookDataFragment = new BookDataFragment();
            Bundle args = new Bundle();

            bookDataFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.bookFragment, bookDataFragment, "Book").commit();
        }

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
            Book newBook = new Book(mApiId, bookDataFragment.getTitle(), bookDataFragment.getAuthor(), mCover);
            intent.putExtra("book", newBook);
            setResult(Activity.RESULT_OK, intent);
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
        Bundle arguments = new Bundle();
        arguments.putString(BookDataFragment.TITLE, mTitle);
        arguments.putString(BookDataFragment.AUTHOR, mAuthor);
        // TODO: Add more fields here.
        bookDataFragment = new BookDataFragment();
        bookDataFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bookFragment, bookDataFragment)
                .commit();
    }
    private void updateTextFields(Book book) {
        if (book != null) {
            mId = book.getId();
            mApiId = book.getApiId();
            mTitle = book.getTitle();
            mAuthor = book.getAuthor();
            mCover = book.getCover();
            updateTextFields();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
