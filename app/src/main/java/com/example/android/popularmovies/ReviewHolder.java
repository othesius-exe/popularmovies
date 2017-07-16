package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 *
 */

public class ReviewHolder extends RecyclerView.ViewHolder {

    private TextView mAuthorView;
    private TextView mContentView;

    public ReviewHolder(View v) {
        super(v);
        mAuthorView = (TextView) v.findViewById(R.id.review_author_text_view);
        mContentView = (TextView) v.findViewById(R.id.review_content_text_view);
    }

    public TextView getAuthorView() {
        return mAuthorView;
    }

    public TextView getContentView() {
        return mContentView;
    }

    public void setAuthorView(TextView authorView) {
        mAuthorView = authorView;
    }

    public void setContentView(TextView contentView) {
        mContentView = contentView;
    }
}
