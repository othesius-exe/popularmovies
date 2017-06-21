package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie/";
    private String API_KEY = "";
    private String POPULAR_PARAM = "popular?";
    private String TOP_RATED_PARAM = "top_rated?";

    private String mFullUrl;
    private static final int MOVIE_LOADER_ID = 1;
    private LoaderManager mLoaderManager;

    private ImageAdapter mImageAdapter;
    private ArrayList<Movie> mMovieList;

    private GridView mGridView;
    private LinearLayout mEmptyView;

    private static String SORT_ORDER = "Sort";
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_TOP_RATED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        API_KEY = getResources().getString(R.string.apiKey);
        mFullUrl = MOVIE_QUERY_URL + POPULAR_PARAM + API_KEY;

        mGridView = (GridView) findViewById(R.id.grid_view);
        mImageAdapter = new ImageAdapter(this, new ArrayList<Movie>());
        mGridView.setAdapter(mImageAdapter);
        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);

        mMovieList = new ArrayList<>();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            mLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mGridView.setEmptyView(mEmptyView);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                Movie movie = new Movie(mMovieList.get(position).getTitle(),
                        mMovieList.get(position).getRating(),
                        mMovieList.get(position).getReleaseInfo(),
                        mMovieList.get(position).getImagePoster(),
                        mMovieList.get(position).getSynopsis());
                detailIntent.putExtra("movie", movie);
                startActivity(detailIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (id) {

            case R.id.action_sort_popular:
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_POPULAR).apply();
                mFullUrl = MOVIE_QUERY_URL + POPULAR_PARAM + API_KEY;
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
            case R.id.action_sort_top_rated:
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_TOP_RATED).apply();
                mFullUrl = MOVIE_QUERY_URL + TOP_RATED_PARAM + API_KEY;
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating Loader");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(LOG_TAG, "Prefs " + sharedPreferences);

        return new MovieLoader(this, mFullUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "Load finished");
        mMovieList.clear();
        if (data != null && !data.isEmpty()) {
            mEmptyView.setVisibility(View.GONE);
            mMovieList.addAll(data);
            mImageAdapter.addAll(mMovieList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovieList.clear();
        mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("movieList", mMovieList);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovieList = savedInstanceState.getParcelableArrayList("movieList");
    }
}
