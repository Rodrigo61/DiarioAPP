package taskList;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.rodrigo.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import categoryPieChart.PieChartActivity;
import updateTask.UpdateTaskActivity;
import global.Category;
import global.Task;
import database.DatabaseContract;
import database.TaskDBAccessor;


public class TaskListActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int LOADER_ID = 0;
    final String SELECTION = DatabaseContract.Task.COLUMN_NAME_DATE + " >= ? AND " +
            DatabaseContract.Task.COLUMN_NAME_DATE + " <= ?";
    final String ORDER_BY =  DatabaseContract.Task.COLUMN_NAME_DATE + " DESC";

    HashMap<String, Integer> categorySumHashMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_task_list);

        getLoaderManager().initLoader(LOADER_ID, null, this);

    }


    private String[] getWeekRangeFromExtraIntent() {

        return new String[] {getIntent().getLongExtra("beginWeek", 0)+"",
                             getIntent().getLongExtra("endWeek", 0)+""};
    }


    void populateExpandableListView(Cursor cursor){

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<ExpandableListAdapter.ExpandableListItem>> listDataChild = new HashMap<>();
        List<ExpandableListAdapter.ExpandableListItem> currentGroup = new ArrayList<>();

        extractFromCursorHeadersAndChildren(cursor, listDataHeader, listDataChild, currentGroup);
        setExpandableListViewAdapter(listDataHeader, listDataChild);

    }


    private void setExpandableListViewAdapter(List<String> listDataHeader,
                                              HashMap<String, List<ExpandableListAdapter.ExpandableListItem>> listDataChild) {

        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.lvExp);
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        setOnChildClickListener(expListView, listDataChild);
    }


    private void extractFromCursorHeadersAndChildren(Cursor cursor, List<String> listDataHeader,
                                                   HashMap<String, List<ExpandableListAdapter.ExpandableListItem>> listDataChild,
                                                   List<ExpandableListAdapter.ExpandableListItem> currentGroup){

        long currentDay = 0;
        Task currentTask = null;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            currentTask = getCurrentTask(cursor);

            if (currentDay != 0 && !sameDayOfWeek(currentDay, currentTask.getDate())) {
                addGroupToExpandableList(currentGroup, currentDay, listDataChild, listDataHeader);
                currentGroup = new ArrayList<>();
            }

            currentGroup.add(new ExpandableListAdapter.ExpandableListItem(currentTask.getTitle(), currentTask.getID()));
            currentDay = currentTask.getDate();
            cursor.moveToNext();
        }

        addGroupToExpandableList(currentGroup, currentDay, listDataChild, listDataHeader);

    }


    private void addGroupToExpandableList(List<ExpandableListAdapter.ExpandableListItem> currentGroup,
                                          long currentDay,
                                          HashMap<String, List<ExpandableListAdapter.ExpandableListItem>> listDataChild,
                                          List<String> listDataHeader) {

        if(currentDay != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentDay);

            String groupName = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH)
                                + "  --  " + calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

            listDataHeader.add(groupName);
            listDataChild.put(groupName, currentGroup);
        }

    }


    private boolean sameDayOfWeek(long date1, long date2){

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(date1);
        calendar2.setTimeInMillis(date2);

        return calendar1.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK);
    }


    private Task getCurrentTask(Cursor cursor) {

        Task current_task = new Task();

        current_task.setID(cursor.getInt(cursor.getColumnIndex(DatabaseContract.Task._ID)));
        current_task.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.Task.COLUMN_NAME_TITLE)));
        current_task.setCategory(new Category(cursor.getString(cursor.getColumnIndex(DatabaseContract.Task.COLUMN_NAME_CATEGORY))));
        current_task.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseContract.Task.COLUMN_NAME_DESCRIPTION)));
        current_task.setDate(cursor.getLong(cursor.getColumnIndex(DatabaseContract.Task.COLUMN_NAME_DATE)));

        return current_task;
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == LOADER_ID){

            String[] selectionArgs = getWeekRangeFromExtraIntent();

            return new CursorLoader(this, DatabaseContract.Task.CONTENT_URI_FOR_ROWS,
                DatabaseContract.Task.DEFAULT_PROJECTION, SELECTION, selectionArgs, ORDER_BY);

        }else{

            throw new IllegalArgumentException("Loader ID incorrect");
        }

    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        populateExpandableListView(data);
        calculateCategoryPercent(data);

    }


    private void calculateCategoryPercent(Cursor data) {
        categorySumHashMap = new HashMap<>();

        data.moveToFirst();
        while(!data.isAfterLast()) {
            String itemKey = getCurrentTask(data).getCategory().toString();
            Integer itemValue = categorySumHashMap.get(itemKey);

            itemValue = itemValue != null ? itemValue+1:1;

            categorySumHashMap.put(itemKey, itemValue);

            data.moveToNext();
        }
    }


    private void setOnChildClickListener(ExpandableListView expListView,
                                         final HashMap<String, List<ExpandableListAdapter.ExpandableListItem>> listDataChild) {


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent updateTaskIntent = new Intent(getBaseContext(), UpdateTaskActivity.class);
                ExpandableListAdapter.ExpandableListItem currentItem =
                        listDataChild.get(listDataChild.keySet().toArray()[groupPosition]).get(childPosition);

                Task selectedTask = TaskDBAccessor.selectByID(getContentResolver(), currentItem.getID());

                updateTaskIntent.putExtra("title", selectedTask.getTitle());
                updateTaskIntent.putExtra("description", selectedTask.getDescription());
                updateTaskIntent.putExtra("category", selectedTask.getCategory().toString());
                updateTaskIntent.putExtra("_ID", selectedTask.getID());

                Log.i("TaskListActivity", selectedTask.getID() + "");
                startActivity(updateTaskIntent);

                return true;
            }
        });
    }


    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public void onStatisticButtonClick(View view){
        if(categorySumHashMap != null){
            Intent pieChartActivityIntent = new Intent(this, PieChartActivity.class);

            addExtrasFromCategorySumHashMap(pieChartActivityIntent);
            
            startActivity(pieChartActivityIntent);
        }else{
            Toast.makeText(this, getString(R.string.task_list_error_load), Toast.LENGTH_LONG);
        }
    }


    /*
    * Como os extras sao dinamicos é preciso que a classe que receba o Intent possa acessa-los se-
    * quencialmente. Isso é feito associando a chave String de cada categoria a um extra.
    * Ex: extra0 = categoriaQualquer
    * */
    private void addExtrasFromCategorySumHashMap(Intent pieChartActivityIntent) {

        int categoryCount = 0;
        Set<String> keySet = categorySumHashMap.keySet();
        String currentCategory = null;
        Iterator<String> iterator = keySet.iterator();

        while(iterator.hasNext()) {
            currentCategory = iterator.next();

            pieChartActivityIntent.putExtra("category" + categoryCount, currentCategory);
            pieChartActivityIntent.putExtra(currentCategory, categorySumHashMap.get(currentCategory));

            ++categoryCount;
        }
        
    }

}

