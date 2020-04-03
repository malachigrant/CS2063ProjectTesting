package com.example.bookorganizerdemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookorganizerdemo.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books WHERE title LIKE :name")
    public abstract List<Book> searchBooks(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertBooks(Book... books);

    @Query("SELECT * from books ORDER BY title ASC")
    public LiveData<List<Book>> listAllRecords();

    @Query("SELECT cover from books WHERE title LIKE :name")
    public abstract String findCover(String name);

    @Delete
    public void deleteBook(Book book);
}
