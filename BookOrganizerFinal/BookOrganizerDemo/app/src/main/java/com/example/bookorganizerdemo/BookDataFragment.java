package com.example.bookorganizerdemo;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BookDataFragment extends Fragment {
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";

    private String mTitle = "";
    private String mAuthor = "";

    private TextView titleField;
    private TextView authorField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(TITLE)) {
            mTitle = args.getString(TITLE);
            mAuthor = args.getString(AUTHOR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);

        titleField = rootView.findViewById(R.id.titleField);
        titleField.setText(mTitle);
        authorField = rootView.findViewById(R.id.authorField);
        authorField.setText(mAuthor);

        return rootView;
    }

    public String getTitle() {
        return titleField.getText().toString();
    }

    public String getAuthor() {
        return authorField.getText().toString();
    }
}
