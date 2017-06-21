package com.example.android.popularmovies;

import android.content.Context;
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
            imageView.setLayoutParams(new GridView.LayoutParams(R.dimen.image_list_item_height, R.dimen.image_list_item_width));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mMovieList.get(position).getImagePoster()).into(imageView);
        //Log.v(LOG_TAG, "Inside getView");
        return convertView;
    }
}
