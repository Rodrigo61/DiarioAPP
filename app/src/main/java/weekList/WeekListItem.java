package weekList;

import java.util.Calendar;

/**
 * Created by rodrigo on 10/02/16.
 */
public class WeekListItem {

    public String name;
    public long weekBeginMillis;
    public long weekEndMillis;

    public WeekListItem(long weekBeginMillis, long weekEndMillis) {
        this.weekBeginMillis = weekBeginMillis;
        this.weekEndMillis = weekEndMillis;

        name = toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWeekBeginMillis() {
        return weekBeginMillis;
    }

    public void setWeekBeginMillis(long weekBeginMillis) {
        this.weekBeginMillis = weekBeginMillis;
    }

    public long getWeekEndMillis() {
        return weekEndMillis;
    }

    public void setWeekEndMillis(long weekEndMillis) {
        this.weekEndMillis = weekEndMillis;
    }

    public String toString(){
        String returnString = null;
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(weekBeginMillis);
        returnString = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH);

        calendar.setTimeInMillis(weekEndMillis);
        returnString += " ~~~ " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH);

        return returnString;
    }
}
