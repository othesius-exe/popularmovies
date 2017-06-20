package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 *
 */

public class MovieLoader extends android.support.v4.content.AsyncTaskLoader<List<Movie>> {

    private String LOG_TAG = MovieLoader.class.getSimpleName();

    private String mURL;

    public MovieLoader(Context context, String url) {
        super(context);
        Log.v(LOG_TAG, "Url in loader: " + url);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "Searching for movies.");
    }

    @Override
    public List<Movie> loadInBackground() {
        Log.i(LOG_TAG, "Populating movie list.");
        if(mURL == null) {
            return null;
        }
        List<Movie> movies = QueryUtils.fetchMovieData(mURL);
        return movies;
    }

}
