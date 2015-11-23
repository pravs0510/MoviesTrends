package com.example.android.moviestrends.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;

import static com.example.android.moviestrends.data.movieTrendsContract.*;

/**
 * Created by praveena on 9/12/2015.
 */
public class MovieProvider extends ContentProvider {
    public static final int FAVORITEMOVIES = 100;
    public static final int MOVIES = 101;
    public static final int MOVIEID = 102;
    public static final int REVIEWS = 200;
    public static final int REVIEWSID = 201;
    //URI matcher used by the content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sFavoriteMovieSettingQueryBuilder;
    private static final SQLiteQueryBuilder sFavoriteReviewSettingQueryBuilder;
    private static String sMovieNameSelection =
            MoviesEntry.TABLE_NAME +
                    "." + MoviesEntry.MOVIE_NAME + " = ?";
    private static String sMovieIDSelection =
            MoviesEntry.TABLE_NAME +
                    "." + MoviesEntry.MOVIE_ID + " = ?";
    private static String sMovieReviewsSelection =
            ReviewEntry.TABLE_NAME +
                    "." + ReviewEntry.MOVIE_ID + " = ?";

    static {
        sFavoriteMovieSettingQueryBuilder = new SQLiteQueryBuilder();
        sFavoriteMovieSettingQueryBuilder.setTables(
                movieTrendsContract.MoviesEntry.TABLE_NAME);
    }

    static {
        sFavoriteReviewSettingQueryBuilder = new SQLiteQueryBuilder();
        sFavoriteReviewSettingQueryBuilder.setTables(
                movieTrendsContract.ReviewEntry.TABLE_NAME);
    }

    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        // matcher.addURI(autority,movieTrendsContract.PATH_FAVORITE_MOVIES,MOVIES);
        matcher.addURI(authority, PATH_FAVORITE_MOVIES, MOVIES);
        matcher.addURI(authority, PATH_FAVORITE_MOVIES + "/*", FAVORITEMOVIES);
        matcher.addURI(authority, PATH_FAVORITE_MOVIES + "/*", MOVIEID);
        matcher.addURI(authority, PATH_MOVIES_REVIEWS, REVIEWS);
        matcher.addURI(authority, PATH_MOVIES_REVIEWS + "/*", REVIEWSID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesEntry.CONTENT_TYPE;
            case FAVORITEMOVIES:
                return MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIEID:
                return MoviesEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return ReviewEntry.CONTENT_TYPE;
            case REVIEWSID:
                return ReviewEntry.CONTENT_ITEM_TYPE;

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

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITEMOVIES:
                rowsUpdated = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsUpdated = db.update(ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
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
        new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public void setExtras(Bundle extras) {

            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
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
            case FAVORITEMOVIES: {
                retCursor = getFavoriteMovies(uri, projection, sortOrder);
                break;
            }
            case MOVIEID: {
                retCursor = getFavoriteMoviesById(uri, projection, sortOrder);
                break;
            }
            case REVIEWS: {
                return mOpenHelper.getReadableDatabase().query(
                        ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

            }
            case REVIEWSID: {
                retCursor = getMovieReviewsById(uri, projection, sortOrder);
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

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES: {
                rowsDeleted = db.delete(MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS: {
                // rowsDeleted =db.delete(ReviewEntry.TABLE_NAME, whereClause, whereArgs);
                rowsDeleted = db.delete(ReviewEntry.TABLE_NAME, selection, selectionArgs);
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
                long _id = db.insertOrThrow(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesEntry.buildFavoriteMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIES: {

                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesEntry.buildFavoriteMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {

                long _id = db.insert(ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ReviewEntry.buildMovieReviewsUri(_id);
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


    private Cursor getFavoriteMovies(Uri uri, String[] projection, String sortorder) {
        String FavoriteMovies = MoviesEntry.getFavoriteMoviesFromUri(uri);
        return sFavoriteMovieSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieNameSelection,
                new String[]{FavoriteMovies},
                null,
                null,
                sortorder);
    }

    private Cursor getFavoriteMoviesById(Uri uri, String[] projection, String sortorder) {
        String FavoriteMovies = MoviesEntry.getFavoriteMoviesFromUri(uri);
        return sFavoriteMovieSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieIDSelection,
                new String[]{FavoriteMovies},
                null,
                null,
                sortorder);
    }

    private Cursor getMovieReviewsById(Uri uri, String[] projection, String sortorder) {
        String FavoriteMovies = ReviewEntry.getMovieReviewsFromUri(uri);
        return sFavoriteReviewSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieReviewsSelection,
                new String[]{FavoriteMovies},
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
