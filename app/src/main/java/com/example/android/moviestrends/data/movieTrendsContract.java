package com.example.android.moviestrends.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by praveena on 9/10/2015.
 */
public class movieTrendsContract {
    // Defining the content authority
    public static final String CONTENT_AUTHORITY = "com.example.android.movietrends.app";
    // Defining the base URI to access this content authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //  Defining the base path to the tables of the content provider
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final String PATH_MOVIES_REVIEWS = "movies_review";


    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES_REVIEWS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES_REVIEWS;

        public static final String TABLE_NAME = "movies_reviews";

        // Column with the foreign key into the favorite_movies table.
        public static final String MOVIE_ID = "movie_id";

        // Column with author of the review for the movie
        public static final String REVIEW_AUTHOR = "author";

        // Column with content of the review for the movies
        public static final String REVIEW_CONTENT = "content";


        public static Uri buildMovieReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieReviewsbyID(String MOVIE_ID) {
            return CONTENT_URI.buildUpon().appendPath(MOVIE_ID).build();
        }

        public static String getMovieReviewsFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        public static final String TABLE_NAME = "favorite_movies";

        // Column with the foreign key into the favorite_movies table.
        public static final String MOVIE_ID = "movie_id";

        // Column with adult  of the movie
        public static final String ADULT = "adult";

        // Column with poster path
        public static final String BACKDROP_PATH = "backdrop_path";

        //Column with overview of the movie
        public static final String OVERVIEW = "overview";

        //Column with the movie title
        public static final String MOVIE_LANG = "original_LANG";

        //Column with the movie title
        public static final String MOVIE_NAME = "original_title";

        //Column with Release date
        public static final String RELEASE_DATE = "release_date";

        //Column with status of the movie
        public static final String MOVIE_STATUS = "status";

        //Column with runtime of the movie
        public static final String MOVIE_RUNTIME = "runtime";

        // Column with poster path
        public static final String POSTER_PATH = "poster_path";

        // Column with Vote average  of the movie
        public static final String VOTEAVERAGE = "vote_average";

        // Column with Vote count  of the movie
        public static final String VOTECOUNT = "vote_count";

        //Column with Insert timestamp ;
        public static final String TRAILER_ID = "trailer_id";

        //Column with genre of the movie
        public static final String GENRE = "genre_id";


        public static Uri buildFavoriteMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoriteMovies(String MovieName) {
            return CONTENT_URI.buildUpon().appendPath(MovieName).build();
        }

        public static Uri buildFavoriteMoviesbyID(String MOVIE_ID) {
            return CONTENT_URI.buildUpon().appendPath(MOVIE_ID).build();
        }

        public static String getFavoriteMoviesFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}
