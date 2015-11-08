/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.moviestrends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
           /* Bundle arguments =new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_URI, getIntent().getData());

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);*/
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.move_detail_container, new DetailActivityFragment())
                            //       .add(R.id.move_detail_container, fragment)
                    .commit();
        }
    }

}

