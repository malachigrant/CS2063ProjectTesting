package mobiledev.unb.ca.recyclerviewlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import mobiledev.unb.ca.recyclerviewlab.model.Book;
import mobiledev.unb.ca.recyclerviewlab.util.JsonUtils;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG,"Here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // TODO 1
        //  Get the ArrayList of Courses from the JsonUtils class
        //  (Ideally we would do this loading off of the main thread. We'll get to that
        //  in the next lab. Today we're focusing on displaying scrolling lists.)

        JsonUtils jsonUtils = new JsonUtils(this);
        ArrayList<Book> books = jsonUtils.getBooks();

        // TODO 2
        //  Get a reference to the RecyclerView and set its adapter
        //  to be an instance of MyAdapter, which you will need to create
        //  using the ArrayList of courses from above.
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new SearchActivity.MyAdapter(books));

    }

    // The RecyclerView.Adapter class provides a layer of abstraction between the
    // RecyclerView's LayoutManager and the underlying data that is being displayed,
    // in this case the ArrayList of Course objects.
    public class MyAdapter extends RecyclerView.Adapter<SearchActivity.MyAdapter.ViewHolder> {
        private ArrayList<Book> mDataset;

        public MyAdapter(ArrayList<Book> myDataset) {
            mDataset = myDataset;
        }

        //Search for a book via book name
       /* public String searchByName(String toSearch){
            String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + toSearch;
            return url;
        }

        //Returns the URL to search via if using ISBN (through barcode scanner) to search
        public String searchByISBN(String toSearch){
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + toSearch;
            return url;
        }

        /*public void getBookList(String url){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            holder.mTextView.setText(response);
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    textView.setText(error.getMessage());
                }
            });
            requestQueue.add(stringRequest);
        }*/

        // ViewHolder represents an individual item to display. In this case
        // it will just be a single TextView (displaying the title of a course)
        // but RecyclerView gives us the flexibility to do more complex things
        // (e.g., display an image and some text).
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // The inflate method of the LayoutInflater class can be used to obtain the
        // View object corresponding to an XML layout resource file. Here
        // onCreateViewHolder inflates the TextView corresponding to item_layout.xml
        // and uses it to instantiate a ViewHolder.
        @Override
        public SearchActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);

            return new SearchActivity.MyAdapter.ViewHolder(v);
        }


        // onBindViewHolder binds a ViewHolder to the data at the specified
        // position in mDataset
        @Override
        public void onBindViewHolder(SearchActivity.MyAdapter.ViewHolder holder, int position) {
            // TODO 3
            //  Get the Course at index position in mDataSet
            //  (Hint: you might need to declare this variable as final.)
            final Book book = mDataset.get(position);

            // TODO 4
            //  Set the TextView in the ViewHolder (holder) to be the title for this Course
            holder.mTextView.setText(book.getTitle());

            // TODO 5
            //  Set the onClickListener for the TextView in the ViewHolder (holder) such
            //  that when it is clicked, it creates an explicit intent to launch DetailActivity
            //  HINT: You will need to put two extra pieces of information in this intent:
            //      The Course title and it's description
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                    intent.putExtra("title", book.getTitle());
                    intent.putExtra("subtitle", book.getSubtitle());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}