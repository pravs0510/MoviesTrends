/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.moviestrends;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviestrends.data.movieTrendsContract;
import com.squareup.picasso.Picasso;

import static android.widget.ImageView.ScaleType.CENTER_CROP;


public class DetailActivity extends ActionBarActivity {

    private static DetailParcelable detailParcelable =null;
    private static String video = null;
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
            final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            if(savedInstanceState == null) {
                Intent intent = getActivity().getIntent();
                detailParcelable.originalTitle = intent.getStringExtra(intent.EXTRA_TEXT);
                detailParcelable.posterPath = intent.getStringExtra(getString(R.string.poster_path));
                detailParcelable.overview = intent.getStringExtra(getString(R.string.overview));
                detailParcelable.userRatings = intent.getStringExtra(getString(R.string.Ratings));
                detailParcelable.releaseDate = intent.getStringExtra(getString(R.string.Release));
            }


            final String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size) + detailParcelable.posterPath;
         //   RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relayout);
            // relativeLayout.setBackgroundResource(R.drawable.relayout);
            //final BlurTransformation blurTransformation = new BlurTransformation(getActivity(), 20);
  /*          Picasso.with(getActivity()).load(url)
                    // .transform(blurTransformation)
                    .into(new Target() {


                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            rootView.setBackground(new BitmapDrawable(getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(final Drawable errorDrawable) {
                            Log.d("TAG", "FAILED");
                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {
                            Log.d("TAG", "Prepare Load");
                        }


                    });*/

            ((TextView) rootView.findViewById(R.id.detail_text)).setText(detailParcelable.originalTitle);
            ImageView image = ((ImageView) rootView.findViewById(R.id.poster_path));
            image.setScaleType(CENTER_CROP);
            //String url = this.getString(R.string.image_base_url) + this.getString(R.string.tmdb_image_size) + detailParcelable.posterPath;
            Picasso.with(getActivity())
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.error)
                    .error(R.drawable.placeholder)
                    .into(image);

            ((TextView) rootView.findViewById(R.id.Overview)).setText(detailParcelable.overview);
            //  ((TextView) rootView.findViewById(R.id.Rating_text)).setText(getString(R.string.User_Ratings));
            //((TextView) rootView.findViewById(R.id.release_date)).setText(getString(R.string.Release_Date));
            ((TextView) rootView.findViewById(R.id.Overview_text)).setText(getString(R.string.Overview));
            ((TextView) rootView.findViewById(R.id.Rating)).setText(detailParcelable.userRatings);
            ((TextView) rootView.findViewById(R.id.release)).setText(detailParcelable.releaseDate);

           /* Uri builtUri = Uri.parse("https://www.googleapis.com/youtube/v3/activities").buildUpon()
                    .appendQueryParameter("q", "kung+fu+panda-trailer")
                    .appendQueryParameter("orderby", "published")
                    .appendQueryParameter("orderby", "published")
                            .build();
            URL url1 = new URL(builtUri.toString());*/
            ImageView trailer = (ImageView) rootView.findViewById(R.id.trailer);
            trailer.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "This button will launch movie trailer", Toast.LENGTH_LONG).show();
                    // need to add the youtube link
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=pWdKf3MneyI")));
                    Log.i("Video", "Video Playing....");
                }

            });

            CheckBox fav = (CheckBox) rootView.findViewById(R.id.favorite);
            fav.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view1){

                   long favMovieId =  insertFavoriteMovie(detailParcelable.originalTitle,detailParcelable.posterPath,detailParcelable.overview,
                           detailParcelable.userRatings,detailParcelable.releaseDate);
                    Log.e("Insert QUERY ", "favMovieId :"+ favMovieId);
                    Toast.makeText(getActivity(), "This movie is marked your favorite", Toast.LENGTH_LONG).show();
                }
            });



            return rootView;
        }

        long insertFavoriteMovie(String MOVIE_NAME ,String POSTER_PATH, String OVERVIEW, String USER_RATINGS,String DATE){
            long movieid ;
            //Time daytime = new Time();

            Cursor movieCursor = getActivity().getContentResolver().query(
                    movieTrendsContract.MoviesEntry.CONTENT_URI,
                    new String[]{movieTrendsContract.MoviesEntry._ID},
                    movieTrendsContract.MoviesEntry.MOVIE_NAME + "= ?",
                    new String[]{MOVIE_NAME},
                    null);
            if(movieCursor.moveToFirst()){
                int moviesIdx = movieCursor.getColumnIndex(movieTrendsContract.MoviesEntry._ID);
                movieid = movieCursor.getLong(moviesIdx);
            }
            else{
                ContentValues movieValues = new ContentValues();
                movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_NAME,MOVIE_NAME);
                movieValues.put(movieTrendsContract.MoviesEntry.POSTER_PATH,POSTER_PATH);
                movieValues.put(movieTrendsContract.MoviesEntry.OVERVIEW,OVERVIEW);
                movieValues.put(movieTrendsContract.MoviesEntry.RATING,USER_RATINGS);
                movieValues.put(movieTrendsContract.MoviesEntry.RELEASE_DATE,DATE);
                movieValues.put(movieTrendsContract.MoviesEntry.INS_TMS, "2015-01-01-00.00.00");

               /* movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_ID, 100);
                movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_NAME, "TELUGU");
                movieValues.put(movieTrendsContract.MoviesEntry.POSTER_PATH, "XYZ");
                movieValues.put(movieTrendsContract.MoviesEntry.OVERVIEW, "TELUGU movies");
                movieValues.put(movieTrendsContract.MoviesEntry.RATING, "6.5/10");
                movieValues.put(movieTrendsContract.MoviesEntry.RELEASE_DATE, "2015-01-01");
                movieValues.put(movieTrendsContract.MoviesEntry.INS_TMS, "2015-09-10-00.00.00");*/

                Uri insertedUri = getActivity().getContentResolver().insert(
                        movieTrendsContract.MoviesEntry.CONTENT_URI,movieValues);

                movieid = ContentUris.parseId(insertedUri);
            }
            movieCursor.close();
            return  movieid;
        }
/*        protected class youtubeDataTask extends AsyncTask<String, Integer, String> {
            private final String LOG_TAG = youtubeDataTask.class.getSimpleName();


            private List<ArrayObj> moviesDataParse(String movieDataJ) throws JSONException {
                //   Log.e(LOG_TAG, "movieDataParse class is called");
                //video.clear();
                video = null;
                final String OWM_LIST = "items";

                final String OWM_ADULT = "id";
                final String OWM_BACKDROP_PATH = "videoId";


                JSONObject youTubeJson = new JSONObject(movieDataJ);
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
            protected String doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String youTubeDataJson = null;
                //String apikey = getString(R.string.apiKey);

                try {
                    final String MOVIES_BASE_URL = getString(R.string.);
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
        }*/

    }

}

