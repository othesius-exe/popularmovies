package com.example.android.popularmovies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }

        Review review = getItem(position);

        TextView authorView = (TextView) convertView.findViewById(R.id.review_author_text_view);
        TextView contentView = (TextView) convertView.findViewById(R.id.review_content_text_view);

        String author = "";
        String content = "";

        author = review.getAuthor();

        if (review.getContent().equals("")) {
            content = getContext().getResources().getString(R.string.no_reviews);
        } else {
            content = review.getContent();
        }

        authorView.setText(author);
        contentView.setText(content);

        return convertView;
    }
}
