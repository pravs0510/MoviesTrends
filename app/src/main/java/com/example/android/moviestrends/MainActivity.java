/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.moviestrends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.moviestrends.AdapterPackage.ArrayObj;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    private boolean mTwoPane;
   // private String mLocation;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.move_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.move_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;

        }
    }

    @Override
    public void onItemSelected(ArrayObj movieItem,boolean favMovies) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, movieItem);
            args.putBoolean(DetailActivityFragment.FAV,favMovies);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.move_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieItem.original_title)
                    .putExtra(getString(R.string.poster_path), movieItem.poster_path)
                    .putExtra(getString(R.string.id), movieItem.id)
                    .putExtra(getString(R.string.favMovie), favMovies);
                startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        MainActivityFragment mf = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mf.updateMovie();
    }
}
