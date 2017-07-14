package com.example.android.popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private String APPEND_VIDEOS = "&append_to_response=videos";
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
    private ArrayList<Review> mReviewArrayList;

    private Trailer mTrailer;
    private String mTrailerId;
    private String mTrailerKey;
    private String mTrailerImagePath;
    private String mYoutubeTrailerPath;

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mDateView;
    private TextView mSynopsisView;
    private ImageView mTrailerImage;
    private ListView mReviewList;
    private ReviewAdapter mReviewAdapter;

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
        mTrailerList = new ArrayList<>();
        mReviewArrayList = new ArrayList<>();
        mReviewList = (ListView) findViewById(R.id.review_list);

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

            mTrailerUrl = MOVIE_QUERY_URL + movieId + QUESTION_KEY + API_KEY + APPEND_VIDEOS;
            mReviewUrl = MOVIE_QUERY_URL + movieId + QUESTION_KEY + API_KEY + APPEND_REVIEWS;

            // Start the review and trailer loaders
            if (isConnected) {
                mLoaderManager.initLoader(TRAILER_LOADER, null, new TrailerCallback());
                mLoaderManager.initLoader(REVIEW_LOADER, null, new ReviewCallback());
            }

            String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + image;

            mImageView = (ImageView) findViewById(R.id.detail_image_view);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(this).load(fullImagePath).into(mImageView);

            mTrailerImage = (ImageView) findViewById(R.id.trailer_image_view);
            mTrailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri trailerLink = Uri.parse(mYoutubeTrailerPath);
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerLink);
                    PackageManager packageManager = getPackageManager();
                    List activities = packageManager.queryIntentActivities(trailerIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    boolean isIntentSafe = activities.size() > 0;
                    if (isIntentSafe) {
                        startActivity((trailerIntent));
                    }
                }
            });

            mReviewList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

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
                Log.i(LOG_TAG, "trailer id: " + mTrailerId);
                Log.i(LOG_TAG, "trailer key: " + mTrailerKey);
                mTrailer = new Trailer(mTrailerKey, mTrailerId);

                mTrailerImagePath = DEFAULT_TRAILER_IMAGE + mTrailerKey + DEFAULT_KEY;
                mYoutubeTrailerPath = YOUTUBE_PATH + mTrailerKey;

                mTrailerImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                mTrailerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(DetailActivity.this).load(mTrailerImagePath).into(mTrailerImage);
                Log.i(LOG_TAG, "Trailer Image path: " + mTrailerImagePath);
                Log.i(LOG_TAG, "Trailer Path: " + mYoutubeTrailerPath);

            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            mLoaderManager.restartLoader(TRAILER_LOADER, null, this);
        }
    }

    private class ReviewCallback implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewLoader(DetailActivity.this, mReviewUrl);
        }

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
