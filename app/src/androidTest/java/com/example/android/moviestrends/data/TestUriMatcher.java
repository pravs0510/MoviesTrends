package com.example.android.moviestrends.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by praveena on 9/12/2015.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String MOVIE_QUERY ="CAPTAIN AMERICA";

    private static final Uri TEST_MOVIE_DIR = movieTrendsContract.MoviesEntry.CONTENT_URI;
    private static final Uri TEST_FAVORITE_MOVIE = movieTrendsContract.MoviesEntry.buildFavoriteMovies(MOVIE_QUERY);

    public void testUriMatcher(){
        UriMatcher matcher = MovieProvider.buildUriMatcher();
        assertEquals("Error: The Movie URI  for TEST_MOVIE DIR was matched incorrectly.", matcher.match(TEST_MOVIE_DIR),MovieProvider.MOVIES);
        assertEquals("Error: The Movie URI was matched incorrectly.+" + MovieProvider.FAVORITEMOVIES, matcher.match(TEST_FAVORITE_MOVIE), MovieProvider.FAVORITEMOVIES);
        //System.out.print(TEST_FAVORITE_MOVIE);
    }

}
