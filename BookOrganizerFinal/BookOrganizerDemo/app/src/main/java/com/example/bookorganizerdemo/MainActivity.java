package com.example.bookorganizerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.bookorganizerdemo.database.BookViewModel;
import com.example.bookorganizerdemo.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_BOOK_RESULT_CODE = 1;

    BookViewModel mBookViewModel;
    Toolbar toolbar;
    SearchView searchView;
    Spinner sortSpinner;
    RecyclerView recyclerView;
    BookListAdapter adapter;
    FloatingActionButton addBook;
    List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sortSpinner = findViewById(R.id.sortSpinner);
        recyclerView = findViewById(R.id.recyclerView);

        mBooks = new ArrayList<>();

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable final List<Book> books) {
                adapter.replaceAll(books);
                mBooks = books;
            }
        });

        adapter = new BookListAdapter();
        recyclerView.setAdapter(adapter);

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

        outState.putParcelableArrayList("books", new ArrayList<>(mBooks));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mBooks = savedInstanceState.getParcelableArrayList("books");
        if (mBooks != null) {
            adapter.add(mBooks);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_BOOK_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Book book = data.getParcelableExtra("book");
            mBookViewModel.insert(book);
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
        for (Book book : mBooks) {
            if (book.getTitle().toLowerCase().contains(s.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public class BookListAdapter extends RecyclerView.Adapter<BookViewHolder> {

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
                return item1.getApiId() == item2.getApiId();
            }
        };
        private SortedList<Book> mDataset = new SortedList<>(Book.class, mCallback);

        public void setSortType(Book.SortType sortType) {
            if (mSortType != sortType) {
                mSortType = sortType;
                SortedList<Book> newDataset = new SortedList<>(Book.class, mCallback);
                newDataset.addAll(mBooks);
                mDataset = newDataset;
                notifyDataSetChanged();
            }
        }

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout textView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);

            return new BookViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int index) {
            final Book book = mDataset.get(index);

            if (book.getCover() != null && !book.getCover().isEmpty()) {
                Picasso.get() //Get the Image for the cover
                        .load(book.getCover())
                        .error(R.drawable.ic_image_24px) // TODO: use a better error image
                        .resize(100, 100)
                        .centerInside()
                        .into(holder.mCoverImageView);
            } else {
                holder.mCoverImageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_image_24px));
            }

            holder.mTitleTextView.setText(book.getTitle());
            holder.mAuthorTextView.setText(book.getAuthor());
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                    intent.putExtra("book", book);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void add(List<Book> models) {
            mDataset.addAll(models);
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
    }
}
