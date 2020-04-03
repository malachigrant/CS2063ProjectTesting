package com.example.bookorganizerdemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public TextView mTitleTextView;
    public TextView mAuthorTextView;
    public ImageView mCoverImageView;
    public RelativeLayout mLayout;

    public BookViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mTitleTextView = itemView.findViewById(R.id.item_textview);
        mAuthorTextView = itemView.findViewById(R.id.item_author_textview);
        mCoverImageView = itemView.findViewById(R.id.image_cover);
        mLayout = itemView.findViewById(R.id.itemLayout);
    }
}
