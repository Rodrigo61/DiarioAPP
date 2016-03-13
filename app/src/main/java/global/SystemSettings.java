package global;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.rodrigo.myapplication.R;

import java.util.Stack;

import database.DatabaseContract;
import database.SleepHourDBAccessor;

/**
 * Created by rodrigo on 10/02/16.
 */
public class SystemSettings {

    private static Stack<Long> taskBeginStack = new Stack();


    public static long getNextTaskBegin(){
        if(taskBeginStack.empty()) {
           return 0;
        }

        return taskBeginStack.pop();
    }

    public static void pushTaskBegin(long taskBegin){
        taskBeginStack.push(taskBegin);
    }

    public static long getAlarmDelay(Context context){
        return 10000;

        /*SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.settings_file), Context.MODE_PRIVATE);
        return sharedPref.getLong(context.getString(R.string.settings_repeat_delay), 0);*/
    }


    public static int getReviewDay(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.settings_file), Context.MODE_PRIVATE);
        return sharedPref.getInt(context.getString(R.string.settings_review_day), 0);
    }


    public static boolean isSleepHour(ContentResolver resolver){

        String selection = DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_BEGIN + " <= ? AND " +
                           DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_END + " >= ? AND " +
                           DatabaseContract.SleepHour.COLUMN_NAME_DAYS + " LIKE ?";


        String selectionArgs[] = {Utils.getCurrentDayTime()+"",
                                  Utils.getCurrentDayTime()+"",
                                  "%"+Utils.getCurrentDayOfWeek()+"%"};


        Cursor cursor = SleepHourDBAccessor.select(resolver, selection, selectionArgs, null, null);

        cursor.moveToFirst();

        return !cursor.isAfterLast();
    }



}
