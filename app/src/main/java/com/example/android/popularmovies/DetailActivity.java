package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 *
 */

public class DetailActivity extends AppCompatActivity {

    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String IMAGE_WIDTH = "w185";

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mRatingView;
    private TextView mDateView;
    private TextView mSynopsisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        String title = movie.getTitle();
        Double rating = movie.getRating();
        String date = movie.getReleaseInfo();
        String synopsis = movie.getSynopsis();
        String image = movie.getImagePoster();

        String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + image;

        mImageView = (ImageView) findViewById(R.id.detail_image_view);
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(this).load(fullImagePath).into(mImageView);

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
