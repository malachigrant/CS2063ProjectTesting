package com.example.bookorganizerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.bookorganizerdemo.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_BOOK_RESULT_CODE = 1;

    Toolbar toolbar;
    SearchView searchView;
    Spinner sortSpinner;
    RecyclerView recyclerView;
    BookListAdapter adapter;
    FloatingActionButton addBook;
    List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sortSpinner = findViewById(R.id.sortSpinner);
        recyclerView = findViewById(R.id.recyclerView);

        books = new ArrayList<>();
        // books.add(new Book("1", "Title 1", "Author"));
        // books.add(new Book("2", "A book", "auth"));
        adapter = new BookListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.add(books);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSortType(Book.SortType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        addBook = findViewById(R.id.addBook);
        addBook.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivityForResult(intent, ADD_BOOK_RESULT_CODE);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("books", new ArrayList<>(books));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        books = savedInstanceState.getParcelableArrayList("books");
        if (books != null) {
            adapter.add(books);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_BOOK_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Book book = data.getParcelableExtra("book");
            books.add(book);
            adapter.add(book);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar, menu);

        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<Book> filteredModelList = filter(s);
                adapter.replaceAll(filteredModelList);
                recyclerView.scrollToPosition(0);
                return true;
            }
        });
        return true;
    }

    private List<Book> filter(String s) {
        final List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(s.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

        private Book.SortType mSortType = Book.SortType.TITLE_A_Z;
        private SortedList.Callback<Book> mCallback = new SortedList.Callback<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.compare(o2, mSortType);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Book oldItem, Book newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Book item1, Book item2) {
                return item1.getID() == item2.getID();
            }
        };
        private SortedList<Book> mDataset = new SortedList<>(Book.class, mCallback);

        public BookListAdapter() {
        }

        public void setSortType(Book.SortType sortType) {
            if (mSortType != sortType) {
                mSortType = sortType;
                SortedList<Book> newDataset = new SortedList<>(Book.class, mCallback);
                newDataset.addAll(books);
                mDataset = newDataset;
                notifyDataSetChanged();
            }
        }

        @Override
        public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout textView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);

            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int index) {
            final Book book = mDataset.get(index);

            holder.mTextView.setText(book.getTitle());
            holder.mAuthorTextView.setText(book.getAuthor());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void add(Book model) {
            mDataset.add(model);
        }

        public void remove(Book model) {
            mDataset.remove(model);
        }

        public void add(List<Book> models) {
            mDataset.addAll(models);
        }

        public void remove(List<Book> models) {
            mDataset.beginBatchedUpdates();
            for (Book model : models) {
                mDataset.remove(model);
            }
            mDataset.endBatchedUpdates();
        }

        public void replaceAll(List<Book> models) {
            mDataset.beginBatchedUpdates();
            for (int i = mDataset.size() - 1; i >= 0; i--) {
                final Book model = mDataset.get(i);
                if (!models.contains(model)) {
                    mDataset.remove(model);
                }
            }
            mDataset.addAll(models);
            mDataset.endBatchedUpdates();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public TextView mAuthorTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.item_textview);
                mAuthorTextView = itemView.findViewById(R.id.item_author_textview);
            }
        }
    }
}
