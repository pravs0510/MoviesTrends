package com.example.android.moviestrends.data;

/**
 * Created by praveena on 9/11/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.moviestrends.utils.PollingCheck;

import java.util.Map;
import java.util.Set;


/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your movieTrendsContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    //static final String TEST_LOCATION = "99705";
    //static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default movie values for your database tests.
     */
    static ContentValues createMovieValues(long moviesRowID) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_ID, moviesRowID);
        movieValues.put(movieTrendsContract.MoviesEntry.MOVIE_NAME, "MINIONS");
        movieValues.put(movieTrendsContract.MoviesEntry.POSTER_PATH, "XYZ");
        movieValues.put(movieTrendsContract.MoviesEntry.OVERVIEW, "minions movies");
     //   movieValues.put(movieTrendsContract.MoviesEntry.RATING, "6.5/10");
        movieValues.put(movieTrendsContract.MoviesEntry.RELEASE_DATE, "2015-01-01");
    //    movieValues.put(movieTrendsContract.MoviesEntry.INS_TMS, "2015-09-10-00.00.00");


        return movieValues;
    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the movieTrendsContract.
     */
    static ContentValues createMovieMinnonValue() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(movieTrendsContract.MoviesEntry.MOVIE_ID, 1);
        testValues.put(movieTrendsContract.MoviesEntry.MOVIE_NAME, "MINIONS");
        testValues.put(movieTrendsContract.MoviesEntry.POSTER_PATH, "XYZ");
        testValues.put(movieTrendsContract.MoviesEntry.OVERVIEW, "minions movies");
    //    testValues.put(movieTrendsContract.MoviesEntry.RATING, "6.5/10");
        testValues.put(movieTrendsContract.MoviesEntry.RELEASE_DATE, "2015-01-01");
     //   testValues.put(movieTrendsContract.MoviesEntry.INS_TMS, "2015-09-10-00.00.00");

        return testValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the movieTrendsContract as well as the MovieDbHelper.
     */
    static long insertMinionMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieMinnonValue();

        long movieRowId;
        movieRowId = db.insert(movieTrendsContract.MoviesEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", movieRowId != -1);

        return movieRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}