package database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import database.DatabaseContract;
import database.TaskDBAccessor;

/**
 * Created by rodrigo on 05/02/16.
 */
public class AppContentProvider extends ContentProvider{

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TASK_ALL_ROWS_ID = 1;
    private static final int SLEEP_HOUR_ALL_ROWS_ID = 2;

    static {
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.Task.TABLE_NAME, TASK_ALL_ROWS_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.SleepHour.TABLE_NAME, SLEEP_HOUR_ALL_ROWS_ID);
    }


    @Override
    public boolean onCreate() {
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)){
            case TASK_ALL_ROWS_ID: {

                SQLiteDatabase db = new TaskDB(getContext()).getReadableDatabase();
                return db.query(DatabaseContract.Task.TABLE_NAME, projection, selection,
                                selectionArgs, null, null, sortOrder);

            }
            case SLEEP_HOUR_ALL_ROWS_ID:{

                SQLiteDatabase db = new SleepHourDB(getContext()).getReadableDatabase();
                return db.query(DatabaseContract.SleepHour.TABLE_NAME, projection, selection,
                                selectionArgs, null, null, sortOrder);

            }
            default:
                throw new IllegalArgumentException("Incorrect URI for query");
        }
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)){
            case TASK_ALL_ROWS_ID: {
                Log.i("Content provider", "insert task uri correct");

                SQLiteDatabase db = new TaskDB(getContext()).getWritableDatabase();

                long _id = db.insert(DatabaseContract.Task.TABLE_NAME, null, values);

                return ContentUris.withAppendedId(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, _id);

            }
            case SLEEP_HOUR_ALL_ROWS_ID:{
                SQLiteDatabase db = new SleepHourDB(getContext()).getWritableDatabase();

                long _id = db.insert(DatabaseContract.SleepHour.TABLE_NAME, null, values);

                return ContentUris.withAppendedId(DatabaseContract.SleepHour.CONTENT_URI_FOR_ROWS, _id);
            }
            default:
                throw new IllegalArgumentException("Incorrect URI for query");
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case TASK_ALL_ROWS_ID: {

                SQLiteDatabase db = new TaskDB(getContext()).getWritableDatabase();
                return db.delete(DatabaseContract.Task.TABLE_NAME, selection, selectionArgs);

            }
            case SLEEP_HOUR_ALL_ROWS_ID: {

                SQLiteDatabase db = new SleepHourDB(getContext()).getWritableDatabase();
                return db.delete(DatabaseContract.SleepHour.TABLE_NAME, selection, selectionArgs);

            }
            default:
                throw new IllegalArgumentException("Incorrect URI for query");
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)){
            case TASK_ALL_ROWS_ID: {
                SQLiteDatabase db = new TaskDB(getContext()).getReadableDatabase();
                return db.update(DatabaseContract.Task.TABLE_NAME, values, selection, selectionArgs);

            }
            default:
                throw new IllegalArgumentException("Incorrect URI for query");
        }
    }
}
