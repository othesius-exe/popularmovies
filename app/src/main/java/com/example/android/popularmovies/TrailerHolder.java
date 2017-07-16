package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 */

public class TrailerHolder extends RecyclerView.ViewHolder {

    private ImageView mTrailerImage;
    private TextView mTitleText;


    public TrailerHolder(View v) {
        super(v);
        mTrailerImage = (ImageView) v.findViewById(R.id.trailer_image_view);
        mTitleText = (TextView) v.findViewById(R.id.trailer_title_view);
        mTrailerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

    }

    public ImageView getImageView() {
        return mTrailerImage;
    }

    public TextView getTextView() {
        return mTitleText;
    }

    public void setTextView(TextView titleText) {
        mTitleText = titleText;
    }

    public void setImageView(ImageView imageView) {
        mTrailerImage = imageView;
    }
}
