package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 *
 */

public class TrailerLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

    private String LOG_TAG = TrailerLoader.class.getSimpleName();

    private String mUrl;

    public TrailerLoader(Context context, String url) {
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
    public ArrayList<Trailer> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<Trailer> trailers;
        trailers = TrailerQueryUtils.fetchTrailerData(mUrl);
        return trailers;
    }
}
