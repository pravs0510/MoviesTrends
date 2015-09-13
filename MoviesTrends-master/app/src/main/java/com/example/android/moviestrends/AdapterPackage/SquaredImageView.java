/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.moviestrends.AdapterPackage;

/**
 * Created by praveena on 8/13/2015.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/** This class is used to create a custom image View , this overrides onMeasure method of image view to give a custom measurement */

final class SquaredImageView extends ImageView {
    public final Double MEASURED_WIDTH = 1.43;
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth()* MEASURED_WIDTH));
    }
}
