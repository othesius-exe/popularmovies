package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 */

public class DetailActivity extends AppCompatActivity {

    private String LOG_TAG = DetailActivity.class.getSimpleName();

    private String MOVIE_QUERY_URL = "https://api.themoviedb.org/3/movie/";
    private String API_KEY = "";
    private String APPEND_VIDEOS = "/videos";
    private String APPEND_REVIEWS = "&append_to_response=reviews";
    private String mTrailerUrl = "";
    private String mReviewUrl = "";

    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String IMAGE_WIDTH = "w185";
    private String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private String DEFAULT_TRAILER_IMAGE = "https://img.youtube.com/vi/";
    private String DEFAULT_KEY = "/default.jpg";
    private String QUESTION_KEY = "?";

    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;

    private Trailer mTrailer;
    private String mTrailerId;
    private String mTrailerKey;

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mDateView;
    private TextView mSynopsisView;
    private ImageView mTrailerImage;

    private LoaderManager mLoaderManager;
    private int REVIEW_LOADER = 1;
    private int TRAILER_LOADER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        mLoaderManager = getSupportLoaderManager();

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

            String title = movie.getTitle();
            int movieId = movie.getMovieId();
            Double rating = movie.getRating();
            String date = movie.getReleaseInfo();
            String synopsis = movie.getSynopsis();
            String image = movie.getImagePoster();

            mTrailerUrl = MOVIE_QUERY_URL + movieId + APPEND_VIDEOS + QUESTION_KEY + API_KEY;
            mReviewUrl = MOVIE_QUERY_URL + movieId + QUESTION_KEY + API_KEY;
            // Start the review and trailer loaders
            if (isConnected) {
                mLoaderManager.initLoader(TRAILER_LOADER, null, new TrailerCallback());
            }

            String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + image;
            String trailerImagePath = DEFAULT_TRAILER_IMAGE + mTrailerId + DEFAULT_KEY;
            String youtubeTrailerPath = YOUTUBE_PATH + mTrailerKey;

            mImageView = (ImageView) findViewById(R.id.detail_image_view);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(this).load(fullImagePath).into(mImageView);

            mTrailerImage = (ImageView) findViewById(R.id.trailer_image_view);
            mTrailerImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
            mTrailerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(this).load(trailerImagePath).into(mTrailerImage);
            Log.i(LOG_TAG, "Trailer Image path: " + trailerImagePath);
            Log.i(LOG_TAG, "Trailer Path: " + youtubeTrailerPath);

            mTitleView = (TextView) findViewById(R.id.detail_title_view);
            mTitleView.setText(title);

            mRatingView = (TextView) findViewById(R.id.detail_rating_view);
            String stringRating = String.valueOf(rating);
            mRatingView.setText(stringRating);

            mDateView = (TextView) findViewById(R.id.detail_date_view);
            mDateView.setText(date);

            mSynopsisView = (TextView) findViewById(R.id.detail_synopsis_view);
            mSynopsisView.setText(synopsis);
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

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");
        if (movie != null) {
            String title = movie.getTitle();
            Double rating = movie.getRating();
            String date = movie.getReleaseInfo();
            String synopsis = movie.getSynopsis();
            String image = movie.getImagePoster();

            String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + image;
        }

        switch (id) {
            case R.id.action_add_to_favorites:

        }

        return true;
    }

    private class TrailerCallback implements LoaderManager.LoaderCallbacks<List<Trailer>> {

        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailerLoader(DetailActivity.this, mTrailerUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            Log.i(LOG_TAG, "Load finished." + data);
            if (data != null && !data.isEmpty()) {
                mTrailerList.addAll(data);
                mTrailerId = mTrailerList.get(0).getTrailerId();
                mTrailerKey = mTrailerList.get(0).getTrailerKey();
                mTrailer = new Trailer(mTrailerKey, mTrailerId);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            mLoaderManager.restartLoader(TRAILER_LOADER, null, this);
        }
    }
}
