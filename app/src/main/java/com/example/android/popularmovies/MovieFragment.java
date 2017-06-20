package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Fragment to add movie images to gridview
 */

public class MovieFragment extends Fragment {
    private View rootView;
    private MovieAdapter mMovieAdapter;
    private GridView gridView;
    private ArrayList<Movie> mMovieArrayList = new ArrayList<Movie>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.sort_order:
                Intent settingsIntent = new Intent(getActivity(), Settings.class);
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_movie, container, false);
        gridView = (GridView) rootView.findViewById(R.id.image_grid_view);
        return rootView;
    }

    public MovieFragment() {}
}
