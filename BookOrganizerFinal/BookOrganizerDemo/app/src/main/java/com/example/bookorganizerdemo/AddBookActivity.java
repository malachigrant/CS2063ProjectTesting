package com.example.bookorganizerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.bookorganizerdemo.model.Book;
import com.example.bookorganizerdemo.util.HttpRequestUtil;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {

    private static final int SELECT_BOOK_CODE = 1;
    private static final int SCAN_BOOK_CODE = 2;
    private static final int CAMERA_PERMISSION_CODE = 3;

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
    String mComments = "";
    int mRating = 0;
    String mLentTo = "";
    String mLentStartDate = "";
    String mLentEndDate = "";

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
            newBook.setComments(bookDataFragment.getComments());
            newBook.setRating(bookDataFragment.getRating());
            newBook.setLentTo(bookDataFragment.getLentTo());
            newBook.setLentStartDate(bookDataFragment.getLentStartDate());
            newBook.setLentEndDate(bookDataFragment.getLentEndDate());
            intent.putExtra("book", newBook);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        barcodeButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(AddBookActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openBarcodeScanner();
            }
        });
    }

    private void openBarcodeScanner() {
        Intent intent = new Intent(AddBookActivity.this, ScannerActivity.class);
        startActivityForResult(intent, SCAN_BOOK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openBarcodeScanner();
            } else {
                Toast.makeText(AddBookActivity.this, "Camera permission is required to scan barcode", Toast.LENGTH_LONG).show();
            }
        }
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
        arguments.putString(BookDataFragment.COMMENTS, mComments);
        arguments.putInt(BookDataFragment.RATING, mRating);
        arguments.putString(BookDataFragment.LENT_TO, mLentTo);
        arguments.putString(BookDataFragment.LENT_START_DATE, mLentStartDate);
        arguments.putString(BookDataFragment.LENT_END_DATE, mLentEndDate);
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
            mComments = book.getComments();
            mRating = book.getRating();
            mLentTo = book.getLentTo();
            mLentStartDate = book.getLentStartDate();
            mLentEndDate = book.getLentEndDate();
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
