package com.example.bookorganizerdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public enum SortType {
        TITLE_A_Z,
        TITLE_Z_A,
        AUTHOR_A_Z,
        AUTHOR_Z_A
    }

    private final String id;
    private final String title;
    private final String author;

    public static boolean isISBN(String str) {
        str = str.trim();
        if (str.length() == 10 && str.matches("^\\d{9}[\\d|X]")) {
            return true;
        } else if (str.length() == 13 && str.matches("^\\d{13}")) {
            return true;
        }
        return false;
    }

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // Only need to include getters/
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int compare(Book other, SortType sortType) {
        int tempResult = 0;
        switch (sortType) {
            case TITLE_A_Z:
            case TITLE_Z_A:
                tempResult = this.title.compareTo(other.title);
                break;
            case AUTHOR_A_Z:
            case AUTHOR_Z_A:
                tempResult = this.author.compareTo(other.author);
                break;
        }
        if (sortType == SortType.AUTHOR_Z_A || sortType == SortType.TITLE_Z_A) {
            return tempResult * -1;
        }
        return tempResult;
    }
}