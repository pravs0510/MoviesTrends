/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.moviestrends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.android.moviestrends.AdapterPackage.ArrayObj;
import com.example.android.moviestrends.AdapterPackage.ImageAdapter;

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
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    List<ArrayObj> movies = new ArrayList<>();
    private ArrayList<ArrayObj> mMoviesAdaptor = new ArrayList<>();
    String sortTypePrev = null;
      //  private ArrayList<ArrayObj> list_of_movies = new ArrayList<>();
    ProgressBar progressBar ;
    private ImageAdapter mImageAdapter;
    private ArrayObj listOfMovies;
    private boolean firstTime = true;

    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.Movies_key))) {
          //  updateMovie();
            mMoviesAdaptor = new ArrayList();
        } else {
            mMoviesAdaptor = savedInstanceState.getParcelableArrayList(getString(R.string.Movies_key));// using the key retrieve the data
            sortTypePrev = savedInstanceState.getString(getString(R.string.SortPrev));
        }
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(getString(R.string.Movies_key), mMoviesAdaptor); // need to pass the bundle to save the current state
        outState.putString(getString(R.string.SortPrev),sortTypePrev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:

                //Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(new Intent(MainActivityFragment.this.getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                mMoviesAdaptor = new ArrayList();
                updateMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
       Private class which is used to get the sort preferences from the settings and pass it to the Async task. This method does not require any input parameters
     */
    private void updateMovie() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sortKey), getString(R.string.sortDefault));

        if ((sortType != sortTypePrev) || mMoviesAdaptor.isEmpty()) {
            sortTypePrev = sortType;
            moviesDataTask moviesTask = new moviesDataTask();
            moviesTask.execute(sortType);
        }
    }

     /*
       onStart activty is overridden to call the updateMovie method with in turn calls the Async task
     */
    @Override
    public void onStart() {
        super.onStart();
         updateMovie();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragment,
                             Bundle savedInstanceState) {

       // updateMovie();
        mImageAdapter = new ImageAdapter(getActivity()
                , R.layout.image_item
                , mMoviesAdaptor);
        View rootView = inflater.inflate(R.layout.fragment_main, fragment, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.INVISIBLE);

         gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayObj movieItem = mImageAdapter.getItem(i);
                // Toast.makeText(getActivity(),"You have clicked on " + movieItem.original_title,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                //bundle.putStringArray(key,movieItem);
                if (movieItem.backdrop_path.equals("null")) {
                    movieItem.backdrop_path = movieItem.poster_path;
                }
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieItem.original_title)
                        .putExtra(getString(R.string.poster_path), movieItem.backdrop_path)
                        .putExtra(getString(R.string.overview), movieItem.overview)
                        .putExtra(getString(R.string.Ratings), (movieItem.vote_average + getString(R.string.byTen)))
                        .putExtra(getString(R.string.Release), movieItem.release_date);

                startActivity(intent);

            }
        });
        return rootView;
    }

    /*
    moviesDataTask is used to retrive the information from the API . This invokes doInBackground method which retreives the data from API on the background thread.
    The json String is parsed using moviesDataParse method
     @Param Sort type which is user input in the settings based on which the URI is built and the data is retrived.
     @Return List<ArrayObj> mMovieAdapter which is the list of movies arranged in Arrayobj type
     */

    protected class moviesDataTask extends AsyncTask<String, Integer, List<ArrayObj>> {
        private final String LOG_TAG = moviesDataTask.class.getSimpleName();


        private List<ArrayObj> moviesDataParse(String movieDataJ) throws JSONException {
         //   Log.e(LOG_TAG, "movieDataParse class is called");
            movies.clear();
            final String OWM_LIST = getString(R.string.results);

            final String OWM_ADULT = getString(R.string.adult);
            final String OWM_BACKDROP_PATH = getString(R.string.backdrop_path);
            final String OWM_OVERVIEW = getString(R.string.overview);
            final String OWN_ORIGINAL_LANG = getString(R.string.original_language);
            final String OWM_ORIGINAL_TITLE = getString(R.string.original_title);
            final String OWN_REL_DTE = getString(R.string.Release);
            final String OWM_POSTER = getString(R.string.poster_path);
            final String OWM_POPULARITY = getString(R.string.popularity);
            final String OWN_VIDEO = getString(R.string.video);
            final String OWN_VOTE_AVG = getString(R.string.vote_average);
            final String OWN_VOTE_CNT = getString(R.string.vote_count);

            JSONObject movieJson = new JSONObject(movieDataJ);
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);
            for (int i = 0; i < movieArray.length(); i++) {
                ArrayObj movie = new ArrayObj();
                JSONObject movieDetails = movieArray.getJSONObject(i);

                movie.backdrop_path = movieDetails.getString(OWM_BACKDROP_PATH);
                movie.overview = movieDetails.getString(OWM_OVERVIEW);
                movie.original_language = movieDetails.getString(OWN_ORIGINAL_LANG);
                movie.original_title = movieDetails.getString(OWM_ORIGINAL_TITLE);
                movie.release_date = movieDetails.getString(OWN_REL_DTE);
                movie.poster_path = movieDetails.getString(OWM_POSTER);
                movie.popularity = movieDetails.getDouble(OWM_POPULARITY);
                movie.video = movieDetails.getString(OWN_VIDEO);
                movie.vote_average = movieDetails.getDouble(OWN_VOTE_AVG);
                movie.vote_count = movieDetails.getDouble(OWN_VOTE_CNT);
                movie.adult = movieDetails.getString(OWM_ADULT);
                movies.add(movie);
            }
            return movies;
        }

        @Override
        protected List<ArrayObj> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesDataJson = null;
            String apikey = getString(R.string.apiKey);

            try {
                final String MOVIES_BASE_URL = getString(R.string.movie_base_url);
                final String SORT_BY = getString(R.string.sort_by);
                final String api_key = getString(R.string.apikey);
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, params[0])
                        .appendQueryParameter(api_key, apikey)
                        .build();
                URL url = new URL(builtUri.toString());
                //Log.v(LOG_TAG, "BUILT URI " + builtUri.toString());

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
                return moviesDataParse(moviesDataJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ArrayObj> arrayObjs) {
            super.onPostExecute(arrayObjs);
            if (!arrayObjs.isEmpty()) {
                mMoviesAdaptor.clear();
                for (ArrayObj movieObj : arrayObjs)
                    mMoviesAdaptor.add(movieObj);
                mImageAdapter.setGridData(mMoviesAdaptor);
                progressBar.setVisibility(View.INVISIBLE);
               //Log.e(LOG_TAG, "mMoviesAdaptor " + mMoviesAdaptor);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress[0]);
            progressBar.setProgress(progress[0]);
        }
    }
}

