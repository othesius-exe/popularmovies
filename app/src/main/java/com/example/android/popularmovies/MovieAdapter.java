package com.example.android.popularmovies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter (Activity context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_detail, parent, false);
        }

        Movie thisMovie = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.detail_image_view);
        TextView titleView = (TextView) convertView.findViewById(R.id.detail_title_view);
        TextView dateView = (TextView) convertView.findViewById(R.id.detail_date_view);
        TextView ratingView = (TextView) convertView.findViewById(R.id.detail_rating_view);
        TextView synopsisView = (TextView) convertView.findViewById(R.id.detail_synopsis_view);

        if (thisMovie.getImagePoster() != null && !thisMovie.getImagePoster().isEmpty()) {
            Picasso.with(getContext()).load(thisMovie.getImagePoster()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.broken_link);
        }
        String stringRating = thisMovie.getRating().toString();
        titleView.setText(thisMovie.getTitle());
        dateView.setText(thisMovie.getReleaseInfo());
        ratingView.setText(stringRating);
        synopsisView.setText(thisMovie.getSynopsis());

        return convertView;
    }
}
