package global;

import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by rodrigo on 23/02/16.
 */
public class Utils {

    public static long convertMinutesToMillis(int minutes){
        return minutes * 60000;
    }


    public static long convertMillisToSeconds(long millis) {
        return millis / 1000;
    }


    public static long convertHourToMillis(int hour){
        return hour * 3600000;
    }


    public static long getCurrentDayTime() {
        Calendar calendar = Calendar.getInstance();

        long time = convertHourToMillis(calendar.get(Calendar.HOUR_OF_DAY));
        time += convertMinutesToMillis(calendar.get(Calendar.MINUTE));

        return time;
    }


    public static long getCurrentDayOfWeek(){
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public static int convertMillisToMinutes(long millis){
        return (int)(millis / 60000);
    }


    public static long convertDayToMillis(int daysCount){
        return daysCount * 24 * 60 * 60 * 1000;
    }


    public static long convertTimeTextToMillis(String timeText) {
        StringTokenizer stringTokenizer = new StringTokenizer(timeText, ":");

        long time = Utils.convertHourToMillis(Integer.parseInt(stringTokenizer.nextToken()));
        time += Utils.convertMinutesToMillis(Integer.parseInt(stringTokenizer.nextToken()));

        return time;
    }


    public static String convertMillisToTimeText(long millis){
        return new SimpleDateFormat("HH:mm").format(new Date(Utils.convertHourToMillis(3) + millis));
    }
}
