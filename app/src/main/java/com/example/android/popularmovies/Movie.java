package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to define Movie Objects
 */

public class Movie implements Parcelable {

    // Member Variables for the Movie class
    // Title:
    private String mTitle;

    // Movie ID
    private int mMovieId;

    // Rating:
    private Double mRating;

    // Release Year:
    private String mReleaseInfo;

    // Poster Image:
    private String mImagePoster;

    // Movie Summary:
    private String mSynopsis;

    private boolean isFavorite;

    public Movie(String title, Double rating, String releaseInfo, String image, String synopsis, int id) {
        mTitle = title;
        mRating = rating;
        mReleaseInfo = releaseInfo;
        mImagePoster = image;
        mSynopsis = synopsis;
        mMovieId = id;
    }

    // Getter methods for retrieving individual attributes
    public String getTitle() {
        return mTitle;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public Double getRating() {
        return mRating;
    }

    public String getReleaseInfo() {
        return mReleaseInfo;
    }

    public String getImagePoster() {
        return mImagePoster;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public boolean setAsFavorite() {
        if (!isFavorite) {
            isFavorite = true;
        }
        return isFavorite;
    }

    // Override toString() to return a string of all included info
    @Override
    public String toString() {
        return "Title: " + mTitle
                + "Rating: " + mRating
                + "Release Year: " + mReleaseInfo
                + "Summary: " + mSynopsis;
    }


    protected Movie(Parcel in) {
        mTitle = in.readString();
        mMovieId = in.readInt();
        mRating = in.readByte() == 0x00 ? null : in.readDouble();
        mReleaseInfo = in.readString();
        mImagePoster = in.readString();
        mSynopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeInt(mMovieId);
        if (mRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mRating);
        }
        dest.writeString(mReleaseInfo);
        dest.writeString(mImagePoster);
        dest.writeString(mSynopsis);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}