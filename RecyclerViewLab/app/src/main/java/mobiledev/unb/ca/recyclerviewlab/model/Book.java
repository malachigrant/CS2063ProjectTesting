package mobiledev.unb.ca.recyclerviewlab.model;

import android.support.annotation.NonNull;

public class Book {
    private final String id;
    private final String title;
    private final String subtitle;

    private Book (Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
    }

    // Only need to include getters/
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public static class Builder {
        private String id;
        private String title;
        private String subtitle;

        public Builder(@NonNull String id, @NonNull String title) {
            this.id = id;
            this.title = title;
            //this.subtitle = subtitle;
        }

        public Book build() {
            return new Book(this);
        }
    }
}