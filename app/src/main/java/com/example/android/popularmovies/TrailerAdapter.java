package com.example.android.popularmovies;

import android.app.Activity;
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
 *
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private String DEFAULT_TRAILER_IMAGE = "https://img.youtube.com/vi/";
    private String DEFAULT_KEY = "/default.jpg";
    private String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private String mYoutubeTrailerPath = "";
    private Context mContext;

    public TrailerAdapter(Activity context, ArrayList<Trailer> trailers) {
        super(context, 0, trailers);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }

        Trailer trailer = getItem(position);
        String trailerId = trailer.getTrailerId();
        String trailerKey = trailer.getTrailerKey();
        String trailerImagePath = DEFAULT_TRAILER_IMAGE + trailerId + DEFAULT_KEY;
        mYoutubeTrailerPath = YOUTUBE_PATH + trailerKey;

        ImageView trailerImage = (ImageView) convertView.findViewById(R.id.trailer_image_view);

        Picasso.with(getContext()).load(trailerImagePath).into(trailerImage);


        return convertView;
    }
}
