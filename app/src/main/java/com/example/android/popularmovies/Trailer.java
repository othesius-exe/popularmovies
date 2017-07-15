package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */

public class Trailer implements Parcelable {

    // Video Key
    private String mTrailerKey;

    // Video ID
    private String mTrailerId;

    // Title
    private String mTitle;

    public Trailer(String key, String id, String title) {
        mTrailerKey = key;
        mTrailerId = id;
        mTitle = title;
    }

    public String getTrailerKey(){
        return mTrailerKey;
    }

    public String getTrailerId() {
        return mTrailerId;
    }

    public String getTrailerTitle() {
        return mTitle;
    }

    public String toString() {
        return "Trailer key: " + mTrailerKey
                + " "
                + "Trailer ID: " + mTrailerId
                + "Trailer title: " + mTitle;
    }

    protected Trailer(Parcel in) {
        mTrailerKey = in.readString();
        mTrailerId = in.readString();
        mTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerKey);
        dest.writeString(mTrailerId);
        dest.writeString(mTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
