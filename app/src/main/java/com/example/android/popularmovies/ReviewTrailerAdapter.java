package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */

public class ReviewTrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> mObjects;

    private final int TRAILER = 0, REVIEW = 1;
    private String DEFAULT_TRAILER_IMAGE = "https://img.youtube.com/vi/";
    private String DEFAULT_KEY = "/default.jpg";

    public ReviewTrailerAdapter(ArrayList<Object> objects) {
        mObjects = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case REVIEW:
                View reviewHolder = inflater.inflate(R.layout.review_item, parent, false);
                viewHolder = new ReviewHolder(reviewHolder);
                break;
            case TRAILER:
                View trailerHolder = inflater.inflate(R.layout.trailer_item, parent, false);
                viewHolder = new TrailerHolder(trailerHolder);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case REVIEW:
                ReviewHolder reviewHolder = (ReviewHolder) holder;
                Review review = (Review) mObjects.get(position);
                configureReviewHolder(reviewHolder, position);
                break;
            case TRAILER:
                TrailerHolder trailerHolder = (TrailerHolder) holder;
                Trailer trailer = (Trailer) mObjects.get(position);
                Context context = ((TrailerHolder) holder).getImageView().getContext();
                String youTubeImagePath = DEFAULT_TRAILER_IMAGE + trailer.getTrailerKey() + DEFAULT_KEY;
                configureTrailerHolder(trailerHolder, position);
                Picasso.with(context).load(youTubeImagePath).into(((TrailerHolder) holder).getImageView());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof Trailer) {
            return TRAILER;
        } else if (mObjects.get(position) instanceof Review) {
            return REVIEW;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    private void configureReviewHolder(ReviewHolder holder, int position) {
        Review review = (Review) mObjects.get(position);
        if (review != null) {
            holder.getAuthorView().setText(review.getAuthor());
            holder.getContentView().setText(review.getContent());
        }
    }

    private void configureTrailerHolder(TrailerHolder holder, int position) {
        Trailer trailer = (Trailer) mObjects.get(position);
        if (trailer != null) {
            holder.getTextView().setText(trailer.getTrailerTitle());
            holder.getImageView().setImageResource(R.drawable.tmdb);

        }
    }
}
