package com.example.android.moviestrends.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import static com.example.android.moviestrends.data.movieTrendsContract.*;

/**
 * Created by praveena on 9/12/2015.
 */
public class MovieProvider extends ContentProvider {
    //URI matcher used by the content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int FAVORITEMOVIES = 100;
    public static final int MOVIES = 101;
    private MovieDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sFavoriteMovieSettingQueryBuilder;
    static{
       sFavoriteMovieSettingQueryBuilder = new SQLiteQueryBuilder();
        sFavoriteMovieSettingQueryBuilder.setTables(
                movieTrendsContract.MoviesEntry.TABLE_NAME);
    }

    private static String sMovieNameSelection =
            MoviesEntry.TABLE_NAME +
                    "." + MoviesEntry.MOVIE_NAME + " = ?";

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher =new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        // matcher.addURI(autority,movieTrendsContract.PATH_FAVORITE_MOVIES,MOVIES);
        matcher.addURI(authority, PATH_FAVORITE_MOVIES,MOVIES);
        matcher.addURI(authority, PATH_FAVORITE_MOVIES+"/*",FAVORITEMOVIES);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return  true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES :
                return MoviesEntry.CONTENT_TYPE;
            case FAVORITEMOVIES :
                return MoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //return 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case MOVIES:
                rowsUpdated =db.update(MoviesEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case FAVORITEMOVIES:
                rowsUpdated =db.update(MoviesEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: + " + uri);
        }



        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:{
                return mOpenHelper.getReadableDatabase().query(
                        MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

            }
            case FAVORITEMOVIES :{
                retCursor = getFavoriteMovies(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        final  int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection  = "1";
        switch(match) {
            case MOVIES : {
                rowsDeleted = db.delete(MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FAVORITEMOVIES: {
                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesEntry.buildFavoriteMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIES:{

                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesEntry.buildFavoriteMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }


    private  Cursor getFavoriteMovies (Uri uri , String[] projection, String sortorder){
        String FavoriteMovies = MoviesEntry.getFavoriteMoviesFromUri(uri);
        return sFavoriteMovieSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieNameSelection,
                new String [] {FavoriteMovies},
                null,
                null,
                sortorder);
    }
//    private  Cursor getAllFavMovies ( String[] projection, String sortorder){
//       // String FavoriteMovies = movieTrendsContract.MoviesEntry.getFavoriteMoviesFromUri(uri);
//        return mOpenHelper.getReadableDatabase().query(
//                MoviesEntry.TABLE_NAME,
//
//                projection,
//                selection
//                null,
//                null,
//                null,
//                sortorder);
//    }
}
