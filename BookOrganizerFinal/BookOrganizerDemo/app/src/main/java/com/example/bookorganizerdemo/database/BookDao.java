package com.example.bookorganizerdemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookorganizerdemo.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(Book... books);

    @Query("SELECT * from books ORDER BY title ASC")
    LiveData<List<Book>> listAllRecords();

    @Update
    void updateBook(Book book);

    @Delete
    void deleteBook(Book book);
}
