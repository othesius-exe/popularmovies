package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */

public class Review implements Parcelable {

    // Author of the review
    private String mAuthor;

    // Content of the review
    private String mContent;

    // Constructor for a review
    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String toString() {
        return "Review by: " + mAuthor
                + "\n"
                + mContent;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}