/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.moviestrends;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviestrends.AdapterPackage.ArrayObj;
import com.example.android.moviestrends.AdapterPackage.DetailActivityAdaptor;
import com.example.android.moviestrends.AdapterPackage.ReviewsObj;
import com.example.android.moviestrends.data.movieTrendsContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

//import android.support.v7.widget.RecyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static ArrayObj movieDetails = new ArrayObj();
    private static ArrayObj detailParcelable = new ArrayObj();
    static boolean mFavorite = false;
    private static boolean rowsFetched = false;
    private static boolean mFavoriteExits = false;
    private static ArrayList<ReviewsObj> movieReviews = new ArrayList<>();
    private DetailActivityAdaptor adaptor;
   // private TextView title;

    @Bind(R.id.detail_text) TextView title;
    @Bind(R.id.poster_path) ImageView poster;
    @Bind(R.id.runtime) TextView runtime;
    @Bind(R.id.genre) TextView genre;
    @Bind(R.id.overview) TextView overview;
    @Bind(R.id.rating) TextView rating;
    @Bind(R.id.trailer) Button trailer;
    @Bind(R.id.favorite) Button fav;
    @Bind(R.id.backdrop_path) ImageView backdrop;
    @Bind(R.id.status) TextView status;
    @Bind(R.id.Reviews_cnt) TextView Reviews_cnt;
    @Bind(R.id.Reviews_txt) TextView Reviews;
    int reviewSize;
    private String mVideo = null;
    static final String DETAIL_URI = "URI";
    static final String FAV = "fav";
    Bundle arguments;
    ShareActionProvider mShareActionProvider;
    public static String SHAREDACTIONUPDATE = "SHAREDACTIONUPDATE";
    /*private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;*/
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            movieDetails = savedInstanceState.getParcelable(getString(R.string.Details_key));
            movieReviews = savedInstanceState.getParcelableArrayList(getString(R.string.Details_Reviews_key));
            rowsFetched = true;
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            //mShareActionProvider.setShareIntent(createShareVideoIntent());

            mShareActionProvider.setShareIntent(createShareVideoIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null");
        }

    }

    private Intent createShareVideoIntent() {


      /*  if(mVideo.equals(null)){

                Toast.makeText(getActivity(), "No trailer to share", Toast.LENGTH_LONG).show();
                return null;
            }*/
       /* BroadcastReceiver onNotice = null;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onNotice,
                new IntentFilter(SHAREDACTIONUPDATE));

        onNotice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey! Check this new trailer " + mVideo);
            }

        };
        return null;*/

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey! Check this new trailer " + mVideo);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                //Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(new Intent(DetailActivityFragment.this.getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                movieDetails = new ArrayObj();
                movieReviews.clear();
                getVideoId();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.Details_key), movieDetails);
        outState.putParcelableArrayList(getString(R.string.Details_Reviews_key), movieReviews);
        //outState.putBoolean(getString(R.string.favMovie),mFavorite);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rowsFetched = false;
        movieDetails = new ArrayObj();
        //Arguments passed from a two frame mode
        arguments = getArguments();
        if (arguments != null) {
            rowsFetched = false;
            movieDetails = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
            mFavorite = arguments.getBoolean(DetailActivityFragment.FAV);
        } else {
            Intent intent = getActivity().getIntent();
            movieDetails.original_title = intent.getStringExtra(intent.EXTRA_TEXT);
            if (intent == null || movieDetails.original_title == null) {

                final View rootView = inflater.inflate(R.layout.dummy_detail, container, false);
                //ButterKnife.bind(this, rootView);
                TextView dummyTitle =  (TextView) rootView.findViewById(R.id.Dummy_text);
                        dummyTitle.setText("Select the movie");
                return rootView;
            }
            movieDetails.id = intent.getStringExtra(getString(R.string.id));
            movieDetails.poster_path = intent.getStringExtra(getString(R.string.poster_path));
            mFavorite = intent.getBooleanExtra(getString(R.string.favMovie), false);
        }
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,rootView);
        if (savedInstanceState == null) {
            if (mFavorite == true) {
                getFavoriteMovieDetails(movieDetails.id);
                getReviewDetails(movieDetails.id);
            }
        }
        if (rowsFetched == false) {
            title.setText(movieDetails.original_title);
        } else {
            title.setText(movieDetails.original_title + " (" + movieDetails.release_date.substring(0, 4) + ")");
        }
        final String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size);
        poster.setScaleType(CENTER_CROP);
        //String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size) + detailParcelable.posterPath;
        Picasso.with(getActivity())
                .load(url + movieDetails.poster_path)
                .fit()
                .placeholder(R.drawable.error)
                .error(R.drawable.placeholder)
                .into(poster);
        runtime.setText(movieDetails.runtime);
        genre.setText(movieDetails.genre_id);
        overview.setText(movieDetails.overview);
        rating.setText(movieDetails.vote_average + getString(R.string.byTen) + movieDetails.vote_count + getString(R.string.users));
        status.setText(movieDetails.status);
        backdrop.setScaleType(CENTER_CROP);
        Picasso.with(getActivity())
                .load(url + movieDetails.backdrop_path)
                .fit()
                .placeholder(R.drawable.error)
                .error(R.drawable.placeholder)
                .into(backdrop);
        // Compute review size and popluate the reviewcnt, set onclick listener for either clicking review cnt or Reviews
        reviewSize = movieReviews.size();
        Reviews_cnt.setText("(" + reviewSize + ")");
        // if it is a 2 pane mode, then reviews will be populated within the listview
        if (arguments != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.list1);
            adaptor = new DetailActivityAdaptor(getActivity(), R.layout.review_grid, movieReviews);
            listView.setAdapter(adaptor);
            adaptor.setmMovieData(movieReviews);
        }
        return rootView;
    }

    @OnClick(R.id.trailer)
    public void playTrailer(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVideo)));
        Log.i("Video", "Video Playing....");
    }

    @OnClick(R.id.favorite)
    public void clickFavorite(){
        // if yes --> open dialog box to ask users if the want to remove it or keep it
        //          if they want to remove it  call deleteFavoriteMovie
        // if no --> then insert the movie into favorite table by calling insertFavoriteMovie
        mFavoriteExits = false;

        long favMovieId = insertFavoriteMovie(movieDetails);
        Log.e("Insert QUERY ", "favMovieId, :" + favMovieId);
        if (!mFavoriteExits) {
            for (ReviewsObj reviewsObj : movieReviews) {
                if (reviewsObj.author.equals(getString(R.string.NoReviews))) {
                    Log.e("Insert QUERY ", "NO reviews to add ");
                } else {
                    long rev = insertMovieReview(movieDetails.id, reviewsObj);
                }
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            // set title
            alertDialogBuilder.setTitle(getString(R.string.FavoriteMovie));
            // set dialog message
            alertDialogBuilder.setMessage("This Movie is already your favorite. Do you want to remove from the list");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, close
                    // current activity
                    deleteFavoriteMovie(movieDetails.id);
                }
            }).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel();
                }
            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    }
    @OnClick({R.id.Reviews_txt,R.id.Reviews_cnt} )
    public void clickReviews(){
        viewReviews();
    }

    public long insertMovieReview(String MOVIE_ID, ReviewsObj reviewInsertDetails) {
        long reviewId;

        // insert the movie reviews in Reviews table
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(movieTrendsContract.ReviewEntry.MOVIE_ID, MOVIE_ID);
        reviewValues.put(movieTrendsContract.ReviewEntry.REVIEW_AUTHOR, reviewInsertDetails.author);
        reviewValues.put(movieTrendsContract.ReviewEntry.REVIEW_CONTENT, reviewInsertDetails.content);

        Uri insertedUri = getActivity().getContentResolver().insert(
                movieTrendsContract.ReviewEntry.CONTENT_URI, reviewValues);


        reviewId = ContentUris.parseId(insertedUri);

        return reviewId;
    }

    public long insertFavoriteMovie(ArrayObj movieInsertDetails) {
        long movieid;
        // Verify if the movie exists in favorite table else insert it

        Cursor movieCursor = getActivity().getContentResolver().query(
                movieTrendsContract.MoviesEntry.CONTENT_URI,
                new String[]{movieTrendsContract.MoviesEntry._ID},
                movieTrendsContract.MoviesEntry.MOVIE_NAME + "= ?",
                new String[]{movieInsertDetails.original_title},
                null);
        if (movieCursor.moveToFirst()) {
            int moviesIdx = movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry._ID);
            movieid = movieCursor.getLong(moviesIdx);
            mFavoriteExits = true;

            Toast.makeText(getActivity(), "This movie already your favorite", Toast.LENGTH_LONG).show();
        } else {
            ContentValues movieValues = new ContentValues();
            movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_ID, movieInsertDetails.id);
            movieValues.put(movieTrendsContract.MoviesEntry.ADULT, movieInsertDetails.adult);
            movieValues.put(movieTrendsContract.MoviesEntry.BACKDROP_PATH, movieInsertDetails.backdrop_path);
            movieValues.put(movieTrendsContract.MoviesEntry.OVERVIEW, movieInsertDetails.overview);
            movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_LANG, movieInsertDetails.original_language);
            movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_NAME, movieInsertDetails.original_title);
            movieValues.put(movieTrendsContract.MoviesEntry.RELEASE_DATE, movieInsertDetails.release_date);
            movieValues.put(movieTrendsContract.MoviesEntry.POSTER_PATH, movieInsertDetails.poster_path);
            movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_STATUS, movieInsertDetails.status);
            movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_RUNTIME, movieInsertDetails.runtime);
            movieValues.put(movieTrendsContract.MoviesEntry.VOTEAVERAGE, movieInsertDetails.vote_average);
            movieValues.put(movieTrendsContract.MoviesEntry.VOTECOUNT, movieInsertDetails.vote_count);
            movieValues.put(movieTrendsContract.MoviesEntry.TRAILER_ID, movieInsertDetails.trailer_id);
            movieValues.put(movieTrendsContract.MoviesEntry.GENRE, movieInsertDetails.genre_id);

            Uri insertedUri = getActivity().getContentResolver().insert(
                    movieTrendsContract.MoviesEntry.CONTENT_URI, movieValues);

            movieid = ContentUris.parseId(insertedUri);
            Toast.makeText(getActivity(), "This movie is marked your favorite", Toast.LENGTH_LONG).show();
        }
        movieCursor.close();

        return movieid;
    }

    public void deleteFavoriteMovie(String Movie_id) {
        // uri for deleting the movies
        int deleteRewiews;
        int deleteFavMovie;
        deleteRewiews = getActivity().getContentResolver().delete(movieTrendsContract.ReviewEntry.CONTENT_URI, movieTrendsContract.MoviesEntry.MOVIE_ID + "= ?", new String[]{Movie_id});
        deleteFavMovie = getActivity().getContentResolver().delete(movieTrendsContract.MoviesEntry.CONTENT_URI, movieTrendsContract.MoviesEntry.MOVIE_ID + "= ?", new String[]{Movie_id});

        Log.e("deleteFavoriteMovie", "No of rows deleted " + deleteRewiews + " " + deleteFavMovie);
        Toast.makeText(getActivity(), "This movie is removed from your favorites", Toast.LENGTH_LONG).show();
    }

    public void viewReviews(){
        if (arguments == null) {
            if (reviewSize > 0) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class).putExtra(Intent.EXTRA_TEXT, movieReviews)
                        .putExtra(getString(R.string.poster_path), movieDetails.poster_path);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "This movie has no reviews", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getFavoriteMovieDetails(String MOVIE_ID) {
        // ArrayObj favoriteMovie= new ArrayObj();;
        //  Boolean rowFound = false;
        Uri uri = movieTrendsContract.MoviesEntry.buildFavoriteMoviesbyID(MOVIE_ID);
        Cursor movieCursor = getActivity().getContentResolver().query(
                uri,
                null,
                movieTrendsContract.MoviesEntry.MOVIE_ID + "= ?",
                new String[]{MOVIE_ID},
                null);
        while (movieCursor.moveToNext()) {

            //    movieDetails.id = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.MOVIE_ID));
            int moviesIdx = movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry._ID);
            movieDetails.adult = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.ADULT));
            movieDetails.backdrop_path = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.BACKDROP_PATH));
            movieDetails.overview = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.OVERVIEW));
            movieDetails.original_language = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.MOVIE_LANG));
            movieDetails.original_title = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.MOVIE_NAME));
            movieDetails.release_date = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.RELEASE_DATE));
            movieDetails.status = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.MOVIE_STATUS));
            movieDetails.runtime = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.MOVIE_RUNTIME));
            //      movieDetails.poster_path = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.POSTER_PATH));
            movieDetails.vote_average = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.VOTEAVERAGE));
            movieDetails.vote_count = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.VOTECOUNT));
            movieDetails.trailer_id = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.TRAILER_ID));
            movieDetails.genre_id = movieCursor.getString(movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry.GENRE));

            rowsFetched = true;
        }
        movieCursor.close();


    }

    public void getReviewDetails(String MOVIE_ID) {
        ReviewsObj movieReview = new ReviewsObj();

        Boolean rowFound = false;
        movieReviews.clear();
        Uri uri = movieTrendsContract.ReviewEntry.buildMovieReviewsbyID(MOVIE_ID);
        Cursor reviewCursor = getActivity().getContentResolver().query(
                uri,
                null,
                movieTrendsContract.ReviewEntry.MOVIE_ID + "= ?",
                new String[]{MOVIE_ID},
                null);
        while (reviewCursor.moveToNext()) {
            movieReview = new ReviewsObj();
            movieReview.author = reviewCursor.getString(reviewCursor.getColumnIndex(movieTrendsContract.ReviewEntry.REVIEW_AUTHOR));
            movieReview.content = reviewCursor.getString(reviewCursor.getColumnIndex(movieTrendsContract.ReviewEntry.REVIEW_CONTENT));
            movieReviews.add(movieReview);
        }
        reviewCursor.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (rowsFetched == false) {
            getVideoId();
        }
    }

    private void setTextForView() {
        final String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size);
        poster.setScaleType(CENTER_CROP);
        Picasso.with(getActivity())
                .load(url + movieDetails.poster_path)
                .fit()
                .placeholder(R.drawable.error)
                .error(R.drawable.placeholder)
                .into(poster);
        status.setText(movieDetails.status);
        backdrop.setScaleType(CENTER_CROP);
        if (movieDetails.backdrop_path.equals("null") || movieDetails.backdrop_path.equals(null)) {
            movieDetails.backdrop_path = (movieDetails.poster_path);
        }
        Picasso.with(getActivity())
                .load(url + movieDetails.backdrop_path)
                .fit()
                .placeholder(R.drawable.error)
                .error(R.drawable.placeholder)
                .into(backdrop);
        title.setText(movieDetails.original_title + " (" + movieDetails.release_date.substring(0, 4) + ")");
        overview.setText(movieDetails.overview);
        genre.setText(movieDetails.genre_id);
        rating.setText(movieDetails.vote_average + "/10 by " + movieDetails.vote_count + " users");
        runtime.setText(movieDetails.runtime + " min");
        final int reviewSize = movieReviews.size();
        Reviews_cnt.setText("(" + reviewSize + ")");
    }

    private void getVideoId() {

        dataMovieTask videoTask = new dataMovieTask();
        videoTask.execute(movieDetails.id);
    }

        protected class dataMovieTask extends AsyncTask<String, Integer, ArrayObj> {
            private final String LOG_TAG = dataMovieTask.class.getSimpleName();


            private ArrayObj movieDataParse(String movieDataJ) throws JSONException {
                final String OWM_GENRES = getString(R.string.genres);
                final String OWM_GENERES_NAME = getString(R.string.genre_name);
                final String OWM_OVERVIEW = getString(R.string.overview);
                final String OWM_RUNTIME = getString(R.string.runtime);
                final String OWM_BTC = getString(R.string.belongs_to_collection);
                final String OWM_POSTER = getString(R.string.poster_path);
                final String OWM_TITLE = getString(R.string.original_title);
                final String OWM_STATUS = getString(R.string.status);
                final String OWM_ORIGINAL_LANG = getString(R.string.original_language);
                final String OWM_REL_DTE = getString(R.string.Release);
                final String OWM_VOTE_AVG = getString(R.string.vote_average);
                final String OWM_VOTE_CNT = getString(R.string.vote_count);
                final String OWM_ADULT = getString(R.string.adult);
                final String OWM_ID = getString(R.string.id);
                final String OWM_TRAILER = getString(R.string.trailers);
                final String OWM_YOUTUBE = getString(R.string.youtube);
                final String OWM_SOURCE = getString(R.string.source);
                final String OWM_REVIEWS = getString(R.string.reviews);
                final String OWM_RESULTS = getString(R.string.results);
                final String OWM_AUTHOR = getString(R.string.reviews_author);
                final String OWM_CONTENT = getString(R.string.content);
                final String OWM_BACKDROP = getString(R.string.backdrop_path);
                final String OWM_TYPE = getString(R.string.type);
                final String OWM_NAME = getString(R.string.trailer_name);

                //    String trailerDescription;
                ArrayObj movie = new ArrayObj();
                ReviewsObj review = new ReviewsObj();
                movieReviews.clear();
                JSONObject movieJson = new JSONObject(movieDataJ);
                movie.adult = movieJson.getString(OWM_ADULT);

                movie.id = movieJson.getString(OWM_ID);

                try {
                    JSONArray genresArray = movieJson.getJSONArray(OWM_GENRES);
                    JSONObject genresDetails = genresArray.getJSONObject(0);
                    movie.genre_id = genresDetails.getString(OWM_GENERES_NAME);
                } catch (Exception j) {
                    movie.genre_id = "Not Specified ";
                }
                //   JSONArray titleArray = movieJson.getJSONArray(OWM_BTC);
                // JSONObject titleDetails =titleArray.getJSONObject(0);
                movie.original_title = movieJson.getString(OWM_TITLE);
                movie.poster_path = movieJson.getString(OWM_POSTER);
                movie.backdrop_path = movieJson.getString(OWM_BACKDROP);

                movie.original_language = movieJson.getString(OWM_ORIGINAL_LANG);
                movie.overview = movieJson.getString(OWM_OVERVIEW);
                movie.release_date = movieJson.getString(OWM_REL_DTE);
                movie.runtime = movieJson.getString(OWM_RUNTIME);
                movie.status = movieJson.getString(OWM_STATUS);
                movie.vote_average = movieJson.getString(OWM_VOTE_AVG);
                movie.vote_count = movieJson.getString(OWM_VOTE_CNT);
                JSONObject trailerArray = movieJson.getJSONObject(OWM_TRAILER);
                try {

                    for (int i = trailerArray.length() - 1; i >= 0; i--) {
                        JSONObject trailerDetails = trailerArray.getJSONArray(OWM_YOUTUBE).getJSONObject(i);
                        String type = trailerDetails.getString(OWM_TYPE);
                        String name = trailerDetails.getString(OWM_NAME);
                        if (type.equals(getString(R.string.Trailer)) && (name.contains("1") || name.contains(getString(R.string.Official)))) {
                            movie.trailer_id = trailerDetails.getString(OWM_SOURCE);
                            i = -1;
                        }
                    }

                } catch (Exception e) {
                    movie.trailer_id = " ";
                }

                JSONObject reviewsArray = movieJson.getJSONObject(OWM_REVIEWS);
                try {
                    for (int i = 0; i < reviewsArray.length() - 1; i++) {
                        review = new ReviewsObj();
                        JSONObject reviewDetails = reviewsArray.getJSONArray(OWM_RESULTS).getJSONObject(i);
                        review.author = reviewDetails.getString(OWM_AUTHOR);
                        review.content = reviewDetails.getString(OWM_CONTENT);
                        movieReviews.add(review);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "No Reviews for the movie");
                }
                return movie;
            }

            @Override
            protected ArrayObj doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String moviesDataJson = null;
                String apikey = getString(R.string.apiKey);

                try {
                    final String MOVIES_BASE_URL = getString(R.string.movie_base1_url);
                    final String SORT_BY = getString(R.string.sort_by);
                    final String api_key = getString(R.string.apikey);
                    Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                            .appendPath(params[0])
                            .appendQueryParameter(api_key, apikey)
                            .appendQueryParameter(getString(R.string.append_to_response), getString(R.string.trailers_reviews))
                            .build();
                    URL url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    //  Log.e(LOG_TAG, "Buffer reader started");
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    int j = 0;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                        j++;
                        publishProgress(j);

                    }
                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    moviesDataJson = buffer.toString();
                    // Log.v(LOG_TAG, "Movie JSON String: " + moviesDataJson);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error", e);
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing the stream", e);
                        }
                    }
                }
                try {
                    return movieDataParse(moviesDataJson);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayObj arrayObjs) {
                super.onPostExecute(arrayObjs);
                if (arrayObjs != null) {
                    movieDetails = null;
                    movieDetails = arrayObjs;
                    rowsFetched = true;
                    mVideo = getString(R.string.YOUTUBE_URL) + movieDetails.trailer_id;
                    if (arguments != null) {
                        adaptor.setmMovieData(movieReviews);
                    }
                    setTextForView();
                    //        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(SHAREDACTIONUPDATE));

                    // mImageAdapter.setGridData(mMoviesAdaptor);
                    //     progressBar.setVisibility(View.INVISIBLE);
                    //Log.e(LOG_TAG, "mMoviesAdaptor " + mMoviesAdaptor);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //    progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                super.onProgressUpdate(progress[0]);
                //      progressBar.setProgress(progress[0]);
            }

        }

}
