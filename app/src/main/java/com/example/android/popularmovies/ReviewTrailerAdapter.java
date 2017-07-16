package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private String mTrailerKey;
    private String mTrailerId;
    private String mFullYoutubePath;
    private Context mContext;

    public ReviewTrailerAdapter(Context context, ArrayList<Object> objects) {
        mObjects = objects;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case REVIEW:
                // Create a holder for Review Objects
                View reviewHolder = inflater.inflate(R.layout.review_item, parent, false);
                viewHolder = new ReviewHolder(reviewHolder);
                break;
            case TRAILER:
                // Create a holder for Trailer Objects
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
                String content = "";
                ReviewHolder reviewHolder = (ReviewHolder) holder;
                Review review = (Review) mObjects.get(position);
                for (int r = 0; r < mObjects.size(); r++) {
                    if (mObjects.get(r) instanceof Review) {
                        TextView authorView = ((ReviewHolder) holder).getAuthorView();
                        TextView contentView = ((ReviewHolder) holder).getContentView();
                        if (review.getContent().equals("")) {
                            content = mContext.getResources().getString(R.string.no_reviews);
                        } else {
                            content = review.getContent();
                        }
                        authorView.setText(review.getAuthor());
                        contentView.setText(content);
                        configureReviewHolder(reviewHolder, position);
                    }
                }
                break;
            case TRAILER:
                TrailerHolder trailerHolder = (TrailerHolder) holder;
                Trailer trailer = (Trailer) mObjects.get(position);
                for (int i = 0; i < mObjects.size(); i++) {
                    // Get each trailer in the Objects Array
                    if (mObjects.get(i) instanceof Trailer) {
                        mTrailerKey = trailer.getTrailerKey();
                        ImageView imageView = ((TrailerHolder) holder).getImageView();
                        Context context = ((TrailerHolder) holder).getImageView().getContext();
                        String youTubeImagePath = DEFAULT_TRAILER_IMAGE + trailer.getTrailerKey() + DEFAULT_KEY;
                        configureTrailerHolder(trailerHolder, position);
                        Picasso.with(context).load(youTubeImagePath).into(imageView);

                        // Handle click events to allow the user to watch the trailer on YouTube
                        AdapterView.OnClickListener mOnClickListener = new AdapterView.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                                mFullYoutubePath = YOUTUBE_PATH + mTrailerKey;
                                trailerIntent.setData(Uri.parse(mFullYoutubePath));
                                mContext.startActivity(trailerIntent);
                            }
                        };
                        imageView.setOnClickListener(mOnClickListener);
                    }
                }
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
