package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for adding Movies to Lists
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_image, parent, false);
        }

        ImageView movieImage = (ImageView) convertView.findViewById(R.id.image_view);
        Picasso.with(getContext())
                .load(movie.getImagePoster())
                .into(movieImage);

        return convertView;
    }
}
