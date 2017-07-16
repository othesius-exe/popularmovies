package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.UserFavoritesContract.FavoritesEntry;
import com.example.android.popularmovies.data.UserFavoritesDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */

public class DetailActivity extends AppCompatActivity {

    private String LOG_TAG = DetailActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie/";
    private String API_KEY = "";
    private String APPEND_VIDEOS = "/videos";
    private String APPEND_REVIEWS = "/reviews";
    private String mTrailerUrl = "";
    private String mReviewUrl = "";

    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String IMAGE_WIDTH = "w185";
    private String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private String DEFAULT_TRAILER_IMAGE = "https://img.youtube.com/vi/";
    private String DEFAULT_KEY = "/default.jpg";
    private String API_PARAM = "?api_key=";

    private ArrayList<Review> mReviewArrayList;

    // Views
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mDateView;
    private TextView mSynopsisView;
    private RecyclerView mTrailerListView;
    private RecyclerView mReviewListView;
    private ReviewTrailerAdapter mReviewAdapter;
    private ReviewTrailerAdapter mTrailerAdapter;
    private ArrayList<Object> mReviews;
    private ArrayList<Object> mTrailers;
    private LinearLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;

    // Loader Managers
    private LoaderManager mLoaderManager;
    private int REVIEW_LOADER = 1;
    private int TRAILER_LOADER = 2;

    // Database Tools
    private UserFavoritesDbHelper mDbHelper;

    // Movie Variables
    private String mTitle;
    private int mMovieId;
    private String mDate;
    private String mPosterPath;
    private Double mRating;
    private String mSummary;

    private ContentValues mValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Loader Manager for your Loader Callbacks
        mLoaderManager = getSupportLoaderManager();

        // Objects Array of Trailers and Reviews to send to the RecyclerAdapter
        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();

        // RecyclerViews to hold Trailers and Reviews
        mReviewListView = (RecyclerView) findViewById(R.id.review_list);
        mTrailerListView = (RecyclerView) findViewById(R.id.trailer_list_view);

        // Database Helper
        mDbHelper = new UserFavoritesDbHelper(this);

        // Layout Managers for Reviews and Trailers
        mTrailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // My API key
        API_KEY = getResources().getString(R.string.apiKeys);

        // Make sure that we are still connected
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Get the intent that opened this activity
        // Extract the movie details that were passed along
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");
        if (movie != null) {

            mTitle = movie.getTitle();
            mMovieId = movie.getMovieId();
            mRating = movie.getRating();
            mDate = movie.getReleaseInfo();
            mSummary = movie.getSynopsis();
            mPosterPath = movie.getImagePoster();

            mTrailerUrl = MOVIE_QUERY_URL + mMovieId + APPEND_VIDEOS + API_PARAM + API_KEY;
            mReviewUrl = MOVIE_QUERY_URL + mMovieId + APPEND_REVIEWS + API_PARAM + API_KEY;

            // Start the review and trailer loaders
            if (isConnected) {
                mLoaderManager.initLoader(TRAILER_LOADER, null, new TrailerCallback());
                mLoaderManager.initLoader(REVIEW_LOADER, null, new ReviewCallback());
            }

            String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + mPosterPath;

            mImageView = (ImageView) findViewById(R.id.detail_image_view);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(this).load(fullImagePath).into(mImageView);

            mTitleView = (TextView) findViewById(R.id.detail_title_view);
            mTitleView.setText(mTitle);

            mRatingView = (TextView) findViewById(R.id.detail_rating_view);
            String stringRating = String.valueOf(mRating);
            mRatingView.setText(stringRating);

            mDateView = (TextView) findViewById(R.id.detail_date_view);
            mDateView.setText(mDate);

            mSynopsisView = (TextView) findViewById(R.id.detail_synopsis_view);
            mSynopsisView.setText(mSummary);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_to_favorites:
                addToFavorites();
                break;
            case R.id.action_remove_from_favorites:
                removeFromFavorites();
        }

        return true;
    }

    /**
     * Add Item to the database
     */
    private void addToFavorites() {
        // Get the database to write to
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Associate values with the correct table columns
        mValues = new ContentValues();
        mValues.put(FavoritesEntry.COLUMN_TITLE, mTitle);
        mValues.put(FavoritesEntry.COLUMN_MOVIE_ID, mMovieId);
        mValues.put(FavoritesEntry.COLUMN_POSTER_PATH, mPosterPath);
        mValues.put(FavoritesEntry.COLUMN_RATING, mRating);
        mValues.put(FavoritesEntry.COLUMN_RELEASE_INFO, mDate);
        mValues.put(FavoritesEntry.COLUMN_SUMMARY, mSummary);

        // Insert the values into the table
        Long newFavoriteId = db.insertWithOnConflict(FavoritesEntry.TABLE_NAME, null,
                mValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.v(LOG_TAG, "New Row ID: " + newFavoriteId);

        if (newFavoriteId == -1) {
            Toast.makeText(this, "Movie not added to favorites.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Movie successfully added to favorites!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Remove item from the database
     */
    private void removeFromFavorites() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        if (FavoritesEntry.CONTENT_URI != null) {
            int rowsDeleted = db.delete(FavoritesEntry.TABLE_NAME, null , null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Failed to remove item or item does not exist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Movie removed from favorites!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Implementation for the Trailer Loader
     */
    private class TrailerCallback implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
            mTrailers.clear();
            return new TrailerLoader(DetailActivity.this, mTrailerUrl);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            Log.i(LOG_TAG, "Load finished." + data);
            mTrailers.clear();
            if (data != null && !data.isEmpty()) {
                mTrailers.addAll(data);
                mTrailerAdapter = new ReviewTrailerAdapter(DetailActivity.this, mTrailers);
                mTrailerLayoutManager.scrollToPosition(0);
                mTrailerListView.setLayoutManager(mTrailerLayoutManager);
                mTrailerListView.setAdapter(mTrailerAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
            mTrailerAdapter.notifyDataSetChanged();
        }
    }

        /**
     * Implementation for the ReviewLoader
     */
    private class ReviewCallback implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

        // Create the Loader with the url to fetch reviews
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
            mReviews.clear();
            return new ReviewLoader(DetailActivity.this, mReviewUrl);
        }

        // Add the Reviews to the list and set the ReviewAdapter on the list
        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            Log.i(LOG_TAG, "Load finished." + data);
            mReviews.clear();
            if (data != null && !data.isEmpty()) {
                mReviews.addAll(data);
                mReviewAdapter = new ReviewTrailerAdapter(DetailActivity.this, mReviews);
                mReviewListView.setLayoutManager(mReviewLayoutManager);
                mReviewListView.setAdapter(mReviewAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {
            mTrailerAdapter.notifyDataSetChanged();
        }
    }
}
