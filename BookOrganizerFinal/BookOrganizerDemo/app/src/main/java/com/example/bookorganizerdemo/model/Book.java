package com.example.bookorganizerdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book implements Parcelable {
    public enum SortType {
        TITLE_A_Z,
        TITLE_Z_A,
        AUTHOR_A_Z,
        AUTHOR_Z_A
    }
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "apiId")
    private String apiId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "cover")
    private String cover;
    @ColumnInfo(name = "comments")
    private String comments;
    @ColumnInfo(name = "rating")
    private int rating;
    @ColumnInfo(name = "lentTo")
    private String lentTo;
    @ColumnInfo(name = "lentStartDate")
    private String lentStartDate;
    @ColumnInfo(name = "lentEndDate")
    private String lentEndDate;

    public static boolean isISBN(String str) {
        str = str.trim();
        if (str.length() == 10 && str.matches("^\\d{9}[\\d|X]")) {
            return true;
        } else if (str.length() == 13 && str.matches("^\\d{13}")) {
            return true;
        }
        return false;
    }

    public Book() {
    }

    public Book(String apiId, String title, String author) {
        this.apiId = apiId;
        this.title = title;
        this.author = author;
    }

    public Book(String apiId, String title, String author, String cover) {
        this.apiId = apiId;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        apiId = in.readString();
        title = in.readString();
        author = in.readString();
        cover = in.readString();
        comments = in.readString();
        rating = in.readInt();
        lentTo = in.readString();
        lentStartDate = in.readString();
        lentEndDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(apiId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(cover);
        dest.writeString(comments);
        dest.writeInt(rating);
        dest.writeString(lentTo);
        dest.writeString(lentStartDate);
        dest.writeString(lentEndDate);
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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getApiId() {
        return apiId;
    }
    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLentTo() {
        return lentTo;
    }
    public void setLentTo(String lentTo) {
        this.lentTo = lentTo;
    }

    public String getLentStartDate() {
        return lentStartDate;
    }
    public void setLentStartDate(String lentStartDate) {
        this.lentStartDate = lentStartDate;
    }

    public String getLentEndDate() {
        return lentEndDate;
    }
    public void setLentEndDate(String lentEndDate) {
        this.lentEndDate = lentEndDate;
    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
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