package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    static class ViewHolder{
        @Bind(R.id.image_view)ImageView imageView;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
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
        return mMovieList.get(position).getImagePoster();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = ((MainActivity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_image, parent, false);
            holder = new ViewHolder(convertView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Picasso.with(mContext).load(mMovieList.get(position).getImagePoster()).into(holder.imageView);
        //Log.v(LOG_TAG, "Inside getView");
        return convertView;
    }
}
