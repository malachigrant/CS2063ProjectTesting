package com.example.bookorganizerdemo.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookorganizerdemo.model.Book;

import java.util.List;

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

    public void insert(Book book) {
        bookRepository.insertRecord(book);
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }
}
