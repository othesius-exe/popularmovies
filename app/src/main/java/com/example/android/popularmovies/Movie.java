package com.example.android.popularmovies;

/**
 * Class to define Movie Objects
 */

public class Movie {

    // Member Variables for the Movie class
    // Title:
    private String mTitle;

    // Rating:
    private Double mRating;

    // Release Year:
    private String mReleaseInfo;

    // Poster Image:
    private String mImagePoster;

    // Movie Summary:
    private String mSynopsis;

    public Movie(String title, Double rating, String releaseInfo, String image, String synopsis) {
        mTitle = title;
        mRating = rating;
        mReleaseInfo = releaseInfo;
        mImagePoster = image;
        mSynopsis = synopsis;
    }

    // Getter methods for retrieving individual attributes
    public String getTitle() {
        return mTitle;
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

    // Override toString() to return a string of all included info
    @Override
    public String toString() {
        return "Title: " + mTitle
                + "Rating: " + mRating
                + "Release Year: " + mReleaseInfo
                + "Summary: " + mSynopsis;
    }

}
