package database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import global.Category;
import global.Task;


/**
 * Created by rodrigo on 01/02/16.
 */
public class SleepHourDBAccessor {

    public static void insert(ContentResolver contentResolver, long timeRangeBegin, long timeRangeEnd,
                              String days) {

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_BEGIN, timeRangeBegin);
        values.put(DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_END, timeRangeEnd);
        values.put(DatabaseContract.SleepHour.COLUMN_NAME_DAYS, days);

        contentResolver.insert(DatabaseContract.SleepHour.CONTENT_URI_FOR_ROWS, values);
    }


    public static int delete(ContentResolver contentResolver, String selection, String[] selectionArgs) {

        return contentResolver.delete(DatabaseContract.SleepHour.CONTENT_URI_FOR_ROWS, selection, selectionArgs);
    }


    public static Cursor select(ContentResolver contentResolver, String selection,
                                String[] selectionArgs, String[] customProjection, String orderBy) {

        customProjection = customProjection != null ? customProjection : DatabaseContract.SleepHour.DEFAULT_PROJECTION;

        return contentResolver.query(DatabaseContract.SleepHour.CONTENT_URI_FOR_ROWS,
                customProjection, selection, selectionArgs, orderBy);
    }


}