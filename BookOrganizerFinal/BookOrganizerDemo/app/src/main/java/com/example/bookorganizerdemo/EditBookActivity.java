package com.example.bookorganizerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bookorganizerdemo.database.BookViewModel;
import com.example.bookorganizerdemo.model.Book;

public class EditBookActivity extends AppCompatActivity {

    private Button backButton;
    private Button saveButton;
    private Button deleteButton;
    private BookDataFragment  bookDataFragment;
    BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        backButton = findViewById(R.id.back);
        saveButton = findViewById(R.id.saveBook);
        deleteButton = findViewById(R.id.deleteBook);

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookViewModel.deleteBook(getIntent().getParcelableExtra("book"));
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = getIntent().getParcelableExtra("book");
                book.setTitle(bookDataFragment.getTitle());
                book.setAuthor(bookDataFragment.getAuthor());
                // TODO: Add more fields
                mBookViewModel.updateBook(book);
                finish();
            }
        });

        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("book");
        FragmentManager fragmentManager = getSupportFragmentManager();
        bookDataFragment = (BookDataFragment) fragmentManager.findFragmentByTag("Book");
        if (bookDataFragment == null) {
            bookDataFragment = new BookDataFragment();
            Bundle args = new Bundle();

            args.putString(BookDataFragment.TITLE, book.getTitle());
            args.putString(BookDataFragment.AUTHOR, book.getAuthor());
            // TODO: Add more fields here.

            bookDataFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.editFragment, bookDataFragment, "Book").commit();
        }
    }
}
