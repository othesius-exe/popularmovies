package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {
    private String LOG_TAG = TrailerLoader.class.getSimpleName();

    private String mUrl;

    public ReviewLoader(Context context, String url) {
        super(context);
        Log.v(LOG_TAG, "Url in loader: " + url);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "Searching for trailers.");
    }

    @Override
    public ArrayList<Review> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<Review> reviews;
         reviews = ReviewQueryUtils.fetchReviewData(mUrl);
        return reviews;
    }
}
