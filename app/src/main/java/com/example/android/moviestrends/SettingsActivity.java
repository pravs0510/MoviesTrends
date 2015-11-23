/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by praveena on 8/14/2015.
 */
package com.example.android.moviestrends;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}