package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie/";
    private String API_KEY = "***REMOVED***";
    private String QUERY_PARAM = "&query=";
    private String DEFAULT_PARAM = "popular?";

    private String mFullUrl;
    private static final int MOVIE_LOADER_ID = 1;
    private LoaderManager mLoaderManager;

    private ImageAdapter mImageAdapter;
    private ArrayList<Movie> mMovieList;

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFullUrl = MOVIE_QUERY_URL + API_KEY;

        mGridView = (GridView) findViewById(R.id.grid_view);
        mImageAdapter = new ImageAdapter(this, new ArrayList<Movie>());
        mGridView.setAdapter(mImageAdapter);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(MOVIE_LOADER_ID, null, this);

        mMovieList = new ArrayList<>();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating Loader");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return new MovieLoader(this, "https://api.themoviedb.org/3/movie/popular?***REMOVED***");
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "Load finished");

        mMovieList.addAll(data);
        mImageAdapter.addAll(mMovieList);

        if (data != null && !data.isEmpty()) {
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
