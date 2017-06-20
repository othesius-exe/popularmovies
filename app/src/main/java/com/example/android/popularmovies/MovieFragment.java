package com.example.android.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Fragment to add movie images to gridview
 */

public class MovieFragment extends Fragment {

    private View rootView;
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
