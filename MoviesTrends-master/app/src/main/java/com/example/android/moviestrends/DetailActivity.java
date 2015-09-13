/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.moviestrends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.widget.ImageView.ScaleType.CENTER_CROP;


public class DetailActivity extends ActionBarActivity {

    private static DetailParcelable detailParcelable =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new PlaceholderFragment())
                    .commit();
            detailParcelable=new DetailParcelable(getString(R.string.test),getString(R.string.test),getString(R.string.test),getString(R.string.test),getString(R.string.test));
        }
        else {
            detailParcelable =savedInstanceState.getParcelable(getString(R.string.Details_key));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.Details_key), detailParcelable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view. this class receives the intent from the mainActivityFragment class which is de bundled and used to populate the detail layout
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            if(savedInstanceState == null) {
                Intent intent = getActivity().getIntent();
                detailParcelable.originalTitle = intent.getStringExtra(intent.EXTRA_TEXT);
                detailParcelable.posterPath = intent.getStringExtra(getString(R.string.poster_path));
                detailParcelable.overview = intent.getStringExtra(getString(R.string.overview));
                detailParcelable.userRatings = intent.getStringExtra(getString(R.string.Ratings));
                detailParcelable.releaseDate = intent.getStringExtra(getString(R.string.Release));
            }

                ((TextView) rootView.findViewById(R.id.detail_text)).setText(detailParcelable.originalTitle);
                ImageView image = ((ImageView) rootView.findViewById(R.id.poster_path));
                image.setScaleType(CENTER_CROP);
                String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size) + detailParcelable.posterPath;
                        Picasso.with(getActivity())
                                .load(url)
                                .fit()
                                .placeholder(R.drawable.error)
                                .error(R.drawable.placeholder)
                                .into(image);

                ((TextView) rootView.findViewById(R.id.Overview)).setText(detailParcelable.overview);
                ((TextView) rootView.findViewById(R.id.Rating_text)).setText(getString(R.string.User_Ratings));
                ((TextView) rootView.findViewById(R.id.release_date)).setText(getString(R.string.Release_Date));
                ((TextView) rootView.findViewById(R.id.Overview_text)).setText(getString(R.string.Overview));
                ((TextView) rootView.findViewById(R.id.Rating)).setText(detailParcelable.userRatings);
                ((TextView) rootView.findViewById(R.id.release)).setText(detailParcelable.releaseDate);

            return rootView;
        }
    }
}

