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
    public static final String CONTENT_AUTHORITY ="com.example.android.movietrends.app";
    // Defining the base URI to access this content authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    //  Defining the base path to the tables of the content provider
    public static final String PATH_FAVORITE_MOVIES ="favorite_movies";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        public static final String TABLE_NAME = "favorite_movies";

        // Column with the foreign key into the favorite_movies table.
        public static final String MOVIE_ID = "movie_id";

        //Column with the movie title
        public static final String MOVIE_NAME = "original_title";

        // Column with poster path
        public static final String POSTER_PATH ="poster_path";

        //Column with overview of the movie
        public static final String OVERVIEW ="overview";

        // Column with Rating of the movie
        public static final String RATING = "rating";

        //Column with Release date
        public static final String RELEASE_DATE ="release_date";

        //Column with Insert timestamp ;
        public  static final String INS_TMS ="ins_tms";

        public static Uri buildFavoriteMoviesUri(long id){ return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static Uri buildFavoriteMovies(String MovieName){return  CONTENT_URI.buildUpon().appendPath(MovieName).build();
        }
        public static String getFavoriteMoviesFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}
