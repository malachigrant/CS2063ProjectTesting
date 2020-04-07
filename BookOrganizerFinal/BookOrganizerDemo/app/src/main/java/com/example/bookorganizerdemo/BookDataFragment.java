package com.example.bookorganizerdemo;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BookDataFragment extends Fragment {
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String COMMENTS = "comments";
    public static final String RATING = "rating";
    public static final String LENT_TO = "lentTo";
    public static final String LENT_START_DATE = "lentStartDate";
    public static final String LENT_END_DATE = "lentEndDate";

    private static final int[] radioIds = { R.id.unreadButton, R.id.oneStarButton,
            R.id.twoStarsButton, R.id.threeStarsButton, R.id.fourStarsButton, R.id.fiveStarsButton};

    private String mTitle = "";
    private String mAuthor = "";
    private String mComments = "";
    private int mRating = 0;
    private String mLentTo = "";
    private String mLentStartDate = "";
    private String mLentEndDate = "";

    private TextView titleField;
    private TextView authorField;
    private TextView commentsField;
    private RadioGroup radioGroup;
    private TextView lentToField;
    private TextView lentStartField;
    private TextView lentEndField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(TITLE)) {
            mTitle = args.getString(TITLE);
            mAuthor = args.getString(AUTHOR);
            mComments = args.getString(COMMENTS);
            mRating = args.getInt(RATING);
            mLentTo = args.getString(LENT_TO);
            mLentStartDate = args.getString(LENT_START_DATE);
            mLentEndDate = args.getString(LENT_END_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);

        titleField = rootView.findViewById(R.id.titleField);
        authorField = rootView.findViewById(R.id.authorField);
        commentsField = rootView.findViewById(R.id.commentsField);
        radioGroup = rootView.findViewById(R.id.radioGroup);
        lentToField = rootView.findViewById(R.id.lentToField);
        lentStartField = rootView.findViewById(R.id.lentStartDate);
        lentEndField = rootView.findViewById(R.id.lentEndDate);


        titleField.setText(mTitle);
        authorField.setText(mAuthor);
        commentsField.setText(mComments);
        ((RadioButton) rootView.findViewById(radioIds[mRating])).setChecked(true);
        lentToField.setText(mLentTo);
        lentStartField.setText(mLentStartDate);
        lentEndField.setText(mLentEndDate);

        return rootView;
    }

    public String getTitle() {
        return titleField.getText().toString();
    }

    public String getAuthor() {
        return authorField.getText().toString();
    }

    public String getComments() {
        return commentsField.getText().toString();
    }

    public int getRating() {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        for (int i = 0; i < 6; i++) {
            if (radioIds[i] == checkedId) {
                return i;
            }
        }
        return 0; // default to unread
    }

    public String getLentTo() {
        return lentToField.getText().toString();
    }

    public String getLentStartDate() {
        return lentStartField.getText().toString();
    }

    public String getLentEndDate() {
        return lentEndField.getText().toString();
    }
}
