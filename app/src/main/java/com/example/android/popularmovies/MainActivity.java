package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie?";
    private String API_KEY = "***REMOVED***";

    public static final int LOADER_VERSION = 1;
    private LoaderManager mLoaderManager;

    private String mQueryUrl = "";

    private MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main, new MovieFragment())
                    .commit();

        }

        mQueryUrl = MOVIE_QUERY_URL + API_KEY;

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(LOADER_VERSION, null, this);

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating Loader...");
        return new MovieLoader(this, mQueryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "Load finished...");
        mMovieAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mMovieAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(LOG_TAG, "Reset the loader");
        mMovieAdapter.clear();
        mLoaderManager.restartLoader(LOADER_VERSION, null, this);
    }
}
