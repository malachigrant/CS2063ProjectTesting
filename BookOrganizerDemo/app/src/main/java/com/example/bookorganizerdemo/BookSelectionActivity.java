package com.example.bookorganizerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookorganizerdemo.model.Book;

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

    private class BookSelectionAdapter extends RecyclerView.Adapter<BookSelectionAdapter.ViewHolder> {

        List<Book> mDataset;

        public BookSelectionAdapter(List<Book> books) {
            mDataset = books;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Book book = mDataset.get(position);

            holder.mTextView.setText(book.getTitle());

            holder.mTextView.setOnClickListener(view -> {
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(TextView textView) {
                super(textView);
                mTextView = textView;
            }
        }
    }
}
