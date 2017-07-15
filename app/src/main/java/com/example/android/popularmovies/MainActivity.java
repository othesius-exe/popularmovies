package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import android.widget.ProgressBar;

import com.example.android.popularmovies.data.UserFavoritesContract.FavoritesEntry;
import com.example.android.popularmovies.data.UserFavoritesDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie/";
    private String API_KEY = "";
    private String APPEND_VIDEOS_AND_REVIEWS = "&append_to_response=videos,reviews";
    private String POPULAR_PARAM = "popular?";
    private String TOP_RATED_PARAM = "top_rated?";

    private String mFullUrl;
    private static final int MOVIE_LOADER_ID = 1;
    private int FAVORITES_LOADER_ID = 2;
    private LoaderManager mLoaderManager;

    private ImageAdapter mImageAdapter;
    private ArrayList<Movie> mMovieList;

    private Cursor mFavoritesCursor;

    private GridView mGridView;
    private LinearLayout mEmptyView;

    private static String SORT_ORDER = "Sort";
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_TOP_RATED = 1;
    private static final int SORT_ORDER_FAVORITES = 2;

    private UserFavoritesDbHelper mDbHelper;

    private int mMovieId = 0;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        API_KEY = getResources().getString(R.string.apiKeys);
        mFullUrl = MOVIE_QUERY_URL + POPULAR_PARAM + API_KEY + APPEND_VIDEOS_AND_REVIEWS;

        mGridView = (GridView) findViewById(R.id.grid_view);
        mImageAdapter = new ImageAdapter(this, new ArrayList<Movie>());

        mEmptyView = (LinearLayout) findViewById(R.id.empty_view);
        mEmptyView.setVisibility(View.INVISIBLE);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(MOVIE_LOADER_ID, null, new MovieCallback());
        mDbHelper = new UserFavoritesDbHelper(this);

        mMovieList = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            mLoaderManager.initLoader(MOVIE_LOADER_ID, null, new MovieCallback());
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mGridView.setEmptyView(mEmptyView);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                mMovieId = mMovieList.get(position).getMovieId();
                mFullUrl = MOVIE_QUERY_URL + mMovieId + API_KEY + APPEND_VIDEOS_AND_REVIEWS;
                Movie movie = new Movie(mMovieList.get(position).getTitle(),
                        mMovieList.get(position).getRating(),
                        mMovieList.get(position).getReleaseInfo(),
                        mMovieList.get(position).getImagePoster(),
                        mMovieList.get(position).getSynopsis(),
                        mMovieList.get(position).getMovieId());
                detailIntent.putExtra("movie", movie);
                detailIntent.putExtra("url", mFullUrl);
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
                mMovieList.clear();
                mFullUrl = MOVIE_QUERY_URL + POPULAR_PARAM + API_KEY + APPEND_VIDEOS_AND_REVIEWS;
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, new MovieCallback());
                return true;
            case R.id.action_sort_top_rated:
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_TOP_RATED).apply();
                mMovieList.clear();
                mFullUrl = MOVIE_QUERY_URL + TOP_RATED_PARAM + API_KEY + APPEND_VIDEOS_AND_REVIEWS;
                mLoaderManager.restartLoader(MOVIE_LOADER_ID, null, new MovieCallback());
                return true;
            case R.id.action_sort_favorites:
                mMovieList.clear();
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_FAVORITES).apply();
                mLoaderManager.initLoader(FAVORITES_LOADER_ID, null, new FavoritesCallback());
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    private class MovieCallback implements LoaderManager.LoaderCallbacks<List<Movie>> {

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            Log.i(LOG_TAG, "Creating Loader");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Log.i(LOG_TAG, "Prefs " + sharedPreferences);
            mProgressBar.setVisibility(View.VISIBLE);
            return new MovieLoader(MainActivity.this, mFullUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Log.i(LOG_TAG, "Load finished");
            mMovieList.clear();
            mGridView.setAdapter(mImageAdapter);
            if (data != null && !data.isEmpty()) {
                mProgressBar.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mMovieList.addAll(data);
                mImageAdapter.addAll(mMovieList);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            mMovieList.clear();
            mLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    private class FavoritesCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {
                    FavoritesEntry.COLUMN_MOVIE_ID,
                    FavoritesEntry.COLUMN_TITLE,
                    FavoritesEntry.COLUMN_RATING,
                    FavoritesEntry.COLUMN_POSTER_PATH,
                    FavoritesEntry.COLUMN_SUMMARY,
                    FavoritesEntry.COLUMN_RELEASE_INFO};

            return new CursorLoader(MainActivity.this, FavoritesEntry.CONTENT_URI,
                    projection, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.v(LOG_TAG, "Data: " + data);
            mMovieList.clear();
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            mFavoritesCursor = db.rawQuery("SELECT * FROM favorites", null);

            while (mFavoritesCursor.moveToNext()) {

                int titleColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_TITLE);
                int idColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_ID);
                int ratingColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_RATING);
                int posterColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH);
                int summaryColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_SUMMARY);
                int releaseColumnIndex = mFavoritesCursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_INFO);
                Movie movie = new Movie(mFavoritesCursor.getString(titleColumnIndex),
                        mFavoritesCursor.getDouble(ratingColumnIndex),
                        mFavoritesCursor.getString(releaseColumnIndex),
                        mFavoritesCursor.getString(posterColumnIndex),
                        mFavoritesCursor.getString(summaryColumnIndex),
                        mFavoritesCursor.getInt(idColumnIndex));
                mMovieList.add(movie);
            }
            mImageAdapter.addAll(mMovieList);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mLoaderManager.initLoader(FAVORITES_LOADER_ID, null, this);
        }
    }
}
