package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rodrigo on 23/02/16.
 */
public class SleepHourDB extends SQLiteOpenHelper {


    public SleepHourDB(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);

    }


    public void onCreate(SQLiteDatabase db) {
        Log.i("DatabaseContract", "sleepHour database created");
        db.execSQL(DatabaseContract.SleepHour.SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SleepHour.SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
