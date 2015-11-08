package com.example.android.moviestrends.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.moviestrends.data.movieTrendsContract.MoviesEntry;

/**
 * Created by praveena on 9/10/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
// Create reviews table to store the reviews of a particular movie based on Movie id
        final String SQL_CREATE_REVIWES_TABLE = "CREATE TABLE " + movieTrendsContract.ReviewEntry.TABLE_NAME + " (" +
                movieTrendsContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                movieTrendsContract.ReviewEntry.MOVIE_ID + " STRING NOT NULL," +
                movieTrendsContract.ReviewEntry.REVIEW_AUTHOR + " STRING NOT NULL," +
                movieTrendsContract.ReviewEntry.REVIEW_CONTENT + " STRING NOT NULL," +
                " FOREIGN KEY (" + movieTrendsContract.ReviewEntry.MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.MOVIE_ID + ") " +
                ");";
//Table to store the favorite movie
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesEntry.MOVIE_ID + " STRING NOT NULL," +
                MoviesEntry.ADULT + " STRING NOT NULL," +
                MoviesEntry.BACKDROP_PATH + " STRING NOT NULL," +
                MoviesEntry.OVERVIEW + " STRING NOT NULL," +
                MoviesEntry.MOVIE_LANG + " STRING NOT NULL," +
                MoviesEntry.MOVIE_NAME + " STRING NOT NULL," +
                MoviesEntry.RELEASE_DATE + " STRING NOT NULL," +
                MoviesEntry.POSTER_PATH + " STRING NOT NULL," +
                MoviesEntry.MOVIE_STATUS + " STRING NOT NULL," +
                MoviesEntry.MOVIE_RUNTIME + " STRING NOT NULL," +
                MoviesEntry.VOTEAVERAGE + " STRING NOT NULL," +
                MoviesEntry.VOTECOUNT + " STRING NOT NULL," +
                MoviesEntry.TRAILER_ID + " STRING NOT NULL," +
                MoviesEntry.GENRE + " STRING NOT NULL," +
                " UNIQUE (" + MoviesEntry.MOVIE_NAME + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIWES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + movieTrendsContract.ReviewEntry.TABLE_NAME);
    }
}
