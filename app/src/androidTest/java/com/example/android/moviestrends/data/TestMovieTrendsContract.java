package com.example.android.moviestrends.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by praveena on 9/12/2015.
 */
public class TestMovieTrendsContract extends AndroidTestCase {
    private static final  String TEST_MOVIES = "MINIONS";

    public void testBuildFavoriteMovies() {
        Uri FavMovieUri = movieTrendsContract.MoviesEntry.buildFavoriteMovies(TEST_MOVIES);
        assertNotNull("ERROR: NULL URI RETURNED : Fill in the buildFavoriteMovies in " + "movieTrendsContract" + FavMovieUri);
        Log.e("TESTMovieTrendsContract","favaMovieURI" + FavMovieUri);
        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_MOVIES, FavMovieUri.getLastPathSegment());
        //assertEquals("Error: Weather location Uri doesn't match our expected result",
        //   FavMovieUri.toString(),"content://com.example.android.movieTrends.app/favorite_movies/"
    }
}
