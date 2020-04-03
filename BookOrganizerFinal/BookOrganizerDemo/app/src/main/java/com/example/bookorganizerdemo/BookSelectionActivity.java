package com.example.bookorganizerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.bookorganizerdemo.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_selection);

        List<Book> books = getIntent().getParcelableArrayListExtra("books");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new BookSelectionAdapter(books));
    }

    private class BookSelectionAdapter extends RecyclerView.Adapter<BookViewHolder> {

        List<Book> mDataset;

        public BookSelectionAdapter(List<Book> books) {
            mDataset = books;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new BookViewHolder(v);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int position) {
            Book book = mDataset.get(position);

            holder.mTitleTextView.setText(book.getTitle());
            holder.mAuthorTextView.setText(book.getAuthor());
            Picasso.get()
                    .load(book.getCover())
                    .resize(100, 100)
                    .centerInside()
                    .into(holder.mCoverImageView);

            holder.mView.setOnClickListener(view -> {
                Intent result = new Intent();
                result.putExtra("bookSelected", book);
                setResult(Activity.RESULT_OK, result);
                finish();
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
