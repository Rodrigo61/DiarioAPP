package database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import global.Category;
import global.Task;

/**
 * Created by rodrigo on 01/02/16.
 */
public class  TaskDBAccessor {

    public static void insert(ContentResolver contentResolver, Task newTask){

        if(newTask.getID() != -1){
            throw new IllegalArgumentException("Can't modify _ID column from Task");
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Task.COLUMN_NAME_TITLE, newTask.getTitle());
        values.put(DatabaseContract.Task.COLUMN_NAME_CATEGORY, newTask.getCategory().toString());
        values.put(DatabaseContract.Task.COLUMN_NAME_DESCRIPTION, newTask.getDescription());
        values.put(DatabaseContract.Task.COLUMN_NAME_DATE, newTask.getDate());

        contentResolver.insert(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, values);

    }




    @NonNull
    private static Task createTaskFromCursor(Cursor cursor) {

        cursor.moveToFirst();

        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseContract.Task.COLUMN_NAME_TITLE));

        String category = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseContract.Task.COLUMN_NAME_CATEGORY));

        String description = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseContract.Task.COLUMN_NAME_DESCRIPTION));

        long date = cursor.getLong(
                cursor.getColumnIndexOrThrow(DatabaseContract.Task.COLUMN_NAME_DATE));

        int ID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Task._ID));


        return new Task(title, new Category(category), description, date, ID);
    }


    public static int update(ContentResolver contentResolver, ContentValues values, String selection, String[] selectionArgs){
        return contentResolver.update(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, values, selection, selectionArgs);
    }


    public static Task selectByID(ContentResolver contentResolver, int id) {

        String selection = DatabaseContract.Task._ID + " = ?";

        Cursor cursor = select(contentResolver, selection, new String[]{id + ""}, null, null);

        Task task = createTaskFromCursor(cursor);

        return task;
    }


    public static int delete(ContentResolver contentResolver, String selection, String[] selectionArgs) {

        return contentResolver.delete(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, selection, selectionArgs);
    }


    public static Task getNewestTask(ContentResolver contentResolver) {

        String orderBy = DatabaseContract.Task.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = select(contentResolver, null, null, null, orderBy);

        Task task = createTaskFromCursor(cursor);


        return task;
    }


    public static Cursor select(ContentResolver contentResolver, String selection,
                                String[] selectionArgs, String[] customProjection, String orderBy) {

        customProjection = customProjection != null ? customProjection:DatabaseContract.Task.DEFAULT_PROJECTION;

        return contentResolver.query(DatabaseContract.Task.CONTENT_URI_FOR_ROWS,
                customProjection, selection, selectionArgs, orderBy);
    }


    public static Task getOlderTask(ContentResolver contentResolver) {

        String orderBy = DatabaseContract.Task.COLUMN_NAME_DATE + " ASC";

        Cursor cursor = select(contentResolver, null, null, null, orderBy);

        Log.i("DEBUG: ", "cursor.getCount() = " + cursor.getCount() );

        Task task = createTaskFromCursor(cursor);

        return task;
    }
}