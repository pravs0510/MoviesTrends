package com.example.android.moviestrends;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.moviestrends.AdapterPackage.DetailActivityAdaptor;
import com.example.android.moviestrends.AdapterPackage.ReviewsObj;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {

    private static ArrayList<ReviewsObj> movieReviews = new ArrayList<>();
    ListView listView;
    private DetailActivityAdaptor adaptor;
    private String poster_path ="";

    public ReviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            movieReviews= savedInstanceState.getParcelableArrayList(getString(R.string.Details_Reviews_key));
            poster_path = savedInstanceState.getString(getString(R.string.poster_path));
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.poster_path), poster_path);
        outState.putParcelableArrayList(getString(R.string.Details_Reviews_key), movieReviews);
        //outState.putBoolean(getString(R.string.favMovie),mFavorite);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  return inflater.inflate(R.layout.fragment_review, container, false);

        final View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        if (savedInstanceState == null) {
            //   getVideoId();
            Intent intent = getActivity().getIntent();
            movieReviews = intent.getParcelableArrayListExtra(intent.EXTRA_TEXT);
            poster_path = intent.getStringExtra(getString(R.string.poster_path));
        }
        final String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size)+ poster_path;
        //final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        final BlurTransformation blurTransformation;
      //  blurTransformation = new BlurTransformation((getActivity(),20);)
        Picasso.with(getActivity()).load(url)
          //      .transform(new BlurTransformation(20))
                .into(new Target() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        rootView.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d("TAG", "PrepareLoad");
                    }
        });



        listView = (ListView) rootView.findViewById(R.id.list);
        adaptor = new DetailActivityAdaptor(getActivity(), R.layout.review_grid, movieReviews);
        listView.setAdapter(adaptor);
        adaptor.setmMovieData(movieReviews);

        return rootView;
    }
}
