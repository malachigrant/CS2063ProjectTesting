package com.example.bookorganizerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
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
import java.util.concurrent.Callable;

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

        // books.add(new Book("1", "Title 1", "Author"));
        // books.add(new Book("2", "A book", "auth"));
        adapter = new BookListAdapter();
        recyclerView.setAdapter(adapter);
        // adapter.add(books);

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
            mBookViewModel.insert(book.getApiId(), book.getTitle(), book.getAuthor(), book.getCover());
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
        public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout textView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);

            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int index) {
            final Book book = mDataset.get(index);

            Picasso.get() //Get the Image for the cover
                    .load(book.getCover())
                    // .resize(50, 50)
                    // .centerCrop()
                    .into(holder.imageView);

            holder.mTextView.setText(book.getTitle());
            holder.mAuthorTextView.setText(book.getAuthor());
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Currently clicking on a book deletes it from the list.
                    // TODO: Change this to open the book detail view instead of deleting the item.
                    mBookViewModel.deleteBook(book);
                }
            });

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
            public RelativeLayout mLayout;
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.item_textview);
                mAuthorTextView = itemView.findViewById(R.id.item_author_textview);
                mLayout = itemView.findViewById(R.id.itemLayout);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }
}
