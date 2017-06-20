package com.example.android.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to add movie images to gridview
 */

public class MovieFragment extends Fragment {
    @Bind(R.id.detail_image_view)ImageView imageView;
    @Bind(R.id.detail_title_view)TextView titleView;
    @Bind(R.id.detail_date_view)TextView dateView;
    @Bind(R.id.detail_rating_view)TextView ratingView;
    @Bind(R.id.detail_synopsis_view)TextView summaryView;

    public MovieFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_details, container, false);
        //Butter knife library for binding views
        ButterKnife.bind(this, rootView);
        Movie movie = getActivity().getIntent().getParcelableExtra("movie");
        //Setting back poster of movie
        Picasso.with(getActivity()).load(movie.getImagePoster()).into(imageView);
        //Setting title of movie
        titleView.setText(movie.getTitle());
        //Setting release date of movie
        dateView.setText(movie.getReleaseInfo());
        //Setting ratings of movie
        String text = movie.getRating()+"";
        ratingView.setText(text);
        //Setting summary of movie
        summaryView.setText(movie.getSynopsis());
        return rootView;
    }

}
