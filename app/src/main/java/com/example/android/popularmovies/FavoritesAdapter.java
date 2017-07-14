package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmovies.data.UserFavoritesContract.FavoritesEntry;
import com.squareup.picasso.Picasso;

/**
 *
 */

public class FavoritesAdapter extends CursorAdapter {

    private String LOG_TAG = FavoritesAdapter.class.getSimpleName();
    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String IMAGE_WIDTH = "w185";

    // CursorAdapter Constructor
    public FavoritesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find the views to inflate
        ImageView imageView;

        // Get the attributes that we want to display; Only Images in this case
        int imagePathColumn = cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH);
        String imagePath = cursor.getString(imagePathColumn);
        String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + imagePath;

        imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(context).load(fullImagePath).into(imageView);
        Log.v(LOG_TAG, "Inside getView");
        Log.v(LOG_TAG, "Poster path " + fullImagePath);
    }
}
