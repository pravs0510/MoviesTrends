/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.moviestrends.AdapterPackage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.moviestrends.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by praveena on 8/9/2015.
 */
/*
    ImageAdapter class is a custom adaptor which accepts listArrays with ArrayObj type and populates the grid.
    This class utilizes Picasso to retrieve the images from the url and load them to the image view
 */
public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private List<ArrayObj> mGridData;

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private int mResource = 0;
    private String baseUrl = null;

   /*
   Constructor for the ImageAdapter class .
   @Param context, resource and movies (type List<ArrayObj>)
   The method also initialises baseUrl variable.
    */
   public ImageAdapter(Context context, int resource, List<ArrayObj> movies) {
       super(context,resource,movies);
       mContext = context;
       mGridData = movies;
       mResource = resource;
       baseUrl = context.getString(R.string.image_base_url) + context.getString(R.string.tmdb_image_size);

   }
    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView grid = (SquaredImageView) convertView;

        if (grid == null) {
            grid = new SquaredImageView(mContext);
            grid.setScaleType(CENTER_CROP);
        }
        ArrayObj movie = null;
        movie = (ArrayObj) getItem(position);
        String url = baseUrl + movie.poster_path;
        Picasso.with(mContext)
                .load(url)
                .fit()
                .placeholder(R.drawable.error)
                .error(R.drawable.placeholder)
                .into(grid);
        return grid;
    }

    /*
    This method traveses the arraylist to get the data as of the position
    @Param position which is Integer, this position parameter is updated in the main ArrayAdaptor
    @Return ArrayObj
     */
   public ArrayObj getItem(int position) {
       return mGridData.get(position);
   }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
       // Log.e(LOG_TAG,"size : " + mGridData.size());
         return mGridData.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
    SetGridData is used to pass the data to the ImageAdaptor class/
    @Param gridData of type List<ArrayObj>
     */

    public void setGridData(List<ArrayObj> gridData) {
//        mGridData.clear();
//        mGridData.addAll(gridData);
        mGridData = gridData;
        notifyDataSetChanged();
    }
}




