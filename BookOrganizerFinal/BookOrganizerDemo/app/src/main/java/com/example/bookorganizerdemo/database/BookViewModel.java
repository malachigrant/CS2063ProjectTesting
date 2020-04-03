package com.example.bookorganizerdemo.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookorganizerdemo.model.Book;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BookViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<List<Book>> mBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        mBooks = bookRepository.listAllRecords();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mBooks;
    }

    public void insert(String apiId, String title, String author, String cover) {
        bookRepository.insertRecord(apiId, title, author, cover);
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }
}
