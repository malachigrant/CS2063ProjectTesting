package com.example.bookorganizerdemo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bookorganizerdemo.model.Book;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> mBooks;

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        bookDao = db.bookDao();
        mBooks = bookDao.listAllRecords();
    }

    public void insertRecord(final Book book) {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.insertBooks(book);
        });
    }

    public LiveData<List<Book>> listAllRecords() {
        return mBooks;
    }

    public void deleteBook(Book book) {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.deleteBook(book);
        });
    }

    public void updateBook(Book book) {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.updateBook(book);
        });
    }
}
