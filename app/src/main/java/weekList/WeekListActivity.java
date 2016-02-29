package weekList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.rodrigo.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

import database.TaskDBAccessor;
import global.SystemSettings;
import global.Task;
import taskList.TaskListActivity;
import updateTask.UpdateTaskActivity;

public class WeekListActivity extends Activity {


    private static final int DAYS_OF_WEEK = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_list);

        populateListView();
    }


    private void populateListView() {

        ArrayList<WeekListItem> weekArray = generateWeekItemArrayFromDB();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, weekArray);

        setListViewAdpter(adapter);

    }


    private long convertDayToMillis(int daysCount){
        return daysCount * 24 * 60 * 60 * 1000;
    }


    private ArrayList generateWeekItemArrayFromDB() {

        ArrayList<WeekListItem> weekItemArray = new ArrayList<>();
        long lastDay = setDayHourToMidnight(TaskDBAccessor.getNewestTask(getContentResolver()).getDate());
        long currentBeginWeek = setDayHourToMidnight(TaskDBAccessor.getOlderTask(getContentResolver()).getDate());
        long currentEndWeek = 0;
        WeekListItem currentListItem = null;


        while(hasMoreWeek(lastDay, currentEndWeek)){
            currentEndWeek = calculateEndDayWeek(currentBeginWeek, lastDay);
            currentListItem = new WeekListItem(currentBeginWeek, currentEndWeek);
            weekItemArray.add(currentListItem);
            currentBeginWeek = currentEndWeek + 1; // 0h of next day
        }

        return weekItemArray;

    }


    private long setDayHourToMidnight(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();
    }


    private long calculateEndDayWeek(long currentBeginWeek, long lastDay) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(currentBeginWeek);

        int daysCountWeek = DAYS_OF_WEEK - Math.abs(calendar.get(Calendar.DAY_OF_WEEK) - SystemSettings.getReviewDay(this));
        long currentEndWeek = currentBeginWeek + convertDayToMillis(daysCountWeek);

        currentEndWeek = currentEndWeek < (lastDay - 1) ? currentEndWeek:lastDay;

        return setDayHourToMidnight(currentEndWeek);
    }


    private boolean hasMoreWeek(long lastDay, long currentEndWeek) {
        return currentEndWeek != lastDay;
    }


    private void setListViewAdpter(ArrayAdapter adapter) {
        ListView listView = (ListView) findViewById(R.id.week_list_activity_listView);

        listView.setAdapter(adapter);

        setOnChildClickListener(listView, adapter);
    }


    private void setOnChildClickListener(ListView listView, final Adapter adapter) {

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentTaskListActivity = new Intent(getBaseContext(), TaskListActivity.class);
                WeekListItem selectedItem = (WeekListItem) adapter.getItem(position);

                intentTaskListActivity.putExtra("beginWeek", selectedItem.getWeekBeginMillis());
                intentTaskListActivity.putExtra("endWeek", selectedItem.getWeekEndMillis());

                startActivity(intentTaskListActivity);
            }
        });
    }


}
