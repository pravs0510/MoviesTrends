package com.example.android.moviestrends.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.moviestrends.data.movieTrendsContract.MoviesEntry;

/**
 * Created by praveena on 9/10/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
              //  MoviesEntry.MOVIE_ID + " INTEGER NOT NULL," +
                MoviesEntry.MOVIE_NAME + " STRING NOT NULL," +
                MoviesEntry.POSTER_PATH + " STRING NOT NULL," +
                MoviesEntry.OVERVIEW + " STRING NOT NULL," +
                MoviesEntry.RATING + " STRING NOT NULL," +
                MoviesEntry.RELEASE_DATE + " STRING NOT NULL," +
                MoviesEntry.INS_TMS + " TIMESTAMP NOT NULL," +
                " UNIQUE (" + MoviesEntry.MOVIE_NAME  + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
    }
}
