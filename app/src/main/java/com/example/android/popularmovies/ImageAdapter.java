package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Adapter for Adding Images to A GridView
 */

public class ImageAdapter extends BaseAdapter {
    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Movie> mMovieList;
    private String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String IMAGE_WIDTH = "w185";

    public ImageAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovieList = movies;
        notifyDataSetChanged();
    }

    public void addAll(ArrayList movieList) {
        if(mMovieList == null) {
            mMovieList = new ArrayList();
        }
        mMovieList.clear();
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        String partialImagePath = mMovieList.get(position).getImagePoster();
        String fullImagePath = BASE_IMAGE_URL + IMAGE_WIDTH + partialImagePath;
        Picasso.with(mContext).load(fullImagePath).into(imageView);
        Log.v(LOG_TAG, "Inside getView");
        Log.v(LOG_TAG, "Poster path " + fullImagePath);
        return imageView;
    }
}
