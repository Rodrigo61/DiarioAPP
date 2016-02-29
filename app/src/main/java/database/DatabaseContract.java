package database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rodrigo on 01/02/16.
 */
public final class DatabaseContract {
    public static final int DATABASE_VERSION = 2;
    /*public static final String DATABASE_NAME = "database";*/
    public static final String DATABASE_NAME = "/mnt/sdcard/database.db";
    public static final String AUTHORITY = "com.example.rodrigo.myapplication.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public DatabaseContract() {
    }


    private static String foreignKey(String localColumnName, String referencedTableName, String referencedColumnName) {
        return " FOREIGN KEY (" + localColumnName + ") REFERENCES " + referencedTableName + "(" + referencedColumnName + ")";
    }


    public static abstract class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";

        public static final String[] DEFAULT_PROJECTION = {_ID, COLUMN_NAME_TITLE,
                COLUMN_NAME_DESCRIPTION, COLUMN_NAME_DATE, COLUMN_NAME_CATEGORY};

        public static final Uri CONTENT_URI_FOR_ROWS =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);


        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Task.TABLE_NAME + " (" +
                        Task._ID + " INTEGER PRIMARY KEY," +
                        Task.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        Task.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        Task.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        Task.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                        foreignKey(Task.COLUMN_NAME_CATEGORY, Category.TABLE_NAME, Category.COLUMN_NAME_CATEGORY) +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Task.TABLE_NAME;
    }


    public static abstract class Category implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Category.TABLE_NAME + " (" +
                        Category._ID + " INTEGER PRIMARY KEY," +
                        Category.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Category.TABLE_NAME;
    }


    public static abstract class SleepHour implements BaseColumns {
        public static final String TABLE_NAME = "sleepHour";
        public static final String COLUMN_NAME_TIME_RANGE_BEGIN = "timeRangeBegin";
        public static final String COLUMN_NAME_TIME_RANGE_END = "timeRangeEnd";
        public static final String COLUMN_NAME_DAYS = "days";

        public static final String[] DEFAULT_PROJECTION = {_ID, COLUMN_NAME_TIME_RANGE_BEGIN,
                COLUMN_NAME_TIME_RANGE_END, COLUMN_NAME_DAYS};

        public static final Uri CONTENT_URI_FOR_ROWS =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SleepHour.TABLE_NAME + " (" +
                        SleepHour._ID + " INTEGER PRIMARY KEY," +
                        SleepHour.COLUMN_NAME_TIME_RANGE_BEGIN + INTEGER_TYPE + COMMA_SEP +
                        SleepHour.COLUMN_NAME_TIME_RANGE_END + INTEGER_TYPE + COMMA_SEP +
                        SleepHour.COLUMN_NAME_DAYS + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SleepHour.TABLE_NAME;
    }
}