package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie?";
    private String API_KEY = "***REMOVED***";

    public static final int LOADER_VERSION = 1;
    private LoaderManager mLoaderManager;

    private String mQueryUrl = "";

    private ArrayList<Movie> mMovieList;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mProgressbar;
    private String mUserQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mMovieAdapter = new MovieAdapter(this, mMovieList);

        mQueryUrl = MOVIE_QUERY_URL + API_KEY;

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(LOADER_VERSION, null, this);

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating Loader...");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mProgressbar.setVisibility(View.VISIBLE);
        return new MovieLoader(this, mQueryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mProgressbar.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.sort_order:
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
}
