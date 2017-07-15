package com.example.android.popularmovies;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.UserFavoritesContract.FavoritesEntry;
import com.example.android.popularmovies.data.UserFavoritesDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Review> mReviewArrayList;

    // Trailer Variables
    private String mTrailerId;
    private String mTrailerKey;
    private String mTrailerImagePath;
    private String mYoutubeTrailerPath;

    // Views
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mDateView;
    private TextView mSynopsisView;
    private ImageView mTrailerImage;
    private ListView mReviewList;
    private ListView mTrailerListView;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

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
        mLoaderManager = getSupportLoaderManager();
        mTrailerList = new ArrayList<>();
        mReviewArrayList = new ArrayList<>();
        mReviewList = (ListView) findViewById(R.id.review_list);
        mDbHelper = new UserFavoritesDbHelper(this);

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

            mReviewList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

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
            return new TrailerLoader(DetailActivity.this, mTrailerUrl);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            Log.i(LOG_TAG, "Load finished." + data);
            if (data != null && !data.isEmpty()) {
                mTrailerList.addAll(data);
                Log.v(LOG_TAG, "Trailers in list: " + mTrailerList.toString());
                LayoutInflater inflater = (LayoutInflater) DetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (Trailer t : mTrailerList) {
                    LinearLayout trailerList = (LinearLayout) findViewById(R.id.trailer_list_view);
                    mTrailerId = t.getTrailerId();
                    mTrailerKey = t.getTrailerKey();
                    mTrailerImagePath = DEFAULT_TRAILER_IMAGE + mTrailerKey + DEFAULT_KEY;
                    mYoutubeTrailerPath = YOUTUBE_PATH + mTrailerKey;
                    View v = inflater.inflate(R.layout.trailer_item, null);
                    TextView titleView = (TextView) v.findViewById(R.id.trailer_title_view);
                    titleView.setText(t.getTrailerTitle());
                    ImageView trailerImage = (ImageView) v.findViewById(R.id.trailer_image_view);
                    Picasso.with(DetailActivity.this).load(mTrailerImagePath).into(trailerImage);
                    trailerList.addView(v);

                    trailerImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri trailerLink = Uri.parse(mYoutubeTrailerPath);
                            Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
                            List activities = getPackageManager().queryIntentActivities(trailerIntent, PackageManager.MATCH_DEFAULT_ONLY);
                            boolean isIntentSafe = activities.size() > 0;
                            if (isIntentSafe) {
                                startActivity((trailerIntent));
                            }
                        }
                    });
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    }

        /**
     * Implementation for the ReviewLoader
     */
    private class ReviewCallback implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

        // Create the Loader with the url to fetch reviews
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewLoader(DetailActivity.this, mReviewUrl);
        }

        // Add the Reviews to the list and set the ReviewAdapter on the list
        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            Log.i(LOG_TAG, "Load finished." + data);
            if (data != null && !data.isEmpty()) {
                mReviewArrayList.addAll(data);
                mReviewAdapter = new ReviewAdapter(DetailActivity.this, mReviewArrayList);
                mReviewList.setAdapter(mReviewAdapter);
                setListViewHeightBasedOnChildren(mReviewList);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }
    }

    // Sets the parameters for the ListView which holds the Review Objects
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams((desiredWidth), ActionBar.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
        listView.setLayoutParams(params);
    }
}
